package com.pingpong.game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GameObjects extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;
	private int WIDTH, HEIGHT;
	private int x;
	private int y;
	private int speed = 2;
	private int renderTime = 10;
	private int velX = speed;
	private int velY = speed;
	private int velPlayer = 5;
	private int offset = 10;
	private int r = 10;
	private int XPlayerA;
	private int YPlayerA;
	private int XPlayerB;
	private int YPlayerB;
	private int scoreA = 0;
	private int scoreB = 0;
	private int collisionCount;
	private boolean running = false;
	public Timer ADown;
	public Timer AUp;
	public Timer BDown;
	public Timer BUp;

	public void setScoreA(int scoreA) {
		this.scoreA = scoreA;
	}

	public void setScoreB(int scoreB) {
		this.scoreB = scoreB;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public GameObjects(int height, int width) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.YPlayerA = height / 2 - 35;
		this.XPlayerA = 10;
		this.YPlayerB = height / 2 - 35;
		this.XPlayerB = width - 15;
		this.x = WIDTH / 2 - 5;
		this.y = HEIGHT / 2;
		this.collisionCount = 0;

		AUp = new Timer(renderTime, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (YPlayerA - 5 >= 0) {
					YPlayerA -= velPlayer;
				}
			}
		});
		ADown = new Timer(renderTime, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (YPlayerA + 80 <= height) {
					YPlayerA += velPlayer;
				}
			}
		});
		BUp = new Timer(renderTime, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (YPlayerB - 5 >= 0) {
					YPlayerB -= velPlayer;
				}

			}
		});
		BDown = new Timer(renderTime, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (YPlayerB + 80 <= height) {
					YPlayerB += velPlayer;
				}
			}
		});
	}

	public void paint(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
		drawDashedLine(g2d);
		g2d.setColor(Color.red);
		g2d.fillOval(x, y, r, r);
		drawPlayers(g2d);
		drawScore(g2d);
		checkScore(g2d);
		if (!running) {
			drawMenu(g2d);
		}
	}

	public void startAnimation() throws InterruptedException {
		(new Thread(this)).start();
		System.out.println("start animation");
	}

	public void run() {
		while (running) {
			moveBall();
		}
	}

	public void drawDashedLine(Graphics2D g2d) {
		g2d.setColor(Color.white);
		Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0);
		g2d.setStroke(dashed);
		g2d.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
	}

	public void drawScore(Graphics2D g2d) {
		g2d.setFont(new Font("default", Font.BOLD, 24));
		g2d.drawString(Integer.toString(scoreA), WIDTH / 2 - 50, 40);
		g2d.drawString(Integer.toString(scoreB), WIDTH / 2 + 35, 40);
	}

	public void drawPlayers(Graphics g2d) {
		g2d.setColor(Color.red);
		g2d.fillRect(XPlayerA, YPlayerA, 5, 70);
		g2d.fillRect(XPlayerB, YPlayerB, 5, 70);
	}

	private void moveBall() {
		try {
			Thread.sleep(renderTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Collision Detection Ball with borders
		// left
		if (x + velX <= 0) {
			x = WIDTH / 2 - 5;
			y = HEIGHT / 2;
			playerScored("b");
		}
		// right
		if (x + velX >= WIDTH - offset) {
			x = WIDTH / 2 - 5;
			y = HEIGHT / 2;
			playerScored("a");
		}
		// top and bot
		if (y + velY <= 0 || y + velY >= HEIGHT - offset) {
			velY *= -1;
		}

		// Collision Detection Players with Ball
		if (x + velX < 15 && y + velY > YPlayerA && y + velY < YPlayerA + 70) {
			velX *= -1;
			collisionCount++;
			accelerateBall();
		}
		if (x + velX > WIDTH - 25 && y + velY > YPlayerB && y + velY < YPlayerB + 70) {
			velX *= -1;
			collisionCount++;
			accelerateBall();
		}

		x += velX;
		y += velY;
		repaint();
	}

	public void playerScored(String player) {
		if (player == "a") {
			scoreA++;
		}
		if (player == "b") {
			scoreB++;
		}
		setSpeed(2);
	}

	public void checkScore(Graphics2D g2d) {
		if (scoreA > 10) {
			this.running = false;
			String winText = "Player A won!";
			g2d.setFont(new Font("default", Font.BOLD, 24));
			FontMetrics fm = g2d.getFontMetrics(new Font("default", Font.BOLD, 24));
			int winSize = fm.stringWidth(winText);
			g2d.drawString(winText, WIDTH / 2 - winSize / 2, 70);
		}
		if (scoreB > 10) {
			this.running = false;
			String winText = "Player B won!";
			g2d.setFont(new Font("default", Font.BOLD, 24));
			FontMetrics fm = g2d.getFontMetrics(new Font("default", Font.BOLD, 24));
			int winSize = fm.stringWidth(winText);
			g2d.drawString(winText, WIDTH / 2 - winSize / 2, 70);
		}
	}

	public void drawMenu(Graphics2D g2d) {
		String menuText = "Press N to start a new game";
		g2d.setFont(new Font("default", Font.BOLD, 24));
		FontMetrics fm = g2d.getFontMetrics(new Font("default", Font.BOLD, 24));
		int menuSize = fm.stringWidth(menuText);
		g2d.drawString(menuText, WIDTH / 2 - menuSize / 2, 100);
	}

	public void accelerateBall() {
		if ((collisionCount % 5) == 0) {
			setSpeed(this.speed + 1);
			System.out.println("Speed: " + speed);
		}
	}

	public void setSpeed(int speed) {
		this.speed = speed;
		this.collisionCount = 0;
		velX = this.speed;
		velY = this.speed;
	}
}
