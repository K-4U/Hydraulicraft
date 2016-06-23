package k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.tileEntities;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileHydraulicPneumaticCompressor extends TileHydraulicBase implements /*IPneumaticMachine, */IHydraulicConsumer {

    //private IAirHandler airHandler;
    private static int dangerPressure = 5;

    
    public TileHydraulicPneumaticCompressor() {

        super(20);
        super.init(this);
    }
    /*
    @Override
    public IAirHandler getAirHandler(){
        if(airHandler == null) airHandler = AirHandlerSupplier.getAirHandler(dangerPressure, 7, 2000);
        return airHandler;
    }
    */
    /*@Override
    public boolean isConnectedTo(EnumFacing side){
        return true;
    }*/

    @Override
    public void update() {

        super.update();
        //getAirHandler().updateEntityI();
    }

    @Override
    public void validate() {

        super.validate();
        //getAirHandler().validateI(this);
    }

    
    public float getPneumaticPressure() {

        return 0.0F;//getAirHandler().getPressure(EnumFacing.UNKNOWN);
    }
    
    public float getPneumaticMaxPressure() {

        return 1.0F;//getAirHandler().getMaxPressure();
    }
    
    public float getPneumaticDangerPressure() {

        return dangerPressure;
    }
    
    /* *****************
     * HYDRAULICRAFT
     */
    @Override
    public float workFunction(boolean simulate, EnumFacing from) {

        if (canRun()) {
            if (!simulate) {
                doCompress();
            }
            //The higher the pressure
            //The higher the speed!
            //But also the more it uses..
            float usage = (getPressure(EnumFacing.UP) / 10000);
            return usage;
        } else {
            return 0F;
        }
    }

    private void doCompress() {
        //Simplest function EVER!
        float usage = (getPressure(EnumFacing.UP) / 10000);
        //getAirHandler().addAir((int)usage * (int)Constants.CONVERSION_RATIO_HYDRAULIC_AIR, EnumFacing.UP);
    }

    private boolean canRun() {

        if (!getRedstonePowered()) {
            return false;
        }
        //Get minimal pressure
        return true;//(getPressure(EnumFacing.UP) > Constants.MIN_REQUIRED_PRESSURE_COMPRESSOR && getAirHandler().getPressure(EnumFacing.UP) < dangerPressure);
    }


    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {

        super.readFromNBT(tagCompound);
        //getAirHandler().readFromNBTI(tagCompound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {

        super.writeToNBT(tagCompound);
        //getAirHandler().writeToNBTI(tagCompound);
        return tagCompound;
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
}
