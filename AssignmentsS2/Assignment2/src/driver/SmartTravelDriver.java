package AssignmentsS2.Assignment2.src.driver;
// --------------------------------------------------------
// Assignment 1 - Main driver
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S - Fall 2025
// --------------------------------------------------------

import AssignmentsS2.Assignment2.src.client.Client;
import AssignmentsS2.Assignment2.src.exceptions.*;
import AssignmentsS2.Assignment2.src.service.SmartTravelService;
import AssignmentsS2.Assignment2.src.travel.*;
import AssignmentsS2.Assignment2.src.visualization.DashboardGenerator;

import java.io.IOException;
import java.util.Scanner;

public class SmartTravelDriver {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SmartTravelService service = new SmartTravelService();

    private static boolean backToMain;
    private static boolean backToSubmenu;
    private static int choice;

    public static void main(String[] args) throws IOException {

        System.out.println("Welcome to the SmartTravel Program!");
        System.out.println();

        choice = readInt("Would you like to continue with a predefined scenario or no (TESTING) (1 = yes, 2 = no)? ");

        boolean loadedPredefinedScenario = false;
        try {
            if (choice == 1) {
                SmartTravelService.testingScenario(true);
                loadedPredefinedScenario = true;
            } else {
                if (choice != 2) {
                    System.out.println("Option invalid. Going default route.");
                }
                SmartTravelService.testingScenario(false);
            }
        } catch (Exception e) {
            System.out.println(">. Unable to load the predefined scenario: " + e.getMessage());
            System.out.println(">. Continuing with partial data that was loaded before the error.");
            loadedPredefinedScenario = true;
        }

        if (loadedPredefinedScenario) {
            printPreloadedData();
        }

        backToMain = true;
        while (backToMain) {
            System.out.print("""
                What would you like to access?
                1. Client Management
                2. Trip Management
                3. Transportation Management
                4. Accommodation Management
                5. Additional Operations
                6. Exit Program
                7. List All Data Summary
                8. Load All Data
                9. Save All Data
                10. Run Predefined Scenario
                11. Generate Dashboard
                0. Exit

                >. Please enter option: """);

            choice = readInt("");
            System.out.println();

            switch (choice) {
                case 1 -> backToMain = clientManagement();
                case 2 -> backToMain = tripManagement();
                case 3 -> backToMain = transportManagement();
                case 4 -> backToMain = accommodationManagement();
                case 5 -> backToMain = additionalOperations();
                case 7 -> {
                    listAllDataSummary();
                    backToMain = true;
                }
                case 8 -> {
                    service.loadAllData(null);
                    backToMain = true;
                }
                case 9 -> {
                    service.saveAllData(null);;
                    backToMain = true;
                }
                case 10 -> {
                    runPredefinedScenario();
                    backToMain = true;
                }
                case 11 -> {
                    generateDashboard();
                    backToMain = true;
                }
                case 0 -> backToMain = false;
                case 6 -> backToMain = false;
                default -> {
                    System.out.println(">. Invalid option. Please try again.");
                    backToMain = true;
                }
            }
        }

        scanner.close();
        System.out.println();
        System.out.println("Program Termination Successful!");
    }

    private static boolean clientManagement() {
        do {
            backToSubmenu = true;

            System.out.println("""
                    What would you like to do?
                    1. Add a client
                    2. Edit a client
                    3. Delete a client
                    4. List all clients
                    5. Exit
                    >. Please enter option: """);

            choice = readInt("");

            switch (choice) {
                case 1 -> addClient();
                case 2 -> editClient();
                case 3 -> deleteClient();
                case 4 -> System.out.println(printAllClientsIncludingInvalid());
                case 5 -> backToSubmenu = false;
                default -> System.out.println(">. Please reenter an option.\n");
            }
        } while (backToSubmenu);

        return true;
    }

    private static void addClient() {
        System.out.println(">. Please enter client details as follows: first name, last name, email address:");
        scanner.nextLine();

        System.out.print(">. First Name: ");
        String firstName = scanner.nextLine();

        System.out.print(">. Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print(">. Email Address: ");
        String emailAddress = scanner.nextLine();

        try {
            Client[] clients = service.getClients();
            Client[] updated = new Client[clients.length + 1];
            System.arraycopy(clients, 0, updated, 0, clients.length);
            updated[clients.length] = new Client(firstName, lastName, emailAddress);
            service.setClients(updated);

            System.out.println();
            System.out.println(">. Client added successfully.");
            System.out.println();
        } catch (InvalidClientDataException e) {
            System.out.println(">. Error adding client: " + e.getMessage());
            System.out.println();
        }
    }

    private static void editClient() {
        System.out.println(printAllClientsIncludingInvalid());
        if (!hasAnyClient()) {
            System.out.println(">. No clients in list.\n");
            return;
        }

        System.out.print(">. Please enter client ID whose information you would like to update: ");
        String clientID = scanner.next();

        int foundIndex = clientExistCheck(clientID);
        if (foundIndex < 0) {
            System.out.println(">. Client not found.\n");
            return;
        }

        scanner.nextLine();

        System.out.print(">. Updated first name: ");
        String firstName = scanner.nextLine();

        System.out.print(">. Updated last name: ");
        String lastName = scanner.nextLine();

        System.out.print(">. Updated email address: ");
        String emailAddress = scanner.nextLine();

        try {
            Client[] clients = service.getClients();
            clients[foundIndex].setFirstName(firstName);
            clients[foundIndex].setLastName(lastName);
            clients[foundIndex].setEmailAdress(emailAddress);
            SmartTravelService.clearInvalidClientById(clientID);

            System.out.println();
            System.out.println(">. Client info updated successfully.");
            System.out.println();
        } catch (InvalidClientDataException e) {
            System.out.println(">. Error updating client: " + e.getMessage());
            System.out.println();
        }
    }

    private static void deleteClient() {
        System.out.println(printAllClientsIncludingInvalid());
        if (!hasAnyClient()) {
            System.out.println(">. No clients in list.\n");
            return;
        }

        System.out.print(">. Please enter client ID whose information you would like to delete: ");
        String clientID = scanner.next();

        int foundIndex = clientExistCheck(clientID);
        if (foundIndex < 0) {
            System.out.println(">. Client not found.\n");
            return;
        }

        Client[] clients = service.getClients();
        Client[] updated = new Client[clients.length - 1];
        if (foundIndex > 0) {
            System.arraycopy(clients, 0, updated, 0, foundIndex);
        }
        if (foundIndex < clients.length - 1) {
            System.arraycopy(clients, foundIndex + 1, updated, foundIndex, clients.length - foundIndex - 1);
        }
        service.setClients(updated);

        System.out.println(">. Client deleted successfully.\n");
    }

    private static boolean tripManagement() {
        do {
            backToSubmenu = true;

            System.out.println("""
                    What would you like to do?
                    1. Create a trip
                    2. Edit trip information
                    3. Cancel a trip
                    4. List all trips
                    5. List all trips for a specific client
                    6. Exit
                    >. Please enter option: """);

            choice = readInt("");

            switch (choice) {
                case 1 -> createTrip();
                case 2 -> editTrip();
                case 3 -> cancelTrip();
                case 4 -> System.out.println(printAllTripsIncludingInvalid());
                case 5 -> listTripsByClient();
                case 6 -> backToSubmenu = false;
                default -> System.out.println(">. Please reenter an option.\n");
            }
        } while (backToSubmenu);

        return true;
    }

    private static void createTrip() {
        if (!hasAnyClient()) {
            System.out.println(">. No clients in list. Please add a client first.\n");
            return;
        }

        scanner.nextLine();
        System.out.print(">. Destination: ");
        String destination = scanner.nextLine();

        int duration = readInt(">. Duration (in days): ");
        double basePrice = readDouble(">. Base Price: ");

        System.out.println(SmartTravelService.printClients());
        System.out.print(">. Please enter client ID whom you'd like to link the trip to: ");
        String clientID = scanner.next();

        int clientIndex = clientExistCheck(clientID);
        if (clientIndex < 0) {
            System.out.println(">. Client not found.\n");
            return;
        }

        System.out.println(SmartTravelService.printAccomodations());
        System.out.print(" >. Please enter accommodation ID: ");
        String accommodationID = scanner.next();

        int accommodationIndex = accommodationExistCheck(accommodationID);
        if (accommodationIndex < 0) {
            System.out.println(" >. Accommodation not found.\n");
            return;
        }

        System.out.println(SmartTravelService.printTransportations());
        System.out.print(" >. Please enter transportation ID: ");
        String transportID = scanner.next();

        int transportIndex = transportExistCheck(transportID);
        if (transportIndex < 0) {
            System.out.println(" >. Transportation not found.\n");
            return;
        }

        try {
            Trip[] trips = service.getTrips();
            Trip[] updated = new Trip[trips.length + 1];
            System.arraycopy(trips, 0, updated, 0, trips.length);
            updated[trips.length] = new Trip(destination, duration, basePrice, clientID, accommodationID, transportID);
            service.setTrips(updated);

            System.out.println();
            System.out.println(">. Trip added successfully.");
            System.out.println();
        } catch (InvalidTripDataException | InvalidClientDataException | InvalidAccommodationDataException |
                InvalidTransportDataException | EntityNotFoundException e) {
            System.out.println(">. Error adding trip: " + e.getMessage());
            System.out.println();
        }
    }

    private static void editTrip() {
        System.out.println(printAllTripsIncludingInvalid());
        if (!hasAnyTrip()) {
            System.out.println(">. No trips in list.\n");
            return;
        }

        System.out.print(">. Please enter trip ID of trip you would like to update: ");
        String tripID = scanner.next();

        int foundIndex = tripExistCheck(tripID);
        if (foundIndex < 0) {
            System.out.println(">. Trip not found.\n");
            return;
        }

        scanner.nextLine();
        System.out.print(">. Updated destination: ");
        String destination = scanner.nextLine();

        int duration = readInt(">. Updated duration (in days): ");
        double basePrice = readDouble(">. Updated base price: ");

        try {
            Trip[] trips = service.getTrips();
            trips[foundIndex].setDestination(destination);
            trips[foundIndex].setDurationInDays(duration);
            trips[foundIndex].setBasePrice(basePrice);
            SmartTravelService.clearInvalidTripById(tripID);

            System.out.println();
            System.out.println(">. Trip info updated successfully.");
            System.out.println();
        } catch (InvalidTripDataException e) {
            System.out.println(">. Error updating trip: " + e.getMessage());
            System.out.println();
        }
    }

    private static void cancelTrip() {
        System.out.println(printAllTripsIncludingInvalid());
        if (!hasAnyTrip()) {
            System.out.println(">. No trips in list.\n");
            return;
        }

        System.out.print(">. Please enter trip ID you would like to cancel: ");
        String tripID = scanner.next();

        int foundIndex = tripExistCheck(tripID);
        if (foundIndex < 0) {
            System.out.println(">. Trip not found.\n");
            return;
        }

        Trip[] trips = service.getTrips();
        Trip[] updated = new Trip[trips.length - 1];
        if (foundIndex > 0) {
            System.arraycopy(trips, 0, updated, 0, foundIndex);
        }
        if (foundIndex < trips.length - 1) {
            System.arraycopy(trips, foundIndex + 1, updated, foundIndex, trips.length - foundIndex - 1);
        }
        service.setTrips(updated);

        System.out.println(">. Trip cancelled successfully.\n");
    }

    private static void listTripsByClient() {
        System.out.println(printAllClientsIncludingInvalid());
        if (!hasAnyClient()) {
            System.out.println(">. No clients in list.\n");
            return;
        }

        System.out.print(">. Please enter client ID to view their trips: ");
        String clientID = scanner.next();

        int clientIndex = clientExistCheck(clientID);
        if (clientIndex < 0) {
            System.out.println(">. Client not found.\n");
            return;
        }

        Client[] clients = service.getClients();
        Trip[] trips = service.getTrips();
        Client client = clients[clientIndex];
        System.out.println();
        System.out.println(">. Trips for " + client.getFirstName() + " " + client.getLastName() + ":");

        boolean foundTrips = false;
        for (Trip t : trips) {
            if (t != null && t.getClientId() != null && t.getClientId().equalsIgnoreCase(clientID)) {
                System.out.println(">. " + t);
                foundTrips = true;
            }
        }

        if (!foundTrips) {
            System.out.println(">. No trips found for this client.");
        }

        int invalidTripCount = SmartTravelService.getInvalidTripCount();
        for (int i = 0; i < invalidTripCount; i++) {
            String invalidRow = SmartTravelService.getInvalidTripRow(i);
            if (invalidRow == null)
                continue;

            String[] parts = invalidRow.split(";", -1);
            if (parts.length > 1 && clientID.equalsIgnoreCase(parts[1].trim())) {
                System.out.println(">. [INVALID] " + invalidRow);
                foundTrips = true;
            }
        }

        System.out.println();
    }

    private static boolean transportManagement() {
        do {
            backToSubmenu = true;

            System.out.println("""
                    What would you like to do?
                    1. Add a transportation option
                    2. Remove a transportation option
                    3. List transportation options by type (Flight, Train, Bus)
                    4. List all transportation options
                    5. Exit
                    >. Please enter option: """);

            choice = readInt("");

            switch (choice) {
                case 1 -> addTransportation();
                case 2 -> removeTransportation();
                case 3 -> listTransportationByType();
                case 4 -> System.out.println(printAllTransportationsIncludingInvalid());
                case 5 -> backToSubmenu = false;
                default -> System.out.println(">. Please reenter an option.\n");
            }
        } while (backToSubmenu);

        return true;
    }

    private static void addTransportation() {
        System.out.println("""
                Which type of transportation would you like to add?
                1. Flight
                2. Train
                3. Bus
                >. Please enter option: """);

        int transportType = readInt("");
        scanner.nextLine();

        System.out.print(">. Company Name: ");
        String companyName = scanner.nextLine();

        System.out.print(">. Departure City: ");
        String departureCity = scanner.nextLine();

        System.out.print(">. Arrival City: ");
        String arrivalCity = scanner.nextLine();

        double baseFare = readDouble(" >. Base Fare: ");

        Transportation newTransport;

        try {
            switch (transportType) {
                case 1 -> {
                    double luggageAllowance = readDouble(">. Luggage Allowance (kg): ");
                    newTransport = new Flight(companyName, departureCity, arrivalCity, baseFare, luggageAllowance);
                }
                case 2 -> {
                    scanner.nextLine();
                    System.out.print(">. Seat Class: ");
                    String seatClass = scanner.nextLine();
                    newTransport = new Train(companyName, departureCity, arrivalCity, baseFare, seatClass);
                }
                case 3 -> {
                    int numOfStops = readInt(">. Number of Stops: ");
                    newTransport = new Bus(companyName, departureCity, arrivalCity, baseFare, numOfStops);
                }
                default -> {
                    System.out.println(">. Invalid option.\n");
                    return;
                }
            }
        } catch (InvalidTransportDataException e) {
            System.out.println(">. Error adding transportation: " + e.getMessage());
            System.out.println();
            return;
        }

        Transportation[] transportations = service.getTransportations();
        Transportation[] updated = new Transportation[transportations.length + 1];
        System.arraycopy(transportations, 0, updated, 0, transportations.length);
        updated[transportations.length] = newTransport;
        service.setTransportations(updated);
        SmartTravelService.clearInvalidTransportationById(newTransport.getTransportID());

        System.out.println(">. Transportation added successfully.\n");
    }

    private static void removeTransportation() {
        System.out.println(printAllTransportationsIncludingInvalid());
        if (!hasAnyTransportation()) {
            System.out.println(">. No transportation in list.\n");
            return;
        }

        System.out.print(">. Please enter transportation ID to remove: ");
        String transportID = scanner.next();

        int foundIndex = transportExistCheck(transportID);
        if (foundIndex < 0) {
            System.out.println(">. Transportation not found.\n");
            return;
        }

        Transportation[] transportations = service.getTransportations();
        Transportation[] updated = new Transportation[transportations.length - 1];
        if (foundIndex > 0) {
            System.arraycopy(transportations, 0, updated, 0, foundIndex);
        }
        if (foundIndex < transportations.length - 1) {
            System.arraycopy(transportations, foundIndex + 1, updated, foundIndex,
                    transportations.length - foundIndex - 1);
        }
        service.setTransportations(updated);

        System.out.println(">. Transportation removed successfully.\n");
    }

    private static void listTransportationByType() {
        System.out.println("""
                Which type would you like to view?
                1. Flight
                2. Train
                3. Bus
                >. Please enter option: """);

        int filterType = readInt("");

        boolean found = false;
        Transportation[] transportations = service.getTransportations();
        for (Transportation t : transportations) {
            if (t == null) {
                continue;
            }

            if ((filterType == 1 && t instanceof Flight)
                    || (filterType == 2 && t instanceof Train)
                    || (filterType == 3 && t instanceof Bus)) {
                System.out.println(">. " + t);
                found = true;
            }
        }

        if (!found) {
            System.out.println(">. No transportation options found for this type.");
        }

        String typePrefix = switch (filterType) {
            case 1 -> "FLIGHT;";
            case 2 -> "TRAIN;";
            case 3 -> "BUS;";
            default -> "";
        };

        if (!typePrefix.isEmpty()) {
            int invalidTransportationCount = SmartTravelService.getInvalidTransportationCount();
            for (int i = 0; i < invalidTransportationCount; i++) {
                String invalidRow = SmartTravelService.getInvalidTransportationRow(i);
                if (invalidRow != null && invalidRow.startsWith(typePrefix)) {
                    System.out.println(">. [INVALID] " + invalidRow);
                    found = true;
                }
            }
        }

        System.out.println();
    }

    private static boolean accommodationManagement() {
        do {
            backToSubmenu = true;

            System.out.println("""
                    What would you like to do?
                    1. Add an accommodation
                    2. Remove an accommodation
                    3. List accommodations by type (Hotel, Hostel)
                    4. List all accommodations
                    5. Exit
                    >. Please enter option: """);

            choice = readInt("");

            switch (choice) {
                case 1 -> addAccommodation();
                case 2 -> removeAccommodation();
                case 3 -> listAccommodationByType();
                case 4 -> System.out.println(printAllAccommodationsIncludingInvalid());
                case 5 -> backToSubmenu = false;
                default -> System.out.println(">. Please reenter an option.\n");
            }
        } while (backToSubmenu);

        return true;
    }

    private static void addAccommodation() {
        System.out.println("""
                Which type of accommodation would you like to add?
                1. Hotel
                2. Hostel
                >. Please enter option: """);

        int accommodationType = readInt("");
        scanner.nextLine();

        System.out.print(">. Name: ");
        String name = scanner.nextLine();

        System.out.print(">. Location: ");
        String location = scanner.nextLine();

        double pricePerNight = readDouble(">. Price Per Night: ");

        Accomodation newAccommodation;

        try {
            switch (accommodationType) {
                case 1 -> {
                    int starRating = readInt(">. Star Rating: ");
                    newAccommodation = new Hotel(name, location, pricePerNight, starRating);
                }
                case 2 -> {
                    int numOfSharedBeds = readInt(">. Number of Shared Beds: ");
                    newAccommodation = new Hostel(name, location, pricePerNight, numOfSharedBeds);
                }
                default -> {
                    System.out.println(">. Invalid option.\n");
                    return;
                }
            }
        } catch (InvalidAccommodationDataException e) {
            System.out.println(">. Error adding accommodation: " + e.getMessage());
            System.out.println();
            return;
        }

        Accomodation[] accomodations = service.getAccomodations();
        Accomodation[] updated = new Accomodation[accomodations.length + 1];
        System.arraycopy(accomodations, 0, updated, 0, accomodations.length);
        updated[accomodations.length] = newAccommodation;
        service.setAccomodations(updated);
        SmartTravelService.clearInvalidAccomodationById(newAccommodation.getAccomodationID());

        System.out.println(">. Accommodation added successfully.\n");
    }

    private static void removeAccommodation() {
        System.out.println(printAllAccommodationsIncludingInvalid());
        if (!hasAnyAccommodation()) {
            System.out.println(">. No accommodations in list.\n");
            return;
        }

        System.out.print(">. Please enter accommodation ID to remove: ");
        String accommodationID = scanner.next();

        int foundIndex = accommodationExistCheck(accommodationID);
        if (foundIndex < 0) {
            System.out.println(">. Accommodation not found.\n");
            return;
        }

        Accomodation[] accommodations = service.getAccomodations();
        Accomodation[] updated = new Accomodation[accommodations.length - 1];
        if (foundIndex > 0) {
            System.arraycopy(accommodations, 0, updated, 0, foundIndex);
        }
        if (foundIndex < accommodations.length - 1) {
            System.arraycopy(accommodations, foundIndex + 1, updated, foundIndex,
                    accommodations.length - foundIndex - 1);
        }
        service.setAccomodations(updated);

        System.out.println(">. Accommodation removed successfully.\n");
    }

    private static void listAccommodationByType() {
        System.out.println("""
                Which type would you like to view?
                1. Hotel
                2. Hostel
                >. Please enter option: """);

        int filterType = readInt("");

        boolean found = false;
        Accomodation[] accommodations = service.getAccomodations();
        for (Accomodation a : accommodations) {
            if (a == null) {
                continue;
            }

            if ((filterType == 1 && a instanceof Hotel)
                    || (filterType == 2 && a instanceof Hostel)) {
                System.out.println(">. " + a);
                found = true;
            }
        }

        if (!found) {
            System.out.println(">. No accommodations found for this type.");
        }

        String typePrefix = switch (filterType) {
            case 1 -> "HOTEL;";
            case 2 -> "HOSTEL;";
            default -> "";
        };

        if (!typePrefix.isEmpty()) {
            int invalidAccommodationCount = SmartTravelService.getInvalidAccomodationCount();
            for (int i = 0; i < invalidAccommodationCount; i++) {
                String invalidRow = SmartTravelService.getInvalidAccomodationRow(i);
                if (invalidRow != null && invalidRow.startsWith(typePrefix)) {
                    System.out.println(">. [INVALID] " + invalidRow);
                    found = true;
                }
            }
        }

        System.out.println();
    }

    private static boolean additionalOperations() {
        do {
            backToSubmenu = true;

            System.out.println("""
                    What would you like to do?
                    1. Display the most expensive trip
                    2. Calculate and display the total cost of a trip
                    3. Create a deep copy of the transportation array
                    4. Create a deep copy of the accommodation array
                    5. Exit
                    >. Please enter option: """);

            choice = readInt("");

            switch (choice) {
                case 1 -> displayMostExpensiveTrip();
                case 2 -> calculateTripCost();
                case 3 -> deepCopyTransportation();
                case 4 -> deepCopyAccommodation();
                case 5 -> backToSubmenu = false;
                default -> System.out.println(">. Please reenter an option.\n");
            }
        } while (backToSubmenu);

        return true;
    }

    private static void listAllDataSummary() {
        System.out.println();
        System.out.println("=== SmartTravel Data Summary ===");
        System.out.println("Clients:");
        System.out.println(printAllClientsIncludingInvalid());
        System.out.println("Trips:");
        System.out.println(printAllTripsIncludingInvalid());
        System.out.println("Transportation:");
        System.out.println(printAllTransportationsIncludingInvalid());
        System.out.println("Accommodation:");
        System.out.println(printAllAccommodationsIncludingInvalid());
        System.out.println("================================");
        System.out.println();
    }

    private static void runPredefinedScenario() {
        try {
            SmartTravelService.testingScenario(true);
            System.out.println();
            System.out.println(">. Predefined scenario loaded successfully.");
            System.out.println();
            printPreloadedData();
        } catch (Exception e) {
            System.out.println(">. Error loading predefined scenario: " + e.getMessage());
            System.out.println();
        }
    }

    private static void generateDashboard() {
        Client[] originalClients = service.getClients();
        Trip[] originalTrips = service.getTrips();
        Transportation[] originalTransportations = service.getTransportations();
        Accomodation[] originalAccommodations = service.getAccomodations();

        try {
            service.setClients(filterValidClients(originalClients));
            service.setTransportations(filterValidTransportations(originalTransportations));
            service.setAccomodations(filterValidAccommodations(originalAccommodations));
            service.setTrips(filterValidTrips(service, originalTrips));

            service.ensureOutputDirectories();
            DashboardGenerator.generateDashboard(service);
            System.out.println(">. Dashboard generated successfully in output/dashboard.");
            System.out.println();
        } catch (IOException | InvalidAccommodationDataException | InvalidTransportDataException e) {
            System.out.println(">. Error generating dashboard: " + e.getMessage());
            System.out.println();
        } finally {
            service.setClients(originalClients);
            service.setTransportations(originalTransportations);
            service.setAccomodations(originalAccommodations);
            service.setTrips(originalTrips);
        }
    }

    private static void displayMostExpensiveTrip() {
        Trip mostExpensive = null;
        double maxCost = -1;

        Trip[] trips = service.getTrips();
        for (Trip t : trips) {
            if (t == null) {
                continue;
            }
            try {
                double currentCost = t.calculateTotalCost();
                if (mostExpensive == null || currentCost > maxCost) {
                    mostExpensive = t;
                    maxCost = currentCost;
                }
            } catch (InvalidAccommodationDataException | InvalidTransportDataException e) {
                // Skip invalid trip references while finding the maximum.
            }
        }

        if (mostExpensive == null) {
            System.out.println(">. No trips in list.\n");
            return;
        }

        System.out.println();
        System.out.println(">. Most Expensive Trip:");
        System.out.println(">. " + mostExpensive);
        System.out.println(" >. Total Cost: $" + String.format("%.2f", maxCost));
        System.out.println();
    }

    private static void calculateTripCost() {
        System.out.println(printAllTripsIncludingInvalid());
        if (!hasAnyTrip()) {
            System.out.println(">. No trips in list.\n");
            return;
        }

        System.out.print(">. Please enter trip ID to calculate total cost: ");
        String tripID = scanner.next();

        int foundIndex = tripExistCheck(tripID);
        if (foundIndex < 0) {
            System.out.println(">. Trip not found.\n");
            return;
        }

        Trip[] trips = service.getTrips();
        try {
            double totalCost = trips[foundIndex].calculateTotalCost();
            System.out.println();
            System.out.println(" >. Total Cost for Trip " + tripID + ": $" + String.format("%.2f", totalCost));
            System.out.println();
        } catch (InvalidAccommodationDataException | InvalidTransportDataException e) {
            System.out.println(" >. Error calculating trip total: " + e.getMessage());
            System.out.println();
        }
    }

    private static void deepCopyTransportation() {
        Transportation[] transportations = service.getTransportations();
        Transportation[] deepCopy = new Transportation[transportations.length];

        for (int i = 0; i < transportations.length; i++) {
            if (transportations[i] instanceof Flight) {
                deepCopy[i] = new Flight((Flight) transportations[i]);
            } else if (transportations[i] instanceof Train) {
                deepCopy[i] = new Train((Train) transportations[i]);
            } else if (transportations[i] instanceof Bus) {
                deepCopy[i] = new Bus((Bus) transportations[i]);
            }
        }

        System.out.println();
        System.out.println(">. Deep copy of transportation array created successfully.");
        System.out.println(">. Original array length: " + transportations.length);
        System.out.println(">. Deep copy array length: " + deepCopy.length);
        System.out.println();
    }

    private static void deepCopyAccommodation() {
        Accomodation[] accommodations = service.getAccomodations();
        Accomodation[] deepCopy = new Accomodation[accommodations.length];

        for (int i = 0; i < accommodations.length; i++) {
            if (accommodations[i] instanceof Hotel) {
                deepCopy[i] = new Hotel((Hotel) accommodations[i]);
            } else if (accommodations[i] instanceof Hostel) {
                deepCopy[i] = new Hostel((Hostel) accommodations[i]);
            }
        }

        System.out.println();
        System.out.println(">. Deep copy of accommodation array created successfully.");
        System.out.println(">. Original array length: " + accommodations.length);
        System.out.println(">. Deep copy array length: " + deepCopy.length);
        System.out.println();
    }

    private static void printPreloadedData() {
        try {
            System.out.println();
            System.out.println("=== Predefined Scenario Loaded ===");
            System.out.println("Clients:");
            System.out.println(printAllClientsIncludingInvalid());

            System.out.println("Trips:");
            System.out.println(printAllTripsIncludingInvalid());

            System.out.println("Transportation:");
            System.out.println(printAllTransportationsIncludingInvalid());

            System.out.println("Accommodation:");
            System.out.println(printAllAccommodationsIncludingInvalid());

            System.out.println("==================================");
        } catch (Exception e) {
            System.out.println(">. Preloaded data has some invalid entries. Continuing to menu.");
        }
    }

    private static boolean hasAnyClient() {
        Client[] clients = service.getClients();
        for (Client c : clients) {
            if (c != null) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasAnyTrip() {
        Trip[] trips = service.getTrips();
        for (Trip t : trips) {
            if (t != null) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasAnyTransportation() {
        Transportation[] transportations = service.getTransportations();
        for (Transportation t : transportations) {
            if (t != null) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasAnyAccommodation() {
        Accomodation[] accommodations = service.getAccomodations();
        for (Accomodation a : accommodations) {
            if (a != null) {
                return true;
            }
        }
        return false;
    }

    private static int clientExistCheck(String clientID) {
        if (clientID == null || clientID.trim().isEmpty()) {
            return -1;
        }

        Client[] clients = service.getClients();
        if (clients.length == 0 || !hasAnyClient()) {
            return -2;
        }

        for (int i = 0; i < clients.length; i++) {
            if (clients[i] != null && clients[i].getClientId().equalsIgnoreCase(clientID)) {
                return i;
            }
        }
        return -1;
    }

    private static int tripExistCheck(String tripID) {
        if (tripID == null || tripID.trim().isEmpty()) {
            return -1;
        }

        Trip[] trips = service.getTrips();
        if (trips.length == 0 || !hasAnyTrip()) {
            return -2;
        }

        for (int i = 0; i < trips.length; i++) {
            if (trips[i] != null && trips[i].getTripId().equalsIgnoreCase(tripID)) {
                return i;
            }
        }
        return -1;
    }

    private static int transportExistCheck(String transportID) {
        if (transportID == null || transportID.trim().isEmpty()) {
            return -1;
        }

        Transportation[] transportations = service.getTransportations();
        if (transportations.length == 0 || !hasAnyTransportation()) {
            return -2;
        }

        for (int i = 0; i < transportations.length; i++) {
            if (transportations[i] != null && transportations[i].getTransportID().equalsIgnoreCase(transportID)) {
                return i;
            }
        }
        return -1;
    }

    private static int accommodationExistCheck(String accommodationID) {
        if (accommodationID == null || accommodationID.trim().isEmpty()) {
            return -1;
        }

        Accomodation[] accommodations = service.getAccomodations();
        if (accommodations.length == 0 || !hasAnyAccommodation()) {
            return -2;
        }

        for (int i = 0; i < accommodations.length; i++) {
            if (accommodations[i] != null && accommodations[i].getAccomodationID().equalsIgnoreCase(accommodationID)) {
                return i;
            }
        }
        return -1;
    }

    private static int readInt(String prompt) {
        if (!prompt.isEmpty()) {
            System.out.print(prompt);
        }

        while (!scanner.hasNextInt()) {
            System.out.println(">. Invalid number. Please enter an integer.");
            scanner.next();
            if (!prompt.isEmpty()) {
                System.out.print(prompt);
            }
        }

        return scanner.nextInt();
    }

    private static double readDouble(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.println(">. Invalid number. Please enter a decimal value.");
            scanner.next();
            System.out.print(prompt);
        }
        return scanner.nextDouble();
    }

    private static String printAllClientsIncludingInvalid() {
        StringBuilder output = new StringBuilder();
        output.append(SmartTravelService.printClients());

        int invalidCount = SmartTravelService.getInvalidClientCount();
        if (invalidCount > 0) {
            output.append(">. Invalid Clients:\n");
            for (int i = 0; i < invalidCount; i++) {
                String row = SmartTravelService.getInvalidClientRow(i);
                if (row != null)
                    output.append(">. [INVALID] ").append(row).append("\n");
            }
            output.append("\n");
        }

        return output.toString();
    }

    private static String printAllTripsIncludingInvalid() {
        StringBuilder output = new StringBuilder();
        output.append(SmartTravelService.printTrips());

        int invalidCount = SmartTravelService.getInvalidTripCount();
        if (invalidCount > 0) {
            output.append(">. Invalid Trips:\n");
            for (int i = 0; i < invalidCount; i++) {
                String row = SmartTravelService.getInvalidTripRow(i);
                if (row != null)
                    output.append(">. [INVALID] ").append(row).append("\n");
            }
            output.append("\n");
        }

        return output.toString();
    }

    private static String printAllTransportationsIncludingInvalid() {
        StringBuilder output = new StringBuilder();
        output.append(SmartTravelService.printTransportations());

        int invalidCount = SmartTravelService.getInvalidTransportationCount();
        if (invalidCount > 0) {
            output.append(">. Invalid Transportation Entries:\n");
            for (int i = 0; i < invalidCount; i++) {
                String row = SmartTravelService.getInvalidTransportationRow(i);
                if (row != null)
                    output.append(">. [INVALID] ").append(row).append("\n");
            }
            output.append("\n");
        }

        return output.toString();
    }

    private static String printAllAccommodationsIncludingInvalid() {
        StringBuilder output = new StringBuilder();
        output.append(SmartTravelService.printAccomodations());

        int invalidCount = SmartTravelService.getInvalidAccomodationCount();
        if (invalidCount > 0) {
            output.append(">. Invalid Accommodation Entries:\n");
            for (int i = 0; i < invalidCount; i++) {
                String row = SmartTravelService.getInvalidAccomodationRow(i);
                if (row != null)
                    output.append(">. [INVALID] ").append(row).append("\n");
            }
            output.append("\n");
        }

        return output.toString();
    }

    private static Client[] filterValidClients(Client[] clients) {
        if (clients == null || clients.length == 0)
            return new Client[0];

        String[] invalidClientIds = new String[SmartTravelService.getInvalidClientCount()];
        int invalidIdCount = 0;
        for (int i = 0; i < SmartTravelService.getInvalidClientCount(); i++) {
            String row = SmartTravelService.getInvalidClientRow(i);
            if (row == null)
                continue;

            String[] parts = row.split(";", -1);
            if (parts.length > 0) {
                String id = parts[0].trim();
                if (!id.isEmpty())
                    invalidClientIds[invalidIdCount++] = id;
            }
        }

        Client[] filtered = new Client[clients.length];
        int count = 0;

        for (Client client : clients) {
            if (client == null)
                continue;

            boolean matchesInvalid = false;
            for (int i = 0; i < invalidIdCount; i++) {
                if (client.getClientId().equalsIgnoreCase(invalidClientIds[i])) {
                    matchesInvalid = true;
                    break;
                }
            }

            if (!matchesInvalid)
                filtered[count++] = client;
        }

        Client[] result = new Client[count];
        System.arraycopy(filtered, 0, result, 0, count);
        return result;
    }

    private static Transportation[] filterValidTransportations(Transportation[] transportations) {
        if (transportations == null || transportations.length == 0)
            return new Transportation[0];

        String[] invalidTransportIds = new String[SmartTravelService.getInvalidTransportationCount()];
        int invalidIdCount = 0;
        for (int i = 0; i < SmartTravelService.getInvalidTransportationCount(); i++) {
            String row = SmartTravelService.getInvalidTransportationRow(i);
            if (row == null)
                continue;

            String[] parts = row.split(";", -1);
            if (parts.length > 1) {
                String id = parts[1].trim();
                if (!id.isEmpty())
                    invalidTransportIds[invalidIdCount++] = id;
            }
        }

        Transportation[] filtered = new Transportation[transportations.length];
        int count = 0;

        for (Transportation transportation : transportations) {
            if (transportation == null)
                continue;

            boolean matchesInvalid = false;
            for (int i = 0; i < invalidIdCount; i++) {
                if (transportation.getTransportID().equalsIgnoreCase(invalidTransportIds[i])) {
                    matchesInvalid = true;
                    break;
                }
            }

            if (!matchesInvalid)
                filtered[count++] = transportation;
        }

        Transportation[] result = new Transportation[count];
        System.arraycopy(filtered, 0, result, 0, count);
        return result;
    }

    private static Accomodation[] filterValidAccommodations(Accomodation[] accommodations) {
        if (accommodations == null || accommodations.length == 0)
            return new Accomodation[0];

        Accomodation[] filtered = new Accomodation[accommodations.length];
        int count = 0;

        for (Accomodation accommodation : accommodations) {
            if (accommodation != null)
                filtered[count++] = accommodation;
        }

        Accomodation[] result = new Accomodation[count];
        System.arraycopy(filtered, 0, result, 0, count);
        return result;
    }

    private static Trip[] filterValidTrips(SmartTravelService currentService, Trip[] trips) {
        if (trips == null || trips.length == 0)
            return new Trip[0];

        String[] invalidTripIds = new String[SmartTravelService.getInvalidTripCount()];
        int invalidIdCount = 0;
        for (int i = 0; i < SmartTravelService.getInvalidTripCount(); i++) {
            String row = SmartTravelService.getInvalidTripRow(i);
            if (row == null)
                continue;

            String[] parts = row.split(";", -1);
            if (parts.length > 0) {
                String id = parts[0].trim();
                if (!id.isEmpty())
                    invalidTripIds[invalidIdCount++] = id;
            }
        }

        Trip[] filtered = new Trip[trips.length];
        int count = 0;

        for (Trip trip : trips) {
            if (trip == null)
                continue;

            boolean matchesInvalid = false;
            for (int i = 0; i < invalidIdCount; i++) {
                if (trip.getTripId().equalsIgnoreCase(invalidTripIds[i])) {
                    matchesInvalid = true;
                    break;
                }
            }
            if (matchesInvalid)
                continue;

            int tripIndex = tripExistCheck(trip.getTripId());
            if (tripIndex < 0)
                continue;

            try {
                currentService.calculateTripTotal(tripIndex);
                filtered[count++] = trip;
            } catch (InvalidAccommodationDataException | InvalidTransportDataException e) {
                // Skip invalid linked references for dashboard and charts.
            }
        }

        Trip[] result = new Trip[count];
        System.arraycopy(filtered, 0, result, 0, count);
        return result;
    }
}
