package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

public class ItemDrill extends ItemTool {
    private ItemPickaxe pickaxe;
    private ItemSpade   shovel;

    protected ItemDrill() {
        super(1.0f, ToolMaterial.EMERALD, null); // to make the super() happy
        pickaxe = new DrillPickaxe();
        shovel = new DrillShovel();
        setCreativeTab(CustomTabs.tabHydraulicraft);
        setUnlocalizedName(Names.itemDrill.unlocalized);
        setHarvestLevel("shovel", 3);
        setHarvestLevel("pickaxe", 3);
    }

    @Override
    public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {
        return pickaxe.canHarvestBlock(par1Block, itemStack) || shovel.canHarvestBlock(par1Block, itemStack);
    }

    @Override
    public float func_150893_a(ItemStack stack, Block block) {
        return Math.max(pickaxe.func_150893_a(stack, block), shovel.func_150893_a(stack, block));
    }

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
