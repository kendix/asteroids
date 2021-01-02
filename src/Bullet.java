import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Bullet {
	double [] position;
	double direction;
	double speed;
	double size;
	double[] maxPosition;
	double maxDistance;
	double distanceTraveled;
	boolean maxDistanceReached = false;

	public Bullet(double[] position, double direction, double speed, double size, double[] maxPosition,
			double maxDistance) {
		super();
		this.position = position;
		this.direction = direction;
		this.speed = speed;
		this.size = size;
		this.maxPosition = maxPosition;
		this.maxDistance = maxDistance;
		this.distanceTraveled = 0;
	}

	public void step(double dt) {
		position[0] += speed * Math.cos(direction) * dt;
		position[1] += speed * Math.sin(direction) * dt;
		distanceTraveled += speed * dt;
		
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
		if(distanceTraveled > maxDistance) {
			speed = 0;
			maxDistanceReached = true;
		}
	}

	public void draw(Graphics2D g) {
		AffineTransform oldTransform=g.getTransform();
		g.translate(position[0], position[1]);
		g.rotate(direction);
		g.setColor(Color.WHITE);
		g.drawLine(-3, 0, 0, 0);
		g.setTransform(oldTransform);
	}
}
