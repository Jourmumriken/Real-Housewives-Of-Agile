package JavaWebSite;
import JavaDataBase.Account;
import JavaDataBase.ManagerLayer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import JavaDataBase.Exceptions.*;




/**
 * Handles user login requests and manages authentication.
 */
public class LoginHandler implements HttpHandler{

    private String username;
    private String password;
    private ManagerLayer database;
    UserLogin userLogin;
    Account account;
    public final Logger logger = Logger.getLogger(MultiPageHttpServer.class.getName());


    /**
     * Constructs a JavaWebSite.LoginHandler with the specified ManagerLayer.
     *
     * @param database The manager layer responsible for database operations.
     */
    public LoginHandler(ManagerLayer database){
        this.database = database;

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {

            System.out.print("inside of Login Handler");

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
            String response = "Not initlized";
            try {

                logger.info("UserName input " + username + "Passowrd " + password);

                if(database.correctPw(username, password)) {

                    this.account = database.getAccount(username);
                    UserLogin userLogin = new UserLogin(account);

                    exchange.getResponseHeaders().add("Set-Cookie", userLogin.getCookie().toString());

                    response = "Login successful!";
                    // we need to send further to the protected site
                    logger.info("Login successful!");

                    // Redirect to index(root) page
                    sendResponse(exchange, response, 302, "/");



                } else {
                    response = "Invalid password.";
                    // Redirect to login page again, if account exists but password was wrong.
                    sendResponse(exchange, response,  302, "/login?error=invalid");
                }
            } catch (AccountNotFoundException e) {
                response = "Account does not exist.";
                // Redirect to login page again, if account does not exist.
                sendResponse(exchange, response, 302, "/login?error=notfound");
            }

            // Send response back to client
            exchange.sendResponseHeaders(200, response.length());
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

    private void sendResponse(HttpExchange exchange, String responseText, int statusCode, String locationURL) throws IOException {
        exchange.getResponseHeaders().set("Location", locationURL);
        exchange.getResponseHeaders().set("Cache-Control", "no-store, no-cache, must-revalidate");


        //exchange.getResponseHeaders().set("responseText", locationURL);
        exchange.sendResponseHeaders(statusCode, -1);

        OutputStream os = exchange.getResponseBody();
        //os.write(responseText.getBytes());
        os.close();
    }


}
