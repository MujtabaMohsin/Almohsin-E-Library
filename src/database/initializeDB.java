package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class initializeDB {

	public static String readDBFile() {

		String path = "";

		File myObj = new File(System.getProperty("user.home") + "\\Almohsin DB.txt");

		if (myObj.exists()) {
			try {
				Scanner myReader = new Scanner(myObj);
				while (myReader.hasNextLine()) {
					path = myReader.nextLine();

				}
				myReader.close();
			} catch (FileNotFoundException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}

			return path;

		}

		else {
			return "no DB";
		}

	}

	public static void createDBFile() {

		try {
			File myObj = new File(System.getProperty("user.home") + "\\Almohsin DB.txt");
			if (myObj.createNewFile()) {

			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

	}

	public static void writeDBFile(String path) {

		// write data

		try {
			FileWriter myWriter = new FileWriter(System.getProperty("user.home") + "\\Almohsin DB.txt");
			myWriter.write(path);
			myWriter.close();

		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	
	public static boolean checkDBexist(String path) {
		
		File myObj = new File(path);

		if (myObj.exists()) {
			return true;
		}
		
		return false;
	}

}
