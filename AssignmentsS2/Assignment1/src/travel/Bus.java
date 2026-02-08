package AssignmentsS2.Assignment1.src.travel;

public class Bus extends Transportation {
    private String busCompany;
    private int numOfStops;

    public Bus() {
        super();
        this.busCompany = "";
        this.numOfStops = 0;
    }

    public Bus(String companyName, String departureCity, String arrivalCity, String busCompany, int numOfStops) {
        super(companyName, departureCity, arrivalCity);
        this.busCompany = busCompany;
        this.numOfStops = numOfStops;
    }

    public Bus(Bus other) {
        super(other);
        this.busCompany = other.busCompany;
        this.numOfStops = other.numOfStops;
    }

    public String getBusCompany() {return this.busCompany;}
    public void setBusCompany(String busCompany) {this.busCompany = busCompany;}

    public int getNumOfStops() {return this.numOfStops;}
    public void setNumOfStops(int numOfStops) {this.numOfStops = numOfStops;}

    @Override
    public String toString() {return super.toString() + ", " + this.busCompany + ", " + this.numOfStops;}

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || this.getClass() != other.getClass())
            return false;

        Bus otherBus = (Bus) other;

        return super.equals(otherBus)
            && this.busCompany == otherBus.busCompany
            && this.numOfStops == otherBus.numOfStops;
    }
}
