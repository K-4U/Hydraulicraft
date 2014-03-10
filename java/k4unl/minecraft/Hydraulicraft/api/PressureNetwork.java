package k4unl.minecraft.Hydraulicraft.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import codechicken.multipart.TileMultipart;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Location;
import k4unl.minecraft.Hydraulicraft.multipart.Multipart;
import k4unl.minecraft.Hydraulicraft.multipart.PartHose;
import net.minecraft.block.BlockLockedChest;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

public class PressureNetwork {
	private float pressure = 0;
	
	private List<IHydraulicMachine> machines;
	private int randomNumber = 0;
	
	
	public PressureNetwork(IHydraulicMachine machine, float beginPressure){
		randomNumber = new Random().nextInt();
		machines = new ArrayList<IHydraulicMachine>();
		machines.add(machine);
		pressure = beginPressure;
	}
	
	public int getRandomNumber(){
		return randomNumber;
	}
	
	public void addMachine(IHydraulicMachine machine, float pressureToAdd){
		if(!machines.contains(machine.getHandler().getBlockLocation())){
			float oPressure = pressure * machines.size();
			oPressure += pressureToAdd;
			machines.add(machine);
			pressure = oPressure / machines.size();
			machine.getHandler().updateFluidOnNextTick();
		}
	}
	
	public void removeMachine(IHydraulicMachine machineToRemove){
		if(machines.contains(machineToRemove.getHandler().getBlockLocation())){
			machines.remove(machineToRemove.getHandler().getBlockLocation());
		}
		//And tell every machine in the block to recheck it's network! :D
		//Note, this might cost a bit of time..
		//There should be a better way to do this..
		for(IHydraulicMachine machine : machines){
			machine.setNetwork(ForgeDirection.UNKNOWN, null);
			machine.getHandler().updateNetworkOnNextTick(getPressure());
		}
		
	}
	
	public float getPressure(){
		return pressure;
	}
	
	public void setPressure(float newPressure){
		pressure = newPressure;
	}
	
	public List<IHydraulicMachine> getMachines(){
		return machines;
	}
	
	public void mergeNetwork(PressureNetwork toMerge){
		Log.info("Trying to merge network " + toMerge.getRandomNumber() + " into " + getRandomNumber());
		if(toMerge.equals(this)) return;
		
		float newPressure = ((pressure - toMerge.getPressure()) / 2) + toMerge.getPressure();
		setPressure(newPressure);

		for(IHydraulicMachine machine : toMerge.getMachines()){
			machine.setNetwork(ForgeDirection.UNKNOWN, this);
			this.addMachine(machine, newPressure);
		}
		
		Log.info("Merged network " + toMerge.getRandomNumber() + " into " + getRandomNumber());
	}

	public void writeToNBT(NBTTagCompound tagCompound) {
		tagCompound.setFloat("pressure", pressure);
	}

	public void readFromNBT(NBTTagCompound tagCompound) {
		pressure = tagCompound.getFloat("pressure");
	}
	
	public void disperseFluid(){
		
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
	
	
}
