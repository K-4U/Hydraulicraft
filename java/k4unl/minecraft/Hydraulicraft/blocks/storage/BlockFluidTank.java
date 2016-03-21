package k4unl.minecraft.Hydraulicraft.blocks.storage;

import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.blocks.ITooltipProvider;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.BuildcraftCompat;
import k4unl.minecraft.Hydraulicraft.tileEntities.storage.TileFluidTank;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Koen Beckers (K-4U)
 */
public class BlockFluidTank extends HydraulicBlockContainerBase implements ITooltipProvider {


    public BlockFluidTank() {

        super(Names.blockFluidTank, true);
        setSoundType(SoundType.GLASS);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int var2) {

        return new TileFluidTank();
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.INVALID;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {

        float pixel = 1F / 16F;
        Vector3fMax vector = new Vector3fMax(pixel * 2, 0, pixel * 2, 1F - (pixel * 2), 1F, 1F - (pixel * 2));

        this.setBlockBounds(vector.getXMin(), vector.getYMin(), vector.getZMin(), vector.getXMax(), vector.getYMax(), vector.getZMax());
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {

        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("fluid")) {
            NBTTagCompound storedFluid = stack.getTagCompound().getCompoundTag("fluid");
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(storedFluid);
            TileFluidTank tank = (TileFluidTank) worldIn.getTileEntity(pos);
            tank.fill(null, fluid, true);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (playerIn.isSneaking() && BuildcraftCompat.INSTANCE.isWrench(playerIn.getActiveItemStack())) {
            if (!worldIn.isRemote) {
                ItemStack stack = new ItemStack(worldIn.getBlockState(pos).getBlock());
                TileFluidTank tank = (TileFluidTank) worldIn.getTileEntity(pos);
                NBTTagCompound tag = new NBTTagCompound();
                tank.writeToNBT(tag);
                NBTTagCompound superTag = new NBTTagCompound();
                superTag.setTag("fluid", tag);
                stack.setTagCompound(superTag);
                EntityItem entityItem = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
                entityItem.motionY = worldIn.rand.nextDouble() / 2;
                entityItem.motionX = worldIn.rand.nextDouble() / 4;
                entityItem.motionZ = worldIn.rand.nextDouble() / 4;
                worldIn.spawnEntityInWorld(entityItem);
                worldIn.setBlockToAir(pos);
            }

            return true;
        }

        if (playerIn.isSneaking())
            return false;

        if (worldIn.isRemote)
            return true;

        TileEntity entity = worldIn.getTileEntity(pos);
        if (entity == null || !(entity instanceof TileFluidTank)) {
            return false;

        }

        if (playerIn.getActiveItemStack() != null) {
            ItemStack inUse = playerIn.getActiveItemStack();
            FluidStack input = FluidContainerRegistry.getFluidForFilledItem(inUse);
            TileFluidTank tank = (TileFluidTank) entity;
            if (input != null) {

                if (!worldIn.isRemote) {

                    int filled = tank.fill(EnumFacing.UP, input, false);
                    if (filled == FluidContainerRegistry.getContainerCapacity(inUse)) {
                        //Do it!
                        tank.fill(EnumFacing.UP, input, true);
                        tank.markDirty();
                        tank.markBlockForUpdate();
                        if (!playerIn.capabilities.isCreativeMode) {
                            playerIn.setHeldItem(EnumHand.MAIN_HAND, FluidContainerRegistry.drainFluidContainer(inUse));
                        }
                    }
                }
                return true;
            } else {
                if (FluidContainerRegistry.isEmptyContainer(inUse)) {
                    FluidTankInfo tankInfo = tank.getTankInfo(EnumFacing.UP)[0];
                    if (tankInfo.fluid == null) return false;
                    ItemStack filledContainer = FluidContainerRegistry.fillFluidContainer(tankInfo.fluid, inUse);
                    if (filledContainer == null) return false;
                    int toDrain = FluidContainerRegistry.getContainerCapacity(filledContainer);

                    FluidStack drained = tank.drain(EnumFacing.UP, toDrain, false);
                    if (drained != null && drained.amount == toDrain) {
                        tank.drain(EnumFacing.UP, toDrain, true);
                        tank.markDirty();
                        tank.markBlockForUpdate();
                    } else {
                        return false;
                    }


                    if (!playerIn.capabilities.isCreativeMode) {
                        playerIn.getActiveItemStack().stackSize--;
                        if (playerIn.getActiveItemStack().stackSize == 0) {
                            playerIn.setHeldItem(EnumHand.MAIN_HAND, null);
                        }
                        if (!playerIn.inventory.addItemStackToInventory(filledContainer)) {
                            //Spawn it in world:
                            EntityItem entityItem = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), filledContainer);
                            worldIn.spawnEntityInWorld(entityItem);
                        }
                    }
                    //storeItemStack
                }
            }
        }

        //playerIn.openGui(Hydraulicraft.instance, getGUIID().ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public String getToolTip(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("fluid")) {
            NBTTagCompound storedFluid = stack.getTagCompound().getCompoundTag("fluid");
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(storedFluid);
            if(fluid == null) return null;
            return fluid.getLocalizedName() + ": " + fluid.amount + "/" + FluidContainerRegistry.BUCKET_VOLUME * 16;
        }
        return null;
    }
}
