import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManagerLayer {
    private DataAccessLayer db;
    private Connection conn;
    private final String DatabaseURL = "jdbc:derby:RepairWikiDB;create=true";

    public ManagerLayer() {
        this.conn = setupConn();
        this.db = new DataAccessLayer(conn);
    }

    /**
     * gets a connection to the database in the DatabaseURL field
     * 
     * @return a Connection object-the connection to the database
     */
    private Connection setupConn() {
        try {
            return DriverManager.getConnection(DatabaseURL);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }
    private boolean correctPw(String username,String password){
        try {
            Account account = db.queryAccount(conn, username);
            return account.checkPw(password);
        }
        catch (SQLException e) {
            return false;
        }
    }
    public void createAccount(String username,String password) throws AccountCreationException{
        try{db.insertAccount(conn,username,password);}
        catch (SQLException e) {
            throw new AccountCreationException();
        }

    }

    // TODO Just a temporary test, remove later
//    public void InsertTest() {
//        try {
//            db.insertGuide(conn, "How to repair electric toothbrush", "SHOVE IT UP YOUR ASS!!");
//        } catch (SQLException e) {
//            System.out.println("Exception in manager layer:");
//            System.out.println(e);
//        }
//    }
    // TODO also just temporary test
    public void RetrieveAllTest() {
        try {
            ArrayList<Guide> guides = db.queryAllGuides(conn);
            for (Guide g : guides) {
                System.out.println(g);
            }
        } catch (SQLException e) {
            System.out.println("Exception in manager layer:");
            System.out.println(e);
        }
    }
}
