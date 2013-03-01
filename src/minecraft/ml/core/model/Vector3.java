package ml.core.model;

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
	
}
