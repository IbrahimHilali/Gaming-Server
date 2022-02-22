package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.HashMap;

/**
 * @author Ibrahim Hilali
 *
 */
public class General extends Figure {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6909048938128069216L;

	/**
	 * @param pos String
	 * @param red boolean
	 */
	public General(String pos, boolean red) {
		super(new Position(pos), red);
	}

	/**
	 * @param pos Position
	 * @param red boolean
	 */
	public General(Position pos, boolean red) {
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
		return !this.isSameType(source, dist, figures) 
				&& this.isInHorizontalLimitation(source, dist)
				&& this.isInVerticalLimitation(source, dist)
				&& this.isMovementLimitation(source, dist)
				&& !this.isDeathLook(move, figures);
	}

	/**
	 * @param source Position
	 * @param dist   Position
	 * @return boolean
	 */
	protected boolean isInHorizontalLimitation(Position source, Position dist) {
		return this.red ? dist.getRow() >= 0 && source.getRow() <= 2: dist.getRow() >= 7 && source.getRow() <= 9;
	}
	
	/**
	 * @param source Position
	 * @param dist   Position
	 * @return boolean
	 */
	protected boolean isMovementLimitation(Position source, Position dist) {
		return( Math.abs(dist.getCol() - source.getCol()) == 1 && dist.getRow()== source.getRow())  
				||(dist.getCol() == source.getCol() && Math.abs(dist.getRow() - source.getRow()) == 1) ;
	}

	/**
	 * @param source Position
	 * @param dist   Position
	 * @return boolean
	 */
	protected boolean isInVerticalLimitation(Position source, Position dist) {
		return (dist.getCol() >= 3 && dist.getCol() <= 5)&&  (source.getCol() >= 3 && source.getCol() <= 5) ;
	}
}
