package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.PressurizableItem;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;

public class ItemHydraulicDrill extends ItemHydraulicTool {

    private ItemPickaxe pickaxe;
    private ItemSpade   shovel;

    public ItemHydraulicDrill() {

        super(1.0f, ToolMaterial.DIAMOND, null); // to make the super() happy
        pickaxe = new DrillPickaxe();
        shovel = new DrillShovel();
        setCreativeTab(CustomTabs.tabHydraulicraft);
        setUnlocalizedName(Names.itemDrill.unlocalized);
        setHarvestLevel("shovel", 3);
        setHarvestLevel("pickaxe", 3);

        pressurizableItem = new PressurizableItem(MAX_PRESSURE, FLUID_CAPACITY);
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        return (pickaxe.canHarvestBlock(state, stack) || shovel.canHarvestBlock(state, stack)) &&
                super.canHarvestBlock(state, stack);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        if (super.getStrVsBlock(stack, state) == 0)
            return 0;

        return Math.max(pickaxe.getStrVsBlock(stack, state), shovel.getStrVsBlock(stack, state));
    }

    /* Helper classes */
    private static class DrillPickaxe extends ItemPickaxe {

        public DrillPickaxe() {

            super(ToolMaterial.DIAMOND);
        }
    }

    private static class DrillShovel extends ItemSpade {

        public DrillShovel() {

            super(ToolMaterial.DIAMOND);
        }
    }
}
