package AssignmentsS2.Assignment1.src.service;

import AssignmentsS2.Assignment1.src.client.Client;
import AssignmentsS2.Assignment1.src.exceptions.InvalidClientDataException;
import AssignmentsS2.Assignment1.src.travel.*;

public class SmartTravelService {
    private static Client clients[];
    private static Trip trips[];
    private static Accomodation accomodations[];
    private static Transportation transportations[];

    public static void addClient(String firstName, String lastName, String email) throws InvalidClientDataException {
        int index = clients.length;
        clients[index + 1] = new Client(firstName, lastName, email);
    }

    public static void createTrip() {

    }

    public static boolean clientExists(String clientID) {
        if (clientID == null || clientID.trim().isEmpty()) {
            throw new IllegalArgumentException("Client ID cannot be empty.");
        }

        String trimmed = clientID.trim();

        if (!trimmed.matches("C\\d+")) {
            throw new IllegalArgumentException("Invalid Client ID format: " + clientID);
        }

        for (Client specifiClient : clients)
            if (trimmed == specifiClient.getClientID())
                return true;

        return false;
    }

    public static void findClientbyId(String clientID) {

    }

    public static void loadAllData(String folderPath) {

    }

    public static void saveAllData(String folderPath) {

    }

    public static void calculateTripTotal(int index) {

    }

}
