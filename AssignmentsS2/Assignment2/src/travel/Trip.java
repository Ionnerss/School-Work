package AssignmentsS2.Assignment2.src.travel;
// -------------------------------------------------------- 
// Assignment 1 - Trip Class
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S – Fall 2025
// --------------------------------------------------------

import AssignmentsS2.Assignment2.src.client.Client;
import AssignmentsS2.Assignment2.src.exceptions.InvalidTripDataException;

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
        this.destination = "No Destination";
        this.duration = 0;
        this.basePrice = 0.0;
        this.client = null;
        this.transport = null;
        this.accomodation = null;
    }

    public Trip(Client client, Accomodation accomodation, Transportation transport, String destination, int duration, double basePrice) 
        throws InvalidTripDataException {
        this.tripID = "T" + nextID++;
        setDestination(destination);
        setDurationInDays(duration);
        setBasePrice(basePrice);
        setClient(client);
        setTransportation(transport);
        setAccomodation(accomodation);
    }

    public Trip(String destination, int duration, double basePrice, Client client)
        throws InvalidTripDataException {
        this(client, null, null, destination, duration, basePrice);
    }

    public Trip(Trip other) {
        this.tripID = "T" + nextID++;
        this.destination = other.destination;
        this.duration = other.duration;
        this.basePrice = other.basePrice;
        this.client = other.client;
        this.transport = other.transport;
        this.accomodation = other.accomodation;
    }

    public Trip(String tripId, Client client, Accomodation accomodation, Transportation transport, String destination, int duration, double basePrice) 
        throws InvalidTripDataException {
        if (tripId == null || tripId.trim().isEmpty()) {
            throw new IllegalArgumentException("Trip ID cannot be empty.");
        }

        String trimmed = tripId.trim();

        if (!trimmed.matches("T\\d+")) {
            throw new IllegalArgumentException("Invalid Client ID format: " + tripId);
        }

        this.tripID = trimmed;
        setDestination(destination);
        setDurationInDays(duration);
        setBasePrice(basePrice);
        setClient(client);
        setTransportation(transport);
        setAccomodation(accomodation);
    }

    public String getTripId() {return tripID;}
    public String getDestination() {return destination;}
    public int getDurationInDays() {return duration;}
    public double getBasePrice() {return basePrice;}
    public Client getClient() {return client;}
    public Transportation geTransportation() {return transport;}
    public Accomodation geAccomodation() {return accomodation;}

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
        if (client == null || client.getClientID() == null)
            throw new InvalidTripDataException("Client ID is null.");

        this.client = client;
    }

    public void setTransportation(Transportation transport) throws InvalidTripDataException {
        if (transport == null) {
            this.transport = null;
            return;
        }

        if (transport.getTransportID() == null)
            throw new InvalidTripDataException("Transport ID is null.");

        this.transport = transport;
    }

    public void setAccomodation(Accomodation accomodation) throws InvalidTripDataException {
        if (accomodation == null) {
            this.accomodation = null;
            return;
        }

        if (accomodation.getAccomodationID() == null)
            throw new InvalidTripDataException("Accomodation ID is null.");

        this.accomodation = accomodation;
    }

    public static void syncNextId(int nextNumericId) {
        if (nextNumericId > nextID)
            nextID = nextNumericId;
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
        double total = this.basePrice;

        if (transport != null)
            total += transport.calculateCost(duration);

        if (accomodation != null)
            total += accomodation.calculateCost(duration);

        return total;
    }
}
