
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
	
	public void KeyScanning() {
		Scanner myKeyboard = new Scanner(System.in);
	}

	public static void main(String[] args) {

		
		//First part: Collection of data. User will input data in this field. Will be displayed later.
		
		/*
		 *The program will display the statements to the user, allowing them to enter any given value of hours,
		 *0 ≤ first/second Hour ≤ 23 and minutes, 0 ≤ first/second Minute ≤ 59.
		 */
		System.out.print("Enter hours for first time (0-23): ");
		int firstHour = myKeyboard.nextInt();
		
		if(firstHour < 0) {
			System.out.print("Value of hour is less than 0. Please enter a different value: ");
			int fHnewValue = myKeyboard.nextInt();
			
			if(fHnewValue < 0 || fHnewValue > 23) {
				System.exit(0);
			}
			else {
				firstHour = fHnewValue;
			}
		}
		else if(firstHour > 23) {
			System.out.print("Value of hour is over 23. Please enter a different value: ");
			int fHnewValue2 = myKeyboard.nextInt();
			
			if(fHnewValue2 < 0 || fHnewValue2 > 23) {
				System.exit(0);
			}
			else {
				firstHour = fHnewValue2;
			}
		}
		
		
		System.out.print("Enter minutes for first time (0-59): ");
		int firstMinute = myKeyboard.nextInt();
		
		if(firstMinute < 0) {
			System.out.print("Value of hour is less than 0. Please enter a different value: ");
			int fHnewValue3 = myKeyboard.nextInt();
			
			if(fHnewValue3 < 0 || fHnewValue3 > 59) {
				System.exit(0);
			}
			else {
				firstMinute = fHnewValue3;
			}
		}
		else if(firstMinute > 59) {
			System.out.print("Value of hour is over 59. Please enter a different value: ");
			int fHnewValue4 = myKeyboard.nextInt();
			
			if(fHnewValue4 < 0 || fHnewValue4 > 59) {
				System.exit(0);
			}
			else {
				firstMinute = fHnewValue4;
			}
		}
		
		
		System.out.print("Enter hours for second time (0-23): ");
		int secondHour = myKeyboard.nextInt();
		
		if(secondHour < 0) {
			System.out.print("Value of hour is less than 0. Please enter a different value: ");
			int fHnewValue5 = myKeyboard.nextInt();
			
			if(fHnewValue5 < 0 || fHnewValue5 > 23) {
				System.exit(0);
			}
			else {
				secondHour = fHnewValue5;
			}
		}
		else if(secondHour > 23) {
			System.out.print("Value of hour is over 23. Please enter a different value: ");
			int fHnewValue6 = myKeyboard.nextInt();
			
			if(fHnewValue6 < 0 || fHnewValue6 > 23) {
				System.exit(0);
			}
			else {
				secondHour = fHnewValue6;
			}
		}
		
		
		System.out.print("Enter minutes for second time (0-59): "); 
		int secondMinute = myKeyboard.nextInt();
		
		if(secondMinute < 0) {
			System.out.print("Value of hour is less than 0. Please enter a different value: ");
			int fHnewValue7 = myKeyboard.nextInt();
			
			if(fHnewValue7 < 0 || fHnewValue7 > 59) {
				System.exit(0);
			}
			else {
				secondMinute = fHnewValue7;
			}
		}
		else if(secondMinute > 59) {
			System.out.print("Value of hour is over 59. Please enter a different value: ");
			int fHnewValue8 = myKeyboard.nextInt();
			
			if(fHnewValue8 < 0 || fHnewValue8 > 59) {
				System.exit(0);
			}
			else {
				secondMinute = fHnewValue8;
			}
		}
		
			
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
	
	
	public void HourLimit(int firstHour, int secondHour) {
		if(firstHour < 0 || secondHour < 0) {
			System.out.print("Value of hour is less than 0. Please enter a different value: ");
			int fHnewValue = myKeyboard.nextInt();
			
			if(fHnewValue < 0 || fHnewValue > 23) {
				System.exit(0);
			}
			else {
				firstHour = fHnewValue;
			}
		}
		else if(firstHour > 23) {
			System.out.print("Value of hour is over 23. Please enter a different value: ");
			int fHnewValue2 = myKeyboard.nextInt();
			
			if(fHnewValue2 < 0 || fHnewValue2 > 23) {
				System.exit(0);
			}
			else {
				firstHour = fHnewValue2;
			}
		}
	}
	
	/*public void HourLimit(int firstHour, int secondHour) {
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
