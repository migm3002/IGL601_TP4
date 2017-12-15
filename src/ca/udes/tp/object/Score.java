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
	

}