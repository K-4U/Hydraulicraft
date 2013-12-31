package k4unl.minecraft.Hydraulicraft.TileEntities;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.blocks.Blocks;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class TileHydraulicHarvester extends TileEntity implements IHydraulicConsumer {
	private IBaseClass baseHandler;
	private boolean isMultiblock;
	private int harvesterLength;
	private int harvesterWidth;
	
	
	private static final int idHorizontalFrame = Block.fence.blockID; //TODO: replace this
	private static final int idVerticalFrame = Block.fence.blockID; //TODO: replace this
	private static final int idPiston = Block.pistonBase.blockID; //TODO: replace this
	private static final int idEndBlock = Ids.blockHydraulicPressureWall.act;
	
	
	@Override
	public int getMaxStorage() {
		return FluidContainerRegistry.BUCKET_VOLUME * 16;
	}

	@Override
	public float getMaxPressure() {
		return Constants.MAX_MBAR_OIL_TIER_3;
	}

	@Override
	public IBaseClass getHandler() {
		if(baseHandler == null) baseHandler = HydraulicBaseClassSupplier.getConsumerClass(this);
        return baseHandler;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		getHandler().readFromNBT(tagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		getHandler().writeToNBT(tagCompound);

	}

	@Override
	public void readNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		isMultiblock = tagCompound.getBoolean("isMultiblock");
		harvesterLength = tagCompound.getInteger("harvesterLength");
		harvesterWidth = tagCompound.getInteger("harvesterWidth");
	}

	@Override
	public void writeNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		
		tagCompound.setBoolean("isMultiblock", isMultiblock);
		tagCompound.setInteger("harvesterLength", harvesterLength);
		tagCompound.setInteger("harvesterWidth", harvesterWidth);
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
	public void updateEntity() {
		getHandler().updateEntity();
		//Every half second.. Or it should be..
		if(worldObj.getTotalWorldTime() % 10 == 0){
			checkMultiblock();
		}
	}

	@Override
	public float workFunction(boolean simulate) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onBlockBreaks() {
		invalidateMultiblock();
	}
	
	public void invalidateMultiblock(){
		
	}
	
	private int getBlockId(int x, int y, int z){
		return worldObj.getBlockId(x, y, z);
	}
	
	private boolean checkMultiblock(){
		//Go up, check for pistons etc
		if(getBlockId(xCoord, yCoord + 1, zCoord) != idVerticalFrame) return false;
		if(getBlockId(xCoord, yCoord + 2, zCoord) != idPiston) return false;
		int x = xCoord;
		int y = yCoord + 2;
		int z = zCoord;
		
	}

}
