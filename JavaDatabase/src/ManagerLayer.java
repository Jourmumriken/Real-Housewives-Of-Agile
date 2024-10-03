import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The ManagerLayer class handles the application logic for managing accounts and guides.
 * It interacts with the DataAccessLayer to perform database operations.
 */
public class ManagerLayer {
    private final DataAccessLayer db;
    private final Connection conn;
    private final String DatabaseURL = "jdbc:derby:RepairWikiDB;create=true";
    public ManagerLayer() {
        this.conn = setupConn();
        this.db = new DataAccessLayer(conn);
    }

    /**
     * gets a connection to the database in the DatabaseURL field
     * 
     * @return a {@link Connection} object-the connection to the database
     */
    private Connection setupConn() {
        try {
            return DriverManager.getConnection(DatabaseURL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    /**
     * Asserts whether a user 'username' has 'password' as their password.
     * @param username the username of the account
     * @param password the password to verify
     * @return a boolean value asserting whether 'username's password is 'password'.
     * @throws AccountNotFoundException if 'username' does not exist.
     */
    public boolean correctPw(String username,String password) throws AccountNotFoundException{
        try {
            Account account = db.queryAccount(conn, username);
            return account.checkPw(password);
        }
        catch (SQLException e) {
            throw new AccountNotFoundException("Account "+username+" does not exists",e);
        }
    }

    /**
     * returns a list of all accounts in db
     * @return {@link ArrayList} of all accounts as {@link Account} objects
     * @throws DatabaseConnectionException Database access error or Database connection closed
     */
    public ArrayList<Account>  getAllAccounts() throws DatabaseConnectionException{
        try{
            return db.queryAllAccounts(conn);
        }
        catch (SQLException e) {
            throw new DatabaseConnectionException("Database access error or Database connection closed",e);
        }
    }

    /**
     * returns the account with the provided username, if it exists.
     * @param username the username of the account as a String
     * @return  {@link Account} object, the account with the username
     * @throws AccountNotFoundException if the account does not exist
     */
    public Account getAccount(String username) throws AccountNotFoundException{
        try{
            return db.queryAccount(conn,username);
        }
        catch (SQLException e) {
            System.out.println("getAccount error");
            throw new AccountNotFoundException("Account "+username+" does not exists");
        }
    }

    /**
     * Creates a new account with the specified username and password.
     * 
     * @param username the username of the new account as a String
     * @param password the password of the new account as a String
     * @throws AccountCreationException if an account with that username already exists
     */
    public void createAccount(String username,String password) throws AccountCreationException{
        try{db.insertAccount(conn,username,password);}
        catch (SQLException e) {
            throw new AccountCreationException("Account "+username+" already exists",e);
        }
    }

    /**
     * returns all guides in database
     * @return {@link ArrayList} with all guides as {@link Guide} objects
     * @throws DatabaseConnectionException Database access error or Database connection closed
     */
    public ArrayList<Guide> getAllGuides() throws DatabaseConnectionException{
        try{
            return db.queryAllGuides(conn);
        }
        catch (SQLException e){
            throw new DatabaseConnectionException("Database access error or Database connection closed",e);
        }
    }

    /**
     * adds new guide in database
     * @param title  title of the guide as a String
     * @param content the content of the guide as a String
     * @param account  the author of the guide as {@link Account} object
     * @param difficulty the difficulty of the guide as an int
     * @throws DatabaseConnectionException Database access error or Database connection closed
     */
    public void createGuide(String title, String content, Account account, int difficulty) throws DatabaseConnectionException {
        try{
            db.insertGuide(conn,title,content,account,difficulty);
        }
        catch (SQLException e) {
           throw new DatabaseConnectionException("Database access error or Database connection closed",e);
        }
    }

    /**
     * returns the guide with provided id
     * 
     * @param id the id as an int
     * @return the guide with matching id as a {@link Guide} object
     * @throws GuideNotFoundException if the guide does not exist
     */
    public Guide getGuideById(int id) throws GuideNotFoundException{
        try{
            return db.queryGuide(conn,id);
        }
        catch (SQLException e) {
            throw new GuideNotFoundException("Guide "+id+" does not exist",e);
        }
    }

    /**
     * returns all guides with titles that matches the provided title
     * 
     * @param title the title that will be searched for as a String
     * @return {@link ArrayList} of all guides in database as {@link Guide} objects
     * @throws DatabaseConnectionException if Database access error or Database connection closed
     */
    public ArrayList<Guide> getGuidesByTitle(String title) throws DatabaseConnectionException{
        try{
            return db.queryAllGuidesWithTitle(conn,title);
        }
        catch (SQLException e) {
            throw new DatabaseConnectionException("Database access error or Database connection closed");
        }
    }
    /**
     * returns all guides authored by the user with provided username
     * @param username the username of the user as a String
     * @return {@link ArrayList} of all guides authored by the user as {@link Guide} objects
     * @throws AccountNotFoundException if the account does not exist
     */
    public ArrayList<Guide> getAllGuidesFromUser(String username) throws AccountNotFoundException {
        try{
            return db.queryAllGuidesFromUser(conn,username);
        }
        catch (SQLException e) {
            throw new AccountNotFoundException("Account "+username+" does not exist",e);
        }
    }
    /**
     * returns all guides in database with the provided difficulty
     * @param difficulty the difficulty of the guides as an int
     * @return {@link ArrayList} of all guides with matching difficulty as {@link Guide} Objects
     * @throws DatabaseConnectionException if Database access error or Database connection closed
     */
    public ArrayList<Guide> getGuidesWithDifficulty(int difficulty) throws DatabaseConnectionException{
        try{
            return db.queryGuidesByDifficulty(conn,difficulty);
        }
        catch (SQLException e) {
            throw new DatabaseConnectionException("Database access error or Database connection closed",e);
        }
    }

    // TODO: remove later, just a temporary test
    private void errorTest(){
        System.out.println("errorTest:");
        try{
            Account acc = getAccount("zzz");
        }
        catch (AccountNotFoundException e) {
            System.out.println(e.getMessage());
        }
//        try{
//            ArrayList<Guide> d = getGuidesByTitle("OAJDOIJD");
//        }
//        catch (DataBaseConnectionException e) {
//            System.out.println(e.getMessage());
//        }
    }
}
