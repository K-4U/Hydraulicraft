package k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineGUI;
import k4unl.minecraft.Hydraulicraft.containers.ContainerEmpty;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.tileEntities.TileKineticPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities.TileElectricPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities.TileHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.ThermalExpansion;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileHydraulicDynamo;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GuiElectricPump extends MachineGUI {
	private static ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/gui/thirdPartyPump.png");
	private TileElectricPump pump;
	
	public GuiElectricPump(InventoryPlayer invPlayer, TileEntity ent) {
		super((IHydraulicMachine)ent, new ContainerEmpty(invPlayer), resLoc);
		
		pump = (TileElectricPump)ent;
	}
	

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		
		drawHorizontalAlignedString(7, 3, xSize - 14, Localization.getLocalizedName(Names.blockRFPump[pump.getTier()].unlocalized), true);
		drawVerticalProgressBar(124, 16, 54, 16, pump.getEUStored(), pump.getMaxEUStorage(), Constants.COLOR_EU, "Energy Units", "EU");
				
		drawFluidAndPressure();
	}
}
