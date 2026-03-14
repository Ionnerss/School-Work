package AssignmentsS2.Assignment1.src.travel;
// -------------------------------------------------------- 
// Assignment 1 - Trip Class
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S – Fall 2025
// --------------------------------------------------------

import AssignmentsS2.Assignment1.src.client.Client;
import AssignmentsS2.Assignment1.src.exceptions.InvalidTripDataException;

public class Trip {
    private static int nextID = 2001;
    private String destination, tripID;
    private int duration;
    private double basePrice;
    private Client client;
    private Transportation transport;
    private Accomodation accomodation;

    public Trip() {
        this.tripID = "T" + nextID++;
        this.destination = "";
        this.duration = 0;
        this.basePrice = 0.0;
        this.client = null;
    }

    public Trip(String destination, int duration, double basePrice, Client client) throws InvalidTripDataException {
        this.tripID = "T" + nextID++;
        setDestination(destination);
        setDurationInDays(duration);
        setBasePrice(basePrice);
        setClient(client);
    }

    public Trip(Trip other) {
        this.tripID = "T" + nextID++;
        this.destination = other.destination;
        this.duration = other.duration;
        this.basePrice = other.basePrice;
        this.client = other.client;
    }

    public String getTripId() {return tripID;}
    public String getDestination() {return destination;}
    public int getDurationInDays() {return duration;}
    public double getBasePrice() {return basePrice;}
    public Client getClient() {return client;}

    public static void syncNextId(int nextNumericId) {
        if (nextNumericId > nextID)
            nextID = nextNumericId;
    }

    public void setDestination(String destination) throws InvalidTripDataException {
        if (destination == null)
            throw new InvalidTripDataException("Destination cannot be null.");

        String trimmed = destination.trim();
        if (trimmed.isEmpty())
            throw new InvalidTripDataException("Destination cannot be null.");
        this.destination = trimmed;
    }

    public void setDurationInDays(int duration) throws InvalidTripDataException {
        if (duration < 1 || duration > 20)
            throw new InvalidTripDataException("Invalid trip duration, must be between 1 and 20 days (inclusive).");
        this.duration = duration;
    }

    public void setBasePrice(double basePrice) throws InvalidTripDataException {
        if (basePrice < 100)
            throw new InvalidTripDataException("Invalid trip base price, must be greater or equal to $100.");
        this.basePrice = basePrice;
    }

    public void setClient(Client client) throws InvalidTripDataException {
        if (client.getClientID() == null) throw new InvalidTripDataException("Client ID is null.");

        this.client = client;
    }


    @Override
    public String toString() {return this.tripID + ", " + this.destination + ", " + this.duration + ", " + 
    this.basePrice + ", " + this.client;}
    
    @Override
    public boolean equals(Object other) {
        if (other == null || this.getClass() != other.getClass()) 
            return false;
        
        Trip otherTrip = (Trip) other;
        
        return this.getTripId().equals(otherTrip.getTripId())
        && this.getDestination().equals(otherTrip.getDestination())
        && this.getDurationInDays() == otherTrip.getDurationInDays()
        && this.getBasePrice() == otherTrip.getBasePrice()
        && this.getClient() == otherTrip.getClient();
    }

    public double calculateTotalCost() {
        if (transport == null && accomodation == null)
            return 0.0;

        return this.basePrice + transport.calculateCost(duration) + accomodation.calculateCost(duration);
    }
}
