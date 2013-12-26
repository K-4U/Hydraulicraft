package k4unl.minecraft.Hydraulicraft.client.GUI;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicCrusher;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineGUI;
import k4unl.minecraft.Hydraulicraft.containers.ContainerCrusher;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiCrusher extends MachineGUI {
	private static final ResourceLocation resLoc = new ResourceLocation(ModInfo.LID,"textures/gui/crusher.png");
	TileHydraulicCrusher crusher;
	
	
	public GuiCrusher(InventoryPlayer invPlayer, TileHydraulicCrusher _crusher) {
		super(_crusher, new ContainerCrusher(invPlayer, _crusher), resLoc);
		crusher = _crusher;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		fontRenderer.drawString(Names.blockHydraulicCrusher.localized, 8, 6, 0xFFFFFF);
		
		drawFluidAndPressure();
		
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}
}
