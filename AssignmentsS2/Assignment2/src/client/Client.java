package AssignmentsS2.Assignment2.src.client;

import AssignmentsS2.Assignment2.src.exceptions.InvalidClientDataException;
import AssignmentsS2.Assignment2.src.service.SmartTravelService;
import AssignmentsS2.Assignment2.src.travel.Trip;

public class Client {
    private static int nextID = 1001;
    private String clientID, firstName, lastName, emailAdress;

    public Client() {
        this.clientID = "C" + nextID++;
        this.firstName = "";
        this.lastName = "";
        this.emailAdress = "";
    }

    
    public Client(String firstName, String lastName, String emailAdress) throws InvalidClientDataException {
        this.clientID = "C" + nextID++;
        setFirstName(firstName);
        setLastName(lastName);
        setEmailAdress(emailAdress);
    }
    
    public Client (String clientID, String firstName, String lastName, String emailAdress) throws InvalidClientDataException {
        if (clientID == null || clientID.trim().isEmpty()) {
            throw new InvalidClientDataException("Client ID cannot be empty.");
        }

        String trimmed = clientID.trim();

        if (!trimmed.matches("C\\d+")) {
            throw new InvalidClientDataException("Invalid Client ID format: " + clientID);
        }

        this.clientID = trimmed;
        setFirstName(firstName);
        setLastName(lastName);
        setEmailAdress(emailAdress);
    }

    public Client(Client other) {
        this.clientID = "C" + nextID++;
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.emailAdress = other.emailAdress;
    }

    public String getClientID() {return this.clientID;}
    public String getFirstName() {return this.firstName;}
    public String getLastName() {return this.lastName;}
    public String getEmailAdress() {return this.emailAdress;}

    public void setFirstName(String firstName) throws InvalidClientDataException {
        this.firstName = validateName(firstName, "First name");
    }
    public void setLastName(String lastName) throws InvalidClientDataException {
        this.lastName = validateName(lastName, "Last name");
    }
    public void setEmailAdress(String emailAdress) throws InvalidClientDataException {
        this.emailAdress = validateEmail(emailAdress);
    }

    private static String validateName(String name, String field) throws InvalidClientDataException {
        if (name == null)
            throw new InvalidClientDataException(field + " cannot be null.");

        String trimmed = name.trim();
        if (trimmed.isEmpty())
            throw new InvalidClientDataException(field + " cannot be null.");
        if (trimmed.length() > 50)
            throw new InvalidClientDataException(field + " must be less or equal to 50 characters.");

        return trimmed;
    }

    private static String validateEmail(String email) throws InvalidClientDataException {
        if (email == null)
            throw new InvalidClientDataException("Email cannot be null.");

        String trimmed = email.trim();
        if (trimmed.isEmpty())
            throw new InvalidClientDataException("Email cannot be null.");
        if (trimmed.length() > 100)
            throw new InvalidClientDataException("Email must be less or equal to 100 characters.");
        if (trimmed.contains(" "))
            throw new InvalidClientDataException("Email cannot contain spaces \" \".");
        if (!trimmed.contains("@"))
            throw new InvalidClientDataException("Invalid email, does not contain \"@\".");
        if (!trimmed.contains("."))
            throw new InvalidClientDataException("Invalid email, does not contain \".\".");

        return trimmed;
    }

    public static void syncNextId(int nextNumericId) {
        if (nextNumericId > nextID)
            nextID = nextNumericId;
    }

    @Override
    public String toString() {return this.clientID + ": " + this.firstName + ", " + this.lastName + ", " + this.emailAdress;}

    @Override
    public boolean equals(Object other) {
        if (other == null || this.getClass() != other.getClass()) 
            return false;
        
        Client otherClient = (Client) other;
        
        return this.getFirstName().equals(otherClient.getFirstName())
            && this.getLastName().equals(otherClient.getLastName())
            && this.getEmailAdress().equals(otherClient.getEmailAdress());
    }


    public double getTotalSpent() {
        SmartTravelService service = new SmartTravelService();
        Trip[] trips = service.getTrips();

        if (trips == null || trips.length == 0)
            return 0.0;

        int limit = Math.min(service.getTripCount(), trips.length);
        double totalSpent = 0.0;

        for (int i = 0; i < limit; i++) {
            Trip trip = trips[i];
            if (trip == null)
                continue;

            if (this.clientID.equals(trip.getClient())) {
                try {
                    totalSpent += service.calculateTripTotal(i);
                }
                catch (Exception ignored) {
                    // Skip invalid trip references and continue summing valid ones.
                }
            }
        }

        return totalSpent;
    }
}
