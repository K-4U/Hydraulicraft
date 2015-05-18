package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.containers.ContainerAssembler;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileAssembler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiAssembler extends HydraulicGUIBase{
    private static final ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/gui/assembler.png");
    TileAssembler assembler;

    public GuiAssembler(InventoryPlayer invPlayer, TileAssembler _assembler) {

        super(_assembler, new ContainerAssembler(invPlayer, _assembler), resLoc);
        assembler = _assembler;
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
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        //fontRenderer.drawString(Names.blockHydraulicCrusher.localized, 8, 6, 0xFFFFFF);
        drawHorizontalAlignedString(7, 3, xSize-14, HCBlocks.blockAssembler.getLocalizedName(), true);
        drawFluidAndPressure();
        checkTooltips(mouseX, mouseY);
    }
}
