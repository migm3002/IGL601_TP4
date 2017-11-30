package ca.udes.tp.object;

public class Score {

	private String[][] tabSet;
	private String[] tabJeu; 

	public static final String[][] TABSET_PAR_DEFAUT = {{"0","",""},{"0","",""}};
	public static final String[] TABJEU_PAR_DEFAUT = {"0","0"}; 
	
	public Score(){
		this.tabSet = TABSET_PAR_DEFAUT;
		this.tabJeu = TABJEU_PAR_DEFAUT;
	}

	public Score(String[][] tabS, String[] tabJ){
		this.tabSet = tabS;
		this.tabJeu = tabJ;
	}

	public void setTabSet(String[][] tab){
		this.tabSet = tab;
	}

	public String[][] getTabSet(){
		return this.tabSet;
	}

	public void setTabJeu(String[] tab){
		this.tabJeu = tab;
	}

	public String[] getTabJeu(){
		return this.tabJeu;
	}
	
	//For a debug purpose only
	public String toString() {
		String[][] tabSet = getTabSet();
		String[] tabJeu = getTabJeu();
		
		return tabSet[0][0]+"|"+tabSet[0][1]+"|"+tabSet[0][2]+"  "+tabJeu[0]+"\n "
			  +tabSet[1][0]+"|"+tabSet[1][1]+"|"+tabSet[1][2]+"  "+tabJeu[1];
	}

}