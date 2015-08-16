package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.api.IPressurizableItem;
import k4unl.minecraft.Hydraulicraft.lib.PressurizableItem;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Set;

public abstract class ItemHydraulicTool extends ItemTool implements IPressurizableItem {
    public static final float MAX_PRESSURE            = 1500 * 1000;
    public static final float PRESSURE_PER_DIG        = 1000;
    public static final int   FLUID_CAPACITY          = 20;
    public static final float CHANCE_TO_RELEASE_WATER = 0.1f;

    PressurizableItem pressurizableItem;


    protected ItemHydraulicTool(float damage, ToolMaterial material, Set worksOn) {
        super(damage, material, worksOn);
    }

    @Override
    public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {
        return pressurizableItem.canUse(itemStack, PRESSURE_PER_DIG);
    }

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta) {
        if (!pressurizableItem.canUse(stack, PRESSURE_PER_DIG))
            return 0;

        return super.getDigSpeed(stack, block, meta);
    }

    @Override
    public float func_150893_a(ItemStack stack, Block block) {
        if (!pressurizableItem.canUse(stack, PRESSURE_PER_DIG))
            return 0;

        return 1;
    }

    /* IPressurizableItem */
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
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase player) {
        if (player instanceof EntityPlayer) {
            boolean retval = pressurizableItem.canUse((EntityPlayer) player, PRESSURE_PER_DIG);
            if (retval)
                pressurizableItem.onItemUse((EntityPlayer) player, CHANCE_TO_RELEASE_WATER, PRESSURE_PER_DIG);

            return retval;
        }

        return super.onBlockDestroyed(stack, world, block, x, y, z, player);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        pressurizableItem.onItemUse(player, CHANCE_TO_RELEASE_WATER, PRESSURE_PER_DIG);
        return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack == null || !(stack.getItem() instanceof ItemHydraulicTool))
            return 1;

        ItemHydraulicTool drill = (ItemHydraulicTool) stack.getItem();
        if (drill.getFluid(stack) == null || drill.getFluid(stack).amount == 0)
            return 1;

        return 1 - drill.getPressure(stack) / drill.getMaxPressure();
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean something) {
        super.addInformation(stack, player, list, something);
        pressurizableItem.addInformation(stack, list);
    }

}
