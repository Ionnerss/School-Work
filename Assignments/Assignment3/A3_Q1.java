package Assignments.Assignment3;
// ------------------------------------------------------- 
// Assignment 3 - Question #1
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S – Fall 2025
// --------------------------------------------------------

import java.util.Scanner;

public class A3_Q1 {

    static Scanner scanner = new Scanner(System.in);
    static boolean mainMenu;
    static double totalCF;
    static double value;

    public static void main(String[] args){
        System.out.println("Welcome to the Carbon Footprint Tracker!");
        System.out.println();

        while (mainMenu) {
            BaseMenu(mainMenu);
            mainMenu = BaseMenu(mainMenu);
        }

        if (mainMenu == false) {
            System.out.println("Thank you for tracking your carbon footprint. Stay green!");
			scanner.close();
			System.exit(0);
        }  
    }

    public static boolean BaseMenu (boolean mainMenu) {
        System.out.println("1. Log Activity");
        System.out.println("2. View Total Emissions");
        System.out.println("3. Get Eco Tips");
        System.out.println("4. Reset Tracker");
        System.out.println("5. Exit");
        
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println();
                ActivitySubmenu();
                break;
            case 2:
                System.out.printf("Your current carbon footprint is %.2f kg CO2e.%n", totalCF);
                System.out.println();
                break;
            case 3:
                break;
            case 4:
                totalCF = 0.0;
                System.out.println("Carbon footprint tracker has been reset.");
                System.out.println();
                break;
            case 5:
                mainMenu = false;
                break;
            default:
                System.out.println("Invalid input. Please retry.");
                break;
        }
        return mainMenu;
    }

    public static void ActivitySubmenu () {
        do {
            System.out.println("Select Activity:");
            System.out.println("1. Drive Car (0.271 kg CO2e per km)");
            System.out.println("2. Use Public Transport (0.105 kg CO2e per km)");
            System.out.println("3. Short-haul Flight (0.254 kg CO2e per km)");
            System.out.println("4. Long-haul Flight (0.150 kg CO2e per km)");
            System.out.println("5. Electricity Usage (0.475 kg CO2e per kWh)");
            System.out.println("6. Back to Main Menu");

            System.out.print("Enter your choice: ");
            int activityChoice = scanner.nextInt();
            
            double kgValue = 0.0;
            if ((activityChoice > 0) && (activityChoice < 5)) {
                System.out.print("Enter km: ");
                value = scanner.nextDouble();

                switch (activityChoice) {
                    case 1:
                        kgValue = value * 0.271;
                        break;
                    case 2:
                        kgValue = value * 0.105;
                        break;
                    case 3:
                        kgValue = value * 0.254;
                        break;
                    case 4:
                        kgValue = value * 0.150;
                    default:
                        break;
                }

                totalCF += kgValue;
                System.out.printf("Activity logged: %.2f kg CO2e.%n", kgValue);
                System.out.println();
            }
            else if (activityChoice == 5) {
                System.out.print("Enter kWh: ");
                value = scanner.nextInt();

                kgValue = value * 0.475;

                totalCF += kgValue;
                System.out.printf("Activity logged: %.2f kg CO2e.%n", kgValue);
                System.out.println();
            }
            else if (activityChoice == 6) {
                value = 6;
                System.out.println();
            }
            else {
                System.out.println("Unrecognized selection!");
                System.out.println();
                value = 0;
            }
        }
        while((value != 6));
    }
}