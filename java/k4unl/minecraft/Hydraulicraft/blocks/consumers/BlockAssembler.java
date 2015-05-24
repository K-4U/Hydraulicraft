package k4unl.minecraft.Hydraulicraft.blocks.consumers;


import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.api.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileAssembler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAssembler extends HydraulicBlockContainerBase implements ITieredBlock {

    public BlockAssembler() {

        super(Names.blockHydraulicAssembler);
        this.hasFrontIcon = true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int var2) {

        return new TileAssembler();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
        if(player.isSneaking())
            return false;

        TileEntity entity = world.getTileEntity(x, y, z);
        if(entity == null || !(entity instanceof TileAssembler)){
            return false;

        }

        player.openGui(Hydraulicraft.instance, GuiIDs.ASSEMBLER.ordinal(), world, x, y, z);

        return true;
    }

    @Override
    public PressureTier getTier() {

        return PressureTier.MEDIUMPRESSURE;
    }
}
