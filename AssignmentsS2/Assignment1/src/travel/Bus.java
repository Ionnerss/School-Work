package AssignmentsS2.Assignment1.src.travel;
// -------------------------------------------------------- 
// Assignment 1 - Bus Class
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S – Fall 2025
// --------------------------------------------------------

import AssignmentsS2.Assignment1.src.exceptions.InvalidTransportDataException;

public class Bus extends Transportation {
    private int numOfStops;
    private Trip trip;

    public Bus() {
        super();
        this.numOfStops = 0;
    }

    public Bus(String companyName, String departureCity, String arrivalCity, int numOfStops) throws InvalidTransportDataException {
        super(companyName, departureCity, arrivalCity);
        setNumOfStops(numOfStops);
    }

    public Bus(String id, String companyName, String departureCity, String arrivalCity, int numOfStops) throws InvalidTransportDataException {
        super(id, companyName, departureCity, arrivalCity);
        setNumOfStops(numOfStops);
    }

    public Bus(Bus other) {
        super(other);
        this.numOfStops = other.numOfStops;
    }

    public int getNumOfStops() {return this.numOfStops;}

    public void setNumOfStops(int numOfStops) throws InvalidTransportDataException {
        if (numOfStops < 1)
            throw new InvalidTransportDataException("Number of stops must be a minimum of 1.");

        this.numOfStops = numOfStops;
    }

    @Override
    public String toString() {return super.toString() + ", " + this.numOfStops;}

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || this.getClass() != other.getClass())
            return false;

        Bus otherBus = (Bus) other;

        return super.equals(otherBus) && this.numOfStops == otherBus.numOfStops;
    }

    @Override
    public double calculateCost(int numOfDays) {
        return ( (double) trip.getBasePrice() + (5.00 * (double) numOfStops));
    }
}
