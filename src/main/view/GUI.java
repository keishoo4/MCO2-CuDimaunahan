// TODO: Impelement reservation numbers properly
package view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import java.awt.*;
import javax.swing.event.*;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import java.awt.event.*;
import java.text.NumberFormat;
import java.text.ParseException;

import model.hotel.Hotel;
import model.hotel.Room;
import model.hotel.Reservation;

import java.util.*;

public class GUI extends JFrame {
    private JFrame mainFrame, bookingFrame;
    private JPanel upperPanel, lowerPanel,
                   hotelHighInfoPanel, hotelLowInfoPanel, 
                   upperLeftCreatePanel, upperRightManagePanel, lowerRightFiller, 
                   guestNamePanel, checkInPanel, checkOutPanel, discountCodePanel, roomTypePanel, 
                   discountCodeFlowPanel, roomDateAvailPanel, 
                   changeHotelNamePanel1, addRoomsPanel2, removeRoomPanel3, updateRoomPricePanel4,
                   updateDateModifierPanel5, removeReservationPanel6, removeHotelPanel7;
    private JLabel guestLabel, checkInLabel, checkOutLabel, discountCodeLabel, earningsValueLabel,
                   totalBaseRoomsLabel, totalDeluxeRoomsLabel, totalExecRoomsLabel,
                   roomDateAvailLabel, roomInfoLabel, reservationInfoLabel,
                   roomNameLabel, roomPriceLabel, roomOccupancyLabel, roomOccupancyValueLabel,
                   reservationInfoReminderLabel, reservationGuestLabel, reservationCheckInLabel, 
                   reservationCheckOutLabel, reservationDiscountCodeLabel, reservationBookingPriceLabel,
                   baseRoomsLabel, deluxeRoomsLabel, execRoomsLabel;

    private JTextField hotelNameField, guestNameField, discountCodeField,
                       changeHotelNameField, removeRoomField, updateRoomPriceField;
    private JFormattedTextField checkInField, checkOutField, 
                                roomDateAvailField, roomInfoField, reservationInfoField,
                                addBaseRoomsField, addDeluxeRoomsField, addExecRoomsField;
    private JButton createHotelBtn, finalizeBookingBtn, clearBtn, 
                    checkAvailBtn, roomDateAvailBtn, roomInfoBtn, reservationInfoBtn,
                    manageHotelBtn, backToFrontManageBtn, changeHotelNameBtn, 
                    addRoomsBtn, addDeluxeRoomsBtn, addExecRoomsBtn, 
                    removeRoomBtn, updateRoomPriceBtn, updateDateModifierBtn, removeReservationBtn,
                    removeHotelBtn;
    private JSlider slider1, slider2, slider3; // SLIDER IS TEMPORARY
    private JComboBox<String> comboBox, roomsWithoutReservationsComboBox;
    private JTabbedPane lowerLeftTabbedPane;
    private DefaultListModel<Hotel> hotelListModel;
    private DefaultListModel<Room> roomDateAvailListModel;
    private DefaultListModel<String> reservationPriceBreakdownListModel;
    private JList<Hotel> hotelJList;
    private JList<Room> roomDateAvailList;
    private JList<String> reservationPriceBreakdownList;
    private JScrollPane roomDateAvailScrollPane;

    private Font currentFont, newFont;
    private NumberFormatter numberFormatter;
    private Dimension preferredSize;

    private Map<String, Integer> hotelTabIndices = new HashMap<String, Integer>();


    private int selectedHotelIndex = -1;
    private String selectedHotelName, currentHotelName, currentTabName,
                   roomName = "", 
                   roomPrice = "", 
                   roomOccupancy = "",
                   guestName = "",
                   checkInDate = "",
                   checkOutDate = "",
                   discountCode = "";

    private int totalHotels = 0, 
                totalRooms, selectedHotelRoomSize, selectedHotelDeluxeRooms, selectedHotelExecRooms,
                reservationTotal = 0,
                baseRoomOcc = 0, deluxeRoomOcc = 0, execRoomOcc = 0,
                remainingBaseRooms, maxDeluxeRooms, remainingDeluxeRooms, 
                maxExecRooms, remainingExecRooms, remainingRooms;

    private double totalHotelEarnings, bookingPrice;

    private final int MAX_TOTAL_ROOMS = 50;
    private final int RATIO_PARTS = 10;
    private final int DELUXE_RATIO = 3;
    private final int EXEC_RATIO = 2;
    public static final String HOTEL_LIST_TAB_NAME = "Hotel List";

    public GUI() {
        super("Hotel Reservation System");
        setSize(1150, 700); // Use 'this.' implicitly
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(null);
    
        setLayout(new GridLayout(2, 1));

        upperPanel = new JPanel(new GridLayout(1, 2));
        lowerPanel = new JPanel(new GridBagLayout());
        
        upperLeftCreatePanel = new JPanel();
        upperRightManagePanel = new JPanel();
        lowerLeftTabbedPane = new JTabbedPane();
        lowerRightFiller = new JPanel();

        upperRightManagePanel.setBackground(Color.LIGHT_GRAY);
        upperRightManagePanel.setOpaque(true);

        
        init(this); // Main program UI

        upperPanel.add(upperLeftCreatePanel);
        upperPanel.add(upperRightManagePanel);

        // Lower row configuration (80/20 split)
        gridBagLayoutConfig(lowerPanel, lowerLeftTabbedPane, 0, 0, 0.5, 1.0);
        gridBagLayoutConfig(lowerPanel, lowerRightFiller, 1, 0, 0.5, 1.0);  
        
        add(upperPanel);
        add(lowerPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void init(JFrame mainFrame) {
        upperLeftCreatePanel.setLayout(new BoxLayout(upperLeftCreatePanel, BoxLayout.Y_AXIS));

        // HOTEL NAME TEXT FIELD
        JPanel hotelNameAndRoomPanel = new JPanel();
        hotelNameAndRoomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel hotelNameLabel = new JLabel("Hotel Name: ");
        hotelNameField = new JTextField(20);
        hotelNameAndRoomPanel.add(hotelNameLabel);
        hotelNameAndRoomPanel.add(hotelNameField);
        upperLeftCreatePanel.add(hotelNameAndRoomPanel);

        setupHotelNameFieldFocusListener();

        // SLIDER 
        JPanel sliderRooms = new JPanel();
        sliderRooms.setLayout(new BoxLayout(sliderRooms, BoxLayout.Y_AXIS));

        // Normal Room Slider
        JPanel sliderPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sliderPanel1.setPreferredSize(new Dimension(300, 20));
        JLabel sliderValueLabel1 = new JLabel("          Base Room(s): 1");
        sliderPanel1.add(sliderValueLabel1);

        // Set fixed size for the label to prevent resizing
        Dimension labelSize = new Dimension(150, 20);
        sliderValueLabel1.setMinimumSize(labelSize);
        sliderValueLabel1.setPreferredSize(labelSize);
        sliderValueLabel1.setMaximumSize(labelSize);
        
        slider1 = new JSlider(1, MAX_TOTAL_ROOMS, 1);
        sliderPanel1.add(slider1);

        // Deluxe Room Slider
        JPanel sliderPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sliderPanel2.setPreferredSize(new Dimension(300, 20));
        JLabel sliderValueLabel2 = new JLabel("      Deluxe Room(s): 0");
        sliderPanel2.add(sliderValueLabel2);

        slider2 = new JSlider(0, 0, 0);
        sliderPanel2.add(slider2);

        Dimension labelSize2 = new Dimension(150, 20);
        sliderValueLabel2.setMinimumSize(labelSize2);
        sliderValueLabel2.setPreferredSize(labelSize2);
        sliderValueLabel2.setMaximumSize(labelSize2);

        // Executive Room Slider
        JPanel sliderPanel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sliderPanel3.setPreferredSize(new Dimension(300, 20));
        JLabel sliderValueLabel3 = new JLabel("Executive Room(s): 0");
        sliderPanel3.add(sliderValueLabel3);

        slider3 = new JSlider(0, 0, 0);
        sliderPanel3.add(slider3);

        Dimension labelSize3 = new Dimension(150, 20); // Adjust the width (160) as needed
        sliderValueLabel3.setMinimumSize(labelSize3);
        sliderValueLabel3.setPreferredSize(labelSize3);
        sliderValueLabel3.setMaximumSize(labelSize3);

        // Update all sliders
        Runnable updateSliders = () -> {
            int normalRooms = slider1.getValue();
            int deluxeRooms = slider2.getValue();
            int executiveRooms = slider3.getValue();
            
            int maxDeluxe    = Math.min((int) Math.floor(normalRooms * 0.6), 
                               MAX_TOTAL_ROOMS - normalRooms);
            int maxExecutive = Math.min((int) Math.floor(normalRooms * 0.4), 
                               MAX_TOTAL_ROOMS - normalRooms - deluxeRooms);
            
            slider2.setMaximum(maxDeluxe);
            slider3.setMaximum(maxExecutive);
            
            if (deluxeRooms > maxDeluxe) {
                slider2.setValue(maxDeluxe);
            }
            if (executiveRooms > maxExecutive) {
                slider3.setValue(maxExecutive);
            }
            
            slider1.setMaximum(MAX_TOTAL_ROOMS - deluxeRooms - executiveRooms);
            
            sliderValueLabel1.setText("          Base Room(s): " + normalRooms);
            sliderValueLabel2.setText("      Deluxe Room(s): " + slider2.getValue());
            sliderValueLabel3.setText("Executive Room(s): " + slider3.getValue());
        };

        // Add ChangeListener to Normal Room Slider
        slider1.addChangeListener(e -> updateSliders.run());

        // Add ChangeListener to Deluxe Room Slider
        slider2.addChangeListener(e -> updateSliders.run());

        // Add ChangeListener to Executive Room Slider
        slider3.addChangeListener(e -> updateSliders.run());

        // Initial update
        updateSliders.run();

        sliderRooms.add(sliderPanel1);
        sliderRooms.add(sliderPanel2);
        sliderRooms.add(sliderPanel3); 
        sliderRooms.add(Box.createVerticalGlue()); // Add vertical glue to push sliders to the top

        // BUTTONS
        JPanel mainButtons = new JPanel();
        mainButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        // Create Hotel Button
        createHotelBtn = new JButton("Create Hotel");
        createHotelBtn.setActionCommand("ADD_HOTEL"); // For Controller
        mainButtons.add(createHotelBtn);
        // Clear Text Field Button
        clearBtn = new JButton("Clear");
        clearBtn.setActionCommand("CLEAR_HOTEL"); // For Controller
        mainButtons.add(clearBtn);

        // Add an empty border to shift the panel to the left
        mainButtons.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 35));
        upperLeftCreatePanel.add(sliderRooms);
        upperLeftCreatePanel.add(mainButtons);
        upperLeftCreatePanel.add(Box.createVerticalGlue());

        // TABBED PANE - UPPER RIGHT
        JPanel tabHotelListPanel = new JPanel();

        displayHotels(tabHotelListPanel);

        lowerLeftTabbedPane.addTab("Hotel List", tabHotelListPanel);
        
        // GUEST NAME TEXT FIELD
        guestNamePanel = new JPanel();
        guestNamePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        guestLabel = new JLabel("Guest Name: ");
        guestNameField = new JTextField(20);

        // ROOM TYPE COMBO BOX
        addComboBoxRoomTypes();

        // CHECK-IN DATE
        checkInPanel = new JPanel();
        checkInPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        checkInLabel = new JLabel("   Check-In Date: ");

        numberFormatter = createNumberFormatter(1, 31);

        checkInField = new JFormattedTextField(numberFormatter);
        checkInField.setColumns(5); // Set the column size

        // CHECK-OUT DATE
        checkOutPanel = new JPanel();
        checkOutPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        checkOutLabel = new JLabel("Check-Out Date: ");

        numberFormatter = createNumberFormatter(1, 31);
        checkOutField = new JFormattedTextField(numberFormatter);
        checkOutField.setColumns(5);

        // BOOK BUTTON
        finalizeBookingBtn = new JButton("Finalize Booking");
        finalizeBookingBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        finalizeBookingBtn.setActionCommand("FINALIZE_BOOKING"); // For Controller

        // GUEST NAME TEXT FIELD
        discountCodeFlowPanel = new JPanel();
        discountCodeFlowPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        discountCodePanel = new JPanel();
        discountCodePanel.setLayout(new BoxLayout(discountCodePanel, BoxLayout.Y_AXIS));
        discountCodeLabel = new JLabel("Applicable Discount Code");
        discountCodeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        discountCodeField = new JTextField(10);        
        discountCodeField.setPreferredSize
        (new Dimension(discountCodeField.getPreferredSize().width, 
                       discountCodeField.getPreferredSize().height + 3));

        setDisplayInfoAndBooking();

        // TODO ADD TO CONTROLLER MAYBE???
        hotelJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && hotelJList.getSelectedValue() != null) {
                    String selectedHotelName = hotelJList.getSelectedValue().toString();
        
                    // Close any existing tab
                    while (lowerLeftTabbedPane.getTabCount() > 1) {
                        lowerLeftTabbedPane.remove(1);
                    }

                    revalidate();
                    repaint();
        
                    JPanel tabHotelPanel = new JPanel();
                    tabHotelPanel.setLayout(new GridBagLayout());
        
                    JPanel tabHotelUpperPanel = new JPanel();
                    tabHotelUpperPanel.setLayout(new GridBagLayout());
        
                    JPanel tabHotelLowerPanel = new JPanel();
                    tabHotelLowerPanel.setLayout(new BoxLayout(tabHotelLowerPanel, BoxLayout.Y_AXIS));
        
                    gridBagLayoutConfig(tabHotelPanel, tabHotelUpperPanel, 0, 0, 1.0, 0.8);
                    gridBagLayoutConfig(tabHotelPanel, tabHotelLowerPanel, 0, 1, 1.0, 0.2);
        
                    gridBagLayoutConfig(tabHotelUpperPanel, hotelHighInfoPanel, 0, 0, 0.1, 1.0);
                    gridBagLayoutConfig(tabHotelUpperPanel, hotelLowInfoPanel, 1, 0, 0.9, 1.0);
        
                    addClosableTab(selectedHotelName, tabHotelPanel);
        
                    // SIMULATE BOOKING
                    JLabel simulateBookingLabel = new JLabel("Simulate Booking");
                    simulateBookingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
                    JButton bookingBtn = createBookingButton(selectedHotelName);
        
                    tabHotelLowerPanel.add(simulateBookingLabel);
                    tabHotelLowerPanel.add(Box.createVerticalStrut(10));
                    tabHotelLowerPanel.add(bookingBtn);
                    // SIMULATE BOOKING END
        
                    int hotelListIndex = hotelJList.getSelectedIndex();
                    hotelTabIndices.put(selectedHotelName, hotelListIndex);
                    lowerLeftTabbedPane.setSelectedIndex(lowerLeftTabbedPane.indexOfTab(selectedHotelName)); // Switch to the newly added tab
                }
            }
        });
    

        // END OF TABBED  PANE




        // MANAGE HOTELS - UPPER RIGHT
        JPanel manageHotelFrontPanel = new JPanel();
        JPanel manageHotelMainPanel = new JPanel();

        CardLayout cardLayout = new CardLayout();
        upperRightManagePanel.setLayout(cardLayout);

        manageHotelFrontPanel.setLayout(new BorderLayout());
        manageHotelBtn = new JButton("Manage Hotel");
        manageHotelBtn.setActionCommand("MANAGE_HOTEL"); // For Controller

        //MAIN MANAGER
        manageHotelMainPanel.setLayout(new GridLayout(7, 1));

        // CHANGE HOTEL NAME
        changeHotelNamePanel1 = new JPanel();
        changeHotelNamePanel1.setLayout(new BoxLayout(changeHotelNamePanel1, BoxLayout.X_AXIS));
        changeHotelNameField = new JTextField(20);
        changeHotelNameField.setMaximumSize
                             (new Dimension(200, changeHotelNameField.getPreferredSize().height));
        changeHotelNameBtn = new JButton("Change Hotel Name");
        changeHotelNameBtn.setActionCommand("CHANGE_HOTEL_NAME"); // For Controller
        changeHotelNamePanel1.add(changeHotelNameBtn);
        changeHotelNamePanel1.add(Box.createHorizontalStrut(10));
        changeHotelNamePanel1.add(changeHotelNameField);

        // ADD ROOMS
        addRoomsPanel2 = new JPanel();
        addRoomsPanel2.setLayout(new BoxLayout(addRoomsPanel2, BoxLayout.X_AXIS));
        // BASE ROOMS
        baseRoomsLabel = new JLabel("Base (" + 0 + "-" + (50-selectedHotelRoomSize) + ") ");
        numberFormatter = createNumberFormatter(1, 50-selectedHotelRoomSize);
        addBaseRoomsField = new JFormattedTextField(numberFormatter);
        addBaseRoomsField.setMaximumSize
                             (new Dimension(30, changeHotelNameField.getPreferredSize().height));
        addRoomsBtn = new JButton("Add");
        addRoomsBtn.setActionCommand("ADD_BASE_ROOMS"); // For Controller
        addRoomsPanel2.add(baseRoomsLabel);
        addRoomsPanel2.add(Box.createHorizontalStrut(5));
        addRoomsPanel2.add(addBaseRoomsField);
        addRoomsPanel2.add(Box.createHorizontalStrut(5));
        addRoomsPanel2.add(addRoomsBtn);
        addRoomsPanel2.add(Box.createHorizontalStrut(10));
        // DELUXE ROOMS
        deluxeRoomsLabel = new JLabel("Deluxe (0-" + remainingDeluxeRooms + ") ");
        numberFormatter = createNumberFormatter(0, remainingDeluxeRooms);
        addDeluxeRoomsField = new JFormattedTextField(numberFormatter);
        addDeluxeRoomsField.setMaximumSize
                             (new Dimension(30, changeHotelNameField.getPreferredSize().height));
        addDeluxeRoomsBtn = new JButton("Add");
        addDeluxeRoomsBtn.setActionCommand("ADD_DELUXE_ROOMS"); // For Controller
        addRoomsPanel2.add(deluxeRoomsLabel);
        addRoomsPanel2.add(Box.createHorizontalStrut(5));
        addRoomsPanel2.add(addDeluxeRoomsField);
        addRoomsPanel2.add(Box.createHorizontalStrut(5));
        addRoomsPanel2.add(addDeluxeRoomsBtn);
        addRoomsPanel2.add(Box.createHorizontalStrut(10));
        // EXECUTIVE ROOMS
        execRoomsLabel = new JLabel("Executive (0-" + remainingExecRooms + ") ");
        numberFormatter = createNumberFormatter(0, remainingExecRooms);
        addExecRoomsField = new JFormattedTextField(numberFormatter);
        addExecRoomsField.setMaximumSize
                           (new Dimension(30, changeHotelNameField.getPreferredSize().height));
        addExecRoomsBtn = new JButton("Add");
        addExecRoomsBtn.setActionCommand("ADD_EXEC_ROOMS"); // For Controller
        addRoomsPanel2.add(execRoomsLabel);
        addRoomsPanel2.add(Box.createHorizontalStrut(5));
        addRoomsPanel2.add(addExecRoomsField);
        addRoomsPanel2.add(Box.createHorizontalStrut(5));
        addRoomsPanel2.add(addExecRoomsBtn);

        // REMOVE ROOM
        removeRoomPanel3 = new JPanel();
        removeRoomPanel3.setLayout(new BoxLayout(removeRoomPanel3, BoxLayout.X_AXIS));
        removeRoomBtn = new JButton("Remove Room");
        removeRoomBtn.setActionCommand("REMOVE_ROOM"); // For Controller

        removeRoomPanel3.add(removeRoomBtn);
        addRoomsWithoutReservationsComboBox();
        removeRoomPanel3.add(roomsWithoutReservationsComboBox);
        backToFrontManageBtn = new JButton("Back");

        //CONFIG
        int margin = 50;
        manageHotelFrontPanel.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
        manageHotelFrontPanel.add(manageHotelBtn, BorderLayout.CENTER);
        currentFont = manageHotelBtn.getFont();
        newFont = new Font(currentFont.getName(), Font.BOLD, currentFont.getSize() + 10);
        manageHotelBtn.setFont(newFont);

        manageHotelMainPanel.add(changeHotelNamePanel1);
        manageHotelMainPanel.add(addRoomsPanel2);
        manageHotelMainPanel.add(removeRoomPanel3);
        manageHotelMainPanel.add(backToFrontManageBtn);

        upperRightManagePanel.add(manageHotelFrontPanel, "FRONT");
        upperRightManagePanel.add(manageHotelMainPanel, "MAIN");
        cardLayout.show(upperRightManagePanel, "FRONT");

        manageHotelBtn.setEnabled(false);
        manageHotelBtn.addActionListener(e -> {
            cardLayout.show(upperRightManagePanel, "MAIN");
        });

        backToFrontManageBtn.addActionListener(e -> {
            cardLayout.show(upperRightManagePanel, "FRONT");
        });

    }

    public void enableManageHotelBtn() {
        manageHotelBtn.setEnabled(true);
    }

    // CHANGE HOTEL NAME
    public String getCurrentTabName() {
        return currentTabName;
    }
    public void setCurrentTabName(String currentTabName) {
        this.currentTabName = currentTabName;
    }

    // ADD ROOMS
    public void updateNumberFieldFormatter(JFormattedTextField field, int min, int max) {
        NumberFormatter numberFormatter = createNumberFormatter(min, max);
        field.setFormatterFactory(new DefaultFormatterFactory(numberFormatter));
    }

    public String getAddBaseRoomsField() {
        return addBaseRoomsField.getText();
    }
    public String getAddDeluxeRoomsField() {
        return addDeluxeRoomsField.getText();
    }
    public String getAddExecRoomsField() {
        return addExecRoomsField.getText();
    }

    public void setRemainingBaseRooms() {
        remainingBaseRooms = MAX_TOTAL_ROOMS - totalRooms;
        remainingBaseRooms = Math.max(remainingBaseRooms, 0);
    }
    public void setRemainingDeluxeRooms() {
        remainingRooms = MAX_TOTAL_ROOMS - totalRooms - selectedHotelRoomSize;
        maxDeluxeRooms = (int) Math.min((int) 
                        Math.floor(selectedHotelRoomSize * 0.6), remainingRooms);
        // remainingDeluxeRooms = maxDeluxeRooms - totalRooms;
        remainingDeluxeRooms = maxDeluxeRooms;
        this.remainingDeluxeRooms = Math.max(remainingDeluxeRooms, 0);
    }
    public void setRemainingExecRooms() {
        maxExecRooms = (MAX_TOTAL_ROOMS * EXEC_RATIO) / RATIO_PARTS;
        remainingExecRooms = maxExecRooms - totalRooms;
        this.remainingExecRooms = Math.max(remainingExecRooms, 0);
    }

    public void updateManageAllRooms() {
        setRemainingBaseRooms();
        if (selectedHotelRoomSize == 50) {
            baseRoomsLabel.setText("Rooms (0) ");
            updateNumberFieldFormatter(addBaseRoomsField, 0, 0);
        } 
        else {
            baseRoomsLabel.setText("Rooms (" + 1 + "-" + (50-selectedHotelRoomSize) + ") ");
            updateNumberFieldFormatter(addBaseRoomsField, 1, 50-selectedHotelRoomSize);
        }
        addBaseRoomsField.setText("");

        setRemainingDeluxeRooms();
        if (selectedHotelDeluxeRooms == maxDeluxeRooms 
            || remainingDeluxeRooms == 0) {
            deluxeRoomsLabel.setText("Deluxe (0) ");
            updateNumberFieldFormatter(addDeluxeRoomsField, 0, 0);
        } 
        else {
            deluxeRoomsLabel.setText("Deluxe (0-" + remainingDeluxeRooms + ") ");
            updateNumberFieldFormatter(addDeluxeRoomsField, 0, remainingDeluxeRooms);
        }
        addDeluxeRoomsField.setText("");

        setRemainingExecRooms();
        if (selectedHotelExecRooms == maxExecRooms
            || remainingExecRooms == 0) {
            execRoomsLabel.setText("Executive (0) ");
            updateNumberFieldFormatter(addExecRoomsField, 0, 0);
        } 
        else {
            execRoomsLabel.setText("Executive (0-" + remainingExecRooms + ") ");
            updateNumberFieldFormatter(addExecRoomsField, 0, remainingExecRooms);
        }
        addExecRoomsField.setText("");
    }

    // REMOVE ROOMS
    public void addRoomsWithoutReservationsComboBox() {
        roomsWithoutReservationsComboBox = new JComboBox<>();
        roomsWithoutReservationsComboBox.setMaximumSize(new Dimension(75, 
                                    roomsWithoutReservationsComboBox.getPreferredSize().height));
        roomsWithoutReservationsComboBox.setActionCommand("ROOMS_WITHOUT_RESERVATIONS");
    }
    public String getSelectedRoomForRemoval() {
        return roomsWithoutReservationsComboBox.getSelectedItem().toString();
    }
    // public void updateRoomForRemoval() {
    //     roomsWithoutReservationsComboBox.removeAllItems();
    //     for (Room room : roomsWithoutReservations) {
    //         roomsWithoutReservationsComboBox.addItem(room.getRoomName());
    //     }
    // }
    public void updateRoomsForRemoval(String roomsNoReserve[]) {
        roomsWithoutReservationsComboBox.setModel(new DefaultComboBoxModel<>(roomsNoReserve));
    }


    private void gridBagLayoutConfig(JPanel panel, Component component, int gridx, 
                                     int gridy, double weightx, double weighty) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(component, gbc);
    }

    private NumberFormatter createNumberFormatter(int minNum, int maxNum) {
        NumberFormat format = NumberFormat.getInstance(); // Step 1: Create a NumberFormat instance
        format.setGroupingUsed(false); // Disable comma grouping
    
        NumberFormatter numberFormatter = new NumberFormatter(format) { // Step 2: Set up a NumberFormatter
            @Override
            public Object stringToValue(String string) throws ParseException {
                if (string == null || string.trim().isEmpty()) {
                    return null; // Return null for empty input
                }
                return super.stringToValue(string);
            }
        };
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setMinimum(minNum); // Minimum value
        numberFormatter.setMaximum(maxNum); // Maximum value
        numberFormatter.setAllowsInvalid(false); // Don't allow invalid values
    
        return numberFormatter;
    }
            
    public JButton createBookingButton(String hotelName) {
        JButton newBookingBtn = new JButton("Book a Room");
        newBookingBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        newBookingBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, newBookingBtn.getPreferredSize().height));
        
        // Lambda expression for the action listener
        newBookingBtn.addActionListener(e -> displayBookingFrame(hotelName));
        
        return newBookingBtn;
    }

    private void addClosableTab(String title, Component component) {
        JPanel tabComponent = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabComponent.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title + " ");
        tabComponent.add(titleLabel);
        
        JButton closeBtn = new JButton("x"); // Create the close button
        closeBtn.setMargin(new Insets(0, 0, 0, 0));
        closeBtn.addActionListener(e -> {
            // Find the index of the component to remove
            int index = lowerLeftTabbedPane.indexOfComponent(component);
            if (index != -1) {
                lowerLeftTabbedPane.removeTabAt(index);
                setRoomName("");
                setRoomPrice("");
                setRoomOccupancy("");
            }
        });
        tabComponent.add(closeBtn); 
        
        // Add the custom component as the tab header
        lowerLeftTabbedPane.addTab(title, component);
        lowerLeftTabbedPane.setTabComponentAt(lowerLeftTabbedPane.indexOfComponent(component), tabComponent);
    }

    public void setDisplayInfoAndBooking() {
        
        // HIGH-LEVEL INFO
        // Display hotel name, total base rooms, total deluxe rooms, 
        // total executive rooms, and estimated earnings
        hotelHighInfoPanel = new JPanel();
        hotelHighInfoPanel.setLayout(new BoxLayout(hotelHighInfoPanel, BoxLayout.Y_AXIS));

        TitledBorder titledBorder = new TitledBorder("- High-Level Info -");
        titledBorder.setTitleColor(new Color(0, 100, 0));
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        hotelHighInfoPanel.setBorder(titledBorder);

        JLabel hotelNameLabel = new JLabel("Hotel Name: " + getSelectedHotelName());
        JLabel occupationStatusLabel = new JLabel(" Occupied/Total ");
        occupationStatusLabel.setOpaque(true);
        occupationStatusLabel.setBackground(Color.GREEN);
      
        totalBaseRoomsLabel = new JLabel("  |Base Rooms: " + baseRoomOcc 
                                                + "/" + getSelectedHotelRoomSize());
        totalDeluxeRoomsLabel = new JLabel("  |Deluxe Rooms: " + deluxeRoomOcc 
                                                  + "/" + getSelectedHotelDeluxeRoomsSize());
        totalExecRoomsLabel = new JLabel("  |Executive Rooms: " + execRoomOcc
                                                  + "/" + getSelectedHotelExecRoomsSize());

        String earnings = String.format("%.2f", 0.0);
        JLabel estimatedEarningsLabel = new JLabel("  |Estimated Earnings:");
        earningsValueLabel = new JLabel("  " + earnings);
        earningsValueLabel.setForeground(new Color(0, 100, 0));
        
        Font customFont = new Font("Consolas", Font.BOLD, 18);
        earningsValueLabel.setFont(customFont);

        hotelHighInfoPanel.add(hotelNameLabel);
        hotelHighInfoPanel.add(Box.createVerticalStrut(10));
        hotelHighInfoPanel.add(occupationStatusLabel);
        hotelHighInfoPanel.add(Box.createVerticalStrut(10));
        hotelHighInfoPanel.add(totalBaseRoomsLabel);
        hotelHighInfoPanel.add(Box.createVerticalStrut(10));
        hotelHighInfoPanel.add(totalDeluxeRoomsLabel);
        hotelHighInfoPanel.add(Box.createVerticalStrut(10)); 
        hotelHighInfoPanel.add(totalExecRoomsLabel);
        hotelHighInfoPanel.add(Box.createVerticalStrut(10)); 
        hotelHighInfoPanel.add(estimatedEarningsLabel);
        hotelHighInfoPanel.add(earningsValueLabel);
        hotelHighInfoPanel.add(Box.createVerticalStrut(5));
        hotelHighInfoPanel.add(Box.createVerticalGlue());

        // LOW-LEVEL INFO
        hotelLowInfoPanel = new JPanel();
        hotelLowInfoPanel.setLayout(new GridLayout(1, 3));

        titledBorder = new TitledBorder("- Low-Level Info -");
        titledBorder.setTitleColor(Color.RED);
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        hotelLowInfoPanel.setBorder(titledBorder);

        // ROOM-DATE AVAIL
        roomDateAvailPanel = new JPanel();
        JPanel roomInfoPanel = new JPanel();
        JPanel reservationInfoPanel = new JPanel();

        roomDateAvailPanel.setLayout(new BoxLayout(roomDateAvailPanel, BoxLayout.Y_AXIS));
        roomInfoPanel.setLayout(new BoxLayout(roomInfoPanel, BoxLayout.Y_AXIS));
        reservationInfoPanel.setLayout(new BoxLayout(reservationInfoPanel, BoxLayout.Y_AXIS));

        roomDateAvailLabel = new JLabel("Room-Date Availability (1-31)");
        roomInfoLabel = new JLabel("Room Info (1-" + totalRooms + ")");
        reservationInfoLabel = new JLabel("Reservation Info");
        reservationInfoReminderLabel = new JLabel("<--('Check' a room first)");

        showLowHotelInfo();

        // Addition of Components
        roomDateAvailPanel.add(roomDateAvailLabel);
        roomDateAvailPanel.add(roomDateAvailField);
        roomDateAvailPanel.add(Box.createVerticalStrut(5));
        roomDateAvailPanel.add(roomDateAvailBtn);
        roomDateAvailPanel.add(Box.createVerticalStrut(5));
        roomDateAvailPanel.add(roomDateAvailScrollPane);

        roomInfoPanel.add(roomInfoLabel);
        roomInfoPanel.add(roomInfoField);
        roomInfoPanel.add(Box.createVerticalStrut(5));
        roomInfoPanel.add(roomInfoBtn);
        roomInfoPanel.add(Box.createVerticalStrut(5));
        roomInfoPanel.add(roomNameLabel);
        roomInfoPanel.add(Box.createVerticalStrut(5));
        roomInfoPanel.add(roomPriceLabel);
        roomInfoPanel.add(Box.createVerticalStrut(5));
        roomInfoPanel.add(roomOccupancyLabel);
        roomInfoPanel.add(roomOccupancyValueLabel);

        reservationInfoPanel.add(reservationInfoLabel);

        reservationInfoPanel.add(reservationInfoField);
        reservationInfoPanel.add(reservationInfoReminderLabel);
        reservationInfoPanel.add(Box.createVerticalStrut(5));
        reservationInfoPanel.add(reservationInfoBtn);
        reservationInfoPanel.add(Box.createVerticalStrut(5));
        reservationInfoPanel.add(reservationGuestLabel);
        reservationInfoPanel.add(Box.createVerticalStrut(5));
        reservationInfoPanel.add(reservationCheckInLabel);
        reservationInfoPanel.add(Box.createVerticalStrut(5));
        reservationInfoPanel.add(reservationCheckOutLabel);
        reservationInfoPanel.add(Box.createVerticalStrut(5));
        reservationInfoPanel.add(reservationDiscountCodeLabel);
        reservationInfoPanel.add(Box.createVerticalStrut(5));
        reservationInfoPanel.add(reservationBookingPriceLabel);
        

        hotelLowInfoPanel.add(roomDateAvailPanel);
        hotelLowInfoPanel.add(roomInfoPanel);
        hotelLowInfoPanel.add(reservationInfoPanel);

        // VIEW HOTEL END


    }

    public String getSelectedHotelName() {
        return selectedHotelName;
    }
    public void setSelectedHotelName(String selectedHotelName) {
        this.selectedHotelName = selectedHotelName;
    }

    public int getSelectedHotelIndex() {
        return selectedHotelIndex;
    }

    public void setSelectedHotelIndex(int selectedHotelIndex) {
        this.selectedHotelIndex = selectedHotelIndex;
    }
    
    public void setSelectedHotelRoomSize(int selectedHotelRoomSize) {
        this.selectedHotelRoomSize = selectedHotelRoomSize;
    }

    public void setSelectedHotelDeluxeRoomSize(int selectedHotelDeluxeRooms) {
        this.selectedHotelDeluxeRooms = selectedHotelDeluxeRooms;
    }

    public void setSelectedHotelExecRoomSize(int selectedHotelExecRooms) {
        this.selectedHotelExecRooms = selectedHotelExecRooms;
    }

    public void setTotalHotelEarnings(double totalHotelEarnings) {
        this.totalHotelEarnings = totalHotelEarnings;
    }

    public void setTotalHotels(int totalHotels) {
        this.totalHotels = totalHotels;
    }

    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }

    // RESERVATION
    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }
    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }
    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }
    public void setBookingPrice(double bookingPrice) {
        this.bookingPrice = bookingPrice;
    }
    public String getReservationInfoTextField() {
        return reservationInfoField.getText();
    }


    public int getSelectedHotelRoomSize() {
        return selectedHotelRoomSize;
    }

    public int getSelectedHotelDeluxeRoomsSize() {
        return selectedHotelDeluxeRooms;
    }

    public int getSelectedHotelExecRoomsSize() {
        return selectedHotelExecRooms;
    }

    public double getTotalHotelEarnings() {
        return totalHotelEarnings;
    }

    // MANAGE HOTEL
    public JTabbedPane getLowerLeftTabbedPane() {
        return lowerLeftTabbedPane;
    }

    public Map<String, Integer> getHotelTabIndices() {
        return hotelTabIndices;
    }

    // ROOM BOOKING UPDATES
    public void setBaseRoomOcc(int baseRoomOcc) {
        this.baseRoomOcc = baseRoomOcc;
    }
    public void setDeluxeRoomOcc(int deluxeRoomOcc) {
        this.deluxeRoomOcc = deluxeRoomOcc;
    }
    public void setExecRoomOcc(int execRoomOcc) {
        this.execRoomOcc = execRoomOcc;
    }

    // BOOKING DETAILS
    public String getGuestName() {
        return guestNameField.getText();
    }
    public String getCheckInDate() {
        return checkInField.getText();
    }
    public String getCheckOutDate() {
        return checkOutField.getText();
    }
    public String getDiscountCode() {
        return discountCodeField.getText();
    }
    public String getSelectedRoomType() {
        return comboBox.getSelectedItem().toString();
    }

    public JList<Hotel> getHotelJList() {
        return hotelJList;
    }

    public DefaultListModel<Hotel> getHotelListModel() {
        return hotelListModel;
    }

    public JList<Room> getRoomDateAvailList() {
        return roomDateAvailList;
    }

    public DefaultListModel<Room> getRoomDateAvailListModel() {
        return roomDateAvailListModel;
    }

    public String getRoomDateAvailFieldText() {
        return roomDateAvailField.getText();
    }

    public String getRoomInfoFieldText() {
        return roomInfoField.getText();
    }

    public void setReservationTotal(int reservationTotal) {
        this.reservationTotal = reservationTotal;
    }

    public void showLowHotelInfo() {
        
        // Column 1 - Room-Date Availability        
        numberFormatter = createNumberFormatter(1, 31);
        roomDateAvailField = new JFormattedTextField(numberFormatter);
        roomDateAvailField.setColumns(5);
        roomDateAvailField.setPreferredSize(new Dimension(10, 20));
        roomDateAvailField.setMaximumSize(roomDateAvailField.getPreferredSize());

        roomDateAvailBtn = new JButton("Check");
        roomDateAvailBtn.setActionCommand("ROOM_DATE_AVAIL");

        roomDateAvailListModel = new DefaultListModel<>();
        roomDateAvailList = new JList<Room>(roomDateAvailListModel);
        roomDateAvailScrollPane = new JScrollPane(roomDateAvailList);
        roomDateAvailScrollPane.setPreferredSize(new Dimension(200, 150));
        roomDateAvailScrollPane.setMaximumSize(roomDateAvailScrollPane.getPreferredSize());

        roomDateAvailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        roomDateAvailBtn.setAlignmentX(Component.CENTER_ALIGNMENT);


        // Column 2 - Room Info
        numberFormatter = createNumberFormatter(1, totalRooms);
        roomInfoField = new JFormattedTextField(numberFormatter);
        roomInfoField.setColumns(5);
        roomInfoField.setPreferredSize(new Dimension(10, 20));
        roomInfoField.setMaximumSize(roomInfoField.getPreferredSize());

        roomInfoBtn = new JButton("Check");
        roomInfoBtn.setActionCommand("ROOM_INFO_SHOW");

        roomNameLabel = new JLabel("Room Name: ");
        roomPriceLabel = new JLabel("Room Price: ");
        roomOccupancyLabel = new JLabel("Room Vacancy: ");
        roomOccupancyValueLabel = new JLabel("");

        roomInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        roomInfoField.setAlignmentX(Component.CENTER_ALIGNMENT);
        roomInfoBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        roomNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        roomPriceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        roomOccupancyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        roomOccupancyValueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // ETC CONDITIONALS
        if (totalRooms < 2) 
            roomInfoLabel.setText("Room Info (1)");

        // Column 3 - Reservation Info
        numberFormatter = createNumberFormatter(1, reservationTotal);
        reservationInfoField = new JFormattedTextField(numberFormatter);
        reservationInfoField.setColumns(5);
        reservationInfoField.setPreferredSize(new Dimension(10, 20));
        reservationInfoField.setMaximumSize(reservationInfoField.getPreferredSize());

        reservationInfoBtn = new JButton("Check");
        reservationInfoBtn.setActionCommand("RESERVATION_INFO_SHOW");

        reservationGuestLabel = new JLabel("Guest Name: ");
        reservationCheckInLabel = new JLabel("Check-In Date: ");
        reservationCheckOutLabel = new JLabel("Check-Out Date: ");
        reservationDiscountCodeLabel = new JLabel("Discount Code: ");
        reservationBookingPriceLabel = new JLabel("Booking Price: ");
        
        reservationInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        reservationInfoReminderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        reservationInfoField.setAlignmentX(Component.CENTER_ALIGNMENT);
        reservationInfoBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        reservationGuestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        reservationCheckInLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        reservationCheckOutLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        reservationDiscountCodeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        reservationBookingPriceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Since no reservations are made yet 
        reservationInfoField.setEnabled(false);
        reservationInfoBtn.setEnabled(false);


    }

    public void updateRoomInfoFieldFormatter(int totalRooms) {
        NumberFormatter numberFormatter = createNumberFormatter(1, totalRooms);
        roomInfoField.setFormatterFactory(new DefaultFormatterFactory(numberFormatter));
    }
    public void updateReservationInfoFieldFormatter(int totalReservations) {
        NumberFormatter numberFormatter = createNumberFormatter(1, totalReservations);
        reservationInfoField.setFormatterFactory(new DefaultFormatterFactory(numberFormatter));
    }    

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }
    public void setRoomOccupancy(String roomOccupancy) {
        this.roomOccupancy = roomOccupancy;
    }

    public void displayHotels(JPanel tabHotelListPanel) {
        hotelListModel = new DefaultListModel<>();
        hotelJList     = new JList<>(hotelListModel);

        JScrollPane hotelListScrollPane = new JScrollPane(hotelJList);
        hotelListScrollPane.setPreferredSize(new Dimension(300, 150));

        JLabel hotelListLabel = new JLabel("Double Click Hotel to Book a Room");
        currentFont = hotelListLabel.getFont();
        hotelListLabel.setFont(currentFont.deriveFont(currentFont.getStyle() + Font.ITALIC));
        hotelListLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        tabHotelListPanel.add(hotelListScrollPane);
        tabHotelListPanel.add(hotelListLabel);
    }

    public void addComboBoxRoomTypes() {
        roomTypePanel = new JPanel();
        roomTypePanel.setLayout(new BoxLayout(roomTypePanel, BoxLayout.X_AXIS));
        comboBox = new JComboBox<>(new String[] {"Base Room", "Deluxe Room", "Executive Room"});
        comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        comboBox.setMaximumSize(new Dimension(comboBox.getPreferredSize().width, 
                                              comboBox.getPreferredSize().height));
        comboBox.setActionCommand("SELECT_ROOM_TYPE"); // For Controller

        checkAvailBtn = new JButton("Availability");
        checkAvailBtn.setPreferredSize(new Dimension(100, comboBox.getPreferredSize().height));
        checkAvailBtn.setMinimumSize(checkAvailBtn.getPreferredSize());
        checkAvailBtn.setMaximumSize(checkAvailBtn.getPreferredSize());
    }

    public void updateComboBoxItems(int deluxeRoomTotal, int execRoomTotal) {
        if (deluxeRoomTotal < 1 && execRoomTotal < 1) {
            comboBox.setModel(new javax.swing.DefaultComboBoxModel<>
                             (new String[] {"Base Room"}));
        } 
        else if (deluxeRoomTotal < 1) {
            comboBox.setModel(new javax.swing.DefaultComboBoxModel<>
                             (new String[] {"Base Room", "Executive Room"}));
        } 
        else if (execRoomTotal < 1) {
            comboBox.setModel(new javax.swing.DefaultComboBoxModel<>
                             (new String[] {"Base Room", "Deluxe Room"}));
        } 
        else {
            comboBox.setModel(new javax.swing.DefaultComboBoxModel<>
                             (new String[] {"Base Room", "Deluxe Room", "Executive Room"}));
        }
    }

    public void displayBookingFrame(String hotelName) {
        /* NEW WINDOW POP-UP FOR BOOKING */
        bookingFrame = new JFrame(hotelName + " Booking");
        setupBookingFrame(bookingFrame);

        JPanel bookingPanel = new JPanel();
        bookingPanel.setLayout(new BoxLayout(bookingPanel, BoxLayout.Y_AXIS));
            
        guestNamePanel.add(guestLabel);
        guestNamePanel.add(guestNameField);
        roomTypePanel.add(Box.createHorizontalGlue());
        roomTypePanel.add(comboBox);
        roomTypePanel.add(Box.createHorizontalStrut(5));
        roomTypePanel.add(checkAvailBtn);
        roomTypePanel.add(Box.createHorizontalGlue());
        checkInPanel.add(checkInLabel);
        checkInPanel.add(checkInField);
        checkOutPanel.add(checkOutLabel);
        checkOutPanel.add(checkOutField);
        discountCodePanel.add(discountCodeLabel);
        discountCodePanel.add(discountCodeField);
        discountCodePanel.add(Box.createVerticalGlue());
        discountCodeFlowPanel.add(discountCodePanel);

        bookingPanel.add(guestNamePanel);
        bookingPanel.add(roomTypePanel);
        bookingPanel.add(Box.createVerticalStrut(15));
        bookingPanel.add(checkInPanel);       
        bookingPanel.add(checkOutPanel);
        bookingPanel.add(Box.createVerticalStrut(10));
        bookingPanel.add(discountCodeFlowPanel);
        bookingPanel.add(finalizeBookingBtn);
        bookingPanel.add(Box.createVerticalStrut(15));
        bookingPanel.add(Box.createVerticalGlue());

        bookingFrame.add(bookingPanel);
    }

    public void setActionListener(ActionListener listener) {
        createHotelBtn.addActionListener(listener);
        clearBtn.addActionListener(listener);
        finalizeBookingBtn.addActionListener(listener);
        comboBox.addActionListener(listener);
        roomDateAvailBtn.addActionListener(listener);
        roomInfoBtn.addActionListener(listener);
        reservationInfoBtn.addActionListener(listener);
        changeHotelNameBtn.addActionListener(listener);
        addRoomsBtn.addActionListener(listener);
        addDeluxeRoomsBtn.addActionListener(listener);
        addExecRoomsBtn.addActionListener(listener);
        removeRoomBtn.addActionListener(listener);
        // roomsWithoutReservationsComboBox.addActionListener(listener);

    }

    public void setDocumentListener(DocumentListener listener) {
        hotelNameField.getDocument().addDocumentListener(listener);
        guestNameField.getDocument().addDocumentListener(listener);
        checkInField.getDocument().addDocumentListener(listener);
        checkOutField.getDocument().addDocumentListener(listener);
        discountCodeField.getDocument().addDocumentListener(listener);
        roomDateAvailField.getDocument().addDocumentListener(listener);
        roomInfoField.getDocument().addDocumentListener(listener);
        reservationInfoField.getDocument().addDocumentListener(listener);
        changeHotelNameField.getDocument().addDocumentListener(listener);
        addBaseRoomsField.getDocument().addDocumentListener(listener);
        addDeluxeRoomsField.getDocument().addDocumentListener(listener);
        addExecRoomsField.getDocument().addDocumentListener(listener);

    }

    public void setMouseListener(MouseListener listener) {
        hotelJList.addMouseListener(listener);
    }

    public void setChangeListener(ChangeListener listener) {
        lowerLeftTabbedPane.addChangeListener(listener);
    }

    public String getChangeHotelNameFieldText() {
        return changeHotelNameField.getText();
    }

    public void setupBookingFrame(JFrame bookingFrame) {
        setEnabled(false);
        bookingFrame.setSize(300, 280);
        // bookingFrame.setResizable(false);
        bookingFrame.setLocationRelativeTo(null);
        bookingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        bookingFrame.setVisible(true);
        bookingFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                setEnabled(true);
            }
        });
        
        finalizeBookingBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setEnabled(true);
                bookingFrame.dispose();
            }
        });       

        bookingFrame.setVisible(true);
    }

    public JFrame getBookingFrame() {
        return bookingFrame;
    }

    public void setupHotelNameFieldFocusListener() {
        hotelNameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // Code to execute when component gains focus
                hotelNameField.setText("");
                hotelNameField.setForeground(Color.BLACK);
                createHotelBtn.setEnabled(true);
                clearBtn.setEnabled(true);
            }

            @Override
            public void focusLost(FocusEvent e) {
                // Code to execute when component loses focus
                if (hotelNameField.getText().isEmpty()) {
                    hotelNameField.setText("Enter Hotel Name..."); 
                    // make setText above appear as grayish in controller
                    hotelNameField.setForeground(Color.GRAY);
                    createHotelBtn.setEnabled(false);
                    clearBtn.setEnabled(false);
                }
            }
        });
    }

    public void setCreateBtnEnabled(boolean enabled) {
        this.createHotelBtn.setEnabled(enabled);
    }

    public int getRoomsSliderValue() {
        return slider1.getValue();
    }

    public int getDeluxeRoomsSliderValue() {
        return slider2.getValue();
    }

    public int getExecRoomsSliderValue() {
        return slider3.getValue();
    }

    public void setHotelName(String hotelName) {
        hotelNameField.setText(hotelName);
    }
    public String getHotelName() {
        return hotelNameField.getText();
    }

    public void setCurrentHotelName(String hotelName) {
        this.currentHotelName = hotelName;
    }

    public void clearCreateHotel() {
        hotelNameField.setText("");
        slider1.setValue(1);
        slider2.setValue(0);
        slider3.setValue(0);
        setCreateBtnEnabled(false);
    }

    public void clearBookingInfo() {
        guestNameField.setText("");
        comboBox.setSelectedIndex(0);
        checkInField.setText("");
        checkOutField.setText("");
        discountCodeField.setText("");
        bookingFrame.dispose();
    }

    // UPDATE METHODS
    public void updateBookingRelated() {
        totalBaseRoomsLabel.setText("  |Base Rooms: " + baseRoomOcc + "/" + getSelectedHotelRoomSize());
        totalDeluxeRoomsLabel.setText("  |Deluxe Rooms: " + deluxeRoomOcc + "/" + getSelectedHotelDeluxeRoomsSize());
        totalExecRoomsLabel.setText("  |Executive Rooms: " + execRoomOcc + "/" + getSelectedHotelExecRoomsSize());
        earningsValueLabel.setText(" " + String.format("%.2f", totalHotelEarnings));
    }

    public void updateLowLevelRoomInfo() {
        roomInfoLabel.setText("Room Info (1-" + totalRooms + ")");
        roomNameLabel.setText("Room Name: " + roomName);
        roomPriceLabel.setText("Room Price: " + roomPrice);
        roomOccupancyLabel.setText("Room Vacancy: ");
        roomOccupancyValueLabel.setText(roomOccupancy);
    }

    public void updateLowLevelReservationNum() {
        updateReservationInfoFieldFormatter(reservationTotal);

        if (reservationTotal >= 1) {
            reservationInfoField.setEnabled(true);
            reservationInfoBtn.setEnabled(true);

            if (reservationTotal == 1)
                reservationInfoLabel.setText("Reservation Info (1)");
            else
                reservationInfoLabel.setText("Reservation Info (1-" + reservationTotal + ")");        
        }
        else {
            reservationInfoField.setEnabled(false);
            reservationInfoBtn.setEnabled(false);
        }
    }

    public void updateLowLevelReservationInfo() {
        reservationGuestLabel.setText("Guest Name: " + guestName);
        reservationCheckInLabel.setText("Check-In Date: " + checkInDate);
        reservationCheckOutLabel.setText("Check-Out Date: " + checkOutDate);
        reservationDiscountCodeLabel.setText("Availed Discount: " + discountCode);
        reservationBookingPriceLabel.setText("Booking Price: " + String.format("%.2f", bookingPrice));
    }

    public void updateManageHotel() {
        manageHotelBtn.setText("Manage '" + currentHotelName + "'");
    }

}
