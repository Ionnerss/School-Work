package AssignmentsS2.Assignment3.src.travel;

/*
 * Assignment 2
 * Question: SmartTravel Flight class
 * Written by: Catalin-Ion Besleaga (40347936)
 *
 * This class represents flight transportation and validates base fare
 * and luggage allowance.
 */

import AssignmentsS2.Assignment3.src.exceptions.InvalidTransportDataException;

public class Flight extends Transportation {
    private double luggageAllowance, baseFare;

    public Flight() {
        super();
        try {
            setLuggageAllowance(0.0);
            setBaseFare(0.0);
        } catch (InvalidTransportDataException e) {
            throw new IllegalStateException("Default flight initialization failed.", e);
        }
    }

    public Flight(String companyName, String departureCity, String arrivalCity, double baseFare, double luggageAllowance) throws InvalidTransportDataException {
        super(companyName, departureCity, arrivalCity);
        setLuggageAllowance(luggageAllowance);
        setBaseFare(baseFare);
    }

    public Flight(String id, String companyName, String departureCity, String arrivalCity, double baseFare, double luggageAllowance) throws InvalidTransportDataException {
        super(id, companyName, departureCity, arrivalCity);
        setLuggageAllowance(luggageAllowance);
        setBaseFare(baseFare);
    }

    public Flight(Flight other) {
        super(other);
        this.luggageAllowance = other.luggageAllowance;
        this.baseFare = other.baseFare;
    }

    public double getLuggageAllowance() {return this.luggageAllowance;}
    public double getBaseFare() {return this.baseFare;}

    public void setLuggageAllowance(double luggageAllowance) throws InvalidTransportDataException {
        if (luggageAllowance < 0)
            throw new InvalidTransportDataException("Luggage allowance cannot be less than 0.");

        this.luggageAllowance = luggageAllowance;
    }

    public void setBaseFare(double baseFare) throws InvalidTransportDataException {
        if (baseFare < 0)
            throw new InvalidTransportDataException("Base fare cannot be less than 0.");

        this.baseFare = baseFare;
    }

    @Override
    public String toString() {return super.toString() + ";" + this.baseFare + ";" + this.luggageAllowance;}

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || this.getClass() != other.getClass())
            return false;

        Flight otherFlight = (Flight) other;

        return super.equals(otherFlight) && this.luggageAllowance == otherFlight.luggageAllowance && this.baseFare == otherFlight.baseFare;
    }

    @Override
    protected double calculateCost(int numOfDays) {
        double cost = getBaseFare();
        if (luggageAllowance > 20) {
            int overAllowance = (int) luggageAllowance - 20;
            cost += 10 * overAllowance;
        }
        return cost;
    }
}
