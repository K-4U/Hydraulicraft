package pet.minecraft.Hydraulicraft.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import pet.minecraft.Hydraulicraft.lib.CustomTabs;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Id;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Name;

public class ItemChunks extends Item {
	private List<String> chunks = new ArrayList<String>();
	
	
	public ItemChunks(Id _id, Name _name) {
		super(_id.act);
		
		setMaxStackSize(64);
		setUnlocalizedName(_name.unlocalized);
		
		setCreativeTab(CustomTabs.tabHydraulicraft);
		
		setHasSubtypes(true);
	}
	
	public void addChunk(String oreDictName){
		
		chunks.add(oreDictName);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack){
		
		return "";
	}

}
