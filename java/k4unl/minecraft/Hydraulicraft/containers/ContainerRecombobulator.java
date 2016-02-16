package k4unl.minecraft.Hydraulicraft.containers;

import k4unl.minecraft.Hydraulicraft.slots.SlotFluidContainer;
import k4unl.minecraft.Hydraulicraft.slots.SlotMachineOutput;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileFluidRecombobulator;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * @author Koen Beckers (K-4U)
 */
public class ContainerRecombobulator extends ContainerBase {

    public ContainerRecombobulator(InventoryPlayer invPlayer, TileFluidRecombobulator recombobulator) {

        super(recombobulator);

        addSlotToContainer(new SlotFluidContainer(recombobulator, 0, 30, 16));
        addSlotToContainer(new SlotMachineOutput(recombobulator, 1, 30, 53));
        addSlotToContainer(new SlotMachineOutput(recombobulator, 2, 123, 36));

        bindPlayerInventory(invPlayer);
    }
}
