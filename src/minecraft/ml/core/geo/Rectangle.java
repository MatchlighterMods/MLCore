package ml.core.geo;

public class Rectangle {
	public int xCoord;
	public int yCoord;
	public int width;
	public int height;
	
	public Rectangle(int x, int y, int width, int height) {
		this.xCoord = x;
		this.yCoord = y;
		this.width = width;
		this.height = height;
	}
	
	public boolean isPointInside(int pntX, int pntY) {
		return GeoMath.pointInRect(pntX, pntY, this.xCoord, this.yCoord, this.width, this.height);
	}
}