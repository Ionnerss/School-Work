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
import AssignmentsS2.Assignment3.src.interfaces.CsvPersistable;
import AssignmentsS2.Assignment3.src.interfaces.Identifiable;

public abstract class Transportation implements Identifiable, CsvPersistable, Comparable<Transportation> {
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

    @Override
    public String getId() {return this.transportID;}
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
    public String toString() {return toBaseCsvRow();}

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

    protected String toBaseCsvRow() {
        return this.transportID + ";" + this.companyName + ";" + this.departureCity + ";" + this.arrivalCity;
    }

    public static Transportation fromCsvRow(String csvLine) throws InvalidTransportDataException {
        if (csvLine == null) {
            throw new InvalidTransportDataException("CSV row cannot be null.");
        }

        String[] parts = csvLine.split(";", -1);

        if (parts.length != 7) {
            throw new InvalidTransportDataException("Transportation CSV row must have exactly 7 fields.");
        }

        String type = parts[0].trim().toUpperCase();
        String id = parts[1].trim();
        String companyName = parts[2].trim();
        String departureCity = parts[3].trim();
        String arrivalCity = parts[4].trim();

        if (id.isEmpty() || companyName.isEmpty() || departureCity.isEmpty()
                || arrivalCity.isEmpty() || parts[5].trim().isEmpty() || parts[6].trim().isEmpty()) {
            throw new InvalidTransportDataException("Transportation required CSV fields cannot be empty.");
        }

        double basePrice;

        try {
            basePrice = Double.parseDouble(parts[5].trim());
        } catch (NumberFormatException e) {
            throw new InvalidTransportDataException("Transportation base price is invalid.");
        }

        if (type.equals("FLIGHT")) {
            try {
                double luggageAllowance = Double.parseDouble(parts[6].trim());
                return new Flight(id, companyName, departureCity, arrivalCity, basePrice, luggageAllowance);
            } catch (NumberFormatException e) {
                throw new InvalidTransportDataException("Flight luggage allowance is invalid.");
            }
        } else if (type.equals("TRAIN")) {
            return new Train(id, companyName, departureCity, arrivalCity, basePrice, parts[6].trim());
        } else if (type.equals("BUS")) {
            try {
                int numOfStops = Integer.parseInt(parts[6].trim());
                return new Bus(id, companyName, departureCity, arrivalCity, basePrice, numOfStops);
            } catch (NumberFormatException e) {
                throw new InvalidTransportDataException("Bus number of stops is invalid.");
            }
        } else {
            throw new InvalidTransportDataException("Unknown transportation type: " + type);
        }
    }

    @Override
    public int compareTo(Transportation other) {
        if (other == null) {
            return -1;
        }

        return Double.compare(other.getBasePrice(), this.getBasePrice());
    }

    public abstract double getBasePrice();

    @Override
    public abstract String toCsvRow();
}
