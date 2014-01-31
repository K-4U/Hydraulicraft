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

import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.IHollowConnect;
import codechicken.multipart.JNormalOcclusion;
import codechicken.multipart.NormalOcclusionTest;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TSlottedPart;
import codechicken.multipart.TileMultipart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PartHose extends TMultiPart implements TSlottedPart, JNormalOcclusion, IHollowConnect, IHydraulicTransporter {
    public static Cuboid6[] boundingBoxes = new Cuboid6[7];
    private static int expandBounds = -1;
    
    private IBaseTransporter baseHandler;
    private Map<ForgeDirection, TileEntity> connectedSides;
    private final boolean[] connectedSideFlags = new boolean[6];
    private boolean needToCheckNeighbors;
    private boolean hasCheckedSinceStartup;
    
    
    private int tier = 0;

    static
    {
        double w = 2 / 8D;
        boundingBoxes[6] = new Cuboid6(0.5 - w, 0.5 - w, 0.5 - w, 0.5 + w, 0.5 + w, 0.5 + w);
        for (int s = 0; s < 6; s++)
            boundingBoxes[s] = new Cuboid6(0.5 - w, 0, 0.5 - w, 0.5 + w, 0.5 - w, 0.5 + w).apply(Rotation.sideRotations[s].at(Vector3.center));
    }
	
	@Override
	public String getType() {
		return Names.blockHydraulicHose[0].unlocalized;
	}

	public void preparePlacement(int itemDamage) {
		tier = itemDamage;
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
    public Iterable<Cuboid6> getOcclusionBoxes()
    {
        if (expandBounds >= 0)
            return Arrays.asList(boundingBoxes[expandBounds]);

        return Arrays.asList(boundingBoxes[6]);
    }

    @Override
    public Iterable<Cuboid6> getCollisionBoxes()
    {
        LinkedList<Cuboid6> list = new LinkedList<Cuboid6>();
        list.add(boundingBoxes[6]);
        //for (int s = 0; s < 6; s++)
        //    if (maskConnects(s))
        //        list.add(boundingBoxes[s]);
        return list;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vector3 pos, float frame, int pass)
    {
        if (pass == 0){
            GL11.glDisable(GL11.GL_LIGHTING);
            RendererHydraulicHose r = new RendererHydraulicHose();
            r.render(pos.x, pos.y, pos.z, 0, tier, connectedSides);
            GL11.glEnable(GL11.GL_LIGHTING);
        }
    }

    private boolean shouldConnectTo(TileEntity entity, ForgeDirection dir){
    	int opposite = Functions.getIntDirFromDirection(dir.getOpposite());
    	if(entity instanceof TileMultipart){
    		if(((TileMultipart)entity).isSolid(opposite)) return false;
    		List<TMultiPart> t = ((TileMultipart)entity).jPartList();
    		for (TMultiPart p: t) {
				if(p instanceof IHydraulicMachine){
					return true;
				}
			}
    		return false;
    	}else{
    		return entity instanceof IHydraulicMachine;
    	}
    }

    public void checkConnectedSides(){
        connectedSides = new HashMap<ForgeDirection, TileEntity>();

        //Should also check here if we have a cover or not.
        List<TMultiPart> t = tile().jPartList();
		for (TMultiPart p: t) {
			Log.info(p.getType());
		}
        
		for(int i = 0; i < 6; i++){
        	ForgeDirection dir = ForgeDirection.getOrientation(i);
        	if(!tile().isSolid(i)){
        	
	            TileEntity te = world().getBlockTileEntity(x() + dir.offsetX, y() + dir.offsetY, z() + dir.offsetZ);
	            if(shouldConnectTo(te, dir)) {
	                connectedSides.put(dir, te);
	            }
        	}
        }

        //getHandler().updateBlock();
    }

    public ItemStack getItem(){
        return new ItemStack(Multipart.itemPartHose, 1, tier);
    }
    
    @Override
    public void onPartChanged(TMultiPart part){
        if (!world().isRemote){
        	checkConnectedSides();
        }
    }
    
    @Override
    public ItemStack pickItem(MovingObjectPosition hit){
        return getItem();
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
		return world().getBlockMetadata(x(), y(), z());
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
    public void update(){
    	if(getHandler() != null){
    		//This should never happen that this is null! :|
    		getHandler().updateEntity();
    	}
    	if(world().getTotalWorldTime() % 60 == 0 && hasCheckedSinceStartup == false){
    		checkConnectedSides();
    		hasCheckedSinceStartup = true;
    		//Hack hack hack
    		//Temporary bug fix that we will forget about
    		tier = world().getBlockMetadata(x(), y(), z());
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
}
