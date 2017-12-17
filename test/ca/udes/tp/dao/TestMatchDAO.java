package ca.udes.tp.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import utility.TestUtility;

public class TestMatchDAO {

	
	@Before
	public void setUp(){
		TestUtility.CleanUpDB();
		TestUtility.addInfosForTestMatchDAO();
	}

	@After
	public void tearDown(){
		TestUtility.CleanUpDB();
	}
	
	
	@Test
	public void testUpdateMatchWinner() {
		assertTrue(MatchDAO.updateMatchWinner(1, 11));
	}
	
	@Test
	public void testUpdateMatchWinnerMatchNotInDB() {
		assertFalse(MatchDAO.updateMatchWinner(2, 1));
	}
	
}
