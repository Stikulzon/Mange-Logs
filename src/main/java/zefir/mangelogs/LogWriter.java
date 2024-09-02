package zefir.mangelogs;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.*;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class LogWriter {
    private static final String LOG_FOLDER = "MangeLogs";
    private static final String ARCHIVE_FOLDER = "MangeLogs/Archive";
    private static final String FILE_NAME_PATTERN = "%d{yyyy-MM-dd}-%i.log";
    private static final String ARCHIVE_FILE_NAME_PATTERN = ARCHIVE_FOLDER + "/%d{yyyy-MM-dd}-%i.zip";
    private static final String LOG_PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";

    private static Logger configureLogger(String eventName) {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration config = context.getConfiguration();

        // Check and archive old log files
        archiveOldLogFiles();

        PatternLayout layout = PatternLayout.newBuilder()
                .withPattern(LOG_PATTERN)
                .withCharset(StandardCharsets.UTF_8)
                .build();

        String fileName = LOG_FOLDER + "/" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + "_" + eventName + ".log";

        RollingFileAppender appender = RollingFileAppender.newBuilder()
                .setName(eventName + "FileAppender")
                .withFileName(fileName)
                .withFilePattern(ARCHIVE_FILE_NAME_PATTERN)
                .setLayout(layout)
                .withPolicy(CompositeTriggeringPolicy.createPolicy(
                        TimeBasedTriggeringPolicy.newBuilder().withInterval(1).withModulate(true).build(),
                        SizeBasedTriggeringPolicy.createPolicy("10MB")
                ))
                .withStrategy(DefaultRolloverStrategy.newBuilder()
                        .withMax("30")
                        .withFileIndex("nomax")
                        .withCompressionLevelStr("9")
                        .build())
                .build();

        appender.start();
        config.addAppender(appender);

        AppenderRef ref = AppenderRef.createAppenderRef(eventName + "FileAppender", null, null);
        AppenderRef[] refs = new AppenderRef[] {ref};

        LoggerConfig loggerConfig = LoggerConfig.createLogger(false, Level.INFO, eventName,
                "true", refs, null, config, null);
        loggerConfig.addAppender(appender, null, null);

        config.addLogger(eventName, loggerConfig);
        context.updateLoggers();

        return context.getLogger(eventName);
    }

    private static void archiveOldLogFiles() {
        Path logFolderPath = Paths.get(LOG_FOLDER);
        Path archiveFolderPath = Paths.get(ARCHIVE_FOLDER);
        LocalDate today = LocalDate.now();

        try {
            Files.createDirectories(logFolderPath);
            Files.createDirectories(archiveFolderPath);

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(logFolderPath, "*.log")) {
                for (Path file : stream) {
                    String fileName = file.getFileName().toString();
                    LocalDate fileDate = LocalDate.parse(fileName.substring(0, 10), DateTimeFormatter.ISO_LOCAL_DATE);

                    if (fileDate.isBefore(today)) {
                        archiveLogFile(file, fileDate);
                    }
                }
            }
        } catch (IOException e) {
            LogManager.getRootLogger().error("Error archiving old log files", e);
        }
    }

    private static void archiveLogFile(Path logFile, LocalDate fileDate) throws IOException {
        String archiveFileName = fileDate.format(DateTimeFormatter.ISO_LOCAL_DATE) + ".zip";
        Path archiveFilePath = Paths.get(ARCHIVE_FOLDER, archiveFileName);

        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(archiveFilePath.toFile()))) {
            zipOut.putNextEntry(new ZipEntry(logFile.getFileName().toString()));
            Files.copy(logFile, zipOut);
            zipOut.closeEntry();
        }

        Files.delete(logFile);
    }

    public static void logToFile(String eventName, String eventInfo) {
        Logger logger = configureLogger(eventName);
        logger.info(eventInfo);
    }
}