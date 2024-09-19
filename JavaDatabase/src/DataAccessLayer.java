import java.sql.*;
import java.util.ArrayList;

public class DataAccessLayer {
    // *SET THIS TO TRUE ONLY IF YOU INTEND TO WIPE THE DATABASE ON NEXT RUN *//
    // Should be set to false outside of testing!
    static final boolean dropAllData = false;

    private final String createSQLTable = "CREATE TABLE RepairGuide (" +
            "id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
            "title VARCHAR(255), " +
            "content VARCHAR(10000))";

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
     * Returns a list of every guide in DB as Guide objects.
     * 
     * @param conn the connection of the database to query
     * @return ArrayList containing all Guides
     * @throws SQLException throws exception if query fails
     */
    public ArrayList<Guide> queryAllGuides(Connection conn) throws SQLException {
        String query = "SELECT id, title, content FROM RepairGuide";
        ArrayList<Guide> guides = new ArrayList<>();

        // Query the database and retrieve all results
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(query);

        // Go through results and add into the array as `Guides`
        while (rs.next()) {
            Guide g = new Guide();
            g.setId(rs.getInt("id"));
            g.setTitle(rs.getString("title"));
            g.setContent(rs.getString("content"));
            guides.add(g);
        }

        return guides;
    }

    /**
     * Creates new guide in db with title and content in parameters
     * 
     * @param conn    the connection as a Connection object to the database to
     *                insert into
     * @param title   the String title of the guide
     * @param content the String content of the guide
     * @throws SQLException throws exception for various reasons, bad connection,
     *                      wrong data type, table does not exist and others
     */
    public void insertGuide(Connection conn, String title, String content) throws SQLException {
        String sql = "INSERT INTO RepairGuide (title, content) VALUES (?, ?)";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1, title);
        stm.setString(2, content);
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
        Guide g = new Guide();
        while (rs.next()) {
            g.setId(rs.getInt("id"));
            g.setTitle(rs.getString("title"));
            g.setContent(rs.getString("content"));
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
            Guide g = new Guide();
            g.setId(rs.getInt("id"));
            g.setTitle(rs.getString("title"));
            g.setContent(rs.getString("content"));
            guides.add(g);
        }
        rs.close();
        stm.close();
        return guides;
    }

}