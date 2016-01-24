package k4unl.minecraft.Hydraulicraft.blocks.storage;

import k4unl.minecraft.Hydraulicraft.api.IMultiTieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicTieredBlockBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.storage.TileHydraulicPressureVat;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockHydraulicPressureVat extends HydraulicTieredBlockBase implements IMultiTieredBlock {


    public BlockHydraulicPressureVat() {
        super(Names.blockHydraulicPressurevat);
        hasTopIcon = true;
        hasBottomIcon = true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileHydraulicPressureVat(getTierFromState(getStateFromMeta(metadata)));
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.PRESSUREVAT;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity ent = worldIn.getTileEntity(pos);
        if (ent instanceof TileHydraulicPressureVat) {
            if (stack != null) {
                if (stack.getTagCompound() != null) {
                    ((TileHydraulicPressureVat) ent).newFromNBT(stack.getTagCompound());
                    worldIn.markBlockForUpdate(pos);
                }
            }
        }
    }

    @Override
    public int quantityDropped(Random p_149745_1_) {
        return 0;
    }

    /**
     * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
     * side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    //TODO: FIX ME
    public int isProvidingStrongPower(IBlockAccess w, int x, int y, int z, int side) {
        return this.isProvidingWeakPower(w, x, y, z, side);
    }

    /**
     * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
     * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
     * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    //TODO: FIX ME
    public int isProvidingWeakPower(IBlockAccess w, int x, int y, int z, int side) {
        TileEntity ent = w.getTileEntity(new BlockPos(x, y, z));
        if (ent instanceof TileHydraulicPressureVat) {
            TileHydraulicPressureVat p = (TileHydraulicPressureVat) ent;
            return p.getRedstoneLevel();
        }
        return 0;
    }


    public boolean canProvidePower() {
        return true;
    }


    @Override
    public PressureTier getTier(int damage) {

        return PressureTier.fromOrdinal(damage);
    }

    @Override
    public PressureTier getTier(IBlockAccess world, BlockPos pos) {
        if(world.getBlockState(pos).getBlock() == this) {
            return getTierFromState(world.getBlockState(pos));
        }else{
            return PressureTier.INVALID;
        }
    }
}
