package ml.core.vec.transform;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;

import ml.core.vec.Matrix;
import ml.core.vec.Vector3d;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TransformationMatrix extends Transformation {

	private DoubleBuffer store = ByteBuffer.allocateDirect(128).order(ByteOrder.nativeOrder()).asDoubleBuffer();
	
	public double m00;
	public double m01;
	public double m02;
	public double m03;
	
	public double m10;
	public double m11;
	public double m12;
	public double m13;
	
	public double m20;
	public double m21;
	public double m22;
	public double m23;
	
	public double m30;
	public double m31;
	public double m32;
	public double m33;
	
	public TransformationMatrix() {
		this.m00 = this.m11 = this.m22 = this.m33 = 1D;
	}
	
	public TransformationMatrix(TransformationMatrix m) {
		set(m);
	}
	
	public TransformationMatrix(double n00,double n01,double n02,double n03,double n10,double n11,double n12,double n13,double n20,double n21,double n22,double n23,double n30,double n31,double n32,double n33) {
		this.m00 = n00;
		this.m01 = n01;
		this.m02 = n02;
		this.m03 = n03;
		this.m10 = n10;
		this.m11 = n11;
		this.m12 = n12;
		this.m13 = n13;
		this.m20 = n20;
		this.m21 = n21;
		this.m22 = n22;
		this.m23 = n23;
		this.m30 = n30;
		this.m31 = n31;
		this.m32 = n32;
		this.m33 = n33;
	}
	
	public TransformationMatrix(double[][] d) {
		if (d.length !=4 || d[0].length != 4) throw new IllegalArgumentException("Matrix4d construction from an Array requires a 4x4 2D array.");
		
		this.m00 = d[0][0];
		this.m01 = d[0][1];
		this.m02 = d[0][2];
		this.m03 = d[0][3];
		this.m10 = d[1][0];
		this.m11 = d[1][1];
		this.m12 = d[1][2];
		this.m13 = d[1][3];
		this.m20 = d[2][0];
		this.m21 = d[2][1];
		this.m22 = d[2][2];
		this.m23 = d[2][3];
		this.m30 = d[3][0];
		this.m31 = d[3][1];
		this.m32 = d[3][2];
		this.m33 = d[3][3];
	}
	
	public static TransformationMatrix fromMatrix(Matrix matr) {
		if (matr.M != 4 || matr.N != 4) throw new IllegalArgumentException("Matrix must be 4x4 for conversion.");
		return new TransformationMatrix(matr.matr);
	}
	
	public Matrix toGenMatrix() {
		return new Matrix(new double[][]{
				{m00, m01, m02, m03,},
				{m10, m11, m12, m13,},
				{m20, m21, m22, m23,},
				{m30, m31, m32, m33,},
		});
	}
	
	public TransformationMatrix setIdentity() {
		this.m00 = this.m11 = this.m22 = this.m33 = 1D;
		this.m01 = this.m02 = this.m03 = this.m10 = this.m12 = this.m13 = this.m20 = this.m21 = this.m23 = this.m30 = this.m31 = this.m32 = 0D;
		
		return this;
	}
	
	public TransformationMatrix setZero() {
		this.m00 = this.m01 = this.m02 = this.m03 = this.m10 = this.m11 = this.m12 = this.m13 = this.m20 = this.m21 = this.m22 = this.m23 = this.m30 = this.m31 = this.m32 = this.m33 = 0D;
		
		return this;
	}
	
	public TransformationMatrix mult(TransformationMatrix r) {
		double n00 = (this.m00*r.m00) + (this.m01*r.m10) + (this.m02*r.m20) + (this.m03*r.m30);
		double n01 = (this.m00*r.m01) + (this.m01*r.m11) + (this.m02*r.m21) + (this.m03*r.m31);
		double n02 = (this.m00*r.m02) + (this.m01*r.m12) + (this.m02*r.m22) + (this.m03*r.m32);
		double n03 = (this.m00*r.m03) + (this.m01*r.m13) + (this.m02*r.m23) + (this.m03*r.m33);
		
		double n10 = (this.m10*r.m00) + (this.m11*r.m10) + (this.m12*r.m20) + (this.m13*r.m30);
		double n11 = (this.m10*r.m01) + (this.m11*r.m11) + (this.m12*r.m21) + (this.m13*r.m31);
		double n12 = (this.m10*r.m02) + (this.m11*r.m12) + (this.m12*r.m22) + (this.m13*r.m32);
		double n13 = (this.m10*r.m03) + (this.m11*r.m13) + (this.m12*r.m23) + (this.m13*r.m33);
		
		double n20 = (this.m20*r.m00) + (this.m21*r.m10) + (this.m22*r.m20) + (this.m23*r.m30);
		double n21 = (this.m20*r.m01) + (this.m21*r.m11) + (this.m22*r.m21) + (this.m23*r.m31);
		double n22 = (this.m20*r.m02) + (this.m21*r.m12) + (this.m22*r.m22) + (this.m23*r.m32);
		double n23 = (this.m20*r.m03) + (this.m21*r.m13) + (this.m22*r.m23) + (this.m23*r.m33);
		
		double n30 = (this.m30*r.m00) + (this.m31*r.m10) + (this.m32*r.m20) + (this.m33*r.m30);
		double n31 = (this.m30*r.m01) + (this.m31*r.m11) + (this.m32*r.m21) + (this.m33*r.m31);
		double n32 = (this.m30*r.m02) + (this.m31*r.m12) + (this.m32*r.m22) + (this.m33*r.m32);
		double n33 = (this.m30*r.m03) + (this.m31*r.m13) + (this.m32*r.m23) + (this.m33*r.m33);
		
		this.m00 = n00;
		this.m01 = n01;
		this.m02 = n02;
		this.m03 = n03;
		this.m10 = n10;
		this.m11 = n11;
		this.m12 = n12;
		this.m13 = n13;
		this.m20 = n20;
		this.m21 = n21;
		this.m22 = n22;
		this.m23 = n23;
		this.m30 = n30;
		this.m31 = n31;
		this.m32 = n32;
		this.m33 = n33;
		
		return this;
	}
	
	public TransformationMatrix transpose() {
		double n00 = m00;
		double n01 = m10;
		double n02 = m20;
		double n03 = m30;
		
		double n10 = m01;
		double n11 = m11;
		double n12 = m21;
		double n13 = m31;
		
		double n20 = m02;
		double n21 = m12;
		double n22 = m22;
		double n23 = m32;
		
		double n30 = m03;
		double n31 = m13;
		double n32 = m23;
		double n33 = m33;
		
		this.m00 = n00;
		this.m01 = n01;
		this.m02 = n02;
		this.m03 = n03;
		this.m10 = n10;
		this.m11 = n11;
		this.m12 = n12;
		this.m13 = n13;
		this.m20 = n20;
		this.m21 = n21;
		this.m22 = n22;
		this.m23 = n23;
		this.m30 = n30;
		this.m31 = n31;
		this.m32 = n32;
		this.m33 = n33;
		return this;
	}
	
	public TransformationMatrix add(TransformationMatrix m) {
		this.m00 = m00 + m.m00;
		this.m01 = m01 + m.m01;
		this.m02 = m02 + m.m02;
		this.m03 = m03 + m.m03;
		
		this.m10 = m10 + m.m10;
		this.m11 = m11 + m.m11;
		this.m12 = m12 + m.m12;
		this.m13 = m13 + m.m13;
		
		this.m20 = m20 + m.m20;
		this.m21 = m21 + m.m21;
		this.m22 = m22 + m.m22;
		this.m23 = m23 + m.m23;
		
		this.m30 = m30 + m.m30;
		this.m31 = m31 + m.m31;
		this.m32 = m32 + m.m32;
		this.m33 = m33 + m.m33;
		
		return this;
	}
	
	public TransformationMatrix subt(TransformationMatrix m) {
		this.m00 = m00 - m.m00;
		this.m01 = m01 - m.m01;
		this.m02 = m02 - m.m02;
		this.m03 = m03 - m.m03;
		
		this.m10 = m10 - m.m10;
		this.m11 = m11 - m.m11;
		this.m12 = m12 - m.m12;
		this.m13 = m13 - m.m13;
		
		this.m20 = m20 - m.m20;
		this.m21 = m21 - m.m21;
		this.m22 = m22 - m.m22;
		this.m23 = m23 - m.m23;
		
		this.m30 = m30 - m.m30;
		this.m31 = m31 - m.m31;
		this.m32 = m32 - m.m32;
		this.m33 = m33 - m.m33;
		
		return this;
	}
	
	public TransformationMatrix set(TransformationMatrix m) {
		this.m00 = m.m00;
		this.m01 = m.m01;
		this.m02 = m.m02;
		this.m03 = m.m03;
		
		this.m10 = m.m10;
		this.m11 = m.m11;
		this.m12 = m.m12;
		this.m13 = m.m13;
		
		this.m20 = m.m20;
		this.m21 = m.m21;
		this.m22 = m.m22;
		this.m23 = m.m23;
		
		this.m30 = m.m30;
		this.m31 = m.m31;
		this.m32 = m.m32;
		this.m33 = m.m33;
		
		return this;
	}
	
	public TransformationMatrix translate(Vector3d vec) {
		this.m30 += this.m00 * vec.x + this.m10 * vec.y + this.m20 * vec.z;
		this.m31 += this.m01 * vec.x + this.m11 * vec.y + this.m21 * vec.z;
		this.m32 += this.m02 * vec.x + this.m12 * vec.y + this.m22 * vec.z;
		this.m33 += this.m03 * vec.x + this.m13 * vec.y + this.m23 * vec.z;
		
		return this;
	}
	
	public TransformationMatrix rotateDegs(Vector3d axis, double degs) {
		double rads = Math.toRadians(degs);
		double l = axis.x; double m = axis.y; double n = axis.z;
		double sT = Math.sin(rads);
		double cT = Math.cos(rads);
		double omcT = 1-cT;
		
		double[][] d = {
				{l*l*omcT+cT, m*l*omcT-n*sT, n*l*omcT+m*sT, 0},
				{l*m*omcT+n*sT, m*m*omcT+cT, n*m*omcT-l*sT, 0},
				{l*n*omcT-m*sT, m*n*omcT+l*sT, n*n*omcT+cT, 0},
				{0, 0, 0, 1,},
		};
		
		return this.mult(new TransformationMatrix(d));
	}
	
	public TransformationMatrix scale(Vector3d vec) {
		this.m00 = this.m00 * vec.x;
		this.m01 = this.m01 * vec.x;
		this.m02 = this.m02 * vec.x;
		this.m03 = this.m03 * vec.x;
		this.m10 = this.m10 * vec.y;
		this.m11 = this.m11 * vec.y;
		this.m12 = this.m12 * vec.y;
		this.m13 = this.m13 * vec.y;
		this.m20 = this.m20 * vec.z;
		this.m21 = this.m21 * vec.z;
		this.m22 = this.m22 * vec.z;
		this.m23 = this.m23 * vec.z;
		
		return this;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void glTransform() {
		store.put(m00);
		store.put(m01);
		store.put(m02);
		store.put(m03);
		store.put(m10);
		store.put(m11);
		store.put(m12);
		store.put(m13);
		store.put(m20);
		store.put(m21);
		store.put(m22);
		store.put(m23);
		store.put(m30);
		store.put(m31);
		store.put(m32);
		store.put(m33);
		
		store.flip();
		GL11.glMultMatrix(store);
	}

	@Override
	public void applyTo(Vector3d V) {
		double x = (this.m00*V.x) + (this.m01*V.y) + (this.m02*V.z) + (this.m03*1);
		double y = (this.m10*V.x) + (this.m11*V.y) + (this.m12*V.z) + (this.m13*1);
		double z = (this.m20*V.x) + (this.m21*V.y) + (this.m22*V.z) + (this.m23*1);
		V.x=x;
		V.y=y;
		V.z=z;
	}

	@Override
	public void applyToNormal(Vector3d N) {
		double x = (this.m00*N.x) + (this.m01*N.y) + (this.m02*N.z);
		double y = (this.m10*N.x) + (this.m11*N.y) + (this.m12*N.z);
		double z = (this.m20*N.x) + (this.m21*N.y) + (this.m22*N.z);
		N.x=x;
		N.y=y;
		N.z=z;
		N.normalize();
	}

	@Override
	public void applyTo(TransformationMatrix mat) {
		mat.mult(this);
	}
}
