package k4unl.minecraft.Hydraulicraft.api;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldAccess;

public interface IPressureNetwork {
	public void addMachine(IHydraulicMachine machine);
	
	public void removeMachine(IHydraulicMachine machine);
	
	public float getPressure();
	
	public void setPressure(float newPressure);
	
	public void mergeNetwork(IPressureNetwork toMerge);
	
	public void writeToNBT(NBTTagCompound tagCompound);
	
	public void readFromNBT(NBTTagCompound tagCompound);
	
	public int getRandomNumber();
	
	public List<IHydraulicMachine> getMachines();
}
