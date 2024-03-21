package org.chatbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class represents a chatbot that can respond to user requests.
 */
public class Chatbot {
    private static final Logger logger = Logger.getLogger(Chatbot.class.getName());

    // Constants for URLs and API keys
    private static final String GEOLOCATION_URL = "https://geolocation-db.com/json/";
    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String OPENWEATHERMAP_API_KEY = "34e1152d67a185532ca8f805c460c8ae";
    private static final String UNABLE_TO_LOCATION = "Sorry, unable to get your location";
    private static final String UNABLE_TO_WEATHER = "Sorry, unable to get the temperature";
    private static final String EXIT_MESSAGE = "See you later %s!";
    private static final String TIME_MESSAGE = "The current date time is: %s";
    private static final String LOCATION_MESSAGE = "You are located approximately at: %s";
    private static final String WEATHER_MESSAGE = "The current temperature is: %s°C";
    private static final String UNKNOWN_MESSAGE = "Sorry, I didn't get that. Can you please repeat?";
    private static final String WELCOME_MESSAGE = "Hi! I am Phoebe, your bot assistance. What is your name?";
    private static final String GREETING_MESSAGE = "Hi, %s. How can I help you today?";

    /**
     * The main method that starts the chatbot.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(WELCOME_MESSAGE);
        String userName = scanner.nextLine();

        System.out.printf((GREETING_MESSAGE) + "%n", userName);

        // Create a ChatbotController object and call its processRequest() method to handle user requests.
        ChatbotController botController = new ChatbotController(userName, scanner);
        botController.processRequest();

        scanner.close();
    }

    /**
         * This class handles user requests.
         */
        private record ChatbotController(String userName, Scanner scanner) {

        /**
             * Processes the user's request until the user types "exit" or "bye".
             */
            public void processRequest() {
                while (true) {
                    String userEntrance = scanner.nextLine().toLowerCase();

                    if (userEntrance.equalsIgnoreCase("exit") || userEntrance.equalsIgnoreCase("bye")) {
                        System.out.printf(EXIT_MESSAGE + "%n", userName);
                        break;
                    }

                    String response = getResponse(userEntrance);
                    System.out.println(response);
                }
            }

            /**
             * Retrieves the bot's response based on the user's request.
             */
            private static String getResponse(String userEntrance) {
                if (userEntrance.contains("time")) {
                    String currentDateTime = getCurrentDateTime();
                    return String.format(TIME_MESSAGE, currentDateTime);
                } else if (userEntrance.contains("locate")) {
                    String location = getLocation();
                    return String.format(LOCATION_MESSAGE, location);
                } else if (userEntrance.contains("weather")) {
                    String temperature = getTemperature();
                    return String.format(WEATHER_MESSAGE, temperature);
                } else {
                    return UNKNOWN_MESSAGE;
                }
            }

            /**
             * Retrieves the current date and time in a specific format.
             */
            private static String getCurrentDateTime() {
                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return currentDateTime.format(formatter);
            }

            /**
             * Retrieves the location of the user based on their IP address.
             */
            private static String getLocation() {
                URI uri = URI.create(GEOLOCATION_URL);

                try {
                    HttpURLConnection con = (HttpURLConnection) uri.toURL().openConnection();
                    con.setRequestMethod("GET");

                    try (InputStream in = con.getInputStream();
                         BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

                        JSONParser parser = new JSONParser();
                        JSONObject response = (JSONObject) parser.parse(reader.readLine());

                        String city = (String) response.get("city");
                        String country = (String) response.get("country_name");
                        return city + ", " + country;
                    }

                } catch (IOException | ParseException e) {
                    logger.log(Level.WARNING, "Error getting location", e);
                    return UNABLE_TO_LOCATION;
                }
            }

            /**
             * Retrieves the current temperature in the user's location.
             */
            private static String getTemperature() {
                String location = getLocation();
                String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8);

                URI uri = URI.create(WEATHER_API_URL
                        + "?q=" + encodedLocation
                        + "&appid=" + OPENWEATHERMAP_API_KEY
                        + "&units=metric");

                try {
                    HttpURLConnection con = (HttpURLConnection) uri.toURL().openConnection();
                    con.setRequestMethod("GET");

                    try (InputStream in = con.getInputStream();
                         BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

                        JSONParser parser = new JSONParser();
                        JSONObject response = (JSONObject) parser.parse(reader.readLine());

                        JSONObject main = (JSONObject) response.get("main");
                        double temperature = (double) main.get("temp");
                        return String.valueOf(temperature);
                    }

                } catch (IOException | ParseException e) {
                    logger.log(Level.WARNING, "Error getting temperature", e);
                    return UNABLE_TO_WEATHER;
                }
            }
        }
}

