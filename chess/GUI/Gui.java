package GUI;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import Game.*;
import Pieces.Piece;
import Pieces.Pieces;
import Tile.*;
import Colors.*;

public class Gui {

	//2D array of buttons
	private JButton[][] buttons = new JButton[8][8];
	//frame instance
	private JFrame frame;

	//Action listeners
	private StartAction startAction = new StartAction();
	private EndAction endAction = new EndAction();

	//First Action Listener
	public class StartAction implements ActionListener {
		public int fromRow;
		public int fromCol;

		@Override
		public void actionPerformed(ActionEvent e) {

			//original row and col
			for (int row = 0; row < 8; row++) {
				for (int col = 0; col < 8; col++) {
					if (buttons[row][col] == (JButton)e.getSource()) {
						fromRow = row;
						fromCol = col;
					}
				}
			}

			//make sure that the chosen piece is a white piece, not null, and has possible moves
			if (Game.PLAYER.board.getBoard()[fromRow][fromCol].piece == null ||
					Game.PLAYER.board.getBoard()[fromRow][fromCol].piece.getColor() != Colors.WHITE || Game.PLAYER.compileLegalMoves(fromRow, fromCol).isEmpty()) {
				return;
			}

			for (JButton[] bRow : buttons) {
				for (JButton b : bRow) {
					b.removeActionListener(startAction);
					b.addKeyListener(new EscapeAction());
				}
			}

			promptMove(Game.PLAYER.compileLegalMoves(fromRow,fromCol));
		}

	}

	//End action listener
	public class EndAction implements ActionListener {

		int xRow;
		int xCol;

		@Override
		public void actionPerformed(ActionEvent e) {

			//Action listener for legal buttons

			//on click, set xRow and xCol values (target/destination)
			for (int row = 0; row < 8; row++) {
				for (int col = 0; col < 8; col++) {
					if (buttons[row][col].equals((JButton)e.getSource())) {
						xRow = row;
						xCol = col;
					}
				}
			}

			//move piece
			Game.move(startAction.fromRow, startAction.fromCol, xRow, xCol);

			//if move puts king in danger
			for (Tile opp : Game.COMPUTER.getPieceTiles()) {
				if (Game.PLAYER.board.getLegalMoves(Game.PLAYER.board.getCoordinates(opp).get(0), Game.PLAYER.board.getCoordinates(opp).get(1)) != null) {
					for (Tile t : Game.PLAYER.board.getLegalMoves(Game.PLAYER.board.getCoordinates(opp).get(0), Game.PLAYER.board.getCoordinates(opp).get(1))) {
						if (t.piece != null && t.piece.piece == Pieces.KING && t.piece.getColor() == Colors.WHITE) {
							//restart move
							JOptionPane.showMessageDialog(frame, "Illegal Move: Cannot sacrifice King");
							//move piece back
							Game.move(xRow, xCol, startAction.fromRow, startAction.fromCol);
							return;
						}
					}
				}
			}

			//change destination button
			buttons[xRow][xCol].setText("("+xRow+","+xCol+") " + Game.PLAYER.board.getPiece(xRow,xCol));
			buttons[xRow][xCol].setIcon(buttons[startAction.fromRow][startAction.fromCol].getIcon());

			//change this button color and text
			buttons[startAction.fromRow][startAction.fromCol].setText("("+startAction.fromRow+","+startAction.fromCol+") null");
			buttons[startAction.fromRow][startAction.fromCol].setIcon(null);

			//change all buttons backgrounds to normal
			for (int i = 0; i < 8; i++) {
				for (int k = 0; k < 8; k++) {
					if (Game.PLAYER.board.getBoard()[i][k].color == Colors.WHITE) {
						buttons[i][k].setBackground(Color.WHITE);
						buttons[i][k].setForeground(Color.WHITE);
					} else {
						buttons[i][k].setBackground(Color.GRAY);
						buttons[i][k].setForeground(Color.GRAY);
					}
				}
			}

			//refresh board
			frame.invalidate();
			frame.validate();
			frame.repaint();

			//play sound for piece placing
			playPieceSound();

			//remove action listeners
			removeActionListeners();

			//Check for game condition
			if (Game.condition() == Conditions.CONTINUE) {
				//add back start action listener
				for (int r = 0; r < 8; r++) {
					for (int c = 0; c < 8; c++) {
						buttons[r][c].addActionListener(startAction);
					}
				}
				//computer move
				computerMove();
				return;
			} else {
				switch(Game.PLAYER.board.kingInCheck()) {
				case WHITE_CHECKMATE:
					playWinSound();
					JOptionPane.showMessageDialog(frame, Game.condition());
					frame.setVisible(false);
					break;
				case BLACK_CHECKMATE:
					JOptionPane.showMessageDialog(frame, Game.condition());
					frame.setVisible(false);
					break;
				case WHITE_IN_CHECK:
					//computer move
					computerMove();

					break;
				case BLACK_IN_CHECK:
					JOptionPane.showMessageDialog(frame, Game.condition());
					//add back start action listener
					for (int r = 0; r < 8; r++) {
						for (int c = 0; c < 8; c++) {
							buttons[r][c].addActionListener(startAction);
						}
					}
					//computer move
					computerMove();
					break;
				default:
					playWinSound();
					JOptionPane.showMessageDialog(frame, Game.condition());
					frame.setVisible(false);
					break;
				}
			}
		}
	}

	//Escape case action listener
	public class EscapeAction implements KeyListener {

		@Override
		public void keyReleased(KeyEvent arg0) {

		}

		@Override
		public void keyPressed(KeyEvent arg0) {
			if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
				//remove all action listeners
				removeActionListeners();

				//add them back
				for (int r = 0; r < Game.PLAYER.board.getBoard().length; r++) {
					for (int c = 0; c < Game.PLAYER.board.getBoard().length; c++) {
						buttons[r][c].addActionListener(startAction);
					}
				}

				//set all colors to normal
				for (int i = 0; i < 8; i++) {
					for (int k = 0; k < 8; k++) {
						if (Game.PLAYER.board.getBoard()[i][k].color == Colors.WHITE) {
							buttons[i][k].setBackground(Color.WHITE);
							buttons[i][k].setForeground(Color.WHITE);
						} else {
							buttons[i][k].setBackground(Color.GRAY);
							buttons[i][k].setForeground(Color.GRAY);
						}
					}
				}

				//refresh frame
				frame.invalidate();
				frame.validate();
				frame.repaint();
			}
		}

		@Override
		public void keyTyped(KeyEvent arg0) {

		}


	}

	//exit game listener
	public class ExitGame implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
				int answer = JOptionPane.showOptionDialog(frame, "Menu", "Menu", 1, 1, null, new String[] {"Resume", "Quit", "Save", "Load"}, "Resume");

				if(answer == 1) {
					System.exit(0);
				} else if (answer == 2) {
					String file = JOptionPane.showInputDialog(frame, "File Name");
					try {
						SaveState.saveState(file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (answer == 3) {
					try {
						SaveState.loadState();

						//update icons
						for (int r = 0; r < 8; r++) {
							for (int c = 0; c < 8; c++) {
								//Set icon
								//if not null and white
								if (Game.PLAYER.board.getPiece(r,c) != null && Game.PLAYER.board.getPiece(r,c).getColor() == Colors.WHITE) {
									switch (Game.PLAYER.board.getPiece(r,c).piece) {
									case PAWN:
										ImageIcon icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\wPawn.png");
										Image img = icon.getImage();
										Image newimg = img.getScaledInstance(1000/12, 500/6, java.awt.Image.SCALE_SMOOTH) ;  
										icon = new ImageIcon(newimg, BorderLayout.CENTER);
										buttons[r][c].setIcon(icon);
										break;
									case ROOK:
										icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\wRook.png");
										img = icon.getImage();
										newimg = img.getScaledInstance(1000/12, 500/6, java.awt.Image.SCALE_SMOOTH) ; 
										icon = new ImageIcon(newimg, BorderLayout.CENTER);
										buttons[r][c].setIcon(icon);										break;
									case BISHOP:
										icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\wBishop.png");
										img = icon.getImage();
										newimg = img.getScaledInstance(1000/12, 500/6, java.awt.Image.SCALE_SMOOTH) ;  
										icon = new ImageIcon(newimg, BorderLayout.CENTER);
										buttons[r][c].setIcon(icon);										break;
									case KNIGHT:
										icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\wKnight.png");
										img = icon.getImage();
										newimg = img.getScaledInstance(1000/12, 500/6, java.awt.Image.SCALE_SMOOTH) ; 
										icon = new ImageIcon(newimg, BorderLayout.CENTER);
										buttons[r][c].setIcon(icon);										break;
									case QUEEN:
										icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\wQueen.png");
										img = icon.getImage();
										newimg = img.getScaledInstance(1000/12, 500/6, java.awt.Image.SCALE_SMOOTH) ; 
										icon = new ImageIcon(newimg, BorderLayout.CENTER);
										buttons[r][c].setIcon(icon);										break;
									case KING:
										icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\wKing.png");
										img = icon.getImage();
										newimg = img.getScaledInstance(1000/12, 500/6, java.awt.Image.SCALE_SMOOTH) ; 
										icon = new ImageIcon(newimg, BorderLayout.CENTER);
										buttons[r][c].setIcon(icon);										break;
									default:
										break;
									}
								}//black pieces
								else if (Game.PLAYER.board.getPiece(r,c) != null && Game.PLAYER.board.getPiece(r,c).getColor() == Colors.BLACK) {
									switch (Game.PLAYER.board.getPiece(r,c).piece) {
									case PAWN:
										ImageIcon icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\bPawn.png");
										Image img = icon.getImage();
										Image newimg = img.getScaledInstance(1000/12, 500/6, java.awt.Image.SCALE_SMOOTH) ;  
										icon = new ImageIcon(newimg, BorderLayout.CENTER);
										buttons[r][c].setIcon(icon);										break;
									case ROOK:
										icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\bRook.png");
										img = icon.getImage();
										newimg = img.getScaledInstance(1000/12, 500/6, java.awt.Image.SCALE_SMOOTH) ;  
										icon = new ImageIcon(newimg, BorderLayout.CENTER);
										buttons[r][c].setIcon(icon);										break;
									case BISHOP:
										icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\bBishop.png");
										img = icon.getImage();
										newimg = img.getScaledInstance(1000/12, 500/6, java.awt.Image.SCALE_SMOOTH) ;   
										icon = new ImageIcon(newimg, BorderLayout.CENTER);
										buttons[r][c].setIcon(icon);										break;
									case KNIGHT:
										icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\bKnight.png");
										img = icon.getImage();
										newimg = img.getScaledInstance(1000/12, 500/6, java.awt.Image.SCALE_SMOOTH) ;  
										icon = new ImageIcon(newimg, BorderLayout.CENTER);
										buttons[r][c].setIcon(icon);										break;
									case QUEEN:
										icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\bQueen.png");
										img = icon.getImage();
										newimg = img.getScaledInstance(1000/12, 500/6, java.awt.Image.SCALE_SMOOTH) ;  
										icon = new ImageIcon(newimg, BorderLayout.CENTER);
										buttons[r][c].setIcon(icon);										break;
									case KING:
										icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\bKing.png");
										img = icon.getImage();
										newimg = img.getScaledInstance(1000/12, 500/6, java.awt.Image.SCALE_SMOOTH) ;  
										icon = new ImageIcon(newimg, BorderLayout.CENTER);
										buttons[r][c].setIcon(icon);										break;
									default:
										buttons[r][c].setIcon(null);
									}
								} else {
									buttons[r][c].setIcon(null);
								}
							}
						}

						//refresh board
						frame.invalidate();
						frame.validate();
						frame.repaint();
						
						JOptionPane.showMessageDialog(frame, "Save State Loaded");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * Constructor
	 */
	public Gui() {

		/************** START UP FRAME ******************/
		//create frame
		JFrame startFrame = new JFrame("Chess");
		startFrame.setSize(1000,500);
		startFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		startFrame.setLayout(new GridLayout(2,1));

		//add text
		JLabel sLbl = new JLabel("Chess ");
		sLbl.setFont(new Font("MonoSpaced", Font.BOLD, 75));

		//add text w/ filler
		JPanel bPnl1 = new JPanel(new GridLayout(3,4));
		for (int i = 0; i < 13; i++) {
			if (i == 0) {
				JLabel temp = new JLabel();
				ImageIcon icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\bKing.png");
				Image img = icon.getImage();
				Image newimg = img.getScaledInstance(100,100, java.awt.Image.SCALE_SMOOTH);  
				icon = new ImageIcon(newimg, BorderLayout.CENTER);
				temp.setIcon(icon);
				bPnl1.add(temp);
			} else if (i == 7) {
				bPnl1.add(sLbl);
			} else {
				bPnl1.add(new JLabel());
			}
		}
		startFrame.add(bPnl1);

		//add button to make chess board visible
		JButton sBtn = new JButton("Start Game");
		sBtn.setFont(new Font(sBtn.getFont()+"", Font.BOLD, 30));
		sBtn.setSize(50,50);
		sBtn.setBackground(Color.LIGHT_GRAY);
		sBtn.setForeground(Color.BLACK);
		sBtn.setBorder(BorderFactory.createLineBorder(Color.black));
		sBtn.addActionListener((a) -> {
			//make game board visible and start up screen not
			frame.setVisible(true);
			startFrame.setVisible(false);
		});

		//Add button to panel w/ filler JLabels to make more visually appealing
		JPanel bPnl = new JPanel(new GridLayout(3,3));
		for (int i = 0; i <= 8; i++) {
			if (i == 1) {
				bPnl.add(sBtn);
			} else if (i == 8) {
				JPanel tPnl = new JPanel(new GridLayout(1,3));
				tPnl.add(new JLabel());
				tPnl.add(new JLabel());
				JLabel temp = new JLabel();
				ImageIcon icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\wQueen.png");
				Image img = icon.getImage();
				Image newimg = img.getScaledInstance(100,100, java.awt.Image.SCALE_SMOOTH);  
				icon = new ImageIcon(newimg, BorderLayout.EAST);
				temp.setIcon(icon);
				tPnl.add(temp);
				bPnl.add(tPnl);
			} else {
				bPnl.add(new JLabel());
			}
		}
		startFrame.add(bPnl);

		//make visible
		startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startFrame.setVisible(true);

		//make frame
		makeFrame();
	}


	/**
	 * Computer Move
	 */
	public void computerMove() {
		/***If game continues, then black moves***/
		//Computer move
		ArrayList<Tile> pcMove = Game.computerMove();
		//Computer from tile
		ArrayList<Integer> pcFrom = Game.PLAYER.board.getCoordinates(pcMove.get(0));
		//Computer destination
		ArrayList<Integer> pcTo = Game.PLAYER.board.getCoordinates(pcMove.get(1));

		//make sure that from is not a piece that was just captured
		while (pcFrom.get(0) == endAction.xRow && pcFrom.get(1) == endAction.xCol) {
			pcMove = Game.computerMove();
			//Computer from tile
			pcFrom = Game.PLAYER.board.getCoordinates(pcMove.get(0));
			//Computer destination
			pcTo = Game.PLAYER.board.getCoordinates(pcMove.get(1));
		}

		//if King is in check and another piece is not capturing the aggressor
		if (Game.condition() == Conditions.BLACK_IN_CHECK && 
				(Game.PLAYER.board.getBoard()[pcTo.get(0)][pcTo.get(1)].piece == null ||
				Game.PLAYER.board.getBoard()[pcTo.get(0)][pcTo.get(1)].piece.getColor() != Colors.WHITE)) {

			//find king
			for (int r = 0; r < 8; r++) {
				for (int c = 0; c < 8; c++) {
					//found king
					if (Game.PLAYER.board.getBoard()[r][c].piece != null &&
							Game.PLAYER.board.getBoard()[r][c].piece.piece == Pieces.KING &&
							Game.PLAYER.board.getBoard()[r][c].piece.getColor() == Colors.BLACK) {

						//add king coordinates for from tile
						pcFrom = new ArrayList<>();
						pcFrom.add(r);
						pcFrom.add(c);

						//possible king moves
						List<Tile> kingMoves = Game.PLAYER.board.getLegalMoves(r, c);
						//first possible move
						Tile move = kingMoves.get(0);

						//iterate thru all possible king moves
						for (int z = 0; z < kingMoves.size(); z++) {
							move = kingMoves.get(z);

							//make sure move isnt putting king into another check
							for (Tile[] pieceRow : Game.PLAYER.board.getBoard()) {
								for (Tile piece : pieceRow) {

									//Possible moves for piece
									List<Tile> pieceMoves = Game.PLAYER.board.getLegalMoves(Game.PLAYER.board.getCoordinates(piece).get(0), 
											Game.PLAYER.board.getCoordinates(piece).get(1));

									//if piece can move where king can move, remove king
									if (pieceMoves != null && pieceMoves.contains(move)) {
										kingMoves.remove(move);
									}

								}
							}

						}

						//reset pcTo
						pcTo = Game.PLAYER.board.getCoordinates(move);

					}
				}
			}

		} 

		//Pop up
		JOptionPane.showMessageDialog(frame, "Computer Moving");

		//Move Piece
		Game.COMPUTER.movePiece(Game.PLAYER.board.getBoard()[pcFrom.get(0)][pcFrom.get(1)], 
				Game.PLAYER.board.getBoard()[pcTo.get(0)][pcTo.get(1)]);

		//Edit destination button
		buttons[pcTo.get(0)][pcTo.get(1)].setText("("+pcTo.get(0)+","+pcTo.get(1)+") " + Game.PLAYER.board.getPiece(pcTo.get(0),pcTo.get(1)));
		buttons[pcTo.get(0)][pcTo.get(1)].setIcon(buttons[pcFrom.get(0)][pcFrom.get(1)].getIcon());

		//Edit original button
		buttons[pcFrom.get(0)][pcFrom.get(1)].setText("("+pcFrom.get(0)+","+pcFrom.get(1)+") null");
		buttons[pcFrom.get(0)][pcFrom.get(1)].setIcon(null);

		//refresh board
		frame.invalidate();
		frame.validate();
		frame.repaint();

		//play sound
		playPieceSound();

		//exit clause
		frame.requestFocus();

		//check conditions
		switch(Game.condition()) {
		case BLACK_CHECKMATE:
			JOptionPane.showMessageDialog(frame, Game.condition());
			frame.setVisible(false);
			break;
		case BLACK_IN_CHECK:
			break;
		case CONTINUE:
			break;
		case PLAYER_LOSE:
			JOptionPane.showMessageDialog(frame, Game.condition());
			frame.setVisible(false);
			break;
		case PLAYER_WIN:
			JOptionPane.showMessageDialog(frame, Game.condition());
			frame.setVisible(false);
			break;
		case WHITE_CHECKMATE:
			JOptionPane.showMessageDialog(frame, Game.condition());
			frame.setVisible(false);
			break;
		case WHITE_IN_CHECK:
			JOptionPane.showMessageDialog(frame, Game.condition());
			//only add action listener back to white king and pieces that can protect white king
			//remove action listeners
			removeActionListeners();
			for (int r = 0; r < Game.PLAYER.board.getBoard().length; r++) {
				for (int c = 0; c < Game.PLAYER.board.getBoard().length; c++) {

					//find king
					if (Game.PLAYER.board.getBoard()[r][c].piece != null && 
							Game.PLAYER.board.getBoard()[r][c].piece.piece == Pieces.KING &&
							Game.PLAYER.board.getBoard()[r][c].piece.getColor() == Colors.WHITE) {

						//add listener to king
						buttons[r][c].addActionListener(startAction);

						//add listener to protectors
						for (Tile tt : Game.COMPUTER.getPieceTiles()) {
							List<Tile> ttMoves = Game.PLAYER.board.getLegalMoves(Game.PLAYER.board.getCoordinates(tt).get(0),
									Game.PLAYER.board.getCoordinates(tt).get(1));
							//check if possible moves for black piece contain king
							if (ttMoves != null && ttMoves.contains(Game.PLAYER.board.getBoard()[r][c])) {

								//see if a white piece can capture aggressor and add action listener
								for (Tile[] tR : Game.PLAYER.board.getBoard()) {
									for (Tile tRT : tR) {
										//if white piece move can capture black piece aggressor
										if (Game.PLAYER.board.getLegalMoves(Game.PLAYER.board.getCoordinates(tRT).get(0),
												Game.PLAYER.board.getCoordinates(tRT).get(1)) != null &&
												Game.PLAYER.compileLegalMoves(Game.PLAYER.board.getCoordinates(tRT).get(0),
														Game.PLAYER.board.getCoordinates(tRT).get(1)).contains(Game.PLAYER.board.getCoordinates(tt))) {
											//add action listener
											buttons[Game.PLAYER.board.getCoordinates(tRT).get(0)][Game.PLAYER.board.getCoordinates(tRT).get(1)].addActionListener(startAction);
										}
									}
								}

							}
						}
					}
				}
			}
			break;
		default:
			break;
		}

		frame.requestFocus();

	}

	/**
	 * Prompts a move
	 * @param list
	 */
	public void promptMove(List<ArrayList<Integer>> list) {
		//highlight buttons that are legal moves
		for (ArrayList<Integer> move : list) {
			for (int r = 0; r < 8; r++) {
				for (int c = 0; c < 8; c++) {
					if (move.get(0) == r && move.get(1) == c) {
						buttons[r][c].setBackground(Color.ORANGE);
						buttons[r][c].setForeground(Color.ORANGE);
						JButton b = buttons[r][c];
						b.addActionListener(endAction);
					}
				}
			}
		}

		//refresh board
		frame.invalidate();
		frame.validate();
		frame.repaint();
	}

	/**
	 * Removes the action listeners from highlighted buttons
	 */
	public void removeActionListeners() {
		//remove actionlisteners
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {

				for (ActionListener al : buttons[r][c].getActionListeners()) {
					buttons[r][c].removeActionListener(al);
				}

			}
		}
	}

	/**
	 * Play sound of a piece hitting the board
	 */
	public void playPieceSound() {
		//play sound for piece placing
		try{
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\chess_move.wav")));
			clip.start();
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}

	public void playWinSound() {
		//play sound for win
		try{
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\chess_win.wav")));
			clip.start();
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}

	public JFrame getFrame() {
		return frame;
	}
	
	public void makeFrame() {
		/****************** CHESS BOARD FRAME *****************/
		//Create frame
		frame = new JFrame("Chess vs Computer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000,500);
		//makes it fullscreen
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setLayout(new GridLayout(9,10));

		//add exit clause
		frame.setFocusable(true);
		frame.requestFocus();
		frame.addKeyListener(new ExitGame());

		//Add 64 buttons
		for (int r = 0; r < 8; r++) {

			//Add number axis
			//new centered label
			JLabel lbl = new JLabel((r+1) + "", SwingConstants.CENTER);
			//make font bigger and bold
			lbl.setFont(new Font(lbl.getFont()+"", Font.BOLD, 16));
			//make border around label
			lbl.setBorder(BorderFactory.createLineBorder(Color.black));
			//set layout
			lbl.setLayout(new FlowLayout());
			//add to frame
			frame.add(lbl, BorderLayout.CENTER);

			for (int c = 0; c < 8; c++) {
				//Create button w/ piece name
				JButton b = new JButton("("+r+","+c+") "+Game.PLAYER.board.getPiece(r,c)+"");
				//add click action
				b.addActionListener(startAction);

				//Set icon
				//if not null and white
				if (Game.PLAYER.board.getPiece(r,c) != null && Game.PLAYER.board.getPiece(r,c).getColor() == Colors.WHITE) {
					switch (Game.PLAYER.board.getPiece(r,c).piece) {
					case PAWN:
						ImageIcon icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\wPawn.png");
						Image img = icon.getImage();
						Image newimg = img.getScaledInstance(frame.getWidth()/12, frame.getHeight()/6, java.awt.Image.SCALE_SMOOTH) ;  
						icon = new ImageIcon(newimg, BorderLayout.CENTER);
						b.setIcon(icon);
						break;
					case ROOK:
						icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\wRook.png");
						img = icon.getImage();
						newimg = img.getScaledInstance(frame.getWidth()/12, frame.getHeight()/6, java.awt.Image.SCALE_SMOOTH) ;  
						icon = new ImageIcon(newimg, BorderLayout.CENTER);
						b.setIcon(icon);
						break;
					case BISHOP:
						icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\wBishop.png");
						img = icon.getImage();
						newimg = img.getScaledInstance(frame.getWidth()/12, frame.getHeight()/6, java.awt.Image.SCALE_SMOOTH) ;  
						icon = new ImageIcon(newimg, BorderLayout.CENTER);
						b.setIcon(icon);
						break;
					case KNIGHT:
						icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\wKnight.png");
						img = icon.getImage();
						newimg = img.getScaledInstance(frame.getWidth()/12, frame.getHeight()/6, java.awt.Image.SCALE_SMOOTH) ;  
						icon = new ImageIcon(newimg, BorderLayout.CENTER);
						b.setIcon(icon);
						break;
					case QUEEN:
						icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\wQueen.png");
						img = icon.getImage();
						newimg = img.getScaledInstance(frame.getWidth()/12, frame.getHeight()/6, java.awt.Image.SCALE_SMOOTH) ;  
						icon = new ImageIcon(newimg, BorderLayout.CENTER);
						b.setIcon(icon);
						break;
					case KING:
						icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\wKing.png");
						img = icon.getImage();
						newimg = img.getScaledInstance(frame.getWidth()/12, frame.getHeight()/6, java.awt.Image.SCALE_SMOOTH) ;  
						icon = new ImageIcon(newimg, BorderLayout.CENTER);
						b.setIcon(icon);
						break;
					default:
						break;
					}
				}//black pieces
				else if (Game.PLAYER.board.getPiece(r,c) != null && Game.PLAYER.board.getPiece(r,c).getColor() == Colors.BLACK) {
					switch (Game.PLAYER.board.getPiece(r,c).piece) {
					case PAWN:
						ImageIcon icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\bPawn.png");
						Image img = icon.getImage();
						Image newimg = img.getScaledInstance(frame.getWidth()/12, frame.getHeight()/6, java.awt.Image.SCALE_SMOOTH) ;  
						icon = new ImageIcon(newimg, BorderLayout.CENTER);
						b.setIcon(icon);
						break;
					case ROOK:
						icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\bRook.png");
						img = icon.getImage();
						newimg = img.getScaledInstance(frame.getWidth()/12, frame.getHeight()/6, java.awt.Image.SCALE_SMOOTH) ;  
						icon = new ImageIcon(newimg, BorderLayout.CENTER);
						b.setIcon(icon);
						break;
					case BISHOP:
						icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\bBishop.png");
						img = icon.getImage();
						newimg = img.getScaledInstance(frame.getWidth()/12, frame.getHeight()/6, java.awt.Image.SCALE_SMOOTH) ;  
						icon = new ImageIcon(newimg, BorderLayout.CENTER);
						b.setIcon(icon);
						break;
					case KNIGHT:
						icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\bKnight.png");
						img = icon.getImage();
						newimg = img.getScaledInstance(frame.getWidth()/12, frame.getHeight()/6, java.awt.Image.SCALE_SMOOTH) ;  
						icon = new ImageIcon(newimg, BorderLayout.CENTER);
						b.setIcon(icon);
						break;
					case QUEEN:
						icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\bQueen.png");
						img = icon.getImage();
						newimg = img.getScaledInstance(frame.getWidth()/12, frame.getHeight()/6, java.awt.Image.SCALE_SMOOTH) ;  
						icon = new ImageIcon(newimg, BorderLayout.CENTER);
						b.setIcon(icon);
						break;
					case KING:
						icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\bKing.png");
						img = icon.getImage();
						newimg = img.getScaledInstance(frame.getWidth()/12, frame.getHeight()/6, java.awt.Image.SCALE_SMOOTH) ;  
						icon = new ImageIcon(newimg, BorderLayout.CENTER);
						b.setIcon(icon);
						break;
					default:
						break;
					}
				}

				//add button color
				if (Game.PLAYER.board.getBoard()[r][c].color == Colors.WHITE) {
					b.setBackground(Color.WHITE);
					b.setForeground(Color.WHITE);
				} else {
					b.setBackground(Color.GRAY);
					b.setForeground(Color.GRAY);
					b.setBorder(BorderFactory.createLineBorder(Color.black));
				}

				//add to buttons list for easy accessibility
				buttons[r][c] = b;
				frame.add(b);
			}
		}
		//Add bottom left with border
		frame.add(new JButton() {

			private static final long serialVersionUID = 1L;

			{
				setBorder(BorderFactory.createLineBorder(Color.black));
				//Show captured pieces
				addActionListener((e)-> {

					//make frame
					JFrame cFrame = new JFrame("Captured Pieces");
					cFrame.setSize(700,200);
					cFrame.setExtendedState(JFrame.MAXIMIZED_HORIZ);
					cFrame.setLayout(new GridLayout(2,6));

					//add black captured pieces
					for (Piece piece : Game.PLAYER.piecesCaptured) {
						JPanel pnl = new JPanel();
						pnl.setBorder(BorderFactory.createLineBorder(Color.black));

						JLabel lbl = new JLabel();
						Icon icon;

						//set icon
						switch(piece.piece) {
						case BISHOP:
							icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\bBishop.png");
							break;
						case KING:
							icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\bKing.png");
							break;
						case KNIGHT:
							icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\bKnight.png");
							break;
						case PAWN:
							icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\bPawn.png");
							break;
						case QUEEN:
							icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\bQueen.png");
							break;
						case ROOK:
							icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\bRook.png");
							break;
						default:
							icon = new ImageIcon();
							break;
						}

						//Add icon to label, and label to panel
						lbl.setIcon(icon);
						pnl.add(lbl);

						//Add panel to frame
						cFrame.add(pnl);
					}

					//add white captured pieces
					for (Piece piece : Game.COMPUTER.capturedPieces) {
						JPanel pnl = new JPanel();
						pnl.setBorder(BorderFactory.createLineBorder(Color.black));

						JLabel lbl = new JLabel();
						Icon icon;

						//set icon
						switch(piece.piece) {
						case BISHOP:
							icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\wBishop.png");
							break;
						case KING:
							icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\wKing.png");
							break;
						case KNIGHT:
							icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\wKnight.png");
							break;
						case PAWN:
							icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\wPawn.png");
							break;
						case QUEEN:
							icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\wQueen.png");
							break;
						case ROOK:
							icon = new ImageIcon("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\wRook.png");
							break;
						default:
							icon = new ImageIcon();
							break;
						}

						//Add icon to label, and label to panel
						lbl.setIcon(icon);
						pnl.add(lbl);

						//Add panel to frame
						cFrame.add(pnl);
					}

					//Make frame visible
					cFrame.setVisible(true);

					//play sound on close
					cFrame.addWindowListener(new WindowListener() {

						@Override
						public void windowActivated(WindowEvent arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void windowClosed(WindowEvent arg0) {

						}

						@Override
						public void windowClosing(WindowEvent arg0) {
							//play sound for closing frame
							try{
								Clip clip = AudioSystem.getClip();
								clip.open(AudioSystem.getAudioInputStream(new File("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\open.wav")));
								clip.start();
							} catch (Exception ex){
								ex.printStackTrace();
							}

							frame.requestFocus();

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
							//play sound for opening frame
							try{
								Clip clip = AudioSystem.getClip();
								clip.open(AudioSystem.getAudioInputStream(new File("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\open.wav")));
								clip.start();
							} catch (Exception ex){
								ex.printStackTrace();
							}

						}

					});
				});
			}


		});
		//Add letter axis
		for (int i = 'H'; i >= 'A'; i--) {
			//new centered label
			JLabel lbl = new JLabel((char)i + "", SwingConstants.CENTER);
			//make font bigger and bold
			lbl.setFont(new Font(lbl.getFont()+"", Font.BOLD, 16));
			//make border around label
			lbl.setBorder(BorderFactory.createLineBorder(Color.black));
			//set layout
			lbl.setLayout(new FlowLayout());
			//add to frame
			frame.add(lbl, BorderLayout.CENTER);
		}

		//frame.setVisible(true);

		//make sure system exits when frame closes
		frame.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent e) {
				frame.requestFocus();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);

			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowOpened(WindowEvent e) {
				try{
					Clip clip = AudioSystem.getClip();
					clip.open(AudioSystem.getAudioInputStream(new File("C:\\Users\\jryan\\OneDrive\\Desktop\\My Programs\\Chess\\src\\Images\\start.wav")));
					clip.start();
				} catch (Exception ex){
					ex.printStackTrace();
				}

			}

		});
	}
}
