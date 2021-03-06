package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    // Windows: jdbc:mysql://localhost:3306/university
    // Mac/Linux: jdbc:mysql://localhost:3306/university?serverTimezone=EST5EDT
    private static final String URL = "jdbc:mysql://localhost:3306/progress_tracker";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root"; // Windows: root
                                                       // Mac/Linux: Root@123
    
    public static Connection getConnection() 
        
        Connection conn = null;
        
        try {
            // DriverManager should automatically be able to find the MySQL Driver
            // but may not always do so
            
            // Register the driver -> load in the correct driver needed for our connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected");
            
        } catch (SQLException e) {
            System.out.println("Connection failed");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("connection failed");
            e.printStackTrace();
        }
        
        return conn;
    }

	
	
	
	
}
