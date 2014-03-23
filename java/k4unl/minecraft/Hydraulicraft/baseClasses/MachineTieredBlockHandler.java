package k4unl.minecraft.Hydraulicraft.baseClasses;

import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class MachineTieredBlockHandler extends ItemBlock {
	private Name[] tNames;
	
	
	public MachineTieredBlockHandler(Block block, Name[] names) {
		super(block);
		
		tNames = names;
		
		setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack){
		String unlocalizedName = tNames[itemStack.getItemDamage()].unlocalized;
		if(!unlocalizedName.startsWith("tile.")){
			unlocalizedName = "tile." + unlocalizedName;
		}
		return unlocalizedName;
	}
	
	@Override
	public int getMetadata(int damage){
		return damage;
	}
	
}

