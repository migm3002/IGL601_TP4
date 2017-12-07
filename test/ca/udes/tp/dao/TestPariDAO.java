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
		PariDAO.placeBet(4, 3, 4, 22.45);
		PariDAO.placeBet(3, 3, 3, 51.86);
		
		assertTrue("GetTotalBet is different from all bet.", PariDAO.getTotalBets(3)==(22.45+51.86));
	}
	
	@Test
	public void testGetAllWinningBets() {
		PariDAO.placeBet(4, 3, 4, 22.45);
		PariDAO.placeBet(3, 3, 3, 51.86);
		
		assertTrue("GetAllWinningBets is different from the winning bet", (PariDAO.getAllWinningBets(3)==(51.86)));
	}
	
	@Test
	public void testGetBetForGivenGambler() {
		PariDAO.placeBet(4, 3, 4, 22.45);
		PariDAO.placeBet(3, 3, 3, 51.86);
		
		assertTrue("GetBetForGivenGambler returns the bad bet", (PariDAO.getBetForGivenGambler(3, 4)==22.45));
	}
	
	@Test
	public void testGetListOfWinners() {
		PariDAO.placeBet(4, 3, 4, 22.45);
		PariDAO.placeBet(3, 3, 3, 51.86);
		ArrayList<Integer> listWinners = new ArrayList <Integer>();
		listWinners.add(3);
		
		assertTrue("GetBetForGivenGambler returns the bad bet", (PariDAO.getListOfWinners(3)==listWinners));
	}
	
	@Test
	public void testHasBetOnWinner() {
		PariDAO.placeBet(4, 3, 4, 22.45);
		PariDAO.placeBet(3, 3, 3, 51.86);
		
		assertTrue("Pari 3 has bet on winner", PariDAO.hasBetOnWinner(3, 3));
	}
	
	@Test
	public void testNotHasBetOnWinner() {
		PariDAO.placeBet(4, 3, 4, 22.45);
		PariDAO.placeBet(3, 3, 3, 51.86);
		
		assertFalse("Pari 4 doesn't have bet on winner", PariDAO.hasBetOnWinner(3, 4));
	}
	
	
}
