package k4unl.minecraft.Hydraulicraft.containers;

import k4unl.minecraft.Hydraulicraft.tileEntities.gow.TilePortalBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class ContainerPortalBase extends ContainerBase {

    public ContainerPortalBase(InventoryPlayer invPlayer, TilePortalBase base) {

        super(base);

        addSlotToContainer(new Slot(base, 0, 31, 42));

        bindPlayerInventory(invPlayer);
    }


}
