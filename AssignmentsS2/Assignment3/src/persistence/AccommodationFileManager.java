package AssignmentsS2.Assignment3.src.persistence;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import AssignmentsS2.Assignment3.src.travel.Accommodation;
import AssignmentsS2.Assignment3.src.travel.Hostel;
import AssignmentsS2.Assignment3.src.travel.Hotel;
import AssignmentsS2.Assignment3.src.exceptions.InvalidAccommodationDataException;
import AssignmentsS2.Assignment3.src.service.SmartTravelService;

public class AccommodationFileManager {
    public static void saveAccomodations(Accommodation[] accomodations, int accCount, String filePath) throws IOException {
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

    public static int loadAccomodations(Accommodation[] accomodations, String filePath) throws IOException {
    int count = 0;
    int lineNo = 0;
    String line = "";
    int maxIdNumSeen = -1;

    try (Scanner inputStream = new Scanner(new FileInputStream(filePath))) {
        while (inputStream.hasNextLine()) {
            lineNo++;
            line = inputStream.nextLine().trim();

            if (line.isEmpty()) {
                ErrorLogger.log("accomodations.csv", "Accomodation data is empty.", lineNo, line);
                continue;
            }

            if (line.startsWith("INVALID_PREDEFINED_ACCOMODATION;")) {
                continue;
            }

            try {
                String[] parts = line.split(";", -1);
                if (parts.length != 6) {
                    ErrorLogger.log("accomodations.csv", "Accomodation data is incomplete, missing info.", lineNo, line);
                    continue;
                }

                boolean hasEmptyField = false;
                for (String part : parts) {
                    if (part == null || part.trim().isEmpty()) {
                        hasEmptyField = true;
                        break;
                    }
                }

                if (hasEmptyField) {
                    ErrorLogger.log("accomodations.csv", "Accomodation data is incomplete, missing info.", lineNo, line);
                    continue;
                }

                String type = parts[0].trim().toUpperCase();
                String id = parts[1].trim();
                String name = parts[2].trim();
                String location = parts[3].trim();
                double pricePerNight = Double.parseDouble(parts[4].trim());
                int starOrBeds = Integer.parseInt(parts[5].trim());

                Accommodation loadedAccommodation;
                if ("HOTEL".equals(type)) {
                    loadedAccommodation = new Hotel(id, name, location, pricePerNight, starOrBeds);
                } else if ("HOSTEL".equals(type)) {
                    loadedAccommodation = new Hostel(id, name, location, pricePerNight, starOrBeds);
                } else {
                    ErrorLogger.log("accomodations.csv", "Unknown accomodation type.", lineNo, line);
                    continue;
                }

                if (count >= accomodations.length) {
                    ErrorLogger.log("accomodations.csv", "Accomodations array is full.", lineNo, line);
                    continue;
                }

                accomodations[count++] = loadedAccommodation;

                int n = extractAccomodationNumber(id);
                if (n > maxIdNumSeen) {
                    maxIdNumSeen = n;
                }
            } catch (InvalidAccommodationDataException | RuntimeException e) {
                ErrorLogger.log("accomodations.csv", e.getMessage(), lineNo, line);
            }
        }
    } catch (IOException e) {
        ErrorLogger.log("accomodations.csv", e.getMessage(), lineNo, line);
        throw e;
    }

    if (maxIdNumSeen >= 0) {
        Accommodation.syncNextId(maxIdNumSeen + 1);
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
