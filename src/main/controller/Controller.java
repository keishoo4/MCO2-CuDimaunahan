// TODO: Update HotelView after booking (For estimated earnings)

package controller;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import model.hotel.HotelList;
import model.hotel.Hotel;
import model.hotel.Room;
import model.hotel.DeluxeRoom;
import model.hotel.ExecutiveRoom;
import model.hotel.Reservation;
import view.GUI;

public class Controller implements ActionListener, DocumentListener, 
                                   /*ListSelectionListener ,*/ MouseListener,
                                   ChangeListener {
    private HotelList hotelList;
    private Hotel hotel;
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
        // gui.setListActionListener(this);
        gui.setMouseListener(this);
        gui.setChangeListener(this);
    }

    public void updateHotelList() {
        hotelController.updateHotelList(hotelList.getHotels());
    }

    public void updateCreateHotel() {
        gui.clearCreateHotel();
    }

    public void updateRoomBooking() {
        gui.clearBookingInfo();
        gui.updateBookingRelated();
        gui.updateLowLevelReservationInfo();
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
                gui.updateComboBoxItems(deluxeRooms, execRooms);
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

            case "ROOM_INFO_SHOW":
                System.out.println("Room Info Show DEBUG");
                hotelController.updateLowLevelRoomInfo();
                gui.updateLowLevelRoomInfo();
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

    public class HotelController {
        private HotelList hotelList;
        private GUI gui;
        private Hotel selectedHotel;
        private ArrayList<Room> baseRooms;
        private ArrayList<DeluxeRoom> deluxeRooms;
        private ArrayList<ExecutiveRoom> execRooms;

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

public void updateLowLevelRoomInfo() {
    int roomNum, roomOccupancy;
    String roomName, roomPrice;

    selectedHotel = hotelList.getHotels().get(gui.getSelectedHotelIndex());
    roomNum = Integer.parseInt(gui.getRoomInfoFieldText()) - 1; // -1 for indexing

    roomName = "";
    roomPrice = "";

    baseRooms = selectedHotel.getRooms();
    deluxeRooms = selectedHotel.getDeluxeRooms();
    execRooms = selectedHotel.getExecRooms();

    if (roomNum < baseRooms.size()) {
        roomName = baseRooms.get(roomNum).getName();
        roomPrice = Double.toString(baseRooms.get(roomNum).getPricePerNight());
        gui.setRoomOccupancy(baseRooms.get(roomNum).getVacancyPeriods());
    } else {
        roomNum -= baseRooms.size();
        if (roomNum < deluxeRooms.size()) {
            roomName = deluxeRooms.get(roomNum).getName();
            roomPrice = Double.toString(deluxeRooms.get(roomNum).getPricePerNight());
            gui.setRoomOccupancy(deluxeRooms.get(roomNum).getVacancyPeriods());
        } 
        else {
            roomNum -= deluxeRooms.size();
            if (roomNum < execRooms.size()) {
                roomName = execRooms.get(roomNum).getName();
                roomPrice = Double.toString(execRooms.get(roomNum).getPricePerNight());
                gui.setRoomOccupancy(execRooms.get(roomNum).getVacancyPeriods());
            } 
            else {
                throw new IndexOutOfBoundsException("Invalid room number");
            }
        }
    }
    gui.setRoomName(roomName);
    gui.setRoomPrice(roomPrice);
}

    public void updateLowRoomDateAvailList() {
        selectedHotel     = hotelList.getHotels().get(gui.getSelectedHotelIndex());
        String dateString = gui.getRoomDateAvailFieldText();

        if (dateString.equals(STRING_EMPTY)) {
            JOptionPane.showMessageDialog(gui, "Please enter a date.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int date = Integer.parseInt(dateString);
        
        gui.getRoomDateAvailListModel().clear();
        for (Room room : selectedHotel.getRooms()) {
            room.setTempDate(date);
            gui.getRoomDateAvailListModel().addElement(room);
        }
        for (DeluxeRoom deluxeRoom : selectedHotel.getDeluxeRooms()) {
            deluxeRoom.setTempDate(date);
            gui.getRoomDateAvailListModel().addElement(deluxeRoom);
        }
        for (ExecutiveRoom execRoom : selectedHotel.getExecRooms()) {
            execRoom.setTempDate(date);
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
        int roomToUse, deluxeRoomToUse, execRoomToUse,
            availRooms, availDeluxeRooms, availExecRooms;

        guestName = gui.getGuestName();
        checkIn = gui.getCheckInDate();
        checkOut = gui.getCheckOutDate();
        discountCode = gui.getDiscountCode();
        selectedHotel   = hotelList.getHotels().get(gui.getSelectedHotelIndex());

        availRooms = selectedHotel.removableRooms();
        availDeluxeRooms = selectedHotel.removableDeluxeRooms();
        availExecRooms = selectedHotel.removableExecRooms();

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

        if (selectedHotel.sameGuestName(guestName)) {
            JOptionPane.showMessageDialog(gui, "Guest with the same name already exists.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

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
            gui.setExecRoomOcc(selectedHotel.totalExecRoomsReserved());
        
        gui.setTotalHotelEarnings(selectedHotel.calculateEstimatedEarnings());
        gui.setRoomReservationTotal(selectedHotel.getTotalHotelReservations());
        gui.updateComboBoxItems(availDeluxeRooms, availExecRooms);

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

    @Override
    public void insertUpdate(DocumentEvent e) {
        
    }

    @Override
    public void removeUpdate(DocumentEvent e) {

    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON1) {
            return;  // Ignore if it's not a left-click
        }
    
        int selectedHotelIndex = gui.getHotelJList().locationToIndex(e.getPoint());
    
        if (selectedHotelIndex >= 0) {
            Object item = gui.getHotelListModel().getElementAt(selectedHotelIndex);
            System.out.println("Clicked on: " + item + " at index " + selectedHotelIndex); // DEBUGGING
            gui.setSelectedHotelIndex(selectedHotelIndex);
            hotel = hotelList.getHotels().get(selectedHotelIndex);
            gui.setCurrentHotelName(hotel.getName());

            gui.getRoomDateAvailListModel().clear(); // Reset List for every different hotel
    
            // Transfer the logic from valueChanged
            hotel = gui.getHotelListModel().getElementAt(selectedHotelIndex);
            int totalRooms = hotel.getRooms().size() + 
                             hotel.getDeluxeRooms().size() + hotel.getExecRooms().size();
            gui.setSelectedHotelName(hotel.getName());
            gui.setSelectedHotelRoomSize(hotel.getRooms().size());
            gui.setSelectedHotelDeluxeRoomSize(hotel.getDeluxeRooms().size());
            gui.setSelectedHotelExecRoomSize(hotel.getExecRooms().size());
            gui.setTotalHotelEarnings(hotel.calculateEstimatedEarnings());
            
            gui.updateRoomInfoFieldFormatter(totalRooms);
            gui.setTotalRooms(totalRooms);

            gui.updateBookingRelated();
        }
        gui.updateManageHotel();



        
        gui.revalidate();
        gui.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        String selectedTabTitle = gui.getLowerLeftTabbedPane().getTitleAt(gui.getLowerLeftTabbedPane().getSelectedIndex());
        if (!GUI.HOTEL_LIST_TAB_NAME.equals(selectedTabTitle)) {
            Integer hotelListIndex = gui.getHotelTabIndices().get(selectedTabTitle);
            if (hotelListIndex != null) {
                gui.setSelectedHotelIndex(hotelListIndex);
                System.out.println("Current Hotel List Index: " + gui.getSelectedHotelIndex());

                hotel = hotelList.getHotels().get(hotelListIndex);
                gui.setCurrentHotelName(hotel.getName());
                gui.updateManageHotel();
            } 
            else {
                System.out.println("Selected tab not found in hotelTabIndices map");
            }
        } 
        else {
            System.out.println("Hotel List tab selected");
        }        
    }
}
