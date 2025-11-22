package Assignments.Assignment4;

import java.util.Scanner;

public class GymDemo {
    private static boolean mainMenu;
    // private static int[] is_ARecep;
    // private static int counter;
    private static int whichRecep;
    private static int whichCard;
    private static Scanner scanner = new Scanner(System.in);

    private Reception[] recep;

    // private static boolean Compare(int value, int[] arrayComp) {
    //     boolean isTrue = false;
    //     for (int i = 0; i <= arrayComp.length - 1; i++) {
    //         if (value != arrayComp[i]) {
    //             isTrue = false;
    //             continue;
    //         }
    //         isTrue = true;
    //         break;
    //     }
    //     return isTrue;
    // }


    // private static int randomInt() {
    //     int randomInt = (int) Math.random() * 6;

    //     boolean isTrue = Compare(randomInt, is_ARecep);
    //     while (isTrue == true) {
    //         randomInt = (int) Math.random() * 6;
    //         isTrue = Compare(randomInt, is_ARecep);
    //     }
        
    //     is_ARecep[counter] = randomInt();
    //     counter += 1;
    //     return randomInt;
    // }

    public void main(String[] args) {
        System.out.println("+++++++");
        System.out.println("| Welcome to Gym Fit @Concordia University Application.   |");
        System.out.println("+++++++");

        // is_ARecep = new int[5];
        // recep = new Reception[5];

        // recep[randomInt()] = recep[randomInt()]; //2 receptions will be exactly the same
            

        


        do
            mainMenu = MainWindow();
        while (mainMenu == true);

        scanner.close();
    }

    private boolean MainWindow() {
        System.out.println("+++++++");
        System.out.println("What would you like to do?");
        System.out.println("|1   >> See the content of all Receptions");
        System.out.println("|2   >> See the content of one Reception");
        System.out.println("|3   >> List Receptions with same $ amount of gym passes");
        System.out.println("|4   >> List Receptions with same number of gym passes types");
        System.out.println("|5   >> List Receptions with same $ amount of gym passes and same number of memberships");
        System.out.println("|6   >> Add a membership card to an existing Reception");
        System.out.println("|7   >> Remove an existing membership card from a Reception");
        System.out.println("|8   >> Update the expiry date of an existing membership card");
        System.out.println("|9   >> Add gym passes to a Reception");
        System.out.println("|0   >> To Quit");
        System.out.println("+++++++");

        System.out.println();
        System.out.println("Please enter your choice and press <Enter>: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("Content of each reception:");
                System.out.println("--------------------------------");
                for (int i = 0; i < recep.length; i++) {
                    System.out.println("Reception #" + i + ":");
                    System.out.println(recep[i].gmBreakdown());
                    System.out.println(recep[i].toString() + ".");
                    System.out.println();
                }
                mainMenu = true;
                break;
            case 2:
                System.out.println("Which Reception do you want to see the content of? (Enter number 0 to " + recep.length + "): ");
                whichRecep = scanner.nextInt();
                whichRecep = Mistake(whichRecep);
                
                System.out.println();
                System.out.println(recep[whichRecep].gmBreakdown());
                System.out.println(recep[whichRecep].toString());
                System.out.println();
                
                mainMenu = true;
                break;
            case 3:
                mainMenu = true;
                break;
            case 4:
                mainMenu = true;
                break;
            case 5:
                System.out.println("List of Receptions with the same $ amount of passes and same number of memberships: ");
                System.out.println();

                for (int i = 0; i < recep.length; i++) {
                    for (int j = 0; j <= (recep.length - 1); j++) {
                        if ((recep[j].total$Reception() == recep[i].total$Reception()) && 
                            (recep[j].totalCardsReception() == recep[i].totalCardsReception()))
                            System.out.println("Reception " + recep[i] + " and " + recep[j]);
                        continue;
                    }
                }
                mainMenu = true;
                break;
            case 6:
                System.out.print("Which Reception do you want to add a membership to? (Enter number 0 to " + recep.length + ")");
                whichRecep = scanner.nextInt();
                whichRecep = Mistake(whichRecep);

                System.out.println("Please enter the following information so that we may complete the membership-");

                System.out.print(" --> Type of membership (Basic, Standard, Premium, PremiumPlus): ");
                String type = scanner.nextLine();

                System.out.print(" --> Name of the membership card holder: ");
                String name = scanner.nextLine();

                System.out.println("Expiry day number and month (seperate by a space): ");
                int expiryDay = scanner.nextInt();
                int expiryMonth = scanner.nextInt();

                recep[whichRecep].addNewGC(type, name, expiryDay, expiryMonth);

                System.out.println("You now have " + recep[whichRecep].totalCardsReception() + "GymCard");
                
                mainMenu = true;
                break;
            case 7:
                System.out.print("Which Reception do you want to remove a membership card from? (Enter number 0 to " + recep.length + ") ");
                whichRecep = scanner.nextInt();
                whichRecep = Mistake(whichRecep);

                System.out.print("(Enter number 0 to " + recep[whichRecep].totalCardsReception() + ") ");
                whichCard = scanner.nextInt();
                whichCard = Mistake(whichCard);

                boolean wasDeleted = recep[whichRecep].removeGC(whichCard);

                if (wasDeleted == true)
                    System.out.println("Membership card was removed succesfully!");
                else
                    System.out.println("Sorry, Reception has no membership cards.");

                mainMenu = true;
                break;
            case 8:
                System.out.print("Which Reception do you want to update a membership card from? ");
                whichRecep = scanner.nextInt();
                whichRecep = Mistake(whichRecep);

                System.out.println("Which membership card do you want to update? (Enter number 0 to " + 
                                    recep[whichRecep].totalCardsReception() + ") ");
                whichCard = scanner.nextInt();
                whichCard = Mistake(whichCard);


                mainMenu = true;
                break;
            case 9:
                mainMenu = true;
                break;
            case 0:
                System.out.println("Thank you for using Fit Gym @Concordia University Application!");
                mainMenu = false;
                break;
            default:
                System.out.println("Sorry that is not a valid choice. Try Again.");
                mainMenu = true;
                break;
        }
        return mainMenu;
    }

    private int Mistake(int value) {
        value -= 1;
        while ((value >= recep.length) && (value < 0)) {
            System.out.println("Sorry but there is no Reception number " + value);
            System.out.println("--> Try Again: (Enter number 0 to " + recep.length + "): ");
            int attempt = scanner.nextInt() - 1;

            if ((attempt < recep.length) && (attempt >= 0)) {
                value = attempt;
                break;
            }
            System.out.println();
        }

        return value;
    }
}
