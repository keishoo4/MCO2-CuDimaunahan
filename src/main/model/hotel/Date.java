package model.hotel;

public class Date {
    private int day;
    private double datePrice;

    public Date(int day, double datePrice) {
        this.day = day;
        this.datePrice = datePrice;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public double getDatePrice() {
        return datePrice;
    }
}