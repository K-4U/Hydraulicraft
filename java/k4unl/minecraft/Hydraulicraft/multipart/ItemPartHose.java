package k4unl.minecraft.Hydraulicraft.multipart;


import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import mcmultipart.item.ItemMultiPart;
import mcmultipart.multipart.IMultipart;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ItemPartHose extends ItemMultiPart {

    public ItemPartHose() {

        super();
        setHasSubtypes(true);
        setCreativeTab(CustomTabs.tabHydraulicraft);
        setUnlocalizedName(Names.partHose[0].unlocalized);
    }

    @Override
    public IMultipart createPart(World world, BlockPos blockPos, EnumFacing enumFacing, Vec3d vec3, ItemStack itemStack, EntityPlayer entityPlayer) {

        PartHose w = new PartHose();
        w.preparePlacement(itemStack.getItemDamage());
        return w;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {

        for (int i = 0; i < 3; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return "item." + Names.partHose[stack.getItemDamage()].unlocalized;
    }
}

