package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.util.ResourceLocation;

public class ItemBucketBase extends ItemBucket {

    private Block fluidBlock;

    public ItemBucketBase(Block fluidBlock, Name itemName) {
        super(fluidBlock);
        this.fluidBlock = fluidBlock;

        setUnlocalizedName(itemName.unlocalized);

        setContainerItem((Item) Item.itemRegistry.getObject(new ResourceLocation("bucket")));

        setCreativeTab(CustomTabs.tabHydraulicraft);
    }

    public Block getFluidBlock() {
        return fluidBlock;
    }
}
