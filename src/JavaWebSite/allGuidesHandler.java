package JavaWebSite;

import JavaDataBase.Exceptions.DataBaseConnectionException;
import JavaDataBase.Guide;
import JavaDataBase.ManagerLayer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;

/**
 * A class that handles requests for all guides in the database.
 */
public class allGuidesHandler implements HttpHandler {
    final private ManagerLayer dataBase;

    /**
     * The constructor for allGuidesHandler
     * @param managerLayer The manager layer for the database to retrieve all guides from
     */
    public allGuidesHandler(ManagerLayer managerLayer) {
        dataBase=managerLayer;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if("GET".equals(exchange.getRequestMethod())) {
            try {
                sendNavigationBar(exchange);
            } catch (DataBaseConnectionException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void sendNavigationBar(HttpExchange exchange) throws IOException,DataBaseConnectionException{
        String response = buildResponse();
        exchange.sendResponseHeaders(200, response.length());
        OutputStream output = exchange.getResponseBody();
        output.write(response.getBytes());
        output.close();
    }
    // The response needs to be formatted like this (JSON):
//    [
//    { name: "How to Fix a Cracked Screen", url: "guide?id=1" },
//    { name: "Repairing a Broken Charger", url: "guide?id=2" },
//    { name: "Troubleshooting Battery Issues", url: "guide?id=3" },
//    { name: "Replacing a Hard Drive", url: "guide?id=4" },
//    { name: "Fixing Overheating Problems", url: "guide?id=5" }
//     ]
    private String buildResponse() throws DataBaseConnectionException{
        // String builder is used to ensure performant
        // behaviour for large inputs.
        StringBuilder str = new StringBuilder();
        str.append("[");
        ArrayList<Guide> guides = dataBase.getAllGuides();
        int nrOfComma = guides.size()-1; // won't matter if < 0
        for(Guide g :guides) {
            String tmp = g.toJson();
            if(nrOfComma > 0) {
                tmp = tmp +",";
                nrOfComma--;
            }
            str.append(tmp);
        }
        str.append("]");
        return str.toString();
    }
}

