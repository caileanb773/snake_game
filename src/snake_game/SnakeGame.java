package snake_game;

import java.awt.CardLayout;
import java.net.URL;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
 * Author: Cailean Bernard
 * Contents: Contains the Frame for the game. The JFrame uses a cardLayout to
 * swap between the main menu screen and the actual game screen.
 */

public class SnakeGame {

	// jframe and cardlayout necessities
	private JFrame frame;
	private JPanel cardPanel;
	private CardLayout cardLayout;

	// game screens
	private MainMenuPanel menuPanel;
	private GamePanel gamePanel;

	public SnakeGame() {

		// init all swing components
		frame = new JFrame();
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);
		menuPanel = new MainMenuPanel(this);
		gamePanel = new GamePanel(this);

		// frame-specific settings
		frame.setTitle("Snake");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		URL iconURL = Main.class.getResource("/asset/icon.png");
		if (iconURL != null) {
			ImageIcon icon = new ImageIcon(iconURL);
			frame.setIconImage(icon.getImage());
		} else {
			System.out.println("Error retrieving icon image.");
		}

		// add game panels cardPanel, cardPanel to the frame
		cardPanel.add(menuPanel, "Menu");
		cardPanel.add(gamePanel, "Game");
		frame.getContentPane().add(cardPanel);

		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);			
	}

	public void showPanel(String name) {
		cardLayout.show(cardPanel, name);
	}

	public void startGame(String playerName) {
		gamePanel.startGame(playerName);
	}

	public void closeGame() {
		frame.dispose();
	}

	public void manageHiScores() {
		Map<String, Integer> scores = gamePanel.getHiScores();
		StringBuilder sb = new StringBuilder();
		for (String s : scores.keySet()) {
			sb.append(s).append(": ").append(scores.get(s)).append("\n");
		}
		
		JOptionPane.showMessageDialog(frame, sb.toString(), "High Scores", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void onHiScoresClicked() {
		List<Map.Entry<String, Integer>> sorted = gamePanel.getSortedHiScores(gamePanel.getHiScores());
		StringBuilder sb = new StringBuilder();
		
		for (Map.Entry<String, Integer> entry : sorted) {
			sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}
		
		JOptionPane.showMessageDialog(frame, sb.toString(), "High Scores", JOptionPane.INFORMATION_MESSAGE);

	}
}
