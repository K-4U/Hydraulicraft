package k4unl.minecraft.Hydraulicraft.TileEntities.harvester;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Location;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidContainerRegistry;
import codechicken.lib.vec.Vector3;

public class TileHydraulicHarvester extends TileEntity implements IHydraulicConsumer {
	private IBaseClass baseHandler;
	private boolean isMultiblock;
	private int harvesterLength;
	private int harvesterWidth;
	private int dir = -1;
	private boolean firstRun = true;
	
	private List<Location> pistonList = new ArrayList<Location>();
	
	
	private static final int idHorizontalFrame = Ids.blockHydraulicHarvester.act;
	private static final int idVerticalFrame = Block.fence.blockID; //TODO: replace this
	private static final int idPiston = Ids.blockHydraulicPiston.act;
	private static final int idEndBlock = Ids.blockHydraulicPressureWall.act;
	private static final int idTrolley = Ids.blockHarvesterTrolley.act;
	
	
	@Override
	public int getMaxStorage() {
		return FluidContainerRegistry.BUCKET_VOLUME * 16;
	}

	@Override
	public float getMaxPressure() {
		if(getHandler().isOilStored()){
			return Constants.MAX_MBAR_OIL_TIER_3;
		}else{
			return Constants.MAX_MBAR_WATER_TIER_3;
		}
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
		dir = tagCompound.getInteger("dir");
	}

	@Override
	public void writeNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		
		tagCompound.setBoolean("isMultiblock", isMultiblock);
		tagCompound.setInteger("harvesterLength", harvesterLength);
		tagCompound.setInteger("harvesterWidth", harvesterWidth);
		tagCompound.setInteger("dir", dir);
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
		if(!worldObj.isRemote){
			if(worldObj.getTotalWorldTime() % 60 == 0){
				if(firstRun){
					firstRun = false;
					if(isMultiblock){
						convertMultiblock();
					}
				}
				if(!isMultiblock){
					int dir = 0;
					while(dir < 4){
						if(checkMultiblock(dir)){
							this.dir = dir;
							isMultiblock = true;
							Functions.showMessageInChat("Width of harvester("+dir+"): " + harvesterWidth);
							Functions.showMessageInChat("Length of harvester("+dir+"): " + harvesterLength);
							convertMultiblock();
							break;
						}
						dir++;
					}
				}else{
					if(!checkMultiblock(dir)){
						//Multiblock no longer valid!
						isMultiblock = false;
						invalidateMultiblock();	
					}
				}
				
				if(isMultiblock){
					TileHydraulicPiston p = getPistonFromList(0);
					float pExtended = p.getExtendedLength();
					p.extendTo(pExtended + 1);
				}
			}
		}
	}
	
	private TileHydraulicPiston getPistonFromList(int index){
		Location v = pistonList.get(index);
		return (TileHydraulicPiston) worldObj.getBlockTileEntity(v.getX(), v.getY(), v.getZ());
	}
	
	private TileHydraulicPiston getPistonFromCoords(Location v){
		return (TileHydraulicPiston) worldObj.getBlockTileEntity(v.getX(), v.getY(), v.getZ());
	}

	@Override
	public float workFunction(boolean simulate) {
		return 0;
	}

	@Override
	public void onBlockBreaks() {
		if(isMultiblock){
			invalidateMultiblock();
		}
	}
	
	public void convertMultiblock(){
		//Build piston list.
		int x = xCoord;
		int y = yCoord + 3;
		int z = zCoord;
		int horiz = 0;
		
		//Check width:
		int width = 0;
		pistonList.clear();
		while(getBlockId(x, y, z) == idPiston){
			
			x = xCoord + (dir == 2 ? -horiz : (dir == 0 ? horiz : 0));
			y = yCoord + 3;
			z = zCoord + (dir == 1 ? -horiz : (dir == 3 ? horiz : 0));
			
			TileEntity tile = worldObj.getBlockTileEntity(x, y, z);
			if(tile instanceof TileHydraulicPiston){
				TileHydraulicPiston p = (TileHydraulicPiston)tile;
				p.setIsHarvesterPart(true);
				p.setMaxLength((float)harvesterLength-1);
				Location l = new Location(p.xCoord, p.yCoord, p.zCoord);
				pistonList.add(l);
			}
			horiz+=1;
			if(horiz > 10){
				break;
			}
		}
	}
	
	
	public void invalidateMultiblock(){
		Functions.showMessageInChat("Harvester invalidated!");
		dir = 0;
		isMultiblock = false;
		for(Location l : pistonList){
			TileHydraulicPiston p = getPistonFromCoords(l);
			if(p!=null){
				p.setIsHarvesterPart(false);
				p.extendTo(0F);
			}
		}
		pistonList.clear();
	}
	
	private int getBlockId(int x, int y, int z){
		return worldObj.getBlockId(x, y, z);
	}
	
	private int getBlockMetaFromCoord(int x, int y, int z){
		return worldObj.getBlockMetadata(x, y, z);
	}
	
	
	private boolean checkMultiblock(int dir){
		Log.info("------------ Now checking "+dir + "-------------");
		//Go up, check for pistons etc
		if(getBlockId(xCoord, yCoord + 1, zCoord) != idVerticalFrame) return false;
		if(getBlockId(xCoord, yCoord + 2, zCoord) != idVerticalFrame) return false;
		if(getBlockId(xCoord, yCoord + 3, zCoord) != idPiston) return false;
		int x = xCoord;
		int y = yCoord + 3;
		int z = zCoord;
		int horiz = 0;
		
		//Check width:
		int width = 0;
		while(getBlockId(x, y, z) == idPiston){
			width+=1;
			horiz+=1;
			x = xCoord + (dir == 2 ? -horiz : (dir == 0 ? horiz : 0));
			y = yCoord + 3;
			z = zCoord + (dir == 1 ? -horiz : (dir == 3 ? horiz : 0));
		}
		
		if(width > 9){
			return false;
		}
		
		
		int f = 0;
		horiz = 1;
		int length = 0;
		int firstLength = 0;
		for(f = 0; f < width; f++){
			if(f == 1){
				firstLength = length;
			}
			horiz = 1;
			length = 0;
			int nsToAdd = (dir == 1 ? -horiz : (dir == 3 ? horiz : 0));
			int ewToAdd = (dir == 0 ? -horiz : (dir == 2 ? horiz : 0));
			int nsSide = (dir == 2 ? -f : (dir == 0 ? f : 0));
			int ewSide = (dir == 1 ? -f : (dir == 3 ? f : 0));
			x = xCoord + nsToAdd + nsSide;
			y = yCoord + 3;
			z = zCoord + ewToAdd + ewSide;
			Log.info("(" + dir + ": " + x + ", " + y + ", " + z + "; " + f + ") = " + getBlockId(x, y, z) + " W: " + width);
			while(getBlockId(x, y, z) == idHorizontalFrame && getBlockMetaFromCoord(x, y, z) == 1){
				if(horiz == 1 && f == 0){
					//Check if there's a trolley right there!
					Log.info("(" + dir + ": " + x + ", " + (y-1) + ", " + z + "; " + f + ") = " + getBlockId(x, y-1, z) + " W: " + width);
					if(getBlockId(x, y-1, z) != idTrolley){
						return false;
					}
				}
				Log.info("(" + dir + ": " + x + ", " + y + ", " + z + "; " + f + ") = " + getBlockId(x, y, z) + " W: " + width);
				
				length += 1;
				horiz += 1;
				
				//Check if the frame is rotated:
				TileEntity tile = worldObj.getBlockTileEntity(x, y, z);
				if(tile instanceof TileHarvesterFrame){
					TileHarvesterFrame fr = (TileHarvesterFrame) tile;
					//Log.info("(" + dir + ": " + x + ", " + y + ", " + z + "; " + f + ") = " + fr.getIsRotated());
					if(dir == 3 || dir == 1){
						if(fr.getIsRotated()){
							return false;
						}
					}else{
						if(!fr.getIsRotated()){
							return false;
						}
					}
				}else{
					return false;
				}
				
				nsToAdd = (dir == 1 ? -horiz : (dir == 3 ? horiz : 0));
				ewToAdd = (dir == 0 ? -horiz : (dir == 2 ? horiz : 0));
				x = xCoord + nsToAdd + nsSide;
				y = yCoord + 3;
				z = zCoord + ewToAdd + ewSide;
			}
			if(f > 0){
				if(firstLength != length){
					return false;
				}
			}
			Log.info("F: "+f);
		}
		length = firstLength;
		
		if(length == 0){
			return false;
		}
		
		if(length < 4 && length > 0){
			return false;
		}
		if(length > 9){
			return false; //No more than 9!
		}
		
		x = xCoord + (dir == 1 ? -horiz : (dir == 3 ? horiz : 0));
		y = yCoord + 3;
		z = zCoord + (dir == 0 ? -horiz : (dir == 2 ? horiz : 0));
		
		if(getBlockId(x, y, z) == idEndBlock){
			length+=1;
		}else{
			return false;
		}
		
		
		//Check legs!
		horiz = 0;
		for(int vert = 0; vert <= 2; vert++){
			//Farmost left first.
			x = xCoord + (dir == 1 ? (-length) : (dir == 3 ? (length) : 0));
			y = yCoord + vert;
			z = zCoord + (dir == 0 ? (-length) : (dir == 2 ? (length) : 0));
			//Log.info("(" + dir + ": " + x + ", " + y + ", " + z + ") = " + getBlockId(x, y, z) + " W: " + width);
			int blockId = getBlockId(x, y, z);
			if(x == xCoord && y == yCoord && z == zCoord){
				
			}else{
				if(blockId != idVerticalFrame){
					return false;
				}
				
			}
			
			x+= (dir == 2 ? (-width+1) : (dir == 0 ? (width-1) : 0));
			z+= (dir == 1 ? (-width+1) : (dir == 3 ? (width-1) : 0));
			
			
			//Log.info("(" + dir + ": " + x + ", " + y + ", " + z + ") = " + getBlockId(x, y, z) + " W: " + width);
			blockId = getBlockId(x, y, z);
			if(x == xCoord && y == yCoord && z == zCoord){
				
			}else{
				if(blockId != idVerticalFrame){
					return false;
				}
				
			}
			
			//Base right
			x = xCoord + (dir == 2 ? (-width+1) : (dir == 0 ? (width-1) : 0));
			y = yCoord + vert;
			z = zCoord + (dir == 1 ? (-width+1) : (dir == 3 ? (width-1) : 0));
			
			//Log.info("(" + dir + ": " + x + ", " + y + ", " + z + ") = " + getBlockId(x, y, z) + " W: " + width);
			blockId = getBlockId(x, y, z);
			if(x == xCoord && y == yCoord && z == zCoord){
				
			}else{
				if(blockId != idVerticalFrame){
					return false;
				}
			}
		}
		
		harvesterLength = length;
		harvesterWidth = width;
		Log.info("Found at dir " + dir);
		return true;
	}

}
