package k4unl.minecraft.Hydraulicraft.tileEntities.rubberHarvesting;

import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.Properties;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.network.NetworkHandler;
import k4unl.minecraft.Hydraulicraft.network.packets.PacketHarvestingRubber;
import k4unl.minecraft.Hydraulicraft.tileEntities.worldgen.TileRubberWood;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * @author Koen Beckers (K-4U)
 */
public class TileRubberTap extends TileEntity implements ITickable {


    private boolean        canWork       = false;
    private TileRubberWood wood          = null;
    private IFluidHandler  tank          = null;
    private int            maxFluidDrain = HCConfig.INSTANCE.getInt("maxRubberInTree") / 20;
    //Only used client side
    private boolean        tapping       = false;

    //Used client and server side:
    private int tankDepth = 0;


    private EnumFacing getFacing() {

        return getWorld().getBlockState(getPos()).getValue(Properties.ROTATION);
    }

    private boolean hasRubberWood() {

        if (getWorld().getTileEntity(getPos().offset(getFacing())) instanceof TileRubberWood) {
            if (getRubberWood().getFacing() == getFacing().getOpposite()) {
                return true;
            }
        }
        return false;
    }

    private TileRubberWood getRubberWood() {

        return (TileRubberWood) getWorld().getTileEntity(getPos().offset(getFacing()));
    }

    private boolean hasTank() {
        //A tank can be about 8 blocks lower.
        for (int i = 1; i <= 8; i++) {
            if (getWorld().getTileEntity(getPos().down(i)) instanceof IFluidHandler) {
                tank = (IFluidHandler) getWorld().getTileEntity(getPos().down(i));
                if (tank.canFill(EnumFacing.UP, Fluids.fluidRubber)) {
                    setTankDepth(i);
                    break;
                } else {
                    tank = null;
                }
            }
        }
        return tank != null;
    }

    public void setCanWork() {

        canWork = hasTank() && hasRubberWood();
        if (canWork) {
            wood = getRubberWood();
        }
    }

    @Override
    public void update() {
        //Check every 20 ticks, maybe a new tank got placed(or removed!)
        if (!getWorld().isRemote) {
            if (getWorld().getTotalWorldTime() % 20 == 0) {
                setCanWork();
            }
            if (canWork && wood != null && tank != null) {
                int drained = wood.drain(maxFluidDrain, true);
                int filled = tank.fill(EnumFacing.UP, new FluidStack(Fluids.fluidRubber, drained), false);

                if (drained > 0 && filled > 0) {
                    wood.drain(filled, false);
                    tank.fill(EnumFacing.UP, new FluidStack(Fluids.fluidRubber, filled), true);
                    NetworkHandler.INSTANCE.sendToAllAround(new PacketHarvestingRubber(getPos(), true, tankDepth), getWorld());
                } else {
                    NetworkHandler.INSTANCE.sendToAllAround(new PacketHarvestingRubber(getPos(), false, tankDepth), getWorld());
                }
            } else {
                NetworkHandler.INSTANCE.sendToAllAround(new PacketHarvestingRubber(getPos(), false, tankDepth), getWorld());
            }
        }
    }

    public boolean isTapping() {

        return tapping;
    }

    public void setTapping(boolean tapping) {

        this.tapping = tapping;
    }

    public int getTankDepth() {

        return tankDepth;
    }

    public void setTankDepth(int tankDepth) {

        this.tankDepth = tankDepth;
    }
}
