package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import controller.HomeController;

 

public class DatabaseConnection {
	
	  // variables
	static Connection connection = null;
    static Statement statement = null;
    static ResultSet resultSet = null;
    
    static String db_directory = HomeController.db_directory;
    
    
    public  static Connection getDB_Connection() {
    	
    	db_directory = HomeController.db_directory;
    	
    	// Step 1: Loading or registering Oracle JDBC driver class
        try {

            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        }
        catch(ClassNotFoundException cnfex) {

            System.out.println("Problem in loading or "
                    + "registering MS Access JDBC driver");
            cnfex.printStackTrace();
        }

        // Step 2: Opening database connection
        try {

            String msAccDB = db_directory;
            String dbURL = "jdbc:ucanaccess://" + msAccDB; 

            // Step 2.A: Create and get connection using DriverManager class
            connection = DriverManager.getConnection(dbURL); 

        }
        
        catch(SQLException sqlex){
            sqlex.printStackTrace();
        }
        
        return connection;
    	
    }
    
    
    
    public  static void closeDB_Connection(Connection connection ) {
    	
    	try {
            if(null != connection) {

                // cleanup resources, once after processing
                resultSet.close();
                statement.close();

                // and then finally close connection
                connection.close();
            }
        }
        catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    	
    	
    }
    
    
 
}

