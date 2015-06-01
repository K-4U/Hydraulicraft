package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.multipart.Multipart;
import k4unl.minecraft.Hydraulicraft.multipart.PartHose;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicPump;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileHydraulicValve;
import k4unl.minecraft.Hydraulicraft.tileEntities.storage.TileHydraulicPressureVat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.multipart.TileMultipart;
import net.minecraftforge.fluids.FluidTankInfo;

public class ItemDebug extends HydraulicItemBase {

	public ItemDebug() {
		super(Names.itemDebugger);
		
		this.maxStackSize = 1;
	}

	
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float par7, float par8, float par9){
		if(!world.isRemote){
			TileEntity ent = world.getTileEntity(x, y, z);
			if(ent != null){
				if(ent instanceof IHydraulicMachine || ent instanceof TileMultipart){
					IHydraulicMachine mEnt = null;
					if(ent instanceof TileMultipart){
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
							FluidTankInfo tankInfo = Multipart.getFluidHandler((TileMultipart)ent).getTankInfo(ForgeDirection.UNKNOWN)[0];
							if(tankInfo.fluid != null) {
								Functions.showMessageInChat(player, "T: " + tankInfo.fluid.amount + "/" + tankInfo.capacity);
							}else{
								Functions.showMessageInChat(player, "T: null/" + tankInfo.capacity);
							}
						}
						if(!Multipart.hasTransporter((TileMultipart)ent)){
							return false;
						}
					}else{
						mEnt = (IHydraulicMachine) ent;
					}
					NBTTagCompound tagC = itemStack.getTagCompound();
					if(tagC == null){
						tagC = new NBTTagCompound();
					}
					
					int stored = mEnt.getHandler().getStored();
					int max = mEnt.getHandler().getMaxStorage();
					
					float pressure = mEnt.getHandler().getPressure(ForgeDirection.UNKNOWN);
					float maxPressure = mEnt.getHandler().getMaxPressure(mEnt.getHandler().isOilStored(), ForgeDirection.UNKNOWN);
					
					float prevPressure = tagC.getFloat("prevPressure");
					int prevFluid = tagC.getInteger("prevFluid");
					
					Functions.showMessageInChat(player, "Stored liquid: " + stored + "/" + max + " milliBuckets (+"  + (stored - prevFluid) + ")");
					Functions.showMessageInChat(player, "Pressure:     " + pressure + "/" + maxPressure + " mBar (+"  + (pressure - prevPressure) + ")");
					
					tagC.setFloat("prevPressure", pressure);
					tagC.setInteger("prevFluid", stored);
					
					if(ent instanceof TileHydraulicPressureVat){
						int tier = ((TileHydraulicPressureVat)ent).getTier();
						Functions.showMessageInChat(player, "Tier:          " + tier);						
					}
					
					
					if(ent instanceof TileMultipart){
						if(Multipart.hasPartHose((TileMultipart)ent)){
							PartHose hose = Multipart.getHose((TileMultipart)ent);
							int tier = hose.getTier().toInt();
							Functions.showMessageInChat(player, "Tier:          " + tier);							
						}
					}
					
					if(ent instanceof TileHydraulicValve){
						TileHydraulicValve v = (TileHydraulicValve) ent;
						if(v.getTarget() != null){
							Functions.showMessageInChat(player, "Target: " + v.xCoord + "," + v.yCoord + "," + v.zCoord);
						}
					}
					
					if(ent instanceof TileHydraulicPiston){
						TileHydraulicPiston p = ((TileHydraulicPiston)ent);
						Functions.showMessageInChat(player, "Length: " + p.getExtendedLength());
						Functions.showMessageInChat(player, "Target: " + p.getExtendTarget());
					}
					
					if(ent instanceof IHydraulicGenerator){
						float gen = ((IHydraulicGenerator) ent).getGenerating(ForgeDirection.UP);
						int maxGen = ((IHydraulicGenerator) ent).getMaxGenerating(ForgeDirection.UP);
						Functions.showMessageInChat(player, "Generating:    " + gen + "/" + maxGen);
						if(ent instanceof TileHydraulicPump){
							int tier = ((TileHydraulicPump) ent).getTier();
							Functions.showMessageInChat(player, "Tier:          " + tier);
						}
					}
					
					if(((TileHydraulicBase)mEnt.getHandler()).getNetwork(ForgeDirection.UNKNOWN) != null){
						Functions.showMessageInChat(player, "Network ID:    " + ((TileHydraulicBase)mEnt.getHandler()).getNetwork(ForgeDirection.UNKNOWN).getRandomNumber());
					}
					
					itemStack.setTagCompound(tagC);
					
					return true;
				}
			}
		}
		return false;
	}
}
