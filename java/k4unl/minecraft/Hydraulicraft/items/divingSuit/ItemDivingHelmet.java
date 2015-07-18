package k4unl.minecraft.Hydraulicraft.items.divingSuit;

import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import java.util.List;

public class ItemDivingHelmet extends ItemDivingSuit implements IFluidContainerItem {
    private static final int TANK_CAPACITY = 8000;

    public ItemDivingHelmet() {
        super(0);
    }

    @Override
    public FluidStack getFluid(ItemStack container) {
        return fetchFluidOrCreate(container);
    }

    @Override
    public int getCapacity(ItemStack container) {
        return TANK_CAPACITY;
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        if (resource == null)
            return 0;

        if (resource.getFluid() != Fluids.fluidFluoroCarbonFluid)
            return 0;

        if (!(container.getItem() instanceof ItemDivingHelmet))
            return 0;

        FluidStack existing = fetchFluidOrCreate(container);

        int maxFill = Math.min(TANK_CAPACITY - existing.amount, resource.amount);
        if (doFill) {
            existing.amount += maxFill;
            saveFluidStackToItem(container, existing);
        }

        return maxFill;
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
        if (maxDrain < 0)
            return null;
        if (!(container.getItem() instanceof ItemDivingHelmet))
            return null;

        FluidStack existing = fetchFluidOrCreate(container);
        int actualDrain = Math.min(existing.amount, maxDrain);
        if (doDrain) {
            existing.amount -= actualDrain;
            saveFluidStackToItem(container, existing);
        }

        return new FluidStack(existing.getFluid(), actualDrain);
    }

    private FluidStack fetchFluidOrCreate(ItemStack container) {
        if (container.getTagCompound() == null || container.getTagCompound().getTag("internalStorage") == null) {
            container.stackTagCompound = new NBTTagCompound();
            container.stackTagCompound.setTag("internalStorage", new NBTTagCompound());
        }

        FluidStack existing = FluidStack.loadFluidStackFromNBT((NBTTagCompound) container.stackTagCompound.getTag("internalStorage"));
        if (existing == null)
            existing = new FluidStack(Fluids.fluidFluoroCarbonFluid, 0);

        saveFluidStackToItem(container, existing);

        return existing;
    }

    private void saveFluidStackToItem(ItemStack container, FluidStack newFluid) {
        if (container.getTagCompound() == null)
            container.setTagCompound(new NBTTagCompound());

        container.stackTagCompound.setTag("internalStorage", newFluid.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List lines, boolean noIdea) {
        super.addInformation(itemStack, player, lines, noIdea);
        FluidStack stack = fetchFluidOrCreate(itemStack);
        if (stack.getFluid() != null)
            lines.add(stack.getFluid().getLocalizedName(stack) + ": " + stack.amount + "mB/" + TANK_CAPACITY + "mB");
    }
}
