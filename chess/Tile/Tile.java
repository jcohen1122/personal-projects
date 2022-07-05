package Tile;

import Colors.Colors;
import Pieces.Piece;

/**
 * Represents a singular tile within a chess board
 * @author Joshua Cohen
 *
 */
public class Tile {
	
	//Tile color
	public Colors color;
	
	//Piece on tile
	public Piece piece;
	
	/**
	 * Constructor
	 * @param color
	 */
	public Tile(Colors color) {
		this.color = color;
		this.piece = null;
	}
	
	/**
	 * Returns true if the tile contains a piece, false otherwise
	 * @return
	 */
	public boolean containsPiece() {
		return this.piece != null ? true : false;
	}
}
