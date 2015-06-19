package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.api.IMultiTieredBlock;
import k4unl.minecraft.Hydraulicraft.api.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.ITooltipProvider;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class HandlerHydraulicBlock extends ItemBlock {
    protected Block blockToHandle;


    public HandlerHydraulicBlock(Block _blockToHandle) {

        super(_blockToHandle);
        blockToHandle = _blockToHandle;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean par4) {

        if (itemstack != null) {
            Item theItem = itemstack.getItem();
            Block btH = ((HandlerHydraulicBlock) theItem).blockToHandle;

            if (btH instanceof ITieredBlock) {
                String toTranslate = "";
                PressureTier pt = ((ITieredBlock) btH).getTier();
                switch (pt) {
                    case LOWPRESSURE:
                        toTranslate = Localization.MAXPRESSURE_LOW;
                        break;
                    case MEDIUMPRESSURE:
                        toTranslate = Localization.MAXPRESSURE_MEDIUM;
                        break;
                    case HIGHPRESSURE:
                        toTranslate = Localization.MAXPRESSURE_HIGH;
                        break;
                    case INVALID:
                        toTranslate = "";
                        break;
                }
                if(!toTranslate.equals("")) {
                    list.add(EnumChatFormatting.GREEN + Localization.getString(toTranslate));
                }
            }
            if(btH instanceof IMultiTieredBlock){
                String toTranslate = "";
                PressureTier pt = ((IMultiTieredBlock)btH).getTier(itemstack.getItemDamageForDisplay());
                switch(pt){
                    case LOWPRESSURE:
                        toTranslate = Localization.MAXPRESSURE_LOW;
                        break;
                    case MEDIUMPRESSURE:
                        toTranslate = Localization.MAXPRESSURE_MEDIUM;
                        break;
                    case HIGHPRESSURE:
                        toTranslate = Localization.MAXPRESSURE_HIGH;
                        break;
                    case INVALID:
                        toTranslate = "";
                        break;
                }
                if(!toTranslate.equals("")) {
                    list.add(EnumChatFormatting.GREEN + Localization.getString(toTranslate));
                }
            }
            if(btH instanceof ITooltipProvider){
                list.add(EnumChatFormatting.RESET + ((ITooltipProvider)btH).getToolTip());
			}

		}
	}
}
