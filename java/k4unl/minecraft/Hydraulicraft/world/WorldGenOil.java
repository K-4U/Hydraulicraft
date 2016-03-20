package k4unl.minecraft.Hydraulicraft.world;


import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenOil {

    public void generate(World world, Random rand, int middleX, int middleZ, int minY, int maxY) {

        int worldHeight = HCWorldGenerator.getTopGroundBlock(world, middleX, middleZ);
        if (minY <= 0) {
            minY = 1;
        }
        //int worldHeight = getTopGroundBlock(world, middleX, middleZ);
        //Log.info(middleX + " " + middleZ + " WH: " + worldHeight);
        int radius = rand.nextInt(6) + 2;
        int middleY = minY + (rand.nextInt(maxY - minY));
        maxY = middleY + radius;

        Block toPlace = Fluids.fluidOil.getBlock();
        for (int x = 0; x < radius; x++) {
            for (int y = 0; y < radius; y++) {
                for (int z = 0; z < radius; z++) {
                    if (x * x + y * y + z * z < radius * radius) {

                        world.setBlockState(new BlockPos(middleX + x, middleY + y, middleZ + z), toPlace.getDefaultState(), 0);
                        world.setBlockState(new BlockPos(middleX + x, middleY + y, middleZ - z), toPlace.getDefaultState(), 0);
                        world.setBlockState(new BlockPos(middleX + x, middleY - y, middleZ + z), toPlace.getDefaultState(), 0);
                        world.setBlockState(new BlockPos(middleX + x, middleY - y, middleZ - z), toPlace.getDefaultState(), 0);
                        world.setBlockState(new BlockPos(middleX - x, middleY + y, middleZ + z), toPlace.getDefaultState(), 0);
                        world.setBlockState(new BlockPos(middleX - x, middleY + y, middleZ - z), toPlace.getDefaultState(), 0);
                        world.setBlockState(new BlockPos(middleX - x, middleY - y, middleZ + z), toPlace.getDefaultState(), 0);
                        world.setBlockState(new BlockPos(middleX - x, middleY - y, middleZ - z), toPlace.getDefaultState(), 0);
                    }
                }
            }
        }

        for (int i = maxY - 1; i <= worldHeight + HCConfig.INSTANCE.getInt("oilSpoutSize", "worldgen"); i++) {
            world.setBlockState(new BlockPos(middleX, i, middleZ), toPlace.getDefaultState());
        }
    }
}
