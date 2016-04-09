package k4unl.minecraft.Hydraulicraft.blocks.misc;

import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.blocks.ITooltipProvider;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileJarOfDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockJarOfDirt extends HydraulicBlockContainerBase implements ITooltipProvider {

    public BlockJarOfDirt() {

        super(Names.blockJarDirt, true);
        hasTextures = false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int var2) {

        return new TileJarOfDirt();
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.INVALID;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public String getToolTip(ItemStack stack) {

        return "I've got a jar of dihirt!";
    }
}
