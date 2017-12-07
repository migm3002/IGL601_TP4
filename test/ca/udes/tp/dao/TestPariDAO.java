package ca.udes.tp.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.udes.tp.dao.PariDAO;
import utility.TestUtility;


public class TestPariDAO {

	@Before
	public void setUp(){
		TestUtility.CleanUpDB();
		TestUtility.addInfosForTestPariDAO();
	}

	@After
	public void tearDown(){
		TestUtility.CleanUpDB();
	}

	@Test
	public void testAddBetInBDD(){
		assertTrue("Bet doesn't be insert in BDD", PariDAO.placeBet(1, 1, 1, 6.90));
	}
	
	@Test
	public void testGetTotalBet(){
		assertTrue("GetTotalBet is different from all bet.", PariDAO.getTotalBets(2)==(22.45+51.86));
	}
	
	@Test
	public void testGetAllWinningBets() {
		assertTrue("GetAllWinningBets is different from the winning bet", (PariDAO.getAllWinningBets(2)==(51.86)));
	}
	
	@Test
	public void testGetBetForGivenGambler() {
		assertTrue("GetBetForGivenGambler returns the bad bet", (PariDAO.getBetForGivenGambler(2, 1)==22.45));
	}
	
	@Test
	public void testGetListOfWinners() {
		ArrayList<Integer> listWinners = new ArrayList <Integer>();
		listWinners.add(1);
		
		assertTrue("GetBetForGivenGambler returns the bad bet", (PariDAO.getListOfWinners(2)==listWinners));
	}
	
	@Test
	public void testHasBetOnWinner() {
		assertTrue("idParieur 1 has bet on winner", PariDAO.hasBetOnWinner(2, 1));
	}
	
	@Test
	public void testNotHasBetOnWinner() {		
		assertFalse("idParieur 2 doesn't have bet on winner", PariDAO.hasBetOnWinner(2, 2));
	}
	
	
}
