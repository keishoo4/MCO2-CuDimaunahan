// package hotel;

// import java.util.Scanner;

// /**
//  * The ViewHotel class represents a user interface for managing hotels and their information.
//  * It provides various options for viewing high-level information, available and booked rooms,
//  * room information, and reservation information for a specific hotel.
//  */
// public class HotelViewManager {

//     /**
//      * Displays the list of hotels and prompts the user to select a hotel to manage.
//      * 
//      * @param sc The Scanner object used for user input.
//      * @param hotelList The HotelList object containing the list of hotels.
//      */
//     // public void mainHotelSelection(Scanner sc, HotelList hotelList) {
//     //     hotelList.displayAllHotels();
//     //     System.out.print("Enter hotel number to manage: ");
//     //     int hotelNum = sc.nextInt();

//     //     if (hotelNum < 1 || hotelNum > hotelList.getHotels().size()) {
//     //         System.out.println("Invalid hotel number");
//     //         return;
//     //     }

//     //     displayHotel(sc, hotelList, hotelNum - 1);
//     // }

//     /**
//      * Displays the menu for viewing hotel information and handles user input.
//      *
//      * @param sc The Scanner object used for user input.
//      * @param hotelList The HotelList object containing the list of hotels.
//      * @param hotelNum The index of the hotel to be displayed.
//      */
//     // private void displayHotel(Scanner sc, HotelList hotelList, int hotelNum) {
//     //     Hotel hotel = hotelList.getHotels().get(hotelNum);
//     //     while (true) {
//     //         System.out.println("\n[1] View High-level Information");
//     //         System.out.println("[2] View Available and Booked Rooms for a Date");
//     //         System.out.println("[3] View Information about a Room");
//     //         System.out.println("[4] View Information about a Reservation");
//     //         System.out.println("[5] Back to Main Menu");
//     //         System.out.print("Enter choice: ");
//     //         int choice = sc.nextInt();
//     //         sc.nextLine(); // Consume newline
//     //         System.out.println();

//     //         switch (choice) {
//     //             case 1:
//     //                 viewHighLevelInfo(hotel);
//     //                 break;
//     //             case 2:
//     //                 viewAvailableAndBookedRooms(hotel, sc);
//     //                 break;
//     //             case 3:
//     //                 viewRoomInfo(hotel, sc);
//     //                 break;
//     //             case 4:
//     //                 viewReservationInfo(hotel, sc);
//     //                 break;
//     //             case 5:
//     //                 return;
//     //             default:
//     //                 System.out.println("Invalid choice");
//     //         }
//     //     }
//     // }

//     /**
//      * Checks if a given date is within a specified range.
//      *
//      * @param date      the date to check
//      * @param startDate the start date of the range (inclusive)
//      * @param endDate   the end date of the range (inclusive)
//      * @return true if the date is within the range, false otherwise
//      */
//     // private boolean isDateInRange(int date, int startDate, int endDate) {
//     //     return date >= startDate && date <= endDate;
//     // }

//     /**
//      * Displays the high-level information of a hotel, including the hotel name, total number of rooms,
//      * and estimated earnings for the month.
//      *
//      * @param hotel The hotel object for which to display the information.
//      */
//     // private void viewHighLevelInfo(Hotel hotel) {
//     //     System.out.println("Hotel Name: " + hotel.getName());
//     //     System.out.println("Total Number of Rooms: " + hotel.getRooms().size());

//     //     double estimatedEarnings = hotel.getRooms().stream()
//     //             .flatMap(room -> room.getReservations().stream())
//     //             .mapToDouble(reservation -> {
//     //                 int checkInDate = reservation.getCheckInDate();
//     //                 int checkOutDate = reservation.getCheckOutDate();

//     //                 int diffInDays = checkOutDate - checkInDate;
//     //                 return (diffInDays) * reservation.getRoom().getPricePerNight(); // including the check-in day
//     //             })
//     //             .sum();

//     //     System.out.println("Estimated Earnings for the Month: " + estimatedEarnings);
//     // }

//     /**
//      * Displays the total number of available and booked rooms in the hotel based on the given date.
//      *
//      * @param hotel The hotel object containing the list of rooms.
//      * @param sc The Scanner object used for user input.
//      */
//     // private void viewAvailableAndBookedRooms(Hotel hotel, Scanner sc) {
//     //     System.out.print("Enter the day of reservation (eg. 23): ");
//     //     int date = sc.nextInt();

//     //     if (date < 1 || date > 31) {
//     //         System.out.println("Invalid day input. Day must be between 1 and 31.");
//     //         return;
//     //     }

//     //     long bookedRoomsCount = hotel.getRooms().stream()
//     //             .flatMap(room -> room.getReservations().stream())
//     //             .filter(reservation -> isDateInRange(date, reservation.getCheckInDate(), reservation.getCheckOutDate()))
//     //             .count();

//     //     long availableRoomsCount = hotel.getRooms().size() - bookedRoomsCount;

//     //     System.out.println("Total Number of Available Rooms: " + availableRoomsCount);
//     //     System.out.println("Total Number of Booked Rooms: " + bookedRoomsCount);
//     // }

//     /**
//      * Displays the availability of a room for each day within a specified range.
//      *
//      * @param maxDay the maximum day in the range
//      * @param room the room for which to display the reservations
//      */
//     // private void showReservations(int maxDay, Room room) {
//     //     for (int d = 1; d <= maxDay; d++) {
//     //         final int day = d; // use a final var because of lambda expression, needs to be constant
//     //         boolean isBooked = room.getReservations().stream()
//     //                 .anyMatch(reservation -> isDateInRange(day, reservation.getCheckInDate(), reservation.getCheckOutDate()));
//     //         System.out.println("1-" + (day < 10 ? "0" + day : day) + ": " + (isBooked ? "Booked" : "Available"));
//     //     }        
//     // }
    
//     /**
//      * Displays information about a specific room in the hotel.
//      *
//      * @param hotel the hotel object containing the rooms
//      * @param sc the scanner object used for user input
//      */
//     // private void viewRoomInfo(Hotel hotel, Scanner sc) {
//     //     System.out.print("Enter room name: ");
//     //     String roomName = sc.nextLine();
//     //     Room room = hotel.getRooms().stream()
//     //             .filter(r -> r.getName().equals(roomName))
//     //             .findFirst()
//     //             .orElse(null);

//     //     if (room == null) {
//     //         System.out.println("Room not found.");
//     //         return;
//     //     }

//     //     System.out.println("Room Name: " + room.getName());
//     //     System.out.println("Price per Night: " + room.getPricePerNight());
//     //     System.out.println("Availability for the Month:");
//     //     int maxDay = 31; // Assuming a fixed 31-day month for "1-DD" format
//     //     showReservations(maxDay, room);

//     // }

//     /**
//      * Displays the reservation information for a given guest name.
//      * 
//      * @param hotel the Hotel object containing the rooms and reservations
//      * @param sc the Scanner object for user input
//      */
//     private void viewReservationInfo(Hotel hotel, Scanner sc) {
//     //     System.out.print("Enter guest name: ");
//     //     String guestName = sc.nextLine();

//     //     Reservation reservation = hotel.getRooms().stream()
//     //             .flatMap(room -> room.getReservations().stream())
//     //             .filter(r -> r.getGuestName().equals(guestName))
//     //             .findFirst()
//     //             .orElse(null);

//     //     if (reservation == null) {
//     //         System.out.println("Reservation not found.");
//     //         return;
//     //     }

//     //     int checkInDate = reservation.getCheckInDate();
//     //     int checkOutDate = reservation.getCheckOutDate();

//     //     int diffInDays = checkOutDate - checkInDate;
//     //     double totalPrice = (diffInDays) * reservation.getRoom().getPricePerNight(); // including the check-in day

//     //     System.out.println("Guest Name: " + reservation.getGuestName());
//     //     System.out.println("Room Name: " + reservation.getRoom().getName());
//     //     System.out.println("Check-in Date: 1-" + (checkInDate < 10 ? "0" + checkInDate : checkInDate));
//     //     System.out.println("Check-out Date: 1-" + (checkOutDate < 10 ? "0" + checkOutDate : checkOutDate));
//     //     System.out.println("Total Price: " + totalPrice);
//     //     System.out.println("Price Breakdown per Night: " + reservation.getRoom().getPricePerNight());
//     // }
// }