package utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
