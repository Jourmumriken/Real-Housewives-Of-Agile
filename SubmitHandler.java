import java.io.IOException;
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

class SubmitHandler implements HttpHandler {

    private final ManagerLayer database;

    // Constructor to inject ManagerLayer
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

                // Use a hardcoded account TODO: (this will be replaced later with dynamic user accounts)
                Account hardcodedAccount = database.getAccount("test"); // Assumes "test" user exists.

                // Pass the data to database
                database.createGuide(guideTitle, guideContent, hardcodedAccount, difficulty);

                // Respond with a success message
                String response = "Guide \"" + guideTitle + "\" has been successfully submitted!";
                sendResponse(exchange, response, 200);
            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, "An error occurred while processing your request.", 500);
            }
        } else {
            sendResponse(exchange, "Method Not Allowed", 405); // Only POST allowed
        }
    }

    // Helper method to parse URL-encoded form data to people langague
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

    // Helper method to send an HTTP response
    private void sendResponse(HttpExchange exchange, String responseText, int statusCode) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/html");
        exchange.sendResponseHeaders(statusCode, responseText.length());

        OutputStream os = exchange.getResponseBody();
        os.write(responseText.getBytes());
        os.close();
    }
}