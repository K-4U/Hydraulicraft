package k4unl.minecraft.Hydraulicraft.ores;

import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.ArrayList;
import java.util.List;

public class OreLead extends OreBase {

	public OreLead() {
		super(Names.oreLead);
		setHarvestLevel("pickaxe", 2);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune){

		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(Ores.oreLeadReplacement));
		return ret;
	}
}
