package k4unl.minecraft.Hydraulicraft.containers;

import k4unl.minecraft.Hydraulicraft.slots.SlotMachineOutput;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileFluidRecombobulator;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * @author Koen Beckers (K-4U)
 */
public class ContainerRecombobulator extends ContainerBase {

    public ContainerRecombobulator(InventoryPlayer invPlayer, TileFluidRecombobulator recombobulator) {

        super(recombobulator);

        //TODO: Add fluid slots.
        addSlotToContainer(new SlotMachineOutput(recombobulator, 0, 123, 36));

        bindPlayerInventory(invPlayer);
    }
}
