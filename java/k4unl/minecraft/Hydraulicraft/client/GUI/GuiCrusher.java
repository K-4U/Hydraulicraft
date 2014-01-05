package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicCrusher;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineGUI;
import k4unl.minecraft.Hydraulicraft.containers.ContainerCrusher;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiCrusher extends MachineGUI{
    private static final ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/gui/crusher.png");
    TileHydraulicCrusher crusher;

    public GuiCrusher(InventoryPlayer invPlayer, TileHydraulicCrusher _crusher){
        super(_crusher, new ContainerCrusher(invPlayer, _crusher), resLoc);
        crusher = _crusher;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j){
        super.drawGuiContainerBackgroundLayer(f, i, j);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        int scaledProgress = crusher.getScaledCrushTime();
        drawTexturedModalRect(xStart + 85, yStart + 32, 207, 0, scaledProgress, 18);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        fontRenderer.drawString(Names.blockHydraulicCrusher.localized, 8, 6, 0xFFFFFF);
        drawFluidAndPressure();
        checkTooltips(mouseX, mouseY);
    }
}
