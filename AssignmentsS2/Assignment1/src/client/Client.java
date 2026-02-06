package AssignmentsS2.Assignment1.src.client;

public class Client {
    private static int nextID = 1001;
    private String clientID, firstName, lastName, emailAdress;

    public Client() {
        this.clientID = "C" + nextID++;
        this.firstName = "";
        this.lastName = "";
        this.emailAdress = "";
    }

    public Client(String firstName, String lastName, String emailAdress) {
        this.clientID = "C" + nextID++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAdress = emailAdress;
    }

    public Client(Client other) {
        this.clientID = "C" + nextID++;
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.emailAdress = other.emailAdress;
    }

    public String getClientID() {return this.clientID;}
    public void setClientID(String clientID) {this.clientID = clientID;}

    public String getFirstName() {return this.firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return this.lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    
    public String getEmailAdress() {return this.emailAdress;}
    public void setEmailAdress(String emailAdress) {this.emailAdress = emailAdress;}

    @Override
    public String toString() {return this.clientID + ", " + this.firstName + ", " + this.lastName + ", " + this.emailAdress;}

    @Override
    public boolean equals(Object other) {
        if (other == null || this.getClass() != other.getClass()) 
            return false;
        
        Client otherClient = (Client) other;
        
        return this.getFirstName() == otherClient.getFirstName()
            && this.getLastName() == otherClient.getLastName()
            && this.getEmailAdress() == otherClient.getEmailAdress();
    }
}
