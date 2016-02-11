package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.PressurizableItem;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

public class ItemHydraulicSaw extends ItemHydraulicTool {

    private static final float SPEED_ON_LEAVES = 6f;

    private ItemAxe axe;

    public ItemHydraulicSaw() {

        super(1.0f, ToolMaterial.EMERALD, null); // to make the super() happy
        axe = new SawAxe();
        setCreativeTab(CustomTabs.tabHydraulicraft);
        setUnlocalizedName(Names.itemSaw.unlocalized);
        setHarvestLevel("axe", 3);

        pressurizableItem = new PressurizableItem(MAX_PRESSURE, FLUID_CAPACITY);
    }

    @Override
    public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {

        return axe.canHarvestBlock(par1Block, itemStack) && super.canHarvestBlock(par1Block, itemStack);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, Block block) {

        if (super.getStrVsBlock(stack, block) == 0)
            return 0;

        if (block.getMaterial() == Material.leaves)
            return SPEED_ON_LEAVES;

        return axe.getStrVsBlock(stack, block);
    }

    /* Helper classes */
    private static class SawAxe extends ItemAxe {

        public SawAxe() {

            super(ToolMaterial.EMERALD);
        }
    }

}
