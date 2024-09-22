import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CrudOperationExample {

    // Method to establish a connection to the MySQL database
    public static Connection getConnection() {
        Connection conn = null;
        try {
            String url = "jdbc:mysql://localhost:3306/JavaAssignment";  // Replace with your database name
            String username = "root";  // Replace with your MySQL username
            String password = "Rakshita@2003";  // Replace with your MySQL password
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to MySQL Database.");
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return conn;
    }

    // Method to insert a new department
    public static void insertDepartment(Connection conn, Scanner sc) {
        try {
            System.out.println("Enter Department ID: ");
            long did = sc.nextInt();
            sc.nextLine(); // Consume newline

            System.out.println("Enter Department Name: ");
            String dname = sc.nextLine();

            String insertDept = "INSERT INTO Department (Did, Dname) VALUES (?, ?)";
            PreparedStatement pstmtDept = conn.prepareStatement(insertDept);
            pstmtDept.setLong(1, did);
            pstmtDept.setString(2, dname);
            pstmtDept.executeUpdate();
            System.out.println("Department inserted successfully!");
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // MySQL Error Code for duplicate entry
                System.out.println("Error: Department ID already exists.");
            } else {
                e.printStackTrace();
            }
        }
    }

    // Method to insert a new employee
    public static void insertEmployee(Connection conn, Scanner sc) {
        try {
            System.out.println("Enter Employee ID: ");
            int eid = sc.nextInt();
            sc.nextLine(); // Consume newline

            System.out.println("Enter Employee Name: ");
            String ename = sc.nextLine();

            System.out.println("Enter Employee Salary: ");
            double salary = sc.nextDouble();
            sc.nextLine(); // Consume newline

            System.out.println("Enter Employee Address: ");
            String Employeecol = sc.nextLine();

            System.out.println("Enter Department ID for the Employee: ");
            int empDid = sc.nextInt();

            String insertEmp = "INSERT INTO Employee (EmpID, Ename, Salary, Employeecol , Did) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmtEmp = conn.prepareStatement(insertEmp);
            pstmtEmp.setInt(1, eid);
            pstmtEmp.setString(2, ename);
            pstmtEmp.setDouble(3, salary);
            pstmtEmp.setString(4, Employeecol);
            pstmtEmp.setInt(5, empDid);
            pstmtEmp.executeUpdate();
            System.out.println("Employee inserted successfully!");
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // MySQL Error Code for duplicate entry
                System.out.println("Error: Employee ID already exists.");
            } else if (e.getErrorCode() == 1452) { // MySQL Error Code for foreign key constraint failure
                System.out.println("Error: Department ID does not exist.");
            } else {
                e.printStackTrace();
            }
        }
    }

    // Method to update employee salary
    public static void updateEmployeeSalary(Connection conn, Scanner sc) {
        try {
            System.out.println("Enter Employee ID to update salary: ");
            int eid = sc.nextInt();

            System.out.println("Enter new salary: ");
            double newSalary = sc.nextDouble();

            String updateQuery = "UPDATE Employee SET Salary = ? WHERE EmpID = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateQuery);
            pstmt.setDouble(1, newSalary);
            pstmt.setInt(2, eid);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Employee salary updated successfully!");
            } else {
                System.out.println("Employee with ID " + eid + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete an employee (already included in the original code)
    public static void deleteEmployee(Connection conn, Scanner sc) {
        try {
            System.out.println("Enter Employee ID to delete: ");
            int eid = sc.nextInt();

            String deleteQuery = "DELETE FROM Employee WHERE EmpID = ?";
            PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
            pstmt.setInt(1, eid);
            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Employee deleted successfully!");
            } else {
                System.out.println("Employee with ID " + eid + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a specific department
    public static void deleteDepartment(Connection conn, Scanner sc) {
        try {
            System.out.println("Enter Department ID to delete: ");
            int did = sc.nextInt();

            String checkDept = "SELECT COUNT(*) FROM Employee WHERE Did = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkDept);
            checkStmt.setInt(1, did);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int empCount = rs.getInt(1);

            if (empCount > 0) {
                System.out.println("Error: Department has employees and cannot be deleted.");
            } else {
                String deleteQuery = "DELETE FROM Department WHERE Did = ?";
                PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
                pstmt.setInt(1, did);
                int rowsDeleted = pstmt.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("Department deleted successfully!");
                } else {
                    System.out.println("Department with ID " + did + " not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete all departments
    public static void deleteAllDepartments(Connection conn) {
        try {
            String checkDept = "SELECT COUNT(*) FROM Employee";
            Statement checkStmt = conn.createStatement();
            ResultSet rs = checkStmt.executeQuery(checkDept);
            rs.next();
            int empCount = rs.getInt(1);

            if (empCount > 0) {
                System.out.println("Error: Some departments have employees and cannot be deleted.");
            } else {
                String deleteAllQuery = "DELETE FROM Department";
                PreparedStatement pstmt = conn.prepareStatement(deleteAllQuery);
                int rowsDeleted = pstmt.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("All departments deleted successfully!");
                } else {
                    System.out.println("No departments to delete.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to display all employees (already included in the original code)
    public static void displayAllEmployees(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            String selectQuery = "SELECT * FROM Employee";
            ResultSet rs = stmt.executeQuery(selectQuery);

            System.out.println("Employee Data:");
            while (rs.next()) {
                System.out.println(rs.getInt("EmpID") + " | " + rs.getString("Ename") + " | " + rs.getDouble("Salary") + " | " + rs.getString("Employeecol") + " | " + rs.getInt("Did"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to display all departments
    public static void displayAllDepartments(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            String selectQuery = "SELECT * FROM Department";
            ResultSet rs = stmt.executeQuery(selectQuery);

            System.out.println("Department Data:");
            while (rs.next()) {
                System.out.println(rs.getInt("Did") + " | " + rs.getString("Dname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Method to display employees working for a specific department
public static void displayEmployeesByDepartment(Connection conn, Scanner sc) {
    try {
        System.out.println("Enter Department ID to view employees: ");
       long did = sc.nextLong();

        String selectQuery = "SELECT * FROM Employee WHERE Did = ?";
        PreparedStatement pstmt = conn.prepareStatement(selectQuery);
        pstmt.setLong(1, did);
        ResultSet rs = pstmt.executeQuery();

        System.out.println("Employees in Department ID " + did + ":");
        boolean hasEmployees = false;
        while (rs.next()) {
            hasEmployees = true;
            System.out.println(rs.getInt("EmpID") + " | " + rs.getString("Ename") + " | " + rs.getDouble("Salary") + " | " + rs.getString("Employeecol"));
        }

        if (!hasEmployees) {
            System.out.println("No employees found in Department ID " + did);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    // Main method for menu-based CRUD operations
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Connection conn = null;

        try {
            conn = CrudOperationExample.getConnection();
            Statement stmt = conn.createStatement();

            // Creating Department and Employee tables if they don't exist
            String createDeptTable = "CREATE TABLE IF NOT EXISTS Department (Did long PRIMARY KEY, Dname VARCHAR(100))";
            stmt.execute(createDeptTable);

            String createEmpTable = "CREATE TABLE IF NOT EXISTS Employee (EmpID INT PRIMARY KEY, Ename VARCHAR(100), Salary DOUBLE, Employeecol VARCHAR(100), Did INT, FOREIGN KEY (Did) REFERENCES Department(Did))";
            stmt.execute(createEmpTable);

            boolean exit = false;
            while (!exit) {
                System.out.println("\nMENU:");
                System.out.println("1. Add Department");
                System.out.println("2. Add Employee");
                System.out.println("3. Update Employee Salary");
                System.out.println("4. Delete Employee");
                System.out.println("5. Delete Department");
                System.out.println("6. Delete All Departments");
                System.out.println("7. Display All Employees");
                System.out.println("8. Display All Departments");
                System.out.println("9. Display Employees by Department");
                System.out.println("10. Exit");
                System.out.print("Choose an option: ");

                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        insertDepartment(conn, sc);
                        break;
                    case 2:
                        insertEmployee(conn, sc);
                        break;
                    case 3:
                        updateEmployeeSalary(conn, sc);
                        break;
                    case 4:
                        deleteEmployee(conn, sc);
                        break;
                    case 5:
                        deleteDepartment(conn, sc);
                        break;
                    case 6:
                        deleteAllDepartments(conn);
                        break;
                    case 7:
                        displayAllEmployees(conn);
                        break;
                    case 8:
                        displayAllDepartments(conn);
                        break;
                    case 9:
                    displayEmployeesByDepartment(conn, sc);
                    break;
                    case 10:
                        exit = true;
                        System.out.println("Exiting program...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter valid data.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (sc != null) {
                sc.close();
            }
        }
    }
}