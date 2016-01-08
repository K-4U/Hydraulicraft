package k4unl.minecraft.Hydraulicraft.tileEntities.generator;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;

public class TileHydraulicPump extends TileHydraulicBase implements IInventory, IHydraulicGenerator {
    private ItemStack inventory;
    private int currentBurnTime;
    private int maxBurnTime;
    private boolean isBurning = false;

    private int tier = -1;

    private EnumFacing facing = EnumFacing.NORTH;

    public TileHydraulicPump() {
        super(1);
        super.init(this);
    }

    public TileHydraulicPump(int _tier) {
        super(2 * (_tier + 1));
        tier = _tier;
        super.init(this);
    }

    @Override
    public void workFunction(EnumFacing from) {
        if (from.equals(EnumFacing.UP)) {
            //This function gets called every tick.
            //It should check how much coal is left
            //How long that stuff burns
            //And how long it has left to burn.
            boolean needsUpdate = false;
            isBurning = (currentBurnTime > 0);
            if (isBurning) {
                currentBurnTime--;
                needsUpdate = true;
                float gen = getGenerating(from);
                if (gen > 0) {
                    setPressure(gen + getPressure(from), from);
                }
            }
            if (!worldObj.isRemote) {
                if (currentBurnTime <= 0 && TileEntityFurnace.isItemFuel(inventory) && getPressure(from) < getMaxPressure(getHandler().isOilStored(), from)) {
                    //Put new item in
                    currentBurnTime = maxBurnTime = TileEntityFurnace.getItemBurnTime(inventory) + 1;
                    if (inventory != null) {
                        ItemStack containerItem = inventory.getItem()
                                .getContainerItem(inventory);

                        if (containerItem == null) {
                            inventory.stackSize--;
                            if (inventory.stackSize == 0) {
                                inventory = null;
                            }
                        } else {
                            inventory = containerItem.copy();
                        }

                        needsUpdate = true;
                    }
                }

            }

            if (needsUpdate) {
                worldObj.markBlockForUpdate(getPos());
            }
        }
    }

    @Override
    public int getMaxGenerating(EnumFacing from) {
        if (!getHandler().isOilStored()) {
            return HCConfig.INSTANCE.getInt("maxMBarGenWaterT" + (getTier() + 1));
        } else {
            return HCConfig.INSTANCE.getInt("maxMBarGenOilT" + (getTier() + 1));
        }
    }

    public float getBurningPercentage() {
        if (maxBurnTime > 0) {
            return ((float) currentBurnTime / (float) maxBurnTime);
        } else {
            return 0;
        }
    }

    public boolean getIsBurning() {
        return (currentBurnTime > 0);
    }

    @Override
    public float getGenerating(EnumFacing from) {
        if (getIsBurning()) {
            //We can only generate at the percentage the system is filled at.
            float perc = (float) getHandler().getStored() / (float) getMaxStorage();
            //Also.. we can only go to a max of which the system is filled at.
            //So, if the system is 50% full, we only generate at 50% and we can only
            //go to 50% of the max pressure.

            float generating = perc;
            float currentPressure = getPressure(EnumFacing.UP);
            float maxPressure = getMaxPressure(getHandler().isOilStored(), EnumFacing.UP);

            if (getFluidInNetwork(from) > 0) {
                generating = generating * ((float) getFluidInNetwork(from) / (float) getFluidCapacity(from));
                generating = generating * getMaxGenerating(EnumFacing.UP);
            } else {
                generating = 0;
            }
            if (generating > getMaxGenerating(EnumFacing.UP))
                generating = getMaxGenerating(EnumFacing.UP);

            if (generating + currentPressure <= (perc * maxPressure)) {
                return generating;
            } else {
                generating = (perc * maxPressure) - currentPressure;
                if (generating < 0) {
                    generating = 0;
                }
                if (generating + currentPressure <= (perc * maxPressure)) {
                    return generating;
                } else {
                    return 0;
                }
            }
        } else {
            return 0;
        }
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        if (i == 0) {
            return inventory;
        } else {
            return null;
        }
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (i < 1) {
            ItemStack ret;
            if (inventory.stackSize < j) {
                ret = inventory;
                inventory = null;

            } else {
                ret = inventory.splitStack(j);
                if (inventory.stackSize == 0) {
                    inventory = null;
                }
            }

            return ret;

        } else {
            return null;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return decrStackSize(index, getStackInSlot(index).stackSize);
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        if (i < 1) {
            inventory = itemStack;
        }
    }

    public int getTier() {
        return tier;
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
        if (i < 1) {
            if (TileEntityFurnace.isItemFuel(itemStack)) {
                return true;
            } else {
                return false;
            }
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
    public void onBlockBreaks() {
        dropItemStackInWorld(inventory);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);


        setTier(tagCompound.getInteger("tier"));

        NBTTagCompound inventoryCompound = tagCompound.getCompoundTag("inventory");
        inventory = ItemStack.loadItemStackFromNBT(inventoryCompound);

        currentBurnTime = tagCompound.getInteger("currentBurnTime");
        maxBurnTime = tagCompound.getInteger("maxBurnTime");

        facing = EnumFacing.byName(tagCompound.getString("facing"));

    }

    private void setTier(int newTier) {
        tier = PressureTier.fromOrdinal(newTier).toInt();
        super.setMaxStorage(2 * (tier + 1));
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        if (inventory != null) {
            NBTTagCompound inventoryCompound = new NBTTagCompound();
            inventory.writeToNBT(inventoryCompound);
            tagCompound.setTag("inventory", inventoryCompound);
        }
        tagCompound.setInteger("currentBurnTime", currentBurnTime);
        tagCompound.setInteger("maxBurnTime", maxBurnTime);

        tagCompound.setInteger("tier", getTier());
        tagCompound.setString("facing", facing.getName());
    }

    @Override
    public void onFluidLevelChanged(int old) {
    }

    @Override
    public boolean canConnectTo(EnumFacing side) {
        return true;
    }

    @Override
    public boolean canWork(EnumFacing dir) {
        return dir.equals(EnumFacing.UP);
    }

    public EnumFacing getFacing() {
        return facing;
    }

    public void setFacing(EnumFacing newDir) {
        facing = newDir;
    }

    @Override
    public String getName() {
        return Localization.getLocalizedName(Names.blockHydraulicPump[getTier()].unlocalized);
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    @Override
    public IChatComponent getDisplayName() {
        return new ChatComponentTranslation(Names.blockHydraulicPump[getTier()].unlocalized);
    }
}
