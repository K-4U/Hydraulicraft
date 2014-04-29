package k4unl.minecraft.Hydraulicraft.client.renderers.consumers;

import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TileMovingPane;
import k4unl.minecraft.Hydraulicraft.client.renderers.RenderHelper;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Vector3fMax;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class RendererMovingPane extends TileEntitySpecialRenderer {
	private static final ResourceLocation resLoc =
			new ResourceLocation(ModInfo.LID,"textures/model/movingpane.png");

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y,
			double z, float f) {
		
		int metadata = tileEntity.getBlockMetadata();
		doRender((TileMovingPane) tileEntity, x, y, z, f, metadata);
	}
	
	public static void doRender(TileMovingPane tileEntity , double x, double y,
			double z, float f, int metadata){
		GL11.glPushMatrix();
		
		GL11.glTranslatef((float) x, (float) y, (float)z);
		
		//Get metadata for rotation:
		if(tileEntity != null){
			switch(tileEntity.getFacing()){
			case WEST:
				GL11.glRotatef(90F, 0.0F, 0.0F, 1.0F);
				GL11.glTranslatef(0.0F, -1.0F, 0.0F);
				break;
			case SOUTH:
				GL11.glRotatef(90F, 1.0F, 0.0F, 0F);
				GL11.glTranslatef(0.0F, 0.0F, -1.0F);
				break;
			case NORTH:
				GL11.glRotatef(90F, -1.0F, 0.0F, 0F);
				GL11.glTranslatef(0.0F, -1.0F, 0.0F);
				break;
			case DOWN:
				GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
				GL11.glTranslatef(-1.0F,-1.0F, 0.0F);
				break;
			case EAST:
				GL11.glRotatef(90F, 0.0F, 0.0F, -1.0F);
				GL11.glTranslatef(-1.0F, 0.0F, 0.0F);
			default:
				break;
			}
			
			switch(tileEntity.getPaneFacing()){
			case DOWN:
				break;
			case EAST:
				GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(-1.0F, 0.0F, 0.0F);
				break;
			case NORTH:
				break;
			case SOUTH:
				GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(-1.0F, 0.0F, -1.0F);
				break;
			case UNKNOWN:
				break;
			case UP:
				break;
			case WEST:
				GL11.glRotatef(90F, 0.0F, -1.0F, 0.0F);
				GL11.glTranslatef(0.0F, 0.0F, -1.0F);
				break;
			default:
				break;
			
			}
			
		}
		FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		GL11.glPushMatrix();
		
		//GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
		GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
		//Do rendering
		if(tileEntity.getIsPane()){
			drawPane(tileEntity, f);
		}else{
			drawBase();
		}
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		
	}
	
	public static void drawBase(){
		GL11.glBegin(GL11.GL_QUADS);
		RenderHelper.drawTexturedCube(new Vector3fMax(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F));
		GL11.glEnd();
	}
	
	
	public static void drawPane(TileMovingPane pane, float f){
		GL11.glTranslatef(0.0F, 0.005F, 0.005f);
		
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F); //TODO: Fix me for transparency
		GL11.glRotatef(90.0F * pane.getMovedPercentageForRender(f), 1.0F, 0.0F, 0.0f);
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_QUADS);
		Vector3fMax vector = new Vector3fMax(0.0F, -0.005F, -0.005F, 1.0F, 0.995F, 0.005F);
		//Top side:
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.5F, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 1.0F, 0.0F);		
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 1.0F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.5F, 0.5F);
		
		//Bottom side:
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.5F, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 1.0F, 0.0F);		
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 1.0F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.5F, 0.5F);

		//Draw west side:
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 1.0F, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 1.0F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 0.5F, 0.0F);
		
		//Draw east side:
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 1.0F, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 1.0F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.5F, 0.0F);
		
		//Draw north side
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 1.0F, 0.0F); 
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 1.0F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.5F, 0.0F);

		//Draw south side
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.5F, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 1.0F, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 1.0F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.5F, 0.5F);
		
		GL11.glEnd();
		GL11.glDisable(GL11.GL_BLEND);
		
		GL11.glPopMatrix();
	}
}
