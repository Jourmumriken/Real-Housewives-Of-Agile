import java.net.HttpCookie;


public class UserLogin {

    Account account; 
    HttpCookie cookie; 
    String sessionId;  
    
    
    UserLogin(Account account)
    {
        this.account = account; 
        sessionId = account.getUsername(); 
        cookie = new HttpCookie("sessionId", sessionId); 
        // System.out.println("sessionId: " + sessionId);
    }

    public String getSessionId() {
        return sessionId;
    }


    
}
