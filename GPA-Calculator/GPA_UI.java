import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

/* User Interface to interact with GPA Calculator */
public class GPA_UI {
	/* Grid dimensions */
	int rows = 7;
	final int cols = 5;

	/* UI Components */
	JFrame frame = new JFrame();
	JFrame menuFrame = new JFrame();
	JFrame cumFrame = new JFrame();
	Component compTable[][];
	JButton calc, reset, load, save, menu, add, remove, prior;
	JTextField cumCredits = new JTextField(), cumGPA = new JTextField();

	/* Number courses */
	int numClasses = 6;

	/* Frame dimensions */
	int width = 550;
	int height = 300;

	/* Cumulative credits and GPA */
	int cumulativeCredits = 0;
	double cumulativeGPA = 0.0;

	public void start() {
		/* Remove all components - for add and remove course*/
		frame.getContentPane().removeAll();

		/* Frame */
		frame.setSize(width,height);
		frame.setLocationRelativeTo(null);
		BorderLayout b = new BorderLayout();
		frame.setLayout(b);
		frame.setResizable(false);

		/************** Add Components **************/

		/* North panel */
		JPanel nPanel = new JPanel();
		nPanel.setBackground(new Color(185, 220, 240));
		nPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		nPanel.setLayout(new BorderLayout());
		frame.add(nPanel, BorderLayout.NORTH);

		/* Label in north panel */
		JLabel nLbl = new JLabel("                  GPA Calculator");
		nLbl.setFont(new Font("Impact", Font. BOLD,25));
		nLbl.setHorizontalAlignment(JLabel.CENTER);
		nPanel.add(nLbl);

		/* Menu button */
		menu = new JButton("Menu");
		menu.addActionListener(new addMenuFunctionality());
		nPanel.add(menu, BorderLayout.EAST);

		/* Add middle panel */
		JPanel mPanel = new JPanel(new GridLayout(rows,cols));
		frame.add(mPanel, BorderLayout.CENTER);

		/* Add middle grid */
		compTable = new Component[rows][cols]; /* Table of components in the grid */
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {

				if (i == 0) {
					JLabel lbl;
					switch(j) {
					case 1:
						lbl = new JLabel("    Name");
						lbl.setFont(new Font("Courier", Font. BOLD,15));
						compTable[i][j] = lbl;
						break;
					case 2:
						lbl = new JLabel("   Credits");
						lbl.setFont(new Font("Courier", Font. BOLD,15));
						compTable[i][j] = lbl;
						break;
					case 3:
						lbl = new JLabel("    Grade");
						lbl.setFont(new Font("Courier", Font. BOLD,15));
						compTable[i][j] = lbl;
						break;
					case 4:
						compTable[i][j] = new JPanel();
						break;
					default:
						compTable[i][j] = new JLabel(" ");
						break;
					}
				} else if (j == 0) {
					JLabel lbl = new JLabel("   Course " + i);
					lbl.setFont(new Font("Courier", Font. BOLD,15));
					compTable[i][j] = lbl;
				} else {
					if (j == 4) {
						compTable[i][j] = new JPanel();
					} else {
						JTextField dataField = new JTextField();
						dataField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
						compTable[i][j] = dataField;
					}
				}

				mPanel.add(compTable[i][j]);
			}
		}

		/* Add bottom panel */
		JPanel bPanel = new JPanel(new FlowLayout());
		frame.add(bPanel, BorderLayout.SOUTH);

		/* Calculate button */
		calc = new JButton("Calculate");
		calc.addActionListener(new addCalculateFunctionality());
		bPanel.add(calc);

		/* Exit and visible */
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private class addMenuFunctionality implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			/* Remove all components - for add and remove course*/
			menuFrame.getContentPane().removeAll();

			menuFrame.setSize(300,175);
			BorderLayout b = new BorderLayout();
			menuFrame.setLayout(b);
			menuFrame.setResizable(false);
			menuFrame.setLocationRelativeTo(menu);

			/****** Add Components ******/

			/* North panel */
			JPanel nPanel = new JPanel();
			nPanel.setBackground(new Color(185, 220, 240));
			nPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			nPanel.setLayout(new BorderLayout());
			menuFrame.add(nPanel, BorderLayout.NORTH);

			/* Label in north panel */
			JLabel nLbl = new JLabel("Menu");
			nLbl.setFont(new Font("Impact", Font. BOLD, 18));
			nLbl.setHorizontalAlignment(JLabel.CENTER);
			nPanel.add(nLbl);

			/* Center Panel */
			JPanel mPanel = new JPanel(new GridLayout(3,2));
			menuFrame.add(mPanel);

			/* Load Button */
			load = new JButton("Load");
			load.addActionListener(new addLoadFunctionality());
			mPanel.add(load);

			/* Save button */
			save = new JButton("Save");
			save.addActionListener(new addSaveFunctionality());
			mPanel.add(save);

			/* Add course button */
			add = new JButton("Add Course");
			add.addActionListener(new addCourseFunctionality());
			mPanel.add(add);

			/* Remove course button */
			remove = new JButton("Remove Course");
			remove.addActionListener(new addRemoveCourseFunctionality());
			mPanel.add(remove);

			/* Previous cumulative credits */
			prior = new JButton("Cumulative GPA");
			prior.addActionListener(new addCumulativeFunctionality());
			mPanel.add(prior);

			/* Reset button */
			reset = new JButton("Reset");
			reset.addActionListener(new addResetFunctionality());
			mPanel.add(reset);

			/* Visible */
			menuFrame.setVisible(true);
		}

	}

	/* Allow user to enter previous credits attempted and cumulative GPA */
	private class addCumulativeFunctionality implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			/* Remove all components */
			cumFrame.getContentPane().removeAll();

			cumFrame.setSize(300,125);
			BorderLayout b = new BorderLayout();
			cumFrame.setLayout(b);
			cumFrame.setResizable(false);
			cumFrame.setLocationRelativeTo(menuFrame);

			/*** Components ***/

			/* North panel */
			JPanel nPanel = new JPanel();
			nPanel.setBackground(new Color(185, 220, 240));
			nPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			nPanel.setLayout(new BorderLayout());
			cumFrame.add(nPanel, BorderLayout.NORTH);

			/* Label in north panel */
			JLabel nLbl = new JLabel("Cumulative GPA");
			nLbl.setFont(new Font("Impact", Font. BOLD, 18));
			nLbl.setHorizontalAlignment(JLabel.CENTER);
			nPanel.add(nLbl);

			/* Center Panel */
			JPanel mPanel = new JPanel(new GridLayout(2,2));
			cumFrame.add(mPanel);

			/* Cumulative Attempted Credits */
			JLabel credLbl = new JLabel("Cumulative Credits");
			credLbl.setFont(new Font("Courier", Font. BOLD, 12));

			mPanel.add(credLbl);
			mPanel.add(cumCredits);

			/* Cumulative GPA */
			JLabel gpaLbl = new JLabel("Cumulative GPA");
			gpaLbl.setFont(new Font("Courier", Font. BOLD, 12));

			mPanel.add(gpaLbl);
			mPanel.add(cumGPA);

			/* Visible */
			cumFrame.setVisible(true);
		}

	}

	/* Remove course row */
	private class addRemoveCourseFunctionality implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (numClasses - 1 >= 1) {
				rows--;
				numClasses--;
				menuFrame.setVisible(false);
				frame.setSize(width, (height-=25));
				start();
			}
		}

	}

	/* Add course row */
	private class addCourseFunctionality implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			rows++;
			numClasses++;
			menuFrame.setVisible(false);
			frame.setSize(width, (height+=25));
			start();
		}
	}

	/* Reset all entries */
	private class addResetFunctionality implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			for (int i = 0; i < compTable.length; i++) {
				for (int j = 0; j < compTable[i].length; j++) {
					if (compTable[i][j] instanceof JTextField) {
						((JTextField)compTable[i][j]).setText(null);
					}
				}
			}
			menuFrame.setVisible(false);
			cumCredits.setText(null);
			cumGPA.setText(null);
			Calculate.clear();
		}

	}

	/* Allow a button to save to a file */
	private class addSaveFunctionality implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Specify a file to save");   

			int userSelection = fileChooser.showSaveDialog(frame);

			if (userSelection == JFileChooser.APPROVE_OPTION) {
				File fileToSave = fileChooser.getSelectedFile();
				try {
					Calculate.save(fileToSave.getPath());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(frame, "Saved to " + fileToSave.getPath());
			}
			menuFrame.setVisible(false);
		}

	}

	/* Allow a button to load to a file */
	private class addLoadFunctionality implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			/** Load in File **/
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Specify a file to load");   

			int userSelection = fileChooser.showOpenDialog(fileChooser);

			if (userSelection == JFileChooser.APPROVE_OPTION) {
				File fileToLoad = fileChooser.getSelectedFile();
				try {
					Calculate.load(fileToLoad.getPath());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(frame, "Loaded " + fileToLoad.getPath());
			}

			/** Update UI **/
			int i = 1;
			ArrayList<Entry> objEntries = Calculate.getEntries();
			for (Entry ent : objEntries) {
				if (i < compTable.length) {
					((JTextField)compTable[i][1]).setText(ent.course);
					((JTextField)compTable[i][2]).setText(ent.credits+"");
					((JTextField)compTable[i][3]).setText(ent.grade);
					i++;
				} else {
					JOptionPane.showMessageDialog(frame, "Too Few Rows: Add a Course");
					Calculate.clear();
					reset.doClick();
					break;
				}
			}
			menuFrame.setVisible(false);
		}

	}

	/* Allow a button to calculate GPA on click */
	private class addCalculateFunctionality implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			/* Load all inputs */
			String inputs[][] = new String[rows - 1][cols - 2];
			double gpa;

			for (int i = 0; i < rows - 1; i++) {
				for (int j = 0; j < cols - 2; j++) {
					if (compTable[i+1][j+1] instanceof JTextField) {
						inputs[i][j] = ((JTextField) compTable[i+1][j+1]).getText();				
					}
				}
			}

			try {
				cumulativeCredits = Integer.valueOf(cumCredits.getText());
				cumulativeGPA = Double.valueOf(cumGPA.getText());
			} catch (Exception exception) {
				cumCredits.setText(null);
				cumGPA.setText(null);
				cumulativeCredits = 0;
				cumulativeGPA = 0.0;
			}
			gpa = calculate(inputs, cumulativeCredits, cumulativeGPA);
			JOptionPane.showMessageDialog(frame, "GPA: " + gpa);
			menuFrame.setVisible(false);
		}

		/* Calculate GPA */
		private double calculate(String inputs[][], int cCreds, double cGPA) {
			String entry = "";
			double gpa;

			/* Push all entries into list */
			for (int i = 0; i < inputs.length; i++) {
				try {
					entry = Calculate.format(inputs[i][0], Integer.valueOf(inputs[i][1]),
							inputs[i][2]);
				} catch (Exception e) {

				}
				Calculate.push(entry);
			}

			/* Calculate GPA */
			try {
				gpa = Calculate.calculateCumulativeGPA(cumulativeCredits, cumulativeGPA);
				return gpa;
			} catch (Exception e) {
				Calculate.clear();
			}

			return 0;
		}

	}
}