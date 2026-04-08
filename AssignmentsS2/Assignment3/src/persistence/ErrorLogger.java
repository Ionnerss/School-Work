package AssignmentsS2.Assignment3.src.persistence;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;

public class ErrorLogger {
    private static Path errorsPath = resolveErrorsPath(Paths.get("").toAbsolutePath().normalize());

    private ErrorLogger() {}

    public static void setBasePath(String basePath) {
        Path start = (basePath == null || basePath.trim().isEmpty())
                ? Paths.get("").toAbsolutePath().normalize()
                : Paths.get(basePath).toAbsolutePath().normalize();

        errorsPath = resolveErrorsPath(start);
    }

    private static Path resolveErrorsPath(Path start) {
        Path basePath = resolveProjectBasePath(start);
        return basePath.resolve("output").resolve("logs").resolve("errors.txt");
    }

    private static Path resolveProjectBasePath(Path start) {
        Path current = (start == null)
                ? Paths.get("").toAbsolutePath().normalize()
                : start.toAbsolutePath().normalize();

        while (current != null) {
            if (Files.exists(current.resolve("src")) && Files.exists(current.resolve("libraries"))) {
                return current;
            }

            String folderName = (current.getFileName() == null) ? "" : current.getFileName().toString();
            if ("Assignment3".equalsIgnoreCase(folderName) && Files.exists(current.resolve("src"))) {
                return current;
            }

            Path nested = current.resolve("AssignmentsS2").resolve("Assignment3");
            if (Files.exists(nested.resolve("src")) && Files.exists(nested.resolve("libraries"))) {
                return nested.toAbsolutePath().normalize();
            }

            current = current.getParent();
        }

        return (start == null)
                ? Paths.get("").toAbsolutePath().normalize()
                : start.toAbsolutePath().normalize();
    }

    public static void log(String sourceFile, String message, int lineNum, String line) {
        PrintWriter writer = null;

        try {
            Path parent = errorsPath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            writer = new PrintWriter(new BufferedWriter(new FileWriter(errorsPath.toString(), true)));
            writer.println("[" + LocalTime.now() + "] "
                    + sourceFile
                    + " line " + lineNum
                    + " | reason: " + message
                    + " | data: " + line);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}