package utils;

import java.util.Scanner;

public class ScannerUtil {
    private static final Scanner sc = new Scanner(System.in);

    public static int readInt() {
        while (true) {
            if (sc.hasNextInt()) {
                int value = sc.nextInt();
                sc.nextLine(); // Consume the newline
                return value;
            } 
            else {
                System.out.println("Invalid input. Please enter an integer.");
                sc.nextLine(); // Consume the invalid input
            }
        }
    }

    public static double readDouble() {
        while (true) {
            if (sc.hasNextDouble()) {
                double value = sc.nextDouble();
                sc.nextLine(); // Consume the newline
                return value;
            } 
            else {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); // Consume the invalid input
            }
        }
    }

    public static float readFloat() {
        while (true) {
            if (sc.hasNextFloat()) {
                float value = sc.nextFloat();
                sc.nextLine(); // Consume the newline
                return value;
            } 
            else {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); // Consume the invalid input
            }
        }
    }

    public static String readString() {
        return sc.nextLine();
    }

    public static boolean readBoolean() {
        while (true) {
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("yes"))
                return true;
            else if (input.equalsIgnoreCase("no"))
                return false;
            else
                System.out.println("Invalid input. Please enter \"Yes\" or \"No\".");
        }
    }

    public static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt();
            if (value >= min && value <= max)
                return value;
            else
                System.out.println("Please enter a number between " + min 
                                    + " and " + max + " (inclusive).");
        }
    }

    public static void closeScanner() {
        sc.close();
    }
}