import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class main {
    public static void main(String[] args) {


        ManagerLayer dbm = new ManagerLayer();
        System.out.println("Running test");
        dbm.test();

    }
}
