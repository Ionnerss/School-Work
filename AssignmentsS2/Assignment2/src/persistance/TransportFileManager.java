package AssignmentsS2.Assignment2.src.persistance;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import AssignmentsS2.Assignment2.src.exceptions.InvalidTransportDataException;
import AssignmentsS2.Assignment2.src.travel.Bus;
import AssignmentsS2.Assignment2.src.travel.Flight;
import AssignmentsS2.Assignment2.src.travel.Train;
import AssignmentsS2.Assignment2.src.travel.Transportation;



public class TransportFileManager {
    public static void saveTransportations(Transportation[] transportations, int transCount, String filePath) throws IOException {
        if (transportations == null) throw new IllegalArgumentException("Transportations array is null.");
        if (transCount < 0 || transCount > transportations.length)
            throw new IllegalArgumentException("Transportation count is out of range.");

        try (PrintWriter outputStream = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))) {
            for (int i = 0; i < transCount; i++) {
                if (transportations[i] == null) {
                    ErrorLogger.log("transportations.csv", "Specific transportation data is empty.", i + 1, "null");
                    continue;
                }

                if (transportations[i] instanceof Flight) {
                    outputStream.println("FLIGHT;" + transportations[i]);
                }
                else if (transportations[i] instanceof Train) {
                    outputStream.println("TRAIN;" + transportations[i]);
                }
                else if (transportations[i] instanceof Bus) {
                    outputStream.println("BUS;" + transportations[i]);
                }
                else {
                    ErrorLogger.log("transportations.csv", "Unknown transportation subtype.", i + 1, transportations[i].toString());
                }
            }
        }
    }

    public static int loadTransportations(Transportation[] transportations, String filePath) throws IOException {
        int count = 0;
        int lineNo = 0;
        String line = "null";
        int maxIdNumSeen = -1; // to resync static ID generator after load

        try (Scanner inputStream = new Scanner(new FileInputStream(filePath))) {

            while (inputStream.hasNextLine()) {
                lineNo++;
                line = inputStream.nextLine();
                line = line.trim();

                if (line.isEmpty()) {
                    ErrorLogger.log("transportations.csv","Transportation data is empty.", lineNo, line);
                    continue;
                }
                try {
                    String[] parts = line.split(";", -1);
                    if (parts.length < 6 || parts.length > 7) {
                        ErrorLogger.log("transportations.csv","Transportation data is incomplete, missing info.", lineNo, line);
                        continue;
                    }

                    for (String str : parts) {
                        if (str == null || str.trim().isEmpty()) {
                            ErrorLogger.log("transportations.csv", "Transportation data is incomplete, missing info.", lineNo, line);
                            continue;
                        }
                    }

                    String type;
                    int base;
                    if (parts.length == 7) {
                        type = parts[0].trim().toUpperCase();
                        base = 1;
                    }
                    else {
                        // Legacy format without type token. Infer based on last field content.
                        type = inferTypeFromTail(parts[5].trim());
                        base = 0;
                    }

                    String id = parts[base].trim();
                    String companyName = parts[base + 1].trim();
                    String departureCity = parts[base + 2].trim();
                    String arrivalCity = parts[base + 3].trim();
                    double baseFare = Double.parseDouble(parts[base + 4].trim());
                    String tail = parts[base + 5].trim();

                    Transportation t;
                    switch (type) {
                        case "TRAIN" -> t = new Train(id, companyName, departureCity, arrivalCity, baseFare, tail);
                        case "BUS" -> t = new Bus(id, companyName, departureCity, arrivalCity, baseFare, Integer.parseInt(tail));
                        case "FLIGHT" -> t = new Flight(id, companyName, departureCity, arrivalCity, baseFare, Double.parseDouble(tail));
                        default -> {
                            ErrorLogger.log("transportations.csv", "Unsupported transportation type: " + type, lineNo, line);
                            continue;
                        }
                    }

                    if (count >= transportations.length) {
                        ErrorLogger.log("transportations.csv", "Transportation array capacity exceeded.", lineNo, line);
                        break;
                    }

                    transportations[count++] = t;

                    // Track max numeric part of ID for static resync
                    int n = extractTransportationNumber(id);
                    if (n > maxIdNumSeen) maxIdNumSeen = n;

                } catch(InvalidTransportDataException | RuntimeException e) {
                    ErrorLogger.log("transportations.csv", e.getMessage(), lineNo, line);
                }
            } 
        } catch (Exception e) {
            ErrorLogger.log("transportations.csv", e.getMessage(), lineNo, line);
        }

        // Resync Transportation static next-id so new transportations don’t collide with loaded IDs
        if (maxIdNumSeen >= 0) {
            Transportation.syncNextId(maxIdNumSeen + 1);
        }
        return count;
    }

    private static String inferTypeFromTail(String tail) {
        try {
            Integer.parseInt(tail);
            return "BUS";
        } catch (NumberFormatException ignored) {
            // not bus
        }

        try {
            Double.parseDouble(tail);
            return "FLIGHT";
        } catch (NumberFormatException ignored) {
            // not flight
        }

        return "TRAIN";
    }

    private static int extractTransportationNumber(String id) {
        if (id == null) return -1;
        id = id.trim();
        if (!id.startsWith("TR") || id.length() < 3) return -1;

        try {
            return Integer.parseInt(id.substring(2));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
