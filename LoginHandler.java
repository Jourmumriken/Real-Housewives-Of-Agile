import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles user login requests and manages authentication.
 */
public class LoginHandler implements HttpHandler{

    private String username; 
    private String password; 
    private ManagerLayer database; 
    UserLogin userLogin; 
    Account account; 


    /**
     * Constructs a LoginHandler with the specified ManagerLayer.
     *
     * @param database The manager layer responsible for database operations.
     */
    public LoginHandler(ManagerLayer database){
        this.database = database; 
         
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            System.out.print("I am here");
            InputStream requestBody = exchange.getRequestBody();
            byte[] data = requestBody.readAllBytes();
            String requestBodyString = new String(data, StandardCharsets.UTF_8);
            // Parse form data
            Map<String, String> parameters = parseFormData(requestBodyString);

            // login validation
            username = parameters.get("username");
            password = parameters.get("password");

            //System.out.print(userName + "," + password);

            
            // validation logic
            String response;

            if (account.checkPw(password)) {
                userLogin = new UserLogin(account);
                //exchange.getResponseHeaders().add("Set-Cookie", userLogin.cookie.toString());
                response = "Login successful!";
                System.out.println(response);
                //exchange.getResponseHeaders().set("Location", "/admin.html"); 
               // exchange.sendResponseHeaders(302, -1);
                
            } else {
                response = "Invalid username or password.";
            }

             

            // Send response back to client
            //exchange.sendResponseHeaders(200, response.length());
            
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }


    /**
     * Parses the form data from the request body into a map of parameters.
     *
     * @param body The request body containing form data.
     * @return A map of parameter names and values.
     */
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
    
}
