package k4unl.minecraft.Hydraulicraft.containers;

import k4unl.minecraft.Hydraulicraft.slots.SlotMachineInput;
import k4unl.minecraft.Hydraulicraft.slots.SlotMachineOutput;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicWasher;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerWasher extends ContainerBase {

    public ContainerWasher(InventoryPlayer invPlayer, TileHydraulicWasher tileWasher) {

        super(tileWasher);

        addSlotToContainer(new SlotMachineInput(tileWasher, tileWasher, 0, 56, 16));
        addSlotToContainer(new SlotMachineOutput(tileWasher, 1, 106, 56));

        addSlotToContainer(new SlotMachineInput(tileWasher, tileWasher, 2, 31, 16));
        addSlotToContainer(new SlotMachineOutput(tileWasher, 3, 31, 56));

        bindPlayerInventory(invPlayer);


    }
}
