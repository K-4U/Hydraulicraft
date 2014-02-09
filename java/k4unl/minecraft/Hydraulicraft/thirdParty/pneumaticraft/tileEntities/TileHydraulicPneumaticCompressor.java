package k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.tileEntities;

import pneumaticCraft.api.tileentity.AirHandlerSupplier;
import pneumaticCraft.api.tileentity.IAirHandler;
import pneumaticCraft.api.tileentity.IPneumaticMachine;
import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class TileHydraulicPneumaticCompressor extends TileEntity implements
		IPneumaticMachine, IHydraulicConsumer {
    private IAirHandler airHandler;
    private IBaseClass baseHandler;
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
        getHandler().updateEntity();
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
			float usage = (getHandler().getPressure() / 10000); 
			return usage;
		}else{
			return 0F;
		}
	}

	private void doCompress() {
		//Simplest function EVER!
		float usage = (getHandler().getPressure() / 10000);
		getAirHandler().addAir(usage * Constants.CONVERSION_RATIO_HYDRAULIC_AIR, ForgeDirection.UNKNOWN);
	}

	private boolean canRun() {
		if(!getHandler().getRedstonePowered()){
			return false;
		}
		//Get minimal pressure
		return (getHandler().getPressure() > Constants.MIN_REQUIRED_PRESSURE_COMPRESSOR && getAirHandler().getPressure(ForgeDirection.UNKNOWN) < dangerPressure);
	}

	@Override
	public float getMaxPressure(boolean isOil) {
		if(isOil){
			return Constants.MAX_MBAR_OIL_TIER_3;
		}else{
			return Constants.MAX_MBAR_WATER_TIER_3;
		}
	}

	@Override
	public int getMaxStorage() {
		return FluidContainerRegistry.BUCKET_VOLUME * 20;
	}

	@Override
	public void onBlockBreaks() {
	}

	@Override
	public IBaseClass getHandler() {
		if(baseHandler == null) baseHandler = HydraulicBaseClassSupplier.getConsumerClass(this);
        return baseHandler;
	}

	@Override
	public void readNBT(NBTTagCompound tagCompound) {
		
	}

	@Override
	public void writeNBT(NBTTagCompound tagCompound) {
		
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
		getHandler().onDataPacket(net, packet);
		
	}

	@Override
	public Packet getDescriptionPacket() {
		return getHandler().getDescriptionPacket();
	}

	public void checkRedstonePower() {
		getHandler().checkRedstonePower();
	}

	@Override
	public void onPressureChanged(float old) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFluidLevelChanged(int old) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return true;
	}
}
