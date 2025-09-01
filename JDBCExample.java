import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBCExample {
    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database (replace the URL, username, and password)
            String url = "jdbc:mysql://localhost:3306";
            String username = "root";
            String password = "root";
            conn = DriverManager.getConnection(url, username, password);

            // Check if the connection was successful
            if (conn != null) {
                System.out.println("Database connected successfully!");
            } else {
                System.out.println("Failed to connect to the database.");
            }
 // Create a statement object to execute SQL queries
            stmt = conn.createStatement();
// Step 1: Create the database
            String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS testdb";
            stmt.executeUpdate(createDatabaseSQL);
            System.out.println("Database 'testdb' created successfully (or already exists).");

// Step 2: Use the database
            String useDatabaseSQL = "USE testdb";
            stmt.executeUpdate(useDatabaseSQL);
            System.out.println("Using database 'testdb'.");

// Step 3: Create the table
            String createTableSQL = "CREATE TABLE IF NOT EXISTS employees ("
                                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                                    + "name VARCHAR(50) NOT NULL, "
                                    + "age INT, "
                                    + "position VARCHAR(50), "
                                    + "salary DECIMAL(10, 2)"
                                    + ")";
            stmt.executeUpdate(createTableSQL);
            System.out.println("Table 'employees' created successfully (or already exists).");

  // Step 4: Insert data into the employees table
            String insertDataSQL = "INSERT INTO employees (name, age, position, salary) "
                                 + "VALUES ('John Doe', 30, 'Software Engineer', 75000.00)";
            int rowsAffected = stmt.executeUpdate(insertDataSQL);
            if (rowsAffected > 0) {
                System.out.println("Data inserted successfully!");
            }
           
        } catch (Exception e) {
            // Print the exception if connection fails
            System.out.println("An error occurred while connecting to the database:");
            e.printStackTrace();
        } finally {
            try {
                // Close the statement and connection

                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
