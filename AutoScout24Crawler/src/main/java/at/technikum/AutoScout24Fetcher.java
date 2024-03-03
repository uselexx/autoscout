package at.technikum;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AutoScout24Fetcher {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GET_URL = "https://api.autoscout24.com/path-to-resource"; // Replace with the actual API URL
    private static final String API_KEY = "your_api_key_here"; // Replace with your actual API key

    public static void main(String[] args) throws Exception {
        sendGetRequest();
    }

    private static void sendGetRequest() throws Exception {
        URL obj = new URL(GET_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Authorization", "Bearer " + API_KEY);

        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
        } else {
            System.out.println("GET request not worked");
        }
    }
}
