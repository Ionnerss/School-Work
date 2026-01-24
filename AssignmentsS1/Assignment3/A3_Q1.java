package AssignmentsS1.Assignment3;
// -------------------------------------------------------- 
// Assignment 3 - Question #1
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S – Fall 2025
// --------------------------------------------------------


// Algorithm #1:  Carbon Footprint Tracker
// --------------------------------------------------------

import java.util.Scanner;

public class A3_Q1 {

    // Shared scanner for all user input (keyboard).
    static Scanner scanner = new Scanner(System.in);

    // Controls the main program loop in main().
    // true  -> keep showing the main menu
    // false -> stop after the user selects Exit
    static boolean mainMenu = true;

    // Accumulates the user's total carbon footprint across all activities.
    static double totalCF;

    // Reused numeric input holder. In activitySubmenu(), it also acts as a
    // simple placeholder to leave the submenu when set to 6.
    static double value;

    public static void main(String[] args) {
        System.out.println("Welcome to the Carbon Footprint Tracker!");
        System.out.println();

        // Reasoning for using do-while here:
        // - The main menu must appear at least once, so do-while is ideal.
        // - The baseMenu(...) method performs exactly one main-menu interaction
        //   and RETURNS the updated running state (true = keep going, false = exit).
        // - Keeping the loop decision in one place (the boolean mainMenu) makes
        //   the program flow easy to follow and avoids nested loops.
        do {
            // Pass the current running state, get back the updated state after the user's choice.
            mainMenu = baseMenu(mainMenu);
        }
        while (mainMenu == true); // Continue until baseMenu() signals to stop.

        // After the loop ends, clean up and exit.
        if (mainMenu == false) {
            System.out.println("Thank you for tracking your carbon footprint. Stay green!");
            scanner.close();
            System.exit(0);
        }
    }

    /*
     * baseMenu(boolean)
     * Purpose and design reasoning:
     * - Shows the MAIN menu once, processes a single user choice, and returns
     *   whether the program should keep running.
     * - Returning a boolean lets main() control the overall loop cleanly (no nested loops here).
     * - Each case either performs an action (log activity, show totals, tips, reset)
     *   or sets mainMenu = false to request exit.
     */
    public static boolean baseMenu (boolean mainMenu) {
        // Display options available at the top level of the app.
        System.out.println("1. Log Activity");
        System.out.println("2. View Total Emissions");
        System.out.println("3. Get Eco Tips");
        System.out.println("4. Reset Tracker");
        System.out.println("5. Exit");
        
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                // Open the logging submenu where the user can add activities.
                System.out.println();
                activitySubmenu();
                break;

            case 2:
                // Show the running total of all emissions recorded so far.
                System.out.printf("Your current carbon footprint is %.2f kg CO2e.%n", totalCF);
                System.out.println();
                break;

            case 3:
                // Provide a random eco tip. We pick an integer in [1, 5] and map it to a message.
                int min = 1;
                int max = 5;
                int random = (int)(Math.random() * (max - min + 1) + min);
                String ecoTip = "";
                switch (random) {
                    case 1:
                        ecoTip = "Consider biking or walking for short distances.";
                        break;
                    case 2:
                        ecoTip = "Turn off appliances when not in use.";
                        break;
                    case 3:
                        ecoTip = "Switch to renewable energy providers.";
                        break;
                    case 4:
                        ecoTip = "Use energy-efficient appliances.";
                        break;
                    case 5:
                        ecoTip = "Fly less and consider local vacations.";
                        break;
                }
                System.out.println("Eco Tip: " + ecoTip);
                System.out.println();
                break;

            case 4:
                // Reset the cumulative total back to zero.
                totalCF = 0.0;
                System.out.println("Carbon footprint tracker has been reset.");
                System.out.println();
                break;

            case 5:
                // Signal to the caller (main) that we should stop the program loop.
                mainMenu = false;
                break;

            default:
                // Any number outside 1–5 is not accepted.
                System.out.println("Invalid input. Please retry.");
                break;
        }
        // Return whether to continue showing the main menu.
        return mainMenu;
    }

    /**
     * activitySubmenu()
     * Purpose and loop reasoning:
     * - Lets the user log one of several activity types, each with a different
     *   emissions factor. The submenu stays active until the user selects "Back".
     * - A do-while loop is used so the submenu appears at least once and keeps
     *   cycling after each logged activity.
     * - The variable 'value' is repurposed as a simple sentinel: when the user chooses
     *   option 6 (Back), we set value = 6 so the loop condition becomes false and exits.
     */
    public static void activitySubmenu() {
        do {
            // Show available activities with their emission factors (kg CO2e per unit).
            System.out.println("Select Activity:");
            System.out.println("1. Drive Car (0.271 kg CO2e per km)");
            System.out.println("2. Use Public Transport (0.105 kg CO2e per km)");
            System.out.println("3. Short-haul Flight (0.254 kg CO2e per km)");
            System.out.println("4. Long-haul Flight (0.150 kg CO2e per km)");
            System.out.println("5. Electricity Usage (0.475 kg CO2e per kWh)");
            System.out.println("6. Back to Main Menu");

            System.out.print("Enter your choice: ");
            int activityChoice = scanner.nextInt();
            
            // Will hold the computed emissions for the current entry.
            double kgValue = 0.0;

            // Choices 1–4 are distance-based activities (ask for km).
            if ((activityChoice > 0) && (activityChoice < 5)) {
                System.out.print("Enter km: ");
                value = scanner.nextDouble();

                // Basic validation: zero distance doesn't make sense here.
                if (value == 0) {
                    System.out.println("Invalid input. Value must be greater than 0.");
                    System.out.println();
                }
                else {
                    // Multiply distance (km) by the corresponding emissions factor.
                    switch (activityChoice) {
                        case 1:
                            kgValue = value * 0.271; // Driving per km
                            break;
                        case 2:
                            kgValue = value * 0.105; // Public transport per km
                            break;
                        case 3:
                            kgValue = value * 0.254; // Short-haul flight per km
                            break;
                        case 4:
                            kgValue = value * 0.150; // Long-haul flight per km
                        default:
                            break;
                    }

                    // Add this activity’s emissions to the cumulative total.
                    totalCF += kgValue;
                    System.out.printf("Activity logged: %.2f kg CO2e.%n", kgValue);
                    System.out.println();
                }
            }
            // Choice 5 is electricity usage (ask for kWh).
            else if (activityChoice == 5) {
                System.out.print("Enter kWh: ");
                value = scanner.nextDouble();

                // Multiply kWh by electricity emissions factor.
                kgValue = value * 0.475;
                totalCF += kgValue;
                System.out.printf("Activity logged: %.2f kg CO2e.%n", kgValue);
                System.out.println();
            }
            // Choice 6 returns to the main menu.
            else if (activityChoice == 6) {
                value = 6; // Sentinel so the loop stops below.
                System.out.println();
            }
            // Any other number is invalid.
            else {
                System.out.println("Unrecognized selection!");
                System.out.println();
                value = 0; // Keep looping.
            }
        }
        // Loop continues until the user chooses "Back" (which sets value to 6).
        while (value != 6);
    }
}