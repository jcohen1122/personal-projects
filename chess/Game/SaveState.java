package Game;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import Tile.Tile;
import Colors.*;
import Pieces.*;

public class SaveState {

	/**
	 * Read in the state of the game
	 * @return
	 */
	private static String readState() {
		StringBuffer saveBoard = new StringBuffer();
		//Player information
		saveBoard.append("PlayerCaptured ");
		for (Piece p : Game.PLAYER.piecesCaptured) {
			saveBoard.append(p.piece + " " + p.getColor() + " ");
		}
		saveBoard.append("\n");
		//Computer information
		saveBoard.append("ComputerCaptured ");
		for (Piece p : Game.COMPUTER.capturedPieces) {
			saveBoard.append(p.piece + " " + p.getColor() + " ");
		}
		saveBoard.append("\n");

		//tile and piece information
		Tile[][] board = Game.PLAYER.board.getBoard();
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				Colors pColor = board[r][c].piece != null ? board[r][c].piece.getColor() : null;
				Pieces pPiece = board[r][c].piece != null ? board[r][c].piece.piece : null;
				saveBoard.append(r + " " + c + " " + board[r][c].color + " " + pColor + " " + pPiece);
				saveBoard.append("\n");
			}
		}
		return saveBoard.toString();
	}

	/**
	 * Save the state of the game
	 * @throws IOException 
	 */
	public static void saveState(String name) throws IOException {
		//create new file
		File file = new File("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Game\\Save States\\"+name);
		file.createNewFile();
		//read save state and write it to file
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(readState());
		fileWriter.close();
	}

	/**
	 * Load state of the game
	 * @throws IOException 
	 */
	public static void loadState() throws IOException {
		//Create a file chooser
		JFileChooser fc = new JFileChooser();
		//In response to a button click:
		fc.showOpenDialog(new JFrame());
		File file = fc.getSelectedFile();
		//create file scanner
		Scanner in = new Scanner(file);

		/******** READ IN FILE AND SET STATES **************/

		/**** PLAYER PIECES CAPTURED ****/
		Game.PLAYER.piecesCaptured = new ArrayList<Piece>();

		String plCaptured = in.nextLine();
		Scanner plScanner = new Scanner(plCaptured);
		//skip header
		plScanner.next();
		//add new pieces
		while (plScanner.hasNext()) {
			Game.PLAYER.piecesCaptured.add(new Piece(Pieces.get(plScanner.next()), Colors.get(plScanner.next()), null));
		}
		plScanner.close();
		/********************************/

		/**** COMPUTER PIECES CAPTURED ****/
		Game.COMPUTER.capturedPieces = new ArrayList<Piece>();

		String pcCaptured = in.nextLine();
		Scanner pcScanner = new Scanner(pcCaptured);
		//skip header
		pcScanner.next();
		//add new pieces
		while (pcScanner.hasNext()) {
			Pieces pc = Pieces.get(pcScanner.next());
			Colors cc = Colors.get(pcScanner.next());
			Game.COMPUTER.capturedPieces.add(new Piece(pc, cc, null));
		}
		pcScanner.close();
		/********************************/

		/********** LOAD BOARD STATE *************/
		//Each line is a new tile and piece
		while (in.hasNext()) {
			//Get tile coordinates
			int x = in.nextInt();
			int y = in.nextInt();
			Tile tile = Game.PLAYER.board.getBoard()[x][y];
			//Set tile color
			tile.color = Colors.get(in.next());
			//Set tile piece
			Colors color = Colors.get(in.next());
			Pieces piece = Pieces.get(in.next());
			if (tile.piece == null) {
				if (piece != null) {
					tile.piece = new Piece(piece, color, tile);
				}
			} else {
				if (piece != null) {
					tile.piece.setColor(color);
					tile.piece.piece = piece;
				} else {
					tile.piece = null;
				}
			}
		}

		in.close();
	}
}
