package Game;

import java.util.ArrayList;

import Tile.Tile;

/**
 * Represents a game of chess
 * @author jryan
 *
 */
public class Game {
	//Player
	public static Player PLAYER;
	//Computer
	public static Computer COMPUTER;

	{
		PLAYER = new Player();
		COMPUTER = new Computer();
	}
	
	/**
	 * Complete a player move from the gui
	 * @param fromRow
	 * @param fromCol
	 * @param toRow
	 * @param toCol
	 */
	public static void move(int fromRow, int fromCol, int toRow, int toCol) {
		PLAYER.movePiece(fromRow,fromCol,toRow,toCol);
	}

	/**
	 * New move for the computer
	 */
	public static ArrayList<Tile> computerMove() {
		ArrayList<Tile> tiles = new ArrayList<>();
		
		//Smart tile
		Tile tile = COMPUTER.heuristicStartMove();
		
		//Smart end
		Tile move = COMPUTER.heuristicEndMove(tile);
		
		tiles.add(tile);
		tiles.add(move);
		
		return tiles;
	}

	/**
	 * Returns the condition of the game
	 * @return
	 */
	public static Conditions condition() {
		if (PLAYER.win()) {
			return Conditions.PLAYER_WIN;
		} else if (PLAYER.lose()) {
			return Conditions.PLAYER_LOSE;
		} else {
			return PLAYER.board.kingInCheck();
		}
	}

}
