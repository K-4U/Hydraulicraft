package k4unl.minecraft.Hydraulicraft.baseClasses.entities;

import java.util.HashMap;
import java.util.Map;

import k4unl.minecraft.Hydraulicraft.baseClasses.MachineEntity;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Id;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public abstract class TileTransporter extends MachineEntity {
	private int storedLiquid = 0;
	private Map<ForgeDirection, TileEntity> connectedSides;
	
	public TileTransporter() {
		
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 14-12-2013
	 * This will return the max ammount of bar this consumer can handle.
	 */
	@Override
	public float getMaxPressure(){
		if(isOilStored()){
			switch(getTier()){
			case 0:
				return Constants.MAX_MBAR_OIL_TIER_1;
			case 1:
				return Constants.MAX_MBAR_OIL_TIER_2;
			case 2:
				return Constants.MAX_MBAR_OIL_TIER_3;
			}			
		}else{
			switch(getTier()){
			case 0:
				return Constants.MAX_MBAR_WATER_TIER_1;
			case 1:
				return Constants.MAX_MBAR_WATER_TIER_2;
			case 2:
				return Constants.MAX_MBAR_WATER_TIER_3;
			}	
		}
		return 0;
	}
	
	public int getTier(){
		return worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
	}
	
	
	private boolean shouldConnectTo(TileEntity entity){
		return (entity instanceof MachineEntity);
	}
	
	public void checkConnectedSides(){
		if(connectedSides == null){
			 connectedSides = new HashMap<ForgeDirection, TileEntity>();
		}
		Map<ForgeDirection, TileEntity> retList = new HashMap<ForgeDirection, TileEntity>();
		
		retList.put(ForgeDirection.WEST, getBlockTileEntity(xCoord+1, yCoord, zCoord));
		retList.put(ForgeDirection.EAST, getBlockTileEntity(xCoord-1, yCoord, zCoord));
		
		retList.put(ForgeDirection.UP, getBlockTileEntity(xCoord, yCoord+1, zCoord));
		retList.put(ForgeDirection.DOWN, getBlockTileEntity(xCoord, yCoord-1, zCoord));
		
		retList.put(ForgeDirection.NORTH, getBlockTileEntity(xCoord, yCoord, zCoord+1));
		retList.put(ForgeDirection.SOUTH, getBlockTileEntity(xCoord, yCoord, zCoord-1));
		
		for(Map.Entry<ForgeDirection, TileEntity> entry : retList.entrySet()){
			if(shouldConnectTo(entry.getValue())){
				connectedSides.put(entry.getKey(), entry.getValue());
			}
		}

        updateBlock();
	}
	
	public Map<ForgeDirection, TileEntity> getConnectedSides(){
		if(connectedSides == null){
			checkConnectedSides();
		}
		return connectedSides;
	}


    private void readConnectedSidesFromNBT(NBTTagCompound tagCompound){
        connectedSides = new HashMap<ForgeDirection, TileEntity>();
        NBTTagCompound ourCompound = tagCompound.getCompoundTag
                ("connectedSides");

        for(ForgeDirection dir: ForgeDirection.VALID_DIRECTIONS){
            int x = xCoord;
            int y = yCoord;
            int z = zCoord;

            if(ourCompound.getBoolean(dir.toString())){
                if(dir.equals(ForgeDirection.WEST)){
                    x+=1;
                }else if(dir.equals(ForgeDirection.EAST)){
                    x-=1;
                }

                if(dir.equals(ForgeDirection.UP)){
                    y+=1;
                }else if(dir.equals(ForgeDirection.DOWN)){
                    y-=1;
                }

                if(dir.equals(ForgeDirection.NORTH)){
                    z+=1;
                }else if(dir.equals(ForgeDirection.SOUTH)){
                    z-=1;
                }

                connectedSides.put(dir, getBlockTileEntity(x, y, z));
            }
        }
    }

    private void writeConnectedSidesToNBT(NBTTagCompound
                                                            tagCompound){
        if(connectedSides == null){
            connectedSides = new HashMap<ForgeDirection, TileEntity>();
        }

        NBTTagCompound ourCompound = tagCompound.getCompoundTag
                ("connectedSides");
        if(ourCompound == null){
            ourCompound = new NBTTagCompound();
        }

        for(Map.Entry<ForgeDirection, TileEntity> entry : connectedSides.entrySet()){
            ourCompound.setBoolean(entry.getKey().name(), true);
        }
        tagCompound.setCompoundTag("connectedSides", ourCompound);
    }

	@Override
	public void readFromNBT(NBTTagCompound tagCompound){
		super.readFromNBT(tagCompound);
        readConnectedSidesFromNBT(tagCompound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		super.writeToNBT(tagCompound);

        writeConnectedSidesToNBT(tagCompound);
	}
}
