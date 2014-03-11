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

public class GuiKineticPump extends MachineGUI {
	private static ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/gui/thirdPartyPump.png");
	private TileKineticPump pump;
	
	public GuiKineticPump(InventoryPlayer invPlayer, TileEntity ent) {
		super((IHydraulicMachine)ent, new ContainerEmpty(invPlayer), resLoc);
		
		pump = (TileKineticPump)ent;
	}
	

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		drawHorizontalAlignedString(7, 3, xSize - 14, Localization.getLocalizedName(Names.blockKineticPump[pump.getTier()].unlocalized), true);
		drawVerticalProgressBar(124, 16, 54, 16, pump.getPowerReceiver(pump.getFacing().getOpposite()).getEnergyStored(), pump.getPowerReceiver(pump.getFacing().getOpposite()).getMaxEnergyStored(), Constants.COLOR_MJ, "Minecraft Joules", "MJ");
		
		drawFluidAndPressure();
	}
}
