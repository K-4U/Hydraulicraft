package k4unl.minecraft.Hydraulicraft.containers;

import k4unl.minecraft.Hydraulicraft.slots.SlotMachineInput;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicFilter;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerFilter extends ContainerBase {

    public ContainerFilter(InventoryPlayer invPlayer, TileHydraulicFilter filter) {
        super(filter);
        addSlotToContainer(new SlotMachineInput(filter, filter, 0, 64, 17));

        bindPlayerInventory(invPlayer);
    }

}
