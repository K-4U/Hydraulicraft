package k4unl.minecraft.Hydraulicraft.TileEntities;

import java.util.HashMap;
import java.util.Map;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseTransporter;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicTransporter;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import codechicken.multipart.scalatraits.TSlottedTile;

public class TileHydraulicHose extends TileEntity implements IHydraulicTransporter {
	private IBaseTransporter baseHandler;
    private Map<ForgeDirection, TileEntity> connectedSides;
    private final boolean[] connectedSideFlags = new boolean[6];
    private boolean needToCheckNeighbors;
    private boolean hasCheckedSinceStartup;

	
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound){
		getHandler().readFromNBT(tagCompound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		getHandler().writeToNBT(tagCompound);
	}

	@Override
	public int getMaxStorage() {
		return FluidContainerRegistry.BUCKET_VOLUME * (2 * (getTier()+1));
	}

	public int getTier() {
		return worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
	}

	@Override
	public void onBlockBreaks() {
	}

   @Override
    public float getMaxPressure(){
        if(getHandler().isOilStored()) {
            switch(getTier()){
                case 0:
                    return Constants.MAX_MBAR_OIL_TIER_1;
                case 1:
                    return Constants.MAX_MBAR_OIL_TIER_2;
                case 2:
                    return Constants.MAX_MBAR_OIL_TIER_3;
            }
        } else {
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

	@Override
	public IBaseTransporter getHandler() {
		if(baseHandler == null) baseHandler = HydraulicBaseClassSupplier.getTransporterClass(this);
        return baseHandler;
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
		getHandler().onDataPacket(net, packet);
	}

	@Override
	public Packet getDescriptionPacket() {
		return getHandler().getDescriptionPacket();
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return getHandler().getRenderBoundingBox();
	}

	@Override
	public void readNBT(NBTTagCompound tagCompound) {
		readConnectedSidesFromNBT(tagCompound);
	}

	@Override
	public void writeNBT(NBTTagCompound tagCompound) {
		writeConnectedSidesToNBT(tagCompound);		
	}
	
    @Override
    public void updateEntity(){
    	if(getHandler() != null){
    		//This should never happen that this is null! :|
    		getHandler().updateEntity();
    	}
    	if(worldObj.getTotalWorldTime() % 60 == 0 && hasCheckedSinceStartup == false){
    		checkConnectedSides();
    		hasCheckedSinceStartup = true;
    		//Hack hack hack
    		//Temporary bug fix that we will forget about
    	}
        if(needToCheckNeighbors) {
            needToCheckNeighbors = false;
            connectedSides = new HashMap<ForgeDirection, TileEntity>();
            for(int i = 0; i < 6; i++) {
                if(connectedSideFlags[i]) {
                    ForgeDirection dir = ForgeDirection.getOrientation(i);
                    connectedSides.put(dir, worldObj.getBlockTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ));
                }
            }
        }
    }

    private boolean shouldConnectTo(TileEntity entity){
        return entity instanceof IHydraulicMachine;
    }

    public void checkConnectedSides(){
        connectedSides = new HashMap<ForgeDirection, TileEntity>();

        for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            TileEntity te = worldObj.getBlockTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
            if(shouldConnectTo(te)) {
                connectedSides.put(dir, te);
            }
        }

        getHandler().updateBlock();
    }

    public Map<ForgeDirection, TileEntity> getConnectedSides(){
        if(connectedSides == null) {
            checkConnectedSides();
        }
        return connectedSides;
    }

    private void readConnectedSidesFromNBT(NBTTagCompound tagCompound){
        NBTTagCompound ourCompound = tagCompound.getCompoundTag("connectedSides");

        for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            connectedSideFlags[dir.ordinal()] = ourCompound.getBoolean(dir.name());
        }
        needToCheckNeighbors = true;
    }

    private void writeConnectedSidesToNBT(NBTTagCompound tagCompound){
        if(connectedSides == null) {
            connectedSides = new HashMap<ForgeDirection, TileEntity>();
        }

        NBTTagCompound ourCompound = new NBTTagCompound();
        for(Map.Entry<ForgeDirection, TileEntity> entry : connectedSides.entrySet()) {
            ourCompound.setBoolean(entry.getKey().name(), true);
        }
        tagCompound.setCompoundTag("connectedSides", ourCompound);
    }

}
