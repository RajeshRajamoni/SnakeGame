package Programs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	static final int DELAY = 75;
	final int x[] = new int[GAME_UNITS]; // x cordinate of body of the snake
	final int y[] = new int[GAME_UNITS]; // y coordinate of body of the snake
	int bodyParts = 5;
	int applesEaten = 0;
	int appleX;
	int appleY; // random position of apple
	char direction = 'R'; // snake begin to flow in right direction!
	boolean running = false;
	Timer timer;
	Random random;

	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}

	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {

		if (running) {
			g.setColor(Color.RED);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

			for (int i = 0; i < bodyParts; i++) {
				g.setColor(Color.green);
				/*
				 * g.setColor(new
				 * Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
				 */
				g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}

			g.setColor(Color.blue);
			g.setFont(new Font("Times New Roman", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont()); // provides dimensions of that font
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
					SCREEN_HEIGHT / 10);
		} else {
			gameOver(g);
		}
	}

	public void newApple() {
		appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
	}

	public void move() {
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		switch (direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;

		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;

		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;

		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}

	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}

	public void checkCollisions() {
		// head touches body of the snake!
		for (int i = bodyParts; i > 0; i--) {
			if (x[0] == x[i] && y[0] == y[i] || // Check if head collides with any body part
					x[0] < 0 || x[0] > SCREEN_WIDTH || // Check if head is out of bounds horizontally
					y[0] < 0 || y[0] > SCREEN_HEIGHT) { // Check if head is out of bounds vertically
				running = false;
				break; // No need to continue checking if running is already false
			}
		}
		if (!running) {
			timer.stop();
		}
	}

	public void gameOver(Graphics g) {
		// score text
		g.setColor(Color.blue);
		g.setFont(new Font("Times New Roman", Font.BOLD, 40));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
				SCREEN_HEIGHT / 10);

		// game over text
		g.setColor(Color.blue);
		g.setFont(new Font("Times New Roman", Font.BOLD, 75));
		g.drawString("Game Over", SCREEN_WIDTH / 5, SCREEN_HEIGHT / 2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter {
		// MyKeyAdapter to handle keyBoard Input

		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}

}