package k4unl.minecraft.Hydraulicraft.client.renderers;

import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Vector3fMax;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class RenderHelper {
	static float lightBottom = 0.5F;
	static float lightTop = 1.0F;
	static float lightEastWest = 0.8F;
	static float lightNorthSouth = 0.6F;
	private static Tessellator tess = Tessellator.instance;
	public static float pixel = 1.0F/16.0F;
	
	public static void vertexWithTexture(float x, float y, float z, float tL, float tT){
		GL11.glTexCoord2f(tL, tT);
		GL11.glVertex3f(x, y, z);
	}
	
	public static void tesselatedTexture(float x, float y, float z, float tL, float tT){
		tess.addVertexWithUV(x, y, z, tL, tT);
	}
	
	public static void startTesselating(){
		tess.startDrawingQuads();
	}
	
	public static void tesselatorDraw(){
		tess.draw();
	}
	
	public static void drawCube(Vector3fMax vector){
		drawTexturedCube(vector);
	}
	
	public static void drawCube(Vector3fMax vector, boolean doColors){
		if(doColors){
			drawColoredCube(vector);
		}else{
			drawCube(vector);
		}
	}
	
	public static void drawColoredCube(Vector3fMax vector){
		//Top side
		GL11.glColor3f(1.0F, 0.0F, 0.0F);
		GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMax());
		GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMax());
		GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMin());
		GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMin());
		
		//Bottom side
		GL11.glColor3f(1.0F, 1.0F, 0.0F);
		GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMax());
		GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMax());
		GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMin());
		GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMin());
		
		//Draw west side:
		GL11.glColor3f(0.0F, 1.0F, 0.0F);
		GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMax());
		GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMax());
		GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMin());
		GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMin());
		
		//Draw east side:
		GL11.glColor3f(0.0F, 1.0F, 1.0F);
		GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMin());
		GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMin());
		GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMax());
		GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMax());
		
		//Draw north side
		GL11.glColor3f(0.0F, 0.0F, 1.0F);
		GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMin()); 
		GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMin());
		GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMin());
		GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMin());

		//Draw south side
		GL11.glColor3f(0.0F, 0.0F, 0.0F);
		GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMax());
		GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMax());
		GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMax());
		GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMax());
	}	
	
	public static void setARGBFromHex(int hexColor){
		float a = (hexColor >> 24 & 255) / 255.0F;
        float r = (hexColor >> 16 & 255) / 255.0F;
        float g = (hexColor >> 8 & 255) / 255.0F;
        float b = (hexColor & 255) / 255.0F;
        
        GL11.glColor4f(r, g, b, a);
	}
	
	public static void setRGBFromHex(int hexColor){
        float r = (hexColor >> 16 & 255) / 255.0F;
        float g = (hexColor >> 8 & 255) / 255.0F;
        float b = (hexColor & 255) / 255.0F;
        
        GL11.glColor3f(r, g, b);
	}
	
	public static void drawCubeWithoutColor(Vector3fMax vector){
		//Top side
		GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMax());
		GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMax());
		GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMin());
		GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMin());
		
		//Bottom side
		GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMax());
		GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMax());
		GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMin());
		GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMin());
		
		//Draw west side:
		GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMax());
		GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMax());
		GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMin());
		GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMin());
		
		//Draw east side:
		GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMin());
		GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMin());
		GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMax());
		GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMax());
		
		//Draw north side
		GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMin()); 
		GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMin());
		GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMin());
		GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMin());

		//Draw south side
		GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMax());
		GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMax());
		GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMax());
		GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMax());
	}

	/**
	 * Draws a cube with the size of vector. Every face has the same color This uses the Tessellator
	 *
	 * @author Koen Beckers (K4Unl)
	 * @param vector
	 */
	public static void drawTesselatedCube(Vector3fMax vector) {

		Tessellator t = Tessellator.instance;
		boolean wasTesselating = false;

		// Check if we were already tesselating
		try {
			t.startDrawingQuads();
		} catch (IllegalStateException e) {
			wasTesselating = true;
		}

		// Top side
		t.setColorRGBA_F(1.0F, 0.0F, 0.0F, 0.7F);
		t.setNormal(0, 1, 0);
		t.addVertex(vector.getXMin(), vector.getYMax(), vector.getZMax());
		t.addVertex(vector.getXMax(), vector.getYMax(), vector.getZMax());
		t.addVertex(vector.getXMax(), vector.getYMax(), vector.getZMin());
		t.addVertex(vector.getXMin(), vector.getYMax(), vector.getZMin());

		// Bottom side
		t.setNormal(0, -1, 0);
		t.setColorRGBA_F(1.0F, 1.0F, 0.0F, 0.7F);
		t.addVertex(vector.getXMax(), vector.getYMin(), vector.getZMax());
		t.addVertex(vector.getXMin(), vector.getYMin(), vector.getZMax());
		t.addVertex(vector.getXMin(), vector.getYMin(), vector.getZMin());
		t.addVertex(vector.getXMax(), vector.getYMin(), vector.getZMin());

		// Draw west side:
		t.setNormal(-1, 0, 0);
		t.setColorRGBA_F(0.0F, 1.0F, 0.0F, 0.7F);
		t.addVertex(vector.getXMin(), vector.getYMin(), vector.getZMax());
		t.addVertex(vector.getXMin(), vector.getYMax(), vector.getZMax());
		t.addVertex(vector.getXMin(), vector.getYMax(), vector.getZMin());
		t.addVertex(vector.getXMin(), vector.getYMin(), vector.getZMin());

		// Draw east side:
		t.setNormal(1, 0, 0);
		t.setColorRGBA_F(0.0F, 1.0F, 1.0F, 0.7F);
		t.addVertex(vector.getXMax(), vector.getYMin(), vector.getZMin());
		t.addVertex(vector.getXMax(), vector.getYMax(), vector.getZMin());
		t.addVertex(vector.getXMax(), vector.getYMax(), vector.getZMax());
		t.addVertex(vector.getXMax(), vector.getYMin(), vector.getZMax());

		// Draw north side
		t.setNormal(0, 0, -1);
		t.setColorRGBA_F(0.0F, 0.0F, 1.0F, 0.7F);
		t.addVertex(vector.getXMin(), vector.getYMin(), vector.getZMin());
		t.addVertex(vector.getXMin(), vector.getYMax(), vector.getZMin());
		t.addVertex(vector.getXMax(), vector.getYMax(), vector.getZMin());
		t.addVertex(vector.getXMax(), vector.getYMin(), vector.getZMin());

		// Draw south side
		t.setNormal(0, 0, 1);
		t.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.7F);
		t.addVertex(vector.getXMin(), vector.getYMin(), vector.getZMax());
		t.addVertex(vector.getXMax(), vector.getYMin(), vector.getZMax());
		t.addVertex(vector.getXMax(), vector.getYMax(), vector.getZMax());
		t.addVertex(vector.getXMin(), vector.getYMax(), vector.getZMax());

		if (!wasTesselating) {
			t.draw();
		}
	}
	
	public static void drawTesselatedCubeWithTexture(Vector3fMax vector, IIcon icon){
		Tessellator tessellator = Tessellator.instance;

		boolean wasTessellating = false;
		try {
			tessellator.startDrawingQuads();
		} catch (IllegalStateException e) {
			wasTessellating = true;
		}
		FMLClientHandler.instance().getClient().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		
		tessellator.setTextureUV(icon.getMinU(), icon.getMinV());
		
		float u = icon.getMinU();
		float v = icon.getMinV();
		float U = icon.getMaxU();
		float V = icon.getMaxV();
		//Top side
		tessellator.setNormal(0, 1, 0);
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMax(), vector.getZMax(), u, v);
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMax(), vector.getZMax(), u, V);
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMax(), vector.getZMin(), U, V);
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMax(), vector.getZMin(), U, v);
		
		//Bottom side
		tessellator.setNormal(0, -1, 0);
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMin(), vector.getZMax(), u, v);
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMin(), vector.getZMax(), u, V);
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMin(), vector.getZMin(), U, V);
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMin(), vector.getZMin(), U, v);
		
		//Draw west side:
		tessellator.setNormal(-1, 0, 0);
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMin(), vector.getZMax(), u, v);
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMax(), vector.getZMax(), u, V);
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMax(), vector.getZMin(), U, V);
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMin(), vector.getZMin(), U, v);
		
		//Draw east side:
		tessellator.setNormal(1, 0, 0);
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMin(), vector.getZMin(), u, v);
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMax(), vector.getZMin(), u, V);
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMax(), vector.getZMax(), U, V);
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMin(), vector.getZMax(), U, v);
		
		//Draw north side
		tessellator.setNormal(0, 0, -1);
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMin(), vector.getZMin(), u, v); 
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMax(), vector.getZMin(), u, V);
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMax(), vector.getZMin(), U, V);
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMin(), vector.getZMin(), U, v);

		//Draw south side
		tessellator.setNormal(0, 0, 1);
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMin(), vector.getZMax(), u, v);
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMin(), vector.getZMax(), u, V);
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMax(), vector.getZMax(), U, V);
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMax(), vector.getZMax(), U, v);

		if (!wasTessellating) {
			tessellator.draw();
		}
	}

		
	public static void drawTexturedCube(Vector3fMax vector){
		//Top side:
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.0F, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.5F, 0.0F);		
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.0F, 0.5F);
		
		//Bottom side:
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.0F, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.5F, 0.0F);		
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.0F, 0.5F);

		//Draw west side:
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.5F, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.0F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 0.0F, 0.0F);
		
		//Draw east side:
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.5F, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.0F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.0F, 0.0F);
		
		//Draw north side
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 0.5F, 0.0F); 
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.0F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.0F, 0.0F);

		//Draw south side
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.0F, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.5F, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.0F, 0.5F);
	}
	
	public static void drawTexturedCubeWithLight(Vector3fMax vector, TileEntity t){
		//TODO: FIX ME
		drawTexturedCube(vector);
		/*float light = t.blockType.getBlockBrightness(t.getWorldObj(), t.xCoord, t.yCoord, t.zCoord);
		light = (light + ((0.8f - light) * 0.4f)) * 0.9F;
		//light = 1.0F;
		
		int l = t.blockType.colorMultiplier(t.getWorldObj(), t.xCoord, t.yCoord, t.zCoord);
        float f = (l >> 16 & 255) / 255.0F;
        float f1 = (l >> 8 & 255) / 255.0F;
        float f2 = (l & 255) / 255.0F;
		//Tessellator tessellator = Tessellator.instance;
		
		//tessellator.startDrawingQuads();
		//tessellator.setBrightness(brightness);

		GL11.glColor3f(lightTop * light * f, lightTop * light * f1, lightTop * light * f2);
		//Top side:
		
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.0F, 0.0F); //BL
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.5F, 0.0F); //BR
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.5F, 0.5F); //TR
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.0F, 0.5F); //TL
		
		GL11.glColor3f(lightBottom * light * f, lightBottom * light * f1, lightBottom * light * f2);
		//Bottom side:
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.0F, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.5F, 0.0F);		
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.0F, 0.5F);

		//Draw west side:
		GL11.glColor3f(lightEastWest * light * f, lightEastWest * light * f1, lightEastWest * light * f2);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.5F, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.0F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 0.0F, 0.0F);
		
		//Draw east side:
		GL11.glColor3f(lightEastWest * light, lightEastWest * light, lightEastWest * light);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.5F, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.0F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.0F, 0.0F);
		
		//Draw north side
		GL11.glColor3f(lightNorthSouth * light, lightNorthSouth * light, lightNorthSouth * light);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 0.5F, 0.0F); 
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.0F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.0F, 0.0F);

		//Draw south side
		GL11.glColor3f(lightNorthSouth * light, lightNorthSouth * light, lightNorthSouth * light);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.0F, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.5F, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.0F, 0.5F);
		
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		*/
	}
	
	public static void draw2DCircle(float xCenter, float yCenter, float r){
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_TRIANGLES);
		int angle = 0;
		int resolution = 10;
		float dToR = (float)(Math.PI / 180.0);
		
		double prevX = xCenter + (r*Math.cos((angle-resolution)*dToR));
		double prevY = yCenter + (r*Math.sin((angle-resolution)*dToR));
		while(angle < 360){
			double x = xCenter + (r*Math.cos(angle*dToR));
			double y = yCenter + (r*Math.sin(angle*dToR));
			
			GL11.glVertex2f(xCenter, yCenter);
			GL11.glVertex2d(x, y);
			GL11.glVertex2d(prevX, prevY);
			prevX = x;
			prevY = y;
			
			angle+= resolution;
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public static void drawCylinder(float xCenter, float yCenter, float zCenter, float r, float length, float xTextureStart, float yTextureStart, float xTextureEnd, float yTextureEnd){
		int ltd = 0;
		float x;
		float z;
		float x2;
		float z2;
		float dToR = (float)(Math.PI / 180.0);
		int reso = 20;

		GL11.glBegin(GL11.GL_QUADS);
		for(ltd = 0; ltd < 360; ltd+=reso){
			x = (float) (xCenter + (Math.sin(ltd * dToR) * r));
			z = (float) (zCenter + (Math.cos(ltd * dToR) * r));
			
			x2 = (float) (xCenter + (Math.sin((ltd+reso) * dToR) * r));
			z2 = (float) (zCenter + (Math.cos((ltd+reso) * dToR) * r));
			
			//GL11.glColor3f(ltd/360.0F, 0.0F, 0F);
			
			GL11.glTexCoord2f(xTextureStart, yTextureStart);
			GL11.glVertex3f(x2, yCenter, z2);
			
			GL11.glTexCoord2f(xTextureEnd, yTextureStart);
			GL11.glVertex3f(x2, yCenter+length, z2);
			
			GL11.glTexCoord2f(xTextureEnd, yTextureEnd);
			GL11.glVertex3f(x, yCenter+length, z);
			
			GL11.glTexCoord2f(xTextureStart, yTextureEnd);
			GL11.glVertex3f(x, yCenter, z);
		}
		GL11.glEnd();
		
		/*
		GL11.glColor3f(1.0F, 1.0F, 0.0F);
		
		Cylinder mainCylinder = new Cylinder();
		GL11.glTranslatef(xCenter, yCenter, zCenter+r);
		mainCylinder.draw(r, r, length-r*2, 10, 10);
		
		Sphere endCap = new Sphere();
		endCap.draw(r, 10, 10);
		GL11.glTranslatef(0, 0, length-r*2);
		endCap.draw(r, 10, 10);*/
	}
	

	
	public static void renderSide(Vector3fMax vector, ForgeDirection dir){
		//Top side
		if(dir == ForgeDirection.UP){
			GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMax());
			GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMax());
			GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMin());
			GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMin());
		}
		
		//Bottom side
		if(dir == ForgeDirection.DOWN){
			GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMax());
			GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMax());
			GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMin());
			GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMin());
		}
		
		//West side
		if(dir == ForgeDirection.WEST){
			GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMax());
			GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMax());
			GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMin());
			GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMin());
		}
		
		//East side
		if(dir == ForgeDirection.EAST){
			GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMin());
			GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMin());
			GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMax());
			GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMax());
		}
		
		//North side
		if(dir == ForgeDirection.NORTH){
			GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMin());
			GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMin());
			GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMin());
			GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMin());
		}
		
		//South side
		if(dir == ForgeDirection.SOUTH){
			GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMax());
			GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMax());
			GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMax());
			GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMax());
		}
	}
	public static void drawCubeWithLines(int size, boolean isActive, float rF, float gF, float bF){
		float minPP = RenderHelper.pixel * (size+1);
		float minNP = RenderHelper.pixel * size;
		float maxPP = RenderHelper.pixel * (16-(size+1));
		float maxNP = RenderHelper.pixel * (16-size);
		
		Vector3fMax vNS = new Vector3fMax(minPP, minPP, minNP, maxPP, maxPP, maxNP);
		Vector3fMax vEW = new Vector3fMax(minNP, minPP, minPP, maxNP, maxPP, maxPP);
		Vector3fMax vTB = new Vector3fMax(minPP, minNP, minPP, maxPP, maxNP, maxPP);
		GL11.glColor3f(rF, gF, bF);
		RenderHelper.renderSide(vNS, ForgeDirection.NORTH);
		RenderHelper.renderSide(vNS, ForgeDirection.SOUTH);
		
		RenderHelper.renderSide(vEW, ForgeDirection.EAST);
		RenderHelper.renderSide(vEW, ForgeDirection.WEST);
		
		RenderHelper.renderSide(vTB, ForgeDirection.UP);
		RenderHelper.renderSide(vTB, ForgeDirection.DOWN);
		
		if(!isActive){
			GL11.glColor3f(1.0F, 0.0F, 0.0F);
		}else{
			GL11.glColor3f(0.0F, 1.0F, 0.0F);
		}
		Vector3fMax vEWS = new Vector3fMax(minNP, minNP, minNP, maxNP, maxNP, minPP);
		Vector3fMax vEWN = new Vector3fMax(minNP, minNP, maxPP, maxNP, maxNP, maxNP);
		
		Vector3fMax vEWT = new Vector3fMax(minNP, maxPP, minPP, maxNP, maxNP, maxPP);
		Vector3fMax vEWB = new Vector3fMax(minNP, minNP, minPP, maxNP, minPP, maxPP);
		
		Vector3fMax vNSW = new Vector3fMax(minNP, minNP, minNP, minPP, maxNP, maxNP);
		Vector3fMax vNSE = new Vector3fMax(maxPP, minNP, minNP, maxNP, maxNP, maxNP);
		Vector3fMax vNST = new Vector3fMax(minPP, maxPP, minNP, maxPP, maxNP, maxNP);
		Vector3fMax vNSB = new Vector3fMax(minPP, minNP, minNP, maxPP, minPP, maxNP);
		
		
		Vector3fMax vTBW = new Vector3fMax(minNP, minNP, minNP, minPP, maxNP, maxNP);
		Vector3fMax vTBE = new Vector3fMax(maxPP, minNP, minNP, maxNP, maxNP, maxNP);
		Vector3fMax vTBN = new Vector3fMax(minPP, minNP, minNP, maxPP, maxNP, minPP);
		Vector3fMax vTBS = new Vector3fMax(minPP, minNP, maxPP, maxPP, maxNP, maxNP);
		
		RenderHelper.renderSide(vEWS, ForgeDirection.EAST);
		RenderHelper.renderSide(vEWS, ForgeDirection.WEST);
		RenderHelper.renderSide(vEWN, ForgeDirection.EAST);
		RenderHelper.renderSide(vEWN, ForgeDirection.WEST);
		RenderHelper.renderSide(vEWT, ForgeDirection.EAST);
		RenderHelper.renderSide(vEWT, ForgeDirection.WEST);
		RenderHelper.renderSide(vEWB, ForgeDirection.EAST);
		RenderHelper.renderSide(vEWB, ForgeDirection.WEST);
		
		
		RenderHelper.renderSide(vNSW, ForgeDirection.NORTH);
		RenderHelper.renderSide(vNSW, ForgeDirection.SOUTH);
		RenderHelper.renderSide(vNSE, ForgeDirection.NORTH);
		RenderHelper.renderSide(vNSE, ForgeDirection.SOUTH);
		
		RenderHelper.renderSide(vNST, ForgeDirection.NORTH);
		RenderHelper.renderSide(vNST, ForgeDirection.SOUTH);
		RenderHelper.renderSide(vNSB, ForgeDirection.NORTH);
		RenderHelper.renderSide(vNSB, ForgeDirection.SOUTH);
		
		RenderHelper.renderSide(vTBW, ForgeDirection.UP);
		RenderHelper.renderSide(vTBW, ForgeDirection.DOWN);
		RenderHelper.renderSide(vTBE, ForgeDirection.UP);
		RenderHelper.renderSide(vTBE, ForgeDirection.DOWN);
		RenderHelper.renderSide(vTBN, ForgeDirection.UP);
		RenderHelper.renderSide(vTBN, ForgeDirection.DOWN);
		RenderHelper.renderSide(vTBS, ForgeDirection.UP);
		RenderHelper.renderSide(vTBS, ForgeDirection.DOWN);
	}
	
	public static void drawWhiteCube(Vector3fMax vector){
		//Top side
		GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMax());
		GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMax());
		GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMin());
		GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMin());
		
		//Bottom side
		GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMax());
		GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMax());
		GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMin());
		GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMin());
		
		//West side
		GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMax());
		GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMax());
		GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMin());
		GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMin());
		
		//East side
		GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMin());
		GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMin());
		GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMax());
		GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMax());
		
		//North side
		GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMin());
		GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMin());
		GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMin());
		GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMin());
		
		//South side
		GL11.glVertex3f(vector.getXMin(), vector.getYMin(), vector.getZMax());
		GL11.glVertex3f(vector.getXMax(), vector.getYMin(), vector.getZMax());
		GL11.glVertex3f(vector.getXMax(), vector.getYMax(), vector.getZMax());
		GL11.glVertex3f(vector.getXMin(), vector.getYMax(), vector.getZMax());
	}
}
