package Gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainGui extends JFrame{

	private static final long serialVersionUID = 1L;

	private MazeGui maze;

	public MainGui() {
		maze = new MazeGui();
		this.setSize(1000,500);
		this.setLayout(new BorderLayout());
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void start() {
		//add solve button
		JButton solve = new JButton("Solve");
		solve.addActionListener((e)->{
			maze.showSolution();
		});
		//Add reset button
		JButton reset = new JButton("Reset");
		reset.addActionListener((e)->{
			for (MazeButton[] bRow : maze.buttons) {
				for (MazeButton b : bRow) {
					b.resetColor();
				}
			}
			maze.redraw();
		});
		//Add exit button
		JButton exit = new JButton("Exit");
		exit.addActionListener((e)->{
			System.exit(0);
		});
		//Add new maze button
		JButton newMaze = new JButton("New Maze");
		newMaze.addActionListener((e)->{
			this.remove(maze.getContentPane());
			
			this.invalidate();
			this.revalidate();
			this.repaint();
			
			maze = new MazeGui();
			maze.requestFocus();
			
			this.add(maze.getContentPane());
			
			this.invalidate();
			this.revalidate();
			this.repaint();
		});
		
		//control bar
		JPanel controls = new JPanel();
		controls.setLayout(new FlowLayout());
		controls.add(solve);
		controls.add(reset);
		controls.add(newMaze);
		controls.add(exit);
		
		this.add(controls, BorderLayout.NORTH);
		this.add(maze.getContentPane(), BorderLayout.CENTER);
		//this.setVisible(true);
	}

}
