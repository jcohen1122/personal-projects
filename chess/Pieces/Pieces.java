package Pieces;

public enum Pieces {
	PAWN(1),
	BISHOP(3),
	KNIGHT(3),
	ROOK(5),
	QUEEN(10),
	KING(100000);
	
	public int value;
	
	private Pieces(int value) {
		this.value = value;
	}
	
	/**
	 * Get piece with string name
	 * @param piece
	 * @return
	 */
	public static Pieces get(String piece) {
		switch (piece) {
		case "PAWN":
			return PAWN;
		case "BISHOP":
			return BISHOP;
		case "KNIGHT":
			return KNIGHT;
		case "ROOK":
			return ROOK;
		case "QUEEN":
			return QUEEN;
		case "KING":
			return KING;
		default:
			break;
		}
		return null;
	}
}
