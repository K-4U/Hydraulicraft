package k4unl.minecraft.Hydraulicraft.blocks.storage;

import java.util.Random;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicTieredBlockBase;
import k4unl.minecraft.Hydraulicraft.api.IMultiTieredBlock;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.storage.TileHydraulicPressureVat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHydraulicPressureVat extends HydraulicTieredBlockBase implements IMultiTieredBlock {
	
	
	public BlockHydraulicPressureVat() {
		super(Names.blockHydraulicPressurevat);
		hasTopIcon = true;
		hasBottomIcon = true;
	}

	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata){
		TileHydraulicPressureVat pVat = new TileHydraulicPressureVat(metadata);
		return pVat;
    }
	
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
		if(player.isSneaking())
			return false;
		
		TileEntity entity = world.getTileEntity(x, y, z);
		if(entity == null || !(entity instanceof TileHydraulicPressureVat)){
			return false;
			
		}
		
		player.openGui(Hydraulicraft.instance, GuiIDs.PRESSUREVAT.ordinal(), world, x, y, z);
		
		return true;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack iStack){
		super.onBlockPlacedBy(world, x, y, z, player, iStack);
		TileEntity ent = world.getTileEntity(x, y, z);
		if(ent instanceof TileHydraulicPressureVat){
			if(iStack != null){
				if(iStack.getTagCompound() != null){
					((TileHydraulicPressureVat)ent).newFromNBT(iStack.getTagCompound());
					world.markBlockForUpdate(x, y, z);
				}
			}
		}
	}
	
	@Override
	public int quantityDropped(Random p_149745_1_){
        return 0;
    }
	/**
     * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
     * side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingStrongPower(IBlockAccess w, int x, int y, int z, int side){
        return this.isProvidingWeakPower(w, x, y, z, side);
    }

    /**
     * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
     * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
     * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingWeakPower(IBlockAccess w, int x, int y, int z, int side){
    	TileEntity ent = w.getTileEntity(x, y, z);
		if(ent instanceof TileHydraulicPressureVat){
			TileHydraulicPressureVat p = (TileHydraulicPressureVat) ent;
			return p.getRedstoneLevel();
		}
		return 0;
    }

    public boolean canProvidePower(){
        return true;
    }


    @Override
    public PressureTier getTier(int metadata) {

        return PressureTier.fromOrdinal(metadata);
    }
}
