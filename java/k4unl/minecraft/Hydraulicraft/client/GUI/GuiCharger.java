package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.containers.ContainerCharger;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicCharger;
import net.minecraft.entity.player.InventoryPlayer;
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
        drawHorizontalAlignedString(7, 3, xSize - 14, HCBlocks.hydraulicFiller.getLocalizedName(), true);

        drawFluidAndPressure();
        checkTooltips(mouseX, mouseY);
    }
}
