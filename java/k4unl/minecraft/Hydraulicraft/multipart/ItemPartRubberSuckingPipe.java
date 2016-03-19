package k4unl.minecraft.Hydraulicraft.multipart;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import mcmultipart.item.ItemMultiPart;
import mcmultipart.multipart.IMultipart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author Koen Beckers (K-4U)
 */
public class ItemPartRubberSuckingPipe extends ItemMultiPart {

    public ItemPartRubberSuckingPipe() {

        super();
        setHasSubtypes(true);
        setCreativeTab(CustomTabs.tabHydraulicraft);
        setUnlocalizedName(Names.partRubberSuckingPipe.unlocalized);
    }

    @Override
    public IMultipart createPart(World world, BlockPos blockPos, EnumFacing enumFacing, Vec3d vec3, ItemStack itemStack, EntityPlayer entityPlayer) {

        return new PartRubberSuckingPipe();
    }
}
