package k4unl.minecraft.Hydraulicraft.client.renderers;



import k4unl.minecraft.Hydraulicraft.TileEntities.TileDummyWasher;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RendererDummyWasher implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
		
		tessellator.addTranslation(x, y, z);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
		
		
		int lightValue = block.getMixedBrightnessForBlock(world, x, y, z);
        tessellator.setBrightness(lightValue);
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		//Lets try this:
		TileDummyWasher washer = (TileDummyWasher) world.getBlockTileEntity(x, y, z);
		
		//First, check where we are in the multiblock:
		boolean renderFront = false;
		boolean renderBack = false;
		boolean renderLeft = false;
		boolean renderRight = false;
		boolean renderTop = false;
		boolean renderBottom = false;
		
		float topX;
		float topY;
		float frontX;
		float frontY;
		float backX;
		float backY;
		
		Icon c = block.getIcon(0, 0);
		int dir = washer.getDir();
		float u = c.getMinU();
		float v = c.getMinV();
		float U = c.getMaxU();
		float V = c.getMaxV();
		float one = (U - u) / 128;
		
		if(washer.getVert() == 1){
			renderTop = true;
		}
		if(washer.getVert() == -1){
			renderBottom = true;
		}
		if(washer.getDepth() == 0){
			if(dir == 3){
				renderFront = true;				
			}
			if(dir == 5){
				renderRight = true;
			}
			if(dir == 2){
				renderBack = true;
			}
			if(dir == 4){
				renderLeft = true;
			}
		}
		if(washer.getDepth() == 2){
			if(dir == 3){
				renderBack = true;
			}
			if(dir == 5){
				renderLeft = true;
			}
			if(dir == 2){
				renderFront = true;
			}
			if(dir == 4){
				renderRight = true;
			}
		}
		if(washer.getHoriz() == -1){
			if(dir == 3 || dir == 2){
				renderLeft = true;
			}
			if(dir == 5 || dir == 4){
				renderBack = true;
			}
		}
		if(washer.getHoriz() == 1){
			if(dir == 3 || dir == 2){
				renderRight = true;
			}
			if(dir == 5 || dir == 4){
				renderFront = true;
			}
		}
		if(dir < 2 || dir > 5){
			renderTop = true;
			renderBottom = true;
			renderLeft = true;
			renderRight = true;
			renderFront = true;
			renderBack = true;
		}
		
		
		if(renderTop){
			float uR = u + one;
			float UR = U - one;
			float vR = v + one;
			float VR = V - one;
			if(renderFront){
				uR = u;
			}
			if(renderBack){
				UR = U;
			}
			if(renderRight){
				VR = V;
			}
			if(renderLeft){
				vR = v;
			}
			tessellator.addVertexWithUV(0, 1, 1, uR, vR);
			tessellator.addVertexWithUV(1, 1, 1, uR, VR);
			tessellator.addVertexWithUV(1, 1, 0, UR, VR);
			tessellator.addVertexWithUV(0, 1, 0, UR, vR);
		}
		if(renderBottom){
			float uR = u + one;
			float UR = U - one;
			float vR = v + one;
			float VR = V - one;
			if(renderFront){
				uR = u;
			}
			if(renderBack){
				UR = U;
			}
			if(renderLeft){
				VR = V;
			}
			if(renderRight){
				vR = v;
			}
			tessellator.addVertexWithUV(1, 0, 1, uR, vR);
			tessellator.addVertexWithUV(0, 0, 1, uR, VR);
			tessellator.addVertexWithUV(0, 0, 0, UR, VR);
			tessellator.addVertexWithUV(1, 0, 0, UR, vR);
		}
		if(renderLeft){
			float uR = u + one;
			float UR = U - one;
			float vR = v + one;
			float VR = V - one;
			
			if(renderTop){
				VR = V;
			}
			if(renderBottom){
				vR = v;
			}
			if(renderFront){
				uR = u;
			}
			if(renderBack){
				UR = U;
			}
			
			tessellator.addVertexWithUV(0, 0, 1, uR, vR);
			tessellator.addVertexWithUV(0, 1, 1, uR, VR);
			tessellator.addVertexWithUV(0, 1, 0, UR, VR);
			tessellator.addVertexWithUV(0, 0, 0, UR, vR);
		}
		if(renderRight){
			float uR = u + one;
			float UR = U - one;
			float vR = v + one;
			float VR = V - one;
			
			if(renderTop){
				VR = V;
			}
			if(renderBottom){
				vR = v;
			}
			if(renderBack){
				uR = u;
			}
			if(renderFront){
				UR = U;
			}
			tessellator.addVertexWithUV(1, 0, 0, uR, vR);
			tessellator.addVertexWithUV(1, 1, 0, uR, VR);
			tessellator.addVertexWithUV(1, 1, 1, UR, VR);
			tessellator.addVertexWithUV(1, 0, 1, UR, vR);
		}
		if(renderFront){
			float uR = u + one;
			float UR = U - one;
			float vR = v + one;
			float VR = V - one;
			
			if(renderTop){
				VR = V;
			}
			if(renderBottom){
				vR = v;
			}
			if(renderRight){
				uR = u;
			}
			if(renderLeft){
				UR = U;
			}
			
			tessellator.addVertexWithUV(1, 0, 1, uR, vR);
			tessellator.addVertexWithUV(1, 1, 1, uR, VR);
			tessellator.addVertexWithUV(0, 1, 1, UR, VR);
			tessellator.addVertexWithUV(0, 0, 1, UR, vR);
		}
		if(renderBack){
			float uR = u + one;
			float UR = U - one;
			float vR = v + one;
			float VR = V - one;
			
			if(renderTop){
				VR = V;
			}
			if(renderBottom){
				vR = v;
			}
			if(renderLeft){
				uR = u;
			}
			if(renderRight){
				UR = U;
			}
			
			tessellator.addVertexWithUV(0, 0, 0, uR, vR);
			tessellator.addVertexWithUV(0, 1, 0, uR, VR);
			tessellator.addVertexWithUV(1, 1, 0, UR, VR);
			tessellator.addVertexWithUV(1, 0, 0, UR, vR);
		}
		
		
		
		//again and again, until you're done, then:
		tessellator.addTranslation(-x, -y, -z);
		
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return false;
	}

	@Override
	public int getRenderId() {
		return Renderers.dummyWasherId;
	}

}
