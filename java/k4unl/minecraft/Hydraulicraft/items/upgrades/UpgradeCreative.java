package k4unl.minecraft.Hydraulicraft.items.upgrades;

import k4unl.minecraft.Hydraulicraft.api.IPressurizableItem;
import k4unl.minecraft.Hydraulicraft.api.IPressurizableItemUpgrade;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.items.HydraulicItemBase;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class UpgradeCreative extends HydraulicItemBase implements IPressurizableItemUpgrade {
   public UpgradeCreative() {
      super(Names.itemUpgradeCreative, true);
   }

   @Override
   public boolean onBeforeUse(ItemStack stack, World world, BlockPos position, EnumFacing facing) {
      return true;
   }

   @Override
   public void onAfterUse(ItemStack stack, World world, BlockPos position, EnumFacing facing) {
      IPressurizableItem item = (IPressurizableItem) stack.getItem();
      item.setFluid(stack, new FluidStack(Fluids.fluidHydraulicOil, (int) item.getMaxFluid()));
      item.setPressure(stack, item.getMaxPressure());
   }

   @Override
   public boolean canBeAppliedOn(IPressurizableItem item) {
      return true;
   }
}
