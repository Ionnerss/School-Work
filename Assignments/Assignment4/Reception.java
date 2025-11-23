package Assignments.Assignment4;

public class Reception {
    private GymPasses gymPass;
    private GymCard[] gymCards;

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
        GymCard[] newArr = new GymCard[gymCards.length + 1];
        
        if (gymCards.length == 0) {
            newArr[0].setType(type);
            newArr[0].setName(name);
            newArr[0].setExpiryDay(expiryDay);
            newArr[0].setExpiryMonth(expiryMonth);
        }
        else {
            newArr[gymCards.length - 1].setType(type);
            newArr[gymCards.length - 1].setName(name);
            newArr[gymCards.length - 1].setExpiryDay(expiryDay);
            newArr[gymCards.length - 1].setExpiryMonth(expiryMonth);
        }
        this.gymCards = newArr;
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

    public double addPasses(int regularCount, int studentCount, int seniorCount, int weekendCount, int weeklyCount) {
        gymPass.addGymPasses(regularCount, studentCount, seniorCount, weekendCount, weeklyCount);
        return gymPass.gymPassesTotal();
    }

    public boolean equal(Reception recep, GymPasses other) { 
        double count = other.getRegularCount() + other.getStudentCount() + other.getSeniorCount() + 
                        other.getWeekendCount() + other.getWeeklyCount();
        
        if (recep.gymPass.gymPassesTotal() == count)
            return true;
        else
            return false;
    }

    public String toString() {
        String cards = "";
        for (int i = 0; i <= this.gymCards.length - 1; i++) {
            cards += this.gymCards[i].toString() + "\n";
        }

        if (this.gymCards == null)
            return this.gymPass.toString() + "\n No membership card \n";
        else
            return this.gymPass.toString() + "\n" + cards;
    }

    public String gmBreakdown() {
        return this.gymPass.toString();
    }
}
