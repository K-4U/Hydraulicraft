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
import codechicken.microblock.IHollowConnect;
import codechicken.microblock.MicroMaterialRegistry;
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
	public void load(NBTTagCompound tagCompound){
		super.load(tagCompound);
		getHandler().readFromNBT(tagCompound);
		tier = tagCompound.getInteger("tier");
	}
	
	@Override
	public void save(NBTTagCompound tagCompound){
		super.save(tagCompound);
		getHandler().writeToNBT(tagCompound);
		tagCompound.setInteger("tier", tier);
	}
	
	@Override
    public void writeDesc(MCDataOutput packet){
		packet.writeInt(getTier());
    }

    @Override
    public void readDesc(MCDataInput packet){
        tier = packet.readInt();
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
    public void renderDynamic(Vector3 pos, float frame, int pass){
        if (pass == 0){
            GL11.glDisable(GL11.GL_LIGHTING);
            RendererHydraulicHose r = new RendererHydraulicHose();
            r.render(pos.x, pos.y, pos.z, 0, tier, connectedSides);
            GL11.glEnable(GL11.GL_LIGHTING);
        }
    }

    private boolean shouldConnectTo(TileEntity entity, ForgeDirection dir, Object caller){
    	int opposite = Functions.getIntDirFromDirection(dir.getOpposite());
    	if(entity instanceof TileMultipart){
    		if(((TileMultipart)entity).isSolid(opposite)) return false;
    		List<TMultiPart> t = ((TileMultipart)entity).jPartList();
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
    		return entity instanceof IHydraulicMachine;
    	}
    }

    public void checkConnectedSides(){
    	checkConnectedSides(this);
    }
    
    public void checkConnectedSides(Object caller){
        connectedSides = new HashMap<ForgeDirection, TileEntity>();

		for(int i = 0; i < 6; i++){
        	ForgeDirection dir = ForgeDirection.getOrientation(i);
        	
            TileEntity te = world().getBlockTileEntity(x() + dir.offsetX, y() + dir.offsetY, z() + dir.offsetZ);
            if(shouldConnectTo(te, dir, caller)) {
            	if(!tile().isSolid(i)){
            		connectedSides.put(dir, te);
            	}
            }
        }

        getHandler().updateBlock();
    }

    
    public void onNeighborChanged(){
        checkConnectedSides();
    }
    
    public ItemStack getItem(){
        return new ItemStack(Multipart.itemPartHose, 1, tier);
    }
    
    @Override
    public void onPartChanged(TMultiPart part){
        checkConnectedSides();
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
	public AxisAlignedBB getRenderBoundingBox() {
		return getHandler().getRenderBoundingBox();
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
    	if(world().getTotalWorldTime() % 60 == 0 && hasCheckedSinceStartup == false){
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
}
