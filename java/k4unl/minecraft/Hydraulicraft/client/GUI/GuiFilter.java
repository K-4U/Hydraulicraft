package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.containers.ContainerFilter;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.thirdParty.nei.widgets.NEIWidgetTank;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicFilter;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidTankInfo;

public class GuiFilter extends HydraulicGUIBase {

    private static ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/gui/filter.png");
    TileHydraulicFilter filter;
    NEIWidgetTank       widgetInput, widgetOutput;


    public GuiFilter(InventoryPlayer invPlayer, TileHydraulicFilter _filter) {

        super(_filter, new ContainerFilter(invPlayer, _filter), resLoc);

        filter = _filter;
        widgetInput = new NEIWidgetTank(_filter.getInventoryCrafting().getTankInfo()[0], 38, 70, 16, 54, this);
        widgetOutput = new NEIWidgetTank(_filter.getInventoryCrafting().getTankInfo()[1], 126, 70, 16, 54, this);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {

        super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        drawHorizontalAlignedString(7, 3, xSize - 14, HCBlocks.hydraulicFilter.getLocalizedName(), true);

        FluidTankInfo[] tankInfo = filter.getTankInfo(EnumFacing.UP);
        widgetInput.render(tankInfo[0]);
        widgetOutput.render(tankInfo[1]);


        double ft = filter.getScaledFilterTime();


        int rgb = 0xFF;
        rgb = (rgb << 8) + (int) (255 - (ft * 255));
        rgb = (rgb << 8) + (int) ((ft * 255));
        rgb = (rgb << 8) + 0;
        //int rgb = Constants.COLOR_EU;

        drawHorizontalProgressBar(82, 30, 2, 16, filter.getFilterTicks(), filter.getMaxFilterTicks(), rgb, "Filter", "t");

        drawFluidAndPressure();
        checkTooltips(mouseX, mouseY);
    }
}
