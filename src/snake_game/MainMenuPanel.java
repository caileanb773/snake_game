package snake_game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/*
 * Author: Cailean Bernard
 * Contents: Swing layout for the Main menu, where the user can either start a 
 * new game, check the hi-scores, or exit.
 */

public class MainMenuPanel extends JPanel {

	// Swing-specific fields
	private JButton newGameBtn;
	private JButton hiScoresBtn;
	private JButton exitBtn;
	private JButton helpBtn;
	private JPanel btnPanel;
	private JLabel titleLbl;
	
	// Other fields
	private static final long serialVersionUID = -2394769013522953624L;

	public MainMenuPanel(SnakeGame snakeGame) {

		// init misc fields
		titleLbl = new JLabel(getScaledIcon("/asset/title.png", 300, 100), SwingConstants.CENTER);

		// init buttons
		newGameBtn = new JButton();
		hiScoresBtn = new JButton();
		exitBtn = new JButton();
		helpBtn = new JButton();
		btnPanel = new JPanel();
		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));

		// set button images
		newGameBtn.setIcon(getScaledIcon("/asset/newGame.png", 150, 50));
		newGameBtn.setRolloverIcon(getScaledIcon("/asset/newGame_rollover.png", 150, 50));
		newGameBtn.setPressedIcon(getScaledIcon("/asset/newGame_pressed.png", 150, 50));
		configBtn(newGameBtn);

		hiScoresBtn.setIcon(getScaledIcon("/asset/hiScores.png", 150, 50));
		hiScoresBtn.setRolloverIcon(getScaledIcon("/asset/hiScores_rollover.png", 150, 50));
		hiScoresBtn.setPressedIcon(getScaledIcon("/asset/hiScores_pressed.png", 150, 50));
		configBtn(hiScoresBtn);

		exitBtn.setIcon(getScaledIcon("/asset/exit.png", 150, 50));
		exitBtn.setRolloverIcon(getScaledIcon("/asset/exit_rollover.png", 150, 50));
		exitBtn.setPressedIcon(getScaledIcon("/asset/exit_pressed.png", 150, 50));
		configBtn(exitBtn);

		helpBtn.setIcon(getScaledIcon("/asset/help.png", 30, 30));
		helpBtn.setRolloverIcon(getScaledIcon("/asset/help_rollover.png", 30, 30));
		helpBtn.setPressedIcon(getScaledIcon("/asset/help_pressed.png", 30, 30));
		configBtn(helpBtn);

		// settings for buttonpanel
		btnPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		btnPanel.setBackground(new Color(223, 192, 192));

		// set action listeners for buttons
		newGameBtn.addActionListener(e -> {
			String playerName = collectUserName();
			if (playerName != null) {
				snakeGame.showPanel("Game");
				snakeGame.startGame(playerName);
			} else {
				JOptionPane.showMessageDialog(this, "You must enter a name.", 
						"Warning",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		hiScoresBtn.addActionListener(e -> snakeGame.onHiScoresClicked());

		exitBtn.addActionListener(e -> snakeGame.closeGame());

		helpBtn.addActionListener(e -> {
			JOptionPane.showMessageDialog(this, "Up: W/Up Arrow\n"
					+ "Left: A/Left Arrow\n"
					+ "Right: D/Right Arrow\n"
					+ "Down: S/Down Arrow\n"
					+ "Autoplay (greedy algorithm): P\n", 
					"Controls", 
					JOptionPane.INFORMATION_MESSAGE);
		});

		// add buttons to button panel
		btnPanel.add(Box.createVerticalGlue());
		btnPanel.add(titleLbl);
		btnPanel.add(Box.createVerticalStrut(40));
		btnPanel.add(newGameBtn);
		btnPanel.add(Box.createVerticalStrut(20));
		btnPanel.add(hiScoresBtn);
		btnPanel.add(Box.createVerticalStrut(20));
		btnPanel.add(exitBtn);
		btnPanel.add(helpBtn);
		btnPanel.add(Box.createVerticalGlue());

		// center assets
		titleLbl.setAlignmentX(CENTER_ALIGNMENT); 
		newGameBtn.setAlignmentX(CENTER_ALIGNMENT);
		hiScoresBtn.setAlignmentX(CENTER_ALIGNMENT);
		exitBtn.setAlignmentX(CENTER_ALIGNMENT);
		helpBtn.setAlignmentX(CENTER_ALIGNMENT);

		// add button panel to main menu panel
		setLayout(new BorderLayout());
		add(btnPanel, BorderLayout.CENTER);
	}

	private ImageIcon getScaledIcon(String path, int x, int y) {
		URL url = getClass().getResource(path);
		ImageIcon rawIcon = new ImageIcon(url);
		Image rawImage = rawIcon.getImage();
		Image scaledImage = rawImage.getScaledInstance(x, y, Image.SCALE_SMOOTH);
		ImageIcon smoothedIcon = new ImageIcon(scaledImage);
		return smoothedIcon;
	}

	private void configBtn(JButton btn) {
		btn.setContentAreaFilled(false);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
	}

	public String collectUserName() {
		String playerName = JOptionPane.showInputDialog(this,
				"Enter your name: ",
				"Player Name",
				JOptionPane.PLAIN_MESSAGE);

		if (playerName != null && !playerName.isEmpty()) {
			return playerName;
		}

		else
			return null;
	}

}
