package k4unl.minecraft.Hydraulicraft.events;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.oreprocessing.BlockHydraulicWasher;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.items.ItemHydraulicDrill;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicWasher;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileInterfaceValve;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileJarOfDirt;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class EventHelper {

    private static boolean   hasShownUpdateInfo = false;
    private static ItemStack itemDust           = null;


    public static void init() {

        MinecraftForge.EVENT_BUS.register(new EventHelper());
    }

    public static void postinit() {

        if (OreDictionary.getOres("dustStone").size() > 0) {
            itemDust = OreDictionary.getOres("dustStone").get(0).copy();
            itemDust.stackSize = 1;
        }
    }


    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent event) {

        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            Location vLocation = Hydraulicraft.tankList.isLocationInTank(event.pos);
            if (vLocation != null) {
                //Open a GUI.
                event.entityPlayer.openGui(Hydraulicraft.instance, GuiIDs.TANK.ordinal(), event.world, vLocation.getX(), vLocation.getY(), vLocation.getZ());
            }
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BreakEvent event) {

        if (event.world.isRemote) {
            return;
        }
        if (event.state.getBlock() == HCBlocks.hydraulicPressureWall || event.state.getBlock() == HCBlocks.blockValve) {
            //check all directions for a hydraulic washer
            boolean breakAll = false;
            for (int horiz = -2; horiz <= 2; horiz++) {
                for (int vert = -2; vert <= 2; vert++) {
                    for (int depth = -2; depth <= 2; depth++) {
                        int x = event.pos.getX() + horiz;
                        int y = event.pos.getY() + vert;
                        int z = event.pos.getZ() + depth;
                        Block block = event.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                        if (block instanceof BlockHydraulicWasher) {
                            TileHydraulicWasher washer = (TileHydraulicWasher) event.world.getTileEntity(new BlockPos(x, y, z));
                            washer.invalidateMultiblock();
                            breakAll = true;
                            break;
                        }
                        //Log.info("Checking " + (event.x + horiz) + "," +(event.y + vert) + "," + (event.z + depth) + " = " + blockId);
                    }
                    if (breakAll) {
                        break;
                    }
                }
                if (breakAll) {
                    break;
                }
            }
        } else {
            Location vLocation = Hydraulicraft.tankList.isLocationInTank(event.pos);
            if (vLocation != null) {
                ((TileInterfaceValve) vLocation.getTE(event.world)).breakTank();
            }
        }
    }

    @SubscribeEvent
    public void onBlockBreakDrill(BlockEvent.HarvestDropsEvent event) {

        if (event.harvester == null)
            return;

        ItemStack heldItem = event.harvester.getHeldItem();
        if (heldItem == null || !(heldItem.getItem() instanceof ItemHydraulicDrill))
            return;

        List<ItemStack> drops = event.drops;
        List<ItemStack> newDrops = new ArrayList<ItemStack>();
        Iterator<ItemStack> iterator = drops.iterator();
        while (iterator.hasNext()) {
            ItemStack stack = iterator.next();
            int[] ores = OreDictionary.getOreIDs(stack);
            for (int oreId : ores) {
                String oreName = OreDictionary.getOreName(oreId);
                if (oreName.startsWith("ore")) {
                    String oreType = oreName.substring(3);
                    List<ItemStack> oreDusts = OreDictionary.getOres("dust" + oreType);
                    if (oreDusts.size() != 0) {
                        iterator.remove();
                        ItemStack toAdd = oreDusts.get(0).copy();
                        toAdd.stackSize = 2;
                        newDrops.add(toAdd); // drop 2 dusts
                        if (itemDust != null)
                            newDrops.add(itemDust.copy());
                    }
                } else if (oreName.equals("cobblestone") && itemDust != null) {
                    newDrops.add(itemDust.copy());
                    iterator.remove();
                }
            }
        }

        drops.addAll(newDrops);
    }

    @SubscribeEvent
    public void onDeathEvent(LivingDeathEvent event) {

        if (event.entity instanceof EntityPig) {
            if (!event.entity.worldObj.isRemote) {
                if(!HCConfig.INSTANCE.getBool("enableBacon"))
                    return;
                //Chance for bacon to drop, config ofcourse
                if ((new Random()).nextDouble() < HCConfig.INSTANCE.getDouble("baconDropChance")) {
                    EntityItem ei = new EntityItem(event.entityLiving.worldObj);
                    ei.setEntityItemStack(new ItemStack(HCItems.itemBacon, 1));
                    ei.setPosition(event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ);
                    event.entityLiving.worldObj.spawnEntityInWorld(ei);
                }
            }
        }
    }

	/*@SubscribeEvent
    public void onEntityJoinEvent(EntityJoinWorldEvent event){
		if(hasShownUpdateInfo || !HCConfig.INSTANCE.getBool("checkForUpdates")) return;
		if(event.entity instanceof EntityPlayer){
			if(event.world.isRemote){
				//If update available and the update message wasn't sent to Version Checker, tell em!
				if(UpdateChecker.isUpdateAvailable && !Loader.isModLoaded("VersionChecker")){
					UpdateInfo info = UpdateChecker.infoAboutUpdate;
					Functions.showMessageInChat(((EntityPlayer)event.entity), Localization.getString(Localization.GUI_UPDATEAVAILABLE, info.latestVersion));
					Functions.showMessageInChat(((EntityPlayer)event.entity), Localization.getString(Localization.GUI_RELEASEDAT, info.dateOfRelease));
					int i = 0;
					for(String cl : info.changelog){
						Functions.showMessageInChat(((EntityPlayer)event.entity), cl);						
						
						i++;
						if(i >= 3){
							break;
						}
					}
					hasShownUpdateInfo = true;
				}else{
					Functions.showMessageInChat(((EntityPlayer) event.entity), Localization.getString(Localization.GUI_UPTODATE) + " (" + ModInfo.VERSION + ")");
					hasShownUpdateInfo = true;
				}
			}
		}
	}*/

    @SubscribeEvent
    public void onBucketFill(FillBucketEvent event) {

        IBlockState state = event.world.getBlockState(event.target.getBlockPos());
        if (state.getBlock() instanceof IFluidBlock) {
            Fluid fluid = ((IFluidBlock) state.getBlock()).getFluid();
            FluidStack fs = new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME);

            ItemStack filled = FluidContainerRegistry.fillFluidContainer(fs, event.current);
            if (filled != null) {
                event.result = filled;
                event.world.setBlockToAir(event.target.getBlockPos());
                event.setResult(Event.Result.ALLOW);
            }
        }
    }

    @SubscribeEvent
    public void endermanTeleportEvent(EnderTeleportEvent event){
        if(!event.entity.getEntityWorld().isRemote){
            return;
        }
        if(!(event.entityLiving instanceof EntityEnderman)){
            return;
        }
        for(TileJarOfDirt jod: Hydraulicraft.jarOfDirtList){
            Location l = jod.getLocation();
            if(l.getDifference(event.entity.getPosition()) < 64){
                event.targetX = l.getX() + 1;
                event.targetY = l.getY();
                event.targetZ = l.getZ();
                event.entityLiving.motionX = 0;
                event.entityLiving.motionY = 0;
                event.entityLiving.motionZ = 0;
            }
        }
    }
}
