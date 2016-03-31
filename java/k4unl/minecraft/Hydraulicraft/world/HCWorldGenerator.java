package k4unl.minecraft.Hydraulicraft.world;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.ores.Ores;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class HCWorldGenerator implements IWorldGenerator {


    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        switch (world.provider.getDimension()) {
            case -1:
                generateNether(world, random, chunkX * 16, chunkZ * 16);
                break;
            case 0:
                generateOverworld(world, random, chunkX * 16, chunkZ * 16);
                break;
            case 1:
                generateEnd(world, random, chunkX * 16, chunkZ * 16);
                break;
            default:
                generateOverworld(world, random, chunkX * 16, chunkZ * 16);
                break;
        }
    }

    private void generateEnd(World world, Random random, int chunkX, int chunkZ) {
        //Do nothing here, we don't want ores in the end!
    }

    private void generateNether(World world, Random random, int chunkX, int chunkZ) {

        if (HCConfig.INSTANCE.getBool("shouldGenFoxium", "worldgen")) {
            generateOre(Ores.oreFoxium, world, HCConfig.INSTANCE.getInt("foxiumVeinSize", "worldgen"), HCConfig.INSTANCE.getInt
                    ("foxiumVeinCount", "worldgen"), HCConfig.INSTANCE.getInt("foxiumMinY", "worldgen"), HCConfig.INSTANCE.getInt("foxiumMaxY", "worldgen"), random, chunkX, chunkZ, Blocks.netherrack);
        }
    }

    private void generateOre(Block ore, World world, int veinSize, int veinCount, int minY, int maxY, Random random, int chunkX, int chunkZ) {

        WorldGenMinable worldGenMinable = new WorldGenMinable(ore.getDefaultState(), veinSize);
        for (int i = 0; i < veinCount; i++) {
            int firstBlockXCoord = chunkX + random.nextInt(16);
            int firstBlockYCoord = minY + random.nextInt(maxY - minY); // From +18 to 70
            int firstBlockZCoord = chunkZ + random.nextInt(16);

            worldGenMinable.generate(world, random, new BlockPos(firstBlockXCoord, firstBlockYCoord, firstBlockZCoord));
        }
    }

    private void generateOre(Block ore, World world, int veinSize, int veinCount, int minY, int maxY, Random random, int chunkX, int chunkZ, Block toReplace) {
        //TODO: TEST IF THE NETHER GEN STILL WORKS
        WorldGenMinable worldGenMinable = new WorldGenMinable(ore.getDefaultState(), veinSize);//, toReplace);
        for (int i = 0; i < veinCount; i++) {
            int firstBlockXCoord = chunkX + random.nextInt(16);
            int firstBlockYCoord = minY + random.nextInt(maxY - minY); // From +18 to 70
            int firstBlockZCoord = chunkZ + random.nextInt(16);

            worldGenMinable.generate(world, random, new BlockPos(firstBlockXCoord, firstBlockYCoord, firstBlockZCoord));
        }
    }

    public static void generateBeachium(World world, Random random, int x, int z) {
        //Check biome
        BiomeGenBase biome = world.getBiomeGenForCoords(new BlockPos(x, 0, z));
        if (!BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.BEACH)) {
            return;
        }
        if (random.nextDouble() < HCConfig.INSTANCE.getDouble("beachiumChance", "worldgen")) {
            for (int j = 0; j < HCConfig.INSTANCE.getInt("beachiumVeinCount", "worldgen"); j++) {
                int randX = x + random.nextInt(16);
                int randZ = z + random.nextInt(16);
                BlockPos upperBlock = world.getTopSolidOrLiquidBlock(new BlockPos(randX, 0, randZ)).down();
                if (world.getBlockState(upperBlock).getBlock() == Blocks.sand) {
                    world.setBlockState(upperBlock, Ores.oreBeachium.getDefaultState());
                }
            }
        }
    }

    public static void generateNadsiumBicarbinate(World world, Random random, int x, int z) {
        //Check biome
        BiomeGenBase biome = world.getBiomeGenForCoords(new BlockPos(x, 0, z));
        if (!BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.OCEAN) && !BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.RIVER)
                && !BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.BEACH)) {
            return;
        }

        if (random.nextDouble() < HCConfig.INSTANCE.getDouble("nadsiumBicarbinateChance", "worldgen")) {
            for (int j = 0; j < HCConfig.INSTANCE.getInt("nadsiumBicarbinateVeinCount", "worldgen"); j++) {
                int randX = x + random.nextInt(16);
                int randZ = z + random.nextInt(16);
                BlockPos topBlockPos = world.getTopSolidOrLiquidBlock(new BlockPos(randX, 0, randZ)).down();
                Block topBlock = world.getBlockState(topBlockPos).getBlock();
                if (world.getBlockState(topBlockPos.up()).getBlock() == Blocks.water) {
                    if (topBlock == Blocks.gravel) {
                        world.setBlockState(topBlockPos, Ores.oreNadsiumBicarbinate.getDefaultState());
                    } else if (topBlock == Blocks.sand) {
                        if (random.nextDouble() < 0.2)
                            world.setBlockState(topBlockPos, HCBlocks.blockRefinedNadsiumBicarbinate.getDefaultState());
                    }
                }
            }
        }
    }

    private void generateOverworld(World world, Random random, int chunkX, int chunkZ) {

        if (HCConfig.INSTANCE.getBool("shouldGenCopperOre", "worldgen")) {
            generateOre(Ores.oreCopper, world, HCConfig.INSTANCE.getInt("copperVeinSize", "worldgen"), HCConfig.INSTANCE.getInt("copperVeinCount",
                    "worldgen"), HCConfig.INSTANCE.getInt("copperMinY", "worldgen"), HCConfig.INSTANCE.getInt("copperMaxY", "worldgen"), random, chunkX, chunkZ);
        }
        if (HCConfig.INSTANCE.getBool("shouldGenLeadOre", "worldgen")) {
            generateOre(Ores.oreLead, world, HCConfig.INSTANCE.getInt("leadVeinSize", "worldgen"), HCConfig.INSTANCE.getInt("leadVeinCount",
                    "worldgen"), HCConfig.INSTANCE.getInt("leadMinY", "worldgen"), HCConfig.INSTANCE.getInt("leadMaxY", "worldgen"), random, chunkX, chunkZ);
        }
        if (HCConfig.INSTANCE.getBool("shouldGenLonezium", "worldgen")) {
            generateOre(Ores.oreLonezium, world, HCConfig.INSTANCE.getInt("loneziumVeinSize", "worldgen"), HCConfig.INSTANCE.getInt
                    ("loneziumVeinCount", "worldgen"), HCConfig.INSTANCE.getInt("loneziumMinY", "worldgen"), HCConfig.INSTANCE.getInt("loneziumMaxY", "worldgen"), random, chunkX, chunkZ);
        }
        if (HCConfig.INSTANCE.getBool("shouldGenNadsiumBicarbinate", "worldgen")) {
            generateNadsiumBicarbinate(world, random, chunkX, chunkZ);
        }
        if (HCConfig.INSTANCE.getBool("shouldGenBeachium", "worldgen")) {
            generateBeachium(world, random, chunkX, chunkZ);
        }
        if (HCConfig.INSTANCE.getBool("shouldGenOil", "worldgen")) {
            //Log.info("Now genning " + (chunkX * 16) + " " + (chunkZ * 16));
            if (random.nextDouble() < HCConfig.INSTANCE.getDouble("oilChance", "worldgen")) {
                int x = chunkX + 8;
                int z = chunkZ + 8;
                int y = 30 + random.nextInt(40);

                (new WorldGenOil()).generate(world, random, x, z, random.nextInt(20), y);
            }
        }
        if (HCConfig.INSTANCE.getBool("shouldGenRubberTrees", "worldgen")) {
            BiomeGenBase biome = world.getBiomeGenForCoords(new BlockPos(chunkX, 0, chunkZ));
            if (!BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.SWAMP) && !BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.JUNGLE)) {
                return;
            }
            if (random.nextDouble() < HCConfig.INSTANCE.getDouble("rubberTreeChance", "worldgen")) {
                int x = chunkX + 8;
                int z = chunkZ + 8;
                int y = getTopGroundBlock(world, x, z);

                (new WorldGenRubberTree(false)).generate(world, random, new BlockPos(x, y, z));
            }
        }
    }

    /**
     * @author TTFTCUTS
     */
    public static int getTopGroundBlock(World world, int x, int z) {

        Chunk chunk = world.getChunkFromBlockCoords(new BlockPos(x, 0, z));
        int y = chunk.getTopFilledSegment() + 15;
        int cx = x & 15;

        for (int cz = z & 15; y > 0; --y) {
            Block block = chunk.getBlockState(cx, y, cz).getBlock();

            if (block.getMaterial(chunk.getBlockState(cx, y, cz)).blocksMovement() &&
                    block.getMaterial(chunk.getBlockState(cx, y, cz)) != Material.leaves &&
                    block.getMaterial(chunk.getBlockState(cx, y, cz)) != Material.wood &&
                    block.getMaterial(chunk.getBlockState(cx, y, cz)) != Material.gourd &&
                    block.getMaterial(chunk.getBlockState(cx, y, cz)) != Material.ice &&
                    !block.isFoliage(world, new BlockPos(x, y, z))) {
                return y + 1;
            }
        }

        return -1;
    }

}
