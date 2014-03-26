package k4unl.minecraft.Hydraulicraft.events;

import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TileHydraulicWasher;
import k4unl.minecraft.Hydraulicraft.blocks.Blocks;
import k4unl.minecraft.Hydraulicraft.items.Items;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.UpdateChecker;
import k4unl.minecraft.Hydraulicraft.lib.UpdateChecker.UpdateInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

public class EventHelper {

	public static void init(){
		MinecraftForge.EVENT_BUS.register(new EventHelper());
	}
	
	@ForgeSubscribe
	public void onBlockBreak(BreakEvent event){
		if(event.block == Blocks.hydraulicPressureWall || event.block == Blocks.blockValve){
			//check all directions for a hydraulic washer
			boolean breakAll = false;
			for(int horiz = -2; horiz <= 2; horiz++) {
				for(int vert = -2; vert <= 2; vert++) {
					for(int depth = -2; depth <= 2; depth++) {
						int x = event.x + horiz;
						int y = event.y + vert;
						int z = event.z + depth;
						int blockId = event.world.getBlockId(x, y, z);
						if(blockId == Ids.blockHydraulicWasher.act){
							TileHydraulicWasher washer = (TileHydraulicWasher) event.world.getBlockTileEntity(x, y, z);
							washer.invalidateMultiblock();
							breakAll = true;
							break;
						}
						//Log.info("Checking " + (event.x + horiz) + "," +(event.y + vert) + "," + (event.z + depth) + " = " + blockId);
					}
					if(breakAll){
						break;
					}
				}
				if(breakAll){
					break;
				}
			}
		}
	}
	
	@ForgeSubscribe
	public void onDeathEvent(LivingDeathEvent event){
		if(event.entity instanceof EntityPig){
			if(!event.entity.worldObj.isRemote){
				EntityItem ei = new EntityItem(event.entityLiving.worldObj);
				ei.setEntityItemStack(new ItemStack(Items.itemBacon, 1));
				ei.setPosition(event.entityLiving.posX,event.entityLiving.posY,event.entityLiving.posZ);
				event.entityLiving.worldObj.spawnEntityInWorld(ei);
			}
		}
	}
	
	@ForgeSubscribe
	public void onEntityJoinEvent(EntityJoinWorldEvent event){
		if(event.entity instanceof EntityPlayer){
			Log.info("Player joined");
			if(event.world.isRemote){
				//If update available, tell em!
				if(UpdateChecker.isUpdateAvailable){
					UpdateInfo info = UpdateChecker.infoAboutUpdate;
					((EntityPlayer)event.entity).addChatMessage("Hydraulicraft version " + info.latestVersion + "-" + info.buildNumber + " available!");
					((EntityPlayer)event.entity).addChatMessage("Released on " + info.dateOfRelease);
					int i = 0;
					for(String cl : info.changelog){
						((EntityPlayer)event.entity).addChatMessage(cl);						
						
						i++;
						if(i >= 3){
							break;
						}
					}
				}
			}
		}
	}
}
