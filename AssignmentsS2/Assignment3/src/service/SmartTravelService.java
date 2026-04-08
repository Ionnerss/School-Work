package AssignmentsS2.Assignment3.src.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import AssignmentsS2.Assignment3.src.client.Client;
import AssignmentsS2.Assignment3.src.exceptions.*;
import AssignmentsS2.Assignment3.src.interfaces.Identifiable;
import AssignmentsS2.Assignment3.src.persistence.*;
import AssignmentsS2.Assignment3.src.travel.*;

public class SmartTravelService {
    public static final int MAX_CLIENTS = 100;
    public static final int MAX_TRIPS = 200;
    public static final int MAX_ACCOMMODATIONS = 50;
    public static final int MAX_TRANSPORTATIONS = 50;
    public static final int nullIndex = -128;

    private static final List<Client> clients = new ArrayList<>();
    private static final List<Trip> trips = new ArrayList<>();
    private static final List<Accommodation> accommodations = new ArrayList<>();
    private static final List<Transportation> transportations = new ArrayList<>();

    // Keep these because your existing flow still references them.
    private static String clientId;
    private static String accommodationId;
    private static String transportationId;
    private static String destination;
    private static int durationDays;
    private static double basePrice;

    // Invalid predefined/file rows preserved for display/logging.
    private static final List<String> invalidClientRows = new ArrayList<>();
    private static final List<String> invalidTripRows = new ArrayList<>();
    private static final List<String> invalidTransportationRows = new ArrayList<>();
    private static final List<String> invalidAccomodationRows = new ArrayList<>();

    /* =========================
       Basic List getters/setters
       ========================= */

    public List<Client> getClients() {
        return clients;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public List<Transportation> getTransportations() {
        return transportations;
    }

    public List<Accommodation> getAccomodations() {
        return accommodations;
    }

    public void setClients(List<Client> updatedClients) {
        replaceCollection(clients, updatedClients, MAX_CLIENTS, "clients");
        refreshClientAmountsSpent();
    }

    public void setTrips(List<Trip> updatedTrips) {
        replaceCollection(trips, updatedTrips, MAX_TRIPS, "trips");
        refreshClientAmountsSpent();
    }

    public void setTransportations(List<Transportation> updatedTransportations) {
        replaceCollection(transportations, updatedTransportations, MAX_TRANSPORTATIONS, "transportations");
    }

    public void setAccomodations(List<Accommodation> updatedAccomodations) {
        replaceCollection(accommodations, updatedAccomodations, MAX_ACCOMMODATIONS, "accommodations");
    }

    private static <T> void replaceCollection(List<T> target, List<T> source, int max, String typeName) {
        target.clear();

        if (source == null) {
            return;
        }

        for (T item : source) {
            if (item == null) {
                continue;
            }

            if (target.size() >= max) {
                throw new IllegalStateException("Too many " + typeName + ". Max is " + max + ".");
            }

            target.add(item);
        }
    }

    public Client getClient(int index) {return getCollection(clients, index);}

    public Trip getTrip(int index) {return getCollection(trips, index);}

    public Transportation getTransportation(int index) {return getCollection(transportations, index);}

    public Accommodation getAccommodation(int index) {return getCollection(accommodations, index);}

    private <T> T getCollection(List<T> collection, int index) {
        if (index < 0 || index >= collection.size()) {
            return null;
        }
        return collection.get(index);
    }

    public int getClientCount() {
        return clients.size();
    }

    public int getTripCount() {
        return trips.size();
    }

    public int getTransportationCount() {
        return transportations.size();
    }

    public int getAccommodationCount() {
        return accommodations.size();
    }

    /* =========================
       Index helpers for driver
       ========================= */

    public int getClientIndexById(String id) {return indexOfId(clients, id);}

    public int getTripIndexById(String id) {return indexOfId(trips, id);}

    public int getTransportationIndexById(String id) {return indexOfId(transportations, id);}

    public int getAccommodationIndexById(String id) {return indexOfId(accommodations, id);}

    private static <T extends Identifiable> int indexOfId(List<T> items, String id) {
        String normalizedId = normalize(id);
        if (normalizedId == null) {
            return -1;
        }

        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);
            if (item != null && normalizedId.equalsIgnoreCase(item.getId())) {
                return i;
            }
        }

        return -1;
    }

    public boolean removeClientById(String id) {
        int index = getClientIndexById(id);
        return removeItemById(clients, id, index);
    }

    public boolean removeTripById(String id) {
        int index = getTripIndexById(id);
        return removeItemById(trips, id, index);
    }

    public boolean removeTransportationById(String id) {
        int index = getTransportationIndexById(id);
        return removeItemById(transportations, id, index);
    }

    public boolean removeAccommodationById(String id) {
        int index = getAccommodationIndexById(id);
        return removeItemById(accommodations, id, index);
    }

    private <T> boolean removeItemById(List<T> collection, String id, int index) {
        if (index < 0) {
            return false;
        }

        collection.remove(index);
        return true;
    }

    /* =========================
       Invalid row handling
       ========================= */

    private static List<String> getInvalidCollection(String collectionType) {
        if (collectionType == null) {
            return null;
        }

        return switch (collectionType.trim().toLowerCase()) {
            case "clients" -> invalidClientRows;
            case "trips" -> invalidTripRows;
            case "transportations" -> invalidTransportationRows;
            case "accommodations" -> invalidAccomodationRows;
            default -> null;
        };
    }

    public static int getInvalidCollectionCount(String collectionType) {
        List<String> rows = getInvalidCollection(collectionType);
        return rows == null ? 0 : rows.size();
    }

    public static String getInvalidCollectionRow(String collectionType, int index) {
        List<String> rows = getInvalidCollection(collectionType);
        if (rows == null || index < 0 || index >= rows.size()) {
            return null;
        }
        return rows.get(index);
    }

    private static void addInvalidRow(String collectionType, String row) {
        List<String> rows = getInvalidCollection(collectionType);
        if (rows != null && row != null) {
            rows.add(row);
        }
    }

    public static void clearInvalidById(String collectionType, String id, boolean isPrefix) {
        List<String> rows = getInvalidCollection(collectionType);
        String normalizedId = normalize(id);

        if (rows == null || normalizedId == null) {
            return;
        }

        for (int i = rows.size() - 1; i >= 0; i--) {
            String row = rows.get(i);
            if (row == null) {
                continue;
            }

            boolean matches = isPrefix
                    ? row.startsWith(normalizedId + ";")
                    : row.contains(normalizedId);

            if (matches) {
                rows.remove(i);
            }
        }
    }

    public static void clearInvalidPredefinedRows() {
        invalidClientRows.clear();
        invalidTripRows.clear();
        invalidTransportationRows.clear();
        invalidAccomodationRows.clear();
    }

    // Specific wrappers so old code can still call them for now.
    public static int getInvalidClientCount() {
        return invalidClientRows.size();
    }

    public static String getInvalidClientRow(int index) {
        return getInvalidCollectionRow("clients", index);
    }

    public static int getInvalidTripCount() {
        return invalidTripRows.size();
    }

    public static String getInvalidTripRow(int index) {
        return getInvalidCollectionRow("trips", index);
    }

    public static int getInvalidTransportationCount() {
        return invalidTransportationRows.size();
    }

    public static String getInvalidTransportationRow(int index) {
        return getInvalidCollectionRow("transportations", index);
    }

    public static int getInvalidAccomodationCount() {
        return invalidAccomodationRows.size();
    }

    public static String getInvalidAccomodationRow(int index) {
        return getInvalidCollectionRow("accommodations", index);
    }

    public static void clearInvalidClientById(String id) {
        clearInvalidById("clients", id, true);
    }

    public static void clearInvalidTripById(String id) {
        clearInvalidById("trips", id, true);
    }

    public static void clearInvalidTransportationById(String id) {
        clearInvalidById("transportations", ";" + id + ";", false);
    }

    public static void clearInvalidAccomodationById(String id) {
        clearInvalidById("accommodations", id + ";", false);
    }

    /* =========================
       Client operations
       ========================= */

    public static void addClient(String firstName, String lastName, String email)
            throws InvalidClientDataException {

        String normalizedEmail = normalize(email);
        if (normalizedEmail == null) {
            throw new InvalidClientDataException("Email cannot be null.");
        }

        if (emailAlreadyExists(normalizedEmail, null)) {
            throw new DuplicateEmailException("A client with this email already exists.");
        }

        if (clients.size() >= MAX_CLIENTS) {
            throw new IllegalStateException("Client list is full. Max is " + MAX_CLIENTS + ".");
        }

        Client newClient = new Client(firstName, lastName, normalizedEmail);
        clients.add(newClient);
    }

    public static void updateClient(String clientId, String firstName, String lastName, String email)
            throws InvalidClientDataException, DuplicateEmailException, EntityNotFoundException {

        String normalizedClientId = normalize(clientId);
        if (normalizedClientId == null) {
            throw new InvalidClientDataException("Client ID cannot be empty.");
        }

        Client client = findClientByIdObj(normalizedClientId);

        String validatedFirstName = Client.validateName(firstName, "First name");
        String validatedLastName = Client.validateName(lastName, "Last name");
        String validatedEmail = Client.validateEmail(email);

        for (Client existing : clients) {
            if (existing == null) {
                continue;
            }

            if (existing.getId().equals(client.getId())) {
                continue;
            }

            if (existing.getEmailAdress().equalsIgnoreCase(validatedEmail)) {
                throw new DuplicateEmailException("A client with this email already exists.");
            }
        }

        client.setFirstName(validatedFirstName);
        client.setLastName(validatedLastName);
        client.setEmailAdress(validatedEmail);
    }

    private static boolean emailAlreadyExists(String email, String excludedClientId) {
        if (email == null) {
            return false;
        }

        for (Client client : clients) {
            if (client == null) {
                continue;
            }

            if (excludedClientId != null && excludedClientId.equalsIgnoreCase(client.getId())) {
                continue;
            }

            if (email.equalsIgnoreCase(client.getEmailAdress())) {
                return true;
            }
        }

        return false;
    }

    /* =========================
       Trip creation + lookups
       ========================= */

    public static Trip createTrip() throws InvalidTripDataException, EntityNotFoundException {
        String normalizedAccommodationId = normalize(accommodationId);
        String normalizedTransportationId = normalize(transportationId);

        if (normalizedAccommodationId == null && normalizedTransportationId == null) {
            return createTrip(destination, durationDays, basePrice, clientId, "", "");
        }

        return createTrip(
                destination,
                durationDays,
                basePrice,
                clientId,
                normalizedAccommodationId,
                normalizedTransportationId
        );
    }

    public static Trip createTrip(String destination, int durationDays, double basePrice,
                                  String clientId, String accommodationId, String transportationId)
            throws InvalidTripDataException, EntityNotFoundException {

        String normalizedDestination = normalize(destination);
        String normalizedClientId = normalize(clientId);
        String normalizedAccommodationId = normalize(accommodationId);
        String normalizedTransportationId = normalize(transportationId);

        if (normalizedDestination == null) {
            throw new InvalidTripDataException("Destination cannot be null.");
        }

        if (normalizedClientId == null) {
            throw new InvalidTripDataException("Client ID is mandatory.");
        }

        if (normalizedAccommodationId == null && normalizedTransportationId == null) {
            throw new InvalidTripDataException(
                    "At least one of accommodation ID or transportation ID must be provided."
            );
        }

        Client client = findClientByIdObj(normalizedClientId);

        String finalAccommodationId = "";
        if (normalizedAccommodationId != null) {
            Accommodation accommodation = findAccommodationById(normalizedAccommodationId);
            finalAccommodationId = accommodation.getId();
        }

        String finalTransportationId = "";
        if (normalizedTransportationId != null) {
            Transportation transportation = findTransportationById(normalizedTransportationId);
            finalTransportationId = transportation.getId();
        }

        Trip newTrip = new Trip(
                client.getId(),
                finalAccommodationId,
                finalTransportationId,
                normalizedDestination,
                durationDays,
                basePrice
        );

        storeTrip(newTrip);
        return newTrip;
    }

    public static Trip findTripById(String tripId) throws EntityNotFoundException {
        String normalizedTripId = normalize(tripId);

        if (normalizedTripId == null || !normalizedTripId.matches("T\\d+")) {
            throw new EntityNotFoundException("Trip ID not found: " + tripId);
        }

        for (Trip trip : trips) {
            if (trip != null && trip.getId().equalsIgnoreCase(normalizedTripId)) {
                return trip;
            }
        }

        throw new EntityNotFoundException("Trip ID not found: " + tripId);
    }

    public static Accommodation findAccommodationById(String accommodationId)
            throws EntityNotFoundException {

        String normalizedAccommodationId = normalize(accommodationId);

        if (normalizedAccommodationId == null || !normalizedAccommodationId.matches("A\\d+")) {
            throw new EntityNotFoundException("Accommodation ID not found: " + accommodationId);
        }

        for (Accommodation accommodation : accommodations) {
            if (accommodation != null && accommodation.getId().equalsIgnoreCase(normalizedAccommodationId)) {
                return accommodation;
            }
        }

        throw new EntityNotFoundException("Accommodation ID not found: " + accommodationId);
    }

    public static Transportation findTransportationById(String transportationId)
            throws EntityNotFoundException {

        String normalizedTransportationId = normalize(transportationId);

        if (normalizedTransportationId == null || !normalizedTransportationId.matches("TR\\d+")) {
            throw new EntityNotFoundException("Transportation ID not found: " + transportationId);
        }

        for (Transportation transportation : transportations) {
            if (transportation != null && transportation.getId().equalsIgnoreCase(normalizedTransportationId)) {
                return transportation;
            }
        }

        throw new EntityNotFoundException("Transportation ID not found: " + transportationId);
    }

    public static Client findClientByIdObj(String clientId) throws EntityNotFoundException {
        String normalizedClientId = normalize(clientId);

        if (normalizedClientId == null || !normalizedClientId.matches("C\\d+")) {
            throw new EntityNotFoundException("Client ID not found: " + clientId);
        }

        for (Client client : clients) {
            if (client != null && client.getId().equalsIgnoreCase(normalizedClientId)) {
                return client;
            }
        }

        throw new EntityNotFoundException("Client ID not found: " + clientId);
    }

    public static boolean findClientbyIdBool(String clientId) {
        String normalizedClientId = normalize(clientId);

        if (normalizedClientId == null) {
            throw new IllegalArgumentException("Client ID cannot be empty.");
        }

        if (!normalizedClientId.matches("C\\d+")) {
            throw new IllegalArgumentException("Invalid Client ID format: " + clientId);
        }

        for (Client client : clients) {
            if (client != null && normalizedClientId.equalsIgnoreCase(client.getId())) {
                return true;
            }
        }

        return false;
    }

    public static boolean clientExists(String clientId) throws InvalidClientDataException {
        String normalizedClientId = normalize(clientId);

        if (normalizedClientId == null) {
            throw new InvalidClientDataException("Client ID cannot be empty.");
        }

        if (!normalizedClientId.matches("C\\d+")) {
            throw new InvalidClientDataException("Invalid Client ID format: " + clientId);
        }

        for (Client client : clients) {
            if (client != null && normalizedClientId.equalsIgnoreCase(client.getId())) {
                return true;
            }
        }

        return false;
    }

    private static void storeTrip(Trip newTrip) {
        if (trips.size() >= MAX_TRIPS) {
            throw new IllegalStateException("Trip list is full. Max is " + MAX_TRIPS + ".");
        }

        trips.add(newTrip);
        refreshClientAmountsSpent();
    }

    public double calculateTripTotal(int index) throws EntityNotFoundException {
        if (index < 0 || index >= trips.size() || trips.get(index) == null) {
            return 0.0;
        }

        return trips.get(index).calculateTotalCost();
    }

    /* =========================
       Printing
       ========================= */

    public static String printClients() {return printCollection(clients, "clients");}

    public static String printTrips() {return printCollection(trips, "trips");}

    public static String printTransportations() {return printCollection(transportations, "transportations");}

    public static String printAccomodations() {return printCollection(accommodations, "accommodations");}

    private static <T> String printCollection(List<T> collection, String type) {
        StringBuilder output = new StringBuilder();

        if (collection == null || collection.isEmpty()) {
            output.append(">. No " + type +" available.\n\n");
            return output.toString();
        }

        for (T element : collection) {
            if (element != null) {
                output.append(">. ").append(element).append("\n");
            }
        }

        output.append("\n");
        return output.toString();
    }

    /* =========================
       Persistence
       Keep old managers for now,
       convert List <-> array here.
       ========================= */

    public void loadAllData(String folderPath) throws IOException {
        try {
            Path basePath = resolveProjectBasePath(folderPath);
            ErrorLogger.setBasePath(basePath.toString());

            Path dataDir = basePath.resolve("output").resolve("data");
            String clientsFile = dataDir.resolve("clients.csv").toString();
            String tripsFile = dataDir.resolve("trips.csv").toString();
            String transportationsFile = dataDir.resolve("transports.csv").toString();
            String accomodationsFile = dataDir.resolve("accommodations.csv").toString();

            ensureOutputDirectories(folderPath);

            Client[] loadedClients = new Client[MAX_CLIENTS];
            ClientFileManager.loadClients(loadedClients, clientsFile);
            setClients(arrayToList(loadedClients));

            Transportation[] loadedTransportations = new Transportation[MAX_TRANSPORTATIONS];
            TransportationFileManager.loadTransportations(loadedTransportations, transportationsFile);
            setTransportations(arrayToList(loadedTransportations));

            Accommodation[] loadedAccommodations = new Accommodation[MAX_ACCOMMODATIONS];
            AccommodationFileManager.loadAccomodations(loadedAccommodations, accomodationsFile);
            setAccomodations(arrayToList(loadedAccommodations));

            Trip[] loadedTrips = new Trip[MAX_TRIPS];
            TripFileManager.loadTrips(loadedTrips, tripsFile);
            setTrips(arrayToList(loadedTrips));

            syncIdsFromCurrentData();

            System.out.println();
            System.out.println(">. Data loaded successfully from output/data/*.csv.");
            System.out.println(">. Errors, if any, were logged to output/logs/errors.txt.");
            System.out.println();
        } catch (IOException e) {
            System.out.println(">. Error loading data: " + e.getMessage());
            System.out.println();
        }
    }

    public void saveAllData(String folderPath) throws IOException {
        try {
            Path basePath = resolveProjectBasePath(folderPath);
            ErrorLogger.setBasePath(basePath.toString());

            Path dataDir = basePath.resolve("output").resolve("data");
            String clientsFile = dataDir.resolve("clients.csv").toString();
            String tripsFile = dataDir.resolve("trips.csv").toString();
            String transportationsFile = dataDir.resolve("transports.csv").toString();
            String accomodationsFile = dataDir.resolve("accommodations.csv").toString();

            resetOutputFolders(folderPath);
            ensureOutputDirectories(folderPath);

            ClientFileManager.saveClients(clients.toArray(new Client[0]), clients.size(), clientsFile);
            TransportationFileManager.saveTransportations(
                    transportations.toArray(new Transportation[0]),
                    transportations.size(),
                    transportationsFile
            );
            AccommodationFileManager.saveAccomodations(
                    accommodations.toArray(new Accommodation[0]),
                    accommodations.size(),
                    accomodationsFile
            );
            TripFileManager.saveTrips(trips.toArray(new Trip[0]), trips.size(), tripsFile);

            System.out.println();
            System.out.println(">. Data saved successfully to output/data/*.csv.");
            System.out.println(">. Errors, if any, were logged to output/logs/errors.txt.");
            System.out.println();
        } catch (IOException e) {
            System.out.println(">. Error saving data: " + e.getMessage());
            System.out.println();
        }
    }

    private static <T> List<T> arrayToList(T[] array) {
        List<T> list = new ArrayList<>();

        if (array == null) {
            return list;
        }

        for (T item : array) {
            if (item != null) {
                list.add(item);
            }
        }

        return list;
    }

    public void ensureOutputDirectories() throws IOException {
        ensureOutputDirectories(null);
    }

    public void ensureOutputDirectories(String folderPath) throws IOException {
        Path basePath = resolveProjectBasePath(folderPath);
        Files.createDirectories(basePath.resolve("output"));
        Files.createDirectories(basePath.resolve("output").resolve("data"));
        Files.createDirectories(basePath.resolve("output").resolve("logs"));
        Files.createDirectories(basePath.resolve("output").resolve("dashboard"));
    }

    private Path resolveProjectBasePath(String folderPath) {
        if (folderPath != null && !folderPath.trim().isEmpty()) {
            return Paths.get(folderPath).toAbsolutePath().normalize();
        }

        Path cwd = Paths.get("").toAbsolutePath().normalize();
        if (Files.exists(cwd.resolve("src")) && Files.exists(cwd.resolve("libraries"))) {
            return cwd;
        }

        Path assignment3 = cwd.resolve("AssignmentsS2").resolve("Assignment3");
        if (Files.exists(assignment3)) {
            return assignment3;
        }

        return cwd;
    }

    private void resetOutputFolders(String folderPath) throws IOException {
        Path basePath = resolveProjectBasePath(folderPath);
        Path outputPath = basePath.resolve("output");

        deleteDirectoryContents(outputPath.resolve("data"));
        deleteDirectoryContents(outputPath.resolve("logs"));
        deleteDirectoryContents(outputPath.resolve("dashboard"));
    }

    private void deleteDirectoryContents(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            return;
        }

        try (var stream = Files.list(directory)) {
            for (Path child : stream.toList()) {
                if ("styles.css".equalsIgnoreCase(child.getFileName().toString())) {
                    continue;
                }

                if (Files.isDirectory(child)) {
                    deleteDirectoryContents(child);
                    Files.deleteIfExists(child);
                } else {
                    Files.deleteIfExists(child);
                }
            }
        }
    }

    /* =========================
       Scenario loading
       ========================= */

    public static void testingScenario(boolean choice)
            throws InvalidClientDataException, InvalidTripDataException,
            InvalidTransportDataException, InvalidAccommodationDataException,
            EntityNotFoundException {

        clearInvalidPredefinedRows();
        clients.clear();
        trips.clear();
        transportations.clear();
        accommodations.clear();

        if (choice) {
            // Clients
            tryLoadClient("C1001", "Sophia", "Rossi", "sophia.rossi@italy.com");
            tryLoadClient("C1002", "Carlos", "Silva", "carlos.silva@brazil.com");
            tryLoadClient("C1003", "Aiko", "Tanaka", "aiko.tanaka@japan.com");
            tryLoadClient("C1004", "Emma", "Wilson", "emma.wilson@usa.com");
            tryLoadClient("C1005", "Miguel", "Gomez", "miguel.gomez@spain.com");
            tryLoadClient("C1006", "John", "Smith", "john.smith@canada.com");
            tryLoadClient("C1007", "Alice", "Johnson", "alice.johnson@uk.com");
            tryLoadClient("C1008", "Bob Browm", "bob.brown@australia.com", "");
            tryLoadClient("C1009", "Diana", "Prince", "diana.prince@greece.com");
            tryLoadClient("C1010", "Lee", "Kim", "lee.kim@korea.com");
            tryLoadClient("C1011", "Mig", "Gomez", "miguel.gomez@spain.com");

            // Transportation
            tryLoadFlight("TR3001", "Alitalia", "JFK", "FCO", 850.00, 23.0);
            tryLoadTrain("TR3002", "Shinkansen", "Tokyo", "Kyoto", 250.00, "HighSpeed");
            tryLoadBus("TR3003", "Greyhound", "NYC", "Boston", 75.00, 3);
            tryLoadFlight("TR3004", "LATAM", "JFK", "GIG", 950.00, 32.0);
            tryLoadTrain("TR3005", "Renfe", "Madrid", "Barcelona", 120.00, "AVE");
            tryLoadBus("TR3006", "Blablacar", "Rome", "Milan", 45.00, 1);
            tryLoadFlight("TR3007", "ANA", "JFK", "NRT", 1200.00, 32.0);
            tryLoadTrain("TR3008", "Amtrak", "NYC", "DC", 89.00, "Acela");
            tryLoadFlight("TR3009", "AirFrance", "YUL", "CDG", 750.00, 25.0);
            tryLoadBus("TR3010", "VIA", "Toronto", "Montreal", 60.00, 2);

            // Accommodations
            tryLoadHotel("Hilton Rome", "Rome", 280.00, 4);
            tryLoadHostel("Rome Backpackers", "Rome", 55.00, 6);
            tryLoadHotel("Grand Rio", "Rio", 320.00, 5);
            tryLoadHotel("Kyoto Imperial", "Kyoto", 410.00, 5);
            tryLoadHostel("Tokyo Youth", "Tokyo", 60.00, 8);
            tryLoadHotel("MGM Grand", "Las Vegas", 290.00, 4);
            tryLoadHotel("W New York", "NYC", 450.00, 5);
            tryLoadHostel("Barcelona Budget", "Barcelona", 48.00, 6);
            tryLoadHostel("Paris Budget", "Paris", 52.00, 8);
            tryLoadHotel("Tokyo Hilton", "Tokyo", 380.00, 4);

            // Trips
            tryLoadTrip("T2001", "C1001", "A4001", "TR3001", "Paris", 5, 150.00);
            tryLoadTrip("T2002", "C1005", "", "TR3002", "Paris", 3, 100.00);
            tryLoadTrip("T2003", "C1002", "A4002", "TR3003", "Venice", 7, 200.00);
            tryLoadTrip("T2004", "C1009", "A4003", "", "Paris", 4, 175.00);
            tryLoadTrip("T2005", "C1006", "", "TR3004", "Rome", 6, 125.00);
            tryLoadTrip("T2006", "C1003", "A4004", "TR3005", "Tokyo", 8, 225.00);
            tryLoadTrip("T2007", "C1001", "A4005", "", "Paris", 10, 300.00);
            tryLoadTrip("T2008", "C1004", "", "TR3006", "London", 5, 90.00);
            tryLoadTrip("T2009", "C1007", "A4006", "TR3007", "Rome", 7, 275.00);
            tryLoadTrip("T2010", "C1005", "A4003", "T5003", "Madrid", -2, 250.00);
            tryLoadTrip("T2007", "C1006", "A4005", "T5005", "Rome", 5, 180.00);
            tryLoadTrip("T2008", "C9999", "A4006", "T5006", "NYC", 8, 300.00);
            tryLoadTrip("X6008", "C1007", "A4007", "T5007", "LA", 10, 400.00);
            tryLoadTrip("T2011", "C1003", "A4104", "TR3005", "Tokyo", 8, 225.00);
        }

        syncIdsFromCurrentData();
        refreshClientAmountsSpent();
    }

    private static void tryLoadClient(String id, String firstName, String lastName, String email) {
        try {
            clients.add(new Client(id, firstName, lastName, email));
        } catch (Exception e) {
            addInvalidRow("clients", id + ";" + firstName + ";" + lastName + ";" + email + " [" + e.getMessage() + "]");

            try {
                String safeFirstName = (firstName == null || firstName.trim().isEmpty()) ? "Unknown" : firstName;
                String safeLastName = (lastName == null || lastName.trim().isEmpty()) ? "Unknown" : lastName;
                String safeEmail = (email == null || email.trim().isEmpty())
                        ? ("missing." + (id == null ? "client" : id.toLowerCase()) + "@invalid.com")
                        : email;

                if (id != null && id.trim().matches("C\\d+")) {
                    clients.add(new Client(id.trim(), safeFirstName, safeLastName, safeEmail));
                } else {
                    clients.add(new Client(safeFirstName, safeLastName, safeEmail));
                }
            } catch (Exception ignored) {
                // Leave only invalid row if fallback also fails.
            }
        }
    }

    private static void tryLoadFlight(String id, String companyName, String departureCity,
                                      String arrivalCity, double baseFare, double luggageAllowance) {
        try {
            transportations.add(new Flight(id, companyName, departureCity, arrivalCity, baseFare, luggageAllowance));
        } catch (Exception e) {
            addInvalidRow("transportations",
                    "FLIGHT;" + id + ";" + companyName + ";" + departureCity + ";" + arrivalCity + ";"
                            + baseFare + ";" + luggageAllowance + " [" + e.getMessage() + "]");
        }
    }

    private static void tryLoadTrain(String id, String companyName, String departureCity,
                                     String arrivalCity, double baseFare, String seatClass) {
        try {
            transportations.add(new Train(id, companyName, departureCity, arrivalCity, baseFare, seatClass));
        } catch (Exception e) {
            addInvalidRow("transportations",
                    "TRAIN;" + id + ";" + companyName + ";" + departureCity + ";" + arrivalCity + ";"
                            + baseFare + ";" + seatClass + " [" + e.getMessage() + "]");
        }
    }

    private static void tryLoadBus(String id, String companyName, String departureCity,
                                   String arrivalCity, double baseFare, int numberOfStops) {
        try {
            transportations.add(new Bus(id, companyName, departureCity, arrivalCity, baseFare, numberOfStops));
        } catch (Exception e) {
            addInvalidRow("transportations",
                    "BUS;" + id + ";" + companyName + ";" + departureCity + ";" + arrivalCity + ";"
                            + baseFare + ";" + numberOfStops + " [" + e.getMessage() + "]");
        }
    }

    private static void tryLoadHotel(String name, String location, double pricePerNight, int starRating) {
        try {
            accommodations.add(new Hotel(name, location, pricePerNight, starRating));
        } catch (Exception e) {
            addInvalidRow("accommodations",
                    "HOTEL;" + name + ";" + location + ";" + pricePerNight + ";" + starRating
                            + " [" + e.getMessage() + "]");
        }
    }

    private static void tryLoadHostel(String name, String location, double pricePerNight, int sharedBeds) {
        try {
            accommodations.add(new Hostel(name, location, pricePerNight, sharedBeds));
        } catch (Exception e) {
            addInvalidRow("accommodations",
                    "HOSTEL;" + name + ";" + location + ";" + pricePerNight + ";" + sharedBeds
                            + " [" + e.getMessage() + "]");
        }
    }

    private static void tryLoadTrip(String tripId, String clientId, String accomodationId,
                                    String transportationId, String destination, int duration, double basePrice) {
        try {
            trips.add(new Trip(tripId, clientId, accomodationId, transportationId, destination, duration, basePrice));
        } catch (Exception e) {
            addInvalidRow("trips",
                    tripId + ";" + clientId + ";" + accomodationId + ";" + transportationId + ";"
                            + destination + ";" + duration + ";" + basePrice + " [" + e.getMessage() + "]");
        }
    }

    /* =========================
       Internal helpers
       ========================= */

    private static String normalize(String s) {
        if (s == null) {
            return null;
        }

        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    private static void refreshClientAmountsSpent() {
        for (Client client : clients) {
            if (client != null) {
                client.resetAmountSpent();
            }
        }

        for (Trip trip : trips) {
            if (trip == null) {
                continue;
            }

            try {
                Client owner = findClientByIdObj(trip.getClientId());
                owner.addToAmountSpent(trip.calculateTotalCost());
            } catch (Exception ignored) {
                // Broken linked records are skipped while rebuilding totals.
            }
        }
    }

    private static void syncIdsFromCurrentData() {
        int maxClientId = -1;
        for (Client client : clients) {
            maxClientId = Math.max(maxClientId, extractNumericId(client == null ? null : client.getId(), "C"));
        }
        if (maxClientId >= 0) {
            Client.syncNextId(maxClientId + 1);
        }

        int maxTripId = -1;
        for (Trip trip : trips) {
            maxTripId = Math.max(maxTripId, extractNumericId(trip == null ? null : trip.getId(), "T"));
        }
        if (maxTripId >= 0) {
            Trip.syncNextId(maxTripId + 1);
        }

        int maxAccommodationId = -1;
        for (Accommodation accommodation : accommodations) {
            maxAccommodationId = Math.max(
                    maxAccommodationId,
                    extractNumericId(accommodation == null ? null : accommodation.getId(), "A")
            );
        }
        if (maxAccommodationId >= 0) {
            Accommodation.syncNextId(maxAccommodationId + 1);
        }

        int maxTransportationId = -1;
        for (Transportation transportation : transportations) {
            maxTransportationId = Math.max(
                    maxTransportationId,
                    extractNumericId(transportation == null ? null : transportation.getId(), "TR")
            );
        }
        if (maxTransportationId >= 0) {
            Transportation.syncNextId(maxTransportationId + 1);
        }
    }

    private static int extractNumericId(String id, String prefix) {
        if (id == null || prefix == null) {
            return -1;
        }

        String trimmed = id.trim();
        if (!trimmed.startsWith(prefix) || trimmed.length() <= prefix.length()) {
            return -1;
        }

        try {
            return Integer.parseInt(trimmed.substring(prefix.length()));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}