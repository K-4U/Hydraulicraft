package k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.baseClasses.MachineGUI;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.Pneumaticraft;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.containers.ContainerPneumaticCompressor;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.tileEntities.TileHydraulicPneumaticCompressor;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import pneumaticCraft.api.client.GuiElementRenderer;

public class GuiPneumaticCompressor extends MachineGUI {
	private static ResourceLocation resLoc = new ResourceLocation(ModInfo.LID,"textures/gui/compressor.png");

	private TileHydraulicPneumaticCompressor compressor;

	
	
	public GuiPneumaticCompressor(InventoryPlayer invPlayer, TileHydraulicPneumaticCompressor _compressor) {
		super(_compressor, new ContainerPneumaticCompressor(invPlayer, _compressor), resLoc);
		compressor = _compressor;
	}
	

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		drawHorizontalAlignedString(7, 3, xSize - 14, Pneumaticraft.hydraulicPneumaticCompressor.getLocalizedName(), true);
		
		GL11.glPushMatrix();
	    float gaugeY = 25;
	    float gaugeX = 50;
		GL11.glTranslatef(gaugeX, gaugeY, 0); 
	    GL11.glScaled(0.85D, 0.85D, 0); 
	    
		GuiElementRenderer.drawPressureGauge(fontRenderer, 0, compressor.getPneumaticMaxPressure(), compressor.getPneumaticDangerPressure(),
				0, compressor.getPneumaticPressure(), (int)gaugeX, (int)gaugeY, 0);
		GL11.glPopMatrix();
		drawFluidAndPressure();
	}

}
