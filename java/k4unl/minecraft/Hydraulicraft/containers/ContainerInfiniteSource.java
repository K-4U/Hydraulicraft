package k4unl.minecraft.Hydraulicraft.containers;

import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileInfiniteSource;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class ContainerInfiniteSource extends ContainerBase {

    public ContainerInfiniteSource(InventoryPlayer invPlayer, TileInfiniteSource source) {

        super(source);

        addSlotToContainer(new Slot(source, 0, 35, 49));

        bindPlayerInventory(invPlayer);

    }
}
