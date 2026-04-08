package AssignmentsS2.Assignment3.src.persistence;

import java.io.*;
import AssignmentsS2.Assignment3.src.client.Client;
import AssignmentsS2.Assignment3.src.exceptions.InvalidClientDataException;
import AssignmentsS2.Assignment3.src.service.SmartTravelService;
import java.util.Scanner;

public class ClientFileManager {
    public static void saveClients(Client[] clients, int clientCount, String filePath) throws IOException {
        if (clients == null) throw new IllegalArgumentException("Clients array is null.");
        if (clientCount < 0 || clientCount > clients.length)
            throw new IllegalArgumentException("Client count is out of range.");

        PrintWriter outputStream = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));

        int outputLine = 0;

        for (int i = 0; i < clientCount; i++) {
            if (clients[i] == null) {
                ErrorLogger.log("clients.csv", "Specific client data is empty.", i + 1, "null");
                continue;
            }

            String csvLine = clients[i].getId() + ";" + clients[i].getFirstName() + ";" +
                clients[i].getLastName() + ";" + clients[i].getEmailAdress();
            outputStream.println(csvLine);
            outputLine++;
        }

        for (int i = 0; i < SmartTravelService.getInvalidClientCount(); i++) {
            String invalidRow = SmartTravelService.getInvalidClientRow(i);
            if (invalidRow == null)
                continue;

            String csvLine = "INVALID_PREDEFINED_CLIENT;" + invalidRow;
            outputStream.println(csvLine);
            outputLine++;
            ErrorLogger.log("clients.csv", "Invalid predefined entry preserved", outputLine, csvLine);
        }

        if (outputStream != null)
            outputStream.close();
    }

    public static int loadClients(Client[] clients, String filePath) throws IOException {
        int count = 0;
        int lineNo = 0;
        String line = "";
        int maxIdNumSeen = -1;

        try (Scanner inputStream = new Scanner(new FileInputStream(filePath))) {
            while (inputStream.hasNextLine()) {
                lineNo++;
                line = inputStream.nextLine().trim();

                if (line.isEmpty()) {
                    ErrorLogger.log("clients.csv", "Client data is empty.", lineNo, line);
                    continue;
                }

                if (line.startsWith("INVALID_PREDEFINED_CLIENT;")) {
                    continue;
                }

                try {
                    String[] parts = line.split(";", -1);
                    if (parts.length != 4) {
                        ErrorLogger.log("clients.csv", "Client data is incomplete, missing info.", lineNo, line);
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
                        ErrorLogger.log("clients.csv", "Client data is incomplete, missing info.", lineNo, line);
                        continue;
                    }

                    String id = parts[0].trim();
                    String firstName = parts[1].trim();
                    String lastName = parts[2].trim();
                    String email = parts[3].trim();

                    if (emailAlreadyLoaded(clients, count, email)) {
                        ErrorLogger.log("clients.csv", "Duplicate email found during load.", lineNo, line);
                        continue;
                    }

                    if (count >= clients.length) {
                        ErrorLogger.log("clients.csv", "Clients array capacity exceeded.", lineNo, line);
                        break;
                    }

                    Client c = new Client(id, firstName, lastName, email);
                    clients[count++] = c;

                    int n = extractClientNumber(id);
                    if (n > maxIdNumSeen) {
                        maxIdNumSeen = n;
                    }

                } catch (InvalidClientDataException | RuntimeException e) {
                    ErrorLogger.log("clients.csv", e.getMessage(), lineNo, line);
                }
            }
        } catch (IOException e) {
            ErrorLogger.log("clients.csv", e.getMessage(), lineNo, line);
            throw e;
        }

        if (maxIdNumSeen >= 0) {
            Client.syncNextId(maxIdNumSeen + 1);
        }

        return count;
    }

    private static boolean emailAlreadyLoaded(Client[] clients, int count, String email) {
        if (email == null) {
            return false;
        }

        for (int i = 0; i < count; i++) {
            if (clients[i] != null && email.equalsIgnoreCase(clients[i].getEmailAdress())) {
                return true;
            }
        }

        return false;
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
