// TODO: Impelement reservation numbers properly
package view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import java.awt.*;
import javax.swing.event.*;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.NumberFormatter;

import java.awt.event.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

import model.hotel.Hotel;
import model.hotel.Room;
import model.hotel.Reservation;

public class GUI extends JFrame {
    private JFrame mainFrame, bookingFrame;
    private JPanel upperPanel, lowerPanel,
                   hotelHighInfoPanel, hotelLowInfoPanel, 
                   upperLeftCreatePanel, upperRightManagePanel, lowerRightFiller, 
                   guestNamePanel, checkInPanel, checkOutPanel, discountCodePanel, roomTypePanel, 
                   discountCodeFlowPanel;
    private JLabel guestLabel, checkInLabel, checkOutLabel, discountCodeLabel, earningsValueLabel,
                   totalBaseRoomsLabel, totalDeluxeRoomsLabel, totalExecRoomsLabel;


    private JTextField hotelNameField, guestNameField, discountCodeField;
    private JFormattedTextField checkInField, checkOutField;
    private JButton createHotelBtn, finalizeBookingBtn, manageHotelBtn, clearBtn, 
                    checkAvailBtn;
    private ArrayList<JButton> roomDateAvailBtns = new ArrayList<JButton>();
    private JSlider slider1, slider2, slider3; // SLIDER IS TEMPORARY
    private JComboBox<String> comboBox;
    private JTabbedPane lowerLeftTabbedPane;
    private JList<Hotel> hotelJList;
    private DefaultListModel<Hotel> hotelListModel;
    private JList<Room> roomDateAvailList, roomInfoList;
    private DefaultListModel<Room> roomDateAvailModel, roomInfoModel;
    private JList<Reservation> reservationInfoList;
    private DefaultListModel<Reservation> reservationInfoModel;

    private Font font;
    private NumberFormatter numberFormatter;
    private Dimension preferredSize;

    private int selectedHotelIndex = -1;
    private String selectedHotelName = "NULL"; // Placeholder
    private int totalHotels = 0, 
                totalRooms, selectedHotelRoomSize, selectedHotelDeluxeRooms, selectedHotelExecRooms,
                reservationTotal = 0,
                baseRoomOcc = 0, deluxeRoomOcc = 0, execRoomOcc = 0;

    private double totalHotelEarnings;

    private final int MAX_TOTAL_ROOMS = 50;

    public GUI() {
        super("Hotel Reservation System");
        setSize(800, 700); // Use 'this.' implicitly
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(null);
    
        setLayout(new GridLayout(2, 1));


        // upperPanel = new JPanel(new GridBagLayout());
        upperPanel = new JPanel(new GridLayout(1, 2));
        lowerPanel = new JPanel(new GridBagLayout());
        
        upperLeftCreatePanel = new JPanel();
        upperRightManagePanel = new JPanel();
        lowerLeftTabbedPane = new JTabbedPane();
        lowerRightFiller = new JPanel();

        upperRightManagePanel.setBackground(Color.LIGHT_GRAY);
        upperRightManagePanel.setOpaque(true);

        
        init(this); // Main program UI

        // MAIN FRAME CONFIGURATION
        // Upper row configuration (50/50 split)
        // gridBagLayoutConfig(upperPanel, upperLeftCreatePanel, 0, 0, 0.5, 1.0);
        // gridBagLayoutConfig(upperPanel, upperRightManagePanel, 1, 0, 0.5, 1.0);

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

        // TODO ADD TO CONTROLLER MAYBE???
        hotelJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && hotelJList.getSelectedValue() != null) {
                    String selectedHotelName = hotelJList.getSelectedValue().toString();
                    boolean tabExists = false;
                    for (int i = 0; i < lowerLeftTabbedPane.getTabCount(); i++) {
                        if (lowerLeftTabbedPane.getTitleAt(i).equals(selectedHotelName)) {
                            tabExists = true;
                            break;
                        }
                    }
                    if (!tabExists) {
                        JPanel tabHotelPanel = new JPanel();
                        tabHotelPanel.setLayout(new GridBagLayout());
                        
                        JPanel tabHotelUpperPanel = new JPanel();
                        tabHotelUpperPanel.setLayout(new GridBagLayout());
                        
                        JPanel tabHotelLowerPanel = new JPanel();
                        tabHotelLowerPanel.setLayout(new BoxLayout(tabHotelLowerPanel, BoxLayout.Y_AXIS));
                        
                        // tabHotelPanel.add(tabHotelLowerPanel, gbcHotel);

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
                        
                        lowerLeftTabbedPane.setSelectedIndex(lowerLeftTabbedPane.getTabCount()-1); // Switch to the newly added tab
                    }
                }
            }
        });


        // END OF TABBED  PANE




        // MANAGE HOTELS - LOWER RIGHT
        // upperRightManagePanel = new JPanel();

        manageHotelBtn = new JButton("Manage Hotels");
        manageHotelBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        upperRightManagePanel.add(manageHotelBtn);   
    }

    private void gridBagLayoutConfig(JPanel panel, Component component, int gridx, int gridy, double weightx, double weighty) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.fill = GridBagConstraints.BOTH;
        // gbc.insets = new Insets(5, 5, 5, 5);
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
        
        JButton closeButton = new JButton("x"); // Create the close button
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.addActionListener(e -> {
            // Find the index of the component to remove
            int index = lowerLeftTabbedPane.indexOfComponent(component);
            if (index != -1) {
                lowerLeftTabbedPane.removeTabAt(index);
                roomDateAvailBtns.remove(index);
            }
        });
        tabComponent.add(closeButton); 
        
        // Add the custom component as the tab header
        lowerLeftTabbedPane.addTab(title, component);
        lowerLeftTabbedPane.setTabComponentAt(lowerLeftTabbedPane.indexOfComponent(component), tabComponent);
    }

    public void setDisplayInfoAndBooking(int totalRooms, int hotelIndex) {
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
        JLabel occupationStatusLabel = new JLabel("Total : Occupied");
        occupationStatusLabel.setOpaque(true);
        occupationStatusLabel.setBackground(Color.RED);
      
        totalBaseRoomsLabel = new JLabel("  |Base Rooms: " + baseRoomOcc 
                                                + "/" + getSelectedHotelRoomSize());
        totalDeluxeRoomsLabel = new JLabel("  |Deluxe Rooms: " + deluxeRoomOcc 
                                                  + "/" + getSelectedHotelExecRoomsSize());
        totalExecRoomsLabel = new JLabel("  |Executive Rooms: " + execRoomOcc
                                                  + "/" + getSelectedHotelDeluxeRoomsSize());
        JLabel estimatedEarningsLabel = new JLabel("  |Estimated Earnings:");
        earningsValueLabel = new JLabel("   " + getTotalHotelEarnings());
        earningsValueLabel.setForeground(new Color(0, 100, 0));

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
        // hotelLowInfoPanel.setLayout(new BoxLayout(hotelLowInfoPanel, BoxLayout.Y_AXIS));
        hotelLowInfoPanel.setLayout(new GridLayout(1, 3));

        titledBorder = new TitledBorder("- Low-Level Info -");
        titledBorder.setTitleColor(Color.RED);
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        hotelLowInfoPanel.setBorder(titledBorder);

        // VIEW HOTEL
        JPanel roomDateAvailPanel = new JPanel();
        JPanel roomInfoPanel = new JPanel();
        JPanel reservationInfoPanel = new JPanel();

        roomDateAvailPanel.setLayout(new BoxLayout(roomDateAvailPanel, BoxLayout.Y_AXIS));
        roomInfoPanel.setLayout(new BoxLayout(roomInfoPanel, BoxLayout.Y_AXIS));
        reservationInfoPanel.setLayout(new BoxLayout(reservationInfoPanel, BoxLayout.Y_AXIS));

        JLabel roomDateAvailLabel = new JLabel("Room-Date Availability (1-31)");
        JLabel roomInfoLabel = new JLabel("Room Info (1-" + totalRooms + ")");
        JLabel reservationInfoLabel = new JLabel("Reservation Info");

        roomDateAvailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        roomInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        reservationInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // TEXT-FIELD FORMATTING
        numberFormatter = createNumberFormatter(1, 31);
        JFormattedTextField roomDateField = new JFormattedTextField(numberFormatter);
        roomDateField.setColumns(5);
        roomDateField.setPreferredSize(new Dimension(10, 20));
        roomDateField.setMaximumSize(roomDateField.getPreferredSize());

        numberFormatter = createNumberFormatter(1, totalRooms);
        JFormattedTextField roomInfoField = new JFormattedTextField(numberFormatter);
        roomInfoField.setColumns(5);
        roomInfoField.setPreferredSize(new Dimension(10, 20));
        roomInfoField.setMaximumSize(roomInfoField.getPreferredSize());

        numberFormatter = createNumberFormatter(1, reservationTotal);
        JFormattedTextField reservationInfoField = new JFormattedTextField(numberFormatter);
        reservationInfoField.setColumns(5);
        reservationInfoField.setPreferredSize(new Dimension(10, 20));
        reservationInfoField.setMaximumSize(reservationInfoField.getPreferredSize());

        // BUTTONS
        // roomDateAvailBtn = new JButton("Check");

        JButton roomInfoBtn = new JButton("Check");
        JButton reservationInfoBtn = new JButton("Check");

        // roomDateAvailBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        roomInfoBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        reservationInfoBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // roomDateAvailBtn.addActionListener(e -> {
        //    System.out.println("Room Date Avail DEBUG");
        // });
        roomInfoBtn.addActionListener(e -> {
            System.out.println("Room Info DEBUG");
        });

        // ETC CONDITIONALS
        if (totalRooms < 2) 
            roomInfoLabel.setText("Room Info (1)");
        if (reservationTotal < 1) {
            reservationInfoField.setEnabled(false);
            reservationInfoBtn.setEnabled(false);
        }


        // Addition of Components
        roomDateAvailPanel.add(roomDateAvailLabel);
        roomDateAvailPanel.add(roomDateField);
        roomDateAvailPanel.add(Box.createVerticalStrut(5));
        roomDateAvailPanel.add(roomDateAvailBtns.get(hotelIndex));
        roomDateAvailPanel.add(Box.createVerticalStrut(5));
        displayLowRoomDateAvail(roomDateAvailPanel);

        roomInfoPanel.add(roomInfoLabel);
        roomInfoPanel.add(roomInfoField);
        roomInfoPanel.add(Box.createVerticalStrut(5));
        roomInfoPanel.add(roomInfoBtn);
        roomInfoPanel.add(Box.createVerticalStrut(5));
        displayLowRoomInfo(roomInfoPanel);


        reservationInfoPanel.add(reservationInfoLabel);
        reservationInfoPanel.add(reservationInfoField);
        reservationInfoPanel.add(Box.createVerticalStrut(5));
        reservationInfoPanel.add(reservationInfoBtn);
        reservationInfoPanel.add(Box.createVerticalStrut(5));
        displayReservationInfo(reservationInfoPanel);


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

    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }

    public void setSelectedRoomType(String roomType) {
        this.comboBox.setSelectedItem(roomType);
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

    public DefaultListModel<Room> getRoomDateAvailListModel() {
        return roomDateAvailModel;
    }

    private void setupHotelListClickMouseListener() {
        hotelJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedHotelIndex = hotelJList.locationToIndex(e.getPoint());
                if (selectedHotelIndex >= 0) {
                    Object item = hotelListModel.getElementAt(selectedHotelIndex);
                    System.out.println("Clicked on: " + item + " at index " + selectedHotelIndex); // DEBUGGING
                }
            }
        });
    }        

    public void displayHotels(JPanel tabHotelListPanel) {
        hotelListModel = new DefaultListModel<>();
        hotelJList     = new JList<>(hotelListModel);

        JScrollPane hotelListScrollPane = new JScrollPane(hotelJList);
        hotelListScrollPane.setPreferredSize(new Dimension(300, 150));

        JLabel hotelListLabel = new JLabel("Double Click Hotel to Book a Room");
        font = hotelListLabel.getFont();
        hotelListLabel.setFont(font.deriveFont(font.getStyle() + Font.ITALIC));
        hotelListLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        tabHotelListPanel.add(hotelListScrollPane);
        tabHotelListPanel.add(hotelListLabel);

        JButton roomDateAvailBtn = new JButton("Check");
        roomDateAvailBtns.add(roomDateAvailBtn);
        roomDateAvailBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        roomDateAvailBtns.get(0).setActionCommand("ROOM_DATE_AVAIL");

        setupHotelListClickMouseListener();
    }

    public void displayLowRoomDateAvail(JPanel panel) {
        roomDateAvailModel = new DefaultListModel<>();
        roomDateAvailList = new JList<>(roomDateAvailModel);

        JScrollPane roomDateAvailScrollPane = new JScrollPane(roomDateAvailList);
        roomDateAvailScrollPane.setPreferredSize(new Dimension(200, 150));
        roomDateAvailScrollPane.setMaximumSize(roomDateAvailScrollPane.getPreferredSize());
        
        panel.add(roomDateAvailScrollPane);
    }
    public void displayLowRoomInfo(JPanel panel) {
        roomInfoModel = new DefaultListModel<>();
        roomInfoList = new JList<>(roomInfoModel);

        JScrollPane roomInfoScrollPane = new JScrollPane(roomInfoList);
        roomInfoScrollPane.setPreferredSize(new Dimension(200, 150));
        roomInfoScrollPane.setMaximumSize(roomInfoScrollPane.getPreferredSize());
        
        panel.add(roomInfoScrollPane);
    }
    public void displayReservationInfo(JPanel panel) {
        reservationInfoModel = new DefaultListModel<>(); 
        reservationInfoList = new JList<>(reservationInfoModel);

        JScrollPane reservationInfoScrollPane = new JScrollPane(reservationInfoList);
        reservationInfoScrollPane.setPreferredSize(new Dimension(200, 150));
        reservationInfoScrollPane.setMaximumSize(reservationInfoScrollPane.getPreferredSize());
        
        panel.add(reservationInfoScrollPane);
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
        for (JButton btn : roomDateAvailBtns) {
            btn.addActionListener(listener);
        }
    }


    public void setListActionListener(ListSelectionListener listener) {
        hotelJList.addListSelectionListener(listener);
    }

    public void setDocumentListener(DocumentListener listener) {
        hotelNameField.getDocument().addDocumentListener(listener);
        guestNameField.getDocument().addDocumentListener(listener);
        checkInField.getDocument().addDocumentListener(listener);
        checkOutField.getDocument().addDocumentListener(listener);
        discountCodeField.getDocument().addDocumentListener(listener);
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
        earningsValueLabel.setText("   " + getTotalHotelEarnings());
    }

}
