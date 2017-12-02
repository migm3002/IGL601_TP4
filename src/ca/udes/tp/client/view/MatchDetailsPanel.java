package ca.udes.tp.client.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

import ca.udes.tp.object.Joueur;
import ca.udes.tp.object.ListeDesMatchs;
import ca.udes.tp.object.Match;

public class MatchDetailsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private ListeDesMatchs listMatch;
	private int numMatchSelected; 

	/**
	 * The MatchDetailsPanel contains all the details of a particular match
	 */
	public MatchDetailsPanel() {
		listMatch = ListMatchPanel.getListMatchs();
		numMatchSelected = ListMatchPanel.getNumMatchSelected(); 

		Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
		border = BorderFactory.createTitledBorder(border, "Détails du match");
		this.setBorder(border);
		this.removeAll();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		initPanel();
	}

	/**
	 * This method initializes the MatchDetailsPanel and its content.
	 */
	public void initPanel() {
		listMatch = ListMatchPanel.getListMatchs();
		numMatchSelected = ListMatchPanel.getNumMatchSelected();
		if(numMatchSelected != -1) {
			this.removeAll();
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			Match match = listMatch.getMatchWithId(numMatchSelected);
			Joueur player1 = match.getJoueur1();
			Joueur player2 = match.getJoueur2();

			JTable matchTable = new JTable(2,5);

			// table look
			matchTable.getColumnModel().getColumn(0).setPreferredWidth(120);
			for(int j=1 ; j<5 ; j++) {
				matchTable.getColumnModel().getColumn(j).setPreferredWidth(40);
			}
			matchTable.setRowHeight(20);
			DefaultTableCellRenderer gameColRenderer = new DefaultTableCellRenderer();
			gameColRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
			matchTable.getColumnModel().getColumn(4).setCellRenderer(gameColRenderer);

			// add data to table
			matchTable.setValueAt(match.getJoueur1().getNom().toUpperCase(), 0, 0);
			matchTable.setValueAt(match.getScore().getTabSet()[0][0], 0, 1);
			matchTable.setValueAt(match.getScore().getTabSet()[0][1], 0, 2);
			matchTable.setValueAt(match.getScore().getTabSet()[0][2], 0, 3);
			matchTable.setValueAt(match.getScore().getTabJeu()[0], 0, 4);
			matchTable.setValueAt(match.getJoueur2().getNom().toUpperCase(), 1, 0);
			matchTable.setValueAt(match.getScore().getTabSet()[1][0], 1, 1);
			matchTable.setValueAt(match.getScore().getTabSet()[1][1], 1, 2);
			matchTable.setValueAt(match.getScore().getTabSet()[1][2], 1, 3);
			matchTable.setValueAt(match.getScore().getTabJeu()[1], 1, 4);

			this.add(matchTable);

			this.add(Box.createRigidArea(new Dimension(10, 0)));

			// display match time
			JLabel timeLabel = new JLabel("Chrono : "+match.getChronometre());
			this.add(timeLabel);

			// display serving player
			JLabel servingPlayerLabel = new JLabel();
			if(match.isEnded()) {
				servingPlayerLabel.setText("Vainqueur : "+match.getWinner().getPrenom()+" "+match.getWinner().getNom());
			} else {
				if(player1.getService()) {
					servingPlayerLabel.setText("Service : "+player1.getPrenom()+" "+player1.getNom());
				} else if(player2.getService()) {
					servingPlayerLabel.setText("Service : "+player2.getPrenom()+" "+player2.getNom());
				}
			}
			this.add(servingPlayerLabel);

			// display remaining challenges
			if(!match.isEnded()) {
				JLabel challengesRemainingPlay1 = new JLabel(player1.getPrenom()+" "+player1.getNom()+" : "+match.getContestationsRestantesJoueur1()+" contestation(s) restante(s)");
				JLabel challengesRemainingPlay2 = new JLabel(player2.getPrenom()+" "+player2.getNom()+" : "+match.getContestationsRestantesJoueur2()+" contestation(s) restante(s)");
				this.add(challengesRemainingPlay1);
				this.add(challengesRemainingPlay2);
			}
			repaint();
		}
	}

}
