package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.HashMap;

/**
 * @author Ibrahim Hilali
 *
 */
public class Soldier extends Figure {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6507458312296750864L;

	/**
	 * @param pos String
	 * @param red boolean
	 */
	public Soldier(String pos, boolean red) {
		super(new Position(pos), red);
	}

	/**
	 * @param pos Position
	 * @param red boolean
	 */
	public Soldier(Position pos, boolean red) {
		super(pos, red);
	}

	/**
	 *
	 */
	@Override
	public boolean checkMove(String move, HashMap<String, Figure> figures) {
		String[] moves = move.split("-");
		Position source = new Position(moves[0]);
		Position dist = new Position(moves[1]);
		return !this.isSameType(source, dist, figures) && this.isInMovementLimitation(source, dist, figures)
				&& !this.isDeathLook(move, figures);
	}

	/**
	 * @param source  Position
	 * @param dist    Position
	 * @param figures
	 * @return HashMap<String, Figure>
	 */
	protected boolean isInMovementLimitation(Position source, Position dist, HashMap<String, Figure> figures) {

		return this.isHorizantalAllowed(source, dist)
				? this.isForwardDirection(source, dist) || isSideMove(source, dist)
				: this.isForwardDirection(source, dist) && dist.getCol() == source.getCol();
	}

	/**
	 * @param source Position
	 * @param dist Position
	 * @return boolean
	 */
	private boolean isSideMove(Position source, Position dist) {
		return Math.abs(dist.getCol() - source.getCol()) == 1;
	}

	/**
	 * @param source Position
	 * @param dist   Position
	 * @return boolean
	 */
	private boolean isHorizantalAllowed(Position source, Position dist) {

		return this.red ? source.getRow() >= 5 : source.getRow() <= 4;
	}

	/**
	 * @param source Position
	 * @param dist   Position
	 * @return boolean
	 */
	private boolean isForwardDirection(Position source, Position dist) {
		return this.red ? dist.getRow() - source.getRow() == 1 && source.getRow() < 9
				: source.getRow() - dist.getRow() == 1 && source.getRow() > 0;
	}
}
