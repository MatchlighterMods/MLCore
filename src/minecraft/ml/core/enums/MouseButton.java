package ml.core.enums;

public enum MouseButton {
	Left,
	Right,
	Middle;
	
	public static MouseButton get(int mb) {
		return values()[mb];
	}
}
