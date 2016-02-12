package k4unl.minecraft.Hydraulicraft.thirdParty.rf.tileEntities;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.thirdParty.rf.IEnergyInfo;
import k4unl.minecraft.Hydraulicraft.tileEntities.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.Hydraulicraft.tileEntities.interfaces.ICustomNetwork;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileHydraulicDynamo extends TileHydraulicBase implements IHydraulicConsumer, IEnergyProvider, IEnergyInfo, ICustomNetwork {

    private EnumFacing facing = EnumFacing.UP;

    private   boolean       isRunning        = true;
    private   float         percentageRun    = 0.0F;
    private   float         direction        = 0.005F;
    protected EnergyStorage storage          = new EnergyStorage(32000, Constants.MAX_TRANSFER_RF);
    private   int           energyGen        = 0;
    private   float         pressureRequired = 0.0F;

    public TileHydraulicDynamo() {

        super(3);
        super.init(this);
    }

    public int getGenerating() {

        return energyGen;
    }

    public EnumFacing getFacing() {

        return facing;
    }

    public void setFacing(EnumFacing newDir) {

        getHandler().updateNetworkOnNextTick(getPressure(getFacing()));
        facing = newDir;
    }


    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {

        super.readFromNBT(tagCompound);
        isRunning = tagCompound.getBoolean("isRunning");
        facing = EnumFacing.byName(tagCompound.getString("facing"));
        storage.readFromNBT(tagCompound);
        energyGen = tagCompound.getInteger("energyGen");
        pressureRequired = tagCompound.getFloat("pressureRequired");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {

        super.writeToNBT(tagCompound);
        tagCompound.setBoolean("isRunning", isRunning);
        tagCompound.setString("facing", facing.toString());
        tagCompound.setInteger("energyGen", energyGen);
        tagCompound.setFloat("pressureRequired", pressureRequired);
        storage.writeToNBT(tagCompound);
    }

    public float getPercentageOfRender() {

        if (isRunning) {
            percentageRun += direction;
        } else if (percentageRun > 0 && Float.compare(direction, 0.0F) > 0) {
            percentageRun += direction;
        }
        if (Float.compare(percentageRun, 1.0F) >= 0 && Float.compare(direction, 0.0F) > 0) {
            //direction = -0.005F;
            percentageRun = 0.0F;
        }
        return percentageRun;
    }

    public boolean getIsRunning() {

        return isRunning;
    }

    @Override
    public void onFluidLevelChanged(int old) {

    }


    @Override
    public float workFunction(boolean simulate, EnumFacing from) {

        pressureRequired = createPower(simulate);

		/*if(simulate && storage.getEnergyStored() > 0 && Float.compare(pressureRequired, 0.0F) == 0){
            pressureRequired += 0.1F;
		}*/

        return pressureRequired;
    }

    private float createPower(boolean simulate) {

        if (getPressure(getFacing().getOpposite()) < Constants.MIN_REQUIRED_PRESSURE_DYNAMO || !getRedstonePowered()) {
            isRunning = false;
            energyGen = 0;
            getHandler().updateBlock();
            return 0F;
        }

        float energyToAdd = ((getPressure(getFacing().getOpposite()) / getMaxPressure(getHandler().isOilStored(), null)) * Constants.CONVERSION_RATIO_HYDRAULIC_RF);//
        energyToAdd *= Constants.MAX_TRANSFER_RF;
        //energyToAdd *= Constants.CONVERSION_RATIO_HYDRAULIC_RF;
        energyToAdd = storage.receiveEnergy((int)energyToAdd, simulate);

        if (!simulate) {
            energyGen = (int) energyToAdd;
        }

        int efficiency = 80;
        float pressureUsage = energyToAdd * (1.0F + (efficiency / 100F));
        if (pressureUsage > 0.0F) {
            isRunning = true;
        } else {
            isRunning = false;
            getHandler().updateBlock();
        }
        return pressureUsage;
    }

    public float getPressureRequired() {

        return pressureRequired;
    }


    @Override
    public void update() {

        super.update();

        //PUSH pressure
        //This had me busy for two days.

        if (!worldObj.isRemote) {
            TileEntity receiver = worldObj.getTileEntity(pos.offset(facing));
            if(receiver != null && receiver instanceof IEnergyReceiver){
				IEnergyReceiver recv = (IEnergyReceiver) receiver;
				if(recv.canConnectEnergy(getFacing().getOpposite())){
					int extracted = storage.extractEnergy(Constants.MAX_TRANSFER_RF, true);
					int energyPushed = recv.receiveEnergy(facing.getOpposite(), extracted, true);
					
					if(energyPushed > 0){
						recv.receiveEnergy(facing.getOpposite(), storage.extractEnergy(energyPushed, false), false);
					}
				}
			}
        }
    }

    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return (from.equals(facing));
    }

    @Override
    public int getEnergyStored(EnumFacing from) {
        return this.storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        if (from.equals(facing) || from.equals(EnumFacing.UP)) {
            return this.storage.getMaxEnergyStored();
        } else {
            return 0;
        }
    }

    @Override
    public boolean canConnectTo(EnumFacing side) {

        return side.equals(facing.getOpposite());
    }


    @Override
    public int getInfoEnergyPerTick() {

        float energyToAdd = ((getPressure(getFacing().getOpposite()) / getMaxPressure(getHandler().isOilStored(), null)) * Constants.CONVERSION_RATIO_HYDRAULIC_RF) * (getPressure(EnumFacing.UP) / 1000);
        energyToAdd = storage.receiveEnergy((int) energyToAdd, true);
        return (int) energyToAdd;
    }

    @Override
    public int getInfoMaxEnergyPerTick() {

        return storage.getMaxExtract();
    }

    @Override
    public int getInfoEnergyStored() {

        return this.storage.getEnergyStored();
    }

    @Override
    public int getInfoMaxEnergyStored() {

        return this.storage.getMaxEnergyStored();
    }

    @Override
    public boolean canWork(EnumFacing dir) {

        return dir.equals(facing.getOpposite());
    }

    @Override
    public void updateNetwork(float oldPressure) {

        PressureNetwork endNetwork = null;

        endNetwork = PressureNetwork.getNetworkInDir(worldObj, getPos(), getFacing().getOpposite());

        if (endNetwork != null) {
            pNetwork = endNetwork;
            pNetwork.addMachine(this, oldPressure, getFacing().getOpposite());
            getHandler().updateFluidOnNextTick();
            //Log.info("Found an existing network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
        } else {
            pNetwork = new PressureNetwork(this, oldPressure, getFacing().getOpposite());
            //Log.info("Created a new network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
        }
    }

}
