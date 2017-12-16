package ca.udes.tp.dao;

import org.junit.After;
import org.junit.Before;

import utility.TestUtility;

public class DatabaseManagerTest {

	@Before
	public void setUp(){
		TestUtility.CleanUpDB();
		TestUtility.addInfosForTestMatchDAO();
	}

	@After
	public void tearDown(){
		TestUtility.CleanUpDB();
	}
	
	
}
