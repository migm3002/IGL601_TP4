package ca.udes.tp.object;

import org.junit.Assert;
import org.junit.Test;

public class JoueurTest {

	Joueur j1 = new Joueur(1, "NADAL", "Rafael");
	Joueur j2 = new Joueur(2, "FEDERER", "Roger", 2, false);
	
	@Test
	public void testSetId() {
		j1.setId(3);
		Assert.assertEquals(3, j1.getId());
	}
	
	@Test
	public void testSetNom() {
		j1.setNom("MURRAY");
		Assert.assertEquals("MURRAY", j1.getNom());
	}
	
	@Test
	public void testSetPrenom() {
		j1.setPrenom("Andy");
		Assert.assertEquals("Andy", j1.getPrenom());
	}
	
	@Test
	public void testSetClassement() {
		j2.setClassement(3);
		Assert.assertEquals(3, j2.getClassement());
	}
	
	@Test
	public void testSetService() {
		j2.setService(true);
		Assert.assertEquals(true, j2.getService());
	}
	
}
