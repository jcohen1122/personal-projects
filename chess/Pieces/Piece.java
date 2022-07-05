package Pieces;

import java.util.ArrayList;
import java.util.List;

import Colors.Colors;
import Tile.Tile;

/**
 * Represents a generic chess piece
 * @author Joshua Cohen
 *
 */
public class Piece {

	//The actual piece
	public Pieces piece;

	//The color of the piece
	private Colors color;

	//The tile that the piece is on
	public Tile tile;

	//Tiles that the piece has moved to
	private List<Tile> allMoves;
	
	{
		allMoves = new ArrayList<>();
	}

	/**
	 * Constructor
	 * @param piece
	 * @param color
	 * @param tile
	 */
	public Piece(Pieces piece, Colors color, Tile tile) {
		this.piece = piece;
		this.color = color;
		this.tile = tile;
	}
	
	/**
	 * Returns the color of the piece
	 * @return
	 */
	public Colors getColor() {
		return color;
	}
	
	/**
	 * Set piece color
	 * @param color
	 * @return
	 */
	public void setColor(Colors color) {
		this.color = color;
	}

	/**
	 * Returns the tile that the piece is on
	 * @return
	 */
	public Tile getTile() {
		return tile;
	}
	
	/**
	 * Returns all moves that this piece has taken
	 * @return
	 */
	public List<Tile> getAllMoves() {
		return new ArrayList<>(allMoves);
	}
	
	/**
	 * Add move to allMoves list
	 * @param tile
	 */
	public void addMove(Tile tile) {
		allMoves.add(tile);
	}
	
	@Override
	public String toString() {
		return piece+"";
	}

}
