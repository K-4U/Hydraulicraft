package k4unl.minecraft.Hydraulicraft.tileEntities.gow;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.lib.config.Config;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Location;
import k4unl.minecraft.Hydraulicraft.network.PacketPipeline;
import k4unl.minecraft.Hydraulicraft.network.packets.PacketPortalEnabled;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBaseNoPower;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;


public class TilePortalTeleporter extends TileHydraulicBaseNoPower {
	private boolean hasSendPacket = true;
	private float transparancy = 0.2F;
	private float prevTransparancy = 0.2F;
	private float directionTransparency = 0.01F;
	private ForgeDirection baseDir;
	private ForgeDirection portalDir;
	private Location portalBase;
	
	@Override
	public void readFromNBT(NBTTagCompound tCompound){
		super.readFromNBT(tCompound);
		
		baseDir = ForgeDirection.getOrientation(tCompound.getInteger("baseDir"));
		portalDir = ForgeDirection.getOrientation(tCompound.getInteger("portalDir"));
		
		portalBase = new Location(tCompound.getIntArray("portalBase"));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tCompound){
		super.writeToNBT(tCompound);
		tCompound.setInteger("baseDir", baseDir.ordinal());
		tCompound.setInteger("portalDir", portalDir.ordinal());
		
		if(portalBase != null){
			tCompound.setIntArray("portalBase", portalBase.getIntArray());
		}
	}
	
	public void setRotation(ForgeDirection _baseDir, ForgeDirection _portalDir){
		baseDir = _baseDir;
		portalDir = _portalDir;
		hasSendPacket = false;
	}
	
	public TilePortalBase getPortalBase(){
		if(portalBase == null){
			return null;
		}else{
			return (TilePortalBase) portalBase.getTE(getWorldObj());
		}
	}
	
	@Override
	public void updateEntity(){
		if(!getWorldObj().isRemote && hasSendPacket == false && baseDir != null){
			hasSendPacket = true;
			PacketPipeline.instance.sendToAllAround(new PacketPortalEnabled(xCoord, yCoord, zCoord, baseDir, portalDir), getWorldObj());
		}
		if(getWorldObj().isRemote){
			prevTransparancy = transparancy;
			transparancy += directionTransparency;
			if(transparancy >= 0.8F){
				directionTransparency = -0.01F;
			}else if(transparancy <= 0.3F){
				directionTransparency = 0.01F;
			}
		}
	}
	
	
	public ForgeDirection getBaseDir(){
		return baseDir;
	}
	
	public ForgeDirection getPortalDir(){
		return portalDir;
	}
	
	public void teleport(Entity ent){
		
	}

	public float getTransparancy(float frame) {
		return transparancy + ((prevTransparancy - transparancy) * frame);
	}

	public void setBase(TilePortalBase tilePortalBase) {
		portalBase = new Location(tilePortalBase.xCoord, tilePortalBase.yCoord, tilePortalBase.zCoord);
	}
	
	@Override
	public boolean shouldRenderInPass(int pass){
		return true;
	}

	public boolean isEdge(ForgeDirection dir) {
		return (new Location(xCoord, yCoord, zCoord, dir).getBlock(getWorldObj()) == HCBlocks.portalFrame);
	}

	public void usePressure() {
		getPortalBase().getHandler().setPressure(getPortalBase().getPressure(ForgeDirection.UNKNOWN) + Config.getInt("pressurePerTeleport"), ForgeDirection.UP);
	}
}
