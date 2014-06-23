package k4unl.minecraft.Hydraulicraft.lib.helperClasses;


public class Vector2fMax {

	private float xMin;
	private float xMax;
	
	private float yMin;
	private float yMax;
	
	public Vector2fMax(float _xMin, float _yMin, float _xMax, float _yMax){
		xMin = _xMin;
		xMax = _xMax;
		
		yMin = _yMin;
		yMax = _yMax;
	}
	
	public float getXMin(){
		return xMin;
	}
	
	public float getXMax(){
		return xMax;
	}
	
	public float getYMin(){
		return yMin;
	}
	
	public float getYMax(){
		return yMax;
	}
	
	
	public void setXMin(float _x){
		xMin = _x;
	}
	
	public void setXMax(float _x){
		xMax = _x;
	}
	
	public void setYMin(float _y){
		yMin = _y;
	}
	
	public void setYMax(float _y){
		yMax = _y;
	}

	public Vector2fMax copy() {
		return new Vector2fMax(xMin, yMin, xMax, yMax);
	}
}

