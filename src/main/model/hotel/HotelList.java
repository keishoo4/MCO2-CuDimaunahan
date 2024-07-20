package model.hotel;

import java.util.ArrayList;
import java.util.Scanner;
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
     * @param nRooms  the number of rooms in the hotel
     */
    public void addHotel(String name, int nRooms) {
        Hotel newHotel = new Hotel(name, nRooms);
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
     * @param sc       the Scanner object for user input
     * @param hotelList the HotelList object to add the hotel to
     */
    public void addHotels(Scanner sc, HotelList hotelList) {
        System.out.print("Enter hotel name: ");
        sc.nextLine(); // Consume the newline left over from previous input
        String hotelName = sc.nextLine();

        if (hotelList.sameHotelName(hotelName)) {
            System.out.println("Hotel with this name already exists!");
            return;
        }

        System.out.print("Enter number of rooms (1-50): ");
        int roomCount = sc.nextInt();

        if (roomCount < 1 || roomCount > 50) {
            System.out.println("Invalid number of rooms! Must be between 1 and 50.");
            return;
        }

        hotelList.addHotel(hotelName, roomCount);
        System.out.println("\nHotel \"" + hotelName + "\" created successfully!\n");
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