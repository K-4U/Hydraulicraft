package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.PressurizableItem;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;

public class ItemHydraulicDrill extends ItemHydraulicTool {
    private ItemPickaxe pickaxe;
    private ItemSpade   shovel;

    public ItemHydraulicDrill() {
        super(1.0f, ToolMaterial.EMERALD, null); // to make the super() happy
        pickaxe = new DrillPickaxe();
        shovel = new DrillShovel();
        setCreativeTab(CustomTabs.tabHydraulicraft);
        setUnlocalizedName(Names.itemDrill.unlocalized);
        setHarvestLevel("shovel", 3);
        setHarvestLevel("pickaxe", 3);

        pressurizableItem = new PressurizableItem(MAX_PRESSURE, FLUID_CAPACITY);
    }

    @Override
    public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {
        return (pickaxe.canHarvestBlock(par1Block, itemStack) || shovel.canHarvestBlock(par1Block, itemStack)) &&
                super.canHarvestBlock(par1Block, itemStack);
    }

    @Override
    public float func_150893_a(ItemStack stack, Block block) {
        if (super.func_150893_a(stack, block) == 0)
            return 0;

        return Math.max(pickaxe.func_150893_a(stack, block), shovel.func_150893_a(stack, block));
    }


    /* Helper classes */
    private static class DrillPickaxe extends ItemPickaxe {
        public DrillPickaxe() {
            super(ToolMaterial.EMERALD);
        }
    }

    private static class DrillShovel extends ItemSpade {
        public DrillShovel() {
            super(ToolMaterial.EMERALD);
        }
    }
}
