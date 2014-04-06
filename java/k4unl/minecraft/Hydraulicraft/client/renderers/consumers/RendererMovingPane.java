package k4unl.minecraft.Hydraulicraft.client.renderers.consumers;

import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TileMovingPane;
import k4unl.minecraft.Hydraulicraft.client.renderers.RenderHelper;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Vector3fMax;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

public class RendererMovingPane extends TileEntitySpecialRenderer {
/*	private static final ResourceLocation resLoc =
			new ResourceLocation(ModInfo.LID,"textures/model/hydraulicPiston_tmap.png");
	*/
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
		//FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		GL11.glPushMatrix();
		
		GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
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
		RenderHelper.drawColoredCube(new Vector3fMax(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F));
		GL11.glEnd();
	}
	
	
	public static void drawPane(TileMovingPane pane, float f){
		GL11.glTranslatef(0.0F, 0.005F, 0.005f);
		
		GL11.glRotatef(90.0F * pane.getMovedPercentageForRender(f), 1.0F, 0.0F, 0.0f);
		GL11.glPushMatrix();
		GL11.glBegin(GL11.GL_QUADS);
		RenderHelper.drawColoredCube(new Vector3fMax(0.0F, -0.005F, -0.005F, 1.0F, 0.995F, 0.005F));
		GL11.glEnd();
		GL11.glPopMatrix();
	}
}
