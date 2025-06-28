package snake_game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
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
import javax.swing.JSlider;
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
	private JButton settingsBtn;
	private JPanel smallBtnPanel;
	private JPanel btnPanel;
	private JLabel titleLbl;

	// Other fields
	private static final long serialVersionUID = -2394769013522953624L;
	private SnakeGame snakeGame;

	public MainMenuPanel(SnakeGame snakeGame) {

		// init misc fields
		titleLbl = new JLabel(getScaledIcon("/asset/title.png", 300, 100), SwingConstants.CENTER);
		smallBtnPanel = new JPanel(new FlowLayout());
		smallBtnPanel.setOpaque(false);
		this.snakeGame = snakeGame;

		// init buttons
		newGameBtn = new JButton();
		hiScoresBtn = new JButton();
		exitBtn = new JButton();
		helpBtn = new JButton();
		settingsBtn = new JButton();
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

		settingsBtn.setIcon(getScaledIcon("/asset/settings.png", 30, 30));
		settingsBtn.setRolloverIcon(getScaledIcon("/asset/settings_rollover.png", 30, 30));
		settingsBtn.setPressedIcon(getScaledIcon("/asset/settings_pressed.png", 30, 30));
		configBtn(settingsBtn);

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

		helpBtn.addActionListener(e -> showHelp());

		settingsBtn.addActionListener(e -> showSettings());

		// add buttons to button panel
		btnPanel.add(Box.createVerticalGlue());
		btnPanel.add(titleLbl);
		btnPanel.add(Box.createVerticalStrut(40));
		btnPanel.add(newGameBtn);
		btnPanel.add(Box.createVerticalStrut(20));
		btnPanel.add(hiScoresBtn);
		btnPanel.add(Box.createVerticalStrut(20));
		btnPanel.add(exitBtn);

		smallBtnPanel.add(helpBtn);
		smallBtnPanel.add(settingsBtn);

		btnPanel.add(smallBtnPanel);

		btnPanel.add(Box.createVerticalGlue());

		// center assets
		titleLbl.setAlignmentX(CENTER_ALIGNMENT); 
		newGameBtn.setAlignmentX(CENTER_ALIGNMENT);
		hiScoresBtn.setAlignmentX(CENTER_ALIGNMENT);
		exitBtn.setAlignmentX(CENTER_ALIGNMENT);
		helpBtn.setAlignmentX(CENTER_ALIGNMENT);
		settingsBtn.setAlignmentX(CENTER_ALIGNMENT);

		// add button panel to main menu panel
		setLayout(new BorderLayout());
		add(btnPanel, BorderLayout.CENTER);
	}

	public void showHelp() {
		JOptionPane.showMessageDialog(this, "Up: W/Up Arrow\n"
				+ "Left: A/Left Arrow\n"
				+ "Right: D/Right Arrow\n"
				+ "Down: S/Down Arrow\n"
				+ "Autoplay (greedy algorithm): P\n", 
				"Controls", 
				JOptionPane.INFORMATION_MESSAGE);
	}

	public void showSettings() {
		
		// window size
		JPanel winSize = new JPanel();
		JSlider winSizeSlider = new JSlider(JSlider.HORIZONTAL, 12, 30, Settings.getCellSize());
		JLabel winSizeLabel = new JLabel("Window Size:");
		winSizeSlider.setPaintTicks(true);
		winSizeSlider.setMajorTickSpacing(3);
		winSizeSlider.setMinorTickSpacing(1);
		winSizeSlider.setPaintLabels(true);
		winSize.add(winSizeLabel);
		winSize.add(winSizeSlider);

		// snake speed
		JPanel gameSpd = new JPanel();
		JSlider gameSpdSlider = new JSlider(JSlider.HORIZONTAL, 12, 25, Const.DEFAULT_FPS);
		JLabel gameSpdLabel = new JLabel("Game Speed (FPS):");
		gameSpdSlider.setPaintTicks(true);
		gameSpdSlider.setMajorTickSpacing(3);
		gameSpdSlider.setMinorTickSpacing(1);
		gameSpdSlider.setPaintLabels(true);
		gameSpd.add(gameSpdLabel);
		gameSpd.add(gameSpdSlider);
		
		// panel to hold both sliders
		JPanel settingsPanel = new JPanel();
		settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
		settingsPanel.add(Box.createVerticalGlue());
		settingsPanel.add(winSize);
		settingsPanel.add(Box.createVerticalStrut(10));
		settingsPanel.add(gameSpd);
		settingsPanel.add(Box.createVerticalGlue());
		
		// listeners for sliders
		winSizeSlider.addChangeListener(e -> onWindowSliderChanged(winSizeSlider.getValue()));
		
		gameSpdSlider.addChangeListener(e -> onGameSpeedSliderChanged(gameSpdSlider.getValue()));
		
		JOptionPane.showMessageDialog(this,
				settingsPanel,
				"Settings",
				JOptionPane.PLAIN_MESSAGE);
	}
	
	public void onGameSpeedSliderChanged(int newSpeed) {
		System.out.println("Game speed slider value: " + newSpeed);
		snakeGame.handleGameSpeedChanged(newSpeed);
	}
	
	public void onWindowSliderChanged(int newSize) {
		System.out.println("Window size slider value: " + newSize);
		snakeGame.handleResizeWindow(newSize);
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
