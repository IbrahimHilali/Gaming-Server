package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.io.Serializable;
import java.util.HashMap;

import de.tuberlin.sese.swtpp.gameserver.model.Move;

/**
 * @author Ibrahim Hilali
 *
 */
public class Board implements Serializable {

	private static final long serialVersionUID = 4465662630164173221L;

	/**
	 * 
	 */
	private HashMap<String, Figure> figures = new HashMap<>();

	/**
	 * @param state String
	 */
	public Board(String state) {
		this.setupFromState(state);
	}

	public void update(Move move) {
		String moves[] = move.getMove().split("-");
		Figure figure = this.figures.get(moves[0]);
		this.figures.remove(moves[0]);
		figure.position = new Position(moves[1]);
		this.figures.put(moves[1], figure);
	}

	/**
	 * @param state String
	 */
	public void setupFromState(String state) {
		this.figures.clear();
		String[] states = state.split("/");
		for (int row = states.length - 1, i = 0; row >= 0; row--, i++) {
			int col = 0;
			for (int j = 0; j < states[row].length(); j++) {
				if (Character.isDigit(states[row].charAt(j))) {
					col += (int) Integer.parseInt(Character.toString(states[row].charAt(j)));
					continue;
				}
				Position pos = new Position(i, col);
				Figure figure = make(pos, states[row].charAt(j));
				this.figures.put(pos.toString(), figure);
				col++;

			}

		}
	}

	/**
	 * Factory method to generate Figures
	 * 
	 * @param postion
	 * @param type
	 * @return Figure
	 */
	public Figure make(Position postion, char type) {
		switch (Character.toLowerCase(type)) {
		case 'g':
			return new General(postion, type == 'G');
		case 'a':
			return new Advisor(postion, type == 'A');
		case 'c':
			return new Cannon(postion, type == 'C');
		case 'h':
			return new Horse(postion, type == 'H');
		case 's':
			return new Soldier(postion, type == 'S');
		case 'r':
			return new Rook(postion, type == 'R');
		case 'e':
			return new Elephant(postion, type == 'E');
		}
		return null;
	}

	/**
	 * @return HashMap<String,Figure>
	 */
	public HashMap<String, Figure> getFigures() {
		return this.figures;
	}

	/**
	 * @param figures
	 */
	public void setFigures(HashMap<String, Figure> figures) {
		this.figures = figures;
	}

	@Override
	public String toString() {
		StringBuilder row = new StringBuilder();
		for (int i = 0; i <= 9; i++) {
			row.insert(0, "/" + buildColumns(i).toString());
		}
		row.delete(0, 1);
		return row.toString();
	}

	/**
	 * @param row int
	 * @return StringBuilder
	 */
	private StringBuilder buildColumns(int row) {
		StringBuilder col = new StringBuilder();
		int empty = 0;
		for (int j = 0; j < 9; j++) {
			Position p = new Position(row, j);
			if (this.figures.containsKey(p.toString())) {
				Figure figure = this.figures.get(p.toString());
				if (figure == null) continue;
				col.append(figure.toString());
				continue;
			}
			char c = col.length() > 0 ? col.charAt(col.length() - 1) : ' ';
			if (Character.isDigit(c)) {
				empty += Integer.parseInt(Character.toString(c));
				col.deleteCharAt(col.length() - 1);
			}
			col.append(++empty);
			empty = 0;

		}
		return col;
	}

	/**
	 * @param move String
	 * @return boolean
	 */
	public boolean isRedMove(String move) {
		String p = move.split("-")[0];
		return this.getFigures().get(p).red;
	}

	/**
	 * @param move Move
	 * @return boolean
	 */
	public boolean checkMate(Move move, boolean currentPlayer) {
		Figure[] generals = figures.values().stream().filter(item -> item.getClass() == General.class)
				.toArray(Figure[]::new);
		if (generals.length < 2)
			return false;

		Figure general = generals[0].red == currentPlayer ? generals[1] : generals[0];
		String attackMove = String.format("%s-%s", move.getMove().split("-")[1], general.getPosition().toString());
		return this.isGeneralThreaten(general, attackMove, this.figures) && !this.hasEscapeMove(general, attackMove);
	}

	/**
	 * @param generals    Figure
	 * @param move        Move
	 * @param redPlayNext boolean
	 * @return boolean
	 */
	public boolean hasEscapeMove(Figure general, String move) {
		return this.canGeneralAttack(general, move) || this.canGeneralEscape(general, move)
				|| this.canFigurePreventCheckMate(general, move);
	}

	/**
	 * @param general     Figure
	 * @param move        Move
	 * @param redPlayNext boolean
	 * @return boolean
	 */
	private boolean canGeneralAttack(Figure general, String move) {
		Position dist = new Position(move.split("-")[1]);
		String m = String.format("%s-%s", general.getPosition().toString(), dist.toString());
		return general.checkMove(m, this.figures);
	}

	/**
	 * @param general Figure
	 * @param move    String
	 * @return boolean
	 */
	private boolean canFigurePreventCheckMate(Figure general, String move) {
		for (int i = 0; i <= 9; i++) {
			for (int j = 0; j < 9; j++) { 
				return checkIfGeneralThreaten(general, move, new Position(i, j));
			}
		}
		return false;
	}

	/**
	 * @param general Figure
	 * @param move    STring
	 * @param p       Position
	 * @return boolean
	 */
	private boolean checkIfGeneralThreaten(Figure general, String move, Position p) {
		for (Figure figure : this.figures.values()) {
			String m = figure.getPosition().toString() + "-" + p.toString();
			if (figure.getClass() != general.getClass() && figure.checkMove(m, this.figures)
					&& general.red == figure.isRed()) {
				HashMap<String, Figure> figures = general.applyMoveOnClone(move, this.figures);
				if (!this.isGeneralThreaten(general, move, figures)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param general Figure
	 * @param move String
	 * @return boolean
	 */
	private boolean canGeneralEscape(Figure general, String move) {
		String escapePos = general.red ? "d0 d1 d2 e0 e1 e2 f0 f1 f2" : "d9 d8 d7 e9 e8 e7 f9 f8 f7";
		Position basic = general.getPosition();
		for (String pos : escapePos.split(" ")) {
			if (pos.equals(basic.toString()))
				continue;
			String m = basic.toString() + "-" + pos;
			if (!this.figures.containsKey(pos) && general.checkMove(m, this.figures)) {
				General g = new General(new Position(pos), general.red);
				HashMap<String, Figure> f = (HashMap<String, Figure>) g.applyMoveOnClone(m, this.figures);
				if (!this.isGeneralThreaten(g, m, f) && !pos.equals(g.position.toString())) {
					return true;
				}

			}
		}
		return false;
	}
	
	/**
	 * @param generals Figure
	 * @param move     Move
	 * @return boolean
	 */
	public boolean isGeneralThreaten(Figure general, String move, HashMap<String, Figure> figures) {
		HashMap<String, Figure> f = general.CloneHashMap(figures);
		for (Figure figure : f.values()) {
			String m = String.format("%s-%s", figure.position.toString(), general.position.toString());
			if (figure.getClass() != General.class && figure.checkMove(m, f) && figure.red == !general.red) {
				return true;
			}

		}
		return false;
	}

	/**
	 * @param move Move
	 * @return boolean
	 */
	public boolean checkMove(String move) {
		Position source = new Position(move.split("-")[0]);
		Figure figure = this.figures.get(source.toString());
		return figure.checkMove(move, this.figures);
	}

}
