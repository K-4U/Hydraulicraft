package k4unl.minecraft.Hydraulicraft.tileEntities.interfaces;


import net.minecraft.block.Block;

public interface IConnectTexture {

    /**
     * Checks whether or not the block with this TE should get connected to.
     * @return
     */
    boolean connectTexture();

    /**
     * Checks if the block should connect to a certain type of block.
     * @return
     */
    boolean connectTextureTo(Block type);
}
