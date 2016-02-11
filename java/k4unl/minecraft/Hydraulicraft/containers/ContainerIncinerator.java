package k4unl.minecraft.Hydraulicraft.containers;

import k4unl.minecraft.Hydraulicraft.slots.SlotMachineInput;
import k4unl.minecraft.Hydraulicraft.slots.SlotMachineOutput;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicFrictionIncinerator;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerIncinerator extends ContainerBase {

    public ContainerIncinerator(InventoryPlayer invPlayer, TileHydraulicFrictionIncinerator incinerator) {

        super(incinerator);

        addSlotToContainer(new SlotMachineInput(incinerator, incinerator, 0, 40, 19));
        addSlotToContainer(new SlotMachineOutput(incinerator, 1, 118, 19));

        bindPlayerInventory(invPlayer);


    }
}
