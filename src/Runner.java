import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Runner {

	public static void main(String [] args) {
		Dimension playArea = new Dimension(500, 500);
		JFrame window = new JFrame("Window");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setPreferredSize(playArea);
		

		Ship ship = new Ship(new double[] {250, 400});
		ArrayList<Bullet> bullets = new ArrayList<Bullet>();
		ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
		@SuppressWarnings("serial")
		JPanel canvas = new JPanel(true) {
			@Override
			public void paintComponent(Graphics og) {
				super.paintComponent(og);
				Graphics2D g = (Graphics2D) og;
				Dimension panelSize = getSize();

				// Fill background
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, panelSize.width, panelSize.height);
				synchronized (bullets) {
					bullets.forEach((bullet) -> {
						bullet.draw(g);
					});	
				}
				synchronized (asteroids) {
					asteroids.forEach((asteroid) -> {
						asteroid.draw(g);
					});
				}
				ship.draw(g);	
			}
		};

		window.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				if(KeyEvent.VK_W == e.getKeyCode() || KeyEvent.VK_S == e.getKeyCode()) {
					ship.forward = 0;
				} else if(KeyEvent.VK_A == e.getKeyCode() || KeyEvent.VK_D == e.getKeyCode()) {
					ship.turning = 0;
				} else if(KeyEvent.VK_SPACE == e.getKeyCode()) {
					ship.firing = false;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(KeyEvent.VK_W == e.getKeyCode()) {
					ship.forward = 1;
				} else if(KeyEvent.VK_S == e.getKeyCode()) {
					ship.forward = -1;
				} else if(KeyEvent.VK_A == e.getKeyCode()) {
					ship.turning = -1;
				} else if(KeyEvent.VK_D == e.getKeyCode()) {
					ship.turning = 1;
				} else if(KeyEvent.VK_SPACE == e.getKeyCode()) {
					ship.firing = true;
				}
			}
		});
		window.add(canvas);
		window.setVisible(true);
		window.pack();
		double [] playAreaBounds = new double[] {canvas.getWidth(), canvas.getHeight()};
		ship.maxPosition = playAreaBounds;
		asteroids.add(new Asteroid(new double[] {400,100}, Math.random()*2*Math.PI , 40, 32, playAreaBounds));
		
		long currentTime = System.currentTimeMillis();
		while(true) {
			long dt = System.currentTimeMillis() - currentTime;
			currentTime = System.currentTimeMillis();
			synchronized (bullets) {
				if(ship.firing && ship.readyToFire()) {
					bullets.add(ship.fire());
				}
				bullets.forEach((bullet) -> {
					bullet.step((double)(dt)/1000);
				});
			}
			synchronized (asteroids) {
				asteroids.forEach((asteroid) -> {
					asteroid.step((double) (dt) / 1000);
				});
			}
			asteroids.forEach((asteroid) -> {
				bullets.forEach((bullet) -> {
					bulletHitAsteroid(bullet, asteroid);
				});
			});
			
			synchronized (bullets) {
				bullets.removeIf((bullet) -> {
					return bullet.maxDistanceReached;
				});
			}
			
			synchronized (asteroids) {
				for(int i = 0 ; i < asteroids.size() ; i++) {
					Asteroid a = asteroids.get(i);
					if(a.destroyed && a.size > 4) {
						asteroids.add(new Asteroid(a.position.clone(), a.direction + Math.PI/2*Math.random(), a.speed*1.5, a.size/2, playAreaBounds));
						asteroids.add(new Asteroid(a.position.clone(), a.direction - Math.PI/2*Math.random(), a.speed*1.5, a.size/2, playAreaBounds));
					}
				}
				asteroids.removeIf((asteroid) -> {
					return asteroid.destroyed;
				});		
			}
			
			ship.step((double)(dt)/1000);
			canvas.repaint();
		}
	}
	
	public static void bulletHitAsteroid(Bullet b, Asteroid a) {
		if(a.shape.contains(b.position[0] - a.position[0], b.position[1] - a.position[1])) {
			a.destroyed = true;
			b.maxDistanceReached = true;
		}
	}
}
