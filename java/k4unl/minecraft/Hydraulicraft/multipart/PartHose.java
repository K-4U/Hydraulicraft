package k4unl.minecraft.Hydraulicraft.multipart;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseTransporter;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicTransporter;
import k4unl.minecraft.Hydraulicraft.client.renderers.RendererHydraulicHose;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;

import org.lwjgl.opengl.GL11;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.FaceMicroblock;
import codechicken.microblock.IHollowConnect;
import codechicken.microblock.MicroMaterialRegistry;
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
    
    private IBaseTransporter baseHandler;
    private Map<ForgeDirection, TileEntity> connectedSides;
    private final boolean[] connectedSideFlags = new boolean[6];
    private boolean needToCheckNeighbors;
    private boolean hasCheckedSinceStartup;
    
    
    private int tier = 0;

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
    		
    		boundingBoxes[i] = new Cuboid6(xMin1, yMin1, zMin1, xMax1, yMax1, zMax1);
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
		return Names.blockHydraulicHose[0].unlocalized;
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
		NBTTagCompound connectedCompound = new NBTTagCompound();
		NBTTagCompound handlerCompound = new NBTTagCompound();
		writeConnectedSidesToNBT(connectedCompound);
		getHandler().writeToNBT(handlerCompound);
		
		mainCompound.setCompoundTag("connectedSides", connectedCompound);
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
		NBTTagCompound connectedCompound = mainCompound.getCompoundTag("connectedSides");
		NBTTagCompound handlerCompound = mainCompound.getCompoundTag("handler");
		readConnectedSidesFromNBT(connectedCompound);
        
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
            GL11.glDisable(GL11.GL_LIGHTING);
            RendererHydraulicHose r = new RendererHydraulicHose();
            r.doRender(pos.x, pos.y, pos.z, 0, tier, connectedSides);
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
    	if(connectedSides == null){
    		checkConnectedSides();
    	}
    	if(connectedSideFlags == null){
    		return false;
    	}
    	return connectedSides.containsKey(side);
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
		getHandler().updateBlock();
    }
    
    @Override
	public boolean canConnectTo(ForgeDirection side) {
    	int d = side.getOpposite().ordinal();
    	return tile().canAddPart(new NormallyOccludedPart(boundingBoxes[d]));
	}
    
    public void onNeighborChanged(){
        checkConnectedSides();
        Functions.checkAndFillSideBlocks(world(), x(), y(), z());
        //getHandler().disperse();
    }
    
    public ItemStack getItem(){
        return new ItemStack(Multipart.itemPartHose, 1, tier);
    }
    
    @Override
    public void onPartChanged(TMultiPart part){
        checkConnectedSides();
        getHandler().disperse();
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
    public float getMaxPressure(boolean isOil){
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
    	}
    	if(world().getTotalWorldTime() % 10 == 0 && hasCheckedSinceStartup == false){
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
                    connectedSides.put(dir, world().getBlockTileEntity(x() + dir.offsetX, y() + dir.offsetY, z() + dir.offsetZ));
                }
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

	
}
