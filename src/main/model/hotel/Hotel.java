package model.hotel;

import java.util.ArrayList;

import utils.ScannerUtil;

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

        this.datePriceModifiers = new double[31];  // Initialize the array with 31 days
        for (int i = 0; i < datePriceModifiers.length; i++) {
            datePriceModifiers[i] = 1.0; // No modification by default
        }
    }

    /**
     * Returns the name of the hotel.
     * 
     * @return the name of the hotel
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " - " + rooms.size() + " rooms";
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
    public void setNewPrice(double pricePerNight) {
        this.basePrice = pricePerNight;
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
     * Prompts the user to enter a check-in date and validates the input.
     * 
     * @param sc the Scanner object used to read user input
     * @return the valid check-in date entered by the user
     */
    public int checkIn() {
        System.out.print(" Enter check-in date: 1-");
        int checkInDate = ScannerUtil.readInt();

        if (checkInDate >= 0 && checkInDate <= 30)
            return checkInDate;
        else {
            System.out.println("Invalid date format. Please try again.\n");
            return checkIn();
        }
    }

    /**
     * Prompts the user to enter a check-out date and validates the input.
     * 
     * @param sc the Scanner object used for user input
     * @param checkInDay the check-in day to compare the check-out date with
     * @return the valid check-out date if it is after the check-in date, otherwise 0
     */
    public int checkOut(int checkInDay) {
        System.out.print("Enter check-out date: 1-");
        int checkOutDate = ScannerUtil.readInt();
        System.out.println();

        if (checkOutDate >= 0 && checkOutDate <= 31) {
            if (checkOutDate > checkInDay)
                return checkOutDate;
            else {
                System.out.println("Check-out date must be after check-in date. Please try again.");
                return 0;
            }
        } 
        else {
            System.out.println("Invalid date! Please try again.");
            return checkOut(checkInDay);
        }
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

    /**
     * Changes the name of the hotel.
     *
     * @param hotelList   the HotelList object containing the list of hotels
     */
    public void changeHotelName(HotelList hotelList) {
        System.out.print("Enter new hotel name: ");
        String newHotelName = ScannerUtil.readString();

        if (hotelList.sameHotelName(newHotelName)) {
            System.out.println("Hotel with this name already exists");
            return;
        }

        System.out.println("Confirm change of hotel name from \"" + this.name +
                "\" to \"" + newHotelName + "\"");
        System.out.print("Confirm [Yes/No]: ");
        boolean confirmed = ScannerUtil.readBoolean();

        if (!confirmed) {
            System.out.println("\nHotel name change cancelled.\n");
            return;
        }

        this.name = newHotelName;
        System.out.println("Hotel name changed to \"" + newHotelName + "\"");
    }

    /**
     * Adds rooms to the hotel.
     *
     * @param hotel the Hotel object to modify
     */
    public void addRoomsPrompt(Hotel hotel) {
        System.out.print("Enter number of rooms to add: ");
        int numRooms = ScannerUtil.readInt();

        if (numRooms < 1) {
            System.out.println("Invalid number of rooms!\n");
            return;
        } 

        if (numRooms + hotel.getRooms().size() > 50) {
            System.out.println("Cannot exceed 50 rooms!\n");
            return;
        }

        System.out.println("Confirm adding " + numRooms + 
                           " room(s) to \"" + hotel.getName() + "\"");
        System.out.print("Confirm [Yes/No]: ");
        boolean confirmed = ScannerUtil.readBoolean();

        if (!confirmed) {
            System.out.println("\nRoom addition cancelled.\n");
            return;
        }

        hotel.addRooms(numRooms);
        System.out.println("Total Room(s) is " + hotel.getRooms().size() + ".\n");
    }

    /**
     * Calculates the total number of rooms reserved in the hotel.
     *
     * @param hotel the Hotel object to calculate the total reserved rooms
     * @return the total number of reserved rooms
     */
    public int totalRoomsReserved(Hotel hotel) {
        int totalRooms = 0;
        for (Room room : hotel.getRooms()) {
            if (room.getBookStatus())
                totalRooms++;
        }
        return totalRooms;
    }

    /**
     * Removes rooms from the hotel.
     *
     * @param hotel the Hotel object to modify
     */
    public void removeRooms(Hotel hotel) {
        int lastRoom = rooms.size();

        if (lastRoom == 1) {
            System.out.println("A hotel cannot have zero rooms.\n");
            return;
        }

        int removableRooms = removableRooms();
        if (removableRooms == 0) {
            System.out.println("All rooms have reservations.\nCannot remove any rooms.\n");
            return;
        }

        System.out.println("Available Rooms for Removal:     " + removableRooms);
        System.out.print("Enter number of rooms to remove: ");
        int numRooms = ScannerUtil.readInt();
        System.out.println();

        if (numRooms < 1) {
            System.out.println("Invalid number of rooms!\n");
            return;
        }

        if (numRooms > (lastRoom - totalRoomsReserved(this))) {
            System.out.println("Picked room numbers for removal exceed current unoccupied rooms.\n");
            return;
        }

        System.out.println("Confirm removing " + numRooms + " room(s) from \"" + name + "\"");
        System.out.print("Confirm [Yes/No]: ");
        boolean confirmed = ScannerUtil.readBoolean();

        if (!confirmed) {
            System.out.println("\nRoom removal cancelled.\n");
            return;
        }

        int i = 0;
        while (numRooms > 0) {
            if (!rooms.get(lastRoom - i - 1).getBookStatus()) {
                rooms.remove(lastRoom - i - 1);
                --numRooms;
            }
            ++i;
        }

        System.out.println("Shifting Room Numbers...");
        for (i = 0; i < rooms.size(); i++) {
            rooms.get(i).setName("RM" + (i + 1));
        }

        System.out.println("Removal Success!\n");
    }

    /**
     * Updates the price of the hotel room.
     *
     * @param hotel the Hotel object to modify
     */
    public void updateRoomPrice(Hotel hotel) {
        if (hotel.reservationStatus() == true) {
            System.out.println("Cannot update price with reservation(s).\n");
            return;
        }

        System.out.print("Enter the new room price: ");
        double newPrice = ScannerUtil.readDouble();

        System.out.println("Confirm change of room price to " + newPrice);
        System.out.print("Confirm [Yes/No]: ");
        boolean confirmed = ScannerUtil.readBoolean();

        if (!confirmed) {
            System.out.println("\nRoom price change cancelled.\n");
            return;
        }

        hotel.setNewPrice(newPrice); // Update the base price
        hotel.changeRoomPrice(newPrice); // Update the price of all rooms
        System.out.println("Room price updated to " + newPrice + ".\n");
    }

    /**
     * Removes reservations from the hotel room.
     *
     * @param hotel the Hotel object to modify
     * @param rooms the list of rooms in the hotel
     */
    public void removeReservations(Hotel hotel, ArrayList<Room> rooms) {
        hotel.showRooms("Reserved");
        System.out.println("[0] Back to Main Menu");
        System.out.print("Input Room Name: ");
        String roomTracker = ScannerUtil.readString();
        System.out.println();

        if (roomTracker.equals("0"))
            return;

        int roomNum = Integer.parseInt(roomTracker.substring(2)) - 1;

        if (roomNum >= rooms.size()) {
            System.out.println("Invalid room number!\n");
            return;
        }

        rooms.get(roomNum).printReservations("View");
        System.out.println("[0] Back to Main Menu");
        System.out.print("Pick reservation(s) to remove: ");
        int resNum = ScannerUtil.readInt();

        if (resNum == 0)
            return;

        System.out.println("\nConfirm removal of reservation for \"" + hotel.getName() + "\"");
        System.out.print("Confirm [Yes/No]: ");
        boolean confirmed = ScannerUtil.readBoolean();

        if (!confirmed) {
            System.out.println("\nReservation removal cancelled.\n");
            return;
        }        

        rooms.get(roomNum).getReservations().remove(resNum - 1);
        if (rooms.get(roomNum).getReservations().isEmpty())
            rooms.get(roomNum).setBookStatus(false); // Set room to unoccupied (false)

        System.out.println("Reservation " + resNum + " of " +
                rooms.get(roomNum).getName() + " removed!\n");
    }

    /**
     * Displays the high-level information of a hotel, including the hotel name, total number of rooms,
     * and estimated earnings for the month.
     *
     * @param hotel The hotel object for which to display the information.
     */
    public void viewHighLevelInfo(Hotel hotel) {
        System.out.println("Hotel Name: " + hotel.getName());
        System.out.println("Total Number of Rooms: " + hotel.getRooms().size());

        double estimatedEarnings = hotel.getRooms().stream()
                .flatMap(room -> room.getReservations().stream())
                .mapToDouble(reservation -> {
                    int checkInDate = reservation.getCheckInDate();
                    int checkOutDate = reservation.getCheckOutDate();

                    int diffInDays = checkOutDate - checkInDate;
                    return (diffInDays) * reservation.getRoom().getPricePerNight(); // including the check-in day
                })
                .sum();

        System.out.println("Estimated Earnings for the Month: " + estimatedEarnings);
    }

    /**
     * Checks if a given date is within a specified range.
     *
     * @param date      the date to check
     * @param startDate the start date of the range (inclusive)
     * @param endDate   the end date of the range (inclusive)
     * @return true if the date is within the range, false otherwise
     */
    public static boolean isDateInRange(int date, int startDate, int endDate) {
        return date >= startDate && date <= endDate;
    }

    /**
     * Displays the total number of available and booked rooms in the hotel based on the given date.
     *
     * @param hotel The hotel object containing the list of rooms.
     */
    public void viewAvailableAndBookedRooms(Hotel hotel) {
        System.out.print("Enter the day of reservation (eg. 23): ");
        int date = ScannerUtil.readInt();

        if (date < 1 || date > 31) {
            System.out.println("Invalid day input. Day must be between 1 and 31.");
            return;
        }

        long bookedRoomsCount = hotel.getRooms().stream()
                .flatMap(room -> room.getReservations().stream())
                .filter(reservation -> isDateInRange(date, reservation.getCheckInDate(), reservation.getCheckOutDate()))
                .count();

        long availableRoomsCount = hotel.getRooms().size() - bookedRoomsCount;

        System.out.println("Total Number of Available Rooms: " + availableRoomsCount);
        System.out.println("Total Number of Booked Rooms: " + bookedRoomsCount);
    }

    /**
     * Displays information about a specific room in the hotel.
     *
     * @param hotel the hotel object containing the rooms
     */
    public void viewRoomInfo(Hotel hotel) {
        System.out.print("Enter room name: ");
        String roomName = ScannerUtil.readString();
        Room room = hotel.getRooms().stream()
                .filter(r -> r.getName().equals(roomName))
                .findFirst()
                .orElse(null);

        if (room == null) {
            System.out.println("Room not found.");
            return;
        }

        System.out.println("Room Name: " + room.getName());
        System.out.println("Price per Night: " + room.getPricePerNight());
        System.out.println("Availability for the Month:");
        int maxDay = 31; // Assuming a fixed 31-day month for "1-DD" format
        room.showReservations(maxDay, room);
    }

    private double[] datePriceModifiers; // Array for storing the price modifiers 

    public void promptDatePriceModifier(Hotel hotel) {
        if (hotel.reservationStatus() == true) {
            System.out.println("Cannot modify price with reservation(s).\n");
            return;
        }
        
        System.out.print("Enter the day of the month (1-31): ");
        int day = ScannerUtil.readInt();
        if (day < 1 || day > 31) {
            System.out.println("Invalid day. Please enter a value between 1 and 31.");
            return;
        }
        
        System.out.print("Enter the price modifier for day " + day + " in percentage.");
        double priceModifier = ScannerUtil.readDouble();
        double priceModifierPrice = priceModifier/100;
        
        if (priceModifier <= 0 || priceModifier > 150 || priceModifier < 50) {
            System.out.println("Invalid price modifier.");
            return;
        }

        setDatePriceModifier(day, priceModifierPrice);
        System.out.println("Price modifier for day " + day + " updated to " + priceModifier + ".");
    }

    public double calculateFinalPrice(int checkInDay, int checkOutDay) {
        double finalPrice = 0.0;
        for (int day = checkInDay; day < checkOutDay; day++) { // loops through each day of the reservation 
            finalPrice += basePrice * datePriceModifiers[day - 1]; 
        }

        return finalPrice;
    }

    public void setDatePriceModifier(int day, double priceModifier) {
        if (day < 1 || day > 31) {
            System.out.println("Invalid day! Please enter a day between 1 and 31.");
            return;
        }
        datePriceModifiers[day - 1] = priceModifier; // sets day in the array to its price modifier 
    }

    /**
     * Displays the reservation information for a given guest name.
     * 
     * @param hotel the Hotel object containing the rooms and reservations
     */
    public void viewReservationInfo(Hotel hotel) {
        Reservation reservation;
        
        System.out.print("Enter guest name: ");
        String guestName = ScannerUtil.readString();

        reservation = hotel.getRooms().stream()
                      .flatMap(room -> room.getReservations().stream())
                      .filter(r -> r.getGuestName().equals(guestName))
                      .findFirst()
                      .orElse(null);

        if (reservation == null) {
            System.out.println("Reservation not found.");
            return;
        }

        int checkInDate = reservation.getCheckInDate();
        int checkOutDate = reservation.getCheckOutDate();

        // double initialPrice = hotel.calculateFinalPrice(checkInDate, checkOutDate);
        // double finalPrice = applyDiscount(initialPrice, checkInDate, checkOutDate);

        System.out.println("Guest Name: " + reservation.getGuestName());
        System.out.println("Room Name: " + reservation.getRoom().getName());
        System.out.println("Check-in Date: 1-" + (checkInDate < 10 ? "0" + checkInDate : checkInDate));
        System.out.println("Check-out Date: 1-" + (checkOutDate < 10 ? "0" + checkOutDate : checkOutDate));
        System.out.println("Total Price: " + String.format("%.2f", finalPrice)); // NOT UPDATED TO CONSIDER DISCOUNT CODES, TO DO SOON
        System.out.println("Price Breakdown per Night: " + reservation.getRoom().getPricePerNight());
    }
}