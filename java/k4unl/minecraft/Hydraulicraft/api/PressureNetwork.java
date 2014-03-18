package k4unl.minecraft.Hydraulicraft.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Location;
import k4unl.minecraft.Hydraulicraft.multipart.Multipart;
import k4unl.minecraft.Hydraulicraft.multipart.PartHose;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;
import codechicken.multipart.TileMultipart;

public class PressureNetwork {
	public static class networkEntry{
		private Location blockLocation;
		private ForgeDirection from;
		
		public networkEntry(Location nLocation, ForgeDirection nFrom){
			blockLocation = nLocation;
			from = nFrom;
		}
		
		public Location getLocation(){
			return blockLocation;
		}
		
		public ForgeDirection getFrom(){
			return from;
		}
	}
	private float pressure = 0;
	
	private List<networkEntry> machines;
	private int randomNumber = 0;
	private IBlockAccess world;
	private int fluidInNetwork = 0;
	private int fluidCapacity = 0;
	private boolean isOilStored = false;
	private int lowestTier = -1;
	
	
	public PressureNetwork(IHydraulicMachine machine, float beginPressure, ForgeDirection from){
		randomNumber = new Random().nextInt();
		machines = new ArrayList<networkEntry>();
		machines.add(new networkEntry(machine.getHandler().getBlockLocation(), from));
		pressure = beginPressure;
		world = machine.getHandler().getWorld();
		isOilStored = machine.getHandler().isOilStored();
		float maxPressure = machine.getMaxPressure(isOilStored, from);
		if(isOilStored){
			if(Float.compare(maxPressure, Constants.MAX_MBAR_OIL_TIER_1) == 0){
				lowestTier = 0;
			}else if(Float.compare(maxPressure, Constants.MAX_MBAR_OIL_TIER_2) == 0){
				lowestTier = 1;
			}else if(Float.compare(maxPressure, Constants.MAX_MBAR_OIL_TIER_3) == 0){
				lowestTier = 2;
			} 
		}else{
			if(Float.compare(maxPressure, Constants.MAX_MBAR_WATER_TIER_1) == 0){
				lowestTier = 0;
			}else if(Float.compare(maxPressure, Constants.MAX_MBAR_WATER_TIER_2) == 0){
				lowestTier = 1;
			}else if(Float.compare(maxPressure, Constants.MAX_MBAR_WATER_TIER_3) == 0){
				lowestTier = 2;
			} 
		}
	}
	
	public int getRandomNumber(){
		return randomNumber;
	}
	
	public int getLowestTier(){
		return lowestTier;
	}
	
	private int contains(IHydraulicMachine machine){
		int i = 0;
		for(i=0; i< machines.size(); i++){
			if(machines.get(i).getLocation().equals(machine.getHandler().getBlockLocation())){
				return i;
			}
		}
		return -1;
	}
	
	public void addMachine(IHydraulicMachine machine, float pressureToAdd, ForgeDirection from){
		if(contains(machine) == -1){
			float oPressure = pressure * machines.size();
			oPressure += pressureToAdd;
			machines.add(new networkEntry(machine.getHandler().getBlockLocation(), from));
			pressure = oPressure / machines.size();
			machine.getHandler().updateFluidOnNextTick();
			if(world == null){
				world = machine.getHandler().getWorld();
			}
			
			int newestTier = 4;
			isOilStored = machine.getHandler().isOilStored();
			float maxPressure = machine.getMaxPressure(isOilStored, from);
			if(isOilStored){
				if(Float.compare(maxPressure, Constants.MAX_MBAR_OIL_TIER_1) == 0){
					newestTier = 0;
				}else if(Float.compare(maxPressure, Constants.MAX_MBAR_OIL_TIER_2) == 0){
					newestTier = 1;
				}else if(Float.compare(maxPressure, Constants.MAX_MBAR_OIL_TIER_3) == 0){
					newestTier = 2;
				} 
			}else{
				if(Float.compare(maxPressure, Constants.MAX_MBAR_WATER_TIER_1) == 0){
					newestTier = 0;
				}else if(Float.compare(maxPressure, Constants.MAX_MBAR_WATER_TIER_1) == 0){
					newestTier = 1;
				}else if(Float.compare(maxPressure, Constants.MAX_MBAR_WATER_TIER_1) == 0){
					newestTier = 2;
				} 
			}
			if(newestTier < lowestTier){
				lowestTier = newestTier;
			}
			
		}
	}
	
	public void removeMachine(IHydraulicMachine machineToRemove){
		int machineIndex = contains(machineToRemove);
		if(machineIndex != -1){
			machineToRemove.setNetwork(machines.get(machineIndex).getFrom(), null);
			machines.remove(machineIndex);
		}
		//And tell every machine in the block to recheck it's network! :D
		//Note, this might cost a bit of time..
		//There should be a better way to do this..
		for(networkEntry entry : machines){
			Location loc = entry.getLocation();
			TileEntity ent = world.getBlockTileEntity(loc.getX(), loc.getY(), loc.getZ());
			if(ent instanceof IHydraulicMachine){
				IHydraulicMachine machine = (IHydraulicMachine) ent;
				machine.setNetwork(entry.getFrom(), null);
				machine.getHandler().updateNetworkOnNextTick(getPressure());
			}else if(ent instanceof TileMultipart && Multipart.hasTransporter((TileMultipart)ent)){
				IHydraulicMachine machine = Multipart.getTransporter((TileMultipart) ent);
				machine.setNetwork(entry.getFrom(), null);
				machine.getHandler().updateNetworkOnNextTick(getPressure());
			}
		}
		
	}
	
	public float getPressure(){
		return pressure;
	}
	
	public void setPressure(float newPressure){
		pressure = newPressure;
	}
	
	public boolean getIsOilStored(){
		return isOilStored;
	}
	
	public List<networkEntry> getMachines(){
		return machines;
	}
	
	public void mergeNetwork(PressureNetwork toMerge){
		//Log.info("Trying to merge network " + toMerge.getRandomNumber() + " into " + getRandomNumber());
		if(toMerge.equals(this)) return;
		
		float newPressure = ((pressure - toMerge.getPressure()) / 2) + toMerge.getPressure();
		setPressure(newPressure);

		List<networkEntry> otherList = toMerge.getMachines();
		
		for(networkEntry entry : otherList){
			Location loc = entry.getLocation();
			TileEntity ent = world.getBlockTileEntity(loc.getX(), loc.getY(), loc.getZ());
			if(ent instanceof IHydraulicMachine){
				IHydraulicMachine machine = (IHydraulicMachine) ent;
				machine.setNetwork(entry.getFrom(), this);
				this.addMachine(machine, newPressure, entry.getFrom());
			}else if(ent instanceof TileMultipart && Multipart.hasTransporter((TileMultipart)ent)){
				IHydraulicMachine machine = Multipart.getTransporter((TileMultipart) ent);
				machine.setNetwork(entry.getFrom(), this);
				this.addMachine(machine, newPressure, entry.getFrom());
			}
		}
		
		//Log.info("Merged network " + toMerge.getRandomNumber() + " into " + getRandomNumber());
	}

	public void writeToNBT(NBTTagCompound tagCompound) {
		tagCompound.setFloat("pressure", pressure);
	}

	public void readFromNBT(NBTTagCompound tagCompound) {
		pressure = tagCompound.getFloat("pressure");
	}
	
	public static PressureNetwork getNetworkInDir(IBlockAccess iba, int x, int y, int z, ForgeDirection dir){
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
			PressureNetwork foundNetwork = null;
			if(isMultipart){
				if(((PartHose)mEnt).isConnectedTo(dir)){
					int xn = x + dir.offsetX;
					int yn = y + dir.offsetY;
					int zn = z + dir.offsetZ;
					TileEntity tn = iba.getBlockTileEntity(xn, yn, zn);
					
					if(tn instanceof IHydraulicMachine){
						if(((IHydraulicMachine)tn).canConnectTo(dir.getOpposite())){
							foundNetwork = ((IHydraulicMachine)tn).getNetwork(dir.getOpposite());	
						}
					}else if(tn instanceof TileMultipart && Multipart.hasTransporter((TileMultipart)tn)){
						if(Multipart.getTransporter((TileMultipart)tn).isConnectedTo(dir.getOpposite())){
							foundNetwork = ((IHydraulicMachine)Multipart.getTransporter((TileMultipart)tn)).getNetwork(dir.getOpposite());
						}
					}					
				}
			}else{
				int xn = x + dir.offsetX;
				int yn = y + dir.offsetY;
				int zn = z + dir.offsetZ;
				TileEntity tn = iba.getBlockTileEntity(xn, yn, zn);
				if(tn instanceof TileMultipart && Multipart.hasTransporter((TileMultipart)tn)){
					if(Multipart.getTransporter((TileMultipart)tn).isConnectedTo(dir.getOpposite())){
						foundNetwork = ((IHydraulicMachine)Multipart.getTransporter((TileMultipart)tn)).getNetwork(dir.getOpposite());
					}
				}
			}
			return foundNetwork;
		}else{
			return null;
		}
	}
	
	public static PressureNetwork getNearestNetwork(IBlockAccess iba, int x, int y, int z){
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
			PressureNetwork newNetwork = null;
			PressureNetwork foundNetwork = null;
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
					if(tn instanceof TileMultipart && Multipart.hasTransporter((TileMultipart)tn)){
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
	
	public int getFluidCapacity(){
		return fluidCapacity;
	}
	
	public int getFluidInNetwork(){
		return fluidInNetwork;
	}
	
	public void updateFluid(IHydraulicMachine mEnt){
		if(mEnt.getHandler().isOilStored()){
			isOilStored = true;
		}else{
			isOilStored = false;
		}
		fluidInNetwork = 0;
		fluidCapacity = 0;
		
		List<IHydraulicMachine> mainList = new ArrayList<IHydraulicMachine>();
		
		for (networkEntry entry : machines) {
			Location loc = entry.getLocation();
			TileEntity ent = world.getBlockTileEntity(loc.getX(), loc.getY(), loc.getZ());
			IHydraulicMachine machine = null;
			if(ent instanceof IHydraulicMachine){
				machine = (IHydraulicMachine) ent;
			}else if(ent instanceof TileMultipart && Multipart.hasTransporter((TileMultipart)ent)){
				machine = Multipart.getTransporter((TileMultipart) ent);
			}
			
			if(machine != null){
				if((getIsOilStored() && machine.getHandler().isOilStored()) || (!getIsOilStored() && !machine.getHandler().isOilStored() ) || machine.getHandler().getStored() == 0){ //Otherwise we would be turning water into oil
					fluidInNetwork = fluidInNetwork + machine.getHandler().getStored();
					fluidCapacity = fluidCapacity + machine.getMaxStorage();
					machine.getHandler().setStored(0, isOilStored, false);
					mainList.add(machine);
				}
			}
		}
		disperseFluid(mainList);
	}
	
	private void disperseFluid(List<IHydraulicMachine> mainList){
		List<IHydraulicMachine> remainingBlocks = new ArrayList<IHydraulicMachine>();
		float newFluidInSystem = 0;
		boolean firstIteration = true;
		float fluidInSystem = fluidInNetwork;
		//Log.info("Before iteration: FIS = " + fluidInSystem + " M = " + mainList.size());
		while(fluidInSystem > 0){
			if(mainList.size() == 0){
				//Error!
				Log.error("Too much fluid in the system!");
				break;
			}
			float toSet = (float)fluidInSystem / (float)mainList.size();
			
			for (IHydraulicMachine machineEntity : mainList) {
				if(machineEntity.getMaxStorage() < (toSet + machineEntity.getHandler().getStored())){
					//This machine can't store this much!
					newFluidInSystem = newFluidInSystem + ((toSet + machineEntity.getHandler().getStored()) - machineEntity.getMaxStorage());
					machineEntity.getHandler().setStored(machineEntity.getMaxStorage(), isOilStored, false);
				}else{
					remainingBlocks.add(machineEntity);
					machineEntity.getHandler().setStored((int)toSet + machineEntity.getHandler().getStored(), isOilStored, false);
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
	
}
