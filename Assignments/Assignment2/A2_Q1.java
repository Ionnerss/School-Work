package Assignments.Assignment2;
// ------------------------------------------------------- 
// Assignment 2 - Question #1
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S – Fall 2025
// --------------------------------------------------------

import java.util.Scanner;



public class A2_Q1 {
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("\\---------------------------------------------------------\\");
        System.out.println("/               Welcome to JavaBeans Coffee Shop!          /");
        System.out.println("\\---------------------------------------------------------\\");
        
        System.out.println();

        System.out.println("Please enter your name: ");
        String name = scanner.next();

        System.out.println("Please enter your age: ");
        int age = scanner.nextInt();


        double drink = 0;
        System.out.println("Please select your drink: ");
        System.out.println("1 - Coffee ($3.50)");
        System.out.println("2 - Tea ($2.75)");
        System.out.println("3 - Hot Chocolate ($4.00)");
        
        System.out.print("Your choice: ");
        int choice = scanner.nextInt();
        String selectedDrink = "";

        if (choice == 1) {
            drink = 3.50;
            selectedDrink = "Coffee";
        }
        else if (choice == 2) {
            drink = 2.75;
            selectedDrink = "Tea";
        }
        else if (choice == 3) {
            drink = 4.00;
            selectedDrink = "Hot Chocolate";
        }


        System.out.println("Do you want an extra shot of espresso (+$1.00)? (yes/no)");
        String espressoInput = scanner.next();
        
        double addEspresso = 0;
        if (espressoInput.equalsIgnoreCase("yes")) {
            addEspresso = 1.00;
        }
        else if (espressoInput.equalsIgnoreCase("no")) {
            addEspresso = 0.00;
        }


        System.out.println("Do you want whipped cream (+$0.50)? (yes/no)");
        String creamInput = scanner.next();

        double addCream = 0;
        if (creamInput.equalsIgnoreCase("yes")) {
            addCream = 0.50;
        }
        else if (creamInput.equalsIgnoreCase("no")) {
            addCream = 0.00;
        }

        System.out.println("Do you want syrup (+$0.75)? (yes/no)");
        String syrupInput = scanner.next();

        double addSyrup = 0;
        if (syrupInput.equalsIgnoreCase("yes")) {
            addSyrup = 0.75;
        }
        else if (syrupInput.equalsIgnoreCase("no")) {
            addSyrup = 0.00;
        }



        double subtotal = (drink + addEspresso + addCream + addSyrup);
        double discount = 0;
        double discountedTotal = 0;
        String discountType = "";

        if (age < 12) {
            discount = (subtotal/50.0);
            discountedTotal = discount;
            discountType = "(Children 50%)";
        }
        else if (age >= 13 || age <= 25) {
            discount = (subtotal/4);
            discountedTotal = (subtotal - discount);
            discountType = "(Student 25%)";
        }
        else if (age > 65) {
            discount = (subtotal/3);
            discountedTotal = (subtotal - discount);
            discountType = "(Senior 30%)";
        }
        else {
            discountedTotal = subtotal;
        }

         /*
         * adding a tip section
         */
        System.out.println("Would you like to add a tip?");
        System.out.println("0 - No tip");
        System.out.println("1 - 10%");
        System.out.println("2 - 20%");

        System.out.print("Your choice: ");
        int tipChoice = scanner.nextInt();

        double total = 0;
        double tip = 0;
        if (tipChoice == 0) {
            tip = 0.00;
            total = discountedTotal;
        }
        else if (tipChoice == 1) {
            tip = (discountedTotal/10);
            total = (discountedTotal + tip);
        }
        else if (tipChoice == 2) {
            tip = (discountedTotal/20);
            total = (discountedTotal + tip);
        }

        System.out.println("Would you like to round up your bill to donate to charity? (yes/no)");
        String roundupInput = scanner.next();

        double roundedTotal = 0;
        double roundup = 0;
        if (roundupInput.equalsIgnoreCase("yes")) {
            roundedTotal =  Math.ceil(total);
            roundup = roundedTotal - total;
        }
        else if (roundupInput.equalsIgnoreCase("no")) {
            roundedTotal = total;
            roundup = roundedTotal - total;
        }


        /*
         * Reciept section
         */
        System.out.println("------- Reciept for " + name + " -------");
        System.out.println("Base drink: " + selectedDrink + " $" + drink);
        System.out.println("Add-ons total: $" + (addEspresso + addCream + addSyrup));
        System.out.println("Subtotal: $" + subtotal);
        System.out.println("Discount " + discountType + ": -$" + discount);
        System.out.println("After discount: $" + discountedTotal);
        System.out.println("Tip: $" + tip);
        System.out.println("Charity donation: $" + roundup);
        System.out.println("-----------------------------------------");
        System.out.println("Final total: $" + roundedTotal);

        System.out.println();

        System.out.println("Thank you for visiting JavaBeans Coffee Shop!");



        }
}