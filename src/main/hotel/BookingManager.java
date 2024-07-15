package hotel;

import java.util.Scanner;
import java.util.ArrayList; 

/**
 * The SimulateBooking class provides a method for simulating hotel bookings. 
 * This includes check-ins and check-outs.
 */
public class BookingManager {

    /**
     * Displays a list of hotels and allows the user to reserve a hotel by entering the hotel number.
     * If the user enters an invalid hotel number, an error message is displayed.
     * 
     * @param sc the Scanner object used for user input
     * @param hotelList the HotelList object containing the list of hotels
     */

    /**
     * Takes user input to make a hotel booking. The user is prompted to enter the 
     * guest name, check-in date, and check-out date.
     * 
     * @param sc The Scanner object to read user input.
     * @param hotel The Hotel object representing the hotel where the booking is made.
     */
    public void bookingInfo(Scanner sc, Hotel hotel, Room room) {
        int lastRoom,
            roomToUse, reservationNum,
            checkInDate, checkOutDate, 
            successRoom, i;
        String guestName;
        ArrayList<Reservation> reservations;
        
        lastRoom = hotel.getRooms().size() - 1; // Last room of the hotel (index)
        
        successRoom = 0;
        roomToUse = 0;
        i = 0;
        while (i <= lastRoom) {
            if (!hotel.getRooms().get(i).getBookStatus()) { // If the room does not have a booking
                roomToUse = hotel.LatestRoomNoReservation();
                successRoom = 1;
                break;
            }
            ++i;
        }

        if (successRoom == 0) {
            System.out.println("All rooms are occupied with a booking!");
            roomToUse = chooseAnotherRoom(sc, hotel) - 1;
        
            if (roomToUse == -1) 
                return;
            if (roomToUse > lastRoom) {
                System.out.println("Invalid room number. Please try again.\n");
                return;
            }
        }

        reservations  = hotel.getRooms().get(roomToUse).getReservations();
        sc.nextLine(); // Consume
        System.out.println("[0] Exit Reservation");
        System.out.print("Enter guest name: ");
        guestName = sc.nextLine();

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
            checkInDate  = checkIn(sc);

            if (checkInDate == 0) {
                System.out.println("Reservation cancelled.\n");
                return;
            }

            checkOutDate = checkOut(sc, checkInDate);
        }

        if (hotel.getRooms().get(roomToUse).getBookStatus() == true) { // if reservation is already made
            if (!(hotel.getRooms().get(roomToUse).isReservationValid(checkInDate, checkOutDate))) {
                System.out.println("Invalid reservation! Try a new date or room.\n");
                return;
            }
        }

        bookInputInfo(hotel, roomToUse, reservations, guestName, checkInDate, checkOutDate);
        
        reservationNum = hotel.getRooms().get(roomToUse).getReservations().size();
        System.out.println("Reservation No.: " + reservationNum);
        System.out.println(hotel.getRooms().get(roomToUse).getName() + " booked successfully!\n");
    }

    /**
     * Allows the user to choose another room from the available options.
     * 
     * @param sc the Scanner object used for user input
     * @param hotel the Hotel object representing the hotel where the room is located
     * @return the room number chosen by the user
     */
    @Deprecated
    public int chooseAnotherRoom(Scanner sc, Hotel hotel) {
        System.out.println("Choose a different date \nor go to a different hotel.\n");
        hotel.showRooms("All");

        System.out.println("[0] Back to Hotel Selection");
        System.out.print("Enter room number: ");
        int roomNum = sc.nextInt();
        System.out.println();
        return roomNum;
    }

    /**
     * Prompts the user to enter a check-in date and validates the input.
     * 
     * @param sc the Scanner object used to read user input
     * @return the valid check-in date entered by the user
     */
    public int checkIn(Scanner sc) {
        System.out.print(" Enter check-in date: 1-");
        int checkInDate = sc.nextInt();

        if (checkInDate >= 0 && checkInDate <= 30)
            return checkInDate;
        else {
            System.out.println("Invalid date format. Please try again.\n");
            return checkIn(sc);
        }
    }

    /**
     * Prompts the user to enter a check-out date and validates the input.
     * 
     * @param sc the Scanner object used for user input
     * @param checkInDay the check-in day to compare the check-out date with
     * @return the valid check-out date if it is after the check-in date, otherwise 0
     */
    public int checkOut(Scanner sc, int checkInDay) {
        System.out.print("Enter check-out date: 1-");
        int checkOutDate = sc.nextInt();
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
            return checkOut(sc, checkInDay);
        }
    }

    /**
     * Adds a new reservation to the given list of reservations and updates the book status of the specified room.
     *
     * @param hotel The hotel object.
     * @param roomToUse The index of the room to be booked.
     * @param reservations The list of reservations.
     * @param guestName The name of the guest making the reservation.
     * @param checkInDate The check-in date of the reservation.
     * @param checkOutDate The check-out date of the reservation.
     */
    public void bookInputInfo(Hotel hotel, int roomToUse, ArrayList<Reservation> reservations, 
                               String guestName, int checkInDate, int checkOutDate) {
            reservations.add(new Reservation(guestName, checkInDate, checkOutDate, 
                                             hotel.getRooms().get(roomToUse)));
            hotel.getRooms().get(roomToUse).setBookStatus(true); // Most important part
    }
}