package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import java.util.List;

import k4unl.minecraft.Hydraulicraft.blocks.consumers.misc.BlockHydraulicWaterPump;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HandlerHydraulicBlock extends ItemBlock {
	private Block blockToHandle;
	
	
	public HandlerHydraulicBlock(Block _blockToHandle) {
		super(_blockToHandle);
		blockToHandle = _blockToHandle;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean par4){
		if(itemstack != null){
			Item theItem  = itemstack.getItem();
			Block btH = ((HandlerHydraulicBlock)theItem).blockToHandle;
			if(btH instanceof BlockHydraulicWaterPump){
				list.add("Max: Low pressure");
			}else{
				list.add("Max: High pressure");
			}
		}
	}
}
