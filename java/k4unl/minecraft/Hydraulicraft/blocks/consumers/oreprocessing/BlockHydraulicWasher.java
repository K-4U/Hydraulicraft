package k4unl.minecraft.Hydraulicraft.blocks.consumers.oreprocessing;

import k4unl.minecraft.Hydraulicraft.api.IMultiTieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.blocks.IBlockWithRotation;
import k4unl.minecraft.Hydraulicraft.blocks.IGUIMultiBlock;
import k4unl.minecraft.Hydraulicraft.blocks.ITooltipProvider;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicWasher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHydraulicWasher extends HydraulicBlockContainerBase implements ITooltipProvider, IMultiTieredBlock, IGUIMultiBlock, IBlockWithRotation {

    public BlockHydraulicWasher() {

        super(Names.blockHydraulicWasher, true);
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
    public String getToolTip(ItemStack stack) {

        return Localization.getString(Localization.GUI_MULTIBLOCK);
    }

    @Override
    public PressureTier getTier(int damage) {

        return PressureTier.fromOrdinal(damage);
    }

    @Override
    public PressureTier getTier(IBlockAccess world, BlockPos pos) {

        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileHydraulicWasher) {
            return ((TileHydraulicWasher) tileEntity).getPressureTier();
        }
        return PressureTier.INVALID;
    }

    @Override
    public boolean isValid(IBlockAccess world, BlockPos pos) {

        return ((TileHydraulicWasher) world.getTileEntity(pos)).getIsValidMultiblock();
    }
}
