package k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.tileEntities;

import k4unl.minecraft.Hydraulicraft.baseClasses.entities.TileConsumer;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry.FluidRegisterEvent;
import pneumaticCraft.api.tileentity.AirHandlerSupplier;
import pneumaticCraft.api.tileentity.IAirHandler;
import pneumaticCraft.api.tileentity.IPneumaticMachine;

public class TileHydraulicPneumaticCompressor extends TileConsumer implements
		IPneumaticMachine {
    private IAirHandler airHandler;
    private static float dangerPressure = 5;  

    @Override
    public IAirHandler getAirHandler(){
        if(airHandler == null) airHandler = AirHandlerSupplier.getAirHandler(dangerPressure, 7, 50, 2000);
        return airHandler;
    }
    
    @Override
    public boolean isConnectedTo(ForgeDirection side){
        return true;
    }

    @Override
    public void updateEntity(){
    	super.updateEntity();
        getAirHandler().update();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag){
        super.writeToNBT(tag);
        getAirHandler().writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag){
        super.readFromNBT(tag);
        getAirHandler().readFromNBT(tag);
    }

    @Override
    public void validate(){
        super.validate();
        getAirHandler().validate(this);
    }
	
    
    public float getPneumaticPressure(){
    	return getAirHandler().getPressure(ForgeDirection.UNKNOWN);
    }
    
    public float getPneumaticMaxPressure(){
    	return getAirHandler().getMaxPressure();
    }
    
    public float getPneumaticDangerPressure(){
    	return dangerPressure;
    }
    
	/* *****************
	 * HYDRAULICRAFT
	 */
	@Override
	public float workFunction(boolean simulate) {
		if(canRun()){
			if(!simulate){
				doCompress();
			}
			//The higher the pressure
			//The higher the speed!
			//But also the more it uses..
			float usage = (getPressure() / 10000); 
			return usage;
		}else{
			return 0F;
		}
	}

	private void doCompress() {
		//Simplest function EVER!
		float usage = (getPressure() / 10000);
		getAirHandler().addAir(usage * Constants.CONVERSION_RATIO_AIR_HYDRAULIC, ForgeDirection.UNKNOWN);
	}

	private boolean canRun() {
		if(!getRedstonePowered()){
			return false;
		}
		//Get minimal pressure
		return (this.getPressure() > Constants.MIN_PRESSURE_COMPRESSOR && getAirHandler().getPressure(ForgeDirection.UNKNOWN) < dangerPressure);
	}

	@Override
	public int getMaxBar() {
		return Constants.MAX_MBAR_OIL_TIER_3;
	}

	@Override
	public int getStorage() {
		return FluidContainerRegistry.BUCKET_VOLUME * 20;
	}

	@Override
	public void onBlockBreaks() {
		// TODO Auto-generated method stub
		
	}


}
