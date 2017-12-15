package ca.udes.tp.client.jarclient.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import ca.udes.tp.client.jarclient.controller.RequestHandlerClient;
import ca.udes.tp.client.jarclient.controller.RequestHandlerClient.MethodClient;
import ca.udes.tp.object.Joueur;
import ca.udes.tp.object.ListeDesMatchs;
import ca.udes.tp.object.Match;
import ca.udes.tp.object.Pari;

public class BetPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private ListeDesMatchs listMatch;
	private int numMatchSelected; 
	private static ArrayList<Pari> listParis = new ArrayList<Pari>();
	public JPanel betPan = this;

	public static ArrayList<Pari> getListParis() {
		return listParis;
	}



	/**
	 * The BetPanel contains the elements to select and fill to place a bet
	 */
	public BetPanel() {
		listMatch = ListMatchPanel.getListMatchs();
		numMatchSelected = ListMatchPanel.getNumMatchSelected();

		Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
		border = BorderFactory.createTitledBorder(border, "Parier sur le match");
		this.setBorder(border);
		this.removeAll();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		initPanel();
	}
	
	/**
	 * This method initializes the BetPanel
	 */
	public void initPanel() {
		listMatch = ListMatchPanel.getListMatchs();
		numMatchSelected = ListMatchPanel.getNumMatchSelected();
		
		if(numMatchSelected != -1) {
			this.removeAll();
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			Match match = listMatch.getMatchWithId(numMatchSelected);
			if(!match.isSet1Finished()) {		// it will only display something if the first set is not over yet.
				Joueur player1 = match.getJoueur1();
				Joueur player2 = match.getJoueur2();

				// the list of players in the match
				final ArrayList<Joueur> listPlayers = new ArrayList<Joueur>();
				listPlayers.add(player1);
				listPlayers.add(player2);

				// ComboBox that allows to chose on which player to bet
				final JComboBox<String> playersComboBox = new JComboBox<String>();
				playersComboBox.setMaximumSize(new Dimension(300, 40));
				playersComboBox.addItem(player1.getPrenom()+" "+player1.getNom());
				playersComboBox.addItem(player2.getPrenom()+" "+player2.getNom());
				this.add(playersComboBox);

				this.add(Box.createRigidArea(new Dimension(0, 10)));

				JLabel amountLabel = new JLabel("Montant ($CA)");
				this.add(amountLabel);

				// TextField to type the amount to bet
				final JTextField amountTextField = new JTextField();
				amountTextField.setMaximumSize(new Dimension(300, 40));
				this.add(amountTextField);

				this.add(Box.createRigidArea(new Dimension(0, 10)));

				// The button to click to send bet request
				final JButton betButton = new JButton("Parier");
				betButton.setEnabled(false);
				this.add(betButton);

				amountTextField.addKeyListener(new KeyListener() {
					
					public void keyTyped(KeyEvent e) {
					}

					
					public void keyReleased(KeyEvent e) {
						// check whether the TextField contains a correct amount of money
						try {
							Double amt = Double.parseDouble(amountTextField.getText());
							if(amt<=0) {
								betButton.setEnabled(false);	// enable bet button if TextField is correctly filled
							} else {
								betButton.setEnabled(true);		// else disable the bet button
							}
						} catch(Exception ex) {
							ex.printStackTrace();
							betButton.setEnabled(false);		// disable button if not correct format (i.e. if TextField contains invalid characters such as letters)
						}
					}

					
					public void keyPressed(KeyEvent e) {
					}
				});

				betButton.addActionListener(new ActionListener() {

					
					public void actionPerformed(ActionEvent e) {
						int idJoueur = (listPlayers.get(playersComboBox.getSelectedIndex())).getId();
						double betAmount = Double.parseDouble(amountTextField.getText());
						int idParieur = 1;
						
						ArrayList<Object> args = new ArrayList<Object>();
						args.add(idJoueur);
						args.add(idParieur);
						args.add(numMatchSelected);
						args.add(betAmount);
						args.add(betPan);
						// Ask RequestHandlerClient to send a bet request to the server
						Thread thread = new Thread(new RequestHandlerClient(MethodClient.bet, args, null));
						thread.start();
					}
				});

				this.repaint();
			} else {		// if the first set is over, it is impossible to place a bet so panel is emptied
				this.removeAll();
				this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				this.repaint();
			}
		}
	}

}
