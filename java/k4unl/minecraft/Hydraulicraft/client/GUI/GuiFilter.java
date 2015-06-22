package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.containers.ContainerFilter;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.thirdParty.nei.widgets.NEIWidgetTank;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicFilter;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;

public class GuiFilter extends HydraulicGUIBase {
    private static ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/gui/filter.png");
    TileHydraulicFilter filter;
    NEIWidgetTank       widgetInput, widgetOutput;


    public GuiFilter(InventoryPlayer invPlayer, TileHydraulicFilter _filter) {
        super(_filter, new ContainerFilter(invPlayer, _filter), resLoc);

        FluidTank tank = new FluidTank(_filter.getInventoryCrafting().getTankInfo()[0].fluid,
                _filter.getInventoryCrafting().getTankInfo()[0].capacity);

        filter = _filter;
        widgetInput = new NEIWidgetTank(tank, 38, 70, 16, 54, this);
        tank = new FluidTank(_filter.getInventoryCrafting().getTankInfo()[1].fluid,
                _filter.getInventoryCrafting().getTankInfo()[1].capacity);
        widgetOutput = new NEIWidgetTank(tank, 126, 70, 16, 54, this);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        drawHorizontalAlignedString(7, 3, xSize - 14, HCBlocks.hydraulicFilter.getLocalizedName(), true);

        FluidTankInfo[] tankInfo = filter.getTankInfo(ForgeDirection.UP);
        if (tankInfo[0].fluid != null) {
            widgetInput.updateAmount(tankInfo[0].fluid.amount);
            widgetInput.render();
        }
        if (tankInfo[1].fluid != null) {
            widgetOutput.updateAmount(tankInfo[1].fluid.amount);
            widgetOutput.render();
        }


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
