package pet.minecraft.Hydraulicraft.lib;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import pet.minecraft.Hydraulicraft.baseClasses.MachineEntity;

public class Functions {

	public static List mergeList(List l1, List l2){
		for (Object object : l1) {
			if(!l2.contains(object)){
				l2.add(object);
			}
		}
		return l2;
	}
	
	public static void checkAndFillSideBlocks(World w, int x, int y, int z){
		if(!w.isRemote){
			TileEntity t = w.getBlockTileEntity(x, y, z);
			if(t instanceof MachineEntity){
				List <MachineEntity> mainList = new ArrayList<MachineEntity>();
				mainList.add((MachineEntity) t);
				mainList = ((MachineEntity) t).getConnectedBlocks(mainList);
				
				//Log.info("Iteration done. " + mainList.size() + " machines found");
				boolean isOil = ((MachineEntity)t).isOilStored();
				int fluidInSystem = 0;
				for (MachineEntity machineEntity : mainList) {
					fluidInSystem = fluidInSystem + machineEntity.getStored();
					machineEntity.setStored(0, isOil);
				}
				 
				//Log.info("Fluid in system: " + fluidInSystem);
				
				List<MachineEntity> remainingBlocks = new ArrayList<MachineEntity>();
				int newFluidInSystem = 0;
				while(fluidInSystem > 0){
					if(mainList.size() == 0){
						//Error!
						Log.error("Too much fluid in the system!");
					}
					int toSet = fluidInSystem / mainList.size();
					//Log.info("Before iteration. Toset = " + toSet);
					for (MachineEntity machineEntity : mainList) {
						if(machineEntity.getStorage() < (toSet + machineEntity.getStored())){
							newFluidInSystem = newFluidInSystem + ((toSet + machineEntity.getStored()) - machineEntity.getStorage());
							machineEntity.setStored(machineEntity.getStorage(), isOil);
						}else{
							remainingBlocks.add(machineEntity);
							machineEntity.setStored(toSet + machineEntity.getStored(), isOil);
						}
						
						//Log.info("Is this the original? " + machineEntity.equals(t));
						w.markBlockForUpdate(machineEntity.xCoord, machineEntity.yCoord, machineEntity.zCoord);
					}

					//Log.info("Iteration done. Fluid remaining: " + newFluidInSystem);
					fluidInSystem = newFluidInSystem;
					newFluidInSystem = 0;
					
					mainList.clear();
					for (MachineEntity machineEntity : remainingBlocks) {
						mainList.add(machineEntity);
					}
					
					remainingBlocks.clear();
				}
				
				
				//Log.info("Done iterating. Found " + mainList.size() + " blocks!");
			}
		}
	}
	
}
