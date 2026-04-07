package AssignmentsS2.Assignment3.src.travel;

/*
 * Assignment 2
 * Question: SmartTravel Accommodation abstract class
 * Written by: Catalin-Ion Besleaga (40347936)
 *
 * This abstract parent class represents common accommodation data and
 * validation shared by hotels and hostels.
 */

import AssignmentsS2.Assignment3.src.exceptions.InvalidAccommodationDataException;
import AssignmentsS2.Assignment3.src.interfaces.CsvPersistable;
import AssignmentsS2.Assignment3.src.interfaces.Identifiable;

public abstract class Accommodation implements Identifiable, CsvPersistable, Comparable<Accommodation> {
    private static int nextID = 4001;
    private String accomodationID, name, location;
    private double pricePerNight;

    public Accommodation() {
        this.accomodationID = "A" + nextID++;
        try {
            setName("Unknown Accommodation");
            setLocation("Unknown");
            setPricePerNight(1.0);
        } catch (InvalidAccommodationDataException e) {
            throw new IllegalStateException("Default accommodation initialization failed.", e);
        }
    }

    public Accommodation(String name, String location, double pricePerNight) throws InvalidAccommodationDataException {
        this.accomodationID = "A" + nextID++;
        setName(name);
        setLocation(location);
        setPricePerNight(pricePerNight);
    }

    public Accommodation(String accomodationID, String name, String location, double pricePerNight) throws InvalidAccommodationDataException {
        if (accomodationID == null || accomodationID.trim().isEmpty()) {
            throw new InvalidAccommodationDataException("Accommodation ID cannot be empty.");
        }

        String trimmed = accomodationID.trim();

        if (!trimmed.matches("A\\d+")) {
            throw new InvalidAccommodationDataException("Invalid Accommodation ID format: " + accomodationID);
        }

        this.accomodationID = trimmed;
        setName(name);
        setLocation(location);
        setPricePerNight(pricePerNight);
    }

    public Accommodation(Accommodation other) {
        this.accomodationID = "A" + nextID++;
        this.name = other.name;
        this.location = other.location;
        this.pricePerNight = other.pricePerNight;
    }

    @Override
    public String getId() {return this.accomodationID;}
    public String getName() {return this.name;}
    public String getLocation() {return this.location;}
    public double getPricePerNight() {return this.pricePerNight;}

     public void setName(String name) throws InvalidAccommodationDataException {
        if (name == null)
            throw new InvalidAccommodationDataException("Name cannot be null.");

        String trimmed = name.trim();
        if (trimmed.isEmpty())
            throw new InvalidAccommodationDataException("Name cannot be null.");

        this.name = trimmed;
    }

    public void setLocation(String location) throws InvalidAccommodationDataException {
        if (location == null)
            throw new InvalidAccommodationDataException("Location cannot be null.");

        String trimmed = location.trim();
        if (trimmed.isEmpty())
            throw new InvalidAccommodationDataException("Location city cannot be null.");

        this.location = trimmed;
    }

    public void setPricePerNight(double pricePerNight) throws InvalidAccommodationDataException {
        if (pricePerNight <= 0.0)
            throw new InvalidAccommodationDataException("Price per night must be greater than 0.");

        this.pricePerNight = pricePerNight;
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

        Accommodation otherAcc = (Accommodation) other;

        return this.getName().equals(otherAcc.getName())
            && this.getLocation().equals(otherAcc.getLocation())
            && this.getPricePerNight() == otherAcc.getPricePerNight();
    }
    
    protected abstract double calculateCost(int numOfDays);

    protected String toBaseCsvRow() {
        return this.accomodationID + ";" + this.name + ";" + this.location + ";" + this.pricePerNight;
    }

    public static Accommodation fromCsvRow(String csvLine) throws InvalidAccommodationDataException {
        if (csvLine == null)
            throw new InvalidAccommodationDataException("CSV row cannot be null.");

        String[] parts = csvLine.split(";", -1);

        if (parts.length != 6)
            throw new InvalidAccommodationDataException("Accommodation CSV row must have exactly 6 fields.");

        String type = parts[0].trim().toUpperCase();
        String id = parts[1].trim();
        String name = parts[2].trim();
        String location = parts[3].trim();

        if (id.isEmpty() || name.isEmpty() || location.isEmpty()
                || parts[4].trim().isEmpty() || parts[5].trim().isEmpty())
            throw new InvalidAccommodationDataException("Accommodation required CSV fields cannot be empty.");

        double pricePerNight;
        int extraValue;

        try {
            pricePerNight = Double.parseDouble(parts[4].trim());
            extraValue = Integer.parseInt(parts[5].trim());
        } catch (NumberFormatException e) {
            throw new InvalidAccommodationDataException("Accommodation CSV numeric fields are invalid.");
        }

        if (type.equals("HOTEL"))
            return new Hotel(id, name, location, pricePerNight, extraValue);
        else if (type.equals("HOSTEL"))
            return new Hostel(id, name, location, pricePerNight, extraValue);
        else
            throw new InvalidAccommodationDataException("Unknown accommodation type: " + type);
    }

    @Override
    public int compareTo(Accommodation other) {
        if (other == null)
            return -1;

        return Double.compare(other.getPricePerNight(), this.getPricePerNight());
    }

    @Override
    public abstract String toCsvRow();
}
