import java.util.ArrayList;
import java.util.Scanner;

import hotel.BookingManager;
import hotel.HotelList;
import hotel.HotelManager;
import hotel.HotelViewManager;
import hotel.Hotel;

import utils.ScannerUtil;

public class Driver {
    public static void main(String[] args) {
        HotelList              hotelList = new HotelList();
        HotelViewManager       viewHotel = new HotelViewManager();
        HotelManager         manageHotel = new HotelManager();
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
                // case 3:
                //     manageHotel.modifyHotel(sc, hotelList);
                //     break;
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
}