package AssignmentsS2.Assignment2.src.client;

/*
 * Assignment 2
 * Question: SmartTravel Client class
 * Written by: Catalin-Ion Besleaga (40347936)
 *
 * This class stores client information, validates client data,
 * manages IDs, and computes total spending across trips.
 */

import AssignmentsS2.Assignment2.src.exceptions.InvalidClientDataException;

public class Client {
    private static int nextID = 1001;
    private String clientId, firstName, lastName, emailAdress;
    private double amountSpent;

    public Client() {
    this.clientId = "C" + nextID++;
    try {
        setFirstName("Unknown");
        setLastName("Client");
        setEmailAdress("placeholder" + this.clientId.substring(1) + "@example.com");
    } catch (InvalidClientDataException e) {
        throw new IllegalStateException("Default client initialization failed.", e);
    }
    this.amountSpent = 0.0;
    }

    public Client(String firstName, String lastName, String emailAdress) throws InvalidClientDataException {
        this.clientId = "C" + nextID++;
        setFirstName(firstName);
        setLastName(lastName);
        setEmailAdress(emailAdress);
        this.amountSpent = 0.0;
    }

    public Client(String clientID, String firstName, String lastName, String emailAdress) throws InvalidClientDataException {
        if (clientID == null || clientID.trim().isEmpty()) {
            throw new InvalidClientDataException("Client ID cannot be empty.");
        }

        String trimmed = clientID.trim();

        if (!trimmed.matches("C\\d+")) {
            throw new InvalidClientDataException("Invalid Client ID format: " + clientID);
        }

        this.clientId = trimmed;
        setFirstName(firstName);
        setLastName(lastName);
        setEmailAdress(emailAdress);
        this.amountSpent = 0.0;
    }

    public Client(Client other) {
        this.clientId = "C" + nextID++;
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.emailAdress = other.emailAdress;
        this.amountSpent = other.amountSpent;
    }

    public String getClientId() {return this.clientId;}
    public String getFirstName() {return this.firstName;}
    public String getLastName() {return this.lastName;}
    public String getEmailAdress() {return this.emailAdress;}
    public double getAmountSpent() {return this.amountSpent;}

    public void setFirstName(String firstName) throws InvalidClientDataException {
        this.firstName = validateName(firstName, "First name");
    }
    public void setLastName(String lastName) throws InvalidClientDataException {
        this.lastName = validateName(lastName, "Last name");
    }
    public void setEmailAdress(String emailAdress) throws InvalidClientDataException {
        this.emailAdress = validateEmail(emailAdress);
    }

    public static String validateName(String name, String field) throws InvalidClientDataException {
        if (name == null)
            throw new InvalidClientDataException(field + " cannot be null.");

        String trimmed = name.trim();
        if (trimmed.isEmpty())
            throw new InvalidClientDataException(field + " cannot be null.");
        if (trimmed.length() > 50)
            throw new InvalidClientDataException(field + " must be less or equal to 50 characters.");

        return trimmed;
    }

    public static String validateEmail(String email) throws InvalidClientDataException {
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

    public void resetAmountSpent() {
        this.amountSpent = 0.0;
    }

    public void addToAmountSpent(double amount) {
        if (amount > 0) {
            this.amountSpent += amount;
        }
    }

    @Override
    public String toString() {return this.clientId + ": " + this.firstName + ", " + this.lastName + ", " + this.emailAdress;}

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
        return this.amountSpent;
    }
}
