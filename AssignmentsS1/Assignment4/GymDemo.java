package AssignmentsS1.Assignment4;

import java.util.Scanner;

public class GymDemo {
    private static boolean mainMenu;
    // private static int[] is_ARecep;
    // private static int counter;
    private static int whichRecep;
    private static int whichCard;
    private static final int timeout = -256;
    private static Scanner scanner = new Scanner(System.in);

    private Reception[] recep;

    public void main(String[] args) {
        recep = new Reception[5];

        GymPasses passes0 = new GymPasses();
        GymCard[] cardArr0 = new GymCard[0];
        Reception reception0 = new Reception(passes0, cardArr0);
        recep[0] = reception0;

        GymPasses passes1 = new GymPasses();
        GymCard[] cardArr1 = new GymCard[0];
        Reception reception1 = new Reception(passes1, cardArr1);
        recep[1] = reception1;

        GymPasses passes2 = new GymPasses();
        GymCard[] cardArr2 = new GymCard[0];
        Reception reception2 = new Reception(passes2, cardArr2);
        recep[2] = reception2;

        GymPasses passes3 = new GymPasses();
        GymCard[] cardArr3 = new GymCard[0];
        Reception reception3 = new Reception(passes3, cardArr3);
        recep[3] = reception3;


        GymPasses passes4 = new GymPasses();
        GymCard[] cardArr4 = new GymCard[0];
        Reception reception4 = new Reception(passes4, cardArr4);
        recep[4] = reception4;


        //we're gonna hard code it first then we make it all random so that it fits rules
        recep[0].addPasses(10, 4, 1, 1, 1);
        recep[0].addNewGC("Basic", "Scrump", 25, 12);
        recep[0].addNewGC("Standard", "Pelekai", 3, 12);

        recep[1].addPasses(10, 4, 1, 1, 1);
        recep[1].addNewGC("Basic", "Jookiba", 24, 8);
        recep[1].addNewGC("Premium", "Scrump", 7, 12);

        recep[2].addPasses(5, 13, 0, 4, 0);
        recep[2].addNewGC("Basic", "Gantu", 18, 12);
        recep[2].addNewGC("Standard", "Stitch", 5, 4);
        recep[2].addNewGC("PremiumPlus", "Pleakly", 1, 6);

        recep[3].addPasses(2, 8, 5, 0, 3);

        recep[4].addPasses(2, 8, 5, 0, 3);

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("|   Welcome to Gym Fit @Concordia University Application.                                 |");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        do
            mainMenu = MainWindow();
        while (mainMenu == true);

        scanner.close();
    }

    private boolean MainWindow() {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("| What would you like to do?                                                              |");
        System.out.println("| 1   >> See the content of all Receptions                                                |");
        System.out.println("| 2   >> See the content of one Reception                                                 |");
        System.out.println("| 3   >> List Receptions with same $ amount of gym passes                                 |");
        System.out.println("| 4   >> List Receptions with same number of gym passes types                             |");
        System.out.println("| 5   >> List Receptions with same $ amount of gym passes and same number of memberships  |");
        System.out.println("| 6   >> Add a membership card to an existing Reception                                   |");
        System.out.println("| 7   >> Remove an existing membership card from a Reception                              |");
        System.out.println("| 8   >> Update the expiry date of an existing membership card                            |");
        System.out.println("| 9   >> Add gym passes to a Reception                                                    |");
        System.out.println("| 0   >> To Quit                                                                          |");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        System.out.println();
        System.out.print("Please enter your choice and press <Enter>: ");
        int choice = scanner.nextInt();
        System.out.println();

        switch (choice) {
            case 1:
                mainMenu = true;
                System.out.println("Content of each reception:");
                System.out.println("----------------------------------");
                for (int i = 0; i < recep.length; i++) {
                    System.out.println("Reception #" + i + ":");
                    System.out.println(recep[i].toString());
                }
                break;
            case 2:
                mainMenu = true;
                System.out.print("Which Reception do you want to see the content of? (Enter number 0 to " + (recep.length - 1) + "): ");
                whichRecep = scanner.nextInt();
                whichRecep = IncorrectValue(whichRecep, "Reception", recep.length);
                
                if (whichRecep == timeout)
                    break;
                else
                    System.out.println(recep[whichRecep].toString());

                break;
            case 3:
                mainMenu = true;
                System.out.println("List of Receptions with same total $ GymPasses:");
                System.out.println();

                for (int i = 0; i < recep.length; i++) {
                    for (int j = i + 1; j < recep.length; j++) {
                        if (recep[i].isDiffTotalEqual(recep[j]))
                            System.out.println("Receptions " + i + " and " + j + " both have " + (int) recep[i].total$Reception());
                    }
                }
                System.out.println();
                break;
            case 4:
                mainMenu = true;
                System.out.println("List of Receptions with same pass types:");
                System.out.println();

                for (int i = 0; i < recep.length; i++) {
                    for (int j = i + 1; j < recep.length; j++) {
                        if (recep[i].isEachTypeEqual(recep[j]))
                            System.out.println("Receptions " + i + " and " + j + " both have " + recep[i].gmBreakdown());
                    }
                }
                System.out.println();
                break;
            case 5:
                mainMenu = true;
                System.out.println("List of Receptions with the same $ amount of passes and same number of memberships: ");
                System.out.println();

                for (int i = 0; i < recep.length; i++) {
                    for (int j = i + 1; j < recep.length; j++) {
                        if (recep[i].equals(recep[j]))
                            System.out.println("Reception " + i + " and " + j);
                        continue;
                    }
                }
                System.out.println();
                break;
            case 6:
                mainMenu = true;
                System.out.print("Which Reception do you want to add a membership to? (Enter number 0 to " + (recep.length - 1) + "): ");
                whichRecep = scanner.nextInt();
                whichRecep = IncorrectValue(whichRecep, "Reception", recep.length);
                scanner.nextLine();

                if (whichRecep == timeout)
                    break;
                else {
                    System.out.println("Please enter the following information so that we may complete the membership-");
    
                    System.out.print(" --> Type of membership (Basic, Standard, Premium, PremiumPlus): ");
                    String type = scanner.nextLine();

                    System.out.print(" --> Name of the membership card holder: ");
                    String name = scanner.nextLine();
    
                    System.out.print(" --> Expiry day number and month (seperate by a space): ");
                    int expiryDay = scanner.nextInt();
                    int expiryMonth = scanner.nextInt();
    
                    recep[whichRecep].addNewGC(type, name, expiryDay, expiryMonth);
    
                    System.out.println("You now have " + recep[whichRecep].totalCardsReception() + (recep[whichRecep].totalCardsReception() > 1 ? " GymCards." : " GymCard."));
                }
                break;
            case 7:
                mainMenu = true;
                System.out.print("Which Reception do you want to remove a membership card from? (Enter number 0 to " + (recep.length - 1) + ") ");
                whichRecep = scanner.nextInt();
                whichRecep = IncorrectValue(whichRecep, "Reception", recep.length);

                if (whichRecep == timeout)
                    break;
                else {
                    if (recep[whichRecep].totalCardsReception() == 0)
                        System.out.println("Sorry, that Reception has no membership cards.");
                    else {
                        System.out.print("(Enter number 0 to " + (recep[whichRecep].totalCardsReception() - 1) + ") ");
                        whichCard = scanner.nextInt();
                        whichCard = IncorrectValue(whichCard, "Card", recep[whichRecep].totalCardsReception());

                        boolean isDeleted = recep[whichRecep].removeGC(whichCard);
                        if (isDeleted == true)
                            System.out.println("Membership card was removed succesfully!");
                        else
                            System.out.println("Error encountered, Membership card has not been removed.");
                    }
                }
                break;
            case 8:
                mainMenu = true;
                System.out.print("Which Reception do you want to update a membership card from? ");
                whichRecep = scanner.nextInt();
                whichRecep = IncorrectValue(whichRecep, "Reception", recep.length);

                if (whichRecep == timeout)
                    break;
                else if (recep[whichRecep].totalCardsReception() == 0) {
                    System.out.println("Sorry, that Reception has no membership cards.");
                    break;
                }
                else {
                    System.out.println("Which membership card do you want to update? (Enter number 0 to " + (recep[whichRecep].totalCardsReception() - 1) + ") ");
                    whichCard = scanner.nextInt();
                    whichCard = IncorrectValue(whichCard, "Card", recep[whichRecep].totalCardsReception());
                    
                    if (whichCard == timeout)
                        break;
                    else {
                        System.out.print("--> Enter new expiry day number and month (seperated by a space): ");
                        int expiryDay = scanner.nextInt();
                        int expiryMonth = scanner.nextInt();

                        if ((expiryDay < 0) || (expiryDay > 31) || (expiryMonth < 0) || (expiryMonth > 31)) {
                            System.out.print("Wrong values, try again: ");
                            int expiryDayTrial = scanner.nextInt();
                            int expiryMonthTrial = scanner.nextInt();

                            if ((expiryDayTrial < 0) || (expiryDayTrial > 31) || (expiryMonthTrial < 0) || (expiryMonthTrial > 31))
                                break;
                            else {
                                recep[whichRecep].updateExpirationDate(whichCard, expiryDayTrial, expiryMonthTrial);
                                System.out.println("Expiry date updated.");
                            }
                        }
                        else {
                            recep[whichRecep].updateExpirationDate(whichCard, expiryDay, expiryMonth);
                            System.out.println("Expiry date updated.");
                            break;
                        }
                    }
                }
                break;
            case 9:
                mainMenu = true;
                System.out.print("Which reception do you want to add passes to? (Enter number 0 to " + (recep.length - 1) + "): ");
                int whichRecep = scanner.nextInt();
                whichRecep = IncorrectValue(whichRecep, "Reception", recep.length);

                if (whichRecep == timeout)
                    break;
                else {
                    System.out.println("How many Regular Pass ($7), Student Pass ($5), Senior Pass ($4), Weekend Pass ($12) and Weekly" +
                                        " Pass ($42) gym passes" + " do you want to add?");
                    System.out.print("Enter 5 numbers seperated by a space: ");
                    int regularCount = scanner.nextInt();
                    int studentCount = scanner.nextInt();
                    int seniorCount = scanner.nextInt();
                    int weekendCount = scanner.nextInt();
                    int weeklyCount = scanner.nextInt();

                    recep[whichRecep].addPasses(regularCount, studentCount, seniorCount, weekendCount, weeklyCount);
                    System.out.println("You now have $" + recep[whichRecep].total$Reception());
                }
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

    private int IncorrectValue(int value, String type, int bound) {
        System.out.println();
        if ((value >= bound) || (value < 0)) {
            System.out.println("Sorry but there is no" + type + " number " + value);
            System.out.print("--> Try Again: (Enter number 0 to " + (bound - 1) + "): ");
            int attempt = scanner.nextInt();

            if ((attempt < bound) && (attempt >= 0))
                value = attempt;
            else
                value = timeout;
        }
        return value;
    }
}