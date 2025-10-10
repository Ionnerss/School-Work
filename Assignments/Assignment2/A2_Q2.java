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

        switch (optionChoice) {
            case 1:
                System.out.println();
                System.out.println("Today's special is Grilled Salmon with Rice - $14.99");

                scanner.close();
                break;

            case 2:
                System.out.println();
                System.out.println("You ordered a Burger Combo.");
                double burger = 7.99;
                double fries = 2.50;
                double drink = 1.50;

                System.out.println("Burger $" + burger + " Fries $" + fries + " Drink $" + drink);
                System.out.println("Your total is $" + (burger + fries + drink));

                scanner.close();
                break;

            case 3:
                System.out.print("Please enter your age: ");
                int age = scanner.nextInt();

                System.out.println();

                if (age < 12)
                    System.out.println("Discount: 50% (children under 12)");
                else if (age >= 12 && age <= 25)
                    System.out.println("Discount: 20% (ages 12-25)");
                else if (age >= 65)
                    System.out.println("Discount: 30% (ages 65+)");
                else
                    System.out.println("Discount: none");
                
                scanner.close();
                break;

            case 4:
                System.out.println("Food delivery may be available to you for an extra fee.");
                System.out.print("Please enter the estimated distance (kms) from the restaurant to your destination <<integer>>: ");
                int distance = scanner.nextInt();

                System.out.println();
                
                if (distance < 10)
                    System.out.println("An extra 5$ for delivery will be added to your oreder.");
                else if (distance >= 10 && distance < 20)
                    System.out.println("An extra 10$ for delivery will be added to your order.");
                else if (distance >= 20 && distance <= 30)
                    System.out.println("An extra 20$ for delivery will be added to your order.");
                else if (distance > 30)
                    System.out.println("Oops! This locaiton is outside our delivery range.");
                
                scanner.close();
                break;

            case 5:
                System.out.println();
                System.out.println("Restaurant Hours: Mon-Sun: 11:00 AM - 10:00 PM");   
                
                scanner.close();
                break;

            case 6:
                System.out.println();
                System.out.println("Thank you for using Smart Restaurant Assistant Program!");
                
                scanner.close();
                break;
            
            default:
                System.out.println("Invalid option. Please try again.");
                
                scanner.close();
                break;
            
            }
        }
    }