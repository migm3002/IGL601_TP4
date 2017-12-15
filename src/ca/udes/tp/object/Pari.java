package ca.udes.tp.object;

public class Pari{

	private int idJoueur;
	private int idParieur;
	private double montant;
	private int idMatch;
	private Status status;

	public enum Status{
		asked,
		commited,
		refused
	}
	
	public Pari(int idParieur, int idMatch, int idJoueur, double montant, Status status){
		this.idParieur = idParieur;
		this.idMatch = idMatch;
		this.idJoueur = idJoueur;
		this.montant = montant;
		this.status=status;
	}

	public void setIdParieur (int idParieur){
		this.idParieur = idParieur;
	}

	public void setIdMatch (int idMatch){
		this.idMatch = idMatch;
	}

	public void setMontant (double montant){
		this.montant = montant;
	}

	public void setIdJoueur (int idJoueur){
		this.idJoueur = idJoueur;
	}

	public int getIdParieur (){
		return this.idParieur;
	}

	public int getIdMatch(){
		return this.idMatch;
	}

	public int getIdJoueur(){
		return this.idJoueur;
	}

	public double getMontant(){
		return this.montant;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}