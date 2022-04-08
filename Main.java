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

		login();
   
	}
	
	
	public static void login() {
		String usernameInput = null;
		String passwordInput = null;
		int userId = 0;
		int choice;
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter username.");
		usernameInput = sc.next();
		System.out.println("Please enter password");
		passwordInput = sc.next();
				 
	    Connection conn = ConnectionManager.getConnection();
		
	     
	    try (PreparedStatement pstmt =
	    		 conn.prepareStatement("SELECT * from user where user_username = '" + usernameInput + "';")){

	    	 boolean loginSuccess = false;
	    	 ResultSet userInfo = pstmt.executeQuery();

	    	 while(userInfo.next()) {
	    		 userId = userInfo.getInt("user_id");
	    		 if(userInfo.getString("user_password").equals(passwordInput)) {			
	    			 loginSuccess = true;
	    			 System.out.println("login successful!");
	    			 System.out.println("user id = " + userId);
	    		 }else {
	    			 System.out.println("login failed!");
	    		 }	    				
	    				
	    	 }
	    	 if (loginSuccess == true) {
	    		 System.out.println("Do you want to: ");
	    		 System.out.println("1. View/edit your watch list      2. View the database		3. Logout");
	    		 choice = sc.nextInt();
	    		 if(choice == 1) {
	    			 dashboard(userId);
	    		 }else if(choice == 2) {
	    			 //function to send user to database
	    		 }else if(choice == 3) {
	    			 login();
	    		 }
	    	 }
			
	     } catch(SQLException e) {
	    	 e.printStackTrace();
	     }  
	}

	
	
	public static void dashboard(int userId) throws SQLException {
		

	    Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT t.user_id, s.show_id, s.show_title, t.status, t.tracker_id from tracker as t inner join shows as s on t.show_id = s.show_id where user_id = '" + userId + "';");
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()){
			System.out.println("ID: " + rs.getString("t.tracker_id") + "  " + rs.getString("s.show_title") + ", STATUS: " + rs.getString("t.status"));
		}
				
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter a number for your choice");
		System.out.println("1: Update status of a show,  2: Search for a new show, 3: Logout");
		int choice = sc.nextInt();
		if(choice == 1) {
			updateStatus(userId);
		}else if(choice == 2) {
			// go to database search from here
		
		}else if(choice == 3) 
			
		sc.close();
		
	}
	
	
	public static void updateStatus(int userId) throws SQLException {
		int newStatus;
		String sqlStatement = null;
		System.out.println("Select a tracker ID to update");	
		
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT t.user_id, s.show_id, s.show_title, t.status, t.tracker_id from tracker as t inner join shows as s on t.show_id = s.show_id where user_id = '" + userId + "';");

		pstmt.executeQuery();


		
		Scanner sc = new Scanner(System.in);
		int updateId = sc.nextInt();
		System.out.println("updateId = " + updateId);
		System.out.println("Select new status: 1 for 'in progress' or 2 for 'finished' or 3 to remove from your watch list or 4 to go back");
		newStatus = sc.nextInt();
		
		if(newStatus == 1) {
			sqlStatement = "update tracker set status = 'in progress' where tracker_id = " + updateId + ";";
		} else if(newStatus == 2) {
			sqlStatement = "update tracker set status = 'finished' where tracker_id = " + updateId + ";";
		} else if(newStatus == 3) {
			sqlStatement = "delete from tracker where tracker_id = " + updateId + ";";
		} else if(newStatus == 4) {
			dashboard(userId);
		}
		
		
		PreparedStatement ps = conn.prepareStatement(sqlStatement);
		ps.execute();
		
		dashboard(userId);
	}
	

}
