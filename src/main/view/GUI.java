package view;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;

import javax.swing.event.*;

import model.hotel.Hotel;

import java.awt.event.*;

import java.util.ArrayList;

public class GUI extends JFrame {
    private JTextField hotelNameField;
    private JButton createHotelButton, bookingBtn, manageHotelBtn;
    private JSlider slider1, slider2, slider3; // SLIDER IS TEMPORARY
    private JList<Hotel> hotelJList;
    private DefaultListModel<Hotel> hotelListModel;

    private final int MAX_TOTAL_ROOMS = 50;


    public GUI() {
        super("Hotel Management System");
        setSize(800, 600);
        setMinimumSize(new Dimension(400, 300));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 2));

        init(mainPanel); // Main program UI
        add(mainPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void init(JPanel mainPanel) {
        // JPanel leftPanel = new JPanel();
        // leftPanel.setLayout(new GridLayout(2, 1));

        JPanel leftPanelUpper = new JPanel();
        leftPanelUpper.setLayout(new BoxLayout(leftPanelUpper, BoxLayout.Y_AXIS));

        // HOTEL NAME TEXT FIELD
        JPanel hotelNameAndRoomPanel = new JPanel();
        hotelNameAndRoomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel hotelNameLabel = new JLabel("Hotel Name: ");
        hotelNameField = new JTextField(20);
        hotelNameAndRoomPanel.add(hotelNameLabel);
        hotelNameAndRoomPanel.add(hotelNameField);
        leftPanelUpper.add(hotelNameAndRoomPanel);

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
            int totalRooms = normalRooms + deluxeRooms + executiveRooms;
            
            int maxDeluxe = Math.min((int) Math.floor(normalRooms * 0.6), MAX_TOTAL_ROOMS - normalRooms);
            int maxExecutive = Math.min((int) Math.floor(normalRooms * 0.4), MAX_TOTAL_ROOMS - normalRooms - deluxeRooms);
            
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
        sliderRooms.add(Box.createVerticalGlue());

        // BUTTONS
        JPanel mainButtons = new JPanel();
        mainButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        // Create Hotel Button
        createHotelButton = new JButton("Create Hotel");
        mainButtons.add(createHotelButton);
        // Clear Text Field Button
        JButton clearButton = new JButton("Clear");
        mainButtons.add(clearButton);

        leftPanelUpper.add(sliderRooms);
        leftPanelUpper.add(mainButtons);

        // SIMULATE BOOKING - UPPER RIGHT
        JPanel rightPanelUpper = new JPanel();
        rightPanelUpper.setLayout(new BoxLayout(rightPanelUpper, BoxLayout.Y_AXIS));

        JLabel simulateBookingLabel = new JLabel("Simulate Booking");
        simulateBookingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanelUpper.add(simulateBookingLabel);

        rightPanelUpper.add(Box.createVerticalStrut(10));

        bookingBtn = new JButton("Book a Room");
        bookingBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookingBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, bookingBtn.getPreferredSize().height));
        rightPanelUpper.add(bookingBtn);

        // MANAGE HOTELS - LOWER RIGHT
        JPanel rightPanelLower = new JPanel();

        manageHotelBtn = new JButton("Manage Hotels");
        manageHotelBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanelLower.add(manageHotelBtn);




        // TABBED PANE - LOWER LEFT
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel tabHotelListPanel = new JPanel();

        displayHotels(tabHotelListPanel);

        tabbedPane.addTab("Hotel List", tabHotelListPanel);

        mainPanel.add(leftPanelUpper);
        mainPanel.add(rightPanelUpper);
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

    public void updateHotelList(ArrayList<Hotel> hotels) {
        hotelListModel.clear();
        for (Hotel hotel : hotels) {
            hotelListModel.addElement(hotel);
        }
    }

    public void setActionListener(ActionListener listener) {
        createHotelButton.addActionListener(listener);
    }

    public void setDocumentListener(DocumentListener listener) {
        hotelNameField.getDocument().addDocumentListener(listener);
    }






    public int getSliderValue() {
        return slider1.getValue(); // Placeholder
    }

    public void setHotelName(String hotelName) {
        hotelNameField.setText(hotelName);

    }

    public String getHotelName() {
        return hotelNameField.getText();
    }




}
