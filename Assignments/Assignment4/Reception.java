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

    public int total$Reception() {
        return this.gymPass.gymPassesTotal();
    }

    public int totalCardsReception() {
        return this.gymCards.length;
    }

    //CHECK THE LOGIC FOR THE ARRAY SLOT LATER
    public int addNewGC(Reception recep, GymCard addCard) {
        if (recep.gymCards.length == 0) {
            GymCard[] updatedList = new GymCard[1];
            updatedList[0] = addCard;
            recep.gymCards = updatedList;
        }
        else {
            GymCard[] updatedList = new GymCard[recep.gymCards.length];
            updatedList[updatedList.length - 1] = addCard; //CHECK IF IT ACCESSES THE CORRECT SLOT IN THE ARRAY
            recep.gymCards = updatedList;
        }
        return recep.gymCards.length;
    }

    //SHOULD BE FINE; WILL HAVE TO CHECK ONCE I CREATE THE INTERFACE
    public int removeGC(Reception recep, GymCard delete) {
        if (recep.gymCards.length != 0) {
            GymCard[] list = new GymCard[recep.gymCards.length];
            GymCard[] updatedList = new GymCard[0];
            for (int i = 0; i <= (list.length -1); i++) {
                if (!list[i].equals(delete))
                    continue;

                System.arraycopy(list, 0, updatedList, 0, i - 1);
                System.arraycopy(list, i + 1, updatedList, i + 1, list.length - 1);
            }
            recep.gymCards = updatedList;
        }
        return recep.gymCards.length;
    }

    //THIS ONE IS COMPLETE : SHOULD BE FUNCTIONAL
    public void updateExpirationDate(Reception recep, int card, int expiryDay, int expiryMonth) {
        recep.gymCards[card].setExpiryDay(expiryDay);
        recep.gymCards[card].setExpiryMonth(expiryMonth);
    }

    public double addPasses(Reception recep, int regularCount, int studentCount, int seniorCount, int weekendCount, int weeklyCount) {
        recep.gymPass.addGymPasses(regularCount, studentCount, seniorCount, weekendCount, weeklyCount);
        return recep.gymPass.gymPassesTotal();
    }
}
