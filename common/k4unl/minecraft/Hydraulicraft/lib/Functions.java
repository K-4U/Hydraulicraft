package k4unl.minecraft.Hydraulicraft.lib;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.baseClasses.MachineEntity;
import k4unl.minecraft.Hydraulicraft.items.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

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

    public static String getPrefixName(String oreDictName){
        //TODO: Fix this function up. It looks ugly
        String[] prefix = {"ingot"};
        if(isInString(oreDictName, prefix)){
            return "ingot";
        }

        prefix[0] = "ore";
        if(isInString(oreDictName, prefix)){
            return "ore";
        }

        if(oreDictName.equals("netherquartz")){
            return "Quartz";
        }else{
            return "ERROR";
        }
    }

	public static String getMetalName(String oreDictName){
		String[] prefix = {"ingot"};
		if(isInString(oreDictName, prefix)){
			return oreDictName.substring(prefix[0].length());
		}
		
		prefix[0] = "ore";
		if(isInString(oreDictName, prefix)){
			return oreDictName.substring(prefix[0].length());
		}
		
		if(oreDictName.equals("netherquartz")){
			return "Quartz";
		}else{
			return "ERROR";
		}
	}
	
	public static int getIngotId(String ingotName){
        if(ingotName.equals("ingotIron")){
            return Item.ingotIron.itemID;
        }else if(ingotName.equals("ingotCopper")){
            return Items.ingotCopper.itemID;
        }else if(ingotName.equals("ingotLead")){
        	return Items.ingotLead.itemID;
        }else if(ingotName.equals("ingotGold")){
			return Item.ingotGold.itemID;
		}
		return 1;
	}
	
	public static void checkSidesSetPressure(World w, int x, int y, int z, float newPressure){
		if(!w.isRemote){
			TileEntity t = w.getBlockTileEntity(x, y, z);
			if(t instanceof MachineEntity){
				List <MachineEntity> mainList = new ArrayList<MachineEntity>();
				mainList.add((MachineEntity) t);
				mainList = ((MachineEntity) t).getConnectedBlocks(mainList);
				
				MachineEntity ent = (MachineEntity) t;
				
				float pressureInSystem = 0;
				if(newPressure < 0){
					newPressure = 0;
				}
				for (MachineEntity machineEntity : mainList) {
					machineEntity.setPressure(newPressure);
				}
			}
		}
	}
	
	public static void setFluidInSystem(List<MachineEntity> mainList, int fluidInSystem, boolean isOil){
		List<MachineEntity> remainingBlocks = new ArrayList<MachineEntity>();
		int newFluidInSystem = 0;
		boolean firstIteration = true;
		//Log.info("Before iteration: FIS = " + fluidInSystem + " M = " + mainList.size());
		while(fluidInSystem > 0){
			if(mainList.size() == 0){
				//Error!
				//Log.error("Too much fluid in the system!");
				break;
			}
			int toSet = fluidInSystem / mainList.size();
			while(fluidInSystem > toSet * mainList.size()){
				fluidInSystem +=1;
				toSet = fluidInSystem / mainList.size();
			}
			
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
	}
	
	public static void checkAndSetSideBlocks(World w, int x, int y, int z, int newFluidInSystem, boolean isOil){
		if(!w.isRemote){
			TileEntity t = w.getBlockTileEntity(x, y, z);
			if(t instanceof MachineEntity){
				List <MachineEntity> mainList = new ArrayList<MachineEntity>();
				mainList.add((MachineEntity) t);
				mainList = ((MachineEntity) t).getConnectedBlocks(mainList);
				
				for (MachineEntity machineEntity : mainList) {
					machineEntity.setStored(0, isOil);
				}
				
				setFluidInSystem(mainList, newFluidInSystem, isOil);
				
				//Log.info("Done iterating. Found " + mainList.size() + " blocks!");
			}
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
					
					
					//if(machineEntity.getPressure() > pressureInSystem){
						pressureInSystem += machineEntity.getPressure();
					//}
					machineEntity.setPressure(0);
				}
				
				
				if(fluidInSystem < 100){
					pressureInSystem = pressureInSystem * ((float)fluidInSystem / 100F);
				}
				pressureInSystem = pressureInSystem / mainList.size();
				//Log.info("Fluid in system: " + fluidInSystem);
				//Log.info("Pressure in system: " + pressureInSystem);
				
				if(oldMachineCount <= mainList.size()){
					//pressureInSystem = pressureInSystem - (pressureInSystem / mainList.size());
				}else if(oldMachineCount > mainList.size()){
					//There were more machines a second ago!
					//Well.. do nothing really..
				}
				for (MachineEntity machineEntity : mainList) {
					machineEntity.setPressure(pressureInSystem);
					machineEntity.setNetworkCount(mainList.size());
					machineEntity.setTotalFluidCapacity(totalFluidCapacity);
					//This will allow the machines themselves to explode when something goes wrong!
				}
				
				setFluidInSystem(mainList, fluidInSystem, isOil);
				
				//Log.info("Done iterating. Found " + mainList.size() + " blocks!");
			}
		}
	}
	
}
