package k4unl.minecraft.Hydraulicraft.lib;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class CustomTeleporter extends Teleporter {

	public CustomTeleporter(WorldServer par1WorldServer) {
		super(par1WorldServer);
	}
	
	@Override
	public boolean makePortal(Entity entity){
		return true;
	}
	
	@Override
	public void placeInPortal(Entity entity, double x, double y, double z, float yaw){
		int newX = MathHelper.floor_double(entity.posX);
		int newY = MathHelper.floor_double(entity.posY);
		int newZ = MathHelper.floor_double(entity.posZ);
		entity.setLocationAndAngles(newX, newY, newZ, entity.rotationYaw, 0.0F);
	}

}
