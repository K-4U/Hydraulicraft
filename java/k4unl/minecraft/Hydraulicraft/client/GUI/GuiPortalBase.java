package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.containers.ContainerPortalBase;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.tileEntities.gow.TilePortalBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiPortalBase extends HydraulicGUIBase {

    protected static final ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/gui/portalBase.png");
    protected TilePortalBase base;

    public GuiPortalBase(InventoryPlayer invPlayer, TilePortalBase _base) {

        super(_base, new ContainerPortalBase(invPlayer, _base), resLoc);

        base = _base;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2,
                                                   int var3) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(resLoc);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        //Draw IP address

        drawHorizontalAlignedString(65 + x, 44 + y, 76, base.getIPString(), false);
        drawMediumString(60 + x, 24 + y, "IP-address:", false);

        drawMediumString(26 + x, 24 + y, "Link card:", false);

        //drawHorizontalAlignedString(18 + x, 24 + y, 76, "Link card:", false);
        //fontRendererObj.drawString(base.getIPString(), 82 + x, 28+y, 0xFFFFFF, true);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        drawHorizontalAlignedString(7, 5, xSize - 14, HCBlocks.portalBase.getLocalizedName(), true);
        drawFluidAndPressure();
        checkTooltips(mouseX, mouseY);
    }
/*
    public void drawHorizontalAlignedString(int xOffset, int yOffset, int w, String text, boolean useShadow){
		int stringWidth = fontRendererObj.getStringWidth(text);
		int newX = xOffset;
		if(stringWidth < w){
			newX = (w / 2) - (stringWidth / 2) + xOffset;
		}
		
		fontRendererObj.drawString(text, newX, yOffset, 0xFFFFFF, useShadow);
	}*/

}
