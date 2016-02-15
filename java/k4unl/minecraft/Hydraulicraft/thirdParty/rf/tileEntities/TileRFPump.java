package k4unl.minecraft.Hydraulicraft.thirdParty.rf.tileEntities;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.Properties;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.tileEntities.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.Hydraulicraft.tileEntities.interfaces.ICustomNetwork;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileRFPump extends TileHydraulicBase implements IHydraulicGenerator, IEnergyReceiver, ICustomNetwork {

    private int currentBurnTime;
    private int maxBurnTime;
    private boolean isRunning = false;
    private EnergyStorage energyStorage;
    private int        RFUsage = 0;

    private int fluidInNetwork;
    private int networkCapacity;


    private EnergyStorage getEnergyStorage() {

        if (this.energyStorage == null)
            this.energyStorage = new EnergyStorage(40000);
        return this.energyStorage;
    }

    public TileRFPump() {

        super(1);
        super.init(this);
    }

    public TileRFPump(PressureTier _tier) {

        super(2 * (_tier.toInt() + 1));
        super.init(this);
    }


    @Override
    public void workFunction(EnumFacing from) {

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
                setPressure(getPressure(getFacing()) + getGenerating(EnumFacing.UP), getFacing());
                getEnergyStorage().extractEnergy(RFUsage, false);
                isRunning = true;
            } else {

                if (getRedstonePowered()) {
                    getEnergyStorage().extractEnergy(RFUsage, false);
                }

                isRunning = false;
            }
        }

        if (needsUpdate) {
            worldObj.markBlockForUpdate(getPos());
        }
    }

    @Override
    public int getMaxGenerating(EnumFacing from) {

        if (!getHandler().isOilStored()) {
            return HCConfig.INSTANCE.getInt("maxMBarGenWaterT" + (getTier().toInt() + 1));
        } else {
            return HCConfig.INSTANCE.getInt("maxMBarGenOilT" + (getTier().toInt() + 1));
        }
    }

    @Override
    public float getGenerating(EnumFacing from) {

        if (!getRedstonePowered() || getFluidInNetwork(from) == 0) {
            RFUsage = 0;
            return 0f;
        }
        RFUsage = getEnergyStorage().extractEnergy(Constants.RF_USAGE_PER_TICK[getTier().toInt()], true);

        if (getEnergyStorage().getEnergyStored() > Constants.MIN_REQUIRED_RF) {
            float gen = RFUsage * Constants.CONVERSION_RATIO_RF_HYDRAULIC * (getHandler().isOilStored() ? 1.0F : Constants.WATER_CONVERSION_RATIO);
            gen = gen * ((float) getFluidInNetwork(from) / (float) getFluidCapacity(from));

            if (Float.compare(gen + getPressure(from), getMaxPressure(getHandler().isOilStored(), from)) > 0) {
                //This means the pressure we are generating is too much!
                gen = getMaxPressure(getHandler().isOilStored(), from) - getPressure(from);
            }
            if (Float.compare(gen, getMaxGenerating(from)) > 0) {
                gen = getMaxGenerating(from);
            }

            //RFUsage = (int)(gen * (getFluidInNetwork(from) / getFluidCapacity(from)) / Constants.CONVERSION_RATIO_RF_HYDRAULIC * (getHandler().isOilStored() ? 1.0F : Constants.WATER_CONVERSION_RATIO));
            return gen;
        } else {
            return 0;
        }
    }


    public PressureTier getTier() {

        return (PressureTier) worldObj.getBlockState(getPos()).getValue(Properties.TIER);
    }

    @Override
    public void onBlockBreaks() {

    }


    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {

        super.readFromNBT(tagCompound);

        networkCapacity = tagCompound.getInteger("networkCapacity");
        fluidInNetwork = tagCompound.getInteger("fluidInNetwork");
        RFUsage = tagCompound.getInteger("RFUsage");

        isRunning = tagCompound.getBoolean("isRunning");

        getEnergyStorage().readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {

        super.writeToNBT(tagCompound);

        tagCompound.setBoolean("isRunning", isRunning);

        if (getNetwork(getFacing()) != null) {
            tagCompound.setInteger("networkCapacity", getNetwork(getFacing()).getFluidCapacity());
            tagCompound.setInteger("fluidInNetwork", getNetwork(getFacing()).getFluidInNetwork());
        }
        tagCompound.setInteger("RFUsage", RFUsage);

        getEnergyStorage().writeToNBT(tagCompound);
    }

    @Override
    public void onFluidLevelChanged(int old) {

    }

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive,
                             boolean simulate) {

        if (from.equals(getFacing().getOpposite())) {
            return getEnergyStorage().receiveEnergy(maxReceive, simulate);
        } else {
            return 0;
        }
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {

        return from.equals(getFacing().getOpposite());
    }

    @Override
    public int getEnergyStored(EnumFacing from) {

        return getEnergyStorage().getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {

        return getEnergyStorage().getMaxEnergyStored();
    }

    @Override
    public boolean canConnectTo(EnumFacing side) {

        return side.equals(getFacing());
    }

    public EnumFacing getFacing() {

        return (EnumFacing) getWorldObj().getBlockState(getPos()).getValue(Properties.ROTATION);
    }

    public void setFacing(EnumFacing rotation) {

        if (!worldObj.isRemote) {
            getHandler().updateNetworkOnNextTick(getNetwork(getFacing()).getPressure());
        }

        getWorldObj().setBlockState(pos, getBlockType().getDefaultState().withProperty(Properties.ROTATION, rotation).withProperty(Properties.TIER, getTier()));
    }

    public boolean getIsRunning() {

        return isRunning;
    }

    @Override
    public boolean canWork(EnumFacing dir) {

        return dir.equals(getFacing());
    }

    @Override
    public void updateNetwork(float oldPressure) {

        PressureNetwork endNetwork;

        endNetwork = PressureNetwork.getNetworkInDir(worldObj, getPos(), getFacing());

        if (endNetwork != null) {
            pNetwork = endNetwork;
            pNetwork.addMachine(this, oldPressure, getFacing());
            //Log.info("Found an existing network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
        } else {
            pNetwork = new PressureNetwork(this, oldPressure, getFacing());
            //Log.info("Created a new network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
        }
    }


    public int getRFUsage() {

        return RFUsage;
    }
}
