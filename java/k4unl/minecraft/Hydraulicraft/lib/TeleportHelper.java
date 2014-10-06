package k4unl.minecraft.Hydraulicraft.lib;

import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class TeleportHelper {

	
	public static void teleportEntity(Entity ent, Location target){
		if(ent.dimension != target.getDimension()){
			travelToDimension(ent, target);
		}else{
			if(ent instanceof EntityPlayer){
				((EntityPlayer)ent).setPositionAndUpdate(target.getX()+0.5, target.getY()+0.5, target.getZ()+0.5);	
			}else{
				ent.setLocationAndAngles(target.getX()+0.5, target.getY()+0.5, target.getZ()+0.5, ent.rotationYaw, ent.rotationPitch);
			}
		}
	}
	
	public static void travelToDimension(Entity ent, Location target){
		if(ent instanceof EntityPlayerMP){
			MinecraftServer minecraftserver = MinecraftServer.getServer();
			WorldServer newWorldServer = minecraftserver.worldServerForDimension(target.getDimension());
			minecraftserver.getConfigurationManager().transferPlayerToDimension((EntityPlayerMP)ent, target.getDimension(), new CustomTeleporter(newWorldServer));
			((EntityPlayer)ent).setPositionAndUpdate(target.getX()+0.5, target.getY()+0.5, target.getZ()+0.5);
			return;
		}
		
		
		if (!ent.worldObj.isRemote && !ent.isDead)
        {
			ent.worldObj.theProfiler.startSection("changeDimension");
            MinecraftServer minecraftserver = MinecraftServer.getServer();
            int j = ent.dimension;
            WorldServer worldserver = minecraftserver.worldServerForDimension(j);
            WorldServer worldserver1 = minecraftserver.worldServerForDimension(target.getDimension());
            ent.dimension = target.getDimension();

            if (j == 1 && target.getDimension() == 1)
            {
                worldserver1 = minecraftserver.worldServerForDimension(0);
                ent.dimension = 0;
            }

            ent.worldObj.removeEntity(ent);
            ent.isDead = false;
            ent.worldObj.theProfiler.startSection("reposition");
            minecraftserver.getConfigurationManager().transferEntityToWorld(ent, j, worldserver, worldserver1);
            ent.worldObj.theProfiler.endStartSection("reloading");
            Entity entity = EntityList.createEntityByName(EntityList.getEntityString(ent), worldserver1);

            if (entity != null)
            {
                entity.copyDataFrom(ent, true);

                if (j == 1 && target.getDimension() == 1)
                {
					entity.setLocationAndAngles(target.getX()+0.5, target.getY()+0.5, target.getZ()+0.5, entity.rotationYaw, entity.rotationPitch);
                }

                worldserver1.spawnEntityInWorld(entity);
                entity.setPosition(target.getX()+0.5, target.getY()+0.5, target.getZ()+0.5);
            }

            ent.isDead = true;
            ent.worldObj.theProfiler.endSection();
            worldserver.resetUpdateEntityTick();
            worldserver1.resetUpdateEntityTick();
            ent.worldObj.theProfiler.endSection();
        }
	}
}
