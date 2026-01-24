package AssignmentsS2.Assignment0;

public class Show {

    private String title;
    private String genre;
    private int year;
    private double rating;
    private static int counter = 0;

    public Show() {
        this.title = "";
        this.genre = "";
        this.year = 0;
        this.rating = 0.0;
        counter++;
    }
    //----------------------------------
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return this.year;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getRating() {
        return this.rating;
    }
    //----------------------------------

    public static int findNumberOfCreatedShows() {
        if (counter == 0)
            return 0;
        return counter;
    }

    @Override
    public boolean equals(Object other) {
        Show diffShow = (Show) other;
        if ((this.year == diffShow.year) && (this.title == diffShow.title))
            return true;

        return false;
    }

    @Override
    public String toString() {
        return "Show # " + counter +
                "\nTitle : " + this.title +
                "\nGenre : " + this.genre +
                "\nYear : " + this.year +
                "\nRating : " + this.rating;
    }
}