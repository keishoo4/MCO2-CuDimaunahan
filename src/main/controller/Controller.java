// TODO: Update HotelView after booking (For estimated earnings)

package controller;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.event.*;
import java.util.ArrayList;

import model.hotel.HotelList;
import model.hotel.Hotel;
import model.hotel.Room;
import model.hotel.Reservation;
import view.GUI;

public class Controller implements ActionListener, DocumentListener, ListSelectionListener {
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
        gui.updateHotelList(hotelList.getHotels());

    }

    public void updateCreateHotel() {
        gui.clearCreateHotel();
    }

    public void updateRoomBooking() {
        gui.clearBookingInfo();
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
                
                if (hotelName.isEmpty()) {
                    JOptionPane.showMessageDialog(gui, "Please enter a hotel name.", 
                                                  "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                hotelController.addHotel(hotelName, rooms, deluxeRooms, execRooms); // From Model
                
                updateHotelList();
                break;
            
            case "CLEAR_HOTEL":
                updateCreateHotel();
                break;    

            case "SELECT_HOTEL":

                updateHotelView();
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
                gui.setSelectedHotelName(hotel.getName());
                gui.setSelectedHotelRoomSize(hotel.getRooms().size());
                gui.setSelectedHotelDeluxeRoomSize(hotel.getDeluxeRooms().size());
                gui.setSelectedHotelExecRoomSize(hotel.getExecRooms().size());
                gui.setTotalHotelEarnings(hotel.calculateEstimatedEarnings(hotel));


                gui.setDisplayInfoAndBooking();
            }
        }

    }


    public class HotelController {
        private HotelList hotelList;
        private Hotel hotel;
        private GUI gui;

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

        public void bookRoomForSelectedHotel() {
            Hotel selectedHotel;
            String checkIn, checkOut, guestName;
            int roomToUse, deluxeRoomToUse, execRoomToUse;

            guestName = gui.getGuestName();
            checkIn = gui.getCheckInDate();
            checkOut = gui.getCheckOutDate();

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

            selectedHotel = hotelList.getHotels().get(gui.getSelectedHotelIndex());
            roomToUse       = selectedHotel.latestRoomNoReservation();
            deluxeRoomToUse = selectedHotel.latestDeluxeRoomNoReservation();
            execRoomToUse   = selectedHotel.latestExecRoomNoReservation();
            // Get the check-in and check-out dates from the GUI, run the booking logic from model
            RoomController roomController = new RoomController(gui);
            roomController.bookRoom();

            JOptionPane.showMessageDialog(gui, "Room booked successfully!", 
                                          "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public class RoomController {
        private GUI gui;
    
        public RoomController(GUI gui) {
            this.gui = gui;
        }
    
        public void bookRoom() {
            String roomType;

            roomType = gui.getSelectedRoomType();
            if (roomType.equals("Base Room")) {
                
            }
            if (roomType.equals("Deluxe Room")) {

            }
            if (roomType.equals("Executive Room")) {

            }
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
