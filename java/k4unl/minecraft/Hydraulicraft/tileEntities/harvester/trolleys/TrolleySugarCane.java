package k4unl.minecraft.Hydraulicraft.tileEntities.harvester.trolleys;

import k4unl.minecraft.Hydraulicraft.api.IHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester.BlockHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;


public class TrolleySugarCane implements IHarvesterTrolley {
    private static final ResourceLocation resLoc =
            new ResourceLocation(ModInfo.LID, "textures/model/harvesterSugarCaneTrolley.png");


    @Override
    public String getName() {
        return "sugarCane";
    }

    @Override
    public boolean canHarvest(World world, BlockPos pos) {
        //Check the block underneath too
        //If the block underneath is sugarcane, then harvest, otherwise, don't.
        return world.getBlockState(pos).getBlock() == Blocks.reeds && world.getBlockState(pos.down(1)).getBlock() == Blocks.reeds;
    }

    @Override
    public boolean canPlant(World world, BlockPos pos, ItemStack seed) {
        return Blocks.reeds.canPlaceBlockAt(world, pos);
    }

    @Override
    public ArrayList<ItemStack> getHandlingSeeds() {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(Items.reeds));
        return ret;
    }

    @Override
    public IBlockState getBlockStateForSeed(ItemStack seed) {
        return Blocks.reeds.getDefaultState();
    }

    @Override
    public ResourceLocation getTexture() {
        return resLoc;
    }

    @Override
    public int getPlantHeight(World world, BlockPos pos) {
        pos = pos.up(3);
        if (world.getBlockState(pos).getBlock().getMaterial() == Material.air || world.getBlockState(pos).getBlock() instanceof BlockHarvesterTrolley) {
            return 2;
        } else {
            return 3;
        }
    }

}
