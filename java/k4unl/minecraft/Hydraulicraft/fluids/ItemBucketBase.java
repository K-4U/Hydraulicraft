package k4unl.minecraft.Hydraulicraft.fluids;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraftforge.fluids.Fluid;

public class ItemBucketBase extends ItemBucket {

    private Block fluidBlock;

    public ItemBucketBase(Fluid fluid) {
        super(fluid.getBlock());
        this.fluidBlock = fluid.getBlock();

        setUnlocalizedName("bucket." + fluid.getUnlocalizedName().substring(6));

        setContainerItem(Items.bucket);
        setCreativeTab(CustomTabs.tabHydraulicraft);
    }

    public Block getFluidBlock() {
        return fluidBlock;
    }
}
