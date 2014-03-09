package k4unl.minecraft.Hydraulicraft.lib;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.api.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.multipart.Multipart;
import k4unl.minecraft.Hydraulicraft.multipart.PartHose;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import codechicken.multipart.TileMultipart;

public class Functions {
	private static boolean isUpdateAvailable;
	
	public static void showMessageInChat(String message){
		EntityClientPlayerMP pl = Minecraft.getMinecraft().thePlayer;
		pl.addChatMessage(message);
		
	}
	
	public static List mergeList(List l1, List l2){
		for (Object object : l1) {
			if(!l2.contains(object)){
				l2.add(object);
			}
		}
		return l2;
	}
	
	public static int getIntDirFromDirection(ForgeDirection dir){
		switch(dir){
		case DOWN:
			return 0;
		case EAST:
			return 5;
		case NORTH:
			return 2;
		case SOUTH:
			return 3;
		case UNKNOWN:
			return 0;
		case UP:
			return 1;
		case WEST:
			return 4;
		default:
			return 0;
		}
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
	
	public static ItemStack getIngot(String ingotName){
		ArrayList<ItemStack> targetStackL = OreDictionary.getOres(ingotName);
		if(targetStackL.size() > 0){
			return targetStackL.get(0);
		}
		return null;
	}
	
	public static void setFluidInSystem(List<IHydraulicMachine> mainList, int fluidInSystem, boolean isOil){
		List<IHydraulicMachine> remainingBlocks = new ArrayList<IHydraulicMachine>();
		int newFluidInSystem = 0;
		boolean firstIteration = true;
		//Log.info("Before iteration: FIS = " + fluidInSystem + " M = " + mainList.size());
		while(fluidInSystem > 0){
			if(mainList.size() == 0){
				//Error!
				Log.error("Too much fluid in the system!");
				break;
			}
			int toSet = fluidInSystem / mainList.size();
			while(fluidInSystem > toSet * mainList.size()){
				fluidInSystem -= 1;
				toSet = fluidInSystem / mainList.size();
			}
			
			for (IHydraulicMachine machineEntity : mainList) {
				if(machineEntity.getMaxStorage() < (toSet + machineEntity.getHandler().getStored(null))){
					//This machine can't store this much!
					newFluidInSystem = newFluidInSystem + ((toSet + machineEntity.getHandler().getStored(null)) - machineEntity.getMaxStorage());
					machineEntity.getHandler().setStored(machineEntity.getMaxStorage(), isOil);
				}else{
					remainingBlocks.add(machineEntity);
					machineEntity.getHandler().setStored(toSet + machineEntity.getHandler().getStored(null), isOil);
				}
				
				if(firstIteration){
					machineEntity.getHandler().setFluidInSystem(fluidInSystem);
				}
			}

			//Log.info("Iteration done. Fluid remaining: " + newFluidInSystem);
			fluidInSystem = newFluidInSystem;
			newFluidInSystem = 0;
			
			mainList.clear();
			for (IHydraulicMachine machineEntity : remainingBlocks) {
				mainList.add(machineEntity);
			}
			
			remainingBlocks.clear();
			firstIteration = false;
		}
	}
	
	public static void checkAndSetSideBlocks(World w, int x, int y, int z, int newFluidInSystem, boolean isOil){
		if(!w.isRemote){
			TileEntity t = w.getBlockTileEntity(x, y, z);
			if(t instanceof IHydraulicMachine){
				List <IHydraulicMachine> mainList = new ArrayList<IHydraulicMachine>();
				//mainList = ((IHydraulicMachine) t).getNetwork(ForgeDirection.UP).getMachines();
				mainList.add((IHydraulicMachine)t);
				mainList = ((IHydraulicMachine)t).getHandler().getConnectedBlocks(mainList);
				
				
				for (IHydraulicMachine machineEntity : mainList) {
					machineEntity.getHandler().setStored(0, isOil);
				}
				
				setFluidInSystem(mainList, newFluidInSystem, isOil);
			}
		}
	}
	
	public static void checkAndFillSideBlocks(World w, int x, int y, int z){
		if(!w.isRemote){
			TileEntity t = w.getBlockTileEntity(x, y, z);
			if(t instanceof IHydraulicMachine || t instanceof TileMultipart){
				IHydraulicMachine mEnt;
				
				if(t instanceof TileMultipart && Multipart.hasTransporter((TileMultipart)t)){
					mEnt = (IHydraulicMachine) Multipart.getTransporter((TileMultipart)t);
				}else{
					mEnt = (IHydraulicMachine) t;
				}
				
				List <IHydraulicMachine> mainList = new ArrayList<IHydraulicMachine>();
				mainList.add(mEnt);
				mainList = mEnt.getHandler().getConnectedBlocks(mainList);
				//if(mEnt.getNetwork(ForgeDirection.UP) == null) return;
				//mainList = mEnt.getNetwork(ForgeDirection.UP).getMachines();
				
				//Log.info("Iteration done. " + mainList.size() + " machines found");
				boolean isOil = false;
				int fluidInSystem = 0;
				int totalFluidCapacity = 0;
				for (IHydraulicMachine machineEntity : mainList) {
					if(isOil == false && machineEntity.getHandler().isOilStored()){
						isOil = true;
					}
					fluidInSystem = fluidInSystem + machineEntity.getHandler().getStored(null);
					totalFluidCapacity = totalFluidCapacity + machineEntity.getMaxStorage();
					machineEntity.getHandler().setStored(0, isOil);
				}
				
				setFluidInSystem(mainList, fluidInSystem, isOil);
			}
		}
	}
}
