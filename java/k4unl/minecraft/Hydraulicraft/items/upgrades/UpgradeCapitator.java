package k4unl.minecraft.Hydraulicraft.items.upgrades;

import k4unl.minecraft.Hydraulicraft.api.IPressurizableItem;
import k4unl.minecraft.Hydraulicraft.api.IPressurizableItemUpgrade;
import k4unl.minecraft.Hydraulicraft.items.HydraulicItemBase;
import k4unl.minecraft.Hydraulicraft.items.ItemHydraulicSaw;
import k4unl.minecraft.Hydraulicraft.lib.UpgradesClass;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class UpgradeCapitator extends HydraulicItemBase implements IPressurizableItemUpgrade {

    public UpgradeCapitator() {

        super(Names.itemUpgradeCapitator, true);
    }

    @Override
    public boolean onBeforeUse(ItemStack stack, World world, BlockPos position, EnumFacing facing) {

        return true; // TODO check for extra pressure
    }

    @Override
    public void onAfterUse(ItemStack stack, World world, BlockPos position, EnumFacing facing) {
        // TODO substract extra pressure
        UpgradesClass.capitate(world, position);
    }

    @Override
    public boolean canBeAppliedOn(IPressurizableItem item) {

        return item instanceof ItemHydraulicSaw;
    }
}
