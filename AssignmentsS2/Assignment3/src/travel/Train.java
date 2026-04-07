package AssignmentsS2.Assignment3.src.travel;

/*
 * Assignment 2
 * Question: SmartTravel Train class
 * Written by: Catalin-Ion Besleaga (40347936)
 *
 * This class represents train transportation and validates seat class
 * and fare information.
 */

import AssignmentsS2.Assignment3.src.exceptions.InvalidTransportDataException;

public class Train extends Transportation {
    private String seatClass;
    private double baseFare;

    public Train() {
        super();
        try {
            setSeatClass("Standard");
            setBaseFare(0.0);
        } catch (InvalidTransportDataException e) {
            throw new IllegalStateException("Default train initialization failed.", e);
        }
    }

    public Train(String companyName, String departureCity, String arrivalCity, double baseFare, String seatClass) throws InvalidTransportDataException {
        super(companyName, departureCity, arrivalCity);
        setSeatClass(seatClass);
        setBaseFare(baseFare);
    }

    public Train(String id, String companyName, String departureCity, String arrivalCity, double baseFare, String seatClass) throws InvalidTransportDataException {
        super(id, companyName, departureCity, arrivalCity);
        setSeatClass(seatClass);
        setBaseFare(baseFare);
    }

    public Train(Train other) {
        super(other);
        this.seatClass = other.seatClass;
        this.baseFare = other.baseFare;
    }

    public String getSeatClass() {return this.seatClass;}
    public double getBaseFare() {return this.baseFare;}
    
    public void setSeatClass(String seatClass) throws InvalidTransportDataException {
        if (seatClass == null)
            throw new InvalidTransportDataException("Seat class cannot be null.");

        String trimmed = seatClass.trim();
        if (trimmed.isEmpty())
            throw new InvalidTransportDataException("Seat class cannot be null.");

        this.seatClass = seatClass;
    }

    public void setBaseFare(double baseFare) throws InvalidTransportDataException {
        if (baseFare < 0)
            throw new InvalidTransportDataException("Base fare cannot be less than 0.");

        this.baseFare = baseFare;
    }

    
    @Override
    public String toString() {return super.toString() + ";" + this.baseFare + ";" + this.seatClass;}

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || this.getClass() != other.getClass())
            return false;

        Train otherTrain = (Train) other;

        return super.equals(otherTrain) && this.seatClass.equals(otherTrain.seatClass);
    }

    @Override
    protected double calculateCost(int numOfDays) {
        double cost = getBaseFare();
        if (seatClass.equalsIgnoreCase("First Class"))
            cost *= 1.50;
        return cost;
    }

    @Override
    public double getBasePrice() {
        return this.baseFare;
    }

    @Override
    public String toCsvRow() {
        return "TRAIN;" + super.toBaseCsvRow() + ";" + this.baseFare + ";" + this.seatClass;
    }
}
