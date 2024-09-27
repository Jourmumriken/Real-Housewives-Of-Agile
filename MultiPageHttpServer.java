import config.serverConfig;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;

import java.net.InetSocketAddress;


public class MultiPageHttpServer extends Thread {

    serverConfig config = serverConfig.getInstance();  
    
    public void main() throws IOException {
        // Create an HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(config.getPort()), 0);

        // Create context for the root (Home page) or index.html
        server.createContext("/", new MyFileHandler("html/index.html"));

        // Create context for the About page
        server.createContext("/about", new MyFileHandler("html/about.html"));

        // Create context fro guide1 
        server.createContext("/guide1", new MyFileHandler("html/guide1.html"));

        // Create context fro guide1 
        server.createContext("/guide2", new MyFileHandler("html/guide2.html"));

        //Create context for the Add page
        server.createContext("/add-item", new MyFileHandler("html/add-item.html")); 

        // Create context for the CSS file
        server.createContext("/style.css", new MyFileHandler("html/style.css", "text/css"));

        //logic behind the submit.
        server.createContext("/fire", new LoginHandler()); 

        //logic behind the register
        server.createContext("/registerNew", new LoginHandler());


        //login page 
        server.createContext("/login", new MyFileHandler("html/login.html"));
        
        //register page
        server.createContext("/register", new MyFileHandler("html/register.html"));

        //load script file 
        server.createContext("/script.js", new MyFileHandler("html/script.js", "application/javascript"));

        // Start the server
        server.setExecutor(null); // Default executor
        server.start();
        System.out.println("Server is running on http://localhost:" + config.getPort());
    }

    @Override
    public void run() {
        try{
            this.main();
        }
        catch(Exception e){
            e.printStackTrace();
            System.exit(0);
        }
        finally{ }
    }

    
}
