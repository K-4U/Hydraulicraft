package k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineGUI;
import k4unl.minecraft.Hydraulicraft.containers.ContainerEmpty;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.Buildcraft;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.tileEntities.TileHydraulicEngine;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.tileEntities.TileKineticPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.ThermalExpansion;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileHydraulicDynamo;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeDirection;

public class GuiHydraulicEngine extends MachineGUI {
	private static ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/gui/thirdPartyGen.png");
	private TileHydraulicEngine engine;
	
	public GuiHydraulicEngine(InventoryPlayer invPlayer, TileEntity ent) {
		super((IHydraulicMachine)ent, new ContainerEmpty(invPlayer), resLoc);
		
		engine = (TileHydraulicEngine)ent;
	}
	

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		drawHorizontalAlignedString(7, 3, xSize - 14, Buildcraft.blockHydraulicEngine.getLocalizedName(), true);
		drawVerticalProgressBar(40, 16, 54, 16, engine.getPowerReceiver(engine.getFacing()).getEnergyStored(), engine.getPowerReceiver(engine.getFacing()).getMaxEnergyStored(), Constants.COLOR_MJ, "Minecraft Joules", "MJ");
	
		drawFluidAndPressure();
	}
}
