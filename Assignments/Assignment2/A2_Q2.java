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
        System.out.println("/        Welcome to the Smart Restaurant Assistant!        /");
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
        String optionChoice = scanner.next();



















        
        scanner.close();
    }
}