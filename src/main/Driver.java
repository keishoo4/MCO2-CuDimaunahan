import java.util.ArrayList;
import java.util.Scanner;

import hotel.BookingManager;
import hotel.HotelList;
// import hotel.HotelManager;
import hotel.HotelViewManager;
import hotel.Hotel;
import hotel.Room;

import utils.ScannerUtil;

public class Driver {
    public static void main(String[] args) {
        HotelList              hotelList = new HotelList();
        HotelViewManager       viewHotel = new HotelViewManager();
        // HotelManager         manageHotel = new HotelManager();
        BookingManager   simulateBooking = new BookingManager();

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
                // case 2:
                //     viewHotel.mainHotelSelection(sc, hotelList);
                //     break;
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
        int hotelNum = 1;
        int lastRoom, roomToUse, roomReserved,
            reservationNum, checkInDate, checkOutDate, 
            i;
        String guestName;

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

                lastRoom = hotelList.getHotels().get(hotelNum-1).getRooms().size() - 1; // Last room of the hotel (index)


                roomReserved = 0;
                roomToUse = 0;
                i = 0;
                while (i <= lastRoom) {
                    if (!hotelList.getHotels().get(hotelNum-1).
                         getRooms().get(i).getBookStatus()) { // If the room does not have a booking
                        roomToUse = hotelList.getHotels().get(hotelNum-1).LatestRoomNoReservation();
                        roomReserved = 1;
                        break;
                    }
                    ++i;
                }

                if (roomReserved == 0) {
                    System.out.println("All rooms are occupied with a booking!");
                    // roomToUse = chooseAnotherRoom(sc, hotel) - 1;

                    System.out.println("Choose a different date \nor go to a different hotel.\n");
                    hotelList.getHotels().get(hotelNum-1).showRooms("All");
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

    
}