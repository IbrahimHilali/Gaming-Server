package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.HashMap;

/**
 * @author Ibrahim Hilali
 *
 */
public class Advisor extends Figure {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5076940024836193996L;

	/**
	 * @param pos
	 * @param red
	 */
	public Advisor(Position pos, boolean red) {
		super(pos, red);
	}

	@Override
	public boolean checkMove(String move, HashMap<String, Figure> figures) {
		String rules = this.red ? "d0 d2 e1 f0 f2" : "d9 d7 e8 f9 f7" ;
		String[] moves = move.split("-");
		Position source = new Position(moves[0]);
		Position dist = new Position(moves[1]);
		return !this.isSameType(source, dist, figures)
				&& rules.contains(moves[1]) && !figures.containsKey(moves[1])
				&&  this.isInMovementLimitation(source , dist)
				&& !this.isDeathLook(move, figures);
	}

	/**
	 * @param source Position
	 * @param dist Position
	 * @return boolean
	 */
	private boolean isInMovementLimitation(Position source, Position dist) {
		return  Math.abs(source.getCol() - dist.getCol()) == 1 && Math.abs(source.getRow() - dist.getRow()) == 1;
	}

}
