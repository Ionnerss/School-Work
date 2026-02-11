package AssignmentsS2.Assignment1.src.driver;

import AssignmentsS2.Assignment1.src.client.Client;
import AssignmentsS2.Assignment1.src.travel.Accomodation;
import AssignmentsS2.Assignment1.src.travel.Transportation;
import AssignmentsS2.Assignment1.src.travel.Trip;
import java.util.Scanner;

public class Main {
    private static Client[] client;
    private static Trip[] trip;
    private static Transportation[] transportation;
    private static Accomodation[] accomodation;
    
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean backToMain, backToSubmenu, testing;
    private static int choice, index;

    public static void main(String[] args) {
        System.out.println("Welcome to the SmartTravel Program!");
        System.out.println();
        
        System.out.print("Would you like to continue with a predefined scenario or no (TESTING) (1 = yes, 2 = no)? ");
        choice = scanner.nextInt();

        switch (choice) {
            case 1 -> testing = true;
            case 2 -> testing = false;
            default -> testing = false;
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
                    case 1:
                        backToSubmenu = true;

                        System.out.println(">. Please enter client details as follows: first name, last name, email adress:");
                        scanner.nextLine();
        
                        System.out.print(">. First Name: ");
                        String firstName = scanner.nextLine();
                        System.out.println();
        
                        System.out.print(">. Last Name: ");
                        String lastName = scanner.nextLine();
                        System.out.println();
        
                        System.out.print("Email Adress: ");
                        String emailAdress = scanner.nextLine();
                        System.out.println();
        
                        index = client.length;
                        client[index] = new Client();
                        client[index].setFirstName(firstName);
                        client[index].setLastName(lastName);
                        client[index].setEmailAdress(emailAdress);
                        
                        System.out.println();
                        System.out.println("Client added successfully.");
                        break;
                case 2:
                    backToSubmenu = true;

                    index = 1;
                    for (Client person : client) {
                        System.out.println(index++ + ". " + person.toString());
                    }
                    System.out.println();
                    System.out.print(">. Please enter client who's information you would like to update: ");
                    choice = scanner.nextInt();
                    
                    System.out.println();
                    System.out.print(">. Updated first name: ");
                    firstName = scanner.next();

                    System.out.println();
                    System.out.print(">. Updated last name: ");
                    lastName = scanner.next();
                    
                    System.out.println();
                    System.out.print(">. Updated email adress: ");
                    emailAdress = scanner.next();
    
                    Client newClient = new Client(firstName, lastName, emailAdress);
                    addInfo(newClient);
    
                    System.out.println("Client info updated succesfully.");
                    System.out.println();
                    break;
                case 3:
                    backToSubmenu = true;






                    // public boolean removeGC(int whichCard) {
                    //     GymCard[] updatedList = new GymCard[this.gymCards.length - 1];

                    //     if (whichCard == 0) {
                    //         System.arraycopy(this.gymCards, 1, updatedList, 0, this.gymCards.length - 1);
                    //     }
                    //     else if (whichCard == 1) {
                    //         System.arraycopy(this.gymCards, 0, updatedList, 0, whichCard);
                    //     }
                    //     else {
                    //         System.arraycopy(this.gymCards, 0, updatedList, 0, whichCard - 1);
                    //         System.arraycopy(this.gymCards, whichCard, updatedList, whichCard - 1, (this.gymCards.length - (whichCard + 1)));
                    //     }

                    //     if (updatedList.length >= this.gymCards.length)
                    //         return false;
                    //     else {
                    //         this.gymCards = updatedList;
                    //         return true;
                    //     }
                    // }













                    index = 1;
                    for (Client person : client) {
                        System.out.println(index++ + ". " + person.toString());
                    }

                    System.out.println();
                    System.out.print(">. Please enter client who's information you would like to delete: ");
                    choice = scanner.nextInt();





                    break;
                case 4:
                    break;
                case 5:
                    break;
                default:
                    break;
            }

        }
        while(backToSubmenu);

        return backToMain;
    }

    private static boolean tripManagment() {
        System.out.println("""
                What would you like to do?
                1. Create a trip
                2. Edit trip information
                3. Cancel a trip
                4. List all trips
                5. List all trips for a specific client
                6. Generate Visualization
                5. Exit
                >. Please enter option: """);
        
        choice = scanner.nextInt();

        switch (choice) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            default:
                break;
        }
        return backToMain;
    }

    private static boolean transportManagment() {
        System.out.println("""
                What would you like to do?
                1. Add a transportation option
                2. Remove a transportation option
                3. List transportation options by type (Flight, Train, Bus)
                5. Exit
                >. Please enter option: """);

        choice = scanner.nextInt();

        switch (choice) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            default:
                break;
        }

        return backToMain;
    }

    private static boolean accomodationManagment() {
        System.out.println("""
                What would you like to do?
                1. Add an accomodation
                2. Remove an accomodation
                3. List accomodations by type (Hotel, Hostel)
                5. Exit
                >. Please enter option: """);

        choice = scanner.nextInt();

        switch (choice) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            default:
                break;
        }

        return backToMain;
    }

    private static boolean additionalOperations() {
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
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            default:
                break;
        }

        return backToMain;
    }
    
    private static boolean generateVisuals() {
        System.out.println("""
                What would you like to do?
                1. Bar chart (Trip Cost)
                2. Pie chart (Trips per destination)
                3. Line chart (Duration over time)
                5. Exit
                >. Please enter option: """);

        choice = scanner.nextInt();

        switch (choice) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            default:
                break;
        }

        return backToMain;
    }

    private static void addInfo(Object newInfo) {
        if (!testing) {
            if (newInfo instanceof Client) {
                if (client.length == 0) {
                    client = new Client[1];
                    client[0] = (Client) newInfo;

                }
                else {
                    Client[] clientCopy = new Client[client.length + 1];
                    System.arraycopy(client, 0, clientCopy, 0, client.length);
                    clientCopy[client.length] = (Client) newInfo;
                    client = clientCopy;
                }
            }
            else if (newInfo instanceof Trip) {
                if (trip.length == 0) {
                    trip = new Trip[1];
                    trip[0] = (Trip) newInfo;

                }
                else {
                    Trip[] tripCopy = new Trip[trip.length + 1];
                    System.arraycopy(trip, 0, tripCopy, 0, trip.length);
                    tripCopy[trip.length] = (Trip) newInfo;
                    trip = tripCopy;
                }
            }
            else if (newInfo instanceof Transportation) {
                if (transportation.length == 0) {
                    transportation = new Transportation[1];
                    transportation[0] = (Transportation) newInfo;

                }
                else {
                    Transportation[] transportationCopy = new Transportation[transportation.length + 1];
                    System.arraycopy(client, 0, transportationCopy, 0, transportation.length);
                    transportationCopy[transportation.length] = (Transportation) newInfo;
                    transportation = transportationCopy;
                }
            }
            else if (newInfo instanceof Accomodation) {
                if (accomodation.length == 0) {
                    accomodation = new Accomodation[1];
                    accomodation[0] = (Accomodation) newInfo;

                }
                else {
                    Accomodation[] accomodationCopy = new Accomodation[accomodation.length + 1];
                    System.arraycopy(accomodation, 0, accomodationCopy, 0, accomodation.length);
                    accomodationCopy[accomodation.length] = (Accomodation) newInfo;
                    accomodation = accomodationCopy;
                }
            }
        }
    }
}
