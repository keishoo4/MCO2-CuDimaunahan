package view;

import javax.swing.*;
import java.awt.*;

import javax.swing.event.*;

import model.hotel.Hotel;

import java.awt.event.*;

import java.util.ArrayList;

public class GUI extends JFrame {
    private JTextField hotelNameField;
    private JButton createHotelButton;
    private JSlider slider;
    private JList<Hotel> hotelJList;
    private DefaultListModel<Hotel> hotelListModel;


    public GUI() {
        super("Hotel Management System");
        setSize(800, 600);
        setMinimumSize(new Dimension(400, 300));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 2));

        init(mainPanel); // Main program UI
        add(mainPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void init(JPanel mainPanel) {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(2, 1));

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
        JPanel sliderAndCreatePanel = new JPanel();
        sliderAndCreatePanel.setLayout(new BorderLayout());

        JLabel sliderValueLabel = new JLabel("Allocate Room(s): 1");
        JPanel sliderPanel = new JPanel(new BorderLayout());
        slider = new JSlider(1, 50, 1);
        sliderPanel.add(sliderValueLabel, BorderLayout.NORTH);
        sliderPanel.add(slider, BorderLayout.CENTER);
        slider.addChangeListener(e -> sliderValueLabel.setText("Allocate Room(s): " 
                                 + ((JSlider)e.getSource()).getValue()));

        sliderAndCreatePanel.add(sliderPanel, BorderLayout.NORTH);

        // BUTTONS
        JPanel mainButtons = new JPanel();
        mainButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        // Create Hotel Button
        createHotelButton = new JButton("Create Hotel");
        mainButtons.add(createHotelButton);
        // Clear Text Field Button
        JButton clearButton = new JButton("Clear");
        mainButtons.add(clearButton);

        sliderAndCreatePanel.add(mainButtons, BorderLayout.CENTER);

        leftPanelUpper.add(sliderAndCreatePanel);
        leftPanel.add(leftPanelUpper);

        // TABBED PANE
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel tabHotelListPanel = new JPanel();

        displayHotels(tabHotelListPanel);

        


        tabbedPane.addTab("Hotel List", tabHotelListPanel);



        mainPanel.add(leftPanel);
        mainPanel.add(tabbedPane); // Add the rightPanel to the mainPanel
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
        return slider.getValue(); // Placeholder
    }

    public void setHotelName(String hotelName) {
        hotelNameField.setText(hotelName);

    }

    public String getHotelName() {
        return hotelNameField.getText();
    }




}