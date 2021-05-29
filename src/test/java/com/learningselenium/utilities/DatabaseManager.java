package com.learningselenium.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseManager {

	static Properties databaseConfig;
	private static Connection con = null;

	private static void loadProperties() throws IOException {

		databaseConfig = new Properties();
		FileInputStream fis = new FileInputStream("src\\test\\resources\\Properties\\DatabaseConfig.properties");
		databaseConfig.load(fis);

	}

	public static void connectMySqlDatabase() {

		try {

			loadProperties();

			String mySqlDriver = databaseConfig.getProperty("mysqldriver");
			String mysqldatabaseURL = databaseConfig.getProperty("mysqldatabaseURL");
			String user = databaseConfig.getProperty("user");
			String password = databaseConfig.getProperty("password");

			// Step: 1 - Registering the Driver - since Java 6 (JDBC 4.0) it is usually not
			// necessary to manually load the driver class using Class.forName
			Class.forName(mySqlDriver);

			// Step: 2 - Creating a Connection
			con = DriverManager.getConnection(mysqldatabaseURL, user, password);

			if (!con.isClosed()) {
				System.out.println("Successfully connected to MySQL server");
			}

		} catch (Throwable T) {
			System.out.println("Unable to establish connection to MySQL database");
		}

	}

	public static String getQueryResult(String query) throws SQLException {

		String value = null;

		try {

			// Step: 3 - Creating a Statement
			Statement statement = con.createStatement();

			// Step: 4 - Executing the query
			ResultSet result = statement.executeQuery(query);

			while (result.next()) {
				value = result.getString(1);
			}

		} catch (Throwable t) {
			t.printStackTrace();

		} finally {

			// Step: 5 - Closing a connection
			con.close();
		}

		return value;
	}

	public static void close() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
