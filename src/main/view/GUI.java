package view;

import javax.swing.*;
import javax.swing.border.Border;

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
    private JTextField hotelNameField, guestNameField;
    private JButton createHotelBtn, bookingBtn, manageHotelBtn, clearBtn;
    private JSlider slider1, slider2, slider3; // SLIDER IS TEMPORARY
    private JList<Hotel> hotelJList;
    private DefaultListModel<Hotel> hotelListModel;

    private final int MAX_TOTAL_ROOMS = 50;


    public GUI() {
        super("Hotel Management System");
        setSize(800, 600);
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 2));

        init(mainPanel); // Main program UI
        add(mainPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void init(JPanel mainPanel) {
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

        // TABBED PANE - LOWER LEFT
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel tabHotelListPanel = new JPanel();

        displayHotels(tabHotelListPanel);
        displayBookingPanel(tabHotelListPanel);

        tabbedPane.addTab("Hotel List", tabHotelListPanel);
        // END OF TABBED  PANE

        // MANAGE HOTELS - LOWER RIGHT
        JPanel rightPanelLower = new JPanel();

        manageHotelBtn = new JButton("Manage Hotels");
        manageHotelBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanelLower.add(manageHotelBtn);

        mainPanel.add(leftPanelUpper);
        // mainPanel.add(rightPanelUpper);
        mainPanel.add(tabbedPane);
        mainPanel.add(rightPanelLower);
    }

    public void displayHotels(JPanel tabHotelListPanel) {
        hotelListModel = new DefaultListModel<>();
        hotelJList     = new JList<>(hotelListModel);

        JScrollPane hotelListScrollPane = new JScrollPane(hotelJList);
        hotelListScrollPane.setPreferredSize(new Dimension(300, 150));
        tabHotelListPanel.add(hotelListScrollPane);
    }

    public void displayBookingPanel(JPanel tabHotelListPanel) {
        // SIMULATE BOOKING - UPPER RIGHT
        JPanel rightPanelUpper = new JPanel();
        rightPanelUpper.setLayout(new BoxLayout(rightPanelUpper, BoxLayout.Y_AXIS));

        JLabel simulateBookingLabel = new JLabel("Simulate Booking");
        simulateBookingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        bookingBtn = new JButton("Book a Room (Pick from Hotel List)");
        bookingBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookingBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, bookingBtn.getPreferredSize().height));
        bookingBtn.setActionCommand("BOOK_ROOM"); // For Controller

        rightPanelUpper.add(simulateBookingLabel);
        rightPanelUpper.add(Box.createVerticalStrut(10));
        rightPanelUpper.add(bookingBtn);

        /* NEW WINDOW POP-UP FOR BOOKING */
        JFrame bookingFrame = new JFrame("Book Room");
        setupBookingButtonActionListener(bookingFrame);

        JPanel bookingPanel = new JPanel();
        bookingPanel.setLayout(new BoxLayout(bookingPanel, BoxLayout.Y_AXIS));

        // GUEST NAME TEXT FIELD
        JPanel guestNamePanel = new JPanel();
        guestNamePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel guestLabel = new JLabel("Guest Name: ");
        guestNameField = new JTextField(20);

        // LIST OF TYPES OF ROOMS
        JPanel roomTypePanel = new JPanel();
        roomTypePanel.setLayout(new BoxLayout(roomTypePanel, BoxLayout.Y_AXIS));
        JComboBox<String> comboBox = new JComboBox<>(new String[] {"Base Room", "Deluxe Room", "Executive Room"});
        comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        comboBox.setMaximumSize(new Dimension(comboBox.getPreferredSize().width + 20, comboBox.getPreferredSize().height));

        // CHECK-IN DATE
        JPanel checkInPanel = new JPanel();
        checkInPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 1));

        
        JLabel checkInLabel = new JLabel("   Check-In Date: ");
        
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
        numberFormatter.setMinimum(1); // Minimum value
        numberFormatter.setMaximum(31); // Maximum value
        numberFormatter.setAllowsInvalid(false); // Don't allow invalid values

        // Step 3: Create a JFormattedTextField using the NumberFormatter
        JFormattedTextField checkInField = new JFormattedTextField(numberFormatter);
        checkInField.setColumns(5); // Set the column size


        // CHECK-OUT DATE
        JPanel checkOutPanel = new JPanel();
        checkOutPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 1));
        JLabel checkOutLabel = new JLabel("Check-Out Date: ");

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

        JFormattedTextField checkOutField = new JFormattedTextField(numberFormatter);
        checkOutField.setColumns(5);
        

        // BOOK BUTTON
        JButton bookBtn = new JButton("BOOK");
        bookBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookBtn.setActionCommand("BOOK_ROOM"); // For Controller

        setupBookingButtonActionListener(bookingFrame);
        
        guestNamePanel.add(guestLabel);
        guestNamePanel.add(guestNameField);
        roomTypePanel.add(comboBox);
        roomTypePanel.add(Box.createVerticalStrut(10));
        checkInPanel.add(checkInLabel);
        checkInPanel.add(checkInField);

        checkOutPanel.add(checkOutLabel);
        checkOutPanel.add(checkOutField);

        bookingPanel.add(guestNamePanel);
        bookingPanel.add(roomTypePanel);
        bookingPanel.add(checkInPanel);       
        bookingPanel.add(checkOutPanel);
        bookingPanel.add(bookBtn);
        bookingPanel.add(Box.createVerticalGlue());

        bookingFrame.add(bookingPanel);

        tabHotelListPanel.add(rightPanelUpper);
    }

    public void updateHotelList(ArrayList<Hotel> hotels) {
        hotelListModel.clear();
        for (Hotel hotel : hotels) {
            hotelListModel.addElement(hotel);
        }
    }

    public void setActionListener(ActionListener listener) {
        createHotelBtn.addActionListener(listener);
        clearBtn.addActionListener(listener);
    }

    public void setDocumentListener(DocumentListener listener) {
        hotelNameField.getDocument().addDocumentListener(listener);
    }

    private void setupHotelNameFieldFocusListener() {
        hotelNameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // Code to execute when component gains focus
                hotelNameField.setText("");
                createHotelBtn.setEnabled(true);
                clearBtn.setEnabled(true);
            }

            @Override
            public void focusLost(FocusEvent e) {
                // Code to execute when component loses focus
                if (hotelNameField.getText().isEmpty()) {
                    hotelNameField.setText("Enter Hotel Name...");
                    createHotelBtn.setEnabled(false);
                    clearBtn.setEnabled(false);
                }
            }
        });
    }

    private void setupBookingButtonActionListener(JFrame bookingFrame) {
        // Make a new window pop up when the button is clicked
        bookingBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookingFrame.setSize(300, 200);
                bookingFrame.setLocationRelativeTo(null);
                bookingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                bookingFrame.setVisible(true);
            }
        });
    }

    public void setCreateBtnEnabled(boolean enabled) {
        createHotelBtn.setEnabled(enabled);
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

    public void clearTextFields() {
        hotelNameField.setText("");
        slider1.setValue(1);
        slider2.setValue(0);
        slider3.setValue(0);
    }
}
