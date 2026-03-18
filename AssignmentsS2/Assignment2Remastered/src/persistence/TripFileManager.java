package AssignmentsS2.Assignment2Remastered.src.persistence;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import AssignmentsS2.Assignment2Remastered.src.client.Client;
import AssignmentsS2.Assignment2Remastered.src.exceptions.InvalidClientDataException;
import AssignmentsS2.Assignment2Remastered.src.service.SmartTravelService;
import AssignmentsS2.Assignment2Remastered.src.travel.Trip;

public class TripFileManager {
    public static void saveClients(Trip[] trips, int tripCount, String filePath) throws IOException {
        if (trips == null) throw new IllegalArgumentException("Trips array is null.");
        if (tripCount < 0 || tripCount > trips.length)
            throw new IllegalArgumentException("Trip count is out of range.");

        PrintWriter outputStream = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));

        for (int i = 0; i < tripCount; i++) {
            if (trips[i] == null) {
                ErrorLogger.log("trips.csv", "Specific trip data is empty.", i, "null");
                continue;
            }

            outputStream.println(trips[i].getTripId() + ";" + trips[i].getClient().getClientID() + ";" + 
                trips[i].geAccomodation().getAccomodationID() + ";" + trips[i].geTransportation().getTransportID() + ";" +
                trips[i].getDestination() + ";" + trips[i].getDurationInDays() + ";" + trips[i].getBasePrice());
        }

        if (outputStream != null)
            outputStream.close();
    }

    public static int loadClients(Trip[] trips, String filePath) throws IOException {
        Scanner inputStream = null;
        int count = 0;
        int lineNo = 0;
        String line = "null";
        int maxIdNumSeen = -1; // to resync static ID generator after load

        try {
            inputStream = new Scanner(new FileInputStream(filePath));

            while (inputStream.hasNextLine()) {
                lineNo++;
                line = inputStream.nextLine();
                line = line.trim();

                if (line.isEmpty()) {
                    ErrorLogger.log("trips.csv","Trip data is empty.", lineNo, line);
                    continue;
                }
                try {
                    String[] parts = line.split(";", -1);
                    if (parts.length != 4) {
                        ErrorLogger.log("trips.csv","Trip data is incomplete, missing info.", lineNo, line);
                        continue;
                    }
    
                    for(String str : parts) {
                        if (str.equalsIgnoreCase("") || str == null || str.isEmpty()) {
                            ErrorLogger.log("trips.csv", "Trip data is incomplete, missing info.", lineNo, line);
                            continue;
                        }
                    }
    
                    if (!parts[3].contains("@")) {
                        ErrorLogger.log("clients.csv", "Email adress is incorrect.", lineNo, line);
                        continue;
                    } 
    
                    String tripId = parts[0].trim();
                    String clientId = parts[1].trim();
                    String accomodationId = parts[2].trim();
                    String transportId = parts[3].trim();
                    String destination = parts[4].trim();
                    int amountOfDays = Integer.parseInt(parts[5].trim());
                    double price = Double.parseDouble(parts[6].trim());

                    //find client, accomodation, and transport by id then assign them to the new trip object t
            
                    // Trip t = new Trip(tripId, clientId, accomodationId, transportId, destination, amountOfDays, price);
                    // clients[count++] = c;

                    // Track max numeric part of ID for static resync
                    int n = extractTripNumber(tripId);
                    if (n > maxIdNumSeen) maxIdNumSeen = n;

                } catch(InvalidTripDataException | RuntimeException e) {
                    ErrorLogger.log("clients.csv", e.getMessage(), lineNo, line);
                }
            } 
        } catch (Exception e) {
            ErrorLogger.log("clients.csv", e.getMessage(), lineNo, line);
        }

        // Resync Trip static next-id so new trips don’t collide with loaded IDs
        if (maxIdNumSeen >= 0) {
            SmartTravelService.syncNextID(Client.class, maxIdNumSeen + 1);
        }
        return count;
    }

    private static int extractTripNumber(String id) {
        if (id == null) return -1;
        id = id.trim();
        if (id.length() < 5 || id.charAt(0) != 'C') return -1;

        try {
            return Integer.parseInt(id.substring(1));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
