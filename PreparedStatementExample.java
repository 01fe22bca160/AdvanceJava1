import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class PreparedStatementExample {
    public static void main(String[] args) {
        Connection con = null;
        PreparedStatement pstmt = null;
        Scanner scanner = new Scanner(System.in);

        try {
            // Establish connection
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String URL = "jdbc:oracle:thin:@localhost:1521:xe";
            String USER = "SYSTEM";
            String PASSWORD = "BCA5C";
            con = DriverManager.getConnection(URL, USER, PASSWORD);

            // Get user input
            System.out.print("Enter Employee ID for insertion: ");
            int eid = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter Employee Name: ");
            String ename = scanner.nextLine();

            System.out.print("Enter Employee Salary: ");
            double salary = scanner.nextDouble();

            System.out.print("Enter Employee Address: ");
            scanner.nextLine(); // Consume newline
            String address = scanner.nextLine();

            System.out.print("Enter Department ID: ");
            int did = scanner.nextInt();

            // Insert a new employee
            String insertQuery = "INSERT INTO Employee (Eid, Ename, Salary, Address, Did) VALUES (?, ?, ?, ?, ?)";
            pstmt = con.prepareStatement(insertQuery);
            pstmt.setInt(1, eid);
            pstmt.setString(2, ename);
            pstmt.setDouble(3, salary);
            pstmt.setString(4, address);
            pstmt.setInt(5, did);
            int rowsInserted = pstmt.executeUpdate();
            System.out.println(rowsInserted + " row(s) inserted.");

            // Update employee's salary
            System.out.print("Enter Employee ID to update salary: ");
            int updateEid = scanner.nextInt();

            System.out.print("Enter new salary: ");
            double newSalary = scanner.nextDouble();

            String updateQuery = "UPDATE Employee SET Salary = ? WHERE Eid = ?";
            pstmt = con.prepareStatement(updateQuery);
            pstmt.setDouble(1, newSalary);
            pstmt.setInt(2, updateEid);
            int rowsUpdated = pstmt.executeUpdate();
            System.out.println(rowsUpdated + " row(s) updated.");

            // Delete an employee
            System.out.print("Enter Employee ID to delete: ");
            int deleteEid = scanner.nextInt();

            String deleteQuery = "DELETE FROM Employee WHERE Eid = ?";
            pstmt = con.prepareStatement(deleteQuery);
            pstmt.setInt(1, deleteEid);
            int rowsDeleted = pstmt.executeUpdate();
            System.out.println(rowsDeleted + " row(s) deleted.");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
                scanner.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
