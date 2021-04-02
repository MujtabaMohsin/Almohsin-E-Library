package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import controller.HomeController;
import database.DatabaseConnection;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class RetrieveData {
	
	

	public static ArrayList<ArrayList<String>> getCategoriesDB() {

		ArrayList<ArrayList<String>> categories_data = new ArrayList<ArrayList<String>>();
		ArrayList<String> category_names = new ArrayList<String>();
		ArrayList<String> number_of_books = new ArrayList<String>();
		ArrayList<String> folder_names = new ArrayList<String>();

		Connection connection = DatabaseConnection.getDB_Connection();

		try {

			// Creating JDBC Statement
			Statement statement = connection.createStatement();

			// Executing SQL & retrieve data into ResultSet
			ResultSet resultSet = statement
					.executeQuery("SELECT category_name,number_of_books,folder_name  FROM Categories");

			while (resultSet.next()) {

				category_names.add(resultSet.getString(1));
				number_of_books.add(String.valueOf(resultSet.getInt(2)));
				folder_names.add(resultSet.getString(3));

			}

			if (null != connection) {

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

		categories_data.add(category_names);
		categories_data.add(number_of_books);
		categories_data.add(folder_names);

		return categories_data;

	}

	public static ArrayList<ArrayList<String>> getCategoryBooksDB(String category) {

		ArrayList<ArrayList<String>> category_books_data = new ArrayList<ArrayList<String>>();
		ArrayList<String> author_names = new ArrayList<String>();
		ArrayList<String> book_names = new ArrayList<String>();

		Connection connection = DatabaseConnection.getDB_Connection();

		try {

			// Creating JDBC Statement
			Statement statement = connection.createStatement();

			// Executing SQL & retrieve data into ResultSet
			ResultSet resultSet = statement.executeQuery("SELECT book_name, author FROM " + category);

			while (resultSet.next()) {

				book_names.add(resultSet.getString(1));
				author_names.add(resultSet.getString(2));

			}

			if (null != connection) {

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

		category_books_data.add(book_names);
		category_books_data.add(author_names);

		return category_books_data;

	}

	public static ArrayList<String> getBookDB(String category, int id) {

		ArrayList<String> book_info = new ArrayList<String>();

		Connection connection = DatabaseConnection.getDB_Connection();

		try {

			// Creating JDBC Statement
			Statement statement = connection.createStatement();

			// Executing SQL & retrieve data into ResultSet
			ResultSet resultSet = statement.executeQuery(
					"SELECT book_name, author, investigator, source , ID FROM " + category + " WHERE ID = " + id);

			while (resultSet.next()) {

				book_info.add(resultSet.getString(1));
				book_info.add(resultSet.getString(2));
				book_info.add(resultSet.getString(3));
				book_info.add(resultSet.getString(4));
				book_info.add(String.valueOf(resultSet.getInt(5)));
				book_info.add(category);

			}

			if (null != connection) {

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

		return book_info;

	}

	public static ArrayList<ArrayList<String>> getMyBooksDB() {

		ArrayList<ArrayList<String>> Mybooks_data = new ArrayList<ArrayList<String>>();
		ArrayList<String> author_names = new ArrayList<String>();
		ArrayList<String> book_names = new ArrayList<String>();
		ArrayList<String> mybooks_categories = new ArrayList<String>();
		ArrayList<String> mybooks_ids = new ArrayList<String>();

		Connection connection = DatabaseConnection.getDB_Connection();

		try {

			// Creating JDBC Statement
			Statement statement = connection.createStatement();

			// Executing SQL & retrieve data into ResultSet
			ResultSet resultSet = statement.executeQuery("SELECT category, book_id FROM Mybooks");

			// first get data from mybooks table
			while (resultSet.next()) {

				String category_temp = resultSet.getString(1);
				mybooks_categories.add(category_temp);
				String book_id_temp = String.valueOf(resultSet.getInt(2));
				mybooks_ids.add(book_id_temp);

				// Executing SQL & retrieve data into ResultSet
				ResultSet resultSet2 = statement.executeQuery(
						"SELECT  book_name, author FROM " + category_temp + " WHERE ID = " + book_id_temp);

				// second get book info from the (category) table.
				while (resultSet2.next()) {
					book_names.add(resultSet2.getString(1));
					author_names.add(resultSet2.getString(2));

				}

			}

			if (null != connection) {

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

		Mybooks_data.add(mybooks_categories);
		Mybooks_data.add(mybooks_ids);
		Mybooks_data.add(book_names);
		Mybooks_data.add(author_names);

		return Mybooks_data;

	}

	public static boolean checkMyBooks(String category, String id) {

		int i = 0;

		Connection connection = DatabaseConnection.getDB_Connection();

		try {

			// Creating JDBC Statement
			Statement statement = connection.createStatement();

			// Executing SQL & retrieve data into ResultSet
			ResultSet resultSet = statement
					.executeQuery("SELECT ID FROM Mybooks WHERE category = '" + category + "' AND book_id = " + id);

			while (resultSet.next()) {

				i++;
			}

			if (null != connection) {

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

		if (i > 0) {

			return true;
		}

		else {
			return false;
		}

	}

	public static String getBookPath(String category, String id) {

		Connection connection = DatabaseConnection.getDB_Connection();
		String file_name = "";
		String folder_name = "";

		try {

			// Creating JDBC Statement
			Statement statement = connection.createStatement();

			// Executing SQL & retrieve data into ResultSet
			ResultSet resultSet = statement
					.executeQuery("SELECT file_name FROM " + category + " WHERE ID = '" + id + "'");

			while (resultSet.next()) {
				file_name = resultSet.getString(1);
			}

			ResultSet resultSet2 = statement
					.executeQuery("SELECT folder_path FROM Categories WHERE folder_name = '" + category + "'");

			while (resultSet2.next()) {
				folder_name = resultSet2.getString(1);
			}

			if (null != connection) {

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

		String books_path = HomeController.book_folder_directory;
		 
		
		String slash = "\\";

		String full_path = books_path + slash + folder_name + slash + file_name + ".pdf";
		
		return full_path;
	}

	public static void addToMyBooks(String category, String id) {

		Connection connection = DatabaseConnection.getDB_Connection();

		try {

			PreparedStatement insertStatement = connection
					.prepareStatement("INSERT INTO Mybooks (category,book_id) VALUES (?, ?)");
			insertStatement.setString(1, category);
			insertStatement.setString(2, id);
			insertStatement.executeUpdate();

			if (null != connection) {

				// cleanup resources, once after processing
				insertStatement.close();

				// and then finally close connection
				connection.close();
			}
		}

		catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}

	}

	public static void removeFromeMyBooks(String category, String id) {

		Connection connection = DatabaseConnection.getDB_Connection();

		try {

			PreparedStatement insertStatement = connection
					.prepareStatement("DELETE FROM Mybooks WHERE category = '" + category + "' AND book_id = " + id);

			insertStatement.executeUpdate();

			if (null != connection) {

				// cleanup resources, once after processing
				insertStatement.close();

				// and then finally close connection
				connection.close();
			}
		}

		catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}

	}

	public static ArrayList<ArrayList<String>> getSearchedBook(String search_type, String word, int number_of_category,
			ArrayList<ArrayList<String>> categories_data) {

		ArrayList<ArrayList<String>> seach_result = new ArrayList<ArrayList<String>>();
		ArrayList<String> author_names = new ArrayList<String>();
		ArrayList<String> book_names = new ArrayList<String>();
		ArrayList<String> book_ids = new ArrayList<String>();
		ArrayList<String> category_names = new ArrayList<String>();

		Connection connection = DatabaseConnection.getDB_Connection();

		try {

			// Creating JDBC Statement
			Statement statement = connection.createStatement();
			ResultSet resultSet = null;
			for (int i = 0; i < number_of_category; i++) {

				// Executing SQL & retrieve data into ResultSet
				resultSet = statement.executeQuery("SELECT ID, book_name, author FROM " + categories_data.get(2).get(i) + " WHERE "
						+ search_type + " LIKE \'%" + word + "%\' ORDER BY book_name ASC");

				while (resultSet.next()) {

					book_ids.add(String.valueOf(resultSet.getString(1)));
					book_names.add(resultSet.getString(2));
					author_names.add(resultSet.getString(3));
					category_names.add(categories_data.get(2).get(i));

				}

			}

			if (null != connection) {

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

		seach_result.add(book_ids);
		seach_result.add(book_names);
		seach_result.add(author_names);
		seach_result.add(category_names);

		return seach_result;

	}

	public static void updateSetting(String rd, String folder, String db) {

		Connection connection = DatabaseConnection.getDB_Connection();

		try {

			PreparedStatement updateStatement = connection.prepareStatement("UPDATE Settings SET show_type = \'" + rd
					+ "\', folder_path = \'" + folder + "\' , db_path = \'" + db + "\'");
			updateStatement.executeUpdate();
			
			 

			if (null != connection) {

				// cleanup resources, once after processing
				updateStatement.close();

				// and then finally close connection
				connection.close();
			}
		}

		catch (SQLException sqlex) {
			sqlex.printStackTrace();
			HomeController.isdbError = true;
			
			
		}

	}

	public static ArrayList<String> settingsCurrentData() {

		ArrayList<String> settings_data = new ArrayList<String>();

		Connection connection = DatabaseConnection.getDB_Connection();

		try {

			// Creating JDBC Statement
			Statement statement = connection.createStatement();

			// Executing SQL & retrieve data into ResultSet
			ResultSet resultSet = statement.executeQuery("SELECT show_type,folder_path,db_path FROM Settings");
			
			while (resultSet.next()) {

				settings_data.add(resultSet.getString(1));
				settings_data.add(resultSet.getString(2));
				settings_data.add(resultSet.getString(3));

			}

			if (null != connection) {

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

		return settings_data;
	}
	
	public static String getDBpath() {
		
		String db = "";
		
		Connection connection = DatabaseConnection.getDB_Connection();

		try {

			// Creating JDBC Statement
			Statement statement = connection.createStatement();

			// Executing SQL & retrieve data into ResultSet
			ResultSet resultSet = statement.executeQuery("SELECT db_path FROM Settings");

			while (resultSet.next()) {

				db = resultSet.getString(1);
			 

			}

			if (null != connection) {

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
		 
		return db;
		
		
	}
	
	
	public static String getCategoryTitle(String table_name) {

		String category_title = "";
		Connection connection = DatabaseConnection.getDB_Connection();

		try {

			// Creating JDBC Statement
			Statement statement = connection.createStatement();

			// Executing SQL & retrieve data into ResultSet
			ResultSet resultSet = statement
					.executeQuery("SELECT category_name FROM Categories WHERE folder_name = \'" + table_name + "\'");

			
			while (resultSet.next()) {

				category_title = resultSet.getString(1);
				 

			}

			if (null != connection) {

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

	 

		return category_title;

	}


}
