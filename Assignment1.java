// ------------------------------------------------------- 
// Assignment 1
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S – Fall 2025
// --------------------------------------------------------

import java.util.Scanner;
import java.lang.Math;
import java.lang.String;
import java.util.Random;

public class Assignment1 {

	public static void main(String[] args) {
		System.out.println("Welcome to my program!");
		
		Scanner keyReader = new Scanner(System.in);
		
		System.out.print("Enter hours for first time: ");
		int firstHour = keyReader.nextInt();
		
		System.out.print("Enter minutes for first time: ");
		int firstMinute = keyReader.nextInt();
		
		System.out.print("Enter hours for second time: ");
		int secondHour = keyReader.nextInt();
		
		System.out.print("Enter minutes for second time: ");
		int secondMinute = keyReader.nextInt();
		
		System.out.println();
		
		
		int diffTime = Math.abs(((secondHour * 60) + secondMinute) - ((firstHour * 60) + firstMinute));
		
		
		
		System.out.println("First time: " + (firstHour < 10 ? "0" + firstHour : firstHour) + 
				":" + (firstMinute < 10 ? "0" + firstMinute : firstMinute));
		System.out.println("Second time:" + (secondHour < 10 ? "0" + secondHour : secondHour) + 
				":" + (secondMinute < 10 ? "0" + secondMinute : secondMinute));
		System.out.println("Difference in minutes: " + diffTime);
		
		
		System.out.println("--------------------------------------------------------------");
		
		System.out.println("Welcome! Let's generate your screen name nd secret code.");
		
		System.out.print("Enter your favorite color: ");
		String color = keyReader.next();
		
		System.out.print("Enter your favorite animal: ");
		String animal = keyReader.next();
		
		System.out.print("Enter the city where you were born: ");
		String city = keyReader.next();
		
		System.out.println("");
		
		
		int lastThreeCharColor = color.length() - 3;
		String colorSub = color.substring(lastThreeCharColor);
		String animalSub = animal.substring(0, 3);
		int cityLength = city.length();
		String secretName = colorSub + animalSub + cityLength;
		
		System.out.println("Generated Screen Name: " + secretName);
		
		
		
		
		
		
		
		
		
		
		
		String concInputs = color + animal + city;
		char firstChar = concInputs.charAt(0);
		char lastChar = concInputs.charAt(concInputs.length() - 1);
		String concInputsSub = concInputs.substring(1, concInputs.length() - 1);
		
		String lC = lastChar + "o";
		String upperlC = lC.toUpperCase();
		char truLastChar = upperlC.charAt(0);
		
		String swapped = truLastChar + concInputsSub + firstChar;
		String secretCodeSub1 = swapped.substring(0, 2);
		String secretCodeSub2 = swapped.substring(3);
		
		System.out.println("Generated secret code: " + secretCodeSub1 + "*" + secretCodeSub2);
		keyReader.close();
		
	}
}
