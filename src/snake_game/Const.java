package snake_game;

/*
 * Author: Cailean Bernard
 * Contents: Constants used within the scope of this project.
 */

public final class Const {

	// prevent instantiation
	private Const() {}

	static final int CELL_SIZE = 15; // pixels
	static final int NUM_COLS = 40;
	static final int NUM_ROWS = 40;
	static final int MAX_FOOD = (NUM_COLS - 2) * (NUM_ROWS - 2);
	static final int SNAKE_START_COL = NUM_COLS / 2;
	static final int SNAKE_START_ROW = NUM_ROWS / 2;
	static final int FPS = 18;
	static final int DELAY = 1000 / FPS; // ms per frame

}
