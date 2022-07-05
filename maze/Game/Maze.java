package Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

/**
 * Maze class
 * @author jryan
 *
 */
public class Maze {
	/****************************************** INSTANCE VARIABLES ******************************************/
	
	//maze dimension
	public static int dim;
	//random generator
	private Random random = new Random();
	//maze
	public Tile[][] maze;
	//stack
	private Stack<Tile> stack = new Stack<>();

	/****************************************** CONSTRUCTORS  ******************************************/
	/**
	 * Constructor
	 * @param dim
	 */
	public Maze(int dim) {
		Maze.dim = dim;
		maze = new Tile[dim][dim];
		generateMaze();
	}
	
	/****************************************** MAZE GENERATION METHODS ******************************************/
	/**
	 * Generate the maze
	 */
	private void generateMaze() {
		stack.push(new Tile(0,0));
		while (!stack.empty()) {
			Tile curr = stack.pop();
			if (validTile(curr)) {
				maze[curr.y][curr.x] = curr;
				ArrayList<Tile> neighbors = findNeighbors(curr);
				randomlyAddNodesToStack(neighbors);
			}
		}
	}

	/**
	 * See if the tile is valid
	 * @param curr
	 * @return
	 */
	public boolean validTile(Tile curr) {
		int numNeighbors = 0;
		for (int y = curr.y-1; y <= curr.y+1; y++) {
			for (int x = curr.x-1; x <= curr.x+1; x++) {
				if (pointOnGrid(x, y) && pointNotNode(curr, x, y) && maze[y][x] != null) {
                    numNeighbors++;
                }
			}
		}
		return (numNeighbors < 3) && maze[curr.y][curr.x] == null;
	}

	/**
	 * Find neighbors of a tile
	 * @param t
	 * @return
	 */
	private ArrayList<Tile> findNeighbors(Tile t) {
		ArrayList<Tile> neighbors = new ArrayList<>();
		//look at tiles to the left, right, up and down - not corners
		for (int y = t.y-1; y <= t.y+1; y++) {
			for (int x = t.x-1; x <= t.x+1; x++) {
				 if (pointOnGrid(x, y) && pointNotCorner(t, x, y)
		                    && pointNotNode(t, x, y)) {
		                    neighbors.add(new Tile(x, y));
		                }
			}
		}
		return neighbors;
	}
	
	/**
	 * Add nodes
	 * @param tiles
	 */
	private void randomlyAddNodesToStack(ArrayList<Tile> tiles) {
        int targetIndex;
        while (!tiles.isEmpty()) {
            targetIndex = random.nextInt(tiles.size());
            stack.push(tiles.remove(targetIndex));
        }
    }
	
	/****************************************** VALIDATION METHODS ******************************************/
	public Boolean pointOnGrid(int x, int y) {
		return x >= 0 && y >= 0 && x < dim && y < dim;
	}

	private Boolean pointNotCorner(Tile t, int x, int y) {
		return (x == t.x || y == t.y);
	}

	private Boolean pointNotNode(Tile t, int x, int y) {
		return !(x == t.x && y == t.y);
	}
	
	/****************************************** OVERRIDEN METHODS ******************************************/
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		for (Tile[] t : maze) {
			str.append(Arrays.toString(t) + "\n");
		}
		return str.toString();
	}

}
