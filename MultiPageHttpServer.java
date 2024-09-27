import config.serverConfig;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;

import java.net.InetSocketAddress;


public class MultiPageHttpServer {

    serverConfig config = serverConfig.getInstance(); 
    ManagerLayer database; 

    
    public void main() throws IOException {
        // Connection to database to insert, retrieve data.
        database = new ManagerLayer();
        try {
            database.createAccount("test", "test");
        } catch (AccountCreationException e) {
            
        }
        // ------------------------------------------------------------------------------- //
        // Test code in this block: Uncomment and run it once, then comment it again to add 1 user and some guides:
        // Note: You can not make a guide with a "random Account" that doesn't match an
        // existing username in the database. Must exist!
        // Wipe database by removing RepairWikiDB folder & derby.log file locally.
        // This can all be removed once we can register, publish guides via the site...
        /*
        try {
            database.createAccount("Jeff", "1234");
            database.createGuide("Guide1", "<h2> step1 </h2>\n something...", database.getAccount("Jeff"), 3);
            database.createGuide("Guide2", "basic content..", database.getAccount("Jeff"), 2);
        } catch (DataBaseConnectionException | AccountNotFoundException | AccountCreationException e) {
            System.out.println(e);
        }
        */
        // ------------------------------------------------------------------------------- //

        // Create an HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(config.getPort()), 0);

        // Create context for the root (Home page) or index.html
        server.createContext("/", new MyFileHandler("html/index.html"));

        // Create context for the About page
        server.createContext("/about", new MyFileHandler("html/about.html"));

        // Create context for generic guide
        server.createContext("/guide", new GuideHandler(database)); // TODO: Attempt at generic guide, WIP.

        // Create context fro guide1 
        server.createContext("/guide1", new MyFileHandler("html/guide1.html"));

        // Create context fro guide1 
        server.createContext("/guide2", new MyFileHandler("html/guide2.html"));

        //Create context for the Add page
        server.createContext("/add-item", new MyFileHandler("html/add-item.html")); 

        // Create context for the CSS file
        server.createContext("/style.css", new MyFileHandler("html/style.css", "text/css"));

        //logic behind the submit.
        server.createContext("/auth", new LoginHandler(database)); 

        //logic behind the register
        server.createContext("/registerNew", new LoginHandler(database));

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

    
    public void start () throws IOException {
        this.main();
    }

    
}
