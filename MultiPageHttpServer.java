
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;

import java.net.InetSocketAddress;


public class MultiPageHttpServer {
    
    public static void main() throws IOException {
        // Create an HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Create context for the root (Home page) or index.html
        server.createContext("/", new MyFileHandler("html/index.html"));

        // Create context for the About page
        server.createContext("/about", new MyFileHandler("html/about.html"));

        //Create context for the Add page
        server.createContext("/add-item", new MyFileHandler("html/add-item.html")); 

        // Create context for the CSS file
        server.createContext("/style.css", new MyFileHandler("html/style.css", "text/css"));

        //logic behind the submit.
        server.createContext("/submit", new SubmitHandler()); 

        // Start the server
        server.setExecutor(null); // Default executor
        server.start();
        System.out.println("Server is running on http://localhost:8080");
    }

    
}
