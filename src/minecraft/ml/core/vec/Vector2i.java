package ml.core.vec;

import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Vector2i {

	public int x, y;
	
	public Vector2i(int xin, int yin) {
		x = xin;
		y = yin;
	}
	
	public Vector2i(Vector2i c) {
		x=c.x;
		y=c.y;
	}

	public Vector2i() {}
	
	public Vector2i copy() {
		return new Vector2i(this);
	}
	
	public Vector2i set(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vector2i){
			Vector2i asV2 = (Vector2i)obj;
			return asV2.x == x && asV2.y == y;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(new int[]{x,y});
	}

	public double dotProd(Vector2i ov2) {
		return this.x*ov2.x + this.y*ov2.y;
	}

	public double magnitude() {
		return Math.sqrt(magSqr());
	}

	public double magSqr() {
		return x*x + y*y;
	}

	public Vector2i normalize() {
		double mag = magnitude();
		if (mag==0)
			return new Vector2i(0, 0);
		return divide(mag);
	}

	public Vector2i minus(Vector2i dif) {
		this.x -= dif.x;
		this.y -= dif.y;
		return this;
	}

	public Vector2i add(Vector2i a) {
		this.x += a.x;
		this.y += a.y;
		return this;
	}

	public Vector2i divide(double div) {
		this.x /= div;
		this.y /= div;
		return this;
	}

	public Vector2i mult(double fac) {
		this.x *= fac;
		this.y *= fac;
		return this;
	}
	
	public Vector2i mult(Vector2i a) {
		this.x *= a.x;
		this.y *= a.y;
		return this;
	}
	
	public Vector2i negate() {
		this.x = -x;
		this.y = -y;
		return this;
	}
	
	@SideOnly(Side.CLIENT)
	public void glTranslate() {
		GL11.glTranslatef(x, y, 0);
	}

}
