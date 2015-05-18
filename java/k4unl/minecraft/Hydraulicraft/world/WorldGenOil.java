package k4unl.minecraft.Hydraulicraft.world;


import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.chunk.Chunk;

import java.util.Random;

public class WorldGenOil {

    public void generate(World world, Random rand, int middleX, int middleZ, int minY, int maxY) {
        int worldHeight = getTopGroundBlock(world, middleX, middleZ);
        Log.info(middleX + " " + middleZ + " WH: " + worldHeight);
        int radius = maxY - minY;//TODO: Some random magic on this.
        radius = Math.min(radius, 10);
        int middleY = minY + (radius/2);


        for (int x = 0; x < radius; x++) {
            for (int y = 0; y < radius; y++) {
                for (int z = 0; z < radius; z++) {
                    if (x * x + y * y + z * z < radius * radius) {
                        world.setBlock(middleX + x, middleY + y, middleZ + z, Fluids.fluidOilBlock, 0, 2);
                        world.setBlock(middleX + x, middleY + y, middleZ - z, Fluids.fluidOilBlock, 0, 2);
                        world.setBlock(middleX + x, middleY - y, middleZ + z, Fluids.fluidOilBlock, 0, 2);
                        world.setBlock(middleX + x, middleY - y, middleZ - z, Fluids.fluidOilBlock, 0, 2);
                        world.setBlock(middleX - x, middleY + y, middleZ + z, Fluids.fluidOilBlock, 0, 2);
                        world.setBlock(middleX - x, middleY + y, middleZ - z, Fluids.fluidOilBlock, 0, 2);
                        world.setBlock(middleX - x, middleY - y, middleZ + z, Fluids.fluidOilBlock, 0, 2);
                        world.setBlock(middleX - x, middleY - y, middleZ - z, Fluids.fluidOilBlock, 0, 2);
                    }
                }
            }
        }

        for(int i = 0; i <= worldHeight; i++){
            world.setBlock(middleX, i, middleZ, Fluids.fluidOilBlock);
        }

    }


    /*!
     * @author TTFTCUTS
     */
    public static int getTopGroundBlock(World world, int x, int z) {
        Chunk chunk = world.getChunkFromBlockCoords(x, z);
        int y = chunk.getTopFilledSegment() + 15;
        int cx = x & 15;

        for (int cz = z & 15; y > 0; --y)
        {
            Block block = chunk.getBlock(cx, y, cz);

            if (block.getMaterial().blocksMovement() &&
              block.getMaterial() != Material.leaves &&
              block.getMaterial() != Material.wood &&
              block.getMaterial() != Material.gourd &&
              block.getMaterial() != Material.ice &&
              !block.isFoliage(world, x, y, z) &&
              block.isBlockNormalCube())
            {
                return y + 1;
            }
        }

        return -1;
    }
}
