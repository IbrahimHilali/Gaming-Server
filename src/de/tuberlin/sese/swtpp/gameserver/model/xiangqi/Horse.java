package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.HashMap;

/**
 * @author Ibrahim Hilali
 *
 */
public class Horse extends Figure {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8431863812291234513L;

	/**
	 * @param pos String
	 * @param red boolean
	 */
	public Horse(String pos, boolean red) {
		super(new Position(pos), red);
	}

	/**
	 * @param pos Position
	 * @param red boolean
	 */
	public Horse(Position pos, boolean red) {
		super(pos, red);
	}

	@Override
	public boolean checkMove(String move, HashMap<String, Figure> figures) {
		String[] moves = move.split("-");
		Position source = new Position(moves[0]);
		Position dist = new Position(moves[1]);
		return !this.isSameType(source, dist, figures) 
				&& (this.isRowFirstPathFree(source, dist, figures)
				|| this.isColFirstPathFree(source, dist, figures)) 
				&& !this.isDeathLook(move, figures);
	}

	/**
	 * @param source Position
	 * @param dist Position 
	 * @param figures 
	 * @return
	 */
	protected boolean isRowFirstPathFree(Position source, Position dist, HashMap<String, Figure> figures) {
		if ((Math.abs(dist.getCol() - source.getCol()) == 1 && Math.abs(dist.getRow() - source.getRow()) == 2)) {
			int step = dist.getRow() - source.getRow() > 0 ? 1 : -1;
			Position p = new Position(source.getRow() + step, source.getCol());
			return !figures.containsKey(p.toString());
		}
	
		return false;
	}

	
	/**
	 * @param source Position
	 * @param dist Position
	 * @param figures HashMap<String, Figure> 
	 * @return boolean HashMap<String, Figure> 
	 */
	protected boolean isColFirstPathFree(Position source, Position dist, HashMap<String, Figure> figures) {
		if ((Math.abs(dist.getCol() - source.getCol()) == 2 && Math.abs(dist.getRow() - source.getRow()) == 1)) {
			int step = dist.getCol() - source.getCol() > 0 ? 1 : -1;
			Position p = new Position(source.getRow(), source.getCol() + step);
			return !figures.containsKey(p.toString());
		}
		
		return false;
	}

}
