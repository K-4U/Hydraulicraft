package k4unl.minecraft.Hydraulicraft.thirdParty.fmp.client.GUI;

import k4unl.minecraft.Hydraulicraft.client.GUI.IconRenderer;
import k4unl.minecraft.Hydraulicraft.client.GUI.HydraulicGUIBase;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.thirdParty.fmp.FMP;
import k4unl.minecraft.Hydraulicraft.thirdParty.fmp.containers.ContainerSaw;
import k4unl.minecraft.Hydraulicraft.thirdParty.fmp.tileEntities.TileHydraulicSaw;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiSaw extends HydraulicGUIBase {

    private static final ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/gui/saw.png");
    TileHydraulicSaw saw;

    public GuiSaw(InventoryPlayer invPlayer, TileHydraulicSaw _saw) {

        super(_saw, new ContainerSaw(invPlayer, _saw), resLoc);
        saw = _saw;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {

        super.drawGuiContainerBackgroundLayer(f, i, j);
        
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        
        drawHorizontalAlignedString(7, 3, xSize - 14, FMP.hydraulicSaw.getLocalizedName(), true);
        
        if (saw.isSawing() && saw.getIsSawingUpDown()) {
            ItemStack sawingItem = saw.getSawingUpDownItem();
            ItemStack targetItem = saw.getTargetUpDownItem();

            int done = saw.getSawingTicksUpDown();
            int startY = 16;
            int maxTicks = 200;
            int targetY = 52;
            int travelPath = targetY - startY;
            float percentage = (float) done / (float) maxTicks;
            int yPos = startY + (int) (travelPath * percentage);

            IconRenderer.drawMergedIcon(80, yPos, zLevel, sawingItem, targetItem, percentage, false);
        }
        
        if (saw.isSawing() && saw.getIsSawingLeftRight()) {
            ItemStack sawingItem = saw.getSawingLeftRightItem();
            ItemStack targetItem = saw.getTargetLeftRightItem();

            int done = saw.getSawingTicksLeftRight();
            int startX = 62;
            int maxTicks = 200;
            int targetX = 98;
            int travelPath = targetX - startX;
            float percentage = (float) done / (float) maxTicks;
            int xPos = startX + (int) (travelPath * percentage);

            IconRenderer.drawMergedIcon(xPos, 34, zLevel, sawingItem, targetItem, percentage, false);
        }
        
        drawFluidAndPressure();
        checkTooltips(mouseX, mouseY);
    }
}
