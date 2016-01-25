package k4unl.minecraft.Hydraulicraft.lib.recipes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class DummyContainer extends Container {
    protected IFluidInventory feedback;

    public DummyContainer() {
        this(null);
    }

    public DummyContainer(IFluidInventory feedback) {
        this.feedback = feedback;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
