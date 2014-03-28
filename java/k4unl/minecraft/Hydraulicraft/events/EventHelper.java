package k4unl.minecraft.Hydraulicraft.events;

public class EventHelper {

	public static void init(){
		MinecraftForge.EVENT_BUS.register(new EventHelper());
	}
	
	@SubscribeEvent
	public void onBlockBreak(BreakEvent event){
		if(event.block == HCBlocks.hydraulicPressureWall || event.block == HCBlocks.blockValve){
			//check all directions for a hydraulic washer
			boolean breakAll = false;
			for(int horiz = -2; horiz <= 2; horiz++) {
				for(int vert = -2; vert <= 2; vert++) {
					for(int depth = -2; depth <= 2; depth++) {
						int x = event.x + horiz;
						int y = event.y + vert;
						int z = event.z + depth;
						Block block = event.world.getBlock(x, y, z);
						if(block instanceof BlockHydraulicWasher){
							TileHydraulicWasher washer = (TileHydraulicWasher) event.world.getTileEntity(x, y, z);
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
	
	@SubscribeEvent
	public void onDeathEvent(LivingDeathEvent event){
		if(event.entity instanceof EntityPig){
			if(!event.entity.worldObj.isRemote){
				EntityItem ei = new EntityItem(event.entityLiving.worldObj);
				ei.setEntityItemStack(new ItemStack(HCItems.itemBacon, 1));
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
