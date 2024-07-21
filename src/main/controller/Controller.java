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

    public Controller(HotelList hotelList, GUI gui) {
        this.hotelList = hotelList;
        this.gui = gui;

        // updateView();

        gui.setActionListener(this);
        gui.setDocumentListener(this);
    }

    public void updateView() {
        gui.updateHotelList(hotelList.getHotels());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String hotelName;
        int rooms;

        hotelName = gui.getHotelName().trim();
        rooms = gui.getSliderValue();

        // Check if hotelName is empty
        if (hotelName.isEmpty()) {
            JOptionPane.showMessageDialog(gui, "Please enter a hotel name.", 
                                    "Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit the method early
        }

        if (hotelList.sameHotelName(hotelName)) {
            JOptionPane.showMessageDialog(gui, "Hotel with the same name already exists", 
                                          "Error", JOptionPane.ERROR_MESSAGE);
        } 
        else {
            hotelList.addHotel(hotelName, rooms);
            JOptionPane.showMessageDialog(gui, "Hotel " + hotelName + " (" + rooms + " rooms)" 
                                          + " created successfully!",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }

        updateView();

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
