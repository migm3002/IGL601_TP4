package ca.udes.tp.object;


import java.util.ArrayList;


public class ListeDesMatchs {

	private ArrayList<Match> listeMatchs;
	

	public ListeDesMatchs(ArrayList<Match> listeMatchs) {
		this.listeMatchs=listeMatchs;
	}
	
	public ListeDesMatchs() {
		initCurrentListeDesMatchs();
	}
	
	/**
	 * Get match n°index in the ArrayList of ListeDesMatchs.
	 * @param int :index
	 * @return Match : match
	 */
	public Match get(int index) {
		if(this.listeMatchs!=null) {
			return this.listeMatchs.get(index);
		}else {
			System.out.println("Error : trying to get a match from an null list");
			return null;
		}
	}
	
	/**
	 * Add a match in the ArrayList of ListeDesMatchs.
	 * @param match
	 */
	public void add(Match match) {
		if(this.listeMatchs!=null) {
			listeMatchs.add(match);
		}else {
			System.out.println("Error : listeMatchs container is null ! Cannot add a new match");
		}
	}
	
	/**
	 * Get the match with an idMatch equal to the argument.
	 * @param int : idMatch
	 * @return Match : matchWithId
	 */
	public Match getMatchWithId(int idMatch) {
		Match matchWithId = null;
		ArrayList<Match> listeMatchs = getListeMatchs();
		if(listeMatchs!=null) {
			for(Match match : listeMatchs) {
				if (match.getId()==idMatch) {
					matchWithId=match;
				}
			}
		}
		return matchWithId;
	}
	
	/**
	 * Return the ArrayList of ended matches in this ListeDesMatchs.
	 * @return ArrayList<Match>
	 */
	public ArrayList<Match> getEndedMatchs(){
		ArrayList<Match> endedMatchs = new ArrayList<Match>();
		for(Match match : getListeMatchs()) {
			if (match.isEnded()) {
				endedMatchs.add(match);
			}
		}
		
		return endedMatchs;
	}
	
	public ArrayList<Match> getListeMatchs(){
		return this.listeMatchs;
	}
	
	public void setListeMatchs(ArrayList<Match> listeMatchs) {
		this.listeMatchs=listeMatchs;
	}
	
	/**
	 * Initialize the value of the the ListeDesMatchs with 10 matches,
	 * some of them already started and some of them already ended.
	 */
	public void initCurrentListeDesMatchs() {
		ArrayList<Match> arrayListeMatchs = new ArrayList<Match>();
		
		Joueur joueur1 = new Joueur(1, "NADAL", "Rafael", 1, true);
		Joueur joueur2 = new Joueur(2, "NISHIKORI", "Kei", 14, false);
		Joueur joueur3 = new Joueur(3, "FEDERER", "Roger", 2, true);
		Joueur joueur4 = new Joueur(4, "TSONGA", "Jo Wilfried", 18, false);
		Joueur joueur5 = new Joueur(5, "MURRAY", "Andy", 3, true);
		Joueur joueur6 = new Joueur(6, "MONFILS", "Gael", 42, false);
		Joueur joueur7 = new Joueur(7, "DJOKOVIC", "Novak", 6, false);
		Joueur joueur8 = new Joueur(8, "RAONIC", "Milos", 12, true);
		Joueur joueur9 = new Joueur(9, "WAVRINKA", "Stan", 8, false);
		Joueur joueur10 = new Joueur(10, "THIEM", "Dominic", 7, true);
		Joueur joueur11 = new Joueur(11, "WILLIAMS", "Serena", 17, false);
		Joueur joueur12 = new Joueur(12, "KVITOVA", "Petra", 14, true);
		Joueur joueur13 = new Joueur(13, "MUGURUZA", "Garbine", 2, false);
		Joueur joueur14 = new Joueur(14, "KUZNETSOVA", "Svetlana", 8, false);
		Joueur joueur15 = new Joueur(15, "SHARAPOVA", "Maria", 86, true);
		Joueur joueur16 = new Joueur(16, "PLISKOVA", "Karolina", 3, false);
		Joueur joueur17 = new Joueur(17, "HALEP", "Simona", 1 ,false);
		Joueur joueur18 = new Joueur(18, "GARCIA", "Caroline", 9, true);
		Joueur joueur19 = new Joueur(19, "BOUCHARD", "Eugenie", 78, true);
		Joueur joueur20 = new Joueur(20, "MLADENOVIC", "Kristina", 13, false);
		
		int nbContestationsJoueur1=3;
		int nbContestationsJoueur2=3;
		int nbContestationsJoueur3=3;
		int nbContestationsJoueur4=2;
		int nbContestationsJoueur5=3;
		int nbContestationsJoueur6=3;
		int nbContestationsJoueur7=2;
		int nbContestationsJoueur8=1;
		int nbContestationsJoueur9=1;
		int nbContestationsJoueur10=1;
		int nbContestationsJoueur11=3;
		int nbContestationsJoueur12=3;
		int nbContestationsJoueur13=3;
		int nbContestationsJoueur14=2;
		int nbContestationsJoueur15=2;
		int nbContestationsJoueur16=3;
		int nbContestationsJoueur17=3;
		int nbContestationsJoueur18=3;
		int nbContestationsJoueur19=1;
		int nbContestationsJoueur20=1;
		
		String[][] tabSet1 = {{"0","",""},{"0","",""}};
		String[] tabJeu1 = {"0","0"};
		Score score1 = new Score(tabSet1,tabJeu1);
		String[][] tabSet2 = {{"6","3",""},{"4","3",""}};
		String[] tabJeu2 = {"30","15"};
		Score score2 = new Score(tabSet2,tabJeu2);
		String[][] tabSet3 = {{"0","",""},{"0","",""}};
		String[] tabJeu3 = {"0","0"};
		Score score3 = new Score(tabSet3,tabJeu3);
		String[][] tabSet4 = {{"7","1",""},{"5","0",""}};
		String[] tabJeu4 = {"Av",""};
		Score score4 = new Score(tabSet4,tabJeu4);
		String[][] tabSet5 = {{"6","4","4"},{"1","6","4"}};
		String[] tabJeu5 = {"0","40"};
		Score score5 = new Score(tabSet5,tabJeu5);
		String[][] tabSet6 = {{"6","3",""},{"4","2",""}};
		String[] tabJeu6 = {"30","30"};
		Score score6 = new Score(tabSet6,tabJeu6);
		String[][] tabSet7 = {{"6","6",""},{"4","3",""}};
		String[] tabJeu7 = {"",""};
		Score score7 = new Score(tabSet7,tabJeu7);
		String[][] tabSet8 = {{"6","4","2"},{"2","6","3"}};
		String[] tabJeu8 = {"40","40"};
		Score score8 = new Score(tabSet8,tabJeu8);
		String[][] tabSet9 = {{"1","",""},{"2","",""}};
		String[] tabJeu9 = {"0","15"};
		Score score9 = new Score(tabSet9,tabJeu9);
		String[][] tabSet10 = {{"3","5",""},{"6","5",""}};
		String[] tabJeu10 = {"30","30"};
		Score score10 = new Score(tabSet10,tabJeu10);
		
		String chronometre1 = "0h0m0s";
		String chronometre2 = "0h45m22s";
		String chronometre3 = "0h0m0s";
		String chronometre4 = "0h25m22s";
		String chronometre5 = "01h10m22s";
		String chronometre6 = "0h40m08s";
		String chronometre7 = "0h35m33s";
		String chronometre8 = "0h59m17s";
		String chronometre9 = "0h08m09s";
		String chronometre10 = "0h47m30s";
				
		arrayListeMatchs.add(new Match(1,joueur1,joueur2,score1,
				chronometre1,nbContestationsJoueur1,nbContestationsJoueur2));
		arrayListeMatchs.add(new Match(2,joueur3,joueur4,score2,
				chronometre2,nbContestationsJoueur3,nbContestationsJoueur4));
		arrayListeMatchs.add(new Match(3,joueur5,joueur6,score3,
				chronometre3,nbContestationsJoueur5,nbContestationsJoueur6));
		arrayListeMatchs.add(new Match(4,joueur7,joueur8,score4,
				chronometre4,nbContestationsJoueur7,nbContestationsJoueur8));
		arrayListeMatchs.add(new Match(5,joueur9,joueur10,score5,
				chronometre5,nbContestationsJoueur9,nbContestationsJoueur10));
		arrayListeMatchs.add(new Match(6,joueur11,joueur12,score6,
				chronometre6,nbContestationsJoueur11,nbContestationsJoueur12));
		arrayListeMatchs.add(new Match(7,joueur13,joueur14,score7,
				chronometre7,nbContestationsJoueur13,nbContestationsJoueur14));
		arrayListeMatchs.add(new Match(8,joueur15,joueur16,score8,
				chronometre8,nbContestationsJoueur15,nbContestationsJoueur16));
		arrayListeMatchs.add(new Match(9,joueur17,joueur18,score9,
				chronometre9,nbContestationsJoueur17,nbContestationsJoueur18));
		arrayListeMatchs.add(new Match(10,joueur19,joueur20,score10,
				chronometre10,nbContestationsJoueur19,nbContestationsJoueur20));
		 
		setListeMatchs(arrayListeMatchs);
		
	}
}
