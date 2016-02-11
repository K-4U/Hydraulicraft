package k4unl.minecraft.Hydraulicraft.blocks.gow;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class GOWBlockBase extends BlockContainer {

    private String tName;

    protected GOWBlockBase(String name) {

        super(Material.iron);
        tName = name;

        setUnlocalizedName(tName);
        setStepSound(Block.soundTypeStone);
        setHardness(3.5F);
        setResistance(10F);
    }

    @Override
    public abstract TileEntity createNewTileEntity(World var1, int var2);

}
