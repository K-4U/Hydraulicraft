package k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineGUI;
import k4unl.minecraft.Hydraulicraft.containers.ContainerEmpty;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.tileEntities.TileKineticPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.IndustrialCraft;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities.TileElectricPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities.TileHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.ThermalExpansion;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileHydraulicDynamo;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GuiHydraulicGenerator extends MachineGUI {
	private static ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/gui/thirdPartyGen.png");
	private TileHydraulicGenerator generator;
	
	public GuiHydraulicGenerator(InventoryPlayer invPlayer, TileEntity ent) {
		super((IHydraulicMachine)ent, new ContainerEmpty(invPlayer), resLoc);
		
		generator = (TileHydraulicGenerator)ent;
		
	}
	

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		
		drawHorizontalAlignedString(7, 3, xSize - 14, IndustrialCraft.blockHydraulicGenerator.getLocalizedName(), true);
		drawVerticalProgressBar(40, 16, 54, 16, generator.getPowerReceiver(generator.getFacing()).getEnergyStored(), generator.getPowerReceiver(generator.getFacing()).getMaxEnergyStored(), Constants.COLOR_EU, "Energy Units", "EU");
		
		drawFluidAndPressure();
	}
}
