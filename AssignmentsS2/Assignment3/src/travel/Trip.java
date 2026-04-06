package AssignmentsS2.Assignment3.src.travel;

/*
 * Assignment 2
 * Question: SmartTravel Trip class
 * Written by: Catalin-Ion Besleaga (40347936)
 *
 * This class stores trip information, validates trip rules, resolves
 * linked entities by ID, and computes the total trip cost.
 */

import AssignmentsS2.Assignment3.src.service.SmartTravelService;
import AssignmentsS2.Assignment3.src.exceptions.*;

public class Trip {
    private static int nextID = 2001;
    private String destination, tripId, clientId, accomodationId, transportId;
    private int duration;
    private double basePrice;


    public Trip() {
    throw new UnsupportedOperationException(
        "Default Trip constructor is not supported because a trip requires a valid client ID and at least one linked accommodation or transportation ID."
    );
}

    public Trip(String clientId, String accomodationId, String transportId, String destination, int duration, double basePrice)
        throws InvalidTripDataException, EntityNotFoundException {
        this.tripId = "T" + nextID++;
        setDestination(destination);
        setDurationInDays(duration);
        setBasePrice(basePrice);
        setClientId(clientId);
        setTransportationId(transportId);
        setAccomodationId(accomodationId);
    }

    public Trip(String destination, int duration, double basePrice, String clientId, String accomodationId, String transportId)
        throws InvalidTripDataException, EntityNotFoundException {
        this(clientId, accomodationId, transportId, destination, duration, basePrice);
    }

    public Trip(Trip other) {
        this.tripId = "T" + nextID++;
        this.destination = other.destination;
        this.duration = other.duration;
        this.basePrice = other.basePrice;
        this.clientId = other.clientId;
        this.transportId = other.transportId;
        this.accomodationId = other.accomodationId;
    }

    public Trip(String tripId, String clientId, String accomodationId, String transportId, String destination, int duration, double basePrice)
        throws InvalidTripDataException, EntityNotFoundException {
        if (tripId == null || tripId.trim().isEmpty()) {
            throw new InvalidTripDataException("Trip ID cannot be empty.");
        }

        String trimmed = tripId.trim();

        if (!trimmed.matches("T\\d+")) {
            throw new InvalidTripDataException("Invalid Trip ID format: " + tripId);
        }

        this.tripId = trimmed;
        setDestination(destination);
        setDurationInDays(duration);
        setBasePrice(basePrice);
        setClientId(clientId);
        setTransportationId(transportId);
        setAccomodationId(accomodationId);
    }

    public String getTripId() {return tripId;}
    public String getDestination() {return destination;}
    public int getDurationInDays() {return duration;}
    public double getBasePrice() {return basePrice;}
    public String getClientId() {return clientId;}
    public String geTransportationId() {return transportId;}
    public String geAccomodationId() {return accomodationId;}

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
            throw new InvalidTripDataException("Invalid trip base price, must be greater than or equal to 100.");
        this.basePrice = basePrice;
    }

    public void setClientId(String clientId) throws InvalidTripDataException, EntityNotFoundException {
        if (clientId == null || clientId.trim().isEmpty())
            throw new InvalidTripDataException("Client ID is mandatory.");

        String trimmed = clientId.trim();
        if (!trimmed.matches("C\\d+"))
            throw new InvalidTripDataException("Invalid Client ID format: " + clientId);

        SmartTravelService.findClientByIdObj(trimmed);
        this.clientId = trimmed;
    }

    public void setTransportationId(String transportId) throws EntityNotFoundException {
        if (transportId == null || transportId.trim().isEmpty()) {
            this.transportId = "";
            return;
        }

        String trimmed = transportId.trim();
        SmartTravelService.findTransportationById(trimmed);
        this.transportId = trimmed;
    }

    public void setAccomodationId(String accomodationId) throws EntityNotFoundException {
        if (accomodationId == null || accomodationId.trim().isEmpty()) {
            this.accomodationId = "";
            return;
        }

        String trimmed = accomodationId.trim();
        SmartTravelService.findAccommodationById(trimmed);
        this.accomodationId = trimmed;
    }

    public static void syncNextId(int nextNumericId) {
        if (nextNumericId > nextID)
            nextID = nextNumericId;
    }

    @Override
    public String toString() {return this.tripId + "; " + this.clientId + ";" + this.accomodationId + ";"
        + this.transportId + ";" + this.destination + ";" + this.duration + ";" + this.basePrice;}
    
    @Override
    public boolean equals(Object other) {
        if (other == null || this.getClass() != other.getClass()) 
            return false;
        
        Trip otherTrip = (Trip) other;
        
        return this.getTripId().equals(otherTrip.getTripId())
        && this.getDestination().equals(otherTrip.getDestination())
        && this.getDurationInDays() == otherTrip.getDurationInDays()
        && this.getBasePrice() == otherTrip.getBasePrice()
        && this.getClientId().equals(otherTrip.getClientId());
    }

    public double calculateTotalCost() throws EntityNotFoundException {
        double total = this.basePrice;

        if (transportId != null && !transportId.trim().equalsIgnoreCase(""))
            total += SmartTravelService.findTransportationById(transportId).calculateCost(duration);

        if (accomodationId != null && !accomodationId.trim().equalsIgnoreCase(""))
            total += SmartTravelService.findAccommodationById(accomodationId).calculateCost(duration);

        return total;
    }
}
