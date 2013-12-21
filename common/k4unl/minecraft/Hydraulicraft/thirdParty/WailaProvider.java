package k4unl.minecraft.Hydraulicraft.thirdParty;

import java.util.List;

import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicHose;
import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicPressureVat;
import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicPump;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineEntity;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.item.ItemStack;

public class WailaProvider implements IWailaDataProvider {

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack,
			List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		// TODO Auto-generated method stub
		return currenttip;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack,
			List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		
		if(accessor.getTileEntity() instanceof MachineEntity){
			MachineEntity mEnt = (MachineEntity) accessor.getTileEntity();
			
			int stored = mEnt.getStored();
			int max = mEnt.getStorage();
			
			float pressure = mEnt.getPressure();
			float maxPressure = mEnt.getMaxPressure();
	
			currenttip.add("Fl: " + stored + "/" + max + " mBuckets (" + (int)(((float)stored / (float)max) * 100) + "%)");
			currenttip.add("Pr: " + pressure + "/" + maxPressure + " mBar (" + (int)((float)pressure / (float)maxPressure) * 100 + "%)");
			
			if(mEnt instanceof TileHydraulicPump){
				float gen = ((TileHydraulicPump) mEnt).getGenerating();
				int maxGen = ((TileHydraulicPump) mEnt).getMaxGenerating();
				int tier = ((TileHydraulicPump) mEnt).getTier();
				currenttip.add("Gen: " + gen + "/" + maxGen);
			}			
		}
		return currenttip;
	}
	
	public static void callbackRegister(IWailaRegistrar registrar){
		registrar.registerHeadProvider(new WailaProvider(), MachineEntity.class);
		registrar.registerBodyProvider(new WailaProvider(), MachineEntity.class);
	}

}
