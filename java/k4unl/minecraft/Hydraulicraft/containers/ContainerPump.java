package k4unl.minecraft.Hydraulicraft.containers;

import k4unl.minecraft.Hydraulicraft.slots.SlotMachineInput;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicPump;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerPump extends ContainerBase {

    public ContainerPump(InventoryPlayer invPlayer, TileHydraulicPump pump) {

        super(pump);

        addSlotToContainer(new SlotMachineInput(pump, pump, 0, 35, 49));

        bindPlayerInventory(invPlayer);


    }
}
