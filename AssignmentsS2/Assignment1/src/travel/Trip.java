package AssignmentsS2.Assignment1.src.travel;

import AssignmentsS2.Assignment1.src.client.Client;

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

    public Trip(String destination, int duration, double basePrice, Client client) {
        this.tripID = "T" + nextID++;
        this.destination = destination;
        this.duration = duration;
        this.basePrice = basePrice;
        this.client = client;
    }

    public Trip(Trip other) {
        this.tripID = "T" + nextID++;
        this.destination = other.destination;
        this.duration = other.duration;
        this.basePrice = other.basePrice;
        this.client = other.client;
    }

    public String getTripId() {return tripID;}
    public void setTripId(String tripID) {this.tripID = tripID;}

    public String getDestination() {return destination;}
    public void setDestination(String destination) {this.destination = destination;}

    public int getDurationInDays() {return duration;}
    public void setDurationInDays(int duration) {this.duration = duration;}

    public double getBasePrice() {return basePrice;}
    public void setBasePrice(double basePrice) {this.basePrice = basePrice;}

    public Client getClient() {return client;}
    public void setClient(Client client) {this.client = client;}

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
