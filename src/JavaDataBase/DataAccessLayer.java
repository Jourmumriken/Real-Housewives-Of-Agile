package JavaDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A class that provides an abstracted interface for accessing data from the
 * database
 */
public class DataAccessLayer {
    // *SET THIS TO TRUE ONLY IF YOU INTEND TO WIPE THE DATABASE ON NEXT RUN *//
    // Should be set to false outside of testing!
    // Might need to run program a couple times with it as true to fully wipe
    // Because they wipe one at a time due to constraint exceptions.
    static final boolean dropAllData = false;

    private final String createSQLTable = "CREATE TABLE RepairGuide (" +
            "id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
            "title VARCHAR(255), " +
            "content VARCHAR(10000)," +
            "username VARCHAR(15)," +
            "FOREIGN KEY (username) REFERENCES Account(username)," + // "ON DELETE CASCADE" maybe?
            "difficulty INT" +
            ")";
    private final String sqlAccount = "CREATE TABLE Account (" +
            "username VARCHAR(15) PRIMARY KEY," + // better that we use an INT as primary key instead.
            // But simple prototype first and incremental progress and all that.
            "password VARCHAR(30) NOT NULL" + // plaintext password LOL!
            ")";
    private final String votes = "CREATE TABLE Votes (" +
            "vote_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
            "vote_type VARCHAR(10) CHECK (vote_type IN ('upvote', 'downvote'))," + // we do this awfulness because derby does not support enum types
            "username VARCHAR(15)," +
            "id INT,"+ // the id of the guide that is voted on
            "FOREIGN KEY (username) REFERENCES Account(username)," +
            "FOREIGN KEY (id) REFERENCES RepairGuide(id)," +
            "UNIQUE(username,id)"+
            ")";
    /**
     * Initializes and creates persistent data tables if
     * they do not exist on the connection.
     * 
     * @param conn the connection object which the database will connect to.
     */
    public DataAccessLayer(Connection conn) {
        if (conn != null) {
            // Initialize each table without throwing exceptions.
            // Note that Account must be initialized before RepairGuide,
            // as RepairGuide has constraints to Account usernames.
            initializeTable(conn, "Account", sqlAccount);
            initializeTable(conn, "RepairGuide", createSQLTable);
            initializeTable(conn,"Votes", votes);

        } else {
            throw new NullPointerException("Connection passed to DataAccessLayer was null.");
        }
    }

    /**
     * Initializes a table based on the given SQL create statement.
     * If dropAllData is true, the table will be dropped before creation.
     * If the table already exists, it will catch the SQLException and continue.
     * 
     * @param conn      the database connection object
     * @param tableName the name of the table to be initialized
     * @param createSQL the SQL statement to create the table
     */
    private void initializeTable(Connection conn, String tableName, String createSQL) {
        try (Statement statement = conn.createStatement()) {
            if (dropAllData) {
                statement.executeUpdate("DROP TABLE " + tableName);
                System.out.println(tableName + " table has been WIPED!");
            } else {
                statement.executeUpdate(createSQL);
                System.out.println(tableName + " table has been created.");
            }
        } catch (SQLException e) {
            // System.out.println(e);
            if (dropAllData) {
                System.out.println("Cannot delete " + tableName + " table, it does not exist.");
            } else {
                System.out.println(tableName + " table already exists, using existing table...");
            }
        }
    }

    /**
     * returns the balance of votes for a guide.
     * @param conn The connection to the database as {@link Connection} object.
     * @param id the id of the guide as int
     * @return an int, upvotes minus downvotes
     * @throws SQLException if guide does not exist or  database access error or the connection is closed
     */
    public int queryVoteBalance(Connection conn, int id) throws SQLException{
        String sql = "Select vote_type FROM Votes Where id=?";
        int upvotes = 0;
        int downvotes =0;
        PreparedStatement stm= conn.prepareStatement(sql);
        stm.setInt(1,id);
        ResultSet rs = stm.executeQuery();
        while(rs.next()){
            String type = rs.getString(1);
            if(Objects.equals(type, "downvote")) {
               downvotes++;
            }
            else if(Objects.equals(type, "upvote")){
                upvotes++;
            }
        }
        return upvotes-downvotes;
    }

    /**
     * Returns the type of vote the account with the provided username voted on the guide with the provided id.
     * @param conn  The connection to the database as {@link Connection} object.
     * @param username the username of the account as String
     * @param id the id of the guide as int
     * @return either VoteType.DOWNVOTE or VoteType.UPVOTE as {@link VoteType} enum type.
     * @throws SQLException If the account have not voted on the guide
     */
    public VoteType queryVoteType(Connection conn, String username, int id) throws SQLException{
        String sql = "SELECT vote_type FROM Votes WHERE username=? AND id=?";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1,username);
        stm.setInt(2,id);
        ResultSet rs = stm.executeQuery();

        if(rs.next()) {
            String type = rs.getString("vote_type");
            if (Objects.equals(type, "downvote")) {
                return VoteType.DOWNVOTE;
            }
            else if (Objects.equals(type, "upvote")) {
                return VoteType.UPVOTE;
            }
            else {
                throw new SQLException("Invalid vote type: " + type);
            }
        }
        else {
            throw new SQLException("Note vote found for user " + username + " and id " + id);
        }
    }

    /**
     * Deletes the vote made by the account with provided username on the guide with the provided id
     * @param conn the connection to the database as {@link Connection} object.
     * @param username the username of the account as String
     * @param id the id of the guide as int
     * @throws SQLException if the deletion fails such as when the vote does not exist.
     */
    public void deleteVote(Connection conn,String username, int id) throws SQLException{
        String sql = "DELETE FROM Votes WHERE username = ? AND id = ?";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1,username);
        stm.setInt(2,id);
        stm.executeUpdate();
    }

    /**
     * Creates a new vote in db
     * @param conn the connection to the database as {@link Connection} object.
     * @param voteType {@link VoteType} the type of vote. Either VoteType.DOWNVOTE or VoteType.UPVOTE
     * @param username the username of the account that made the vote
     * @param guideId the id of the guide the account voted on
     * @throws SQLException if the insertion fails such as when vote already exist with the username and guide id.
     */
    public void insertVote(Connection conn,VoteType voteType, String username, int guideId) throws SQLException{
        String sql = "INSERT INTO Votes(vote_type,username,id) VALUES (?,?,?)";
        String type="";
        if(voteType==VoteType.DOWNVOTE) {
            type = "downvote";
        }
        else if (voteType==VoteType.UPVOTE) {
            type="upvote";
        }
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1,type);
        stm.setString(2,username);
        stm.setInt(3,guideId);
        stm.executeUpdate();
    }

    /**
     * updates an existing vote with to the provided vote type
     * @param conn the connection to the database as {@link Connection} object.
     * @param username the username of the account that made the vote as String
     * @param guideId the id of the guide that the vote was cast on as int.
     * @param voteType the {@link VoteType} that the vote will be updated to
     * @throws SQLException if the update fails such as when the vote does not exist.
     */
    public void setVoteType(Connection conn,String username, int guideId, VoteType voteType) throws SQLException {
        String type="";
        if(voteType==VoteType.DOWNVOTE) {
            type = "downvote";
        }
        else if (voteType==VoteType.UPVOTE) {
            type="upvote";
        }
        String sql = "UPDATE Votes SET vote_type=? WHERE username=? AND id=?";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1,type);
        stm.setString(2,username);
        stm.setInt(3,guideId);
        stm.executeUpdate();
    }
    /**
     * returns a list of all accounts in db as Account objects
     * 
     * @param conn the connection to the database to be queried
     * @return an ArrayList of Accounts objects
     * @throws SQLException throws if the query fails
     */
    public ArrayList<Account> queryAllAccounts(Connection conn) throws SQLException {
        String query = "SELECT username,password FROM Account";
        PreparedStatement stm = conn.prepareStatement(query);
        ResultSet rs = stm.executeQuery();
        ArrayList<Account> accounts = new ArrayList<>();
        while (rs.next()) {
            String username = rs.getString("username");
            String password = rs.getString("password");
            Account a = new Account(username, password);
            accounts.add(a);
        }
        return accounts;
    }

    /**
     * returns an Account object representing the account with the provided username
     * 
     * @param conn     the connection to the database to be queried
     * @param username the username of the account
     * @return an Account object, or NULL if user doesn't exist!
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
            a = new Account(username, password);
        }
        if(a==null) { // account does not exist-> error
            throw new SQLException("No entry for 'username' exists in database.");
        }
        return a;
    }

    /**
     * Creates a new account in db with username and password in parameters
     * 
     * @param conn     the connection of the database to insert into
     * @param username the username, max 15 characters.
     * @param password the password, max 30 characters.
     * @throws SQLException throws exception if the insertion fails, such as when
     *                      user already exists.
     */
    public void insertAccount(Connection conn, String username, String password) throws SQLException {
        String sql = "INSERT INTO Account (username,password) VALUES (?,?)";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1, username);
        stm.setString(2, password);
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
        String query = "SELECT id, title, content, username, difficulty  FROM RepairGuide";
        ArrayList<Guide> guides = new ArrayList<>();

        // Query the database and retrieve all results
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(query);

        // Go through results and add into the array as `Guides`
        while (rs.next()) {
            int id = rs.getInt("id");
            String title = rs.getString("title");
            String content = rs.getString("content");
            String username = rs.getString("username");
            int difficulty = rs.getInt("difficulty");
            Guide g = new Guide(id, title, content, queryAccount(conn, username), difficulty);
            guides.add(g);
        }
        return guides;
    }

    /**
     * Creates a new guide in the database with the specified title, content, and associated account.
     *
     * @param conn    the connection to the database to insert into
     * @param title   the title of the guide
     * @param content the content of the guide
     * @param account the Account associated with the guide
     * @param difficulty the difficulty level of the guide
     * @throws SQLException if the insertion fails
     */
    public void insertGuide(Connection conn, String title, String content, Account account, int difficulty)
            throws SQLException {
        String sql = "INSERT INTO RepairGuide (title, content, username, difficulty) VALUES (?, ?, ?, ?)";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1, title);
        stm.setString(2, content);
        stm.setString(3, account.getUsername()); // the username for the account that created the guide
        stm.setInt(4, difficulty);
        stm.executeUpdate();
        stm.close();
    }

    /**
     * Queries a guide by the guide's ID.
     *
     * @param conn the database connection to be queried
     * @param id   the ID of the guide to query
     * @return a Guide object; if the guide doesn't exist, throws SQLException
     * @throws SQLException if the connection is bad or if the guide doesn't exist
     */
    public Guide queryGuide(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM RepairGuide WHERE id = ?";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setInt(1, id);
        ResultSet rs = stm.executeQuery();
        Guide g = null;
        while (rs.next()) {
            String title = rs.getString("title");
            String content = rs.getString("content");
            String username = rs.getString("username");
            int difficulty = rs.getInt("difficulty");
            g = new Guide(id, title, content, queryAccount(conn, username), difficulty); // Each guide has an associated
                                                                                         // user
        }
        rs.close();
        stm.close();
        if(g==null){ // guide does not exist -> error
            throw new SQLException("No entry for guide with 'id' exists in database.");
        }
        return g;
    }

    /**
     * Query *all* guides by the title
     * 
     * @param title the title that will be queried with
     * @param conn  Database connection of the database that will be queried
     * @throws SQLException If a database access error occurs or this method is
     *                      called on a closed connection.
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
            String content = rs.getString("content");
            String username = rs.getString("username");
            int difficulty = rs.getInt("difficulty");
            Guide g = new Guide(id, title, content, queryAccount(conn, username), difficulty);
            guides.add(g);
        }
        rs.close();
        stm.close();
        return guides;
    }

    /**
     * Retrieves all repair guides associated with a specific user from the
     * database.
     *
     * @param conn     The Connection object that provides the database
     *                 connection.
     * @param username The username of the account whose guides are to be retrieved.
     * @return An {@link ArrayList} of {@link Guide} objects, each representing a
     *         repair guide belonging to the specified user.
     * @throws SQLException If a database access error occurs or this method is
     *                      called on a closed connection. Can also be thrown as a
     *                      result of querying a null user.
     */
    public ArrayList<Guide> queryAllGuidesFromUser(Connection conn, String username) throws SQLException {
        String sql = "SELECT * FROM RepairGuide WHERE username = ?";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1, username);
        ResultSet rs = stm.executeQuery();
        ArrayList<Guide> guides = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String title = rs.getString("title");
            String content = rs.getString("content");
            int difficulty = rs.getInt("difficulty");
            Guide g = new Guide(id, title, content, queryAccount(conn, username), difficulty);
            guides.add(g);
        }
        rs.close();
        stm.close();
        return guides;
    }

    /**
     * Retrieves all repair guides from the database that match the specified
     * difficulty level.
     *
     * @param conn       The Connection object that provides the database
     *                   connection.
     * @param difficulty The difficulty level to filter the guides by.
     * @return An ArrayList of Guide objects, each representing a
     *         repair guide with the specified difficulty level.
     * @throws SQLException If a database access error occurs or this method is
     *                      called on a closed connection.
     */
    public ArrayList<Guide> queryGuidesByDifficulty(Connection conn, int difficulty) throws SQLException {
        String sql = "SELECT * FROM RepairGuide WHERE difficulty = ?";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setInt(1, difficulty);
        ResultSet rs = stm.executeQuery();
        ArrayList<Guide> guides = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String title = rs.getString("title");
            String content = rs.getString("content");
            String username = rs.getString("username");
            Guide g = new Guide(id, title, content, queryAccount(conn, username), difficulty);
            guides.add(g);
        }
        rs.close();
        stm.close();
        return guides;
    }

}