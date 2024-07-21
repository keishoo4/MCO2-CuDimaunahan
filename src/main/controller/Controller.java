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

    public Controller(HotelList hotelList, GUI gui) {
        this.hotelList = hotelList;
        this.gui = gui;
        this.hotelController = new HotelController(hotelList, gui);

        // updateView();

        gui.setActionListener(this);
        gui.setDocumentListener(this);
    }

    public void updateHotelList() {
        gui.updateHotelList(hotelList.getHotels());
    }

    public void updateClearTextFields() {
        gui.clearTextFields();
    }

    public void updateRoomBooking() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
    
        switch (command) {
            case "ADD_HOTEL":
                String hotelName = gui.getHotelName().trim();
                int rooms = gui.getSliderValue();
                if (hotelName.isEmpty()) {
                    JOptionPane.showMessageDialog(gui, "Please enter a hotel name.", 
                                                  "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                hotelController.addHotel(hotelName, rooms);
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
    
        public void addHotel(String hotelName, int rooms) {
            // Implementation for adding a hotel
            if (hotelList.sameHotelName(hotelName)) {
                JOptionPane.showMessageDialog(gui, "Hotel with the same name already exists", 
                                              "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                hotelList.addHotel(hotelName, rooms);
                JOptionPane.showMessageDialog(gui, "Hotel " + hotelName + " (" + rooms + " rooms)" 
                                              + " created successfully!",
                                              "Success", JOptionPane.INFORMATION_MESSAGE);
            }
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
