package AssignmentsS2.Assignment3.src.persistence;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import AssignmentsS2.Assignment3.src.client.Client;
import AssignmentsS2.Assignment3.src.interfaces.CsvPersistable;
import AssignmentsS2.Assignment3.src.interfaces.Identifiable;
import AssignmentsS2.Assignment3.src.travel.Accommodation;
import AssignmentsS2.Assignment3.src.travel.Transportation;
import AssignmentsS2.Assignment3.src.travel.Trip;

public final class GenericFileManager {

    private GenericFileManager() {
    }

    public static <T extends CsvPersistable> List<T> load(String filepath, Class<T> clazz) {
        if (filepath == null || filepath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be empty.");
        }
        if (clazz == null) {
            throw new IllegalArgumentException("Class type cannot be null.");
        }

        List<T> items = new ArrayList<>();
        String sourceFile = getSourceName(filepath);
        Path path = Paths.get(filepath);

        if (!Files.exists(path)) {
            ErrorLogger.log(sourceFile, "File does not exist.", 0, filepath);
            return items;
        }

        Method factoryMethod;
        try {
            factoryMethod = clazz.getMethod("fromCsvRow", String.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    clazz.getSimpleName() + " must define public static fromCsvRow(String).", e);
        }

        int lineNo = 0;
        int maxNumericId = -1;

        try (Scanner inputStream = new Scanner(new FileInputStream(filepath))) {
            while (inputStream.hasNextLine()) {
                lineNo++;
                String line = inputStream.nextLine().trim();

                if (line.isEmpty()) {
                    ErrorLogger.log(sourceFile, "Data row is empty.", lineNo, line);
                    continue;
                }

                if (line.startsWith("INVALID_PREDEFINED_")) {
                    continue;
                }

                try {
                    @SuppressWarnings("unchecked")
                    T item = (T) factoryMethod.invoke(null, line);

                    if (item == null) {
                        ErrorLogger.log(sourceFile, "Parsed item is null.", lineNo, line);
                        continue;
                    }

                    items.add(item);

                    if (item instanceof Identifiable identifiable) {
                        int numericId = extractNumericId(identifiable.getId());
                        if (numericId > maxNumericId) {
                            maxNumericId = numericId;
                        }
                    }

                } catch (InvocationTargetException e) {
                    Throwable cause = (e.getCause() == null) ? e : e.getCause();
                    String message = (cause.getMessage() == null || cause.getMessage().trim().isEmpty())
                            ? "Unable to parse CSV row."
                            : cause.getMessage();
                    ErrorLogger.log(sourceFile, message, lineNo, line);

                } catch (IllegalAccessException | ClassCastException e) {
                    String message = (e.getMessage() == null || e.getMessage().trim().isEmpty())
                            ? "Unable to build object from CSV row."
                            : e.getMessage();
                    ErrorLogger.log(sourceFile, message, lineNo, line);
                }
            }
        } catch (IOException e) {
            ErrorLogger.log(sourceFile, e.getMessage(), lineNo, filepath);
        }

        if (maxNumericId >= 0) {
            syncNextIdIfPossible(clazz, maxNumericId + 1);
        }

        return items;
    }

    public static <T extends CsvPersistable> void save(List<T> items, String filepath) {
        if (filepath == null || filepath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be empty.");
        }

        String sourceFile = getSourceName(filepath);
        Path outputPath = Paths.get(filepath).toAbsolutePath().normalize();

        try {
            Path parent = outputPath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
        } catch (IOException e) {
            ErrorLogger.log(sourceFile, e.getMessage(), 0, filepath);
            return;
        }

        try (PrintWriter outputStream =
                     new PrintWriter(new BufferedWriter(new FileWriter(outputPath.toString())))) {

            if (items == null) {
                return;
            }

            int lineNo = 0;
            for (T item : items) {
                lineNo++;

                if (item == null) {
                    ErrorLogger.log(sourceFile, "Specific item data is empty.", lineNo, "null");
                    continue;
                }

                outputStream.println(item.toCsvRow());
            }

        } catch (IOException e) {
            ErrorLogger.log(sourceFile, e.getMessage(), 0, filepath);
        }
    }

    private static String getSourceName(String filepath) {
        Path path = Paths.get(filepath);
        Path fileName = path.getFileName();
        return (fileName == null) ? filepath : fileName.toString();
    }

    private static void syncNextIdIfPossible(Class<?> clazz, int nextNumericId) {
        Class<?> syncClass = resolveSyncClass(clazz);
        if (syncClass == null) {
            return;
        }

        try {
            Method syncMethod = syncClass.getMethod("syncNextId", int.class);
            syncMethod.invoke(null, nextNumericId);
        } catch (NoSuchMethodException e) {
            // Ignore silently: not every possible type must support ID syncing.
        } catch (IllegalAccessException | InvocationTargetException e) {
            ErrorLogger.log(
                    syncClass.getSimpleName(),
                    "Unable to sync next ID: " + e.getMessage(),
                    0,
                    String.valueOf(nextNumericId)
            );
        }
    }

    private static Class<?> resolveSyncClass(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        if (Client.class.equals(clazz) || Trip.class.equals(clazz)) {
            return clazz;
        }

        if (Transportation.class.isAssignableFrom(clazz)) {
            return Transportation.class;
        }

        if (Accommodation.class.isAssignableFrom(clazz)) {
            return Accommodation.class;
        }

        return null;
    }

    private static int extractNumericId(String id) {
        if (id == null) {
            return -1;
        }

        int start = 0;
        while (start < id.length() && !Character.isDigit(id.charAt(start))) {
            start++;
        }

        if (start >= id.length()) {
            return -1;
        }

        try {
            return Integer.parseInt(id.substring(start));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}