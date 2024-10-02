package JavaDataBase;

import JavaDataBase.Exceptions.AccountCreationException;
import JavaDataBase.Exceptions.AccountNotFoundException;
import JavaDataBase.Exceptions.DataBaseConnectionException;
import JavaDataBase.Exceptions.GuideNotFoundException;

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
    private Connection setupConn(){
        try {
            return DriverManager.getConnection(DatabaseURL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * returns the number of upvotes minus the number of downvotes for a guide as an int.
     * @param guide the guide that the method will return the vote balance for as {@link Guide} object.
     * @return nr of upvotes-downvotes as int
     * @throws GuideNotFoundException if the guide does not exist.
     */
    public int getVoteBalance(Guide guide) throws GuideNotFoundException {
        int balance;
        try {
            balance= db.queryVoteBalance(conn,guide.getId());
        } catch (SQLException e) {
            throw new GuideNotFoundException("The guide with id"+guide.getId()+"does not exist",e);
        }
        return balance;
    }
    /**
     * The provided Account Makes a vote on the provided guide
     * <p>If user votes on guide but already has voted and the vote is same as before: remove the vote.
     * If the new vote is not the same as the old vote: switch the old vote to the type of new vote.
     * If the user have not voted on guide before: add vote as you would expect </p>
     * @param account the account that is voting as an {@link Account} object
     * @param guide the guide that will be voted on as an {@link Guide} object
     * @param voteType the type of vote as {@link VoteType} enum type. Only VoteType.DOWNVOTE or VoteType.UPVOTE allowed
     * @throws DataBaseConnectionException if an access error occurs or the database connection is closed
     */
    //_______________________________________________________________________________________________________________
    // The reason we do it like this is that we want to replicate the upvote/downvote system of, for example, reddit.
    // In which the user is presented with two buttons, upvote and downvote. If the user haven't voted yet; pressing
    // either of the buttons causes the respective vote. But if the user already has voted there are two cases:
    // 1. The user votes the same as the user already has voted: The vote goes away,
    // and it's as if the user never voted at all
    // 2. The user votes the opposite that the user already has voted: The vote switches to the other vote type.
    //
    // The benefit of the current implementation of voteOnGuide() is that all of this logic is contained inside the
    // voteOnGuide() method. Anyone that wants to use voteOnGuide() just has to provide the arguments and the logic is
    // resolved behind the scenes.
    //
    // To use the voteOnGuide() method: create one upvote and one downvote button on the GUI. if the user pressed the upvote
    // button call voteOnGuide() with VoteType.UPVOTE. If the user pressed the downvote button call voteOnGuide() with
    // VoteType.DOWNVOTE.
    // -Markus.H
    // ________________________________________________________________________________________________________________
    public void voteOnGuide(Account account,Guide guide, VoteType voteType) throws DataBaseConnectionException {
        String username = account.getUsername();
        int id = guide.getId();
        try{
            db.insertVote(conn, voteType,username,id); // the user haven't voted yet.
        } catch (SQLException e) {    // if the vote already exists
            VoteType type = queryVoteHelper(username,id);
            if(type==voteType) {
               deleteVoteHelper(username,id); // the type is the same so we delete the database entry
            }
            else {
                setVoteHelper(username,id,voteType); // the type is different so we switch to the other type
            }
        }
    }

    /**
     * Just a helper method for voteOnGuide().
     * @throws DataBaseConnectionException if Database access error or database connection closed
     */
    private VoteType queryVoteHelper(String username,int id) throws DataBaseConnectionException {
        VoteType type = null;
        try {
            type = db.queryVoteType(conn, username, id);
        }
        catch (SQLException ex) {
            throw new DataBaseConnectionException("1Database access error or database connection closed",ex);
        }
        return type;
    }
    /**
     * Just a helper method for voteOnGuide().
     * @throws DataBaseConnectionException if Database access error or database connection closed
     */
    private void deleteVoteHelper(String username,int id) throws DataBaseConnectionException {
        try {
            db.deleteVote(conn,username, id);
        } catch (SQLException ex) {
            throw new DataBaseConnectionException("2Database access error or database connection closed",ex);
        }
    }
    /**
     * Just a helper method for voteOnGuide().
     * @throws DataBaseConnectionException if Database access error or database connection closed
     */
    private void setVoteHelper(String username,int id, VoteType voteType) throws DataBaseConnectionException {
        try {
            db.setVoteType(conn,username,id,voteType);
        } catch (SQLException ex) {
            throw new DataBaseConnectionException("3Database access error or database connection closed",ex);
        }
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
     * @throws DataBaseConnectionException Database access error or Database connection closed
     */
    public ArrayList<Account>  getAllAccounts() throws DataBaseConnectionException {
        try{
            return db.queryAllAccounts(conn);
        }
        catch (SQLException e) {
            throw new DataBaseConnectionException("Database access error or Database connection closed",e);
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
     * @throws DataBaseConnectionException Database access error or Database connection closed
     */
    public ArrayList<Guide> getAllGuides() throws DataBaseConnectionException {
        try{
            return db.queryAllGuides(conn);
        }
        catch (SQLException e){
            throw new DataBaseConnectionException("Database access error or Database connection closed",e);
        }
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
     * @throws DataBaseConnectionException if Database access error or Database connection closed
     */
    public ArrayList<Guide> getGuidesByTitle(String title) throws DataBaseConnectionException {
        try{
            return db.queryAllGuidesWithTitle(conn,title);
        }
        catch (SQLException e) {
            throw new DataBaseConnectionException("Database access error or Database connection closed");
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
     * @throws DataBaseConnectionException if Database access error or Database connection closed
     */
    public ArrayList<Guide> getGuidesWithDifficulty(int difficulty) throws DataBaseConnectionException {
        try{
            return db.queryGuidesByDifficulty(conn,difficulty);
        }
        catch (SQLException e) {
            throw new DataBaseConnectionException("Database access error or Database connection closed",e);
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
