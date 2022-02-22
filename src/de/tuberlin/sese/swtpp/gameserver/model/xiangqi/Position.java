package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.io.Serializable;

/**
 * @author Ibrahim Hilali
 *
 */
public class Position implements Serializable {

    private static final long serialVersionUID = 8104952702236150205L;
    private static final String columns = "abcdefghi";

	private int col;
    private int row; 
    
    public Position(String position){
    	this.col = columns.indexOf(position.charAt(0));
    	this.row =  Integer.parseInt(String.valueOf(position.charAt(1)));
    	
    }
    public Position(int row , int col){
    	this.col = col;
    	this.row = row;
    }
    
    public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj.getClass()  != this.getClass() || obj == null) return false;
		Position p = (Position) obj;
		return this.getCol() == p.getCol() && this.getRow() == p.getRow();
	}
	@Override
	public String toString() {	
		return String.format("%s%d", columns.charAt(this.col) , this.row);
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Position p  =(Position) super.clone();
		p.setCol(col);
		p.setRow(row);
		return (Object) p;
	}
	

}
