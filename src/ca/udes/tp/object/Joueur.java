package ca.udes.tp.object;
// MDJAHEL 26/07/2017
// Classe Joueurs



public class Joueur {

	//Lors d'une egalite, on renvoie cet objet a la fonction qui demande le gagnant
	public static final Joueur NO_WINNER_TIE = new Joueur(-1,"tie","tie");
	//Si le match n'est pas fini, on renvoie cet objet a la fonction qui demande le gagnant
	public static final Joueur MATCH_NOT_ENDED = new Joueur(-2,"notEnded","notEnded");

	private int id;
	private String nom;
	private String prenom;
	private int classement;

	private boolean service = false;

	public static final int CLASSEMENT_PAR_DEFAUT = 0;

	public Joueur(int id, String nom, String prenom, int classement, boolean service){
		this.id=id;
		this.nom = nom;
		this.prenom = prenom;
		this.classement = classement;
		this.service=service;
	}

	public Joueur(int id, String nom, String prenom){
		this.id=id;
		this.nom = nom;
		this.prenom = prenom;
		this.classement = CLASSEMENT_PAR_DEFAUT;
	}

	public void setId(int id) {
		this.id=id;
	}

	public int getId() {
		return this.id;
	}

	public void setNom(String nom){
		this.nom = nom;
	}

	public String getNom(){
		return this.nom;
	}

	public void setPrenom(String prenom){
		this.prenom = prenom;
	}

	public String getPrenom(){
		return this.prenom;
	}

	public int getClassement(){
		return this.classement;
	}

	public void setClassement(int classement){
		this.classement = classement;
	}
	public synchronized boolean getService(){
		return this.service;
	}

	public synchronized void setService(boolean service){
		this.service = service;
	}

}