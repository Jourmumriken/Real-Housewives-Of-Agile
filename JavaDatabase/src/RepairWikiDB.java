import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepairWikiDB {
    // *SET THIS TO TRUE ONLY IF YOU INTEND TO WIPE THE DATABASE ON NEXT RUN *//
    // Should be set to false outside of testing!
    static final boolean dropAllData = false;

    public RepairWikiDB() {
        try {
            // Create the connection
            Connection conn = createConnection();
            // Create statement object to communicate with server via SQL statements
            Statement statement = conn.createStatement();

            // Use helper method to define the data table
            createSQLDataTable(statement);

            // Insert some data
            String title = "Hardcoded title example";
            String content = "Hardcoded content thingy example...";
            insertRepairGuide(conn, title, content);

            // Retrieve all data
            retrieveAllGuides(statement);

        } catch (SQLException | ClassNotFoundException e) {
            // Enable if needed for debug. It *will* spout SQL exceptions.
            // e.printStackTrace();
        }
    }

    // ----- Helper methods for the database: ----- //
    private static Connection createConnection() throws ClassNotFoundException, SQLException {
        // Path to local database. Will be created if not already existing:
        final String DatabaseURL = "jdbc:derby:RepairWikiDB;create=true";

        // Load the embedded driver from derby (This is how Derby says to do it)
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

        // Connect to the database, as per Derby's instructions
        // and return the connection.
        return DriverManager.getConnection(DatabaseURL);
    }

    private static void createSQLDataTable(Statement statement) {
        // Define an SQL table to create for data storage:
        // It holds a unique identifying ID, and also a title and content.
        // In SQL, each data row must be unique, therefore unique id is needed.
        // ...No need to understand the SQL semantics too much, it creates a
        // place to store and retrieve data on the local server.
        // If more fields are needed, it can be extended.
        String createSQLTable = "CREATE TABLE RepairGuide (" +
                "id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                "title VARCHAR(255), " +
                "content VARCHAR(10000))";

        try {
            // If dropAllData == true, delete the table (mainly to clean up testing)
            if (dropAllData) {
                statement.executeUpdate("DROP TABLE RepairGuide");
                System.out.println("RepairGuide table has been wiped!");
            } else { // Else create the table. If already existing -> SQLException
                statement.executeUpdate(createSQLTable);
                System.out.println("RepairGuide table has been created.");
            }
        } catch (SQLException e) {
            // e.printStackTrace();
            if (dropAllData)
                System.out.println("Can not delete table, it does not exist.");
            else
                System.out.println("RepairGuide table already exists, using existing table...");
        }
    }

    private static void insertRepairGuide(Connection conn, String title, String content) throws SQLException {
        // Define query to add a `title` alongside some `content`
        // to RepairGuide SQL table
        String insertData = "INSERT INTO RepairGuide (title, content) VALUES (?, ?)";
        PreparedStatement injectionSafeQuery = conn.prepareStatement(insertData);
        // Safely input the data into the data insert query
        injectionSafeQuery.setString(1, title);
        injectionSafeQuery.setString(2, content);
        // Perform the data insert on the database
        injectionSafeQuery.executeUpdate();

        System.out.println("Data has been inserted"); // Just a debug print
    }

    private static void retrieveAllGuides(Statement statement) throws SQLException {
        // Ask to obtain the everything of each row in repairguide table
        // .. to instead retrieve only some columns, "select title, content..."
        String getDataQuery = "SELECT * FROM RepairGuide";

        // Execute the query and store the return values in a ResultSet
        ResultSet data = statement.executeQuery(getDataQuery);

        // As long as data exists, loop through it so we can retrieve it.
        while (data.next()) {
            int retrievedID = data.getInt("id");
            String retrievedTitle = data.getString("title");
            String retrievedContent = data.getString("content");

            // This is just to print database content, since we have
            // no other way of doing it currently.
            System.out.println("//----------//");
            System.out.println("ID:" + retrievedID);
            System.out.println("Title: " + retrievedTitle);
            System.out.println("Content: " + retrievedContent);
            System.out.println("//----------//");
        }
    }

    // ----- Main Method ----- //
    public static void main(String[] args) {
        // TODO: Debatable if this class should have the main going forward.
        // ... Might want a "driver" class with main, if we
        // need additional java files for handling communication
        // with frontend. Adjust this class as needed.
        new RepairWikiDB();
    }
}