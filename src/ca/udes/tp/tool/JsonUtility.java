package ca.udes.tp.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import ca.udes.tp.communication.Message;
import ca.udes.tp.communication.Message.Method;
import ca.udes.tp.object.Joueur;
import ca.udes.tp.object.ListeDesMatchs;
import ca.udes.tp.object.Match;
import ca.udes.tp.object.Pari;
import ca.udes.tp.object.Score;
import ca.udes.tp.object.Pari.Status;

/**
 * Contains every utility methods in order
 * to manipulate JSON files and objects,
 * and to generate JSON messages.
 *
 */
public final class JsonUtility {

	private static final String BOOLEAN_REQUEST_KEY = "request";
	private static final String REQUEST_NUMBER_KEY = "requestNumber";
	private static final String REQUEST_METHOD_KEY = "method";
	private static final String CLIENT_ADDRESS_KEY = "clientAddress";
	private static final String CLIENT_PORT_KEY = "clientPort";
	private static final String LIST_MATCH_KEY = "listeDesMatchs";
	private static final String MATCH_ID_KEY = "matchId";
	private static final String MATCH_PLAYER_1_KEY = "joueur1";
	private static final String MATCH_PLAYER_2_KEY = "joueur2";
	private static final String MATCH_CHRONO_KEY = "chronometre";
	private static final String PLAYER_1_CONTESTATIONS_KEY = "contestationsRestantesJ1";
	private static final String PLAYER_2_CONTESTATIONS_KEY = "contestationsRestantesJ2";
	private static final String PLAYER_ID_KEY = "playerId";
	private static final String PLAYER_SURNAME_KEY = "nom";
	private static final String PLAYER_NAME_KEY = "prenom";
	private static final String PLAYER_RANKING_KEY = "classement";
	private static final String BOOLEAN_PLAYER_SERVICE_KEY = "service";
	private static final String MATCH_SCORE_KEY = "score";
	private static final String SCORE_SET_TABLE_KEY = "tableauSet";
	private static final String SCORE_JEU_TABLE_KEY = "tableauJeu";
	private static final String SCORE_SET_1_PLAYER_1_KEY = "set1Joueur1";
	private static final String SCORE_SET_2_PLAYER_1_KEY = "set2Joueur1";
	private static final String SCORE_SET_3_PLAYER_1_KEY = "set3Joueur1";
	private static final String SCORE_SET_1_PLAYER_2_KEY = "set1Joueur2";
	private static final String SCORE_SET_2_PLAYER_2_KEY = "set2Joueur2";
	private static final String SCORE_SET_3_PLAYER_2_KEY = "set3Joueur2";
	private static final String SCORE_JEU_PLAYER_1_KEY = "pointsJoueur1";
	private static final String SCORE_JEU_PLAYER_2_KEY = "pointsJoueur2";
	private static final String BET_KEY = "pari";
	private static final String BET_PLAYER_ID_KEY = "idJoueur";
	private static final String BET_BETTOR_ID_KEY = "idParieur";
	private static final String BET_MATCH_ID_KEY = "idMatch";
	private static final String BET_AMOUNT_KEY = "montant";
	private static final String BET_STATUS_KEY = "status";
	private static final String BET_INFO_KEY = "betInfo";
	private static final String BET_EARNINGS_KEY = "betEarnings";

	///////////////////////////////////////////////////////////////// COMMON METHODS
	/**
	 * Get a JSON Object from a .json file (needs the path to this file).
	 * 
	 * @param String : path
	 * @return JSONObject : jsonObject
	 */
	public static JSONObject getJsonObjectFromFile (String path) {
		try {
			File file = new File(path);
			URI uri = file.toURI();

			JSONTokener jsonTokener = new JSONTokener(uri.toURL().openStream());
			JSONObject jsonObject = new JSONObject(jsonTokener);

			return jsonObject;
		}catch(JSONException e) {
			e.printStackTrace();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}

		//S'il y a une erreur
		return null;
	}

	/**
	 * Get a JSON Object from a String that implements JSON syntax.
	 * 
	 * @param String : string
	 * @return JSONObject : jsonObject
	 */
	public static JSONObject getJsonObjectFromString(String string) {
		try {
			JSONObject jsonObject = new JSONObject(string);
			return jsonObject;
		}catch(JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get a JSONObject message from a java Message.
	 * @param Message : message
	 * @return JSONObject : jsonMessage
	 */
	public static JSONObject convertMessageIntoJsonMessage(Message message) {
		JSONObject jsonMessage = new JSONObject();

		boolean isRequest = message.isRequest();
		int requestNumber = message.getRequestNumber();
		Method method = message.getMethod();
		Object argument = message.getArgument();

		try {
			jsonMessage.put(BOOLEAN_REQUEST_KEY, isRequest);
			jsonMessage.put(REQUEST_NUMBER_KEY, requestNumber);
			jsonMessage.put(REQUEST_METHOD_KEY, message.getMethod().toString());

			//Put the argument
			if(method==Method.bet) {					//If it's a bet request/answer
				JSONObject pariObject = getPariJsonObjectFrom((Pari) message.getArgument());
				jsonMessage.put(BET_KEY, pariObject);
			}else if(method==Method.updateScore) {
				if(!isRequest) {						//If it's an update score answer
					JSONArray scoreArray = createJsonListeDesMatchsWith((ListeDesMatchs) argument);
					jsonMessage.put(LIST_MATCH_KEY, scoreArray);
				}
			}else if(method==Method.getEarnings) {		//If it's a getEarnings method
				JSONObject betInfo = getPariJsonObjectFrom((Pari) message.getArgument());
				if(isRequest) {
					jsonMessage.put(BET_INFO_KEY,betInfo);
				}else {
					jsonMessage.put(BET_EARNINGS_KEY,betInfo);
				}
			}

			jsonMessage.put(CLIENT_ADDRESS_KEY, message.getClientAddress());
			jsonMessage.put(CLIENT_PORT_KEY, message.getClientPort());
		}catch(JSONException e) {
			e.printStackTrace();
		}

		return jsonMessage;
	}

	/**
	 * Get a JSONObject bet from a java Pari.
	 * @param Pari : pari
	 * @return JSONObject : pariJson
	 */
	public static JSONObject getPariJsonObjectFrom(Pari pari) {
		JSONObject pariJson = new JSONObject();

		try {
			pariJson.put(BET_PLAYER_ID_KEY, pari.getIdJoueur());
			pariJson.put(BET_BETTOR_ID_KEY, pari.getIdParieur());
			pariJson.put(BET_MATCH_ID_KEY, pari.getIdMatch());
			pariJson.put(BET_AMOUNT_KEY, String.valueOf(pari.getMontant()));
			pariJson.put(BET_STATUS_KEY, pari.getStatus().toString());
		}catch(JSONException e) {
			e.printStackTrace();
		}

		return pariJson;

	}

	/**
	 * Get Pari from a JSONObject pari.
	 * @param JSONObject : jsonPari
	 * @return Pari : pari
	 */
	public static Pari getPariFromPariJsonObject(JSONObject jsonPari) {
		Pari pari = null;
		try {
			int idParieur = jsonPari.getInt(BET_BETTOR_ID_KEY);
			int idMatch = jsonPari.getInt(BET_MATCH_ID_KEY);
			int idJoueur = jsonPari.getInt(BET_PLAYER_ID_KEY);
			double montant = jsonPari.getDouble(BET_AMOUNT_KEY);
			Status status = Status.valueOf(jsonPari.getString(BET_STATUS_KEY));
			pari = new Pari(idParieur, idMatch, idJoueur, montant, status);
		}catch(JSONException e) {
			e.printStackTrace();
		}

		return pari;
	}

	/**
	 * Convert a String that follows JSON syntax and Message structure, into
	 * a Message object.
	 * @param String : jsonString
	 * @return Message : message
	 */
	public static Message convertJsonStringIntoMessage(String jsonString) {
		Message message = null;

		JSONObject jsonMessage = getJsonObjectFromString(jsonString);

		try {
			boolean isRequest = jsonMessage.getBoolean(BOOLEAN_REQUEST_KEY);
			int requestNumber = jsonMessage.getInt(REQUEST_NUMBER_KEY);
			Method method = Method.valueOf(jsonMessage.getString(REQUEST_METHOD_KEY));

			//Get the argument
			Object argument = null;
			if(method==Method.bet) {					//If it's a bet request/answer
				JSONObject jsonPari = jsonMessage.getJSONObject(BET_KEY);
				Pari pari = getPariFromPariJsonObject(jsonPari);
				argument = pari;

			}else if(method==Method.updateScore) {
				if(!isRequest) {						//If it's an update score answer
					ListeDesMatchs listeDesMatchs = getListeDesMatchs(jsonMessage);
					argument=listeDesMatchs;
				}else {									//If it's an update score request
					argument = Message.EMPTY_ARGUMENT;
				}
			}else if(method==Method.getEarnings){		
				if(isRequest) {							//If it's a getEarnings request
					JSONObject jsonPari = jsonMessage.getJSONObject(BET_INFO_KEY);
					Pari pari = getPariFromPariJsonObject(jsonPari);
					argument=pari;
				}else {									//If it's a getEarnings response
					JSONObject jsonPari = jsonMessage.getJSONObject(BET_EARNINGS_KEY);
					Pari pari = getPariFromPariJsonObject(jsonPari);
					argument=pari;
				}
			}

			String clientAddress = jsonMessage.getString(CLIENT_ADDRESS_KEY);
			int clientPort = jsonMessage.getInt(CLIENT_PORT_KEY);

			message = new Message(isRequest, requestNumber, method, argument, clientAddress, clientPort);

		}catch(JSONException e) {
			e.printStackTrace();
		}
		return message;
	}



	////////////////////////////////////////////////////////////////////////////SERVER METHODS
	/**
	 * Get a JSONArray that contains all the JSONObject Match, from a java ListeDesMatchs.
	 * @param ListeDesMatchs : listeMatchs
	 * @return JSONArray : jsonListeMatchs
	 */
	public static JSONArray createJsonListeDesMatchsWith(ListeDesMatchs listeMatchs) {
		JSONArray jsonListeMatchs = new JSONArray();
		try {
			for(Match match : listeMatchs.getListeMatchs()) {
				JSONObject newJsonMatch = new JSONObject();

				//Initialisation des objets JSON
				JSONObject joueur1 = new JSONObject();
				JSONObject joueur2 = new JSONObject();
				JSONObject score = new JSONObject();
				JSONObject tableauSetLigne1 = new JSONObject();
				JSONObject tableauSetLigne2 = new JSONObject();
				JSONObject tableauJeuLigne1 = new JSONObject();
				JSONObject tableauJeuLigne2 = new JSONObject();

				//Initialisation des tableaux JSON
				JSONArray tableauSet = new JSONArray();
				JSONArray tableauJeu = new JSONArray();


				//Remplissage des objets (l'ordre entre les blocs est important, pour certains blocs)
				joueur1.put(PLAYER_ID_KEY, match.getJoueur1().getId());
				joueur1.put(PLAYER_SURNAME_KEY, match.getJoueur1().getNom());
				joueur1.put(PLAYER_NAME_KEY, match.getJoueur1().getPrenom());
				joueur1.put(PLAYER_RANKING_KEY, match.getJoueur1().getClassement());
				joueur1.put(BOOLEAN_PLAYER_SERVICE_KEY, match.getJoueur1().getService());

				joueur2.put(PLAYER_ID_KEY, match.getJoueur2().getId());
				joueur2.put(PLAYER_SURNAME_KEY, match.getJoueur2().getNom());
				joueur2.put(PLAYER_NAME_KEY, match.getJoueur2().getPrenom());
				joueur2.put(PLAYER_RANKING_KEY, match.getJoueur2().getClassement());
				joueur2.put(BOOLEAN_PLAYER_SERVICE_KEY, match.getJoueur2().getService());

				tableauSetLigne1.put(SCORE_SET_1_PLAYER_1_KEY, match.getScore().getTabSet()[0][0]);
				tableauSetLigne1.put(SCORE_SET_2_PLAYER_1_KEY, match.getScore().getTabSet()[0][1]);
				tableauSetLigne1.put(SCORE_SET_3_PLAYER_1_KEY, match.getScore().getTabSet()[0][2]);

				tableauSetLigne2.put(SCORE_SET_1_PLAYER_2_KEY, match.getScore().getTabSet()[1][0]);
				tableauSetLigne2.put(SCORE_SET_2_PLAYER_2_KEY, match.getScore().getTabSet()[1][1]);
				tableauSetLigne2.put(SCORE_SET_3_PLAYER_2_KEY, match.getScore().getTabSet()[1][2]);

				tableauJeuLigne1.put(SCORE_JEU_PLAYER_1_KEY, match.getScore().getTabJeu()[0]);
				tableauJeuLigne2.put(SCORE_JEU_PLAYER_2_KEY, match.getScore().getTabJeu()[1]);

				tableauSet.put(tableauSetLigne1);
				tableauSet.put(tableauSetLigne2);

				tableauJeu.put(tableauJeuLigne1);
				tableauJeu.put(tableauJeuLigne2);

				score.put(SCORE_SET_TABLE_KEY, tableauSet);
				score.put(SCORE_JEU_TABLE_KEY, tableauJeu);

				newJsonMatch.put(MATCH_ID_KEY, match.getId());
				newJsonMatch.put(MATCH_CHRONO_KEY, match.getChronometre());
				newJsonMatch.put(MATCH_PLAYER_1_KEY, joueur1);
				newJsonMatch.put(MATCH_PLAYER_2_KEY, joueur2);
				newJsonMatch.put(PLAYER_1_CONTESTATIONS_KEY, 
						match.getContestationsRestantesJoueur1());
				newJsonMatch.put(PLAYER_2_CONTESTATIONS_KEY, 
						match.getContestationsRestantesJoueur2());
				newJsonMatch.put(MATCH_SCORE_KEY, score);


				jsonListeMatchs.put(newJsonMatch);
			}
		}catch(JSONException e) {
			e.printStackTrace();
		}
		return jsonListeMatchs;
	}



	//////////////////////////////////////////////////////////////////////////////// CLIENT METHODS

	/**
	 * Get a java ListeDesMatchs from a JSONObject message.
	 * 
	 * @param JSONObject : messageJson
	 * @return ListeDesMatchs : listeDesMatchs
	 */
	public static ListeDesMatchs getListeDesMatchs(JSONObject messageJson) {
		ListeDesMatchs listeDesMatchs=null;
		ArrayList<Match> arrayListMatchs = new ArrayList<Match>();
		try {
			if(messageJson.getJSONArray(LIST_MATCH_KEY)!=null) {
				JSONArray matchArray = messageJson.getJSONArray(LIST_MATCH_KEY);

				for (int i=0; i<matchArray.length();i++) {
					arrayListMatchs.add(getMatch(matchArray,i));
				}

				listeDesMatchs = new ListeDesMatchs(arrayListMatchs);
			}else {
				System.out.println("Error : listeDesMatchs not found in the message");
			}
		}catch(JSONException e) {
			e.printStackTrace();
		}
		return listeDesMatchs;
	}

	/**
	 * Instanciate Match n°index from a JSONArray with JSONObject matchs.
	 * (index must be between 0 and 9)
	 * 
	 * @param JSONArray : matchArray
	 * @param int : index 
	 * @return Match : match
	 */
	public static Match getMatch(JSONArray matchArray, int index) {
		Match match;
		JSONObject jsonMatch = getJsonMatch(matchArray,index);

		int idMatch = getIdMatch(jsonMatch);
		Joueur joueur1 = getJoueur1(jsonMatch);
		Joueur joueur2 = getJoueur2(jsonMatch);
		Score score = getScore(jsonMatch);
		String chronometre = getChronometreFromMatch(jsonMatch);
		int contestationsJ1 = getConstestationsRestantesJoueur1FromMatch(jsonMatch);
		int contestationsJ2 = getConstestationsRestantesJoueur2FromMatch(jsonMatch);

		match = new Match(idMatch,joueur1, joueur2, score, chronometre, contestationsJ1, contestationsJ2);
		return match;
	}

	/**
	 * Get a JSONObject match n°index from a JSONArray.
	 * (index must be between 0 and 9)
	 * 
	 * @param JSONArray : matchArray
	 * @param int : index
	 * @return JSONObject : match
	 */
	public static JSONObject getJsonMatch(JSONArray matchArray, int index) {
		if(index<0 || index>9) {
			System.out.println("Error : match index must be between 0 and 9");
			return null;
		}else {
			try {
				return matchArray.getJSONObject(index);
			}catch(JSONException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	//Parcours d'un match
	/**
	 * Get the id of a JSONObject match.
	 * 
	 * @param JSONObject : match
	 * @return int : id
	 */
	public static int getIdMatch(JSONObject match) {
		try {
			return match.getInt(MATCH_ID_KEY);
		}catch(JSONException e) {
			e.printStackTrace();
		}

		return -1;
	}

	/**
	 * Get the time(chronometre) of the JSONObject match
	 * @param JSONObject : match
	 * @return String : chronometre
	 */
	public static String getChronometreFromMatch(JSONObject match) {
		try {
			return match.getString(MATCH_CHRONO_KEY);
		}catch(JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get the remaining appeals of player 1 in the JSONObject match.
	 * @param JSONObject : match
	 * @return int : remainingAppeals
	 */
	public static int getConstestationsRestantesJoueur1FromMatch(JSONObject match) {
		try {
			return match.getInt(PLAYER_1_CONTESTATIONS_KEY);
		}catch(JSONException e) {
			e.printStackTrace();
		}

		return -1;
	}

	/**
	 * Get the remaining appeals of player 2 in the JSONObject match.
	 * @param JSONObject : match
	 * @return int : remainingAppeals
	 */
	public static int getConstestationsRestantesJoueur2FromMatch(JSONObject match) {
		try {
			return match.getInt(PLAYER_2_CONTESTATIONS_KEY);
		}catch(JSONException e) {
			e.printStackTrace();
		}

		return -1;
	}

	/**
	 * Get the player 1 (class Joueur) from a JSONObject match.
	 * 
	 * @param JSONObject : match
	 * @return Joueur : joueur1
	 */
	public static Joueur getJoueur1(JSONObject match) {
		Joueur joueur1;
		JSONObject jsonJoueur1 = getJsonJoueur1(match);
		joueur1 = new Joueur(getIdJoueur(jsonJoueur1),
				getNomJoueur(jsonJoueur1),
				getPrenomJoueur(jsonJoueur1),
				getClassementJoueur(jsonJoueur1),
				getServiceJoueur(jsonJoueur1));
		return joueur1;
	}

	/**
	 * Get the JSONObject player1 from a JSONObject match.
	 * 
	 * @param JSONObject : match
	 * @return JSONObject : player1
	 */
	public static JSONObject getJsonJoueur1(JSONObject match) {
		try {
			return match.getJSONObject(MATCH_PLAYER_1_KEY);
		}catch(JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get the player 2 (class Joueur) from a JSONObject match.
	 * 
	 * @param JSONObject : match
	 * @return Joueur : joueur2
	 */
	public static Joueur getJoueur2(JSONObject match) {
		Joueur joueur2;
		JSONObject jsonJoueur2 = getJsonJoueur2(match);
		joueur2 = new Joueur(getIdJoueur(jsonJoueur2),
				getNomJoueur(jsonJoueur2),
				getPrenomJoueur(jsonJoueur2),
				getClassementJoueur(jsonJoueur2),
				getServiceJoueur(jsonJoueur2));
		return joueur2;
	}

	/**
	 * Get the JSONObject player 2 from a JSONObject match.
	 * 
	 * @param JSONObject : match
	 * @return JSONObject : player2
	 */
	public static JSONObject getJsonJoueur2(JSONObject match) {
		try {
			return match.getJSONObject(MATCH_PLAYER_2_KEY);
		}catch(JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get the Score from a JSONObject match.
	 * @param JSONObject : match
	 * @return Score : score
	 */
	public static Score getScore(JSONObject match) {
		Score score;
		JSONObject jsonScore = getJsonScore(match);
		String[][] tableauSet = getTableauSetFromScore(jsonScore);
		String[] tableauJeu = getTableauJeuFromScore(jsonScore);

		score = new Score(tableauSet, tableauJeu);

		return score;
	}

	/**
	 * Get the JSONObject score from a JSONObject match.
	 * @param JSONObject : match
	 * @return JSONObject : score
	 */
	public static JSONObject getJsonScore(JSONObject match){
		try {
			return match.getJSONObject(MATCH_SCORE_KEY);
		}catch(JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	//Parcours d'un joueur

	/**
	 * Get the id of a JSONObject player.
	 * 
	 * @param JSONObject : joueur
	 * @return int : id
	 */
	public static int getIdJoueur(JSONObject joueur) {
		try {
			return joueur.getInt(PLAYER_ID_KEY);
		}catch(JSONException e) {
			e.printStackTrace();
		}

		return -1;
	}

	/**
	 * Get the surname of a JSONObject player.
	 * 
	 * @param JSONObject : joueur
	 * @return String : nom
	 */
	public static String getNomJoueur(JSONObject joueur) {
		try {
			return joueur.getString(PLAYER_SURNAME_KEY);
		}catch(JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get the name of a JSONObject player.
	 * 
	 * @param JSONObject : joueur
	 * @return String : prenom
	 */
	public static String getPrenomJoueur(JSONObject joueur) {
		try {
			return joueur.getString(PLAYER_NAME_KEY);
		}catch(JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get the ranking of a JSONObject player.
	 * 
	 * @param JSONObject : joueur
	 * @return int : classement
	 */
	public static int getClassementJoueur(JSONObject joueur) {
		try {
			return joueur.getInt(PLAYER_RANKING_KEY);
		}catch(JSONException e) {
			e.printStackTrace();
		}

		return -1;
	}
	/**
	 * Get the boolean value service of a JSONObject player.
	 * @param JSONObject : joueur
	 * @return boolean : service
	 */
	public static boolean getServiceJoueur(JSONObject joueur) {
		//If unreadable, return false by default.
		try {
			return joueur.getBoolean(BOOLEAN_PLAYER_SERVICE_KEY);
		}catch(JSONException e) {
			e.printStackTrace();
		}

		return false;
	}

	//Parcours d'un score

	/**
	 * Get the String Set table from a JSONObject score.
	 * 
	 * @param JSONObject : score
	 * @return String[][] : tableauSet
	 */
	public static String[][] getTableauSetFromScore(JSONObject score){
		String[][] tableauSet= new String[2][3];

		JSONArray jsonTableauSet = getJsonTableauSetFromScore(score);

		tableauSet[0]=getTableauSetJoueur1FromTableauSet(jsonTableauSet);
		tableauSet[1]=getTableauSetJoueur2FromTableauSet(jsonTableauSet);

		return tableauSet;
	}

	/**
	 * Get the JSONArray Set table from a JSONObject score.
	 * @param JSONObject : score
	 * @return JSONArray : tableauSet
	 */
	public static JSONArray getJsonTableauSetFromScore(JSONObject score) {
		try {
			return score.getJSONArray(SCORE_SET_TABLE_KEY);
		}catch(JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get the string Game(Jeu) table from a JSONObject score.
	 * 
	 * @param JSONObject : score
	 * @return String[] : tableauJeu
	 */
	public static String[] getTableauJeuFromScore(JSONObject score) {
		String[] tableauJeu = new String[2];
		JSONArray jsonTableauJeu = getJsonTableauJeuFromScore(score);


		tableauJeu[0]=getPointsJoueur1FromTableauJeu(jsonTableauJeu);
		tableauJeu[1]=getPointsJoueur2FromTableauJeu(jsonTableauJeu);

		return tableauJeu;
	}

	/**
	 * Get the JSONArray game(jeu) table from a JSONObject score.
	 * @param JSONObject : score
	 * @return JSONArray : tableauJeu
	 */
	public static JSONArray getJsonTableauJeuFromScore(JSONObject score) {
		try {
			return score.getJSONArray(SCORE_JEU_TABLE_KEY);
		}catch(JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get the String Set table of player 1 from a JSONArray Set table.
	 * @param JSONArray : tableauSet
	 * @return String[] : tableauSetJoueur1
	 */
	public static String[] getTableauSetJoueur1FromTableauSet(JSONArray tableauSet) {
		String[] tableauSetJoueur1 = new String[3];
		JSONObject jsonTableauJoueur1 = getJsonTableauSetJoueur1FromTableauSet(tableauSet);
		try {
			tableauSetJoueur1[0]=jsonTableauJoueur1.getString(SCORE_SET_1_PLAYER_1_KEY);
			tableauSetJoueur1[1]=jsonTableauJoueur1.getString(SCORE_SET_2_PLAYER_1_KEY);
			tableauSetJoueur1[2]=jsonTableauJoueur1.getString(SCORE_SET_3_PLAYER_1_KEY);

			return tableauSetJoueur1;
		}catch(JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get the JSONObject Set table of player 1 from a JSONArray Set table.
	 * 
	 * @param JSONArray : tableauSet
	 * @return JSONObject : jsonTableauSetJoueur1
	 */
	public static JSONObject getJsonTableauSetJoueur1FromTableauSet(JSONArray tableauSet) {
		try {
			return tableauSet.getJSONObject(0);
		}catch(JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get the String Set table of player 2 from a JSONArray Set table.
	 * @param JSONArray : tableauSet
	 * @return String[] : tableauSetJoueur2
	 */
	public static String[] getTableauSetJoueur2FromTableauSet(JSONArray tableauSet) {
		String[] tableauSetJoueur2 = new String[3];
		JSONObject jsonTableauJoueur2 = getJsonTableauSetJoueur2FromTableauSet(tableauSet);
		try {
			tableauSetJoueur2[0]=jsonTableauJoueur2.getString(SCORE_SET_1_PLAYER_2_KEY);
			tableauSetJoueur2[1]=jsonTableauJoueur2.getString(SCORE_SET_2_PLAYER_2_KEY);
			tableauSetJoueur2[2]=jsonTableauJoueur2.getString(SCORE_SET_3_PLAYER_2_KEY);

			return tableauSetJoueur2;
		}catch(JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get the JSONObject Set table of player 2 from a JSONArray Set table.
	 * @param JSONArray : tableauSet
	 * @return JSONObject : tableauSetJoueur2
	 */
	public static JSONObject getJsonTableauSetJoueur2FromTableauSet(JSONArray tableauSet) {
		try {
			return tableauSet.getJSONObject(1);
		}catch(JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get the String game(jeu) points of player 1 from a JSONArray game(jeu) table.
	 * @param JSONArray : tableauJeu
	 * @return String : pointsJoueur1
	 */
	public static String getPointsJoueur1FromTableauJeu(JSONArray tableauJeu) {
		try {
			return tableauJeu.getJSONObject(0).getString(SCORE_JEU_PLAYER_1_KEY);
		}catch(JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get the String game(jeu) points of player 2 from a JSONArray game(jeu) table.
	 * @param JSONArray : tableauJeu
	 * @return String : pointsJoueur2
	 */
	public static String getPointsJoueur2FromTableauJeu(JSONArray tableauJeu) {
		try {
			return tableauJeu.getJSONObject(1).getString(SCORE_JEU_PLAYER_2_KEY);
		}catch(JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

}
