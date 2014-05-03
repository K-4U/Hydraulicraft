package k4unl.minecraft.Hydraulicraft.blocks.misc;

import java.util.Random;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockBase;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.items.ItemMiningHelmet;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockLight extends HydraulicBlockBase {

	public BlockLight(){
		super(Names.blockLight, Material.air);
		setLightLevel(0.9F);
		setLightOpacity(15);
		setTickRandomly(true);
	}
	
	@Override
	public boolean getTickRandomly(){
		return true;
	}
	
	public int getRenderType(){
        return 0;
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * Returns whether this block is collideable based on the arguments passed in n@param par1 block metaData n@param
     * par2 whether the player right-clicked while holding a boat
     */
    public boolean canCollideCheck(int p_149678_1_, boolean p_149678_2_){
        return false;
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    public void dropBlockAsItemWithChance(World p_149690_1_, int p_149690_2_, int p_149690_3_, int p_149690_4_, int p_149690_5_, float p_149690_6_, int p_149690_7_) {}
    
    
    public void updateTick(World world, int x, int y, int z, Random random){
    	//if(world.getBlock(x, y, z) instanceof BlockLight){
	    	//Seach within 3 blocks for a player.
	    	//If no player found. Remove the block
    		world.setBlockToAir(x, y, z);
	    	if(world.getClosestPlayer(x, y, z, 3) == null){
	    		world.setBlockToAir(x, y, z);
	    	}else{
	    		if(world.getClosestPlayer(x, y, z, 3).getCurrentArmor(3) != null){
		    		if(world.getClosestPlayer(x, y, z, 3).getCurrentArmor(3).getItem() == HCItems.itemMiningHelmet){
		    			if(!ItemMiningHelmet.isPoweredOn(world.getClosestPlayer(x, y, z, 3).getCurrentArmor(3))){
		    				world.setBlockToAir(x, y, z);
		    			}
		    		}else{
		    			world.setBlockToAir(x, y, z);
		    		}
	    		}else{
	    			world.setBlockToAir(x, y, z);
	    		}
	    	}
    	//}
	    world.scheduleBlockUpdate(x, y, z, HCBlocks.blockLight, 20);
    }
}
