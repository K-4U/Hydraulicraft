package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class HydraulicTieredBlockHandler extends HandlerHydraulicBlock {

    private Name[] tNames;


    public HydraulicTieredBlockHandler(Block block, Name[] names) {

        super(block);

        tNames = names;

        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {

        return getUnlocalizedName(itemStack.getItemDamage());
    }

    public String getUnlocalizedName(int meta) {

        if (meta > tNames.length - 1) {
            return "ERROR";
        }
        String unlocalizedName = tNames[meta].unlocalized;
        if (!unlocalizedName.startsWith("tile.")) {
            unlocalizedName = "tile." + unlocalizedName;
        }
        return unlocalizedName;
    }

    @Override
    public int getMetadata(int damage) {

        return damage;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean par4) {

        super.addInformation(itemstack, player, list, par4);
        if (itemstack != null) {
            Item theItem = itemstack.getItem();
            Block btH = ((HandlerHydraulicBlock) theItem).blockToHandle;
            //TODO: Handle me? I think there's something missing here, but no idea what.

        }
    }

}

