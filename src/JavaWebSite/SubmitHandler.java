package JavaWebSite;

import java.io.IOException;

import JavaDataBase.Account;
import JavaDataBase.ManagerLayer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles submission of guides.
 */
class SubmitHandler implements HttpHandler {

    private final ManagerLayer database;

    /**
     * Constructs a JavaWebSite.SubmitHandler with the specified database manager.
     * 
     * @param database the ManagerLayer instance for database operations
     */
    public SubmitHandler(ManagerLayer database) {
        this.database = database;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                // Read the request body (form data)
                InputStream is = exchange.getRequestBody();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

                // Collect and decode form data
                String formData = reader.readLine();
                Map<String, String> parsedData = parseFormData(formData);

                // Validate the input data
                if (parsedData.get("name1") == null || parsedData.get("name2") == null || parsedData.get("range") == null) {
                    sendResponse(exchange, "Invalid submission. Please provide all required fields.", 400);
                    return;
                }

                // Extract data from parsed form
                String guideTitle = parsedData.get("name1");
                String guideContent = parsedData.get("name2");
                int difficulty = Integer.parseInt(parsedData.get("range")); // Assuming range is always valid

                String sessionId = UserLogin.extractSessionIdFromCookies(exchange);
                if(sessionId != null){ // Only creates guide if there's a cookie session(login session)
                    // Pass the data to database
                    database.createGuide(guideTitle, guideContent, database.getAccount(sessionId), difficulty);    
                    
                    // Respond with a success message
                    String response = "Guide \"" + guideTitle + "\" has been successfully submitted!";
                    sendResponse(exchange, response, 200);
                } else {
                    sendResponse(exchange, "Guide submission failed. Must be logged in!", 401);
                }

            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, "An error occurred while processing your request.", 500);
            }
        } else {
            sendResponse(exchange, "Method Not Allowed", 405); // Only POST allowed
        }
    }

    /**
     * Parses URL-encoded form data into a map.
     * 
     * @param formData the URL-encoded form data as a string
     * @return a map of form parameters
     * @throws IOException if an error occurs during decoding
     */
    private Map<String, String> parseFormData(String formData) throws IOException {
        Map<String, String> formParams = new HashMap<>();
        String[] pairs = formData.split("&");

        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
            String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
            formParams.put(key, value);
        }

        return formParams;
    }

    /**
     * Sends an HTTP response to the client.
     * 
     * @param exchange the HttpExchange object representing the request/response
     * @param responseText the response text to send
     * @param statusCode the HTTP status code to send
     * @throws IOException if an error occurs while sending the response
     */
    private void sendResponse(HttpExchange exchange, String responseText, int statusCode) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/html");
        exchange.sendResponseHeaders(statusCode, responseText.length());

        OutputStream os = exchange.getResponseBody();
        os.write(responseText.getBytes());
        os.close();
    }
}