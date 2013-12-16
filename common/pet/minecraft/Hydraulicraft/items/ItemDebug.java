package pet.minecraft.Hydraulicraft.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicHose;
import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicPressureVat;
import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicPump;
import pet.minecraft.Hydraulicraft.baseClasses.MachineEntity;
import pet.minecraft.Hydraulicraft.baseClasses.MachineItem;
import pet.minecraft.Hydraulicraft.lib.config.Ids;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class ItemDebug extends MachineItem {

	public ItemDebug() {
		super(Ids.itemDebug, Names.itemDebugger);
	}

	
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float par7, float par8, float par9){
		if(!world.isRemote){
			TileEntity ent = world.getBlockTileEntity(x, y, z);
			if(ent != null){
				if(ent instanceof MachineEntity){
					NBTTagCompound tagC = itemStack.getTagCompound();
					if(tagC == null){
						tagC = new NBTTagCompound();
					}
					MachineEntity mEnt = (MachineEntity) ent;
					
					int stored = mEnt.getStored();
					int max = mEnt.getStorage();
					
					float pressure = mEnt.getPressure();
					float maxPressure = mEnt.getMaxPressure();
					
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
					
					if(ent instanceof TileHydraulicHose){
						int tier = ((TileHydraulicHose)ent).getTier();
						player.addChatMessage("Tier:          " + tier);						
					}
					
					if(ent instanceof TileHydraulicPump){
						float gen = ((TileHydraulicPump) ent).getGenerating();
						int maxGen = ((TileHydraulicPump) ent).getMaxGenerating();
						int tier = ((TileHydraulicPump) ent).getTier();
						player.addChatMessage("Generating:    " + gen + "/" + maxGen);
						player.addChatMessage("Tier:          " + tier);
					}
					
					itemStack.setTagCompound(tagC);
					
					return true;
				}
			}
		}
		return false;
	}
}
