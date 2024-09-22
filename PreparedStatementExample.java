import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PreparedStatementExample {
    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // 1. Establishing the connection
            String url = "jdbc:mysql://localhost:3306/JavaAssignment";  // Replace with your database name
            String username = "root";  // Replace with your MySQL username
            String password = "Rakshita@2003";  // Replace with your MySQL password
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to MySQL Database.");

            // 2. Inserting into Employee Table using PreparedStatement
            String insertEmp = "INSERT INTO Employee (EmpID, Ename, Salary, Employeecol, Did) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(insertEmp);

            // Inserting multiple employees
            pstmt.setInt(1, 106);
            pstmt.setString(2, "Bob Smith");
            pstmt.setDouble(3, 70000);
            pstmt.setString(4, "Texas");
            pstmt.setInt(5, 3);
            pstmt.executeUpdate();

            pstmt.setInt(1, 107);
            pstmt.setString(2, "Alice Johnson");
            pstmt.setDouble(3, 65000);
            pstmt.setString(4, "Florida");
            pstmt.setInt(5, 4);
            pstmt.executeUpdate();

            pstmt.setInt(1, 108);
            pstmt.setString(2, "Michael Lee");
            pstmt.setDouble(3, 72000);
            pstmt.setString(4, "Nevada");
            pstmt.setInt(5, 2);
            pstmt.executeUpdate();

            // 3. Select Query using PreparedStatement
            String selectQuery = "SELECT * FROM Employee WHERE Did = ?";
            pstmt = conn.prepareStatement(selectQuery);
            
            // Selecting employees belonging to Department 3
            pstmt.setInt(1, 3);
            rs = pstmt.executeQuery();

            // Printing the results
            System.out.println("Employees in Department 3:");
            while (rs.next()) {
                System.out.println(rs.getInt("EmpID") + " | " + rs.getString("Ename") + " | " + rs.getDouble("Salary"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 4. Clean up resources
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}