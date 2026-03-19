package AssignmentsS2.Assignment2.src.persistance;

import java.io.*;
import java.time.LocalTime;

public class ErrorLogger {
    private static final String errorsPath = "output/logs/errors.txt";

    private ErrorLogger() {}

    public static void log(String sourceFile, String message, int lineNum, String line) {
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(errorsPath, true)));
            writer.println("[" + LocalTime.now() + "] " + sourceFile + "line" + lineNum + " | reason:" + message + " | data: " + line);

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (writer != null)
                writer.close();
        }
    }
}
