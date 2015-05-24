package k4unl.minecraft.Hydraulicraft.multipart;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicTransporter;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.api.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.client.renderers.transportation.RendererPartValve;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.Hydraulicraft.tileEntities.interfaces.ICustomNetwork;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.render.EntityDigIconFX;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.ISidedHollowConnect;
import codechicken.multipart.JNormalOcclusion;
import codechicken.multipart.NormalOcclusionTest;
import codechicken.multipart.NormallyOccludedPart;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TSlottedPart;
import codechicken.multipart.TileMultipart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class PartValve extends TMultiPart implements TSlottedPart, JNormalOcclusion, ISidedHollowConnect, IHydraulicTransporter, ICustomNetwork,
  ITieredBlock {

	public static Cuboid6 boundingBox;
	public static Cuboid6[] boundingBoxes = new Cuboid6[6];
	public static Cuboid6 boundingBoxC;
    public static Cuboid6 boundingBoxNS;
    public static Cuboid6 boundingBoxEW;
    public static Cuboid6 boundingBoxUD;
    private static int expandBounds = -1;

    private IBaseClass baseHandler;
    private boolean needToCheckNeighbors;
    private boolean connectedSidesHaveChanged = true;
    private boolean hasCheckedSinceStartup;
    private boolean hasFoundNetwork = false;
    private ForgeDirection facing = ForgeDirection.NORTH;
    private boolean hasDirection = false;

    private PressureNetwork pNetwork1;
    private PressureNetwork pNetwork2;
    private boolean hasMerged = false;

    private PressureTier tier = PressureTier.INVALID;

    @SideOnly(Side.CLIENT)
    private static RendererPartValve renderer;

    @SideOnly(Side.CLIENT)
    private static IIcon breakIcon;

    static {
    	float center = 0.5F;

        float width = 0.2F;
        float min = (center - width);
		float max = (center + width);
        boundingBoxNS = new Cuboid6(min, min, 0.0F, max, max, 1.0F);
        boundingBoxEW = new Cuboid6(0.0F, min, min, 1.0F, max, max);
        boundingBoxUD = new Cuboid6(min, 0.0F, min, max, 1.0F, max);
        boundingBoxC = new Cuboid6(min, min, min, max, max, max);
        boundingBox = boundingBoxNS;
        boundingBoxes[ForgeDirection.NORTH.ordinal()] = new Cuboid6(min, min, 0.0F, max, max, min);
        boundingBoxes[ForgeDirection.SOUTH.ordinal()] = new Cuboid6(min, min, max, max, max, 1.0F);

        boundingBoxes[ForgeDirection.EAST.ordinal()] = new Cuboid6(max, min, min, 1.0F, max, max);
        boundingBoxes[ForgeDirection.WEST.ordinal()] = new Cuboid6(0.0F, min, min, min, max, max);

        boundingBoxes[ForgeDirection.UP.ordinal()] = new Cuboid6(min, max, min, max, 1.0F, max);
        boundingBoxes[ForgeDirection.DOWN.ordinal()] = new Cuboid6(min, 0.0F, min, max, min, max);
    }

	@Override
	public String getType() {
		return "tile." + Names.partValve[0].unlocalized;
	}

	public void preparePlacement(int itemDamage) {
		tier = PressureTier.fromOrdinal(itemDamage);
	}

	@Override
	public void load(NBTTagCompound tagCompound){
		super.load(tagCompound);
		if(getHandler() != null)
			getHandler().readFromNBTI(tagCompound);
		tier = PressureTier.fromOrdinal(tagCompound.getInteger("tier"));
		facing = ForgeDirection.getOrientation(tagCompound.getInteger("facing"));
		hasDirection = tagCompound.getBoolean("hasDirection");
	}

	@Override
	public void save(NBTTagCompound tagCompound){
		super.save(tagCompound);
		getHandler().writeToNBTI(tagCompound);
		tagCompound.setInteger("facing", getFacing().ordinal());
		tagCompound.setBoolean("hasDirection", hasDirection);
		tagCompound.setInteger("tier", getTier().toInt());
	}

	@Override
    public void writeDesc(MCDataOutput packet){
		packet.writeInt(getTier().toInt());

		NBTTagCompound mainCompound = new NBTTagCompound();
		mainCompound.setInteger("facing", getFacing().ordinal());
		mainCompound.setBoolean("hasDirection", hasDirection);
		mainCompound.setBoolean("hasMerged", hasMerged);
		NBTTagCompound handlerCompound = new NBTTagCompound();
		if(connectedSidesHaveChanged && world() != null && !world().isRemote){
			connectedSidesHaveChanged = false;
			mainCompound.setBoolean("connectedSidesHaveChanged", true);
		}
		getHandler().writeToNBTI(handlerCompound);
		mainCompound.setTag("handler", handlerCompound);
		
		packet.writeNBTTagCompound(mainCompound);
    }
	
    @Override
    public void readDesc(MCDataInput packet){
        tier = PressureTier.fromOrdinal(packet.readInt());
        
        NBTTagCompound mainCompound = packet.readNBTTagCompound();
        facing = ForgeDirection.getOrientation(mainCompound.getInteger("facing"));
        hasDirection = mainCompound.getBoolean("hasDirection");
        hasMerged = mainCompound.getBoolean("hasMerged");
		NBTTagCompound handlerCompound = mainCompound.getCompoundTag("handler");
		if(mainCompound.getBoolean("connectedSidesHaveChanged")){
			hasCheckedSinceStartup = false;
		}
        
        getHandler().readFromNBTI(handlerCompound);
    }

	@Override
	public int getSlotMask() {
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
    	LinkedList<Cuboid6> list = new LinkedList<Cuboid6>();
    	list.add(boundingBoxC);
    	list.add(boundingBoxes[getFacing().ordinal()]);
    	list.add(boundingBoxes[getFacing().getOpposite().ordinal()]);
    	return list;
    }
    
    public Iterable<Cuboid6> getBoundingBox(ForgeDirection dir){
    	return Arrays.asList(boundingBoxes[dir.ordinal()], boundingBoxes[dir.getOpposite().ordinal()]);
    }

    @Override
    public Iterable<Cuboid6> getCollisionBoxes(){
    	LinkedList<Cuboid6> list = new LinkedList<Cuboid6>();
    	list.add(boundingBoxC);
    	if(!getFacing().equals(ForgeDirection.UNKNOWN)){
	    	list.add(boundingBoxes[getFacing().ordinal()]);
	    	list.add(boundingBoxes[getFacing().getOpposite().ordinal()]);
    	}
    	return list;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vector3 pos, float frame, int pass){
        if (pass == 0){
        	if(renderer == null){
        		renderer = new RendererPartValve();
        	}
        	GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_LIGHTING);
			
            renderer.doRender(pos.x, pos.y, pos.z, frame, tier.toInt(), this);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
    }

    private boolean shouldConnectTo(TileEntity entity, ForgeDirection dir, Object caller){
    	if(entity instanceof TileMultipart){
    		List<TMultiPart> t = ((TileMultipart)entity).jPartList();
    		
    		for (TMultiPart p: t) {
    			if(p instanceof IHydraulicTransporter && caller.equals(this)){
    				((IHydraulicTransporter)p).checkConnectedSides(this);
    			}
				if(p instanceof IHydraulicMachine){
					return ((IHydraulicMachine)p).canConnectTo(dir.getOpposite());
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
    	if(world() != null && tile() != null){
	    	TileEntity te = world().getTileEntity(x() + side.offsetX, y() + side.offsetY, z() + side.offsetZ);
	    	NormallyOccludedPart p = new NormallyOccludedPart(getBoundingBox(side));
	    	boolean canAddPart = tile().canAddPart(p);
	    	if(side.equals(getFacing()) || side.getOpposite().equals(getFacing())){
	    		canAddPart = true;
	    	}
	    	return canAddPart && shouldConnectTo(te, side, this);
    	}else{
    		return false;
    	}
    }
    
    public void checkConnectedSides(){
    	checkConnectedSides(this);
    }
    
    public void checkConnectedSides(Object caller){
    	if(hasDirection){
	    	if(isConnectedTo(getFacing()) || isConnectedTo(getFacing().getOpposite())){
	    		
	    	}else{
	    		NormallyOccludedPart p = new NormallyOccludedPart(getBoundingBox(ForgeDirection.NORTH));
		    	boolean canAddPart = tile().canAddPart(p);
		    	if(canAddPart){
		    		facing = ForgeDirection.NORTH;
		    	}
	    		hasDirection = false;
	    	}
    	}else{
    		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
    			if(isConnectedTo(dir)){
    				facing = dir;
    				hasDirection = true;
    				break;
    			}
    		}
    	}
		//connectedSidesHaveChanged = true;
		//getHandler().updateBlock();
    }
    
    @Override
	public boolean canConnectTo(ForgeDirection side) {
    	//Do some ray tracing here as well..
    	if(!hasDirection){
    		NormallyOccludedPart p = new NormallyOccludedPart(getBoundingBox(side));
    		if(tile().canAddPart(p)){
	    		return true;
    		}else{
    			return false;
    		}
    	}else if(getFacing().equals(side) || getFacing().equals(side.getOpposite())){
    		return true;
    	}else{
    		return false;
    	}
	}
    
    public ForgeDirection getFacing(){
    	return facing;
    }
    
    public void onNeighborChanged(){
        checkConnectedSides();
        checkRedstone();
        if(!world().isRemote){
        	//getHandler().updateFluidOnNextTick();
        	//getHandler().updateNetworkOnNextTick(oldPressure);
        }
    }
    
    public ItemStack getItem(){
        return new ItemStack(Multipart.itemPartValve, 1, tier.toInt());
    }
    
    @Override
    public void onPartChanged(TMultiPart part){
        checkConnectedSides();
        checkRedstone();
        //getHandler().updateFluidOnNextTick();
        if(!world().isRemote){
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

	public PressureTier getTier() {
		return tier;
	}
	
	@Override
	public IBaseClass getHandler() {
		if(baseHandler == null) baseHandler = HydraulicBaseClassSupplier.getBaseClass(this, 2 * (getTier().toInt()+1));
        return baseHandler;
	}

	private void checkRedstone(){
		((TileHydraulicBase) getHandler()).checkRedstonePower();
	}
	
    @Override
    public void update(){
    	if(getHandler() != null){
    		//This should never happen that this is null! :|
    		getHandler().updateEntityI();
    	}else{
    		Log.error("PartValve does not have a handler!");
    	}
    	if(world() != null){
	    	if(world().getTotalWorldTime() % 10 == 0 && hasCheckedSinceStartup == false){
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
    	
    	if(((TileHydraulicBase) getHandler()).getRedstonePowered() && hasMerged == false && pNetwork1 != null && pNetwork2 != null){
			pNetwork1.mergeNetwork(pNetwork2);
			hasMerged = true;
		}else if(hasMerged == true && !((TileHydraulicBase) getHandler()).getRedstonePowered() && pNetwork1 != null){
			hasMerged = false;
			getHandler().updateNetworkOnNextTick(pNetwork1.getPressure());
			pNetwork1.removeMachine(this);
			
		}
        if(needToCheckNeighbors) {
            needToCheckNeighbors = false;
            
            if(!world().isRemote){
        		connectedSidesHaveChanged = true;
        		getHandler().updateBlock();
        	}
        }
    }

	@Override
	public PressureNetwork getNetwork(ForgeDirection side) {
		if(side.equals(getFacing())){
			return pNetwork1;			
		}else if(side.equals(getFacing().getOpposite())){
			return pNetwork2;
		}else{
			return null;
		}
		
	}

	@Override
	public void setNetwork(ForgeDirection side, PressureNetwork toSet) {
		if(side.equals(getFacing())){
			pNetwork1 = toSet;			
		}else if(side.equals(getFacing().getOpposite())){
			pNetwork2 = toSet;
		}
	}

	@Override
	public void updateNetwork(float oldPressure) {
		if(!hasDirection){
			pNetwork1 = new PressureNetwork(this, oldPressure, getFacing());
			pNetwork2 = new PressureNetwork(this, oldPressure, getFacing().getOpposite());
		}else{
			PressureNetwork foundNetwork = null;
			foundNetwork = PressureNetwork.getNetworkInDir(world(), x(), y(), z(), getFacing());
			if(foundNetwork != null){
				if(pNetwork1 != null){
					pNetwork1.mergeNetwork(foundNetwork);
				}else{
					pNetwork1 = foundNetwork;
					pNetwork1.addMachine(this, oldPressure, getFacing());
				}
			}else{
				pNetwork1 = new PressureNetwork(this, oldPressure, getFacing());
			}
			foundNetwork = null;
			foundNetwork = PressureNetwork.getNetworkInDir(world(), x(), y(), z(), getFacing().getOpposite());
			if(foundNetwork != null){
				if(pNetwork2 != null){
					pNetwork2.mergeNetwork(foundNetwork);
				}else{
					pNetwork2 = foundNetwork;
					pNetwork2.addMachine(this, oldPressure, getFacing());
				}
			}else{
				pNetwork2 = new PressureNetwork(this, oldPressure, getFacing().getOpposite());
			}
		}
		if(((TileHydraulicBase) getHandler()).getRedstonePowered()){
			pNetwork1.mergeNetwork(pNetwork2);
			hasMerged = true;
		}
	}
	
	@Override
	public void onRemoved(){
		if(!world().isRemote){
			if(pNetwork1 != null){
				pNetwork1.removeMachine(this);
				pNetwork1 = null;
			}
			if(pNetwork2 != null){
				pNetwork2.removeMachine(this);
				pNetwork2 = null;
			}
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
			breakIcon = HCBlocks.hydraulicPressureWall.getIcon(0, 0);
		}
        EntityDigIconFX.addBlockDestroyEffects(world(), Cuboid6.full.copy()
                .add(Vector3.fromTileEntity(tile())), new IIcon[] { breakIcon,
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

	public boolean isActive() {
		return hasMerged;
	}

	@Override
	public int getHollowSize(int arg0) {
		return 6;
	}

	@Override
	public void onFluidLevelChanged(int old) {
		// TODO Auto-generated method stub
		
	}
}
