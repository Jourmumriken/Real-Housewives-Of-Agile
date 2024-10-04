package JavaWebSite;

import JavaDataBase.Account;

import java.net.HttpCookie;
import java.util.List;
import java.util.Optional;

import com.sun.net.httpserver.HttpExchange;

/**
 * Represents a user login session with associated account and session details.
 */
public class UserLogin {

    private Account account;
    private HttpCookie cookie;
    private String sessionId;

    /**
     * Constructs a UserLogin instance with the specified account.
     * 
     * @param account the Account associated with this login
     */
    UserLogin(Account account) {
        this.account = account;
        this.sessionId = account.getUsername();
        this.cookie = createSessionCookie();
        // System.out.println("sessionId: " + sessionId);
    }

    /**
     * Creates a session Cookie.
     *
     * @return a new HttpCookie that contains the sessionID
     */
    private HttpCookie createSessionCookie() {
        HttpCookie sessionCookie = new HttpCookie("sessionId", sessionId);
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);
        sessionCookie.setMaxAge(3600); // Expires after an hour
        return sessionCookie;
    }

    /**
     * Extracts a sessionId from the request cookies.
     *
     * @param exchange the httpExchange object representing a request.
     * @return the sessionId from the cookie if it is found, otherwise null.
     */
    public static String extractSessionIdFromCookies(HttpExchange exchange) {
        List<String> cookiesHeader = exchange.getRequestHeaders().get("Cookie");
        if (cookiesHeader != null) {
            for (String cookieHeader : cookiesHeader) {
                List<HttpCookie> cookies = HttpCookie.parse(cookieHeader);
                Optional<HttpCookie> sessionCookie = cookies.stream()
                        .filter((httpCookie) -> "sessionId".equals(httpCookie.getName()))
                        .findFirst();
                if (sessionCookie.isPresent()) {
                    return sessionCookie.get().getValue(); // Return sessionID
                }
            }
        }
        return null; // No session cookie could be found :(
    }

    /**
     * Checks if the sessionId in the user's cookie matches the
     * current user's session
     *
     * @param exchange The HttpExchange object.
     * @return true if the session matches, otherwise false.
     */
    public boolean isValidSession(HttpExchange exchange) {
        String cookieSessionId = extractSessionIdFromCookies(exchange);
        return cookieSessionId != null && cookieSessionId.equals(this.sessionId);
    }

    /**
     * Returns the cookie associated with this UserLogin.
     *
     * @return the cookie as an HttpCookie
     */
    public HttpCookie getCookie() {
        return this.cookie;
    }

    /**
     * Returns the session ID associated with this UserLogin
     * 
     * @return the session ID as a string
     */
    public String getSessionID() {
        return this.sessionId;
    }


    
}
