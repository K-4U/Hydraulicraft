package k4unl.minecraft.Hydraulicraft.blocks.worldgen;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.world.WorldGenRubberTree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

import java.util.Random;

/**
 * @author Koen Beckers (K-4U)
 */
public class BlockRubberSapling extends BlockBush implements IGrowable {

    public BlockRubberSapling() {

        super(Material.plants);
        setBlockName(Names.blockRubberSapling.unlocalized);
        this.setStepSound(soundTypeGrass);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote) {
            super.updateTick(world, x, y, z, random);

            if (world.getBlockLightValue(x, y + 1, z) >= 9 && random.nextInt(7) == 0) {
                this.func_149879_c(world, x, y, z, random);
            }
        }
    }

    public void func_149879_c(World world, int x, int y, int z, Random random) {
        int l = world.getBlockMetadata(x, y, z);

        if ((l & 8) == 0) {
            world.setBlockMetadataWithNotify(x, y, z, l | 8, 4);
        } else {
            this.growTree(world, x, y, z, random);
        }
    }

    public void growTree(World world, int x, int y, int z, Random random) {
        if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(world, random, x, y, z)) return;
        int l = world.getBlockMetadata(x, y, z) & 7;
        Object object = new WorldGenRubberTree(true);
        int i1 = 0;
        int j1 = 0;
        boolean flag = false;

        Block block = Blocks.air;
        world.setBlock(x, y, z, block, 0, 4);

        if (!((WorldGenerator)object).generate(world, random, x + i1, y, z + j1)) {
            if (flag) {
                world.setBlock(x + i1, y, z + j1, this, l, 4);
                world.setBlock(x + i1 + 1, y, z + j1, this, l, 4);
                world.setBlock(x + i1, y, z + j1 + 1, this, l, 4);
                world.setBlock(x + i1 + 1, y, z + j1 + 1, this, l, 4);
            } else {
                world.setBlock(x, y, z, this, l, 4);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {

    }

    //CanFertilize
    public boolean func_149851_a(World p_149851_1_, int p_149851_2_, int p_149851_3_, int p_149851_4_, boolean p_149851_5_) {
        return true;
    }

    //ShouldFertilize
    public boolean func_149852_a(World p_149852_1_, Random p_149852_2_, int p_149852_3_, int p_149852_4_, int p_149852_5_) {
        return (double)p_149852_1_.rand.nextFloat() < 0.45D;
    }

    //Fertilize
    public void func_149853_b(World p_149853_1_, Random p_149853_2_, int p_149853_3_, int p_149853_4_, int p_149853_5_) {
        this.func_149879_c(p_149853_1_, p_149853_3_, p_149853_4_, p_149853_5_, p_149853_2_);
    }

}
