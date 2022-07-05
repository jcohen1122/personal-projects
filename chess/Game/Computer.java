package Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Tile.Tile;
import Colors.*;
import Pieces.Piece;

/**
 * Represents the opponent to the player
 * @author jryan
 *
 */
public class Computer {
	//game board
	public Board board;
	//computer pieces
	private ArrayList<Tile> pieces;
	//move as a string
	public String move;
	//captured pieces
	public ArrayList<Piece> capturedPieces;

	/**
	 * Initialization block
	 */
	{
		board = Game.PLAYER.board;
		pieces = getPieceTiles();
		capturedPieces = new ArrayList<>();
	}

	/**
	 * Return list of tiles that have black pieces
	 * @return
	 */
	public ArrayList<Tile> getPieceTiles() {
		ArrayList<Tile> pieces = new ArrayList<>();
		for (Tile[] row : board.getBoard()) {
			for (Tile tile : row) {
				if (tile.piece != null && tile.piece.getColor() == Colors.BLACK)
					pieces.add(tile);
			}
		}
		return pieces;
	}

	/**
	 * Retrieve a random black piece
	 * @return
	 */
	public Tile getRandomPiece() {
		Random random = new Random();

		//Random tile
		Tile rPiece = pieces.get(random.nextInt(pieces.size()));
		//Coordinates of tile
		List<Integer> cos = board.getCoordinates(rPiece);
		//Legal moves
		List<Tile> moves = board.getLegalMoves(cos.get(0),cos.get(1));

		//Make sure there are legal moves
		while (moves == null || moves.isEmpty()) {
			//Random piece
			rPiece = pieces.get(random.nextInt(pieces.size()));
			//Coordinates of random piece
			cos = board.getCoordinates(rPiece);
			//Legal moves of piece
			moves = board.getLegalMoves(cos.get(0),cos.get(1));
		}

		//now that there are certainly moves for the piece, return it
		return rPiece;
	}

	/**
	 * Returns a random move for a selected piece
	 * @return
	 */
	public Tile getRandomMove(Tile tile) {
		Random random = new Random();
		//Coordinates of tile
		List<Integer> cos = board.getCoordinates(tile);
		//Legal moves
		List<Tile> moves = board.getLegalMoves(cos.get(0),cos.get(1));

		//Return random move
		return moves.get(random.nextInt(moves.size()));
	}

	/**
	 * Heuristic for smart move
	 * Lower score is better - means closer to another tile
	 * @param tile
	 * @return
	 */
	public double heuristicScore(Tile tile) {
		//high starting # so that starting piece can override score
		double score = 10000;

		//don't calculate if tile doesn't have a piece
		if (tile.piece == null) {
			//return absurdly high # so that it won't get picked
			return 10000000;
		}

		/*****************calculate distance of end moves to opposite colored pieces*******************/
		//possible moves for tile
		List<Tile> moves = board.getLegalMoves(board.getCoordinates(tile).get(0), board.getCoordinates(tile).get(1));

		//iterate through possible moves
		for (Tile move : moves) {
			//Move coordinates
			ArrayList<Integer> mc = board.getCoordinates(move);

			//iterate through entire board, setting the lowest distance to score
			for (Tile[] tRow : board.getBoard()) {
				for (Tile t : tRow) {
					ArrayList<Integer> coordinate = board.getCoordinates(t);

					//add distance to other tile only if other tile is the opposite color
					if (t.piece != null && t.piece.getColor() != tile.piece.getColor()) {
						double temp = Math.sqrt(Math.pow(coordinate.get(0)-mc.get(0), 2) + Math.pow(coordinate.get(1)-mc.get(1), 2));
						//subtract original distance
						temp -=  Math.sqrt(Math.pow(board.getCoordinates(tile).get(0)-mc.get(0), 2) + Math.pow(board.getCoordinates(tile).get(1)-mc.get(1), 2));
						//take into consideration if a piece can capture it at this location -> add to make less desirable
						for (int i = 0; i < board.getBoard().length; i++) {
							for (int j = 0; j < board.getBoard()[i].length; j++) {
								List<Tile> boardMove = board.getLegalMoves(i, j);

								//add piece value
								if (boardMove != null && boardMove.contains(tile)) {
									temp += tile.piece.piece.value*10;
								}
							}
						}

						if (temp < score) {
							score = temp;

							//if score is 0, then that means it is on a piece --> subtract piece value so that higher value pieces are targeted first
							if (score == 0) {
								score -= board.getPiece(t).piece.value*2;
							}
						}
					}
				}
			}

		}

		return score;
	}

	/**
	 * Find starting tile
	 * @return
	 */
	public Tile heuristicStartMove() {
		//default starting piece
		Tile start = getRandomPiece();
		//default score
		double score = heuristicScore(start);
		//comparison value
		int value = 0;

		//see if any pieces are under threat
		boolean underThreat = false;
		for (Tile t : getPieceTiles()) {

			//pick piece if it is under threat of being captured
			for (Tile[] tRow : board.getBoard()) {
				for (Tile tile : tRow) {
					List<Tile> moves = board.getLegalMoves(board.getCoordinates(tile).get(0), board.getCoordinates(tile).get(1));
					//higher ranked pieces are prioritized first if under threat
					if (tile.piece != null && tile.piece.getColor() == Colors.WHITE 
							&& moves != null && moves.contains(t) && t.piece.piece.value > value) {
						value = t.piece.piece.value;
						start = t;
						underThreat = true;
						
						//in the case that a piece can capture the aggressor, pick that one instead
						//prioritize using lower value pieces first in case of sacrifice
						int lowVal = 10000;
						for (Tile tt : getPieceTiles()) {
							List<Tile> ttMoves = board.getLegalMoves(board.getCoordinates(tt).get(0), board.getCoordinates(tt).get(1));
							//check if possible moves for black piece contains white aggressor
							if (ttMoves != null && ttMoves.contains(tile) && tt.piece.piece.value < lowVal) {
								start = tt;
								lowVal = tt.piece.piece.value;
							}
						}
						
					}

				}
			}
		}

		//use heuristic if not under threat
		if (!underThreat) {
			for (Tile t : getPieceTiles()) {
				if (heuristicScore(t) < score && 
						!board.getLegalMoves(board.getCoordinates(t).get(0), 
								board.getCoordinates(t).get(1)).isEmpty()) {
					start = t;
					//make it less likely to move high ranked pieces
					score = heuristicScore(t) + t.piece.piece.value*2;
				}
			}
		}
		
		//make sure start piece has a possible move after heuristics
		//if not, resort to random
		if (board.getLegalMoves(board.getCoordinates(start).get(0), board.getCoordinates(start).get(1)).isEmpty()) {
			start = getRandomPiece();
		}

		return start;
	}

	/**
	 * Find smart ending tile
	 * @return
	 */
	public Tile heuristicEndMove(Tile tile) {
		Tile end = getRandomMove(tile);
		double score = heuristicScore(end);

		for (Tile move : board.getLegalMoves(board.getCoordinates(tile).get(0), board.getCoordinates(tile).get(1))) {
			if (heuristicScore(move) < score) {
				end = move;
				score = heuristicScore(move);
			}
		}

		return end;
	}

	/**
	 * Moves a computer piece
	 * @param from
	 * @param to
	 */
	public void movePiece(Tile from, Tile to) {

		//if captured piece
		if (to.piece != null && to.piece.getColor() == Colors.WHITE) {
			capturedPieces.add(to.piece);
		}

		board.movePiece(from.piece, to);
	}

}
