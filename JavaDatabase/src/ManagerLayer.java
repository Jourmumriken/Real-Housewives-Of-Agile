import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManagerLayer {
    DataAccessLayer db;
     Connection conn;
     final String DatabaseURL = "jdbc:derby:RepairWikiDB;create=true";
    public ManagerLayer( ) {
        this.conn=setupConn();
        this.db = new DataAccessLayer(conn);
    }

    /**
     * gets a connection to the database in the DatabaseURL field
     * @return a Connection object-the connection to the database
     */
    private Connection setupConn() {
        try {
            return DriverManager.getConnection(DatabaseURL);
        }
        catch (SQLException e) {

        }
        return null;
    }

    // TODO Just a temporary test, remove later
    public void test() {
        try {
            db.insertGuide(conn, "How to repair electric toothbrush", "SHOVE IT UP YOUR ASS!!");
            ArrayList<Guide> res = db.queryAllGuides(conn);
            System.out.println(res.getFirst().toString());

        } catch (SQLException e) {
            System.out.println("SQL Exception in DB manager");
        }
    }
}
