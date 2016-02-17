package k4unl.minecraft.Hydraulicraft.tileEntities.misc;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.tileentity.TileEntity;

public class TileJarOfDirt extends TileEntity {

    Location location;

    @Override
    public void onLoad() {

        super.onLoad();
        location = new Location(getPos().getX(), getPos().getY(), getPos().getZ());
        if (!Hydraulicraft.jarOfDirtList.contains(this)) {
            Hydraulicraft.jarOfDirtList.add(this);
        }
    }

    @Override
    public void onChunkUnload() {

        super.onChunkUnload();
        if (Hydraulicraft.jarOfDirtList.contains(this)) {
            Hydraulicraft.jarOfDirtList.remove(this);
        }

    }

    @Override
    public void invalidate() {

        super.invalidate();
        if (Hydraulicraft.jarOfDirtList.contains(this)) {
            Hydraulicraft.jarOfDirtList.remove(this);
        }
    }

    public Location getLocation(){
        return location;
    }
}
