import java.sql.*;
import java.util.Scanner;

public class ScrollableResultSetExample {
    public static void main(String[] args) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Scanner scanner = new Scanner(System.in);

        try {
            // Establish connection
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String URL = "jdbc:oracle:thin:@localhost:1521:xe";
            String USER = "SYSTEM";
            String PASSWORD = "BCA5C";
            con = DriverManager.getConnection(URL, USER, PASSWORD);

            // Use a scrollable result set
            String query = "SELECT * FROM Employee";
            pstmt = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pstmt.executeQuery();

            // Menu to scroll through results
            System.out.println("Scrollable Employee Records:");
            boolean exit = false;
            while (!exit) {
                System.out.println("\nEnter command (next, previous, first, last, exit): ");
                String command = scanner.nextLine();

                switch (command.toLowerCase()) {
                    case "next":
                        if (rs.next()) {
                            printEmployee(rs);
                        } else {
                            System.out.println("No more records.");
                        }
                        break;
                    case "previous":
                        if (rs.previous()) {
                            printEmployee(rs);
                        } else {
                            System.out.println("No more records.");
                        }
                        break;
                    case "first":
                        if (rs.first()) {
                            printEmployee(rs);
                        }
                        break;
                    case "last":
                        if (rs.last()) {
                            printEmployee(rs);
                        }
                        break;
                    case "exit":
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid command.");
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Helper method to print employee details
    public static void printEmployee(ResultSet rs) throws SQLException {
        System.out.println("EID: " + rs.getInt("Eid") + ", Name: " + rs.getString("Ename") +
                           ", Salary: " + rs.getDouble("Salary") + ", Address: " + rs.getString("Address") +
                           ", Did: " + rs.getInt("Did"));
    }
}
