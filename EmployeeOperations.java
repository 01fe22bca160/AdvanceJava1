import java.sql.*;
import java.util.Scanner;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

public class EmployeeOperations {
    public static void main(String[] args) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Scanner scanner = new Scanner(System.in);

        try {
            // Register Oracle driver
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

            // Menu for user to choose operation
            while (true) {
                System.out.println("\nChoose an operation:");
                System.out.println("1. Insert new Employee");
                System.out.println("2. Delete Employee");
                System.out.println("3. Update Employee");
                System.out.println("4. Query Employees");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        // Insert Employee
                        insertEmployee(con, scanner);
                        break;
                    case 2:
                        // Delete Employee
                        deleteEmployee(con, scanner);
                        break;
                    case 3:
                        // Update Employee
                        updateEmployee(con, scanner);
                        break;
                    case 4:
                        // Query Employees
                        queryEmployees(con);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            // Step 7: Close all connections and resources
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (con != null) con.close();
            scanner.close();
        }
    }

    // Method to insert employee
    public static void insertEmployee(Connection con, Scanner scanner) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        System.out.print("Enter Employee ID (Eid): ");
        int empId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter Employee Name (Ename): ");
        String empName = scanner.nextLine();

        System.out.print("Enter Employee Salary: ");
        double empSalary = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter Employee Address: ");
        String empAddress = scanner.nextLine();

        System.out.print("Enter Department ID (Did): ");
        int deptId = scanner.nextInt();

        // Check if Department exists
        String checkDeptQuery = "SELECT * FROM Department WHERE Did = ?";
        pstmt = con.prepareStatement(checkDeptQuery);
        pstmt.setInt(1, deptId);
        rs = pstmt.executeQuery();

        if (!rs.next()) {
            System.out.println("Department with ID " + deptId + " does not exist.");
            return;
        }

        // Check if Employee already exists
        String checkQuery = "SELECT * FROM Employee WHERE Eid = ?";
        pstmt = con.prepareStatement(checkQuery);
        pstmt.setInt(1, empId);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            System.out.println("Employee with ID " + empId + " already exists.");
        } else {
            String insertQuery = "INSERT INTO Employee (Eid, Ename, Salary, Address, Did) VALUES (?, ?, ?, ?, ?)";
            pstmt = con.prepareStatement(insertQuery);
            pstmt.setInt(1, empId);
            pstmt.setString(2, empName);
            pstmt.setDouble(3, empSalary);
            pstmt.setString(4, empAddress);
            pstmt.setInt(5, deptId);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Employee inserted successfully.");
            }
        }
    }

    // Method to delete employee
    public static void deleteEmployee(Connection con, Scanner scanner) throws SQLException {
        PreparedStatement pstmt = null;

        System.out.print("Enter Employee ID (Eid) to delete: ");
        int empId = scanner.nextInt();

        String deleteQuery = "DELETE FROM Employee WHERE Eid = ?";
        pstmt = con.prepareStatement(deleteQuery);
        pstmt.setInt(1, empId);

        int rowsDeleted = pstmt.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Employee with ID " + empId + " deleted successfully.");
        } else {
            System.out.println("Employee with ID " + empId + " does not exist.");
        }
    }

    // Method to update employee details
    public static void updateEmployee(Connection con, Scanner scanner) throws SQLException {
        PreparedStatement pstmt = null;

        System.out.print("Enter Employee ID (Eid) to update: ");
        int empId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new Employee Name (Ename): ");
        String empName = scanner.nextLine();

        System.out.print("Enter new Employee Salary: ");
        double empSalary = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new Employee Address: ");
        String empAddress = scanner.nextLine();

        String updateQuery = "UPDATE Employee SET Ename = ?, Salary = ?, Address = ? WHERE Eid = ?";
        pstmt = con.prepareStatement(updateQuery);
        pstmt.setString(1, empName);
        pstmt.setDouble(2, empSalary);
        pstmt.setString(3, empAddress);
        pstmt.setInt(4, empId);

        int rowsUpdated = pstmt.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Employee with ID " + empId + " updated successfully.");
        } else {
            System.out.println("Employee with ID " + empId + " does not exist.");
        }
    }

    // Method to query and display employees using RowSet
    public static void queryEmployees(Connection con) throws SQLException {
        CachedRowSet rowSet = null;
        Statement stmt = null;

        try {
            // Create RowSet
            rowSet = RowSetProvider.newFactory().createCachedRowSet();
            
            // Query to get all employees
            String query = "SELECT * FROM Employee";
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Populate RowSet with result
            rowSet.populate(rs);

            // Display the results
            System.out.println("\nEmployee Records:");
            while (rowSet.next()) {
                System.out.println("Eid: " + rowSet.getInt("Eid"));
                System.out.println("Ename: " + rowSet.getString("Ename"));
                System.out.println("Salary: " + rowSet.getDouble("Salary"));
                System.out.println("Address: " + rowSet.getString("Address"));
                System.out.println("Did: " + rowSet.getInt("Did"));
                System.out.println("----------------------");
            }
        } finally {
            if (rowSet != null) rowSet.close();
            if (stmt != null) stmt.close();
        }
    }
}