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
	
	public static boolean isInString(String oreName, String[] list){
		boolean ret = false;
		for(int i = 0; i < list.length; i++){
			ret = ret || (oreName.substring(0, list[i].length()).equals(list[i]));
		}
		return ret;
	}
	
	public static String getMetalName(String oreDictName){
		String[] prefix = {"ingot"};
		if(isInString(oreDictName, prefix)){
			return oreDictName.substring(prefix[0].length());
		}
		
		prefix[0] = "ore";
		if(isInString(oreDictName, prefix)){
			return oreDictName.substring(prefix[0].length());
		}else{
			return "ERROR";
		}
	}
	
	public static void checkAndFillSideBlocks(World w, int x, int y, int z){
		if(!w.isRemote){
			TileEntity t = w.getBlockTileEntity(x, y, z);
			if(t instanceof MachineEntity){
				List <MachineEntity> mainList = new ArrayList<MachineEntity>();
				mainList.add((MachineEntity) t);
				mainList = ((MachineEntity) t).getConnectedBlocks(mainList);
				
				//Log.info("Iteration done. " + mainList.size() + " machines found");
				boolean isOil = false;
				int fluidInSystem = 0;
				int totalFluidCapacity = 0;
				float pressureInSystem = 0;
				int oldMachineCount = 0;
				for (MachineEntity machineEntity : mainList) {
					if(oldMachineCount == 0){
						oldMachineCount = machineEntity.getNetworkCount();
					}
					if(isOil == false && machineEntity.isOilStored()){
						isOil = true;
					}
					fluidInSystem = fluidInSystem + machineEntity.getStored();
					totalFluidCapacity = totalFluidCapacity + machineEntity.getStorage();
					machineEntity.setStored(0, isOil);
					
					
					if(machineEntity.getPressure() > pressureInSystem){
						pressureInSystem = machineEntity.getPressure();
					}
					machineEntity.setPressure(0);
				}
				
				//Log.info("Fluid in system: " + fluidInSystem);
				Log.info("Pressure in system: " + pressureInSystem);
				
				if(oldMachineCount < mainList.size()){
					pressureInSystem = pressureInSystem - (pressureInSystem / mainList.size());
				}
				for (MachineEntity machineEntity : mainList) {
					machineEntity.setPressure(pressureInSystem);
					machineEntity.setNetworkCount(mainList.size());
					machineEntity.setTotalFluidCapacity(totalFluidCapacity);
					//This will allow the machines themselves to explode when something goes wrong!
					w.markBlockForUpdate(machineEntity.xCoord, machineEntity.yCoord, machineEntity.zCoord);
				}
				
				List<MachineEntity> remainingBlocks = new ArrayList<MachineEntity>();
				int newFluidInSystem = 0;
				boolean firstIteration = true;
				while(fluidInSystem > 0){
					if(mainList.size() == 0){
						//Error!
						Log.error("Too much fluid in the system!");
						break;
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
						
						if(firstIteration){
							machineEntity.setFluidInSystem(fluidInSystem);
						}
						
						//Log.info("Is this the original? " + machineEntity.equals(t));
						
					}

					//Log.info("Iteration done. Fluid remaining: " + newFluidInSystem);
					fluidInSystem = newFluidInSystem;
					newFluidInSystem = 0;
					
					mainList.clear();
					for (MachineEntity machineEntity : remainingBlocks) {
						mainList.add(machineEntity);
					}
					
					remainingBlocks.clear();
					firstIteration = false;
				}
				
				//Log.info("Done iterating. Found " + mainList.size() + " blocks!");
			}
		}
	}
	
}
