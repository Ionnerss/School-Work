package AssignmentsS2.Assignment2.src.persistance;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import AssignmentsS2.Assignment2.src.travel.Accomodation;
import AssignmentsS2.Assignment2.src.travel.Hostel;
import AssignmentsS2.Assignment2.src.travel.Hotel;
import AssignmentsS2.Assignment2.src.exceptions.InvalidAccommodationDataException;
import AssignmentsS2.Assignment2.src.service.SmartTravelService;

public class AccommodationFileManager {
    public static void saveAccomodations(Accomodation[] accomodations, int accCount, String filePath) throws IOException {
        if (accomodations == null) throw new IllegalArgumentException("Accomodations array is null.");
        if (accCount < 0 || accCount > accomodations.length)
            throw new IllegalArgumentException("Accomodation count is out of range.");

        try (PrintWriter outputStream = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))) {
            int outputLine = 0;

            for (int i = 0; i < accCount; i++) {
                if (accomodations[i] == null) {
                    ErrorLogger.log("accomodations.csv", "Specific accomodation data is empty.", i + 1, "null");
                    continue;
                }

                String type = accomodations[i] instanceof Hotel ? "HOTEL"
                        : accomodations[i] instanceof Hostel ? "HOSTEL" : "ACCOMMODATION";
                String csvLine = type + ";" + accomodations[i];
                outputStream.println(csvLine);
                outputLine++;
            }

            for (int i = 0; i < SmartTravelService.getInvalidAccomodationCount(); i++) {
                String invalidRow = SmartTravelService.getInvalidAccomodationRow(i);
                if (invalidRow == null)
                    continue;

                String csvLine = "INVALID_PREDEFINED_ACCOMODATION;" + invalidRow;
                outputStream.println(csvLine);
                outputLine++;
                ErrorLogger.log("accomodations.csv", "Invalid predefined entry preserved", outputLine, csvLine);
            }
        }
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

                if (line.startsWith("INVALID_PREDEFINED_ACCOMODATION;")) {
                    continue;
                }
                try {
                    String[] parts = line.split(";", -1);
                    if (parts.length != 5) {
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
                    int starOrBeds = Integer.parseInt(parts[4].trim());

                    Object tempObject = parts[lineNo].getClass();
                    if (tempObject instanceof Hotel) {
                        Accomodation a = new Hotel(id, name, location, pricePerNight, starOrBeds);
                        accomodations[count++] = a;
                    }
                    else if (tempObject instanceof Hostel) {
                        Accomodation a = new Hostel(id, name, location, pricePerNight, starOrBeds);
                        accomodations[count++] = a;
                    }

                    // Track max numeric part of ID for static resync
                    int n = extractAccomodationNumber(id);
                    if (n > maxIdNumSeen) maxIdNumSeen = n;

                } catch(InvalidAccommodationDataException | RuntimeException e) {
                    ErrorLogger.log("accomodations.csv", e.getMessage(), lineNo, line);
                }
            } 
        } catch (Exception e) {
            ErrorLogger.log("accomodations.csv", e.getMessage(), lineNo, line);
        }

        // Resync Accomodation static next-id so new accomodations don’t collide with loaded IDs
        if (maxIdNumSeen >= 0) {
            Accomodation.syncNextId(maxIdNumSeen + 1);
        }
        return count;
    }

    private static int extractAccomodationNumber(String id) {
        if (id == null) return -1;
        id = id.trim();
        if (id.length() < 5 || id.charAt(0) != 'A') return -1;

        try {
            return Integer.parseInt(id.substring(1));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
