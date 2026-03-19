package AssignmentsS2.Assignment2.src.persistance;

import java.io.*;
import AssignmentsS2.Assignment2.src.client.Client;
import AssignmentsS2.Assignment2.src.exceptions.InvalidClientDataException;
import java.util.Scanner;

public class ClientFileManager {
    public static void saveClients(Client[] clients, int clientCount, String filePath) throws IOException {
        if (clients == null) throw new IllegalArgumentException("Clients array is null.");
        if (clientCount < 0 || clientCount > clients.length)
            throw new IllegalArgumentException("Client count is out of range.");

        PrintWriter outputStream = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));

        for (int i = 0; i < clientCount; i++) {
            if (clients[i] == null) {
                ErrorLogger.log("clients.csv", "Specific client data is empty.", i, "null");
                continue;
            }

            outputStream.println(clients[i].getClientID() + ";" + clients[i].getFirstName() + ";" + 
                clients[i].getLastName() + ";" + clients[i].getEmailAdress());
        }

        if (outputStream != null)
            outputStream.close();
    }

    public static int loadClients(Client[] clients, String filePath) throws IOException {
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
                    ErrorLogger.log("clients.csv","Client data is empty.", lineNo, line);
                    continue;
                }
                try {
                    String[] parts = line.split(";", -1);
                    if (parts.length != 4) {
                        ErrorLogger.log("clients.csv","Client data is incomplete, missing info.", lineNo, line);
                        continue;
                    }
    
                    for(String str : parts) {
                        if (str.equalsIgnoreCase("") || str == null || str.isEmpty()) {
                            ErrorLogger.log("clients.csv", "Client data is incomplete, missing info.", lineNo, line);
                            continue;
                        }
                    }
    
                    if (!parts[3].contains("@")) {
                        ErrorLogger.log("clients.csv", "Email adress is incorrect.", lineNo, line);
                        continue;
                    }
    
                    String id = parts[0].trim();
                    String firstName = parts[1].trim();
                    String lastName = parts[2].trim();
                    String email = parts[3].trim();

                    // Build Client WITH ID FROM FILE (you must support this in Client)
                    Client c = new Client(id, firstName, lastName, email);
                    clients[count++] = c;

                    // Track max numeric part of ID for static resync
                    int n = extractClientNumber(id);
                    if (n > maxIdNumSeen) maxIdNumSeen = n;

                } catch(InvalidClientDataException | RuntimeException e) {
                    ErrorLogger.log("clients.csv", e.getMessage(), lineNo, line);
                }
            } 
        } catch (Exception e) {
            ErrorLogger.log("clients.csv", e.getMessage(), lineNo, line);
        }

        // Resync Client static next-id so new clients don’t collide with loaded IDs
        if (maxIdNumSeen >= 0) {
            Client.syncNextId(maxIdNumSeen + 1);
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
