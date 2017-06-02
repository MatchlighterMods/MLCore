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
	
	public Rectangle translate(int dx, int dy) {
		this.xCoord += dx;
		this.yCoord += dy;
		return this;
	}
	
	public Rectangle translate(Vector2i dv) {
		return translate(dv.x, dv.y);
	}
	
	public Rectangle expand(int dx, int dy) {
		if (dx > 0)
			this.width += dx;
		else {
			this.xCoord += dx;
			this.width -= dx;
		}
		if (dy > 0)
			this.height += dy;
		else {
			this.yCoord += dy;
			this.height -= dy;
		}
		return this;
	}
	
	public Rectangle expand(Vector2i dv) {
		return expand(dv.x, dv.y);
	}
	
	public Rectangle include(int ix, int iy) {
		if (ix < xCoord) expand(ix-xCoord,0);
		if (ix > xCoord+width) expand(ix - xCoord - width, 0);
		if (iy < yCoord) expand(iy-yCoord,0);
		if (iy > yCoord+height) expand(iy - yCoord - height, 0);
		return this;
	}
	
	public Rectangle include(Vector2i iv) {
		return include(iv.x, iv.y);
	}
	
	public Rectangle include(Rectangle ir) {
		include(ir.xCoord, ir.yCoord);
		return include(ir.xCoord+ir.width, ir.yCoord+ir.height);
	}
	
	public boolean isPointInside(int pntX, int pntY) {
		return GeoMath.pointInRect(pntX, pntY, this.xCoord, this.yCoord, this.width, this.height);
	}
	
	public boolean intersects(Rectangle r) {
		return (this.xCoord <= r.xCoord + r.width) && (r.xCoord <= this.xCoord + this.width) && (this.yCoord <= r.yCoord + r.height) && (r.yCoord <= this.yCoord + this.height);
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