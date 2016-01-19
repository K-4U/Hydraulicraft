package k4unl.minecraft.Hydraulicraft.multipart;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import mcmultipart.item.ItemMultiPart;
import mcmultipart.multipart.IMultipart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemPartFluidInterface extends ItemMultiPart {

    public ItemPartFluidInterface(){
        super();
        setHasSubtypes(true);
        setCreativeTab(CustomTabs.tabHydraulicraft);
        setUnlocalizedName(Names.partFluidInterface.unlocalized);
    }
/*
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World w, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {

        if (super.onItemUse(stack, player, w, x, y, z, side, hitX, hitY, hitZ)){
            w.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, Block.soundTypeGlass.getStepResourcePath(), Block.soundTypeGlass.getVolume() * 5.0F, Block.soundTypeGlass.getPitch() * .9F);
            return true;
        }
        return false;
    }*/

    @Override
    public IMultipart createPart(World world, BlockPos blockPos, EnumFacing enumFacing, Vec3 vec3, ItemStack itemStack, EntityPlayer entityPlayer) {
        PartFluidInterface w = new PartFluidInterface();
        w.preparePlacement(enumFacing);
        return w;
    }

}
