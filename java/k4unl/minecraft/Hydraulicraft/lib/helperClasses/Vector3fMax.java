package k4unl.minecraft.Hydraulicraft.lib.helperClasses;

public class Vector3fMax {
	private float xMin;
	private float yMin;
	private float zMin;
	
	private float xMax;
	private float yMax;
	private float zMax;
	
	
	public Vector3fMax(float xMin, float yMin, float zMin, float xMax, float yMax, float zMax) {
		this.xMin = xMin;
		this.yMin = yMin;
		this.zMin = zMin;
		
		this.xMax = xMax;
		this.yMax = yMax;
		this.zMax = zMax;
	}
	
	public float getXMin(){
		return this.xMin;
	}
	
	public float getYMin(){
		return this.yMin;
	}
	
	public float getZMin(){
		return this.zMin;
	}
	
	public void setXMin(float newX){
		this.xMin = newX;
	}
	
	public void setYMin(float newY){
		this.yMin = newY;
	}
	
	public void setZMin(float newZ){
		this.zMin = newZ;
	}
	
	
	public float getXMax(){
		return this.xMax;
	}
	
	public float getYMax(){
		return this.yMax;
	}
	
	public float getZMax(){
		return this.zMax;
	}
	
	public void setXMax(float newX){
		this.xMax = newX;
	}
	
	public void setYMax(float newY){
		this.yMax = newY;
	}
	
	public void setZMax(float newZ){
		this.zMax = newZ;
	}
}
