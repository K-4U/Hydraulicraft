package k4unl.minecraft.Hydraulicraft.thirdParty;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester.BlockHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities.TileElectricPump;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.tileEntities.storage.TileFluidTank;
import k4unl.minecraft.k4lib.lib.Functions;
import mcp.mobius.waila.api.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTankInfo;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WailaProvider implements IWailaDataProvider {

    public static void callbackRegister(IWailaRegistrar registrar) {

        registrar.registerHeadProvider(new WailaProvider(), IHydraulicMachine.class);
        registrar.registerBodyProvider(new WailaProvider(), IHydraulicMachine.class);
        registrar.registerTailProvider(new WailaProvider(), IHydraulicMachine.class);
        //registrar.registerBodyProvider(new WailaProvider(), TileMultipart.class);
        registrar.registerStackProvider(new WailaProvider(), BlockHarvesterTrolley.class);
        registrar.registerBodyProvider(new WailaProvider(), TileFluidTank.class);
        //registrar.registerBodyProvider(new WailaProvider(), Ids.blockHydraulicPump.act);
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor,
                                   IWailaConfigHandler config) {

        if (accessor.getTileEntity() instanceof TileHarvesterTrolley) {
            TileHarvesterTrolley harvesterTrolley = (TileHarvesterTrolley) accessor.getTileEntity();
            String name = harvesterTrolley.getTrolley().getName();
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setString("name", name);

            ItemStack ret = new ItemStack(accessor.getBlock());
            ret.setTagCompound(tagCompound);
            return ret;
        }
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack,
                                     List<String> currenttip, IWailaDataAccessor accessor,
                                     IWailaConfigHandler config) {

        return currenttip;
    }

    @SuppressWarnings("cast")
    @Override
    public List<String> getWailaBody(ItemStack itemStack,
                                     List<String> currenttip, IWailaDataAccessor accessor,
                                     IWailaConfigHandler config) {

        TileEntity ent = accessor.getTileEntity();
        if (accessor.getTileEntity() instanceof IHydraulicMachine/* || ent instanceof TileMultipart*/) {
            IHydraulicMachine mEnt;
            Map<String, String> values = new HashMap<String, String>();

            /*if (ent instanceof TileMultipart) {
                if (Multipart.hasTransporter((TileMultipart) ent)) {
                    mEnt = Multipart.getTransporter((TileMultipart) ent);
                } else {
                    return currenttip;
                }
            } else {*/
            mEnt = (IHydraulicMachine) ent;
            //}
            //mEnt = (IHydraulicMachine) ent;
            //IHydraulicMachine mEnt = (IHydraulicMachine) accessor.getTileEntity();

            int stored = mEnt.getHandler().getStored();
            int max = mEnt.getHandler().getMaxStorage();

            float pressure = mEnt.getHandler().getPressure(EnumFacing.UP);
            int maxPressure = (int) mEnt.getHandler().getMaxPressure(mEnt.getHandler().isOilStored(), null);

            values.put("Fl", stored + "/" + max + " mBuckets (" + (int) (((float) stored / (float) max) * 100) + "%)");
            values.put("Pr", (new DecimalFormat("#.##")).format(pressure) + "/" + maxPressure + " mBar (" + (int) ((pressure / (float) maxPressure) * 100) + "%)");

            if (mEnt instanceof IHydraulicGenerator) {
                float gen = ((IHydraulicGenerator) mEnt).getGenerating(EnumFacing.UP);
                int maxGen = ((IHydraulicGenerator) mEnt).getMaxGenerating(EnumFacing.UP);
                values.put("Gen", (new DecimalFormat("#.##")).format(gen) + "/" + maxGen);
            }
            if (mEnt instanceof TileElectricPump) {
                int storedEU = ((TileElectricPump) mEnt).getEUStored();
                //int maxEU = ((TileElectricPump) mEnt).getMaxEUStorage();
                //values.put("EU", storedEU + "/" + maxEU);
            }
            if (Functions.isInDev()) {
                values.put("C", ((TileHydraulicBase) mEnt.getHandler()).getBlockLocation().printLocation());
            }

            //Put it up there.
            for (Map.Entry<String, String> entry : values.entrySet()) {
                currenttip.add(entry.getKey() + ": " + /*SpecialChars.ALIGNRIGHT +*/ SpecialChars.WHITE + entry.getValue());
            }

        } else if (ent instanceof TileFluidTank) {
            TileFluidTank fluidTank = (TileFluidTank) ent;
            Map<String, String> values = new HashMap<String, String>();
            FluidTankInfo tankInfo = fluidTank.getTankInfo(EnumFacing.UP)[0];
            if (tankInfo != null && tankInfo.fluid != null) {
                currenttip.add("Fluid: " + SpecialChars.WHITE + tankInfo.fluid.getLocalizedName());
                currenttip.add(tankInfo.fluid.amount + "/" + tankInfo.capacity + " mB");
            } else {
                currenttip.add("Fluid: " + SpecialChars.WHITE + "None");
            }
        }
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack,
                                     List<String> currenttip, IWailaDataAccessor accessor,
                                     IWailaConfigHandler config) {

        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {

        return null;
    }
}
