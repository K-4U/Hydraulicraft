package k4unl.minecraft.Hydraulicraft.blocks.gow;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.gow.TilePortalFrame;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class BlockPortalFrame extends GOWBlockRendering {
    private static Vector3fMax blockBounds = new Vector3fMax(0.3F, 0.3F, 0.3F, 0.7F, 0.7F, 0.7F);

    public BlockPortalFrame() {

        super(Names.portalFrame.unlocalized);
        setCreativeTab(CustomTabs.tabGOW);
    }

    @Override
    protected Class<? extends TileEntity> getTileEntity() {

        return TilePortalFrame.class;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iba, int x, int y, int z) {

        TileEntity ent = iba.getTileEntity(x, y, z);

        if (ent instanceof TilePortalFrame) {
            TilePortalFrame frame = (TilePortalFrame) ent;
            Vector3fMax vector = blockBounds.copy();
            if (frame.isConnectedTo(ForgeDirection.UP))
                vector.setYMax(1.0F);
            if (frame.isConnectedTo(ForgeDirection.DOWN))
				vector.setYMin(0.0F);
			
			if(frame.isConnectedTo(ForgeDirection.EAST))
				vector.setXMax(1.0F);
			if(frame.isConnectedTo(ForgeDirection.WEST))
				vector.setXMin(0.0F);
			
			if(frame.isConnectedTo(ForgeDirection.SOUTH))
				vector.setZMax(1.0F);
			if(frame.isConnectedTo(ForgeDirection.NORTH))
				vector.setZMin(0.0F);
			
			
			this.setBlockBounds(vector.getXMin(), vector.getYMin(), vector.getZMin(), vector.getXMax(), vector.getYMax(), vector.getZMax());
		}
	}
	
	@Override
	public void addCollisionBoxesToList(World w, int x, int y, int z, AxisAlignedBB axigAlignedBB, List arrayList, Entity entity){
		TileEntity ent = w.getTileEntity(x, y, z);
		
		if(ent instanceof TilePortalFrame){
			TilePortalFrame frame = (TilePortalFrame)ent;
			Vector3fMax vector = blockBounds.copy();
			if(frame.isConnectedTo(ForgeDirection.UP))
				vector.setYMax(1.0F);
			if(frame.isConnectedTo(ForgeDirection.DOWN))
				vector.setYMin(0.0F);
			
			if(frame.isConnectedTo(ForgeDirection.WEST))
				vector.setXMin(0.0F);
			if(frame.isConnectedTo(ForgeDirection.EAST))
				vector.setXMax(1.0F);
			
			if(frame.isConnectedTo(ForgeDirection.SOUTH))
				vector.setZMax(1.0F);
			if(frame.isConnectedTo(ForgeDirection.NORTH))
				vector.setZMin(0.0F);
			
			
			this.setBlockBounds(vector.getXMin(), vector.getYMin(), vector.getZMin(), vector.getXMax(), vector.getYMax(), vector.getZMax());
			super.addCollisionBoxesToList(w, x, y, z, axigAlignedBB, arrayList, entity);
		}
	}
}
