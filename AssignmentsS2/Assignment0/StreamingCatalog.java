package AssignmentsS2.Assignment0;

import java.util.Scanner;

public class StreamingCatalog {

    public static Scanner scanner = new Scanner(System.in);
    private static Show[] catalog;
    private static final String password = "STREAM";
    private boolean backToMain;

    public void main(String[] args) {
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
        while (backToMain == true);
    }

    public boolean mainMenu() {
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
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                backToMain = false;
                break;
            default:
                System.out.println("Invalid choice, try again.");
                backToMain = true;
        }
        return backToMain;
    }
}
