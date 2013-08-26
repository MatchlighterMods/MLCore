package ml.core.vec;

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
	
	public Rectangle(Rectangle crect) {
		this.xCoord = crect.xCoord;
		this.yCoord = crect.yCoord;
		this.width = crect.width;
		this.height = crect.height;
	}
	
	public boolean isPointInside(int pntX, int pntY) {
		return GeoMath.pointInRect(pntX, pntY, this.xCoord, this.yCoord, this.width, this.height);
	}
}