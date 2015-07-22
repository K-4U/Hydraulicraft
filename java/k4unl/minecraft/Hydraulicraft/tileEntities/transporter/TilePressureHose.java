package k4unl.minecraft.Hydraulicraft.tileEntities.transporter;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicTransporter;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.tileEntities.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;
import java.util.Map;

public class TilePressureHose extends TileHydraulicBase implements IHydraulicTransporter {
	private Map<ForgeDirection, TileEntity> connectedSides = new HashMap<ForgeDirection, TileEntity>();
	private final boolean[] connectedSideFlags = new boolean[6];
    private boolean needToCheckNeighbors;
    private boolean connectedSidesHaveChanged = true;
    private boolean hasCheckedSinceStartup;
    private boolean hasFoundNetwork = false;
    private int tier = -1;
    
    public TilePressureHose(){
    	super(1);
    	super.init(this);
    }
    

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		setTier(tagCompound.getInteger("tier"));
		
		if(tagCompound.getBoolean("connectedSidesHaveChanged")){
			hasCheckedSinceStartup = false;
		}
		readConnectedSidesFromNBT(tagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setInteger("tier", getTier());
		
		if(connectedSidesHaveChanged && worldObj != null && !worldObj.isRemote){
			connectedSidesHaveChanged = false;
			tagCompound.setBoolean("connectedSidesHaveChanged", true);
		}
		writeConnectedSidesToNBT(tagCompound);
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
        tagCompound.setTag("connectedSides", ourCompound);
    }

	@Override
	public void updateEntity() {
		super.updateEntity();
		if(getHandler() != null){
    		//This should never happen that this is null! :|
    		//getHandler().updateEntity();
    	}else{
    		Log.error("PartHose does not have a handler!");
    	}
    	if(getWorldObj() != null){
	    	if(getWorldObj().getTotalWorldTime() % 10 == 0 && hasCheckedSinceStartup == false){
	    		checkConnectedSides();
	    		hasCheckedSinceStartup = true;
	    		//Hack hack hack
	    		//Temporary bug fix that we will forget about
	    	}
	    	//if(world().getTotalWorldTime() % 10 == 0 && pNetwork != null && !pNetwork.getMachines().contains(this.getHandler().getBlockLocation())){
	    		//Dum tie dum tie dum
	    		//If you see this, please step out of this if
	    		// *makes jedi hand motion* You never saw this!
	    		// TODO: figure out why the fuck this code is auto removing itself, without letting me know.
	    		// I Honestly believe it's because of FMP
	    		//getHandler().updateNetworkOnNextTick(pNetwork.getPressure());
	    		//pNetwork.addMachine(this, pNetwork.getPressure());
	    	//}
    	}
    	
        if(needToCheckNeighbors) {
            needToCheckNeighbors = false;
            connectedSides = new HashMap<ForgeDirection, TileEntity>();
            for(int i = 0; i < 6; i++) {
                if(connectedSideFlags[i]) {
                    ForgeDirection dir = ForgeDirection.getOrientation(i);
                    connectedSides.put(dir, getWorldObj().getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ));
                }
            }
            if(!getWorldObj().isRemote){
        		connectedSidesHaveChanged = true;
        		getHandler().updateBlock();
        	}
        }
	}

	@Override
	public void onFluidLevelChanged(int old) { }

	private boolean shouldConnectTo(TileEntity entity, ForgeDirection dir, Object caller){
    	/* FMP if(entity instanceof TileMultipart){
    		List<TMultiPart> t = ((TileMultipart)entity).jPartList();
    		
    		if(Multipart.hasPartHose((TileMultipart)entity)){
    			if(!((TileMultipart)entity).canAddPart(new NormallyOccludedPart(boundingBoxes[opposite]))) return false;
    		}
    		
    		for (TMultiPart p: t) {
    			if(p instanceof IHydraulicTransporter && caller.equals(this)){
    				((IHydraulicTransporter)p).checkConnectedSides(this);
    			}
				if(p instanceof IHydraulicMachine){
					return ((IHydraulicMachine)p).canConnectTo(dir.getOpposite());
				}
			}
    		return false;
    	}else{*/
    		if(entity instanceof IHydraulicMachine){
    			return ((IHydraulicMachine)entity).canConnectTo(dir.getOpposite());
    		}else{
    			return false;
    		}
    	//}
    }

    public boolean isConnectedTo(ForgeDirection side){
    	if(getWorldObj() != null){
	    	TileEntity te = getWorldObj().getTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ);
	    	return /* FMP tile().canAddPart(new NormallyOccludedPart(boundingBoxes[d])) && */shouldConnectTo(te, side, this);
    	}else{
    		return false;
    	}
    }
    
    public void checkConnectedSides(){
    	checkConnectedSides(this);
    }
    
    public void checkConnectedSides(Object caller){
        connectedSides = new HashMap<ForgeDirection, TileEntity>();
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			
            TileEntity te = getWorldObj().getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
            if(shouldConnectTo(te, dir, caller)) {
            	/*if(tile().canAddPart(new NormallyOccludedPart(boundingBoxes[d]))){
            		connectedSides.put(dir, te);
            	}*/
            	connectedSides.put(dir, te);
            }
        }
		connectedSidesHaveChanged = true;
		getHandler().updateBlock();
    }
    
    @Override
	public boolean canConnectTo(ForgeDirection side) {
    	/* FMP int d = side.getOpposite().ordinal();
    	return tile().canAddPart(new NormallyOccludedPart(boundingBoxes[d]));*/
    	return true;
	}
	
    public Map<ForgeDirection, TileEntity> getConnectedSides() {
		if(connectedSides == null){
			checkConnectedSides();
		}
		return connectedSides;
	}

	@Override
	public void updateNetwork(float oldPressure) {
		PressureNetwork newNetwork = null;
		PressureNetwork foundNetwork;
		PressureNetwork endNetwork = null;
		//This block can merge networks!
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			if(!isConnectedTo(dir)){
				continue;
			}
			TileEntity ent = getWorldObj().getTileEntity(xCoord + dir.offsetX, yCoord+dir.offsetY, zCoord+ dir.offsetZ);
			if(ent == null) continue;
			if(!shouldConnectTo(ent, dir, this)) continue;
			foundNetwork = PressureNetwork.getNetworkInDir(getWorldObj(), xCoord, yCoord, zCoord, dir);
			if(foundNetwork != null){
				if(endNetwork == null){
					endNetwork = foundNetwork;
				}else{
					newNetwork = foundNetwork;
				}
			}
			
			if(newNetwork != null){
				//Hmm.. More networks!? What's this!?
				//Log.info("Found an existing network (" + newNetwork.getRandomNumber() + ") @ " + x() + "," + y() + "," + z());
				endNetwork.mergeNetwork(newNetwork);
				newNetwork = null;
			}
			
		}
			
		if(endNetwork != null){
			pNetwork = endNetwork;
			pNetwork.addMachine(this, oldPressure, ForgeDirection.UP);
			//Log.info("Found an existing network (" + pNetwork.getRandomNumber() + ") @ " + x() + "," + y() + "," + z());
		}else{
			pNetwork = new PressureNetwork(this, oldPressure, ForgeDirection.UP);
			//Log.info("Created a new network (" + pNetwork.getRandomNumber() + ") @ " + x() + "," + y() + "," + z());
		}
		hasFoundNetwork = true;
	}
   
	
	public int getTier() {
		if(tier == -1){
			tier = getWorldObj().getBlockMetadata(xCoord, yCoord, zCoord);
		}
		return tier;
	}

	public void setTier(int metadata) {
		tier = metadata;
		super.setMaxStorage(2*(tier+1));
	}

	public void refreshConnectedSides() {
		this.hasCheckedSinceStartup = false;
	}
}
