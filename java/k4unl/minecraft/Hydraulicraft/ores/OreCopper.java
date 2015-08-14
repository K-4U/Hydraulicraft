package k4unl.minecraft.Hydraulicraft.ores;

import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import java.util.ArrayList;

public class OreCopper extends OreBase {

	public OreCopper() {
		super(Names.oreCopper);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune){

		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(Ores.oreCopperReplacement));
		return ret;
	}
}
