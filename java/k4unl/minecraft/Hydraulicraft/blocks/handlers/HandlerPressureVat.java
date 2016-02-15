package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class HandlerPressureVat extends HydraulicTieredBlockHandler {

    public HandlerPressureVat(Block block) {

        super(block, Names.blockHydraulicPressureReservoir);
    }

    /*
    @Override
    public IIcon getIconFromDamage(int metadata) {
        return HCBlocks.hydraulicPressurevat.getIcon(0, metadata);
    }
*/
    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean par4) {

        super.addInformation(itemstack, player, list, par4);
        String fluidName = FluidRegistry.WATER.getLocalizedName(new FluidStack(FluidRegistry.WATER, 1));
        float fluidStored = 0F;
        float fluidMax = 0F;
        float pressureStored = 0F;
        float pressureMax = 0F;
        if (itemstack.getTagCompound() != null && itemstack.getTagCompound().getBoolean("hasBeenPlaced")) {
            if (itemstack.getTagCompound().getBoolean("isOilStored")) {
                fluidName = Fluids.fluidHydraulicOil.getLocalizedName(new FluidStack(Fluids.fluidHydraulicOil, 1));
            }

            if (itemstack.getTagCompound().getCompoundTag("tank") != null) {
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
