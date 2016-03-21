package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicPump;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileHydraulicValve;
import k4unl.minecraft.Hydraulicraft.tileEntities.storage.TileHydraulicPressureReservoir;
import k4unl.minecraft.Hydraulicraft.tileEntities.worldgen.TileRubberWood;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDebug extends HydraulicItemBase {

    public ItemDebug() {

        super(Names.itemDebugger, true);

        this.maxStackSize = 1;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            TileEntity ent = worldIn.getTileEntity(pos);
            if (ent != null) {
                if (ent instanceof IHydraulicMachine/* || ent instanceof TileMultipart*/) {
                    IHydraulicMachine mEnt = null;
                    /*if(ent instanceof TileMultipart){
						if(Multipart.hasTransporter((TileMultipart)ent)){
							mEnt = Multipart.getTransporter((TileMultipart)ent);
						}
						if(Multipart.hasPartFluidPipe((TileMultipart)ent)) {
							if(Multipart.getFluidPipe((TileMultipart)ent).getFluidStored() != null) {
								Functions.showMessageInChat(player, "FL: " + Multipart.getFluidPipe((TileMultipart) ent).getFluidStored().getName() + " " + Multipart.getFluidPipe((TileMultipart) ent).getFluidAmountStored());
							}else{
								Functions.showMessageInChat(player, "FL: null " + Multipart.getFluidPipe((TileMultipart) ent).getFluidAmountStored());
							}
						}
						if(Multipart.hasPartFluidHandler((TileMultipart) ent)){
							FluidTankInfo tankInfo = Multipart.getFluidHandler((TileMultipart)ent).getTankInfo(EnumFacing.UNKNOWN)[0];
							if(tankInfo.fluid != null) {
								Functions.showMessageInChat(player, "T: " + tankInfo.fluid.amount + "/" + tankInfo.capacity);
							}else{
								Functions.showMessageInChat(player, "T: null/" + tankInfo.capacity);
							}
						}
						if(!Multipart.hasTransporter((TileMultipart)ent)){
							return false;
						}
					}else{*/
                    mEnt = (IHydraulicMachine) ent;
//					}
                    NBTTagCompound tagC = stack.getTagCompound();
                    if (tagC == null) {
                        tagC = new NBTTagCompound();
                    }

                    int stored = mEnt.getHandler().getStored();
                    int max = mEnt.getHandler().getMaxStorage();

                    float pressure = mEnt.getHandler().getPressure(EnumFacing.UP);
                    float maxPressure = mEnt.getHandler().getMaxPressure(mEnt.getHandler().isOilStored(), EnumFacing.UP);

                    float prevPressure = tagC.getFloat("prevPressure");
                    int prevFluid = tagC.getInteger("prevFluid");

                    Functions.showMessageInChat(playerIn, "Stored liquid: " + stored + "/" + max + " milliBuckets (+" + (stored - prevFluid) + ")");
                    Functions.showMessageInChat(playerIn, "Pressure:     " + pressure + "/" + maxPressure + " mBar (+" + (pressure - prevPressure) + ")");

                    tagC.setFloat("prevPressure", pressure);
                    tagC.setInteger("prevFluid", stored);

                    if (ent instanceof TileHydraulicPressureReservoir) {
                        PressureTier tier = ((TileHydraulicPressureReservoir) ent).getTier();
                        Functions.showMessageInChat(playerIn, "Tier:          " + tier);
                    }

					/*if(ent instanceof TileMultipart){
						if(Multipart.hasPartHose((TileMultipart)ent)){
							PartHose hose = Multipart.getHose((TileMultipart)ent);
							int tier = hose.getTier().toInt();
							Functions.showMessageInChat(playerIn, "Tier:          " + tier);
						}
					}*/

                    if (ent instanceof TileHydraulicValve) {
                        TileHydraulicValve v = (TileHydraulicValve) ent;
                        if (v.getTarget() != null) {
                            Functions.showMessageInChat(playerIn, "Target: " + v.getBlockLocation().printCoords());
                        }
                    }

                    if (ent instanceof TileHydraulicPiston) {
                        TileHydraulicPiston p = ((TileHydraulicPiston) ent);
                        Functions.showMessageInChat(playerIn, "Length: " + p.getExtendedLength());
                        Functions.showMessageInChat(playerIn, "Target: " + p.getExtendTarget());
                    }

                    if (ent instanceof IHydraulicGenerator) {
                        float gen = ((IHydraulicGenerator) ent).getGenerating(EnumFacing.UP);
                        int maxGen = ((IHydraulicGenerator) ent).getMaxGenerating(EnumFacing.UP);
                        Functions.showMessageInChat(playerIn, "Generating:    " + gen + "/" + maxGen);
                        if (ent instanceof TileHydraulicPump) {
                            int tier = ((TileHydraulicPump) ent).getTier();
                            Functions.showMessageInChat(playerIn, "Tier:          " + tier);
                        }
                    }

                    if (((TileHydraulicBase) mEnt.getHandler()).getNetwork(EnumFacing.UP) != null) {
                        Functions.showMessageInChat(playerIn, "Network ID:    " + ((TileHydraulicBase) mEnt.getHandler()).getNetwork(EnumFacing.UP).getRandomNumber());
                    }

                    stack.setTagCompound(tagC);

                    return EnumActionResult.SUCCESS;
                } else if(ent instanceof TileRubberWood){
                    if(playerIn.isSneaking()){
                        ((TileRubberWood)ent).randomTick();
                    }
                    Functions.showMessageInChat(playerIn, "Rubber: " + ((TileRubberWood)ent).getRubber());
                }
            }
        }
        return EnumActionResult.FAIL;
    }
}
