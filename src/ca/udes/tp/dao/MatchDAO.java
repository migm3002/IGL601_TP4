package ca.udes.tp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public final class MatchDAO {

	private static final String UPDATE_MATCH_WINNER_REQUEST =
			"UPDATE public.Rencontre "
			+ "SET vainqueur = ? "
			+ "WHERE idMatch = ? RETURNING idMatch;";
	
	/**
	 * At the end of a match, update the id of the match winner.
	 * 
	 * @param int : idMatch
	 * @param int : idWinner
	 * @return boolean : winnerUpdated
	 */
	public static boolean updateMatchWinner(int idMatch, int idWinner) {
		boolean winnerUpdated = false;
		
		PreparedStatement insertWinnerPrepStmt = null;
		ResultSet rsIdMatch = null;
		
		Connection connection = ConnectionDB.connection();
		
		if(connection != null) {
			try {
				insertWinnerPrepStmt = connection.prepareStatement(UPDATE_MATCH_WINNER_REQUEST);
				insertWinnerPrepStmt.setInt(1, idWinner);
				insertWinnerPrepStmt.setInt(2, idMatch);
				
				rsIdMatch = insertWinnerPrepStmt.executeQuery();
				
				if(rsIdMatch.next()) {
					winnerUpdated = true;
				} else {
					winnerUpdated = false;
					System.out.println("ERROR : winner could not be inserted");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				ArrayList<PreparedStatement> psArray = new ArrayList<>();
				ArrayList<ResultSet> rsArray = new ArrayList<>();
				
				psArray.add(insertWinnerPrepStmt);
				rsArray.add(rsIdMatch);
				
				ConnectionDB.closeConnections(rsArray, psArray, connection);
			}
		}
		
		return winnerUpdated;
	}
}
