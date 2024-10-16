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
 * An endpoint for handling votes. Requires that vote type and guide id is sent in a JSON object.
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
        System.out.println("handle:");
        System.out.println(json);
        int id = Integer.parseInt(parsed[1]);
        String username = UserLogin.extractSessionIdFromCookies(exchange); // When a client receives a cookie it will automatically send the cookie with every request thereafter
        // in LoginHandler we attach a cookie (which includes the username of the logged in account) to the response when logging in. We extract it here.
        System.out.println("yes" + username);
        String response = "Vote received";
        if(username != null){
            try{
                if(parsed[0].equals("down")){
                    database.voteOnGuide(username,id, VoteType.DOWNVOTE);
                }
                else if(parsed[0].equals("up")){
                    database.voteOnGuide(username,id, VoteType.UPVOTE);
                }
            }
            catch (DataBaseConnectionException e) {
                System.out.println("DataBaseError voteHandler");
                response="An error occurred on our end...";
                sendResponse(exchange,response,500);
                return;
            }
        }
        else{
            response = "not registered";
        }

        sendResponse(exchange,response,200);
    }

    /**
     * Sends the response back to the client
     * @param exchange the Http exchange
     * @param responseText the message of the response as String
     * @param statusCode the status code as int
     * @throws IOException if an I/O error occurs
     */
    private void sendResponse(HttpExchange exchange, String responseText, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, responseText.length() );
        OutputStream os = exchange.getResponseBody();
        os.write(responseText.getBytes());
        os.close();
    }

    /**
     * parses the JSON and returns the vote type and the guide id.
     * @param json The JSON object to parse as string
     * @return An array of Strings first element is vote type, either "up" or "down". Seconds element is id which is a the id as a String.
     * An example of returned data: {"down","5"}
     */
    private String[] parse(String json){
        String str = json.replaceAll("[{} \"]",""); //remove '{','}',' ' (empty space) and '"' characters
        String[] arr = str.split("[:,]"); // split at ':' or ','
        return new String[]{arr[1],arr[3]}; // from the example below we get "down" and "7" as output.
    }
//    {
//        "vote": "down", "guideID": "7"
//    }



}