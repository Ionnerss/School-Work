package AssignmentsS2.Assignment3.src.persistence;

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
import AssignmentsS2.Assignment3.src.travel.*;

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
                String line = inputStream.nextLine().trim();

                if (line.isEmpty()) {
                    ErrorLogger.log(sourceFile, "Data row is empty.", lineNo, line);
                    continue;
                }

                if (line.startsWith("INVALID_PREDEFINED_")) {
                    continue;
                }

                try {
                    T item = parseRow(line, clazz);

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

        try (PrintWriter outputStream = new PrintWriter(new BufferedWriter(new FileWriter(outputPath.toString())))) {
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

        String simpleName = clazz.getSimpleName();

        switch (simpleName) {
            case "Client":
                return clazz.cast(Client.fromCsvRow(csvLine));

            case "Trip":
                return clazz.cast(Trip.fromCsvRow(csvLine));

            case "Transportation":
            case "Flight":
            case "Train":
            case "Bus":
                return clazz.cast(parseTransportationRow(csvLine));

            case "Accommodation":
            case "Hotel":
            case "Hostel":
                return clazz.cast(parseAccommodationRow(csvLine));

            default:
                throw new IllegalArgumentException("Unsupported class type: " + simpleName);
        }
    }

    private static Transportation parseTransportationRow(String csvLine)
            throws InvalidTransportDataException {

        if (csvLine == null) {
            throw new InvalidTransportDataException("CSV row cannot be null.");
        }

        String[] parts = csvLine.split(";", -1);

        if (parts.length != 7) {
            throw new InvalidTransportDataException("Transportation CSV row must have exactly 7 fields.");
        }

        String type = parts[0].trim().toUpperCase();
        String id = parts[1].trim();
        String companyName = parts[2].trim();
        String departureCity = parts[3].trim();
        String arrivalCity = parts[4].trim();
        double baseFare = Double.parseDouble(parts[5].trim());
        String lastField = parts[6].trim();

        return switch (type) {
            case "FLIGHT" -> new Flight(id, companyName, departureCity, arrivalCity, baseFare,
                    Double.parseDouble(lastField));
            case "TRAIN" -> new Train(id, companyName, departureCity, arrivalCity, baseFare, lastField);
            case "BUS" -> new Bus(id, companyName, departureCity, arrivalCity, baseFare,
                    Integer.parseInt(lastField));
            default -> throw new InvalidTransportDataException("Unsupported transportation type: " + type);
        };
    }

    private static Accommodation parseAccommodationRow(String csvLine)
            throws InvalidAccommodationDataException {

        if (csvLine == null) {
            throw new InvalidAccommodationDataException("CSV row cannot be null.");
        }

        String[] parts = csvLine.split(";", -1);

        if (parts.length != 6) {
            throw new InvalidAccommodationDataException("Accommodation CSV row must have exactly 6 fields.");
        }

        String type = parts[0].trim().toUpperCase();
        String id = parts[1].trim();
        String name = parts[2].trim();
        String location = parts[3].trim();
        double pricePerNight = Double.parseDouble(parts[4].trim());
        int lastField = Integer.parseInt(parts[5].trim());

        return switch (type) {
            case "HOTEL" -> new Hotel(id, name, location, pricePerNight, lastField);
            case "HOSTEL" -> new Hostel(id, name, location, pricePerNight, lastField);
            default -> throw new InvalidAccommodationDataException("Unsupported accommodation type: " + type);
        };
    }

    private static void syncNextIdIfPossible(Class<?> clazz, int nextNumericId) {
        if (clazz == null) {
            return;
        }

        if (Client.class.equals(clazz)) {
            Client.syncNextId(nextNumericId);
        } else if (Trip.class.equals(clazz)) {
            Trip.syncNextId(nextNumericId);
        } else if (Transportation.class.equals(clazz)
                || Flight.class.equals(clazz)
                || Train.class.equals(clazz)
                || Bus.class.equals(clazz)) {
            Transportation.syncNextId(nextNumericId);
        } else if (Accommodation.class.equals(clazz)
                || Hotel.class.equals(clazz)
                || Hostel.class.equals(clazz)) {
            Accommodation.syncNextId(nextNumericId);
        }
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