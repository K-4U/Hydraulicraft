package k4unl.minecraft.Hydraulicraft.containers;

import k4unl.minecraft.Hydraulicraft.slots.SlotFluidContainer;
import k4unl.minecraft.Hydraulicraft.slots.SlotMachineOutput;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileInterfaceValve;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerTank extends ContainerBase {

    public ContainerTank(InventoryPlayer invPlayer, TileInterfaceValve valve) {

        super(valve);

        addSlotToContainer(new SlotFluidContainer(valve, 0, 113, 19));
        addSlotToContainer(new SlotMachineOutput(valve, 1, 151, 19));

        bindPlayerInventory(invPlayer);


    }
}
