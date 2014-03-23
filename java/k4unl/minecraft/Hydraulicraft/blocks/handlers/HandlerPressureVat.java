package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import java.util.List;

import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlockHandler;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicraftBlocks;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidRegistry;

public class HandlerPressureVat extends MachineTieredBlockHandler {

	public HandlerPressureVat(Block block) {
		super(block, Names.blockHydraulicPressurevat);
	}
	
	@Override
	public IIcon getIconFromDamage(int metadata) {
		return HydraulicraftBlocks.hydraulicPressurevat.getIcon(0, metadata);
	}

	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean par4){
		super.addInformation(itemstack, player, list, par4);
		String fluidName = FluidRegistry.WATER.getLocalizedName();
		float fluidStored = 0F;
		float fluidMax = 0F;
		float pressureStored = 0F;
		float pressureMax = 0F;
		if(itemstack.getTagCompound() != null && itemstack.getTagCompound().getBoolean("hasBeenPlaced")){
			if(itemstack.getTagCompound().getBoolean("isOilStored")){
				fluidName = Fluids.fluidOil.getLocalizedName(); 
			}
			
			if(itemstack.getTagCompound().getCompoundTag("tank") != null){
				NBTTagCompound tankCompound = itemstack.getTagCompound().getCompoundTag("tank");
				fluidStored = tankCompound.getInteger("Amount");
			}
			fluidMax = itemstack.getTagCompound().getInteger("maxStorage");
			
			
			pressureMax = itemstack.getTagCompound().getFloat("maxPressure");
			pressureStored = itemstack.getTagCompound().getFloat("oldPressure");			
		}
		
		list.add("Fluid: " + fluidName);
		list.add(fluidStored + " / " + fluidMax + "mB");
		list.add(pressureStored + " / " + pressureMax + "mBar");
	}
	
}
