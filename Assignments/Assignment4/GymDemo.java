package Assignments.Assignment4;

import java.util.Scanner;

public class GymDemo {
    private static boolean mainMenu;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("+++++++");
        System.out.println("| Welcome to Gym Fit @Concordia University Application.   |");
        System.out.println("+++++++");

        do {
            mainMenu = MainWindow();
        }
        while (mainMenu == true);

        scanner.close();
    }

    public static boolean MainWindow() {
        System.out.println("What would you like to do?");
        System.out.println("1   >> See the content of all Receptions");
        System.out.println("2   >> See the content of one Reception");
        System.out.println("3   >> List Receptions with same $ amount of gym passes");
        System.out.println("4   >> List Receptions with same number of gym passes types");
        System.out.println("5   >> List Receptions with same $ amount of gym passes and same number of memberships");
        System.out.println("6   >> Add a membership card to an existing Reception");
        System.out.println("7   >> Remove an existing membership card from a Reception");
        System.out.println("8   >> Update the expiry date of an existing membership card");
        System.out.println("9   >> Add gym passes to a Reception");
        System.out.println("0   >> To Quit");



        return mainMenu;
    }
}
