package ca.udes.tp.client.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

import ca.udes.tp.client.controller.RequestHandlerClient;
import ca.udes.tp.client.controller.RequestSender;
import ca.udes.tp.client.controller.RequestHandlerClient.MethodClient;
import ca.udes.tp.object.ListeDesMatchs;

public class ListMatchPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static ListeDesMatchs listMatchs;
	private static int numMatchSelected = -1;
	private Thread thread;
	private RequestSender requestSender;

	public static ListeDesMatchs getListMatchs() {
		return listMatchs;
	}

	public static void setListMatchs(ListeDesMatchs listMatchs) {
		ListMatchPanel.listMatchs = listMatchs;
	}

	public static int getNumMatchSelected() {
		return numMatchSelected;
	}

	public void setNumMatchSelected(int numMatchSelected) {
		this.numMatchSelected = numMatchSelected;
	}


	/**
	 * Creates the panel that contains the list of all the matches
	 * @param reqSender
	 */
	public ListMatchPanel(RequestSender reqSender) {

		this.requestSender = reqSender;
		
		Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
		border = BorderFactory.createTitledBorder(border, "Liste des Matchs");
		this.setBorder(border);

		this.setLayout(new GridLayout(11, 1));
		initPanel();
	}
	

	/**
	 * This method creates all the components necessary to display the matches
	 */
	public void initPanel() {
		// Send a request to get the list of matches
		initListeDesMatch();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for(int i=0 ; i<listMatchs.getListeMatchs().size() ; i++) {
			JPanel mPan = new JPanel();
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
			matchTable.setValueAt(listMatchs.get(i).getJoueur1().getNom().toUpperCase(), 0, 0);
			matchTable.setValueAt(listMatchs.get(i).getScore().getTabSet()[0][0], 0, 1);
			matchTable.setValueAt(listMatchs.get(i).getScore().getTabSet()[0][1], 0, 2);
			matchTable.setValueAt(listMatchs.get(i).getScore().getTabSet()[0][2], 0, 3);
			matchTable.setValueAt(listMatchs.get(i).getScore().getTabJeu()[0], 0, 4);
			matchTable.setValueAt(listMatchs.get(i).getJoueur2().getNom().toUpperCase(), 1, 0);
			matchTable.setValueAt(listMatchs.get(i).getScore().getTabSet()[1][0], 1, 1);
			matchTable.setValueAt(listMatchs.get(i).getScore().getTabSet()[1][1], 1, 2);
			matchTable.setValueAt(listMatchs.get(i).getScore().getTabSet()[1][2], 1, 3);
			matchTable.setValueAt(listMatchs.get(i).getScore().getTabJeu()[1], 1, 4);

			mPan.add(matchTable);

			// add button to follow a match
			JButton followBut = new JButton("Suivre");
			followBut.setName(""+listMatchs.get(i).getId());
			followBut.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// When the follow button is clicked, display match details
					numMatchSelected = Integer.parseInt(((JButton)e.getSource()).getName());
					System.out.println("DEBUG : match selected : "+((JButton)e.getSource()).getName());
					TournoiTennisFrame.getMatchPan().getMatchDetPan().initPanel();
					TournoiTennisFrame.getMatchPan().getBetPan().initPanel();
				}
			});
			
			mPan.add(followBut);
			this.add(mPan);
			
		}
	}

	/**
	 * This method asks the RequestHandlerClient to send a request to get the list of matches
	 */
	public void initListeDesMatch() {
		ArrayList<Object> args = new ArrayList<>();
		thread = new Thread(new RequestHandlerClient(MethodClient.initListeDesMatchs, args, requestSender));
		thread.start();
	}
	
	
	

}
