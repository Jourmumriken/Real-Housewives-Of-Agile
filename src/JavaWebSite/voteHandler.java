package JavaWebSite;

import JavaDataBase.Account;
import JavaDataBase.Exceptions.DataBaseConnectionException;
import JavaDataBase.Exceptions.GuideNotFoundException;
import JavaDataBase.Guide;
import JavaDataBase.ManagerLayer;
import JavaDataBase.VoteType;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * An endpoint for handling votes. Requires that name and vote type  is sent in a JSON object.
 */

public class voteHandler implements HttpHandler {
    private final ManagerLayer database;

    public voteHandler(ManagerLayer database) {
        this.database = database;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        InputStream is = exchange.getRequestBody();

        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        // Parse data
        String json = reader.readLine();
        String[] parsed = parse(json);
        String query = exchange.getRequestURI().getQuery();
        int id = Integer.parseInt(query.substring(3));
        String username = parsed[1];
        String response = "Vote received";

        try{
            if(parsed[2].equals("down")){
                database.voteOnGuide(username,id, VoteType.DOWNVOTE);
            }
            else if(parsed[2].equals("up")){
                database.voteOnGuide(username,id, VoteType.UPVOTE);
            }
        }
        catch (DataBaseConnectionException e) {
            response="An error occurred on our end...";
            exchange.sendResponseHeaders(500, response.length());
        }
        exchange.sendResponseHeaders(200, response.length());

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }

    private String[] parse(String json){
        String str = json.replaceAll("[{} \"]",""); //remove '{','}',' ' and '"' characters
        String[] arr = str.split("[:,]"); // split at ':' or ','
        return new String[]{arr[1],arr[3]}; // from the example below we get "name" and "down" as output.
    }
//    {
//        "username": "name","vote": "down"
//    }



}