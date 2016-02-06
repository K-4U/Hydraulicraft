package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.containers.ContainerPressureVat;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.tileEntities.storage.TileHydraulicPressureReservoir;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiPressureVat extends HydraulicGUIBase {
	private static ResourceLocation resLoc = new ResourceLocation(ModInfo.LID,"textures/gui/pressureVat.png");

	private TileHydraulicPressureReservoir pvat;

	
	
	public GuiPressureVat(InventoryPlayer invPlayer, TileHydraulicPressureReservoir vat) {
		super(vat, new ContainerPressureVat(invPlayer, vat), resLoc);
		pvat = vat;
	}
	

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		//fontRenderer.drawString(pvat.getInvName(), 30, 6, 0xFFFFFF);
		drawHorizontalAlignedString(7, 3, xSize-14, pvat.getName(), true);
		
		drawFluidAndPressure();
		checkTooltips(mouseX, mouseY);
	}

}
