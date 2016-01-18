package k4unl.minecraft.Hydraulicraft.blocks.consumers;

import k4unl.minecraft.Hydraulicraft.api.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.blocks.IBlockWithRotation;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicFiller;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHydraulicFiller extends HydraulicBlockContainerBase implements ITieredBlock, IBlockWithRotation {
    public BlockHydraulicFiller() {
        super(Names.blockHydraulicFiller, true);
        this.hasFrontIcon = true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int var2) {
        return new TileHydraulicFiller();
    }

    @Override
    public GuiIDs getGUIID() {
        return GuiIDs.FILLER;
    }

    @Override
    public PressureTier getTier() {
        return PressureTier.MEDIUMPRESSURE;
    }
}
