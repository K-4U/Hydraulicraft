package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Location;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileMovingPane;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemMovingPane extends HydraulicItemBase {

	public ItemMovingPane() {
		super(Names.blockMovingPane);
	}

	// Called when a player right-clicks with this item in his hand
	@Override
	public boolean onItemUse(ItemStack item, EntityPlayer player, World world,
			int x, int y, int z, int side, float xOffset, float yOffset,
			float zOffSet) {
		// Prevents itemstack from decreasing when in creative mod
		if (!player.capabilities.isCreativeMode) {
			--item.stackSize;
		}
		
		// Prevents from making changes in inactive world
		if (!world.isRemote) {
			// Increases y coordinate, so our block will be placed on top of the
			// block you clicked, just as it should be
			x += ForgeDirection.getOrientation(side).offsetX;
			y += ForgeDirection.getOrientation(side).offsetY;
			z += ForgeDirection.getOrientation(side).offsetZ;
			
			//y++;
			int sideToPlace = MathHelper.floor_double(player.rotationYaw / 90F + 0.5D) & 3;
			ForgeDirection s = Functions.getDirectionFromInt(sideToPlace);
			
			boolean canPlace = true;
			//ForgeDirection dir = ForgeDirection.getOrientation(sideToPlace);
			ForgeDirection dir = ForgeDirection.UP;
			int i = 0;
			while(!world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ).equals(Blocks.air)){
				dir = dir.getRotation(s);
				i++;
				if(i == 4){
					return false;
				}
			}
			//canPlace = (world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ).equals(Blocks.air));
			
			
			// If the check was successful
			world.setBlock(x, y, z, HCBlocks.movingPane);
			TileMovingPane tilePane = (TileMovingPane) world.getTileEntity(x, y, z);
			if(tilePane != null){
				tilePane.setChildLocation(new Location(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ));
				tilePane.setPaneFacing(s);
			}
			
			world.setBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, HCBlocks.movingPane);
			tilePane = (TileMovingPane) world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
			if(tilePane != null){
				tilePane.setParentLocation(new Location(x,y,z));
				tilePane.setPaneFacing(s);
				tilePane.setIsPane(true);
			}
			return true;
		}
		return false;
	}

	// This function rotates the relative coordinates accordingly to the given
	// direction
	public int[] rotXZByDir(int x, int y, int z, int dir) {
		if (dir == 0) {
			return new int[] { x, y, z };
		} else if (dir == 1) {
			return new int[] { -z, y, x };
		} else if (dir == 2) {
			return new int[] { -x, y, -z };
		} else {
			return new int[] { z, y, -x };
		}
	}
}
