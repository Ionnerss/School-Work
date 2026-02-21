package AssignmentsS2.Assignment1.src.driver;

import AssignmentsS2.Assignment1.src.client.Client;
import AssignmentsS2.Assignment1.src.travel.*;
import AssignmentsS2.Assignment1.src.visualization.TripChartGenerator;
import java.util.Scanner;

public class Main {
    private static Client[] client;
    private static Trip[] trip;
    private static Transportation[] transportation;
    private static Accomodation[] accomodation;
    
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean backToMain, backToSubmenu, testing;
    private static int choice, index, check;
    private static String clientID, tripID;

    public static void main(String[] args) {
        System.out.println("Welcome to the SmartTravel Program!");
        System.out.println();
        
        System.out.print("Would you like to continue with a predefined scenario or no (TESTING) (1 = yes, 2 = no)? ");
        choice = scanner.nextInt();

        switch (choice) {
            case 1 -> testing = true;
            case 2 -> {
                testing = false;
                client = new Client[1];
                trip = new Trip[1];
                transportation = new Transportation[1];
                accomodation = new Accomodation[1];
            }
            default -> {
                testing = false;
                testing = false;
                client = new Client[1];
                trip = new Trip[1];
                transportation = new Transportation[1];
                accomodation = new Accomodation[1];
            }
        }

        do{
            System.out.print("""
                What would you like to access?
                1. Client Managment
                2. Trip Managment
                3. Transportation Managment
                4. Accomodation Managment
                5. Additional Operations
                6. Generate Visualization
                7. Exit Program
                
                >  Please enter option: """);

            choice = scanner.nextInt();
            System.out.println();

            switch (choice) {
                case 1 -> backToMain = clientManagment();
                case 2 -> backToMain = tripManagment();
                case 3 -> backToMain = transportManagment();
                case 4 -> backToMain = accomodationManagment();
                case 5 -> backToMain = additionalOperations();
                case 6 -> backToMain = generateVisuals();
                case 7 -> backToMain = false;
                default -> {
                    System.out.println("");
                    backToMain = true;
                }
            }
        }
        while (backToMain);

        scanner.close();
        System.out.println();
        System.out.println("Program Termination Succesfull!");
    }

    private static boolean clientManagment() {
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
                        scanner.nextLine();
        
                        System.out.print(">. First Name: ");
                        String firstName = scanner.nextLine();
                        System.out.println();
        
                        System.out.print(">. Last Name: ");
                        String lastName = scanner.nextLine();
                        System.out.println();
        
                        System.out.print(">. Email Adress: ");
                        String emailAdress = scanner.nextLine();
                        System.out.println();
        
                        index = client.length;
                        client[index + 1] = new Client(firstName, lastName, emailAdress);
                        
                        System.out.println();
                        System.out.println(">. Client added successfully.");
                    }
                case 2 -> {
                    backToSubmenu = true;

                    printClient();
                    System.out.println();
                    System.out.print(">. Please enter client id who's information you would like to update: ");
                    clientID = scanner.next();

                    check = clientExistCheck(clientID);
                    
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
                        System.out.print(">. Updated first name: ");
                        String firstName = scanner.next();
    
                        System.out.println();
                        System.out.print(">. Updated last name: ");
                        String lastName = scanner.next();
                        
                        System.out.println();
                        System.out.print(">. Updated email adress: ");
                        String emailAdress = scanner.next();
        
                        client[check].setFirstName(firstName);
                        client[check].setLastName(lastName);
                        client[check].setEmailAdress(emailAdress);

                        System.out.println(">. Client info updated succesfully.");
                        System.out.println();
                    }
                }
                case 3 -> {
                    backToSubmenu = true;

                    printClient();
                    System.out.println();
                    System.out.print(">. Please enter client ID who's information you would like to delete: ");
                    clientID = scanner.next();

                    check = clientExistCheck(clientID);

                    if (check == -1) {
                        System.out.println(">. Client not found.");
                        break;
                    }
                    else if (check == -2) {
                        System.out.println(">. No clients in list.");
                        break;
                    }
                    else {
                        Client[] updatedList = new Client[client.length - 1];

                        if (check == 0)
                            System.arraycopy(client, 1, updatedList, 0, client.length - 1);
                        else {
                            System.arraycopy(client, 0, updatedList, 0, check);
                            System.arraycopy(client, check + 1, updatedList, check, client.length - check - 1);
                        }
                        client = updatedList;
                        System.out.println(">. Client deleted succesfully.");
                    }
                }
                case 4 -> {
                    backToSubmenu = true;
                    printClient();
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

    private static boolean tripManagment() {
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

                    printClient();

                    System.out.println();
                    System.out.print(">. Please enter client id whom you'd like to link the trip to: ");
                    clientID = scanner.next();

                    check = clientExistCheck(clientID);
                    
                    if (check == -1) {
                        System.out.println(">. Client not found.");
                        break;
                    }
                    else if (check == -2) {
                        System.out.println(">. No clients in list.");
                        break;
                    }
                    else {
                        index = trip.length;
                        trip[trip.length + 1] = new Trip(destination, duration, basePrice, client[check]);
                        System.out.println();
                        System.out.println(">. Trip added successfully.");
                    }
                }
                case 2 -> {
                    backToSubmenu = true;

                    printTrip();
                    System.out.print(">. Please enter trip id of trip you would like to update: ");
                    tripID = scanner.next();

                    check = tripExistCheck(tripID);

                    if (check == -1) {
                        System.out.println(">. Trip not found.");
                        break;
                    }
                    else if (check == -2) {
                        System.out.println(">. No trips in list.");
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

                    printTrip();
                    System.out.println();
                    System.out.print(">. Please enter trip ID you would like to cancel: ");
                    tripID = scanner.next();

                    check = tripExistCheck(tripID);

                    if (check == -1) {
                        System.out.println(">. Trip not found.");
                        break;
                    }
                    else if (check == -2) {
                        System.out.println(">. No trips in list.");
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
                    printTrip();
                }
                case 5 -> {
                    backToSubmenu = true;

                    printClient();
                    System.out.println();
                    System.out.print(">. Please enter client ID to view their trips: ");
                    clientID = scanner.next();

                    check = clientExistCheck(clientID);
                    
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

    private static boolean transportManagment() {
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
                        Transportation[] updatedList = Arrays.copyOf(transportation, transportation.length + 1);
                        updatedList[transportation.length] = newTransport;
                        transportation = updatedList;
                        System.out.println(">. Transportation added successfully.");
                    }
                }
                case 2 -> {
                    backToSubmenu = true;

                    printTransportation();
                    System.out.println();
                    System.out.print(">. Please enter transportation ID to remove: ");
                    String transportID = scanner.next();

                    check = transportExistCheck(transportID);

                    if (check == -1) {
                        System.out.println(">. Transportation not found.");
                        break;
                    }
                    else if (check == -2) {
                        System.out.println(">. No transportation in list.");
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
                    printTransportation();
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

    private static boolean accomodationManagment() {
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
                        Accomodation[] updatedList = Arrays.copyOf(accomodation, accomodation.length + 1);
                        updatedList[accomodation.length] = newAccomodation;
                        accomodation = updatedList;
                        System.out.println(">. Accomodation added successfully.");
                    }
                }
                case 2 -> {
                    backToSubmenu = true;

                    printAccomodation();
                    System.out.println();
                    System.out.print(">. Please enter accomodation ID to remove: ");
                    String accomID = scanner.next();

                    check = accomodationExistCheck(accomID);

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
                    printAccomodation();
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

                    printTrip();
                    System.out.println();
                    System.out.print(">. Please enter trip ID to calculate total cost: ");
                    tripID = scanner.next();

                    check = tripExistCheck(tripID);

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

                    Transportation[] deepCopy = new Transportation[transportation.length];
                    for (int i = 0; i < transportation.length; i++) {
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

                    Accomodation[] deepCopy = new Accomodation[accomodation.length];
                    for (int i = 0; i < accomodation.length; i++) {
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
    
    private static boolean generateVisuals() {
        do {
            backToSubmenu = true;

            System.out.println("""
                    What would you like to do?
                    1. Bar chart (Trip Cost)
                    2. Pie chart (Trips per destination)
                    3. Line chart (Duration over time)
                    4. Exit
                    >. Please enter option: """);

            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    backToSubmenu = true;

                    if (trip.length == 0) {
                        System.out.println(">. No trips available to generate chart.");
                        break;
                    }

                    try {
                        TripChartGenerator.generateCostBarChart(trip, trip.length);
                        System.out.println(">. Bar chart generated successfully: output/trip_cost_bar_chart.png");
                    } catch (IOException e) {
                        System.out.println(">. Error generating chart: " + e.getMessage());
                    }
                }
                case 2 -> {
                    backToSubmenu = true;

                    if (trip.length == 0) {
                        System.out.println(">. No trips available to generate chart.");
                        break;
                    }

                    try {
                        TripChartGenerator.generateDestinationPieChart(trip, trip.length);
                        System.out.println(">. Pie chart generated successfully: output/trips_per_destination_pie.png");
                    } catch (IOException e) {
                        System.out.println(">. Error generating chart: " + e.getMessage());
                    }
                }
                case 3 -> {
                    backToSubmenu = true;

                    if (trip.length == 0) {
                        System.out.println(">. No trips available to generate chart.");
                        break;
                    }

                    try {
                        TripChartGenerator.generateDurationLineChart(trip, trip.length);
                        System.out.println(">. Line chart generated successfully: output/trip_duration_line_chart.png");
                    } catch (IOException e) {
                        System.out.println(">. Error generating chart: " + e.getMessage());
                    }
                }
                case 4 -> backToSubmenu = false;
                default -> {
                    backToSubmenu = true;
                    System.out.println(">. Please reenter an option.");
                    System.out.println();
                }
            }
        } while (backToSubmenu);

        return backToMain;
    }

    private static void printClient() {
        System.out.println();
        for (Client person : client)
            System.out.println(">. " + person.toString());
        System.out.println();
    }

    private static void printTrip() {
        System.out.println();
        for (Trip trips : trip)
            System.out.println(">. " + trips.toString());
        System.out.println();
    }

    private static int clientExistCheck(String clientID) {
        index = -1;

        if (client.length == 0)
            return -2;

        for (int i = 0; i < client.length; i++) {
            if (client[i] != null && client[i].getClientID().equalsIgnoreCase(clientID)) {
                index = i;
                return index;
            }
        }   
        return index;
    }

    private static int tripExistCheck(String tripID) {
        index = -1;

        if (trip.length == 0)
            return -2;

        for (int i = 0; i < trip.length; i++) {
            if (trip[i] != null && trip[i].getTripId().equalsIgnoreCase(tripID)) {
                index = i;
                return index;
            }
        }   
        return index;
    }

    private static int transportExistCheck(String transportID) {
        index = -1;

        if (transportation.length == 0)
            return -2;

        for (int i = 0; i < transportation.length; i++) {
            if (transportation[i] != null && transportation[i].getTransportID().equalsIgnoreCase(transportID)) {
                index = i;
                return index;
            }
        }   
        return index;
    }

    private static int accomodationExistCheck(String accomodationID) {
        index = -1;

        if (accomodation.length == 0)
            return -2;

        for (int i = 0; i < accomodation.length; i++) {
            if (accomodation[i] != null && accomodation[i].getAccomodationID().equalsIgnoreCase(accomodationID)) {
                index = i;
                return index;
            }
        }   
        return index;
    }

    private static void printTransportation() {
        System.out.println();
        if (transportation.length == 0) {
            System.out.println(">. No transportation options available.");
        } else {
            for (Transportation t : transportation) {
                if (t != null) {
                    System.out.println(">. " + t.toString());
                }
            }
        }
        System.out.println();
    }

    private static void printAccomodation() {
        System.out.println();
        if (accomodation.length == 0) {
            System.out.println(">. No accomodations available.");
        } else {
            for (Accomodation a : accomodation) {
                if (a != null) {
                    System.out.println(">. " + a.toString());
                }
            }
        }
        System.out.println();
    }
}
