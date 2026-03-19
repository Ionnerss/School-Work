package AssignmentsS2.Assignment2.src.travel;
// -------------------------------------------------------- 
// Assignment 1 - Accomodation Class
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S – Fall 2025
// --------------------------------------------------------

import AssignmentsS2.Assignment2.src.exceptions.InvalidAccommodationDataException;

public abstract class Accomodation {
    private static int nextID = 4001;
    private String accomodationID, name, location;
    private double pricePerNight;

    public Accomodation() {
        this.accomodationID = "A" + nextID++;
        this.name = "";
        this.location = "";
        this.pricePerNight = 0.0;
    }

    public Accomodation(String name, String location, double pricePerNight) throws InvalidAccommodationDataException {
        this.accomodationID = "A" + nextID++;
        setName(name);
        setLocation(location);
        setPricePerNight(pricePerNight);
    }

    public Accomodation(String accomodationID, String name, String location, double pricePerNight) throws InvalidAccommodationDataException {
        if (accomodationID == null || accomodationID.trim().isEmpty()) {
            throw new IllegalArgumentException("Accomodation ID cannot be empty.");
        }

        String trimmed = accomodationID.trim();

        if (!trimmed.matches("C\\d+")) {
            throw new IllegalArgumentException("Invalid Accomodation ID format: " + accomodationID);
        }

        this.accomodationID = trimmed;
        setName(name);
        setLocation(location);
        setPricePerNight(pricePerNight);
    }

    public Accomodation(Accomodation other) {
        this.accomodationID = "A" + nextID++;
        this.name = other.name;
        this.location = other.location;
        this.pricePerNight = other.pricePerNight;
    }

    public String getAccomodationID() {return this.accomodationID;}
    public String getName() {return this.name;}
    public String getLocation() {return this.location;}
    public double getPricePerNight() {return this.pricePerNight;}

    public static void syncNextId(int nextNumericId) {
        if (nextNumericId > nextID)
            nextID = nextNumericId;
    }

     public void setName(String name) throws InvalidAccommodationDataException {
        if (name == null)
            throw new InvalidAccommodationDataException("Name cannot be null.");

        String trimmed = name.trim();
        if (trimmed.isEmpty())
            throw new InvalidAccommodationDataException("Name cannot be null.");

        this.name = name;
    }

    public void setLocation(String location) throws InvalidAccommodationDataException {
        if (location == null)
            throw new InvalidAccommodationDataException("Location cannot be null.");

        String trimmed = location.trim();
        if (trimmed.isEmpty())
            throw new InvalidAccommodationDataException("Location city cannot be null.");

        this.location = location;
    }

    public void setPricePerNight(double pricePerNight) throws InvalidAccommodationDataException {
        if (pricePerNight == 0.0)
            throw new InvalidAccommodationDataException("Price per night cannot be 0.");

        this.pricePerNight = pricePerNight;
    }

    @Override
    public String toString() {return this.accomodationID + ", " + this.name + ", " + this.location + ", " + this.pricePerNight;}

    @Override
    public boolean equals(Object other) {
        if (other == null || this.getClass() != other.getClass())
            return false;

        Accomodation otherAcc = (Accomodation) other;

        return this.getName().equals(otherAcc.getName())
            && this.getLocation().equals(otherAcc.getLocation())
            && this.getPricePerNight() == otherAcc.getPricePerNight();
    }
    
    public abstract double calculateCost(int numOfDays);
}
