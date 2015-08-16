package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.api.IPressurizableItem;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.containers.ContainerCharger;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicCharger;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class GuiCharger extends HydraulicGUIBase {
    private static final ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/gui/charger.png");

    TileHydraulicCharger filler;

    public GuiCharger(InventoryPlayer inventoryPlayer, TileHydraulicCharger charger) {
        super(charger, new ContainerCharger(inventoryPlayer, charger), resLoc);

        this.filler = charger;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        drawHorizontalAlignedString(7, 3, xSize - 14, HCBlocks.blockCharger.getLocalizedName(), true);

        //See if there's an item in the charging slot
        if (filler.getStackInSlot(0) != null) {
            if (filler.getStackInSlot(0).getItem() instanceof IPressurizableItem) {
                ItemStack pressurizingItemStack = filler.getStackInSlot(0);
                IPressurizableItem item = (IPressurizableItem) pressurizingItemStack.getItem();
                String fluidName = "";
                if (item.getFluid(pressurizingItemStack) != null) {
                    fluidName = item.getFluid(pressurizingItemStack).getUnlocalizedName();

                    IIcon icon = item.getFluid(pressurizingItemStack).getFluid().getIcon();

                    drawVerticalProgressBarWithTexture(54, 16, 27, 7, item.getFluid(pressurizingItemStack).amount, item.getMaxFluid(), icon, fluidName, "mB");
                    drawVerticalProgressBar(64, 16, 27, 7, (item.getPressure(pressurizingItemStack) / 1000),
                            (item.getMaxPressure() / 1000), Constants.COLOR_PRESSURE, Localization.getString(Localization.PRESSURE_ENTRY), "Bar");
                }
            }
        }


        drawFluidAndPressure();
        checkTooltips(mouseX, mouseY);
    }
}
