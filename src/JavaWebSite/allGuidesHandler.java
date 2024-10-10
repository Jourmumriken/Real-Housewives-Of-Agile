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
    private ManagerLayer dataBase;

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
        System.out.println(response);
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
        StringBuilder str = new StringBuilder(); // String builder is used to ensure performant behaviour for large inputs.
        str.append("[");
        ArrayList<Guide> guides = dataBase.getAllGuides();
        int nrOfComma = guides.size()-1; // wont matter if < 0
        for(Guide g :guides) {
            /*
            Since JSON requires the values to be enclosed with double quotations marks
            we use '\"' to prevent java from interpreting each closed set of
            quotation marks to be a separate string.
             */
            String name="\""+g.getTitle()+"\"";
            String id =g.getId()+"";

            String tmp = "{ \"name\":"+name+",\"url\":\"guide?id="+id+"\"}";

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

