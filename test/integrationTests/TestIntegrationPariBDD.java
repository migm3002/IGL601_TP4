package integrationTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.udes.tp.dao.PariDAO;
import ca.udes.tp.object.Joueur;
import ca.udes.tp.object.Match;
import ca.udes.tp.object.Pari;
import ca.udes.tp.object.Pari.Status;
import ca.udes.tp.object.Score;

public class TestIntegrationPariBDD {
	private Pari pariTest;
	private Match matchTest;
	private Match matchTest2;
	private Pari pariMatch2_1;
	private Pari pariMatch2_2;

	@Before
	public void setUp(){
		//Ajouter script de BDD pour la simuler
		Joueur j1 = new Joueur(1, "NADAL", "Rafael", 1, true);
		Joueur j2 = new Joueur(2, "FEDERER", "Roger", 2, false);
		Joueur j3 = new Joueur(3, "WILLIAMS", "Serena", 1, false);
		Joueur j4 = new Joueur(4, "SHARAPOVA", "Maria", 2, false);
		
		String[][] tabSet1 = {{"3","",""},{"5","",""}};
		String[] tabJeu1 = {"0","30"};
		Score s1 = new Score(tabSet1, tabJeu1);
		
		String[][] tabSet3 = {{"6","2","6"},{"4","6","3"}};
		String[] tabJeu3 = {"",""};
		Score s3 = new Score(tabSet3, tabJeu3);
		
		matchTest = new Match(1, j1, j2, s1, "0:32:00", 3, 3);
		matchTest2 = new Match(3, j3, j4, s3, "2:06:51", 3, 3);
		pariMatch2_1 = new Pari(3, 3, 3, 51.86 , Status.commited);
		pariMatch2_2 = new Pari(4, 3, 4, 22.45 , Status.commited);
	}

	@After
	public void tearDown(){

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