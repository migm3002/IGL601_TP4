package utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ca.udes.tp.dao.ConnectionDB;

public final class TestUtility {
	
	private static final String CLEAN_UP_REQUEST = "DROP TABLE IF EXISTS Pari CASCADE;\r\n" + 
			"DROP TABLE IF EXISTS Jouer CASCADE;\r\n" + 
			"DROP TABLE IF EXISTS Rencontre CASCADE;\r\n" + 
			"DROP TABLE IF EXISTS Joueur CASCADE;\r\n" + 
			"DROP TABLE IF EXISTS Parieur CASCADE;\r\n" + 
			"\r\n" + 
			"CREATE TABLE Parieur(\r\n" + 
			"	idParieur int NOT NULL,\r\n" + 
			"	nom VARCHAR(10),\r\n" + 
			"	prenom VARCHAR(10),\r\n" + 
			"	PRIMARY KEY(idParieur)\r\n" + 
			");\r\n" + 
			"\r\n" + 
			"CREATE TABLE Joueur(\r\n" + 
			"	idJoueur int NOT NULL,\r\n" + 
			"	nom VARCHAR(20),\r\n" + 
			"	prenom VARCHAR(20),\r\n" + 
			"	classement int,\r\n" + 
			"	PRIMARY KEY (idJoueur)\r\n" + 
			");\r\n" + 
			"\r\n" + 
			"CREATE TABLE Rencontre(\r\n" + 
			"	idMatch int NOT NULL,\r\n" + 
			"	vainqueur int,\r\n" + 
			"	PRIMARY KEY (idMatch),\r\n" + 
			"	FOREIGN KEY (vainqueur) REFERENCES Joueur(idJoueur)\r\n" + 
			");\r\n" + 
			"\r\n" + 
			"CREATE TABLE Jouer(\r\n" + 
			"	idJoueur int NOT NULL,\r\n" + 
			"	idMatch int NOT NULL,\r\n" + 
			"	PRIMARY KEY(idJoueur, idMatch),\r\n" + 
			"	FOREIGN KEY(idMatch) REFERENCES Rencontre(idMatch),\r\n" + 
			"	FOREIGN KEY (idJoueur) REFERENCES Joueur(idJoueur)\r\n" + 
			");\r\n" + 
			"\r\n" + 
			"CREATE TABLE Pari(\r\n" + 
			"	idPari serial NOT NULL,\r\n" + 
			"	idParieur int NOT NULL,\r\n" + 
			"	idMatch int NOT NULL,\r\n" + 
			"	idJoueur int NOT NULL,\r\n" + 
			"	montant real NOT NULL,\r\n" + 
			"	PRIMARY KEY(idParieur, idJoueur, idMatch),\r\n" + 
			"	FOREIGN KEY(idParieur) REFERENCES Parieur(idParieur),\r\n" + 
			"	FOREIGN KEY(idMatch) REFERENCES Rencontre(idMatch),\r\n" + 
			"	FOREIGN KEY(idJoueur) REFERENCES Joueur(idJoueur)\r\n" + 
			");";

	private static final String SCRIPT_ADD_PARIDAO = "INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(1, 'Nadal', 'Rafael', 1);\r\n" + 
			"INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(3, 'Federer', 'Roger', 2);\r\n" + 
			"INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(15, 'Sharapova', 'Maria', 86);\r\n" + 
			"INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(11, 'Williams', 'Serena', 24);\r\n" + 
			"INSERT INTO Parieur(idParieur, nom, prenom)\r\n" + 
			"VALUES(1, 'Barre', 'Mathias');\r\n" + 
			"INSERT INTO Parieur(idParieur, nom, prenom)\r\n" + 
			"VALUES(2, 'Cosneau', 'Alexandre');\r\n" + 
			"INSERT INTO Rencontre(idMatch, vainqueur)\r\n" + 
			"VALUES(1,null);\r\n" + 
			"INSERT INTO Rencontre(idMatch, vainqueur)\r\n" + 
			"VALUES(2,11);\r\n" + 
			"INSERT INTO Pari(idPari, idParieur, idMatch, idJoueur, montant)\r\n" + 
			"VALUES (1,1,2,11,22.45);\r\n" + 
			"INSERT INTO Pari(idPari, idParieur, idMatch, idJoueur, montant)\r\n" + 
			"VALUES (2,2,2,15,51.86);\r\n" +
			"INSERT INTO Pari(idPari, idParieur, idMatch, idJoueur, montant)\r\n" + 
			"VALUES (2,2,1,15,51.86);";
	
	private static final String SCRIPT_ADD_MATCHDAO = "INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(15, 'Sharapova', 'Maria', 86);\r\n" + 
			"INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(11, 'Williams', 'Serena', 24);\r\n" + 
			"INSERT INTO Rencontre(idMatch, vainqueur)\r\n" + 
			"VALUES(1,null);";
	
	private static final String SCRIPT_FOR_INTEGRATION_MATCH_WINNER = "INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(1, 'Nadal', 'Rafael', 1);\r\n" + 
			"INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(3, 'Federer', 'Roger', 2);\r\n" + 
			"INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(5, 'Murray', 'Andy', 3);\r\n" + 
			"INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(7, 'Djokovic', 'Novak', 6);\r\n" + 
			"INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(9, 'Wavrinka', 'Stan', 8);\r\n" + 
			"INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(10, 'Thiem', 'Dominic', 7);\r\n" + 
			"INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(8, 'Raonic', 'Milos', 12);\r\n" + 
			"INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(6, 'Monfils','Gael', 42);\r\n" + 
			"INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(4, 'Tsonga', 'Jo Wilfried', 18);\r\n" + 
			"INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(2, 'Nishikori', 'Kei', 14);\r\n" + 
			"INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(11, 'Williams', 'Serena', 24);\r\n" + 
			"INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(13, 'Muguruza', 'Garbine', 2);\r\n" + 
			"INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(15, 'Sharapova', 'Maria', 86);\r\n" + 
			"INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(17, 'Halep', 'Simona', 1);\r\n" + 
			"INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(19, 'Bouchard', 'Eugenie', 78);\r\n" + 
			"INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(20, 'Mladenovic', 'Kristina', 13);\r\n" + 
			"INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(18, 'Garcia', 'Caroline', 9);\r\n" + 
			"INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(16, 'Pliskova', 'Karolina', 3);\r\n" + 
			"INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(14, 'Kuznetsova', 'Svetlana', 8);\r\n" + 
			"INSERT INTO Joueur(idJoueur, nom, prenom, classement)\r\n" + 
			"VALUES(12, 'Kvitova', 'Petra', 17);\r\n" +  
			"INSERT INTO Rencontre(idMatch, vainqueur)\r\n" + 
			"VALUES(1,null);\r\n" + 
			"INSERT INTO Rencontre(idMatch, vainqueur)\r\n" + 
			"VALUES(2,null);\r\n" + 
			"INSERT INTO Rencontre(idMatch, vainqueur)\r\n" + 
			"VALUES(3,null);\r\n" + 
			"INSERT INTO Rencontre(idMatch, vainqueur)\r\n" + 
			"VALUES(4,null);\r\n" + 
			"INSERT INTO Rencontre(idMatch, vainqueur)\r\n" + 
			"VALUES(5,null);\r\n" + 
			"INSERT INTO Rencontre(idMatch, vainqueur)\r\n" + 
			"VALUES(6,null);\r\n" + 
			"INSERT INTO Rencontre(idMatch, vainqueur)\r\n" + 
			"VALUES(7,13);\r\n" + 
			"INSERT INTO Rencontre(idMatch, vainqueur)\r\n" + 
			"VALUES(8,null);\r\n" + 
			"INSERT INTO Rencontre(idMatch, vainqueur)\r\n" + 
			"VALUES(9,null);\r\n" + 
			"INSERT INTO Rencontre(idMatch, vainqueur)\r\n" + 
			"VALUES(10,null);\r\n";
	
	//private static final String SCRIPT_ADD_DB_MANAGER = "INSERT"
	
	private static final String SCRIPT_SELECT_MATCH_WINNER = "SELECT idMatch "
			+ "FROM public.Rencontre r "
			+ "WHERE r.vainqueur IS NOT NULL ";
	
	public static void CleanUpDB() {

		PreparedStatement cleanPs = null;

		Connection connection = ConnectionDB.connection();
		if(connection != null) {
			try {
				cleanPs = connection.prepareStatement(CLEAN_UP_REQUEST);
				cleanPs.executeUpdate();
			}catch(SQLException e) {
				e.printStackTrace();
			}finally {
				if(cleanPs!=null) {
					try {
						cleanPs.close();
					}catch(SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static void addInfosForTestPariDAO() {

		PreparedStatement cleanPs = null;

		Connection connection = ConnectionDB.connection();
		if(connection != null) {
			try {
				cleanPs = connection.prepareStatement(SCRIPT_ADD_PARIDAO);
				cleanPs.executeUpdate();
			}catch(SQLException e) {
				e.printStackTrace();
			}finally {
				if(cleanPs!=null) {
					try {
						cleanPs.close();
					}catch(SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void addInfosForTestMatchDAO() {

		PreparedStatement cleanPs = null;

		Connection connection = ConnectionDB.connection();
		if(connection != null) {
			try {
				cleanPs = connection.prepareStatement(SCRIPT_ADD_MATCHDAO);
				cleanPs.executeUpdate();
			}catch(SQLException e) {
				e.printStackTrace();
			}finally {
				if(cleanPs!=null) {
					try {
						cleanPs.close();
					}catch(SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static void addInfosForTestIntegrationMatch() {

		PreparedStatement cleanPs = null;

		Connection connection = ConnectionDB.connection();
		if(connection != null) {
			try {
				cleanPs = connection.prepareStatement(SCRIPT_FOR_INTEGRATION_MATCH_WINNER);
				cleanPs.executeUpdate();
			}catch(SQLException e) {
				e.printStackTrace();
			}finally {
				if(cleanPs!=null) {
					try {
						cleanPs.close();
					}catch(SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static int[] obtainMatchWithWinners() {
		int[] idsMatchs = new int[10];

		PreparedStatement getidsMatchPrepStmt = null;
		ResultSet rsIdsMatchs = null;

		Connection connection = ConnectionDB.connection();

		if(connection != null) {
			try {
				getidsMatchPrepStmt = connection.prepareStatement(SCRIPT_SELECT_MATCH_WINNER, ResultSet.TYPE_SCROLL_SENSITIVE, 
                        ResultSet.CONCUR_UPDATABLE);
				rsIdsMatchs = getidsMatchPrepStmt.executeQuery();
				int sizeRS = -1;
				
				if (rsIdsMatchs.last()) {
					  sizeRS = rsIdsMatchs.getRow();
					  rsIdsMatchs.beforeFirst();
				}

				while(rsIdsMatchs.next()) {
					for(int i=0; i<sizeRS+1; i++) {
						idsMatchs[0] = (rsIdsMatchs.getInt(1));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				ArrayList<PreparedStatement> psArray = new ArrayList<PreparedStatement>();
				ArrayList<ResultSet> rsArray = new ArrayList<ResultSet>();

				psArray.add(getidsMatchPrepStmt);
				rsArray.add(rsIdsMatchs);

				ConnectionDB.closeConnections(rsArray, psArray, connection);
			}
		}
		return idsMatchs;
	}
	
	/*public static void addInfosForTestDatabaseManager() {
		PreparedStatement cleanPs = null;

		Connection connection = ConnectionDB.connection();
		if(connection != null) {
			try {
				cleanPs = connection.prepareStatement(SCRIPT_ADD_DB_MANAGER);
				cleanPs.executeUpdate();
			}catch(SQLException e) {
				e.printStackTrace();
			}finally {
				if(cleanPs!=null) {
					try {
						cleanPs.close();
					}catch(SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}*/
}
