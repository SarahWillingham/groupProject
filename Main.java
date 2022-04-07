package Controller;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {


	public static void main(String[] args) {

		String usernameInput;
		String passwordInput;
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter username.");
		usernameInput = sc.next();
		System.out.println("Please enter password");
		passwordInput = sc.next();
		
////		
//		 String selectStatement = ("SELECT " + usernameInput + " from user;");
//		 
	     Connection conn = ConnectionManager.getConnection();
//
//	     ConnectionManager.setPreparedStatement(conn, selectStatement);
//	     PreparedStatement ps = ConnectionManager.getPreparedStatement();
//	     ps.execute();

		
	     
	     try (PreparedStatement pstmt =
	    		 conn.prepareStatement("SELECT * from user where username = " + usernameInput + ";")){
	    				

	    				boolean loginSuccess = false;
	    				ResultSet userInfo = pstmt.executeQuery();
	    				
	    				
	    				while(userInfo.next()) {
	    					
	    					if(userInfo.getString("user_password") == passwordInput) {
	    						
	    						loginSuccess = true;
	    						System.out.println("login successful!");
	    					
	    					}else {
	    					System.out.println("login failed!");
	    				}	    				
	    				
	    			}
	    		} catch(SQLException e) {
	    				e.printStackTrace();
	    			}
	    			//no update occurred
	    			//return false;
		
		
		
	}

}
