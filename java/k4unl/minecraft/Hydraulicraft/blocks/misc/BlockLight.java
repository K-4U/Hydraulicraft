package k4unl.minecraft.Hydraulicraft.blocks.misc;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockBase;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.items.ItemMiningHelmet;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockLight extends HydraulicBlockBase {
	public static final int maxLightLevel = (int)(0.9F * 15);
	
	
	public BlockLight(){
		super(Names.blockLight, Material.rock, false);
		setLightOpacity(15);
		setTickRandomly(false);
	}
	
	@Override
	public boolean getTickRandomly(){
		return true;
	}
	
	public int getRenderType(){
        return -1;
    }

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
		return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
	}

	/**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

	@Override
	public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
		return false;
	}

	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {

	}

	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    	//if(world.getBlock(x, y, z) instanceof BlockLight){
	    	//Seach within 3 blocks for a player.
	    	//If no player found. Remove the block
    		EntityPlayer closestPlayer = worldIn.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 15);
	    	if(closestPlayer == null){
	    		worldIn.setBlockToAir(pos);
	    	}else{
	    		if(closestPlayer.getCurrentArmor(3) != null){
		    		if(closestPlayer.getCurrentArmor(3).getItem() == HCItems.itemMiningHelmet){
		    			if(!ItemMiningHelmet.isPoweredOn(closestPlayer.getCurrentArmor(3))){
		    				worldIn.setBlockToAir(pos);
		    			}
		    		}else{
		    			worldIn.setBlockToAir(pos);
		    		}
	    		}else{
	    			worldIn.setBlockToAir(pos);
	    		}
	    	}
    	//}
	    //world.scheduleBlockUpdate(x, y, z, HCBlocks.blockLight, 10);
    }

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    	worldIn.scheduleBlockUpdate(pos, HCBlocks.blockLight, 10, 10);
    }


	@Override
	public int getLightValue(IBlockAccess world, BlockPos pos) {
		return 15;//world.getBlockMetadata(x, y, z);
    }
}
