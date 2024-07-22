package model.hotel;

import java.util.ArrayList;

import utils.ScannerUtil;

/**
 * The HotelList class represents a list of hotels.
 */
public class HotelList {
    private ArrayList<Hotel> hotels;
    
    /**
     * Constructs an empty HotelList.
     */
    public HotelList() {
        this.hotels = new ArrayList<Hotel>();
    }

    /**
     * Returns the list of hotels.
     *
     * @return the list of hotels
     */
    public ArrayList<Hotel> getHotels() {
        return hotels;
    }

    /**
     * Adds a new hotel to the hotel list.
     *
     * @param name    the name of the hotel
     * @param rooms  the number of rooms in the hotel
     * @param deluxeRooms the number of deluxe rooms in the hotel
     * @param execRooms the number of executive rooms in the hotel
     */
    public void addHotel(String name, int rooms, int deluxeRooms, int execRooms) {
        Hotel newHotel = new Hotel(name, rooms, deluxeRooms, execRooms);
        hotels.add(newHotel);
    }

    /**
     * Checks if a hotel with the given name already exists in the hotel list.
     *
     * @param hotelName  the name of the hotel to check
     * @return true if a hotel with the given name exists, false otherwise
     */
    public boolean sameHotelName(String hotelName) {
        for (Hotel hotel : hotels) {
            if (hotel.getName().equals(hotelName))
                return true;
        }
        return false;
    }

    /**
     * Displays all the hotels in the hotel list.
     * If the hotel list is empty, it prints "No hotels available".
     */
    public void displayAllHotels() {
        if (getHotels().isEmpty()) {
            System.out.println("No hotels available");
            return;
        }

        int count = 0;
        System.out.println("[HOTELS]");
        for (Hotel hotel : getHotels())
            System.out.println("[" + (++count) + "] " + hotel.getName());
    }

    /**
     * Adds a new hotel to the hotel list.
     *
     * @param hotelList the HotelList object to add the hotel to
     */
    public void addHotels(HotelList hotelList) {
        String hotelName;
        int baseRooms, deluxeRooms, execRooms, totalRooms, 
            maxDeluxe, maxExec, remainingRooms;
        
        System.out.print("Enter hotel name: ");
        hotelName = ScannerUtil.readString();
    
        if (hotelList.sameHotelName(hotelName)) {
            System.out.println("Hotel with this name already exists!");
            return;
        }
    
        System.out.print("Enter number of base rooms (1-50): ");
        baseRooms = ScannerUtil.readInt();
    
        if (baseRooms < 1 || baseRooms > 50) {
            System.out.println("Invalid number of base rooms! Must be between 1 and 50.");
            return;
        }
    
        maxDeluxe = (int) Math.floor(baseRooms * 0.6); // 3/5 ratio
        maxExec = (int) Math.floor(baseRooms * 0.4);   // 2/5 ratio
    
        System.out.print("Enter number of deluxe rooms (0-" + maxDeluxe + "): ");
        deluxeRooms = ScannerUtil.readInt();
    
        if (deluxeRooms < 0 || deluxeRooms > maxDeluxe) {
            System.out.println("Invalid number of deluxe rooms!");
            return;
        }
    
        remainingRooms = 50 - baseRooms - deluxeRooms;
        maxExec = Math.min(maxExec, remainingRooms);
    
        System.out.print("Enter number of executive rooms (0-" + maxExec + "): ");
        execRooms = ScannerUtil.readInt();
    
        if (execRooms < 0 || execRooms > maxExec) {
            System.out.println("Invalid number of executive rooms!");
            return;
        }
    
        totalRooms = baseRooms + deluxeRooms + execRooms;
    
        if (totalRooms > 50) {
            System.out.println("Total number of rooms exceeds the maximum of 50!");
            return;
        }

        hotelList.addHotel(hotelName, baseRooms, deluxeRooms, execRooms);
        System.out.println("Hotel " + hotelName + " (" + totalRooms + " rooms) created successfully!");
        // Summary of each in println
        System.out.println("     Base Rooms - [" + baseRooms + "]");
        System.out.println("   Deluxe Rooms - [" + deluxeRooms + "]");
        System.out.println("Executive Rooms - [" + execRooms + "]\n");
    }

    /**
     * Removes the hotel from the hotel list.
     *
     * @param hotel the Hotel object to remove
     */
    public void removeHotel(Hotel hotel) {
        if (hotel.reservationStatus()) {
            System.out.println("Cannot remove hotel with reservation(s).\n");
            return;
        }

        System.out.println("Confirm removal of \"" + hotel.getName() + "\" from the hotel list");
        System.out.print("Confirm [Yes/No]: ");
        boolean confirmed = ScannerUtil.readBoolean();

        if (!confirmed) {
            System.out.println("\nHotel removal cancelled.\n");
            return;
        }

        hotels.remove(hotel);
        System.out.println("Hotel \"" + hotel.getName() + "\" removed\n");
    }
}