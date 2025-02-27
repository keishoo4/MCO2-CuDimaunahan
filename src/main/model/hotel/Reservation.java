package model.hotel;

/**
 * The Reservation class represents a reservation made by a guest.
 */
public class Reservation {
    private String guestName;
    private int checkInDate;
    private int checkOutDate;
    private Room room;
    private String discountCode;

    /**
     * Constructs a Reservation object with the specified guest name, check-in date, check-out date, and room.
     * The total price is automatically calculated based on the room's price per night.
     *
     * @param guestName     the name of the guest making the reservation
     * @param checkInDate   the check-in date of the reservation
     * @param checkOutDate  the check-out date of the reservation
     * @param room          the room reserved for the guest
     * @param discountCode  the discount code used for the reservation
     */
    public Reservation(String guestName, int checkInDate, int checkOutDate, Room room, String discountCode) {
        this.guestName = guestName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.room = room;
        this.discountCode = discountCode;
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
     * Sets the name of the guest making the reservation.
     *
     * @param guestName the guest name to set
     */
    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    /**
     * Returns the discount code used for the reservation.
     *
     * @return the discount code applied to the reservation. If no discount code is used, this method will return null.
     */
    public String getDiscountCode() {
        return discountCode;
    }

    /**
     * Sets the discount code used for the reservation.
     *
     * @param discountCode the discount code to set. If no discount code is used, this parameter should be set to null.
     *                     The discount code should be a valid one, otherwise, it will not be applied to the reservation.
     */
    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }    
}