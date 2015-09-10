package k4unl.minecraft.Hydraulicraft.blocks.misc;


import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileChunkLoader;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockChunkLoader extends HydraulicBlockContainerBase {

    public BlockChunkLoader(){
        super(Names.blockChunkLoader, false);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int var2) {
        return new TileChunkLoader();
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.INVALID;
    }
}
