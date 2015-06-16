package k4unl.minecraft.Hydraulicraft.containers;

import k4unl.minecraft.Hydraulicraft.slots.SlotMachineInput;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicMixer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerMixer extends ContainerBase {

    public ContainerMixer(InventoryPlayer invPlayer, TileHydraulicMixer mixer) {
        super(mixer);
        addSlotToContainer(new SlotMachineInput(mixer, mixer, 0, 64, 17));

        bindPlayerInventory(invPlayer);
    }

}
