package AssignmentsS2.Assignment1.src.travel;
// -------------------------------------------------------- 
// Assignment 1 - Bus Class
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S – Fall 2025
// --------------------------------------------------------

import AssignmentsS2.Assignment1.src.exceptions.InvalidTransportDataException;

public class Bus extends Transportation {
    private String busCompany;
    private int numOfStops;
    private Trip trip;

    public Bus() {
        super();
        this.busCompany = "";
        this.numOfStops = 0;
    }

    public Bus(String companyName, String departureCity, String arrivalCity, String busCompany, int numOfStops) throws InvalidTransportDataException {
        super(companyName, departureCity, arrivalCity);
        setBusCompany(busCompany);
        setNumOfStops(numOfStops);
    }

    public Bus(Bus other) {
        super(other);
        this.busCompany = other.busCompany;
        this.numOfStops = other.numOfStops;
    }

    public String getBusCompany() {return this.busCompany;}
    public int getNumOfStops() {return this.numOfStops;}

    public void setBusCompany(String busCompany) throws InvalidTransportDataException {
        if (busCompany == null)
            throw new InvalidTransportDataException("Bus company name cannot be null.");

        String trimmed = busCompany.trim();
        if (trimmed.isEmpty())
            throw new InvalidTransportDataException("Bus company name cannot be null.");

        this.busCompany = busCompany;
    }

    public void setNumOfStops(int numOfStops) throws InvalidTransportDataException {
        if (numOfStops < 1)
            throw new InvalidTransportDataException("Number of stops must be a minimum of 1.");

        this.numOfStops = numOfStops;
    }

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

    @Override
    public double calculateCost(int numOfDays) {
        return ( (double) trip.getBasePrice() + (5.00 * (double) numOfStops));
    }
}
