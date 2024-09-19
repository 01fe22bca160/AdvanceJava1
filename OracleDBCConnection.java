import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleDBCConnection {
    public static void main(String[] args) {
        Connection con = null;
        try {
            // Register Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Open a connection to the Oracle DB
            String URL = "jdbc:oracle:thin:@localhost:1521:xe"; // Update with your DB URL
            String USER = "SYSTEM"; // Your Oracle DB username
            String PASSWORD = "BCA5C"; // Your Oracle DB password

            con = DriverManager.getConnection(URL, USER, PASSWORD);

            // Verify the connection
            if (con != null) {
                System.out.println("Connection to Oracle Database is successful!");
            } else {
                System.out.println("Failed to connect to Oracle Database.");
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
