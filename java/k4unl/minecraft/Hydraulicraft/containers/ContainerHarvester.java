package k4unl.minecraft.Hydraulicraft.containers;

import k4unl.minecraft.Hydraulicraft.slots.SlotMachineInput;
import k4unl.minecraft.Hydraulicraft.slots.SlotMachineOutput;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHydraulicHarvester;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerHarvester extends ContainerBase {

    public ContainerHarvester(InventoryPlayer invPlayer, TileHydraulicHarvester harvester) {
        super(harvester);

        //First inputs!
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addSlotToContainer(new SlotMachineInput(harvester, harvester, (i * 3) + j, 27 + (i * 18), 17 + (j * 18)));
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addSlotToContainer(new SlotMachineOutput(harvester, 9 + (i * 3) + j, 96 + (i * 18), 17 + (j * 18)));
            }
        }

        bindPlayerInventory(invPlayer);


    }
}
