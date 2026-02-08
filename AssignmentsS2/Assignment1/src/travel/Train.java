package AssignmentsS2.Assignment1.src.travel;

public class Train extends Transportation {
    private String trainType, seatClass;

    public Train() {
        super();
        this.trainType = "";
        this.seatClass = "";
    }

    public Train(String companyName, String departureCity, String arrivalCity, String trainType, String seatClass) {
        super(companyName, departureCity, arrivalCity);
        this.trainType = trainType;
        this.seatClass = seatClass;
    }

    public Train(Train other) {
        super(other);
        this.trainType = other.trainType;
        this.seatClass = other.seatClass;
    }

    public String getTrainType() {return this.trainType;}
    public void setTrainType(String trainType) {this.trainType = trainType;}

    public String getSeatClass() {return this.seatClass;}
    public void setSeatClass(String seatClass) {this.seatClass = seatClass;}

    @Override
    public String toString() {return super.toString() + ", " + this.trainType + ", " + this.seatClass;}

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || this.getClass() != other.getClass())
            return false;

        Train otherTrain = (Train) other;

        return super.equals(otherTrain)
            && this.trainType == otherTrain.trainType
            && this.seatClass == otherTrain.seatClass;
    }
}
