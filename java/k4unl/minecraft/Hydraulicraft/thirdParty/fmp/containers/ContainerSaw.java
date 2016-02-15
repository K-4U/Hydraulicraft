package k4unl.minecraft.Hydraulicraft.thirdParty.fmp.containers;

import k4unl.minecraft.Hydraulicraft.slots.SlotMachineInput;
import k4unl.minecraft.Hydraulicraft.slots.SlotMachineOutput;
import k4unl.minecraft.Hydraulicraft.thirdParty.fmp.tileEntities.TileHydraulicSaw;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSaw extends Container {

    protected TileHydraulicSaw tileSaw;


    public ContainerSaw(InventoryPlayer invPlayer, TileHydraulicSaw saw) {

        tileSaw = saw;

        addSlotToContainer(new SlotMachineInput(saw, saw, 0, 80, 16));
        addSlotToContainer(new SlotMachineInput(saw, saw, 1, 62, 34));
        addSlotToContainer(new SlotMachineOutput(saw, 2, 80, 52));
        addSlotToContainer(new SlotMachineOutput(saw, 3, 98, 34));

        bindPlayerInventory(invPlayer);


    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {

        return tileSaw.isUseableByPlayer(entityplayer);
    }


    protected void bindPlayerInventory(InventoryPlayer invPlayer) {
        //Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(invPlayer, j + (i * 9) + 9, 8 + (j * 18), 84 + (i * 18)));
            }
        }

        //Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlotToContainer(new Slot(invPlayer, j, 8 + (j * 18), 142));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {

        ItemStack stack = null;
        Slot slotObject = (Slot) inventorySlots.get(slot);

        if (slotObject != null && slotObject.getHasStack()) {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();

            //From output slot to player inventory

            if (slot == 1) {
                if (!mergeItemStack(stackInSlot, 2, 37, true)) {
                    return null;
                }
            } else {
                if (tileSaw.isItemValidForSlot(0, stackInSlot)) {
                    if (slot == 0) {
                        if (!mergeItemStack(stackInSlot, 2, 37, false)) {
                            return null;
                        }
                    } else {
                        if (!mergeItemStack(stackInSlot, 0, 1, false)) {
                            return null;
                        }
                    }
                }
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
