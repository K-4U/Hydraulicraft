package k4unl.minecraft.Hydraulicraft.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.TileEntities.generator.TileHydraulicPump;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlock;
import k4unl.minecraft.Hydraulicraft.client.renderers.Renderers;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHydraulicPump extends MachineTieredBlock {

    protected BlockHydraulicPump() {
        super(Ids.blockHydraulicPump, Names.blockHydraulicPump);
        this.hasTopIcon = true;
    }

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileHydraulicPump();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType(){
        return -1;
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
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
		if(player.isSneaking())
			return false;
		
		TileEntity entity = world.getBlockTileEntity(x, y, z);
		if(entity == null || !(entity instanceof TileHydraulicPump)){
			return false;
			
		}
		TileHydraulicPump pump = (TileHydraulicPump) entity;
		player.openGui(Hydraulicraft.instance, Ids.GUIPump.act, world, x, y, z);
		
		return true;
	}
}
