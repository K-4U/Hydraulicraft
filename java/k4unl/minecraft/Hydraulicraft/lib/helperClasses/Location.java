package k4unl.minecraft.Hydraulicraft.lib.helperClasses;

import net.minecraftforge.common.util.ForgeDirection;

public class Location {
	private int x;
	private int y;
	private int z;
	
	
	public Location(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Location(int xCoord, int yCoord, int zCoord, ForgeDirection dir) {
		this.x = xCoord + dir.offsetX;
		this.y = yCoord + dir.offsetY;
		this.z = zCoord + dir.offsetZ;
	}
	
	
	public Location(int[] coords) {
		if(coords.length >= 2){
			this.x = coords[0];
			this.y = coords[1];
			this.z = coords[2];
		}
	}
	
	public boolean equals(Location toTest){
		if(this.x == toTest.x && this.y == toTest.y && this.z == toTest.z){
			return true;
		}
		return false;
	}

	public void setLocation(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setLocation(int[] coords) {
		this.x = coords[0];
		this.y = coords[1];
		this.z = coords[2];
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public int getZ(){
		return this.z;
	}
	
	public void setX(int newX){
		this.x = newX;
	}
	
	public void setY(int newY){
		this.y = newY;
	}
	
	public void setZ(int newZ){
		this.z = newZ;
	}

	public int[] getLocation() {
		int[] ret = new int[3];
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
	
	public boolean compare(int x, int y, int z){
		return (this.x == x && this.y == y && this.z == z);
	}
}
