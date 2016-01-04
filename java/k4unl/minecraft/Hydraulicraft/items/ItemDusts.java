package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class ItemDusts extends Item {
	class dust{
		private String _name;
		//private IIcon _icon;
		
		public dust(String targetName){
			_name = targetName;
		}
		
		public void setName(String n){
			_name = n;
		}
		/*public void setIcon(IIcon i){
			_icon = i;
		}*/
		public String getName(){
			return _name;
		}
		/*public IIcon getIcon(){
			return _icon;
		}*/
	}
	
	
	private List<dust> dusts = new ArrayList<dust>();
	
	public ItemDusts() {
		super();
		
		setMaxStackSize(64);
		setUnlocalizedName(Names.itemDust.unlocalized);
		
		setCreativeTab(CustomTabs.tabHydraulicraft);
		
		setHasSubtypes(true);
	}
	
	public int addDust(String oreDictName, int meta){
		//Log.info("Adding dust " + oreDictName + " on " + meta);
		dusts.add(meta, new dust(oreDictName));

        OreDictionary.registerOre("dust" + oreDictName,
                new ItemStack(HCItems.itemDust, 1, meta));

        String ingotName = "ingot" + oreDictName;
        ItemStack ingotTarget = Functions.getIngot(ingotName);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(this, 1, meta),
                ingotTarget, 0F);

        return meta;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack){
		return "dust" + dusts.get(itemStack.getItemDamage()).getName();
	}
	/*
	@Override
	public void registerIcons(IIconRegister icon){
		for (dust c : dusts) {
			c.setIcon(icon.registerIcon(ModInfo.LID + ":" + "dust" + c.getName()));
		}
	}
	
	@Override
	public IIcon getIconFromDamage(int damage){
		if(dusts.get(damage) != null){
			return dusts.get(damage).getIcon();
		}
		return null;
	}*/
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list){
		for(int i = 0; i < dusts.size(); i++){
			list.add(new ItemStack(this,1,i));
		}
	}
	
}
