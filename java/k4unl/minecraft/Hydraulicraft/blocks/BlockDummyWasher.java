package k4unl.minecraft.Hydraulicraft.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import k4unl.minecraft.Hydraulicraft.TileEntities.TileDummyWasher;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineBlock;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineBlockContainer;
import k4unl.minecraft.Hydraulicraft.client.renderers.Renderers;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Id;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;

public class BlockDummyWasher extends MachineBlockContainer {
	private Icon blockIcon;
	
	
	protected BlockDummyWasher() {
		super(Ids.blockDummyWasher, Names.blockDummyWasher);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileDummyWasher();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType(){
        return Renderers.dummyWasherId;
    }
	
	@Override
    public boolean isOpaqueCube(){
        return false;
    }

    @Override
    public boolean renderAsNormalBlock(){
        return false;
    }
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {
		return false;
	}
	
	@Override
	public void registerIcons(IconRegister iconRegistry){
		blockIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + mName.unlocalized);
	}
	
	@Override
	public Icon getIcon(int side, int metadata){
		return blockIcon;
	}

	@Override
	public int idPicked(World par1World, int par2, int par3, int par4){
        return Ids.blockHydraulicWasher.act;
    }
	
	
}
