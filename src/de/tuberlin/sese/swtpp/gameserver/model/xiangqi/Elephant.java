package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.HashMap;

/**
 * @author Ibrahim Hilali
 *
 */
public class Elephant extends Figure {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9171246029491320264L;

	/**
	 * @param pos String
	 * @param red boolean
	 */
	public Elephant(String pos, boolean red) {
		super(new Position(pos), red);
	}

	/**
	 * @param pos Position
	 * @param red boolean
	 */
	public Elephant(Position pos, boolean red) {
		super(pos, red);
	}

	@Override
	public boolean checkMove(String move, HashMap<String, Figure> figures) {
		String[] moves = move.split("-");
		Position source = new Position(moves[0]);
		Position dist = new Position(moves[1]);
		return !this.isSameType(source, dist, figures)
				&& this.isBehindeRiverLimitation(source, dist, figures)
				&& this.isInMovementLimitation(source, dist, figures)
				&& this.isPathFree(source, dist, figures)
				&& !this.isDeathLook(move, figures);
	}

	protected boolean isPathFree(Position source, Position dist, HashMap<String, Figure> figures) {
		if(dist.getCol() > source.getCol() && dist.getRow() > source.getRow()) {
			return !figures.containsKey((new Position(dist.getRow()-1, dist.getCol()-1)).toString());
		}
		if(dist.getCol() > source.getCol() && dist.getRow() < source.getRow()) {
			return !figures.containsKey((new Position(dist.getRow()-1, dist.getCol()+1)).toString());
		}
		if(dist.getCol() < source.getCol() && dist.getRow() > source.getRow()) {
			return !figures.containsKey((new Position(dist.getRow()+1, dist.getCol()-1)).toString());
		}
		if(dist.getCol() < source.getCol() && dist.getRow() < source.getRow()) {
			return !figures.containsKey((new Position(dist.getRow()+1, dist.getCol()+1)).toString());

		}
		return  false;
	}

	/**
	 * @param source  Position
	 * @param dist    Position
	 * @param figures HashMap<String, Figure>
	 * @return boolean
	 */
	protected boolean isBehindeRiverLimitation(Position source, Position dist, HashMap<String, Figure> figures) {
		return this.red ? dist.getRow() <= 4 : dist.getRow() >= 5;
	}

	/**
	 * @param source  Position
	 * @param dist    Position
	 * @param figures HashMap<String, Figure>
	 * @return boolean
	 */
	protected boolean isInMovementLimitation(Position source, Position dist, HashMap<String, Figure> figures) {
		return (Math.abs(dist.getCol() - source.getCol()) == 2 && Math.abs(dist.getRow() - source.getRow()) == 2);

	}
}
