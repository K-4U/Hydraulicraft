package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.api.IPressurizableItem;
import k4unl.minecraft.Hydraulicraft.api.IPressurizableItemUpgrade;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author Koen Beckers (K-4U)
 */
public class ItemCannister extends HydraulicItemBase implements IPressurizableItem {

    public static final float MAX_PRESSURE            = 1500 * 1000;
    public static final int   FLUID_CAPACITY          = 20;
    public static final float CHANCE_TO_RELEASE_WATER = 0.1f;

    public ItemCannister() {

        super(Names.itemCannister, true);
        setNoRepair();
        maxStackSize = 1;

    }

    @Override
    public float getPressure(ItemStack itemStack) {

        return fetchPressure(itemStack);
    }

    @Override
    public void setPressure(ItemStack itemStack, float newStored) {

        savePressure(itemStack, newStored);
    }

    @Override
    public float getMaxPressure() {

        return MAX_PRESSURE;
    }

    @Override
    public FluidStack getFluid(ItemStack itemStack) {

        return fetchFluidOrCreate(itemStack);
    }

    @Override
    public void setFluid(ItemStack itemStack, FluidStack fluidStack) {

        saveFluid(itemStack, fluidStack);
    }

    @Override
    public float getMaxFluid() {

        return FLUID_CAPACITY;
    }

    @Override
    public List<IPressurizableItemUpgrade> getUpgrades() {

        return null;
    }

    @Override
    public boolean addUpgrade(IPressurizableItemUpgrade upgrade) {

        return false;
    }

    @Override
    public void removeUpgrade(IPressurizableItem upgrade) {

    }

    @Override
    public int getItemStackLimit(ItemStack stack) {

        return 1;
    }

    private float fetchPressure(ItemStack container) {

        if (container.getTagCompound() == null || container.getTagCompound().getTag("pressure") == null) {
            container.setTagCompound(new NBTTagCompound());
            container.getTagCompound().setFloat("pressure", 0);
        }

        return container.getTagCompound().getFloat("pressure");
    }

    private FluidStack fetchFluidOrCreate(ItemStack container) {

        if (container.getTagCompound() == null || container.getTagCompound().getTag("fluid") == null) {
            if (container.getTagCompound() == null)
                container.setTagCompound(new NBTTagCompound());
            container.getTagCompound().setTag("fluid", new NBTTagCompound());
        }

        FluidStack existing = FluidStack.loadFluidStackFromNBT((NBTTagCompound) container.getTagCompound().getTag("fluid"));

        if (existing != null)
            saveFluid(container, existing);

        return existing;
    }

    private void saveFluid(ItemStack container, FluidStack newFluid) {

        if (container.getTagCompound() == null)
            container.setTagCompound(new NBTTagCompound());

        container.getTagCompound().setTag("fluid", newFluid.writeToNBT(new NBTTagCompound()));
    }

    private void savePressure(ItemStack container, float newPressure) {

        if (container.getTagCompound() == null)
            container.setTagCompound(new NBTTagCompound());

        container.getTagCompound().setFloat("pressure", newPressure);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List lines, boolean noIdea) {

        super.addInformation(itemStack, player, lines, noIdea);
        float pressure = fetchPressure(itemStack);
        FluidStack fluidStack = fetchFluidOrCreate(itemStack);
        lines.add("Pressure: " + Math.floor(pressure / 1000) + " Bar/" + Math.round(getMaxPressure() / 1000) + " Bar");
        if (fluidStack != null)
            lines.add(fluidStack.getFluid().getLocalizedName(fluidStack) + ": " + fluidStack.amount + " mB/" + FLUID_CAPACITY + " mB");
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {

        par3List.add(new ItemStack(par1, 1, 0));
        ItemStack filled = new ItemStack(par1, 1);
        savePressure(filled, getMaxPressure());
        setFluid(filled, new FluidStack(Fluids.fluidHydraulicOil, (int) getMaxFluid()));
        par3List.add(filled);
    }
}
