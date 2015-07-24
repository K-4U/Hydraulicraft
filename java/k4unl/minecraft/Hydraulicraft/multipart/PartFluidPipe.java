package k4unl.minecraft.Hydraulicraft.multipart;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.render.EntityDigIconFX;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.ISidedHollowConnect;
import codechicken.multipart.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.client.renderers.transportation.RendererPartFluidPipe;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import org.lwjgl.opengl.GL11;

import java.util.*;


public class PartFluidPipe extends TMultiPart implements TSlottedPart, JNormalOcclusion, ISidedHollowConnect {

    public static Cuboid6[] boundingBoxes = new Cuboid6[7];
    private static float pixel = 1.0F / 16F;
    private static int expandBounds = -1;

    private Map<ForgeDirection, TileEntity> connectedSides;
    private final boolean[] connectedSideFlags = new boolean[6];
    private boolean needToCheckNeighbors;
    private boolean connectedSidesHaveChanged = true;
    private boolean hasCheckedSinceStartup;
    private Fluid fluidStored;

    @SideOnly(Side.CLIENT)
    private static RendererPartFluidPipe renderer;

    @SideOnly(Side.CLIENT)
    private static IIcon breakIcon;

    static {
        float center = 0.5F;
        double w = pixel*3;
        boundingBoxes[6] = new Cuboid6(center - w, center - w, center - w, center + w, center + w, center + w);

        int i = 0;
        for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
            double xMin1 = (dir.offsetX < 0 ? 0.0 : (dir.offsetX == 0 ? center - w : center + w));
            double xMax1 = (dir.offsetX > 0 ? 1.0 : (dir.offsetX == 0 ? center + w : center - w));

            double yMin1 = (dir.offsetY < 0 ? 0.0 : (dir.offsetY == 0 ? center - w : center + w));
            double yMax1 = (dir.offsetY > 0 ? 1.0 : (dir.offsetY == 0 ? center + w : center - w));

            double zMin1 = (dir.offsetZ < 0 ? 0.0 : (dir.offsetZ == 0 ? center - w : center + w));
            double zMax1 = (dir.offsetZ > 0 ? 1.0 : (dir.offsetZ == 0 ? center + w : center - w));

            boundingBoxes[i] = new Cuboid6(xMin1, yMin1, zMin1, xMax1, yMax1, zMax1);
            i++;
        }
    }

    private double fluidAmountStored;

    @Override
    public String getType() {
        return "tile." + Names.partFluidPipe.unlocalized;
    }
    @Override
    public void load(NBTTagCompound tagCompound){
        super.load(tagCompound);
        connectedSides = new HashMap<ForgeDirection, TileEntity>();
        fluidAmountStored = tagCompound.getDouble("fluidAmountStored");
        fluidStored = FluidRegistry.getFluid(tagCompound.getString("fluidName"));
        //getHandler().updateNetworkOnNextTick(oldPressure);
        //checkConnectedSides();
        readConnectedSidesFromNBT(tagCompound);
    }

    @Override
    public void save(NBTTagCompound tagCompound){
        super.save(tagCompound);
        writeConnectedSidesToNBT(tagCompound);
        tagCompound.setDouble("fluidAmountStored", fluidAmountStored);
        if(fluidStored != null) {
            tagCompound.setString("fluidName", fluidStored.getName());
        }
    }

    @Override
    public void writeDesc(MCDataOutput packet){

        NBTTagCompound mainCompound = new NBTTagCompound();
        if(connectedSidesHaveChanged && world() != null && !world().isRemote){
            connectedSidesHaveChanged = false;
            mainCompound.setBoolean("connectedSidesHaveChanged", true);
            mainCompound.setDouble("fluidAmountStored", fluidAmountStored);
            if(fluidStored != null) {
                mainCompound.setString("fluidName", fluidStored.getName());
            }
        }


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
        tagCompound.setTag("connectedSides", ourCompound);
    }

    @Override
    public void readDesc(MCDataInput packet){

        NBTTagCompound mainCompound = packet.readNBTTagCompound();
        if(mainCompound.getBoolean("connectedSidesHaveChanged")){
            hasCheckedSinceStartup = false;
        }
        fluidAmountStored = mainCompound.getDouble("fluidAmountStored");
        fluidStored = FluidRegistry.getFluid(mainCompound.getString("fluidName"));

    }


    @Override
    public int getSlotMask() {

        return PartMap.CENTER.mask;
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
            return Collections.singletonList(boundingBoxes[expandBounds]);

        return Collections.singletonList(boundingBoxes[6]);
    }

    @Override
    public Iterable<Cuboid6> getCollisionBoxes(){
        LinkedList<Cuboid6> list = new LinkedList<Cuboid6>();
        list.add(boundingBoxes[6]);
        if(connectedSides == null) return list;
        for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
            if(connectedSides.containsKey(dir)){
                list.add(boundingBoxes[Functions.getIntDirFromDirection(dir)]);
            }
        }
        return list;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vector3 pos, float frame, int pass){
        if (pass == 0){
            if(renderer == null){
                renderer = new RendererPartFluidPipe();
            }
            GL11.glDisable(GL11.GL_LIGHTING);
            renderer.doRender(pos.x, pos.y, pos.z, frame, connectedSides);
            GL11.glEnable(GL11.GL_LIGHTING);
        }
    }

    private boolean shouldConnectTo(TileEntity entity, ForgeDirection dir, Object caller){
        int opposite = Functions.getIntDirFromDirection(dir.getOpposite());
        if(entity instanceof TileMultipart){
            List<TMultiPart> t = ((TileMultipart)entity).jPartList();

            if (Multipart.hasPartFluidPipe((TileMultipart) entity)){
                if(!((TileMultipart)entity).canAddPart(new NormallyOccludedPart(boundingBoxes[opposite]))) return false;
            }

            for (TMultiPart p: t) {
                if(p instanceof PartFluidPipe){
                    return true;
                }
            }
            return false;
        }else {
            return entity instanceof IFluidHandler && ((IFluidHandler) entity).canFill(dir.getOpposite(), getFluidStored());
        }
    }

    public boolean isConnectedTo(ForgeDirection side){
        int d = side.ordinal();

        if(world() != null && tile() != null){
            TileEntity te = world().getTileEntity(x() + side.offsetX, y() + side.offsetY, z() + side.offsetZ);
            return tile().canAddPart(new NormallyOccludedPart(boundingBoxes[d])) && shouldConnectTo(te, side, this);
        }else{
            return false;
        }
    }

    public void checkConnectedSides(){
        checkConnectedSides(this);
    }

    public void checkConnectedSides(Object caller){
        HashMap<ForgeDirection, TileEntity> oldSides;
        if(connectedSides != null) {
            oldSides = new HashMap<ForgeDirection, TileEntity>(connectedSides);
        }else{
            oldSides = new HashMap<ForgeDirection, TileEntity>();
        }
        connectedSides = new HashMap<ForgeDirection, TileEntity>();

        for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
            int d = Functions.getIntDirFromDirection(dir);

            TileEntity te = world().getTileEntity(x() + dir.offsetX, y() + dir.offsetY, z() + dir.offsetZ);
            if(shouldConnectTo(te, dir, caller)) {
                if(tile().canAddPart(new NormallyOccludedPart(boundingBoxes[d]))){
                    if(!oldSides.containsKey(dir)){
                        connectedSidesHaveChanged = true;
                    }
                    connectedSides.put(dir, te);
                }
            }
        }
        if(connectedSidesHaveChanged){
            if(!world().isRemote) {
                MCDataOutput writeStream = tile().getWriteStream(this);
                writeDesc(writeStream);
            }
        }
    }

    public boolean canConnectTo(ForgeDirection side) {
        int d = side.ordinal();
        return tile().canAddPart(new NormallyOccludedPart(boundingBoxes[d]));
    }

    public void onNeighborChanged(){

        //Actually, wait a few ticks
        needToCheckNeighbors = true;
        if(!world().isRemote){
            //getHandler().updateFluidOnNextTick();
            //getHandler().updateNetworkOnNextTick(oldPressure);
        }
    }

    public ItemStack getItem(){
        return new ItemStack(Multipart.itemPartFluidPipe, 1);
    }

    @Override
    public void onPartChanged(TMultiPart part){
        checkConnectedSides();
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
    public void update(){
        if(world() != null){
            if(world().getTotalWorldTime() % 2 == 0 && !hasCheckedSinceStartup){
                checkConnectedSides();
                hasCheckedSinceStartup = true;
                //Hack hack hack
                //Temporary bug fix that we will forget about
            }
            if(!world().isRemote){
                redistributeFluid();
            }
        }

        if(needToCheckNeighbors) {
            needToCheckNeighbors = false;
            checkConnectedSides();
            //connectedSides = new HashMap<ForgeDirection, TileEntity>();
            for(int i = 0; i < 6; i++) {
                if(connectedSideFlags[i]) {
                    ForgeDirection dir = ForgeDirection.getOrientation(i);
                    connectedSides.put(dir, world().getTileEntity(x() + dir.offsetX, y() + dir.offsetY, z() + dir.offsetZ));
                }
            }
            if(!world().isRemote){
                connectedSidesHaveChanged = true;
            }
        }

    }

    public Map<ForgeDirection, TileEntity> getConnectedSides() {
        if(connectedSides == null){
            checkConnectedSides();
        }
        return connectedSides;
    }


    @Override
    public void onRemoved(){
        super.onRemoved();
        if(!world().isRemote){

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
                        .add(Vector3.fromTileEntity(tile())), new IIcon[]{breakIcon,
                        breakIcon, breakIcon, breakIcon, breakIcon, breakIcon},
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

    @Override
    public int getHollowSize(int arg0) {
        return 4;
    }

    private void redistributeFluid() {
        if(connectedSides == null) return;
        double avgFluid = getFluidAmountStored();
        int neighbourCount = 1;
        for (ForgeDirection dir : ForgeDirection.values()) {
            TileEntity neighbor = connectedSides.get(dir);
            if (neighbor != null && neighbor instanceof TileMultipart) {
                if (Multipart.hasPartFluidPipe((TileMultipart) neighbor)) {
                    avgFluid += Multipart.getFluidPipe((TileMultipart) neighbor).getFluidAmountStored();
                    neighbourCount++;
                }
            }
        }
        avgFluid = avgFluid / neighbourCount;

        addFluid(avgFluid - getFluidAmountStored());
        for (ForgeDirection dir : ForgeDirection.values()) {
            TileEntity neighbor = connectedSides.get(dir);

            if (neighbor != null && neighbor instanceof TileMultipart) {

                if (Multipart.hasPartFluidPipe((TileMultipart) neighbor)) {

                    if(Multipart.getFluidPipe((TileMultipart)neighbor).getFluidStored() == null) {
                        Multipart.getFluidPipe((TileMultipart) neighbor).setFluidStored(getFluidStored());
                    }else if(getFluidStored() == null){
                        setFluidStored(Multipart.getFluidPipe((TileMultipart) neighbor).getFluidStored());
                    }

                    Multipart.getFluidPipe((TileMultipart) neighbor).addFluid(avgFluid - Multipart.getFluidPipe((TileMultipart)neighbor).getFluidAmountStored());
                }
            }else if(neighbor != null && neighbor instanceof IFluidHandler){
                if(getFluidStored() != null) {
                    int toFill = ((IFluidHandler) neighbor).fill(dir.getOpposite(), new FluidStack(getFluidStored(), (int) getFluidAmountStored()), false);
                    ((IFluidHandler) neighbor).fill(dir.getOpposite(), new FluidStack(getFluidStored(), toFill), true);
                    addFluid(-toFill);
                }
            }
        }
    }

    public double getFluidAmountStored() {
        return fluidAmountStored;
    }

    public void setFluidAmountStored(double fluidStored) {
        this.fluidAmountStored = fluidStored;
        if(fluidAmountStored == 0){
            setFluidStored(null);
        }
    }

    public void addFluid(double fluidStored) {
        this.fluidAmountStored += fluidStored;
        if(fluidAmountStored == 0){
            setFluidStored(null);
        }
    }

    public Fluid getFluidStored() {
        return fluidStored;
    }

    public void setFluidStored(Fluid fluidStored) {
        this.fluidStored = fluidStored;
    }
}
