package k4unl.minecraft.Hydraulicraft.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public abstract class ContainerBase extends Container {
    protected TileEntity tileEntity;
    private int internalSlots = 0;
    private int totalSlots    = 0;

    public ContainerBase(TileEntity tileEntity) {
        this.tileEntity = tileEntity;
    }

    /**
     * Binds player inventory to the current GUI (does not count offset!) Should be called after adding
     * internal inventories!
     *
     * @param inv Player's inventory
     */
    protected void bindPlayerInventory(InventoryPlayer inv) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                super.addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            super.addSlotToContainer(new Slot(inv, i, 8 + i * 18, 142));
        }

        totalSlots += 36;
    }

    protected void bindPlayerArmorSlots(InventoryPlayer player, int offX, int offY) {
        for (int i = 0; i < 4; i++) {
            super.addSlotToContainer(new Slot(player, 36 + i, offX + i * 18, offY));
        }

        totalSlots += 4;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return player.getDistanceSq(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) < 64;
    }

    @Override
    protected Slot addSlotToContainer(Slot slot) {
        internalSlots++;
        totalSlots++;
        return super.addSlotToContainer(slot);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int sourceSlot) {
        ItemStack stack = null;
        Slot slotObject = (Slot) inventorySlots.get(sourceSlot);

        if (slotObject != null && slotObject.getHasStack()) {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();

            // items with source in Sacred Slots (c)
            if (sourceSlot < internalSlots && totalSlots > internalSlots) {
                if (!this.mergeItemStack(stackInSlot, internalSlots, totalSlots, true)) {
                    return null;
                }
            }
            // items with source in user's inventory...
            else {
                int slot = -1;
                for (int i = 0; i < internalSlots; i++) {
                    if (((Slot) inventorySlots.get(i)).isItemValid(stackInSlot)
                            && this.mergeItemStack(stackInSlot, i, i + 1, false))
                        slot = i;
                }

                if (slot == -1)
                    return null;
            }

            if (stackInSlot.stackSize == 0) {
                slotObject.putStack(null);
            } else {
                slotObject.onSlotChanged();
            }

            if (stackInSlot.stackSize == stack.stackSize) {
                return null;
            }
            slotObject.onPickupFromSlot(player, stackInSlot);
        }
        return stack;

    }
}
