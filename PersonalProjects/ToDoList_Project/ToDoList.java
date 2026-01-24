package ToDoList_Project;
import java.util.Scanner;


/*
 * Dont forget to make it so that it is possible to repeat the operation multiple times. Therefore i should
 * put all the contents of the questionnaire and creation of the task in a different method in order to be 
 * able to call it will. 
 * 
 * Also, would be fun if I can make it so that it saves these tasks for another time, like a database bs
 * type thing so the user can come back to it. In this case, I should also make it possible to delete tasks
 * and/or mark some as completed. 
 * 
 * If I do mark a task as completed then I should make a save or file or wtv the fuck where you can see all
 * completed tasks.
 */
public class ToDoList {

	static Scanner scanner = new Scanner(System.in);
	static boolean redoUIP; 
	// check static vs instance variable (when var is defined in class, not method); 
	//static int x; -> static | int x; -> instance 

    public static void main(String[] args) {
		System.out.println("Welcome to the ToDo List App!");
		System.out.println();

		do {
			redoUIP = UserInputProcess(redoUIP);
		}
		while(redoUIP);

		if (redoUIP == false) {
			System.out.println("Thank you for using my program!");
			scanner.close();
			System.exit(0);
		}
	}
	
	public static String ChoiceVerif(String anotherTask) {
		String retry = "";
		if (!anotherTask.equalsIgnoreCase("yes") && !anotherTask.equalsIgnoreCase("no")) {
			System.out.println("You can retry to enter a correct value.");
			System.out.print("Would you like to create a new task (yes / no)? ");
			retry = scanner.next();

			if (!retry.equalsIgnoreCase("yes") && !retry.equalsIgnoreCase("no")) {
				System.out.println("Invalid value entered again. System will now terminate.");
				System.out.println("Thank you for using my prgram!");
				scanner.close();
				System.exit(0);
			}
			else {
				return retry;
			}
		}
		return retry;
	}

	public static String DescChoiceVerif(String descChoice) {
		String retryDesc = "";
		if (!descChoice.equalsIgnoreCase("yes") && !descChoice.equalsIgnoreCase("no")) {
				System.out.println("You can retry to enter a correct value.");
				System.out.print("Would you like to add any details to the task (yes / no)? ");
				retryDesc = scanner.next();

				if (!retryDesc.equalsIgnoreCase("yes") && !retryDesc.equalsIgnoreCase("no")) {
					System.out.println("Invalid value entered again. System will now terminate.");
					System.out.println("Thank you for using my prgram!");
					scanner.close();
					System.exit(0);
				}
				else {
					return retryDesc;
				}
			}
		return retryDesc;
	}

	/*
	 * Make safe proof so that if user does not have any previous tasks saved, any other options from the menu will not be available.
	 * 2. Will no be able to view previous tasks since there arent any
	 * 3. Cant modify any tasks since there arent any.
	 * 4. Cant delete any tasks since there arent any.
	 * Make so that for every category there is a message displaying the above mentioned fact. 
	 */


	public static boolean UserInputProcess (boolean redoUIP) {
		redoUIP = false;
		
		System.out.println("Here is a list of possible actions. Select what you would like to do:");
		System.out.println("1. Create a new task.");
		System.out.println("2. View past / current tasks.");
		System.out.println("3. Modify a task.");
		System.out.println("4. Delete a task.");
		System.out.println("5. Exit program.");
		
		System.out.println();
		System.out.print("Please enter what you would like to do: ");
		int actionChoice = scanner.nextInt();

		switch (actionChoice) {
			case 1:
				System.out.println();
				System.out.println("|------------------------------|");
				System.out.println("|   Let's create a new task!   |");
				System.out.println("|------------------------------|");
				System.out.println();

				System.out.print("What will the name of the new task be? ");
				String name = scanner.next();
				System.out.println();

        		System.out.print("Great! Now when will it take place (hh:mm and day)? ");
				String time = scanner.next();
				System.out.println();

				System.out.print("Perfect! Would you like to add any details to the task (yes / no)? ");
				String descChoice = scanner.next();
				System.out.println();
			

				if(!descChoice.equalsIgnoreCase("yes") && !descChoice.equalsIgnoreCase("no")) {
				descChoice = DescChoiceVerif(descChoice);
				}

				String[] info;
				String description;
				if(descChoice.equalsIgnoreCase("no")) {
					description = "";
					info = new String[] {name, time, description};
					
					TaskCreator.TaskProcessor(info);
				}
				else if(descChoice.equalsIgnoreCase("yes")) {
					System.out.println("What would you like to add?");
					description = scanner.next();
					info = new String[] {name, time, description};

					TaskCreator.TaskProcessor(info);
				}
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				System.out.println();
				System.out.println("Thank you for using my program!");
				scanner.close();
				System.exit(0);
				break;
		}
		System.out.println();
		System.out.print("Would you like to create another task (yes/no)? ");
		String anotherTask = scanner.next();

		if (anotherTask.equalsIgnoreCase("yes")) 
			redoUIP = true;
		else if (anotherTask.equalsIgnoreCase("no")) 
			redoUIP = false;
		else {
			anotherTask = ChoiceVerif(anotherTask);
			if (anotherTask.equalsIgnoreCase("yes")) 
				redoUIP = true;
			else if (anotherTask.equalsIgnoreCase("no")) 
				redoUIP = false;			
			}
		return redoUIP;
	}
}