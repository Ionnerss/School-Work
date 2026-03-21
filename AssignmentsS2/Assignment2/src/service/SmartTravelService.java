package AssignmentsS2.Assignment2.src.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


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

    // Keep invalid predefined rows visible to users instead of silently dropping them.
    private static String[] invalidClientRows = new String[20];
    private static String[] invalidTripRows = new String[30];
    private static String[] invalidTransportationRows = new String[20];
    private static String[] invalidAccomodationRows = new String[20];
    private static int invalidClientCount = 0;
    private static int invalidTripCount = 0;
    private static int invalidTransportationCount = 0;
    private static int invalidAccomodationCount = 0;

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

    public static int getInvalidClientCount() {
        return invalidClientCount;
    }

    public static String getInvalidClientRow(int index) {
        if (index < 0 || index >= invalidClientCount)
            return null;
        return invalidClientRows[index];
    }

    public static int getInvalidTripCount() {
        return invalidTripCount;
    }

    public static String getInvalidTripRow(int index) {
        if (index < 0 || index >= invalidTripCount)
            return null;
        return invalidTripRows[index];
    }

    public static int getInvalidTransportationCount() {
        return invalidTransportationCount;
    }

    public static String getInvalidTransportationRow(int index) {
        if (index < 0 || index >= invalidTransportationCount)
            return null;
        return invalidTransportationRows[index];
    }

    public static int getInvalidAccomodationCount() {
        return invalidAccomodationCount;
    }

    public static String getInvalidAccomodationRow(int index) {
        if (index < 0 || index >= invalidAccomodationCount)
            return null;
        return invalidAccomodationRows[index];
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

        Trip newTrip = new Trip(client.getClientId(), accommodation.getAccomodationID(), transportation.getTransportID(), normalizedDestination, durationDays, basePrice);
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
                if (client != null && client.getClientId().equals(clientId))
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
            if (trimmed == specifiClient.getClientId())
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
            if (trimmed == specifiClient.getClientId())
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

    public void loadAllData(String folderPath) throws IOException {
        try {
            Path basePath = resolveProjectBasePath(folderPath);
            ErrorLogger.setBasePath(basePath.toString());
            Path dataDir = basePath.resolve("output").resolve("data");
            String clientsFile = dataDir.resolve("clients.csv").toString();
            String tripsFile = dataDir.resolve("trips.csv").toString();
            String transportationsFile = dataDir.resolve("transportations.csv").toString();
            String accomodationsFile = dataDir.resolve("accomodations.csv").toString();

            ensureOutputDirectories(folderPath);

            Client[] loadedClients = new Client[Math.max(1, countDataLines(clientsFile))];
            ClientFileManager.loadClients(loadedClients, clientsFile);
            setClients(trimClients(loadedClients));

            Transportation[] loadedTransportations =
                new Transportation[Math.max(1, countDataLines(transportationsFile))];
            TransportFileManager.loadTransportations(loadedTransportations, transportationsFile);
            setTransportations(trimTransportations(loadedTransportations));

            Accomodation[] loadedAccommodations = loadAccommodationsFromFile(accomodationsFile);
            setAccomodations(loadedAccommodations);

            Trip[] loadedTrips = new Trip[Math.max(1, countDataLines(tripsFile))];
            TripFileManager.loadTrips(loadedTrips, tripsFile);
            setTrips(trimTrips(loadedTrips));

            System.out.println();
            System.out.println(">. Data loaded successfully from output/data/*.csv.");
            System.out.println(">. Errors, if any, were logged to output/logs/errors.txt.");
            System.out.println();
        } catch (IOException e) {
            System.out.println(">. Error loading data: " + e.getMessage());
            System.out.println();
        }
    }

    private static Accomodation[] loadAccommodationsFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            return new Accomodation[0];
        }

        Accomodation[] loaded = new Accomodation[Math.max(1, countDataLines(filePath))];
        int index = 0;

        for (String rawLine : Files.readAllLines(path)) {
            String line = rawLine.trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split(";", -1);
            if (parts.length != 6) {
                continue;
            }

            String type = parts[0].trim().toUpperCase();
            String id = parts[1].trim();
            String name = parts[2].trim();
            String location = parts[3].trim();
            double pricePerNight = Double.parseDouble(parts[4].trim());
            int starOrBeds = Integer.parseInt(parts[5].trim());

            try {
                if ("HOTEL".equals(type)) {
                    loaded[index++] = new Hotel(id, name, location, pricePerNight, starOrBeds);
                } else if ("HOSTEL".equals(type)) {
                    loaded[index++] = new Hostel(id, name, location, pricePerNight, starOrBeds);
                }
            } catch (InvalidAccommodationDataException e) {
                // Ignore invalid entries here; dedicated file managers already handle formal error logging elsewhere.
            }
        }

        return trimAccomodations(loaded);
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

        Path assignment2 = cwd.resolve("AssignmentsS2").resolve("Assignment2");
        if (Files.exists(assignment2)) {
            return assignment2;
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
                if (Files.isDirectory(child)) {
                    deleteDirectoryContents(child);
                    Files.deleteIfExists(child);
                } else {
                    Files.deleteIfExists(child);
                }
            }
        }
    }

    private static Client[] trimClients(Client[] values) {
        int size = countNonNull(values);
        Client[] trimmed = new Client[size];
        int index = 0;

        for (Client value : values) {
            if (value != null)
                trimmed[index++] = value;
        }

        return trimmed;
    }

    private static Transportation[] trimTransportations(Transportation[] values) {
        int size = countNonNull(values);
        Transportation[] trimmed = new Transportation[size];
        int index = 0;

        for (Transportation value : values) {
            if (value != null)
                trimmed[index++] = value;
        }

        return trimmed;
    }

    private static Trip[] trimTrips(Trip[] values) {
        int size = countNonNull(values);
        Trip[] trimmed = new Trip[size];
        int index = 0;

        for (Trip value : values) {
            if (value != null)
                trimmed[index++] = value;
        }

        return trimmed;
    }

    private static Accomodation[] trimAccomodations(Accomodation[] values) {
        int size = countNonNull(values);
        Accomodation[] trimmed = new Accomodation[size];
        int index = 0;

        for (Accomodation value : values) {
            if (value != null)
                trimmed[index++] = value;
        }

        return trimmed;
    }

    private static int countDataLines(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            return 0;
        }

        int count = 0;
        for (String line : Files.readAllLines(path)) {
            if (!line.trim().isEmpty()) {
                count++;
            }
        }
        return count;
    }

    public void saveAllData(String folderPath) throws IOException{
        try {
            Path basePath = resolveProjectBasePath(folderPath);
            ErrorLogger.setBasePath(basePath.toString());
            Path dataDir = basePath.resolve("output").resolve("data");
            String clientsFile = dataDir.resolve("clients.csv").toString();
            String tripsFile = dataDir.resolve("trips.csv").toString();
            String transportationsFile = dataDir.resolve("transportations.csv").toString();
            String accomodationsFile = dataDir.resolve("accomodations.csv").toString();

            resetOutputFolders(folderPath);
            ensureOutputDirectories(folderPath);

            ClientFileManager.saveClients(getClients(), countNonNull(getClients()), clientsFile);
            TransportFileManager.saveTransportations(getTransportations(),countNonNull(getTransportations()),
                transportationsFile);
            AccommodationFileManager.saveAccomodations(getAccomodations(), countNonNull(getAccomodations()), accomodationsFile);
            TripFileManager.saveTrips(getTrips(), countNonNull(getTrips()), tripsFile);

            System.out.println();
            System.out.println(">. Data saved successfully to output/data/*.csv.");
            System.out.println(">. Errors, if any, were logged to output/logs/errors.txt.");
            System.out.println();
        } catch (IOException e) {
            System.out.println(">. Error saving data: " + e.getMessage());
            System.out.println();
        }
    }

    public double calculateTripTotal(int index) throws InvalidAccommodationDataException, InvalidTransportDataException {
        if (trips == null || index < 0 || index >= trips.length || trips[index] == null)
            return 0.0;

        return trips[index].calculateTotalCost();
    }

    public static void testingScenario(boolean choice) throws InvalidClientDataException, InvalidTripDataException,
        InvalidTransportDataException, InvalidAccommodationDataException, EntityNotFoundException {
            
        if (choice) {
            clearInvalidPredefinedRows();
            clients = new Client[11];
            trips = new Trip[14];
            transportations = new Transportation[10];
            accomodations = new Accomodation[10];

            // Clients from clients.csv (C1008 row skipped: missing lastName field).
            tryLoadClient(0, "C1001", "Sophia", "Rossi", "sophia.rossi@italy.com");
            tryLoadClient(1, "C1002", "Carlos", "Silva", "carlos.silva@brazil.com");
            tryLoadClient(2, "C1003", "Aiko", "Tanaka", "aiko.tanaka@japan.com");
            tryLoadClient(3, "C1004", "Emma", "Wilson", "emma.wilson@usa.com");
            tryLoadClient(4, "C1005", "Miguel", "Gomez", "miguel.gomez@spain.com");
            tryLoadClient(5, "C1006", "John", "Smith", "john.smith@canada.com");
            tryLoadClient(6, "C1007", "Alice", "Johnson", "alice.johnson@uk.com");
            tryLoadClient(7, "C1008", "Bob Browm", "bob.brown@australia.com", "");
            tryLoadClient(8, "C1009", "Diana", "Prince", "diana.prince@greece.com");
            tryLoadClient(9, "C1010", "Lee", "Kim", "lee.kim@korea.com");
            tryLoadClient(10, "C1011", "Mig", "Gomez", "miguel.gomez@spain.com");

            // Transportation from transports.csv (CRUISE row skipped: unsupported type).
            tryLoadFlight(0, "TR3001", "Alitalia", "JFK", "FCO", 850.00, 23.0);
            tryLoadTrain(1, "TR3002", "Shinkansen", "Tokyo", "Kyoto", 250.00, "HighSpeed");
            tryLoadBus(2, "TR3003", "Greyhound", "NYC", "Boston", 75.00, 3);
            tryLoadFlight(3, "TR3004", "LATAM", "JFK", "GIG", 950.00, 32.0);
            tryLoadTrain(4, "TR3005", "Renfe", "Madrid", "Barcelona", 120.00, "AVE");
            tryLoadBus(5, "TR3006", "Blablacar", "Rome", "Milan", 45.00, 1);
            tryLoadFlight(6, "TR3007", "ANA", "JFK", "NRT", 1200.00, 32.0);
            tryLoadTrain(7, "TR3008", "Amtrak", "NYC", "DC", 89.00, "Acela");
            tryLoadFlight(8, "TR3009", "AirFrance", "YUL", "CDG", 750.00, 25.0);
            tryLoadBus(9, "TR3010", "VIA", "Toronto", "Montreal", 60.00, 2);

            // Accommodations from accommodations.csv (HOME row skipped: unsupported type).
            tryLoadHotel(0, "Hilton Rome", "Rome", 280.00, 4);
            tryLoadHostel(1, "Rome Backpackers", "Rome", 55.00, 6);
            tryLoadHotel(2, "Grand Rio", "Rio", 320.00, 5);
            tryLoadHotel(3, "Kyoto Imperial", "Kyoto", 410.00, 5);
            tryLoadHostel(4, "Tokyo Youth", "Tokyo", 60.00, 8);
            tryLoadHotel(5, "MGM Grand", "Las Vegas", 290.00, 4);
            tryLoadHotel(6, "W New York", "NYC", 450.00, 5);
            tryLoadHostel(7, "Barcelona Budget", "Barcelona", 48.00, 6);
            tryLoadHostel(8, "Paris Budget", "Paris", 52.00, 8);
            tryLoadHotel(9, "Tokyo Hilton", "Tokyo", 380.00, 4);

            tryLoadTrip(0, "T2001", "C1001", "A4001", "TR3001", "Paris", 5, 150.00);
            tryLoadTrip(1, "T2002", "C1005", "", "TR3002", "Paris", 3, 100.00);
            tryLoadTrip(2, "T2003", "C1002", "A4002", "TR3003", "Venice", 7, 200.00);
            tryLoadTrip(3, "T2004", "C1009", "A4003", "", "Paris", 4, 175.00);
            tryLoadTrip(4, "T2005", "C1006", "", "TR3004", "Rome", 6, 125.00);
            tryLoadTrip(5, "T2006", "C1003", "A4004", "TR3005", "Tokyo", 8, 225.00);
            tryLoadTrip(6, "T2007", "C1001", "A4005", "", "Paris", 10, 300.00);
            tryLoadTrip(7, "T2008", "C1004", "", "TR3006", "London", 5, 90.00);
            tryLoadTrip(8, "T2009", "C1007", "A4006", "TR3007", "Rome", 7, 275.00);
            tryLoadTrip(9, "T2010", "C1005", "A4003", "T5003", "Madrid", -2, 250.00);
            tryLoadTrip(10, "T2007", "C1006", "A4005", "T5005", "Rome", 5, 180.00);
            tryLoadTrip(11, "T2008", "C9999", "A4006", "T5006", "NYC", 8, 300.00);
            tryLoadTrip(12, "X6008", "C1007", "A4007", "T5007", "LA", 10, 400.00);
            tryLoadTrip(13, "T2011", "C1003", "A4104", "TR3005", "Tokyo", 8, 225.00);

            clientCount = countNonNull(clients);
            tripCount = countNonNull(trips);
            transportationCount = countNonNull(transportations);
            accommodationCount = countNonNull(accomodations);
        }
        else {
            clearInvalidPredefinedRows();
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

    private static void tryLoadClient(int index, String id, String firstName, String lastName, String email) {
        try {
            clients[index] = new Client(id, firstName, lastName, email);
        } catch (Exception e) {
            addInvalidClientRow(id + ";" + firstName + ";" + lastName + ";" + email + " [" + e.getMessage() + "]");

            // Keep a recoverable client entry so users can still find/edit it later.
            try {
                String safeFirstName = (firstName == null || firstName.trim().isEmpty()) ? "Unknown" : firstName;
                String safeLastName = (lastName == null || lastName.trim().isEmpty()) ? "Unknown" : lastName;
                String safeEmail = (email == null || email.trim().isEmpty())
                        ? ("missing." + (id == null ? "client" : id.toLowerCase()) + "@invalid.com")
                        : email;

                if (id != null && id.trim().matches("C\\d+"))
                    clients[index] = new Client(id.trim(), safeFirstName, safeLastName, safeEmail);
                else
                    clients[index] = new Client(safeFirstName, safeLastName, safeEmail);
            } catch (Exception ignored) {
                // If fallback creation also fails, keep the row only in invalid output logs.
            }
        }
    }

    private static void tryLoadFlight(int index, String id, String companyName, String departureCity,
                                      String arrivalCity, double baseFare, double luggageAllowance) {
        try {
            transportations[index] = new Flight(id, companyName, departureCity, arrivalCity, baseFare, luggageAllowance);
        } catch (Exception e) {
            addInvalidTransportationRow("FLIGHT;" + id + ";" + companyName + ";" + departureCity + ";"
                    + arrivalCity + ";" + baseFare + ";" + luggageAllowance + " [" + e.getMessage() + "]");
        }
    }

    private static void tryLoadTrain(int index, String id, String companyName, String departureCity,
                                     String arrivalCity, double baseFare, String seatClass) {
        try {
            transportations[index] = new Train(id, companyName, departureCity, arrivalCity, baseFare, seatClass);
        } catch (Exception e) {
            addInvalidTransportationRow("TRAIN;" + id + ";" + companyName + ";" + departureCity + ";"
                    + arrivalCity + ";" + baseFare + ";" + seatClass + " [" + e.getMessage() + "]");
        }
    }

    private static void tryLoadBus(int index, String id, String companyName, String departureCity,
                                   String arrivalCity, double baseFare, int numberOfStops) {
        try {
            transportations[index] = new Bus(id, companyName, departureCity, arrivalCity, baseFare, numberOfStops);
        } catch (Exception e) {
            addInvalidTransportationRow("BUS;" + id + ";" + companyName + ";" + departureCity + ";"
                    + arrivalCity + ";" + baseFare + ";" + numberOfStops + " [" + e.getMessage() + "]");
        }
    }

    private static void tryLoadHotel(int index, String name, String location, double pricePerNight, int starRating) {
        try {
            accomodations[index] = new Hotel(name, location, pricePerNight, starRating);
        } catch (Exception e) {
            addInvalidAccomodationRow("HOTEL;" + name + ";" + location + ";" + pricePerNight + ";"
                    + starRating + " [" + e.getMessage() + "]");
        }
    }

    private static void tryLoadHostel(int index, String name, String location, double pricePerNight, int sharedBeds) {
        try {
            accomodations[index] = new Hostel(name, location, pricePerNight, sharedBeds);
        } catch (Exception e) {
            addInvalidAccomodationRow("HOSTEL;" + name + ";" + location + ";" + pricePerNight + ";"
                    + sharedBeds + " [" + e.getMessage() + "]");
        }
    }

    private static void tryLoadTrip(int index, String tripId, String clientId, String accomodationId,
                                    String transportationId, String destination, int duration, double basePrice) {
        try {
            trips[index] = new Trip(tripId, clientId, accomodationId, transportationId, destination, duration, basePrice);
        } catch (Exception e) {
            addInvalidTripRow(tripId + ";" + clientId + ";" + accomodationId + ";" + transportationId + ";"
                    + destination + ";" + duration + ";" + basePrice + " [" + e.getMessage() + "]");
        }
    }

    private static void clearInvalidPredefinedRows() {
        invalidClientRows = new String[20];
        invalidTripRows = new String[30];
        invalidTransportationRows = new String[20];
        invalidAccomodationRows = new String[20];
        invalidClientCount = 0;
        invalidTripCount = 0;
        invalidTransportationCount = 0;
        invalidAccomodationCount = 0;
    }

    private static void addInvalidClientRow(String row) {
        if (invalidClientCount < invalidClientRows.length)
            invalidClientRows[invalidClientCount++] = row;
    }

    private static void addInvalidTripRow(String row) {
        if (invalidTripCount < invalidTripRows.length)
            invalidTripRows[invalidTripCount++] = row;
    }

    private static void addInvalidTransportationRow(String row) {
        if (invalidTransportationCount < invalidTransportationRows.length)
            invalidTransportationRows[invalidTransportationCount++] = row;
    }

    private static void addInvalidAccomodationRow(String row) {
        if (invalidAccomodationCount < invalidAccomodationRows.length)
            invalidAccomodationRows[invalidAccomodationCount++] = row;
    }

    public static void clearInvalidClientById(String clientId) {
        if (clientId == null || clientId.trim().isEmpty())
            return;

        int write = 0;
        for (int i = 0; i < invalidClientCount; i++) {
            String row = invalidClientRows[i];
            if (row == null || !row.startsWith(clientId + ";")) {
                invalidClientRows[write++] = row;
            }
        }

        for (int i = write; i < invalidClientRows.length; i++)
            invalidClientRows[i] = null;
        invalidClientCount = write;
    }

    public static void clearInvalidTripById(String tripId) {
        if (tripId == null || tripId.trim().isEmpty())
            return;

        int write = 0;
        for (int i = 0; i < invalidTripCount; i++) {
            String row = invalidTripRows[i];
            if (row == null || !row.startsWith(tripId + ";")) {
                invalidTripRows[write++] = row;
            }
        }

        for (int i = write; i < invalidTripRows.length; i++)
            invalidTripRows[i] = null;
        invalidTripCount = write;
    }

    public static void clearInvalidTransportationById(String transportId) {
        if (transportId == null || transportId.trim().isEmpty())
            return;

        int write = 0;
        for (int i = 0; i < invalidTransportationCount; i++) {
            String row = invalidTransportationRows[i];
            if (row == null || !row.contains(";" + transportId + ";")) {
                invalidTransportationRows[write++] = row;
            }
        }

        for (int i = write; i < invalidTransportationRows.length; i++)
            invalidTransportationRows[i] = null;
        invalidTransportationCount = write;
    }

    public static void clearInvalidAccomodationById(String accomodationId) {
        if (accomodationId == null || accomodationId.trim().isEmpty())
            return;

        int write = 0;
        for (int i = 0; i < invalidAccomodationCount; i++) {
            String row = invalidAccomodationRows[i];
            if (row == null || !row.contains(accomodationId + ";")) {
                invalidAccomodationRows[write++] = row;
            }
        }

        for (int i = write; i < invalidAccomodationRows.length; i++)
            invalidAccomodationRows[i] = null;
        invalidAccomodationCount = write;
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
        String printString = "";
        for (Client person : clients) {
            if (person != null)
                printString += (">. " + person.toString() + "\n");
        }

        return (printString + "\n");
    }

    /**
     * Prints all trips in the system to console.
     * Iterates through trip array and displays each trip's toString() representation.
     * Each trip displays its ID, destination, duration, base price, and associated client.
     */
    public static String printTrips() {
        String printString = "";
        for (Trip trip : trips) {
            if (trip != null)
                printString += (">. " + trip.toString()+ "\n");
        }

        return (printString + "\n");
    }

    /**
     * Prints all transportation options in the system to console.
     * Displays transportation entries if available, or a message if the list is empty.
     * Demonstrates polymorphism: calls toString() on base-class references (Flight, Train, Bus).
     */
    public static String printTransportations() {
        String printString = "";
        if (transportations.length == 0)
            printString += ">. No transportation options available.";
        else {
            // Iterate through array and display each non-null transportation option
            for (Transportation t : transportations) {
                if (t != null) {
                    // Polymorphic call: actual subclass toString() is invoked
                    printString += (">. " + t.toString() + "\n");
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
        String printString = "";
        if (accomodations.length == 0) {
            printString = ">. No accomodations available.";
        } else {
            // Iterate through array and display each non-null accommodation option
            for (Accomodation a : accomodations) {
                if (a != null) {
                    // Polymorphic call: actual subclass toString() is invoked
                    printString += (">. " + a.toString() + "\n");
                }
            }
        }

        return (printString + "\n");
    }
}
