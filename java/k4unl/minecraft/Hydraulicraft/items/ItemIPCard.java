package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.gow.TilePortalBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.List;

public class ItemIPCard extends HydraulicItemBase {

    public ItemIPCard() {

        super(Names.itemIPCard, false);

        setMaxStackSize(1);
        this.setCreativeTab(CustomTabs.tabGOW);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {

        if (!worldIn.isRemote) {
            TileEntity ent = worldIn.getTileEntity(pos);
            if (ent instanceof TilePortalBase) {
                if (stack.getTagCompound() == null) {
                    stack.setTagCompound(new NBTTagCompound());
                }
                NBTTagCompound stackCompound = stack.getTagCompound();
                ((ItemIPCard) HCItems.itemIPCard).setDefaultInfo(stack, "Linked to: " + ((TilePortalBase) ent).getIPString());

                stackCompound.setLong("linked", ((TilePortalBase) ent).getIPLong());
                ((ItemIPCard) HCItems.itemIPCard).setEffect(stack, true);

                stack.setTagCompound(stackCompound);
                return true;
            }
        }

        return true;
    }

    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {

        if (player.isSneaking()) {
            if (itemStack.getTagCompound() == null) {
                itemStack.setTagCompound(new NBTTagCompound());
            }
            ((ItemIPCard) HCItems.itemIPCard).setDefaultInfo(itemStack, "");

            itemStack.getTagCompound().setLong("linked", 0);
            ((ItemIPCard) HCItems.itemIPCard).setEffect(itemStack, false);
        }
        return itemStack;
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {

        super.addInformation(itemStack, player, list, par4);
        if (itemStack.getTagCompound() != null) {
            if (itemStack.getTagCompound().getLong("linked") != 0) {
                if (Hydraulicraft.ipList.getLocation(itemStack.getTagCompound().getLong("linked")) != null) {
                    list.add(Hydraulicraft.ipList.getLocation(itemStack.getTagCompound().getLong("linked")).print());
                } else {
                    list.add("Invalid target location");
                }
            }
        }
    }


}
