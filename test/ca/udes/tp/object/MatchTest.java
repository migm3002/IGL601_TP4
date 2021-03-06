package ca.udes.tp.object;

import org.junit.Assert;
import org.junit.Test;

public class MatchTest {
	
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
	
	// score board : match tied (1 set)
	private String[][] tabSet4 = {{"6","6","4"},{"3","6","6"}};
	private String[] tabJeu4 = {"0","0"};
	private Score s4 = new Score(tabSet4, tabJeu4);
	
	// score board : match tied (3 sets)
	private String[][] tabSet5 = {{"6","6","6"},{"6","6","6"}};
	private String[] tabJeu5 = {"0","0"};
	private Score s5 = new Score(tabSet5, tabJeu5);
	
	//score board : match not tied but 2 sets tied
	private String[][] tabSet6 = {{"3","6","6"},{"6","6","6"}};
	private String[] tabJeu6 = {"0","0"};
	private Score s6 = new Score(tabSet6, tabJeu6);
	
	private String[][] tabSet7 = {{"4","6","3"},{"6","2","6"}};
	private String[] tabJeu7 = {"",""};
	private Score s7 = new Score(tabSet7, tabJeu7);
	
	private Match m1 = new Match(1, j1, j2, s1, "1:38:12", 3, 3);
	private Match m2 = new Match(2, j1, j2, s2, "0:31:45", 2, 3);
	private Match m3 = new Match(3, j3, j4, s3, "2:06:51", 3, 3);
	private Match m4 = new Match(4, j3, j4, s4, "2:28:01", 3, 3);
	private Match m5 = new Match(5, j3, j4, s5, "2:51:38", 3, 3);
	private Match m6 = new Match(6, j3, j4, s6, "2:37:26", 3, 3);
	private Match m7 = new Match(3, j3, j4, s7, "2:06:51", 3, 3);
	
	
	@Test
	public void testGetId() {
		Assert.assertEquals(1, m1.getId());
	}
	
	@Test
	public void testGetJoueur1() {
		Assert.assertEquals(1, m1.getJoueur1().getId());
		Assert.assertEquals("Rafael", m1.getJoueur1().getPrenom());
		Assert.assertEquals("NADAL", m1.getJoueur1().getNom());
		Assert.assertEquals(1, m1.getJoueur1().getClassement());
		Assert.assertEquals(true, m1.getJoueur1().getService());
	}
	
	@Test
	public void testGetJoueur2() {
		Assert.assertEquals(2, m1.getJoueur2().getId());
		Assert.assertEquals("Roger", m1.getJoueur2().getPrenom());
		Assert.assertEquals("FEDERER", m1.getJoueur2().getNom());
		Assert.assertEquals(2, m1.getJoueur2().getClassement());
		Assert.assertEquals(false, m1.getJoueur2().getService());
	}
	
	@Test
	public void testGetScore() {
		Assert.assertEquals("6", m1.getScore().getTabSet()[0][0]);
		Assert.assertEquals("0", m1.getScore().getTabJeu()[0]);
	}
	
	@Test
	public void testGetChronometre() {
		Assert.assertEquals("1:38:12",m1.getChronometre());
	}
	
	@Test
	public void testGetConstestationsRestantesJoueur1() {
		Assert.assertEquals(3, m1.getContestationsRestantesJoueur1());
	}
	
	@Test
	public void testGetConstestationsRestantesJoueur2() {
		Assert.assertEquals(3, m1.getContestationsRestantesJoueur2());
	}
	
	@Test
	public void testIsSet1OverTrue() {
		Assert.assertTrue("First set is not over", m1.isSet1Finished());
		Assert.assertTrue("First set is not over", m3.isSet1Finished());
	}
	
	@Test
	public void testIsSet1OverFalse() {
		Assert.assertFalse("First set is over", m2.isSet1Finished());
	}
	
	@Test
	public void testIsEndedTrue() {
		Assert.assertTrue("Match is not over", m3.isEnded());
	}
	
	@Test
	public void testIsEndedFalse() {
		Assert.assertFalse("Match is over", m1.isEnded());
		Assert.assertFalse("Match is over", m2.isEnded());
		m2.changeService();
		Assert.assertFalse(m2.isEnded());
	}
	
	@Test
	public void testIsTiedTrue() {
		Assert.assertTrue("Match is not tied", m4.isTied());
		Assert.assertTrue("Match is not tied", m5.isTied());
	}
	
	@Test
	public void testIsTiedFalse() {
		Assert.assertFalse("Match is tied", m3.isTied());
		Assert.assertFalse("Match is tied", m1.isTied());
		Assert.assertFalse("Match is tied", m6.isTied());
	}
	
	@Test
	public void testGetWinnerMatchOver() {
		Assert.assertSame("Winner is not j3", j3, m3.getWinner());
		Assert.assertSame(j4, m7.getWinner());
	}
	
	@Test
	public void testGetWinnerMatchNotOver() {
		Assert.assertNull("Match has a winner", m1.getWinner());
	}
	
	@Test
	public void testGetWinnerMatchTied() {
		Assert.assertNull("Match has a winner", m4.getWinner());
		Assert.assertNull("Match has a winner", m5.getWinner());
	}
	
	@Test
	public void testChangeService() {
		Joueur j1test = j1;
		Joueur j2test = j2;
		Match mtest = new Match(0, j1test, j2test, s1, "1:00:00", 3, 3);
		
		Assert.assertTrue("j1test is not serving", mtest.getJoueur1().getService());
		Assert.assertFalse("j2test is serving", mtest.getJoueur2().getService());
		mtest.changeService();
		Assert.assertFalse("j1test is serving", mtest.getJoueur1().getService());
		Assert.assertTrue("j2test is not serving", mtest.getJoueur2().getService());
		mtest.changeService();
		Assert.assertTrue("j1test is not serving", mtest.getJoueur1().getService());
		Assert.assertFalse("j2test is serving", mtest.getJoueur2().getService());
	}
	
	@Test
	public void testChangeServiceMatchOver() {
		m3.changeService();
		Assert.assertFalse(m3.getJoueur1().getService());
		Assert.assertFalse(m3.getJoueur2().getService());
	}
	
	@Test
	public void testAddPoint() {

		Assert.assertEquals("0", m1.getScore().getTabJeu()[0]);
		Assert.assertEquals("30", m1.getScore().getTabJeu()[1]);
		
		m1.addPoint(1, false);
		
		Assert.assertEquals("15", m1.getScore().getTabJeu()[0]);
		Assert.assertEquals("30", m1.getScore().getTabJeu()[1]);
	}
	
	@Test
	public void testDecreaseContestationPointForWithGoodValues() {
		m2.decreaseContestationPointFor(1);
		m2.decreaseContestationPointFor(2);
		Assert.assertEquals(1, m2.getContestationsRestantesJoueur1());
		Assert.assertEquals(2, m2.getContestationsRestantesJoueur2());
	}
	
	@Test
	public void testDecreaseContestationPointForWithWrongValues() {
		m2.decreaseContestationPointFor(0);
		Assert.assertEquals(2, m2.getContestationsRestantesJoueur1());
	}
	
	
}
