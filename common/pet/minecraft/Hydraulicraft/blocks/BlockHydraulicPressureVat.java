package pet.minecraft.Hydraulicraft.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicPressureVat;
import pet.minecraft.Hydraulicraft.baseClasses.MachineBlock;
import pet.minecraft.Hydraulicraft.lib.config.Ids;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class BlockHydraulicPressureVat extends MachineBlock {

	protected BlockHydraulicPressureVat() {
		super(Ids.blockHydraulicPressureVat, Names.blockHydraulicPressurevat);
		hasTopIcon = true;
		hasBottomIcon = true;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		// TODO Auto-generated method stub
		return new TileHydraulicPressureVat();
	}
}
