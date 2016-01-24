package k4unl.minecraft.Hydraulicraft.tileEntities.storage;

import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicTieredBlockBase;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.Hydraulicraft.tileEntities.interfaces.IHydraulicStorageWithTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fluids.*;

public class TileHydraulicPressureVat extends TileHydraulicBase implements IInventory, IFluidHandler, IHydraulicStorageWithTank {
    private ItemStack inputInventory;
    private ItemStack outputInventory;


    private FluidTank tank = null;//
    private PressureTier tier = PressureTier.INVALID;
    private int prevRedstoneLevel = 0;

    public TileHydraulicPressureVat() {
        super(48);
        super.init(this);
    }

    public TileHydraulicPressureVat(PressureTier _tier) {
        super(16 * (_tier.toInt() + 1));
        super.init(this);
        tier = _tier;
        if (tank == null) {
            tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * (16 * (tier.toInt() + 1)));
        }
    }

    public void setTier(PressureTier newTier) {
        if (tier == PressureTier.INVALID && newTier != PressureTier.INVALID) {
            tier = newTier;
            super.setMaxStorage(16 * (tier.toInt() + 1));
            tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * (16 * (tier.toInt() + 1)));
        }
    }

    public PressureTier getTier() {
        if (tier == PressureTier.INVALID && getWorldObj() != null) {
            tier = (PressureTier) worldObj.getBlockState(getPos()).getValue(HydraulicTieredBlockBase.TIER);
            tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * (16 * (tier.toInt() + 1)));
            super.setMaxStorage(2 * (tier.toInt() + 1));
        }
        return tier;
    }

    public void newFromNBT(NBTTagCompound tagCompound) {
        NBTTagCompound inventoryCompound = tagCompound.getCompoundTag("inputInventory");
        inputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);

        inventoryCompound = tagCompound.getCompoundTag("outputInventory");
        outputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);

        setTier(PressureTier.fromOrdinal(tagCompound.getInteger("tier")));
        prevRedstoneLevel = tagCompound.getInteger("prevRedstoneLevel");
        setIsOilStored(tagCompound.getBoolean("isOilStored"));

        NBTTagCompound tankCompound = tagCompound.getCompoundTag("tank");
        if (tankCompound != null) {
            if (tank != null) {
                tank.readFromNBT(tankCompound);
            }
        }
        setIsOilStored(tagCompound.getBoolean("isOilStored"));
    }


    @Override
    public int getSizeInventory() {
        return 2;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        switch (i) {
            case 0:
                return inputInventory;
            case 1:
                return outputInventory;
            default:
                return null;

        }
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        ItemStack inventory = getStackInSlot(i);

        ItemStack ret;
        if (inventory.stackSize < j) {
            ret = inventory;
            if (i == 0) {
                inputInventory = null;
            } else if (i == 1) {
                outputInventory = null;
            }
        } else {
            ret = inventory.splitStack(j);
            if (inventory.stackSize == 0) {
                if (i == 0) {
                    inputInventory = null;
                } else if (i == 1) {
                    outputInventory = null;
                }
            }
        }

        return ret;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        if (i == 0) {
            inputInventory = itemStack;
            onInventoryChanged();
        } else if (i == 1) {
            outputInventory = itemStack;
            onInventoryChanged();
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
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        if (i == 0) {
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
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        if (worldObj.isRemote) return 0;
        if (resource == null) //What!?
            return 0;
        if (resource.getFluid() == null)
            return 0;

        if (tank != null && tank.getFluid() != null && tank.getFluidAmount() > 0) {
            if (resource.getFluid() != tank.getFluid().getFluid()) {
                return 0;
            }
        }

        int filled = tank.fill(resource, doFill);
        if (resource.getFluid() == Fluids.fluidHydraulicOil) {
            getHandler().setIsOilStored(true);
        } else {
            getHandler().setIsOilStored(false);
        }
        if (doFill && filled > 10) {
            getHandler().updateFluidOnNextTick();
        } else if (getNetwork(from) != null) {
            if ((getNetwork(from).getFluidInNetwork() + resource.amount) < getNetwork(from).getFluidCapacity()) {
                if (doFill) {
                    getHandler().updateFluidOnNextTick();
                }
                filled = resource.amount;
            } else if (getNetwork(from).getFluidInNetwork() < getNetwork(from).getFluidCapacity()) {
                if (doFill) {
                    getHandler().updateFluidOnNextTick();
                }
                filled = getNetwork(from).getFluidCapacity() - getNetwork(from).getFluidInNetwork();
            }
        }
        return filled;

    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        FluidStack drained = tank.drain(maxDrain, doDrain);
        if (doDrain && drained != null && drained.amount > 0) {
            //Functions.checkAndFillSideBlocks(worldObj, xCoord, yCoord, zCoord);
            getHandler().updateFluidOnNextTick();
        }

        return drained;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        if (fluid == null) return true;
        return fluid == FluidRegistry.WATER || fluid == Fluids.fluidHydraulicOil;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        if (tank != null) {
            return new FluidTankInfo[]{new FluidTankInfo(tank)};
        } else {
            return null;
        }

    }

    @Override
    public int getMaxStorage() {
        if (tank == null) {
            return 0;
        }
        return tank.getCapacity();
    }

    @Override
    public int getStored() {
        if (tank == null) {
            return 0;
        }
        return tank.getFluidAmount();
    }

    @Override
    public void setStored(int i, boolean isOil) {
        if (tank != null) {
            if (isOil) {
                if (i == 0) {
                    tank.setFluid(null);
                } else {
                    tank.setFluid(new FluidStack(Fluids.fluidHydraulicOil, i));
                }
            } else {
                if (i == 0) {
                    tank.setFluid(null);
                } else {
                    tank.setFluid(new FluidStack(FluidRegistry.WATER, i));
                }
                //Log.info("Fluid in tank: " + tank.getFluidAmount() + "x" + FluidRegistry.getFluidName(tank.getFluid().fluidID));
                //if(!worldObj.isRemote){

                //}
            }
            worldObj.markBlockForUpdate(getPos());
        }
    }

    @Override
    public void onBlockBreaks() {
        ItemStack ourEnt = new ItemStack(HCBlocks.hydraulicPressurevat, 1, getTier().toInt());
        NBTTagCompound tCompound = new NBTTagCompound();
        writeToNBT(tCompound);
        tCompound.removeTag("x");
        tCompound.removeTag("y");
        tCompound.removeTag("z");
        tCompound.removeTag("id");

        tCompound.setBoolean("hasBeenPlaced", true);
        tCompound.setInteger("maxStorage", getMaxStorage());
        tCompound.setFloat("maxPressure", getMaxPressure(getHandler().isOilStored(), EnumFacing.UP));
        tCompound.setFloat("oldPressure", getPressure(EnumFacing.UP));
        ourEnt.setTagCompound(tCompound);
        dropItemStackInWorld(ourEnt);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        NBTTagCompound inventoryCompound = tagCompound.getCompoundTag("inputInventory");
        inputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);

        inventoryCompound = tagCompound.getCompoundTag("outputInventory");
        outputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);

        setTier(PressureTier.fromOrdinal(tagCompound.getInteger("tier")));
        prevRedstoneLevel = tagCompound.getInteger("prevRedstoneLevel");

        NBTTagCompound tankCompound = tagCompound.getCompoundTag("tank");
        if (tankCompound != null) {
            if (tank != null) {
                tank.readFromNBT(tankCompound);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        if (inputInventory != null) {
            NBTTagCompound inventoryCompound = new NBTTagCompound();
            inputInventory.writeToNBT(inventoryCompound);
            tagCompound.setTag("inputInventory", inventoryCompound);
        }
        if (outputInventory != null) {
            NBTTagCompound inventoryCompound = new NBTTagCompound();
            outputInventory.writeToNBT(inventoryCompound);
            tagCompound.setTag("outputInventory", inventoryCompound);
        }

        tagCompound.setInteger("tier", tier.toInt());
        tagCompound.setInteger("prevRedstoneLevel", prevRedstoneLevel);

        NBTTagCompound tankCompound = new NBTTagCompound();
        if (tank != null) {
            tank.writeToNBT(tankCompound);
            tagCompound.setTag("tank", tankCompound);
        }
    }

    @Override
    public void update() {
        super.update();
        if (getNetwork(EnumFacing.UP) != null && worldObj != null) {
            float curPressure = getPressure(EnumFacing.UP);
            float maxPressure = getMaxPressure(getHandler().isOilStored(), EnumFacing.UP);
            float percentage = curPressure / maxPressure;
            int newRedstoneLevel = (int) (15 * percentage);
            if (newRedstoneLevel != prevRedstoneLevel) {
                prevRedstoneLevel = newRedstoneLevel;
                worldObj.notifyNeighborsOfStateChange(getPos(), getBlockType());
                getHandler().updateBlock();
            }
        }
    }


    public void onInventoryChanged() {
        if (inputInventory != null) {
            FluidStack input = FluidContainerRegistry.getFluidForFilledItem(inputInventory);
            if (fill(EnumFacing.UP, input, false) == input.amount) {
                Item outputI = inputInventory.getItem().getContainerItem();
                if (outputI != null && outputInventory != null) {
                    ItemStack output = new ItemStack(outputI);
                    if (outputInventory.isItemEqual(output)) {
                        if (outputInventory.stackSize < output.getMaxStackSize()) {
                            outputInventory.stackSize += output.stackSize;
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else if (outputInventory == null && outputI != null) {
                    outputInventory = new ItemStack(outputI);
                } else if (outputI == null) {

                } else {
                    return;
                }
                fill(EnumFacing.UP, input, true);

                decrStackSize(0, 1);
                //Leave an empty container:

            }
        }
    }

    @Override
    public void onFluidLevelChanged(int old) {
    }

    @Override
    public boolean canConnectTo(EnumFacing side) {
        return true;
    }

    public int getRedstoneLevel() {
        return prevRedstoneLevel;
    }

    @Override
    public String getName() {
        return Localization.getLocalizedName(Names.blockHydraulicPressurevat[getTier().toInt()].unlocalized);
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    @Override
    public IChatComponent getDisplayName() {
        return new ChatComponentTranslation(Names.blockHydraulicPressurevat[getTier().toInt()].unlocalized);
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }
}
