package k4unl.minecraft.Hydraulicraft.items;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.Icon;
import net.minecraftforge.oredict.OreDictionary;

public class ItemChunks extends Item {
	class chunk{
		private String _name;
		private Icon _icon;
		
		public chunk(String targetName){
			_name = targetName;
		}
		
		public void setName(String n){
			_name = n;
		}
		public void setIcon(Icon i){
			_icon = i;
		}
		public String getName(){
			return _name;
		}
		public Icon getIcon(){
			return _icon;
		}
	}
	
	
	private List<chunk> chunks = new ArrayList<chunk>();
	
	public ItemChunks() {
		super(Ids.itemChunks.act);
		
		setMaxStackSize(64);
		setUnlocalizedName(Names.itemChunk.unlocalized);
		
		setCreativeTab(CustomTabs.tabHydraulicraft);
		
		setHasSubtypes(true);
	}


    /*!
     * @author Koen Beckers
     * @date 24-12-2013
     * Adds a chunk, immediately registers to ore dictionary and the smelting
     * recipes
     */
	public int addChunk(String metalName){
		chunks.add(new chunk(metalName));
        int subId = chunks.size() - 1;
        OreDictionary.registerOre("chunk" + metalName,
                new ItemStack(Items.itemChunk, 1, subId));

        String ingotName = "ingot" + metalName;
        ItemStack ingotTarget = Functions.getIngot(ingotName);
        FurnaceRecipes.smelting().addSmelting(this.itemID, subId,
                ingotTarget, 0);

		return subId;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack){
		return "chunk" + chunks.get(itemStack.getItemDamage()).getName();
	}
	
	@Override	
	public String getItemDisplayName(ItemStack itemStack){
		return Localization.getString(Localization.CHUNK_ENTRY, chunks.get(itemStack.getItemDamage()).getName());
	}
	
	@Override
	public void registerIcons(IconRegister icon){
		for (chunk c : chunks) {
			c.setIcon(icon.registerIcon(ModInfo.LID + ":" + "chunk" + c.getName()));
		}
	}
	
	@Override
	public Icon getIconFromDamage(int damage){
		if(chunks.get(damage) != null){
			return chunks.get(damage).getIcon();
		}
		return null;
	}
	
	@Override
	public void getSubItems(int id, CreativeTabs tab, List list){
		for(int i = 0; i < chunks.size(); i++){
			list.add(new ItemStack(this,1,i));
		}
	}
	
}
