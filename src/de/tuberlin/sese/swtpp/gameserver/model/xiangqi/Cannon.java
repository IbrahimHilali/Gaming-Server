package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.HashMap;

public class Cannon extends Figure {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6196969243988544722L;

	/**
	 * @param pos String
	 * @param red boolean
	 */
	public Cannon(String pos, boolean red) {
		super(new Position(pos), red);
	}

	/**
	 * @param pos Position
	 * @param red boolean
	 */
	public Cannon(Position pos, boolean red) {
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
				&& (this.checkHorizontal(source, dist, figures) || this.checkVertical(source, dist, figures))
				&& !this.isDeathLook(move, figures);
	}

	/**
	 * @param source  Position
	 * @param dist    Position
	 * @param figures HashMap<String, Figure>
	 * @return boolean
	 */
	protected boolean checkHorizontal(Position source, Position dist, HashMap<String, Figure> figures) {
		Position p = new Position(source.toString());
		boolean notFound = true;
		int inBetween = p.getRow();
		if (dist.getCol() == source.getCol() && dist.getRow() != source.getRow()) {
		
			int step = dist.getRow() - source.getRow() > 0 ? 1 : -1;
			for (int i = source.getRow() + step; i != dist.getRow(); i += step) {
				p = new Position(i, p.getCol());
				if (figures.containsKey(p.toString()) && notFound) {
					notFound = false;
					inBetween = p.getRow();
				} else if (figures.containsKey(p.toString())) {
					return false;
				}
			}
			if(!notFound) {
				return this.isAttackMove(dist, figures) && (Math.abs(inBetween - dist.getRow())) > 1 ;
			}
			return !figures.containsKey(dist.toString());
		}
	
		return false;
	}

	private boolean isAttackMove(Position dist, HashMap<String, Figure> figures) {
		return figures.containsKey(dist.toString()) && (figures.get(dist.toString()).isRed() != this.isRed());
	}

	/**
	 * @param source  Position
	 * @param dist    Position
	 * @param figures HashMap<String, Figure>
	 * @return boolean
	 */
	protected boolean checkVertical(Position source, Position dist, HashMap<String, Figure> figures) {
		Position p = new Position(source.toString());
		boolean notFound = true;
		int inBetween = p.getCol();
		if (dist.getCol() != source.getCol() && dist.getRow() == source.getRow()) {
			int step = dist.getCol() - source.getCol() > 0 ? 1 : -1;
			for (int i = source.getCol() + step; i != dist.getCol(); i += step) {
				p = new Position(p.getRow(), i);
				if (figures.containsKey(p.toString()) && notFound) {
					notFound = false;
					inBetween = p.getCol();
				} else if (figures.containsKey(p.toString())) {
					return false;
				 }
			}
			if(!notFound) {
				return this.isAttackMove(dist, figures) && (Math.abs(inBetween - dist.getCol())) > 1 ? true : false;
			}
			return !figures.containsKey(dist.toString());
		}
		
		return false;

	}
}
