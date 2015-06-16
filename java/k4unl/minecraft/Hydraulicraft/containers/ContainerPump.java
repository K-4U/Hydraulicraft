package k4unl.minecraft.Hydraulicraft.containers;

import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicPump;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class ContainerPump extends ContainerBase {

    public ContainerPump(InventoryPlayer invPlayer, TileHydraulicPump pump) {
        super(pump);

        addSlotToContainer(new Slot(pump, 0, 35, 49));

        bindPlayerInventory(invPlayer);


    }
}
