package model.hotel;

public class Date {
    private int day;
    private double datePrice;

    public Date(int day, double datePrice) {
        this.day = day;
        this.datePrice = datePrice;
    }

    /**
     * Returns the day of the date.
     *
     * @return the day of the date. The value is an integer representing the day of the month.
     */
    public int getDay() {
        return day;
    }
    /**
     * Sets the day of the date.
     *
     * @param day the new day of the date. The value is an integer representing the day of the month.
     *
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Returns the price of the date.
     *
     * @return the price of the date. The value is a double representing the price of the date.
     */
    public double getDatePrice() {
        return datePrice;
    }
}