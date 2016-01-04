package k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.blocks;

import k4unl.minecraft.Hydraulicraft.api.IMultiTieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicTieredBlockBase;
import k4unl.minecraft.Hydraulicraft.blocks.IRotateableBlock;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities.TileElectricPump;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockElectricPump extends HydraulicTieredBlockBase implements IMultiTieredBlock, IRotateableBlock {
	public BlockElectricPump() {
		super(Names.blockElectricPump);
		
		this.hasTopIcon = true;
		hasTextures = false;
	}


	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileElectricPump(getTier(metadata));
	}

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.ELECTRICPUMP;
    }

    public boolean canConnectRedstone(IBlockAccess iba, int i, int j, int k, int dir){
		return true;
    }

	
	@Override
    public int getRenderType(){
        return -1;
    }

    @Override
    public boolean isOpaqueCube(){
        return false;
    }

	@Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing side) {
        if(!world.isRemote) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileElectricPump) {
                if (side.equals(EnumFacing.UP) || side.equals(EnumFacing.DOWN)) {
                    TileElectricPump e = (TileElectricPump) te;
                    EnumFacing facing = e.getFacing();
                    e.setFacing(facing.rotateAround(side.getAxis()));
                    e.getHandler().updateBlock();
                    world.notifyNeighborsOfStateChange(pos, this);
                }
            }
        }

        return true;
    }

    @Override
    public PressureTier getTier(int damage) {

        return PressureTier.fromOrdinal(damage);
    }

    @Override
    public PressureTier getTier(IBlockAccess world, BlockPos pos) {

        return getTierFromState(world.getBlockState(pos));
    }
}
