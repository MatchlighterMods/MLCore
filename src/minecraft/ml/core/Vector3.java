package ml.core;

public class Vector3{

	public double x;
	public double y;
	public double z;
	
	public Vector3(double xin, double yin, double zin){
		x = xin;
		y = yin;
		z = zin;
	}
	
	public double dotProd(Vector3 ov3){
		return this.x*ov3.x + this.y*ov3.y + this.z*ov3.z;
	}
	
	public double magnitude(){
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
	}
	
	public Vector3 normalize(){
		double mag = magnitude();
		if (mag==0)
			return new Vector3(0, 0, 0);
		return divide(mag);
	}
	
	public Vector3 minus(Vector3 dif){
		return new Vector3(this.x - dif.x, this.y - dif.y, this.z - dif.z);
	}
	
	public Vector3 add(Vector3 a){
		return new Vector3(this.x + a.x, this.y + a.y, this.z + a.z);
	}
	
	public Vector3 divide(double div){
		
		return new Vector3(this.x / div, this.y / div, this.z / div);
	}
	
	public Vector3 multiply(double fac){
		return new Vector3(this.x * fac, this.y * fac, this.z * fac);
	}
	
}
