package JavaWebSite;

import java.io.File;
import java.io.IOException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.OutputStream;
import java.util.Map;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;




/**
 * A handler for serving files over HTTP.
 */
class MyFileHandler implements HttpHandler {
    private String filePath;
    private String contentType;
    File file; 

    /**
     * Constructor for JavaWebSite.MyFileHandler with default content type as "text/html".
     * 
     * @param filePath the path of the file to serve
     */
    public MyFileHandler(String filePath) {
        this (filePath, "text/html");
    }

     /**
     * Constructor for JavaWebSite.MyFileHandler with specified content type.
     * 
     * @param filePath   the path of the file to serve
     * @param contentType the content type of the file
     */
    public MyFileHandler(String filePath, String contentType) {
        this.filePath = filePath;
        this.contentType = contentType;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        //use method to display output to the terminal for easy understading. 
        // logRequestInfo(exchange);

        file = new File(filePath); // Path to the file

        if (file.exists()) {
            // Set response header based on file type
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.sendResponseHeaders(200, file.length());

            // Read the file and send it as a response
            InputStream is = new FileInputStream(file);
            OutputStream os = exchange.getResponseBody();
            byte[] buffer = new byte[1024];
            int count;
            while ((count = is.read(buffer)) != -1) {
                os.write(buffer, 0, count);
            }

            is.close();
            os.close();
        } else {
            // If file is not found, return 404 and crush the program
            String notFound = "404 (Not Found)";
            exchange.sendResponseHeaders(404, notFound.length());
            System.out.println(notFound + "Error: " + file + " not been found");
            System.exit(0);
        }
    }
    
    /**
     * Logs the request information to the console.
     * 
     * @param exchange the HTTP exchange containing request data
     */
    private void logRequestInfo(HttpExchange exchange) {
        System.out.println("---- Incoming Request ----");
        System.out.println("Method: " + exchange.getRequestMethod());
        System.out.println("URL: " + exchange.getRequestURI().toString());

        // Print headers
        System.out.println("Headers:");
        for (Map.Entry<String, List<String>> header : exchange.getRequestHeaders().entrySet()) {
            System.out.println(header.getKey() + ": " + header.getValue());
        }
        System.out.println("------------END OF REQUEST--------------");
    }
}