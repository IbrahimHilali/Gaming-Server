package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author Ibrahim Hilali
 *
 */
public abstract class Figure implements Serializable {

	private static final long serialVersionUID = 6960200064173281L;

	/**
	 * 
	 */
	protected Position position;
	protected boolean red;
	protected HashSet<String> threatArea;

	/**
	 * @param pos Position
	 * @param red boolean
	 */
	public Figure(Position pos, boolean red) {
		this.position = pos;
		this.red = red;
	}

	/**
	 * @return Position
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * @param position Position
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	/**
	 * @return boolean
	 */
	public boolean isRed() {
		return red;
	}

	/**
	 * @param red boolean
	 */
	public void setRed(boolean red) {
		this.red = red;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != this.getClass() || obj == null)
			return false;
		Figure f = (Figure) obj;
		return this.getPosition().equals(f.getPosition()) && this.isRed() == f.isRed();
	}

	@Override
	public String toString() {
		return this.red ? getClass().getSimpleName().substring(0, 1)
				: getClass().getSimpleName().substring(0, 1).toLowerCase();
	}

	/**
	 * @param source  Position
	 * @param dist    Position
	 * @param figures HashMap<String, Figure>
	 * @return boolean
	 */
	protected boolean isSameType(Position source, Position dist, HashMap<String, Figure> figures) {
		return figures.containsKey(source.toString()) && figures.containsKey(dist.toString())
				? figures.get(source.toString()).red == figures.get(dist.toString()).red
				: false;
	}

	/**
	 * @param move    Move
	 * @param figures HashMap<String, Figure>
	 * @return boolean
	 */
	protected boolean isDeathLook(String move, HashMap<String, Figure> figures) {
		HashMap<String , Figure> temp = this.applyMoveOnClone(move, figures);
		Figure[] generals = temp.values().stream().filter(item -> item.getClass() == General.class)
				.toArray(Figure[]::new);

		if (generals.length < 2)
			return false;
		Figure redGeneral = generals[0].red ?  generals[0] :  generals[1];
		Figure blackGeneral = !generals[0].red ?  generals[0] :  generals[1];
		if(redGeneral.getPosition().getCol() != blackGeneral.getPosition().getCol()) {
			return false;
		}
	
		Position pos = new Position(redGeneral.getPosition().toString());
	
		for (int i = redGeneral.getPosition().getRow() + 1; i < blackGeneral.getPosition().getRow() ; i++) {
			pos.setRow(i);
			if(temp.containsKey(pos.toString())) {
				return false;
			}
		}
		return true;
	}


	/**
	 * @param move    Move
	 * @param figures HashMap<String, Figure>
	 * @return boolean
	 */
	public HashMap<String, Figure> applyMoveOnClone(String move, HashMap<String, Figure> figures) {
		HashMap<String, Figure> temp = CloneHashMap(figures);
		String[] moves = move.split("-");
		Figure figure = temp.get(moves[0]);
		temp.remove(moves[0]);
		figure.setPosition(new Position(moves[1]));
		temp.put(moves[1], figure);
		return (HashMap<String, Figure>) temp;
	}

	/**
	 * @param figures HashMap<String, Figure>
	 * @return HashMap<String, Figure>
	 */
	public HashMap<String, Figure> CloneHashMap(HashMap<String, Figure> figures) {
		HashMap<String, Figure> temp = new HashMap<String, Figure>();
		for (Map.Entry<String, Figure> entry : figures.entrySet()) {
			newChildFromParent(temp, entry);
		}
		return temp;
	}

	private void newChildFromParent(HashMap<String, Figure> temp, Map.Entry<String, Figure> entry) {
		Position p = new Position(entry.getValue().getPosition().toString());
		boolean b = entry.getValue().isRed();
		Class<? extends Figure> clazz = entry.getValue().getClass();
		try {
			temp.put(p.toString(),
					clazz.getDeclaredConstructor(new Class[] { Position.class, boolean.class }).newInstance(p, b));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return boolean
	 */
	public abstract boolean checkMove(String move, HashMap<String, Figure> figures);

}
