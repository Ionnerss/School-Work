package AssignmentsS1.Assignment4;

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

    public boolean isDiffTotalEqual(Reception other) {
        if (this.gymPasses.gymPassesTotal() == other.gymPasses.gymPassesTotal())
            return true;
        else
            return false;
    }

    public boolean isEachTypeEqual(Reception other) {
        if (this.gymPasses.equals(other.gymPasses))
            return true;
        else
            return false;
    }

    public double total$Reception() {
        return this.gymPasses.gymPassesTotal();
    }

    public int totalCardsReception() {
        int count = this.gymCards.length;
        for (int i = 0; i < this.gymCards.length; i++)
            if (this.gymCards[i] == null)
                count--;
        return count;
    }

    //I might be overcomplicating this so check it out later, make it simpler.
    public void addNewGC(String type, String name, int expiryDay, int expiryMonth) {
        GymCard newElement = new GymCard();
        boolean isFirst = false;

        newElement.setType(type);
        newElement.setName(name);
        newElement.setExpiryDay(expiryDay);
        newElement.setExpiryMonth(expiryMonth);

        if (this.gymCards.length == 0) {
            GymCard[] newArr = new GymCard[] {newElement, null};
            this.gymCards = newArr;
            isFirst = true;
        }
        else {
            for (int i = 0; i < gymCards.length; i++) {
                if (this.gymCards[i] == null) {
                    this.gymCards[i] = newElement;
                    isFirst = true;
                    break;
                }
            }
        }

        if (isFirst == false) {
            boolean nullElement = false;
            for (int i = 0; i < this.gymCards.length; i++)
                if (this.gymCards[i] == null) {
                    nullElement = true;
                }
    
            if ((this.gymCards.length != 0) && (nullElement == false)) {
                GymCard[] newArr = new GymCard[gymCards.length + 1];
    
                System.arraycopy(gymCards, 0, newArr, 0, this.gymCards.length);
                newArr[gymCards.length] = newElement;
    
                this.gymCards = newArr;
            }
        }
    }

    public boolean removeGC(int whichCard) {
        GymCard[] updatedList = new GymCard[this.gymCards.length - 1];

        if (whichCard == 0) {
            System.arraycopy(this.gymCards, 1, updatedList, 0, this.gymCards.length - 1);
        }
        else if (whichCard == 1) {
            System.arraycopy(this.gymCards, 0, updatedList, 0, whichCard);
        }
        else {
            System.arraycopy(this.gymCards, 0, updatedList, 0, whichCard - 1);
            System.arraycopy(this.gymCards, whichCard, updatedList, whichCard - 1, (this.gymCards.length - (whichCard + 1)));
        }

        if (updatedList.length >= this.gymCards.length)
            return false;
        else {
            this.gymCards = updatedList;
            return true;
        }
    }

    public void updateExpirationDate(int whichCard, int expiryDay, int expiryMonth) {
        this.gymCards[whichCard].setExpiryDay(expiryDay);
        this.gymCards[whichCard].setExpiryMonth(expiryMonth);
    }

    public void addPasses(int regularCount, int studentCount, int seniorCount, int weekendCount, int weeklyCount) {
        this.gymPasses.addGymPasses(regularCount, studentCount, seniorCount, weekendCount, weeklyCount);
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
        return this.gymPasses.toString();
    }
}
