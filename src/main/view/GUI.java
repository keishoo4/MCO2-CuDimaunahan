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
import java.util.Date;

import model.hotel.Hotel;

public class GUI extends JFrame {
    private JFrame mainFrame, bookingFrame;
    private JPanel hotelInfoPanel,
                   guestNamePanel, checkInPanel, checkOutPanel, discountCodePanel, roomTypePanel, 
                   discountCodeFlowPanel;
    private JLabel guestLabel, checkInLabel, checkOutLabel, discountCodeLabel;
    private JTextField hotelNameField, guestNameField, discountCodeField;
    private JFormattedTextField checkInField, checkOutField;
    private JButton createHotelBtn, finalizeBookingBtn, manageHotelBtn, clearBtn;
    private JSlider slider1, slider2, slider3; // SLIDER IS TEMPORARY
    private JList<Hotel> hotelJList;
    private JComboBox<String> comboBox;
    private JTabbedPane tabbedPane;
    private DefaultListModel<Hotel> hotelListModel;

    private Font font;
    private NumberFormatter numberFormatter;

    private int selectedHotelIndex = -1;
    private String selectedHotelName = "NULL"; // Placeholder
    private int selectedHotelRoomSize, selectedHotelDeluxeRooms, selectedHotelExecRooms;
    private double totalHotelEarnings;

    private final int MAX_TOTAL_ROOMS = 50;

    public GUI() {
        super("Hotel Reservation System");
        setSize(800, 700); // Use 'this.' implicitly
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(null);
    
        setLayout(new GridLayout(2, 2));
    
        init(this); // Main program UI
    
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void init(JFrame mainFrame) {
        JPanel leftPanelUpper = new JPanel();
        leftPanelUpper.setLayout(new BoxLayout(leftPanelUpper, BoxLayout.Y_AXIS));

        // HOTEL NAME TEXT FIELD
        JPanel hotelNameAndRoomPanel = new JPanel();
        hotelNameAndRoomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel hotelNameLabel = new JLabel("Hotel Name: ");
        hotelNameField = new JTextField(20);
        hotelNameAndRoomPanel.add(hotelNameLabel);
        hotelNameAndRoomPanel.add(hotelNameField);
        leftPanelUpper.add(hotelNameAndRoomPanel);

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
            // int totalRooms = normalRooms + deluxeRooms + executiveRooms; // UNUSED VARIABLE
            
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

        leftPanelUpper.add(sliderRooms);
        leftPanelUpper.add(mainButtons);

        // TABBED PANE - UPPER RIGHT
        tabbedPane = new JTabbedPane();
        JPanel tabHotelListPanel = new JPanel();

        displayHotels(tabHotelListPanel);

        tabbedPane.addTab("Hotel List", tabHotelListPanel);
        
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

        checkInField = new JFormattedTextField(numberFormatter);
        checkInField.setColumns(5); // Set the column size

        // CHECK-OUT DATE
        checkOutPanel = new JPanel();
        checkOutPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        checkOutLabel = new JLabel("Check-Out Date: ");

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
        // discountCodePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        discountCodePanel.setLayout(new BoxLayout(discountCodePanel, BoxLayout.Y_AXIS));
        discountCodeLabel = new JLabel("Applicable Discount Code: ");
        discountCodeField = new JTextField(11);        

        // TODO ADD TO CONTROLLER MAYBE???
        hotelJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && hotelJList.getSelectedValue() != null) {
                    String selectedHotelName = hotelJList.getSelectedValue().toString();
                    boolean tabExists = false;
                    for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                        if (tabbedPane.getTitleAt(i).equals(selectedHotelName)) {
                            tabExists = true;
                            break;
                        }
                    }
                    if (!tabExists) {
                        JPanel tabHotelPanel = new JPanel();
                        tabHotelPanel.setLayout(new GridBagLayout());
                        GridBagConstraints gbc = new GridBagConstraints();

                        // First row
                        gbc.gridx = 0;
                        gbc.gridy = 0;
                        gbc.weightx = 1.0;
                        gbc.weighty = 0.8; // 80% of the height
                        gbc.fill = GridBagConstraints.BOTH;     
                        
                        JPanel tabHotelUpperPanel = new JPanel();
                        tabHotelUpperPanel.setLayout(new GridLayout(1, 2));

                        JPanel tabHotelUpperRightPanel = new JPanel();
                        tabHotelUpperRightPanel
                        .setLayout(new BoxLayout(tabHotelUpperRightPanel, BoxLayout.Y_AXIS));

                        tabHotelPanel.add(tabHotelUpperPanel, gbc);

                        // Second row
                        gbc.gridy = 1;
                        gbc.weighty = 0.2; // 20% of the height
                
                        JPanel tabHotelLowerPanel = new JPanel();
                        tabHotelLowerPanel.setLayout(new BoxLayout(tabHotelLowerPanel, BoxLayout.Y_AXIS));

                        tabHotelPanel.add(tabHotelLowerPanel, gbc);

                        addClosableTab(selectedHotelName, tabHotelPanel);

                        tabHotelUpperPanel.add(hotelInfoPanel);
                        tabHotelUpperPanel.add(tabHotelUpperRightPanel);

                        // SIMULATE BOOKING
                        JLabel simulateBookingLabel = new JLabel("Simulate Booking");
                        simulateBookingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                        JButton bookingBtn = createBookingButton(selectedHotelName);

                        tabHotelLowerPanel.add(simulateBookingLabel);
                        tabHotelLowerPanel.add(Box.createVerticalStrut(10));
                        tabHotelLowerPanel.add(bookingBtn);
                        
                        tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1); // Switch to the newly added tab
                    }
                }
            }
        });


        // END OF TABBED  PANE




        // MANAGE HOTELS - LOWER RIGHT
        JPanel rightPanelLower = new JPanel();

        manageHotelBtn = new JButton("Manage Hotels");
        manageHotelBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanelLower.add(manageHotelBtn);

        mainFrame.add(leftPanelUpper);
        // mainFrame.add(rightPanelUpper);
        mainFrame.add(tabbedPane);
        mainFrame.add(rightPanelLower);
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
            int index = tabbedPane.indexOfComponent(component);
            if (index != -1) {
                tabbedPane.removeTabAt(index);
            }
        });
        tabComponent.add(closeButton); 
        
        // Add the custom component as the tab header
        tabbedPane.addTab(title, component);
        tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(component), tabComponent);
    }

    public void setDisplayInfoAndBooking() {
        // Display hotel name, total base rooms, total deluxe rooms, 
        // total executive rooms, and estimated earnings
        hotelInfoPanel = new JPanel();
        hotelInfoPanel.setLayout(new BoxLayout(hotelInfoPanel, BoxLayout.Y_AXIS));

        TitledBorder titledBorder = new TitledBorder("-" + selectedHotelName + " High-Level Info-");
        titledBorder.setTitleColor(new Color(0, 100, 0));
        hotelInfoPanel.setBorder(titledBorder);

        JLabel hotelNameLabel = new JLabel("Hotel Name: " + getSelectedHotelName());
        JLabel occupationStatusLabel = new JLabel("Total : Occupied");
        occupationStatusLabel.setOpaque(true);
        occupationStatusLabel.setBackground(Color.RED);
      
        JLabel totalBaseRoomsLabel = new JLabel("  |Base Rooms: " + getSelectedHotelRoomSize());
        JLabel totalDeluxeRoomsLabel = new JLabel("  |Deluxe Rooms: " + getSelectedHotelExecRoomsSize());
        JLabel totalExecutiveRoomsLabel = new JLabel("  |Executive Rooms: " + getSelectedHotelDeluxeRoomsSize());
        JLabel estimatedEarningsLabel = new JLabel("  |Estimated Earnings: " + getTotalHotelEarnings());

        hotelInfoPanel.add(hotelNameLabel);
        hotelInfoPanel.add(Box.createVerticalStrut(10));
        hotelInfoPanel.add(occupationStatusLabel);
        hotelInfoPanel.add(Box.createVerticalStrut(10));
        hotelInfoPanel.add(totalBaseRoomsLabel);
        hotelInfoPanel.add(Box.createVerticalStrut(10));
        hotelInfoPanel.add(totalDeluxeRoomsLabel);
        hotelInfoPanel.add(Box.createVerticalStrut(10)); 
        hotelInfoPanel.add(totalExecutiveRoomsLabel);
        hotelInfoPanel.add(Box.createVerticalStrut(10)); 
        hotelInfoPanel.add(estimatedEarningsLabel);
        hotelInfoPanel.add(Box.createVerticalGlue());
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

    private void setupHotelListClickMouseListener() {
        hotelJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedHotelIndex = hotelJList.locationToIndex(e.getPoint());
                if (selectedHotelIndex >= 0) {
                    Object item = hotelListModel.getElementAt(selectedHotelIndex);
                    // Perform your action with the selected item here
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


        setupHotelListClickMouseListener();
        getSelectedHotelIndex();

    }

    public void addComboBoxRoomTypes() {
        roomTypePanel = new JPanel();
        roomTypePanel.setLayout(new BoxLayout(roomTypePanel, BoxLayout.Y_AXIS));
        comboBox = new JComboBox<>(new String[] {"Base Room", "Deluxe Room", "Executive Room"});
        comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        comboBox.setMaximumSize(new Dimension(comboBox.getPreferredSize().width + 20, 
                                              comboBox.getPreferredSize().height));
        comboBox.setActionCommand("SELECT_ROOM_TYPE"); // For Controller
    }

    public void displayBookingFrame(String hotelName) {
        /* NEW WINDOW POP-UP FOR BOOKING */
        bookingFrame = new JFrame(hotelName + " Booking (Reservation)");
        setupBookingFrame(bookingFrame);

        JPanel bookingPanel = new JPanel();
        bookingPanel.setLayout(new BoxLayout(bookingPanel, BoxLayout.Y_AXIS));
        
        // For check-in date
        NumberFormat format = NumberFormat.getInstance(); // Step 1: Create a NumberFormat instance
        format.setGroupingUsed(false); // Disable comma grouping

        numberFormatter = new NumberFormatter(format) { // Step 2: Set up a NumberFormatter
            @Override
            public Object stringToValue(String string) throws ParseException {
                if (string == null || string.trim().isEmpty()) {
                    return null; // Return null for empty input
                }
                return super.stringToValue(string);
            }
        };
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setMinimum(1); // Minimum value
        numberFormatter.setMaximum(31); // Maximum value
        numberFormatter.setAllowsInvalid(false); // Don't allow invalid values

        // For check-out date
        format = NumberFormat.getInstance();
        format.setGroupingUsed(false);
        
        numberFormatter = new NumberFormatter(format) { // Step 2: Set up a NumberFormatter
            @Override
            public Object stringToValue(String string) throws ParseException {
                if (string == null || string.trim().isEmpty()) {
                    return null;
                }
                return super.stringToValue(string);
            }
        };
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setMinimum(1);
        numberFormatter.setMaximum(31);
        numberFormatter.setAllowsInvalid(false);
        
        guestNamePanel.add(guestLabel);
        guestNamePanel.add(guestNameField);
        roomTypePanel.add(comboBox);
        roomTypePanel.add(Box.createVerticalStrut(10));
        checkInPanel.add(checkInLabel);
        checkInPanel.add(checkInField);
        checkOutPanel.add(checkOutLabel);
        checkOutPanel.add(checkOutField);
        checkOutPanel.add(Box.createVerticalStrut(10));
        discountCodePanel.add(discountCodeLabel);
        discountCodePanel.add(discountCodeField);
        discountCodePanel.add(Box.createVerticalGlue());
        discountCodeFlowPanel.add(discountCodePanel);

        bookingPanel.add(guestNamePanel);
        bookingPanel.add(roomTypePanel);
        bookingPanel.add(checkInPanel);       
        bookingPanel.add(checkOutPanel);
        bookingPanel.add(discountCodeFlowPanel);
        bookingPanel.add(finalizeBookingBtn);
        bookingPanel.add(Box.createVerticalGlue());

        bookingFrame.add(bookingPanel);
    }

    public void updateHotelList(ArrayList<Hotel> hotels) {
        hotelListModel.clear();
        for (Hotel hotel : hotels) {
            hotelListModel.addElement(hotel);
        }
        setCreateBtnEnabled(false);
    }

    public void setActionListener(ActionListener listener) {
        createHotelBtn.addActionListener(listener);
        clearBtn.addActionListener(listener);
        // bookingBtn.addActionListener(listener);
        finalizeBookingBtn.addActionListener(listener);
        comboBox.addActionListener(listener);
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
        bookingFrame.setSize(300, 280);
        bookingFrame.setResizable(false);
        bookingFrame.setLocationRelativeTo(null);
        bookingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
    }

}
