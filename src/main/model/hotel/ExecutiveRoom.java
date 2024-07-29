package model.hotel;

/**
 * The ExecutiveRoom class represents an executive room in a hotel, which has a 35% price increase compared to a regular room.
 */
public class ExecutiveRoom extends Room {
    private static final double PRICE_MULTIPLIER = 1.35;

    /**
     * Constructs an ExecutiveRoom object with the specified name and base price per night.
     * The price per night of an executive room is increased by 35% compared to a regular room.
     *
     * @param name          the name of the room
     * @param basePricePerNight the base price per night of the room before applying the increase
     */
    public ExecutiveRoom(String name, double basePricePerNight) {
        super(name, basePricePerNight * PRICE_MULTIPLIER);
    }

    @Override
    public String toString() {
        return super.toString(); // Assuming DeluxeRoom uses the same name property
    }
}