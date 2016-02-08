package k4unl.minecraft.Hydraulicraft.tileEntities.generator;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicTieredBlockBase;
import k4unl.minecraft.Hydraulicraft.lib.Properties;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;

public class TileHydraulicLavaPump extends TileHydraulicBase implements IHydraulicGenerator, IFluidHandler {
    private FluidTank    tank         = null;
    private PressureTier tier         = PressureTier.INVALID;
    private boolean      isRunning    = false;
    private int          lavaUsage    = 0;
    private int          runningTicks = 0;

    public TileHydraulicLavaPump() {
        super(2);
        super.init(this);
    }

    public TileHydraulicLavaPump(int tier) {
        super(2 * (tier + 1));
        super.init(this);
    }

    @Override
    public void validate() {
        super.validate();
    }

    @Override
    public void workFunction(EnumFacing from) {
        if (!from.equals(EnumFacing.UP)) return;

        if (!getRedstonePowered()) {
            isRunning = false;
            getHandler().updateBlock();
            return;
        }
        //This function gets called every tick.
        boolean needsUpdate = false;
        if (!worldObj.isRemote) {
            needsUpdate = true;
            if (Float.compare(getGenerating(EnumFacing.UP), 0.0F) > 0) {
                getHandler().setPressure(getPressure(getFacing()) + getGenerating(EnumFacing.UP), getFacing());
                runningTicks++;
                if (runningTicks == 10) {
                    tank.drain((int) (Constants.MAX_LAVA_USAGE * 10), true);
                    runningTicks = 0;
                }
                isRunning = true;
            } else {
                isRunning = false;
            }
        }

        if (needsUpdate) {
            worldObj.markBlockForUpdate(getPos());
        }
    }

    @Override
    public int getMaxGenerating(EnumFacing from) {
        float tiered = (getTier().toInt() + 1) / 3.0F;
        return (int) (Constants.MAX_LAVA_USAGE * (HCConfig.INSTANCE.getInt("conversionRatioLavaHydraulic") * tiered) * (getHandler().isOilStored() ? 1.0F : Constants.WATER_CONVERSION_RATIO));
    }

    public float getBurningPercentage() {
        return 0;
    }

    public int getLavaUsage() {
        return lavaUsage;
    }

    @Override
    public float getGenerating(EnumFacing from) {
        if (!getRedstonePowered() || getFluidInNetwork(from) == 0 || tank == null || tank.getFluid() == null) {
            lavaUsage = 0;
            return 0f;
        }

        if (tank.getFluidAmount() >= Constants.MAX_LAVA_USAGE) {
            lavaUsage = (int) Math.max(Constants.MAX_LAVA_USAGE, 1F);
            FluidStack drained = tank.drain(lavaUsage, false);
            if (drained == null) return 0F;
            lavaUsage = drained.amount;


            float tiered = (getTier().toInt() + 1) / 3.0F;

            float gen = lavaUsage * (HCConfig.INSTANCE.getInt("conversionRatioLavaHydraulic") * tiered) * (getHandler().isOilStored() ? 1.0F : Constants.WATER_CONVERSION_RATIO);
            gen = gen * ((float) getFluidInNetwork(from) / (float) getFluidCapacity(from));

            if (Float.compare(gen + getPressure(from), getMaxPressure(getHandler().isOilStored(), from)) > 0) {
                //This means the pressure we are generating is too much!
                gen = getMaxPressure(getHandler().isOilStored(), from) - getPressure(from);
            }
            if (Float.compare(gen, getMaxGenerating(from)) > 0) {
                gen = getMaxGenerating(from);
            }

            lavaUsage = (int) (gen * ((float) getFluidInNetwork(from) / (float) getFluidCapacity(from)) / HCConfig.INSTANCE.getInt("conversionRatioLavaHydraulic") * (getHandler().isOilStored() ? 1.0F : Constants.WATER_CONVERSION_RATIO));
            return gen;
        } else {
            return 0;
        }
    }

    public void setTier(PressureTier tier) {
        if (tank == null) {
            tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * (16 * (tier.toInt() + 1)));
        }
        super.setMaxStorage(2 * (tier.toInt() + 1));
    }

    public PressureTier getTier() {
        if (tier == PressureTier.INVALID && worldObj != null) {
            tier = (PressureTier) worldObj.getBlockState(getPos()).getValue(HydraulicTieredBlockBase.TIER);
            if (tank == null) {
                tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * (16 * (tier.toInt() + 1)));
            }
            super.setMaxStorage(2 * (tier.toInt() + 1));
        }

        return tier;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        setTier(PressureTier.fromOrdinal(tagCompound.getInteger("tier")));
        NBTTagCompound tankCompound = tagCompound.getCompoundTag("tank");
        if (tankCompound != null) {
            tank = tank.readFromNBT(tankCompound);
        }

        lavaUsage = tagCompound.getInteger("lavaUsage");
        isRunning = tagCompound.getBoolean("isRunning");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("tier", getTier().toInt());
        if (tank != null) {
            NBTTagCompound inventoryCompound = new NBTTagCompound();
            tank.writeToNBT(inventoryCompound);
            tagCompound.setTag("tank", inventoryCompound);
        }

        tagCompound.setInteger("lavaUsage", lavaUsage);
        tagCompound.setBoolean("isRunning", isRunning);
    }

    @Override
    public void onFluidLevelChanged(int old) {
    }

    @Override
    public boolean canConnectTo(EnumFacing side) {
        return true;
    }

    @Override
    public boolean canWork(EnumFacing dir) {
        return dir.equals(EnumFacing.UP);
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        if (worldObj.isRemote) return 0;
        if (resource == null) //What!?
            return 0;
        if (resource.getFluid() == null)
            return 0;
        if (resource.getFluid() != FluidRegistry.LAVA) {
            return 0;
        }

        if (tank != null && tank.getFluid() != null && tank.getFluidAmount() > 0) {
            if (resource.getFluid() != tank.getFluid().getFluid()) {
                return 0;
            }
        } else if (tank == null) {
            setTier(getTier());
        }

        return tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource,
                            boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        FluidStack drained = tank.drain(maxDrain, doDrain);
        if (tank.getFluidAmount() == 0 && doDrain) {
            tank.setFluid(null);
        }
        return drained;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        if (fluid == null) return true;
        return fluid == FluidRegistry.LAVA;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return fluid == FluidRegistry.LAVA;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        if (tank != null) {
            return new FluidTankInfo[]{new FluidTankInfo(tank)};
        } else {
            return null;
        }
    }

    public EnumFacing getFacing() {
        return (EnumFacing)getWorldObj().getBlockState(getPos()).getValue(Properties.ROTATION);
    }

}
