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
    private ArrayList<DeluxeRoom> deluxeRooms;
    private ArrayList<ExecutiveRoom> execRooms;
    private double basePrice = 1299.0;
    private ArrayList<Date> dates;
    private double[] datePriceModifiers; // Array for storing the price modifiers 

    /**
     * Constructs a Hotel object with the given name and number of rooms.
     * 
     * @param name    the name of the hotel
     * @param rooms  the number of rooms in the hotel
     * @param deluxeRooms the number of deluxe rooms in the hotel
     * @param execRooms the number of suite rooms in the hotel
     */
    public Hotel(String name, int rooms, int deluxeRooms, int execRooms) {
        this.name = name;
        this.rooms = new ArrayList<Room>();
        this.deluxeRooms = new ArrayList<DeluxeRoom>();
        this.execRooms = new ArrayList<ExecutiveRoom>();
        this.dates = new ArrayList<Date>();
        this.datePriceModifiers = new double[31];
        addRooms(rooms, deluxeRooms, execRooms);
    
        for (int i = 0; i < this.datePriceModifiers.length; i++) {
            this.datePriceModifiers[i] = 1.0;
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
        return " " + name /*+ " - " + 
               (rooms.size() + deluxeRooms.size() + execRooms.size())  
               + " rooms"*/;
    }

    /**
     * Returns the base price of the hotel.
     * 
     * @return the base price of the hotel
     */
    public double getRoomPrice() {
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
     * Returns the list of deluxe rooms in the hotel.
     * 
     * @return the list of deluxe rooms in the hotel
     */
    public ArrayList<DeluxeRoom> getDeluxeRooms() {
        return deluxeRooms;
    }

    /**
     * Returns the list of executive rooms in the hotel.
     * 
     * @return the list of executive rooms in the hotel
     */
    public ArrayList<ExecutiveRoom> getExecRooms() {
        return execRooms;
    }

    public ArrayList<Date> getDates() {
        return dates;
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
        for (DeluxeRoom room : deluxeRooms) {
            room.setPricePerNight(newPrice);
        }
        for (ExecutiveRoom room : execRooms) {
            room.setPricePerNight(newPrice);
        }
    }

    /**
     * Allows the user to choose another room from the available options.
     * 
     * @param sc the Scanner object used for user input
     * @param hotel the Hotel object representing the hotel where the room is located
     * @return the room number chosen by the user
     */
    public int chooseAnotherRoom() {
        System.out.println("Choose a different date \nor go to a different hotel.\n");
        showRooms("All");

        System.out.println("[0] Back to Hotel Selection");
        System.out.print("Enter room number: ");
        int roomNum = ScannerUtil.readInt();
        System.out.println();
        return roomNum;
    }

    /**
     * Adds the specified number of rooms to the hotel.
     * 
     * @param rooms the number of rooms to add
     * @param deluxeRooms the number of deluxe rooms to add
     * @param execRooms the number of suite rooms to add
     */
    public void addRooms(int rooms, int deluxeRooms, int execRooms) {
        int i;
        for (i=0; i<rooms; i++)
            this.rooms.add(new Room("RM" + (i + 1), basePrice));

        for (i=rooms; i<rooms+deluxeRooms; i++)
            this.deluxeRooms.add(new DeluxeRoom("RM" + (i + 1) + "-DL", basePrice));

        for (i=rooms+deluxeRooms; i<rooms+deluxeRooms+execRooms; i++)
            this.execRooms.add(new ExecutiveRoom("RM" + (i + 1) + "-EX", basePrice));
    }

    /**
     * Returns the index of the latest room without a reservation.
     * 
     * @return the index of the latest room without a reservation, or -1 if all rooms are reserved
     */
    public int latestRoomNoReservation() {
        for (Room room : rooms) {
            if (!room.getBookStatus())
                return rooms.indexOf(room);
        }
        return -1;
    }

    /**
     * Returns the index of the latest deluxe room without a reservation.
     * 
     * @return the index of the latest deluxe room without a reservation, or -1 if all rooms are reserved
     */
    public int latestDeluxeRoomNoReservation() {
        for (DeluxeRoom deluxeRoom : deluxeRooms) {
            if (!deluxeRoom.getBookStatus()) {
                return deluxeRooms.indexOf(deluxeRoom);
            }
        }
        return -1;
    }

    /**
     * Returns the index of the latest exexutive room without a reservation.
     * 
     * @return the index of the latest executive room without a reservation, or -1 if all rooms are reserved
     */
    public int latestExecRoomNoReservation() {
        for (ExecutiveRoom execRoom : execRooms) {
            if (!execRoom.getBookStatus()) {
                return execRooms.indexOf(execRoom);
            }
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
     * Returns the number of deluxe rooms that can be removed (i.e., not reserved).
     * 
     * @return the number of deluxe rooms that can be removed
     */
    public int removableDeluxeRooms() {
        int count = 0;
        for (DeluxeRoom deluxeRoom : deluxeRooms) {
            if (!deluxeRoom.getBookStatus())
                ++count;
        }
        return count;
    }

    /**
     * Returns the number of deluxe rooms that can be removed (i.e., not reserved).
     * 
     * @return the number of deluxe rooms that can be removed
     */
    public int removeableExecutiveRooms(){
        int count = 0;
        for (ExecutiveRoom executiveRoom : execRooms) {
            if (!executiveRoom.getBookStatus())
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
        for (DeluxeRoom deluxeRoom : deluxeRooms) {
            if (deluxeRoom.getBookStatus() == true) {
                for (Reservation reservation : deluxeRoom.getReservations()) {
                    if (reservation.getGuestName().equals(guestName)) {
                        return true;
                    }
                }
            }
        }
        for (ExecutiveRoom executiveRoom : execRooms){
            if (executiveRoom.getBookStatus() == true) {
                for (Reservation reservation : executiveRoom.getReservations()) {
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
        for (DeluxeRoom deluxeRooms : getDeluxeRooms()) {
            if (deluxeRooms.getBookStatus() == true)
                return true;
        }
        for (ExecutiveRoom execRooms : getExecRooms()) {
            if (execRooms.getBookStatus() == true)
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
        for (DeluxeRoom deluxeRooms : deluxeRooms)
            totalReservations += deluxeRooms.getReservations().size();
        for (ExecutiveRoom execRooms : execRooms)
            totalReservations += execRooms.getReservations().size();

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
            for (DeluxeRoom deluxeRoom : deluxeRooms) {
                System.out.print("[" + (++count) + "] " + deluxeRoom.getName());
                deluxeRoom.printReservations("none");
            }
            for (ExecutiveRoom execRoom : execRooms) {
                System.out.print("[" + (++count) + "] " + execRoom.getName());
                execRoom.printReservations("none");
            }
        }

        if (availability.equals("Reserved")) {
            System.out.println("Reserved Rooms: ");

            for (Room room : rooms) {
                if (room.getBookStatus()) {
                    System.out.print(" - " + room.getName() + "\n");
                }
            }
            for (DeluxeRoom deluxeRoom : deluxeRooms) {
                if (deluxeRoom.getBookStatus()) {
                    System.out.print(" - " + deluxeRoom.getName() + "\n");
                }
            }
            for (ExecutiveRoom execRoom : execRooms) {
                if (execRoom.getBookStatus()) {
                    System.out.print(" - " + execRoom.getName() + "\n");
                }
            }
        }
    }

    /**
     * Adds rooms to the hotel.
     *
     * @param hotel the Hotel object to modify
     */
    public void addRoomsPrompt(Hotel hotel) {
        int baseRooms, deluxeRooms = 0, execRooms = 0, remainingRooms, totalRooms;

        int initialTotalRooms = getRooms().size() + getDeluxeRooms().size() + getExecRooms().size();

        if (initialTotalRooms >= 50) {
            System.out.println("Hotel already has 50 or more rooms! Cannot add more rooms.");
            return;
        }

        System.out.print("Enter number of base rooms to add: ");
        baseRooms = ScannerUtil.readInt();

        if (baseRooms < 1) {
            System.out.println("Invalid number of base rooms!\n");
            return;
        }
        if (initialTotalRooms + baseRooms > 50) {
            System.out.println("Cannot exceed 50 rooms!\n");
            return;
        }

        remainingRooms = 50 - initialTotalRooms - baseRooms;
        totalRooms = baseRooms + initialTotalRooms;

        if (remainingRooms > 0) {
            int maxDeluxe = Math.min((int) Math.floor(baseRooms * 0.6), remainingRooms);
            System.out.print("Enter number of deluxe rooms to add (0-" + maxDeluxe + "): ");
            deluxeRooms = ScannerUtil.readInt();

            if (deluxeRooms < 0 || deluxeRooms > maxDeluxe) {
                System.out.println("Invalid number of deluxe rooms!\n");
                return;
            }

            totalRooms += deluxeRooms;
            remainingRooms -= deluxeRooms;

            if (totalRooms >= 50) {
                System.out.println("Cannot exceed 50 rooms!\n");
                return;
            }
        }

        if (remainingRooms > 0) {
            int maxExec = Math.min((int) Math.floor(baseRooms * 0.4), remainingRooms);
            System.out.print("Enter number of executive rooms to add (0-" + maxExec + "): ");
            execRooms = ScannerUtil.readInt();

            if (execRooms < 0 || execRooms > maxExec) {
                System.out.println("Invalid number of executive rooms!\n");
                return;
            }

            totalRooms += execRooms;

            if (totalRooms > 50) {
                System.out.println("Cannot exceed 50 rooms!\n");
                return;
            }
        }

        System.out.println("Confirm adding " + baseRooms + " base room(s), " +
                        deluxeRooms + " deluxe room(s), and " + execRooms + 
                        " executive room(s) to \"" + getName() + "\"");
        System.out.print("Confirm [Yes/No]: ");
        boolean confirmed = ScannerUtil.readBoolean();

        if (!confirmed) {
            System.out.println("\nRoom addition cancelled.\n");
            return;
        }

        addRooms(baseRooms, deluxeRooms, execRooms);
        System.out.println("Total Room(s) is " + (initialTotalRooms + baseRooms + deluxeRooms + execRooms) + ".\n");
    }

    /**
     * Calculates the total number of rooms reserved for all room types in the hotel.
     *
     * @param hotel the Hotel object to calculate the total reserved rooms
     * @return the total number of reserved rooms
     */
    public int totalRoomsReserved() {
        int totalRooms = 0;
        totalRooms += totalStandardRoomsReserved();
        totalRooms += totalDeluxeRoomsReserved();
        totalRooms += totalExecutiveRoomsReserved();
        return totalRooms;
    }

    /**
     * Calculates the total number of standard rooms reserved in the hotel.
     *
     * @param hotel the Hotel object to calculate the total reserved rooms
     * @return the total number of reserved standard rooms
     */
    public int totalStandardRoomsReserved(){
        int totalStandardRooms = 0;
        for (Room room : getRooms()) {
            if (room.getBookStatus())
                totalStandardRooms++;
        }
        return totalStandardRooms;
    }

    /**
     * Calculates the total number of deluxe rooms reserved in the hotel.
     *
     * @param hotel the Hotel object to calculate the total reserved rooms
     * @return the total number of reserved deluxe rooms
     */
    public int totalDeluxeRoomsReserved(){
        int totalDeluxeRooms = 0;
        for (DeluxeRoom room : getDeluxeRooms()) {
            if (room.getBookStatus())
                totalDeluxeRooms++;
        }
        return totalDeluxeRooms;
    }

    /**
     * Calculates the total number of executiverooms reserved in the hotel.
     *
     * @param hotel the Hotel object to calculate the total reserved rooms
     * @return the total number of executive reserved rooms
     */
    public int totalExecutiveRoomsReserved(){
        int totalExecRooms = 0;
        for (ExecutiveRoom room : getExecRooms()) {
            if (room.getBookStatus())
                totalExecRooms++;
        }
        return totalExecRooms;
    }

    /**
     * Removes rooms from the hotel.
     *
     * @param hotel the Hotel object to modify
     */
    public void removeRooms(Hotel hotel) {
        int lastRoom = getRooms().size() + getDeluxeRooms().size() + getExecRooms().size();
        int removableRooms = removableRooms() + removableDeluxeRooms() + removeableExecutiveRooms();

        if (lastRoom == 1) {
            System.out.println("A hotel cannot have zero rooms.\n");
            return;
        }

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

        if (numRooms > (lastRoom - totalRoomsReserved())) {
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
        while (numRooms > 0 && lastRoom > 1) {
            if (!rooms.isEmpty() && !rooms.get(rooms.size() - 1).getBookStatus()) {
                rooms.remove(rooms.size() - 1);
                --numRooms;
            } else if (!deluxeRooms.isEmpty() && !deluxeRooms.get(deluxeRooms.size() - 1).getBookStatus()) {
                deluxeRooms.remove(deluxeRooms.size() - 1);
                --numRooms;
            } else if (!execRooms.isEmpty() && !execRooms.get(execRooms.size() - 1).getBookStatus()) {
                execRooms.remove(execRooms.size() - 1);
                --numRooms;
            }
            lastRoom = rooms.size() + deluxeRooms.size() + execRooms.size();
        }

        System.out.println("Shifting Room Numbers...");
        for (i = 0; i < rooms.size(); i++) {
            rooms.get(i).setName("RM" + (i + 1));
        }
        for (DeluxeRoom deluxeRoom : deluxeRooms){
            deluxeRoom.setName("RM" + (i + 1));
        }
        for (ExecutiveRoom executiveRoom : execRooms){
            executiveRoom.setName("RM" + (i + 1));
        }

        System.out.println("Removal Success!\n");
    }

    /**
     * Adds a new reservation to the given list of reservations and updates the book status of the specified room.
     *
     * @param room The room to be booked.
     * @param roomToUse The index of the room to be booked.
     * @param reservations The list of reservations.
     * @param guestName The name of the guest making the reservation.
     * @param checkInDate The check-in date of the reservation.
     * @param checkOutDate The check-out date of the reservation.
     */
    public void bookRoomInputInfo(Room room, ArrayList<Reservation> reservations, 
                              String guestName, int checkInDate, int checkOutDate, String discountCode) {
        reservations.add(new Reservation(guestName, checkInDate, checkOutDate, room, discountCode));
        room.setBookStatus(true);
    }

    public void bookDeluxeRoomInputInfo(DeluxeRoom room, ArrayList<Reservation> reservations, 
                              String guestName, int checkInDate, int checkOutDate, String discountCode) {
        reservations.add(new Reservation(guestName, checkInDate, checkOutDate, room, discountCode));
        room.setBookStatus(true);
    }

    public void bookExecRoomInputInfo(ExecutiveRoom room, ArrayList<Reservation> reservations, 
                              String guestName, int checkInDate, int checkOutDate, String discountCode) {
        reservations.add(new Reservation(guestName, checkInDate, checkOutDate, room, discountCode));
        room.setBookStatus(true);
    }

    public double fillDates(double pricePerNight, int checkInDate, int checkOutDate, String discountCode) {
        double totalPrice = 0.0;

        for (int i = 1; i <= 31; i++) {
            double nightPrice = 0.0;

            if (i >= checkInDate && i < checkOutDate) {
                // Get the date modifier for the current day
                double dateModifier = getDateModifier(i);
                System.out.println("Date Modifier: " + dateModifier);
                // Calculate the price for the night including the date modifier
                nightPrice = pricePerNight * getDiscountCode(discountCode) * dateModifier;

                // Add the Date object to the list with the calculated price
                dates.add(new Date(i, nightPrice));
                totalPrice += nightPrice;
            } else {
                // Add the Date object with zero price for days outside the reservation
                dates.add(new Date(i, 0));
            }
        }
        return totalPrice;
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
     * Updates the price of the hotel room.
     *
     * @param hotel the Hotel object to modify
     */
    public void updateRoomPrice(Hotel hotel) {
        if (reservationStatus() == true) {
            System.out.println("Cannot update price nor set date modifier with reservation(s).\n");
            return;
        }

        System.out.print("Would you like to update the base room price or set a modifier?: \n");
        System.out.println("[1] Update base price of rooms");
        System.out.println("[2] Set a date modifier");
        int choice = ScannerUtil.readInt();

        switch (choice) {
            case 1:
                System.out.print("Enter the new room price: ");
                double newPrice = ScannerUtil.readDouble();

                System.out.println("Confirm change of room price to " + newPrice);
                System.out.print("Confirm [Yes/No]: ");
                boolean confirmed = ScannerUtil.readBoolean();

                if (!confirmed) {
                    System.out.println("\nRoom price change cancelled.\n");
                    return;
                }

                setNewPrice(newPrice); // Update the base price
                changeRoomPrice(newPrice); // Update the price of all rooms
                System.out.println("Room price updated to " + newPrice + ".\n");
                break;
                
            case 2:
                System.out.print("Enter the day of the month (1-31): ");
                int day = ScannerUtil.readInt();
                if (day < 1 || day > 31) {
                    System.out.println("Invalid day. Please enter a value between 1 and 31.");
                    return;
                }
                
                System.out.print("Enter the price modifier for day " + day + " in percentage.");
                double priceModifier = ScannerUtil.readDouble();
                
                if (priceModifier <= 0 || priceModifier > 150 || priceModifier < 50) {
                    System.out.println("Invalid price modifier.");
                    return;
                }

                setDatePriceModifier(day, priceModifier);
                System.out.println("Price modifier for day " + day + " updated to " + priceModifier + ".");
                break;
            default:
                System.out.println("Invalid choice.\n");
        }
    }

    public void setDatePriceModifier(int day, double priceModifier) {
        if (day < 1 || day > 31) {
            System.out.println("Invalid day! Please enter a day between 1 and 31.");
            return;
        }
        datePriceModifiers[day - 1] = priceModifier / 100; // sets day in the array to its price modifier 
    }

    public double getDateModifier(int day) {
        if (day < 1 || day > 31) {
            System.out.println("Invalid day! Please enter a day between 1 and 31.");
            return 1.0; // Default modifier if day is invalid
        }
            // Return the date price modifier for the specified day
            return datePriceModifiers[day - 1]; 
    }

    /**
     * Removes reservations from the hotel room.
     *
     * @param hotel the Hotel object to modify
     * @param rooms the list of rooms in the hotel
     */
    public void removeReservations(Hotel hotel, ArrayList<Room> rooms) {
        showRooms("Reserved");
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

        System.out.println("\nConfirm removal of reservation for \"" + getName() + "\"");
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

    public double calculateMonthlyEarnings(Hotel hotel) {
        double totalEarnings = 0.0;
    
        for (Room room : getRooms()) {
            totalEarnings += calculateRoomEarnings(room);
        }
    
        for (DeluxeRoom deluxeRoom : getDeluxeRooms()) {
            totalEarnings += calculateRoomEarnings(deluxeRoom);
        }
    
        for (ExecutiveRoom executiveRoom : getExecRooms()) {
            totalEarnings += calculateRoomEarnings(executiveRoom);
        }
    
        return totalEarnings;
    }
    
    private double calculateRoomEarnings(Room room) {
        double roomEarnings = 0.0;
    
        for (Reservation reservation : room.getReservations()) {
            int checkInDate = reservation.getCheckInDate();
            int checkOutDate = reservation.getCheckOutDate();
            double pricePerNight = room.getPricePerNight();
            String discountCode = reservation.getDiscountCode();
    
            int numNights = checkOutDate - checkInDate;
            double totalPrice = fillDates(pricePerNight, checkInDate, checkOutDate, discountCode) * numNights;
    
            roomEarnings += totalPrice;
        }
    
        return roomEarnings;
    }

    /**
     * Displays the high-level information of a hotel, including the hotel name, total number of rooms,
     * and estimated earnings for the month.
     *
     * @param hotel The hotel object for which to display the information.
     */
    public void viewHighLevelInfo(Hotel hotel) {
        double estimatedEarnings = calculateMonthlyEarnings(hotel);
        
        int totalRooms = getRooms().size() + 
                        getDeluxeRooms().size() + 
                        getExecRooms().size();

        System.out.println("Hotel Name: " + getName());
        System.out.println("Total Number of Rooms: " + totalRooms);
        System.out.println("Estimated Earnings for the Month: " + String.format("%.2f", estimatedEarnings));
    }

    public double calculateEstimatedEarnings(Hotel hotel) { // ONLY FOR ONE RESERVATION
        double estimatedEarnings = getRooms().stream()
                .flatMap(room -> room.getReservations().stream())
                .mapToDouble(reservation -> {
                    int checkInDate = reservation.getCheckInDate();
                    int checkOutDate = reservation.getCheckOutDate();
                    Room room = reservation.getRoom();
                    String discountCode = reservation.getDiscountCode();
    
                    double pricePerNight = room.getPricePerNight();
    
                    return fillDates(pricePerNight, checkInDate, checkOutDate, discountCode);
                }).sum();
        return estimatedEarnings;
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
     * Prompts the user to enter the day of reservation and then calls another method to display
     *
     * @param hotel The hotel object containing the list of rooms.
     */
    public void viewAvailableAndBookedRoomsPrompt(Hotel hotel) {
        System.out.print("Enter the day of reservation (eg. 23): ");
        int date = ScannerUtil.readInt();
        if (date < 1 || date > 31) {
            System.out.println("Invalid day input. Day must be between 1 and 31.");
            return;
        }
        viewAvailableAndBookedRooms(date); // Assuming a single-day reservation
    }

    /**
     * Displays the total number of available and booked rooms in the hotel based on the given date.
     *
     * @param hotel The hotel object containing the list of rooms.
     */
    public void viewAvailableAndBookedRooms(int date) {
        int totalRooms = getRooms().size() + getDeluxeRooms().size() + getExecRooms().size();
        int totalBookedRooms = 0;
    
        for (Room room : getRooms()) {
            totalBookedRooms += room.getReservations().stream()
                    .filter(r -> r.getCheckInDate() == date || r.getCheckOutDate() == date)
                    .count();
        }
    
        for (Room room : getDeluxeRooms()) {
            totalBookedRooms += room.getReservations().stream()
                    .filter(r -> r.getCheckInDate() == date || r.getCheckOutDate() == date)
                    .count();
        }
    
        for (Room room : getExecRooms()) {
            totalBookedRooms += room.getReservations().stream()
                    .filter(r -> r.getCheckInDate() == date || r.getCheckOutDate() == date)
                    .count();
        }
    
        int totalAvailableRooms = totalRooms - totalBookedRooms;
    
        System.out.println("Total Available Rooms: " + totalAvailableRooms);
        System.out.println("Total Booked Rooms: " + totalBookedRooms);
        System.out.println("Total Available Rooms: " + totalAvailableRooms);
    }

    /**
     * Displays information about a specific room in the hotel.
     *
     * @param hotel the hotel object containing the rooms
     */
    public void viewRoomInfo(Hotel hotel) {
        System.out.println("What room type would you like to view?: ");
        System.out.println("[1] Base Room");
        System.out.println("[2] Deluxe Room");
        System.out.println("[3] Executive Room");
        int roomType = ScannerUtil.readInt();
        System.out.print("Enter room name: ");
        String roomName = ScannerUtil.readString();

        if (roomType == 1){
            Room room = getRooms().stream()
                .filter(r -> r.getName().equals(roomName))
                .findFirst()
                .orElse(null);
                if (room == null) {
                    System.out.println("Standard room not found.");
                    return;
                }
                System.out.println("Room Name: " + room.getName());
                System.out.println("Price per Night: " + room.getPricePerNight());
                System.out.println("Availability for the Month:");
                int maxDay = 31; // Assuming a fixed 31-day month for "1-DD" format
                room.showReservations(maxDay, room);
        } else if (roomType == 2){
            Room room = getDeluxeRooms().stream()
                .filter(r -> r.getName().equals(roomName))
                .findFirst()
                .orElse(null);
                if (room == null) {
                    System.out.println("Deluxe room not found.");
                    return;
                }
                System.out.println("Room Name: " + room.getName());
                System.out.println("Price per Night: " + room.getPricePerNight() + (room.getPricePerNight()*0.20));
                System.out.println("Availability for the Month:");
                int maxDay = 31; // Assuming a fixed 31-day month for "1-DD" format
                room.showReservations(maxDay, room);
        } else if (roomType == 3){
            Room room = getExecRooms().stream()
                .filter(r -> r.getName().equals(roomName))
                .findFirst()
                .orElse(null);
                if (room == null) {
                    System.out.println("Executive room not found.");
                    return;
                }
                System.out.println("Room Name: " + room.getName());
                System.out.println("Price per Night: " + room.getPricePerNight() + (room.getPricePerNight()*0.35));
                System.out.println("Availability for the Month:");
                int maxDay = 31; // Assuming a fixed 31-day month for "1-DD" format
                room.showReservations(maxDay, room);
        }
    }

    /**
     * Displays the reservation information for a given guest name.
     * 
     * @param hotel the Hotel object containing the rooms and reservations
     */
    public void viewReservationInfo(Hotel hotel) {
        Reservation reservation;
        
        System.out.print("Under what room type is the guest staying in?: ");
        System.out.println("[1] Base Room");
        System.out.println("[2] Deluxe Room");
        System.out.println("[3] Executive Room");
        int roomType = ScannerUtil.readInt();
        System.out.print("Enter guest name: ");
        String guestName = ScannerUtil.readString();

        if (roomType == 1){
        reservation = getRooms().stream()
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

            Room room = reservation.getRoom();
            String discountCode = reservation.getDiscountCode();
            double totalPrice = fillDates(room.getPricePerNight(), checkInDate, checkOutDate, discountCode);


            System.out.println("Guest Name: " + reservation.getGuestName());
            System.out.println("Room Name: " + reservation.getRoom().getName());
            System.out.println("Check-in Date: 1-" + (checkInDate < 10 ? "0" + checkInDate : checkInDate));
            System.out.println("Check-out Date: 1-" + (checkOutDate < 10 ? "0" + checkOutDate : checkOutDate));
            System.out.println("Total Price: " + String.format("%.2f", totalPrice));  
        } else if (roomType == 2){
            reservation = getDeluxeRooms().stream()
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

            Room room = reservation.getRoom();
            String discountCode = reservation.getDiscountCode();
            double totalPrice = fillDates(room.getPricePerNight(), checkInDate, checkOutDate, discountCode);


            System.out.println("Guest Name: " + reservation.getGuestName());
            System.out.println("Room Name: " + reservation.getRoom().getName());
            System.out.println("Check-in Date: 1-" + (checkInDate < 10 ? "0" + checkInDate : checkInDate));
            System.out.println("Check-out Date: 1-" + (checkOutDate < 10 ? "0" + checkOutDate : checkOutDate));
            System.out.println("Total Price: " + String.format("%.2f", totalPrice));  
        } else if (roomType == 3){
            reservation = hotel.getExecRooms().stream()
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

            Room room = reservation.getRoom();
            String discountCode = reservation.getDiscountCode();
            double totalPrice = fillDates(room.getPricePerNight(), checkInDate, checkOutDate, discountCode);


            System.out.println("Guest Name: " + reservation.getGuestName());
            System.out.println("Room Name: " + reservation.getRoom().getName());
            System.out.println("Check-in Date: 1-" + (checkInDate < 10 ? "0" + checkInDate : checkInDate));
            System.out.println("Check-out Date: 1-" + (checkOutDate < 10 ? "0" + checkOutDate : checkOutDate));
            System.out.println("Total Price: " + String.format("%.2f", totalPrice));  
        } 
    }

    public void displayRoomTypes() {
        System.out.println("[1] Base Room      - " + (rooms.size()-totalStandardRoomsReserved()) 
                           + " rooms unbooked");

        if (deluxeRooms.size() > 0) {
            System.out.println("[2] Deluxe Room    - " + (deluxeRooms.size()-totalDeluxeRoomsReserved()) 
                               + " rooms unbooked");
        } 
        if (execRooms.size() > 0) {
            System.out.println("[3] Executive Room - "  + (execRooms.size()-totalExecutiveRoomsReserved()) 
                               + " rooms unbooked");
        }
    }
}