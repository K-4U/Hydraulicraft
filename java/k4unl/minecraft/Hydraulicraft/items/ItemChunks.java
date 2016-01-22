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

public class ItemChunks extends Item {
    public class chunk {
        private String _name;
        private int _meta;

        public chunk(String targetName) {
            _name = targetName;
        }

        public void setName(String n) {
            _name = n;
        }

        public String getName() {
            return _name;
        }

        public void setMeta(int _meta) {
            this._meta = _meta;
        }

        public int getMeta() {
            return _meta;
        }
    }


    private List<chunk> chunks = new ArrayList<chunk>();

    public ItemChunks() {
        super();

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
    public int addChunk(String metalName) {
        chunks.add(new chunk(metalName));
        int subId = chunks.size() - 1;
        OreDictionary.registerOre("chunk" + metalName,
                new ItemStack(HCItems.itemChunk, 1, subId));

        String ingotName = "ingot" + metalName;
        ItemStack ingotTarget = Functions.getIngot(ingotName);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(this, 1, subId),
                ingotTarget, 0.0F);

        return subId;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return "chunk" + chunks.get(itemStack.getItemDamage()).getName();
    }

    /*
    @Override
    public void registerIcons(IIconRegister icon){
        for (chunk c : chunks) {
            c.setIcon(icon.registerIcon(ModInfo.LID + ":" + "chunk" + c.getName()));
        }
    }

    @Override
    public IIcon getIconFromDamage(int damage){
        if(chunks.get(damage) != null){
            return chunks.get(damage).getIcon();
        }
        return null;
    }
    */
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < chunks.size(); i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    public chunk getChunk(int id){
        return chunks.get(id);
    }

    public List<chunk> getChunks() {
        return chunks;
    }

}
