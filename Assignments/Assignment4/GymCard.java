package Assignments.Assignment4;

public class GymCard {
    private final String basic = "Basic", standard = "Standard", premium = "Premium", premiumPlus = "PremiumPlus";
    private String type;
    private String name;
    private int expiryDay, expiryMonth;

    public GymCard() {
        this.type = "";
        this.name = "";
        this.expiryDay = 0;
        this.expiryMonth = 0;
    }

    public GymCard(String type, String name, int expiryDay, int expiryMonth) {
        this.type = type;
        this.name = name;

        if ((expiryDay > 0) && (expiryDay < 32))
            this.expiryDay = expiryDay;
        else
            this.expiryDay = 0;

        if ((expiryMonth > 0) && (expiryMonth < 13))
            this.expiryMonth = expiryMonth;
        else
            this.expiryMonth = 0;
    }

    public GymCard(GymCard other) {
        this.type = other.type;
        this.name = other.name;
        this.expiryDay = other.expiryDay;
        this.expiryMonth = other.expiryMonth;
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public int getExpiryDay() {
        return this.expiryDay;
    }

    public int getExpiryMonth() {
        return this.expiryMonth;
    }

    public void setExpiryDay(int expiryDay) {
        if ((expiryDay > 0) && (expiryDay < 32))
            this.expiryDay = expiryDay;
        else
            this.expiryDay = 0;
    }

    public void setExpiryMonth(int expiryMonth) {
        if ((expiryMonth > 0) && (expiryMonth < 32))
            this.expiryMonth = expiryMonth;
        else
            this.expiryMonth = 0;
    }

    public String toString() {
        return "Gym Card Type: " + type + "; Expiry Date: " + (expiryDay < 10 ? ("0" + expiryDay) : expiryDay)+ "/" + 
                (expiryMonth < 10 ? ("0" + expiryMonth) : expiryMonth);
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        else
            return false;
    }
}
