package ca.udes.tp.dao;

import static org.junit.Assert.*;

import java.text.NumberFormat;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.udes.tp.dao.PariDAO;
import utility.TestUtility;


public class TestPariDAO {
	private NumberFormat nf = NumberFormat.getInstance();
	

	@Before
	public void setUp(){
		TestUtility.CleanUpDB();
		TestUtility.addInfosForTestPariDAO();
		nf.setMaximumIntegerDigits(2);
		nf.setGroupingUsed(false);
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
		double sum = (22.45+51.86);
		assertTrue("GetTotalBet is different from all bet : "+nf.format(PariDAO.getTotalBets(2)), (nf.format(PariDAO.getTotalBets(2)).equals(nf.format(sum))));
	}
	
	@Test
	public void testGetAllWinningBets() {
		assertTrue("GetAllWinningBets is different from the winning bet", nf.format(PariDAO.getAllWinningBets(2)).equals(nf.format(22.45)));
	}
	
	@Test
	public void testGetBetForGivenGambler() {
		assertTrue("GetBetForGivenGambler returns the bad bet", nf.format(PariDAO.getBetForGivenGambler(2, 1)).equals(nf.format(22.45)));
	}
	
	@Test
	public void testGetListOfWinners() {
		ArrayList<Integer> listWinners = new ArrayList <Integer>();
		listWinners.add(1);
		
		assertTrue("GetBetForGivenGambler returns the bad bet : "+PariDAO.getListOfWinners(2).toString(), (PariDAO.getListOfWinners(2).equals(listWinners)));
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
