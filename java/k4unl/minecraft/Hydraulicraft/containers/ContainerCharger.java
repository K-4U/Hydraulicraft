package k4unl.minecraft.Hydraulicraft.containers;

import k4unl.minecraft.Hydraulicraft.slots.SlotPressure;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicCharger;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerCharger extends ContainerBase {

    public ContainerCharger(InventoryPlayer invPlayer, TileHydraulicCharger charger) {

        super(charger);

        addSlotToContainer(new SlotPressure(charger, 0, 78, 17));

        bindPlayerInventory(invPlayer);
        bindPlayerArmorSlots(invPlayer, 44, 56);
    }
}
