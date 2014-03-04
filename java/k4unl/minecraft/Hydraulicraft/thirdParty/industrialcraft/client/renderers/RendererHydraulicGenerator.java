package k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.client.renderers;

import k4unl.minecraft.Hydraulicraft.client.renderers.RenderHelper;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Vector3fMax;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities.TileHydraulicGenerator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class RendererHydraulicGenerator extends TileEntitySpecialRenderer  {

	private static final ResourceLocation resLoc =
			new ResourceLocation(ModInfo.LID,"textures/model/hydraulicGenerator.png");
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y,
			double z, float f) {
		
		TileHydraulicGenerator t = (TileHydraulicGenerator)tileentity;
		//Get metadata for rotation:
		int rotation = 0;//t.getDir();
		int metadata = t.getBlockMetadata();
		
		doRender(t, (float)x, (float)y, (float)z, f, rotation, metadata);
	}
	
	public void itemRender(float x, float y,
			float z, float f){
		GL11.glPushMatrix();
		
		GL11.glTranslatef(x, y, z);

		FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);
		
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
		GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
		//Do rendering
		GL11.glBegin(GL11.GL_QUADS);
		drawBase();
		GL11.glEnd();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
	
	public void doRender(TileHydraulicGenerator t, float x, float y,
			float z, float f, int rotation, int metadata){
		GL11.glPushMatrix();
		
		GL11.glTranslatef(x, y, z);

		switch(t.getFacing()){
		case EAST:
			GL11.glTranslatef(0.0F, 1.0F, 1.0F);
			GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(90F, 0.0F, 0.0F, -1.0F);
			break;
		case NORTH:
			GL11.glTranslatef(0.0F, 0.0F, 1.0F);
			GL11.glRotatef(90F, -1.0F, 0.0F, 0.0F);
			//GL11.glRotatef(90F, 0.0F, 0.0F, 1.0F);
			break;
		case WEST:
			GL11.glTranslatef(1.0F, 1.0F, 0.0F);
			GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(90F, 0.0F, 0.0F, 1.0F);
			break;
		case DOWN:
			GL11.glTranslatef(0.0F, 1.0F, 1.0F);
			GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
			break;
		case SOUTH:
			GL11.glTranslatef(0.0F, 1.0F, 0.0F);
			GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
			break;
		case UNKNOWN:
		case UP:
		default:
			break;
		}
		
		
		FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);
		
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
		GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
		//Do rendering
		GL11.glBegin(GL11.GL_QUADS);
		drawBase();
		
		GL11.glEnd();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
	
	private void drawBase(){
		float sideXb = 0.125F;
		float sideXe = 0.25F;
		float width = 0.5F / 3F;
		float spacing = 0.5F/ 4F;
		float insetFirst = 0.1F;
		float insetSecond = 0.14F;
		
		Vector3fMax vector = new Vector3fMax(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
		RenderHelper.drawColoredCube(vector);
		
		RenderHelper.drawColoredCube(new Vector3fMax(0F, 0.9F, insetFirst, 1.0F, 1.0F, 1.0F));
		RenderHelper.drawColoredCube(new Vector3fMax(0F, 0.2F, insetFirst, 1.0F, 0.3F, 1.0F));
		
		RenderHelper.drawColoredCube(new Vector3fMax(0.0F, 0.3F, insetFirst, spacing, 0.9F, 1.0F));

		RenderHelper.drawColoredCube(new Vector3fMax(spacing, 0.3F, insetSecond, spacing+width, 0.9F, 1.0F));
		RenderHelper.drawColoredCube(new Vector3fMax(spacing+width, 0.3F, insetFirst, (spacing*2)+(width), 0.9F, 1.0F));
		
		RenderHelper.drawColoredCube(new Vector3fMax((spacing*2)+width, 0.3F, insetSecond, (spacing*2)+(width*2), 0.9F, 1.0F));
		RenderHelper.drawColoredCube(new Vector3fMax((spacing*2)+(width*2), 0.3F, insetFirst, (spacing*2)+(width*3), 0.9F, 1.0F));
		
		RenderHelper.drawColoredCube(new Vector3fMax((spacing*2)+(width*3), 0.3F, insetSecond, (spacing*3)+(width*3), 0.9F, 1.0F));
		RenderHelper.drawColoredCube(new Vector3fMax((spacing*3)+(width*3), 0.3F, insetFirst, (spacing*4)+(width*3), 0.9F, 1.0F));
		
		//RenderHelper.drawColoredCube(new Vector3fMax((spacing*3)+(width*2), 0.3F, 0.1F, (spacing*3)+(width*3), 0.9F, 0.1F));
		//RenderHelper.drawColoredCube(new Vector3fMax(spacing, 0.3F, 0.0F, spacing+width, 0.9F, 0.1F));
		//RenderHelper.drawColoredCube(new Vector3fMax((spacing*2)+(width), 0.3F, 0.0F, (spacing*2)+(width*2), 0.9F, 0.1F));
		//RenderHelper.drawColoredCube(new Vector3fMax((spacing*3)+(width*2), 0.3F, 0.0F, (spacing*3)+(width*3), 0.9F, 0.1F));
		/*//Top side:
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
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), sideXe, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), sideXb, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), sideXb, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), sideXe, 0.5F);
		
		//Draw east side:
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), sideXe, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), sideXb, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), sideXb, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), sideXe, 0.5F);
		
		//Draw north side
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), sideXe, 1.0F); 
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), sideXb, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), sideXb, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), sideXe, 0.5F);

		//Draw south side
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), sideXb, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), sideXb, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), sideXe, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), sideXe, 0.5F);*/
	}
		
}
