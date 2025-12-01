package Assignments.Assignment4;

public class GymPasses {
    public static final int regularPrice = 7, studentPrice = 5, seniorPrice = 4 , weekendPrice = 12, weeklyPrice = 42;
    public static final int[] passPrices = {regularPrice, studentPrice, seniorPrice, weekendPrice, weeklyPrice};

    private int regularCount, studentCount, seniorCount, weekendCount, weeklyCount;

    public GymPasses() {
        this.regularCount = 0;
        this.studentCount = 0;
        this.seniorCount = 0;
        this.weekendCount = 0;
        this.weeklyCount = 0;
    }

    public GymPasses(int regularCount, int studentCount, int seniorCount, int weekendCount, int weeklyCount) {
        this.regularCount = regularCount;
        this.studentCount = studentCount;
        this.seniorCount = seniorCount;
        this.weekendCount = weekendCount;
        this.weeklyCount = weeklyCount;
    }

    public GymPasses(GymPasses other) {
        this.regularCount = other.regularCount;
        this.studentCount = other.studentCount;
        this.seniorCount = other.seniorCount;
        this.weekendCount = other.weekendCount;
        this.weeklyCount = other.weeklyCount;
    }
    
    //--------------------------------
    public int getRegularCount() {
        return this.regularCount;
    }

    public int getStudentCount() {
        return this.studentCount;
    }

    public int getSeniorCount() {
        return this.seniorCount;
    }

    public int getWeekendCount() {
        return this.weekendCount;
    }

    public int getWeeklyCount() {
        return this.weeklyCount;
    }

    //--------------------------------
    public void setRegularCount(int regularCount) {
        this.regularCount = regularCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public void setSeniorCount(int seniorCount) {
        this.seniorCount = seniorCount;
    }

    public void setWeekendCount(int weekendCount) {
        this.weekendCount = weekendCount;
    }

    public void setWeeklyCount(int weeklyCount) {
        this.weeklyCount = weeklyCount;
    }

    //--------------------------------
    public void addGymPasses(int regularCount, int studentCount, int seniorCount, int weekendCount, int weeklyCount) {
        this.regularCount += regularCount;
        this.studentCount += studentCount;
        this.seniorCount += seniorCount;
        this.weekendCount += weekendCount;
        this.weeklyCount += weeklyCount;
    }

    public double gymPassesTotal() {
        int total = (this.regularCount * regularPrice) + (this.studentCount * studentPrice) + (this.seniorCount * seniorPrice) + (this.weekendCount * weekendPrice) + 
                (this.weeklyCount * weeklyPrice);
        return total;
    } 

    public String toString() {
        return this.regularCount + " X $" + regularPrice + " + " + this.studentCount + " X $" + studentPrice + " + " +
                this.seniorCount + " X $" + seniorPrice + " + " + this.weekendCount + " X $" + weekendPrice + " + " +
                this.weeklyCount + " X $" + weeklyPrice;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        else
            return false;
    }

}
