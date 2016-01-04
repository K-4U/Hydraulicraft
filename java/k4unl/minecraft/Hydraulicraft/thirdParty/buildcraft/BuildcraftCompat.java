package k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

public class BuildcraftCompat extends BuildcraftBase {

    public static BuildcraftCompat INSTANCE;

    public BuildcraftCompat() {
        INSTANCE = this;
    }

    @Optional.Method(modid = "BuildCraftAPI|tools")
    @Override
    public boolean isWrench(ItemStack stack) {
        return stack != null /*&& stack.getItem() instanceof IToolWrench*/ || super.isWrench(stack);
    }
}
