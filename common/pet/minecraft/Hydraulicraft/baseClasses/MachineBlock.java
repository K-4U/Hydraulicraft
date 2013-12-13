package pet.minecraft.Hydraulicraft.baseClasses;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Id;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Name;

public class MachineBlock extends BlockContainer {
	private Id tBlockId;
	private Name mName;
	
	protected MachineBlock(Id blockId, Name machineName) {
		super(blockId.act, Material.rock);
		
		tBlockId = blockId;
		mName = machineName;
		
		setUnlocalizedName(mName.unlocalized);
		setStepSound(Block.soundStoneFootstep);
		setHardness(3.5F);
		
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}
}
