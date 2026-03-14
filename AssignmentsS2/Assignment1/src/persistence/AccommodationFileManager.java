package AssignmentsS2.Assignment1.src.persistence;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import AssignmentsS2.Assignment1.src.travel.Accomodation;
import AssignmentsS2.Assignment1.src.travel.Hostel;
import AssignmentsS2.Assignment1.src.travel.Hotel;
import AssignmentsS2.Assignment1.src.exceptions.InvalidAccommodationDataException;
import AssignmentsS2.Assignment1.src.service.SmartTravelService;

public class AccommodationFileManager {
    public static void saveAccomodations(Accomodation[] accomodations, int accCount, String filePath) throws IOException {
        if (accomodations == null) throw new IllegalArgumentException("Accomodations array is null.");
        if (accCount < 0 || accCount > accomodations.length)
            throw new IllegalArgumentException("Accomodation count is out of range.");

        PrintWriter outputStream = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));

        for (int i = 0; i < accCount; i++) {
            if (accomodations[i] == null) {
                ErrorLogger.log("accomodations.csv", "Specific accomodation data is empty.", i, "null");
                continue;
            }

            outputStream.println(accomodations[i].getAccomodationID() + ";" + accomodations[i].getName() + ";" + 
                accomodations[i].getLocation() + ";" + accomodations[i].getPricePerNight());
        }

        if (outputStream != null)
            outputStream.close();
    }

    public static int loadAccomodations(Accomodation[] accomodations, String filePath) throws IOException {
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
                    ErrorLogger.log("accomodations.csv","Accomodation data is empty.", lineNo, line);
                    continue;
                }
                try {
                    String[] parts = line.split(";", -1);
                    if (parts.length != 4) {
                        ErrorLogger.log("accomodations.csv","Accomodation data is incomplete, missing info.", lineNo, line);
                        continue;
                    }
    
                    for(String str : parts) {
                        if (str.equalsIgnoreCase("") || str == null || str.isEmpty()) {
                            ErrorLogger.log("accomodations.csv", "Accomodation data is incomplete, missing info.", lineNo, line);
                            continue;
                        }
                    }
    
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String location = parts[2].trim();
                    double pricePerNight = Double.parseDouble(parts[3].trim());

                    // If you want to distinguish Hostel/Hotel, add a type field to the CSV and check it here.
                    // For now, just create a generic Accomodation object.
                    Accomodation a = new Accomodation(id, name, location, pricePerNight);
                    accomodations[count++] = a;

                    // Track max numeric part of ID for static resync
                    int n = extractClientNumber(id);
                    if (n > maxIdNumSeen) maxIdNumSeen = n;

                } catch(InvalidAccommodationDataException | RuntimeException e) {
                    ErrorLogger.log("clients.csv", e.getMessage(), lineNo, line);
                }
            } 
        } catch (Exception e) {
            ErrorLogger.log("clients.csv", e.getMessage(), lineNo, line);
        }

        // Resync Client static next-id so new clients don’t collide with loaded IDs
        if (maxIdNumSeen >= 0) {
            SmartTravelService.syncNextID(Accomodation.class, maxIdNumSeen + 1);
        }
        return count;
    }

    private static int extractClientNumber(String id) {
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
