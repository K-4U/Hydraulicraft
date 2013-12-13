package pet.minecraft.Hydraulicraft.TileEntities;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.ForgeDirection;
import pet.minecraft.Hydraulicraft.baseClasses.MachineEntity;

public class TileHydraulicHose extends MachineEntity {
	
	
	private int getBlockId(int x, int y, int z){
		return worldObj.getBlockId(x, y, z);
	}
	
	public Map<ForgeDirection, Integer> getConnectedSides(){
		Map<ForgeDirection, Integer> retList = new HashMap<ForgeDirection, Integer>();
		
		retList.put(ForgeDirection.WEST, getBlockId(xCoord+1, yCoord, zCoord));
		retList.put(ForgeDirection.EAST, getBlockId(xCoord-1, yCoord, zCoord));
		
		retList.put(ForgeDirection.UP, getBlockId(xCoord, yCoord+1, zCoord));
		retList.put(ForgeDirection.DOWN, getBlockId(xCoord, yCoord-1, zCoord));
		
		retList.put(ForgeDirection.NORTH, getBlockId(xCoord, yCoord, zCoord+1));
		retList.put(ForgeDirection.SOUTH, getBlockId(xCoord, yCoord, zCoord-1));
		
		return retList;
	}
}
