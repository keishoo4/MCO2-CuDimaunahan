package model.hotel;

/**
 * The Reservation class represents a reservation made by a guest.
 */
public class Reservation {
    private String guestName;
    private int checkInDate;
    private int checkOutDate;
    private Room room;
    private double totalPrice;

    /**
     * Constructs a Reservation object with the specified guest name, check-in date, check-out date, and room.
     * The total price is automatically calculated based on the room's price per night.
     *
     * @param guestName     the name of the guest making the reservation
     * @param checkInDate   the check-in date of the reservation
     * @param checkOutDate  the check-out date of the reservation
     * @param room          the room reserved for the guest
     */
    public Reservation(String guestName, int checkInDate, int checkOutDate, Room room) {
        this.guestName = guestName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.room = room;
        this.totalPrice = calculateTotalPrice();
    }

    /**
     * Returns the name of the guest making the reservation.
     *
     * @return the guest name
     */
    public String getGuestName() {
        return guestName;
    }

    /**
     * Returns the check-in date of the reservation.
     *
     * @return the check-in date
     */
    public int getCheckInDate() {
        return checkInDate;
    }

    /**
     * Returns the check-out date of the reservation.
     *
     * @return the check-out date
     */
    public int getCheckOutDate() {
        return checkOutDate;
    }

    /**
     * Returns the room reserved for the guest.
     *
     * @return the reserved room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Returns the total price of the reservation.
     *
     * @return the total price
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Sets the name of the guest making the reservation.
     *
     * @param guestName the guest name to set
     */
    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    /**
     * Calculates the total price of the reservation based on the room's price per night.
     *
     * @return the total price of the reservation
     */
    private double calculateTotalPrice() {
        return room.getPricePerNight();
    }
}