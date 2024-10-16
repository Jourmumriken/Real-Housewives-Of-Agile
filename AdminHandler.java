
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import src.JavaDataBase.Guide;
import src.JavaDataBase.ManagerLayer;
import src.JavaWebSite.UserLogin;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import java.net.HttpCookie;

import java.io.BufferedReader;
import java.io.FileReader;


public class AdminHandler implements HttpHandler {

    private ManagerLayer database;
    private UserLogin userLogin; 
    
    

    public AdminHandler(ManagerLayer database, UserLogin inLogin) {
        this.database = database;
        this.userLogin = inLogin; 
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
       
        // Check if user is logged in and has admin privileges
       /* if (!isLoggedIn(exchange)) {
            // If not logged in, redirect to login page
            exchange.getResponseHeaders().set("Location", "/login");
            exchange.sendResponseHeaders(302, -1);  // 302 Redirect
            return;
        } */

         

        

        // Serve the admin page if the user is logged in and is an admin
        String response = null;
        try {
            response = insertGuidesIntoAdminPage(readHtmlAsString());
        } catch (AccountNotFoundException | DatabaseConnectionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        exchange.getResponseHeaders().set("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    // Check if the user is logged in by validating the session
    private boolean isLoggedIn(HttpExchange exchange) throws IOException{
        List<String> cookies = exchange.getRequestHeaders().get("Cookie");
        if (cookies != null) {
            for (String cookie : cookies) {
                HttpCookie parsedCookie = HttpCookie.parse(cookie).get(0);
                //logic to implment but now only returns true 
                return true;  
            }
        }
        return false;  // No valid session found
    }

    private String readHtmlAsString() {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new FileReader("html/admin.html"))) {
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return contentBuilder.toString();
    }

    private String insertGuidesIntoAdminPage(String htmlContent) throws AccountNotFoundException, DatabaseConnectionException {
        // Find the position of the <ul id="item-container"> tag
        String ulStartTag = "<ul id=\"item-container\">";
        // Find where the tag ends
        String ulEndTag = "</ul>";
        int ulStartIndex = htmlContent.indexOf(ulStartTag);
        int ulEndIndex = htmlContent.indexOf(ulEndTag, ulStartIndex);
        database.getAllGuidesFromUser(userLogin.getSessionID()); 

        // If the container exists:
        if (ulStartIndex != -1 && ulEndIndex != -1) {
            // Build the new <li> elements with the guide information
            StringBuilder guidesList = new StringBuilder();
            // Add href:s to guides by unique ID and the Title.
            for (Guide guide : this.database.getAllGuidesFromUser(userLogin.getSessionID())) {
                guidesList.append("<li><a href=\"/guide?id=")
                        .append(guide.getId())
                        .append("\">")
                        .append(guide.getTitle())
                        .append("</a></li>\n");
            }

            // Insert the new guide list into the <ul> section
            String beforeUl = htmlContent.substring(0, ulStartIndex + ulStartTag.length());
            String afterUl = htmlContent.substring(ulEndIndex);

            // Return the modified HTML content
            return beforeUl + "\n" + guidesList.toString() + afterUl;
        } else {
            System.out.println("Could not find <ul id=\"item-container\"> in the HTML content.");
            return htmlContent; // Return original content if the <ul> section isn't found
        }
    }

    
}
