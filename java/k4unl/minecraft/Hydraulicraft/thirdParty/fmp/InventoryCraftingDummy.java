package k4unl.minecraft.Hydraulicraft.thirdParty.fmp;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;

/**
 * @author Koen Beckers (K-4U)
 */
public class InventoryCraftingDummy extends InventoryCrafting {
    public InventoryCraftingDummy() {
        super(new Container() {
            public boolean canInteractWith(EntityPlayer entityplayer) {
                return true;
            }
        }, 3, 3);
    }
}
