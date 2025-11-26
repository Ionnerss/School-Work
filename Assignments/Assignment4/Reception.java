package Assignments.Assignment4;

public class Reception {
    private GymPasses gymPass;
    private GymCard[] gymCards;
    private int counter;
    private int counterStr;

    public Reception() {
        this.gymPass = null;
        this.gymCards = null;
    }

    public Reception(GymPasses gymPass, GymCard[] gymCards) {
        this.gymPass = gymPass;
        this.gymCards = gymCards;
    }

    //NOT SURE FOR THIS ONE SO CHECK OUT LATER
    public boolean isTotalEqual(Reception other) {
        if (this.gymPass == other.gymPass)
            return true;
        else
            return false;
    }

    public boolean isEachEqual(Reception other) {
        if (this.gymPass.toString() == other.gymPass.toString())
            return true;
        else
            return false;
    }

    public double total$Reception() {
        return this.gymPass.gymPassesTotal();
    }

    public int totalCardsReception() {
        return this.gymCards.length;
    }

    public void addNewGC(String type, String name, int expiryDay, int expiryMonth) {
        GymCard newElement = new GymCard(); 

        newElement.setType(type);
        newElement.setName(name);
        newElement.setExpiryDay(expiryDay);
        newElement.setExpiryMonth(expiryMonth);

        if (counter == 0) {
            gymCards[0] = newElement;
            counter++;
        }
        else {
            GymCard[] newArr = new GymCard[gymCards.length + 1];

            System.arraycopy(gymCards, 0, newArr, 0, gymCards.length - 1);
            newArr[gymCards.length] = newElement;
            this.gymCards = newArr;
            counter++;
        }
    }

    public boolean removeGC(int whichCard) {
        if (gymCards.length == 0)
            return false;
        else {
            whichCard -= 1;
            GymCard[] updatedList = new GymCard[0];
            System.arraycopy(gymCards, 0, updatedList, 0, whichCard -1);
            System.arraycopy(gymCards, whichCard + 1, updatedList, whichCard, gymCards.length);
            
            this.gymCards = updatedList;
            return true;
        }
    }

    //THIS ONE IS COMPLETE : SHOULD BE FUNCTIONAL
    public void updateExpirationDate(int whichCard, int expiryDay, int expiryMonth) {
        gymCards[whichCard].setExpiryDay(expiryDay);
        gymCards[whichCard].setExpiryMonth(expiryMonth);
    }

    public void addPasses(int regularCount, int studentCount, int seniorCount, int weekendCount, int weeklyCount) {
        GymPasses newPasses  = new GymPasses();
        newPasses.addGymPasses(regularCount, studentCount, seniorCount, weekendCount, weeklyCount);
        this.gymPass = newPasses;
    }

    public boolean equal(GymPasses other) { 
        if (gymPass.gymPassesTotal() == other.gymPassesTotal())
            return true;
        else
            return false;
    }

    public String toString() {
        if (counterStr == 0) {
            counterStr++;
            return this.gymPass.toString() + "\nNo membership card\n";
        }
        else {
            String cards = "";
            for (int i = 0; i <= this.gymCards.length - 1; i++)
                cards += gymCards[i].toString() + "\n";

            return this.gymPass.toString() + "\n" + cards;
        }
    }

    public String gmBreakdown() {
        return this.gymPass.toString();
    }
}
