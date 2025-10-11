package Assignments.Assignment2;
// ------------------------------------------------------- 
// Assignment 2 - Question #2
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S – Fall 2025
// --------------------------------------------------------

//Algorithm 2: Smart Restaurant Assistant Program

import java.util.Scanner;

public class A2_Q2 {
    public static void main(String[] args) {
        System.out.println("\\---------------------------------------------------------\\");
        System.out.println("/       Welcome to the Smart Restaurant Assistant!        /");
        System.out.println("\\---------------------------------------------------------\\");
        
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please choose an option:");
        System.out.println();
        System.out.println("1. Show Today's Special Meal");
        System.out.println("2. Order a Burger Combo");
        System.out.println("3. Get a Discout Offer");
        System.out.println("4. Check Order Delivery Availability");
        System.out.println("5. Show Restaurant Hours");
        System.out.println("6. Exit");

        System.out.println();
        System.out.print("Your choice: ");
        int optionChoice = scanner.nextInt();

        System.out.println();

        String output = switch (optionChoice) {
            case 1:
                scanner.close();
                yield("Today's special is Grilled Salmon with Rice - $14.99 \n");

            case 2:
                scanner.close();
                yield("\n");
                yield("You ordered a Burger Combo. \n");
                double burger = 7.99;
                double fries = 2.50;
                double drink = 1.50;

                yield("Burger $" + burger + " Fries $" + fries + " Drink $" + drink + "\n");
                yield("Your total is $" + (burger + fries + drink) + "\n");

            case 3:
                yield("Please enter your age: \n");
                int age = scanner.nextInt();

                System.out.println();

                if (age < 12)
                    yield("Discount: 50% (children under 12) \n");
                else if (age >= 12 && age <= 25)
                    yield("Discount: 20% (ages 12-25) \n");
                else if (age >= 65)
                    yield("Discount: 30% (ages 65+) \n");
                else
                    yield("Discount: none \n");
                
                scanner.close();

            case 4:
                yield("Food delivery may be available to you for an extra fee.\n");
                yield("Please enter the estimated distance (kms) from the restaurant to your destination <<integer>>: ");
                int distance = scanner.nextInt();

                yield("\n");
                
                if (distance < 10)
                    yield("An extra 5$ for delivery will be added to your oreder.\n");
                else if (distance >= 10 && distance < 20)
                    yield("An extra 10$ for delivery will be added to your order.\n");
                else if (distance >= 20 && distance <= 30)
                    yield("An extra 20$ for delivery will be added to your order.\n");
                else if (distance > 30)
                    yield("Oops! This locaiton is outside our delivery range.\n");
                
                scanner.close();

            case 5:
                yield("Restaurant Hours: Mon-Sun: 11:00 AM - 10:00 PM\n");   
                scanner.close();

            case 6:
                yield("Thank you for using Smart Restaurant Assistant Program!\n");
                scanner.close();
            
            default:
                scanner.close();
                yield("Invalid option. Please try again.\n");
        }
    }
}