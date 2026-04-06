package AssignmentsS2.Assignment3.src.travel;

/*
 * Assignment 2
 * Question: SmartTravel Transportation abstract class
 * Written by: Catalin-Ion Besleaga (40347936)
 *
 * This abstract parent class stores shared transportation information
 * and validation for flights, trains, and buses.
 */

import AssignmentsS2.Assignment3.src.exceptions.InvalidTransportDataException;

public abstract class Transportation {
    private static int nextID = 3001;
    protected String transportID, companyName, departureCity, arrivalCity;

    public Transportation() {
        this.transportID = "TR" + nextID++;
        try {
            setCompanyName("Unknown Company");
            setDepartureCity("Unknown");
            setArrivalCity("Unknown");
        } catch (InvalidTransportDataException e) {
            throw new IllegalStateException("Default transportation initialization failed.", e);
        }
    }

    public Transportation(String companyName, String departureCity, String arrivalCity) throws InvalidTransportDataException {
        this.transportID = "TR" + nextID++;
        setCompanyName(companyName);
        setDepartureCity(departureCity);
        setArrivalCity(arrivalCity);
    }

    public Transportation(String transportationID, String companyName, String departureCity, String arrivalCity) throws InvalidTransportDataException {
        if (transportationID == null || transportationID.trim().isEmpty()) {
            throw new InvalidTransportDataException("Transportation ID cannot be empty.");
        }

        String trimmed = transportationID.trim();

        if (!trimmed.matches("TR\\d+")) {
            throw new InvalidTransportDataException("Invalid Transportation ID format: " + transportationID);
        }

        this.transportID = trimmed;
        setCompanyName(companyName);
        setDepartureCity(departureCity);
        setArrivalCity(arrivalCity);
    }

    public Transportation(Transportation other) {
        this.transportID = "TR" + nextID++;
        this.companyName = other.companyName;
        this.departureCity = other.departureCity;
        this.arrivalCity = other.arrivalCity;
    }

    public String getTransportID() {return this.transportID;}
    public String getCompanyName() {return this.companyName;}
    public String getDepartureCity() {return this.departureCity;}
    public String getArrivalCity() {return this.arrivalCity;}

    public void setCompanyName(String companyName) throws InvalidTransportDataException {
        if (companyName == null)
            throw new InvalidTransportDataException("Company name cannot be null.");

        String trimmed = companyName.trim();
        if (trimmed.isEmpty())
            throw new InvalidTransportDataException("Company name cannot be null.");

        this.companyName = trimmed;
    }

    public void setDepartureCity(String departureCity) throws InvalidTransportDataException {
        if (departureCity == null)
            throw new InvalidTransportDataException("Departure city cannot be null.");

        String trimmed = departureCity.trim();
        if (trimmed.isEmpty())
            throw new InvalidTransportDataException("Departure city cannot be null.");

        this.departureCity = trimmed;
    }

    public void setArrivalCity(String arrivalCity) throws InvalidTransportDataException {
        if (arrivalCity == null)
            throw new InvalidTransportDataException("Arrival city cannot be null.");

        String trimmed = arrivalCity.trim();
        if (trimmed.isEmpty())
            throw new InvalidTransportDataException("Arrival city cannot be null.");

        this.arrivalCity = trimmed;
    }

    public static void syncNextId(int nextNumericId) {
        if (nextNumericId > nextID)
            nextID = nextNumericId;
    }

    @Override
    public String toString() {return this.transportID + ";" + this.companyName + ";" + this.departureCity + ";" + this.arrivalCity;}

    @Override
    public boolean equals(Object other) {
        if (other == null || this.getClass() != other.getClass()) 
            return false;
        
        Transportation otherTr = (Transportation) other;
        
        return this.getCompanyName().equals(otherTr.getCompanyName())
            && this.getDepartureCity().equals(otherTr.getDepartureCity())
            && this.getArrivalCity().equals(otherTr.getArrivalCity());
    }

    protected abstract double calculateCost(int numOfDays);
}
