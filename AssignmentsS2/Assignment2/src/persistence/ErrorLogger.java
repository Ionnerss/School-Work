package AssignmentsS2.Assignment2.src.persistence;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;

public class ErrorLogger {
    private static Path errorsPath = Paths.get("output", "logs", "errors.txt");

    private ErrorLogger() {}

    public static void setBasePath(String basePath) {
        if (basePath == null || basePath.trim().isEmpty()) {
            errorsPath = Paths.get("output", "logs", "errors.txt");
            return;
        }

        errorsPath = Paths.get(basePath).toAbsolutePath().normalize().resolve("output").resolve("logs").resolve("errors.txt");
    }

    public static void log(String sourceFile, String message, int lineNum, String line) {
        PrintWriter writer = null;

        try {
            Path parent = errorsPath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            writer = new PrintWriter(new BufferedWriter(new FileWriter(errorsPath.toString(), true)));
            writer.println("[" + LocalTime.now() + "] " + sourceFile + " line " + lineNum + " | reason: " + message + " | data: " + line);

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (writer != null)
                writer.close();
        }
    }
}
