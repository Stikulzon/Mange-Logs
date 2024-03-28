package zefir.mangelogs;

import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class LogWriter {
    private static final String LOG_FOLDER = "MangeLogs";
    private static final String ARCHIVE_FOLDER = "MangeLogs/Archive";
    public static void logToFile(String eventName, String eventInfo) {
        try {
            String currentDateString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String logFileName = currentDateString + "_" + eventName + ".txt";

            File logFolder = new File(LOG_FOLDER);
            if (!logFolder.exists()) {
                logFolder.mkdir();
            }

            File archiveFolder = new File(ARCHIVE_FOLDER);
            if (!archiveFolder.exists()) {
                archiveFolder.mkdir();
            }

            for (File file : logFolder.listFiles()) {
                if (file.isFile() && !file.getName().equals(logFileName)) {
                    String fileDate = file.getName().substring(0, 10);
                    if (!fileDate.equals(currentDateString)) {
                        archiveLog(file, fileDate);
                    }
                }
            }

            File logFile = new File(logFolder, logFileName);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }

            FileWriter writer = new FileWriter(logFile, true);
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").format(new Date());
            writer.write("|Time: " + timestamp + "|" + eventInfo + "|\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to log file:");
            e.printStackTrace();
        }
    }

    private static void archiveLog(File logFile, String fileDate) throws IOException {
        String archiveFileName = fileDate + ".zip";
        File archiveFile = new File(ARCHIVE_FOLDER, archiveFileName);

        File tempZipFile = File.createTempFile("temp_archive", ".zip");

        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(tempZipFile))) {
            if (archiveFile.exists()) {
                try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(archiveFile))) {
                    ZipEntry entry;
                    while ((entry = zipIn.getNextEntry()) != null) {
                        zipOut.putNextEntry(entry);
                        zipOut.write(zipIn.readAllBytes());
                        zipOut.closeEntry();
                    }
                }
            }

            ZipEntry entry = new ZipEntry(logFile.getName());
            zipOut.putNextEntry(entry);
            zipOut.write(Files.readAllBytes(logFile.toPath()));
            zipOut.closeEntry();
        }

        if (archiveFile.exists()) {
            archiveFile.delete();
        }
        tempZipFile.renameTo(archiveFile);

        logFile.delete();
    }
}
