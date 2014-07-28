package k4unl.minecraft.Hydraulicraft.tileEntities.misc;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.ForgeChunkManager;

public class TileChunkLoader extends TileEntity{

    private ForgeChunkManager.Ticket chunkTicket;
    private boolean hasLoaded;


    public void updateEntity(){
        super.updateEntity();
        if(!hasLoaded){
            loadChunk();
            hasLoaded = true;
        }
    }

    @Override
    public void invalidate() {
        ForgeChunkManager.releaseTicket(chunkTicket);
    }

    public void loadChunk(){
        if (chunkTicket == null) {
            chunkTicket = ForgeChunkManager.requestTicket(Hydraulicraft
                    .instance, worldObj, ForgeChunkManager.Type.NORMAL);
        }

        ForgeChunkManager.forceChunk(chunkTicket, new ChunkCoordIntPair(xCoord, zCoord));
    }

}
