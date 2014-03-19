package k4unl.minecraft.Hydraulicraft.client.renderers;

import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Vector3fMax;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

import codechicken.multipart.TileMultipart;
import cpw.mods.fml.client.FMLClientHandler;

public class RendererPartValve extends TileEntitySpecialRenderer {

	private static final ResourceLocation resLoc[] = {
		new ResourceLocation(ModInfo.LID,"textures/model/hydraulicHose_tmap_0.png"),
		new ResourceLocation(ModInfo.LID,"textures/model/hydraulicHose_tmap_1.png"),
		new ResourceLocation(ModInfo.LID,"textures/model/hydraulicHose_tmap_2.png")
	};
	
	public void doRender(double x, double y, double z, float f, int tier, ForgeDirection facing, boolean hasDirection){
		GL11.glPushMatrix();
		
		GL11.glTranslatef((float) x, (float) y, (float)z);
		
		//Bind texture
		FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc[tier]);
		
		switch(facing){
		case UP:
		case DOWN:
			GL11.glTranslatef(1.0F, 1.0F, 1.0F);
			GL11.glRotatef(90F, -1.0F, 0.0F, 0.0F);
			GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
			break;
		case EAST:
		case WEST:
			GL11.glTranslatef(1.0F, 0.0F, 0.0F);
			//GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(90F, 0.0F, -1.0F, 0.0F);
			break;
		case NORTH:
		case SOUTH:
		case UNKNOWN:
		default:
			break;
		}
		
		
		GL11.glColor3f(0.8F, 0.8F, 0.8F);
		GL11.glPushMatrix();
		
		//GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
		GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
		//Do rendering
		float center = 0.5F;
		float offset = 0.1F;
		float centerOffset = 0.2F;
		
		drawCable(center, -offset, centerOffset);
		drawCable(center, +offset, centerOffset);

		drawCorner(new Vector3fMax(center-centerOffset, center-centerOffset, center-centerOffset, center+centerOffset, center+centerOffset, center+centerOffset));
		
		//GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y,
			double z, float f) {
		if(!(tileentity instanceof TileMultipart)) return;
		/*
		TileMultipart mp = (TileMultipart)tileentity;
		if(Multipart.hasPartValve(mp)){
			PartValve tp = Multipart.getValve(mp);
			doRender(x, y, z, f, tp.getTier());
		}*/
	}
	
	
	private void drawFirstCable(){

	}
	
	
	private void drawCable(float center, float offset, float centerOffset){
		float width = 0.2F;
		float min = (center + offset)- (width / 2);
		float max = (center + offset)  + (width / 2);
		float xMin = center - centerOffset;
		float xMax = center + centerOffset;

		drawCube(new Vector3fMax(min, min, xMax, max, max, 1.0F), ForgeDirection.SOUTH);
		drawCube(new Vector3fMax(min, min, 0.0F, max, max, xMin), ForgeDirection.SOUTH);
	}
	
	private void drawCube(Vector3fMax vector, ForgeDirection dirToDraw){
		GL11.glBegin(GL11.GL_QUADS);
		
		float th = 1.0F;
		
		float txl = 0.0F;
		float txh = th;
		float tyl = 0.0F;
		float tyh = 0.5F;
		
		float sxl = 0.0F;
		float sxh = th;
		float syl = 0.0F;
		float syh = 0.5F;
		
		if(dirToDraw.equals(ForgeDirection.SOUTH) || dirToDraw.equals(ForgeDirection.NORTH)){
			txl = th;
			txh = 0.0F;
			tyl = 0.5F;
			tyh = 0.0F;
		}
		if(dirToDraw.equals(ForgeDirection.UP) || dirToDraw.equals(ForgeDirection.DOWN)){
			sxl = th;
			sxh = 0.0F;
			syl = 0.5F;
			syh = 0.0F;
		}
		//Top side:
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), txl, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), th, tyl);		
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), txh, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.0F, tyh);
		
		//Bottom side:
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), txl, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), th, tyl);		
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), txh, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.0F, tyh);

		//Draw west side:
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), th, syl);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), sxh, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.0F, syh);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), sxl, 0.0F);
		
		//Draw east side:
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 1.0F, syl);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), sxh, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.0F, syh);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), sxl, 0.0F);
		
		//Draw north side
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 1.0F, syl); 
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), sxh, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.0F, syh);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), sxl, 0.0F);

		//Draw south side
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.0F, syl);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), sxh, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 1.0F, syh);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), sxl, 0.5F);
		
		GL11.glEnd();
	}
	
	private void drawCorner(Vector3fMax vector){
		GL11.glBegin(GL11.GL_QUADS);

		//Top side:
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.0F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.5F, 0.5F);		
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.5F, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.0F, 1.0F);
		
		//Bottom side:
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.0F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.5F, 0.5F);		
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 0.5F, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.0F, 1.0F);

		//Draw west side:
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.5F, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.0F, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 0.0F, 0.5F);
		
		
		//Draw east side:
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.5F, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.0F, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.0F, 0.5F);
		
		//Draw north side
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 0.5F, 0.5F); 
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.5F, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.0F, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.0F, 0.5F);

		//Draw south side
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.0F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.5F, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.0F, 1.0F);
		
		GL11.glEnd();
	}
}
