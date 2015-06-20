package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.containers.ContainerAssembler;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.thirdParty.nei.widgets.NEIWidgetTank;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileAssembler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import org.lwjgl.opengl.GL11;

public class GuiAssembler extends HydraulicGUIBase {
    private static final ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/gui/assembler.png");
    TileAssembler assembler;
    NEIWidgetTank tankFluid;

    public GuiAssembler(InventoryPlayer invPlayer, TileAssembler _assembler) {

        super(_assembler, new ContainerAssembler(invPlayer, _assembler), resLoc);
        assembler = _assembler;
        FluidTank tank = new FluidTank(assembler.getFluidInventory().getTankInfo()[0].fluid,
                assembler.getFluidInventory().getTankInfo()[0].capacity);

        tankFluid = new NEIWidgetTank(tank, 31, 70, 16, 54);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {

        super.drawGuiContainerBackgroundLayer(f, i, j);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        int scaledProgress = assembler.getScaledAssembleTime();
        drawTexturedModalRect(xStart + 85, yStart + 34, 207, 0, scaledProgress, 18);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        //fontRenderer.drawString(Names.blockHydraulicCrusher.localized, 8, 6, 0xFFFFFF);
        drawHorizontalAlignedString(7, 3, xSize - 14, HCBlocks.blockAssembler.getLocalizedName(), true);

        FluidTankInfo[] tankInfo = assembler.getTankInfo(ForgeDirection.UP);
        if (tankInfo[0].fluid != null) {
            //drawVerticalProgressBarWithTexture(31, 16, 54, 16, tankInfo[0].fluid.amount, tankInfo[0].capacity, tankInfo[0].fluid.getFluid()
            //  .getIcon(), tankInfo[0].fluid.getLocalizedName(), "mB");
            tankFluid.updateAmount(tankInfo[0].fluid.amount);
            tankFluid.render();
            tooltipList.add(new ToolTip(31, 16, 16, 54, tankInfo[0].fluid.getLocalizedName(), "mB",
                    tankInfo[0].fluid.amount, tankInfo[0].capacity));

        }


        drawFluidAndPressure();
        checkTooltips(mouseX, mouseY);
    }
}
