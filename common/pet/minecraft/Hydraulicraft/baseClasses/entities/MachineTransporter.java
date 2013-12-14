package pet.minecraft.Hydraulicraft.baseClasses.entities;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import pet.minecraft.Hydraulicraft.baseClasses.MachineEntity;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Id;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Name;

public abstract class MachineTransporter extends MachineEntity {
	
	private boolean shouldConnectTo(TileEntity entity){
		return (entity instanceof MachineEntity);
	}
	
	public Map<ForgeDirection, TileEntity> getConnectedSides(){
		Map<ForgeDirection, TileEntity> retList = new HashMap<ForgeDirection, TileEntity>();
		
		retList.put(ForgeDirection.WEST, getBlockTileEntity(xCoord+1, yCoord, zCoord));
		retList.put(ForgeDirection.EAST, getBlockTileEntity(xCoord-1, yCoord, zCoord));
		
		retList.put(ForgeDirection.UP, getBlockTileEntity(xCoord, yCoord+1, zCoord));
		retList.put(ForgeDirection.DOWN, getBlockTileEntity(xCoord, yCoord-1, zCoord));
		
		retList.put(ForgeDirection.NORTH, getBlockTileEntity(xCoord, yCoord, zCoord+1));
		retList.put(ForgeDirection.SOUTH, getBlockTileEntity(xCoord, yCoord, zCoord-1));
		
		
		Map<ForgeDirection, TileEntity> retMap = new HashMap<ForgeDirection, TileEntity>();
		
		for(Map.Entry<ForgeDirection, TileEntity> entry : retList.entrySet()){
			if(shouldConnectTo(entry.getValue())){
				retMap.put(entry.getKey(), entry.getValue());
			}
		}
		
		return retMap;
	}
}
