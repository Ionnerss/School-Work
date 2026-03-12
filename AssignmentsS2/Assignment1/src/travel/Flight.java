package AssignmentsS2.Assignment1.src.travel;
// -------------------------------------------------------- 
// Assignment 1 - Flight Class
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S – Fall 2025
// --------------------------------------------------------

import AssignmentsS2.Assignment1.src.exceptions.InvalidTransportDataException;

public class Flight extends Transportation {
    private String airlineName;
    private double luggageAllowance;
    private Trip trip;


    public Flight() {
        super();
        this.airlineName = "";
        this.luggageAllowance = 0.0;
    }

    public Flight(String companyName, String departureCity, String arrivalCity, String airlineName, double luggageAllowance) throws InvalidTransportDataException {
        super(companyName, departureCity, arrivalCity);
        setAirlineName(airlineName);
        setLuggageAllowance(luggageAllowance);
    }

    public Flight(Flight other) {
        super(other);
        this.airlineName = other.airlineName;
        this.luggageAllowance = other.luggageAllowance;
    }

    public String getAirlineName() {return this.airlineName;}
    public double getLuggageAllowance() {return this.luggageAllowance;}
    
    public void setAirlineName(String airlineName) throws InvalidTransportDataException {
        if (airlineName == null)
            throw new InvalidTransportDataException("Airline name cannot be null.");

        String trimmed = airlineName.trim();
        if (trimmed.isEmpty())
            throw new InvalidTransportDataException("Airline name cannot be null.");

        this.airlineName = airlineName;
    }

    public void setLuggageAllowance(double luggageAllowance) throws InvalidTransportDataException {
        if (luggageAllowance < 0)
            throw new InvalidTransportDataException("Luggage allowance cannot be less than 0.");

        this.luggageAllowance = luggageAllowance;
    }

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
