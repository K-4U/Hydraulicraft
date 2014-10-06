package k4unl.minecraft.Hydraulicraft.client.renderers.consumers;

import cpw.mods.fml.client.FMLClientHandler;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileMovingPane;
import k4unl.minecraft.k4lib.client.RenderHelper;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;

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
		if(MinecraftForgeClient.getRenderPass() == 0){
			GL11.glColor3f(0.9F, 0.9F, 0.9F);
			GL11.glBegin(GL11.GL_QUADS);
			RenderHelper.drawTexturedCube(new Vector3fMax(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F));
			GL11.glEnd();
		}
	}
	
	
	public static void drawPane(TileMovingPane pane, float f){
		GL11.glBegin(GL11.GL_QUADS);
		RenderHelper.drawTexturedCube(new Vector3fMax(-0.001F, 0.0F, -0.001F, 1.0001F, 0.051F, 0.051F));
		GL11.glEnd();
		GL11.glTranslatef(0.0F, 0.025F, 0.025f);
		
		//
		GL11.glRotatef(90.0F * pane.getMovedPercentageForRender(f), 1.0F, 0.0F, 0.0f);
		//GL11.glAlphaFunc(GL11.GL_GREATER, 0.8F); //TODO: Fix me for transparency
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Vector3fMax vector = new Vector3fMax(0.0F, -0.005F, -0.025F, 1.0F, 0.975F, 0.025F);
		Vector3fMax vectorS = new Vector3fMax(0.001F, -0.004F, -0.024F, 0.999F, 0.974F, 0.024F);
		if(MinecraftForgeClient.getRenderPass() == 0){
			
			GL11.glBegin(GL11.GL_QUADS);
			//Top side:
			/*RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.5F, 0.0F);
			RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 1.0F, 0.0F);		
			RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 1.0F, 0.5F);
			RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.5F, 0.5F);
			
			//Bottom side:
			RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.5F, 0.0F);
			RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 1.0F, 0.0F);		
			RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 1.0F, 0.5F);
			RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.5F, 0.5F);
	*/
			//Draw west side:
			/*RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 1.0F, 0.0F);
			RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 1.0F, 0.5F);
			RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.5F, 0.5F);
			RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 0.5F, 0.0F);
			
			//Draw east side:
			RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 1.0F, 0.0F);
			RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 1.0F, 0.5F);
			RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.5F, 0.5F);
			RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.5F, 0.0F);
			*/
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
			
			
			
		}else{
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glAlphaFunc(GL11.GL_EQUAL, 0.6F); //TODO: Fix me for transparency
			GL11.glColor4f(0.75686F, 0.88F, 0.9294F, 0.6F);
			
			
			GL11.glBegin(GL11.GL_QUADS);
			//Top side:
			GL11.glVertex3f(vectorS.getXMin(), vectorS.getYMax(), vectorS.getZMax());
			GL11.glVertex3f(vectorS.getXMax(), vectorS.getYMax(), vectorS.getZMax());		
			GL11.glVertex3f(vectorS.getXMax(), vectorS.getYMax(), vectorS.getZMin());
			GL11.glVertex3f(vectorS.getXMin(), vectorS.getYMax(), vectorS.getZMin());
			
			//Bottom side:
			GL11.glVertex3f(vectorS.getXMax(), vectorS.getYMin(), vectorS.getZMax());
			GL11.glVertex3f(vectorS.getXMin(), vectorS.getYMin(), vectorS.getZMax());		
			GL11.glVertex3f(vectorS.getXMin(), vectorS.getYMin(), vectorS.getZMin());
			GL11.glVertex3f(vectorS.getXMax(), vectorS.getYMin(), vectorS.getZMin());
	
			//Draw west side:
			GL11.glVertex3f(vectorS.getXMin(), vectorS.getYMin(), vectorS.getZMax());
			GL11.glVertex3f(vectorS.getXMin(), vectorS.getYMax(), vectorS.getZMax());
			GL11.glVertex3f(vectorS.getXMin(), vectorS.getYMax(), vectorS.getZMin());
			GL11.glVertex3f(vectorS.getXMin(), vectorS.getYMin(), vectorS.getZMin());
			
			//Draw east side:
			GL11.glVertex3f(vectorS.getXMax(), vectorS.getYMin(), vectorS.getZMin());
			GL11.glVertex3f(vectorS.getXMax(), vectorS.getYMax(), vectorS.getZMin());
			GL11.glVertex3f(vectorS.getXMax(), vectorS.getYMax(), vectorS.getZMax());
			GL11.glVertex3f(vectorS.getXMax(), vectorS.getYMin(), vectorS.getZMax());
			
			//Draw north side
			GL11.glVertex3f(vectorS.getXMin(), vectorS.getYMin(), vectorS.getZMin()); 
			GL11.glVertex3f(vectorS.getXMin(), vectorS.getYMax(), vectorS.getZMin());
			GL11.glVertex3f(vectorS.getXMax(), vectorS.getYMax(), vectorS.getZMin());
			GL11.glVertex3f(vectorS.getXMax(), vectorS.getYMin(), vectorS.getZMin());
	
			//Draw south side
			GL11.glVertex3f(vectorS.getXMin(), vectorS.getYMin(), vectorS.getZMax());
			GL11.glVertex3f(vectorS.getXMax(), vectorS.getYMin(), vectorS.getZMax());
			GL11.glVertex3f(vectorS.getXMax(), vectorS.getYMax(), vectorS.getZMax());
			GL11.glVertex3f(vectorS.getXMin(), vectorS.getYMax(), vectorS.getZMax());
			GL11.glEnd();
		}
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F); //TODO: Fix me for transparency
		GL11.glColor4f(0.9F, 0.9F, 0.9F, 1.0F);
		GL11.glPopMatrix();
		
	}
}
