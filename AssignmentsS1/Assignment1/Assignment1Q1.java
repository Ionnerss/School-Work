package AssignmentsS1.Assignment1;
// ------------------------------------------------------- 
// Assignment 1 - Question #1
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S – Fall 2025
// --------------------------------------------------------

import java.util.Scanner;
import java.lang.Math;
import java.lang.String;

/*
 * This program prompts the user to enter two times in hours and minutes, calculates the difference in minutes between the two times,
 * and displayes the information to the user. It utilizes a bounding system which will make sure that the program re-prompts
 * the user if the input values are not within bounds.
 */

public class Assignment1Q1 {

	public static Scanner keyReader = new Scanner(System.in);
	public static void main(String[] args) {
		
		/*
		 * The program will display the statements to the user, allowing them to enter any given value of hours,
		 * 0 ≤ first/second Hour ≤ 23 and minutes, 0 ≤ first/second Minute ≤ 59.
		 * If the user enters an invalid value, they will be prompted to re-enter the value or a different valid value.
		 * If the user enters an invalid value again, the program will terminate.
		 * This process will continue until all input values are valid.
		 * The program uses while loops and if statements to validate the input values.
		 */

		System.out.println("Welcome to my program!");
		System.out.println();
		
		System.out.print("Enter hours for first time (0-23): ");
		int firstHour = keyReader.nextInt();
		
		System.out.print("Enter minutes for first time (0-59): ");
		int firstMinute = keyReader.nextInt();
		
		System.out.print("Enter hours for second time (0-23): ");
		int secondHour = keyReader.nextInt();
		
		System.out.print("Enter minutes for second time (0-59): ");
		int secondMinute = keyReader.nextInt();
		
		System.out.println();

		/*
		 * The program will check if the input values are within the specified ranges.
		 * If any value is out of range, the user will be prompted to re-enter the value or a different valid value.
		 * If the user enters an invalid value again, the program will terminate.
		 * This process will continue until all input values are valid.
		 * The program uses while loops and if statements to validate the input values.
		 */
		while(0 > firstHour || firstHour > 23 || 0 > firstMinute || firstMinute > 59 ||
				0 > secondHour || secondHour > 23 || 0 > secondMinute || secondMinute > 59) {			

			if(0 > firstHour || firstHour > 23 || 0 > firstMinute || firstMinute > 59 ||
				0 > secondHour || secondHour > 23|| 0 > secondMinute || secondMinute > 59) {	
					
				if(0 > firstHour || firstHour > 23) {
					firstHour = HourLimit(firstHour, secondHour);
					System.out.println();
				}
				else if(0 > firstMinute || firstMinute > 59) {
					firstMinute = MinuteLimit(firstMinute, secondMinute);
					System.out.println();
				}
				else if(0 > secondHour || secondHour > 23) {
					secondHour = HourLimit(firstHour, secondHour);
					System.out.println();
				}
				else if(0 > secondMinute || secondMinute > 59) {
					secondMinute = MinuteLimit(firstMinute, secondMinute);
					System.out.println();
				}
			}
			/*
			 * EXPLANATION:
			 * - The while loop continues as long as any of the input values are out of range.
			 * - The if statements inside the loop check each input value individually.
			 * - If an input value is out of range, the user is prompted to re-enter a valid value.
			 * - If the user enters an invalid value again, the program terminates using System.exit(0).
			 * - The loop ensures that the program only proceeds when all input values are valid.
			 * - This approach ensures robust input validation and user interaction.
			 */
		}

		int diffTime = Math.abs(((secondHour * 60) + secondMinute) - ((firstHour * 60) + firstMinute));
		/*
		 * EXPLANATION:
		 * - The hours are converted to minutes by multiplying them by 60.
		 * - The total minutes for each time is calculated by adding the converted hours and the minutes.
		 */
		
		System.out.println("First time: " + (firstHour < 10 ? "0" + firstHour : firstHour) + 
				":" + (firstMinute < 10 ? "0" + firstMinute : firstMinute));
		System.out.println("Second time:" + (secondHour < 10 ? "0" + secondHour : secondHour) + 
				":" + (secondMinute < 10 ? "0" + secondMinute : secondMinute));
		System.out.println("Difference in minutes: " + diffTime);
		System.out.println();
		/*
		 * - The times are displayed in HH:MM format, ensuring that single-digit hours and minutes are prefixed with a zero for proper formatting.
		 * - The difference in minutes between the two times is displayed.
		 * 
		 * EXPLANATION: (firstHour < 10 ? "0" + firstHour : firstHour)
		 * - If the value of firstHour is less than ten then we display the value of the variable with a zero before it.
		 * - Otherwise, if the value is larger than 10, the variable will be displayed as is.
		 * 
		 * This is an if statement used within the method that allows me to display the time correctly. I have the ability 
		 * to add a zero to the variable if it is less than 10, making it easier to read for the user.
		 */
		System.out.println("Thank you for using my program. Goodbye!");
		keyReader.close();
	}

	public static int HourLimit(int firstHour, int secondHour) {
		/*
		 * This method prompts the user to re-enter a valid hour value if the initial input is out of range.
		 * If the user enters an invalid value again, the program terminates.
		 * We do the following in order to check the bounds that the user has entered. This prevents the input of
		 * incorrect values which may return absurd data.
		 */

		String whichHour = "0";
		if(0 > firstHour || firstHour > 23 ){
			whichHour =  "first time";
		}
		else if(0 > secondHour || secondHour > 23){
			whichHour = "second time";
		}

		if(0 > firstHour || firstHour > 23 || 0 > secondHour || secondHour > 23) {

			System.out.print("Hour value for " + whichHour + " is invalid. Please enter a different value: ");
			int hourNewValue = keyReader.nextInt();

			if (0 > hourNewValue || hourNewValue > 23) {
				System.out.println("Invalid hour value for " + whichHour + " has been entered (" + hourNewValue + "). Program will now terminate."); 
				System.out.println();

				System.out.println("Thank you for using my program. Goodbye!");
				keyReader.close();
				System.exit(0);
			}

			return hourNewValue;
		}
		return 0;
	}
	
	public static int MinuteLimit(int firstMinute, int secondMinute) {
		/*
		 * This method prompts the user to re-enter a valid minute value if the initial input is out of range.
		 * If the user enters an invalid value again, the program terminates.
		 * We do the following in order to check the bounds that the user has entered. This prevents the input of
		 * incorrect values which may return absurd data.
		 */

		String whichMinute = "0";
		if(0 > firstMinute || firstMinute > 59 ){
			whichMinute =  "first time";
		}
		else if(0 > secondMinute || secondMinute > 59){
			whichMinute = "second time";
		}

		if (0 > firstMinute || firstMinute > 59 || 0 > secondMinute || secondMinute > 59) {

			System.out.print("Minute value for " + whichMinute + " is invalid. Please enter a different value: ");
			int minuteNewValue = keyReader.nextInt();

			if (0 > minuteNewValue || minuteNewValue > 59) {
				System.out.println("Invalid minute value for " + whichMinute + " has been entered (" + minuteNewValue + "). Program will now terminate.");
				System.out.println();

				System.out.println("Thank you for using my program. Goodbye!");
				keyReader.close();
				System.exit(0);
			}
			return minuteNewValue;
		}	
		return 0; 
	}
}