package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.client.renderers;

import k4unl.minecraft.Hydraulicraft.client.renderers.RenderHelper;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Vector3fMax;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileRFPump;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Disk;
import org.lwjgl.util.glu.Sphere;

import cpw.mods.fml.client.FMLClientHandler;

public class RendererRFPump extends TileEntitySpecialRenderer {

	private static final ResourceLocation resLoc =
			new ResourceLocation(ModInfo.LID,"textures/model/rfPump.png");
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y,
			double z, float f) {
		
		TileRFPump t = (TileRFPump)tileentity;
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
		//GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
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
	
	public void doRender(TileRFPump t, float x, float y,
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
		RenderHelper.drawColoredCube(new Vector3fMax(0.0F, 0.0F, 0.0F, 1.0F, 0.1F, 1.0F));
		RenderHelper.drawColoredCube(new Vector3fMax(0.2F, 0.1F, 0.8F, 0.8F, 0.8F, 1.0F));
		
		RenderHelper.drawColoredCube(new Vector3fMax(0.25F, 0.1F, 0.0F, 0.75F, 0.75F, 0.2F));
		
		GL11.glRotatef(90F, 0.0F, 0.0F, 1.0F);
		GL11.glPushMatrix();
		Cylinder engine = new Cylinder();
		engine.draw(0.2F, 0.2F, 0.8F, 10, 40);
		
		GL11.glColor3f(1.0F,1.0F,1.0F);
		Sphere caps = new Sphere();
		caps.draw(0.2F, 10, 10);
		GL11.glPopMatrix();
	}
}
