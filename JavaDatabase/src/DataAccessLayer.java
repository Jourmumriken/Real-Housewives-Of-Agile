
import java.sql.*;
import java.util.ArrayList;

/**
 * A class that provides an abstracted interface for accessing data from the database
 */
public class DataAccessLayer {
    // *SET THIS TO TRUE ONLY IF YOU INTEND TO WIPE THE DATABASE ON NEXT RUN *//
    // Should be set to false outside of testing!
    static final boolean dropAllData = false;

    private final String createSQLTable = "CREATE TABLE RepairGuide (" +
            "id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
            "title VARCHAR(255), " +
            "content VARCHAR(10000)," +
            "FOREIGN KEY (username) REFERENCES Account(username)" + // "ON DELETE CASCADE" maybe?
            ")";
    private final String sqlAccount = "CREATE TABLE Account ("+
            "username VARCHAR(15) PRIMARY KEY,"+ // better that we use an INT as primary key instead.
            // But simple prototype first and incremental progress and all that.
            "password VARCHAR(15) NOT NULL"+ // plaintext password LOL!
            ")";

    public DataAccessLayer(Connection conn) {
        if (conn != null) {
            try {
                // If dropAllData == true, delete the table (mainly to clean up testing)
                if (dropAllData) {
                    Statement statement = conn.createStatement();
                    statement.executeUpdate("DROP TABLE RepairGuide");
                    statement.close();
                    System.out.println("RepairGuide table has been WIPED!");

                } else { // Else create the table. If already existing -> SQLException
                    Statement statement = conn.createStatement();
                    statement.executeUpdate(createSQLTable);
                    System.out.println("RepairGuide table has been created.");
                    statement.close();
                }
            } catch (SQLException e) {
                // e.printStackTrace();
                if (dropAllData)
                    System.out.println("Can not delete table, it does not exist.");
                else
                    System.out.println("RepairGuide table already exists, using existing table...");
            }
        } else {
            throw new NullPointerException("Connection passed to DataAccessLayer was null.");
        }
    }

    /**
     * returns a list of all accounts in db as Account objects
     * @param conn the connection to the database to be queried
     * @return an ArrayList of Accounts objects
     * @throws SQLException throws if the query fails
     */
    public ArrayList<Account> queryAllAccounts(Connection conn) throws SQLException{
        String query = "SELECT username,password FROM Account";
        PreparedStatement stm = conn.prepareStatement(query);
        ResultSet rs = stm.executeQuery();
        ArrayList<Account> accounts = new ArrayList<>();
        while (rs.next()) {
           String username= rs.getString("username");
           String password= rs.getString("password");
           Account a = new Account(username,password);
           accounts.add(a);
        }
        return accounts;
    }

    /**
     * returns an Account object representing the account with the provided username
     * @param conn the connection to the database to be queried
     * @param username the username of the account
     * @return an Account object
     * @throws SQLException throws exception if the query fails
     */
    public Account queryAccount(Connection conn, String username) throws SQLException {
        String query = "SELECT * FROM Account WHERE username = ?";
        PreparedStatement stm = conn.prepareStatement(query);
        stm.setString(1, username);
        ResultSet rs = stm.executeQuery();
        Account a = null;
        while (rs.next()) {
            String password = rs.getString("password");
            a = new Account(username,password);
        }
        return a;
}

    /**
     * Creates a new account in db with username and password in parameters
     * @param conn the connection of the database to insert into
     * @param username the username
     * @param password the password
     * @throws SQLException throws exception if the insertion fails
     */
    public void insertAccount(Connection conn, String username, String password) throws SQLException{
        String sql = "INSERT INTO Account (username,password) VALUES (?,?)";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1,username);
        stm.setString(2,password);
        stm.executeUpdate();
        stm.close();
}
    /**
     * Returns a list of every guide in DB as Guide objects.
     * 
     * @param conn the connection of the database to query
     * @return ArrayList containing all Guides
     * @throws SQLException throws exception if query fails
     */
    public ArrayList<Guide> queryAllGuides(Connection conn) throws SQLException {
        String query = "SELECT id, title, content, username  FROM RepairGuide";
        ArrayList<Guide> guides = new ArrayList<>();

        // Query the database and retrieve all results
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(query);

        // Go through results and add into the array as `Guides`
        while (rs.next()) {
            int id =rs.getInt("id");
           String title= rs.getString("title");
            String content= rs.getString("content");
            String username = rs.getString("username");
            Guide g = new Guide(id,title,content,queryAccount(conn,username));
            guides.add(g);
        }

        return guides;
    }

    /**
     * Creates new guide in db with title and content in parameters that is associated with the
     * account in the parameters
     * 
     * @param conn    the connection as a Connection object to the database to
     *                insert into
     * @param title   the String title of the guide
     * @param content the String content of the guide
     * @throws SQLException throws exception for various reasons, bad connection,
     *                      wrong data type, table does not exist and others
     */
    public void insertGuide(Connection conn, String title, String content,Account account) throws SQLException {
        String sql = "INSERT INTO RepairGuide (title, content,username) VALUES (?, ?, ?)";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1, title);
        stm.setString(2, content);
        stm.setString(3,account.getUsername()); // the username for the account that created the guide
        stm.executeUpdate();
        stm.close();
        // "INSERT INTO RepairGuide (title, content) VALUES (?,?,?)"
    }

    /**
     * Query a guide by the guide's ID
     * 
     * @param conn Database connection of the database that will be queried
     * @throws SQLException If the connection is bad throws SQLException
     * @return A Guide object. If guide doesn't exist, id = 0 and title/content =
     *         null.
     */
    public Guide queryGuide(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM RepairGuide WHERE id = ?";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setInt(1, id);
        ResultSet rs = stm.executeQuery();
        Guide g = null;
        while (rs.next()) {
            String title= rs.getString("title");
            String content= rs.getString("content");
            String username = rs.getString("username");
            g = new Guide(id,title,content,queryAccount(conn,username)); // Each guide has an associated user
        }
        rs.close();
        stm.close();
        return g;
    }

    /**
     * Query *all* guides by the title
     * 
     * @param title the title that will be queried with
     * @param conn  Database connection of the database that will be queried
     * @throws SQLException If the connection is bad
     * @return A list with all Guides with the same title as title parameter
     */

    public ArrayList<Guide> queryAllGuidesWithTitle(Connection conn, String title) throws SQLException {
        String sql = "SELECT * FROM RepairGuide WHERE title = ?";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1, title);
        ResultSet rs = stm.executeQuery();
        ArrayList<Guide> guides = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String content= rs.getString("content");
            String username = rs.getString("username");
            Guide g = new Guide(id,title,content,queryAccount(conn,username));
            guides.add(g);
        }
        rs.close();
        stm.close();
        return guides;
    }

}