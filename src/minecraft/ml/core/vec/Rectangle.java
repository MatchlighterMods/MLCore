package ml.core.vec;

import java.util.Arrays;

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
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Rectangle){
			Rectangle asRct = (Rectangle)obj;
			return asRct.xCoord == xCoord && asRct.yCoord == yCoord && asRct.width == width && asRct.height == height;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(new double[]{xCoord, yCoord, width, height});
	}
}