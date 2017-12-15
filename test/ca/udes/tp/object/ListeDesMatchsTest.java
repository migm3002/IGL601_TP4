package ca.udes.tp.object;

import java.util.ArrayList;

import org.junit.Test;

import org.junit.Assert;

public class ListeDesMatchsTest {

	private Joueur j1 = new Joueur(1, "NADAL", "Rafael", 1, true);
	private Joueur j2 = new Joueur(2, "FEDERER", "Roger", 2, false);
	
	private Joueur j3 = new Joueur(3, "WILLIAMS", "Serena", 1, false);
	private Joueur j4 = new Joueur(4, "SHARAPOVA", "Maria", 2, false);
	
	// score board : first set over but match not over
	private String[][] tabSet1 = {{"6","4","1"},{"4","6","0"}};
	private String[] tabJeu1 = {"0","30"};
	private Score s1 = new Score(tabSet1, tabJeu1);
	
	// score board : first set not over
	private String[][] tabSet2 = {{"3","",""},{"5","",""}};
	private String[] tabJeu2 = {"40","30"};
	private Score s2 = new Score(tabSet2, tabJeu2);
	
	// score board : match over
	private String[][] tabSet3 = {{"6","2","6"},{"4","6","3"}};
	private String[] tabJeu3 = {"",""};
	private Score s3 = new Score(tabSet3, tabJeu3);
	
	private Match m1 = new Match(1, j1, j2, s1, "1:38:12", 3, 3);
	private Match m2 = new Match(2, j1, j2, s2, "0:31:45", 2, 3);
	private Match m3 = new Match(3, j3, j4, s3, "2:06:51", 3, 3);

	
	@Test
	public void testListeDesMatchs() {
		ArrayList<Match> arrayListMatchs = new ArrayList<Match>();
		ListeDesMatchs listeMatchs1 = new ListeDesMatchs(arrayListMatchs);
		listeMatchs1.add(m1);
		listeMatchs1.add(m2);
		listeMatchs1.add(m3);
		Assert.assertEquals(m1,listeMatchs1.get(0));
		Assert.assertEquals(m2,listeMatchs1.get(1));
		Assert.assertEquals(m3,listeMatchs1.get(2));
		Assert.assertEquals(m1,listeMatchs1.getMatchWithId(1));
		
		ArrayList<Match> arrayListEndedMatchs = listeMatchs1.getEndedMatchs();
		Assert.assertEquals(m3, arrayListEndedMatchs.get(0));
		
		ListeDesMatchs listeMatchs2 = new ListeDesMatchs();
		Assert.assertTrue(listeMatchs2.getListeMatchs().size()==10);
		
		ListeDesMatchs listeMatchs3 = new ListeDesMatchs(null);
		listeMatchs3.add(m1);
		Assert.assertNull(listeMatchs3.get(0));
		Assert.assertNull(listeMatchs3.getMatchWithId(1));
	}
}
