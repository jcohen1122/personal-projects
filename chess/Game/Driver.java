package Game;

import GUI.Gui;

public class Driver {

	//game instance
	static final Game game = new Game();
	
	//gui instance
	static final Gui gui = new Gui();

	/**
	 * Main method driver
	 * @param args 
	 */
	public static void main(String[] args) {
		while(gui.getFrame().isVisible()) {
			
		}
	}

}
