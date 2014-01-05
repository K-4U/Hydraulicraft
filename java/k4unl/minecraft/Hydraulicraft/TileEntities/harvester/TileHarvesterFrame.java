package k4unl.minecraft.Hydraulicraft.TileEntities.harvester;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileHarvesterFrame extends TileEntity {
	private boolean isRotated;
	
	public boolean getIsRotated(){
		return this.isRotated;
	}
	
	
	public void setRotated(boolean b) {
		this.isRotated = b;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet){
		NBTTagCompound tagCompound = packet.data;
		this.readFromNBT(tagCompound);
	}
	
	@Override
	public Packet getDescriptionPacket(){
		NBTTagCompound tagCompound = new NBTTagCompound();
		this.writeToNBT(tagCompound);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 4, tagCompound);
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound){
		super.readFromNBT(tagCompound);
		isRotated = tagCompound.getBoolean("isRotated");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		super.writeToNBT(tagCompound);
		tagCompound.setBoolean("isRotated", isRotated);
	}
}
