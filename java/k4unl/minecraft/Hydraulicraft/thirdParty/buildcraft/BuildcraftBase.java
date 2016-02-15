package k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft;

import k4unl.minecraft.Hydraulicraft.items.ItemHydraulicWrench;
import k4unl.minecraft.Hydraulicraft.thirdParty.IThirdParty;
import net.minecraft.item.ItemStack;

public class BuildcraftBase implements IThirdParty {

    public boolean isWrench(ItemStack stack) {

        return stack != null && stack.getItem() instanceof ItemHydraulicWrench;
    }

    @Override
    public void preInit() {

    }

    @Override
    public void init() {

    }

    @Override
    public void postInit() {

    }

    @Override
    public void clientSide() {

    }
}
