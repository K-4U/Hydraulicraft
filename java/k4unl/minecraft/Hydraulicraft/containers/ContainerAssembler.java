package k4unl.minecraft.Hydraulicraft.containers;

import k4unl.minecraft.Hydraulicraft.lib.recipes.SlotFluidCrafting;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileAssembler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerAssembler extends ContainerBase {

    SlotFluidCrafting slot;

    public ContainerAssembler(InventoryPlayer invPlayer, TileAssembler assembler) {

        super(assembler);

        slot = new SlotFluidCrafting(invPlayer.player, assembler.getFluidInventory(), assembler.getInventoryResult(), 0, 131, 35);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addSlotToContainer(new Slot(assembler.getFluidInventory(), i * 3 + j, 53 + 18 * j, 17 + 18 * i));
            }
        }

        addSlotToContainer(slot);

        bindPlayerInventory(invPlayer);
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventory) {
        super.onCraftMatrixChanged(inventory);
        slot.onMatrixChanged();
    }


}
