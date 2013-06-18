package ml.core.geo;

public class Vector2<T> {
	public T X;
	public T Y;
	
	public Vector2(T x, T y) {
		this.X = x;
		this.Y = y;
	}
	
	@Override
	public String toString(){
		return "X: " + X + ", Y: " + Y;
	}
}