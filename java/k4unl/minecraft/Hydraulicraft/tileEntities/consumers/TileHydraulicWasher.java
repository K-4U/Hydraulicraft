package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockBase;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicTieredBlockBase;
import k4unl.minecraft.Hydraulicraft.blocks.IHydraulicMultiBlock;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockHydraulicCore;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockHydraulicPressureWall;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockHydraulicValve;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockInterfaceValve;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.recipes.HydraulicRecipes;
import k4unl.minecraft.Hydraulicraft.lib.recipes.IFluidCraftingMachine;
import k4unl.minecraft.Hydraulicraft.lib.recipes.InventoryFluidCrafting;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.Hydraulicraft.tileEntities.interfaces.IConnectTexture;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileHydraulicValve;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileInterfaceValve;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fluids.*;

import java.util.ArrayList;
import java.util.List;

public class TileHydraulicWasher extends TileHydraulicBase implements
        ISidedInventory, IFluidHandler, IHydraulicConsumer, IHydraulicMultiBlock, IConnectTexture, IFluidCraftingMachine {

    private ItemStack fluidInputInventory;
    private ItemStack fluidOutputInventory;
    private float requiredPressure = 5F;

    private boolean isValidMultiblock;

    private List<TileHydraulicValve> valves;
    private TileInterfaceValve fluidValve;
    private TileInterfaceValve itemValve;
    private PressureTier tier = PressureTier.INVALID;
    private float pressurePerTick = 0F;
    private PressureTier pressureTier;

    private InventoryFluidCrafting inventory;
    private IFluidRecipe recipe;
    private ItemStack inventor;
    private ItemStack targetItem;
    private int maximumTicks;

    public TileHydraulicWasher() {

        super(10);
        super.init(this);
        valves = new ArrayList<TileHydraulicValve>();
        FluidTank[] inputTanks = new FluidTank[]{new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 50)};
        inventory = new InventoryFluidCrafting(this, 1, inputTanks, null);
    }

    public boolean getIsValidMultiblock() {

        return isValidMultiblock;
    }

    public int getWashingTicks() {
        return (int) inventory.getProgress();
    }

    public boolean isWashing() {
        return inventory.isCraftingInProgress();
    }

    @Override
    public float workFunction(boolean simulate, EnumFacing from) {
        if (recipe != null) {
            float usedPressure = recipe.getPressure();

            if (!inventory.canWork(recipe))
                return 0;

            float maxPressure = Functions.getMaxPressurePerTier(pNetwork.getLowestTier(), true);
            float ratio = getPressure(EnumFacing.UP) / maxPressure;
            //int ticks = (int) ((float) pNetwork.getLowestTier().ordinal() * 16 * ratio);
            // TODO WASHER used pressure based on the amount of it inside (same for speed)
            int ticks = 1;

            if (!simulate)
                for (int i = 0; i < ticks; i++)
                    inventory.recipeTick(recipe);

            return usedPressure * (Functions.getMaxGenPerTier(pNetwork.getLowestTier(), true) * 0.75f);
        }

        return 0;
    }


    /*!
     * Checks if the outputslot is free, if there's enough pressure in the system
     * and if the item is smeltable
     */
    private boolean canRun() {
        if (!getIsValidMultiblock()) {
            return false;
        }
        if (getPressure(EnumFacing.UP) < requiredPressure) {
            return false;
        }

        return true;
    }

    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        if (i < inventory.getSizeInventory())
            return inventory.getStackInSlot(i);

        // TODO possibly move into the inventory too (custom tank filling inventory)

        switch (i) {
            case 2:
                return fluidInputInventory;
            case 3:
                return fluidOutputInventory;
            default:
                return null;

        }
    }

    @Override
    public ItemStack decrStackSize(int slot, int decrBy) {
        if (slot < inventory.getSizeInventory())
            return inventory.decrStackSize(slot, decrBy);

        if (slot == 2) {
            ItemStack ret;
            if (fluidInputInventory.stackSize < decrBy) {
                ret = fluidInputInventory;
                fluidInputInventory = null;
                worldObj.markBlockForUpdate(getPos());
                return ret;
            } else {
                ret = fluidInputInventory.splitStack(decrBy);
                if (fluidInputInventory.stackSize <= 0) {
                    fluidInputInventory = null;
                }
                worldObj.markBlockForUpdate(getPos());
                return ret;
            }
        } else {
            ItemStack ret;
            if (fluidOutputInventory.stackSize < decrBy) {
                ret = fluidOutputInventory;
                fluidOutputInventory = null;
                worldObj.markBlockForUpdate(getPos());
                return ret;
            } else {
                ret = fluidOutputInventory.splitStack(decrBy);
                if (fluidOutputInventory.stackSize <= 0) {
                    fluidOutputInventory = null;
                }
                worldObj.markBlockForUpdate(getPos());
                return ret;
            }
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return decrStackSize(index, getStackInSlot(index).stackSize);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        if (slot < inventory.getSizeInventory()) {
            inventory.setInventorySlotContents(slot, itemStack);
            return;
        }

        if (slot == 2) {
            fluidInputInventory = itemStack;
            worldObj.markBlockForUpdate(getPos());
        } else if (slot == 3) {
            fluidOutputInventory = itemStack;
            worldObj.markBlockForUpdate(getPos());
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return ((worldObj.getTileEntity(getPos()) == this) &&
                player.getDistanceSq(getPos()) < 64);
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        if (i == 0 && inventory.isItemValidForSlot(i, itemStack)) {
            return true;
        } else if (i == 2) {
            if (FluidContainerRegistry.isFilledContainer(itemStack)) {
                if (FluidContainerRegistry.getFluidForFilledItem(itemStack).isFluidEqual(new FluidStack(FluidRegistry.WATER, 1))) {
                    return true;
                } else if (FluidContainerRegistry.getFluidForFilledItem(itemStack).isFluidEqual(new FluidStack(Fluids.fluidHydraulicOil, 1))) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public int[] getSlotsForFace(EnumFacing var1) {
        return new int[]{0, 1, 2};
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, EnumFacing j) {
        if (i == 0 && HCConfig.canBeWashed(itemStack)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, EnumFacing j) {
        if (i == 1) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        if (resource.getFluid() != FluidRegistry.WATER)
            return 0;

        return inventory.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        if (fluid.equals(FluidRegistry.WATER)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return inventory.getTankInfo();

    }

    @Override
    public void onBlockBreaks() {
        dropItemStackInWorld(inventory.getStackInSlot(0));
        dropItemStackInWorld(inventory.getStackInSlot(1));
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        inventory.load(tagCompound);

        isValidMultiblock = tagCompound.getBoolean("isValidMultiblock");

        tier = PressureTier.fromOrdinal(tagCompound.getInteger("tier"));
        setPressureTier(tier);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        inventory.save(tagCompound);

        tagCompound.setBoolean("isValidMultiblock", isValidMultiblock);
        if(getPressureTier() != null) {
            tagCompound.setInteger("tier", getPressureTier().toInt());
        }
    }

    @Override
    public void update() {
        super.update();
        if (!worldObj.isRemote) {
            if (worldObj.getTotalWorldTime() % 10 == 0 && !getIsValidMultiblock()) {
                if (checkMultiblock()) {
                    isValidMultiblock = true;
                    convertMultiblock();
                }
            }
        } else {
            if (isValidMultiblock && valves.size() == 0) {
                convertMultiblock();
            }
        }
    }

    public void invalidateMultiblock() {
        EnumFacing dir = (EnumFacing) getWorldObj().getBlockState(getPos()).getValue(HydraulicBlockBase.ROTATION);

        int depthMultiplier = ((dir == EnumFacing.EAST || dir == EnumFacing.WEST) ? 1 : -1);
        boolean forwardZ = ((dir == EnumFacing.NORTH) || (dir == EnumFacing.NORTH));

        int xCoord = getPos().getX();
        int yCoord = getPos().getY();
        int zCoord = getPos().getZ();
        for (int horiz = -1; horiz <= 1; horiz++) {
            for (int vert = -1; vert <= 1; vert++) {
                for (int depth = 0; depth <= 2; depth++) {
                    int x = xCoord + (forwardZ ? horiz : (depth * depthMultiplier));
                    int y = yCoord + vert;
                    int z = zCoord + (forwardZ ? (depth * depthMultiplier) : horiz);

                    BlockPos nPos = new BlockPos(x, y, z);

                    Block block = getWorldObj().getBlockState(nPos).getBlock();

                    if (block instanceof BlockHydraulicValve) {
                        TileHydraulicValve temp = (TileHydraulicValve) getWorldObj().getTileEntity(nPos);
                        temp.resetTarget();
                    }

                    getWorldObj().markBlockForUpdate(nPos);
                }
            }
        }
        isValidMultiblock = false;
        getWorldObj().markBlockForUpdate(getPos());
    }

    public boolean checkMultiblock() {
        //So, it should be this:
        // 1       2      3
        //W W W  W W W  W W W
        //W W W  W F W  W W W
        //W W W  W C W  W W W

        EnumFacing dir = (EnumFacing) getWorldObj().getBlockState(getPos()).getValue(HydraulicBlockBase.ROTATION);

        int depthMultiplier = ((dir == EnumFacing.EAST || dir == EnumFacing.WEST) ? 1 : -1);
        boolean forwardZ = ((dir == EnumFacing.NORTH) || (dir == EnumFacing.NORTH));

        int xCoord = getPos().getX();
        int yCoord = getPos().getY();
        int zCoord = getPos().getZ();

        boolean hasAtLeastOneValve = false;
        boolean hasFluidValve = false;
        boolean hasItemValve = false;
        for (int horiz = -1; horiz <= 1; horiz++) {
            for (int vert = -1; vert <= 1; vert++) {
                for (int depth = 0; depth <= 2; depth++) {
                    int x = xCoord + (forwardZ ? horiz : (depth * depthMultiplier));
                    int y = yCoord + vert;
                    int z = zCoord + (forwardZ ? (depth * depthMultiplier) : horiz);

                    BlockPos nPos = new BlockPos(x, y, z);
                    Block block = getWorldObj().getBlockState(nPos).getBlock();


                    if (horiz == 0 && vert == 0) {
                        if (depth == 0) { //Looking at self.
                            continue;
                        }

                        if (depth == 1) {
                            if (!(block instanceof BlockHydraulicCore)) {
                                return false;
                            } else {
                                tier = (PressureTier) getWorldObj().getBlockState(nPos).getValue(HydraulicTieredBlockBase.TIER);
                                setPressureTier(tier);
                                continue;
                            }
                        }
                    }

                    if (!(block instanceof BlockHydraulicPressureWall)) {
                        if (block instanceof BlockHydraulicValve) {
                            hasAtLeastOneValve = true;
                        } else if (block instanceof BlockInterfaceValve) {
                            if (hasFluidValve = true) {
                                hasItemValve = true;
                            } else {
                                hasFluidValve = true;
                            }
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        return (hasAtLeastOneValve && (hasFluidValve || hasItemValve));
    }

    public void convertMultiblock() {
        isValidMultiblock = true;

        EnumFacing dir = (EnumFacing) getWorldObj().getBlockState(getPos()).getValue(HydraulicBlockBase.ROTATION);

        int depthMultiplier = ((dir == EnumFacing.EAST || dir == EnumFacing.WEST) ? 1 : -1);
        boolean forwardZ = ((dir == EnumFacing.NORTH) || (dir == EnumFacing.NORTH));

        int xCoord = getPos().getX();
        int yCoord = getPos().getY();
        int zCoord = getPos().getZ();
        valves.clear();

        for (int horiz = -1; horiz <= 1; horiz++) {
            for (int vert = -1; vert <= 1; vert++) {
                for (int depth = 0; depth <= 2; depth++) {
                    int x = xCoord + (forwardZ ? horiz : (depth * depthMultiplier));
                    int y = yCoord + vert;
                    int z = zCoord + (forwardZ ? (depth * depthMultiplier) : horiz);
                    BlockPos nPos = new BlockPos(x, y, z);
                    Block block = getWorldObj().getBlockState(nPos).getBlock();

                    if (horiz == 0 && vert == 0 && depth == 0)
                        continue;

                    if (block instanceof BlockHydraulicValve) {
                        TileHydraulicValve dummyTE = (TileHydraulicValve) getWorldObj().getTileEntity(nPos);
                        dummyTE.setTarget(getPos());
                        valves.add(dummyTE);
                        dummyTE.getHandler().updateNetworkOnNextTick(0);
                    }
                    if (block instanceof BlockInterfaceValve) {
                        if (worldObj.getTileEntity(nPos) instanceof TileInterfaceValve) {
                            TileInterfaceValve dummyTE = (TileInterfaceValve) worldObj.getTileEntity(nPos);
                            dummyTE.setTarget(getPos());
                            if (fluidValve == null) {
                                fluidValve = dummyTE;
                            } else {
                                itemValve = dummyTE;
                            }
                        } else {
                            isValidMultiblock = false;
                            break;
                        }
                    }

                    //worldObj.setBlock(x, y, z, Ids.blockDummyWasher.act);
                    //worldObj.markBlockForUpdate(x, y, z);
                    //TileDummyWasher dummyTE = (TileDummyWasher)worldObj.getBlockTileEntity(x, y, z);
                    //dummyTE.setCore(getPos());

                    //dummyTE.setLocationInMultiBlock(dir, horiz, vert, depth);
                    getWorldObj().markBlockForUpdate(getPos());

                }
            }
        }
        float p = 0;
        if (pNetwork != null) {
            getPressure(EnumFacing.UP);
        }
        getHandler().updateNetworkOnNextTick(p);
    }

    @Override
    public void onFluidLevelChanged(int old) {
    }

    @Override
    public boolean canConnectTo(EnumFacing side) {
        return true;
    }

    @Override
    public void firstTick() {
        super.firstTick();
        if (isValidMultiblock) {
            convertMultiblock();
        }
    }


    @Override
    public boolean canWork(EnumFacing dir) {
        if (getNetwork(dir) == null) {
            return false;
        }
        return dir.equals(EnumFacing.UP);
    }

    @Override
    public void updateNetwork(float oldPressure) {
        if (!isValidMultiblock) {
            getHandler().updateNetworkOnNextTick(oldPressure);
            return;
        } else {
            if (valves.size() > 0) {

            } else {
                getHandler().updateNetworkOnNextTick(oldPressure);
            }
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.invalidateMultiblock();
    }

    @Override
    public List<TileHydraulicValve> getValves() {
        return valves;
    }


    @Override
    public String getName() {
        return Localization.getLocalizedName(Names.blockHydraulicWasher.unlocalized);
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    @Override
    public IChatComponent getDisplayName() {
        return new ChatComponentTranslation(Names.blockHydraulicWasher.unlocalized);
    }

    @Override
    public PressureTier getPressureTier() {
        return this.pressureTier;
    }

    public void setPressureTier(PressureTier pressureTier) {
        this.pressureTier = pressureTier;
    }

    @Override
    public boolean connectTexture() {
        return getIsValidMultiblock();
    }

    @Override
    public boolean connectTextureTo(Block type) {
        return type instanceof BlockInterfaceValve;
    }

    @Override
    public void onCraftingMatrixChanged() {
        if (inventory.isCraftingInProgress())
            return;

        recipe = HydraulicRecipes.getWasherRecipe(inventory);
        if (recipe != null)
            inventory.startRecipe(recipe);

        markDirty();
    }

    @Override
    public void spawnOverflowItemStack(ItemStack stack) {
        worldObj.spawnEntityInWorld(new EntityItem(worldObj, getPos().getX(), getPos().getY(), getPos().getZ(), stack));
    }

    public ItemStack getWashingItem() {
        if (recipe == null)
            return null;

        return (ItemStack) recipe.getInputItems()[0];
    }

    public ItemStack getTargetItem() {
        if (recipe == null)
            return null;

        return recipe.getRecipeOutput();
    }

    public int getMaximumTicks() {
        if (recipe == null)
            return -1;

        return recipe.getCraftingTime();
    }
}
