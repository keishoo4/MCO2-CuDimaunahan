// TODO Fix room types in reservation details

import java.util.ArrayList;

import model.hotel.HotelList;
import model.hotel.Reservation;
import model.hotel.Hotel;
import model.hotel.Room;
import model.hotel.DeluxeRoom;
import model.hotel.ExecutiveRoom;
import view.GUI;
import controller.Controller;

import utils.ScannerUtil;

public class Driver {
    public static void main(String[] args) {
        HotelList   hotelList  = new HotelList();
        GUI         gui       = new GUI();
            new Controller(hotelList, gui);

        gui.setVisible(true);

        // TERMINAL VERSION
        while (true) {
            System.out.println("[HOTEL MANAGEMENT SYSTEM]");
            System.out.println("[1] Create Hotel");
            System.out.println("[2] View Hotels");
            System.out.println("[3] Manage Hotel");
            System.out.println("[4] Simulate Booking");
            System.out.println("[5] Exit Hotel Manager");
            System.out.print("Enter choice: ");            
            int choice = ScannerUtil.readInt();
            System.out.println();

            switch (choice) {
                case 1:
                    // createHotel(hotelList);
                    addHotels(hotelList);
                    break;
                case 2:
                    mainHotelSelection(hotelList);
                    break;
                case 3:
                    modifyHotel(hotelList);
                    break;
                case 4:
                    simulateBooking(hotelList);
                    break;
                case 5:
                    System.out.println("Exiting Hotel Management...");
                    ScannerUtil.closeScanner();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public static void addHotels(HotelList hotelList) {
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
        System.out.println("Executive Rooms - [" + execRooms + "]");
    }

public static void simulateBooking(HotelList hotelList) {
    Hotel hotel;
    Room              room = null;
    DeluxeRoom  deluxeRoom = null;
    ExecutiveRoom execRoom = null;
    ArrayList<Reservation> reservations = null;
    String guestName;

    int hotelNum, roomToUse, reservationNum, 
        successRoom, i, roomChoice,
        lastRoom, lastDeluxeRoom, lastExecRoom, totalRooms,
        checkInDate, checkOutDate;

    hotelNum = 1;
    while (hotelNum != 0) {
        hotelList.displayAllHotels();
        System.out.println("[0] Back to Main Menu");
        System.out.print("Enter hotel number to reserve to: ");
        hotelNum = ScannerUtil.readInt();
        System.out.println();

        if (hotelNum != 0) {
            if (hotelNum < 1 || hotelNum > hotelList.getHotels().size()) {
                System.out.println("Invalid hotel number!\n");
                return;
            }

            hotel = hotelList.getHotels().get(hotelNum - 1);

            successRoom = 0;
            roomToUse = 0;
            lastRoom = hotel.getRooms().size();
            lastDeluxeRoom = hotel.getDeluxeRooms().size();
            lastExecRoom = hotel.getExecRooms().size();
            roomChoice = 0;
            totalRooms = 0;
 
            for (i=0; i<=lastRoom-1; i++) {
                if (!hotel.getRooms().get(i).getBookStatus()) { // If the room does not have a booking
                    roomToUse = hotel.latestRoomNoReservation();
                    successRoom = 1;
                    break;
                }
            }
            for (i=0; i<=lastDeluxeRoom-1; i++) {
                if (!hotel.getDeluxeRooms().get(i).getBookStatus()) { // If the room does not have a booking
                    roomToUse = hotel.latestDeluxeRoomNoReservation();
                    successRoom = 1;
                    break;
                }
            }
            for (i=0; i<=lastExecRoom-1; i++) {
                if (!hotel.getExecRooms().get(i).getBookStatus()) { // If the room does not have a booking
                    roomToUse = hotel.latestExecutiveRoomNoReservation();
                    successRoom = 1;
                    break;
                }
            }            

            if (successRoom == 0) {
                System.out.println("All rooms are occupied with a booking!");
                roomToUse = hotel.chooseAnotherRoom()-1;
                totalRooms = lastRoom + lastDeluxeRoom + lastExecRoom;

                if (roomToUse == -1) 
                    return;
                if (roomToUse > (totalRooms)) {
                    System.out.println("Invalid room number. Please try again.\n");
                    return;
                }
            }

            // CHOOSING ROOMS STARTS HERE
            if (successRoom != 0) {
                System.out.println("What kind of room would you like to book?");
                hotel.displayRoomTypes();
                roomChoice = ScannerUtil.readInt();

                if (roomChoice == 1) {
                    System.out.println("Standard room selected!\n");
                    roomToUse = hotel.latestRoomNoReservation();
                } 
                else if (roomChoice == 2) {
                    System.out.println("Deluxe room selected!\n");
                    roomToUse = hotel.latestDeluxeRoomNoReservation();
                } 
                else if (roomChoice == 3) {
                    System.out.println("Executive room selected!\n");
                    roomToUse = hotel.latestExecutiveRoomNoReservation();
                } 
                else {
                    System.out.println("Invalid room type!\n");
                    return;
                }

                if (roomToUse == -1) {
                    System.out.println("No available rooms of the selected type.\n");
                    return;
                }
            }

            System.out.println("[0] Exit Reservation");
            System.out.print("Enter guest name: ");
            guestName = ScannerUtil.readString();

            if (guestName.equals("0")) {
                System.out.println("Reservation cancelled.\n");
                return;
            }
            if (hotel.sameGuestName(guestName)) {
                System.out.println("Guest name already exists. Please try again.\n");
                return;
            }

            // START OF ROOM USAGE
            if (roomChoice == 1) {
                room = hotel.getRooms().get(roomToUse);        
                reservations = room.getReservations();
            }
            else if (roomChoice == 2) {
                if (successRoom == 0)
                    roomToUse -= hotel.getRooms().size();
                
                deluxeRoom = hotel.getDeluxeRooms().get(roomToUse);
                reservations = deluxeRoom.getReservations();
            }
            else if (roomChoice == 3) {
                if (successRoom == 0)
                    roomToUse -= hotel.getRooms().size() + hotel.getDeluxeRooms().size();
                
                execRoom = hotel.getExecRooms().get(roomToUse);
                reservations = execRoom.getReservations();
            }

            checkInDate = 0;
            checkOutDate = 0;
            while (checkOutDate == 0) {
                System.out.println("   [0] Exit Reservation");
                System.out.println("   STRICT FORMATTING: 1-DD");
                checkInDate = hotel.checkIn();

                if (checkInDate == 0) {
                    System.out.println("Reservation cancelled.\n");
                    return;
                }

                checkOutDate = hotel.checkOut(checkInDate);
            }

            if (roomChoice == 1) {
                if (!room.isReservationValid(checkInDate, checkOutDate)) {
                    System.out.println("Invalid reservation! Try a new date or room.\n");
                    return;
                }
            }
            else if (roomChoice == 2) {
                if (!deluxeRoom.isReservationValid(checkInDate, checkOutDate)) {
                    System.out.println("Invalid reservation! Try a new date or room.\n");
                    return;
                }
            }
            else {
                if (!execRoom.isReservationValid(checkInDate, checkOutDate)) {
                    System.out.println("Invalid reservation! Try a new date or room.\n");
                    return;
                }
            }
            System.out.print("Enter discount code (or press Enter to skip): ");
            String discountCode = ScannerUtil.readString();
            if (discountCode.equals("I_WORK_HERE")) {
                System.out.println("Discount code I_WORK_HERE applied!\n");
            } 
            else if (discountCode.equals("STAY4_GET1") && checkOutDate - checkInDate >= 4) {
                System.out.println("Discount code STAY4_GET1 applied!\n");
            }
            else if (discountCode.equals("PAYDAY") && 
                    ((checkInDate <= 15 && 15 < checkOutDate) || (checkInDate <= 30 && 30 < checkOutDate))) {
                System.out.println("Discount code PAYDAY applied!\n");
            } 
            else if (discountCode.equals("")) {
                System.out.println("No discount code applied.\n");
            } 
            else {
                System.out.println("Invalid discount code!\n");
            }

            if (roomChoice == 1) {
                hotel.fillDates(room.getPricePerNight(), checkInDate, checkOutDate, discountCode);
                room.bookInputInfo(room, reservations, guestName, checkInDate, checkOutDate, discountCode);
                reservationNum = room.getReservations().size();
                System.out.println("Reservation No.: " + reservationNum);
                System.out.println(room.getName() + " booked successfully!\n");
            }
            else if (roomChoice == 2) {
                hotel.fillDates(deluxeRoom.getPricePerNight(), checkInDate, checkOutDate, discountCode);
                deluxeRoom.bookInputInfo(deluxeRoom, reservations, guestName, checkInDate, checkOutDate, discountCode);
                reservationNum = deluxeRoom.getReservations().size();
                System.out.println("Reservation No.: " + reservationNum);
                System.out.println(deluxeRoom.getName() + " booked successfully!\n");
            }
            else {
                hotel.fillDates(execRoom.getPricePerNight(), checkInDate, checkOutDate, discountCode);
                execRoom.bookInputInfo(execRoom, reservations, guestName, checkInDate, checkOutDate, discountCode);
                reservationNum = execRoom.getReservations().size();
                System.out.println("Reservation No.: " + reservationNum);
                System.out.println(execRoom.getName() + " booked successfully!\n");
            }
        }
    }
}

    private static void modifyHotel(HotelList hotelList) {
        int hotelNum = 1;
        while (hotelNum != 0) {
            hotelList.displayAllHotels();
            System.out.println("[0] Back to Main Menu");
            System.out.print("Enter hotel number to manage: ");
            hotelNum = ScannerUtil.readInt();
            System.out.println();

            if (hotelNum == 0){
                return;
            }

            Hotel hotel = hotelList.getHotels().get(hotelNum - 1);
            if (hotelNum != 0) {
                if (hotelNum < 1 || hotelNum > hotelList.getHotels().size()) {
                    System.out.println("Invalid hotel number!");
                    return;
                }

                manageHotelConfig(hotelList, hotel, hotel.getRooms());
            }
        }
    }
    
    private static void manageHotelConfig(HotelList hotelList, Hotel hotel, ArrayList<Room> rooms) {
        System.out.println("[1] Change Hotel Name  - " + hotel.getName());
        System.out.println("[2] Add Room           - " + hotel.getRooms().size() + " room(s)");
        System.out.println("[3] Remove Room          [CAUTION]");
        System.out.println("[4] Update Room Price and Date Modifier  - " + hotel.getPrice());
        System.out.println("[5] Remove Reservation - " + hotel.getAllReservations() + " reservation(s)");
        System.out.println("[6] Remove Hotel         [CAUTION]");
        System.out.println("[7] Back to Main Menu");
        System.out.print("Enter choice: ");
        int choice = ScannerUtil.readInt();
        System.out.println();

        switch (choice) {
            case 1:
                hotelList.changeHotelName(hotelList, hotel);
                break;
            case 2:
                hotel.addRoomsPrompt(hotel);
                break;
            case 3:
                hotel.removeRooms(hotel);
                break;
            case 4:
                hotel.updateRoomPrice(hotel);
                break;
            case 5:
                hotel.removeReservations(hotel, rooms);
                break;
            case 6:
                hotelList.removeHotel(hotel);
                break;
            case 7:
                return;
            default:
                System.out.println("Invalid choice!");
        }
    }

    /**
     * Displays the list of hotels and prompts the user to select a hotel to manage.
     * 
     * @param sc The Scanner object used for user input.
     * @param hotelList The HotelList object containing the list of hotels.
     */
    public static void mainHotelSelection(HotelList hotelList) {
        hotelList.displayAllHotels();
        System.out.print("Enter hotel number to manage: ");
        int hotelNum = ScannerUtil.readInt();;

        if (hotelNum < 1 || hotelNum > hotelList.getHotels().size()) {
            System.out.println("Invalid hotel number\n");
            return;
        }

        displayHotel(hotelList, hotelNum - 1);
    }

    /**
     * Displays the menu for viewing hotel information and handles user input.
     *
     * @param hotelList The HotelList object containing the list of hotels.
     * @param hotelNum The index of the hotel to be displayed.
     */

    private static void displayHotel(HotelList hotelList, int hotelNum) {
        Hotel hotel = hotelList.getHotels().get(hotelNum);
        while (true) {
            System.out.println("\n[1] View High-level Information");
            System.out.println("[2] View Available and Booked Rooms for a Date");
            System.out.println("[3] View Information about a Room");
            System.out.println("[4] View Information about a Reservation");
            System.out.println("[5] Back to Main Menu");
            System.out.print("Enter choice: ");
            int choice = ScannerUtil.readInt();
            System.out.println();

            switch (choice) {
                case 1:
                    hotel.viewHighLevelInfo(hotel);
                    break;
                case 2:
                    hotel.viewAvailableAndBookedRooms(hotel);
                    break;
                case 3:
                    hotel.viewRoomInfo(hotel);
                    break;
                case 4:
                    hotel.viewReservationInfo(hotel);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
    
}