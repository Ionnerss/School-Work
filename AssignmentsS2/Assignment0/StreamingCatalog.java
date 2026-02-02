package AssignmentsS2.Assignment0;

import java.util.Scanner;

public class StreamingCatalog {

    public static Scanner scanner = new Scanner(System.in);
    private static Show[] catalog;
    private static final String password = "STREAM";
    private static String attempt;
    private static boolean backToMain, noShows, consecutive;
    private static int counter = 0, currentSesh = 0;

    public static void main(String[] args) {
        System.out.println("Welcome to the Streaming Service Catalog Manager!");
        System.out.println();
        System.out.println("What is the maximum amount of shows you would like to add? ");
        int maxShows = scanner.nextInt();
        catalog = new Show[maxShows];


        do
            backToMain = mainMenu();
        while (backToMain);

        scanner.close();
        System.out.println();
        System.out.println("Thank you for using the Streaming catalog!!!");
    }
    public static boolean checkShowIndex(int index) {
        if (index >= catalog.length || catalog[index] == null)
            return false;
        return true;
    }

    public static boolean mainMenu() {
        System.out.println("What do you want to do?\n" + //
                        "1. Enter new Shows (password required)\n" + //
                        "2. Change information of a Show (password required)\n" + //
                        "3. Display all Shows of a specific genre\n" + //
                        "4. Display all Shows with a rating above a certain value.\n" + //
                        "5. Quit\n" + //
                        "\n" +
                        "Please enter your choice > ");

        int choice = scanner.nextInt();
        switch(choice) {
            case 1:
                backToMain = true;
                System.out.println("Please enter password: ");
                attempt = scanner.next();
                
                consecutive = true;
                currentSesh = 0;
                if (!password(attempt)) {
                    System.out.println("Too many attempts.");
                    break;
                }
                System.out.println("How many shows would you like to enter? ");
                int nbShows = scanner.nextInt();

                int space = 0;
                for (Show show : catalog)
                    if (show == null)
                        space++;
            
                if (nbShows > space) {
                    System.out.println("Unable to add this many shows (" + nbShows + "). There are " + space + " slots left in the catalog.");
                    break;
                }
                else {
                    System.out.println("Please enter show details as follows: Title, genre, year, and rating.");
                    for (int i = 0; i < nbShows; i++) {
                        scanner.nextLine();
                        System.out.print("Title: ");
                        String title = scanner.nextLine();

                        System.out.print("Genre: ");
                        String genre = scanner.nextLine();

                        System.out.print("Year: ");
                        int year = scanner.nextInt();

                        System.out.print("Rating: ");
                        double rating = scanner.nextDouble();

                        int index = catalog.length - space + i + 1;
                        catalog[index] = new Show();
                        catalog[index].setTitle(title);
                        catalog[index].setGenre(genre);
                        catalog[index].setYear(year);
                        catalog[index].setRating(rating);
                    }
                    System.out.println();
                    System.out.println("Shows added successfully");
                }
                break;
            case 2:
                backToMain = true;
                System.out.println("Please enter password: ");
                attempt = scanner.next();

                consecutive = false;
                currentSesh = 0;
                if (!password(attempt)) {
                    System.out.println("Too many attempts.");
                    break;
                }
                
                for (Show show : catalog)
                    System.out.println(show);

                System.out.println();
                System.out.println("Which show do you wish to update? ");
                int update = scanner.nextInt();
                boolean exists = checkShowIndex(update);
                if (exists == false) {
                    System.out.println("Please re-enter another Show? ");
                    int reenter  = scanner.nextInt();
                    exists = checkShowIndex(reenter);
                    if (exists == false)
                        break;
                    else {
                        int i = reenter;
                        System.out.println(catalog[i].toString());
                        System.out.println();

                        System.out.println("What information would you like to change?\r\n" + //
                                            "1. Genre\r\n" + //
                                            "2. Title\r\n" + //
                                            "3. Year\r\n" + //
                                            "4. Rating\r\n" + //
                                            "5. Quit\r\n" + //
                                            "Enter your choice >");
                        
                        int infoChoice = scanner.nextInt();

                        switch (infoChoice) {
                            case 1:
                                System.out.println("Please enter new Genre: ");
                                String nGenre = scanner.nextLine();
                                catalog[i].setGenre(nGenre);
                                break;
                            case 2:
                                System.out.println("Please enter new Title: ");
                                String nTitle = scanner.nextLine();
                                catalog[i].setGenre(nTitle);
                                break;
                            case 3:
                                System.out.println("Please enter new Year: ");
                                String nYear = scanner.nextLine();
                                catalog[i].setGenre(nYear);
                                break;
                            case 4:
                                System.out.println("Please enter new Rating: ");
                                String nRating = scanner.nextLine();
                                catalog[i].setGenre(nRating);
                                break;
                            case 5:
                                break;
                        }   
                    }
                }
                break;
            case 3:
                backToMain = true;
                scanner.nextLine();
                System.out.println("Please enter a genre: ");
                String genreStr = scanner.nextLine().replace(" ", "-");

                for (Show show : catalog) {
                    if (show != null && show.getGenre().equalsIgnoreCase(genreStr)){
                        noShows = false;
                        System.out.println(show);
                    }
                }
                if (noShows)
                    System.out.println("No show with that genre.");

                break;
            case 4:
                backToMain = true;
                System.out.println("Display Shows above rating: ");
                int rating = scanner.nextInt();
                
                for (Show show : catalog) {
                    if (show.getRating() > rating) {
                        noShows = false;
                        System.out.println(show);
                    }
                }
                if (noShows)
                    System.out.println("No Show has that high a rating.");

                break;
            case 5:
                backToMain = false;
                break;
            default:
                backToMain = true;
                System.out.println("Invalid choice, try again.");
        }
        return backToMain;
    }
    public static boolean password(String attempt) {
        counter++;
        if (counter > 9) {
            System.out.println("Program detected suspicious activities and will terminate immediately!");
            System.exit(0);
        }

        System.out.println();
        if (!attempt.equalsIgnoreCase(password)) {
            if (currentSesh < 2) {
                System.out.println("Password failed, please try again: ");
                String diffAttempt = scanner.next();
                
                if (consecutive)
                    counter++;
                currentSesh++;
                return password(diffAttempt);
            }
            else
                return false;
        }
        return true;
    }
}


