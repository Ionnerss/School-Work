package Assignments.Assignment4;

public class Reception {
    private GymPasses gymPasses;
    private GymCard[] gymCards;

    public Reception() {
        this.gymPasses = null;
        this.gymCards = null;
    }

    public Reception(GymPasses gymPasses, GymCard[] gymCards) {
        this.gymPasses = gymPasses;
        this.gymCards = gymCards;
    }

    //NOT SURE FOR THIS ONE SO CHECK OUT LATER
    public boolean isDiffTotalEqual(Reception other) {
        if (this.gymPasses.gymPassesTotal() == other.gymPasses.gymPassesTotal())
            return true;
        else
            return false;
    }

    public boolean isEachTypeEqual(Reception other) {
        if (this.toString() == other.toString())
            return true;
        else
            return false;
    }

    public double total$Reception() {
        return this.gymPasses.gymPassesTotal();
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

        if (this.gymCards.length == 0)
            this.gymCards[0] = newElement;
        else {
            for (int i = 0; i < gymCards.length; i++) {
                if (this.gymCards[i] == null) {
                    this.gymCards[i] = newElement;
                    break;
                }
            }
        }

        if ((this.gymCards.length != 0) && (this.gymCards[this.gymCards.length - 1] != null)) {
            GymCard[] newArr = new GymCard[gymCards.length + 1];

            System.arraycopy(gymCards, 0, newArr, 0, this.gymCards.length);
            newArr[gymCards.length + 1] = newElement;

            this.gymCards = newArr;
        }
    }

    public boolean removeGC(int whichCard) {
        if (this.gymCards.length == 0)
            return false;
        else {
            whichCard -= 1;
            GymCard[] updatedList = new GymCard[0];
            System.arraycopy(gymCards, 0, updatedList, 0, whichCard -1);
            System.arraycopy(gymCards, whichCard + 1, updatedList, whichCard, this.gymCards.length);
            
            this.gymCards = updatedList;
            return true;
        }
    }

    public void updateExpirationDate(int whichCard, int expiryDay, int expiryMonth) {
        this.gymCards[whichCard].setExpiryDay(expiryDay);
        this.gymCards[whichCard].setExpiryMonth(expiryMonth);
    }

    public void addPasses(int regularCount, int studentCount, int seniorCount, int weekendCount, int weeklyCount) {
        GymPasses newPasses  = new GymPasses();
        newPasses.addGymPasses(regularCount, studentCount, seniorCount, weekendCount, weeklyCount);
        this.gymPasses = newPasses;
    }

    public boolean equals(Reception other) { 
        if ((this.isDiffTotalEqual(other)) && (this.isEachTypeEqual(other)))
            return true;
        else
            return false;
    }

    public String toString() {
        String cards = "";
        String result = "";
        int count = 0;

        for (int i = 0; i < this.gymCards.length; i++)
            if (this.gymCards[i] == null)
                count++;

        if (this.gymCards.length == count)
                    result = this.gymPasses.toString() + "\n" + "No membership card" + "\n";

        for (int i = 0; i < this.gymCards.length - count; i++) {
            if (this.gymCards[i] == null) {
                if (i > 0) {
                    for (int j = 0; j < i; j++)
                        cards += gymCards[j].toString() + ".\n";

                    result = this.gymPasses.toString() + "\n" + cards;
                    break;
                }
                else {
                    result = this.gymPasses.toString() + "\n" + this.gymCards[0].toString() + "\n";
                    break;
                }
            }
            else {
                cards += gymCards[i].toString() + ".\n";
                result = this.gymPasses.toString() + "\n" + cards;
            }
        }
        return result;
    }

    public String gmBreakdown() {
        return this.gymPasses.toString() + "\n";
    }
}
