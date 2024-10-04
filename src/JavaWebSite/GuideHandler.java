package JavaWebSite;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import JavaDataBase.ManagerLayer;
import JavaDataBase.Guide;
import JavaDataBase.Exceptions.*;
//import org.apache.derby.impl.load.Import;
/**
 * Currently messy "proof of concept". It works by grabbing index.html as
 * a string, editing the item container to include existing guides,
 * and then href:ing to the guide pages. (this should be done in JS instead...)
 * The guides are referenced by ID, as each guide has a random generated unique
 * ID on the sql side, which can be obtained via guide.getID().
 * localhost:8080/guide shows the "edited index.html" page with database guides,
 * Pressing any guide links you to localhost:8080/guide?id=(guideID), and shows
 * the temporary html display page made by ´createGuideHtml(Guide guide)´
 */
class GuideHandler implements HttpHandler {
    ManagerLayer database;

    /**
     * Constructor to initialize the JavaWebSite.GuideHandler with a database reference.
     *
     * @param database The database management layer.
     */
    public GuideHandler(ManagerLayer database) {
        this.database = database;
    }

    /**
     * Reads an entire HTML file and returns its content as a string.
     *
     * @param htmlFile The name of the HTML file to be read.
     * @return The content of the HTML file as a string.
     */
    private String readHtmlAsString(String htmlFile) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new FileReader("html/" + htmlFile))) {
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return contentBuilder.toString();
    }

    /**
     * Inserts guide links dynamically into the <ul id="item-container"> of the provided HTML content.
     *
     * @param htmlContent The HTML content as a string.
     * @return The modified HTML content with inserted guide links.
     */

    // Method to insert guide links dynamically into the '<ul id="item-container">'
    // of the provided html file (as a string).
    // Yes, this is quite cursed, but it is a java solution that works
    // as long as the html file supplied has the container above.
    // This should realistically be done via JS in the html file instead :)
    private String insertGuidesIntoHtml(String htmlContent) {
        // Find the position of the <ul id="item-container"> tag
        String ulStartTag = "<ul id=\"item-container\">";
        // Find where the tag ends
        String ulEndTag = "</ul>";
        int ulStartIndex = htmlContent.indexOf(ulStartTag);
        int ulEndIndex = htmlContent.indexOf(ulEndTag, ulStartIndex);

        // If the container exists:
        if (ulStartIndex != -1 && ulEndIndex != -1) {
            // Build the new <li> elements with the guide information
            StringBuilder guidesList = new StringBuilder();
            try {
                // Add href:s to guides by unique ID and the Title.
                for (Guide guide : this.database.getAllGuides()) {
                    guidesList.append("<li><a href=\"/guide?id=")
                            .append(guide.getId())
                            .append("\">")
                            .append(guide.getTitle())
                            .append("</a></li>\n");
                }
            } catch (DataBaseConnectionException e) {
                System.out.println(e);
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

     /**
     * Handles HTTP requests and generates responses based on the provided query parameters.
     *
     * @param exchange The HTTP exchange object containing the request and response.
     * @throws IOException If an input or output exception occurs.
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery(); // Extract query parameters from the URL

        if (query == null || !query.startsWith("id=")) {
            // No "id" in query, render the list of all guides
            String htmlContent = readHtmlAsString("index.html");
            String response = insertGuidesIntoHtml(htmlContent);
            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            // Handle a specific guide with ?id=<number>
            int guideId = parseGuideId(query);
            if (guideId == -1) {
                send404(exchange);
                return;
            }

            Guide guide = null;
            try {
                guide = database.getGuideById(guideId);
            } catch (GuideNotFoundException e) {
                System.out.println("GuideID not found");
            }
            if (guide == null) {
                send404(exchange); // Guide not found
            } else {
                String response = createGuideHtml(guide);
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }

    /**
     * Parses the guide ID from the query string.
     *
     * @param query The query string containing the guide ID.
     * @return The guide ID as an integer, or -1 if the ID is invalid.
     */
    // This extracts the guide ID from the query (guides?id=101) -> returns 101
    private int parseGuideId(String query) {
        if (query != null && query.startsWith("id=")) {
            try {
                return Integer.parseInt(query.substring(3)); // Get the guide ID from the URL
            } catch (NumberFormatException e) {
                return -1; // Invalid ID format
            }
        }
        return -1; // No ID found
    }

    /**
     * Sends a 404 error response to the client.
     *
     * @param exchange The HTTP exchange object containing the request and response.
     * @throws IOException If an input or output exception occurs.
     */
    private void send404(HttpExchange exchange) throws IOException {
        String notFound = "404 (Not Found)";
        exchange.sendResponseHeaders(404, notFound.length());
        OutputStream os = exchange.getResponseBody();
        os.write(notFound.getBytes());
        os.close();
    }

    /**
     * Creates HTML content for a specific guide.
     *
     * @param guide The guide object containing the content to be displayed.
     * @return The dynamically generated HTML content for the guide.
     */
    // TODO: I am bad at html, need help refactoring this to project standard.
    private String createGuideHtml(Guide guide) {
        // Dynamically generate HTML content for the guide
        return "<html>" +
                "<head><title>" + guide.getTitle() + "</title></head>" +
                "<body>" +
                "<h1>" + guide.getTitle() + "</h1>" +
                "<p><strong>Difficulty:</strong> " + guide.getDifficulty() + "</p>" +
                "<p><strong>By:</strong> " + guide.getAccount().getUsername() + "</p>" +
                "<div>" + guide.getContent() + "</div>" +
                "</body>" +
                "</html>";
    }
}
