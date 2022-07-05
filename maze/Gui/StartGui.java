package Gui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class StartGui extends JFrame{

	private static final long serialVersionUID = 1L;

	private MainGui gui;

	public StartGui() {
		start();
	}

	public void start() {
		this.setSize(1000,500);
		this.setLayout(new GridLayout(3,3));
		
		//Maze text
		JLabel pnl = new JLabel();
		pnl.setText("                                                          MAZE GAME");

		//start button
		JButton start = new JButton("Start");
		start.setBackground(Color.GRAY);
		start.addActionListener((e)->{
			gui = new MainGui();
			gui.start();
			this.setVisible(false);
			gui.setVisible(true);
		});

		//add contents
		this.add(new JLabel());
		this.add(pnl);
		this.add(new JLabel());
		this.add(new JLabel());
		this.add(start);
		this.add(new JLabel());
		this.add(new JLabel());
		
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

}
