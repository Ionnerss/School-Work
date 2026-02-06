package AssignmentsS2.Assignment1.src.travel;

import AssignmentsS2.Assignment1.src.client.Client;

public class Trip {
    private static int nextID = 2001;
    private String destination, tripID;
    private int duration;
    private double basePrice;
    private Client client;

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

    public String getTripID() {return tripID;}
    public void setTripID(String tripID) {this.tripID = tripID;}

    public String getDestination() {return destination;}
    public void setDestination(String destination) {this.destination = destination;}

    public int getDuration() {return duration;}
    public void setDuration(int duration) {this.duration = duration;}

    public double getBasePrice() {return basePrice;}
    public void setBasePrice(double basePrice) {this.basePrice = basePrice;}

    public Client getClient() {return client;}
    public void setClient(Client client) {this.client = client;}
}
