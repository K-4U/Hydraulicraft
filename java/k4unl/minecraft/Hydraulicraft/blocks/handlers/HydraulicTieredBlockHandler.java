package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.IMultiTieredBlock;
import k4unl.minecraft.Hydraulicraft.blocks.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.blocks.ITooltipProvider;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class HydraulicTieredBlockHandler extends HandlerHydraulicBlock {
	private Name[] tNames;
	
	
	public HydraulicTieredBlockHandler(Block block, Name[] names) {
		super(block);
		
		tNames = names;
		
		setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack){
		String unlocalizedName = tNames[itemStack.getItemDamage()].unlocalized;
		if(!unlocalizedName.startsWith("tile.")){
			unlocalizedName = "tile." + unlocalizedName;
		}
		return unlocalizedName;
	}
	
	@Override
	public int getMetadata(int damage){
		return damage;
	}

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean par4){
        super.addInformation(itemstack, player, list, par4);
        if(itemstack != null){
            Item theItem  = itemstack.getItem();
            Block btH = ((HandlerHydraulicBlock)theItem).blockToHandle;

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
                        toTranslate = "ERROR";
                        break;
                }
                list.add(EnumChatFormatting.GREEN + Localization.getString(toTranslate));
            }

        }
    }
	
}

