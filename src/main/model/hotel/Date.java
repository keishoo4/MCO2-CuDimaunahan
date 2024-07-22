package model.hotel;

public class Date {
    private int day;
    private double datePrice;

    public Date(int day, double datePrice) {
        this.day = day;
        this.datePrice = datePrice;
        // datePriceModifiers = new double[31]; // Initialize the array
        // // Initialize array with default values if needed
        // for (int i = 0; i < datePriceModifiers.length; i++) {
        // datePriceModifiers[i] = 1.0; // Default modifier
        // }
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public double getDatePrice() {
        return datePrice;
    }

    // private static double[] datePriceModifiers; // Array for storing the price modifiers 

    // public static void promptDatePriceModifier(Hotel hotel) {
    //     if (hotel.reservationStatus() == true) {
    //         System.out.println("Cannot modify price with reservation(s).\n");
    //         return;
    //     }
        
    //     System.out.print("Enter the day of the month (1-31): ");
    //     int day = ScannerUtil.readInt();
    //     if (day < 1 || day > 31) {
    //         System.out.println("Invalid day. Please enter a value between 1 and 31.");
    //         return;
    //     }
        
    //     System.out.print("Enter the price modifier for day " + day + " in percentage.");
    //     double priceModifier = ScannerUtil.readDouble();
        
    //     if (priceModifier <= 0 || priceModifier > 150 || priceModifier < 50) {
    //         System.out.println("Invalid price modifier.");
    //         return;
    //     }

    //     setDatePriceModifier(day, priceModifier/100);
    //     System.out.println("Price modifier for day " + day + " updated to " + priceModifier + ".");
    // }

    // public static void setDatePriceModifier(int day, double priceModifier) {
    //     if (day < 1 || day > 31) {
    //         System.out.println("Invalid day! Please enter a day between 1 and 31.");
    //         return;
    //     }
    //     datePriceModifiers[day - 1] = priceModifier/100; // sets day in the array to its price modifier 
    // }

    // public double getDateModifier(int day) {
    //     // Ensure day is within the valid range
    //     if (day < 1 || day > 31) {
    //         System.out.println("Invalid day! Please enter a day between 1 and 31.");
    //         return 1.0; // Default modifier if day is invalid
    //     }
    //         // Return the date price modifier for the specified day
    //         return datePriceModifiers[day - 1]; 
    // }

    // public void setDatePrice(double datePrice, int day, String discountCode, Room room) {
    //     double discountMultiplier = room.getDiscountCode(discountCode);
    //     double dateModifier = getDateModifier(day);
    //     this.datePrice = datePrice * discountMultiplier * dateModifier;
    // }
}