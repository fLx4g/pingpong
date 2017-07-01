package com.pingpong.game;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

public class Gui {
	private int WIDTH = 600, HEIGHT = WIDTH / 12 * 9;
	private JFrame frame;
	private int lastX, lastY;

	public Gui() {
		initGui();
	}

	private void initGui() {
		frame = new JFrame();
		frame.setTitle("Pong");
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setAlwaysOnTop(true);

		GameObjects gameObjects = new GameObjects(frame.getHeight(), frame.getWidth());

		gameObjects.setVisible(true);
		frame.add(gameObjects);
		frame.setUndecorated(true);
		frame.setResizable(true);
		frame.getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		frame.setVisible(true);

		frame.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				lastX = e.getXOnScreen();
				lastY = e.getYOnScreen();
			}
		});

		frame.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				frame.setLocation(frame.getLocationOnScreen().x + x - lastX, frame.getLocationOnScreen().y + y - lastY);
				lastX = x;
				lastY = y;
			}
		});
		frame.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				switch (keyCode) {
				case KeyEvent.VK_UP:
					gameObjects.BUp.start();
					break;
				case KeyEvent.VK_DOWN:
					gameObjects.BDown.start();
					break;
				case KeyEvent.VK_W:
					gameObjects.AUp.start();
					break;
				case KeyEvent.VK_S:
					gameObjects.ADown.start();
					break;
				case KeyEvent.VK_ESCAPE:
					System.out.println("close game");
					System.exit(0);
					break;
				case KeyEvent.VK_N:
					if (!gameObjects.isRunning()) {
						gameObjects.setRunning(true);
						gameObjects.setScoreA(0);
						gameObjects.setScoreB(0);
						try {
							gameObjects.startAnimation();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						break;
					}

				}
			}

			public void keyReleased(KeyEvent e) {
				int keyCode = e.getKeyCode();
				switch (keyCode) {
				case KeyEvent.VK_UP:
					gameObjects.BUp.stop();
					break;
				case KeyEvent.VK_DOWN:
					gameObjects.BDown.stop();
					break;
				case KeyEvent.VK_W:
					gameObjects.AUp.stop();
					break;
				case KeyEvent.VK_S:
					gameObjects.ADown.stop();
					break;
				}
			}

			public void keyTyped(KeyEvent e) {
			}
		});
	}
}
