
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
		Scanner myKeyboard = new Scanner(System.in);
		int firstHour, firstMinute, secondHour, secondMinute;

		//First part: Collection of data. User will input data in this field. Will be displayed later.
		
		/*
		 *The program will display the statements to the user, allowing them to enter any given value of hours,
		 *0 ≤ first/second Hour ≤ 23 and minutes, 0 ≤ first/second Minute ≤ 59.
		 */
		System.out.print("Enter hours for first time (0-23): ");
		firstHour = myKeyboard.nextInt();

		System.out.print("Enter minutes for first time (0-59): ");
		firstMinute = myKeyboard.nextInt();

		System.out.print("Enter hours for second time (0-23): ");
		secondHour = myKeyboard.nextInt();
	
		System.out.print("Enter minutes for second time (0-59): "); 
		secondMinute = myKeyboard.nextInt();

		System.out.println();
				
		//Second part: Displaying the data. Data input by user will be displayed here in this section.		
		
		System.out.println("First time: " + (firstHour < 10 ? "0" + firstHour : firstHour) + ":" + (firstMinute < 10 ? "0" + firstMinute : firstMinute));
		System.out.println("Second time: " + (secondHour < 10 ? "0" + secondHour : secondHour) + ":" + (secondMinute < 10 ? "0" + secondMinute : secondMinute));
		/*
		 * (firstHour < 10 ? "0" + firstHour : firstHour)
		 * EXPLANATION: 
		 * - If the value of firstHour is less than ten then we display the value of the variable with a zero before it.
		 * - Otherwise, if the value is larger than 10, the variable will be displayed as is.
		 * 
		 * This is an if statement used within the method that allows me to display the time correctly. I have the ability 
		 * to add a zero to the variable if it is less than 10, making it easier to read for the user.
		 */
		
		int diffInMinutes = ((secondHour * 60) + secondMinute) - ((firstHour * 60) + firstMinute);
		System.out.println("Difference in minutes: " + diffInMinutes);
		/* We simply calculate the difference between the two values with the data we have received from the user. */
	}
	
	
	public static void HourLimit(int firstHour, int secondHour) {
		Scanner hKeyboard = new Scanner(System.in);
		if(0 > firstHour || firstHour > 23 || 0 > secondHour || secondHour > 23) {
		
			System.out.print("Value of hour is invalid. Please enter a different value: ");
			
			int hnewValue = hKeyboard.nextInt();

			if (0 > hnewValue || hnewValue > 23) {
				System.exit(0);
			}
			else if (firstHour < 0 || firstHour > 23) {
				firstHour = hnewValue;
			}
			else if (secondHour < 0 || secondHour > 23) {
				secondHour = hnewValue;
			}
			else {
				System.exit(0);
			}
		}
	}
	public static void MinuteLimit(int firstMinute, int secondMinute) {
		Scanner mKeyboard = new Scanner(System.in);
		if (0 > firstMinute || firstMinute > 59 || 0 > secondMinute || secondMinute > 59) {
			System.out.print("Value of minute is invalid. Please enter a different value: ");
			
			int mnewValue = mKeyboard.nextInt();

			if (0 > mnewValue || mnewValue > 59) {
				System.exit(0);
			}
			else if (firstMinute < 0 || firstMinute > 59) {
				firstMinute = mnewValue;
			}
			else if (secondMinute < 0 || secondMinute > 59) {
				secondMinute = mnewValue;
			}
			else {
				System.exit(0);
			}
		}
	}




	/*
	public void HourLimit(int firstHour, int secondHour) {
		if(firstHour < 0 || firstHour > 23 || secondHour < 0 || secondHour > 23) {
			throw new IllegalArgumentException("Hour must be between 0 and 23");
		}
		this.HourLimit(firstHour, secondHour);
	}
	
	public void MinuteLimit(int firstMinute, int secondMinute) {
		if (firstMinute < 0 || firstMinute > 59 || secondMinute < 0 || secondMinute > 59) {
			throw new IllegalArgumentException("Minute must be between 0 and 59");
		}
		this.MinuteLimit(firstMinute, secondMinute);
	}
	*/

}
