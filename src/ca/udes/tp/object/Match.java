package ca.udes.tp.object;

public class Match {

	private int id;
	private Joueur joueur1; 
	private Joueur joueur2; 
	private Score score;
	private String chronometre;
	private int contestationsRestantesJoueur1;
	private int contestationsRestantesJoueur2;


	public Match(int id, Joueur joueur1, Joueur joueur2, Score score,
			String chronometre, int contestationsJ1, int contestationsJ2){
		this.id=id;
		this.joueur1 = joueur1;
		this.joueur2 = joueur2;
		this.score = score;
		this.chronometre=chronometre;
		this.contestationsRestantesJoueur1=contestationsJ1;
		this.contestationsRestantesJoueur2=contestationsJ2;
	}

	public synchronized void changeService() {
		if(!isEnded()) {
			getJoueur1().setService(!getJoueur1().getService());
			getJoueur2().setService(!getJoueur2().getService());
		}else {
			System.out.println("Error : cannot change service, match is already over.");
		}
	}
	/**
	 * Returns true is set1 of Match is over.
	 * 
	 * @return boolean
	 */
	public boolean isSet1Finished() {
		return !(getScore().getTabSet()[0][1].equals("") && getScore().getTabSet()[1][1].equals(""));
	}

	/**
	 * Return true if match is ended.
	 * @return boolean 
	 */
	public synchronized boolean isEnded() {
		if((!this.getJoueur1().getService()) && (!this.getJoueur2().getService())) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 * Return the winner of the match.
	 * Return null if match is not over, or if this is a tie.
	 */
	public Joueur getWinner() {
		Joueur winner = null;

		int nbSetWonPlay1 = 0;
		int nbSetWonPlay2 = 0;

		int nbSetsPlayed = 0;

		String[][] tabSet = this.getScore().getTabSet();

		for(int i=0 ; i<3 ; i++) {
			if(!tabSet[0][i].equals("")) {
				nbSetsPlayed++;
			}
		}

		if(!this.isEnded()) {
			winner = null;
		} else if(this.isTied()) {
			winner = null;
		} else {
			for(int i=0 ; i<nbSetsPlayed ; i++) {
				if(Integer.parseInt(tabSet[0][i])>=6 && Integer.parseInt(tabSet[1][i])<Integer.parseInt(tabSet[0][i])) {
					nbSetWonPlay1++;	// counts the number of sets won by player 1
				} else if(Integer.parseInt(tabSet[1][i])>=6 && Integer.parseInt(tabSet[0][i])<Integer.parseInt(tabSet[1][i])) {
					nbSetWonPlay2++;	// counts the number of sets won by player 2
				}
			}

			if (nbSetWonPlay1 > nbSetWonPlay2) {
				winner = this.getJoueur1();
			} else {
				winner = this.getJoueur2();
			}
		}

		return winner;
	}

	/**
	 * Calculate and set the new Score of match, after
	 * adding a new point to player (playerNumber).
	 */
	public synchronized void addPoint(int playerNumber, boolean endOfMatch) {
		if(!(playerNumber<1 || playerNumber>2)) {
			Score score = getScore();
			String[][] tabSet = score.getTabSet();
			String pointsPlayer;
			String pointsAdversary;
			if(playerNumber==1) {
				pointsPlayer = score.getTabJeu()[0];
				pointsAdversary = score.getTabJeu()[1];
			}else {
				pointsPlayer = score.getTabJeu()[1];
				pointsAdversary = score.getTabJeu()[0];
			}

			String[][] newTabSet = tabSet;
			String[] newTabJeu = new String[2];
			Score newScore;
			if(pointsPlayer.equals("0")){
				pointsPlayer="15";
				
			}else if(pointsPlayer.equals("15")){
				pointsPlayer="30";
				
			}else if(pointsPlayer.equals("30")){
				pointsPlayer="40";
				
			}else if(pointsPlayer.equals("40")){
				if(pointsAdversary.equals("40")) {
					pointsPlayer="Av";
					pointsAdversary="";
				}else {
					if(endOfMatch) {
						pointsPlayer="";
						pointsAdversary="";
					}else {
						pointsPlayer="0";
						pointsAdversary="0";
						changeService();
					}
					newTabSet = getNewTabSet(tabSet, playerNumber, endOfMatch);
				}
			
			}else if(pointsPlayer.equals("Av")) {
				if(endOfMatch) {
					pointsPlayer="";
					pointsAdversary="";
				}else {
					pointsPlayer="0";
					pointsAdversary="0";
					changeService();
				}
				newTabSet = getNewTabSet(tabSet, playerNumber, endOfMatch);
				
			}else if(pointsPlayer.equals("")) {
				pointsPlayer="40";
				pointsAdversary="40";
				
			}else {
				System.out.println("Error while adding point to the score");
				
			}

			if(playerNumber==1) {
				newTabJeu[0]=pointsPlayer;
				newTabJeu[1]=pointsAdversary;
			}else {
				newTabJeu[0]=pointsAdversary;
				newTabJeu[1]=pointsPlayer;
			}
			newScore = new Score(newTabSet, newTabJeu);
			setScore(newScore);

			if(endOfMatch) {
				this.getJoueur1().setService(false);
				this.getJoueur2().setService(false);
			}
		}else {
			System.out.println("Error : player number must be 1 or 2");
		}

	}

	/**
	 * Decrease the number ContestationsPoint left for player (playerNumber).
	 * @param int : playerNumber
	 */
	public void decreaseContestationPointFor(int playerNumber) {

		if(playerNumber==1) {
			int points = getContestationsRestantesJoueur1();
			if(points >0) {
				setContestationsRestantesJoueur1(points-1);
			}else {
				System.out.println("Error : player 1 already has 0 contestation points left.");
			}
		}else if(playerNumber==2) {
			int points = getContestationsRestantesJoueur2();
			if(points>0) {
				setContestationsRestantesJoueur2(points-1);
			}else {
				System.out.println("Error : player 2 already has 0 contestation points left.");
			}
		}else {
			System.out.println("Error : playerNumber must be 1 or 2");
		}
	}

	/**
	 * Calculate the new TabSet, after player (playerNumber) won a game.
	 * Result depends on whether it was the last game before the end of match, or not.
	 * @param String[][] oldTabSet
	 * @param int : playerNumber
	 * @param boolean : endOfMatch
	 * 
	 * @return String[][] newTabSet
	 * 
	 */
	private String[][] getNewTabSet(String[][] oldTabSet, int playerNumber, boolean endOfMatch) {
		String[][] newTabSet = new String[2][3];
		String[] tabSetPlayer;
		String[] tabSetAdversary;
		if(playerNumber==1) {
			tabSetPlayer=oldTabSet[0];
			tabSetAdversary = oldTabSet[1];

		}else {
			tabSetPlayer=oldTabSet[1];
			tabSetAdversary = oldTabSet[0];
		}

		String set1P = tabSetPlayer[0];
		String set2P = tabSetPlayer[1];
		String set3P = tabSetPlayer[2];

		String set1A = tabSetAdversary[0];
		String set2A = tabSetAdversary[1];
		String set3A = tabSetAdversary[2];

		if(set2P.equals("")) {
			set1P=String.valueOf(Integer.parseInt(set1P)+1);
			if((Integer.parseInt(set1P)==6 && (Integer.parseInt(set1A)<=4 || Integer.parseInt(set1A)==6)) ||
					Integer.parseInt(set1P)==7){
				set2P="0";
				set2A="0";
				setContestationsRestantesJoueur1(3);
				setContestationsRestantesJoueur2(3);
			}
		}else if(set3P.equals("")) {
			set2P=String.valueOf(Integer.parseInt(set2P)+1);
			if((Integer.parseInt(set2P)==6 && (Integer.parseInt(set2A)<=4 || Integer.parseInt(set2A)==6)) ||
					Integer.parseInt(set2P)==7){
				if(!endOfMatch) {
					set3P="0";
					set3A="0";
					setContestationsRestantesJoueur1(3);
					setContestationsRestantesJoueur2(3);
				}
			}
		}else {
			set3P=String.valueOf(Integer.parseInt(set3P)+1);
		}

		if(playerNumber==1) {
			newTabSet[0][0]=set1P;
			newTabSet[0][1]=set2P;
			newTabSet[0][2]=set3P;
			newTabSet[1][0]=set1A;
			newTabSet[1][1]=set2A;
			newTabSet[1][2]=set3A;
		}else {
			newTabSet[0][0]=set1A;
			newTabSet[0][1]=set2A;
			newTabSet[0][2]=set3A;
			newTabSet[1][0]=set1P;
			newTabSet[1][1]=set2P;
			newTabSet[1][2]=set3P;
		}

		return newTabSet;
	}

	/**
	 * Return true if this is a tie (if there is no winner)
	 * @return boolean
	 */
	public boolean isTied() {
		int nbTiedSets = 0;
		String[][] tabSet = this.getScore().getTabSet();

		if(!this.isEnded()) {									// if match not ended
			return false;										// match not tied
		} else {												// if match ended
			for(int i=0 ; i<3 ; i++) {							// counts the number of tied sets
				if(tabSet[0][i].equals("6") && tabSet[1][i].equals("6")) {
					nbTiedSets++;
				}
			}

			if(nbTiedSets==1 || nbTiedSets==3) {				// if 1 set tied or 3 sets tied
				return true;									// match tied
			} else {
				return false;									// in case two sets are tied, the match is not finished so it cannot be considered in this loop
			}
		}
	}

	public void setId(int id) {
		this.id=id;
	}

	public int getId() {
		return this.id;
	}

	public void setJoueur1(Joueur joueur){
		this.joueur1 = joueur;
	}

	public Joueur getJoueur1(){
		return this.joueur1;
	}

	public void setJoueur2(Joueur joueur){
		this.joueur2 = joueur;
	}

	public Joueur getJoueur2(){
		return this.joueur2;
	}

	public synchronized void setScore(Score score){
		this.score = score;
	}

	public synchronized Score getScore(){
		return this.score;
	}

	public synchronized String getChronometre() {
		return chronometre;
	}

	public synchronized void setChronometre(String chronometre) {
		this.chronometre = chronometre;
	}

	public synchronized int getContestationsRestantesJoueur1() {
		return contestationsRestantesJoueur1;
	}

	public synchronized void setContestationsRestantesJoueur1(int contestationsRestantesJoueur1) {
		if(contestationsRestantesJoueur1<0 || contestationsRestantesJoueur1>3) {
			System.out.println("DEDUG : invalid number of contestations");
		} else {
			this.contestationsRestantesJoueur1 = contestationsRestantesJoueur1;
		}
	}

	public synchronized int getContestationsRestantesJoueur2() {
		return contestationsRestantesJoueur2;
	}

	public synchronized void setContestationsRestantesJoueur2(int contestationsRestantesJoueur2) {
		if(contestationsRestantesJoueur2<0 || contestationsRestantesJoueur2>3) {
			System.out.println("DEDUG : invalid number of contestations");
		} else {
			this.contestationsRestantesJoueur2 = contestationsRestantesJoueur2;
		}
	}


}