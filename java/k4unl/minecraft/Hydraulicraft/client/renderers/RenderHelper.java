package k4unl.minecraft.Hydraulicraft.client.renderers;

import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Vector3fMax;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class RenderHelper {
	static float lightBottom = 0.5F;
	static float lightTop = 1.0F;
	static float lightEastWest = 0.8F;
	static float lightNorthSouth = 0.6F;
	private static Tessellator tess = Tessellator.instance;
	
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
	
	public static void drawTesselatedCubeWithTexture(Vector3fMax vector, IIcon icon){
		Tessellator tessellator = Tessellator.instance;
		
		
		/*if(!tessellator.isDrawing){
			
			wasTesselating = false;
		}*/
		tessellator.startDrawingQuads();
		
		FMLClientHandler.instance().getClient().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		
		tessellator.setTextureUV(icon.getMinU(), icon.getMinV());
		
		float u = icon.getMinU();
		float v = icon.getMinV();
		float U = icon.getMaxU();
		float V = icon.getMaxV();
		//Top side
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMax(), vector.getZMax(), u, v);
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMax(), vector.getZMax(), u, V);
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMax(), vector.getZMin(), U, V);
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMax(), vector.getZMin(), U, v);
		
		//Bottom side
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMin(), vector.getZMax(), u, v);
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMin(), vector.getZMax(), u, V);
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMin(), vector.getZMin(), U, V);
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMin(), vector.getZMin(), U, v);
		
		//Draw west side:
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMin(), vector.getZMax(), u, v);
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMax(), vector.getZMax(), u, V);
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMax(), vector.getZMin(), U, V);
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMin(), vector.getZMin(), U, v);
		
		//Draw east side:
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMin(), vector.getZMin(), u, v);
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMax(), vector.getZMin(), u, V);
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMax(), vector.getZMax(), U, V);
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMin(), vector.getZMax(), U, v);
		
		//Draw north side
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMin(), vector.getZMin(), u, v); 
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMax(), vector.getZMin(), u, V);
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMax(), vector.getZMin(), U, V);
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMin(), vector.getZMin(), U, v);

		//Draw south side
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMin(), vector.getZMax(), u, v);
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMin(), vector.getZMax(), u, V);
		tessellator.addVertexWithUV(vector.getXMax(), vector.getYMax(), vector.getZMax(), U, V);
		tessellator.addVertexWithUV(vector.getXMin(), vector.getYMax(), vector.getZMax(), U, v);
		
		//if(!wasTesselating){
			tessellator.draw();
		//}
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
	
}
