package model.hotel;

/**
 * The DeluxeRoom class represents a deluxe room in a hotel, which has a base price 20% higher than a regular room.
 */
public class DeluxeRoom extends Room {
    private static final double PRICE_MULTIPLIER = 1.2;

    /**
     * Constructs a DeluxeRoom object with the specified name and price per night.
     * The price per night is 20% higher than the base price provided.
     *
     * @param name          the name of the deluxe room
     * @param basePricePerNight the base price per night of the room before the 20% increase
     */
    public DeluxeRoom(String name, double basePricePerNight) {
        super(name, basePricePerNight * PRICE_MULTIPLIER);
    }

    @Override
    public void setPricePerNight(double pricePerNight) {
        super.setPricePerNight(pricePerNight * PRICE_MULTIPLIER);
    }

    /**
     * Overrides the toString method to provide a string representation of the DeluxeRoom object.
     * The string representation includes the room's name and price per night.
     *
     * @return a string representation of the DeluxeRoom object in the format: "DeluxeRoom{name=<roomName>, pricePerNight=<pricePerNight>}"
     */
    @Override
    public String toString() {
        return super.toString();
    }
}