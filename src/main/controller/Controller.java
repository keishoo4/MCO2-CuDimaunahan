// TODO: Update HotelView after booking (For estimated earnings)

package controller;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.event.*;
import java.util.ArrayList;

import model.hotel.HotelList;
import model.hotel.Hotel;
import model.hotel.Room;
import model.hotel.DeluxeRoom;
import model.hotel.ExecutiveRoom;
import model.hotel.Reservation;
import view.GUI;

public class Controller implements ActionListener, DocumentListener, 
                                   ListSelectionListener {
    private HotelList hotelList;
    private Hotel hotel;
    private Room room;
    private Reservation reservation;
    private GUI gui;
    private HotelController hotelController;
    private RoomController roomController;

    public Controller(HotelList hotelList, GUI gui) {
        this.hotelList = hotelList;
        this.gui = gui;
        this.hotelController = new HotelController(hotelList, gui);
        // updateView();

        gui.setActionListener(this);
        gui.setDocumentListener(this);
        gui.setListActionListener(this);
    }

    public void updateHotelList() {
        hotelController.updateHotelList(hotelList.getHotels());
    }

    public void updateCreateHotel() {
        gui.clearCreateHotel();
    }

    public void updateRoomBooking() {
        gui.clearBookingInfo();
        // gui.update
        gui.updateBookingRelated();
    }

    public void updateHotelView() {
        gui.setSelectedHotelName(hotel.getName());
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
    
        switch (command) {
            case "ADD_HOTEL":
                String hotelName = gui.getHotelName().trim();
                int rooms, deluxeRooms, execRooms; 
                
                rooms = gui.getRoomsSliderValue();
                deluxeRooms = gui.getDeluxeRoomsSliderValue();
                execRooms = gui.getExecRoomsSliderValue();

                gui.setTotalRooms(rooms + deluxeRooms + execRooms);
                
                if (hotelName.isEmpty() || hotelName.equals("") ||
                    hotelName.equals("Enter Hotel Name...")) {
                    JOptionPane.showMessageDialog(gui, "Please enter a hotel name.", 
                                                  "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                hotelController.addHotel(hotelName, rooms, deluxeRooms, execRooms); // From Model
                gui.setTotalHotels(hotelList.getHotels().size());
                
                updateHotelList();
                break;
            
            case "CLEAR_HOTEL":
                updateCreateHotel();
                break;    

            case "SELECT_HOTEL":

                updateHotelView();
                break;

            case "ROOM_DATE_AVAIL":
                System.out.println("Room Date Avail DEBUG");
                hotelController.updateLowRoomDateAvailList();
                break;

            case "MANAGE_HOTELS":
                updateHotelList();
                break;

            case "FINALIZE_BOOKING":
                hotelController.bookRoomForSelectedHotel();
                updateRoomBooking();
                break;
   
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedHotelIndex;
        if (!e.getValueIsAdjusting()) {
            selectedHotelIndex = gui.getHotelJList().getSelectedIndex();
            if (selectedHotelIndex >= 0) {
                Hotel hotel = gui.getHotelListModel().getElementAt(selectedHotelIndex);
                int totalRooms = hotel.getRooms().size() + 
                                 hotel.getDeluxeRooms().size() + hotel.getExecRooms().size();
                gui.setSelectedHotelName(hotel.getName());
                gui.setSelectedHotelRoomSize(hotel.getRooms().size());
                gui.setSelectedHotelDeluxeRoomSize(hotel.getDeluxeRooms().size());
                gui.setSelectedHotelExecRoomSize(hotel.getExecRooms().size());
                gui.setTotalHotelEarnings(hotel.calculateEstimatedEarnings(hotel));
                
                gui.setDisplayInfoAndBooking(totalRooms, selectedHotelIndex);
            }
        }
    }

    public class HotelController {
        private HotelList hotelList;
        private GUI gui;
        private Hotel selectedHotel;

        private int selectedHotelIndex;
        private final String STRING_EMPTY = "";
    
        public HotelController(HotelList hotelList, GUI gui) {
            this.hotelList = hotelList;
            this.gui = gui;
        }
    
        public void addHotel(String hotelName, int rooms, int deluxeRooms, int execRooms) {
            int totalRooms;
            if (hotelList.sameHotelName(hotelName)) {
                JOptionPane.showMessageDialog(gui, "Hotel with the same name already exists", 
                                              "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                totalRooms = rooms + deluxeRooms + execRooms;
                hotelList.addHotel(hotelName, rooms, deluxeRooms, execRooms);
                JOptionPane.showMessageDialog(gui, "Hotel " + hotelName + " (" + totalRooms + " rooms)" 
                                              + " created successfully!",
                                              "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }

    public void updateLowRoomDateAvailList() {
        selectedHotel   = hotelList.getHotels().get(gui.getSelectedHotelIndex());
        int date = Integer.parseInt(gui.getRoomDateAvailFieldText(gui.getSelectedHotelIndex()));


        gui.getRoomDateAvailListModel().clear();
        for (Room room : selectedHotel.getRooms()) {
            // if (date >=)
                gui.getRoomDateAvailListModel().addElement(room);
        }
        for (DeluxeRoom deluxeRoom : selectedHotel.getDeluxeRooms()) {

                gui.getRoomDateAvailListModel().addElement(deluxeRoom);
        }
        for (ExecutiveRoom execRoom : selectedHotel.getExecRooms()) {

                gui.getRoomDateAvailListModel().addElement(execRoom);
        }
    }

    public void updateHotelList(ArrayList<Hotel> hotels) {
        gui.getHotelListModel().clear();
        for (Hotel hotel : hotels) {
            gui.getHotelListModel().addElement(hotel);
        }
        gui.setCreateBtnEnabled(false);
    }
    
    public void bookRoomForSelectedHotel() {
        Room room;
        DeluxeRoom deluxeRoom;
        ExecutiveRoom execRoom;
        String checkIn, checkOut, guestName, discountCode;
        int roomToUse, deluxeRoomToUse, execRoomToUse;

        guestName = gui.getGuestName();
        checkIn = gui.getCheckInDate();
        checkOut = gui.getCheckOutDate();
        discountCode = gui.getDiscountCode();

        deluxeRoom = null;
        execRoom = null;

        if (guestName.equals(STRING_EMPTY)) {
            JOptionPane.showMessageDialog(gui, "Please enter guest name.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        else if (checkIn.equals(STRING_EMPTY) || checkOut.equals(STRING_EMPTY)) {
            JOptionPane.showMessageDialog(gui, "Please enter check-in AND check-out dates.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (checkIn.equals(checkOut)) {
            JOptionPane.showMessageDialog(gui, "Check-in and check-out dates cannot be the same.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (Integer.parseInt(checkOut) < Integer.parseInt(checkIn)) {
            JOptionPane.showMessageDialog(gui, "Check-out date cannot be before check-in date.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        selectedHotel   = hotelList.getHotels().get(gui.getSelectedHotelIndex());

        roomToUse       = selectedHotel.latestRoomNoReservation();
        room            = selectedHotel.getRooms().get(roomToUse);

        if (selectedHotel.getDeluxeRooms().size() > 0) {
            deluxeRoomToUse = selectedHotel.latestDeluxeRoomNoReservation();
            deluxeRoom      = selectedHotel.getDeluxeRooms().get(deluxeRoomToUse);
        }
        if (selectedHotel.getExecRooms().size() > 0) {
            execRoomToUse   = selectedHotel.latestExecRoomNoReservation();
            execRoom        = selectedHotel.getExecRooms().get(execRoomToUse);
        }
        // Get the check-in and check-out dates from the GUI, run the booking logic from model
        bookRoom(selectedHotel, guestName, Integer.parseInt(checkIn), Integer.parseInt(checkOut), 
                    room, deluxeRoom, execRoom, discountCode);

        gui.setBaseRoomOcc(selectedHotel.totalStandardRoomsReserved());
        if (selectedHotel.getDeluxeRooms().size() > 0)
            gui.setDeluxeRoomOcc(selectedHotel.totalDeluxeRoomsReserved());
        if (selectedHotel.getExecRooms().size() > 0)
            gui.setExecRoomOcc(selectedHotel.totalExecutiveRoomsReserved());
        
            gui.setTotalHotelEarnings(selectedHotel.calculateEstimatedEarnings(selectedHotel));

        JOptionPane.showMessageDialog(gui, "Room booked successfully!", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void bookRoom(Hotel hotel, String guestName, int checkInDate, int checkOutDate, 
                            Room room, DeluxeRoom deluxeRoom, ExecutiveRoom execRoom,
                            String discountCode) {
        String roomType;

        roomType = gui.getSelectedRoomType();
        if (roomType.equals("Base Room")) {
            hotel.bookRoomInputInfo(room, room.getReservations(), guestName, 
                                    checkInDate, checkOutDate, discountCode);
        }
        if (roomType.equals("Deluxe Room")) {
            hotel.bookRoomInputInfo(deluxeRoom, deluxeRoom.getReservations(), guestName, 
                                    checkInDate, checkOutDate, discountCode);
        }
        if (roomType.equals("Executive Room")) {
            hotel.bookRoomInputInfo(execRoom, execRoom.getReservations(), guestName, 
                                    checkInDate, checkOutDate, discountCode);
        }
    }

}

    public class RoomController {
        private GUI gui;
    
        public RoomController(GUI gui) {
            this.gui = gui;
        }

    }


    
    // public ArrayList<Hotel> getHotels() {
    //     return hotelList.getHotels();
    // }

    @Override
    public void insertUpdate(DocumentEvent e) {
        
    }

    @Override
    public void removeUpdate(DocumentEvent e) {

    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }
}
