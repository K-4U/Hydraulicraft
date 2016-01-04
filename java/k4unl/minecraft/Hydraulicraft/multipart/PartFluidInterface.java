package k4unl.minecraft.Hydraulicraft.multipart;
/*
import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.ISidedHollowConnect;
import codechicken.multipart.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.client.renderers.transportation.RendererPartFluidInterface;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.k4lib.client.RenderHelper;
import k4unl.minecraft.k4lib.lib.Location;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.EnumFacing;
import net.minecraftforge.fluids.*;
import org.lwjgl.opengl.GL11;

import java.util.LinkedList;


public class PartFluidInterface extends TMultiPart implements IFluidHandler, TFacePart, TSlottedPart, JNormalOcclusion, ISidedHollowConnect {
    private static float          pixel        = 1.0F / 16F;
    private static int            expandBounds = -1;
    private        EnumFacing side         = EnumFacing.DOWN;

    public static Cuboid6[] boundingBoxes = new Cuboid6[6];

    private FluidTank tank = new FluidTank(1000);

    @SideOnly(Side.CLIENT)
    private static RendererPartFluidInterface renderer;

    @SideOnly(Side.CLIENT)
    private static IIcon breakIcon;

    static {
        //boundingBoxes[6] = new Cuboid6(center - w, center - w, center - w, center + w, center + w, center + w);

        float thickness = 2 * RenderHelper.pixel;
        float width = 8 * RenderHelper.pixel;
        float min = 0.5F - (width/2);
        float max = 0.5F + (width/2);
        float tMax = 1.0F - thickness;
        float tMin = 0.0F + thickness;
        Vector3fMax vector;
        int i = 0;
        for (EnumFacing dir : EnumFacing.VALID_DIRECTIONS) {

            if(dir.equals(EnumFacing.UP)){
                vector = new Vector3fMax(min, tMax, min, max, 1.0F, max);
            }else if(dir.equals(EnumFacing.DOWN)){
                vector = new Vector3fMax(min, 0.0F, min, max, tMin, max);
            }else if(dir.equals(EnumFacing.NORTH)){
                vector = new Vector3fMax(min, min, 0.0F, max, max, tMin);
            }else if(dir.equals(EnumFacing.SOUTH)){
                vector = new Vector3fMax(min, min, tMax, max, max, 1.0F);
            }else if(dir.equals(EnumFacing.WEST)){
                vector = new Vector3fMax(0.0F, min, min, tMin, max, max);
            }else if(dir.equals(EnumFacing.EAST)){
                vector = new Vector3fMax(tMax, min, min, 1.0F, max, max);
            }else{
                vector = new Vector3fMax(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }

            boundingBoxes[i] = new Cuboid6(vector.getXMin(), vector.getYMin(), vector.getZMin(), vector.getXMax(), vector.getYMax(), vector.getZMax());
            i++;
        }
    }

    private boolean isRedstonePowered;

    public void preparePlacement(EnumFacing side_) {

        side = side_;
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {

        if (!from.equals(side))
            return 0;
        return tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {

        if (!from.equals(side))
            return null;

        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {

        if (!from.equals(side))
            return null;

        return tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {

        return from.equals(side);
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {

        return from.equals(side);
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {

        return new FluidTankInfo[] { new FluidTankInfo(tank) };
    }

    @Override
    public String getType() {

        return "tile." + Names.partFluidInterface.unlocalized;
    }

    @Override
    public int getSlotMask() {

        return PartMap.face(side.ordinal()).mask;
    }

    @Override
    public int getHollowSize(int i) {

        return 10;
    }

    @Override
    public Iterable<Cuboid6> getOcclusionBoxes() {

        return getCollisionBoxes();
    }

    @Override
    public Iterable<Cuboid6> getCollisionBoxes() {

        LinkedList<Cuboid6> list = new LinkedList<Cuboid6>();
        list.add(boundingBoxes[Functions.getIntDirFromDirection(side)]);
        return list;
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
    public boolean occlusionTest(TMultiPart npart) {

        return NormalOcclusionTest.apply(this, npart);
    }

    @Override
    public void load(NBTTagCompound tagCompound) {

        super.load(tagCompound);
        //connectedSides = new HashMap<EnumFacing, TileEntity>();
        //getHandler().updateNetworkOnNextTick(oldPressure);
        //checkConnectedSides();
        //readConnectedSidesFromNBT(tagCompound);
        side = EnumFacing.getOrientation(tagCompound.getInteger("side"));
        tank.readFromNBT(tagCompound);
    }

    @Override
    public void save(NBTTagCompound tagCompound){
        super.save(tagCompound);
        //writeConnectedSidesToNBT(tagCompound);
        //tagCompound.setDouble("fluidStored", fluidStored);
        tagCompound.setInteger("side", side.ordinal());
        tank.writeToNBT(tagCompound);
    }

    @Override
    public void writeDesc(MCDataOutput packet){

        NBTTagCompound mainCompound = new NBTTagCompound();
        if(world() != null && !world().isRemote){
            mainCompound.setInteger("side", side.ordinal());
            tank.writeToNBT(mainCompound);
        }


        packet.writeNBTTagCompound(mainCompound);
    }

    @Override
    public void readDesc(MCDataInput packet){

        NBTTagCompound mainCompound = packet.readNBTTagCompound();

        side = EnumFacing.getOrientation(mainCompound.getInteger("side"));
        tank.readFromNBT(mainCompound);
    }

    public void onChunkLoad() {
        super.onChunkLoad();

    }

    public void onAdded(){
        super.onAdded();
        if(!world().isRemote) {
            Multipart.updateMultiPart(this);
        }
    }

    @Override
    public void update(){
        super.update();


        if(!world().isRemote && tank != null && tank.getFluid() != null) {
            //Find a fluid pipe
            if (Multipart.hasPartFluidPipe(tile())) {
                PartFluidPipe pipe = Multipart.getFluidPipe(tile());
                if (pipe.getFluidStored() == null) {
                    pipe.setFluidStored(tank.getFluid().getFluid());
                }
                if (pipe.getFluidAmountStored() < Constants.MAX_FLUID_TRANSFER_T * 10) {
                    double fluidAdded = (Constants.MAX_FLUID_TRANSFER_T * 10) - pipe.getFluidAmountStored();
                    if (tank.drain((int) fluidAdded, false).amount >= (int) fluidAdded) {
                        pipe.addFluid(fluidAdded);
                        tank.drain((int) fluidAdded, true);
                    }

                }
            }
        }
        if(!world().isRemote){
            if (getIsRedstonePowered()){
                //Find a tank on the side we're on.
                Location tankLocation = new Location(x(), y(), z(), side);
                TileEntity te = tankLocation.getTE(world());
                if(te instanceof IFluidHandler){
                    if(tank.getFluid() == null){
                        drainFluid((IFluidHandler)te);
                    } else if(tank.getFluid() != null){
                        if(tank.getFluidAmount() == 0){
                            drainFluid((IFluidHandler) te);
                        }else{
                            FluidTankInfo tankInfo = ((IFluidHandler)te).getTankInfo(side.getOpposite())[0];
                            if(tankInfo != null && tankInfo.fluid != null) {
                                if (tank.getFluid().getFluid() == tankInfo.fluid.getFluid()) {
                                    drainFluid((IFluidHandler) te);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public ItemStack pickItem(MovingObjectPosition hit){
        return getItem();
    }

    public ItemStack getItem(){
        return new ItemStack(Multipart.itemPartFluidInterface, 1);
    }

    private void drainFluid(IFluidHandler fluidHandler){
        FluidStack drained = fluidHandler.drain(side.getOpposite(), Constants.MAX_FLUID_TRANSFER_T, false);
        if(drained != null && drained.amount > 0) {
            int filled = tank.fill(drained, false);
            if(filled > 0){
                //Do the actual filling.
                drained = fluidHandler.drain(side.getOpposite(), filled, true);
                tank.fill(drained, true);
            }
        }
    }

    public void checkRedstonePower() {
        boolean isIndirectlyPowered = world().isBlockIndirectlyGettingPowered(x(), y(), z());
        if(isIndirectlyPowered && !getIsRedstonePowered()){
            setRedstonePowered(true);
        }else if(getIsRedstonePowered() && !isIndirectlyPowered){
            setRedstonePowered(false);
        }
    }

    public boolean getIsRedstonePowered() {
        return isRedstonePowered;
    }

    public void setRedstonePowered(boolean isRedstonePowered) {
        this.isRedstonePowered = isRedstonePowered;
    }


    public void onNeighborChanged(){
        if(!world().isRemote){
            checkRedstonePower();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vector3 pos, float frame, int pass){
        if (pass == 0){
            if(renderer == null){
                renderer = new RendererPartFluidInterface();
            }
            GL11.glDisable(GL11.GL_LIGHTING);
            renderer.doRender(pos.x, pos.y, pos.z, frame, side);
            GL11.glEnable(GL11.GL_LIGHTING);
        }
    }

    @Override
    public boolean solid(int i) {

        return true;
    }

    @Override
    public int redstoneConductionMap() {

        return 0;
    }
}
*/
