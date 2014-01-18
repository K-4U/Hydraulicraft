package k4unl.minecraft.Hydraulicraft.client.renderers;



import k4unl.minecraft.Hydraulicraft.TileEntities.TileDummyWasher;
import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHydraulicHarvester;
import k4unl.minecraft.Hydraulicraft.blocks.BlockHydraulicHarvester;
import k4unl.minecraft.Hydraulicraft.blocks.Blocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RendererHarvesterSource extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y,
			double z, float f) {
		Tessellator tessellator = Tessellator.instance;
		
		tessellator.addTranslation((float)x, (float)y, (float)z);
        
		/*
		int lightValue = block.getMixedBrightnessForBlock(world, x, y, z);
        tessellator.setBrightness(lightValue);
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		//Lets try this:
        TileHydraulicHarvester harvester = (TileHydraulicHarvester) world.getBlockTileEntity(x, y, z);
		*/
		//First, check where we are in the multiblock:
		float topX;
		float topY;
		float frontX;
		float frontY;
		float backX;
		float backY;
		
		Icon c = Blocks.hydraulicHarvesterSource.getIcon(0, 0);
		float u = c.getMinU();
		float v = c.getMinV();
		float U = c.getMaxU();
		float V = c.getMaxV();
		float one = (U - u) / 128;
		
		//Render TOP
		tessellator.addVertexWithUV(0, 1, 1, u, v);
		tessellator.addVertexWithUV(1, 1, 1, u, V);
		tessellator.addVertexWithUV(1, 1, 0, U, V);
		tessellator.addVertexWithUV(0, 1, 0, U, v);
		
		//Render BOTTOM
		tessellator.addVertexWithUV(1, 0, 1, u, v);
		tessellator.addVertexWithUV(0, 0, 1, u, V);
		tessellator.addVertexWithUV(0, 0, 0, U, V);
		tessellator.addVertexWithUV(1, 0, 0, U, v);
		
		//Render left	
		tessellator.addVertexWithUV(0, 0, 1, u, v);
		tessellator.addVertexWithUV(0, 1, 1, u, V);
		tessellator.addVertexWithUV(0, 1, 0, U, V);
		tessellator.addVertexWithUV(0, 0, 0, U, v);
		
		//Render Right
		tessellator.addVertexWithUV(1, 0, 0, u, v);
		tessellator.addVertexWithUV(1, 1, 0, u, V);
		tessellator.addVertexWithUV(1, 1, 1, U, V);
		tessellator.addVertexWithUV(1, 0, 1, U, v);
		
		//Render front
		tessellator.addVertexWithUV(1, 0, 1, u, v);
		tessellator.addVertexWithUV(1, 1, 1, u, V);
		tessellator.addVertexWithUV(0, 1, 1, U, V);
		tessellator.addVertexWithUV(0, 0, 1, U, v);
		
		//Render right
		tessellator.addVertexWithUV(0, 0, 0, u, v);
		tessellator.addVertexWithUV(0, 1, 0, u, V);
		tessellator.addVertexWithUV(1, 1, 0, U, V);
		tessellator.addVertexWithUV(1, 0, 0, U, v);
		
		
		
		//again and again, until you're done, then:
		tessellator.addTranslation((float)-x, (float)-y, (float)-z);
		//RenderHelper.enableStandardItemLighting();
		
	}

}
