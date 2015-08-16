package k4unl.minecraft.Hydraulicraft.items;

import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.api.IPressurizableItem;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.PressurizableItem;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

@Optional.Interface(iface = "buildcraft.api.tools.IToolWrench", modid = "BuildCraftAPI|core")
public class ItemHydraulicWrench extends HydraulicItemBase implements IPressurizableItem, IToolWrench {
    public static final float MAX_PRESSURE            = 1500 * 1000;
    public static final float PRESSURE_PER_WRENCH     = 1000;
    public static final int   FLUID_CAPACITY          = 20;
    public static final float CHANCE_TO_RELEASE_WATER = 0.1f;

    private PressurizableItem pressurizableItem;

    public ItemHydraulicWrench() {
        super(Names.itemHydraulicWrench);
        setNoRepair();
        maxStackSize = 1;
        pressurizableItem = new PressurizableItem(MAX_PRESSURE, FLUID_CAPACITY);
    }

    @Override
    public float getPressure(ItemStack itemStack) {
        return pressurizableItem.getPressure(itemStack);
    }

    @Override
    public void setPressure(ItemStack itemStack, float newStored) {
        pressurizableItem.setPressure(itemStack, newStored);
    }

    @Override
    public float getMaxPressure() {
        return pressurizableItem.getMaxPressure();
    }

    @Override
    public FluidStack getFluid(ItemStack itemStack) {
        return pressurizableItem.getFluid(itemStack);
    }

    @Override
    public void setFluid(ItemStack itemStack, FluidStack fluidStack) {
        pressurizableItem.setFluid(itemStack, fluidStack);
    }

    @Override
    public float getMaxFluid() {
        return pressurizableItem.getMaxFluid();
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        Block block = world.getBlock(x, y, z);
        if (block == null)
            return false;

        if (player.isSneaking())
            return false;

        if (!canWrench(player, x, y, z))
            return false;

        if (block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side))) {
            player.swingItem();
            wrenchUsed(player, x, y, z);
            return !world.isRemote;
        }

        return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
        return true;
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List lines, boolean noIdea) {
        super.addInformation(itemStack, player, lines, noIdea);
        pressurizableItem.addInformation(itemStack, lines);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack == null || !(stack.getItem() instanceof ItemDrill))
            return 1;

        ItemDrill drill = (ItemDrill) stack.getItem();
        if (drill.getFluid(stack) == null || drill.getFluid(stack).amount == 0)
            return 1;

        return 1 - drill.getPressure(stack) / drill.getMaxPressure();
    }

    @Override
    public boolean canWrench(EntityPlayer entityPlayer, int i, int i1, int i2) {
        return pressurizableItem.canUse(entityPlayer, PRESSURE_PER_WRENCH);
    }

    @Override
    public void wrenchUsed(EntityPlayer entityPlayer, int i, int i1, int i2) {
        pressurizableItem.onItemUse(entityPlayer, CHANCE_TO_RELEASE_WATER, PRESSURE_PER_WRENCH);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        ItemStack filled = new ItemStack(par1, 1);
        pressurizableItem.savePressure(filled, getMaxPressure());
        setFluid(filled, new FluidStack(Fluids.fluidHydraulicOil, (int) getMaxFluid()));
        par3List.add(filled);
    }
}
