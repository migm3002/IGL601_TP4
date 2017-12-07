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
		TestUtility.addInfosForTestMatchDAO();
		Joueur j11 = new Joueur(11, "WILLIAMS", "Serena", 1, false);
		Joueur j15 = new Joueur(15, "SHARAPOVA", "Maria", 2, false);
		String[][] tabSet3 = {{"6","2","6"},{"4","6","3"}};
		String[] tabJeu3 = {"",""};
		Score s3 = new Score(tabSet3, tabJeu3);

		matchTest = new Match(1, j11, j15, s3, "2:06:51", 3, 3);
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
