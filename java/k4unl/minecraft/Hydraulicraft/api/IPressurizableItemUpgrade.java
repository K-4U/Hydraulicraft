package k4unl.minecraft.Hydraulicraft.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IPressurizableItemUpgrade {

   /**
    * Called before an IPressurizableItem with this upgrade is used (can be cancelled in theory?)
    *
    * @param stack the stack
    * @param facing
    * @return whether to allow the item usage
    */
   boolean onBeforeUse(ItemStack stack, World world, BlockPos position, EnumFacing facing);

   /**
    * Called after an item is used with this upgrade
    * @param stack the stack
    * @param world
    * @param position
    * @param facing
    */
   void onAfterUse(ItemStack stack, World world, BlockPos position, EnumFacing facing);

   /**
    * Called to check whether this upgrade can be used on an item
    *
    * @param item the item
    * @return true to allow, false to deny
    */
   boolean canBeAppliedOn(IPressurizableItem item);
}
