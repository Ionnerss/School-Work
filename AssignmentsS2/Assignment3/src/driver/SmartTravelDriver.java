package AssignmentsS2.Assignment3.src.driver;

/*
 * Assignment 3
 * Question: Full SmartTravel Data Management Project
 * Written by: Catalin-Ion Besleaga (40347936)
 *
 * This driver runs the SmartTravel console system. It lets the user manage
 * clients, trips, transportation, accommodations, load and save CSV data,
 * run the predefined scenario, generate the dashboard, and use A3 analytics.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

import AssignmentsS2.Assignment3.src.client.Client;
import AssignmentsS2.Assignment3.src.exceptions.*;
import AssignmentsS2.Assignment3.src.service.RecentList;
import AssignmentsS2.Assignment3.src.service.SmartTravelService;
import AssignmentsS2.Assignment3.src.travel.*;
import AssignmentsS2.Assignment3.src.visualization.DashboardGenerator;

public class SmartTravelDriver {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SmartTravelService service = new SmartTravelService();
    private static final RecentList<Trip> recentTrips = new RecentList<>();
    private static final RecentList<Client> recentTopClients = new RecentList<>();

    private static boolean backToMain;
    private static boolean backToSubmenu;
    private static int choice;

    public static void main(String[] args) {
        System.out.println("Welcome to the SmartTravel Program!");
        System.out.println("Written by: Catalin-Ion Besleaga (40347936)");
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
                === SmartTravel A3 Console ===
                1. Client Management
                2. Trip Management
                3. Transportation Management
                4. Accommodation Management
                5. Additional Operations
                6. Exit Program
                7. Advanced Analytics
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
                case 7 -> backToMain = advancedAnalytics();
                case 8 -> {
                    try {
                        service.loadAllData(null);
                    } catch (IOException e) {
                        System.out.println(">. Error loading all data: " + e.getMessage());
                        System.out.println();
                    }
                    backToMain = true;
                }
                case 9 -> {
                    try {
                        service.saveAllData(null);
                    } catch (IOException e) {
                        System.out.println(">. Error saving all data: " + e.getMessage());
                        System.out.println();
                    }
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
                case 0, 6 -> backToMain = false;
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
        scanner.nextLine();
        System.out.print(">. First Name: ");
        String firstName = scanner.nextLine();

        System.out.print(">. Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print(">. Email Address: ");
        String emailAddress = scanner.nextLine();

        try {
            SmartTravelService.addClient(firstName, lastName, emailAddress);
            System.out.println();
            System.out.println(">. Client added successfully.");
            System.out.println();
        } catch (InvalidClientDataException | DuplicateEmailException e) {
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
            SmartTravelService.updateClient(clientID, firstName, lastName, emailAddress);
            SmartTravelService.clearInvalidClientById(clientID);

            System.out.println();
            System.out.println(">. Client info updated successfully.");
            System.out.println();
        } catch (InvalidClientDataException | DuplicateEmailException | EntityNotFoundException e) {
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

        if (!service.removeClientById(clientID)) {
            System.out.println(">. Client not found.\n");
            return;
        }

        SmartTravelService.clearInvalidClientById(clientID);
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

        if (clientExistCheck(clientID) < 0) {
            System.out.println(">. Client not found.\n");
            return;
        }

        String accommodationID = "";
        if (service.getAccommodationCount() > 0) {
            System.out.println(SmartTravelService.printAccomodations());
            System.out.print(">. Please enter accommodation ID (or NONE): ");
            accommodationID = scanner.next();

            if (accommodationID.equalsIgnoreCase("NONE")) {
                accommodationID = "";
            } else if (accommodationExistCheck(accommodationID) < 0) {
                System.out.println(">. Accommodation not found.\n");
                return;
            }
        }

        String transportID = "";
        if (service.getTransportationCount() > 0) {
            System.out.println(SmartTravelService.printTransportations());
            System.out.print(">. Please enter transportation ID (or NONE): ");
            transportID = scanner.next();

            if (transportID.equalsIgnoreCase("NONE")) {
                transportID = "";
            } else if (transportExistCheck(transportID) < 0) {
                System.out.println(">. Transportation not found.\n");
                return;
            }
        }

        if (accommodationID.isBlank() && transportID.isBlank()) {
            System.out.println(">. You must provide at least one of accommodation ID or transportation ID.\n");
            return;
        }

        try {
            Trip createdTrip = SmartTravelService.createTrip(destination, duration, basePrice, clientID, accommodationID, transportID);
            recentTrips.addRecent(createdTrip);

            System.out.println();
            System.out.println(">. Trip added successfully.");
            System.out.println();
        } catch (InvalidTripDataException | EntityNotFoundException e) {
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
            Trip trip = service.getTrip(foundIndex);
            if (trip == null) {
                System.out.println(">. Trip not found.\n");
                return;
            }

            trip.setDestination(destination);
            trip.setDurationInDays(duration);
            trip.setBasePrice(basePrice);
            SmartTravelService.clearInvalidTripById(tripID);
            recentTrips.addRecent(trip);

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

        if (!service.removeTripById(tripID)) {
            System.out.println(">. Trip not found.\n");
            return;
        }

        SmartTravelService.clearInvalidTripById(tripID);
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

        Client client = service.getClient(clientIndex);
        System.out.println();
        System.out.println(">. Trips for " + client.getFirstName() + " " + client.getLastName() + ":");

        boolean foundTrips = false;
        for (Trip trip : service.getTrips()) {
            if (trip != null && trip.getClientId() != null && trip.getClientId().equalsIgnoreCase(clientID)) {
                System.out.println(">. " + trip);
                recentTrips.addRecent(trip);
                foundTrips = true;
            }
        }

        if (!foundTrips) {
            System.out.println(">. No trips found for this client.");
        }

        int invalidTripCount = SmartTravelService.getInvalidTripCount();
        for (int i = 0; i < invalidTripCount; i++) {
            String invalidRow = SmartTravelService.getInvalidTripRow(i);
            if (invalidRow == null) {
                continue;
            }

            String[] parts = invalidRow.split(";", -1);
            if (parts.length > 1 && clientID.equalsIgnoreCase(parts[1].trim())) {
                System.out.println(">. [INVALID] " + invalidRow);
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

        double baseFare = readDouble(">. Base Fare: ");

        try {
            Transportation newTransport;
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

            SmartTravelService.addTransportation(newTransport);
            SmartTravelService.clearInvalidTransportationById(newTransport.getId());
            System.out.println(">. Transportation added successfully.\n");
        } catch (InvalidTransportDataException | IllegalStateException e) {
            System.out.println(">. Error adding transportation: " + e.getMessage());
            System.out.println();
        }
    }

    private static void removeTransportation() {
        System.out.println(printAllTransportationsIncludingInvalid());
        if (!hasAnyTransportation()) {
            System.out.println(">. No transportation in list.\n");
            return;
        }

        System.out.print(">. Please enter transportation ID to remove: ");
        String transportID = scanner.next();

        if (!service.removeTransportationById(transportID)) {
            System.out.println(">. Transportation not found.\n");
            return;
        }

        SmartTravelService.clearInvalidTransportationById(transportID);
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

        for (Transportation transportation : service.getTransportations()) {
            if (transportation == null) {
                continue;
            }

            if ((filterType == 1 && transportation instanceof Flight)
                    || (filterType == 2 && transportation instanceof Train)
                    || (filterType == 3 && transportation instanceof Bus)) {
                System.out.println(">. " + transportation);
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
            for (int i = 0; i < SmartTravelService.getInvalidTransportationCount(); i++) {
                String invalidRow = SmartTravelService.getInvalidTransportationRow(i);
                if (invalidRow != null && invalidRow.startsWith(typePrefix)) {
                    System.out.println(">. [INVALID] " + invalidRow);
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

        try {
            Accommodation newAccommodation;
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

            SmartTravelService.addAccommodation(newAccommodation);
            SmartTravelService.clearInvalidAccomodationById(newAccommodation.getId());
            System.out.println(">. Accommodation added successfully.\n");
        } catch (InvalidAccommodationDataException | IllegalStateException e) {
            System.out.println(">. Error adding accommodation: " + e.getMessage());
            System.out.println();
        }
    }

    private static void removeAccommodation() {
        System.out.println(printAllAccommodationsIncludingInvalid());
        if (!hasAnyAccommodation()) {
            System.out.println(">. No accommodations in list.\n");
            return;
        }

        System.out.print(">. Please enter accommodation ID to remove: ");
        String accommodationID = scanner.next();

        if (!service.removeAccommodationById(accommodationID)) {
            System.out.println(">. Accommodation not found.\n");
            return;
        }

        SmartTravelService.clearInvalidAccomodationById(accommodationID);
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

        for (Accommodation accommodation : service.getAccomodations()) {
            if (accommodation == null) {
                continue;
            }

            if ((filterType == 1 && accommodation instanceof Hotel)
                    || (filterType == 2 && accommodation instanceof Hostel)) {
                System.out.println(">. " + accommodation);
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
            for (int i = 0; i < SmartTravelService.getInvalidAccomodationCount(); i++) {
                String invalidRow = SmartTravelService.getInvalidAccomodationRow(i);
                if (invalidRow != null && invalidRow.startsWith(typePrefix)) {
                    System.out.println(">. [INVALID] " + invalidRow);
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
                    3. Create a deep copy of the transportation collection
                    4. Create a deep copy of the accommodation collection
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

    private static void displayMostExpensiveTrip() {
        Trip mostExpensive = null;
        double maxCost = -1.0;

        for (Trip trip : service.getTrips()) {
            if (trip == null) {
                continue;
            }

            try {
                double currentCost = trip.calculateTotalCost();
                if (mostExpensive == null || currentCost > maxCost) {
                    mostExpensive = trip;
                    maxCost = currentCost;
                }
            } catch (EntityNotFoundException ignored) {
                // Skip broken trip links.
            }
        }

        if (mostExpensive == null) {
            System.out.println(">. No trips in list.\n");
            return;
        }

        System.out.println();
        System.out.println(">. Most Expensive Trip:");
        System.out.println(">. " + mostExpensive);
        System.out.println(">. Total Cost: $" + String.format("%.2f", maxCost));
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

        try {
            Trip trip = service.getTrip(foundIndex);
            double totalCost = trip.calculateTotalCost();
            recentTrips.addRecent(trip);

            System.out.println();
            System.out.println(">. Total Cost for Trip " + tripID + ": $" + String.format("%.2f", totalCost));
            System.out.println();
        } catch (EntityNotFoundException e) {
            System.out.println(">. Error calculating trip total: " + e.getMessage());
            System.out.println();
        }
    }

    private static void deepCopyTransportation() {
        List<Transportation> deepCopy = new ArrayList<>();

        for (Transportation transportation : service.getTransportations()) {
            if (transportation instanceof Flight) {
                deepCopy.add(new Flight((Flight) transportation));
            } else if (transportation instanceof Train) {
                deepCopy.add(new Train((Train) transportation));
            } else if (transportation instanceof Bus) {
                deepCopy.add(new Bus((Bus) transportation));
            }
        }

        System.out.println();
        System.out.println(">. Deep copy of transportation collection created successfully.");
        System.out.println(">. Original collection size: " + service.getTransportations().size());
        System.out.println(">. Deep copy collection size: " + deepCopy.size());
        System.out.println();
    }

    private static void deepCopyAccommodation() {
        List<Accommodation> deepCopy = new ArrayList<>();

        for (Accommodation accommodation : service.getAccomodations()) {
            if (accommodation instanceof Hotel) {
                deepCopy.add(new Hotel((Hotel) accommodation));
            } else if (accommodation instanceof Hostel) {
                deepCopy.add(new Hostel((Hostel) accommodation));
            }
        }

        System.out.println();
        System.out.println(">. Deep copy of accommodation collection created successfully.");
        System.out.println(">. Original collection size: " + service.getAccomodations().size());
        System.out.println(">. Deep copy collection size: " + deepCopy.size());
        System.out.println();
    }

    private static boolean advancedAnalytics() {
        do {
            backToSubmenu = true;

            System.out.println("""
                    === Advanced Analytics ===
                    1. Trips by Destination
                    2. Trips by Cost Range
                    3. Top Clients by Spending
                    4. Recent Trips
                    5. Smart Sort Collections
                    6. Back to main menu
                    >. Please enter option: """);

            choice = readInt("");

            switch (choice) {
                case 1 -> tripsByDestination();
                case 2 -> tripsByCostRange();
                case 3 -> topClientsBySpending();
                case 4 -> recentTripsDemo();
                case 5 -> smartSortCollections();
                case 6 -> backToSubmenu = false;
                default -> System.out.println(">. Please reenter an option.\n");
            }
        } while (backToSubmenu);

        return true;
    }

    private static void tripsByDestination() {
        if (!hasAnyTrip()) {
            System.out.println(">. No trips in list.\n");
            return;
        }

        scanner.nextLine();
        System.out.print(">. Enter destination to filter by: ");
        String destination = scanner.nextLine().trim();

        Predicate<Trip> destinationFilter = trip ->
                trip != null
                        && trip.getDestination() != null
                        && trip.getDestination().equalsIgnoreCase(destination);

        List<Trip> filteredTrips = service.filterTrips(destinationFilter);

        System.out.println();
        System.out.println(">. Trips matching destination \"" + destination + "\":");
        printTripList(filteredTrips);
    }

    private static void tripsByCostRange() {
        if (!hasAnyTrip()) {
            System.out.println(">. No trips in list.\n");
            return;
        }

        double min = readDouble(">. Minimum total cost: ");
        double max = readDouble(">. Maximum total cost: ");

        if (min > max) {
            System.out.println(">. Minimum cannot be greater than maximum.\n");
            return;
        }

        Predicate<Trip> costRangeFilter = trip -> {
            if (trip == null) {
                return false;
            }

            try {
                double total = trip.calculateTotalCost();
                return total >= min && total <= max;
            } catch (EntityNotFoundException e) {
                return false;
            }
        };

        List<Trip> filteredTrips = service.filterTrips(costRangeFilter);

        System.out.println();
        System.out.println(">. Trips in cost range $" + String.format("%.2f", min)
                + " to $" + String.format("%.2f", max) + ":");
        printTripList(filteredTrips);
    }

    private static void topClientsBySpending() {
        if (!hasAnyClient()) {
            System.out.println(">. No clients in list.\n");
            return;
        }

        int maxToShow = readInt(">. How many top clients would you like to show? ");
        List<Client> sortedClients = service.getSortedClients();

        System.out.println();
        System.out.println(">. Top Clients by Spending:");

        int limit = Math.min(maxToShow, sortedClients.size());
        for (int i = 0; i < limit; i++) {
            Client client = sortedClients.get(i);
            System.out.println(">. " + client + " | Total Spent: $" + String.format("%.2f", client.getTotalSpent()));
            recentTopClients.addRecent(client);
        }

        if (limit == 0) {
            System.out.println(">. No clients to display.");
        }

        System.out.println();
        System.out.println(">. Recent Top-Client Highlights:");
        recentTopClients.printRecent(Math.min(5, maxToShow));
        System.out.println();
    }

    private static void recentTripsDemo() {
        System.out.println();
        System.out.println(">. Recent Trips:");

        if (recentTrips.isEmpty()) {
            System.out.println(">. No recent trips tracked yet.");
            System.out.println();
            return;
        }

        int maxToShow = readInt(">. How many recent trips would you like to show? ");
        recentTrips.printRecent(maxToShow);
        System.out.println();
    }

    private static void smartSortCollections() {
        System.out.println("""
                Which collection would you like to smart sort?
                1. Clients
                2. Trips
                3. Transportation
                4. Accommodation
                5. All
                >. Please enter option: """);

        int sortChoice = readInt("");

        switch (sortChoice) {
            case 1 -> printSortedClients();
            case 2 -> printSortedTrips();
            case 3 -> printSortedTransportations();
            case 4 -> printSortedAccommodations();
            case 5 -> {
                printSortedClients();
                printSortedTrips();
                printSortedTransportations();
                printSortedAccommodations();
            }
            default -> System.out.println(">. Invalid option.\n");
        }
    }

    private static void printSortedClients() {
        List<Client> sortedClients = service.getSortedClients();

        System.out.println();
        System.out.println(">. Clients (business natural order):");

        if (sortedClients.isEmpty()) {
            System.out.println(">. No clients in list.\n");
            return;
        }

        for (Client client : sortedClients) {
            System.out.println(">. " + client + " | Total Spent: $" + String.format("%.2f", client.getTotalSpent()));
        }

        System.out.println();
    }

    private static void printSortedTrips() {
        List<Trip> sortedTrips;
        try {
            sortedTrips = service.getSortedTrips();
        } catch (RuntimeException e) {
            sortedTrips = getSafelySortedTrips();
        }

        System.out.println();
        System.out.println(">. Trips (business natural order):");

        if (sortedTrips.isEmpty()) {
            System.out.println(">. No trips in list.\n");
            return;
        }

        for (Trip trip : sortedTrips) {
            try {
                System.out.println(">. " + trip + " | Total Cost: $" + String.format("%.2f", trip.calculateTotalCost()));
            } catch (EntityNotFoundException e) {
                System.out.println(">. " + trip + " | Total Cost: unavailable");
            }
        }

        System.out.println();
    }

    private static List<Trip> getSafelySortedTrips() {
        List<Trip> copy = new ArrayList<>();
        for (Trip trip : service.getTrips()) {
            if (trip != null) {
                copy.add(trip);
            }
        }

        for (int i = 1; i < copy.size(); i++) {
            Trip key = copy.get(i);
            double keyCost = safeTripTotal(key);
            int j = i - 1;

            while (j >= 0 && safeTripTotal(copy.get(j)) < keyCost) {
                copy.set(j + 1, copy.get(j));
                j--;
            }

            copy.set(j + 1, key);
        }

        return copy;
    }

    private static double safeTripTotal(Trip trip) {
        if (trip == null) {
            return -1.0;
        }

        try {
            return trip.calculateTotalCost();
        } catch (EntityNotFoundException e) {
            return -1.0;
        }
    }

    private static void printSortedTransportations() {
        List<Transportation> sortedTransportations = service.getSortedTransportations();

        System.out.println();
        System.out.println(">. Transportation (business natural order):");

        if (sortedTransportations.isEmpty()) {
            System.out.println(">. No transportation in list.\n");
            return;
        }

        for (Transportation transportation : sortedTransportations) {
            System.out.println(">. " + transportation + " | Base Price: $" + String.format("%.2f", transportation.getBasePrice()));
        }

        System.out.println();
    }

    private static void printSortedAccommodations() {
        List<Accommodation> sortedAccommodations = service.getSortedAccommodations();

        System.out.println();
        System.out.println(">. Accommodations (business natural order):");

        if (sortedAccommodations.isEmpty()) {
            System.out.println(">. No accommodations in list.\n");
            return;
        }

        for (Accommodation accommodation : sortedAccommodations) {
            System.out.println(">. " + accommodation + " | Price/Night: $" + String.format("%.2f", accommodation.getPricePerNight()));
        }

        System.out.println();
    }

    private static void printTripList(List<Trip> trips) {
        if (trips == null || trips.isEmpty()) {
            System.out.println(">. No trips matched.");
            System.out.println();
            return;
        }

        for (Trip trip : trips) {
            if (trip == null) {
                continue;
            }

            try {
                System.out.println(">. " + trip + " | Total Cost: $" + String.format("%.2f", trip.calculateTotalCost()));
            } catch (EntityNotFoundException e) {
                System.out.println(">. " + trip + " | Total Cost: unavailable");
            }
        }

        System.out.println();
    }

    private static void generateDashboard() {
        try {
            service.ensureOutputDirectories();
            DashboardGenerator.generateDashboard(service);
            System.out.println(">. Dashboard generated successfully in output/dashboard.");
            System.out.println();
        } catch (IOException | InvalidAccommodationDataException | InvalidTransportDataException | EntityNotFoundException e) {
            System.out.println(">. Error generating dashboard: " + e.getMessage());
            System.out.println();
        }
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
        return service.getClientCount() > 0;
    }

    private static boolean hasAnyTrip() {
        return service.getTripCount() > 0;
    }

    private static boolean hasAnyTransportation() {
        return service.getTransportationCount() > 0;
    }

    private static boolean hasAnyAccommodation() {
        return service.getAccommodationCount() > 0;
    }

    private static int clientExistCheck(String clientID) {
        return service.getClientIndexById(clientID);
    }

    private static int tripExistCheck(String tripID) {
        return service.getTripIndexById(tripID);
    }

    private static int transportExistCheck(String transportID) {
        return service.getTransportationIndexById(transportID);
    }

    private static int accommodationExistCheck(String accommodationID) {
        return service.getAccommodationIndexById(accommodationID);
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
                if (row != null) {
                    output.append(">. [INVALID] ").append(row).append("\n");
                }
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
                if (row != null) {
                    output.append(">. [INVALID] ").append(row).append("\n");
                }
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
                if (row != null) {
                    output.append(">. [INVALID] ").append(row).append("\n");
                }
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
                if (row != null) {
                    output.append(">. [INVALID] ").append(row).append("\n");
                }
            }
            output.append("\n");
        }

        return output.toString();
    }
}
