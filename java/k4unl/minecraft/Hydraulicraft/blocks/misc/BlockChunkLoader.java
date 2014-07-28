package k4unl.minecraft.Hydraulicraft.blocks.misc;


import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileChunkLoader;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockChunkLoader extends HydraulicBlockContainerBase {

    public BlockChunkLoader(){
        super(Names.blockChunkLoader);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int var2) {
        return new TileChunkLoader();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        return false;
    }
}
