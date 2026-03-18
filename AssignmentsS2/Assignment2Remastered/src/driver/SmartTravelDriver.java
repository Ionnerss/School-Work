package AssignmentsS2.Assignment2Remastered.src.driver;
// -------------------------------------------------------- 
// Assignment 1 - Main driver
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S – Fall 2025
// --------------------------------------------------------

import AssignmentsS2.Assignment2Remastered.src.client.Client;
import AssignmentsS2.Assignment2Remastered.src.exceptions.*;
import AssignmentsS2.Assignment2Remastered.src.travel.*;
import AssignmentsS2.Assignment2Remastered.src.service.SmartTravelService;
import AssignmentsS2.Assignment2Remastered.src.service.SmartTravelService.ArrayType;
import java.util.Scanner;

/**
 * SmartTravel Program - Main Driver Class
 * 
 * This class serves as the main entry point for the SmartTravel application.
 * It manages the interactive menu system and orchestrates client, trip, transportation,
 * and accommodation management operations. The program demonstrates polymorphism through
 * base-class references (Transportation and Accomodation) and dynamic method dispatch.
 * 
 * Features:
 * - Predefined scenario with sample clients, trips, transportation, and accommodations
 * - CRUD operations for all entity types
 * - Trip filtering by client
 * - Deep copy functionality for arrays
 * - Cost calculations and expense tracking
 * 
 * @author Catalin-Ion Besleaga
 * @version 1.0
 */
public class SmartTravelDriver {
    // Arrays to store all entities in the system  WILL FIX ALL OF THIS SO THAT ALL ARRAYS REMAIN IN SmartTravelService.java
    /** Array to store all registered clients */
    private static Client[] client;
    /** Array to store all trips in the system */
    private static Trip[] trip;
    /** Array to store all transportation options (Flight, Train, Bus) */
    private static Transportation[] transportation;
    /** Array to store all accommodation options (Hotel, Hostel) */
    private static Accomodation[] accomodation;
    
    // Control variables for menu navigation
    /** Scanner for reading user input from console */
    private static final Scanner scanner = new Scanner(System.in);
    /** Flag to control return to main menu loop | Flag to control return to submenu loop | Check variable for validation results*/
    private static boolean backToMain, backToSubmenu;

    private static int check;
    /** User's menu choice input */
    private static int choice;
    /** Client ID for filtering/searching operations | Trip ID for filtering/searching operations */
    private static String clientID, tripID;

    /**
     * Main entry point for the SmartTravel application.
     * Initializes the system with either a predefined scenario or empty arrays,
     * then presents the main menu for user interaction until exit.
     * 
     * @param args Command-line arguments (not used)
     * @throws InvalidAccommodationDataException 
     * @throws InvalidTransportDataException 
     */
    public static void main(String[] args) throws InvalidClientDataException, InvalidTripDataException, 
        InvalidTransportDataException, InvalidAccommodationDataException {

        System.out.println("Welcome to the SmartTravel Program!");
        System.out.println();
        
        // Prompt user to choose between predefined scenario or manual setup
        System.out.print("Would you like to continue with a predefined scenario or no (TESTING) (1 = yes, 2 = no)? ");
        choice = scanner.nextInt();

        // Initialize system based on user choice
        switch (choice) {
            case 1 -> {
                SmartTravelService.testingScenario(true);
                // Display all loaded entities
                System.out.println();
                System.out.println("=== Predefined Scenario Loaded ===");
                System.out.println("Clients (>=3):");
                System.out.println(SmartTravelService.printArray(ArrayType.CLIENTS));

                System.out.println("Trips (>=3):");
                System.out.println(SmartTravelService.printArray(ArrayType.TRIPS));

                System.out.println("Transportation (>=2 per type):");
                System.out.println(SmartTravelService.printArray(ArrayType.TRANSPORTATIONS));

                System.out.println("Accomodation (>=2 per type):");
                System.out.println(SmartTravelService.printArray(ArrayType.ACCOMODATIONS));

                // Demonstrate polymorphism: base-class references calling overridden methods
                System.out.println("Polymorphism Demo (base class references):");
                System.out.println(SmartTravelService.printArray(ArrayType.TRANSPORTATIONS));
                System.out.println(SmartTravelService.printArray(ArrayType.ACCOMODATIONS));
                System.out.println("==================================");
            }
            case 2 -> {
                // MANUAL SETUP: Initialize empty arrays for user to populate
                SmartTravelService.testingScenario(false);
            }
            default -> {
                // INVALID CHOICE: Default to manual setup
                System.out.println("Option invalid. Going default route.");
                SmartTravelService.testingScenario(false);
            }
        }

        // Main menu loop: continues until user selects exit (option 7)
        do{
            System.out.print("""
                What would you like to access?
                1. Client Managment
                2. Trip Managment
                3. Transportation Managment
                4. Accomodation Managment
                5. Additional Operations
                6. Exit Program
                
                >  Please enter option: """);

            choice = scanner.nextInt();
            System.out.println();

            // Route user input to appropriate management function
            switch (choice) {
                case 1 -> backToMain = clientManagment();      // Manage clients
                case 2 -> backToMain = tripManagment();        // Manage trips
                case 3 -> backToMain = transportManagment();   // Manage transportation
                case 4 -> backToMain = accomodationManagment(); // Manage accommodations
                case 5 -> backToMain = additionalOperations(); // Perform additional operations
                //case 6 -> backToMain = generateVisuals();
                case 6 -> backToMain = false;                  // Exit program
                default -> {
                    System.out.println("");
                    backToMain = true; // Invalid choice, return to menu
                }
            }
        }
        while (backToMain);

        // Clean up resources
        scanner.close();
        System.out.println();
        System.out.println("Program Termination Succesfull!");
    }

    /**
     * Manages client-related operations (Add, Edit, Delete, List).
     * Allows users to create new clients, modify existing client information,
     * remove clients from the system, or list all registered clients.
     * 
     * @return true to continue main menu loop, false to exit
     */
    private static boolean clientManagment() throws InvalidClientDataException {
        backToMain = true;

        do {
            System.out.println("""
                    What would you like to do?
                    1. Add a client
                    2. Edit a client
                    3. Delete a client
                    4. List all clients
                    5. Exit
                    >. Please enter option: """);
            
            choice = scanner.nextInt();
    
                switch (choice) {
                    case 1 -> {
                        backToSubmenu = true;

                        System.out.println(">. Please enter client details as follows: first name, last name, email adress:");
                        scanner.nextLine(); // Consume remaining newline
        
                        System.out.print(">. First Name: ");
                        String firstName = scanner.nextLine();
                        System.out.println();
        
                        System.out.print(">. Last Name: ");
                        String lastName = scanner.nextLine();
                        System.out.println();
        
                        System.out.print(">. Email Adress: ");
                        String emailAdress = scanner.nextLine();
                        System.out.println();

                        SmartTravelService.addClient(firstName, lastName, emailAdress);
                        System.out.println();
                        System.out.println(">. Client added successfully.");

                    }
                case 2 -> {
                    backToSubmenu = true;

                    System.out.println(SmartTravelService.printArray(ArrayType.CLIENTS));
                    System.out.println();
                    System.out.print(">. Please enter client id who's information you would like to update: ");
                    clientID = scanner.next();

                    boolean exist = SmartTravelService.clientExists(clientID);
                    
                    if (!exist) {
                        System.out.println(">. Client not found.");
                        break;
                    }
                    else {
                        System.out.println();
                        System.out.print(">. Updated first name: ");
                        String firstName = scanner.next();
    
                        System.out.println();
                        System.out.print(">. Updated last name: ");
                        String lastName = scanner.next();
                        
                        System.out.println();
                        System.out.print(">. Updated email adress: ");
                        String emailAdress = scanner.next();
                        
                        //WTF DOES CHECK DO HERE??????????? Have to modify somewhere else 
                        client[check].setFirstName(firstName);
                        client[check].setLastName(lastName);
                        client[check].setEmailAdress(emailAdress);

                        System.out.println(">. Client info updated succesfully.");
                        System.out.println();
                    }
                }
                case 3 -> {
                    backToSubmenu = true;

                    System.out.println(SmartTravelService.printArray(ArrayType.CLIENTS));
                    System.out.println();
                    System.out.print(">. Please enter client ID who's information you would like to delete: ");
                    clientID = scanner.next();

                    check = SmartTravelService.itemExistCheck(ArrayType.CLIENTS, clientID);

                    if (check == SmartTravelService.nullIndex) {
                        System.out.println(">. Client not found.");
                        break;
                    }
                    else {
                        // Create new array one element smaller
                        Client[] updatedList = new Client[client.length - 1];

                        // Copy elements: if deleted index is 0, copy rest; otherwise copy before and after
                        if (check == 0)
                            System.arraycopy(client, 1, updatedList, 0, client.length - 1);
                        else {
                            System.arraycopy(client, 0, updatedList, 0, check); // Copy before deleted element
                            System.arraycopy(client, check + 1, updatedList, check, client.length - check - 1); // Copy after
                        }
                        client = updatedList; // Replace old array with updated array
                        System.out.println(">. Client deleted succesfully.");
                    }
                }
                case 4 -> {
                    backToSubmenu = true;
                    System.out.println(SmartTravelService.printArray(ArrayType.CLIENTS));
                }
                case 5 -> backToSubmenu = false;
                default -> {
                    backToSubmenu = true;
                    System.out.println(">. Please reenter an option.");
                    System.out.println();
                }
            }

        } while(backToSubmenu);

        return backToMain;
    }

    /**
     * Manages trip-related operations (Create, Edit, Cancel, List, Filter by Client).
     * Allows users to create new trips, modify trip details, cancel trips,
     * list all trips, or view trips specific to a client.
     * 
     * @return true to continue main menu loop, false to exit
     */
    private static boolean tripManagment() throws InvalidTripDataException {
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
            
            choice = scanner.nextInt();
    
            switch (choice) {
                case 1 -> {
                    backToSubmenu = true;

                    System.out.println(">. Please enter trip details as follows: destination, duration, base price, and client you would like to assign it to: ");
                    scanner.nextLine();
    
                    System.out.print(">. Destination: ");
                    String destination = scanner.nextLine();
                    System.out.println();
    
                    System.out.print(">. Duration (in days): ");
                    int duration = scanner.nextInt();
                    System.out.println();
    
                    System.out.print(">. Base Price: ");
                    double basePrice = scanner.nextDouble();
                    System.out.println();

                    System.out.println(SmartTravelService.printArray(ArrayType.CLIENTS));

                    System.out.println();
                    System.out.print(">. Please enter client id whom you'd like to link the trip to: ");
                    clientID = scanner.next();

                    try {
                        SmartTravelService.createTrip(destination, duration, basePrice, clientID);
                        System.out.println();
                        System.out.println(">. Trip added successfully.");
                    } catch (EntityNotFoundException | InvalidTripDataException e) {
                        System.out.println(">. Unable to create trip: " + e.getMessage());
                    }
                }
                case 2 -> {
                    backToSubmenu = true;

                    System.out.println(SmartTravelService.printArray(ArrayType.TRIPS));
                    System.out.print(">. Please enter trip id of trip you would like to update: ");
                    tripID = scanner.next();

                    check = SmartTravelService.itemExistCheck(ArrayType.TRIPS, tripID);

                    if (check == SmartTravelService.nullIndex) {
                        System.out.println(">. Trip not found.");
                        break;
                    }
                    else {
                        System.out.println();
                        System.out.print(">. Updated destination: ");
                        String destination = scanner.nextLine();
    
                        System.out.println();
                        System.out.print(">. Updated duration (in days): ");
                        int duration = scanner.nextInt();
                        
                        System.out.println();
                        System.out.print(">. Updated base price: ");
                        double basePrice = scanner.nextDouble();

                        trip[check].setDestination(destination);
                        trip[check].setDurationInDays(duration);
                        trip[check].setBasePrice(basePrice);

                        System.out.println(">. Trip info updated successfully.");
                        System.out.println();
                    }

                }
                case 3 -> {
                    backToSubmenu = true;

                    System.out.println(SmartTravelService.printArray(ArrayType.TRIPS));
                    System.out.println();
                    System.out.print(">. Please enter trip ID you would like to cancel: ");
                    tripID = scanner.next();

                    check = SmartTravelService.itemExistCheck(ArrayType.TRIPS, tripID);

                    if (check == SmartTravelService.nullIndex) {
                        System.out.println(">. Trip not found.");
                        break;
                    }
                    else {
                        Trip[] updatedList = new Trip[trip.length - 1];

                        if (check == 0)
                            System.arraycopy(trip, 1, updatedList, 0, trip.length - 1);
                        else {
                            System.arraycopy(trip, 0, updatedList, 0, check);
                            System.arraycopy(trip, check + 1, updatedList, check, trip.length - check - 1);
                        }
                        trip = updatedList;
                        System.out.println(">. Trip cancelled successfully.");
                    }
                }
                case 4 -> {
                    backToSubmenu = true;
                    System.out.println(SmartTravelService.printArray(ArrayType.TRIPS));
                }
                case 5 -> {
                    backToSubmenu = true;

                    System.out.println(SmartTravelService.printArray(ArrayType.CLIENTS));
                    System.out.println();
                    System.out.print(">. Please enter client ID to view their trips: ");
                    clientID = scanner.next();

                    check = SmartTravelService.itemExistCheck(ArrayType.CLIENTS, clientID);
                    
                    if (check == -1) {
                        System.out.println(">. Client not found.");
                        break;
                    }
                    else if (check == -2) {
                        System.out.println(">. No clients in list.");
                        break;
                    }
                    else {
                        System.out.println();
                        System.out.println(">. Trips for " + client[check].getFirstName() + " " + client[check].getLastName() + ":");
                        boolean foundTrips = false;
                        for (Trip t : trip) {
                            if (t != null && t.getClient().getClientID().equals(clientID)) {
                                System.out.println(">. " + t.toString());
                                foundTrips = true;
                            }
                        }
                        if (!foundTrips) {
                            System.out.println(">. No trips found for this client.");
                        }
                        System.out.println();
                    }
                }
                case 6 -> backToSubmenu = false;
                default -> {
                    backToSubmenu = true;
                    System.out.println(">. Please reenter an option.");
                    System.out.println();
                }
            }
        } 
        while (backToSubmenu);

        return backToMain;
    }

    /**
     * Manages transportation options (Add, Remove, List by Type, List All).
     * Demonstrates polymorphism through Transportation base class. Users can add flights, trains, 
     * or buses; remove existing transportation options; and filter by transportation type or view all.
     * Uses dynamic array resizing with System.arraycopy for memory-efficient operations.
     * 
     * @return true to continue main menu loop, false to exit
     */
    private static boolean transportManagment() throws InvalidTransportDataException {
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

            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    backToSubmenu = true;
                    
                    System.out.println("""
                            Which type of transportation would you like to add?
                            1. Flight
                            2. Train
                            3. Bus
                            >. Please enter option: """);
                    int transportType = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print(">. Company Name: ");
                    String companyName = scanner.nextLine();

                    System.out.print(">. Departure City: ");
                    String departureCity = scanner.nextLine();

                    System.out.print(">. Arrival City: ");
                    String arrivalCity = scanner.nextLine();

                    Transportation newTransport = null;

                    switch (transportType) {
                        case 1 -> {
                            System.out.print(">. Airline Name: ");
                            String airlineName = scanner.nextLine();
                            System.out.print(">. Luggage Allowance (kg): ");
                            double luggageAllowance = scanner.nextDouble();
                            newTransport = new Flight(companyName, departureCity, arrivalCity, airlineName, luggageAllowance);
                        }
                        case 2 -> {
                            System.out.print(">. Train Type: ");
                            String trainType = scanner.nextLine();
                            System.out.print(">. Seat Class: ");
                            String seatClass = scanner.nextLine();
                            newTransport = new Train(companyName, departureCity, arrivalCity, trainType, seatClass);
                        }
                        case 3 -> {
                            System.out.print(">. Bus Company: ");
                            String busCompany = scanner.nextLine();
                            System.out.print(">. Number of Stops: ");
                            int numOfStops = scanner.nextInt();
                            newTransport = new Bus(companyName, departureCity, arrivalCity, busCompany, numOfStops);
                        }
                        default -> System.out.println(">. Invalid option.");
                    }

                    if (newTransport != null) {
                        Transportation[] updatedList = new Transportation[transportation.length + 1];
                        System.arraycopy(transportation, 0, updatedList, 0, transportation.length);
                        updatedList[transportation.length] = newTransport;
                        transportation = updatedList;
                        System.out.println(">. Transportation added successfully.");
                    }
                }
                case 2 -> {
                    backToSubmenu = true;

                    System.out.println(SmartTravelService.printArray(ArrayType.TRANSPORTATIONS));
                    System.out.println();
                    System.out.print(">. Please enter transportation ID to remove: ");
                    String transportID = scanner.next();

                    check = SmartTravelService.itemExistCheck(ArrayType.TRANSPORTATIONS, transportID);

                    if (check == SmartTravelService.nullIndex) {
                        System.out.println(">. Transportation not found.");
                        break;
                    }
                    else {
                        Transportation[] updatedList = new Transportation[transportation.length - 1];

                        if (check == 0)
                            System.arraycopy(transportation, 1, updatedList, 0, transportation.length - 1);
                        else {
                            System.arraycopy(transportation, 0, updatedList, 0, check);
                            System.arraycopy(transportation, check + 1, updatedList, check, transportation.length - check - 1);
                        }
                        transportation = updatedList;
                        System.out.println(">. Transportation removed successfully.");
                    }
                }
                case 3 -> {
                    backToSubmenu = true;
                    
                    System.out.println("""
                            Which type would you like to view?
                            1. Flight
                            2. Train
                            3. Bus
                            >. Please enter option: """);
                    int filterType = scanner.nextInt();

                    System.out.println();
                    boolean found = false;
                    for (Transportation t : transportation) {
                        if (t != null) {
                            if ((filterType == 1 && t instanceof Flight) ||
                                (filterType == 2 && t instanceof Train) ||
                                (filterType == 3 && t instanceof Bus)) {
                                System.out.println(">. " + t.toString());
                                found = true;
                            }
                        }
                    }
                    if (!found) {
                        System.out.println(">. No transportation options found for this type.");
                    }
                    System.out.println();
                }
                case 4 -> {
                    backToSubmenu = true;
                    System.out.println(SmartTravelService.printArray(ArrayType.TRANSPORTATIONS));
                }
                case 5 -> backToSubmenu = false;
                default -> {
                    backToSubmenu = true;
                    System.out.println(">. Please reenter an option.");
                    System.out.println();
                }
            }
        } while (backToSubmenu);

        return backToMain;
    }

    /**
     * Manages accommodation options (Add, Remove, List by Type, List All).
     * Demonstrates polymorphism through Accomodation base class. Users can add hotels or hostels;
     * remove existing accommodations; filter by type or view all accommodations.
     * Uses dynamic array resizing with System.arraycopy for memory-efficient operations.
     * 
     * @return true to continue main menu loop, false to exit
     */
    private static boolean accomodationManagment() throws InvalidAccommodationDataException {
        do {
            backToSubmenu = true;

            System.out.println("""
                    What would you like to do?
                    1. Add an accomodation
                    2. Remove an accomodation
                    3. List accomodations by type (Hotel, Hostel)
                    4. List all accomodations
                    5. Exit
                    >. Please enter option: """);

            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    backToSubmenu = true;
                    
                    System.out.println("""
                            Which type of accomodation would you like to add?
                            1. Hotel
                            2. Hostel
                            >. Please enter option: """);
                    int accomType = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print(">. Name: ");
                    String name = scanner.nextLine();

                    System.out.print(">. Location: ");
                    String location = scanner.nextLine();

                    System.out.print(">. Price Per Night: ");
                    double pricePerNight = scanner.nextDouble();

                    Accomodation newAccomodation = null;

                    switch (accomType) {
                        case 1 -> {
                            System.out.print(">. Star Rating: ");
                            double starRating = scanner.nextDouble();
                            newAccomodation = new Hotel(name, location, pricePerNight, starRating);
                        }
                        case 2 -> {
                            System.out.print(">. Number of Shared Beds: ");
                            int numOfSharedBeds = scanner.nextInt();
                            newAccomodation = new Hostel(name, location, pricePerNight, numOfSharedBeds);
                        }
                        default -> System.out.println(">. Invalid option.");
                    }

                    if (newAccomodation != null) {
                        Accomodation[] updatedList = new Accomodation[accomodation.length + 1];
                        System.arraycopy(accomodation, 0, updatedList, 0, accomodation.length);
                        updatedList[accomodation.length] = newAccomodation;
                        accomodation = updatedList;
                        System.out.println(">. Accomodation added successfully.");
                    }
                }
                case 2 -> {
                    backToSubmenu = true;

                    System.out.println(SmartTravelService.printArray(ArrayType.ACCOMODATIONS));
                    System.out.println();
                    System.out.print(">. Please enter accomodation ID to remove: ");
                    String accomID = scanner.next();

                    check = SmartTravelService.itemExistCheck(ArrayType.ACCOMODATIONS, accomID);

                    if (check == -1) {
                        System.out.println(">. Accomodation not found.");
                        break;
                    }
                    else if (check == -2) {
                        System.out.println(">. No accomodations in list.");
                        break;
                    }
                    else {
                        Accomodation[] updatedList = new Accomodation[accomodation.length - 1];

                        if (check == 0)
                            System.arraycopy(accomodation, 1, updatedList, 0, accomodation.length - 1);
                        else {
                            System.arraycopy(accomodation, 0, updatedList, 0, check);
                            System.arraycopy(accomodation, check + 1, updatedList, check, accomodation.length - check - 1);
                        }
                        accomodation = updatedList;
                        System.out.println(">. Accomodation removed successfully.");
                    }
                }
                case 3 -> {
                    backToSubmenu = true;
                    
                    System.out.println("""
                            Which type would you like to view?
                            1. Hotel
                            2. Hostel
                            >. Please enter option: """);
                    int filterType = scanner.nextInt();

                    System.out.println();
                    boolean found = false;
                    for (Accomodation a : accomodation) {
                        if (a != null) {
                            if ((filterType == 1 && a instanceof Hotel) ||
                                (filterType == 2 && a instanceof Hostel)) {
                                System.out.println(">. " + a.toString());
                                found = true;
                            }
                        }
                    }
                    if (!found) {
                        System.out.println(">. No accomodations found for this type.");
                    }
                    System.out.println();
                }
                case 4 -> {
                    backToSubmenu = true;
                    System.out.println(SmartTravelService.printArray(ArrayType.ACCOMODATIONS));
                }
                case 5 -> backToSubmenu = false;
                default -> {
                    backToSubmenu = true;
                    System.out.println(">. Please reenter an option.");
                    System.out.println();
                }
            }
        } while (backToSubmenu);

        return backToMain;
    }

    /**
     * Performs additional operations on system data.
     * Includes: finding the most expensive trip, calculating trip total costs,
     * and creating deep copies of transportation and accommodation arrays.
     * Demonstrates deep copy techniques with polymorphic casting and constructor copying.
     * 
     * @return true to continue main menu loop, false to exit
     */
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

            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    backToSubmenu = true;

                    if (trip.length == 0) {
                        System.out.println(">. No trips in list.");
                        break;
                    }

                    // Find the trip with maximum total cost by iterating through array
                    Trip mostExpensive = trip[0];
                    for (Trip t : trip) {
                        if (t != null && t.calculateTotalCost() > mostExpensive.calculateTotalCost()) {
                            mostExpensive = t;
                        }
                    }

                    System.out.println();
                    System.out.println(">. Most Expensive Trip:");
                    System.out.println(">. " + mostExpensive.toString());
                    System.out.println(">. Total Cost: $" + String.format("%.2f", mostExpensive.calculateTotalCost()));
                    System.out.println();
                }
                case 2 -> {
                    backToSubmenu = true;

                    System.out.println(SmartTravelService.printArray(ArrayType.TRIPS));
                    System.out.println();
                    System.out.print(">. Please enter trip ID to calculate total cost: ");
                    tripID = scanner.next();

                    check = SmartTravelService.itemExistCheck(ArrayType.TRIPS, tripID);

                    if (check == -1) {
                        System.out.println(">. Trip not found.");
                        break;
                    }
                    else if (check == -2) {
                        System.out.println(">. No trips in list.");
                        break;
                    }
                    else {
                        double totalCost = trip[check].calculateTotalCost();
                        System.out.println();
                        System.out.println(">. Total Cost for Trip " + tripID + ": $" + String.format("%.2f", totalCost));
                        System.out.println();
                    }
                }
                case 3 -> {
                    backToSubmenu = true;

                    // Create deep copy of transportation array using polymorphic casting
                    Transportation[] deepCopy = new Transportation[transportation.length];
                    for (int i = 0; i < transportation.length; i++) {
                        // Check actual runtime type and create appropriate copy using copy constructor
                        if (transportation[i] instanceof Flight) {
                            deepCopy[i] = new Flight((Flight) transportation[i]);
                        } else if (transportation[i] instanceof Train) {
                            deepCopy[i] = new Train((Train) transportation[i]);
                        } else if (transportation[i] instanceof Bus) {
                            deepCopy[i] = new Bus((Bus) transportation[i]);
                        }
                    }

                    System.out.println();
                    System.out.println(">. Deep copy of transportation array created successfully.");
                    System.out.println(">. Original array length: " + transportation.length);
                    System.out.println(">. Deep copy array length: " + deepCopy.length);
                    System.out.println();
                }
                case 4 -> {
                    backToSubmenu = true;

                    // Create deep copy of accommodation array using polymorphic casting
                    Accomodation[] deepCopy = new Accomodation[accomodation.length];
                    for (int i = 0; i < accomodation.length; i++) {
                        // Check actual runtime type and create appropriate copy using copy constructor
                        if (accomodation[i] instanceof Hotel) {
                            deepCopy[i] = new Hotel((Hotel) accomodation[i]);
                        } else if (accomodation[i] instanceof Hostel) {
                            deepCopy[i] = new Hostel((Hostel) accomodation[i]);
                        }
                    }

                    System.out.println();
                    System.out.println(">. Deep copy of accommodation array created successfully.");
                    System.out.println(">. Original array length: " + accomodation.length);
                    System.out.println(">. Deep copy array length: " + deepCopy.length);
                    System.out.println();
                }
                case 5 -> backToSubmenu = false;
                default -> {
                    backToSubmenu = true;
                    System.out.println(">. Please reenter an option.");
                    System.out.println();
                }
            }
        } while (backToSubmenu);

        return backToMain;
    }
    
    // private static boolean generateVisuals() {
    //     do {
    //         backToSubmenu = true;

    //         System.out.println("""
    //                 What would you like to do?
    //                 1. Bar chart (Trip Cost)
    //                 2. Pie chart (Trips per destination)
    //                 3. Line chart (Duration over time)
    //                 4. Exit
    //                 >. Please enter option: """);

    //         choice = scanner.nextInt();

    //         switch (choice) {
    //             case 1 -> {
    //                 backToSubmenu = true;

    //                 if (trip.length == 0) {
    //                     System.out.println(">. No trips available to generate chart.");
    //                     break;
    //                 }

    //                 try {
    //                     TripChartGenerator.generateCostBarChart(trip, trip.length);
    //                     System.out.println(">. Bar chart generated successfully: output/trip_cost_bar_chart.png");
    //                 } catch (IOException e) {
    //                     System.out.println(">. Error generating chart: " + e.getMessage());
    //                 }
    //             }
    //             case 2 -> {
    //                 backToSubmenu = true;

    //                 if (trip.length == 0) {
    //                     System.out.println(">. No trips available to generate chart.");
    //                     break;
    //                 }

    //                 try {
    //                     TripChartGenerator.generateDestinationPieChart(trip, trip.length);
    //                     System.out.println(">. Pie chart generated successfully: output/trips_per_destination_pie.png");
    //                 } catch (IOException e) {
    //                     System.out.println(">. Error generating chart: " + e.getMessage());
    //                 }
    //             }
    //             case 3 -> {
    //                 backToSubmenu = true;

    //                 if (trip.length == 0) {
    //                     System.out.println(">. No trips available to generate chart.");
    //                     break;
    //                 }

    //                 try {
    //                     TripChartGenerator.generateDurationLineChart(trip, trip.length);
    //                     System.out.println(">. Line chart generated successfully: output/trip_duration_line_chart.png");
    //                 } catch (IOException e) {
    //                     System.out.println(">. Error generating chart: " + e.getMessage());
    //                 }
    //             }
    //             case 4 -> backToSubmenu = false;
    //             default -> {
    //                 backToSubmenu = true;
    //                 System.out.println(">. Please reenter an option.");
    //                 System.out.println();
    //             }
    //         }
    //     } while (backToSubmenu);

    //     return backToMain;
    // }

    
}
