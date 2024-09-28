import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//import java.net.HttpCookie;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RegisterHandler implements HttpHandler{

    private String userName; 
    private String password; 
    private ManagerLayer database; 
    UserLogin userLogin; 
    Account account; 


    RegisterHandler(ManagerLayer database){
        this.database = database; 
         
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            System.out.println("Inside of RegisterHandler");
            InputStream requestBody = exchange.getRequestBody();
            byte[] data = requestBody.readAllBytes();
            String requestBodyString = new String(data, StandardCharsets.UTF_8);
            // Parse form data
            Map<String, String> parameters = parseFormData(requestBodyString);

            // login validation
            userName = parameters.get("username");
            password = parameters.get("password");

            //System.out.print(userName + "," + password);

            
            // validation logic
            String response = "Not Initlized";

            try{
                database.createAccount(userName, password);
                //exchange.getResponseHeaders().add("Set-Cookie", userLogin.cookie.toString());
                response = "Register successful!";
                //System.out.println(response);
                //exchange.getResponseHeaders().set("Location", "/admin.html"); 
                //exchange.sendResponseHeaders(302, -1);
            } catch (AccountCreationException e){
                response = "Username is already occupied.";
                //System.out.println(response);
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