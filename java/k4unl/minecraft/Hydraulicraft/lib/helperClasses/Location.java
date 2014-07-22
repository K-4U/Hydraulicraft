package k4unl.minecraft.Hydraulicraft.lib.helperClasses;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class Location {
	private int x;
	private int y;
	private int z;
	private int dimension;
	
	
	public Location(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Location(Location clone) {
		this.x = clone.x;
		this.y = clone.y;
		this.z = clone.z;
	}
	
	public Location(int xCoord, int yCoord, int zCoord, ForgeDirection dir) {
		this.x = xCoord + dir.offsetX;
		this.y = yCoord + dir.offsetY;
		this.z = zCoord + dir.offsetZ;
	}
	
	public Location(int _x, int _y, int _z, int _dimension){
		x = _x;
		y = _y;
		z = _z;
		dimension = _dimension;
	}
	
	public Location(int[] loc){
		if(loc.length > 2){
			x = loc[0];
			y = loc[1];
			z = loc[2];
			if(loc.length > 3){
				dimension = loc[3];
			}
		}else{
			//Log.error("Trying to load a location with a wrong int array!");
		}
	}
	
	public Location(int _x, int _y, int _z, ForgeDirection d, int offset){
		x = _x + (d.offsetX * offset);
		y = _y + (d.offsetY * offset);
		z = _z + (d.offsetZ * offset);
	}
	
	public Location(int _x, int _y, int _z, int _dimension, ForgeDirection d, int offset){
		x = _x + (d.offsetX * offset);
		y = _y + (d.offsetY * offset);
		z = _z + (d.offsetZ * offset);
		dimension = _dimension;
	}
	
	public Location(Location baseLoc, ForgeDirection d, int offset){
		x = baseLoc.getX() + (d.offsetX * offset);
		y = baseLoc.getY() + (d.offsetY * offset);
		z = baseLoc.getZ() + (d.offsetZ * offset);
		dimension = baseLoc.dimension;
	}
	
	public Location(int _x, int _y, int _z, int _dimension, ForgeDirection d) {
		x = _x + d.offsetX;
		y = _y + d.offsetY;
		z = _z + d.offsetZ;
		dimension = _dimension;
	}
	
	public Location(ChunkPosition pos){
		if(pos != null){
			this.x = pos.chunkPosX;
			this.y = pos.chunkPosY;
			this.z = pos.chunkPosZ;
		}
	}
	
	public Location(MovingObjectPosition blockLookedAt) {
		if(blockLookedAt != null){
			this.x = blockLookedAt.blockX;
			this.y = blockLookedAt.blockY;
			this.z = blockLookedAt.blockZ;
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
	
	public int getDifference(Location otherLoc){
		return (int)Math.sqrt(Math.pow(this.x - otherLoc.x, 2) + Math.pow(this.y - otherLoc.y, 2) + Math.pow(this.z - otherLoc.z, 2));
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
	
	public void addX(int toAdd){
		x += toAdd;
	}
	public void addY(int toAdd){
		y += toAdd;
	}
	public void addZ(int toAdd){
		z += toAdd;
	}
	
	public void offset(ForgeDirection dir, int offsetInt){
		x += dir.offsetX * offsetInt;
		y += dir.offsetY * offsetInt;
		z += dir.offsetZ * offsetInt;
	}
	
	public int[] getIntArray(){
		return new int[] {x, y, z, dimension};
	}
	
	public Block getBlock(IBlockAccess iba){
		return iba.getBlock(x, y, z);
	}
	
	public Block getBlock(IBlockAccess iba, ForgeDirection dir){
		return iba.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
	}
	
	public TileEntity getTE(IBlockAccess iba){
		return iba.getTileEntity(x, y, z);
	}
	
	public TileEntity getTE(IBlockAccess iba, ForgeDirection dir){
		return iba.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
	}
	
	public String print() {
		return String.format("D: " + dimension + " X: " + x + " Y: " + y + " Z: " + z);
	}
	
	public int getDimension(){
		return dimension;
	}
}
