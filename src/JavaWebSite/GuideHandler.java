package JavaWebSite;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import JavaDataBase.ManagerLayer;
import JavaDataBase.Guide;
import JavaDataBase.Exceptions.*;




//import org.apache.derby.impl.load.Import;
/**
 * Currently messy "proof of concept". It works by grabbing html files as
 * strings, editing item containers to include existing guides,
 * and then href:ing to the guide pages. (this should optimally done in JS
 * instead...)
 * The guides are referenced by ID, as each guide has a random generated unique
 * ID on the sql side, which can be obtained via guide.getID().
 * localhost:8080/guide shows the "edited index.html" page with database guides,
 * Pressing any guide links you to localhost:8080/guide?id=(guideID), and shows
 * the temporary html display page made by ´createGuideHtml(Guide guide)´.
 * The same proccess is applied to create the display page of a specific guide.
 */
class GuideHandler implements HttpHandler {
    ManagerLayer database;

    /**
     * Constructor to initialize the JavaWebSite.GuideHandler with a database
     * reference.
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
     * Inserts guide links dynamically into the
     * <ul id="item-container">
     * of the provided HTML content.
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
     * Handles HTTP requests and generates responses based on the provided query
     * parameters.
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
    // -------------------------------------------------------------------------//
    // ---Methods below are for dynamically creating the "guide display page"---//
    // -------------------------------------------------------------------------//

    // Extracts headers like <h2>, <h3> from the guide content
    private List<String> extractHeaders(String content) {
        List<String> headers = new ArrayList<>();
        Pattern headerPattern = Pattern.compile("<h[2-3]>(.*?)</h[2-3]>");
        Matcher matcher = headerPattern.matcher(content);
        while (matcher.find()) {
            headers.add(matcher.group(1)); // Extract the header text
        }
        return headers;
    }

    // Generates a Table of Contents based on the list of headers
    private String generateTableOfContents(List<String> headers) {
        StringBuilder tocBuilder = new StringBuilder();
        tocBuilder.append("<div class=\"table-of-contents\">\n<h3>Table of Contents</h3>\n<ul>\n");
        int sectionCounter = 1;
        for (String header : headers) {
            tocBuilder.append("<li><a href=\"#section-" + sectionCounter + "\">")
                    .append(header)
                    .append("</a></li>\n");
            sectionCounter++;
        }
        tocBuilder.append("</ul>\n</div>");
        return tocBuilder.toString();
    }

    // Just generates the side navigation bar that shows when a guide is open
    // that dynamically fetches guides from the database to show.
    private String generateSideNavigation() {
        StringBuilder sideNavBuilder = new StringBuilder();

        // Copypasted from guide1.html, idk exactly how it works but it does :)
        sideNavBuilder.append("<div id=\"toggle-sidebar\" class=\"side-nav\">\n")
                .append("<a href=\"#\" class=\"toggle-btn\" onclick=\"toggleSideNav()\">")
                .append("<i class=\"fa-solid fa-bars\"> </i></a>\n");

        try {
            for (Guide guide : database.getAllGuides()) {
                // Add each guide as a link in the sidebar
                sideNavBuilder.append("<li><a href=\"/guide?id=")
                        .append(guide.getId())
                        .append("\">")
                        .append(guide.getTitle())
                        .append("</a></li>\n");
            }
        } catch (DataBaseConnectionException e) {
            // Something fucked up, print issue and discard navbar.
            System.out.println(e);
            return "";
        }
        // End the divvy-mcthingy and return navbar html stuff as string.
        sideNavBuilder.append("</div>");
        return sideNavBuilder.toString();
    }

    /**
     * Creates HTML content for a specific guide by removing the existing ToC and
     * content,
     * and dynamically injecting the guide's new Table of Contents and content.
     * This is meant to be used for the html page to view a *specific* guide!
     * Furthermore it configures the navbar dynamically
     *
     * @param guide The guide object containing the content to be displayed.
     * @return The dynamically generated HTML content for the guide.
     */
    private String createGuideHtml(Guide guide) {
        // Step 1: Load the HTML template
        String template = readHtmlAsString("guide1.html");

        // ----- Replace the <h1> tag with the guide title -----//
        // Find the <h1> tag within the intro section and replace it with the guide's
        // title
        template = template.replaceFirst("<h1>.*?</h1>", "<h1>" + guide.getTitle() + "</h1>");

        // Add the author and difficulty level of the guide
        template = template.replaceFirst("<hr>", "<hr>" + "\n" + "Author: " + guide.getAccount().getUsername()
                + "<br>" + "Difficulty: " + guide.getDifficulty());

        // ----- Remove existing table of contentes ----- //
        String tocStartTag = "<div class=\"TableOfContents\">";
        String tocEndTag = "</div>";
        int tocStartIndex = template.indexOf(tocStartTag);
        int tocEndIndex = template.indexOf(tocEndTag, tocStartIndex);

        if (tocStartIndex != -1 && tocEndIndex != -1) {
            // perform the removal
            template = template.substring(0, tocStartIndex) + template.substring(tocEndIndex + tocEndTag.length());
        }

        // ----- Remove existing "content" section -----//
        String contentStartTag = "<!-- Text sections -->";
        String contentEndTag = "<!-- End of Text sections -->";
        int contentStartIndex = template.indexOf(contentStartTag);
        int contentEndIndex = template.indexOf(contentEndTag, contentStartIndex);

        if (contentStartIndex != -1 && contentEndIndex != -1) {
            // Remove everything between the content placeholders
            template = template.substring(0, contentStartIndex + contentStartTag.length()) +
                    template.substring(contentEndIndex);
        }

        // ---- Gnerate table of contents if headers exist in content -----//
        // Extract headers (e.g., <h2>, <h3>) from the guide content
        String guideContent = guide.getContent();
        List<String> headers = extractHeaders(guideContent);

        // Replace all newlines in the content string by html line breaks (<br>)
        guideContent = guideContent.replaceAll("\n", "<br>");

        // Step 2: Generate the Table of Contents (ToC) only if headers are present
        String toc = "";
        if (!headers.isEmpty()) {
            toc = generateTableOfContents(headers); // This will create the <ul> with <li><a> links to headers
        }

        // Insert the Table of Contents back into the template if it exists
        if (!toc.isEmpty()) {
            template = template.replace("<!-- Table of Contents -->", toc);
        }

        // ----- Insert guide content with spacing ----- //
        String guideContentWithSpacing = guideContent.replaceAll("<h2>", "<h2 style='margin-top: 15px;'>")
                .replaceAll("<p>", "<p style='margin-bottom: 15px;'>");

        template = template.replace("<!-- Text sections -->", guideContentWithSpacing);

        // ----- Remove and replace template's sidebar with dynamic one ----- //
        String navStartTag = "<div id=\"toggle-sidebar\"";
        String navEndTag = "</div>";
        int navStartIndex = template.indexOf(navStartTag);
        int navEndIndex = template.indexOf(navEndTag, navStartIndex);

        if (navStartIndex != -1 && navEndIndex != -1) {
            template = template.substring(0, navStartIndex) + template.substring(navEndIndex + navEndTag.length());
        }

        template = template.replace("<!-- side navigation -->", generateSideNavigation());

        // Return the complete & edited template to match the guide.
        return template;
    }

}
