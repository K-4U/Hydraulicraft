package k4unl.minecraft.Hydraulicraft.multipart;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Location;
import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.TileMultipart;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.TMultiPart;

public class Multipart implements IPartFactory{
	public static ItemPartHose itemPartHose; 
	
	public Multipart(){
		MultiPartRegistry.registerParts(this, new String[] { 
                Names.blockHydraulicHose[0].unlocalized,
                Names.blockHydraulicHose[1].unlocalized,
                Names.blockHydraulicHose[2].unlocalized});
		
		itemPartHose = new ItemPartHose(Ids.blockHydraulicHose.act);
	}

	@Override
	public TMultiPart createPart(String id, boolean client) {
		if(id.equals(Names.blockHydraulicHose[0].unlocalized) || id.equals(Names.blockHydraulicHose[1].unlocalized) || id.equals(Names.blockHydraulicHose[2].unlocalized)){
			return new PartHose();
		}
		return null;
	}
	
	public static TileMultipart getMultipartTile(IBlockAccess access, Location pos){
        TileEntity te = access.getBlockTileEntity(pos.getX(), pos.getY(), pos.getZ());
        return te instanceof TileMultipart ? (TileMultipart) te : null;
    }

    public static TMultiPart getMultiPart(IBlockAccess w, Location bc, int part){
        TileMultipart t = getMultipartTile(w, bc);
        if (t != null)
            return t.partMap(part);

        return null;
    }
}
