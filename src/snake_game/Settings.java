package snake_game;

/*
 * Author: Cailean Bernard
 * Contents: 
 */

public class Settings {
	
	private static int cellSize = Const.DEFAULT_CELL_SIZE;
	private static int gameSpeed = Const.DEFAULT_FPS;

	public static int getCellSize() { return cellSize; }
	public static int getSnakeSpeed() { return gameSpeed; }
	public static void setCellSize(int newSize) { cellSize = newSize; }
	
}
