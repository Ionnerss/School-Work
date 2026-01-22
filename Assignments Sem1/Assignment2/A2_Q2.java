package Assignments.Assignment2;
// ------------------------------------------------------- 
// Assignment 2 - Question #2
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S – Fall 2025
// --------------------------------------------------------

//Algorithm 2: Smart Restaurant Assistant Program

// --------------------------------------------------------
import java.util.Scanner;

public class A2_Q2 {
    public static void main(String[] args) {
        // Print welcome banner
        System.out.println("\\---------------------------------------------------------\\");
        System.out.println("/       Welcome to the Smart Restaurant Assistant!        /");
        System.out.println("\\---------------------------------------------------------\\");
        
        //Create a scanner for user input
        Scanner scanner = new Scanner(System.in);

        //Option selection to choose from
        System.out.println("Please choose an option:");
        System.out.println();
        System.out.println("1. Show Today's Special Meal");
        System.out.println("2. Order a Burger Combo");
        System.out.println("3. Get a Discout Offer");
        System.out.println("4. Check Order Delivery Availability");
        System.out.println("5. Show Restaurant Hours");
        System.out.println("6. Exit");

        System.out.println();
        System.out.print("Your choice: ");  //Get user option choice input
        int optionChoice = scanner.nextInt();

        System.out.println();

        String output = switch (optionChoice) {
            case 1 -> // Show today's special meal
                "Today's special is Grilled Salmon with Rice - $14.99 \n";

            case 2 -> { // Show burger combo details and total price
                    double burger = 7.99;
                    double fries = 2.50;
                    double drink = 1.50;
                    double total = burger + fries + drink;
                    yield String.format("\nYou ordered a Burger Combo. \nBurger $%.2f Fries $%.2f Drink $%.2f\nYour total is $%.2f\n", burger, fries, drink, total);
            }

            case 3 -> { // Ask for user's age and display the applicable discount
                System.out.print("Please enter your age: ");
                int age = scanner.nextInt();
                String discount;
                // Determine discount based on age group
                if (age < 12)
                    discount = "Discount: 50% (children under 12) \n";
                else if (age >= 12 && age <= 25)
                    discount = "Discount: 20% (ages 12-25) \n";
                else if (age >= 65)
                    discount = "Discount: 30% (ages 65+) \n";
                else
                    discount = "Discount: none \n";
                yield ("\n" + discount);
            }

            case 4 -> { // Ask for delivery distance and display delivery fee or availability
                System.out.println("Food delivery may be available to you for an extra fee.");
                System.out.print("Please enter the estimated distance (kms) from the restaurant to your destination <<integer>>: ");
                int distance = scanner.nextInt();
                String delivery;
                // Calculate delivery fee based on distance
                if (distance < 10)
                    delivery = "An extra 5$ for delivery will be added to your order.\n";
                else if (distance >= 10 && distance < 20)
                    delivery = "An extra 10$ for delivery will be added to your order.\n";
                else if (distance >= 20 && distance <= 30)
                    delivery = "An extra 20$ for delivery will be added to your order.\n";
                else
                    delivery = "Oops! This location is outside our delivery range.\n";
                yield ("\n" + delivery);
            }

            case 5 -> // Show restaurant hours
                "Restaurant Hours: Mon-Sun: 11:00 AM - 10:00 PM\n";

            case 6 -> // Exit the program
                "Thank you for using Smart Restaurant Assistant Program!\n";

            default -> // Handle invalid option
                "Invalid option. Please try again.\n";
        };

        System.out.println(output);
        scanner.close();
    }
}