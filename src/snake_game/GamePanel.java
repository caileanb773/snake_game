package snake_game;

import static snake_game.Const.DEFAULT_DELAY;
import static snake_game.Const.NUM_COLS;
import static snake_game.Const.NUM_ROWS;
import static snake_game.Const.SNAKE_START_COL;
import static snake_game.Const.SNAKE_START_ROW;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/*
 * Author: Cailean Bernard
 * Contents: Extends JPanel and contains the interface that the user interacts
 * with.
 */

public class GamePanel extends JPanel {

	// Gamestate-specific fields
	private Cell[][] gameArea;
	private boolean isGameOver;
	private int foodEaten;
	private Timer timer;
	private Point foodPnt;
	private String playerName;
	private boolean autoPlay;

	// Snake-specific fields
	private LinkedList<Point> snake;
	private Direction snakeDir;
	private int snakeCol;
	private int snakeRow;

	// Sounds
	private Clip eatSound;

	// Other
	private static final long serialVersionUID = -2969235666770895319L;
	private SnakeGame snakeGame;
	private Map<String, Integer> hiScores;
	private int delay;

	public GamePanel(SnakeGame snakeGame) {

		// init fields
		this.snakeGame = snakeGame;
		hiScores = new HashMap<>();
		readHiScores(); // load the hiscores from hiscores.txt, if it exists
		delay = DEFAULT_DELAY;

		// jframe settings
		int cellSize = Settings.getCellSize();
		setPreferredSize(new Dimension(NUM_COLS * cellSize, NUM_ROWS * cellSize));
		setFocusable(true);
		requestFocusInWindow();
		gameArea = new Cell[NUM_ROWS][NUM_COLS];
		isGameOver = false;
		autoPlay = false;
		foodEaten = 0;

		// initialize each game cell to be empty
		initGameArea();

		// snake initialization
		initSnake();

		// load sounds
		initSound();

		// spawn the first food
		spawnFood();

		// game loop
		timer = new Timer(delay, e -> {

			if (autoPlay)
				moveTowardFood();
			updateGame();
			repaint();
		});

		// input handler
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				switch (key) {
				case KeyEvent.VK_W:
				case KeyEvent.VK_UP:
					if (snakeDir != Direction.DOWN) snakeDir = Direction.UP;
					break;
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
					if (snakeDir != Direction.UP) snakeDir = Direction.DOWN;
					break;
				case KeyEvent.VK_A:
				case KeyEvent.VK_LEFT:
					if (snakeDir != Direction.RIGHT) snakeDir = Direction.LEFT;
					break;
				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
					if (snakeDir != Direction.LEFT) snakeDir = Direction.RIGHT;
					break;
				case KeyEvent.VK_P:
					autoPlay = !autoPlay;
					System.out.println("AutoPlay on: " + autoPlay);
					break;
				}
			}
		});
	}

	public void resizeWindow(int newCellSize) {
		Settings.setCellSize(newCellSize);
		setPreferredSize(new Dimension(NUM_COLS * newCellSize, NUM_ROWS * newCellSize));
		revalidate();
		repaint();
	}

	public void startGame(String playerName) {
		this.playerName = playerName;
		requestFocusInWindow();

		// starting the timer effectively starts the game
		timer.start();
	}

	public void initSnake() {
		snakeCol = SNAKE_START_COL;
		snakeRow = SNAKE_START_ROW;
		snakeDir = Direction.RIGHT;
		snake = new LinkedList<>();
		snake.add(new Point(SNAKE_START_COL, SNAKE_START_ROW));
		gameArea[snakeRow][snakeCol].setCellType(CellType.SNAKE);
	}

	public void initGameArea() {
		for (int row = 0; row < NUM_ROWS; row++) {
			for (int col = 0; col < NUM_COLS; col++) {
				if (row == 0 || row == NUM_ROWS - 1) {
					gameArea[row][col] = new Cell(row, col, CellType.HORIZONTAL_WALL);
				} else if (col == 0 || col == NUM_COLS - 1) {
					gameArea[row][col] = new Cell(row, col, CellType.VERTICAL_WALL);
				} else {
					gameArea[row][col] = new Cell(row, col, CellType.EMPTY);
				}
			}
		}
	}

	public void initSound() {
		try {
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(
					getClass().getResource("/asset/eatFood.wav"));
			eatSound = AudioSystem.getClip();
			eatSound.open(audioIn);
		} catch (Exception e) {
			// should catch more specific exceptions than just "Exception"
			e.printStackTrace();
		}
	}

	public void playEatSound() {
		if (eatSound != null) {
			if (eatSound.isRunning()) {
				eatSound.stop();
			}
			eatSound.setFramePosition(0);
			eatSound.start();
		}
	}

	public void resetGame() {
		gameArea = new Cell[NUM_ROWS][NUM_COLS];
		isGameOver = false;
		foodEaten = 0;
		snake.clear();
		initGameArea();
		initSnake();
		spawnFood();
	}

	public void addPlayerToScores() {

		// "foodEaten" is representative of the player's score
		System.out.println("Adding " + playerName + ": " + foodEaten);
		hiScores.put(playerName, foodEaten);
	}

	public void updateGame() {
		if (isGameOver) {

			// stop game
			timer.stop();

			int choice = JOptionPane.showConfirmDialog(
					this,
					"Game Over! Your score was: " + foodEaten +  ". Play again?",
					"Game Over",
					JOptionPane.YES_NO_OPTION
					);

			if (choice == JOptionPane.YES_OPTION) {
				resetGame();
				timer.start();
			} else {
				addPlayerToScores();
				writeHiScores();
				resetGame();
				snakeGame.showPanel("Menu");
			}
			return;
		}

		int nextSnakeRow = snakeRow;
		int nextSnakeCol = snakeCol;
		boolean foodEncountered = false;

		switch (snakeDir) {
		case UP: nextSnakeRow--; break;
		case DOWN: nextSnakeRow++; break;
		case LEFT: nextSnakeCol--; break;
		case RIGHT: nextSnakeCol++; break;
		}

		// check the type of the next cell
		CellType nextCell = gameArea[nextSnakeRow][nextSnakeCol].getCellType();

		/* if it's empty, just move the snake to the next coordinate this is the
		 * most likely scenario so it is checked first */
		switch (nextCell) {
		case EMPTY: // just move the snake
			break;
		case FOOD: // set foodEaten = true, don't delete the tail
			foodEncountered = true;
			break;
		default: // Wall or snake
			isGameOver = true;
			return;
		}

		snake.add(new Point(nextSnakeRow, nextSnakeCol));
		if (!foodEncountered) {
			Point tail = snake.removeFirst();
			gameArea[tail.x][tail.y].setCellType(CellType.EMPTY);
		} else {
			playEatSound();
			spawnFood();
			foodEaten++;
		}

		snakeRow = nextSnakeRow;
		snakeCol = nextSnakeCol;

		for (Point p : snake) {
			gameArea[p.x][p.y].setCellType(CellType.SNAKE);
		}

	}

	public void moveTowardFood() {
		if (foodPnt == null) { return; }

		Point head = snake.getLast();

		int dx = foodPnt.x - head.x;
		int dy = foodPnt.y - head.y;

		if (dx != 0 && isSafe(head.x + Integer.signum(dx), head.y)) {
			snakeDir = dx < 0 ? Direction.UP : Direction.DOWN;
		} else if (dy != 0 && isSafe(head.x, head.y + Integer.signum(dy))) {
			snakeDir = dy < 0 ? Direction.LEFT : Direction.RIGHT;
		}
	}

	private boolean isSafe(int row, int col) {
		if (row < 0 || row >= NUM_ROWS || col < 0 || col >= NUM_COLS) return false;
		CellType type = gameArea[row][col].getCellType();
		return type == CellType.EMPTY || type == CellType.FOOD;
	}

	public void cleanup() {
		initGameArea();
	}

	public void spawnFood() {
		if (foodEaten >= Const.MAX_FOOD) {
			// "You win" dialog?
			isGameOver = true;
			return;
		}

		int row, col;
		CellType foodCell;

		do {
			row = getRandomCoord(NUM_ROWS - 2) + 1;
			col = getRandomCoord(NUM_COLS - 2) + 1;
			foodCell = gameArea[row][col].getCellType();

		} while (foodCell == CellType.SNAKE ||
				foodCell == CellType.HORIZONTAL_WALL ||
				foodCell == CellType.VERTICAL_WALL);

		foodPnt = new Point(row, col);
		gameArea[row][col].setCellType(CellType.FOOD);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		for (int row = 0; row < NUM_ROWS; row++) {
			for (int col = 0; col < NUM_COLS; col++) {
				Cell cell = gameArea[row][col];

				switch (cell.getCellType()) {
				case EMPTY:
					g.setColor(Color.black);
					break;
				case FOOD:
					g.setColor(Color.green);
					break;
				case SNAKE:
					g.setColor(Color.white);
					break;
				case HORIZONTAL_WALL:
					g.setColor(Color.red);
					break;
				case VERTICAL_WALL:
					g.setColor(Color.red);
					break;
				}

				int cellSize = Settings.getCellSize();
				int x = col * cellSize;
				int y = row * cellSize;
				g.fillRect(x, y, cellSize, cellSize);
			}
		}			 
	}

	public void writeHiScores() {
		System.out.println("Attempting write to hiScores.txt...");
		String path = "hiScores.txt";
		StringBuilder sb = new StringBuilder();

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {

			// iterate over hiScore map, and append/format entries
			for (String name : hiScores.keySet()) {
				sb.append(name).append(",").append(hiScores.get(name)).append("\n");
			}

			System.out.println("Writing " + sb.toString() + " to disk.");
			// write contents of hiScores to hiScores.txt
			writer.write(sb.toString());

		} catch (IOException e) {
			System.err.println("IO Exception encountered in saveScore().");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("General exception caught in saveScore().");
			e.printStackTrace();
		}
	}

	private void readHiScores() {
		System.out.println("Attempting read from hiScores.txt...");
		File hiScoresPath = new File("hiScores.txt");
		if (hiScoresPath.exists()) {
			System.out.println("hiScores.txt found.");
			try (BufferedReader reader = new BufferedReader(new FileReader (hiScoresPath))) {
				String line;

				while ((line = reader.readLine()) != null) {
					String[] lineData = line.split(",");
					String name = lineData[0];
					int score = Integer.parseInt(lineData[1]);
					hiScores.put(name, score);
				}
			} catch (FileNotFoundException e) {
				System.err.println("File coult not be found by readHiScores().");
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("Generic IOException thrown in readHiScores().");
				e.printStackTrace();
			} catch (Exception e) {
				System.err.println("Generic Exception thrown in readHiScores().");
				e.printStackTrace();
			}
		} else {
			System.err.println("Could not find 'hiScores.txt'.");
			return;
		}
	}

	public List<Map.Entry<String, Integer>> getSortedHiScores(Map<String, Integer> unsorted) {
		List<Map.Entry<String, Integer>> sorted = new ArrayList<>(unsorted.entrySet());

		sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));;

		return sorted;
	}

	private int getRandomCoord(int clamp) { return new Random().nextInt(clamp); }

	public void setUserName(String name) { playerName = name; }

	public Map<String, Integer> getHiScores() { return hiScores; }

	public void setDelay(int delay) {
		this.delay = delay;
		timer.setDelay(delay);
	}

}
