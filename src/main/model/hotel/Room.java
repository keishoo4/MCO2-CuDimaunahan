package model.hotel;

import java.util.ArrayList;

/**
 * The Room class represents a room in a hotel.
 */
public class Room {
    private String name;
    private double pricePerNight;
    private ArrayList<Reservation> reservations;
    private boolean isBooked = false;
    private ArrayList<Date> dates = new ArrayList<Date>();

    /**
     * Constructs a Room object with the specified name and price per night.
     *
     * @param name           the name of the room
     * @param pricePerNight  the price per night of the room
     */
    public Room(String name, double pricePerNight) {
        this.name = name;
        this.pricePerNight = pricePerNight;
        this.reservations = new ArrayList<Reservation>();
    }

    /**
     * Returns the name of the room.
     *
     * @return the name of the room
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the price per night of the room.
     *
     * @return the price per night of the room
     */
    public double getPricePerNight() {
        return pricePerNight;
    }

    /**
     * Returns the booking status of the room.
     *
     * @return true if the room is booked, false otherwise
     */
    public boolean getBookStatus() {
        return isBooked;
    }

    /**
     * Returns the list of reservations for the room.
     *
     * @return the list of reservations for the room
     */
    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    /**
     * Prints the reservations for the room.
     *
     * @param status the status of the reservations to be printed ("View" or "none")
     */
    public void printReservations(String status) {
        int count = 0;

        for (Reservation reservation : reservations) {
            if (status.equals("View"))
                System.out.print("[" + (++count) + "] Reservation " + count);

            System.out.print(" | 1-");

            if (reservation.getCheckInDate() < 10)
                System.out.print("0" + reservation.getCheckInDate() + " to 1-");
            else
                System.out.print(reservation.getCheckInDate() + " to 1-");

            if (reservation.getCheckOutDate() < 10)
                System.out.print("0" + reservation.getCheckOutDate());
            else
                System.out.print(reservation.getCheckOutDate());

            if (status.equals("none")) {
                if (reservations.size() == 1)
                    System.out.print(" |");
            }

            if (status.equals("View"))
                System.out.println();
        }

        if (reservations.size() != 1)
            System.out.print(" |");

        System.out.println();
    }

    /**
     * Checks if a reservation with the specified check-in and check-out dates can be made for the room.
     *
     * @param newCheckIn   the check-in date of the new reservation
     * @param newCheckOut  the check-out date of the new reservation
     * @return true if the reservation is valid, false otherwise
     */
    public boolean isReservationValid(int newCheckIn, int newCheckOut) {
        int allReservations = reservations.size();
        int i;

        for (i = 0; i < allReservations; i++) {
            if (newCheckIn > reservations.get(allReservations - 1).getCheckInDate()) {
                if (newCheckIn >= reservations.get(allReservations - 1).getCheckOutDate()) {
                    return true;
                } else
                    return false;
            }
            --allReservations;
        }
        return false;
    }

    /**
     * Sets the name of the room.
     *
     * @param name the name of the room
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the booking status of the room.
     *
     * @param isBooked true if the room is booked, false otherwise
     */
    public void setBookStatus(boolean isBooked) {
        this.isBooked = isBooked;
    }

    /**
     * Sets the price per night of the room.
     *
     * @param pricePerNight the price per night of the room
     */
    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    /**
     * Displays the availability of a room for each day within a specified range.
     *
     * @param maxDay the maximum day in the range
     * @param room the room for which to display the reservations
     */
    public void showReservations(int maxDay, Room room) {
        for (int d = 1; d <= maxDay; d++) {
            final int day = d; // use a final var because of lambda expression, needs to be constant
            boolean isBooked = room.getReservations().stream()
                    .anyMatch(reservation -> Hotel.isDateInRange(day, reservation.getCheckInDate(), reservation.getCheckOutDate()));
            System.out.println("1-" + (day < 10 ? "0" + day : day) + ": " + (isBooked ? "Booked" : "Available"));
        }        
    }

    public ArrayList<Date> getDates() {
        return dates;
    }

    public void fillDates(double pricePerNight, int checkInDate, int checkOutDate, String discountCode) {
        for (int i=1; i<=31; i++) {
            if (i >= checkInDate && i <= checkOutDate)
                dates.add(new Date(i, pricePerNight * getDiscountCode(discountCode)));
            else
                dates.add(new Date(i, 0));
        }
    }

    public double getDiscountCode(String discountCode) {
        switch (discountCode) {
            case "I_WORK_HERE":
                return 0.9; // 10% discount
            case "STAY4_GET1":
                return 1.0; // No discount multiplier here; handled differently in applyDiscount
            case "PAYDAY":
                return 0.93; // 7% discount
            default:
                return 1.0; // No discount
        }
    }

    /**
     * Adds a new reservation to the given list of reservations and updates the book status of the specified room.
     *
     * @param hotel The hotel object.
     * @param roomToUse The index of the room to be booked.
     * @param reservations The list of reservations.
     * @param guestName The name of the guest making the reservation.
     * @param checkInDate The check-in date of the reservation.
     * @param checkOutDate The check-out date of the reservation.
     */
    public void bookInputInfo(Room room, ArrayList<Reservation> reservations, 
                              String guestName, int checkInDate, int checkOutDate) {
        reservations.add(new Reservation(guestName, checkInDate, checkOutDate, room));
        room.setBookStatus(true); // Most important part
    }
}
