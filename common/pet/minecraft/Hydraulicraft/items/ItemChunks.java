package pet.minecraft.Hydraulicraft.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import pet.minecraft.Hydraulicraft.lib.CustomTabs;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Id;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Name;

public class ItemChunks extends Item {
	private Item[] chunks;
	
	
	public ItemChunks(Id _id, Name _name) {
		super(_id.act);
		
		setMaxStackSize(64);
		setUnlocalizedName(_name.unlocalized);
		
		setCreativeTab(CustomTabs.tabHydraulicraft);
	}
	
	public void addChunk(Item item){
		
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack){
		return "";
	}

}
