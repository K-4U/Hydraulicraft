package k4unl.minecraft.Hydraulicraft.containers;

import k4unl.minecraft.Hydraulicraft.slots.SlotMachineInput;
import k4unl.minecraft.Hydraulicraft.slots.SlotMachineOutput;
import k4unl.minecraft.Hydraulicraft.tileEntities.storage.TileHydraulicPressureVat;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerPressureVat extends ContainerBase {

    public ContainerPressureVat(InventoryPlayer invPlayer, TileHydraulicPressureVat vat) {
        super(vat);

        addSlotToContainer(new SlotMachineInput(vat, vat, 0, 31, 16));
        addSlotToContainer(new SlotMachineOutput(vat, 1, 31, 54));

        bindPlayerInventory(invPlayer);


    }
}
