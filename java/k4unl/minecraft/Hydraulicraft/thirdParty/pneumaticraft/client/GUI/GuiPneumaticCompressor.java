package k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.client.GUI;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.TileEntities.storage.TileHydraulicPressureVat;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineGUI;
import k4unl.minecraft.Hydraulicraft.blocks.Blocks;
import k4unl.minecraft.Hydraulicraft.containers.ContainerPressureVat;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.Pneumaticraft;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.containers.ContainerPneumaticCompressor;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.tileEntities.TileHydraulicPneumaticCompressor;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.ThermalExpansion;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTankInfo;

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
