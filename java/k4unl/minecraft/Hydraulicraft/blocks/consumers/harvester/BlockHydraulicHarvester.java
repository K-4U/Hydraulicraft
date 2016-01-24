package k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester;

import k4unl.minecraft.Hydraulicraft.api.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.blocks.IBlockWithRotation;
import k4unl.minecraft.Hydraulicraft.blocks.IGUIMultiBlock;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHydraulicHarvester;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHydraulicHarvester extends HydraulicBlockContainerBase implements ITieredBlock, IGUIMultiBlock, IBlockWithRotation {

    public BlockHydraulicHarvester() {
        super(Names.blockHydraulicHarvester, true);
    }


    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {

        return new TileHydraulicHarvester();
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.HARVESTER;
    }


    @Override
    public PressureTier getTier() {

        return PressureTier.MEDIUMPRESSURE;
    }

    @Override
    public boolean isValid(IBlockAccess world, BlockPos pos) {

        return ((TileHydraulicHarvester) world.getTileEntity(pos)).getIsMultiblock();
    }


    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {

        if (worldIn.isRemote) return true;
        if (!isValid(worldIn, pos)) {
            TileHydraulicHarvester harvester = ((TileHydraulicHarvester) worldIn.getTileEntity(pos));
            //Tell the harvester to quickly check:
            harvester.doMultiBlockChecking();
            if (!isValid(worldIn, pos)) {
                playerIn.addChatMessage(new ChatComponentTranslation(harvester.getError().getTranslation(), harvester.getExtraErrorInfo()));
                return true;
            }
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
    }
}
