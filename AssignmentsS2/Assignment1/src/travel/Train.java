package AssignmentsS2.Assignment1.src.travel;
// -------------------------------------------------------- 
// Assignment 1 - Train Class
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S – Fall 2025
// --------------------------------------------------------

import AssignmentsS2.Assignment1.src.exceptions.InvalidTransportDataException;

public class Train extends Transportation {
    private String trainType, seatClass;
    private Trip trip;

    public Train() {
        super();
        this.trainType = "";
        this.seatClass = "";
    }

    public Train(String companyName, String departureCity, String arrivalCity, String trainType, String seatClass) throws InvalidTransportDataException {
        super(companyName, departureCity, arrivalCity);
        this.trainType = trainType;
        this.seatClass = seatClass;
    }

    public Train(Train other) {
        super(other);
        this.trainType = other.trainType;
        this.seatClass = other.seatClass;
    }

    public String getTrainType() {return this.trainType;}
    public String getSeatClass() {return this.seatClass;}
    
    public void setTrainType(String trainType) throws InvalidTransportDataException {
        if (trainType == null)
            throw new InvalidTransportDataException("Train type cannot be null.");

        String trimmed = trainType.trim();
        if (trimmed.isEmpty())
            throw new InvalidTransportDataException("Train type cannot be null.");

        this.trainType = trainType;
    }
    
    public void setSeatClass(String seatClass) throws InvalidTransportDataException {
        if (seatClass == null)
            throw new InvalidTransportDataException("Seat class cannot be null.");

        String trimmed = seatClass.trim();
        if (trimmed.isEmpty())
            throw new InvalidTransportDataException("Seat class cannot be null.");

        this.seatClass = seatClass;
    }
    
    @Override
    public String toString() {return super.toString() + ", " + this.trainType + ", " + this.seatClass;}

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || this.getClass() != other.getClass())
            return false;

        Train otherTrain = (Train) other;

        return super.equals(otherTrain)
            && this.trainType.equals(otherTrain.trainType)
            && this.seatClass.equals(otherTrain.seatClass);
    }

    @Override
    public double calculateCost(int numOfDays) {
        double cost = trip.getBasePrice();
        if (seatClass.equalsIgnoreCase("First Class"))
            cost *= 1.50;
        return cost;
    }
}
