package Game;

import Tile.Tile;

import java.util.ArrayList;
import java.util.List;

import Colors.*;
import Moves.LegalMoveList;
import Pieces.*;

/**
 * Represents the chess board
 * @author Joshua Cohen
 *
 */
public class Board {

	//2D array of tiles making up the board
	private Tile[][] board;

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	public static final String ANSI_YELLOW = "\u001B[33m";

	/**
	 * Constructor
	 */
	public Board() {
		board = new Tile[8][8];
	}

	/**
	 * Create the chess board and fill it with empty tiles
	 */
	public void createBoard() {
		for (int i = 1; i < 9; i++){
			for (int j = 1; j < 9; j++){
				if (i % 2 == j % 2){
					board[i-1][j-1] = new Tile(Colors.WHITE);
				} else {
					board[i-1][j-1] = new Tile(Colors.BLACK);
				}
			}
		}
	}

	/**
	 * Return the chess board
	 * @return
	 */
	public Tile[][] getBoard() {
		return board;
	}

	/**
	 * Finds the coordinates of a given tile
	 * @param tile
	 * @return
	 */
	public ArrayList<Integer> getCoordinates(Tile tile) {
		ArrayList<Integer> coordinates = new ArrayList<>();
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (board[x][y] == tile) {
					coordinates.add(x);
					coordinates.add(y);
				}
			}
		}
		return coordinates;
	}

	/**
	 * Sets the default pieces on the board
	 */
	public void setDefaultPieces() {
		//BLACK TEAM
		//Rooks
		board[0][0].piece = new Piece(Pieces.ROOK, Colors.BLACK, board[0][0]);
		board[0][7].piece = new Piece(Pieces.ROOK, Colors.BLACK, board[0][7]);
		//Knights
		board[0][1].piece = new Piece(Pieces.KNIGHT, Colors.BLACK, board[0][1]);
		board[0][6].piece = new Piece(Pieces.KNIGHT, Colors.BLACK, board[0][6]);
		//Bishops
		board[0][2].piece = new Piece(Pieces.BISHOP, Colors.BLACK, board[0][2]);
		board[0][5].piece = new Piece(Pieces.BISHOP, Colors.BLACK, board[0][5]);
		//Royalty
		board[0][3].piece = new Piece(Pieces.KING, Colors.BLACK, board[0][3]);
		board[0][4].piece = new Piece(Pieces.QUEEN, Colors.BLACK, board[0][4]);
		//Pawns
		for (int i = 0; i < 8; i++) {
			board[1][i].piece = new Piece(Pieces.PAWN, Colors.BLACK, board[1][i]);
		}

		//WHITE TEAM
		//Rooks
		board[7][0].piece = new Piece(Pieces.ROOK, Colors.WHITE, board[7][0]);
		board[7][7].piece = new Piece(Pieces.ROOK, Colors.WHITE, board[7][7]);
		//Knights
		board[7][1].piece = new Piece(Pieces.KNIGHT, Colors.WHITE, board[7][1]);
		board[7][6].piece = new Piece(Pieces.KNIGHT, Colors.WHITE, board[7][6]);
		//Bishops
		board[7][2].piece = new Piece(Pieces.BISHOP, Colors.WHITE, board[7][2]);
		board[7][5].piece = new Piece(Pieces.BISHOP, Colors.WHITE, board[7][5]);
		//Royalty
		board[7][4].piece = new Piece(Pieces.KING, Colors.WHITE, board[7][4]);
		board[7][3].piece = new Piece(Pieces.QUEEN, Colors.WHITE, board[7][3]);
		//Pawns
		for (int i = 0; i < 8; i++) {
			board[6][i].piece = new Piece(Pieces.PAWN, Colors.WHITE, board[6][i]);
		}

	}

	/**
	 * Return the piece at a specific tile
	 * @param tile
	 * @return
	 */
	public Piece getPiece(Tile tile) {
		return tile.piece;
	}

	/**
	 * Return the piece at a specific coordinate
	 * @param x
	 * @param y
	 * @return
	 */
	public Piece getPiece(int x, int y) {
		return (x < 0 || y < 0 || x > 7 || y > 7) ? null : board[x][y].piece;
	}

	/**
	 * Move piece to a specific tile
	 * @param piece
	 * @param tile
	 */
	public void movePiece(Piece piece, Tile tile) {
		//set current tile piece to null
		piece.tile.piece = null;

		//set piece tile to new tile
		piece.tile = tile;

		//set new tile piece to piece
		tile.piece = piece;

		//add move to piece list
		piece.addMove(tile);
	}

	/**
	 * Generates all legal Tiles for the piece
	 * @return
	 */
	public List<Tile> getLegalMoves(int x, int y) {
		if (getPiece(x,y) != null) {
			return new LegalMoveList(getPiece(x,y), x, y, board).legalMoves;
		} else {
			return null;
		}
	}

	/**
	 * Counts the number of a certain color piece
	 * @return
	 */
	public int countColor(Colors color) {
		int num = 0;
		for (Tile[] row : board) {
			for (Tile tile : row) {
				if (tile.piece != null && tile.piece.getColor() == color) {
					num++;
				}
			}
		}

		return num;
	}

	/**
	 * Determines if the king of a certain color is alive
	 * @param color
	 * @return
	 */
	public boolean kingAlive(Colors color) {
		boolean alive = false;
		for (Tile[] row : board) {
			for (Tile tile : row) {
				if (tile.piece != null && tile.piece.piece == Pieces.KING && tile.piece.getColor() == color) {
					alive = true;
				}
			}
		}
		return alive;
	}

	/**
	 * Determines if the king of a certain color is in check
	 * @param color
	 * @return
	 */
	public Conditions kingInCheck() {
		for (int r = 0; r < board.length; r++) {
			for (int c = 0; c < board.length; c++) {

				//go thru legal moves for each piece that is not null
				if (board[r][c].piece != null) {
					//possible moves for the piece
					List<Tile> moves = getLegalMoves(r,c);

					//go thru each move
					for (Tile move : moves) {
						//if move is onto a king
						if (move.piece != null && move.piece.piece == Pieces.KING) {
							//if king does not have a move
							if (getLegalMoves(getCoordinates(move).get(0), getCoordinates(move).get(1)) == null) {
								//check color
								if (move.piece.getColor() == Colors.WHITE) {
									return Conditions.BLACK_CHECKMATE;
								}

								//see if any white pieces can take a black piece to protect it
								boolean safe = false;
								for (Tile[] tR : board) {
									for (Tile tt : tR) {
										List<Tile> ttMoves = getLegalMoves(getCoordinates(tt).get(0), getCoordinates(tt).get(1));
										//check if possible moves for white piece contains white aggressor
										if (ttMoves != null && ttMoves.contains(move)) {
											safe = true;
										}
									}
								}

								//only white checkmate if king has no moves and no other pieces can protect it
								if (!safe) {
									return Conditions.WHITE_CHECKMATE;
								}

							} else {
								if (move.piece.getColor() == Colors.WHITE) {
									return Conditions.WHITE_IN_CHECK;
								} else {
									return Conditions.BLACK_IN_CHECK;
								}
							}
						}
					}
				}
			}
		}

		return Conditions.CONTINUE;
	}

	/**
	 * Return ANSI_RESET color code
	 * @return
	 */
	public String getReset() {
		return ANSI_RESET;
	}

}
