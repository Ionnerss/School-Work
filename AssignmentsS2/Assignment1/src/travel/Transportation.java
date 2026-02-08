package AssignmentsS2.Assignment1.src.travel;

abstract class Transportation {
    private static int nextID = 3001;
    private String transportID, companyName, departureCity, arrivalCity;

    public Transportation() {
        this.transportID = "TR" + nextID++;
        this.companyName = "";
        this.departureCity = "";
        this.arrivalCity = "";
    }

    public Transportation(String companyName, String departureCity, String arrivalCity) {
        this.transportID = "TR" + nextID++;
        this.companyName = companyName;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
    }

    public Transportation(Transportation other) {
        this.transportID = "TR" + nextID++;
        this.companyName = other.companyName;
        this.departureCity = other.departureCity;
        this.arrivalCity = other.arrivalCity;
    }

    public String getTransportID() {return this.transportID;}
    public void setTransportID(String transportID) {this.transportID = transportID;}

    public String getCompanyName() {return this.companyName;}
    public void setCompanyName(String companyName) {this.companyName = companyName;}

    public String getDepartureCity() {return this.departureCity;}
    public void setDepartureCity(String departureCity) {this.departureCity = departureCity;}

    public String getArrivalCity() {return this.arrivalCity;}
    public void setArrivalCity(String arrivalCity) {this.arrivalCity = arrivalCity;}

    @Override
    public String toString() {return this.transportID + ", " + this.companyName + ", " + this.departureCity + ", " + this.arrivalCity;}

    @Override
    public boolean equals(Object other) {
        if (other == null || this.getClass() != other.getClass()) 
            return false;
        
        Transportation otherTr = (Transportation) other;
        
        return this.getCompanyName() == otherTr.getCompanyName()
            && this.getDepartureCity() == otherTr.getDepartureCity()
            && this.getArrivalCity() == otherTr.getArrivalCity();
    }

    public abstract double calculateCost(int numOfDays);
}
