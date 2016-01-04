package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.util.EnumFacing;

public class TilePressureDisposal extends TileHydraulicBase implements
		IHydraulicConsumer {

	public TilePressureDisposal(){
		super(1);
		super.init(this);
	}

	@Override
	public float workFunction(boolean simulate, EnumFacing from) {
		if(getRedstonePowered()){
			if(getPressure(EnumFacing.UP) > (HCConfig.INSTANCE.getInt("maxMBarGenWaterT3")*4)){
				return (HCConfig.INSTANCE.getInt("maxMBarGenWaterT3")*4) % getPressure(EnumFacing.UP);
			}else{
				return getPressure(EnumFacing.UP);
			}
		}
		return 0;
	}

	@Override
	public void onFluidLevelChanged(int old) {}
	
	@Override
	public boolean canConnectTo(EnumFacing side) {
		return true;
	}

	@Override
	public boolean canWork(EnumFacing dir) {
		return dir.equals(EnumFacing.UP);
	}
}
