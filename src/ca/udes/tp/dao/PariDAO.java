package ca.udes.tp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public final class PariDAO {

	private static final Double PERCENTAGE = 0.75;

	private static final String INSERT_BET_REQUEST =
			"INSERT INTO public.Pari "
					+ "(idParieur, idMatch, idJoueur, montant) "
					+ "VALUES (?,?,?,?) RETURNING idPari;";

	// the total amount bet (for a given match)
	private static final String GET_TOTAL_BETS_REQUEST =
			"SELECT sum(montant) "
					+ "FROM public.Pari "
					+ "WHERE idMatch = ?;";

	// the total amount of winning bets (for a given match)
	private static final String GET_ALL_WINNING_BETS_REQUEST =
			"SELECT sum(montant) "
					+ "FROM public.Pari p, public.Rencontre r "
					+ "WHERE p.idMatch = ? "
					+ "AND p.idMatch = r.idMatch "
					+ "AND p.idJoueur = r.vainqueur;";

	// the amount bet by a gambler (for a given match)
	private static final String GET_BET_FOR_GIVEN_GAMBLER_REQUEST =
			"SELECT montant "
					+ "FROM public.Pari "
					+ "WHERE idMatch = ? "
					+ "AND idParieur = ?;";

	// the list of winning gamblers (for a given match)
	private static final String GET_WINNERS_REQUEST =
			"SELECT idParieur "
					+ "FROM public.Pari p, public.Rencontre r "
					+ "WHERE p.idMatch = ? "
					+ "AND p.idMatch = r.idMatch "
					+ "AND p.idJoueur = r.vainqueur;";
	
	/**
	 * Inserts a bet in the database
	 * @param int : idParieur
	 * @param int : idMatch
	 * @param int : idJoueur
	 * @param double : montant
	 * @return true if bet was inserted, else false
	 */
	public static boolean placeBet(int idParieur, int idMatch, int idJoueur, double montant) {
		PreparedStatement placeBetPrepStatement = null;
		ResultSet rsIdPari = null;
		boolean betPlaced = false;

		Connection connection = ConnectionDB.connection();

		if(connection != null) {
			try {
				placeBetPrepStatement = connection.prepareStatement(INSERT_BET_REQUEST);

				placeBetPrepStatement.setInt(1, idParieur);
				placeBetPrepStatement.setInt(2, idMatch);
				placeBetPrepStatement.setInt(3, idJoueur);
				placeBetPrepStatement.setDouble(4, montant);


				rsIdPari = placeBetPrepStatement.executeQuery();

				if(rsIdPari.next()) {
					betPlaced = true;
				} else {
					betPlaced = false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				ArrayList<PreparedStatement> psArray = new ArrayList<>();
				ArrayList<ResultSet> rsArray = new ArrayList<>();

				psArray.add(placeBetPrepStatement);
				rsArray.add(rsIdPari);

				ConnectionDB.closeConnections(rsArray, psArray, connection);
			}
		}

		return betPlaced;
	}

	/**
	 * Calculates the total amount of bets on a match.
	 * @param int : idMatch
	 * @return Double : the amount
	 */
	public static Double getTotalBets(int idMatch) {
		Double sum = null;

		PreparedStatement getTotalBetsPrepStmt = null;
		ResultSet rsTotalBets = null;

		Connection connection = ConnectionDB.connection();

		if(connection != null) {
			try {
				getTotalBetsPrepStmt = connection.prepareStatement(GET_TOTAL_BETS_REQUEST);
				getTotalBetsPrepStmt.setInt(1, idMatch);

				rsTotalBets = getTotalBetsPrepStmt.executeQuery();

				if(rsTotalBets.next()) {
					sum = rsTotalBets.getDouble(1);
					System.out.println("Total sum bet : " + sum);
				} else {
					sum = null;
					System.out.println("ERROR : sum could not be calculated");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				ArrayList<PreparedStatement> psArray = new ArrayList<>();
				ArrayList<ResultSet> rsArray = new ArrayList<>();

				psArray.add(getTotalBetsPrepStmt);
				rsArray.add(rsTotalBets);

				ConnectionDB.closeConnections(rsArray, psArray, connection);
			}
		}

		System.out.println("Debug : somme totale:"+sum);
		return sum;
	}

	/**
	 * Calculates the total amount of winning bets on a match. 
	 * @param int : idMatch
	 * @return the total amount
	 */
	public static Double getAllWinningBets(int idMatch) {
		Double sumWinningBets = null;

		PreparedStatement getAllWinningBetPrepStmt = null;
		ResultSet rsWinningBets = null;

		Connection connection = ConnectionDB.connection();

		if(connection != null) {
			try {
				getAllWinningBetPrepStmt = connection.prepareStatement(GET_ALL_WINNING_BETS_REQUEST);
				getAllWinningBetPrepStmt.setInt(1, idMatch);

				rsWinningBets = getAllWinningBetPrepStmt.executeQuery();

				if(rsWinningBets.next()) {
					sumWinningBets = rsWinningBets.getDouble(1);
					System.out.println("Total sum of winning bets : " + sumWinningBets);
				} else {
					sumWinningBets = null;
					System.out.println("ERROR : sum of winning bets could not be calculated");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				ArrayList<PreparedStatement> psArray = new ArrayList<>();
				ArrayList<ResultSet> rsArray = new ArrayList<>();

				psArray.add(getAllWinningBetPrepStmt);
				rsArray.add(rsWinningBets);

				ConnectionDB.closeConnections(rsArray, psArray, connection);
			}
		}

		return sumWinningBets;
	}


	/**
	 * Gets the bet placed by a given gambler on a given match.
	 * @param int : idMatch
	 * @param int : idParieur
	 * @return Double : the bet
	 */
	public static Double getBetForGivenGambler(int idMatch, int idParieur) {
		Double bet = null;

		PreparedStatement getBetPrepStmt = null;
		ResultSet rsBet = null;

		Connection connection = ConnectionDB.connection();

		if(connection != null) {
			try {
				getBetPrepStmt = connection.prepareStatement(GET_BET_FOR_GIVEN_GAMBLER_REQUEST);
				getBetPrepStmt.setInt(1, idMatch);
				getBetPrepStmt.setInt(2, idParieur);

				rsBet = getBetPrepStmt.executeQuery();

				if(rsBet.next()) {
					bet = rsBet.getDouble(1);
					System.out.println("The amount bet is : " + bet);
				} else {
					bet = null;
					System.out.println("ERROR : Could not find a bet for this gambler and match");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				ArrayList<PreparedStatement> psArray = new ArrayList<>();
				ArrayList<ResultSet> rsArray = new ArrayList<>();

				psArray.add(getBetPrepStmt);
				rsArray.add(rsBet);

				ConnectionDB.closeConnections(rsArray, psArray, connection);
			}
		}

		return bet;
	}


	/**
	 * Gets the list of gamblers (id) who won their bet on a match.
	 * @param int : idMatch
	 * @return ArrayList<Integer> : the list of gamblers
	 */
	public static ArrayList<Integer> getListOfWinners(int idMatch){
		ArrayList<Integer> listWinners = new ArrayList<>();

		PreparedStatement getListOfWinnersPrepStmt = null;
		ResultSet rsListWinners = null;

		Connection connection = ConnectionDB.connection();

		if(connection != null) {
			try {
				getListOfWinnersPrepStmt = connection.prepareStatement(GET_WINNERS_REQUEST);
				getListOfWinnersPrepStmt.setInt(1, idMatch);

				rsListWinners = getListOfWinnersPrepStmt.executeQuery();

				if(rsListWinners.next()) {
					listWinners.add(rsListWinners.getInt(1));
					System.out.println("Winner : " + rsListWinners.getInt(1));
				} else {
					System.out.println("No winning bets for this match");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				ArrayList<PreparedStatement> psArray = new ArrayList<>();
				ArrayList<ResultSet> rsArray = new ArrayList<>();

				psArray.add(getListOfWinnersPrepStmt);
				rsArray.add(rsListWinners);

				ConnectionDB.closeConnections(rsArray, psArray, connection);
			}
		}

		return listWinners;
	}


	/**
	 * Calculates the earnings of a gambler for a given match.
	 * @param int : idMatch
	 * @param int : idParieur
	 * @return the amount won by the gambler
	 * @throws Exception 
	 */
	public static Double calculateEarnings(int idMatch, int idParieur) throws Exception {
		Double sum = null;

		Double amtBetByGambler = getBetForGivenGambler(idMatch, idParieur);
		Double amtWinningBets = getAllWinningBets(idMatch);
		Double totalBets = getTotalBets(idMatch);
		if(hasBetOnWinner(idMatch, idParieur)) {
			if((amtBetByGambler != null) && (amtWinningBets != null) && (totalBets != null)) {
				if(amtWinningBets == 0) {
					throw new Exception("ERROR : division by 0");
				} else {
					sum = (amtBetByGambler/amtWinningBets)*(PERCENTAGE*totalBets);
					System.out.println("Gambler " + idParieur + " won " + sum);
				}
			} else {
				sum = null;
				System.out.println("ERROR : arguments missing to calculate earnings");
			}
		}else {
			//If bettor has lost his bet.
			sum=0d;
		}
		return sum;
	}

	/**
	 * Return true if bettor (idParieur) has bet on the winner of (idMatch), else false.
	 * @param int : idMatch
	 * @param int : idParieur
	 * @return 
	 */
	public static boolean hasBetOnWinner(int idMatch, int idParieur) {
		boolean hasBetOnWinner=false;

		ArrayList<Integer> listOfWinnerBettors = getListOfWinners(idMatch);
		if(listOfWinnerBettors.contains(idParieur)) {
			hasBetOnWinner=true;
		}
		return hasBetOnWinner;
	}
	/**
	 * Calculates the earnings of each winning gambler
	 * @param idMatch
	 * @return the gambler associated with its earnings
	 */
	public static HashMap<Integer, Double> listOfEarnings(int idMatch){
		HashMap<Integer, Double> listOfEarnings = new HashMap<>();

		ArrayList<Integer> listWinners = getListOfWinners(idMatch);

		for(Integer i : listWinners) {
			Double earnings;
			try {
				earnings = calculateEarnings(idMatch, i);
				listOfEarnings.put(i, earnings);
				System.out.println("Gambler " + i + " won " + earnings);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listOfEarnings;
	}

}