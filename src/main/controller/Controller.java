package controller;

import model.hotel.HotelList;
import view.View;

public class Controller {
    HotelList hotelList;
    View view;

    public Controller(HotelList hotelList, View view) {
        this.hotelList = hotelList;
        this.view = view;

        
    }
}
