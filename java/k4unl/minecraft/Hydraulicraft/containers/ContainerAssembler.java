package k4unl.minecraft.Hydraulicraft.containers;

import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileAssembler;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerAssembler extends ContainerBase {

    public ContainerAssembler(InventoryPlayer invPlayer, TileAssembler assembler) {
        super(assembler);

        //addSlotToContainer(new SlotMachineInput(assembler, assembler, 0, 47, 35));
        //addSlotToContainer(new SlotMachineOutput(assembler, 1, 121, 35));

        bindPlayerInventory(invPlayer);


    }
}
