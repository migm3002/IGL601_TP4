package ca.udes.tp.client.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ca.udes.tp.client.controller.RequestHandlerClient;
import ca.udes.tp.client.controller.RequestSender;
import ca.udes.tp.client.controller.RequestHandlerClient.MethodClient;
import ca.udes.tp.object.ListeDesMatchs;
import ca.udes.tp.object.Match;
import ca.udes.tp.object.Pari;

public class TournoiTennisFrame extends JFrame{

	private ListMatchPanel listMatchPan; 
	private static MatchPanel matchPan;
	private JButton refreshButton;
	private RequestSender requestSender;

	private ArrayList<Match> currentListMatchEnded;
	private ArrayList<Match> oldListMatchEnded;
	private ArrayList<Match> newListMatchEnded;

	
	/**
	 * The main frame of the app
	 */
	public TournoiTennisFrame() {

		// lists of match that are over. Necessary to inform the gambler whether they have won or not and how much
		currentListMatchEnded = new ArrayList<>();
		oldListMatchEnded = new ArrayList<>();
		newListMatchEnded = new ArrayList<>();

		// initialize the frame's parameter
		this.setTitle("Tournois de tennis");
		this.setSize(1000, 1000);
		this.setLayout(new GridLayout(1, 2));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// RequestSender to manage UDP requests
		requestSender = new RequestSender();

		// Refresh button
		refreshButton = new JButton("Refresh");
		refreshButton.setPreferredSize(new Dimension(400, 30));
		refreshButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// When refreshing, the different panels are emptied and repainted with new data
				listMatchPan.removeAll();
				listMatchPan.setLayout(new GridLayout(11, 1));
				listMatchPan.initPanel();

				// manages the lists of matches that have ended to inform the gambler of their earnings if necessery
				manageEndedMatchLists();

				// continuation of repainting the frame's components
				listMatchPan.add(refreshButton);
				getMatchPan().getMatchDetPan().initPanel();
				ListeDesMatchs ldm = listMatchPan.getListMatchs();
				int numMatchSelected = listMatchPan.getNumMatchSelected();
				Match match = ldm.getMatchWithId(numMatchSelected);
				if(match != null && match.isSet1Finished()) {
					getMatchPan().getBetPan().initPanel();
				}
				// inform the gambler of their earnings (depends of the matches that have ended, as previously calculated)
				getEarnings();
				
				setVisible(true);
			}
		});

		// initialize the frame's different panels
		this.listMatchPan = new ListMatchPanel(requestSender);
		listMatchPan.add(refreshButton);
		this.add(listMatchPan);

		for(Match m : listMatchPan.getListMatchs().getListeMatchs()) {
			if(m.isEnded()) {
				currentListMatchEnded.add(m);
			}
		}

		matchPan = new MatchPanel();
		this.add(matchPan);

		this.setVisible(true);
		
		// Allows to send requests to the server regularly to have have a display up to date with the current state of matches
		this.start();
	}


	/**
	 * Allows to send requests to the server regularly
	 * to have have a display up to date with the current state of matches.
	 * This function has a behavior similar to the refresh button
	 */
	public void start() {
		while(true) {
			try {
				Thread.sleep(10000);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			listMatchPan.removeAll();
			listMatchPan.setLayout(new GridLayout(11, 1));
			
			listMatchPan.initPanel();

			manageEndedMatchLists();

			listMatchPan.add(refreshButton);
			this.getMatchPan().getMatchDetPan().initPanel();
			getMatchPan().getMatchDetPan().initPanel();
			ListeDesMatchs ldm = listMatchPan.getListMatchs();
			int numMatchSelected = listMatchPan.getNumMatchSelected();
			Match match = ldm.getMatchWithId(numMatchSelected);
			if(match != null && match.isSet1Finished()) {
				getMatchPan().getBetPan().initPanel();
			}
			getEarnings();
			setVisible(true);
		}
	}

	/**
	 * This method asks RequestHandlerClient to send a request to the server to get the gambler's earnings
	 */
	public void getEarnings() {
		// get the list of bets placed by gambler
		ArrayList<Pari> betslist = BetPanel.getListParis();

		if(!newListMatchEnded.isEmpty()) {	// if the list of matches that have just ended is not empty
			for(Pari p : betslist) {	// for each bet in the list
				for(int i=0 ; i<newListMatchEnded.size() ; i++) {	// for each match that have just ended
					if(p.getIdMatch() == newListMatchEnded.get(i).getId()) {	// if the match is a match the gambler has bet on
						if(p.getIdJoueur() == newListMatchEnded.get(i).getWinner().getId()) {	// if the player the gabler bet on has actually won
							
							ArrayList<Object> args = new ArrayList<>();
							int idParieur = 1;
							int idMatch = p.getIdMatch();
							int idJoueur = p.getIdJoueur();
							double montant = p.getMontant();
							Pari.Status status = p.getStatus();
							args.add(idParieur);
							args.add(idMatch);
							args.add(idJoueur);
							args.add(montant);
							args.add(status);
							args.add(this);
							// send a request to get earnings
							Thread th = new Thread(new RequestHandlerClient(MethodClient.getEarnings, args, null));
							th.start();
						} else {	// else, warn the gambler that they lost
							JOptionPane.showMessageDialog(this,
									"Match "+p.getIdMatch()+" : "+newListMatchEnded.get(i).getJoueur1().getNom()
									+" vs. "+newListMatchEnded.get(i).getJoueur2().getNom()
									+"\nPari perdu !\nRetentez votre chance sur un autre match.",
									"Pari",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * This method manages the matches that have ended
	 */
	public void manageEndedMatchLists() {
		// the old list becomes the previous version of the current list
		oldListMatchEnded = new ArrayList<>();
		for(Match m : currentListMatchEnded) {
			oldListMatchEnded.add(m);
		}

		// the current list is recalculated from the list of all �atches
		currentListMatchEnded = new ArrayList<>();
		for(Match m : listMatchPan.getListMatchs().getListeMatchs()) {
			if(m.isEnded()) {
				currentListMatchEnded.add(m);
			}
		}
				
		// create a temporary list, similar to the current list
		ListeDesMatchs listTemp = new ListeDesMatchs(new ArrayList<>());
		for(Match m : currentListMatchEnded) {
			listTemp.add(m);
		}

		// remove from the temporary list the matches that appear both in the current and old list to obtain the newly ended matches
		for(Match ma : oldListMatchEnded) {
			for(Match mat : newListMatchEnded) {
				if(mat.getId() == ma.getId()) {
					listTemp.getListeMatchs().remove(listTemp.getMatchWithId(ma.getId()));
				}
			}
		}
		// the new list becomes the temporary list.
		newListMatchEnded = new ArrayList<>();
		newListMatchEnded = listTemp.getListeMatchs();
	}


	public ListMatchPanel getListMatchPan() {
		return listMatchPan;
	}

	public void setListMatchPan(ListMatchPanel listMatchPan) {
		this.listMatchPan = listMatchPan;
	}

	public static MatchPanel getMatchPan() {
		return matchPan;
	}

	public static void setMatchPan(MatchPanel matchPan) {
		matchPan = matchPan;
	}




	public static void main(String[] args) {
		TournoiTennisFrame tn = new TournoiTennisFrame();
	}

}
