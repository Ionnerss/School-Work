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

	public static Scanner keyReader = new Scanner(System.in);
    public static void main(String[] args) {
		System.out.println("Welcome to the ToDo List App!");
		System.out.print("Would you like to create a new task (1 = yes / 2 = no)? ");
		int choice = keyReader.nextInt();
		System.out.println();
		
		if (choice != 1 && choice != 2) {
			choice = ChoiceVerif(choice);
		}
		if (choice == 1){
			System.out.println("|--------------------------|");
			System.out.println("| Let's create a new task! |");
			System.out.println("|--------------------------|");

			System.out.print("What will the name of the new task be? ");
			String name = keyReader.next();
			System.out.println();

        	System.out.print("Great! Now when will it take place (hh:mm and day)? ");
			String time = keyReader.next();
			System.out.println();

			System.out.println("Perfect! Would you like to add any details to the task (1 = yes / 2 = no)? ");
			int descChoice = keyReader.nextInt();
			System.out.println();
		
			if(descChoice != 1 && descChoice != 2){
				descChoice = DescChoiceVerif(descChoice);
			}
			if(descChoice == 2){
				String[] info = {name, time};
				TaskCreator obj = new TaskCreator();
				obj.TaskProcessor(info);
			}
			else if(descChoice == 1){
				System.out.println("What would you like to add?");
            	String description = keyReader.next();

				String[] info = {name, time, description};
				TaskCreator obj = new TaskCreator();
				obj.TaskProcessor(info);
			}
		}
		else if(choice == 2){
			System.out.println("Thank you for using my prgram!");
			keyReader.close();
			System.exit(0);
		}
	}
	
	public static int ChoiceVerif(int choice) {
		int retry;
		if (choice != 1 && choice != 2){
			System.out.println("You can retry to enter a correct value.");
			System.out.print("Would you like to create a new task (1 = yes / 2 = no)? ");
			retry = keyReader.nextInt();

			if (retry != 1 && retry != 2){
				System.out.println("Invalid value entered again. System will now terminate.");
				System.out.println("Thank you for using my prgram!");
				keyReader.close();
				System.exit(0);
			}
			else {
				return retry;
			}
			return retry;
		}
		return 0;
	}

	public static int DescChoiceVerif(int descChoice) {
		int retryDesc;
		if (descChoice != 1 && descChoice != 2){
				System.out.println("You can retry to enter a correct value.");
				System.out.print("Would you like to add any details to the task (1 = yes / 2 = no)? ");
				retryDesc = keyReader.nextInt();

				if (retryDesc != 1 && retryDesc != 2){
					System.out.println("Invalid value entered again. System will now terminate.");
					System.out.println("Thank you for using my prgram!");
					keyReader.close();
					System.exit(0);
				}
				else{
					return retryDesc;
				}
				return retryDesc;
			}
		return 0;
	}
}
