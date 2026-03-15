package AssignmentsS2.Assignment1.src.persistence;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import AssignmentsS2.Assignment1.src.exceptions.InvalidTransportDataException;
import AssignmentsS2.Assignment1.src.service.SmartTravelService;
import AssignmentsS2.Assignment1.src.service.SmartTravelService.ArrayType;
import AssignmentsS2.Assignment1.src.travel.Bus;
import AssignmentsS2.Assignment1.src.travel.Flight;
import AssignmentsS2.Assignment1.src.travel.Train;
import AssignmentsS2.Assignment1.src.travel.Transportation;



public class TransportFileManager {
    public static void saveTransportations(Transportation[] transportations, int transCount, String filePath) throws IOException {
        if (transportations == null) throw new IllegalArgumentException("Transportations array is null.");
        if (transCount < 0 || transCount > transportations.length)
            throw new IllegalArgumentException("Transportation count is out of range.");

        PrintWriter outputStream = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));

        for (int i = 0; i < transCount; i++) {
            if (transportations[i] == null) {
                ErrorLogger.log("transportations.csv", "Specific transportation data is empty.", i, "null");
                continue;
            }

            outputStream.println(SmartTravelService.printArray(ArrayType.TRANSPORTATIONS));
        }

        if (outputStream != null)
            outputStream.close();
    }

    public static int loadTransportations(Transportation[] transportations, String filePath) throws IOException {
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
                    ErrorLogger.log("transportations.csv","Transportation data is empty.", lineNo, line);
                    continue;
                }
                try {
                    String[] parts = line.split(";", -1);
                    if (parts.length != 5) {
                        ErrorLogger.log("transportations.csv","Transportation data is incomplete, missing info.", lineNo, line);
                        continue;
                    }
    
                    for(String str : parts) {
                        if (str.equalsIgnoreCase("") || str == null || str.isEmpty()) {
                            ErrorLogger.log("transportations.csv", "Transportation data is incomplete, missing info.", lineNo, line);
                            continue;
                        }
                    }
    
                    String id = parts[0].trim();
                    String companyName = parts[1].trim();
                    String departureCity = parts[2].trim();
                    String arrivalCity = parts[3].trim();
                    // Double cost = Double.parseDouble(parts[4].trim());

                    Object tempObject = parts[lineNo].getClass();
                    if (tempObject instanceof Train) {
                        String seatClass = parts[5].trim();
                        Transportation t = new Train(id, companyName, departureCity, arrivalCity, seatClass);
                        transportations[count++] = t;
                    }
                    else if (tempObject instanceof Bus) {
                        int numOfStops = Integer.parseInt(parts[5].trim());
                        Transportation t = new Bus(id, companyName, departureCity, arrivalCity, numOfStops);
                        transportations[count++] = t;
                    }
                    else if (tempObject instanceof Flight) {
                        double luggageAllowance = Double.parseDouble(parts[5].trim());
                        Transportation t = new Flight(id, companyName, departureCity, arrivalCity, luggageAllowance);
                        transportations[count++] = t;
                    }

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
            SmartTravelService.syncNextID(Transportation.class, maxIdNumSeen + 1);
        }
        return count;
    }

    private static int extractTransportationNumber(String id) {
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
