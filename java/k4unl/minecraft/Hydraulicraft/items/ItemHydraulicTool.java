package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.api.IPressurizableItem;
import k4unl.minecraft.Hydraulicraft.api.IPressurizableItemUpgrade;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.PressurizableItem;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
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


    protected ItemHydraulicTool(float damage, ToolMaterial material, Set<Block> worksOn) {

        super(damage, 1.0f, material, worksOn);
        setHasSubtypes(true);
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        return pressurizableItem.canUse(stack, PRESSURE_PER_DIG);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
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
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos, EntityLivingBase playerIn) {

        if (playerIn instanceof EntityPlayer) {
            RayTraceResult mop = getMovingObjectPositionFromPlayer(worldIn, (EntityPlayer) playerIn, true);
            boolean retval = pressurizableItem.canUse((EntityPlayer) playerIn, worldIn, pos, mop.sideHit, PRESSURE_PER_DIG);
            if (retval) {

                pressurizableItem.onItemUse((EntityPlayer) playerIn, pos, mop.sideHit, CHANCE_TO_RELEASE_WATER, PRESSURE_PER_DIG);
            }

            return retval;
        }
        return super.onBlockDestroyed(stack, worldIn, blockIn, pos, playerIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        pressurizableItem.onItemUse(playerIn, pos, facing, CHANCE_TO_RELEASE_WATER, PRESSURE_PER_DIG);
        return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {

        if (stack == null || !(stack.getItem() instanceof ItemHydraulicTool))
            return 1;

        ItemHydraulicTool tool = (ItemHydraulicTool) stack.getItem();
        if (tool.getFluid(stack) == null || tool.getFluid(stack).amount == 0)
            return 1;

        return 1 - tool.getPressure(stack) / tool.getMaxPressure();
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean something) {

        super.addInformation(stack, player, list, something);
        pressurizableItem.addInformation(stack, list);
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {

        super.getSubItems(itemIn, tab, subItems);

        // add charged one
        ItemStack charged = new ItemStack(itemIn, 1, 0);
        IPressurizableItem item = (IPressurizableItem) charged.getItem();
        item.setFluid(charged, new FluidStack(Fluids.fluidHydraulicOil, (int) item.getMaxFluid()));
        item.setPressure(charged, item.getMaxPressure());
        subItems.add(charged);
    }

    @Override
    public List<IPressurizableItemUpgrade> getUpgrades() {

        return pressurizableItem.getUpgrades();
    }

    @Override
    public boolean addUpgrade(IPressurizableItemUpgrade upgrade) {

        return pressurizableItem.addUpgrade(upgrade);
    }

    @Override
    public void removeUpgrade(IPressurizableItem upgrade) {

        pressurizableItem.removeUpgrade(upgrade);
    }
}
