package pet.minecraft.Hydraulicraft.baseClasses;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Id;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Name;

public class MachineTieredBlockHandler extends ItemBlock {
	private Name[] tNames;
	
	
	public MachineTieredBlockHandler(int blockId, Name[] names) {
		super(blockId);
		
		tNames = names;
		
		setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack){
		return tNames[itemStack.getItemDamage()].unlocalized;
	}
	
	@Override
	public int getMetadata(int damage){
		return damage;
	}
}

