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
                                   MouseListener,
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
        // gui.updateLowLevelReservationNum();
    }

    public void updateHotelView() {
        gui.setSelectedHotelName(hotel.getName());
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        int hotelIndex = gui.getSelectedHotelIndex();
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
                updateCreateHotel();
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

            case "RESERVATION_INFO_SHOW":
                System.out.println("Reservation Info Show DEBUG");
                hotelController.updateLowLevelReservationInfo();
                gui.updateLowLevelReservationInfo();
                break;

            case "CHANGE_HOTEL_NAME":
                hotelController.changeHotelName();
                updateHotelList();
                break;

            case "ADD_BASE_ROOMS":
                hotel = hotelList.getHotels().get(hotelIndex);
                hotelController.addBaseRooms();
                gui.updateManageAllRooms();
                gui.updateBookingRelated();
                updateRoomsToRemove(hotel);
                break;

            case "ADD_DELUXE_ROOMS":
                hotel = hotelList.getHotels().get(hotelIndex);
                hotelController.addDeluxeRooms();
                gui.updateManageAllRooms();
                gui.updateBookingRelated();
                updateRoomsToRemove(hotel);
                break;

            case "ADD_EXEC_ROOMS":
                hotel = hotelList.getHotels().get(hotelIndex);
                hotelController.addExecRooms();
                gui.updateManageAllRooms();
                gui.updateBookingRelated();
                updateRoomsToRemove(hotel);
                break;

            case "REMOVE_ROOM":
                hotel = hotelList.getHotels().get(hotelIndex);
                System.out.println("Current Hotel  " + hotel.getName()); // DEBUGGING
                hotelController.removeRoom();
                updateRoomsToRemove(hotel);
                break;

            case "UPDATE_ROOM_PRICE":
                hotelController.updateRoomsPrice();
                gui.updateBookingRelated();
                break;

            case "REMOVE_HOTEL":
                hotelController.removeHotel();
                updateHotelList();
                break;



            case "FINALIZE_BOOKING":
                hotelController.bookRoomForSelectedHotel();
                updateRoomBooking();
                updateRoomsToRemove(hotel);
                break;
        }
    }

    public class HotelController {
        private HotelList hotelList;
        private GUI gui;
        private Hotel selectedHotel;
        private Room room;
        private DeluxeRoom deluxeRoom;
        private ExecutiveRoom execRoom;
        private ArrayList<Room> baseRooms;
        private ArrayList<DeluxeRoom> deluxeRooms;
        private ArrayList<ExecutiveRoom> execRooms;
        private ArrayList<Reservation> reservations;

        private double roomPrice;
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
        } 
        else {
            totalRooms = rooms + deluxeRooms + execRooms;
            hotelList.addHotel(hotelName, rooms, deluxeRooms, execRooms);
            JOptionPane.showMessageDialog(gui, "Hotel " + hotelName + " (" + totalRooms + " rooms)" 
                                            + " created successfully!",
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void removeHotel() {
        selectedHotel = hotelList.getHotels().get(gui.getSelectedHotelIndex());

        if (selectedHotel.totalRoomsReserved() > 0) {
            JOptionPane.showMessageDialog(gui, "Cannot remove hotel while rooms are reserved.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int response = JOptionPane.showConfirmDialog(gui, "Remove hotel '" + hotel.getName() + "'?", 
        "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                int secondResponse = JOptionPane.showConfirmDialog(gui, "CONFIRM REMOVAL OF '" + hotel.getName() + "'?", 
                "Confirm Again", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (secondResponse == JOptionPane.NO_OPTION) {
                    return;
                }
            }   

        hotelList.getHotels().remove(gui.getSelectedHotelIndex());
        gui.setTotalHotels(hotelList.getHotels().size());
        gui.setCurrentHotelName(STRING_EMPTY);
        gui.setSelectedHotelIndex(-1);
        gui.getBackToFrontManageBtn().doClick();
        gui.getCloseTabBtn().doClick();
        gui.updateManageHotel();
        updateHotelList(hotelList.getHotels());

    }

    public void updateRoomsPrice() {
        selectedHotel = hotelList.getHotels().get(gui.getSelectedHotelIndex());

        if (selectedHotel.totalRoomsReserved() > 0) {
            JOptionPane.showMessageDialog(gui, "Cannot change price while rooms are reserved.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (gui.getUpdateRoomPriceField().equals("")) {
            JOptionPane.showMessageDialog(gui, "Please enter a new price.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double newPrice = Double.parseDouble(gui.getUpdateRoomPriceField());
        if (newPrice < 100) {
            JOptionPane.showMessageDialog(gui, "New Price cannot be lower than 100.0.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int response = JOptionPane.showConfirmDialog(gui, "Change base price of ALL rooms to '" + newPrice + "'?", 
                 "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.NO_OPTION) {
                return;
            }

        selectedHotel.setNewPrice(newPrice);
        selectedHotel.changeRoomPrice(newPrice);
        gui.updateLowLevelRoomInfo();
        gui.updateBookingRelated();
    }

    public void removeRoom() {
        selectedHotel = hotelList.getHotels().get(gui.getSelectedHotelIndex());
        String roomName = gui.getSelectedRoomForRemoval();
        int response = JOptionPane.showConfirmDialog(gui, "Remove room '" + roomName + "'?", 
        "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.NO_OPTION) {
                return;
            }
        selectedHotel.removeRoom(roomName);
        gui.updateLowLevelReservationInfo();
        gui.updateManageAllRooms();
        gui.updateBookingRelated();
        updateRoomsToRemove(selectedHotel);

    }

    public void addBaseRooms() {
        selectedHotel = hotelList.getHotels().get(gui.getSelectedHotelIndex());

        if (gui.getAddBaseRoomsField().equals("")) {
            JOptionPane.showMessageDialog(gui, "Please enter a number of rooms to add.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int toAddRooms = Integer.parseInt(gui.getAddBaseRoomsField());
        int response = JOptionPane.showConfirmDialog(gui, "Add '" + toAddRooms + "' to existing " + 
                        selectedHotel.getRooms().size() + " rooms?", 
        "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.NO_OPTION) {
                return;
            }

        selectedHotel.addRooms(toAddRooms, 0, 0);
        gui.setSelectedHotelRoomSize(hotel.getRooms().size());
        gui.setTotalRooms(hotel.getRooms().size() + 
            hotel.getDeluxeRooms().size() + hotel.getExecRooms().size());
    
    }

    public void addDeluxeRooms() {
        selectedHotel = hotelList.getHotels().get(gui.getSelectedHotelIndex());

        if (gui.getAddDeluxeRoomsField().equals("")) {
            JOptionPane.showMessageDialog(gui, "Please enter a number of deluxe rooms to add.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int toAddDeluxeRooms = Integer.parseInt(gui.getAddDeluxeRoomsField());
        int response = JOptionPane.showConfirmDialog(gui, "Add '" + toAddDeluxeRooms + "' to existing " + 
                        selectedHotel.getDeluxeRooms().size() + " deluxe rooms?", 
        "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.NO_OPTION) {
                return;
            }

        selectedHotel.addRooms(0, toAddDeluxeRooms, 0);
        gui.setSelectedHotelDeluxeRoomSize(hotel.getDeluxeRooms().size());
        gui.setTotalRooms(hotel.getRooms().size() + 
            hotel.getDeluxeRooms().size() + hotel.getExecRooms().size());
   
    }

    public void addExecRooms() {
        selectedHotel = hotelList.getHotels().get(gui.getSelectedHotelIndex());

        if (gui.getAddExecRoomsField().equals("")) {
            JOptionPane.showMessageDialog(gui, "Please enter a number of executive rooms to add.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int toAddExecRooms = Integer.parseInt(gui.getAddExecRoomsField());
        int response = JOptionPane.showConfirmDialog(gui, "Add '" + toAddExecRooms + "' to existing " + 
                        selectedHotel.getExecRooms().size() + " executive rooms?", 
        "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.NO_OPTION) {
                return;
            }

        selectedHotel.addRooms(0, 0, toAddExecRooms);
        gui.setSelectedHotelExecRoomSize(hotel.getExecRooms().size());
        gui.setTotalRooms(hotel.getRooms().size() + 
            hotel.getDeluxeRooms().size() + hotel.getExecRooms().size());
    }

    public void changeHotelName() {
        if (gui.getChangeHotelNameFieldText().equals("")) {
            JOptionPane.showMessageDialog(gui, "Please enter a new hotel name.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        hotel = hotelList.getHotels().get(gui.getSelectedHotelIndex());

        int response = JOptionPane.showConfirmDialog(gui, "Change '" + hotel.getName() 
                       + "'' to '" + gui.getChangeHotelNameFieldText() + "'?", 
        "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.NO_OPTION) {
                return;
            }
        
        hotel.setName(gui.getChangeHotelNameFieldText());
    }

    public void updateLowLevelReservationInfo() {
        int reservationNum;
        String guestName, checkInDate, checkOutDate, discountCode;
        double bookingPrice;

        if (gui.getReservationInfoTextField().equals("")) {
            JOptionPane.showMessageDialog(gui, "Please enter a reservation number.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        gui.setReservationTotal(reservations.size());
        // gui.updateLowLevelReservationNum();
        reservationNum = Integer.parseInt(gui.getReservationInfoTextField()) - 1;
        
        reservation = reservations.get(reservationNum);
        guestName = reservation.getGuestName();
        checkInDate = Integer.toString(reservation.getCheckInDate());
        checkOutDate = Integer.toString(reservation.getCheckOutDate());
        discountCode = reservation.getDiscountCode();
        bookingPrice = (hotel.fillDates(roomPrice, Integer.parseInt(checkInDate), 
                       Integer.parseInt(checkOutDate), 
                       discountCode));



        gui.setGuestName(reservation.getGuestName());
        gui.setCheckInDate(Integer.toString(reservation.getCheckInDate()));
        gui.setCheckOutDate(Integer.toString(reservation.getCheckOutDate()));
        if (selectedHotel.getDiscountCode(reservation.getDiscountCode()) != 1.0)
            gui.setDiscountCode(reservation.getDiscountCode());
        else
            gui.setDiscountCode("N/A");
            
        gui.setBookingPrice(bookingPrice);
    }

    public void updateLowLevelRoomInfo() {
        int roomNum, roomOccupancy, reservationNum;
        String roomName, roomPrice;

        if (gui.getRoomInfoFieldText().equals("")) {
            JOptionPane.showMessageDialog(gui, "Please enter a room number.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

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
            setRoomPrice(Double.parseDouble(roomPrice));
            gui.setRoomOccupancy(baseRooms.get(roomNum).getVacancyPeriods());
            reservations = baseRooms.get(roomNum).getReservations();
        } 
        else {
            roomNum -= baseRooms.size();
            if (roomNum < deluxeRooms.size()) {
                roomName = deluxeRooms.get(roomNum).getName();
                roomPrice = Double.toString(deluxeRooms.get(roomNum).getPricePerNight());
                setRoomPrice(Double.parseDouble(roomPrice));
                gui.setRoomOccupancy(deluxeRooms.get(roomNum).getVacancyPeriods());
                reservations = deluxeRooms.get(roomNum).getReservations();
            } 
            else {
                roomNum -= deluxeRooms.size();
                if (roomNum < execRooms.size()) {
                    roomName = execRooms.get(roomNum).getName();
                    roomPrice = Double.toString(execRooms.get(roomNum).getPricePerNight());
                    setRoomPrice(Double.parseDouble(roomPrice));
                    gui.setRoomOccupancy(execRooms.get(roomNum).getVacancyPeriods());
                    reservations = execRooms.get(roomNum).getReservations();
                } 
                else {
                    throw new IndexOutOfBoundsException("Invalid room number");
                }
            }
        }
        gui.setRoomName(roomName);
        gui.setRoomPrice(roomPrice);

        setReservations(reservations);
        gui.updateLowLevelReservationNum();
    }

    public void setRoomPrice(double roomPrice) {
        this.roomPrice = roomPrice;
    }
    public void setReservations(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
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

        availRooms = selectedHotel.removableRooms();
        availDeluxeRooms = selectedHotel.removableDeluxeRooms();
        availExecRooms = selectedHotel.removableExecRooms();

        gui.setBaseRoomOcc(selectedHotel.totalBaseRoomsReserved());
        if (selectedHotel.getDeluxeRooms().size() > 0)
            gui.setDeluxeRoomOcc(selectedHotel.totalDeluxeRoomsReserved());
        if (selectedHotel.getExecRooms().size() > 0)
            gui.setExecRoomOcc(selectedHotel.totalExecRoomsReserved());
        
        gui.setTotalHotelEarnings(selectedHotel.calculateMonthlyEarnings());
        gui.setReservationTotal(selectedHotel.getTotalHotelReservations());
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

            gui.getRoomDateAvailListModel().clear();
            gui.enableManageHotelBtn();
            setCurrentHotelInfo(hotel);
        }
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
        String selectedTabTitle = gui.getLowerLeftTabbedPane()
               .getTitleAt(gui.getLowerLeftTabbedPane().getSelectedIndex());
        if (!GUI.HOTEL_LIST_TAB_NAME.equals(selectedTabTitle)) {
            Integer hotelListIndex = gui.getHotelTabIndices().get(selectedTabTitle);
            if (hotelListIndex != null) {
                gui.setSelectedHotelIndex(hotelListIndex);
                System.out.println("Current Hotel List Index: " + gui.getSelectedHotelIndex());

                hotel = hotelList.getHotels().get(hotelListIndex);
                setCurrentHotelInfo(hotel);
            } 
            else {
                System.out.println("Selected tab not found in hotelTabIndices map");
            }
        } 
        else {
            System.out.println("Hotel List tab selected");
        }        
    }

    public void setCurrentHotelInfo(Hotel hotel) {
        gui.setCurrentHotelName(hotel.getName());

        int totalRooms = hotel.getRooms().size() + 
                            hotel.getDeluxeRooms().size() + hotel.getExecRooms().size();
        gui.setSelectedHotelName(hotel.getName());
        gui.setSelectedHotelRoomSize(hotel.getRooms().size());
        gui.setSelectedHotelDeluxeRoomSize(hotel.getDeluxeRooms().size());
        gui.setSelectedHotelExecRoomSize(hotel.getExecRooms().size());
        gui.setTotalHotelEarnings(hotel.calculateMonthlyEarnings());
        
        gui.updateRoomInfoFieldFormatter(totalRooms);
        gui.setTotalRooms(totalRooms);

        gui.updateBookingRelated();
        gui.updateLowLevelReservationInfo();

        gui.updateManageHotel();
        gui.updateManageAllRooms();
        updateRoomsToRemove(hotel);
        
        // updateReservationToRemove(hotel);
    }

    public void updateReserationsToRemove(Hotel hotel) {
        ArrayList<String> allReservations = new ArrayList<String>();
        for (Room room : hotel.getRooms()) {
            for (Reservation reservation : room.getReservations()) {
                allReservations.add(reservation.getGuestName() + " "
                                + reservation.getRoom().getName() + " "
                                + reservation.getCheckInDate() + "-"
                                + reservation.getCheckOutDate());
            }
        }
        for (DeluxeRoom room : hotel.getDeluxeRooms()) {
            for (Reservation reservation : room.getReservations()) {
                allReservations.add(reservation.getGuestName());
            }
        }
        for (ExecutiveRoom room : hotel.getExecRooms()) {
            for (Reservation reservation : room.getReservations()) {
                allReservations.add(reservation.getGuestName());
            }
        }

        // Convert the list to a String[] array
        String[] allReservationsArray = new String[allReservations.size()];
        allReservationsArray = allReservations.toArray(allReservationsArray);

        // Update reservations for removal
        gui.updateReservationsForRemoval(allReservationsArray);
    }

    public void updateRoomsToRemove(Hotel hotel) {
        ArrayList<String> allRooms = new ArrayList<String>();
        for (Room room : hotel.getRooms()) {
            if (room.getBookStatus() == false)
                allRooms.add(room.getName());
        }
        for (DeluxeRoom room : hotel.getDeluxeRooms()) {
            if (room.getBookStatus() == false)
                allRooms.add(room.getName());
        }
        for (ExecutiveRoom room : hotel.getExecRooms()) {
            if (room.getBookStatus() == false)
                allRooms.add(room.getName());
        }

        // Convert the list to a String[] array
        String[] allRoomsArray = new String[allRooms.size()];
        allRoomsArray = allRooms.toArray(allRoomsArray);

        // Update rooms for removal
        gui.updateRoomsForRemoval(allRoomsArray);        
    }
}
