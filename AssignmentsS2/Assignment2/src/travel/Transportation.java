package AssignmentsS2.Assignment2.src.travel;
// -------------------------------------------------------- 
// Assignment 1 - Transportation Class
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S – Fall 2025
// --------------------------------------------------------

import AssignmentsS2.Assignment2.src.exceptions.InvalidTransportDataException;

public abstract class Transportation {
    private static int nextID = 3001;
    protected String transportID, companyName, departureCity, arrivalCity;

    public Transportation() {
        this.transportID = "TR" + nextID++;
        this.companyName = "";
        this.departureCity = "";
        this.arrivalCity = "";
    }

    public Transportation(String companyName, String departureCity, String arrivalCity) throws InvalidTransportDataException {
        this.transportID = "TR" + nextID++;
        setCompanyName(companyName);
        setDepartureCity(departureCity);
        setArrivalCity(arrivalCity);
    }

    public Transportation(String transportationID, String companyName, String departureCity, String arrivalCity) throws InvalidTransportDataException {
        if (transportationID == null || transportationID.trim().isEmpty()) {
            throw new IllegalArgumentException("Transportation ID cannot be empty.");
        }

        String trimmed = transportationID.trim();

        if (!trimmed.matches("C\\d+")) {
            throw new IllegalArgumentException("Invalid Transportation ID format: " + transportationID);
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

    public static void syncNextId(int nextNumericId) {
        if (nextNumericId > nextID)
            nextID = nextNumericId;
    }

    public void setCompanyName(String companyName) throws InvalidTransportDataException {
        if (companyName == null)
            throw new InvalidTransportDataException("Company name cannot be null.");

        String trimmed = companyName.trim();
        if (trimmed.isEmpty())
            throw new InvalidTransportDataException("Company name cannot be null.");

        this.companyName = companyName;
    }

    public void setDepartureCity(String departureCity) throws InvalidTransportDataException {
        if (departureCity == null)
            throw new InvalidTransportDataException("Departure city cannot be null.");

        String trimmed = departureCity.trim();
        if (trimmed.isEmpty())
            throw new InvalidTransportDataException("Departure city cannot be null.");

        this.departureCity = departureCity;
    }

    public void setArrivalCity(String arrivalCity) throws InvalidTransportDataException {
        if (arrivalCity == null)
            throw new InvalidTransportDataException("Arrival city cannot be null.");

        String trimmed = arrivalCity.trim();
        if (trimmed.isEmpty())
            throw new InvalidTransportDataException("Arrival city cannot be null.");

        this.arrivalCity = arrivalCity;
    }

    @Override
    public String toString() {return this.transportID + ", " + this.companyName + ", " + this.departureCity + ", " + this.arrivalCity;}

    @Override
    public boolean equals(Object other) {
        if (other == null || this.getClass() != other.getClass()) 
            return false;
        
        Transportation otherTr = (Transportation) other;
        
        return this.getCompanyName().equals(otherTr.getCompanyName())
            && this.getDepartureCity().equals(otherTr.getDepartureCity())
            && this.getArrivalCity().equals(otherTr.getArrivalCity());
    }

    public abstract double calculateCost(int numOfDays);
}
