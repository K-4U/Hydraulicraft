package k4unl.minecraft.Hydraulicraft.blocks.gow;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.api.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.items.ItemIPCard;
import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.gow.TilePortalBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockPortalBase extends GOWBlockRendering implements ITieredBlock {

    public BlockPortalBase() {

        super(Names.portalBase.unlocalized);
        setCreativeTab(CustomTabs.tabGOW);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (playerIn.isSneaking()) {
            return false;
        }

        TileEntity entity = worldIn.getTileEntity(pos);
        if (entity == null || !(entity instanceof TilePortalBase)) {
            return false;
        }

        if (playerIn.getActiveItemStack() != null) {
            if (playerIn.getActiveItemStack().getItem() instanceof ItemIPCard) {
                return false;
            }
            if (playerIn.getActiveItemStack().getItem() instanceof ItemDye) {
                //Dye that shit!
                if (((TilePortalBase) entity).getIsValid()) {
                    ((TilePortalBase) entity).dye(~playerIn.getActiveItemStack().getItemDamage() & 15);
                }
                return false;
            }
        }

        if (((TilePortalBase) entity).getIsValid()) {
            playerIn.openGui(Hydraulicraft.instance, GuiIDs.PORTALBASE.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {

        super.neighborChanged(state, worldIn, pos, blockIn);

        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TilePortalBase) {
            ((TilePortalBase) tile).checkRedstonePower();
        }
    }

    @Override
    public PressureTier getTier() {

        return PressureTier.HIGHPRESSURE;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {

        return new TilePortalBase(getTier());
    }

}
