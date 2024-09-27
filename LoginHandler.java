import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LoginHandler implements HttpHandler {

    private String userName; 
    private String password; 
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            InputStream requestBody = exchange.getRequestBody();
            byte[] data = requestBody.readAllBytes();
            String requestBodyString = new String(data, StandardCharsets.UTF_8);

            // Parse form data
            Map<String, String> parameters = parseFormData(requestBodyString);

            // login validation
            userName = parameters.get("username");
            password = parameters.get("password");

            // validation logic
            String response;
            if ("admin".equals(userName) && "password".equals(password)) {
                response = "Login successful!";
            } else {
                response = "Invalid username or password.";
            }

            System.out.print(userName + "," + password);

            Connection(); 

            // Send response back to client
            exchange.sendResponseHeaders(200, response.length());
            
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }

    private Map<String, String> parseFormData(String body) {
        Map<String, String> parameters = new HashMap<>();
        String[] pairs = body.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length > 1) {
                parameters.put(keyValue[0], keyValue[1]);
            } else {
                parameters.put(keyValue[0], "");
            }
        }
        return parameters;
    }

    // test method 

    private void Connection(){

        Account myAccount = new Account(userName, password); 

  


    }
}
