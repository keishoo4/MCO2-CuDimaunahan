package hotel;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * The ManageHotel class is responsible for managing the hotel operations such as modifying hotel details,
 * adding or removing rooms, updating room prices, removing reservations, and removing the hotel itself.
 */
public class HotelManager {

    /**
     * Modifies the hotel by displaying the list of hotels, prompting the user to select a hotel,
     * and managing the selected hotel's configuration.
     *
     * @param sc        the Scanner object for user input
     * @param hotelList the HotelList object containing the list of hotels
     */
    public void modifyHotel(Scanner sc, HotelList hotelList) {
        int hotelNum = 1;
        while (hotelNum != 0) {
            hotelList.displayAllHotels();
            System.out.println("[0] Back to Main Menu");
            System.out.print("Enter hotel number to manage: ");
            hotelNum = sc.nextInt();
            System.out.println();

            if (hotelNum != 0) {
                if (hotelNum < 1 || hotelNum > hotelList.getHotels().size()) {
                    System.out.println("Invalid hotel number!");
                    return;
                }

                manageHotelConfig(sc, hotelList, hotelList.getHotels().get(hotelNum - 1),
                        hotelList.getHotels().get(hotelNum - 1).getRooms());
            }
        }
    }

    /**
     * Manages the configuration of the selected hotel by displaying the available options,
     * prompting the user to select an option, and performing the corresponding action.
     *
     * @param sc     the Scanner object for user input
     * @param hotelList the HotelList object containing the list of hotels
     * @param hotel  the Hotel object to manage
     * @param rooms  the list of rooms in the hotel
     */
    public void manageHotelConfig(Scanner sc, HotelList hotelList, Hotel hotel, ArrayList<Room> rooms) {
        System.out.println("[1] Change Hotel Name  - " + hotel.getName());
        System.out.println("[2] Add Room           - " + hotel.getRooms().size() + " room(s)");
        System.out.println("[3] Remove Room          [CAUTION]");
        System.out.println("[4] Update Room Price  - " + hotel.getPrice());
        System.out.println("[5] Remove Reservation - " + hotel.getAllReservations() + " reservation(s)");
        System.out.println("[6] Remove Hotel         [CAUTION]");
        System.out.println("[7] Back to Main Menu");
        System.out.print("Enter choice: ");
        int choice = sc.nextInt();
        System.out.println();

        switch (choice) {
            case 1:
                changeHotelName(sc, hotelList, hotel);
                break;
            case 2:
                addRooms(sc, hotel);
                break;
            case 3:
                removeRooms(sc, hotel);
                break;
            case 4:
                updateRoomPrice(sc, hotel);
                break;
            case 5:
                removeReservations(sc, hotel, rooms);
                break;
            case 6:
                removeHotel(sc, hotelList, hotel);
                break;
            case 7:
                return;
            default:
                System.out.println("Invalid choice!");
        }
    }

    /**
     * Changes the name of the hotel.
     *
     * @param sc          the Scanner object for user input
     * @param hotelList   the HotelList object containing the list of hotels
     * @param hotel       the Hotel object to modify
     */
    public void changeHotelName(Scanner sc, HotelList hotelList, Hotel hotel) {
        System.out.print("Enter new hotel name: ");
        sc.nextLine(); // Consume
        String newHotelName = sc.nextLine();

        if (hotelList.sameHotelName(newHotelName)) {
            System.out.println("Hotel with this name already exists\n");
            return;
        }

        System.out.println("Confirm change of hotel name from \"" + hotel.getName() 
                            + "\" to \"" + newHotelName + "\"");
        System.out.print("Confirm [Yes/No]: ");
        String confirm = sc.nextLine();

        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("\nHotel name change cancelled.\n");
            return;
        }

        hotel.setName(newHotelName);
        System.out.println("Hotel name changed to \"" + newHotelName + "\"");
        System.out.println();
    }

    /**
     * Adds rooms to the hotel.
     *
     * @param sc    the Scanner object for user input
     * @param hotel the Hotel object to modify
     */
    public void addRooms(Scanner sc, Hotel hotel) {
        System.out.print("Enter number of rooms to add: ");
        int numRooms = sc.nextInt();

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
        sc.nextLine(); // Consume
        System.out.print("Confirm [Yes/No]: ");
        String confirm = sc.nextLine();
        
        if (!confirm.equalsIgnoreCase("yes")) {
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
     * @param sc    the Scanner object for user input
     * @param hotel the Hotel object to modify
     */
    public void removeRooms(Scanner sc, Hotel hotel) {
        int lastRoom = hotel.getRooms().size();

        if (lastRoom == 1) {
            System.out.println("A hotel cannot have zero rooms.\n");
            return;
        }

        int removableRooms = hotel.removableRooms();
        if (removableRooms == 0) {
            System.out.println("All rooms have reservations.\n"
                    + "Cannot remove any rooms.\n");
            return;
        }

        System.out.println("Available Rooms for Removal:     " + removableRooms);
        System.out.print("Enter number of rooms to remove: ");
        int numRooms = sc.nextInt();
        System.out.println();
        
        if (numRooms < 1) {
            System.out.println("Invalid number of rooms!\n");
            return;
        }
        
        if (numRooms > (lastRoom - totalRoomsReserved(hotel))) {
            System.out.println("Picked room numbers for removal\n"
                    + "exceed current unoccupied rooms.\n");
            return;
        }

        System.out.println("Confirm removing " + numRooms + 
                           " room(s) from \"" + hotel.getName() + "\"");
        sc.nextLine(); // Consume
        System.out.print("Confirm [Yes/No]: ");
        String confirm = sc.nextLine();

        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("\nRoom removal cancelled.\n");
            return;
        }

        int i = 0;
        while (numRooms > 0) {
            if (hotel.getRooms().get(lastRoom - i - 1).getBookStatus() == false) {
                hotel.getRooms().remove(lastRoom - i - 1);

                --numRooms;
            }
            ++i;
        }

        System.out.println("Shifting Room Numbers...");
        for (i = 0; i < hotel.getRooms().size(); i++) {
            hotel.getRooms().get(i).setName("RM" + (i + 1));
        }

        System.out.println("Removal Success!\n");
    }

    /**
     * Updates the price of the hotel room.
     *
     * @param sc    the Scanner object for user input
     * @param hotel the Hotel object to modify
     */
    public void updateRoomPrice(Scanner sc, Hotel hotel) {
        if (hotel.reservationStatus() == true) {
            System.out.println("Cannot update price with reservation(s).\n");
            return;
        }

        System.out.println("  Current Price: " + hotel.getPrice());
        System.out.print("Enter New Price: ");
        double newPrice = sc.nextDouble();

        if (newPrice < 100) {
            System.out.println("Price too Low!\n");
            return;
        }

        System.out.println("Confirm updating price for \"" + hotel.getName() + "\"");
        sc.nextLine(); // Consume
        System.out.print("Confirm [Yes/No]: ");
        String confirm = sc.nextLine();
        
        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("\nPrice update cancelled.\n");
            return;
        }

        hotel.setNewPrice(newPrice);
        hotel.changeRoomPrice(hotel.getPrice());
        System.out.println("\nNew Price set to " + hotel.getPrice() + "!\n");
    }

    /**
     * Removes reservations from the hotel room.
     *
     * @param sc    the Scanner object for user input
     * @param hotel the Hotel object to modify
     * @param rooms the list of rooms in the hotel
     */
    public void removeReservations(Scanner sc, Hotel hotel, ArrayList<Room> rooms) {
        hotel.showRooms("Reserved");
        System.out.println("[0] Back to Main Menu");
        System.out.print("Input Room Name: ");
        sc.nextLine(); // Consume
        String roomTracker = sc.nextLine();
        System.out.println();

        if (roomTracker.equals("0"))
            return;

        int roomNum = Integer.parseInt(roomTracker.substring(2)) - 1;

        if (roomNum >= rooms.size()) {
            System.out.println("Invalid room number!\n");
            return;
        }

        hotel.getRooms().get(roomNum).printReservations("View");
        System.out.println("[0] Back to Main Menu");
        System.out.print("Pick reservation(s) to remove: ");
        int resNum = sc.nextInt();

        if (resNum == 0)
            return;

        System.out.println("\nConfirm removal of reservation for \"" + 
                            hotel.getName() + "\"");
        sc.nextLine(); // Consume
        System.out.print("Confirm [Yes/No]: ");
        String confirm = sc.nextLine();
        
        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("\nReservation removal cancelled.\n");
            return;
        }        
        
        rooms.get(roomNum).getReservations().remove(resNum - 1);
        if (rooms.get(roomNum).getReservations().isEmpty())
            rooms.get(roomNum).setBookStatus(false); // Set room to unoccupied (false

        System.out.println("Reservation " + resNum + " of " +
                rooms.get(roomNum).getName() + " removed!\n");
    }

    /**
     * Removes the hotel from the hotel list.
     *
     * @param sc        the Scanner object for user input
     * @param hotelList the HotelList object containing the list of hotels
     * @param hotel     the Hotel object to remove
     */
    public void removeHotel(Scanner sc, HotelList hotelList, Hotel hotel) {
        if (hotel.reservationStatus() == true) {
            System.out.println("Cannot remove hotel with reservation(s).\n");
            return;
        }

        System.out.println("Confirm removal of \"" + hotel.getName() + 
                           "\" from the hotel list");
        sc.nextLine(); // Consume
        System.out.print("Confirm [Yes/No]: ");
        String confirm = sc.nextLine();
        
        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("\nHotel removal cancelled.\n");
            return;
        }

        hotelList.getHotels().remove(hotel);
        System.out.println("Hotel \"" + hotel.getName() + "\" removed\n");
    }
}