package k4unl.minecraft.Hydraulicraft.tileEntities.harvester.trolleys;

import java.util.ArrayList;

import k4unl.minecraft.Hydraulicraft.api.IHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

public class TrolleyCrops implements IHarvesterTrolley{
    private static final ResourceLocation resLoc =
            new ResourceLocation(ModInfo.LID,"textures/model/harvesterTrolley_tmap.png");
    
	@Override
	public ArrayList<ItemStack> getHandlingSeeds() {
		return null;
	}

	@Override
	public String getName() {
		return "crops";
	}

	@Override
	public boolean canPlant(IBlockAccess world, int x, int y, int z,
			ItemStack seed) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Block getBlockForSeed(ItemStack seed) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canHarvest(IBlockAccess world, int x, int y, int z) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
    public ResourceLocation getTexture(){
        return resLoc;
    }

}
