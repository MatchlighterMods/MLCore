package ml.core.vec;

import java.util.Arrays;

public class Vector2d {

	public final double x;
	public final double y;
	
	public Vector2d(double xin, double yin) {
		x = xin;
		y = yin;
	}
	
	public Vector2d(Vector2d c) {
		x=c.x;
		y=c.y;
	}

	public Vector2d copy() {
		return new Vector2d(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vector2d){
			Vector2d asV2 = (Vector2d)obj;
			return asV2.x == x && asV2.y == y;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(new double[]{x,y});
	}

	public double dot(Vector2d ov3) {
		return this.x*ov3.x + this.y*ov3.y;
	}

	public double magnitude() {
		return Math.sqrt(magSqr());
	}

	public double magSqr() {
		return Math.pow(x, 2) + Math.pow(y, 2);
	}

	public Vector2d normalize() {
		double mag = magnitude();
		if (mag==0)
			return new Vector2d(0, 0);
		return divide(mag);
	}

	public Vector2d minus(Vector2d dif) {
		return new Vector2d(x-dif.x, y-dif.y);
	}

	public Vector2d add(Vector2d a) {
		return new Vector2d(x+a.x, y+a.y);
	}

	public Vector2d divide(double div) {
		return new Vector2d(x / div, y / div);
	}

	public Vector2d mult(double fac) {
		return new Vector2d(x * fac, y * fac);
	}
	
	public Vector2d mult(Vector2d a) {
		return new Vector2d(x*a.x, y*a.y);
	}
	
	public Vector2d negate() {
		return new Vector2d(-x, -y);
	}
	
	@Override
	public String toString() {
		return "{"+x+", "+y+"}";
	}

}
