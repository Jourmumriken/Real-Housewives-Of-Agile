import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

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
     * 
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
     * @throws DataBaseConnectionException Database access error or Database connection closed
     */
    public ArrayList<Account>  getAllAccounts() throws DataBaseConnectionException{
        ArrayList<Account> accounts = null;
        try{
            accounts=db.queryAllAccounts(conn);
        }
        catch (SQLException e) {
            throw new DataBaseConnectionException("Database access error or Database connection closed",e);
        }
        return accounts;
    }

    /**
     * returns the account with the provided username, if it exists.
     * @param username the username of the account as a String
     * @return  {@link Account} object, the account with the username
     * @throws AccountNotFoundException if the account does not exist
     */
    public Account getAccount(String username) throws AccountNotFoundException{
        Account account;
        try{
            account=db.queryAccount(conn,username);
        }
        catch (SQLException e) {
            System.out.println("getAccount error");
            throw new AccountNotFoundException("Account "+username+" does not exists");
        }
        return account;
    }

    /**
     * adds new account in database
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
     * @throws DataBaseConnectionException Database access error or Database connection closed
     */
    public ArrayList<Guide> getAllGuides() throws DataBaseConnectionException{
        ArrayList<Guide> guides;
        try{
            guides=db.queryAllGuides(conn);
        }
        catch (SQLException e){
            throw new DataBaseConnectionException("Database access error or Database connection closed",e);
        }
        return  guides;
    }

    /**
     * adds new guide in database
     * @param title  title of the guide as a String
     * @param content the content of the guide as a String
     * @param account  the author of the guide as {@link Account} object
     * @param difficulty the difficulty of the guide as an int
     * @throws DataBaseConnectionException Database access error or Database connection closed
     */
    public void createGuide(String title, String content, Account account, int difficulty) throws DataBaseConnectionException {
        try{
            db.insertGuide(conn,title,content,account,difficulty);
        }
        catch (SQLException e) {
           throw new DataBaseConnectionException("Database access error or Database connection closed",e);
        }
    }

    /**
     * returns the guide with provided id
     * @param id the id as an int
     * @return the guide with matching id as a {@link Guide} object
     * @throws GuideNotFoundException if the guide does not exist
     */
    public Guide getGuideById(int id) throws GuideNotFoundException{
        Guide g;
        try{
            g=db.queryGuide(conn,id);
        }
        catch (SQLException e) {
            throw new GuideNotFoundException("Guide "+id+" does not exist",e);
        }
        return g;
    }

    /**
     * returns all guides with titles that matches the provided title
     * @param title the title that will be searched for as a String
     * @return {@link ArrayList} of all guides in database as {@link Guide} objects
     * @throws DataBaseConnectionException if Database access error or Database connection closed
     */
    public ArrayList<Guide> getGuidesByTitle(String title) throws DataBaseConnectionException{
        ArrayList<Guide> guides;
        try{
            guides=db.queryAllGuidesWithTitle(conn,title);
        }
        catch (SQLException e) {
            throw new DataBaseConnectionException("Database access error or Database connection closed");
        }
        return guides;
    }
    /**
     * returns all guides authored by the user with provided username
     * @param username the username of the user as a String
     * @return {@link ArrayList} of all guides authored by the user as {@link Guide} objects
     * @throws AccountNotFoundException if the account does not exist
     */
    public ArrayList<Guide> getAllGuidesFromUser(String username) throws AccountNotFoundException {
        ArrayList<Guide> guides;
        try{
            guides=db.queryAllGuidesFromUser(conn,username);
        }
        catch (SQLException e) {
            throw new AccountNotFoundException("Account "+username+" does not exist",e);
        }
        return guides;
    }
    /**
     * returns all guides in database with the provided difficulty
     * @param difficulty the difficulty of the guides as an int
     * @return {@link ArrayList} of all guides with matching difficulty as {@link Guide} Objects
     * @throws DataBaseConnectionException if Database access error or Database connection closed
     */
    public ArrayList<Guide> getGuidesWithDifficulty(int difficulty) throws DataBaseConnectionException{
        ArrayList<Guide> guides;
        try{
            guides=db.queryGuidesByDifficulty(conn,difficulty);
        }
        catch (SQLException e) {
            throw new DataBaseConnectionException("Database access error or Database connection closed",e);
        }
        return guides;
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
