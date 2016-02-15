package k4unl.minecraft.Hydraulicraft.containers;

import k4unl.minecraft.Hydraulicraft.slots.SlotMachineInput;
import k4unl.minecraft.Hydraulicraft.slots.SlotMachineOutput;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicCrusher;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerCrusher extends ContainerBase {

    public ContainerCrusher(InventoryPlayer invPlayer, TileHydraulicCrusher crusher) {

        super(crusher);

        addSlotToContainer(new SlotMachineInput(crusher, crusher, 0, 47, 35));
        addSlotToContainer(new SlotMachineOutput(crusher, 1, 121, 35));

        bindPlayerInventory(invPlayer);
    }
}
