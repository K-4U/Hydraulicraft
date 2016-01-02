package k4unl.minecraft.Hydraulicraft.items.divingSuit;

import k4unl.minecraft.Hydraulicraft.api.IPressureDivingSuit;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.DamageSourceHydraulicraft;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import java.util.List;

public class ItemDivingHelmet extends ItemDivingSuit implements IFluidContainerItem, IPressureDivingSuit {
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
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemStack, EntityPlayer player, List lines, boolean noIdea) {
        super.addInformation(itemStack, player, lines, noIdea);
        FluidStack stack = fetchFluidOrCreate(itemStack);
        if (stack.getFluid() != null)
            lines.add(stack.getFluid().getLocalizedName(stack) + ": " + stack.amount + "mB/" + TANK_CAPACITY + "mB");
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        super.onArmorTick(world, player, itemStack);
        //Log.info("Tick");
        NBTTagCompound entityData = player.getEntityData();
        if (entityData.getBoolean("isWearingFullScubaSuit")) {
            //Do 10 damage
            if (HCConfig.INSTANCE.getBool("doScubaDamage")) {
                FluidStack fluidStack = fetchFluidOrCreate(itemStack);
                if (fluidStack.amount > 0) {
                    doDamage(player);
                }
            }

            if (world.getTotalWorldTime() % 10 == 0) {
                FluidStack fluidStack = fetchFluidOrCreate(itemStack);
                //Drain 1 millibucket every tick?
                int drained = drain(itemStack, 10, false).amount;
                if (drained > 0) {
                    drain(itemStack, drained, true);
                }
                float percentage = ((float) fluidStack.amount) / ((float) TANK_CAPACITY);
                if (percentage == 0.0F) {
                    player.attackEntityFrom(DamageSourceHydraulicraft.noFluid, 100.0F);
                } else {
                    player.setAir((int) (300F * percentage));
                    player.addPotionEffect(new PotionEffect(16, 800));
                }

            }
        }
    }

    @Override
    public boolean isPressureSafe(EntityPlayer player, ItemStack stack, int pressure) {
        FluidStack fluidStack = fetchFluidOrCreate(stack);
        return (fluidStack.getFluid() != null && fluidStack.amount > 0);
    }
}
