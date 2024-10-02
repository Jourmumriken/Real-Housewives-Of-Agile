package JavaWebSite;

import JavaDataBase.Account;

import java.net.HttpCookie;


/**
 * Represents a user login session with associated account and session details.
 */
public class UserLogin {

    Account account;
    HttpCookie cookie; 
    String sessionId;  
    
    /**
     * Constructs a JavaWebSite.UserLogin instance with the specified account.
     * 
     * @param account the Account associated with this login
     */
    UserLogin(Account account)
    {
        this.account = account; 
        sessionId = account.getUsername(); 
        cookie = new HttpCookie("sessionId", sessionId); 
        // System.out.println("sessionId: " + sessionId);
    }

    /**
     * Returns the session ID associated with this JavaWebSite.UserLogin.
     * 
     * @return the session ID as a string
     */
    public String getSessionId() {
        return sessionId;
    }


    
}
