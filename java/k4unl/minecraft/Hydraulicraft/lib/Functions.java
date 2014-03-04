package k4unl.minecraft.Hydraulicraft.lib;

import java.util.ArrayList;
import java.util.List;

import codechicken.multipart.TileMultipart;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.api.IPressureNetwork;
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
				//Log.error("Too much fluid in the system!");
				break;
			}
			int toSet = fluidInSystem / mainList.size();
			while(fluidInSystem > toSet * mainList.size()){
				fluidInSystem -= 1;
				toSet = fluidInSystem / mainList.size();
			}
			
			for (IHydraulicMachine machineEntity : mainList) {
				if(machineEntity.getMaxStorage() < (toSet + machineEntity.getHandler().getStored())){
					newFluidInSystem = newFluidInSystem + ((toSet + machineEntity.getHandler().getStored()) - machineEntity.getMaxStorage());
					machineEntity.getHandler().setStored(machineEntity.getMaxStorage(), isOil);
				}else{
					remainingBlocks.add(machineEntity);
					machineEntity.getHandler().setStored(toSet + machineEntity.getHandler().getStored(), isOil);
				}
				
				if(firstIteration){
					machineEntity.getHandler().setFluidInSystem(fluidInSystem);
				}
				
				//Log.info("Is this the original? " + machineEntity.equals(t));
				
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
				mainList.add((IHydraulicMachine) t);
				mainList = ((IHydraulicMachine) t).getHandler().getConnectedBlocks(mainList);
				
				for (IHydraulicMachine machineEntity : mainList) {
					machineEntity.getHandler().setStored(0, isOil);
				}
				
				setFluidInSystem(mainList, newFluidInSystem, isOil);
				
				//Log.info("Done iterating. Found " + mainList.size() + " blocks!");
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
				mainList = (mEnt).getHandler().getConnectedBlocks(mainList);
				
				//Log.info("Iteration done. " + mainList.size() + " machines found");
				boolean isOil = false;
				int fluidInSystem = 0;
				int totalFluidCapacity = 0;
				float pressureInSystem = 0;
				int oldMachineCount = 0;
				float generating = 0;
				int generators = 0;
				for (IHydraulicMachine machineEntity : mainList) {
					if(oldMachineCount == 0){
						oldMachineCount = machineEntity.getHandler().getNetworkCount();
					}
					if(isOil == false && machineEntity.getHandler().isOilStored()){
						isOil = true;
					}
					fluidInSystem = fluidInSystem + machineEntity.getHandler().getStored();
					totalFluidCapacity = totalFluidCapacity + machineEntity.getMaxStorage();
					machineEntity.getHandler().setStored(0, isOil);
					
					
					//if(machineEntity.getHandler().getPressure() > pressureInSystem){
					if(machineEntity.getHandler().getPressure() > 0.0F){
						//pressureInSystem+= machineEntity.getHandler().getPressure();
					}
					//}
					//machineEntity.getHandler().setPressure(0);
				}
				
				
				/*
				if(fluidInSystem < 10000 && fluidInSystem > 1){
					pressureInSystem = pressureInSystem * ((float)fluidInSystem / 100F);
				}*/
				//pressureInSystem = pressureInSystem / mainList.size();
				//Log.info("Fluid in system: " + fluidInSystem);
				
				
				
				//Log.info("Pressure in system: " + pressureInSystem);
				//We only have to set the pressure once..
				//Saves us on calls
				boolean firstPressureSet = false;
				for (IHydraulicMachine machineEntity : mainList) {
					machineEntity.getHandler().setIsOilStored(isOil);
					//if(Float.compare(machineEntity.getHandler().getPressure(), 0.0F) == 0){
					if(firstPressureSet == false){
						firstPressureSet = true;
						//machineEntity.getHandler().setPressure(pressureInSystem);
					}
					//}
					machineEntity.getHandler().setNetworkCount(mainList.size());
					machineEntity.getHandler().setTotalFluidCapacity(totalFluidCapacity);
					//This will allow the machines themselves to explode when something goes wrong!
				}
				
				setFluidInSystem(mainList, fluidInSystem, isOil);
				
				//Log.info("Done iterating. Found " + mainList.size() + " blocks!");
			}
		}
	}
	
	
	public static IPressureNetwork getNearestNetwork(IBlockAccess iba, int x, int y, int z){
		TileEntity t = iba.getBlockTileEntity(x, y, z);
		if(t instanceof IHydraulicMachine || t instanceof TileMultipart){
			IHydraulicMachine mEnt;
			boolean isMultipart = false;
			if(t instanceof TileMultipart && Multipart.hasTransporter((TileMultipart)t)){
				mEnt = (IHydraulicMachine) Multipart.getTransporter((TileMultipart)t);
				isMultipart = true;
			}else{
				mEnt = (IHydraulicMachine) t;
			}
			
			List<IHydraulicMachine> machines = new ArrayList<IHydraulicMachine>();
			IPressureNetwork newNetwork = null;
			IPressureNetwork foundNetwork = null;
			for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
				
				if(isMultipart){
					if(((PartHose)mEnt).isConnectedTo(dir)){
						int xn = x + dir.offsetX;
						int yn = y + dir.offsetY;
						int zn = z + dir.offsetZ;
						TileEntity tn = iba.getBlockTileEntity(xn, yn, zn);
						
						if(tn instanceof IHydraulicMachine){
							if(((IHydraulicMachine)tn).canConnectTo(dir.getOpposite())){
								if(foundNetwork == null){
									foundNetwork = ((IHydraulicMachine)tn).getNetwork(dir.getOpposite());	
								}else{
									newNetwork = ((IHydraulicMachine)tn).getNetwork(dir.getOpposite());
								}
								
								//break;
							}
						}else if(tn instanceof TileMultipart && Multipart.hasTransporter((TileMultipart)tn)){
							if(Multipart.getTransporter((TileMultipart)tn).isConnectedTo(dir.getOpposite())){
								if(foundNetwork == null){
									foundNetwork = ((IHydraulicMachine)Multipart.getTransporter((TileMultipart)tn)).getNetwork(dir.getOpposite());
								}else{
									newNetwork = ((IHydraulicMachine)Multipart.getTransporter((TileMultipart)tn)).getNetwork(dir.getOpposite());
								}
								//break;
							}
						}					
					}
				}else{
					int xn = x + dir.offsetX;
					int yn = y + dir.offsetY;
					int zn = z + dir.offsetZ;
					TileEntity tn = iba.getBlockTileEntity(xn, yn, zn);
					if(tn instanceof IHydraulicMachine){
						if(((IHydraulicMachine)tn).canConnectTo(dir.getOpposite())){
							if(foundNetwork == null){
								foundNetwork = ((IHydraulicMachine)tn).getNetwork(dir.getOpposite());	
							}else{
								newNetwork = ((IHydraulicMachine)tn).getNetwork(dir.getOpposite());
							}
							
							//break;
						}
					}else if(tn instanceof TileMultipart && Multipart.hasTransporter((TileMultipart)tn)){
						if(Multipart.getTransporter((TileMultipart)tn).isConnectedTo(dir.getOpposite())){
							if(foundNetwork == null){
								foundNetwork = ((IHydraulicMachine)Multipart.getTransporter((TileMultipart)tn)).getNetwork(dir.getOpposite());
							}else{
								newNetwork = ((IHydraulicMachine)Multipart.getTransporter((TileMultipart)tn)).getNetwork(dir.getOpposite());
							}
							//break;
						}
					}
				}
				if(newNetwork != null && foundNetwork != null){
					//Hmm.. More networks!? What's this!?
					foundNetwork.mergeNetwork(newNetwork);
					newNetwork = null;
				}
			}
			return foundNetwork;
		}else{
			return null;
		}
	}
}
