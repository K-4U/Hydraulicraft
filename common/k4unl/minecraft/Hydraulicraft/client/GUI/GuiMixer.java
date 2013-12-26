package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicMixer;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineGUI;
import k4unl.minecraft.Hydraulicraft.containers.ContainerMixer;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTankInfo;

import org.lwjgl.opengl.GL11;

public class GuiMixer extends MachineGUI {
	private static ResourceLocation resLoc = new ResourceLocation(ModInfo.LID,"textures/gui/mixer.png");
	TileHydraulicMixer mixer;
	
	
	public GuiMixer(InventoryPlayer invPlayer, TileHydraulicMixer _mixer) {
		super(_mixer, new ContainerMixer(invPlayer, _mixer), resLoc);
		
		mixer = _mixer;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		fontRenderer.drawString(Names.blockHydraulicMixer.localized, 8, 6, 0xFFFFFF);
		
		
		FluidTankInfo[] tankInfo = mixer.getTankInfo(ForgeDirection.UP);
		if(tankInfo[0].fluid != null){
			if(tankInfo[0].fluid.amount > 0){
				Fluid inTank = FluidRegistry.getFluid(tankInfo[0].fluid.fluidID);
				int color = 0xFFFFFFFF;
				if(inTank.equals(FluidRegistry.WATER)){
					color = 0x7FFFFFFF;
				}
				
				drawVerticalProgressBar(34, 14, 52, 26, tankInfo[0].fluid.amount, tankInfo[0].capacity, color, "Water:", "mB");
			}
		}
		if(tankInfo[1].fluid != null){
			if(tankInfo[1].fluid.amount > 0){
				Fluid inTank = FluidRegistry.getFluid(tankInfo[1].fluid.fluidID);
				int color = 0xFFFFFFFF;
				if(inTank.equals(FluidRegistry.WATER)){
					color = 0x7FFFFFFF;
				}

				drawVerticalProgressBar(34, 14, 52, 26, tankInfo[1].fluid.amount, tankInfo[1].capacity, color, "Oil:", "mB");
			}
		}
		
		drawFluidAndPressure();
	}
}
