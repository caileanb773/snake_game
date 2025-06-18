package snake_game;

/*
 * Author: Cailean Bernard
 * Contents: Enumeration for the different 'types' a cell can have. Cell type
 * is used to determine how the snake interacts with certain cells, such as with
 * collision with food/walls/itself.
 */

public enum CellType {
	
	EMPTY, FOOD, HORIZONTAL_WALL, VERTICAL_WALL, SNAKE

}
