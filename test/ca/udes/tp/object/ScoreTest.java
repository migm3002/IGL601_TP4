package ca.udes.tp.object;

import org.junit.Assert;
import org.junit.Test;

public class ScoreTest {

	private String[][] tabSet = {{"6","4",""},{"4","2",""}};
	private String[] tabJeu = {"15","30"};
	
	@Test
	public void scoreConstructorTest() {
		Score scoreTest1 = new Score();
		Assert.assertEquals(Score.TABJEU_PAR_DEFAUT, scoreTest1.getTabJeu());
		Assert.assertEquals(Score.TABSET_PAR_DEFAUT, scoreTest1.getTabSet());
		
		Score scoreTest2 = new Score(tabSet, tabJeu);
		Assert.assertEquals(tabJeu, scoreTest2.getTabJeu());
		Assert.assertEquals(tabSet, scoreTest2.getTabSet());
		
	}
	
	@Test
	public void scoreGettersSettersTest() {
		Score scoreTest = new Score();
		scoreTest.setTabJeu(tabJeu);
		scoreTest.setTabSet(tabSet);

		Assert.assertEquals(tabJeu, scoreTest.getTabJeu());
		Assert.assertEquals(tabSet, scoreTest.getTabSet());
	}
}
