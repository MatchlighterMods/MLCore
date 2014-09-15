package ml.core.vec;

import java.util.ArrayList;
import java.util.List;

public class Polygon {

	private final List<Vector2d> points = new ArrayList<Vector2d>();
	
	public Polygon(Vector2d... points) {
		for (Vector2d pnt : points) {
			this.points.add(pnt);
		}
	}
	
	public void appendPoint(Vector2d point) {
		this.points.add(point);
	}
	
	public boolean pointInPolygon(Vector2d point) {
		boolean oddNodes = false;
		
		Vector2d lastPoint = points.get(points.size() - 1);
		
		for (Vector2d nextPoint : points) {
			if (((nextPoint.y < point.y) && (lastPoint.y >= point.y))
					|| (nextPoint.y >= point.y) && (lastPoint.y < point.y)) {
				if ((point.y - nextPoint.y) / (lastPoint.y - nextPoint.y)
						* (lastPoint.x - nextPoint.x) < (point.x - nextPoint.x))
					oddNodes = !oddNodes;
			}
			lastPoint = nextPoint;
		}
		return oddNodes;
	}
	
}
