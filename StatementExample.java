import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class StatementExample {
    public static void main(String[] args) {
        Connection con = null;
        Statement stmt = null;
        Scanner scanner = new Scanner(System.in);

        try {
            // Establish connection
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String URL = "jdbc:oracle:thin:@localhost:1521:xe";
            String USER = "SYSTEM";
            String PASSWORD = "BCA5C";
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = con.createStatement();

            // Drop existing tables if they exist
            try {
                stmt.execute("DROP TABLE Employee");
                stmt.execute("DROP TABLE Department");
                System.out.println("Existing tables dropped.");
            } catch (SQLException e) {
                if (e.getErrorCode() == 942) { // ORA-00942: table or view does not exist
                    System.out.println("No existing tables to drop.");
                } else {
                    throw e;
                }
            }

            // Create Department table
            String createDeptTable = "CREATE TABLE Department (" +
                                      "Did NUMBER PRIMARY KEY, " +
                                      "Dname VARCHAR2(50))";
            stmt.execute(createDeptTable);
            System.out.println("Department table created.");

            // Create Employee table
            String createEmpTable = "CREATE TABLE Employee (" +
                                    "Eid NUMBER PRIMARY KEY, " +
                                    "Ename VARCHAR2(50), " +
                                    "Salary NUMBER, " +
                                    "Address VARCHAR2(100), " +
                                    "Did NUMBER, " +
                                    "FOREIGN KEY (Did) REFERENCES Department(Did))";
            stmt.execute(createEmpTable);
            System.out.println("Employee table created.");

            // Insert sample rows into Department table using Scanner
            for (int i = 1; i <= 5; i++) {
                System.out.print("Enter Department ID (Did) for Department " + i + ": ");
                int deptId = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                System.out.print("Enter Department Name (Dname) for Department " + i + ": ");
                String deptName = scanner.nextLine();
                String insertDeptQuery = "INSERT INTO Department (Did, Dname) VALUES (" + deptId + ", '" + deptName + "')";
                stmt.executeUpdate(insertDeptQuery);
            }
            System.out.println("Departments inserted.");

            // Insert sample rows into Employee table using Scanner
            for (int i = 1; i <= 5; i++) {
                System.out.print("Enter Employee ID (Eid) for Employee " + i + ": ");
                int empId = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                System.out.print("Enter Employee Name (Ename) for Employee " + i + ": ");
                String empName = scanner.nextLine();
                System.out.print("Enter Employee Salary for Employee " + i + ": ");
                double empSalary = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                System.out.print("Enter Employee Address for Employee " + i + ": ");
                String empAddress = scanner.nextLine();
                System.out.print("Enter Department ID (Did) for Employee " + i + ": ");
                int empDeptId = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                String insertEmpQuery = "INSERT INTO Employee (Eid, Ename, Salary, Address, Did) VALUES (" +
                                         empId + ", '" + empName + "', " + empSalary + ", '" + empAddress + "', " + empDeptId + ")";
                stmt.executeUpdate(insertEmpQuery);
            }
            System.out.println("Employees inserted.");

            // Select query to display data
            String selectQuery = "SELECT * FROM Employee";
            var rs = stmt.executeQuery(selectQuery);
            System.out.println("Employee Records:");
            while (rs.next()) {
                System.out.println("EID: " + rs.getInt("Eid") + ", Name: " + rs.getString("Ename") +
                                   ", Salary: " + rs.getDouble("Salary") + ", Address: " + rs.getString("Address") +
                                   ", Did: " + rs.getInt("Did"));
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (con != null) con.close();
                scanner.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
