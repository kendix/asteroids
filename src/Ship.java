import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

public class Ship {
	double [] position;
	double [] maxPosition; 
	double [] velocity;
	double maxVelocity = 200;

	double forward; // is the ship moving forwards/backwards
	double forwardAcceleration = 100;

	double turning; // is the ship turning
	double turningSpeed = 3;

	// Direction in radians
	double direction;
	
	long fireSpeed; // ms between bullets
	long lastBulletFired = 0;
	boolean firing = false;;

	int [][] shipPoints;

	public Ship(double [] p) {
		velocity = new double[] {0,0};
		position = p;
		maxPosition = p;
		direction = Math.PI*3/2;
		turning = 0;
		forward = 0;
		shipPoints = new int[][] {{7, -7, -7},{0, 6, -6}};
		fireSpeed = 250;
	}

	public void draw(Graphics2D g) {
		AffineTransform oldTransform=g.getTransform();
		g.translate(position[0], position[1]);
		g.rotate(direction);
		g.setColor(Color.WHITE);
		g.drawPolygon(new Polygon(shipPoints[0], shipPoints[1], 3));
		g.setTransform(oldTransform);
	}

	public void step(double dt) {

		stepVelocity(dt);
		stepPosition(dt);
	}

	private void stepVelocity(double dt) {
		double dvx = forward * forwardAcceleration * Math.cos(direction) * dt;
		double dvy = forward * forwardAcceleration * Math.sin(direction) * dt;
		velocity[0] += dvx;
		velocity[1] += dvy;
		double currentVelocity = getVelocity(velocity[0], velocity[1]);
		if(currentVelocity > maxVelocity) {
			double velocityScale = maxVelocity/currentVelocity;
			velocity[0] *= velocityScale;
			velocity[1] *= velocityScale;
		}
	}

	private void stepPosition(double dt) {
		position[0] += velocity[0] * dt;
		position[1] += velocity[1] * dt;
		if(position[0] > maxPosition[0]) {
			position[0] -= maxPosition[0];
		} else if(position[0] < 0) {
			position[0] += maxPosition[0];
		}
		if(position[1] > maxPosition[1]) {
			position[1] -= maxPosition[1];
		} else if(position[1] < 0) {
			position[1] += maxPosition[1];
		}
		direction += turningSpeed * turning * dt;	
	}

	public double getVelocity(double x, double y) {
		return Math.sqrt(x*x + y*y);
	}
	
	public boolean readyToFire() {
		return System.currentTimeMillis() > lastBulletFired + fireSpeed;
	}
	public Bullet fire() {
		if(readyToFire()) {
			lastBulletFired = System.currentTimeMillis();
			return new Bullet(position.clone(), direction, 300, 2, maxPosition, 250);
		} else {
			return null;
		}
	}
}
