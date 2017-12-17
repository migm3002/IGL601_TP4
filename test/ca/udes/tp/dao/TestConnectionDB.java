package ca.udes.tp.dao;


import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import utility.TestUtility;

public class TestConnectionDB {
	Connection connexion = ConnectionDB.connection();
	
	@Before
	public void setUp(){
		TestUtility.CleanUpDB();		
	}

	@After
	public void tearDown(){
		TestUtility.CleanUpDB();
	}
	
	
	@Test
	public void testConnection() {
		assertTrue("This isn't a connection", (connexion.getClass().equals(ConnectionDB.connection().getClass())));
	}
	
	@Test
	public void testDeconnection() {
		assertTrue("Connection not deconnected", ConnectionDB.deconnection(connexion));
	}
	
	@Test
	public void testCloseConnection() throws SQLException {
		ConnectionDB.deconnection(connexion);
		ArrayList<ResultSet> rsArray = new ArrayList<ResultSet>();
		ArrayList<PreparedStatement> psArray = new ArrayList<PreparedStatement>();
		ConnectionDB.closeConnections(rsArray, psArray, connexion);
		for (ResultSet rs : rsArray) {
			assertTrue("One resultSet not closed", rs.isClosed());
		}
		for (PreparedStatement ps : psArray) {
			assertTrue("One preparedStatement not closed", ps.isClosed());
		}
	}
}
