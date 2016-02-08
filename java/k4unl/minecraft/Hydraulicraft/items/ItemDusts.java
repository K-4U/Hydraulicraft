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

    class dust {
        private String _name;
        private int meta;
        private boolean visible;

        public dust(String targetName, int meta) {

            _name = targetName;
            this.meta = meta;
        }

        public void setName(String n) {

            _name = n;
        }

        public String getName() {

            return _name;
        }

        public int getMeta() {

            return meta;
        }

        public void setMeta(int meta) {

            this.meta = meta;
        }

        public boolean isVisible() {

            return visible;
        }

        public void setVisible(boolean visible) {

            this.visible = visible;
        }
    }


    private List<dust> dusts = new ArrayList<dust>();

    public ItemDusts() {

        super();

        setMaxStackSize(64);
        setUnlocalizedName(Names.itemDust.unlocalized);

        setCreativeTab(CustomTabs.tabHydraulicraft);

        setHasSubtypes(true);
    }

    public int addDust(String oreDictName, int meta) {
        //Log.info("Adding dust " + oreDictName + " on " + meta);
        dusts.add(meta, new dust(oreDictName, meta));

        return meta;
    }

    public void showDust(String oreDictName) {

        for (dust dust : dusts) {
            if(dust.getName().equals(oreDictName)){
                dust.setVisible(true);
                String ingotName = "ingot" + oreDictName;
                ItemStack ingotTarget = Functions.getIngot(ingotName);
                FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(this, 1, dust.getMeta()),
                  ingotTarget, 0F);

                OreDictionary.registerOre("dust" + oreDictName,
                        new ItemStack(HCItems.itemDust, 1, dust.getMeta()));
            }
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {

        return "dust" + dusts.get(itemStack.getItemDamage()).getName();
    }

    public String getUnlocalizedName(int metadata) {

        return "dust" + dusts.get(metadata).getName();
    }


    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {

        for (int i = 0; i < dusts.size(); i++) {
            if(getDust(i).isVisible()) {
                list.add(new ItemStack(this, 1, i));
            }
        }
    }

    public dust getDust(int id){
        return dusts.get(id);
    }

    public List<dust> getChunks() {
        return dusts;
    }

}
