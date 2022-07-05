package Gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;
import Game.*;
import java.util.*;
import java.util.Map.Entry;

public class MazeGui extends JFrame{

	/****************************************** INSTANCE VARIABLES ******************************************/
	private static final long serialVersionUID = 1L;
	MazeButton[][] buttons;
	private static Maze maze;
	private int dim;
	private Player player;

	/****************************************** CONSTRUCTOR ******************************************/
	/**
	 * Constructor
	 */
	public MazeGui() {
		dim = Integer.valueOf(JOptionPane.showInputDialog(this, "Enter Dimension"));
		maze = new Maze(dim);
		buttons = new MazeButton[dim][dim];
		player = new Player(0,0);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setFocusable(true);
		this.requestFocus();
		
		//visual stuff
		this.setSize(1000,500);
		this.setLayout(new GridLayout(dim+1,dim));
		start();
	}

	/****************************************** METHODS******************************************/
	public void start() {
		addTiles();
		startPlayerActions();
		findEnd();
	}
	
	public Tile getEndTile() {
		for (MazeButton[] bRow : buttons) {
			for (MazeButton b : bRow) {
				if (b.color == Color.yellow) {
					return b.tile;
				}
			}
		}
		return null;
	}
	
	public List<Tile> solution() {
		//start
		List<Tile> q = new LinkedList<>();
		//linked so can traverse in order
		List<Tile> vis = new ArrayList<>();
		List<Tile> path = new ArrayList<>();
		int distance = findDistanceFromEnd(getEndTile().y,getEndTile().x,maze.maze[0][0],0, new HashSet<Tile>(),false);
		
		q.add(maze.maze[0][0]);
		path.add(maze.maze[0][0]);
		
		while(!q.isEmpty()) {
			Tile curr = q.remove(0);
			
			if (curr == null || vis.contains(curr)) {
				continue;
			}
			
			vis.add(curr);
			
			if (findDistanceFromEnd(getEndTile().y,getEndTile().x,curr,0, new HashSet<Tile>(),false) < distance) {
				path.add(curr);
				distance = findDistanceFromEnd(getEndTile().y,getEndTile().x,curr,0, new HashSet<Tile>(),false);
			}
			
			if (curr.x - 1 >= 0 && curr.y < dim && maze.maze[curr.y][curr.x-1] != null) {
				q.add(maze.maze[curr.y][curr.x-1]);
			}
			if (curr.x + 1 < dim && curr.y >= 0 && maze.maze[curr.y][curr.x+1] != null) {
				q.add(maze.maze[curr.y][curr.x+1]);
			}
			if (curr.x >= 0 && curr.y - 1>= 0 && maze.maze[curr.y-1][curr.x] != null) {
				q.add(maze.maze[curr.y-1][curr.x]);
			}
			if (curr.x >= 0 && curr.y + 1 < dim && maze.maze[curr.y+1][curr.x] != null) {
				q.add(maze.maze[curr.y+1][curr.x]);
			}
			
		}
		
		return path;
		
	}

	public void showSolution() {
		int speedChoice = JOptionPane.showOptionDialog(null, "Choose Speed", "Solve Maze", 0, JOptionPane.DEFAULT_OPTION, null, new String[]{"Slow","Medium","Fast","Extreme"},"Medium");
		int speed = 0;
		
		switch(speedChoice) {
		case 0:
			speed = 250;
			break;
		case 1:
			speed = 150;
			break;
		case 2:
			speed = 50;
			break;
		case 3:
			speed = 10;
		default:
			break;
		}
		
		final int sleepTime = speed;
		
		new Thread(() -> {
			for (Tile t : solution()) {
				for (MazeButton[] bRow : buttons) {
					for (MazeButton b : bRow) {
						if (b.tile != null && b.tile.x == t.x && b.tile.y == t.y && b.color == Color.WHITE) {
							b.setBackground(Color.CYAN);
							redraw();
							//wait to show progression
							try {
								Thread.sleep(sleepTime);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}).start();
	}
	
	private void addTiles() {
		for (int y = 0; y < dim; y++) {
			for (int x = 0; x < dim; x++) {
				buttons[y][x] = new MazeButton(maze.maze[y][x]);
				this.add(buttons[y][x]);
			}
		}
	}

	private void startPlayerActions() {
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {
				getGui().setFocusable(true);
				getGui().requestFocus();

			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

		this.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				MazeButton start = buttons[player.y][player.x];
				switch(arg0.getKeyCode()) {
				case KeyEvent.VK_A:
					if (maze.pointOnGrid(player.x-1, player.y) && 
							maze.maze[player.y][player.x-1] != null) {
						player.x--;
						buttons[player.y][player.x].setBackground(Color.RED);
						start.resetColor();
					}
					break;
				case KeyEvent.VK_S:
					if (maze.pointOnGrid(player.x, player.y+1) && 
							maze.maze[player.y+1][player.x] != null) {
						player.y++;
						buttons[player.y][player.x].setBackground(Color.RED);
						start.resetColor();
					}
					break;
				case KeyEvent.VK_D:
					if (maze.pointOnGrid(player.x+1, player.y) && 
							maze.maze[player.y][player.x+1] != null) {
						player.x++;
						buttons[player.y][player.x].setBackground(Color.RED);
						start.resetColor();
					}
					break;
				case KeyEvent.VK_W:
					if (maze.pointOnGrid(player.x, player.y-1) && 
							maze.maze[player.y-1][player.x] != null) {
						player.y--;
						buttons[player.y][player.x].setBackground(Color.RED);
						start.resetColor();
					}
					break;
				default:
					break;
				}

				redraw();

			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

		});
	
		for (MazeButton[] bRow : buttons) {
			for (MazeButton b : bRow) {
				b.addActionListener((e)-> {
					getGui().setFocusable(true);
					getGui().requestFocus();
				});
			}
		}
	}

	/**
	 * Find the furthest tile from the start and designate it as the end
	 */
	private void findEnd() {
		HashMap<Tile, Integer> ends = new HashMap<>();
		Stack<Tile> st = new Stack<>();
		Tile[][] m= maze.maze;
		Set<Tile> visited = new HashSet<>();

		st.push(m[0][0]);

		//int length = 0;
		while (!st.isEmpty()) {
			Tile curr = st.pop();

			if (curr != null && !visited.contains(curr)) {
				int length = findLength(0,0,curr,0,new HashSet<Tile>(), false);
				if (length > 0) {
					ends.put(curr, length);
				}
				visited.add(curr);
			} else {
				continue;
			}

			if (curr.x - 1 >= 0 && curr.y < dim && m[curr.y][curr.x-1] != null) {
				st.push(m[curr.y][curr.x-1]);
			}
			if (curr.x + 1 < dim && curr.y >= 0 && m[curr.y][curr.x+1] != null) {
				st.push(m[curr.y][curr.x+1]);
			}
			if (curr.x >= 0 && curr.y - 1>= 0 && m[curr.y-1][curr.x] != null) {
				st.push(m[curr.y-1][curr.x]);
			}
			if (curr.x >= 0 && curr.y + 1 < dim && m[curr.y+1][curr.x] != null) {
				st.push(m[curr.y+1][curr.x]);
			}
		}

		//pick the longest end
		int longest = 0;
		Tile end = null;
		for (Entry<Tile, Integer> entry : ends.entrySet()) {
			if (entry != null && entry.getValue() > longest) {
				longest = entry.getValue();
				end = entry.getKey();
			}
		}

		//set longest end to yellow
		buttons[end.y][end.x].color = Color.yellow;
		buttons[end.y][end.x].setBackground(Color.YELLOW);

		redraw();
	}

	/**
	 * Find the distance of a certain tile from the beginning
	 * @param y
	 * @param x
	 * @param curr
	 * @param length
	 * @param vis
	 * @param found
	 * @return
	 */
	private int findLength(int y, int x, Tile curr, int length, Set<Tile> vis, boolean found) {
		if (x < 0 || y < 0 || x >= dim ||
				y >= dim || maze.maze[y][x] == null || vis.contains(maze.maze[y][x])) {
			return 0;
		}
		if ((x == curr.x && y == curr.y) || found) {
			found = true;
			return length;
		}

		vis.add(maze.maze[y][x]);
		length++;

		return findLength(y-1,x, curr, length, vis, found) + 
				findLength(y+1,x, curr, length, vis, found) + 
				findLength(y,x-1, curr, length, vis, found) + 
				findLength(y,x+1, curr, length, vis, found);
	}

	private int findDistanceFromEnd(int y, int x, Tile curr, int length, Set<Tile> vis, boolean found) {
		if (x < 0 || y < 0 || x >= dim ||
				y >= dim || maze.maze[y][x] == null || vis.contains(maze.maze[y][x])) {
			return 0;
		}
		if ((x == curr.x && y == curr.y) || found) {
			found = true;
			return length;
		}

		vis.add(maze.maze[y][x]);
		length++;

		return findDistanceFromEnd(y-1,x, curr, length, vis, found) + 
				findDistanceFromEnd(y+1,x, curr, length, vis, found) + 
				findDistanceFromEnd(y,x-1, curr, length, vis, found) + 
				findDistanceFromEnd(y,x+1, curr, length, vis, found);
	}

	void redraw() {
		this.invalidate();
		this.validate();
		this.repaint();
	}

	private MazeGui getGui() {
		return this;
	}

}
