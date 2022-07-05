package Colors;
/**
 * Possible colors for pieces, tiles, and players
 * @author Joshua Cohen
 *
 */
public enum Colors {
	BLACK,
	WHITE;
	
	public static Colors get(String color) {
		switch(color) {
		case "BLACK":
			return BLACK;
		case "WHITE":
			return WHITE;
		default:
			break;
		}
		return null;
	}
}
