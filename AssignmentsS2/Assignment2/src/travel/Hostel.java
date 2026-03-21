package AssignmentsS2.Assignment2.src.travel;

/*
 * Assignment 2
 * Question: SmartTravel Hostel class
 * Written by: Catalin-Ion Besleaga (40347936)
 *
 * This class represents hostel accommodations and validates shared beds
 * and hostel pricing rules.
 */

import AssignmentsS2.Assignment2.src.exceptions.InvalidAccommodationDataException;

public class Hostel extends Accommodation {
    private int numOfSharedBeds;

    public Hostel() {
        super();
        try {
            setNumOfSharedBeds(0);
        } catch (InvalidAccommodationDataException e) {
            throw new IllegalStateException("Default hostel initialization failed.", e);
        }
    }

    public Hostel(String name, String location, double pricePerNight, int numOfSharedBeds) throws InvalidAccommodationDataException {
        super(name, location, pricePerNight);
        setNumOfSharedBeds(numOfSharedBeds);
    }

    public Hostel(String id, String name, String location, double pricePerNight, int numOfSharedBeds) throws InvalidAccommodationDataException {
        super(id, name, location, pricePerNight);
        setNumOfSharedBeds(numOfSharedBeds);
    }

    public Hostel(Hostel other) {
        super(other);
        this.numOfSharedBeds = other.numOfSharedBeds;
    }

    public int getNumOfSharedBeds() {return this.numOfSharedBeds;}

    public void setNumOfSharedBeds(int numOfSharedBeds) throws InvalidAccommodationDataException {
        if (numOfSharedBeds < 0)
            throw new InvalidAccommodationDataException("Number of shared beds cannot be less than 0.");

        this.numOfSharedBeds = numOfSharedBeds;
    }

    @Override
    public void setPricePerNight(double pricePerNight) throws InvalidAccommodationDataException {
        super.setPricePerNight(pricePerNight);
        if (pricePerNight > 150.0) {
            throw new InvalidAccommodationDataException("Hostel price per night cannot exceed $150.");
        }
    }

    @Override
    public String toString() {return super.toString() + ";" + this.numOfSharedBeds;}

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || this.getClass() != other.getClass())
            return false;

        Hostel otherHostel = (Hostel) other;

        return super.equals(otherHostel)
            && this.numOfSharedBeds == otherHostel.numOfSharedBeds;
    }

    @Override
    protected double calculateCost(int numOfDays) {
        return this.getPricePerNight() * numOfDays;
    }
}
