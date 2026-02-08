package AssignmentsS2.Assignment1.src.travel;

public class Hotel extends Accomodation {
    private double startRating;
    
    public Hotel() {
        super();
        this.startRating = 0.0;
    }

    public Hotel(String name, String location, double pricePerNight, double startRating) {
        super(name, location, pricePerNight);
        this.startRating = startRating;
    }

    public Hotel(Hotel other) {
        super(other);
        this.startRating = other.startRating;
    }

    public double getStartRating() {return this.startRating;}
    public void setStartRating(double startRating) {this.startRating = startRating;}

    @Override
    public String toString() {return super.toString() + ", " + this.startRating;}

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || this.getClass() != other.getClass())
            return false;

        Hotel otherHotel = (Hotel) other;

        return super.equals(otherHotel)
            && this.startRating == otherHotel.startRating;
    }

    @Override
    public double calculateCost(int numOfDays) {
        double costPerNight = this.getPricePerNight();
        return costPerNight * numOfDays;
    }
}
