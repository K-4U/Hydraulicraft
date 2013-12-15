package pet.minecraft.Hydraulicraft.TileEntities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidContainerRegistry;
import pet.minecraft.Hydraulicraft.baseClasses.entities.TileTransporter;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Id;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Name;

public class TileHydraulicHose extends TileTransporter {

	@Override
	public void readFromNBT(NBTTagCompound tagCompound){
		super.readFromNBT(tagCompound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		super.writeToNBT(tagCompound);
	}

	@Override
	public int getMaxBar() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStorage() {
		return FluidContainerRegistry.BUCKET_VOLUME * 2;
	}

	@Override
	public int getStored() {
		// TODO Auto-generated method stub
		return 0;
	}
}
