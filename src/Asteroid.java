import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

public class Asteroid {

	double [] position;
	double direction;
	double speed;
	double size;
	double[] maxPosition;
	boolean destroyed = false;
	Polygon shape;

	public Asteroid(double[] position, double direction, double speed, double size, double[] maxPosition) {
		super();
		this.position = position;
		this.direction = direction;
		this.speed = speed;
		this.size = size;
		this.maxPosition = maxPosition;
		int points = (int) (size * 0.5 * Math.PI / 3);
		shape = generateShape(points > 4 ? points : 4);
	}

	private Polygon generateShape(int points) {
		int [] xpts = new int[points];
		int [] ypts = new int[points];
		
		double angle = Math.random()*2*Math.PI;
		double randomMod = (Math.random()*1)+1;
		double prevMod = getMod();
		for(int i = 0 ; i < points ; i ++) {
			xpts[i] += size * Math.cos(angle) * (randomMod + prevMod)/2;
			ypts[i] += size * Math.sin(angle) * (randomMod + prevMod)/2;
			angle += 1 / (double) points * Math.PI * 2;
			prevMod = randomMod;
			randomMod = getMod();
		}
		
		shape = new Polygon(xpts, ypts, points);
		return shape;
	}
	
	private double getMod() {
		return (Math.random()*1)+1;
	}

	public void step(double dt) {
		position[0] += speed * Math.cos(direction) * dt;
		position[1] += speed * Math.sin(direction) * dt;
		
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
	}

	public void draw(Graphics2D g) {
		AffineTransform oldTransform=g.getTransform();
		g.translate(position[0], position[1]);
		g.setColor(Color.WHITE);
		g.drawPolygon(shape);
		g.setTransform(oldTransform);
		
		g.translate(position[0] + maxPosition[0], position[1]);
		g.setColor(Color.WHITE);
		g.drawPolygon(shape);
		g.setTransform(oldTransform);
		
		g.translate(position[0] - maxPosition[0], position[1]);
		g.setColor(Color.WHITE);
		g.drawPolygon(shape);
		g.setTransform(oldTransform);
		
		g.translate(position[0], position[1] + maxPosition[1]);
		g.setColor(Color.WHITE);
		g.drawPolygon(shape);
		g.setTransform(oldTransform);
		
		g.translate(position[0], position[1] - maxPosition[1]);
		g.setColor(Color.WHITE);
		g.drawPolygon(shape);
		g.setTransform(oldTransform);
	}
}
