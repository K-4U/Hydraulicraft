package k4unl.minecraft.Hydraulicraft.blocks.consumers.misc;

import k4unl.minecraft.Hydraulicraft.api.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.blocks.IBlockWithRotation;
import k4unl.minecraft.Hydraulicraft.blocks.ITooltipProvider;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileFluidRecombobulator;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author Koen Beckers (K-4U)
 */
public class BlockFluidRecombobulator extends HydraulicBlockContainerBase implements ITieredBlock, IBlockWithRotation, ITooltipProvider {


    public BlockFluidRecombobulator() {

        super(Names.blockFluidRecombobulator, true);


    }

    @Override
    public TileEntity createNewTileEntity(World world, int var2) {

        return new TileFluidRecombobulator();
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.FLUID_RECOMBOBULATOR;
    }

    @Override
    public String getToolTip(ItemStack stack) {

        return "Try saying that 3 times fast";
    }

    @Override
    public PressureTier getTier() {

        return PressureTier.MEDIUMPRESSURE;
    }
}
