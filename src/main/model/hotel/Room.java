package model.hotel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * The Room class represents a room in a hotel.
 */
public class Room {
    private String name;
    private double pricePerNight;
    private ArrayList<Reservation> reservations;
    private boolean isBooked = false;

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
        for (Reservation reservation : reservations) {
            // Check if new reservation overlaps with any existing reservation
            if (newCheckIn < reservation.getCheckOutDate() && newCheckOut > reservation.getCheckInDate()) {
                return false; // Overlapping reservation
            }
        }
        return true; // No overlap
    }

    public boolean isFullyBooked() {
        if (reservations.isEmpty()) {
            return false;
        }

        // Sort reservations by check-in date
        ArrayList<Reservation> sortedReservations = new ArrayList<>(reservations);
        Collections.sort(sortedReservations, Comparator.comparingInt(Reservation::getCheckInDate));

        int lastCheckout = 0;

        // Check the beginning of the month
        for (Reservation reservation : sortedReservations) {
            if (reservation.getCheckInDate() - lastCheckout >= 3) {
                return false;
            }
            lastCheckout = Math.max(lastCheckout, reservation.getCheckOutDate());
            if (lastCheckout >= 3) {
                break;  // We've covered the first 3 days, move on to the main loop
            }
        }

        // Continue checking the rest of the month
        for (Reservation reservation : sortedReservations) {
            int currentCheckin = reservation.getCheckInDate();
            if (currentCheckin - lastCheckout >= 3) {
                return false;
            }
            lastCheckout = Math.max(lastCheckout, reservation.getCheckOutDate());
        }

        // Check the gap after the last reservation
        return lastCheckout >= 29;  // If the last checkout is before day 29, there's a bookable gap at the end
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

    @Override
    public String toString() {
        return " " + name;
    }
}