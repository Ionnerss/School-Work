package AssignmentsS2.Assignment2Remastered.src.travel;
// -------------------------------------------------------- 
// Assignment 1 - Train Class
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S – Fall 2025
// --------------------------------------------------------

import AssignmentsS2.Assignment2Remastered.src.exceptions.InvalidTransportDataException;

public class Train extends Transportation {
    private String seatClass;
    private Trip trip;

    public Train() {
        super();
        this.seatClass = "";
    }

    public Train(String companyName, String departureCity, String arrivalCity, String seatClass) throws InvalidTransportDataException {
        super(companyName, departureCity, arrivalCity);
        this.seatClass = seatClass;
    }

    public Train(String id, String companyName, String departureCity, String arrivalCity, String seatClass) throws InvalidTransportDataException {
        super(id, companyName, departureCity, arrivalCity);
        this.seatClass = seatClass;
    }

    public Train(Train other) {
        super(other);
        this.seatClass = other.seatClass;
    }

    public String getSeatClass() {return this.seatClass;}
    
    public void setSeatClass(String seatClass) throws InvalidTransportDataException {
        if (seatClass == null)
            throw new InvalidTransportDataException("Seat class cannot be null.");

        String trimmed = seatClass.trim();
        if (trimmed.isEmpty())
            throw new InvalidTransportDataException("Seat class cannot be null.");

        this.seatClass = seatClass;
    }
    
    @Override
    public String toString() {return super.toString() + ", " + this.seatClass;}

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || this.getClass() != other.getClass())
            return false;

        Train otherTrain = (Train) other;

        return super.equals(otherTrain) && this.seatClass.equals(otherTrain.seatClass);
    }

    @Override
    public double calculateCost(int numOfDays) {
        double cost = trip.getBasePrice();
        if (seatClass.equalsIgnoreCase("First Class"))
            cost *= 1.50;
        return cost;
    }
}
