package ml.core.geo;

public class Vector2<T> {
	public T X;
	public T Y;
	
	public Vector2(T x, T y) {
		this.X = x;
		this.Y = y;
	}
	
	public Vector2<T> set(T x, T y) {
		X=x;
		Y=y;
		return this;
	}
	
	public Vector2<T> set(Vector2<T> ov) {
		return set(ov.X, ov.Y);
	}
	
	public Vector2<T> copy() {
		return new Vector2<T>(X, Y);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() == this.getClass()) {
			return ((Vector2)obj).X == X && ((Vector2)obj).Y == Y; 
		}
		return false;
	}
	
	@Override
	public String toString(){
		return "X: " + X + ", Y: " + Y;
	}
}