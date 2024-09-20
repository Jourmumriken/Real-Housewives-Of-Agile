import java.io.File;
import java.io.IOException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import java.io.OutputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


class SubmitHandler implements HttpHandler {
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            // Read the request body (form data)
            InputStream is = exchange.getRequestBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            // Collect and decode form data
            String formData = reader.readLine();
            Map<String, String> parsedData = parseFormData(formData);

            // Respond with a confirmation message
            String response = "Thank you, " + parsedData.get("name1") + "! We have received your submission.<br>" +
                              "Text2: " + parsedData.get("name2");

            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, response.length());

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
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
}