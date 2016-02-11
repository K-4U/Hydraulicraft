package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.containers.ContainerIncinerator;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicFrictionIncinerator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiIncinerator extends HydraulicGUIBase {

    private static ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/gui/incinerator.png");
    TileHydraulicFrictionIncinerator incinerator;


    public GuiIncinerator(InventoryPlayer invPlayer, TileHydraulicFrictionIncinerator _incinerator) {

        super(_incinerator, new ContainerIncinerator(invPlayer, _incinerator), resLoc);
        incinerator = _incinerator;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        drawHorizontalAlignedString(7, 3, xSize - 14, HCBlocks.hydraulicFrictionIncinerator.getLocalizedName(), true);

        drawFluidAndPressure();

        if (incinerator.isSmelting()) {
            ItemStack smeltingItem = incinerator.getSmeltingItem();
            ItemStack targetItem = incinerator.getTargetItem();

            int done = incinerator.getSmeltingTicks();
            int startX = 40;
            int maxTicks = 200;
            int targetX = 118;
            int travelPath = targetX - startX;
            float percentage = (float) done / (float) maxTicks;
            int xPos = startX + (int) (travelPath * percentage);

            IconRenderer.drawMergedIcon(xPos, 19, zLevel, smeltingItem, targetItem, percentage, smeltingItem.stackSize % 2 == 0);
        }
        checkTooltips(mouseX, mouseY);
    }
}
