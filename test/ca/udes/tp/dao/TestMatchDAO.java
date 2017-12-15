package ca.udes.tp.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;
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
		Assert.assertTrue(MatchDAO.updateMatchWinner(1, 11));
	}
	
	@Test
	public void testUpdateMatchWinnerMatchNotInDB() {
		Assert.assertFalse(MatchDAO.updateMatchWinner(2, 1));
	}
	
}
