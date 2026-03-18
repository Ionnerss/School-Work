package AssignmentsS2.Assignment2Remastered.src.travel;
// -------------------------------------------------------- 
// Assignment 1 - Hotel Class
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S – Fall 2025
// --------------------------------------------------------

import AssignmentsS2.Assignment2Remastered.src.exceptions.InvalidAccommodationDataException;

public class Hotel extends Accomodation {
    private int starRating;
    
    public Hotel() {
        super();
        this.starRating = 0;
    }

    public Hotel(String name, String location, double pricePerNight, int starRating) throws InvalidAccommodationDataException {
        super(name, location, pricePerNight);
        this.starRating = starRating;
    }

    public Hotel(String id, String name, String location, double pricePerNight, int starRating) throws InvalidAccommodationDataException {
        super(id, name, location, pricePerNight);
        this.starRating = starRating;
    }

    public Hotel(Hotel other) {
        super(other);
        this.starRating = other.starRating;
    }

    public double getStartRating() {return this.starRating;}

    public void setStartRating(int starRating) throws InvalidAccommodationDataException {
        if (starRating < 0)
            throw new InvalidAccommodationDataException("Start rating cannot be less than 0.");

        this.starRating = starRating;
    }

    @Override
    public String toString() {return super.toString() + ", " + this.starRating;}

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || this.getClass() != other.getClass())
            return false;

        Hotel otherHotel = (Hotel) other;

        return super.equals(otherHotel)
            && this.starRating == otherHotel.starRating;
    }

    @Override
    public double calculateCost(int numOfDays) {
        double costPerNight = this.getPricePerNight();
        return costPerNight * numOfDays;
    }
}
