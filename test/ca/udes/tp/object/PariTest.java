package ca.udes.tp.object;

import org.junit.Assert;
import org.junit.Test;

import ca.udes.tp.object.Pari.Status;

public class PariTest {
	
	private int idParieur = 12;
	private int idMatch = 142;
	private int idJoueur = 8;
	private double montant = 30;
	private Status status = Status.asked;

	
	@Test
	public void testPariGettersSetters(){
		Pari pariTest = new Pari(idParieur, idMatch, idJoueur, montant, status);
		pariTest.setIdJoueur(23);
		pariTest.setIdMatch(185);
		pariTest.setIdParieur(15);
		pariTest.setMontant(45);
		pariTest.setStatus(Status.refused);
		
		Assert.assertEquals(15,pariTest.getIdParieur());
		Assert.assertEquals(185,pariTest.getIdMatch());
		Assert.assertEquals(23, pariTest.getIdJoueur());
		Assert.assertEquals(Status.refused, pariTest.getStatus());
		Assert.assertEquals(45, pariTest.getMontant(), 0.0001);
		
	}
	@Test
	public void testPariConstructor() {
		Pari pariTest = new Pari(idParieur, idMatch, idJoueur, montant, status);
		
		Assert.assertEquals(idParieur,pariTest.getIdParieur());
		Assert.assertEquals(idMatch,pariTest.getIdMatch());
		Assert.assertEquals(idJoueur, pariTest.getIdJoueur());
		Assert.assertEquals(status, pariTest.getStatus());
		Assert.assertEquals(montant, pariTest.getMontant(), 0.0001);
	}
}
