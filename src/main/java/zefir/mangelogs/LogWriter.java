package zefir.mangelogs;

import java.io.IOException;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class LogWriter {
    public static void CreateDir() {
        try {
            Path dirPath = Paths.get("MangeLogs");
            if (!Files.exists(dirPath)) {
                Files.createDirectory(dirPath);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void CreateSubDir(String dir) {
        try {
            Path dirPath = Paths.get("MangeLogs/" + dir);
            if (!Files.exists(dirPath)) {
                Files.createDirectory(dirPath);
            }
//            Path filePath = dirPath.resolve(fileName);
//            if (!Files.exists(filePath)) {
//                Files.createFile(filePath);
//            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void WriteToLog (String name, String logText) {
        CreateDir();


        SessionManager sessionMgr = new SessionManager(); // Я не їбу що тут робити

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();

        String LogFileName = "MangeLogs/" + name + "/" + name + "-" + dtf.format(now) + ".txt";
        CreateSubDir(name);

            try (FileOutputStream fos = new FileOutputStream(LogFileName)) {
                byte[] buffer = (logText + "\n").getBytes();

                fos.write(buffer, 0, buffer.length);
                System.out.println("The file has been written");
            } catch (IOException ex) {

                System.out.println(ex.getMessage());
            }


    }
}
