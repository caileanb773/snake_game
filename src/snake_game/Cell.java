package snake_game;

/*
 * Author: Cailean Bernard
 * Contents: A 'Cell' is a game-area component that has an x and y coordinate
 * as well as a Cell 'Type' which is represented by an enum.
 */

public class Cell {
	
	private int x;
	private int y;
	private CellType cellType;
	
	public Cell() {}
	
	public Cell(int x, int y, CellType cellType) {
		this.x = x;
		this.y = y;
		this.cellType = cellType;
	}

	public void setCellType(CellType cellType) {
		this.cellType = cellType;
	}
	
	public CellType getCellType() { return cellType; }
	public int getX() { return x; }
	public int getY() { return y; }
}
