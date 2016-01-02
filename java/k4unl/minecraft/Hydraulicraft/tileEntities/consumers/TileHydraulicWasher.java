package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
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
import net.minecraftforge.common.util.ForgeDirection;
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
    private TileInterfaceValve       fluidValve;
    private TileInterfaceValve       itemValve;
    private int   tier            = 0;
    private float pressurePerTick = 0F;
    private PressureTier pressureTier;

    private InventoryFluidCrafting inventory;
    private IFluidRecipe           recipe;
    private ItemStack              inventor;
    private ItemStack              targetItem;
    private int                    maximumTicks;

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
    public float workFunction(boolean simulate, ForgeDirection from) {
        if (recipe != null) {
            float usedPressure = recipe.getPressure();

            if (!inventory.canWork(recipe))
                return 0;

            float maxPressure = Functions.getMaxPressurePerTier(pNetwork.getLowestTier(), true);
            float ratio = getPressure(ForgeDirection.UP) / maxPressure;
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
        if (getPressure(ForgeDirection.UNKNOWN) < requiredPressure) {
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
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                return ret;
            } else {
                ret = fluidInputInventory.splitStack(decrBy);
                if (fluidInputInventory.stackSize <= 0) {
                    fluidInputInventory = null;
                }
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                return ret;
            }
        } else {
            ItemStack ret;
            if (fluidOutputInventory.stackSize < decrBy) {
                ret = fluidOutputInventory;
                fluidOutputInventory = null;
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                return ret;
            } else {
                ret = fluidOutputInventory.splitStack(decrBy);
                if (fluidOutputInventory.stackSize <= 0) {
                    fluidOutputInventory = null;
                }
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                return ret;
            }
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
            setInventorySlotContents(slot, null);
        }
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        if (slot < inventory.getSizeInventory()) {
            inventory.setInventorySlotContents(slot, itemStack);
            return;
        }

        if (slot == 2) {
            fluidInputInventory = itemStack;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        } else if (slot == 3) {
            fluidOutputInventory = itemStack;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return ((worldObj.getTileEntity(xCoord, yCoord, zCoord) == this) &&
                player.getDistanceSq(xCoord, yCoord, zCoord) < 64);
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
    public int[] getAccessibleSlotsFromSide(int var1) {
        return new int[]{0, 1, 2};
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, int j) {
        if (i == 0 && HCConfig.canBeWashed(itemStack)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        if (i == 1) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource.getFluid() != FluidRegistry.WATER)
            return 0;

        return inventory.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        if (fluid.equals(FluidRegistry.WATER)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
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

        tier = tagCompound.getInteger("tier");
        setPressureTier(PressureTier.fromOrdinal(tier));
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        inventory.save(tagCompound);

        tagCompound.setBoolean("isValidMultiblock", isValidMultiblock);
        tagCompound.setInteger("tier", tier);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
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
        int dir = getWorldObj().getBlockMetadata(xCoord, yCoord, zCoord);

        int depthMultiplier = ((dir == 2 || dir == 4) ? 1 : -1);
        boolean forwardZ = ((dir == 2) || (dir == 3));

        for (int horiz = -1; horiz <= 1; horiz++) {
            for (int vert = -1; vert <= 1; vert++) {
                for (int depth = 0; depth <= 2; depth++) {
                    int x = xCoord + (forwardZ ? horiz : (depth * depthMultiplier));
                    int y = yCoord + vert;
                    int z = zCoord + (forwardZ ? (depth * depthMultiplier) : horiz);

                    Block block = getWorldObj().getBlock(x, y, z);
                    /*
                    if(horiz == 0 && vert == 0 && (depth == 0 || depth == 1))
						continue;*/

                    if (block instanceof BlockHydraulicValve) {
                        TileHydraulicValve temp = (TileHydraulicValve) getWorldObj().getTileEntity(x, y, z);
                        temp.resetTarget();
                    }
                    //if(blockId != Ids.blockDummyWasher.act)
                    //	continue;

                    //worldObj.setBlock(x, y, z, Ids.blockHydraulicPressureWall.act);
                    getWorldObj().markBlockForUpdate(x, y, z);
                }
            }
        }
        isValidMultiblock = false;
        getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public boolean checkMultiblock() {
        //So, it should be this:
        // 1       2      3
        //W W W  W W W  W W W
        //W W W  W F W  W W W
        //W W W  W C W  W W W

        int dir = getWorldObj().getBlockMetadata(xCoord, yCoord, zCoord);

        int depthMultiplier = ((dir == 2 || dir == 4) ? 1 : -1);
        boolean forwardZ = ((dir == 2) || (dir == 3));

        boolean hasAtLeastOneValve = false;
        boolean hasFluidValve = false;
        boolean hasItemValve = false;
        for (int horiz = -1; horiz <= 1; horiz++) {
            for (int vert = -1; vert <= 1; vert++) {
                for (int depth = 0; depth <= 2; depth++) {
                    int x = xCoord + (forwardZ ? horiz : (depth * depthMultiplier));
                    int y = yCoord + vert;
                    int z = zCoord + (forwardZ ? (depth * depthMultiplier) : horiz);

                    Block block = getWorldObj().getBlock(x, y, z);
                    int meta = getWorldObj().getBlockMetadata(x, y, z);

                    if (horiz == 0 && vert == 0) {
                        if (depth == 0) { //Looking at self.
                            continue;
                        }

                        if (depth == 1) {
                            if (!(block instanceof BlockHydraulicCore)) {
                                return false;
                            } else {
                                tier = meta;
                                setPressureTier(PressureTier.fromOrdinal(tier));
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

        int dir = getWorldObj().getBlockMetadata(xCoord, yCoord, zCoord);

        int depthMultiplier = ((dir == 2 || dir == 4) ? 1 : -1);
        boolean forwardZ = ((dir == 2) || (dir == 3));
        valves.clear();

        for (int horiz = -1; horiz <= 1; horiz++) {
            for (int vert = -1; vert <= 1; vert++) {
                for (int depth = 0; depth <= 2; depth++) {
                    int x = xCoord + (forwardZ ? horiz : (depth * depthMultiplier));
                    int y = yCoord + vert;
                    int z = zCoord + (forwardZ ? (depth * depthMultiplier) : horiz);
                    Block block = getWorldObj().getBlock(x, y, z);

                    if (horiz == 0 && vert == 0 && depth == 0)
                        continue;

                    if (block instanceof BlockHydraulicValve) {
                        TileHydraulicValve dummyTE = (TileHydraulicValve) getWorldObj().getTileEntity(x, y, z);
                        dummyTE.setTarget(xCoord, yCoord, zCoord);
                        valves.add(dummyTE);
                        dummyTE.getHandler().updateNetworkOnNextTick(0);
                    }
                    if (block instanceof BlockInterfaceValve) {
                        if (worldObj.getTileEntity(x, y, z) instanceof TileInterfaceValve) {
                            TileInterfaceValve dummyTE = (TileInterfaceValve) worldObj.getTileEntity(x, y, z);
                            dummyTE.setTarget(xCoord, yCoord, zCoord);
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
                    //dummyTE.setCore(xCoord, yCoord, zCoord);

                    //dummyTE.setLocationInMultiBlock(dir, horiz, vert, depth);
                    getWorldObj().markBlockForUpdate(x, y, z);

                }
            }
        }
        float p = 0;
        if (pNetwork != null) {
            getPressure(ForgeDirection.UP);
        }
        getHandler().updateNetworkOnNextTick(p);
    }

    @Override
    public void onFluidLevelChanged(int old) {
    }

    @Override
    public boolean canConnectTo(ForgeDirection side) {
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
    public boolean canWork(ForgeDirection dir) {
        if (getNetwork(dir) == null) {
            return false;
        }
        return dir.equals(ForgeDirection.UP);
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
    public String getInventoryName() {
        return Localization.getLocalizedName(Names.blockHydraulicWasher.unlocalized);
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
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
        worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord, zCoord, stack));
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
