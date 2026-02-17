package AssignmentsS2.Assignment1.src.travel;

public class Flight extends Transportation {
    private String airlineName;
    private double luggageAllowance;
    private Trip trip;


    public Flight() {
        super();
        this.airlineName = "";
        this.luggageAllowance = 0.0;
    }

    public Flight(String companyName, String departureCity, String arrivalCity, String airlineName, double luggageAllowance) {
        super(companyName, departureCity, arrivalCity);
        this.airlineName = airlineName;
        this.luggageAllowance = luggageAllowance;
    }

    public Flight(Flight other) {
        super(other);
        this.airlineName = other.airlineName;
        this.luggageAllowance = other.luggageAllowance;
    }

    public String getAirlineName() {return this.airlineName;}
    public void setAirlineName(String airlineName) {this.airlineName = airlineName;}

    public double getLuggageAllowance() {return this.luggageAllowance;}
    public void setLuggageAllowance(double luggageAllowance) {this.luggageAllowance = luggageAllowance;}

    @Override
    public String toString() {return super.toString() + ", " + this.airlineName + ", " + this.luggageAllowance;}

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || this.getClass() != other.getClass())
            return false;

        Flight otherFlight = (Flight) other;

        return super.equals(otherFlight)
            && this.airlineName == otherFlight.airlineName
            && this.luggageAllowance == otherFlight.luggageAllowance;
    }

    @Override
    public double calculateCost(int numOfDays) {
        double cost = (double) trip.getBasePrice();
        if (luggageAllowance > 20) {
            int overAllowance = (int) luggageAllowance - 20;
            cost += 10 * overAllowance;
        }
        return cost;
    }
}
