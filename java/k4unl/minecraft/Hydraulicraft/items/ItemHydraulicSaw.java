package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.PressurizableItem;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

public class ItemHydraulicSaw extends ItemHydraulicTool {

    private static final float SPEED_ON_LEAVES = 6f;

    private ItemAxe axe;

    public ItemHydraulicSaw() {

        super(1.0f, ToolMaterial.DIAMOND, null); // to make the super() happy
        axe = new SawAxe();
        setCreativeTab(CustomTabs.tabHydraulicraft);
        setUnlocalizedName(Names.itemSaw.unlocalized);
        setHarvestLevel("axe", 3);

        pressurizableItem = new PressurizableItem(MAX_PRESSURE, FLUID_CAPACITY);
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        return axe.canHarvestBlock(state, stack) && super.canHarvestBlock(state, stack);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        if (super.getStrVsBlock(stack, state) == 0)
            return 0;

        if (state.getMaterial() == Material.leaves)
            return SPEED_ON_LEAVES;

        return axe.getStrVsBlock(stack, state);
    }

    /* Helper classes */
    private static class SawAxe extends ItemAxe {

        public SawAxe() {

            super(ToolMaterial.DIAMOND);
        }
    }

}
