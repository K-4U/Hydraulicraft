package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.containers.ContainerHarvester;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHydraulicHarvester;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiHarvester extends HydraulicGUIBase {

    private static ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/gui/harvester.png");

    private TileHydraulicHarvester harvester;


    public GuiHarvester(InventoryPlayer invPlayer, TileHydraulicHarvester harv) {

        super(harv, new ContainerHarvester(invPlayer, harv), resLoc);
        harvester = harv;
    }


    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        drawHorizontalAlignedString(7, 3, xSize - 14, HCBlocks.hydraulicHarvesterSource.getLocalizedName(), true);

        drawFluidAndPressure();
        checkTooltips(mouseX, mouseY);
    }

}
