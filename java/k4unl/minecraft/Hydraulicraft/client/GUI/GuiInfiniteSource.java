package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.containers.ContainerInfiniteSource;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileInfiniteSource;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiInfiniteSource extends HydraulicGUIBase {
	private static ResourceLocation resLoc = new ResourceLocation(ModInfo.LID,"textures/gui/infiniteSource.png");
	TileInfiniteSource tileSource;
	
	
	public GuiInfiniteSource(InventoryPlayer invPlayer, TileInfiniteSource source) {
		super(source, new ContainerInfiniteSource(invPlayer, source), resLoc);
		tileSource = source;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		drawHorizontalAlignedString(7, 3, xSize-14, tileSource.getInventoryName(), true);
		
		/*
		int startY = 17;
		int step = (int)(Hydraulicraft.smallGuiFont.getLineHeight() / 3.2F);
		//int step = 6;
		
		String generating = (new DecimalFormat("#.##")).format(pump.getGenerating(ForgeDirection.UP));
		drawSmallerString(61, startY + (step * 0), EnumChatFormatting.GREEN + "Generating:", false);
		drawSmallerString(65, startY + (step * 1), EnumChatFormatting.GREEN + "" + generating + " mBar/t", false);
		drawSmallerString(61, startY + (step * 2), EnumChatFormatting.GREEN + "Max:", false);
		drawSmallerString(65, startY + (step * 3), EnumChatFormatting.GREEN + "" + pump.getMaxGenerating(ForgeDirection.UP) + " mBar/t", false);
		drawSmallerString(61, startY + (step * 4), EnumChatFormatting.GREEN + "Burn left:", false);
		drawSmallerString(65, startY + (step * 5), EnumChatFormatting.GREEN + "" + (int)(pump.getBurningPercentage()*100) + " %", false);
		*/
		
		drawFluidAndPressure();
		checkTooltips(mouseX, mouseY);
	}
}
