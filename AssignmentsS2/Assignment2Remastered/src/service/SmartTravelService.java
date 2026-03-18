package AssignmentsS2.Assignment2Remastered.src.service;

import AssignmentsS2.Assignment2Remastered.src.client.Client;
import AssignmentsS2.Assignment2Remastered.src.travel.*;
import AssignmentsS2.Assignment2Remastered.src.exceptions.*;

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

    public static void addClient(String firstName, String lastName, String email) throws InvalidClientDataException {
        Client newClient = new Client(firstName, lastName, email);

        if (clients == null) {
            clients = new Client[1];
        }

        if (clientCount >= clients.length) {
            clients = appendToArray(clients, newClient, new Client[clients.length + 1]);
            clientCount++;
            return;
        }

        clients[clientCount++] = newClient;
    }

    public static Trip createTrip() throws InvalidTripDataException, InvalidClientDataException, InvalidAccommodationDataException, 
            InvalidTransportDataException {

        String normalizedAccommodationId = normalize(accommodationId);
        String normalizedTransportationId = normalize(transportationId);

        if (normalizedAccommodationId == null && normalizedTransportationId == null) {
            return createTrip(destination, durationDays, basePrice, clientId);
        }

        return createTrip(destination, durationDays, basePrice, clientId,
                normalizedAccommodationId, normalizedTransportationId);
    }

    public static Trip createTrip(String destination, int durationDays, double basePrice, String clientId)
            throws InvalidTripDataException, InvalidClientDataException {

        String normalizedDestination = normalize(destination);
        String normalizedClientId = normalize(clientId);

        if (normalizedClientId == null) {
            throw new InvalidTripDataException("Client ID is mandatory.");
        }

        Client client = findClientById(normalizedClientId);
        Trip newTrip = new Trip(normalizedDestination, durationDays, basePrice, client);
        storeTrip(newTrip);
        return newTrip;
    }

    public static Trip createTrip(String destination, int durationDays, double basePrice, String clientId, String accommodationId, String transportationId)
            throws InvalidTripDataException, InvalidClientDataException,InvalidAccommodationDataException, InvalidTransportDataException {
                
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

        Client client = findClientById(normalizedClientId);

        Accomodation accommodation = null;
        if (normalizedAccommodationId != null) {
            accommodation = findAccommodationById(normalizedAccommodationId);
        }

        Transportation transportation = null;
        if (normalizedTransportationId != null) {
            transportation = findTransportationById(normalizedTransportationId);
        }

        Trip newTrip = new Trip(client, accommodation, transportation,
                normalizedDestination, durationDays, basePrice);
        storeTrip(newTrip);
        return newTrip;
    }

    private static String normalize(String s) {
        if (s == null) return null;
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    private static void storeTrip(Trip newTrip) {
        if (trips == null) {
            trips = new Trip[1];
        }

        if (tripCount >= trips.length) {
            trips = appendToArray(trips, newTrip, new Trip[trips.length + 1]);
            tripCount++;
            return;
        }

        trips[tripCount++] = newTrip;
    }

    public static Client findClientById(String clientId) throws InvalidClientDataException {
        if (clients != null) {
            for (Client client : clients) {
                if (client != null && client.getClientID().equals(clientId))
                    return client;
            }
        }

        throw new InvalidClientDataException("Client ID not found: " + clientId);
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

    public static boolean clientExists(String clientID) {
        // Linear search through array for matching client ID
        try {
            if (clients.length == 0)
                throw new IllegalArgumentException("");

            for (int i = 0; i < clients.length; i++)
                if (clients[i] != null && clients[i].getClientID().equalsIgnoreCase(clientID))
                    return true;
        } catch (NullPointerException e) {
            System.out.println();
            System.out.println("Client array is empty | " + e.getMessage());
        } 
        return false;
    }

    public static boolean findClientbyId(String clientID) {
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

    public static void loadAllData(String folderPath) {

    }

    public static void saveAllData(String folderPath) {

    }

    public static void calculateTripTotal(int index) {

    }

    public static void testingScenario(boolean choice) throws InvalidClientDataException, InvalidTripDataException,
        InvalidTransportDataException, InvalidAccommodationDataException {
            
        if (choice) {
            clients = new Client[10];
            trips = new Trip[11];
            transportations = new Transportation[10];
            accomodations = new Accomodation[10];

            // Clients from clients.csv (C1008 row skipped: missing lastName field).
            clients[0] = new Client("Sophia", "Rossi", "sophia.rossi@italy.com");
            clients[1] = new Client("Carlos", "Silva", "carlos.silva@brazil.com");
            clients[2] = new Client("Aiko", "Tanaka", "aiko.tanaka@japan.com");
            clients[3] = new Client("Emma", "Wilson", "emma.wilson@usa.com");
            clients[4] = new Client("Miguel", "Gomez", "miguel.gomez@spain.com");
            clients[5] = new Client("John", "Smith", "john.smith@canada.com");
            clients[6] = new Client("Alice", "Johnson", "alice.johnson@uk.com");
            clients[7] = new Client("Diana", "Prince", "diana.prince@greece.com");
            clients[8] = new Client("Lee", "Kim", "lee.kim@korea.com");
            clients[9] = new Client("Mig", "Gomez", "miguel.gomez@spain.com");

            // Transportation from transports.csv (CRUISE row skipped: unsupported type).
            transportations[0] = new Flight("Alitalia", "JFK", "FCO", "Alitalia", 23.0);
            transportations[1] = new Train("Shinkansen", "Tokyo", "Kyoto", "HighSpeed", "Standard");
            transportations[2] = new Bus("Greyhound", "NYC", "Boston", "Greyhound", 3);
            transportations[3] = new Flight("LATAM", "JFK", "GIG", "LATAM", 32.0);
            transportations[4] = new Train("Renfe", "Madrid", "Barcelona", "AVE", "Standard");
            transportations[5] = new Bus("Blablacar", "Rome", "Milan", "Blablacar", 1);
            transportations[6] = new Flight("ANA", "JFK", "NRT", "ANA", 32.0);
            transportations[7] = new Train("Amtrak", "NYC", "DC", "Acela", "Standard");
            transportations[8] = new Flight("AirFrance", "YUL", "CDG", "AirFrance", 25.0);
            transportations[9] = new Bus("VIA", "Toronto", "Montreal", "VIA", 2);

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

            // Valid trips from trips.csv (invalid rows skipped: bad duration/basePrice/missing client).
            trips[0] = new Trip("Paris", 5, 150.00, clients[0]);
            trips[1] = new Trip("Paris", 3, 100.00, clients[4]);
            trips[2] = new Trip("Venice", 7, 200.00, clients[1]);
            trips[3] = new Trip("Paris", 4, 175.00, clients[7]);
            trips[4] = new Trip("Rome", 6, 125.00, clients[5]);
            trips[5] = new Trip("Tokyo", 8, 225.00, clients[2]);
            trips[6] = new Trip("Paris", 10, 300.00, clients[0]);
            trips[7] = new Trip("Rome", 7, 275.00, clients[6]);
            trips[8] = new Trip("Rome", 5, 180.00, clients[5]);
            trips[9] = new Trip("LA", 10, 400.00, clients[6]);
            trips[10] = new Trip("Tokyo", 8, 225.00, clients[2]);

            clientCount = 10;
            tripCount = 11;
            transportationCount = 10;
            accommodationCount = 10;
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

    //Helper method 1
    private static <T> T[] appendToArray(T[] source, T element, T[] expanded) {
        int oldLength = (source == null) ? 0 : source.length;

        if (expanded == null || expanded.length != oldLength + 1)
            throw new IllegalArgumentException("Expanded array must have length oldLength + 1.");

        for (int i = 0; i < oldLength; i++)
            expanded[i] = source[i];

        expanded[oldLength] = element;
        return expanded;
    }

    //Enum values to check for specific types with switch
    public enum ArrayType {
        CLIENTS, TRIPS, TRANSPORTATIONS, ACCOMODATIONS
    }

    //Helper methods 2
    public static String printArray(ArrayType type) {
        String itemString = "";

        switch (type) {
            case CLIENTS -> itemString = buildArrayString(clients, "Client array");
            case TRIPS -> itemString = buildArrayString(trips, "Trip array");
            case TRANSPORTATIONS -> itemString = buildArrayString(transportations, "Transportation array");
            case ACCOMODATIONS -> itemString = buildArrayString(accomodations, "Accommodation array");
            default -> throw new IllegalArgumentException("Invalid array type.");
        }
        return itemString;
    }

    private static <T> String buildArrayString(T[] array, String type) {
        String itemString = "";

        if (array == null || array.length == 0)
            return type + " is empty.";


        if (array instanceof Client[] || array instanceof Trip[]) {
            for (T item : array) {
                if (item != null) {
                    itemString += ">. " + item.toString() + "\n";
                }
            }
        }
        else {
            for (T item : array) {
                if (item != null) {
                    itemString += ">. " + item.getClass().getSimpleName() + " -> " + item.toString() + "\n";
                }
            }
        }
        return itemString;
    }

    //Helper methods 3
    public static int itemExistCheck(ArrayType type, String searchID) {
        int exists = 0;

        switch (type) {
            case CLIENTS -> exists = checkIfExist(clients, searchID, "Client array");
            case TRIPS -> exists = checkIfExist(trips, searchID, "Trip array");
            case TRANSPORTATIONS -> exists = checkIfExist(transportations, searchID, "Transportation array");
            case ACCOMODATIONS -> exists = checkIfExist(accomodations, searchID, "Accomodation array");
            default -> throw new IllegalArgumentException("Invalid array type.");
        }
        return exists;
    }

    private static String extractID(Object item) {
        if (item instanceof Client c) return c.getClientID();
        if (item instanceof Trip t) return t.getTripId();
        if (item instanceof Transportation tr) return tr.getTransportID();
        if (item instanceof Accomodation a) return a.getAccomodationID();
        return null;
    }

    private static <T> int checkIfExist(T[] array, String searchID, String type) {
        int index = 0;
        try {
            if (array == null || array.length == 0)
                throw new IllegalArgumentException();

            for (T item : array) {
                if (item == null) continue;

                String currentID = extractID(item);
                if (currentID != null && currentID.equalsIgnoreCase(searchID.trim()))
                    return index;
                
                index++;
            }
        } catch (NullPointerException e) {
            System.out.println(type + " | " + e.getMessage());
            return nullIndex;
        } catch (IllegalArgumentException f) {
            System.out.println(type + " | " + f.getMessage());
            return nullIndex;
        }
        return nullIndex;
    }

    // A way to resync the static generator after loading
    public static void syncNextID(Class<?> classType, int nextNumericId) {
        //PURPOSE:     Client.nextID = Math.max(Client.nextID, nextNumericId);
        if (nextNumericId < 0)
            throw new IllegalArgumentException("nextNumericId cannot be negative.");

        if (classType == Client.class) {
            Client.syncNextId(nextNumericId);
        } else if (classType == Trip.class) {
            Trip.syncNextId(nextNumericId);
        } else if (classType == Transportation.class) {
            Transportation.syncNextId(nextNumericId);
        } else if (classType == Accomodation.class) {
            Accomodation.syncNextId(nextNumericId);
        } else {
            throw new IllegalArgumentException("Unsupported class type for ID sync: " + classType);
        }
    }
}
