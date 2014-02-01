package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TileHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.TileEntities.generator.TileHydraulicPump;
import k4unl.minecraft.Hydraulicraft.TileEntities.storage.TileHydraulicPressureVat;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineItem;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.multipart.Multipart;
import k4unl.minecraft.Hydraulicraft.multipart.PartHose;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import codechicken.multipart.TileMultipart;

public class ItemDebug extends MachineItem {

	public ItemDebug() {
		super(Ids.itemDebug, Names.itemDebugger);
		
		this.maxStackSize = 1;
	}

	
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float par7, float par8, float par9){
		if(!world.isRemote){
			TileEntity ent = world.getBlockTileEntity(x, y, z);
			if(ent != null){
				if(ent instanceof IHydraulicMachine || ent instanceof TileMultipart){
					IHydraulicMachine mEnt = null;
					if(ent instanceof TileMultipart){
						if(Multipart.hasPartHose((TileMultipart)ent)){
							mEnt = (IHydraulicMachine) Multipart.getHose((TileMultipart)ent);
						}else{
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
					int max = mEnt.getMaxStorage();
					
					float pressure = mEnt.getHandler().getPressure();
					float maxPressure = mEnt.getMaxPressure(mEnt.getHandler().isOilStored());
					
					float prevPressure = tagC.getFloat("prevPressure");
					int prevFluid = tagC.getInteger("prevFluid");
					
					player.addChatMessage("Stored liquid: " + stored + "/" + max + " milliBuckets (+"  + (stored - prevFluid) + ")");
					player.addChatMessage("Pressure:     " + pressure + "/" + maxPressure + " mBar (+"  + (pressure - prevPressure) + ")");
					
					tagC.setFloat("prevPressure", pressure);
					tagC.setInteger("prevFluid", stored);
					
					if(ent instanceof TileHydraulicPressureVat){
						int tier = ((TileHydraulicPressureVat)ent).getTier();
						player.addChatMessage("Tier:          " + tier);						
					}
					
					if(ent instanceof TileMultipart){
						if(Multipart.hasTransporter((TileMultipart)ent)){
							PartHose hose = Multipart.getHose((TileMultipart)ent);
							int tier = hose.getTier();
							player.addChatMessage("Tier:          " + tier);							
						}
					}
					
					if(ent instanceof TileHydraulicPiston){
						TileHydraulicPiston p = ((TileHydraulicPiston)ent);
						player.addChatMessage("Length: " + p.getExtendedLength());
						player.addChatMessage("Target: " + p.getExtendTarget());
					}
					
					if(ent instanceof IHydraulicGenerator){
						float gen = ((IHydraulicGenerator) ent).getGenerating();
						int maxGen = ((IHydraulicGenerator) ent).getMaxGenerating();
						player.addChatMessage("Generating:    " + gen + "/" + maxGen);
						if(ent instanceof TileHydraulicPump){
							int tier = ((TileHydraulicPump) ent).getTier();
							player.addChatMessage("Tier:          " + tier);
						}
					}
					
					itemStack.setTagCompound(tagC);
					
					return true;
				}
			}
		}
		return false;
	}
}
