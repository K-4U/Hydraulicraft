package k4unl.minecraft.Hydraulicraft.baseClasses.entities;

import k4unl.minecraft.Hydraulicraft.api.IBaseStorage;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineEntity;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.IFluidHandler;

public class TileStorage extends MachineEntity implements IBaseStorage {

	public TileStorage(TileEntity _target) {
		super(_target);
	}
}
