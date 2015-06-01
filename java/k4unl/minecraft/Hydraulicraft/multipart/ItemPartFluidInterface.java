package k4unl.minecraft.Hydraulicraft.multipart;


import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.JItemMultiPart;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.TMultiPart;
import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemPartFluidInterface extends JItemMultiPart {

    public ItemPartFluidInterface(){
        super();
        setHasSubtypes(true);
        setCreativeTab(CustomTabs.tabHydraulicraft);
        setUnlocalizedName(Names.partFluidInterface.unlocalized);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World w, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {

        if (super.onItemUse(stack, player, w, x, y, z, side, hitX, hitY, hitZ)){
            w.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, Block.soundTypeGlass.getStepResourcePath(), Block.soundTypeGlass.getVolume() * 5.0F, Block.soundTypeGlass.getPitch() * .9F);
            return true;
        }
        return false;
    }

    @Override
    public TMultiPart newPart(ItemStack item, EntityPlayer player, World world, BlockCoord pos, int side, Vector3 vhit) {
        PartFluidInterface w = (PartFluidInterface) MultiPartRegistry.createPart("tile." + Names.partFluidInterface.unlocalized, false);

        w.preparePlacement(ForgeDirection.getOrientation(side).getOpposite());
        return w;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack){
        return "tile." + Names.partFluidInterface.unlocalized;
    }

    @Override
    public void registerIcons(IIconRegister reg){
    }
}
