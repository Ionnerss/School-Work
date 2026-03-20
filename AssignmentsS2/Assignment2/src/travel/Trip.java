package AssignmentsS2.Assignment2.src.travel;
// -------------------------------------------------------- 
// Assignment 1 - Trip Class
// Written by: Catalin-Ion Besleaga (40347936)
// For COMP 248 Section S – Fall 2025
// --------------------------------------------------------

import AssignmentsS2.Assignment2.src.service.SmartTravelService;
import AssignmentsS2.Assignment2.src.exceptions.*;

public class Trip {
    private static int nextID = 2001;
    private String destination, tripId, clientId, accomodationId, transportId;
    private int duration;
    private double basePrice;


    public Trip() {
        this.tripId = "T" + nextID++;
        this.destination = "No Destination";
        this.duration = 0;
        this.basePrice = 0.0;
        this.clientId = "";
        this.transportId = "";
        this.accomodationId = "";
    }

    public Trip(String clientId, String accomodationId, String transportId, String destination, int duration, double basePrice) 
            throws InvalidTripDataException, InvalidClientDataException, EntityNotFoundException, InvalidTransportDataException, 
                    InvalidAccommodationDataException {
        this.tripId = "T" + nextID++;
        setDestination(destination);
        setDurationInDays(duration);
        setBasePrice(basePrice);
        setClientId(clientId);
        setTransportationId(transportId);
        setAccomodationId(accomodationId);
    }

    public Trip(String destination, int duration, double basePrice, String clientId, String accomodationId, String transportId)
            throws InvalidTripDataException, InvalidClientDataException, EntityNotFoundException, InvalidTransportDataException, InvalidAccommodationDataException {
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
            throws InvalidTripDataException, InvalidClientDataException, EntityNotFoundException, InvalidTransportDataException, InvalidAccommodationDataException {
        if (tripId == null || tripId.trim().isEmpty()) {
            throw new IllegalArgumentException("Trip ID cannot be empty.");
        }

        String trimmed = tripId.trim();

        if (!trimmed.matches("T\\d+")) {
            throw new IllegalArgumentException("Invalid Client ID format: " + tripId);
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
    public String getClient() {return clientId;}
    public String geTransportation() {return transportId;}
    public String geAccomodation() {return accomodationId;}

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
            throw new InvalidTripDataException("Invalid trip base price, must be greater or equal to .");
        this.basePrice = basePrice;
    }

    public void setClientId(String clientId) throws InvalidTripDataException, InvalidClientDataException, EntityNotFoundException {
        if (clientId == null || clientId.trim().isEmpty())
            throw new InvalidTripDataException("Client ID is null.");
        
        if (SmartTravelService.findClientByIdObj(clientId).getClientID() == null || 
                SmartTravelService.findClientByIdObj(clientId) == null)
            throw new EntityNotFoundException("Client ID is null or does not exist.");
            

        this.clientId = clientId;
    }

    public void setTransportationId(String transportId) throws InvalidTripDataException, InvalidTransportDataException, EntityNotFoundException {
        if (transportId == null || transportId.trim().isEmpty())
            throw new InvalidTripDataException("Transportation ID is null.");
        
        if (SmartTravelService.findTransportationById(transportId).getTransportID() == null || 
                SmartTravelService.findTransportationById(transportId) == null)
            throw new EntityNotFoundException("Transport ID is null or does not exist.");

        this.transportId = transportId;
    }

    public void setAccomodationId(String accomodationId) throws InvalidTripDataException, InvalidAccommodationDataException, EntityNotFoundException {
        if (accomodationId == null || accomodationId.trim().isEmpty())
            throw new InvalidTripDataException("Accomodation ID is null.");
        
        if (SmartTravelService.findAccommodationById(accomodationId).getAccomodationID() == null ||
                SmartTravelService.findAccommodationById(accomodationId) == null)
            throw new EntityNotFoundException("Accomodation ID is null or does not exist.");

        this.accomodationId = accomodationId;
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
        && this.getClient() == otherTrip.getClient();
    }

    public double calculateTotalCost() throws InvalidAccommodationDataException, InvalidTransportDataException {
        double total = this.basePrice;

        if (transportId != null && !transportId.trim().equalsIgnoreCase(""))
            total += SmartTravelService.findTransportationById(transportId).calculateCost(duration);

        if (accomodationId != null && !accomodationId.trim().equalsIgnoreCase(""))
            total += SmartTravelService.findAccommodationById(accomodationId).calculateCost(duration);

        return total;
    }
}
