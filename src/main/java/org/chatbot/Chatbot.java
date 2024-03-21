package org.chatbot;

import java.util.Scanner;
import java.util.logging.Logger;
import org.chatbot.controller.ChatbotController;

/**
 * This class represents a chatbot that can respond to user requests.
 */
public class Chatbot {
    public static final Logger logger = Logger.getLogger(Chatbot.class.getName());

    // Constants for URLs and API keys
    public static final String GEOLOCATION_URL = "https://geolocation-db.com/json/";
    public static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather";
    public static final String OPENWEATHERMAP_API_KEY = "34e1152d67a185532ca8f805c460c8ae";
    public static final String UNABLE_TO_LOCATION = "Sorry, unable to get your location";
    public static final String UNABLE_TO_WEATHER = "Sorry, unable to get the temperature";
    public static final String EXIT_MESSAGE = "See you later %s!";
    public static final String TIME_MESSAGE = "The current date time is: %s";
    public static final String LOCATION_MESSAGE = "You are located approximately at: %s";
    public static final String WEATHER_MESSAGE = "The current temperature is: %s°C";
    public static final String UNKNOWN_MESSAGE = "Sorry, I didn't get that. Can you please repeat?";
    public static final String WELCOME_MESSAGE = "Hi! I am Phoebe, your bot assistance. What is your name?";
    public static final String GREETING_MESSAGE = "Hi, %s. How can I help you today?";

    /**
     * The main method that starts the chatbot.
     */
    public static void main(String[] args)  {
        Scanner scanner = new Scanner(System.in);

        System.out.println(WELCOME_MESSAGE);
        String userName = scanner.nextLine();

        System.out.printf((GREETING_MESSAGE) + "%n", userName);

        // Create a ChatbotController object and call its processRequest() method to handle user requests.
        ChatbotController botController = new ChatbotController(userName, scanner);
        botController.processRequest();

        scanner.close();
    }
}

