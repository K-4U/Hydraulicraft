package pet.minecraft.Hydraulicraft.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import pet.minecraft.Hydraulicraft.lib.CustomTabs;
import pet.minecraft.Hydraulicraft.lib.Functions;
import pet.minecraft.Hydraulicraft.lib.config.Ids;
import pet.minecraft.Hydraulicraft.lib.config.ModInfo;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class ItemDusts extends Item {
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
	
	public ItemDusts() {
		super(Ids.itemChunks.act);
		
		setMaxStackSize(64);
		setUnlocalizedName(Names.itemChunk.unlocalized);
		
		setCreativeTab(CustomTabs.tabHydraulicraft);
		
		setHasSubtypes(true);
	}
	
	public int addChunk(String oreDictName){
		chunks.add(new chunk(oreDictName));
		return chunks.size() - 1;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack){
		return chunks.get(itemStack.getItemDamage()).getName();
	}
	
	@Override
	public void registerIcons(IconRegister icon){
		for (chunk c : chunks) {
			c.setIcon(icon.registerIcon(ModInfo.LID + ":" + "chunk" + c.getName()));
		}
	}
	
	public ItemStack getCrushingRecipe(ItemStack itemStack){
		ItemStack ret = null;
		
		
		
		List<String> allowedList = new ArrayList<String>();
		allowedList.add("Gold");
		allowedList.add("Iron");
		allowedList.add("Copper");
		allowedList.add("Lead");
		allowedList.add("Quartz");
		
		//Get oreDictionaryName
		String oreName = itemStack.getUnlocalizedName();
		oreName = oreName.substring("tile.".length());
		String metalName = Functions.getMetalName(oreName);
		if(allowedList.contains(metalName)){
			for(int i = 0; i < chunks.size(); i++){
				String cName = chunks.get(i).getName(); 
				if(cName.equals(metalName)){
					if(metalName.equals("Quartz")){
						return new ItemStack(Item.netherQuartz,3 + ((new Random()).nextFloat() > 0.75F ? 1 : 0));
					}else{
						return new ItemStack(this.itemID, 1, i);
					}
				}
			}
		}
		
		return ret;
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
