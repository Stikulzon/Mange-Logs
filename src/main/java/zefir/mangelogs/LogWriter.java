package zefir.mangelogs;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class LogWriter {
    private static final String LOG_FOLDER = "MangeLogs";
    private static final String ARCHIVE_FOLDER = "MangeLogs/Archive";
    private static final Path LOG_FOLDER_PATH = Paths.get(LOG_FOLDER);
    private static final Path ARCHIVE_FOLDER_PATH = Paths.get(ARCHIVE_FOLDER);

    public static void logToFile(String eventName, String eventInfo) {
        try {
            String currentDateString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String logFileName = currentDateString + "_" + eventName + ".txt";
            Path logFilePath = LOG_FOLDER_PATH.resolve(logFileName);

            Files.createDirectories(LOG_FOLDER_PATH);
            Files.createDirectories(ARCHIVE_FOLDER_PATH);

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(LOG_FOLDER_PATH)) {
                for (Path file : stream) {
                    if (Files.isRegularFile(file) && !file.getFileName().toString().equals(logFileName)) {
                        String fileDate = file.getFileName().toString().substring(0, 10);
                        if (!fileDate.equals(currentDateString)) {
                            archiveLog(file, fileDate);
                        }
                    }
                }
            }

            if (Files.notExists(logFilePath)) {
                Files.createFile(logFilePath);
            }

            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").format(new Date());
            String logEntry = "|Time: " + timestamp + "|" + eventInfo + "|\n";
            Files.write(logFilePath, logEntry.getBytes(), java.nio.file.StandardOpenOption.APPEND);

        } catch (IOException e) {
            System.err.println("Error writing to log file:");
            e.printStackTrace();
        }
    }

    private static void archiveLog(Path logFile, String fileDate) throws IOException {
        String archiveFileName = fileDate + ".zip";
        Path archiveFilePath = ARCHIVE_FOLDER_PATH.resolve(archiveFileName);

        Path tempZipFile = Files.createTempFile("temp_archive", ".zip");

        try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(tempZipFile))) {
            if (Files.exists(archiveFilePath)) {
                try (ZipInputStream zipIn = new ZipInputStream(Files.newInputStream(archiveFilePath))) {
                    ZipEntry entry;
                    while ((entry = zipIn.getNextEntry()) != null) {
                        zipOut.putNextEntry(new ZipEntry(entry.getName()));
                        copyInputStreamToOutputStream(zipIn, zipOut);
                        zipOut.closeEntry();
                    }
                }
            }

            ZipEntry entry = new ZipEntry(logFile.getFileName().toString());
            zipOut.putNextEntry(entry);
            try (InputStream inputStream = Files.newInputStream(logFile)) {
                copyInputStreamToOutputStream(inputStream, zipOut);
            }
            zipOut.closeEntry();
        }

        Files.deleteIfExists(archiveFilePath);
        Files.move(tempZipFile, archiveFilePath);
        Files.delete(logFile);
    }

    private static void copyInputStreamToOutputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
    }
}