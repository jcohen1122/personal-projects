package Game;

import Pieces.*;
import Tile.Tile;
import Colors.*;

import java.util.*;

/**
 * Represents a generic player of the game
 * @author jryan
 *
 */
public class Player {

	//player board
	public Board board;

	//Pieces captured
	public List<Piece> piecesCaptured;

	//Scanner
	private Scanner in;

	/**
	 * Constructor
	 */
	public Player() {
		board = new Board();
		piecesCaptured = new ArrayList<>();
		in = new Scanner(System.in);

		//initialize player board
		board.createBoard();
		board.setDefaultPieces();
	}

	/**
	 * Prompt for new move
	 * @return
	 */
	public ArrayList<Integer> newMove() {
		//Wrap tile into move
		ArrayList<Integer> move = new ArrayList<>();

		//Get piece to move
		System.out.print("\nPiece to Move: \nRow:");
		int fromX = in.nextInt();

		//escape
		if (fromX == -1) {
			return null;
		}

		System.out.print("Col:");
		int fromY = in.nextInt();

		//Make sure it is an actual piece and that it is a white piece AND in bounds
		while (fromX < 0 || fromX > 7 || fromY < 0 || fromY > 7 || board.getPiece(fromX,fromY) == null || board.getPiece(fromX,fromY).getColor() != Colors.WHITE) {
			if (fromX < 0 || fromX > 7 || fromY < 0 || fromY > 7) {
				System.out.println("\u001b[31;1m"+"Error: Out of bounds"+board.getReset());
			} else if (board.getPiece(fromX,fromY) == null) {
				System.out.println("\u001b[31;1m"+"Error: Not a piece"+board.getReset());
			} else {
				System.out.println("\u001b[31;1m"+"Error: Cannot move opponent's piece"+board.getReset());
			}

			System.out.print("\nPiece to Move: \nRow:");
			fromX = in.nextInt();
			System.out.print("Col:");
			fromY = in.nextInt();
		}

		//Compile legal moves for that piece
		List<ArrayList<Integer>> legalMoves = compileLegalMoves(fromX,fromY);
		System.out.println("\nLegal Moves: " + legalMoves);
		
		while (legalMoves.isEmpty()) {
			System.out.println("\u001b[31;1m"+"Error: Cannot move this piece"+board.getReset());
			System.out.print("\nPiece to Move: \nRow:");
			fromX = in.nextInt();
			System.out.print("Col:");
			fromY = in.nextInt();
			
			legalMoves = compileLegalMoves(fromX,fromY);
			System.out.println("\nLegal Moves: " + legalMoves);
		}

		//Get tile to move to
		System.out.print("\nDestination Tile: \nRow:");
		int toX = in.nextInt();
		System.out.print("Col:");
		int toY = in.nextInt();

		move.add(toX);
		move.add(toY);

		//make sure move is legal
		while (!legalMoves.contains(move)) {
			System.out.println("\u001b[31;1m"+"Error: Illegal move at (" + toX + "," + toY + ")"+
					board.getReset());
			//Get tile to move to
			System.out.print("\nDestination Tile: \nRow:");
			toX = in.nextInt();
			System.out.print("Col:");
			toY = in.nextInt();

			//Wrap tile into move
			move = new ArrayList<>();
			move.add(toX);
			move.add(toY);
		}

		//add beginning tile after while loop to make sure contains works
		move.add(fromX);
		move.add(fromY);

		return move;
	}

	/**
	 * Move a piece from x,y to another x,y
	 * Checks for bounds
	 * @param fromX
	 * @param fromY
	 * @param toX
	 * @param toY
	 */
	public void movePiece(int fromX, int fromY, int toX, int toY) {
		if (fromX >= 8 || fromX < 0 || fromY >= 8 || fromY < 0) {
			System.out.println("\u001b[31;1m"+"Error: (" + fromX + "," + fromY +") is out of bounds"+board.getReset());
		} else {
			if (board.getPiece(fromX,fromY) != null && toX >= 0 && toX < 8 && toY >= 0 && toY < 8
					//checking if destination is a legal move
					&& board.getLegalMoves(fromX, fromY).contains(board.getBoard()[toX][toY])) {

				//check to see if captured piece
				if (board.getBoard()[toX][toY].piece == null) {
					board.movePiece(board.getPiece(fromX,fromY), board.getBoard()[toX][toY]);
				} else {
					piecesCaptured.add(board.getPiece(toX,toY));
					board.movePiece(board.getPiece(fromX,fromY), board.getBoard()[toX][toY]);
				}

			} else {
				System.out.println("\u001b[31;1m"+"Error: Illegal Move of " + board.getPiece(fromX,fromY) +
						" at (" + fromX + "," + fromY+") to (" + toX + "," + toY + ")"+board.getReset());
			}
		}
	}

	/**
	 * Returns an ArrayList of coordinate pairs for each legal move for a piece
	 * @param x
	 * @param y
	 * @return
	 */
	public List<ArrayList<Integer>> compileLegalMoves(int x, int y) {
		List<Tile> legalMoves = board.getLegalMoves(x,y);
		List<ArrayList<Integer>> compiledMoves = new ArrayList<>();

		for (Tile tile : legalMoves) {
			compiledMoves.add(board.getCoordinates(tile));
		}

		return compiledMoves;
	}

	/**
	 * Determines if the player has won
	 * @return
	 */
	public boolean win() {
		if (!board.kingAlive(Colors.BLACK) || board.countColor(Colors.BLACK) == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Determines if the player has lost
	 * @return
	 */
	public boolean lose() {
		if (!board.kingAlive(Colors.WHITE) || board.countColor(Colors.WHITE) == 0) {
			return true;
		}
		return false;
	}

}
