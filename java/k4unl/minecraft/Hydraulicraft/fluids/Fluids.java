package k4unl.minecraft.Hydraulicraft.fluids;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;


public class Fluids {

    public static Fluid fluidHydraulicOil;
    public static Fluid fluidOil;
    public static Fluid fluidLubricant;
    public static Fluid fluidFluoroCarbonFluid;
    public static Fluid fluidRubber;
    public static List<ItemBucket> buckets = new ArrayList<ItemBucket>();


    public static void init() {

        fluidHydraulicOil = new FluidHydraulicOil();
        fluidOil = new FluidOil();
        fluidLubricant = new FluidLubricant();
        fluidFluoroCarbonFluid = new FluidFluoroCarbon();
        fluidRubber = new FluidRubber();

        registerFluids();
    }

    /*!
     * @author Koen Beckers
     * @date 13-12-2013
     * Registers the Fluids to the GameRegistry
     */
    public static void registerFluids() {

        registerFluid(fluidHydraulicOil);
        registerFluid(fluidOil);
        registerFluid(fluidLubricant);
        registerFluid(fluidFluoroCarbonFluid);
        registerFluid(fluidRubber);
    }

    private static void registerFluid(Fluid toRegister) {

        Block fluidBlock = toRegister.getBlock();
        GameRegistry.registerBlock(fluidBlock, fluidBlock.getUnlocalizedName().substring(5));

        Item itemBucket = new ItemBucket(fluidBlock).setUnlocalizedName("bucket." + toRegister.getUnlocalizedName().substring(6)).setContainerItem(Items.bucket).setCreativeTab(CustomTabs.tabHydraulicraft);
        buckets.add((ItemBucket) itemBucket);
        GameRegistry.registerItem(itemBucket, itemBucket.getUnlocalizedName().substring(5));

        FluidContainerRegistry.registerFluidContainer(new FluidStack(toRegister, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(itemBucket), new ItemStack(Items.bucket));

    }

}
