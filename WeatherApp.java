import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherApp {
    private static final String API_BASE_URL = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            printMenu();
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid option.");
                scanner.nextLine(); // Consume the invalid input
            }
            option = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (option) {
                case 1:
                    getWeather(scanner);
                    break;
                case 2:
                    getWindSpeed(scanner);
                    break;
                case 3:
                    getPressure(scanner);
                    break;
                case 0:
                    System.out.println("Exiting the program...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        } while (option != 0);

        scanner.close();
    }

 private static void printMenu() {
        System.out.println("\n=== Weather App ===");
        System.out.println("Please choose an option:");
        System.out.println("1. Get weather");
        System.out.println("2. Get Wind Speed");
        System.out.println("3. Get Pressure");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }


    private static void getWeather(Scanner scanner) {
        System.out.println("\nEnter the date (YYYY-MM-DD HH:mm:ss):");
        String date = scanner.nextLine();

        String response = fetchDataFromAPI();
        if (response != null) {
            if (response.contains("\"dt_txt\":\"" + date + "\"")) {
                double temperature = extractDataFromResponse(response, "\"temp\":");
                System.out.println("\nTemperature on " + date + ": " + temperature + "°C");
            } else {
                System.out.println("\nNo weather data found for the given date.");
            }
        } else {
            System.out.println("\nError fetching data from the API.");
        }
    }

    private static void getWindSpeed(Scanner scanner) {
        System.out.println("\nEnter the date (YYYY-MM-DD HH:mm:ss):");
        String date = scanner.nextLine();

        String response = fetchDataFromAPI();
        if (response != null) {
            if (response.contains("\"dt_txt\":\"" + date + "\"")) {
                double windSpeed = extractDataFromResponse(response, "\"speed\":");
                System.out.println("\nWind speed on " + date + ": " + windSpeed + " m/s");
            } else {
                System.out.println("\nNo wind speed data found for the given date.");
            }
        } else {
            System.out.println("\nError fetching data from the API.");
        }
    }

    private static void getPressure(Scanner scanner) {
        System.out.println("\nEnter the date (YYYY-MM-DD HH:mm:ss):");
        String date = scanner.nextLine();

        String response = fetchDataFromAPI();
        if (response != null) {
            if (response.contains("\"dt_txt\":\"" + date + "\"")) {
                double pressure = extractDataFromResponse(response, "\"pressure\":");
                System.out.println("\nPressure on " + date + ": " + pressure + " hPa");
            } else {
                System.out.println("\nNo pressure data found for the given date.");
            }
        } else {
            System.out.println("\nError fetching data from the API.");
        }
    }

    private static String fetchDataFromAPI() {
        try {
            URL url = new URL(API_BASE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() != 200) {
                System.out.println("Error: " + connection.getResponseMessage());
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static double extractDataFromResponse(String response, String key) {
        int startIdx = response.indexOf(key) + key.length();
        int endIdx = response.indexOf(",", startIdx);
        return Double.parseDouble(response.substring(startIdx, endIdx));
    }
}
