package Controller;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {


	public static void main(String[] args) throws InvalidInputException, IncorrectPasswordException {
		// go to log in menu
		login();
   
	}
	
	// method to take user login info and direct user to first choice menu
	public static void login() {
		
		// variables to hold the user's inputs
		String usernameInput = null;
		String passwordInput = null;
		
		int userId = 0;
		
		// ask for username and password, store them in usernameInput and passwordInput
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter username.");
		usernameInput = sc.next();
		System.out.println("Please enter password");
		passwordInput = sc.next();
				 
	    Connection conn = ConnectionManager.getConnection();
		
	    // SQL statement to get the record for the username entered
	    try (PreparedStatement pstmt =
	    		 conn.prepareStatement("SELECT * from user where user_username = '" + usernameInput + "';")){

	    	 boolean loginSuccess = false;
	    	 ResultSet userInfo = pstmt.executeQuery();

	    	 // get values from user_id, store it in variable, check if user_password matches passwordInput 
	    	 while(userInfo.next()) {
	    		 userId = userInfo.getInt("user_id");
	    		 
	    		 // if passwords match, loginSuccess = true
	    		 if(userInfo.getString("user_password").equals(passwordInput)) {			
	    			 loginSuccess = true;
	    			 System.out.println("login successful!");
	    			 System.out.println("user id = " + userId);
	    			 
	    		 // if passwords don't match, go back to beginning of method and ask for username again
	    		 }else {
	    			 throw new IncorrectPasswordException();
	    		 }	    				
	    	 }
	    	 
	    	 // if passwords match, ask user what to do next
	    	 if (loginSuccess == true) {
	    		 loginMenu(userId);
	    	 }
			
	     } catch(SQLException e) {
	    	 e.printStackTrace();
	     } catch (IncorrectPasswordException e) {
	    	 System.out.println("Incorrect password.  Login failed!");
	    	 login();
		}
	}

	
	
	public static void loginMenu(int userId) throws SQLException, IncorrectPasswordException {
		
		int choice;
		Scanner sc = new Scanner(System.in);

		System.out.println("Do you want to: ");
		System.out.println("1. View/edit your watch list \n2. View the database \n3. Logout \n4. Change password");
		
		try {
			choice = sc.nextInt();
			
			// go to user's watch list
			if(choice == 1) {
				dashboard(userId);
			 
			// display list of shows in the database by alphabetical order
			}else if(choice == 2) {
				displayShowsDB(userId);
				 
		    // go back to login menu
			}else if(choice == 3) {
				 login();
				 
			// go to method to update user password
			}else if(choice == 4) {
				updateUserPassword(userId);
			}else {
				throw new InvalidInputException();
			}
		} catch (InvalidInputException e) {
	    	 System.out.println("Invalid input.  Please try again.");
	    	 loginMenu(userId);
		} catch (java.util.InputMismatchException e) {
	    	 System.out.println("Invalid input.  Please try again.");
	    	 loginMenu(userId);
		}
		
	}
	
	
	// method to display user's personal watch list, gives option to update trackers, go to database, or change password
	public static void dashboard(int userId) throws SQLException, InvalidInputException, IncorrectPasswordException {
		

	    Connection conn = ConnectionManager.getConnection();
	    
	    // statement to join the tables to display the users tracker list
		PreparedStatement pstmt = conn.prepareStatement("SELECT t.user_id, s.show_id, s.show_title, t.status, t.tracker_id from tracker as t inner join shows as s on t.show_id = s.show_id where user_id = '" + userId + "';");
		ResultSet rs = pstmt.executeQuery();
		
		// print the tracker list to the console
		while(rs.next()){
			System.out.println("ID: " + rs.getString("t.tracker_id") + "  " + rs.getString("s.show_title") + ", STATUS: " + rs.getString("t.status"));
		}
				
		Scanner sc = new Scanner(System.in);

		// ask user to select an option
		System.out.println("Enter a number for your choice");
		System.out.println("1: Update status of a show \n2: Search for a new show \n3: Logout \n4: change password");
		
		// store user's input
		int choice = sc.nextInt();
		
		// call update status method
		if(choice == 1) {
			updateStatus(userId);
			
		// display list of shows in the database
		}else if(choice == 2) {
			displayShowsDB(userId);
			
		// return to log in menu
		}else if(choice == 3) { 
			login();
			
		// call method to update the user's password
		}else if(choice == 4) {
			updateUserPassword(userId);
		}
		sc.close();
		
	}
	
	// method to change the status of a show in the user's tracker list
	public static void updateStatus(int userId) throws SQLException, InvalidInputException, IncorrectPasswordException {
		int newStatus;
		String sqlStatement = null;
		System.out.println("Select a tracker ID to update");	
		
		Connection conn = ConnectionManager.getConnection();
		
		// statement to display the user's tracker list
		PreparedStatement pstmt = conn.prepareStatement("SELECT t.user_id, s.show_id, s.show_title, t.status, t.tracker_id from tracker as t inner join shows as s on t.show_id = s.show_id where user_id = '" + userId + "';");

		pstmt.executeQuery();
		
		// user inputs id number of the tracker they want to update
		Scanner sc = new Scanner(System.in);
		int updateId = sc.nextInt();
		System.out.println("updateId = " + updateId);
		System.out.println("Select new status: 1 for 'in progress' or 2 for 'finished' or 3 to remove from your watch list or 4 to go back");
		newStatus = sc.nextInt();
		
		// if user selects 1, change status to 'in progress'
		if(newStatus == 1) {
			sqlStatement = "update tracker set status = 'in progress' where tracker_id = " + updateId + ";";
		// if user selects 2, change status to 'finished'
		} else if(newStatus == 2) {
			sqlStatement = "update tracker set status = 'finished' where tracker_id = " + updateId + ";";
		// if user selects 3, call method to delete the tracker from the database
		} else if(newStatus == 3) {
			removeFromWatchList(updateId, userId);
		// go back to the user's tracker list
		} else if(newStatus == 4) {
			dashboard(userId);
		}
		
		// execute the statement based on the user's choice
		PreparedStatement ps = conn.prepareStatement(sqlStatement);
		ps.execute();
		
		// after updating the database, go back to the user's watch list
		dashboard(userId);
	}
	
	// method to display list of shows in the database
	public static void displayShowsDB(int userId) throws SQLException, InvalidInputException, IncorrectPasswordException {
		Connection conn = ConnectionManager.getConnection();
		
		// statement to get list of all shows
		String sql = "SELECT * FROM shows;";
		PreparedStatement stst = conn.prepareStatement(sql);

		// print list of shows and their ID numbers to the console
	    ResultSet result = stst.executeQuery();
    	while(result.next()) {
    		System.out.println(result.getString("show_id") + " " + result.getString("show_title"));
    	 }
		
    	// ask user to choose what to do
		System.out.println("Select an option:");
		System.out.println("1: Add show to watch List    2: Go to watch list");
		
		Scanner sc = new Scanner(System.in);
		int choice = sc.nextInt();
		
		// if user picks 1, ask user to pick a show then call addToWatchList method to add the tracker to the database
		if(choice == 1) {
			System.out.println("Enter the ID number of the show you want to add");
			int showId = sc.nextInt();
			addToWatchList(showId, userId);
		// if user picks 2, go back to the user's tracker list
		}else if(choice == 2) {
			dashboard(userId);
		}
		
	}
	

	// method to add a show to a user's tracker list
    public static boolean addToWatchList(int showId, int userId) throws SQLException, InvalidInputException, IncorrectPasswordException{
    	
    	Connection conn = ConnectionManager.getConnection();
    	boolean success = false;
    	
    	// statement to insert a new tracker record into the database
    	String sql = "INSERT INTO tracker(user_id, show_id, status) values (" + userId + ", " + showId + ", 'not started');";
    	PreparedStatement pstmt = conn.prepareStatement(sql);
    	int row = pstmt.executeUpdate();
    		
    	// if added successfully, print success statement
   		if(row > 0) {
   			System.out.println("A new show has been added successfully!");
   			success = true;
   		}else {
   			System.out.println("Failed to add show.");
   		}
   		
  		conn.close();
  		
  		// go back to list of shows in the database
   		displayShowsDB(userId);
   		return success;
    }
	
	// method to remove a show from a user's tracker list
	public static void removeFromWatchList(int trackerId, int userId) throws SQLException, InvalidInputException, IncorrectPasswordException {
		
    	Connection conn = ConnectionManager.getConnection();
    	
    	// statement to delete the tracker from the database
    	PreparedStatement pstmt = conn.prepareStatement("DELETE from tracker where tracker_id = " + trackerId + ";");	
   		int showRemoved = pstmt.executeUpdate();
   		if(showRemoved > 0) {
   			System.out.println("Show has been removed!");    			
   		}
   		
   		// go back to user's watch list
   		dashboard(userId);
	}
	
	// method to change the user's password
	public static void updateUserPassword(int userId) throws SQLException, InvalidInputException, IncorrectPasswordException {
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter your new password.");
		String newPassword = sc.next();

		Connection conn = ConnectionManager.getConnection();
		
		// statement to update the user's password in the database
		String sqlStatement ="UPDATE user SET user_password = '" + newPassword + "' WHERE user_Id = '" + userId + "';";
		PreparedStatement ps = conn.prepareStatement(sqlStatement);
		ps.execute();
		
		// go back to user's watch list
		dashboard(userId);
		conn.close();
		
	}
	
	// this method is not implemented.  It would only be used by an admin user to edit the database of shows
    public static void newShow(int userId) throws SQLException, InvalidInputException, IncorrectPasswordException {
    
    	Connection conn = ConnectionManager.getConnection();
    	
    	System.out.println("Enter the name of a show you want to add: ");
    	
    	Scanner sc = new Scanner(System.in);
    	String showName = sc.nextLine();
    	    	
		PreparedStatement ps = conn.prepareStatement("INSERT INTO shows(show_name) values('" + showName + "');");
		ps.execute();
    	
		dashboard(userId);
  	}
	
}

