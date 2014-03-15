package k4unl.minecraft.Hydraulicraft.multipart;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicTransporter;
import k4unl.minecraft.Hydraulicraft.api.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.blocks.Blocks;
import k4unl.minecraft.Hydraulicraft.client.renderers.RendererHydraulicHose;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.BlockWall;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;

import org.lwjgl.opengl.GL11;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.render.EntityDigIconFX;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.IHollowConnect;
import codechicken.multipart.JNormalOcclusion;
import codechicken.multipart.NormalOcclusionTest;
import codechicken.multipart.NormallyOccludedPart;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TSlottedPart;
import codechicken.multipart.TileMultipart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PartHose extends TMultiPart implements TSlottedPart, JNormalOcclusion, IHollowConnect, IHydraulicTransporter {
    public static Cuboid6[] boundingBoxes = new Cuboid6[14];
    private static int expandBounds = -1;
    
    private PressureNetwork pNetwork;
    
    private IBaseClass baseHandler;
    private Map<ForgeDirection, TileEntity> connectedSides;
    private final boolean[] connectedSideFlags = new boolean[6];
    private boolean needToCheckNeighbors;
    private boolean connectedSidesHaveChanged = true;
    private boolean hasCheckedSinceStartup;
    private boolean hasFoundNetwork = false;
    
    private int tier = 0;

    @SideOnly(Side.CLIENT)
    private static RendererHydraulicHose renderer;
    
    @SideOnly(Side.CLIENT)
    private static Icon breakIcon;
    
    static {
    	float center = 0.5F;
    	float offset = 0.10F;
    	//float offsetY = 0.2F;
    	//float offsetZ = 0.2F;
    	float centerFirst = center - offset;
    	float centerSecond = center + offset;
    	Vector3 rotateCenterFirst = new Vector3(centerFirst, centerFirst, centerFirst);
    	Vector3 rotateCenterSecond = new Vector3(centerSecond, centerSecond, centerSecond);
        double w = 0.2D / 2;
        boundingBoxes[6] = new Cuboid6(centerFirst - w, centerFirst - w, centerFirst - w, centerFirst + w, centerFirst + w, centerFirst + w);
        boundingBoxes[13] = new Cuboid6(centerSecond - w, centerSecond - w, centerSecond - w, centerSecond + w, centerSecond + w, centerSecond + w);
        
        int i = 0;
    	for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
    		double xMin1 = (dir.offsetX < 0 ? 0.0 : (dir.offsetX == 0 ? centerFirst - w : centerFirst + w));
    		double xMax1 = (dir.offsetX > 0 ? 1.0 : (dir.offsetX == 0 ? centerFirst + w : centerFirst - w));
    		
    		double yMin1 = (dir.offsetY < 0 ? 0.0 : (dir.offsetY == 0 ? centerFirst - w : centerFirst + w));
    		double yMax1 = (dir.offsetY > 0 ? 1.0 : (dir.offsetY == 0 ? centerFirst + w : centerFirst - w));
    		
    		double zMin1 = (dir.offsetZ < 0 ? 0.0 : (dir.offsetZ == 0 ? centerFirst - w : centerFirst + w));
    		double zMax1 = (dir.offsetZ > 0 ? 1.0 : (dir.offsetZ == 0 ? centerFirst + w : centerFirst - w));
    		
    		double xMin2 = (dir.offsetX < 0 ? 0.0 : (dir.offsetX == 0 ? centerSecond - w : centerSecond + w));
    		double xMax2 = (dir.offsetX > 0 ? 1.0 : (dir.offsetX == 0 ? centerSecond + w : centerSecond - w));
    		
    		double yMin2 = (dir.offsetY < 0 ? 0.0 : (dir.offsetY == 0 ? centerSecond - w : centerSecond + w));
    		double yMax2 = (dir.offsetY > 0 ? 1.0 : (dir.offsetY == 0 ? centerSecond + w : centerSecond - w));
    		
    		double zMin2 = (dir.offsetZ < 0 ? 0.0 : (dir.offsetZ == 0 ? centerSecond - w : centerSecond + w));
    		double zMax2 = (dir.offsetZ > 0 ? 1.0 : (dir.offsetZ == 0 ? centerSecond + w : centerSecond - w));
    		
    		Cuboid6 first = new Cuboid6(xMin1, yMin1, zMin1, xMax1, yMax1, zMax1);
    		Cuboid6 second = new Cuboid6(xMin2, yMin2, zMin2, xMax2, yMax2, zMax2);
    		boundingBoxes[i] = first;
    		boundingBoxes[i+7] = new Cuboid6(xMin2, yMin2, zMin2, xMax2, yMax2, zMax2);
    		i++;
    	}
        
        //for (int s = 0; s < 6; s++)
        //	boundingBoxes[s] = new Cuboid6(centerFirst - w, offset, centerFirst - w, centerFirst + w, centerFirst - w, centerFirst + w).apply(Rotation.sideRotations[s].at(rotateCenterFirst));
        
        //for (int s = 7; s < 12; s++)
        //    boundingBoxes[s] = new Cuboid6(centerSecond - w, 0, centerSecond - w, centerSecond + w, centerSecond - w, centerSecond + w).apply(Rotation.sideRotations[s-7].at(rotateCenterSecond));
    }
    
	@Override
	public String getType() {
		return "tile." + Names.blockHydraulicHose[0].unlocalized;
	}

	public void preparePlacement(int itemDamage) {
		tier = itemDamage;
	}
	
	@Override
	public void load(NBTTagCompound tagCompound){
		super.load(tagCompound);
		if(getHandler() != null)
			getHandler().readFromNBT(tagCompound);
		tier = tagCompound.getInteger("tier");
		/*
		float oldPressure = 0F;
        if(pNetwork != null){
        	oldPressure = pNetwork.getPressure();
        }*/
		//getHandler().updateNetworkOnNextTick(oldPressure);
		//checkConnectedSides();
		readConnectedSidesFromNBT(tagCompound);
	}
	
	@Override
	public void save(NBTTagCompound tagCompound){
		super.save(tagCompound);
		getHandler().writeToNBT(tagCompound);
		tagCompound.setInteger("tier", tier);
		writeConnectedSidesToNBT(tagCompound);
	}
	
	@Override
    public void writeDesc(MCDataOutput packet){
		packet.writeInt(getTier());
		
		NBTTagCompound mainCompound = new NBTTagCompound();
		NBTTagCompound handlerCompound = new NBTTagCompound();
		if(connectedSidesHaveChanged && world() != null && !world().isRemote){
			connectedSidesHaveChanged = false;
			mainCompound.setBoolean("connectedSidesHaveChanged", true);
			//writeConnectedSidesToNBT(connectedCompound);
			//mainCompound.setCompoundTag("connectedSides", connectedCompound);
		}
		getHandler().writeToNBT(handlerCompound);
		mainCompound.setCompoundTag("handler", handlerCompound);
		
		packet.writeNBTTagCompound(mainCompound);
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
	
    @Override
    public void readDesc(MCDataInput packet){
        tier = packet.readInt();
        
        NBTTagCompound mainCompound = packet.readNBTTagCompound();
		//NBTTagCompound connectedCompound = mainCompound.getCompoundTag("connectedSides");
		NBTTagCompound handlerCompound = mainCompound.getCompoundTag("handler");
		if(mainCompound.getBoolean("connectedSidesHaveChanged")){
			hasCheckedSinceStartup = false;
		}
		//readConnectedSidesFromNBT(connectedCompound);
        
        getHandler().readFromNBT(handlerCompound);
    }

	
	@Override
	public int getHollowSize() {
		return 6;
	}

	@Override
	public int getSlotMask() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	 @Override
    public Iterable<IndexedCuboid6> getSubParts() {
        Iterable<Cuboid6> boxList = getCollisionBoxes();
        LinkedList<IndexedCuboid6> partList = new LinkedList<IndexedCuboid6>();
        for (Cuboid6 c : boxList)
            partList.add(new IndexedCuboid6(0, c));
        return partList;
    }

    @Override
    public boolean occlusionTest(TMultiPart npart){
        return NormalOcclusionTest.apply(this, npart);
    }

    @Override
    public Iterable<Cuboid6> getOcclusionBoxes(){
        if (expandBounds >= 0)
            return Arrays.asList(boundingBoxes[expandBounds]);

        return Arrays.asList(boundingBoxes[6], boundingBoxes[13]);
    }

    @Override
    public Iterable<Cuboid6> getCollisionBoxes(){
        LinkedList<Cuboid6> list = new LinkedList<Cuboid6>();
        list.add(boundingBoxes[6]);
        list.add(boundingBoxes[13]);
        if(connectedSides == null) return list;
        for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
        	if(connectedSides.containsKey(dir)){
        		list.add(boundingBoxes[Functions.getIntDirFromDirection(dir)]);
        		list.add(boundingBoxes[Functions.getIntDirFromDirection(dir)+7]);
        	}
        }
        return list;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vector3 pos, float frame, int pass){
        if (pass == 0){
        	if(renderer == null){
        		renderer = new RendererHydraulicHose();
        	}
            GL11.glDisable(GL11.GL_LIGHTING);
            renderer.doRender(pos.x, pos.y, pos.z, 0, tier, connectedSides);
            GL11.glEnable(GL11.GL_LIGHTING);
        }
    }

    private boolean shouldConnectTo(TileEntity entity, ForgeDirection dir, Object caller){
    	int opposite = Functions.getIntDirFromDirection(dir.getOpposite());
    	if(entity instanceof TileMultipart){
    		List<TMultiPart> t = ((TileMultipart)entity).jPartList();
    		
    		if(!((TileMultipart)entity).canAddPart(new NormallyOccludedPart(boundingBoxes[opposite]))) return false;
    		
    		for (TMultiPart p: t) {
    			if(p instanceof PartHose && caller.equals(this)){
    				((PartHose)p).checkConnectedSides(this);
    			}
				if(p instanceof IHydraulicMachine){
					return true;
				}
			}
    		return false;
    	}else{
    		if(entity instanceof IHydraulicMachine){
    			return ((IHydraulicMachine)entity).canConnectTo(dir.getOpposite());
    		}else{
    			return false;
    		}
    	}
    }

    public boolean isConnectedTo(ForgeDirection side){
    	int d = side.ordinal();
    	
    	if(world() != null && tile() != null){
	    	TileEntity te = world().getBlockTileEntity(x() + side.offsetX, y() + side.offsetY, z() + side.offsetZ);
	    	return tile().canAddPart(new NormallyOccludedPart(boundingBoxes[d])) && shouldConnectTo(te, side, this);
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
			int d = Functions.getIntDirFromDirection(dir);
			
            TileEntity te = world().getBlockTileEntity(x() + dir.offsetX, y() + dir.offsetY, z() + dir.offsetZ);
            if(shouldConnectTo(te, dir, caller)) {
            	if(tile().canAddPart(new NormallyOccludedPart(boundingBoxes[d]))){
            		connectedSides.put(dir, te);
            	}
            }
        }
		connectedSidesHaveChanged = true;
		getHandler().updateBlock();
    }
    
    @Override
	public boolean canConnectTo(ForgeDirection side) {
    	int d = side.getOpposite().ordinal();
    	return tile().canAddPart(new NormallyOccludedPart(boundingBoxes[d]));
	}
    
    public void onNeighborChanged(){
        checkConnectedSides();
        if(!world().isRemote){
        	//getHandler().updateFluidOnNextTick();
        	/*float oldPressure = 0F;
            if(pNetwork != null){
            	oldPressure = pNetwork.getPressure();
            }*/
        	//getHandler().updateNetworkOnNextTick(oldPressure);
        }
    }
    
    public ItemStack getItem(){
        return new ItemStack(Multipart.itemPartHose, 1, tier);
    }
    
    @Override
    public void onPartChanged(TMultiPart part){
        checkConnectedSides();
        //getHandler().updateFluidOnNextTick();
        if(!world().isRemote && hasFoundNetwork == true){
	        float oldPressure = 0F;
	        if(pNetwork != null){
	        	oldPressure = pNetwork.getPressure();
	        }
			getHandler().updateNetworkOnNextTick(oldPressure);
        }
    }
    
    @Override
    public Iterable<ItemStack> getDrops() {
    
        LinkedList<ItemStack> items = new LinkedList<ItemStack>();
        items.add(getItem());
        return items;
    }
    
    @Override
    public ItemStack pickItem(MovingObjectPosition hit){
        return getItem();
    }

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validate() {
		
	}

	@Override
	public void onPressureChanged(float old) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFluidLevelChanged(int old) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getMaxStorage() {
		return FluidContainerRegistry.BUCKET_VOLUME * (2 * (getTier()+1));
	}

	public int getTier() {
		return tier;
	}
	
	@Override
	public void onBlockBreaks() {
	}

    @Override
    public float getMaxPressure(boolean isOil, ForgeDirection from){
        if(isOil) {
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
	public IBaseClass getHandler() {
		if(baseHandler == null) baseHandler = HydraulicBaseClassSupplier.getBaseClass(this);
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
	public void readNBT(NBTTagCompound tagCompound) {
		//readConnectedSidesFromNBT(tagCompound);
	}

	@Override
	public void writeNBT(NBTTagCompound tagCompound) {
		//writeConnectedSidesToNBT(tagCompound);		
	}
	
    @Override
    public void update(){
    	if(getHandler() != null){
    		//This should never happen that this is null! :|
    		getHandler().updateEntity();
    	}else{
    		Log.error("PartHose does not have a handler!");
    	}
    	if(world() != null){
	    	if(world().getTotalWorldTime() % 10 == 0 && hasCheckedSinceStartup == false){
	    		checkConnectedSides();
	    		hasCheckedSinceStartup = true;
	    		//Hack hack hack
	    		//Temporary bug fix that we will forget about
	    	}
	    	if(world().getTotalWorldTime() % 10 == 0 && pNetwork != null && !pNetwork.getMachines().contains(this.getHandler().getBlockLocation())){
	    		//Dum tie dum tie dum
	    		//If you see this, please step out of this if
	    		// *makes jedi hand motion* You never saw this!
	    		// TODO: figure out why the fuck this code is auto removing itself, without letting me know.
	    		// I Honestly believe it's because of FMP
	    		//getHandler().updateNetworkOnNextTick(pNetwork.getPressure());
	    		//pNetwork.addMachine(this, pNetwork.getPressure());
	    	}
    	}
    	
        if(needToCheckNeighbors) {
            needToCheckNeighbors = false;
            connectedSides = new HashMap<ForgeDirection, TileEntity>();
            for(int i = 0; i < 6; i++) {
                if(connectedSideFlags[i]) {
                    ForgeDirection dir = ForgeDirection.getOrientation(i);
                    connectedSides.put(dir, world().getBlockTileEntity(x() + dir.offsetX, y() + dir.offsetY, z() + dir.offsetZ));
                }
            }
            if(!world().isRemote){
        		connectedSidesHaveChanged = true;
        		getHandler().updateBlock();
        	}
        }
    }

	@Override
	public void updateEntity() {
		// TODO Auto-generated method stub
		
	}

	public Map<ForgeDirection, TileEntity> getConnectedSides() {
		if(connectedSides == null){
			checkConnectedSides();
		}
		return connectedSides;
	}
	
	

	@Override
	public PressureNetwork getNetwork(ForgeDirection side) {
		return pNetwork;
	}

	@Override
	public void setNetwork(ForgeDirection side, PressureNetwork toSet) {
		pNetwork = toSet;
	}

	@Override
	public void firstTick() {
				
	}

	@Override
	public float getPressure(ForgeDirection from) {
		if(world().isRemote){
			return getHandler().getPressure();
		}
		if(getNetwork(from) == null){
			Log.error("PVAT at " + getHandler().getBlockLocation().printCoords() + " has no pressure network!");
			return 0;
		}
		return getNetwork(from).getPressure();
	}

	@Override
	public void setPressure(float newPressure, ForgeDirection side) {
		getNetwork(side).setPressure(newPressure);
	}

	@Override
	public void updateNetwork(float oldPressure) {
		PressureNetwork newNetwork = null;
		PressureNetwork foundNetwork = null;
		PressureNetwork endNetwork = null;
		//This block can merge networks!
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			if(!isConnectedTo(dir)){
				continue;
			}
			foundNetwork = PressureNetwork.getNetworkInDir(world(), x(), y(), z(), dir);
			if(foundNetwork != null){
				if(endNetwork == null){
					endNetwork = foundNetwork;
				}else{
					newNetwork = foundNetwork;
				}
			}
			
			if(newNetwork != null && endNetwork != null){
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
	
	@Override
	public void onRemoved(){
		if(!world().isRemote){
			if(pNetwork != null){
				pNetwork.removeMachine(this);
			}
		}
	}

	@Override
	public int getFluidInNetwork(ForgeDirection from) {
		if(world().isRemote){
			//TODO: Store this in a variable locally. Mostly important for pumps though.
			return 0;
		}else{
			return getNetwork(from).getFluidInNetwork();
		}
	}

	@Override
	public int getFluidCapacity(ForgeDirection from) {
		if(world().isRemote){
			//TODO: Store this in a variable locally. Mostly important for pumps though.
			return 0;
		}else{
			return getNetwork(from).getFluidCapacity();
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void addDestroyEffects(MovingObjectPosition hit, EffectRenderer effectRenderer){
		addDestroyEffects(effectRenderer);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void addDestroyEffects(EffectRenderer effectRenderer) {
		if(breakIcon == null){
			breakIcon = Blocks.hydraulicPressureWall.getIcon(0, 0);
		}
        EntityDigIconFX.addBlockDestroyEffects(world(), Cuboid6.full.copy()
                .add(Vector3.fromTileEntity(tile())), new Icon[] { breakIcon,
                breakIcon, breakIcon, breakIcon, breakIcon, breakIcon },
                effectRenderer);
    }
	
	@SideOnly(Side.CLIENT)
    @Override
    public void addHitEffects(MovingObjectPosition hit,
            EffectRenderer effectRenderer) {
   
        EntityDigIconFX.addBlockHitEffects(world(),
                Cuboid6.full.copy().add(Vector3.fromTileEntity(tile())),
                hit.sideHit, breakIcon, effectRenderer);
    }
	
	@Override
	public float getStrength(MovingObjectPosition hit, EntityPlayer player){
		return 8F;
	}
	
}
