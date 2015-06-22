package k4unl.minecraft.Hydraulicraft.blocks.consumers.oreprocessing;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.IMultiTieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.blocks.IGUIMultiBlock;
import k4unl.minecraft.Hydraulicraft.blocks.ITooltipProvider;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicWasher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHydraulicWasher extends HydraulicBlockContainerBase implements ITooltipProvider, IMultiTieredBlock, IGUIMultiBlock {

	public BlockHydraulicWasher() {
		super(Names.blockHydraulicWasher);
		this.hasFrontIcon = true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileHydraulicWasher();
	}

	@Override
	public GuiIDs getGUIID() {

		return GuiIDs.WASHER;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
		if(player.isSneaking())
			return false;
		
		TileEntity entity = world.getTileEntity(x, y, z);
		if(entity == null || !(entity instanceof TileHydraulicWasher)){
			return false;
			
		}
		TileHydraulicWasher washer = (TileHydraulicWasher) entity;
		if(washer.getIsValidMultiblock()){
			player.openGui(Hydraulicraft.instance, getGUIID().ordinal(), world, x, y, z);
			return true;
		}
		
		return false;
	}

    @Override
    public String getToolTip() {

        return Localization.getString(Localization.GUI_MULTIBLOCK);
    }

    @Override
    public PressureTier getTier(int metadata) {

        return PressureTier.INVALID;
    }

    @Override
    public PressureTier getTier(IBlockAccess world, int x, int y, int z) {

        return PressureTier.fromOrdinal(world.getBlockMetadata(x, y, z));
    }

	@Override
	public boolean isValid(IBlockAccess world, int x, int y, int z) {
		return ((TileHydraulicWasher)world.getTileEntity(x, y, z)).getIsValidMultiblock();
	}
}
