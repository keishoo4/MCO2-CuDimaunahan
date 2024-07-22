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

public class Controller implements ActionListener, DocumentListener {
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
        this.roomController = new RoomController(hotel, room, gui);

        // updateView();

        gui.setActionListener(this);
        gui.setDocumentListener(this);
    }

    public void updateHotelList() {
        gui.updateHotelList(hotelList.getHotels());
    }

    public void updateClearTextFields() {
        gui.clearTextFields();
        gui.setCreateBtnEnabled(false);
    }

    public void updateRoomBooking() {

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
                updateClearTextFields();
                break;    

            case "BOOK_ROOM":
                
                updateRoomBooking();
                break;
        }
    }

    public class HotelController {
        private HotelList hotelList;
        private GUI gui;
    
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
    }

    public class RoomController {
        private Hotel hotel;
        private Room room;
        private GUI gui;
    
        public RoomController(Hotel hotel, Room room, GUI gui) {
            this.hotel = hotel;
            this.room = room;
            this.gui = gui;
        }
    
        public void bookRoom() {
            // Implementation for booking a room
        }
    }


    
    public ArrayList<Hotel> getHotels() {
        return hotelList.getHotels();
    }

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
