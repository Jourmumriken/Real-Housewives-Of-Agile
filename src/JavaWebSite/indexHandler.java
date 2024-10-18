package JavaWebSite;

import JavaDataBase.Exceptions.DataBaseConnectionException;
import JavaDataBase.Guide;
import JavaDataBase.ManagerLayer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.*;
import java.util.ArrayList;

public class indexHandler implements HttpHandler {
    private ManagerLayer dataBase;
    public indexHandler(ManagerLayer managerLayer) {
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
    public void sendNavigationBar(HttpExchange exchange) throws IOException,DataBaseConnectionException{
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
    public String buildResponse() throws DataBaseConnectionException{
        StringBuilder str = new StringBuilder();
        str.append("[");
        ArrayList<Guide> guides = dataBase.getAllGuides();
        int nrOfComma = guides.size()-1; // Could be < 0, but in that case we won't get bad output because of the for loop.
        for(Guide g :guides) {
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

