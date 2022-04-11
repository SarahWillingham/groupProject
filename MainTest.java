package main;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainTest {

	
	private static Main testLogin;
	
	private static int testUserid;
	
	private static int testShowid;
	
	private static int testTrackerid;
	
	private static int testCount;
	
	
	//run once before any of the tests
	
		@BeforeAll
		public static void setup() {
			
			testLogin = new Main();
			
			
				testCount = 0;

		}
		
		
		// run once after all the tests
		@AfterAll
		public static void cleanup() {
			
			System.out.println("All tests have been run," + " total tests run is " + testCount);
			testCount = 0;
		}
		
		
		// run before each test
		@BeforeEach
		public void beforeTest() {
			
			
			System.out.println("Running test.....");
	
		}
		
		// run after each test
		@AfterEach
		public void afterTest() {
			
			testCount++;
			System.out.println("Finished Running test," + " testCount = " + testCount);
			
		}
		
		
//		
		
	//	@Test
//		public void testLogin() {
//			//set variable holder for actual result
//			boolean result = false;
//	
//			
//			//call the login method here
//			result = testLogin.login();
//			boolean expected = true;
//				
//				assertEquals(expected, result);
//				
//			
//		
//		}
//		
//	
//		
//		@Test
//		public void testUpdateStatus() {
//			//set variable holder for actual result
//			
//			boolean actual = false;
//			
//			//call the add method here
//		
//			actual	= testLogin.login();
//				
//
//			boolean expected = true;
//			
//			assertEquals(expected, actual);
//			
//		}
		
		@Test
		public void testAddShow() {
			
			//set variable holder for actual result
			
			boolean actual = false;
			
			
			
			boolean expected = true;
			try {
				expected = testLogin.addToWatchList(testShowid, testUserid);
			} catch (SQLException | InvalidInputException | IncorrectPasswordException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			assertEquals(expected, actual);
		
		}
		
				
		
}	