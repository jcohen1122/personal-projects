package Gui;

import java.awt.Color;

import javax.swing.JButton;

import Game.*;

public class MazeButton extends JButton{

	private static final long serialVersionUID = 1L;
	public Tile tile;
	public Color color;

	public MazeButton(Tile tile) {
		//tile
		this.tile = tile;

		//color
		if (tile == null) {
			color = Color.black;
		} else {
			if (tile.x == 0 && tile.y == 0) {
				color = Color.green;
			} else {
				color = Color.white;
			}
		}
		this.setBackground(color);

	}

	public void resetColor() {
		this.setBackground(color);
	}

}
