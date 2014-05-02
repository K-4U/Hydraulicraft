package k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.tileEntities;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import pneumaticCraft.api.tileentity.AirHandlerSupplier;
import pneumaticCraft.api.tileentity.IAirHandler;
import pneumaticCraft.api.tileentity.IPneumaticMachine;

public class TileHydraulicPneumaticCompressor extends TileHydraulicBase implements
		IPneumaticMachine, IHydraulicConsumer {
    private IAirHandler airHandler;
    private static float dangerPressure = 5;  

    
    public TileHydraulicPneumaticCompressor(){
    	super(PressureTier.HIGHPRESSURE, 20);
    	super.validateI(this);
    }
    
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
        getAirHandler().updateEntityI();
    }

    @Override
    public void validate(){
        super.validate();
        getAirHandler().validateI(this);
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
	public float workFunction(boolean simulate, ForgeDirection from) {
		if(canRun()){
			if(!simulate){
				doCompress();
			}
			//The higher the pressure
			//The higher the speed!
			//But also the more it uses..
			float usage = (getPressure(ForgeDirection.UNKNOWN) / 10000); 
			return usage;
		}else{
			return 0F;
		}
	}

	private void doCompress() {
		//Simplest function EVER!
		float usage = (getPressure(ForgeDirection.UNKNOWN) / 10000);
		getAirHandler().addAir(usage * Constants.CONVERSION_RATIO_HYDRAULIC_AIR, ForgeDirection.UNKNOWN);
	}

	private boolean canRun() {
		if(!getRedstonePowered()){
			return false;
		}
		//Get minimal pressure
		return (getPressure(ForgeDirection.UNKNOWN) > Constants.MIN_REQUIRED_PRESSURE_COMPRESSOR && getAirHandler().getPressure(ForgeDirection.UNKNOWN) < dangerPressure);
	}

	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		getAirHandler().readFromNBTI(tagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		getAirHandler().writeToNBTI(tagCompound);
	}

	@Override
	public void onFluidLevelChanged(int old) {	}
	
	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return true;
	}

	@Override
	public boolean canWork(ForgeDirection dir) {
		return dir.equals(ForgeDirection.UP);
	}
}
