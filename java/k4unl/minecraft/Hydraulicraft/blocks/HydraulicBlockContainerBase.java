package k4unl.minecraft.Hydraulicraft.blocks;


import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.BuildcraftCompat;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class HydraulicBlockContainerBase extends HydraulicBlockBase implements ITileEntityProvider {

    protected HydraulicBlockContainerBase(Name machineName, boolean addToTab) {
        super(machineName, addToTab);
    }

    @Override
    public abstract TileEntity createNewTileEntity(World world, int var2);

    public abstract GuiIDs getGUIID();

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }


    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        //Call TileEntity's onBlockBreaks function
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileHydraulicBase) {
            ((TileHydraulicBase) tile).onBlockBreaks();
        }

        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }

    public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam) {
        super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(eventID, eventParam);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity entity = worldIn.getTileEntity(pos);
        if (entity == null) {
            return false;
        }

        if (this instanceof IRotateableBlock) {
            if (playerIn.getCurrentEquippedItem() != null && BuildcraftCompat.INSTANCE.isWrench(playerIn.getCurrentEquippedItem())) {
                return false;
            }
        }

        if (getGUIID() != GuiIDs.INVALID) {
            if (this instanceof IGUIMultiBlock) {
                if (!((IGUIMultiBlock) this).isValid(worldIn, pos)) {
                    return false;
                }
            }
            playerIn.openGui(Hydraulicraft.instance, getGUIID().ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }

        return false;
    }
}
