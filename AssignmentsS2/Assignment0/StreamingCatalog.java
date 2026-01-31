package AssignmentsS2.Assignment0;

import java.util.Scanner;

public class StreamingCatalog {

    public static Scanner scanner = new Scanner(System.in);
    private static Show[] catalog;
    private static final String password = "STREAM";
    private static boolean backToMain;
    private static boolean noShows;
    private static int counter = 0;
    private static int currentSesh = 0;

    public static void main(String[] args) {
        System.out.println("Welcome to the Streaming Service Catalog Manager!");
        System.out.println();
        System.out.print("What is the maximum amount of shows you would like to add? ");
        int maxShows = scanner.nextInt();

        for (int i = 0; i < maxShows; i++) {
            Show nShow = new Show();
            catalog[i] = nShow;
        }

        do
            backToMain = mainMenu();
        while (backToMain);

        scanner.close();
        System.out.println();
        System.out.println("Thank you for using the Streaming catalog!!!");
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
                String attempt = scanner.next();
                
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
                        System.out.print("Title: ");
                        String title = scanner.nextLine();

                        System.out.print("Genre: ");
                        String genre = scanner.nextLine();

                        System.out.println("Year: ");
                        int year = scanner.nextInt();

                        System.out.println("Rating: ");
                        double rating = scanner.nextDouble();

                        catalog[catalog.length - space + i + 1].setTitle(title);
                        catalog[catalog.length - space + i + 1].setGenre(genre);
                        catalog[catalog.length - space + i + 1].setYear(year);
                        catalog[catalog.length - space + i + 1].setRating(rating);
                    }
                    System.out.println();
                    System.out.println("Shows added successfully");
                }
                break;
            case 2:

                break;
            

            //Work on the hyphen thing for multi word genres
            case 3:
                backToMain = true;
                System.out.println("Please enter a genre: ");
                String genreStr = scanner.next();

                for (Show show : catalog) {
                    if (show.getGenre().equalsIgnoreCase(genreStr)){
                        noShows = false;
                        System.out.println(show.getTitle() + " | ");
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
                        System.out.println(show.getTitle() + " | ");
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
        currentSesh = 0;
        currentSesh++;
        if (counter > 9) {
            System.out.println("Program detected suspicious activities and will terminate immediately!");
            System.exit(0);
        }

        System.out.println();
        if (!attempt.equalsIgnoreCase(password)) {
            if (currentSesh < 2) {
                System.out.println("Password failed, please try again: ");
                String diffAttempt = scanner.next();
    
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
