package Moves;

import java.util.ArrayList;
import java.util.List;

import Pieces.Piece;
import Pieces.Pieces;
import Tile.Tile;
import Colors.*;

public class LegalMoveList {

	//piece
	public Pieces piece;
	public Piece WrappedPiece;

	//Legal moves
	public List<Tile> legalMoves;

	//coordinates
	int x,y;

	//board
	Tile[][] board;

	/**
	 * Constructor
	 * @param piece
	 */
	public LegalMoveList(Piece piece, int x, int y, Tile[][] board) {
		this.x = x;
		this.y = y;
		this.board = board;

		this.piece = piece.piece;
		WrappedPiece = piece;
		legalMoves = new ArrayList<>();
		generateLegalMoves();
	}

	/**
	 * Generates legal moves based on piece type and adds it to the ArrayList
	 */
	public void generateLegalMoves() {
		if (WrappedPiece.getColor() == Colors.WHITE) {
			switch(piece) {
			case PAWN:
				//first move - 2 spaces
				if (x == 6 && !board[x-2][y].containsPiece() && !board[x-1][y].containsPiece()) {
					legalMoves.add(board[x-2][y]);
				}//general move forward 1 space 
				if (x > 0 && y >= 0 && !board[x-1][y].containsPiece()) {
					legalMoves.add(board[x-1][y]);
				}//diagonal if there is a piece there - top left
				if (x > 0 && y > 0 && board[x-1][y-1].containsPiece() && 
						board[x-1][y-1].piece.getColor() != Colors.WHITE) {
					legalMoves.add(board[x-1][y-1]);
				} 
				//top right
				if (x > 0 && y < 7 && board[x-1][y+1].containsPiece() && 
						board[x-1][y+1].piece.getColor() != Colors.WHITE) {
					legalMoves.add(board[x-1][y+1]);
				}
				break;
			case BISHOP:
				//left up
				int j = y;
				for (int i = x - 1; i >= 0; i--) {
					if (j - 1 >= 0) {
						if (board[i][j-1].containsPiece()) {
							//if there is a piece that is not white
							if (board[i][j-1].piece.getColor() != Colors.WHITE) {
								legalMoves.add(board[i][--j]);
								break;
							} else {
								break;
							}
						} else {
							legalMoves.add(board[i][--j]);
						}
					}
				}
				//right down
				j = y;
				for (int i = x + 1; i < 8; i++) {
					if (j + 1 < 8 ) {
						if (board[i][j+1].containsPiece()) {
							//if there is a piece that is not white
							if (board[i][j+1].piece.getColor() != Colors.WHITE) {
								legalMoves.add(board[i][++j]);
								break;
							} else {
								break;
							}
						} else {
							legalMoves.add(board[i][++j]);
						}
					}
				}
				//right up
				j = y;
				for (int i = x - 1; i >= 0; i--) {
					if (j + 1 < 8) {
						if (board[i][j+1].containsPiece()) {
							//if there is a piece that is not white
							if (board[i][j+1].piece.getColor() != Colors.WHITE) {
								legalMoves.add(board[i][++j]);
								break;
							} else {
								break;
							}
						} else {
							legalMoves.add(board[i][++j]);
						}
					}
				}
				//left down
				j = y;
				for (int i = x + 1; i < 8; i++) {
					if (j - 1 >=0 ) {
						if (board[i][j-1].containsPiece()) {
							//if there is a piece that is not white
							if (board[i][j-1].piece.getColor() != Colors.WHITE) {
								legalMoves.add(board[i][--j]);
								break;
							} else {
								break;
							}
						} else {
							legalMoves.add(board[i][--j]);
						}
					}
				}
				break;
			case KING:
				//up 1
				if (x > 0) {
					if (board[x-1][y].containsPiece()) {
						if (board[x-1][y].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[x-1][y]);
						}
					} else {
						legalMoves.add(board[x-1][y]);
					}
				}
				//down 1
				if (x < 7) {
					if (board[x+1][y].containsPiece()) {
						if (board[x+1][y].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[x+1][y]);
						}
					} else {
						legalMoves.add(board[x+1][y]);
					}
				}
				//left 1
				if (y > 0) {
					if (board[x][y-1].containsPiece()) {
						if (board[x][y-1].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[x][y-1]);
						}
					} else {
						legalMoves.add(board[x][y-1]);
					}
				}
				//right 1
				if (y < 7) {
					if (board[x][y+1].containsPiece()) {
						if (board[x][y+1].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[x][y+1]);
						}
					} else {
						legalMoves.add(board[x][y+1]);
					}
				}
				//up 1 left 1
				if (x > 0 && y > 0) {
					if (board[x-1][y-1].containsPiece()) {
						if (board[x-1][y-1].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[x-1][y-1]);
						}
					} else {
						legalMoves.add(board[x-1][y-1]);
					}
				}
				//up 1 right 1
				if (x > 0 && y < 7) {
					if (board[x-1][y+1].containsPiece()) {
						if (board[x-1][y+1].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[x-1][y+1]);
						}
					} else {
						legalMoves.add(board[x-1][y+1]);
					}
				}
				//down 1 left 1
				if (x < 7 && y > 0) {
					if (board[x+1][y-1].containsPiece()) {
						if (board[x+1][y-1].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[x+1][y-1]);
						}
					} else {
						legalMoves.add(board[x+1][y-1]);
					}
				}
				//down 1 right 1
				if (x < 7 && y < 7) {
					if (board[x+1][y+1].containsPiece()) {
						if (board[x+1][y+1].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[x+1][y+1]);
						}
					} else {
						legalMoves.add(board[x+1][y+1]);
					}
				}
				break;
			case KNIGHT:
				//up 2 left 1
				if (x > 1 && y > 0) {
					if (board[x-2][y-1].containsPiece()) {
						if (board[x-2][y-1].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[x-2][y-1]);
						}
					} else {
						legalMoves.add(board[x-2][y-1]);
					}
				}
				//up 2 right 1
				if (x > 1 && y < 7) {
					if (board[x-2][y+1].containsPiece()) {
						if (board[x-2][y+1].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[x-2][y+1]);
						}
					} else {
						legalMoves.add(board[x-2][y+1]);
					}
				}
				//down 2 left 1
				if (x < 6 && y > 0) {
					if (board[x+2][y-1].containsPiece()) {
						if (board[x+2][y-1].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[x+2][y-1]);
						}
					} else {
						legalMoves.add(board[x+2][y-1]);
					}
				}
				//down 2 right 1
				if (x < 6 && y < 7) {
					if (board[x+2][y+1].containsPiece()) {
						if (board[x+2][y+1].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[x+2][y+1]);
						}
					} else {
						legalMoves.add(board[x+2][y+1]);
					}
				}
				//up 1 left 2
				if (x > 0 && y > 1) {
					if (board[x-1][y-2].containsPiece()) {
						if (board[x-1][y-2].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[x-1][y-2]);
						}
					} else {
						legalMoves.add(board[x-1][y-2]);
					}
				}
				//up 1 right 2
				if (x > 0 && y < 6) {
					if (board[x-1][y+2].containsPiece()) {
						if (board[x-1][y+2].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[x-1][y+2]);
						}
					} else {
						legalMoves.add(board[x-1][y+2]);
					}
				}
				//down 1 left 2
				if (x < 7 && y > 1) {
					if (board[x+1][y-2].containsPiece()) {
						if (board[x+1][y-2].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[x+1][y-2]);
						}
					} else {
						legalMoves.add(board[x+1][y-2]);
					}
				}
				//down 1 right 2
				if (x < 7 && y < 6) {
					if (board[x+1][y+2].containsPiece()) {
						if (board[x+1][y+2].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[x+1][y+2]);
						}
					} else {
						legalMoves.add(board[x+1][y+2]);
					}
				}
				break;
			case QUEEN:
				//left up
				//j already instantiated within Bishop case
				j = y;
				for (int i = x - 1; i >= 0; i--) {
					if (j - 1 >= 0) {
						if (board[i][j-1].containsPiece()) {
							//if there is a piece that is not white
							if (board[i][j-1].piece.getColor() != Colors.WHITE) {
								legalMoves.add(board[i][--j]);
								break;
							} else {
								break;
							}
						} else {
							legalMoves.add(board[i][--j]);
						}
					}
				}
				//right down
				j = y;
				for (int i = x + 1; i < 8; i++) {
					if (j + 1 < 8 ) {
						if (board[i][j+1].containsPiece()) {
							//if there is a piece that is not white
							if (board[i][j+1].piece.getColor() != Colors.WHITE) {
								legalMoves.add(board[i][++j]);
								break;
							} else {
								break;
							}
						} else {
							legalMoves.add(board[i][++j]);
						}
					}
				}
				//right up
				j = y;
				for (int i = x - 1; i >= 0; i--) {
					if (j + 1 < 8) {
						if (board[i][j+1].containsPiece()) {
							//if there is a piece that is not white
							if (board[i][j+1].piece.getColor() != Colors.WHITE) {
								legalMoves.add(board[i][++j]);
								break;
							} else {
								break;
							}
						} else {
							legalMoves.add(board[i][++j]);
						}
					}
				}
				//left down
				j = y;
				for (int i = x + 1; i < 8; i++) {
					if (j - 1 >=0 ) {
						if (board[i][j-1].containsPiece()) {
							//if there is a piece that is not white
							if (board[i][j-1].piece.getColor() != Colors.WHITE) {
								legalMoves.add(board[i][--j]);
								break;
							} else {
								break;
							}
						} else {
							legalMoves.add(board[i][--j]);
						}
					}
				}
				//up
				for (int i = x - 1; i >= 0; i--) {
					if (board[i][y].containsPiece()) {
						if (board[i][y].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[i][y]);
							break;
						} else {
							break;
						}
					} else {
						legalMoves.add(board[i][y]);
					}
				}
				//down
				for (int i = x + 1; i < 8; i++) {
					if (board[i][y].containsPiece()) {
						if (board[i][y].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[i][y]);
							break;
						} else {
							break;
						}
					} else {
						legalMoves.add(board[i][y]);
					}
				}
				//left
				for (int i = y - 1; i >= 0; i--) {
					if (board[x][i].containsPiece()) {
						if (board[x][i].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[x][i]);
							break;
						} else {
							break;
						}
					} else {
						legalMoves.add(board[x][i]);
					}
				}
				//right
				for (int i = y + 1; i < 8; i++) {
					if (board[x][i].containsPiece()) {
						if (board[x][i].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[x][i]);
							break;
						} else {
							break;
						}
					} else {
						legalMoves.add(board[x][i]);
					}
				}
				break;
			case ROOK:
				//up
				for (int i = x - 1; i >= 0; i--) {
					if (board[i][y].containsPiece()) {
						if (board[i][y].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[i][y]);
							break;
						} else {
							break;
						}
					} else {
						legalMoves.add(board[i][y]);
					}
				}
				//down
				for (int i = x + 1; i < 8; i++) {
					if (board[i][y].containsPiece()) {
						if (board[i][y].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[i][y]);
							break;
						} else {
							break;
						}
					} else {
						legalMoves.add(board[i][y]);
					}
				}
				//left
				for (int i = y - 1; i >= 0; i--) {
					if (board[x][i].containsPiece()) {
						if (board[x][i].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[x][i]);
							break;
						} else {
							break;
						}
					} else {
						legalMoves.add(board[x][i]);
					}
				}
				//right
				for (int i = y + 1; i < 8; i++) {
					if (board[x][i].containsPiece()) {
						if (board[x][i].piece.getColor() != Colors.WHITE) {
							legalMoves.add(board[x][i]);
							break;
						} else {
							break;
						}
					} else {
						legalMoves.add(board[x][i]);
					}
				}
				break;
			default:
				break;
			}
		} else { //for black team
			switch(piece) {
			case PAWN:
				//first move - 2 spaces
				if (x == 1 && !board[x+2][y].containsPiece() && !board[x+1][y].containsPiece()) {
					legalMoves.add(board[x+2][y]);
				}//general move forward 1 space 
				if (x < 7 && !board[x+1][y].containsPiece()) {
					legalMoves.add(board[x+1][y]);
				}//diagonal if there is a piece there - bottom left
				if (x < 7 && y > 0 && board[x+1][y-1].containsPiece() && 
						board[x+1][y-1].piece.getColor() != Colors.BLACK) {
					legalMoves.add(board[x+1][y-1]);
				} 
				//bottom right
				if (x < 7 && y < 7 && board[x+1][y+1].containsPiece() && 
						board[x+1][y+1].piece.getColor() != Colors.BLACK) {
					legalMoves.add(board[x+1][y+1]);
				}
				break;
			case BISHOP:
				//left up
				int j = y;
				for (int i = x - 1; i >= 0; i--) {
					if (j - 1 >= 0) {
						if (board[i][j-1].containsPiece()) {
							//if there is a piece that is not white
							if (board[i][j-1].piece.getColor() != Colors.BLACK) {
								legalMoves.add(board[i][--j]);
								break;
							} else {
								break;
							}
						} else {
							legalMoves.add(board[i][--j]);
						}
					}
				}
				//right down
				j = y;
				for (int i = x + 1; i < 8; i++) {
					if (j + 1 < 8 ) {
						if (board[i][j+1].containsPiece()) {
							//if there is a piece that is not white
							if (board[i][j+1].piece.getColor() != Colors.BLACK) {
								legalMoves.add(board[i][++j]);
								break;
							} else {
								break;
							}
						} else {
							legalMoves.add(board[i][++j]);
						}
					}
				}
				//right up
				j = y;
				for (int i = x - 1; i >= 0; i--) {
					if (j + 1 < 8) {
						if (board[i][j+1].containsPiece()) {
							//if there is a piece that is not white
							if (board[i][j+1].piece.getColor() != Colors.BLACK) {
								legalMoves.add(board[i][++j]);
								break;
							} else {
								break;
							}
						} else {
							legalMoves.add(board[i][++j]);
						}
					}
				}
				//left down
				j = y;
				for (int i = x + 1; i < 8; i++) {
					if (j - 1 >=0 ) {
						if (board[i][j-1].containsPiece()) {
							//if there is a piece that is not white
							if (board[i][j-1].piece.getColor() != Colors.BLACK) {
								legalMoves.add(board[i][--j]);
								break;
							} else {
								break;
							}
						} else {
							legalMoves.add(board[i][--j]);
						}
					}
				}
				break;
			case KING:
				//up 1
				if (x > 0) {
					if (board[x-1][y].containsPiece()) {
						if (board[x-1][y].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[x-1][y]);
						}
					} else {
						legalMoves.add(board[x-1][y]);
					}
				}
				//down 1
				if (x < 7) {
					if (board[x+1][y].containsPiece()) {
						if (board[x+1][y].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[x+1][y]);
						}
					} else {
						legalMoves.add(board[x+1][y]);
					}
				}
				//left 1
				if (y > 0) {
					if (board[x][y-1].containsPiece()) {
						if (board[x][y-1].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[x][y-1]);
						}
					} else {
						legalMoves.add(board[x][y-1]);
					}
				}
				//right 1
				if (y < 7) {
					if (board[x][y+1].containsPiece()) {
						if (board[x][y+1].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[x][y+1]);
						}
					} else {
						legalMoves.add(board[x][y+1]);
					}
				}
				//up 1 left 1
				if (x > 0 && y > 0) {
					if (board[x-1][y-1].containsPiece()) {
						if (board[x-1][y-1].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[x-1][y-1]);
						}
					} else {
						legalMoves.add(board[x-1][y-1]);
					}
				}
				//up 1 right 1
				if (x > 0 && y < 7) {
					if (board[x-1][y+1].containsPiece()) {
						if (board[x-1][y+1].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[x-1][y+1]);
						}
					} else {
						legalMoves.add(board[x-1][y+1]);
					}
				}
				//down 1 left 1
				if (x < 7 && y > 0) {
					if (board[x+1][y-1].containsPiece()) {
						if (board[x+1][y-1].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[x+1][y-1]);
						}
					} else {
						legalMoves.add(board[x+1][y-1]);
					}
				}
				//down 1 right 1
				if (x < 7 && y < 7) {
					if (board[x+1][y+1].containsPiece()) {
						if (board[x+1][y+1].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[x+1][y+1]);
						}
					} else {
						legalMoves.add(board[x+1][y+1]);
					}
				}
				break;
			case KNIGHT:
				//up 2 left 1
				if (x > 1 && y > 0) {
					if (board[x-2][y-1].containsPiece()) {
						if (board[x-2][y-1].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[x-2][y-1]);
						}
					} else {
						legalMoves.add(board[x-2][y-1]);
					}
				}
				//up 2 right 1
				if (x > 1 && y < 7) {
					if (board[x-2][y+1].containsPiece()) {
						if (board[x-2][y+1].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[x-2][y+1]);
						}
					} else {
						legalMoves.add(board[x-2][y+1]);
					}
				}
				//down 2 left 1
				if (x < 6 && y > 0) {
					if (board[x+2][y-1].containsPiece()) {
						if (board[x+2][y-1].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[x+2][y-1]);
						}
					} else {
						legalMoves.add(board[x+2][y-1]);
					}
				}
				//down 2 right 1
				if (x < 6 && y < 7) {
					if (board[x+2][y+1].containsPiece()) {
						if (board[x+2][y+1].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[x+2][y+1]);
						}
					} else {
						legalMoves.add(board[x+2][y+1]);
					}
				}
				//up 1 left 2
				if (x > 0 && y > 1) {
					if (board[x-1][y-2].containsPiece()) {
						if (board[x-1][y-2].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[x-1][y-2]);
						}
					} else {
						legalMoves.add(board[x-1][y-2]);
					}
				}
				//up 1 right 2
				if (x > 0 && y < 6) {
					if (board[x-1][y+2].containsPiece()) {
						if (board[x-1][y+2].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[x-1][y+2]);
						}
					} else {
						legalMoves.add(board[x-1][y+2]);
					}
				}
				//down 1 left 2
				if (x < 7 && y > 1) {
					if (board[x+1][y-2].containsPiece()) {
						if (board[x+1][y-2].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[x+1][y-2]);
						}
					} else {
						legalMoves.add(board[x+1][y-2]);
					}
				}
				//down 1 right 2
				if (x < 7 && y < 6) {
					if (board[x+1][y+2].containsPiece()) {
						if (board[x+1][y+2].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[x+1][y+2]);
						}
					} else {
						legalMoves.add(board[x+1][y+2]);
					}
				}
				break;
			case QUEEN:
				//left up
				//j already instantiated within Bishop case
				j = y;
				for (int i = x - 1; i >= 0; i--) {
					if (j - 1 >= 0) {
						if (board[i][j-1].containsPiece()) {
							//if there is a piece that is not white
							if (board[i][j-1].piece.getColor() != Colors.BLACK) {
								legalMoves.add(board[i][--j]);
								break;
							} else {
								break;
							}
						} else {
							legalMoves.add(board[i][--j]);
						}
					}
				}
				//right down
				j = y;
				for (int i = x + 1; i < 8; i++) {
					if (j + 1 < 8 ) {
						if (board[i][j+1].containsPiece()) {
							//if there is a piece that is not white
							if (board[i][j+1].piece.getColor() != Colors.BLACK) {
								legalMoves.add(board[i][++j]);
								break;
							} else {
								break;
							}
						} else {
							legalMoves.add(board[i][++j]);
						}
					}
				}
				//right up
				j = y;
				for (int i = x - 1; i >= 0; i--) {
					if (j + 1 < 8) {
						if (board[i][j+1].containsPiece()) {
							//if there is a piece that is not white
							if (board[i][j+1].piece.getColor() != Colors.BLACK) {
								legalMoves.add(board[i][++j]);
								break;
							} else {
								break;
							}
						} else {
							legalMoves.add(board[i][++j]);
						}
					}
				}
				//left down
				j = y;
				for (int i = x + 1; i < 8; i++) {
					if (j - 1 >=0 ) {
						if (board[i][j-1].containsPiece()) {
							//if there is a piece that is not white
							if (board[i][j-1].piece.getColor() != Colors.BLACK) {
								legalMoves.add(board[i][--j]);
								break;
							} else {
								break;
							}
						} else {
							legalMoves.add(board[i][--j]);
						}
					}
				}
				//up
				for (int i = x - 1; i >= 0; i--) {
					if (board[i][y].containsPiece()) {
						if (board[i][y].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[i][y]);
							break;
						} else {
							break;
						}
					} else {
						legalMoves.add(board[i][y]);
					}
				}
				//down
				for (int i = x + 1; i < 8; i++) {
					if (board[i][y].containsPiece()) {
						if (board[i][y].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[i][y]);
							break;
						} else {
							break;
						}
					} else {
						legalMoves.add(board[i][y]);
					}
				}
				//left
				for (int i = y - 1; i >= 0; i--) {
					if (board[x][i].containsPiece()) {
						if (board[x][i].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[x][i]);
							break;
						} else {
							break;
						}
					} else {
						legalMoves.add(board[x][i]);
					}
				}
				//right
				for (int i = y + 1; i < 8; i++) {
					if (board[x][i].containsPiece()) {
						if (board[x][i].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[x][i]);
							break;
						} else {
							break;
						}
					} else {
						legalMoves.add(board[x][i]);
					}
				}
				break;
			case ROOK:
				//up
				for (int i = x - 1; i >= 0; i--) {
					if (board[i][y].containsPiece()) {
						if (board[i][y].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[i][y]);
							break;
						} else {
							break;
						}
					} else {
						legalMoves.add(board[i][y]);
					}
				}
				//down
				for (int i = x + 1; i < 8; i++) {
					if (board[i][y].containsPiece()) {
						if (board[i][y].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[i][y]);
							break;
						} else {
							break;
						}
					} else {
						legalMoves.add(board[i][y]);
					}
				}
				//left
				for (int i = y - 1; i >= 0; i--) {
					if (board[x][i].containsPiece()) {
						if (board[x][i].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[x][i]);
							break;
						} else {
							break;
						}
					} else {
						legalMoves.add(board[x][i]);
					}
				}
				//right
				for (int i = y + 1; i < 8; i++) {
					if (board[x][i].containsPiece()) {
						if (board[x][i].piece.getColor() != Colors.BLACK) {
							legalMoves.add(board[x][i]);
							break;
						} else {
							break;
						}
					} else {
						legalMoves.add(board[x][i]);
					}
				}
				break;
			default:
				break;
			}
		}
	}

}