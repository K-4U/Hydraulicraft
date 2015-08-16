package k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft;

import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.Optional;
import net.minecraft.item.ItemStack;

public class BuildcraftCompat extends BuildcraftBase {

    public static BuildcraftCompat INSTANCE;

    public BuildcraftCompat() {
        INSTANCE = this;
    }

    @Optional.Method(modid = "BuildCraftAPI|tools")
    @Override
    public boolean isWrench(ItemStack stack) {
        return stack != null && stack.getItem() instanceof IToolWrench || super.isWrench(stack);
    }
}
