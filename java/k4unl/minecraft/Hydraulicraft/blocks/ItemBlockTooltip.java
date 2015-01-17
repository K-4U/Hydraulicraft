package k4unl.minecraft.Hydraulicraft.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by K-4U on 17-1-2015.
 */
public class ItemBlockTooltip extends ItemBlock {

    public Block block;

    public ItemBlockTooltip(Block block) {

        super(block);
        this.block = block;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        super.addInformation(itemStack, p_77624_2_, p_77624_3_, p_77624_4_);

        if(itemStack != null){
            Item theItem  = itemStack.getItem();
            Block btH = ((ItemBlockTooltip)theItem).block;
            p_77624_3_.add(((ITooltipProvider) this.block).getToolTip());
        }
    }


}
