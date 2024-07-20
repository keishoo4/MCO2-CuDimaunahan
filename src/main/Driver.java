// TODO: Make a new class for Managing the Reservation?

import java.util.ArrayList;
import hotel.HotelList;
import hotel.Reservation;
import hotel.Hotel;
import hotel.Room;

import utils.ScannerUtil;

public class Driver {
    public static void main(String[] args) {
        HotelList   hotelList = new HotelList();

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
                    createHotel(hotelList);
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

    private static void createHotel(HotelList hotelList) {
        System.out.print("Enter hotel name: ");
        String hotelName = ScannerUtil.readString();

        if (hotelList.sameHotelName(hotelName)) {
            System.out.println("Hotel with this name already exists!\n");
            return;
        }

        System.out.print("Enter number of rooms (1-50): ");
        int roomCount = ScannerUtil.readInt();

        if (roomCount < 1 || roomCount > 50) {
            System.out.println("Invalid number of rooms! Must be between 1 and 50.");
            return;
        }

        hotelList.addHotel(hotelName, roomCount);
        System.out.println("\nHotel \"" + hotelName + "\" created successfully!\n");
    }

    public static void simulateBooking(HotelList hotelList) {        
        int hotelNum;
        int lastRoom, roomToUse, roomReserved,
            reservationNum, checkInDate, checkOutDate, 
            i;
        String guestName;
        Hotel hotel;
        Room room;
        ArrayList<Reservation> reservations;


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
                
                hotel    = hotelList.getHotels().get(hotelNum-1);
                lastRoom = hotel.getRooms().size() - 1; // Last room of the hotel (index)

                roomReserved = 0;
                roomToUse = 0;
                i = 0;
                while (i <= lastRoom) {
                    if (!hotel.getRooms().get(i).getBookStatus()) { // If the room does not have a booking
                        roomToUse = hotel.LatestRoomNoReservation();
                        roomReserved = 1;
                        break;
                    }
                    ++i;
                }

                if (roomReserved == 0) {
                    System.out.println("All rooms are occupied with a booking!");
                    // roomToUse = chooseAnotherRoom(sc, hotel) - 1;

                    System.out.println("Choose a different date \nor go to a different hotel.\n");
                    hotel.showRooms("All");
                    System.out.println("[0] Back to Hotel Selection");
                    System.out.print("Enter room number: ");
                    roomToUse = ScannerUtil.readInt();
                    System.out.println();

                    if (roomToUse == 0) 
                        return;
                    if (roomToUse > lastRoom) {
                        System.out.println("Invalid room number. Please try again.\n");
                        return;
                    }
                }

                room = hotel.getRooms().get(roomToUse);
                reservations = room.getReservations();
                System.out.println("[0] Exit Reservation");
                System.out.print("Enter guest name: ");
                guestName = ScannerUtil.readString();                

                if (guestName.equals("0")) {
                    System.out.println("Reservation cancelled.\n");
                    return;
                }
        
                if (hotel.getRooms().get(0).getBookStatus() == true && 
                    hotel.sameGuestName(guestName)) {
                    System.out.println("Guest name already exists. Please try again.\n");
                    return;
                }

                checkInDate = 0;
                checkOutDate = 0;
                while (checkOutDate == 0) {
                    System.out.println("   [0] Exit Reservation");
                    System.out.println("   STRICT FORMATTING: 1-DD");
                    checkInDate  = hotel.checkIn();
        
                    if (checkInDate == 0) {
                        System.out.println("Reservation cancelled.\n");
                        return;
                    }
        
                    checkOutDate = hotel.checkOut(checkInDate);
                }

                if (room.getBookStatus() == true) { // if reservation is already made
                    if (!(room.isReservationValid(checkInDate, checkOutDate))) {
                        System.out.println("Invalid reservation! Try a new date or room.\n");
                        return;
                    }
                }

                room.bookInputInfo(room, reservations, guestName, checkInDate, checkOutDate);
        
                reservationNum = room.getReservations().size();
                System.out.println("Reservation No.: " + reservationNum);
                System.out.println(room.getName() + " booked successfully!\n");                


            }
        }    
    }

    // private static void viewHotel(HotelList hotelList) {

    public static void modifyHotel(HotelList hotelList) {
        int hotelNum = 1;
        while (hotelNum != 0) {
            hotelList.displayAllHotels();
            System.out.println("[0] Back to Main Menu");
            System.out.print("Enter hotel number to manage: ");
            hotelNum = ScannerUtil.readInt();
            System.out.println();

            if (hotelNum != 0) {
                if (hotelNum < 1 || hotelNum > hotelList.getHotels().size()) {
                    System.out.println("Invalid hotel number!");
                    return;
                }

                manageHotelConfig(hotelList, hotelList.getHotels().get(hotelNum - 1),
                                  hotelList.getHotels().get(hotelNum - 1).getRooms());
            }
        }
    }
    
    private static void manageHotelConfig(HotelList hotelList, Hotel hotel, ArrayList<Room> rooms) {
        System.out.println("[1] Change Hotel Name  - " + hotel.getName());
        System.out.println("[2] Add Room           - " + hotel.getRooms().size() + " room(s)");
        System.out.println("[3] Remove Room          [CAUTION]");
        System.out.println("[4] Update Room Price  - " + hotel.getPrice());
        System.out.println("[5] Remove Reservation - " + hotel.getAllReservations() + " reservation(s)");
        System.out.println("[6] Remove Hotel         [CAUTION]");
        System.out.println("[7] Back to Main Menu");
        System.out.print("Enter choice: ");
        int choice = ScannerUtil.readInt();
        System.out.println();

        switch (choice) {
            case 1:
                hotel.changeHotelName(hotelList);
                break;
            case 2:
                hotel.addRooms(hotel);
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
            System.out.println("Invalid hotel number");
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