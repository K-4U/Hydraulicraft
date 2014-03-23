package k4unl.minecraft.Hydraulicraft.lib.helperClasses;

public class Vector3f {
	private float x;
	private float y;
	private float z;
	
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f(float[] coords) {
		if(coords.length >= 2){
			this.x = coords[0];
			this.y = coords[1];
			this.z = coords[2];
		}
	}
	
	public boolean equals(Vector3f toTest){
		int compX = Float.compare(this.x, toTest.x);
		int compY = Float.compare(this.z, toTest.y);
		int compZ = Float.compare(this.y, toTest.z);
		if(compX == 0 && compY == 0 && compZ == 0){
			return true;
		}
		return false;
	}

	public void setLocation(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setLocation(float[] coords) {
		this.x = coords[0];
		this.y = coords[1];
		this.z = coords[2];
	}
	
	public float getX(){
		return this.x;
	}
	
	public float getY(){
		return this.y;
	}
	
	public float getZ(){
		return this.z;
	}
	
	public void setX(float newX){
		this.x = newX;
	}
	
	public void setY(float newY){
		this.y = newY;
	}
	
	public void setZ(float newZ){
		this.z = newZ;
	}

	public float[] getLocation() {
		float[] ret = new float[3];
		ret[0] = this.x;
		ret[1] = this.y;
		ret[2] = this.z;
		return ret;
	}
	
	public String printLocation(){
		return "X: " + this.x + " Y: " + this.y + " Z: " + this.z;
	}
	
	public String printCoords(){
		return this.x + ", " + this.y + ", " + this.z;
	}
	
	public boolean compare(float x, float y, float z){
		return this.equals(new Vector3f(x, y, z));
	}
}
