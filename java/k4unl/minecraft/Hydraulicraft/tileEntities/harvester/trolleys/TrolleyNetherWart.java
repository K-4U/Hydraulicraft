package k4unl.minecraft.Hydraulicraft.tileEntities.harvester.trolleys;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import k4unl.minecraft.Hydraulicraft.api.IHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;

public class TrolleyNetherWart implements IHarvesterTrolley {
	private static final ResourceLocation resLoc =
            new ResourceLocation(ModInfo.LID,"textures/model/harvesterNetherTrolley.png");
	
	@Override
	public String getName() {
		return "netherWart";
	}

	@Override
	public boolean canPlant(World world, int x, int y, int z, ItemStack seed) {
		if(seed.getItem() instanceof IPlantable){
			Block soil = world.getBlock(x, y-1, z);
		    return (world.getFullBlockLightValue(x, y, z) >= 8 ||
		    		world.canBlockSeeTheSky(x, y, z)) &&
		            soil != null && soil.canSustainPlant(world, x, y - 1, z,
		                  ForgeDirection.UP, (IPlantable)seed.getItem());			
		}else{
			return false;
		}
	}

	@Override
	public ArrayList<ItemStack> getHandlingSeeds() {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(Items.nether_wart));
		return ret;
	}
	
	@Override
	public Block getBlockForSeed(ItemStack seed) {
		IPlantable plantingItem = (IPlantable)seed.getItem();
		return plantingItem.getPlant(null, 0, 0, 0);
	}

	@Override
	public boolean canHarvest(World world, int x, int y, int z) {
		for(ItemStack s : getHandlingSeeds()){
			if(world.getBlock(x, y, z).equals(getBlockForSeed(s))){
				if(world.getBlockMetadata(x, y, z) == 7){
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public ResourceLocation getTexture() {
		return resLoc;
	}

	@Override
	public int getPlantHeight(World world, int x, int y, int z) {
		return 1;
	}

}
