package AssignmentsS2.Assignment1.src.travel;

abstract class Accomodation {
    private static int nextID = 4001;
    private String accomodationID, name, location;
    private double pricePerNight;

    public Accomodation() {
        this.accomodationID = "A" + nextID++;
        this.name = "";
        this.location = "";
        this.pricePerNight = 0.0;
    }

    public Accomodation(String name, String location, double pricePerNight) {
        this.accomodationID = "A" + nextID++;
        this.name = name;
        this.location = location;
        this.pricePerNight = pricePerNight;
    }

    public Accomodation(Accomodation other) {
        this.accomodationID = "A" + nextID++;
        this.name = other.name;
        this.location = other.location;
        this.pricePerNight = other.pricePerNight;
    }

    public String getAccomodationID() {return this.accomodationID;}
    public void setAccomodationID(String accomodationID) {this.accomodationID = accomodationID;}

    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}

    public String getLocation() {return this.location;}
    public void setLocation(String location) {this.location = location;}

    public double getPricePerNight() {return this.pricePerNight;}
    public void setPricePerNight(double pricePerNight) {this.pricePerNight = pricePerNight;}

    @Override
    public String toString() {return this.accomodationID + ", " + this.name + ", " + this.location + ", " + this.pricePerNight;}

    @Override
    public boolean equals(Object other) {
        if (other == null || this.getClass() != other.getClass())
            return false;

        Accomodation otherAcc = (Accomodation) other;

        return this.getName() == otherAcc.getName()
            && this.getLocation() == otherAcc.getLocation()
            && this.getPricePerNight() == otherAcc.getPricePerNight();
    }
    
    public abstract double calculateCost(int numOfDays);
}
