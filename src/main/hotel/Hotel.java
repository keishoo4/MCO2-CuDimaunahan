package hotel;

import java.util.ArrayList;

/**
 * The Hotel class represents a hotel with rooms and 
 * provides various operations related to the hotel.
 */
public class Hotel {
    private String name;
    private ArrayList<Room> rooms;
    private double basePrice = 1299.0;

    /**
     * Constructs a Hotel object with the given name and number of rooms.
     * 
     * @param name    the name of the hotel
     * @param nRooms  the number of rooms in the hotel
     */
    public Hotel(String name, int nRooms) {
        this.name = name;
        this.rooms = new ArrayList<Room>();
        addRooms(nRooms);
    }

    /**
     * Returns the name of the hotel.
     * 
     * @return the name of the hotel
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the base price of the hotel.
     * 
     * @return the base price of the hotel
     */
    public double getPrice() {
        return basePrice;
    }

    /**
     * Returns the list of rooms in the hotel.
     * 
     * @return the list of rooms in the hotel
     */
    public ArrayList<Room> getRooms() {
        return rooms;
    }

    /**
     * Sets the name of the hotel.
     * 
     * @param name the new name of the hotel
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the new base price of the hotel.
     * 
     * @param basePrice the new base price of the hotel
     */
    public void setNewPrice(double basePrice) {
        this.basePrice = basePrice;
    }

    /**
     * Changes the price of all rooms in the hotel.
     * 
     * @param newPrice the new price per night for all rooms
     */
    public void changeRoomPrice(double newPrice) {
        for (Room room : rooms) {
            room.setPricePerNight(newPrice);
        }
    }

    /**
     * Adds the specified number of rooms to the hotel.
     * 
     * @param nRooms the number of rooms to add
     */
    public void addRooms(int nRooms) {
        for (int i = 0; i < nRooms; i++)
            this.rooms.add(new Room("RM" + (i + 1), basePrice));
    }

    /**
     * Returns the index of the latest room without a reservation.
     * 
     * @return the index of the latest room without a reservation, or -1 if all rooms are reserved
     */
    public int LatestRoomNoReservation() {
        for (Room room : rooms) {
            if (!room.getBookStatus())
                return rooms.indexOf(room);
        }
        return -1;
    }

    /**
     * Returns the number of rooms that can be removed (i.e., not reserved).
     * 
     * @return the number of rooms that can be removed
     */
    public int removableRooms() {
        int count = 0;
        for (Room room : rooms) {
            if (!room.getBookStatus())
                ++count;
        } 
        return count;
    }

    /**
     * Checks if there is a reservation with the given guest name in any of the rooms.
     * 
     * @param guestName the guest name to check
     * @return true if there is a reservation with the given guest name, false otherwise
     */
    public boolean sameGuestName(String guestName) {
        for (Room room : rooms) {
            if (room.getBookStatus() == true) {
                for (Reservation reservation : room.getReservations()) {
                    if (reservation.getGuestName().equals(guestName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if there is any room with a reservation.
     * 
     * @return true if there is any room with a reservation, false otherwise
     */
    public boolean reservationStatus() {
        for (Room room : getRooms()) {
            if (room.getBookStatus() == true)
                return true;
        }
        return false;
    }

    /**
     * Returns the total number of reservations in all rooms.
     * 
     * @return the total number of reservations in all rooms
     */
    public int getAllReservations() {
        int totalReservations = 0;
        for (Room room : rooms)
            totalReservations += room.getReservations().size();

        return totalReservations;
    }

    /**
     * Prints the list of rooms based on the availability.
     * 
     * @param availability the availability of the rooms to print ("All" or "Reserved")
     */
    public void showRooms(String availability) {
        if (availability.equals("All")) {
            System.out.println("All Rooms: ");

            int count = 0;
            for (Room room : rooms) {
                System.out.print("[" + (++count) + "] " + room.getName());
                room.printReservations("none");
            }
        }

        if (availability.equals("Reserved")) {
            System.out.println("Reserved Rooms: ");

            for (Room room : rooms) {
                if (room.getBookStatus()) {
                    System.out.print(" - " + room.getName() + "\n");
                }
            }
        }
    }
}