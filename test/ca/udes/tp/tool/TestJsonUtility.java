package ca.udes.tp.tool;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.udes.tp.communication.Message;
import ca.udes.tp.object.Joueur;
import ca.udes.tp.object.ListeDesMatchs;
import ca.udes.tp.object.Match;
import ca.udes.tp.object.Pari;
import ca.udes.tp.object.Score;

public class TestJsonUtility {
	
	@Before
	public void setUp(){
		
	}

	@After
	public void tearDown(){
		
	}

	/*@Test
	public void testGetJsonObjectFromFile(){
		String path = "{}";
		assertTrue("Not a JsonObject", (JsonUtility.getJsonObjectFromFile(path)) instanceof JSONObject);
	}*/
	
	@Test
	public void testGetJsonObjectFromString() {
		String s = "{Joueur:{prenom : Venus, nom: Williams}}";
		assertTrue("Not a JsonObject", (JsonUtility.getJsonObjectFromString(s)) instanceof JSONObject);
	}

	@Test
	public void testConvertMessageIntoJsonMessage() {
		Message msg = new Message(true, 1234, Message.Method.updateScore, null, "localhost", 9015);
		assertTrue("Not a JsonObject", (JsonUtility.convertMessageIntoJsonMessage(msg)) instanceof JSONObject);
	}
	
	@Test
	public void testGetPariJsonObjectFrom() {
		Pari pari = new Pari(1,2,3,4.25, Pari.Status.asked);
		assertTrue("Not a JsonObject", (JsonUtility.getPariJsonObjectFrom(pari))instanceof JSONObject);
	}
	
	@Test
	public void testGetPariFromPariJsonObject() {
		JSONObject parijson = new JSONObject("{'idJoueur':42,'idParieur':2,'idMatch':1475,'montant':50.0," + 
				"'status':'asked'}");
		assertTrue("Not a Pari", ((JsonUtility.getPariFromPariJsonObject(parijson)) instanceof Pari));
	}
	
	@Test
	public void testConvertJsonStringIntoMessage() {
		String s = "{\"request\":true,\"requestNumber\":1234,\"method\":\"updateScore\",\"clientPort\":9015,\"clientAddress\":\"localhost\"}";
		assertTrue("Not a message", (JsonUtility.convertJsonStringIntoMessage(s)) instanceof Message);
	}

	@Test
	public void testCreateJsonListeDesMatchsWith() {
		ListeDesMatchs lm = new ListeDesMatchs();
		assertTrue("Not a JsonArray", (JsonUtility.createJsonListeDesMatchsWith(lm))instanceof JSONArray);
	}

	@Test
	public void testGetListeDesMatchs() {
		JSONObject json = new JSONObject("{listeDesMatchs:[{\"joueur1\":{\"service\":true,\"classement\":1,\"nom\":\"NADAL\",\"prenom\":\"Rafael\",\"playerId\":1},"+
				"\"score\":{\"tableauSet\":[{\"set2Joueur1\":\"\",\"set3Joueur1\":\"\",\"set1Joueur1\":\"0\"},{\"set3Joueur2\":\"\",\"set1Joueur2\":\"0\",\"set2Joueur2\":\"\"}],"+
				"\"tableauJeu\":[{\"pointsJoueur1\":\"0\"},{\"pointsJoueur2\":\"0\"}]},\"chronometre\":\"0h0m0s\",\"contestationsRestantesJ1\":3,\"contestationsRestantesJ2\":3,"+
				"\"matchId\":1,\"joueur2\":{\"service\":false,\"classement\":14,\"nom\":\"NISHIKORI\",\"prenom\":\"Kei\",\"playerId\":2}}]}");
		assertTrue("Not a ListeDesMatchs", (JsonUtility.getListeDesMatchs(json))instanceof ListeDesMatchs);
	}

	@Test
	public void testGetMatch() {
		ListeDesMatchs lm = new ListeDesMatchs();
		JSONArray lmArray = JsonUtility.createJsonListeDesMatchsWith(lm);
		assertTrue("Not a Match", (JsonUtility.getMatch(lmArray, 4))instanceof Match);
	}

	@Test
	public void testGetJsonMatch() {
		ListeDesMatchs lm = new ListeDesMatchs();
		JSONArray lmArray = JsonUtility.createJsonListeDesMatchsWith(lm);
		assertTrue("Not a JsonObject", (JsonUtility.getJsonMatch(lmArray, 3))instanceof JSONObject);
	}

	@Test
	public void testInfosMatch() {
		ListeDesMatchs lm = new ListeDesMatchs();
		JSONArray lmArray = JsonUtility.createJsonListeDesMatchsWith(lm);
		JSONObject lmObj = JsonUtility.getJsonMatch(lmArray, 3);
		assertEquals("getIdMatch not okay", 4, JsonUtility.getIdMatch(lmObj));
		assertEquals("getChronometreFromMatch not a string", "0h25m22s", JsonUtility.getChronometreFromMatch(lmObj));
		assertEquals("getContestationsJ1 not okay", 2, JsonUtility.getConstestationsRestantesJoueur1FromMatch(lmObj));
		assertEquals("getContestationsJ2 not okay", 1, JsonUtility.getConstestationsRestantesJoueur2FromMatch(lmObj));
		
		assertTrue("getJoueur1 not Joueur", (JsonUtility.getJoueur1(lmObj))instanceof Joueur);
		assertTrue("getJoueur2 not Joueur", (JsonUtility.getJoueur2(lmObj)) instanceof Joueur);
		
		assertTrue("getJsonJoueur1 not JSONObject", (JsonUtility.getJsonJoueur1(lmObj))instanceof JSONObject);
		assertTrue("getJsonJoueur2 not JSONObject", (JsonUtility.getJsonJoueur2(lmObj)) instanceof JSONObject);
		
		assertTrue("getScore not Score", (JsonUtility.getScore(lmObj) instanceof Score));
		assertTrue("getJsonScore not JSONObject", (JsonUtility.getJsonScore(lmObj) instanceof JSONObject));
		
		
	}

	@Test
	public void testInfosJoueur() {
		ListeDesMatchs lm = new ListeDesMatchs();
		JSONArray lmArray = JsonUtility.createJsonListeDesMatchsWith(lm);
		JSONObject lmObj = JsonUtility.getJsonMatch(lmArray, 3);
		JSONObject joueur = JsonUtility.getJsonJoueur2(lmObj);

		assertEquals("getIdJoueur not okay", 8, JsonUtility.getIdJoueur(joueur));
		assertTrue("getNomJoueur not okay", JsonUtility.getNomJoueur(joueur).equals("RAONIC"));
		assertTrue("getPrenomJoueur not okay", JsonUtility.getPrenomJoueur(joueur).equals("Milos"));
		assertEquals("getClassementJoueur not okay", 12, JsonUtility.getClassementJoueur(joueur));
		assertTrue("getServiceJoueur not okay", JsonUtility.getServiceJoueur(joueur));

	}
	
	@Test
	public void testInfosScore() {
		ListeDesMatchs lm = new ListeDesMatchs();
		JSONArray lmArray = JsonUtility.createJsonListeDesMatchsWith(lm);
		JSONObject lmObj = JsonUtility.getJsonMatch(lmArray, 3);
		JSONObject score = JsonUtility.getJsonScore(lmObj);
		
		String[][] tabSet = {{"7","1",""},{"5","0",""}};
		assertArrayEquals(JsonUtility.getTableauSetFromScore(score), tabSet);
		
		assertTrue("getJsonTableauSetFromScore not JSONArray", JsonUtility.getJsonTableauSetFromScore(score)instanceof JSONArray);
		
		String[] tabJeu = {"Av",""};
		assertArrayEquals(JsonUtility.getTableauJeuFromScore(score), tabJeu);
		
		assertTrue("getJsonTableauJeuFromScore not JSONArray", JsonUtility.getJsonTableauJeuFromScore(score)instanceof JSONArray);
	
		JSONArray tableauSet = JsonUtility.getJsonTableauSetFromScore(score);
		String[] tabSetJ1 = {"7","1",""};
		assertArrayEquals(JsonUtility.getTableauSetJoueur1FromTableauSet(tableauSet), tabSetJ1);
		String[] tabSetJ2 = {"5","0",""};
		assertArrayEquals(JsonUtility.getTableauSetJoueur2FromTableauSet(tableauSet), tabSetJ2);

		assertTrue("getJsonTableauSetJoueur1FromTableauSet not a JSONObject", JsonUtility.getJsonTableauSetJoueur1FromTableauSet(tableauSet)instanceof JSONObject);
		assertTrue("getJsonTableauSetJoueur2FromTableauSet not a JSONObject", JsonUtility.getJsonTableauSetJoueur2FromTableauSet(tableauSet)instanceof JSONObject);
		
		JSONArray tableauJeu = JsonUtility.getJsonTableauJeuFromScore(score);
		assertEquals("Av", JsonUtility.getPointsJoueur1FromTableauJeu(tableauJeu));
		assertEquals("", JsonUtility.getPointsJoueur2FromTableauJeu(tableauJeu));
		
	}

}
