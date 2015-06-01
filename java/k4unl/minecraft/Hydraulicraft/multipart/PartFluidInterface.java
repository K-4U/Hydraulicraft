package k4unl.minecraft.Hydraulicraft.multipart;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Cuboid6;
import codechicken.microblock.ISidedHollowConnect;
import codechicken.multipart.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.client.renderers.transportation.RendererPartFluidPipe;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.LinkedList;


public class PartFluidInterface extends TMultiPart implements IFluidHandler, TSlottedPart, JNormalOcclusion, ISidedHollowConnect {
    private static float pixel = 1.0F / 16F;
    private static int expandBounds = -1;
    private ForgeDirection side = ForgeDirection.DOWN;

    public static Cuboid6[] boundingBoxes = new Cuboid6[6];

    private FluidTank tank = new FluidTank(1000);

    @SideOnly(Side.CLIENT)
    private static RendererPartFluidPipe renderer;

    @SideOnly(Side.CLIENT)
    private static IIcon breakIcon;

    static {
        float center = 0.5F;
        double w = pixel*4;
        //boundingBoxes[6] = new Cuboid6(center - w, center - w, center - w, center + w, center + w, center + w);

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

    public void preparePlacement(ForgeDirection side_) {
        side = side_;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if(!from.equals(side))
            return 0;
        return tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if(!from.equals(side))
            return null;

        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        if(!from.equals(side))
            return null;

        return tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return from.equals(side);
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return from.equals(side);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] {new FluidTankInfo(tank)};
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
    public Iterable<Cuboid6> getOcclusionBoxes(){
        return getCollisionBoxes();
    }

    @Override
    public Iterable<Cuboid6> getCollisionBoxes(){
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
    public boolean occlusionTest(TMultiPart npart){
        return NormalOcclusionTest.apply(this, npart);
    }

    @Override
    public void load(NBTTagCompound tagCompound){
        super.load(tagCompound);
        //connectedSides = new HashMap<ForgeDirection, TileEntity>();
        //getHandler().updateNetworkOnNextTick(oldPressure);
        //checkConnectedSides();
        //readConnectedSidesFromNBT(tagCompound);
        side = ForgeDirection.getOrientation(tagCompound.getInteger("side"));
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

        side = ForgeDirection.getOrientation(mainCompound.getInteger("side"));
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
                if(pipe.getFluidStored() == null){
                    pipe.setFluidStored(tank.getFluid().getFluid());
                }
                if (pipe.getFluidAmountStored() < 100) {
                    double fluidAdded = 100 - pipe.getFluidAmountStored();
                    if (tank.drain((int) fluidAdded, false).amount >= (int)fluidAdded) {
                        pipe.addFluid(fluidAdded);
                        tank.drain((int) fluidAdded, true);
                    }

                }
            }
        }
    }

}
