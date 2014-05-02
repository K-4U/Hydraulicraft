package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraftforge.common.util.ForgeDirection;

public class TilePressureDisposal extends TileHydraulicBase implements
		IHydraulicConsumer {

	public TilePressureDisposal(){
		super(PressureTier.HIGHPRESSURE, 1);
		super.validateI(this);
	}

	@Override
	public float workFunction(boolean simulate, ForgeDirection from) {
		if(getRedstonePowered()){
			if(getPressure(ForgeDirection.UNKNOWN) > (Constants.MAX_MBAR_GEN_OIL_TIER_3*4)){
				return (Constants.MAX_MBAR_GEN_OIL_TIER_3*4) % getPressure(ForgeDirection.UNKNOWN);
			}else{
				return getPressure(ForgeDirection.UNKNOWN);
			}
		}
		return 0;
	}

	@Override
	public void onFluidLevelChanged(int old) {}
	
	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return true;
	}

	@Override
	public boolean canWork(ForgeDirection dir) {
		return dir.equals(ForgeDirection.UP);
	}
}
