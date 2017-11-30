package ca.udes.tp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public final class ConnectionDB {
	
	private static final String DRIVER = "org.postgresql.Driver";
	private static final String PROTOCOL = "jdbc:postgresql";
	private static final String SERVER_ADDRESS = "localhost";
	private static final String PORT = "5432";
	private static final String LOGIN = "client";
	private static final String PASSWORD = "network";
	private static final String DB_NAME = "DB_Tennis";
	//URL : "jdbc:postgresql://localhost:5432/DB_Tennis";
	private static final String URL = PROTOCOL+"://"+SERVER_ADDRESS+":"+PORT+"/"+DB_NAME;
	
	private ConnectionDB() {}
	
	/**
	 * Create a connection to the database
	 * @return the connection
	 */
	public static Connection connection() {
		Connection connection = null;
		
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try {
			connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return connection;
	}
	
	
	/**
	 * Closes every prepared statements and result sets created when interrogating the database.
	 * Also closes the connection to the database itself.
	 * @param rsArray the list of result sets
	 * @param psArray the list of prepared statements
	 * @param connection the connection to the database
	 */
	public static void closeConnections(ArrayList<ResultSet> rsArray, ArrayList<PreparedStatement> psArray, Connection connection) {
		
		// Closing result sets
		for (ResultSet rs : rsArray) {
			if(rs != null) {
				try {
					rs.close();
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		// Closing prepared statements
		for (PreparedStatement ps : psArray) {
			if(ps != null) {
				try {
					ps.close();
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		// Closing the connection to the database
		if(!ConnectionDB.deconnection(connection)) {
			System.out.println("ERROR : Could not close connection properly");
		}
		
	}
	
	/**
	 * Closes the connection to the database
	 * @param connection the connection to the database
	 * @return true if disconnected, else false
	 */
	public static boolean deconnection(Connection connection) {
		boolean success = false;
		if(connection != null) {
			try {
				connection.close();
				success = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return success;
	}

}