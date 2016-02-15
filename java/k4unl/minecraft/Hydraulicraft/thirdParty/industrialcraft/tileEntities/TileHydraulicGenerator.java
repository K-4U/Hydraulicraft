package k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileHydraulicGenerator extends TileHydraulicBase implements IHydraulicConsumer { //}, IEnergySource {

    private EnumFacing facing = EnumFacing.UP;
    private int   ic2EnergyStored;
    private int   energyToAdd;
    private float pressureRequired;

    public TileHydraulicGenerator() {

        super(20);
        super.init(this);
    }

    public EnumFacing getFacing() {

        return facing;
    }

    public void setFacing(EnumFacing newDir) {

        facing = newDir;
    }


    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {

        super.readFromNBT(tagCompound);
        ic2EnergyStored = tagCompound.getInteger("ic2EnergyStored");
        facing = EnumFacing.byName(tagCompound.getString("facing"));
        energyToAdd = tagCompound.getInteger("energyToAdd");
        pressureRequired = tagCompound.getFloat("pressureRequired");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {

        super.writeToNBT(tagCompound);
        tagCompound.setInteger("ic2EnergyStored", ic2EnergyStored);
        tagCompound.setString("facing", facing.toString());
        tagCompound.setInteger("energyToAdd", energyToAdd);
        tagCompound.setFloat("pressureRequired", pressureRequired);
    }

    @Override
    public void onFluidLevelChanged(int old) {

    }

    @Override
    public float workFunction(boolean simulate, EnumFacing from) {

        return createPower(simulate);
    }

    public int getEnergyToAdd() {

        return energyToAdd;
    }

    public float createPower(boolean simulate) {

        boolean rp = getRedstonePowered();
        int pressureReq = Float.compare(getPressure(getFacing().getOpposite()), Constants.MIN_REQUIRED_PRESSURE_DYNAMO);
        float energyStored = getEUStored();
        float energyMax = getMaxEUStorage();
        int EUReq = Float.compare(energyStored, energyMax);

        if (!rp || pressureReq < 0 || EUReq >= 0) {
            energyToAdd = 0;
            pressureRequired = 0F;
            getHandler().updateBlock();
            return 0F;
        }

        energyToAdd = (int) (((getPressure(getFacing().getOpposite()) / getMaxPressure(getHandler().isOilStored(), getFacing())) * Constants.CONVERSION_RATIO_HYDRAULIC_EU) * Constants.MAX_TRANSFER_EU);
        if (!simulate) {
            energyToAdd = addEnergy(energyToAdd);
        }

        int efficiency = 20;
        float pressureUsage = energyToAdd * (1.0F - (efficiency / 100F));
        pressureRequired = pressureUsage;
        return pressureUsage;
    }

    @Override
    public boolean canConnectTo(EnumFacing side) {

        return !side.equals(facing);
    }

    @Override
    public void firstTick() {
        //MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
    }

    @Override
    public boolean canWork(EnumFacing dir) {

        return dir.equals(EnumFacing.UP);
    }

    /*@Override
    public boolean emitsEnergyTo(TileEntity receiver, EnumFacing direction) {
        if(!direction.equals(getFacing())){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public double getOfferedEnergy() {
        if(worldObj.isRemote){
            //GUI
            if(ic2EnergyStored <= energyToAdd){
                return energyToAdd;
            }else{
                return Math.min(ic2EnergyStored, Constants.MAX_TRANSFER_EU);
            }
        }else{
            return Math.min(ic2EnergyStored, Constants.MAX_TRANSFER_EU);
        }
    }

    @Override
    public void drawEnergy(double amount) {
        ic2EnergyStored -= amount;
        if(ic2EnergyStored < 0){
            ic2EnergyStored = 0;
        }
    }

    @Override
    public int getSourceTier() {
        return 1;
    }
*/
    public int getEUStored() {

        return ic2EnergyStored;
    }

    public int getMaxEUStorage() {

        return Constants.INTERNAL_EU_STORAGE[2];
    }

    public float getPressureRequired() {

        return pressureRequired;
    }

    @Override
    public void invalidate() {

        if (worldObj != null && !worldObj.isRemote) {
            //MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }
        super.invalidate();
    }

    @Override
    public void onChunkUnload() {

        if (worldObj != null && !worldObj.isRemote) {
            //MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }
        super.onChunkUnload();
    }
    
    public int addEnergy(int quantity) {

        ic2EnergyStored += quantity;

        if (ic2EnergyStored > getMaxEUStorage()) {
            quantity -= ic2EnergyStored - getMaxEUStorage();
            ic2EnergyStored = getMaxEUStorage();
        } else if (ic2EnergyStored < 0) {
            quantity -= ic2EnergyStored;
            ic2EnergyStored = 0;
        }


        return quantity;
    }
}
