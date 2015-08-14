package k4unl.minecraft.Hydraulicraft.containers;

import k4unl.minecraft.Hydraulicraft.slots.SlotFluidContainer;
import k4unl.minecraft.Hydraulicraft.slots.SlotMachineOutput;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicFiller;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerFiller extends ContainerBase {
    public ContainerFiller(InventoryPlayer invPlayer, TileHydraulicFiller filler) {
        super(filler);

        addSlotToContainer(new SlotFluidContainer(filler, 0, 64, 25));
        addSlotToContainer(new SlotMachineOutput(filler, 1, 86, 25));

        bindPlayerInventory(invPlayer);
        bindPlayerArmorSlots(invPlayer, 28, 54);
    }
}
