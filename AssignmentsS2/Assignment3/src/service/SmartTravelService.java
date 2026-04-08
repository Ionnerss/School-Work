package AssignmentsS2.Assignment3.src.service;

/*
 * Assignment 3
 * Question: Full SmartTravel Data Management Project
 * Written by: Catalin-Ion Besleaga (40347936)
 *
 * This service stores SmartTravel data with ArrayList collections, mirrors
 * queryable data into repositories, preserves invalid predefined rows for
 * display, performs generic file persistence, and keeps A2 behaviour working.
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import AssignmentsS2.Assignment3.src.client.Client;
import AssignmentsS2.Assignment3.src.exceptions.*;
import AssignmentsS2.Assignment3.src.persistence.ErrorLogger;
import AssignmentsS2.Assignment3.src.persistence.GenericFileManager;
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

    private static final Repository<Client> clientRepo = new Repository<>();
    private static final Repository<Trip> tripRepo = new Repository<>();
    private static final Repository<Accommodation> accommodationRepo = new Repository<>();
    private static final Repository<Transportation> transportationRepo = new Repository<>();

    private static String clientId;
    private static String accommodationId;
    private static String transportationId;
    private static String destination;
    private static int durationDays;
    private static double basePrice;

    private static final List<String> invalidClientRows = new ArrayList<>();
    private static final List<String> invalidTripRows = new ArrayList<>();
    private static final List<String> invalidTransportationRows = new ArrayList<>();
    private static final List<String> invalidAccomodationRows = new ArrayList<>();

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
        rebuildClientRepo();
        refreshClientAmountsSpent();
    }

    public void setTrips(List<Trip> updatedTrips) {
        replaceCollection(trips, updatedTrips, MAX_TRIPS, "trips");
        rebuildTripRepo();
        refreshClientAmountsSpent();
    }

    public void setTransportations(List<Transportation> updatedTransportations) {
        replaceCollection(transportations, updatedTransportations, MAX_TRANSPORTATIONS, "transportations");
        rebuildTransportationRepo();
        refreshClientAmountsSpent();
    }

    public void setAccomodations(List<Accommodation> updatedAccomodations) {
        replaceCollection(accommodations, updatedAccomodations, MAX_ACCOMMODATIONS, "accommodations");
        rebuildAccommodationRepo();
        refreshClientAmountsSpent();
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

    private static void rebuildClientRepo() {
        clientRepo.clear();
        clientRepo.addAll(clients);
    }

    private static void rebuildTripRepo() {
        tripRepo.clear();
        tripRepo.addAll(trips);
    }

    private static void rebuildAccommodationRepo() {
        accommodationRepo.clear();
        accommodationRepo.addAll(accommodations);
    }

    private static void rebuildTransportationRepo() {
        transportationRepo.clear();
        transportationRepo.addAll(transportations);
    }

    private static void rebuildAllRepos() {
        rebuildClientRepo();
        rebuildTripRepo();
        rebuildAccommodationRepo();
        rebuildTransportationRepo();
    }

    public Client getClient(int index) {
        return getFromList(clients, index);
    }

    public Trip getTrip(int index) {
        return getFromList(trips, index);
    }

    public Transportation getTransportation(int index) {
        return getFromList(transportations, index);
    }

    public Accommodation getAccommodation(int index) {
        return getFromList(accommodations, index);
    }

    private <T> T getFromList(List<T> list, int index) {
        if (index < 0 || index >= list.size()) {
            return null;
        }

        return list.get(index);
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

    public int getClientIndexById(String id) {
        return indexOfId(clients, id);
    }

    public int getTripIndexById(String id) {
        return indexOfId(trips, id);
    }

    public int getTransportationIndexById(String id) {
        return indexOfId(transportations, id);
    }

    public int getAccommodationIndexById(String id) {
        return indexOfId(accommodations, id);
    }

    private static <T extends AssignmentsS2.Assignment3.src.interfaces.Identifiable> int indexOfId(List<T> items, String id) {
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
        if (index < 0) {
            return false;
        }

        clients.remove(index);
        rebuildClientRepo();
        refreshClientAmountsSpent();
        return true;
    }

    public boolean removeTripById(String id) {
        int index = getTripIndexById(id);
        if (index < 0) {
            return false;
        }

        trips.remove(index);
        rebuildTripRepo();
        refreshClientAmountsSpent();
        return true;
    }

    public boolean removeTransportationById(String id) {
        int index = getTransportationIndexById(id);
        if (index < 0) {
            return false;
        }

        transportations.remove(index);
        rebuildTransportationRepo();
        refreshClientAmountsSpent();
        return true;
    }

    public boolean removeAccommodationById(String id) {
        int index = getAccommodationIndexById(id);
        if (index < 0) {
            return false;
        }

        accommodations.remove(index);
        rebuildAccommodationRepo();
        refreshClientAmountsSpent();
        return true;
    }

    public static void addClient(String firstName, String lastName, String email) throws InvalidClientDataException {
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
        clientRepo.add(newClient);
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

    public static void addTransportation(Transportation transportation) {
        if (transportation == null) {
            throw new IllegalArgumentException("Transportation cannot be null.");
        }

        if (transportations.size() >= MAX_TRANSPORTATIONS) {
            throw new IllegalStateException("Transportation list is full. Max is " + MAX_TRANSPORTATIONS + ".");
        }

        transportations.add(transportation);
        transportationRepo.add(transportation);
        refreshClientAmountsSpent();
    }

    public static void addAccommodation(Accommodation accommodation) {
        if (accommodation == null) {
            throw new IllegalArgumentException("Accommodation cannot be null.");
        }

        if (accommodations.size() >= MAX_ACCOMMODATIONS) {
            throw new IllegalStateException("Accommodation list is full. Max is " + MAX_ACCOMMODATIONS + ".");
        }

        accommodations.add(accommodation);
        accommodationRepo.add(accommodation);
        refreshClientAmountsSpent();
    }

    public static Trip createTrip() throws InvalidTripDataException, EntityNotFoundException {
        String normalizedAccommodationId = normalize(accommodationId);
        String normalizedTransportationId = normalize(transportationId);

        if (normalizedAccommodationId == null && normalizedTransportationId == null) {
            return createTrip(destination, durationDays, basePrice, clientId, "", "");
        }

        return createTrip(destination, durationDays, basePrice, clientId,
                normalizedAccommodationId, normalizedTransportationId);
    }

    public static Trip createTrip(String destination, int durationDays, double basePrice, String clientId,
                                  String accommodationId, String transportationId)
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
            throw new InvalidTripDataException("At least one of accommodation ID or transportation ID must be provided.");
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

        Trip newTrip = new Trip(client.getId(), finalAccommodationId, finalTransportationId,
                normalizedDestination, durationDays, basePrice);
        storeTrip(newTrip);
        return newTrip;
    }

    private static void storeTrip(Trip trip) {
        if (trip == null) {
            return;
        }

        if (trips.size() >= MAX_TRIPS) {
            throw new IllegalStateException("Trip list is full. Max is " + MAX_TRIPS + ".");
        }

        trips.add(trip);
        tripRepo.add(trip);
        refreshClientAmountsSpent();
    }

    public static Accommodation findAccommodationById(String accommodationId) throws EntityNotFoundException {
        String normalizedAccommodationId = normalize(accommodationId);

        if (normalizedAccommodationId == null || !normalizedAccommodationId.matches("A\\d+")) {
            throw new EntityNotFoundException("Accommodation ID not found: " + accommodationId);
        }

        return accommodationRepo.findById(normalizedAccommodationId);
    }

    public static Transportation findTransportationById(String transportationId) throws EntityNotFoundException {
        String normalizedTransportationId = normalize(transportationId);

        if (normalizedTransportationId == null || !normalizedTransportationId.matches("TR\\d+")) {
            throw new EntityNotFoundException("Transportation ID not found: " + transportationId);
        }

        return transportationRepo.findById(normalizedTransportationId);
    }

    public static Client findClientByIdObj(String clientId) throws EntityNotFoundException {
        String normalizedClientId = normalize(clientId);

        if (normalizedClientId == null || !normalizedClientId.matches("C\\d+")) {
            throw new EntityNotFoundException("Client ID not found: " + clientId);
        }

        return clientRepo.findById(normalizedClientId);
    }

    public static boolean findClientbyIdBool(String clientId) {
        if (clientId == null || clientId.trim().isEmpty()) {
            throw new IllegalArgumentException("Client ID cannot be empty.");
        }

        String trimmed = clientId.trim();

        if (!trimmed.matches("C\\d+")) {
            throw new IllegalArgumentException("Invalid Client ID format: " + clientId);
        }

        for (Client client : clients) {
            if (client != null && trimmed.equals(client.getId())) {
                return true;
            }
        }

        return false;
    }

    public static boolean clientExists(String clientId) throws InvalidClientDataException {
        if (clientId == null || clientId.trim().isEmpty()) {
            throw new InvalidClientDataException("Client ID cannot be empty.");
        }

        String trimmed = clientId.trim();

        if (!trimmed.matches("C\\d+")) {
            throw new InvalidClientDataException("Invalid Client ID format: " + clientId);
        }

        for (Client client : clients) {
            if (client != null && trimmed.equals(client.getId())) {
                return true;
            }
        }

        return false;
    }

    public List<Trip> filterTrips(Predicate<Trip> predicate) {
        return tripRepo.filter(predicate);
    }

    public List<Client> getSortedClients() {
        return clientRepo.getSorted();
    }

    public List<Trip> getSortedTrips() {
        return tripRepo.getSorted();
    }

    public List<Transportation> getSortedTransportations() {
        return transportationRepo.getSorted();
    }

    public List<Accommodation> getSortedAccommodations() {
        return accommodationRepo.getSorted();
    }

    private static String normalize(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static boolean emailAlreadyExists(String email, String excludedClientId) {
        if (email == null) {
            return false;
        }

        for (Client client : clients) {
            if (client == null) {
                continue;
            }

            if (excludedClientId != null && excludedClientId.equals(client.getId())) {
                continue;
            }

            if (email.equalsIgnoreCase(client.getEmailAdress())) {
                return true;
            }
        }

        return false;
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
                // Ignore broken links while rebuilding stored totals.
            }
        }
    }

    public static int getInvalidClientCount() {
        return invalidClientRows.size();
    }

    public static String getInvalidClientRow(int index) {
        return getInvalidRow(invalidClientRows, index);
    }

    public static int getInvalidTripCount() {
        return invalidTripRows.size();
    }

    public static String getInvalidTripRow(int index) {
        return getInvalidRow(invalidTripRows, index);
    }

    public static int getInvalidTransportationCount() {
        return invalidTransportationRows.size();
    }

    public static String getInvalidTransportationRow(int index) {
        return getInvalidRow(invalidTransportationRows, index);
    }

    public static int getInvalidAccomodationCount() {
        return invalidAccomodationRows.size();
    }

    public static String getInvalidAccomodationRow(int index) {
        return getInvalidRow(invalidAccomodationRows, index);
    }

    private static String getInvalidRow(List<String> rows, int index) {
        if (index < 0 || index >= rows.size()) {
            return null;
        }

        return rows.get(index);
    }

    public static void clearInvalidClientById(String clientId) {
        clearInvalidByPrefix(invalidClientRows, clientId);
    }

    public static void clearInvalidTripById(String tripId) {
        clearInvalidByPrefix(invalidTripRows, tripId);
    }

    public static void clearInvalidTransportationById(String transportationId) {
        if (transportationId == null || transportationId.trim().isEmpty()) {
            return;
        }

        clearInvalidByContains(invalidTransportationRows, ";" + transportationId.trim() + ";");
    }

    public static void clearInvalidAccomodationById(String accommodationId) {
        if (accommodationId == null || accommodationId.trim().isEmpty()) {
            return;
        }

        clearInvalidByContains(invalidAccomodationRows, accommodationId.trim() + ";");
    }

    private static void clearInvalidByPrefix(List<String> rows, String id) {
        if (id == null || id.trim().isEmpty()) {
            return;
        }

        String prefix = id.trim() + ";";

        for (int i = rows.size() - 1; i >= 0; i--) {
            String row = rows.get(i);
            if (row != null && row.startsWith(prefix)) {
                rows.remove(i);
            }
        }
    }

    private static void clearInvalidByContains(List<String> rows, String token) {
        for (int i = rows.size() - 1; i >= 0; i--) {
            String row = rows.get(i);
            if (row != null && row.contains(token)) {
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

    private static void addInvalidClientRow(String row) {
        if (row != null) {
            invalidClientRows.add(row);
        }
    }

    private static void addInvalidTripRow(String row) {
        if (row != null) {
            invalidTripRows.add(row);
        }
    }

    private static void addInvalidTransportationRow(String row) {
        if (row != null) {
            invalidTransportationRows.add(row);
        }
    }

    private static void addInvalidAccomodationRow(String row) {
        if (row != null) {
            invalidAccomodationRows.add(row);
        }
    }

    public static String printClients() {
        return printCollection(clients, "clients");
    }

    public static String printTrips() {
        return printCollection(trips, "trips");
    }

    public static String printTransportations() {
        return printCollection(transportations, "transportation options");
    }

    public static String printAccomodations() {
        return printCollection(accommodations, "accommodations");
    }

    private static String printCollection(List<?> items, String emptyLabel) {
        StringBuilder output = new StringBuilder();

        if (items == null || items.isEmpty()) {
            output.append(">. No ").append(emptyLabel).append(" available.");
            return output.toString();
        }

        for (Object item : items) {
            if (item != null) {
                output.append(">. ").append(item).append("");
            }
        }

        output.append("");
        return output.toString();
    }

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
            clearInvalidPredefinedRows();

            setClients(GenericFileManager.load(clientsFile, Client.class));
            setTransportations(GenericFileManager.load(transportationsFile, Transportation.class));
            setAccomodations(GenericFileManager.load(accomodationsFile, Accommodation.class));
            setTrips(GenericFileManager.load(tripsFile, Trip.class));

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

            GenericFileManager.save(clients, clientsFile);
            appendInvalidRows(clientsFile, "INVALID_PREDEFINED_CLIENT;", invalidClientRows);

            GenericFileManager.save(transportations, transportationsFile);
            appendInvalidRows(transportationsFile, "INVALID_PREDEFINED_TRANSPORT;", invalidTransportationRows);

            GenericFileManager.save(accommodations, accomodationsFile);
            appendInvalidRows(accomodationsFile, "INVALID_PREDEFINED_ACCOMODATION;", invalidAccomodationRows);

            GenericFileManager.save(trips, tripsFile);
            appendInvalidRows(tripsFile, "INVALID_PREDEFINED_TRIP;", invalidTripRows);

            System.out.println();
            System.out.println(">. Data saved successfully to output/data/*.csv.");
            System.out.println(">. Errors, if any, were logged to output/logs/errors.txt.");
            System.out.println();
        } catch (IOException e) {
            System.out.println(">. Error saving data: " + e.getMessage());
            System.out.println();
        }
    }

    private void appendInvalidRows(String filepath, String prefix, List<String> rows) throws IOException {
        if (rows == null || rows.isEmpty()) {
            return;
        }

        try (PrintWriter outputStream = new PrintWriter(new BufferedWriter(new FileWriter(filepath, true)))) {
            for (String row : rows) {
                if (row != null) {
                    outputStream.println(prefix + row);
                }
            }
        }
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
        Files.createDirectories(basePath.resolve("output").resolve("charts"));
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
        deleteDirectoryContents(outputPath.resolve("charts"));
    }

    private void deleteDirectoryContents(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            return;
        }

        try (java.util.stream.Stream<Path> stream = Files.list(directory)) {
            java.util.Iterator<Path> iterator = stream.iterator();

            while (iterator.hasNext()) {
                Path child = iterator.next();
                if (Files.isDirectory(child)) {
                    deleteDirectoryContents(child);
                    Files.deleteIfExists(child);
                } else {
                    Files.deleteIfExists(child);
                }
            }
        }
    }

    public double calculateTripTotal(int index) throws EntityNotFoundException {
        if (index < 0 || index >= trips.size() || trips.get(index) == null) {
            return 0.0;
        }

        return trips.get(index).calculateTotalCost();
    }

    private void syncIdsFromCurrentData() {
        int maxClient = -1;
        int maxTrip = -1;
        int maxTransportation = -1;
        int maxAccommodation = -1;

        for (Client client : clients) {
            if (client != null) {
                int current = extractNumericId(client.getId());
                if (current > maxClient) {
                    maxClient = current;
                }
            }
        }

        for (Trip trip : trips) {
            if (trip != null) {
                int current = extractNumericId(trip.getId());
                if (current > maxTrip) {
                    maxTrip = current;
                }
            }
        }

        for (Transportation transportation : transportations) {
            if (transportation != null) {
                int current = extractNumericId(transportation.getId());
                if (current > maxTransportation) {
                    maxTransportation = current;
                }
            }
        }

        for (Accommodation accommodation : accommodations) {
            if (accommodation != null) {
                int current = extractNumericId(accommodation.getId());
                if (current > maxAccommodation) {
                    maxAccommodation = current;
                }
            }
        }

        if (maxClient >= 0) {
            Client.syncNextId(maxClient + 1);
        }
        if (maxTrip >= 0) {
            Trip.syncNextId(maxTrip + 1);
        }
        if (maxTransportation >= 0) {
            Transportation.syncNextId(maxTransportation + 1);
        }
        if (maxAccommodation >= 0) {
            Accommodation.syncNextId(maxAccommodation + 1);
        }
    }

    private int extractNumericId(String id) {
        if (id == null) {
            return -1;
        }

        int index = 0;
        while (index < id.length() && !Character.isDigit(id.charAt(index))) {
            index++;
        }

        if (index >= id.length()) {
            return -1;
        }

        try {
            return Integer.parseInt(id.substring(index));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void testingScenario(boolean choice)
            throws InvalidClientDataException, InvalidTripDataException,
            InvalidTransportDataException, InvalidAccommodationDataException,
            EntityNotFoundException {

        clearInvalidPredefinedRows();
        clients.clear();
        trips.clear();
        transportations.clear();
        accommodations.clear();
        rebuildAllRepos();

        if (!choice) {
            return;
        }

        tryLoadClient("C1001", "Sophia", "Rossi", "sophia.rossi@italy.com");
        tryLoadClient("C1002", "Carlos", "Silva", "carlos.silva@brazil.com");
        tryLoadClient("C1003", "Aiko", "Tanaka", "aiko.tanaka@japan.com");
        tryLoadClient("C1004", "Emma", "Wilson", "emma.wilson@usa.com");
        tryLoadClient("C1005", "Miguel", "Gomez", "miguel.gomez@spain.com");
        tryLoadClient("C1006", "John", "Smith", "john.smith@canada.com");
        tryLoadClient("C1007", "Alice", "Johnson", "alice.johnson@uk.com");
        tryLoadClient("C1008", "Bob Browm", "", "bob.brown@australia.com");
        tryLoadClient("C1009", "Diana", "Prince", "diana.prince@greece.com");
        tryLoadClient("C1010", "Lee", "Kim", "lee.kim@korea.com");
        tryLoadClient("C1011", "Mig", "Gomez", "miguel.gomez@spain.com");

        tryLoadTransportation(new Flight("TR3001", "Alitalia", "JFK", "FCO", 850.00, 23.0), null);
        tryLoadTransportation(new Train("TR3002", "Shinkansen", "Tokyo", "Kyoto", 250.00, "HighSpeed"), null);
        tryLoadTransportation(new Bus("TR3003", "Greyhound", "NYC", "Boston", 75.00, 3), null);
        tryLoadTransportation(new Flight("TR3004", "LATAM", "JFK", "GIG", 950.00, 32.0), null);
        tryLoadTransportation(new Train("TR3005", "Renfe", "Madrid", "Barcelona", 120.00, "AVE"), null);
        tryLoadTransportation(new Bus("TR3006", "Blablacar", "Rome", "Milan", 45.00, 1), null);
        tryLoadTransportation(new Flight("TR3007", "ANA", "JFK", "NRT", 1200.00, 32.0), null);
        tryLoadTransportation(new Train("TR3008", "Amtrak", "NYC", "DC", 89.00, "Acela"), null);
        tryLoadTransportation(new Flight("TR3009", "AirFrance", "YUL", "CDG", 750.00, 25.0), null);
        tryLoadTransportation(new Bus("TR3010", "VIA", "Toronto", "Montreal", 60.00, 2), null);

        tryLoadAccommodation(new Hotel("Hilton Rome", "Rome", 280.00, 4), null);
        tryLoadAccommodation(new Hostel("Rome Backpackers", "Rome", 55.00, 6), null);
        tryLoadAccommodation(new Hotel("Grand Rio", "Rio", 320.00, 5), null);
        tryLoadAccommodation(new Hotel("Kyoto Imperial", "Kyoto", 410.00, 5), null);
        tryLoadAccommodation(new Hostel("Tokyo Youth", "Tokyo", 60.00, 8), null);
        tryLoadAccommodation(new Hotel("MGM Grand", "Las Vegas", 290.00, 4), null);
        tryLoadAccommodation(new Hotel("W New York", "NYC", 450.00, 5), null);
        tryLoadAccommodation(new Hostel("Barcelona Budget", "Barcelona", 48.00, 6), null);
        tryLoadAccommodation(new Hostel("Paris Budget", "Paris", 52.00, 8), null);
        tryLoadAccommodation(new Hotel("Tokyo Hilton", "Tokyo", 380.00, 4), null);

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

        syncScenarioIds();
        refreshClientAmountsSpent();
    }

    private static void tryLoadClient(String id, String firstName, String lastName, String email) {
        try {
            Client client = new Client(id, firstName, lastName, email);

            if (emailAlreadyExists(client.getEmailAdress(), null)) {
                throw new DuplicateEmailException("A client with this email already exists.");
            }

            if (clients.size() >= MAX_CLIENTS) {
                throw new IllegalStateException("Client list is full.");
            }

            clients.add(client);
            clientRepo.add(client);
        } catch (Exception e) {
            addInvalidClientRow(id + ";" + firstName + ";" + lastName + ";" + email + " [" + e.getMessage() + "]");

            try {
                String safeFirstName = (firstName == null || firstName.trim().isEmpty()) ? "Unknown" : firstName;
                String safeLastName = (lastName == null || lastName.trim().isEmpty()) ? "Unknown" : lastName;
                String safeEmail = (email == null || email.trim().isEmpty())
                        ? ("missing." + (id == null ? "client" : id.toLowerCase()) + "@invalid.com")
                        : email;

                Client fallback;
                if (id != null && id.trim().matches("C\\d+")) {
                    fallback = new Client(id.trim(), safeFirstName, safeLastName, safeEmail);
                } else {
                    fallback = new Client(safeFirstName, safeLastName, safeEmail);
                }

                if (!emailAlreadyExists(fallback.getEmailAdress(), null) && clients.size() < MAX_CLIENTS) {
                    clients.add(fallback);
                    clientRepo.add(fallback);
                }
            } catch (Exception ignored) {
                // Keep only the invalid-row record if fallback creation also fails.
            }
        }
    }

    private static void tryLoadTransportation(Transportation transportation, String originalRow) {
        try {
            addTransportation(transportation);
        } catch (Exception e) {
            if (originalRow == null) {
                originalRow = transportation == null ? "null" : transportation.toString();
            }
            addInvalidTransportationRow(originalRow + " [" + e.getMessage() + "]");
        }
    }

    private static void tryLoadAccommodation(Accommodation accommodation, String originalRow) {
        try {
            addAccommodation(accommodation);
        } catch (Exception e) {
            if (originalRow == null) {
                originalRow = accommodation == null ? "null" : accommodation.toString();
            }
            addInvalidAccomodationRow(originalRow + " [" + e.getMessage() + "]");
        }
    }

    private static void tryLoadTrip(String tripId, String clientId, String accommodationId,
                                    String transportationId, String destination,
                                    int duration, double basePrice) {
        try {
            Trip trip = new Trip(tripId, clientId, accommodationId, transportationId, destination, duration, basePrice);
            if (trips.size() >= MAX_TRIPS) {
                throw new IllegalStateException("Trip list is full.");
            }
            trips.add(trip);
            tripRepo.add(trip);
        } catch (Exception e) {
            addInvalidTripRow(tripId + ";" + clientId + ";" + accommodationId + ";" + transportationId + ";"
                    + destination + ";" + duration + ";" + basePrice + " [" + e.getMessage() + "]");
        }
    }

    private static void syncScenarioIds() {
        int maxClient = 0;
        int maxTrip = 0;
        int maxTransport = 0;
        int maxAccommodation = 0;

        for (Client client : clients) {
            int current = extractNumericIdStatic(client == null ? null : client.getId());
            if (current > maxClient) {
                maxClient = current;
            }
        }

        for (Trip trip : trips) {
            int current = extractNumericIdStatic(trip == null ? null : trip.getId());
            if (current > maxTrip) {
                maxTrip = current;
            }
        }

        for (Transportation transportation : transportations) {
            int current = extractNumericIdStatic(transportation == null ? null : transportation.getId());
            if (current > maxTransport) {
                maxTransport = current;
            }
        }

        for (Accommodation accommodation : accommodations) {
            int current = extractNumericIdStatic(accommodation == null ? null : accommodation.getId());
            if (current > maxAccommodation) {
                maxAccommodation = current;
            }
        }

        if (maxClient > 0) {
            Client.syncNextId(maxClient + 1);
        }
        if (maxTrip > 0) {
            Trip.syncNextId(maxTrip + 1);
        }
        if (maxTransport > 0) {
            Transportation.syncNextId(maxTransport + 1);
        }
        if (maxAccommodation > 0) {
            Accommodation.syncNextId(maxAccommodation + 1);
        }
    }

    private static int extractNumericIdStatic(String id) {
        if (id == null) {
            return -1;
        }

        int index = 0;
        while (index < id.length() && !Character.isDigit(id.charAt(index))) {
            index++;
        }

        if (index >= id.length()) {
            return -1;
        }

        try {
            return Integer.parseInt(id.substring(index));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
