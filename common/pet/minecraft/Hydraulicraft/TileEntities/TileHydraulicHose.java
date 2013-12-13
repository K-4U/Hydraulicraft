package pet.minecraft.Hydraulicraft.TileEntities;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.ForgeDirection;
import pet.minecraft.Hydraulicraft.baseClasses.MachineEntity;
import pet.minecraft.Hydraulicraft.lib.config.Ids;

public class TileHydraulicHose extends MachineEntity {
	
	
	private int getBlockId(int x, int y, int z){
		return worldObj.getBlockId(x, y, z);
	}
	
	
	private boolean shouldConnectTo(int bId){
		return (bId == Ids.blockHydraulicCrusher.act ||
				bId == Ids.blockHydraulicFrictionIncinerator.act ||
				bId == Ids.blockHydraulicHose.act ||
				bId == Ids.blockHydraulicMixer.act ||
				bId == Ids.blockHydraulicPiston.act ||
				bId == Ids.blockHydraulicPressureGauge.act ||
				bId == Ids.blockHydraulicPressureValve.act ||
				bId == Ids.blockHydraulicPressureVat.act ||
				bId == Ids.blockHydraulicPump.act ||
				bId == Ids.blockHydraulicWasher.act);
	}
	
	public Map<ForgeDirection, Integer> getConnectedSides(){
		Map<ForgeDirection, Integer> retList = new HashMap<ForgeDirection, Integer>();
		
		retList.put(ForgeDirection.WEST, getBlockId(xCoord+1, yCoord, zCoord));
		retList.put(ForgeDirection.EAST, getBlockId(xCoord-1, yCoord, zCoord));
		
		retList.put(ForgeDirection.UP, getBlockId(xCoord, yCoord+1, zCoord));
		retList.put(ForgeDirection.DOWN, getBlockId(xCoord, yCoord-1, zCoord));
		
		retList.put(ForgeDirection.NORTH, getBlockId(xCoord, yCoord, zCoord+1));
		retList.put(ForgeDirection.SOUTH, getBlockId(xCoord, yCoord, zCoord-1));
		
		
		Map<ForgeDirection, Integer> retMap = new HashMap<ForgeDirection, Integer>();
		
		for(Map.Entry<ForgeDirection, Integer> entry : retList.entrySet()){
			if(shouldConnectTo(entry.getValue())){
				retMap.put(entry.getKey(), entry.getValue());
			}
		}
		
		return retMap;
	}
}
