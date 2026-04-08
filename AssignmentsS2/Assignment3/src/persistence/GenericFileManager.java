package AssignmentsS2.Assignment3.src.persistence;

/*
 * Assignment 3
 * Question: Generic File Manager
 * Written by: Catalin-Ion Besleaga (40347936)
 *
 * This class generically loads and saves SmartTravel entities using CSV.
 * Invalid rows are logged and preserved so they are not lost on the next save.
 */

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import AssignmentsS2.Assignment3.src.client.Client;
import AssignmentsS2.Assignment3.src.exceptions.*;
import AssignmentsS2.Assignment3.src.interfaces.CsvPersistable;
import AssignmentsS2.Assignment3.src.interfaces.Identifiable;
import AssignmentsS2.Assignment3.src.service.SmartTravelService;
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

        int lineNo = 0;
        int maxNumericId = -1;

        try (Scanner inputStream = new Scanner(new FileInputStream(filepath))) {
            while (inputStream.hasNextLine()) {
                lineNo++;
                String rawLine = inputStream.nextLine();
                String line = rawLine.trim();

                if (line.isEmpty()) {
                    ErrorLogger.log(sourceFile, "Data row is empty.", lineNo, rawLine);
                    SmartTravelService.registerInvalidRow(clazz, rawLine);
                    continue;
                }

                if (line.startsWith("INVALID_PREDEFINED_")) {
                    String preservedRow = stripInvalidPrefix(line);
                    SmartTravelService.registerInvalidRow(clazz, preservedRow);
                    continue;
                }

                try {
                    T item = parseRow(line, clazz);

                    if (item == null) {
                        ErrorLogger.log(sourceFile, "Parsed item is null.", lineNo, line);
                        SmartTravelService.registerInvalidRow(clazz, line);
                        continue;
                    }

                    items.add(item);

                    if (item instanceof Identifiable identifiable) {
                        int numericId = extractNumericId(identifiable.getId());
                        if (numericId > maxNumericId) {
                            maxNumericId = numericId;
                        }
                    }

                } catch (InvalidClientDataException
                        | InvalidTripDataException
                        | InvalidTransportDataException
                        | InvalidAccommodationDataException
                        | EntityNotFoundException
                        | IllegalArgumentException e) {

                    String message = (e.getMessage() == null || e.getMessage().trim().isEmpty())
                            ? "Unable to parse CSV row."
                            : e.getMessage();

                    ErrorLogger.log(sourceFile, message, lineNo, line);
                    SmartTravelService.registerInvalidRow(clazz, line);
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

    private static <T extends CsvPersistable> T parseRow(String csvLine, Class<T> clazz)
            throws InvalidClientDataException, InvalidTripDataException,
                   InvalidTransportDataException, InvalidAccommodationDataException,
                   EntityNotFoundException {

        if (clazz == Client.class) {
            return clazz.cast(Client.fromCsvRow(csvLine));
        }

        if (clazz == Trip.class) {
            return clazz.cast(Trip.fromCsvRow(csvLine));
        }

        if (clazz == Transportation.class) {
            return clazz.cast(Transportation.fromCsvRow(csvLine));
        }

        if (clazz == Accommodation.class) {
            return clazz.cast(Accommodation.fromCsvRow(csvLine));
        }

        throw new IllegalArgumentException("Unsupported class type: " + clazz.getSimpleName());
    }

    private static void syncNextIdIfPossible(Class<?> clazz, int nextNumericId) {
        if (clazz == Client.class) {
            Client.syncNextId(nextNumericId);
        } else if (clazz == Trip.class) {
            Trip.syncNextId(nextNumericId);
        } else if (clazz == Transportation.class) {
            Transportation.syncNextId(nextNumericId);
        } else if (clazz == Accommodation.class) {
            Accommodation.syncNextId(nextNumericId);
        }
    }

    private static String stripInvalidPrefix(String line) {
        int index = line.indexOf(';');
        if (index < 0 || index == line.length() - 1) {
            return "";
        }
        return line.substring(index + 1).trim();
    }

    private static String getSourceName(String filepath) {
        Path path = Paths.get(filepath);
        Path fileName = path.getFileName();
        return (fileName == null) ? filepath : fileName.toString();
    }

    private static int extractNumericId(String id) {
        if (id == null) {
            return -1;
        }

        int index = 0;
        while (index < id.length() && !Character.isDigit(id.charAt(index))) {
            index++;
        }

        if (index >= id.length()) {
            return -1;
        }

        try {
            return Integer.parseInt(id.substring(index));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}