package AssignmentsS2.Assignment3.src.persistence;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import AssignmentsS2.Assignment3.src.exceptions.*;
import AssignmentsS2.Assignment3.src.service.SmartTravelService;
import AssignmentsS2.Assignment3.src.travel.Trip;

public class TripFileManager {
    public static void saveTrips(Trip[] trips, int tripCount, String filePath) throws IOException {
        if (trips == null) throw new IllegalArgumentException("Trips array is null.");
        if (tripCount < 0 || tripCount > trips.length)
            throw new IllegalArgumentException("Trip count is out of range.");

        try (PrintWriter outputStream = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))) {
            int outputLine = 0;

            for (int i = 0; i < tripCount; i++) {
                if (trips[i] == null) {
                    ErrorLogger.log("trips.csv", "Specific trip data is empty.", i + 1, "null");
                    continue;
                }

                String accomodationId = (trips[i].geAccomodationId() == null) ? "" : trips[i].geAccomodationId();
                String transportId = (trips[i].geTransportationId() == null) ? "" : trips[i].geTransportationId();

                String csvLine = trips[i].getTripId() + ";" + trips[i].getClientId() + ";"
                        + accomodationId + ";" + transportId + ";"
                        + trips[i].getDestination() + ";" + trips[i].getDurationInDays() + ";" + trips[i].getBasePrice();

                outputStream.println(csvLine);
                outputLine++;
            }

            for (int i = 0; i < SmartTravelService.getInvalidTripCount(); i++) {
                String invalidRow = SmartTravelService.getInvalidTripRow(i);
                if (invalidRow == null)
                    continue;

                String csvLine = "INVALID_PREDEFINED_TRIP;" + invalidRow;
                outputStream.println(csvLine);
                outputLine++;
                ErrorLogger.log("trips.csv", "Invalid predefined entry preserved", outputLine, csvLine);
            }
        }
    }

    public static int loadTrips(Trip[] trips, String filePath) throws IOException {
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
                    ErrorLogger.log("trips.csv","Trip data is empty.", lineNo, line);
                    continue;
                }

                if (line.startsWith("INVALID_PREDEFINED_TRIP;")) {
                    continue;
                }
                try {
                    String[] parts = line.split(";", -1);
                    if (parts.length != 7) {
                        ErrorLogger.log("trips.csv","Trip data is incomplete, missing info.", lineNo, line);
                        continue;
                    }

                    // Required columns: tripId, clientId, destination, duration, basePrice
                    if (parts[0].trim().isEmpty() || parts[1].trim().isEmpty()
                            || parts[4].trim().isEmpty() || parts[5].trim().isEmpty()
                            || parts[6].trim().isEmpty()) {
                        ErrorLogger.log("trips.csv", "Trip required data is incomplete.", lineNo, line);
                        continue;
                    }
    
                    String tripId = parts[0].trim();
                    String clientId = parts[1].trim();
                    String accomodationId = parts[2].trim();
                    String transportId = parts[3].trim();
                    String destination = parts[4].trim();
                    int amountOfDays = Integer.parseInt(parts[5].trim());
                    double price = Double.parseDouble(parts[6].trim());

                    if (count >= trips.length) {
                        ErrorLogger.log("trips.csv", "Trips array capacity exceeded.", lineNo, line);
                        break;
                    }

                    Trip t = new Trip(tripId, clientId, accomodationId, transportId, destination, amountOfDays, price);
                    trips[count++] = t;

                    // Track max numeric part of ID for static resync
                    int n = extractTripNumber(tripId);
                    if (n > maxIdNumSeen) maxIdNumSeen = n;

                } catch (InvalidTripDataException | EntityNotFoundException | RuntimeException e) {
                    ErrorLogger.log("trips.csv", e.getMessage(), lineNo, line);
                }
            } 
        } catch (Exception e) {
            ErrorLogger.log("trips.csv", e.getMessage(), lineNo, line);
        }

        // Resync Trip static next-id so new trips don’t collide with loaded IDs
        if (maxIdNumSeen >= 0) {
            Trip.syncNextId(maxIdNumSeen + 1);
        }
        return count;
    }

    private static int extractTripNumber(String id) {
        if (id == null) return -1;
        id = id.trim();
        if (id.length() < 4 || id.charAt(0) != 'T') return -1;

        try {
            return Integer.parseInt(id.substring(1));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
