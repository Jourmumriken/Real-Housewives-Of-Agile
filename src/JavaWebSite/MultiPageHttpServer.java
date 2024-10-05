package JavaWebSite;

import JavaDataBase.*;
import JavaDataBase.Exceptions.AccountCreationException;
import com.sun.net.httpserver.HttpServer;
import config.ServerConfig;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * A multi-page HTTP server that handles various requests and serves HTML pages.
 */
public class MultiPageHttpServer {

    ServerConfig config = ServerConfig.getInstance(); 
    ManagerLayer database;
    private final Logger logger = Logger.getLogger(MultiPageHttpServer.class.getName());

    /**
     * Main method to set up and start the HTTP server.
     * 
     * @throws IOException if an I/O error occurs when creating the server.
     */
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

//        try {
//            database.createAccount("Jeff", "1234");
//            database.createGuide("Guide1", "<h2> step1 </h2>\n something...", database.getAccount("Jeff"), 3);
//            database.createGuide("Guide2", "basic content..", database.getAccount("Jeff"), 2);
//            Guide g = database.getGuidesByTitle("Guide1").getFirst();
//            database.voteOnGuide(database.getAccount("Jeff"),g, VoteType.UPVOTE);
//            System.out.println(database.getVoteBalance(g));
//            database.voteOnGuide(database.getAccount("Jeff"),g, VoteType.UPVOTE);
//            System.out.println(database.getVoteBalance(g));
//        } catch (DataBaseConnectionException | AccountNotFoundException | AccountCreationException| GuideNotFoundException e) {
//            System.out.println(e);
//        }

        // ------------------------------------------------------------------------------- //

        // Create an HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(config.getPort()), 0);

        //Logger to help us read HTTP requests
        setupLogger();

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
        server.createContext("/submit", new SubmitHandler(database));

        // Create context for the CSS file
        server.createContext("/style.css", new MyFileHandler("html/style.css", "text/css"));

        //logic behind the submit.
        server.createContext("/auth", new LoginHandler(database)); 

        //logic behind the register
        server.createContext("/registerNew", new RegisterHandler(database));

        //login page 
        server.createContext("/login", new MyFileHandler("html/login.html"));
        
        //register page
        server.createContext("/register", new MyFileHandler("html/register.html"));

        //load script file 
        server.createContext("/script.js", new MyFileHandler("html/script.js", "application/javascript"));

        // Start the server
        server.setExecutor(null); // Default executor
        server.start();
        logger.info("Logger is running");
        System.out.println("Server is running on http://localhost:" + config.getPort());
    }

    /**
     * Starts the HTTP server by calling the main method.
     * 
     * @throws IOException if an I/O error occurs when starting the server.
     */
    public void start () throws IOException {
        this.main();
    }


    private void setupLogger() {
        try {
            FileHandler fileHandler = new FileHandler("server.log", true); // Append mode
            fileHandler.setFormatter(new SimpleFormatter()); // Simple text format
            logger.addHandler(fileHandler);
            //logger.setUseParentHandlers(false); // Disable console logging
        } catch (IOException e) {
            logger.severe("Failed to setup logger: " + e.getMessage());
        }
    }


}