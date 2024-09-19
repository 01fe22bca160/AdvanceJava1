import java.sql.*;
import java.util.Scanner;

public class Department {
    public static void main(String[] args) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Scanner scanner = new Scanner(System.in);

        try {
            // Step 1: Register Oracle driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Step 2: Open DB connection
            String URL = "jdbc:oracle:thin:@localhost:1521:xe";
            String USER = "SYSTEM";
            String PASSWORD = "BCA5C";
            con = DriverManager.getConnection(URL, USER, PASSWORD);

            if (con == null) {
                System.out.println("Connection Unsuccessful");
            } else {
                System.out.println("Connection Successful");
            }

            // Step 3: Insert data into Department table
            System.out.println("Insert into Department Table:");
            System.out.print("Enter Department ID: ");
            int deptId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter Department Name: ");
            String deptName = scanner.nextLine();

            // Step 4: Check if Department already exists to avoid duplicates
            String checkDepartment = "SELECT * FROM Department WHERE Did = ?";
            pstmt = con.prepareStatement(checkDepartment);
            pstmt.setInt(1, deptId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Department with ID " + deptId + " already exists.");
            } else {
                // Step 5: Insert the new Department record
                String insertDepartment = "INSERT INTO Department (Did, Dname) VALUES (?, ?)";
                pstmt = con.prepareStatement(insertDepartment);
                pstmt.setInt(1, deptId);
                pstmt.setString(2, deptName);
                pstmt.executeUpdate();
                System.out.println("Department inserted successfully.");

                // Step 6: Fetch and print the inserted department data
                String selectInserted = "SELECT * FROM Department WHERE Did = ?";
                pstmt = con.prepareStatement(selectInserted);
                pstmt.setInt(1, deptId);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    System.out.println("\nInserted Department Data:");
                    System.out.println("Did: " + rs.getInt("Did"));
                    System.out.println("Dname: " + rs.getString("Dname"));
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Step 7: Close resources
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            scanner.close();
        }
    }
}