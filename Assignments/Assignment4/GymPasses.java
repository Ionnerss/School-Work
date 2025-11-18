package Assignments.Assignment4;

public class GymPasses {
    public static final int regularPrice = 7, studentPrice = 5, seniorPrice = 4 , weekendPrice = 12, weeklyPrice = 42;
    public static final int[] passPrices = {regularPrice, studentPrice, seniorPrice, weekendPrice, weeklyPrice};

    private int regularPasses, studentPasses, seniorPasses, weekendPasses, weeklyPasses;
    private int[] passCounts = {regularPasses, studentPasses, seniorPasses, weekendPasses, weeklyPasses};

    public GymPasses() {
        this.regularPasses = 0;
        this.studentPasses = 0;
        this.seniorPasses = 0;
        this.weekendPasses = 0;
        this.weeklyPasses = 0;
    }

    public GymPasses(int regularPasses, int studentPasses, int seniorPasses, int weekendPasses, int weeklyPasses) {
        this.regularPasses = regularPasses;
        this.studentPasses = studentPasses;
        this.seniorPasses = seniorPasses;
        this.weekendPasses = weekendPasses;
        this.weeklyPasses = weeklyPasses;
    }

    public GymPasses(GymPasses other) {
        this.regularPasses = other.regularPasses;
        this.studentPasses = other.studentPasses;
        this.seniorPasses = other.seniorPasses;
        this.weekendPasses = other.weekendPasses;
        this.weeklyPasses = other.weeklyPasses;
    }
    
    //--------------------------------
    public int getRegularCount() {
        return this.regularPasses;
    }

    public int getStudentCount() {
        return this.studentPasses;
    }

    public int getSeniorCount() {
        return this.seniorPasses;
    }

    public int getWeekendCount() {
        return this.weekendPasses;
    }

    public int getWeeklyCount() {
        return this.weeklyPasses;
    }

    //--------------------------------
    public void setRegularCount(int regularPasses) {
        this.regularPasses = regularPasses;
    }

    public void setStudentCount(int studentPasses) {
        this.studentPasses = studentPasses;
    }

    public void setSeniorCount(int seniorPasses) {
        this.seniorPasses = studentPasses;
    }

    public void setWeekendCount(int weekendPasses) {
        this.weekendPasses = weekendPasses;
    }

    public void setWeeklyCount(int weeklyPasses) {
        this.weeklyPasses = weeklyPasses;
    }

    //--------------------------------
    private void addGymPasses(int regularPasses, int studentPasses, int seniorPasses, int weekendPasses, int weeklyPasses) {
        int[] additions = new int[] {regularPasses, studentPasses, seniorPasses, weekendPasses, weeklyPasses};
        for (int i = 0; i <= (passCounts.length - 1); i++) {
            passCounts[i] += additions[i];
        }
    }

    private int GymPassesTotal(int regularPasses, int studentPasses, int seniorPasses, int weekendPasses, int weeklyPasses) {
        int total = 0;
        for (int i = 0; i <= (passCounts.length - 1); i++) {
            total += (passCounts[i] * passPrices[i]);
        }
        return total;
    } 
    // FIX THIS BULLSHIT CUZ ITS COOKED RAAAAAAAAAAAAAAAAAAAAAAAAAAAAGH
    public String toString() {
        return "";
    }
    public boolean equals() {
        return false;
    }

}
