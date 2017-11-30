package ca.udes.tp.client.view;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class MatchPanel extends JPanel {
	
	private MatchDetailsPanel matchDetPan;
	private BetPanel betPan;

	/**
	 * The MatchPanel is the right side of the app. It contains the MatchDetailsPanel and the BetPanel
	 */
	public MatchPanel() {
		this.removeAll();
		matchDetPan = new MatchDetailsPanel();
		betPan = new BetPanel();
				
		Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
		border = BorderFactory.createTitledBorder(border, "Match");
		this.setBorder(border);
		
		this.setLayout(new GridLayout(2, 1));
		this.add(matchDetPan);
		this.add(betPan);
	}

	public MatchDetailsPanel getMatchDetPan() {
		return matchDetPan;
	}

	public void setMatchDetPan(MatchDetailsPanel matchDetPan) {
		this.matchDetPan = matchDetPan;
	}

	public BetPanel getBetPan() {
		return betPan;
	}

	public void setBetPan(BetPanel betPan) {
		this.betPan = betPan;
	}
	
}
