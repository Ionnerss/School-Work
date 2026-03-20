package AssignmentsS2.Assignment2.src.service;

import java.io.IOException;

import AssignmentsS2.Assignment2.src.client.Client;
import AssignmentsS2.Assignment2.src.exceptions.*;
import AssignmentsS2.Assignment2.src.persistance.*;
import AssignmentsS2.Assignment2.src.travel.*;

public class SmartTravelService {
    private static Client[] clients;
    private static Trip[] trips;
    private static Accomodation[] accomodations;
    private static Transportation[] transportations;
    public static final int nullIndex = -128;

    // Added missing state used by existing logic
    private static int clientCount = 0;
    private static int tripCount = 0;
    private static int accommodationCount = 0;
    private static int transportationCount = 0;

    // Added missing fields referenced by createTrip()
    private static String clientId;
    private static String accommodationId;
    private static String transportationId;
    private static String destination;
    private static int durationDays;
    private static double basePrice;

    public Client[] getClients() {
        return (clients == null) ? new Client[0] : clients;
    }

    public void setClients(Client[] updatedClients) {
        clients = (updatedClients == null) ? new Client[0] : updatedClients;
        clientCount = countNonNull(clients);
    }

    public Trip[] getTrips() {
        return (trips == null) ? new Trip[0] : trips;
    }

    public void setTrips(Trip[] updatedTrips) {
        trips = (updatedTrips == null) ? new Trip[0] : updatedTrips;
        tripCount = countNonNull(trips);
    }

    public int getTripCount() {
        return tripCount;
    }

    public int getClientCount() {
        return clientCount;
    }

    public int getTransportationCount() {
        return transportationCount;
    }

    public int getAccommodationCount() {
        return accommodationCount;
    }

    public Transportation[] getTransportations() {
        return (transportations == null) ? new Transportation[0] : transportations;
    }

    public void setTransportations(Transportation[] updatedTransportations) {
        transportations = (updatedTransportations == null) ? new Transportation[0] : updatedTransportations;
        transportationCount = countNonNull(transportations);
    }

    public Accomodation[] getAccomodations() {
        return (accomodations == null) ? new Accomodation[0] : accomodations;
    }

    public void setAccomodations(Accomodation[] updatedAccomodations) {
        accomodations = (updatedAccomodations == null) ? new Accomodation[0] : updatedAccomodations;
        accommodationCount = countNonNull(accomodations);
    }

    public static void addClient(String firstName, String lastName, String email) throws InvalidClientDataException {
        Client newClient = new Client(firstName, lastName, email);

        if (clients == null) {
            clients = new Client[1];
        }

        if (clientCount >= clients.length) {
            clients = appendToClientArray(newClient, new Client[clients.length + 1]);

            clientCount++;
            return;
        }

        clients[clientCount++] = newClient;
    }

    public static Trip createTrip() throws InvalidTripDataException, InvalidClientDataException, InvalidAccommodationDataException, 
            InvalidTransportDataException, EntityNotFoundException {

        String normalizedAccommodationId = normalize(accommodationId);
        String normalizedTransportationId = normalize(transportationId);

        if (normalizedAccommodationId == null && normalizedTransportationId == null) {
            return createTrip(destination, durationDays, basePrice, clientId, "", "");
        }

        return createTrip(destination, durationDays, basePrice, clientId,
                normalizedAccommodationId, normalizedTransportationId);
    }

    public static Trip createTrip(String destination, int durationDays, double basePrice, String clientId, String accommodationId, String transportationId)
            throws InvalidTripDataException, InvalidClientDataException,InvalidAccommodationDataException, InvalidTransportDataException, EntityNotFoundException {
                
        String normalizedDestination = normalize(destination);
        String normalizedClientId = normalize(clientId);
        String normalizedAccommodationId = normalize(accommodationId);
        String normalizedTransportationId = normalize(transportationId);

        if (normalizedClientId == null) {
            throw new InvalidTripDataException("Client ID is mandatory.");
        }

        if (normalizedAccommodationId == null && normalizedTransportationId == null) {
            throw new InvalidTripDataException(
                    "At least one of accommodation ID or transportation ID must be provided."
            );
        }

        Client client = findClientByIdObj(normalizedClientId);

        Accomodation accommodation = null;
        if (normalizedAccommodationId != null) {
            accommodation = findAccommodationById(normalizedAccommodationId);
        }

        Transportation transportation = null;
        if (normalizedTransportationId != null) {
            transportation = findTransportationById(normalizedTransportationId);
        }

        Trip newTrip = new Trip(client.getClientID(), accommodation.getAccomodationID(), transportation.getTransportID(), normalizedDestination, durationDays, basePrice);
        storeTrip(newTrip);
        return newTrip;
    }

    public static Accomodation findAccommodationById(String accommodationId)
            throws InvalidAccommodationDataException {

        if (accomodations != null) {
            for (Accomodation accomodation : accomodations) {
                if (accomodation != null && accomodation.getAccomodationID().equals(accommodationId))
                    return accomodation;
            }
        }

        throw new InvalidAccommodationDataException("Accommodation ID not found: " + accommodationId);
    }

    public static Transportation findTransportationById(String transportationId)
            throws InvalidTransportDataException {

        if (transportations != null) {
            for (Transportation transportation : transportations) {
                if (transportation != null && transportation.getTransportID().equals(transportationId))
                    return transportation;
            }
        }

        throw new InvalidTransportDataException("Transportation ID not found: " + transportationId);
    }

    public static Client findClientByIdObj(String clientId) throws InvalidClientDataException {
        if (clients != null) {
            for (Client client : clients) {
                if (client != null && client.getClientID().equals(clientId))
                    return client;
            }
        }

        throw new InvalidClientDataException("Client ID not found: " + clientId);
    }

    public static boolean findClientbyIdBool(String clientId) {
        if (clientId == null || clientId.trim().isEmpty()) {
            throw new IllegalArgumentException("Client ID cannot be empty.");
        }

        String trimmed = clientId.trim();

        if (!trimmed.matches("C\\d+")) {
            throw new IllegalArgumentException("Invalid Client ID format: " + clientId);
        }

        for (Client specifiClient : clients)
            if (trimmed == specifiClient.getClientID())
                return true;

        return false;
    }

    public static boolean clientExists(String clientID) throws InvalidClientDataException {
        if (clientID == null || clientID.trim().isEmpty()) {
            throw new InvalidClientDataException("Client ID cannot be empty.");
        }

        String trimmed = clientID.trim();

        if (!trimmed.matches("C\\d+")) {
            throw new InvalidClientDataException("Invalid Client ID format: " + clientID);
        }

        for (Client specifiClient : clients)
            if (trimmed == specifiClient.getClientID())
                return true;

        return false;
    }


    private static String normalize(String s) {
        if (s == null) return null;
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    private static int countNonNull(Object[] array) {
        if (array == null)
            return 0;

        int count = 0;
        for (Object entry : array) {
            if (entry != null)
                count++;
        }
        return count;
    }

    private static void storeTrip(Trip newTrip) {
        if (trips == null) {
            trips = new Trip[1];
        }

        if (tripCount >= trips.length) {
            trips = appendToTripArray(newTrip, new Trip[trips.length + 1]);
            tripCount++;
            return;
        }

        trips[tripCount++] = newTrip;
    }

    public static void loadAllData(String folderPath) throws IOException {
        ClientFileManager.loadClients(clients, folderPath);
        AccommodationFileManager.loadAccomodations(accomodations, folderPath);
        TransportFileManager.loadTransportations(transportations, folderPath);
        TripFileManager.loadTrips(trips, folderPath);
    }

    public static void saveAllData(String folderPath) throws IOException{
        ClientFileManager.saveClients(clients, clientCount, folderPath);
        AccommodationFileManager.saveAccomodations(accomodations, accommodationCount, folderPath);
        TransportFileManager.saveTransportations(transportations, transportationCount, folderPath);
        TripFileManager.saveTrips(trips, tripCount, folderPath);
    }

    public double calculateTripTotal(int index) throws InvalidAccommodationDataException, InvalidTransportDataException {
        return trips[index].calculateTotalCost();
    }

    public static void testingScenario(boolean choice) throws InvalidClientDataException, InvalidTripDataException,
        InvalidTransportDataException, InvalidAccommodationDataException, EntityNotFoundException {
            
        if (choice) {
            clients = new Client[10];
            trips = new Trip[14];
            transportations = new Transportation[10];
            accomodations = new Accomodation[10];

            // Clients from clients.csv (C1008 row skipped: missing lastName field).
            clients[0] = new Client("C1001", "Sophia", "Rossi", "sophia.rossi@italy.com");
            clients[1] = new Client("C1002", "Carlos", "Silva", "carlos.silva@brazil.com");
            clients[2] = new Client("C1003", "Aiko", "Tanaka", "aiko.tanaka@japan.com");
            clients[3] = new Client("C1004", "Emma", "Wilson", "emma.wilson@usa.com");
            clients[4] = new Client("C1005", "Miguel", "Gomez", "miguel.gomez@spain.com");
            clients[5] = new Client("C1006", "John", "Smith", "john.smith@canada.com");
            clients[6] = new Client("C1007", "Alice", "Johnson", "alice.johnson@uk.com");
            clients[7] = new Client("C1008", "Bob Browm", "bob.brown@australia.com", "");
            clients[8] = new Client("C1009", "Diana", "Prince", "diana.prince@greece.com");
            clients[9] = new Client("C1010", "Lee", "Kim", "lee.kim@korea.com");
            clients[10] = new Client("C1011", "Mig", "Gomez", "miguel.gomez@spain.com");

            // Transportation from transports.csv (CRUISE row skipped: unsupported type).
            transportations[0] = new Flight("TR3001", "Alitalia", "JFK", "FCO", 850.00, 23.0);
            transportations[1] = new Train("TR3002", "Shinkansen", "Tokyo", "Kyoto", 250.00, "HighSpeed");
            transportations[2] = new Bus("TR3003", "Greyhound", "NYC", "Boston", 75.00, 3);
            transportations[3] = new Flight("TR3004", "LATAM", "JFK", "GIG", 950.00, 32.0);
            transportations[4] = new Train("TR3005", "Renfe", "Madrid", "Barcelona", 120.00, "AVE");
            transportations[5] = new Bus("TR3006", "Blablacar", "Rome", "Milan", 45.00, 1);
            transportations[6] = new Flight("TR3007", "ANA", "JFK", "NRT", 1200.00, 32.0);
            transportations[7] = new Train("TR3008", "Amtrak", "NYC", "DC", 89.00, "Acela");
            transportations[8] = new Flight("TR3009", "AirFrance", "YUL", "CDG", 750.00, 25.0);
            transportations[9] = new Bus("TR3010", "VIA", "Toronto", "Montreal", 60.00, 2);

            // Accommodations from accommodations.csv (HOME row skipped: unsupported type).
            accomodations[0] = new Hotel("Hilton Rome", "Rome", 280.00, 4);
            accomodations[1] = new Hostel("Rome Backpackers", "Rome", 55.00, 6);
            accomodations[2] = new Hotel("Grand Rio", "Rio", 320.00, 5);
            accomodations[3] = new Hotel("Kyoto Imperial", "Kyoto", 410.00, 5);
            accomodations[4] = new Hostel("Tokyo Youth", "Tokyo", 60.00, 8);
            accomodations[5] = new Hotel("MGM Grand", "Las Vegas", 290.00, 4);
            accomodations[6] = new Hotel("W New York", "NYC", 450.00, 5);
            accomodations[7] = new Hostel("Barcelona Budget", "Barcelona", 48.00, 6);
            accomodations[8] = new Hostel("Paris Budget", "Paris", 52.00, 8);
            accomodations[9] = new Hotel("Tokyo Hilton", "Tokyo", 380.00, 4);

            trips[0] = new Trip("T2001", "C1001", "A4001", "TR3001", "Paris", 5, 150.00);
            trips[1] = new Trip("T2002", "C1005", "", "TR3002", "Paris", 3, 100.00);
            trips[2] = new Trip("T2003", "C1002", "A4002", "TR3003", "Venice", 7, 200.00);
            trips[3] = new Trip("T2004", "C1009", "A4003", "", "Paris", 4, 175.00);
            trips[4] = new Trip("T2005", "C1006", "", "TR3004", "Rome", 6, 125.00);
            trips[5] = new Trip("T2006", "C1003", "A4004", "TR3005", "Tokyo", 8, 225.00);
            trips[6] = new Trip("T2007", "C1001", "A4005", "", "Paris", 10, 300.00);
            trips[7] = new Trip("T2008", "C1004", "", "TR3006", "London", 5, 90.00);
            trips[8] = new Trip("T2009", "C1007", "A4006", "TR3007", "Rome", 7, 275.00);
            trips[9] = new Trip("T2010", "C1005", "A4003", "T5003", "Madrid", -2, 250.00);
            trips[10] = new Trip("T2007", "C1006", "A4005", "T5005", "Rome", 5, 180.00);
            trips[11] = new Trip("T2008", "C9999", "A4006", "T5006", "NYC", 8, 300.00);
            trips[12] = new Trip("X6008", "C1007", "A4007", "T5007", "LA", 10, 400.00);
            trips[13] = new Trip("T2011", "C1003", "A4104", "TR3005", "Tokyo", 8, 225.00);

            clientCount = clients.length;
            tripCount = trips.length;
            transportationCount = transportations.length;
            accommodationCount = accomodations.length;
        }
        else {
            clients = new Client[1];
            trips = new Trip[1];
            transportations = new Transportation[2];
            accomodations = new Accomodation[1];

            clientCount = 0;
            tripCount = 0;
            transportationCount = 0;
            accommodationCount = 0;
        }
    }

    private static Client[] appendToClientArray(Client element, Client[] expanded) {
        int oldLength = (clients == null) ? 0 : clients.length;

        if (expanded == null || expanded.length != oldLength + 1)
            throw new IllegalArgumentException("Expanded array must have length oldLength + 1.");

        for (int i = 0; i < oldLength; i++)
            expanded[i] = clients[i];

        expanded[oldLength] = element;
        return expanded;
    }

    private static Trip[] appendToTripArray(Trip element, Trip[] expanded) {
        int oldLength = (trips == null) ? 0 : trips.length;

        if (expanded == null || expanded.length != oldLength + 1)
            throw new IllegalArgumentException("Expanded array must have length oldLength + 1.");

        for (int i = 0; i < oldLength; i++)
            expanded[i] = trips[i];

        expanded[oldLength] = element;
        return expanded;
    }

    /**
     * Prints all clients in the system to console.
     * Iterates through client array and displays each client's toString() representation.
     * Each client displays their ID, first name, last name, and email address.
     */
    public static String printClients() {
        String printString = "\n";
        for (Client person : clients)
            printString += ">. " + person.toString();

        return (printString + "\n");
    }

    /**
     * Prints all trips in the system to console.
     * Iterates through trip array and displays each trip's toString() representation.
     * Each trip displays its ID, destination, duration, base price, and associated client.
     */
    public static String printTrips() {
        String printString = "\n";
        for (Trip trip : trips)
            printString += ">. " + trip.toString();

        return (printString + "\n");
    }

    /**
     * Prints all transportation options in the system to console.
     * Displays transportation entries if available, or a message if the list is empty.
     * Demonstrates polymorphism: calls toString() on base-class references (Flight, Train, Bus).
     */
    public static String printTransportations() {
        String printString = "\n";
        if (transportations.length == 0)
            printString += ">. No transportation options available.";
        else {
            // Iterate through array and display each non-null transportation option
            for (Transportation t : transportations) {
                if (t != null) {
                    // Polymorphic call: actual subclass toString() is invoked
                    printString += (">. " + t.toString());
                }
            }
        }
        return (printString + "\n");
    }

    /**
     * Prints all accommodation options in the system to console.
     * Displays accommodation entries if available, or a message if the list is empty.
     * Demonstrates polymorphism: calls toString() on base-class references (Hotel, Hostel).
     */
    public static String printAccomodations() {
        String printString = "\n";
        if (accomodations.length == 0) {
            printString = ">. No accomodations available.";
        } else {
            // Iterate through array and display each non-null accommodation option
            for (Accomodation a : accomodations) {
                if (a != null) {
                    // Polymorphic call: actual subclass toString() is invoked
                    printString = (">. " + a.toString());
                }
            }
        }
        return (printString + "\n");
    }

    public Trip getTrip(int i) {
        if (trips == null || i < 0 || i >= tripCount)
            return null;
        return trips[i];
    }

    public Client getClient(int i) {
        if (clients == null || i < 0 || i >= clientCount)
            return null;
        return clients[i];
    }
}
