package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.HashMap;

/**
 * @author Ibrahim Hilali
 *
 */
public class Rook extends Figure {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6799694132035246638L;

	/**
	 * @param pos String
	 * @param red boolean
	 */
	public Rook(String pos, boolean red) {
		super(new Position(pos), red);
	}

	/**
	 * @param pos Position
	 * @param red boolean
	 */
	public Rook(Position pos, boolean red) {
		super(pos, red);
	}

	@Override
	public boolean checkMove(String move, HashMap<String, Figure> figures) {
		String[] moves = move.split("-");
		Position source = new Position(moves[0]);
		Position dist = new Position(moves[1]);
		return !this.isSameType(source, dist, figures) && (this.checkHorizontal(source, dist, figures)
				|| this.checkVertical(source, dist, figures)) && !this.isDeathLook(move, figures);
	}

	/**
	 * @param source  Position
	 * @param dist    Position
	 * @param figures HashMap<String, Figure>
	 * @return boolean
	 */
	protected boolean checkHorizontal(Position source, Position dist, HashMap<String, Figure> figures) {
		Position p = source;
		if (dist.getCol() == source.getCol() && dist.getRow() != source.getRow()) {
			int step = dist.getRow() - source.getRow() > 0 ? 1 : -1;
			for (int i = source.getRow() + step; i != dist.getRow(); i += step) {
				p.setRow(i);
				if (figures.containsKey(p.toString())) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * @param source  Position
	 * @param dist    Position
	 * @param figures HashMap<String, Figure>
	 * @return boolean
	 */
	protected boolean checkVertical(Position source, Position dist, HashMap<String, Figure> figures) {
		Position p = source;
		if (dist.getCol() != source.getCol() && dist.getRow() == source.getRow()) {
			int step = dist.getCol() - source.getCol() > 0 ? 1 : -1;
			for (int i = source.getCol() + step; i != dist.getCol(); i += step) {
				p.setCol(i);
				if (figures.containsKey(p.toString())) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
