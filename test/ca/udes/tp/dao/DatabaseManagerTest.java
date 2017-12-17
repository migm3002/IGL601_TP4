package ca.udes.tp.dao;

import java.util.ArrayList;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.udes.tp.communication.Message;
import ca.udes.tp.communication.Message.Method;
import ca.udes.tp.object.Joueur;
import ca.udes.tp.object.ListeDesMatchs;
import ca.udes.tp.object.Match;
import ca.udes.tp.object.Pari;
import ca.udes.tp.object.Score;
import ca.udes.tp.server.Server;
import ca.udes.tp.object.Pari.Status;
import utility.TestUtility;

public class DatabaseManagerTest {

	private Joueur j1 = new Joueur(1, "NADAL", "Rafael", 1, true);
	private Joueur j2 = new Joueur(3, "FEDERER", "Roger", 2, false);
	
	
	// score board : first set over but match not over
	private String[][] tabSet1 = {{"4","",""},{"2","",""}};
	private String[] tabJeu1 = {"0","30"};
	private Score s1 = new Score(tabSet1, tabJeu1);

	private Match m1 = new Match(1, j1, j2, s1, "0:38:12", 3, 3);
	
	private ArrayList<Match> arrayListMatch;
	private ListeDesMatchs listeDesMatchs;
	private Server server;
	private DatabaseManager dbManagerTest;

	
	@Before
	public void setUp(){
		TestUtility.CleanUpDB();
		TestUtility.addInfosForTestPariDAO();
		
		m1 = new Match(1, j1, j2, s1, "1:38:12", 3, 3);
		arrayListMatch = new ArrayList<Match>();
		arrayListMatch.add(m1);
		listeDesMatchs = new ListeDesMatchs(arrayListMatch);
		server = EasyMock.createMock(Server.class);
		EasyMock.expect(server.getCurrentListeDesMatchs()).andStubReturn(listeDesMatchs);
		EasyMock.replay(server);

	}

	@After
	public void tearDown(){
		TestUtility.CleanUpDB();
	}
	
	@Test
	public void testInsertBet() {
		
		Pari pariTest = new Pari(1, 1, 1, 30, Status.asked);
		Message messageTest = new Message(true, 1, Method.bet,pariTest, "127.0.0.1", 18000);
		dbManagerTest = new DatabaseManager(server);
		Assert.assertTrue("Bet not inserted in the database", dbManagerTest.insertBet(messageTest));
		
	}
}
