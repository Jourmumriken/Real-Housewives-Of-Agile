import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepairWikiDB {
    // Path to local database. Will be created if not already existing:
    static final String DatabaseURL = "jdbc:derby:RepairWikiDB;create=true";
    // *SET THIS TO TRUE ONLY IF YOU INTEND TO WIPE THE DATABASE ON NEXT RUN *//
    // Should be set to false outside of testing!
    static final boolean dropAllData = false;

    public static void main(String[] args) {
        try {
            // Load the embedded driver from derby (This is how Derby says to do it)
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

            // Connect to the database, as per Derby's instructions
            Connection conn = DriverManager.getConnection(DatabaseURL);
            // we can never use "Statement" for queries that take user input. It allows for
            // SQL-injection attacks. Just a bad practice :)
            Statement statement = conn.createStatement();

            // Define an SQL table to create for data storage:
            // It holds a unique identifying ID, and also a title and content.
            // In SQL, each data row must be unique, therefore unique id is needed.
            // ...No need to understand the SQL semantics too much, it creates a
            // place to store and retrieve data on the local server.
            String createSQLTable = "CREATE TABLE RepairGuide (" +
                    "id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                    "title VARCHAR(255), " +
                    "content VARCHAR(10000))";

            try {
                // If dropAllData == true, delete the table (mainly to clean up testing)
                if (dropAllData) {
                    statement.executeUpdate("DROP TABLE RepairGuide");
                    System.out.println("RepairGuide table has been WIPED!");
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

            // --- Hardcoded test data to insert: ---//
            String title = "Insert guide title:";
            String content = "Insert guide content/steps, whatever.";

            // Define query to add a `title` alongside some `content` to RepairGuide SQL
            // table
            String insertData = "INSERT INTO RepairGuide (title, content) VALUES (?, ?)";
            PreparedStatement injectionSafeQuery = conn.prepareStatement(insertData);
            // Safely input the data into the data insert query
            injectionSafeQuery.setString(1, title);
            injectionSafeQuery.setString(2, content);
            // Perform the data insert
            injectionSafeQuery.executeUpdate();
            System.out.println("Data has been inserted"); // Mainly a debug print

            // --- Query existing data: --//
            // Ask to obtain the title and content of each row in repairguide table
            String getDataQuery = "SELECT id, title, content FROM RepairGuide";
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

        } catch (SQLException | ClassNotFoundException e) {
            // Enable if needed for debug. It *will* spout SQL exceptions
            // e.printStackTrace();
        }
    }
}