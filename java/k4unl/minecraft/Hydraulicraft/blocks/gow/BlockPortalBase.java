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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPortalBase extends GOWBlockRendering implements ITieredBlock {

	public BlockPortalBase() {
		super(Names.portalBase.unlocalized);
		setCreativeTab(CustomTabs.tabGOW);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
		if(player.isSneaking()){
			return false;
		}
		
		TileEntity entity = world.getTileEntity(x, y, z);
		if(entity == null || !(entity instanceof TilePortalBase)){
			return false;
		}
		
		if(player.getCurrentEquippedItem() != null){
			if(player.getCurrentEquippedItem().getItem() instanceof ItemIPCard){
				return false;
			}
			if(player.getCurrentEquippedItem().getItem() instanceof ItemDye){
				//Dye that shit!
				if(((TilePortalBase)entity).getIsValid()){
					((TilePortalBase)entity).dye(~player.getCurrentEquippedItem().getItemDamage() & 15);
				}
				return false;
			}
		}
		
		if(((TilePortalBase)entity).getIsValid()){
			player.openGui(Hydraulicraft.instance, GuiIDs.PORTALBASE.ordinal(), world, x, y, z);
		}else{
			return false;
		}
		return true;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y,
				int z, Block blockId) {
		super.onNeighborBlockChange(world, x, y, z, blockId);
		
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TilePortalBase){
			((TilePortalBase)tile).checkRedstonePower();			
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
