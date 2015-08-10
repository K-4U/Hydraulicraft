package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.containers.ContainerAssembler;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.thirdParty.nei.widgets.NEIWidgetTank;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileAssembler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTankInfo;
import org.lwjgl.opengl.GL11;

public class GuiAssembler extends HydraulicGUIBase {
    private static final ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/gui/assembler.png");
    TileAssembler assembler;
    NEIWidgetTank tankFluid;

    public GuiAssembler(InventoryPlayer invPlayer, TileAssembler _assembler) {

        super(_assembler, new ContainerAssembler(invPlayer, _assembler), resLoc);
        assembler = _assembler;

        tankFluid = new NEIWidgetTank(assembler.getFluidInventory().getTankInfo()[0], 31, 70, 16, 54, this);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        super.drawGuiContainerBackgroundLayer(f, i, j);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        int scaledProgress = assembler.getScaledAssembleTime();
        drawTexturedModalRect(xStart + 109, yStart + 37, 177, 0, scaledProgress, 18);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        drawHorizontalAlignedString(7, 3, xSize - 14, HCBlocks.blockAssembler.getLocalizedName(), true);

        FluidTankInfo[] tankInfo = assembler.getTankInfo(ForgeDirection.UP);
        tankFluid.render(tankInfo[0]);

        drawFluidAndPressure();
        checkTooltips(mouseX, mouseY);
    }
}
