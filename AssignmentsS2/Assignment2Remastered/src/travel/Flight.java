package AssignmentsS2.Assignment2Remastered.src.travel;
// -------------------------------------------------------- 
// Assignment 1 - Flight Class
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S – Fall 2025
// --------------------------------------------------------

import AssignmentsS2.Assignment2Remastered.src.exceptions.InvalidTransportDataException;

public class Flight extends Transportation {
    private double luggageAllowance;
    private Trip trip;


    public Flight() {
        super();
        this.luggageAllowance = 0.0;
    }

    public Flight(String companyName, String departureCity, String arrivalCity, double luggageAllowance) throws InvalidTransportDataException {
        super(companyName, departureCity, arrivalCity);
        setLuggageAllowance(luggageAllowance);
    }

    public Flight(String id, String companyName, String departureCity, String arrivalCity, double luggageAllowance) throws InvalidTransportDataException {
        super(id, companyName, departureCity, arrivalCity);
        setLuggageAllowance(luggageAllowance);
    }

    public Flight(Flight other) {
        super(other);
        this.luggageAllowance = other.luggageAllowance;
    }

    public double getLuggageAllowance() {return this.luggageAllowance;}

    public void setLuggageAllowance(double luggageAllowance) throws InvalidTransportDataException {
        if (luggageAllowance < 0)
            throw new InvalidTransportDataException("Luggage allowance cannot be less than 0.");

        this.luggageAllowance = luggageAllowance;
    }

    @Override
    public String toString() {return super.toString() + ", " + this.luggageAllowance;}

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || this.getClass() != other.getClass())
            return false;

        Flight otherFlight = (Flight) other;

        return super.equals(otherFlight) && this.luggageAllowance == otherFlight.luggageAllowance;
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
