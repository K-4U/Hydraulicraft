package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.containers.ContainerRecombobulator;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.thirdParty.nei.widgets.NEIWidgetTank;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileFluidRecombobulator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidTankInfo;

public class GuiFluidRecombobulator extends HydraulicGUIBase {

    private static ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/gui/recombobulator.png");
    TileFluidRecombobulator recombobulator;
    NEIWidgetTank           widgetInput;


    public GuiFluidRecombobulator(InventoryPlayer invPlayer, TileFluidRecombobulator recombobulator) {

        super(recombobulator, new ContainerRecombobulator(invPlayer, recombobulator), resLoc);

        this.recombobulator = recombobulator;
        widgetInput = new NEIWidgetTank(recombobulator.getInventoryCrafting().getTankInfo()[0], 53, 70, 16, 54, this);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {

        super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        drawHorizontalAlignedString(7, 3, xSize - 14, HCBlocks.blockFluidRecombobulator.getLocalizedName(), true);

        FluidTankInfo[] tankInfo = recombobulator.getTankInfo(EnumFacing.UP);
        widgetInput.render(tankInfo[0]);

        drawFluidAndPressure();
        checkTooltips(mouseX, mouseY);
    }
}
