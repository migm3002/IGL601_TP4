package integrationTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.udes.tp.dao.MatchDAO;
import ca.udes.tp.object.Joueur;
import ca.udes.tp.object.Match;
import ca.udes.tp.object.Score;
import utility.TestUtility;

public class TestIntegrationMatchBDD {

	private Match matchTest;

	@Before
	public void setUp(){
		TestUtility.CleanUpDB();
		Joueur j3 = new Joueur(3, "WILLIAMS", "Serena", 1, false);
		Joueur j4 = new Joueur(4, "SHARAPOVA", "Maria", 2, false);
		String[][] tabSet3 = {{"6","2","6"},{"4","6","3"}};
		String[] tabJeu3 = {"",""};
		Score s3 = new Score(tabSet3, tabJeu3);

		matchTest = new Match(3, j3, j4, s3, "2:06:51", 3, 3);
	}

	@After
	public void tearDown(){
		TestUtility.CleanUpDB();
	}

	@Test
	public void testUpdateMatch(){
		assertTrue("Match doesn't update in BDD", MatchDAO.updateMatchWinner(matchTest.getId(),matchTest.getWinner().getId()));
	}
}
