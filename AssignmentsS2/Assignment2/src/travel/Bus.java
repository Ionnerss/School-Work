package AssignmentsS2.Assignment2.src.travel;

/*
 * Assignment 2
 * Question: SmartTravel Bus class
 * Written by: Catalin-Ion Besleaga (40347936)
 *
 * This class represents bus transportation and validates stop count
 * and fare rules.
 */

import AssignmentsS2.Assignment2.src.exceptions.InvalidTransportDataException;

public class Bus extends Transportation {
    private int numOfStops;
    private double baseFare;

    public Bus() {
        super();
        try {
            setNumOfStops(1);
            setBaseFare(0.0);
        } catch (InvalidTransportDataException e) {
            throw new IllegalStateException("Default bus initialization failed.", e);
        }
    }

    public Bus(String companyName, String departureCity, String arrivalCity, double baseFare, int numOfStops) throws InvalidTransportDataException {
        super(companyName, departureCity, arrivalCity);
        setNumOfStops(numOfStops);
        setBaseFare(baseFare);
    }

    public Bus(String id, String companyName, String departureCity, String arrivalCity, double baseFare, int numOfStops) throws InvalidTransportDataException {
        super(id, companyName, departureCity, arrivalCity);
        setNumOfStops(numOfStops);
        setBaseFare(baseFare);
    }

    public Bus(Bus other) {
        super(other);
        this.numOfStops = other.numOfStops;
        this.baseFare = other.baseFare;
    }

    public int getNumOfStops() {return this.numOfStops;}
    public double getBaseFare() {return this.baseFare;}

    public void setNumOfStops(int numOfStops) throws InvalidTransportDataException {
        if (numOfStops < 1)
            throw new InvalidTransportDataException("Number of stops must be a minimum of 1.");

        this.numOfStops = numOfStops;
    }

    public void setBaseFare(double baseFare) throws InvalidTransportDataException {
        if (baseFare < 0)
            throw new InvalidTransportDataException("Base fare cannot be less than 0.");

        this.baseFare = baseFare;
    }

    @Override
    public String toString() {return super.toString() + ";" + this.baseFare + ";" + this.numOfStops;}

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || this.getClass() != other.getClass())
            return false;

        Bus otherBus = (Bus) other;

        return super.equals(otherBus) && this.numOfStops == otherBus.numOfStops;
    }

    @Override
    protected double calculateCost(int numOfDays) {
        return ( (double) getBaseFare() + (5.00 * (double) numOfStops));
    }
}
