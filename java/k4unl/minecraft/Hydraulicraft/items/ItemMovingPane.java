package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileMovingPane;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemMovingPane extends HydraulicItemBase {

    public ItemMovingPane() {
        super(Names.blockMovingPane);
    }

    // Called when a player right-clicks with this item in his hand
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        // Prevents itemstack from decreasing when in creative mod
        if (!playerIn.capabilities.isCreativeMode) {
            --stack.stackSize;
        }

        // Prevents from making changes in inactive world
        if (!worldIn.isRemote) {
            // Increases y coordinate, so our block will be placed on top of the
            // block you clicked, just as it should be
            pos = pos.offset(side);
            EnumFacing s = playerIn.getHorizontalFacing();

            EnumFacing dir = EnumFacing.UP;
            int i = 0;
            while (!(worldIn.getBlockState(pos.offset(dir)).getBlock() == Blocks.air)) {
                dir = dir.rotateAround(s.getAxis());
                i++;
                if (i == 4) {
                    return false;
                }
            }

            // If the check was successful
            worldIn.setBlockState(pos, HCBlocks.movingPane.getDefaultState());
            TileMovingPane tilePane = (TileMovingPane) worldIn.getTileEntity(pos);
            if (tilePane != null) {
                tilePane.setChildLocation(new Location(pos.offset(dir)));
                tilePane.setPaneFacing(s);
                tilePane.setFacing(dir);
            }

            worldIn.setBlockState(pos.offset(dir), HCBlocks.movingPane.getDefaultState());
            tilePane = (TileMovingPane) worldIn.getTileEntity(pos.offset(dir));
            if (tilePane != null) {
                tilePane.setIsPane(true);
                tilePane.setParentLocation(new Location(pos));
                tilePane.setPaneFacing(s);
                tilePane.setFacing(dir);
            }
            return true;
        }
        return false;
    }

}
