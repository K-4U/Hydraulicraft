package k4unl.minecraft.Hydraulicraft.client.renderers.consumers;

import cpw.mods.fml.client.FMLClientHandler;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicPiston;
import k4unl.minecraft.k4lib.client.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RendererHydraulicPiston extends TileEntitySpecialRenderer {
	private static final ResourceLocation resLoc =
			new ResourceLocation(ModInfo.LID,"textures/model/hydraulicPiston_tmap.png");
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y,
			double z, float f) {
		
		int metadata = tileentity.getBlockMetadata();
		doRender((TileHydraulicPiston) tileentity, x, y, z, f, metadata);
	}
	
	public static void doRender(TileHydraulicPiston tileentity , double x, double y,
			double z, float f, int metadata){
		GL11.glPushMatrix();
		
		GL11.glTranslatef((float) x, (float) y, (float)z);
		
		//Get metadata for rotation:
		if(tileentity != null){
			switch(tileentity.getFacing()){
			case WEST:
				GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(-1.0F, 0.0F, -1.0F);
				break;
			case SOUTH:
				GL11.glRotatef(-90F, 0.0F, 1.0F, 0F);
				GL11.glTranslatef(0.0F, 0.0F, -1.0F);
				break;
			case NORTH:
				GL11.glRotatef(90F, 0.0F, 1.0F, 0F);
				GL11.glTranslatef(-1.0F, 0.0F, 0.0F);
				break;
			default:
				break;
			}
		}
		
		FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);
		GL11.glColor3f(0.8F, 0.8F, 0.8F);
		GL11.glPushMatrix();
		
		//GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
		GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
		//Do rendering
		drawBase(tileentity);
		if(tileentity != null){
			if(!tileentity.getIsHarvesterPart()){
				drawPistonHead(tileentity);
			}
		}else{
			drawPistonHead(null);
		}
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		drawPistonArm(tileentity, f);
		
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		
	}
	
	public static void drawBase(TileHydraulicPiston tileentity){
		float half = 0.9F;
		if(tileentity != null){
			if(tileentity.getIsHarvesterPart()){
				half = 1F;
			}
		}
		//Draw TOP side.
		GL11.glBegin(GL11.GL_QUADS);
		RenderHelper.vertexWithTexture(0.0F, 1.0F, 1.0F, 0F, 0F);
		RenderHelper.vertexWithTexture(half, 1.0F, 1.0F, 0.5F, 0F);		
		RenderHelper.vertexWithTexture(half, 1.0F, 0.0F, 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(0.0F, 1.0F, 0.0F, 0.0F, 0.5F);
		
		//Draw bottom side.
		RenderHelper.vertexWithTexture(half, 0.0F, 1.0F, 0.0F, 0F); // TR
		RenderHelper.vertexWithTexture(0.0F, 0.0F, 1.0F, 0.5F, 0F);	 //BR
		RenderHelper.vertexWithTexture(0.0F, 0.0F, 0.0F, 0.5F, 0.5F); //TL
		RenderHelper.vertexWithTexture(half, 0.0F, 0.0F, 0.0F, 0.5F); //BL
		

		//Draw back side:
		RenderHelper.vertexWithTexture(0.0F, 0.0F, 1.0F, 0.0F, 0.0F); // BR
		RenderHelper.vertexWithTexture(0.0F, 1.0F, 1.0F, 0.5F, 0.0F); //TR
		RenderHelper.vertexWithTexture(0.0F, 1.0F, 0.0F, 0.5F, 0.5F); //TL
		RenderHelper.vertexWithTexture(0.0F, 0.0F, 0.0F, 0.0F, 0.5F); //BL

		//Draw front side:
		
		RenderHelper.vertexWithTexture(half, 0.0F, 0.0F, 0.5F, 0.0F); // BR
		RenderHelper.vertexWithTexture(half, 1.0F, 0.0F, 1.0F, 0.0F); //TR
		RenderHelper.vertexWithTexture(half, 1.0F, 1.0F, 1.0F, 0.5F); //TL
		RenderHelper.vertexWithTexture(half, 0.0F, 1.0F, 0.5F, 0.5F); //BL

		
		//Draw right side:
		RenderHelper.vertexWithTexture(0.0F, 0.0F, 0.0F, 0.0F, 0.0F); // BR
		RenderHelper.vertexWithTexture(0.0F, 1.0F, 0.0F, 0.5F, 0.0F);	 //TR
		RenderHelper.vertexWithTexture(half, 1.0F, 0.0F, 0.5F, 0.5F); //TL
		RenderHelper.vertexWithTexture(half, 0.0F, 0.0F, 0.0F, 0.5F); //BL

		//Draw left side:
		RenderHelper.vertexWithTexture(0.0F, 0.0F, 1.0F, 0.0F, 0.0F); // BR
		RenderHelper.vertexWithTexture(half, 0.0F, 1.0F, 0.5F, 0.0F);	 //TR
		RenderHelper.vertexWithTexture(half, 1.0F, 1.0F, 0.5F, 0.5F); //TL
		RenderHelper.vertexWithTexture(0.0F, 1.0F, 1.0F, 0.0F, 0.5F); //BL
		
		GL11.glEnd();
	}
	
	public static void drawPistonHead(TileHydraulicPiston tileentity){
		float half = 0.9F;
		float startCoord = half;
		
		if(tileentity != null){
			if(tileentity.getIsHarvesterPart()){
				half = 1F;
			}
			startCoord += tileentity.getExtendedLength();
		}
		
		
		float headThickness = 0.1F;
		float endCoord = startCoord + headThickness;
		//Draw TOP side.
		GL11.glBegin(GL11.GL_QUADS);
		
		RenderHelper.vertexWithTexture(startCoord, 1.0F, 1.0F, 0F, 0.5F);
		RenderHelper.vertexWithTexture(endCoord, 1.0F, 1.0F, 0.0976F, 0.5F);		
		RenderHelper.vertexWithTexture(endCoord, 1.0F, 0.0F, 0.0976F, 1.0F);
		RenderHelper.vertexWithTexture(startCoord, 1.0F, 0.0F, 0.0F, 1.0F);

		//Draw bottom side.
		RenderHelper.vertexWithTexture(endCoord, 0.0F, 1.0F, 0F, 0.5F);
		RenderHelper.vertexWithTexture(startCoord, 0.0F, 1.0F, 0.0976F, 0.5F);		
		RenderHelper.vertexWithTexture(startCoord, 0.0F, 0.0F, 0.0976F, 1.0F);
		RenderHelper.vertexWithTexture(endCoord, 0.0F, 0.0F, 0.0F, 1.0F);
		
		//Draw back side:
		RenderHelper.vertexWithTexture(startCoord, 0.0F, 1.0F, 0.0976F, 0.5F);
		RenderHelper.vertexWithTexture(startCoord, 1.0F, 1.0F, 0.5976F, 0.5F);		
		RenderHelper.vertexWithTexture(startCoord, 1.0F, 0.0F, 0.5976F, 1.0F);
		RenderHelper.vertexWithTexture(startCoord, 0.0F, 0.0F, 0.0976F, 1.0F);

		//Draw front side:
		RenderHelper.vertexWithTexture(endCoord, 0.0F, 0.0F, 0.0976F, 0.5F); 
		RenderHelper.vertexWithTexture(endCoord, 1.0F, 0.0F, 0.5976F, 0.5F); 	
		RenderHelper.vertexWithTexture(endCoord, 1.0F, 1.0F, 0.5976F, 1.0F); 
		RenderHelper.vertexWithTexture(endCoord, 0.0F, 1.0F, 0.0976F, 1.0F); 

		
		//Draw right side:
		RenderHelper.vertexWithTexture(startCoord, 0.0F, 0.0F, 0.0976F, 1.0F); //BR
		RenderHelper.vertexWithTexture(startCoord, 1.0F, 0.0F, 0.0976F, 0.5F); // TR
		RenderHelper.vertexWithTexture(endCoord, 1.0F, 0.0F, 0.0F, 0.5F); //TL
		RenderHelper.vertexWithTexture(endCoord, 0.0F, 0.0F, 0.0F, 1.0F); //BL
		
		//Draw left side:
		
		RenderHelper.vertexWithTexture(startCoord, 0.0F, 1.0F, 0.0F, 0.5F); 
		RenderHelper.vertexWithTexture(endCoord, 0.0F, 1.0F, 0.0976F, 0.5F); 
		RenderHelper.vertexWithTexture(endCoord, 1.0F, 1.0F, 0.0976F, 1.0F); 
		RenderHelper.vertexWithTexture(startCoord, 1.0F, 1.0F, 0.0F, 1.0F); 
		GL11.glEnd();
	}
	
	public static void drawPistonArm(TileHydraulicPiston tileentity, float f){
		float half = 0.9F;
		float totalLength = 0F;
		float maxLength = 1F;
		if(tileentity != null){
			totalLength = tileentity.getOldExtendedLength() + (tileentity.getExtendedLength() - tileentity.getOldExtendedLength()) * f;
			maxLength = tileentity.getMaxLength();
			if(tileentity.getIsHarvesterPart()){
				half = 1F;
				totalLength+=0.5F;
			}
		}
		
		float begin = half;
		float remainingPercentage = totalLength;
		float thickness = 0.15F;
		float maxThickness = 0.46F;
		float armLength = 0.81F;
		float thicknessChange = (maxThickness - thickness) / (maxLength / armLength);

		while(remainingPercentage > 0F && remainingPercentage < 200F){
			drawPistonArmPiece(thickness, begin, remainingPercentage + begin);
			remainingPercentage-=armLength;
			thickness+=thicknessChange;
		}
	}
	
	public static void drawPistonArmPiece(float thickness, float begin, float end){
		float armBeginCoord = 0.5F - (thickness / 2);
		float armEndCoord = 0.5F + (thickness / 2);
		float startCoord = begin;
		float endCoord = end;
		GL11.glPushMatrix();
		GL11.glColor3f(0.8F, 0.8F, 0.8F);
		
		//Draw TOP side.
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3f(startCoord, armEndCoord, armEndCoord);
		GL11.glVertex3f(endCoord, armEndCoord, armEndCoord);
		GL11.glVertex3f(endCoord, armEndCoord, armBeginCoord);
		GL11.glVertex3f(startCoord, armEndCoord, armBeginCoord);
		
		//Draw bottom side.
		GL11.glVertex3f(endCoord, armBeginCoord, armEndCoord);
		GL11.glVertex3f(startCoord, armBeginCoord, armEndCoord);
		GL11.glVertex3f(startCoord, armBeginCoord, armBeginCoord);
		GL11.glVertex3f(endCoord, armBeginCoord, armBeginCoord);
		
		//Draw right side:
		GL11.glVertex3f(startCoord, armBeginCoord, armBeginCoord); 
		GL11.glVertex3f(startCoord, armEndCoord, armBeginCoord); 
		GL11.glVertex3f(endCoord, armEndCoord, armBeginCoord); 
		GL11.glVertex3f(endCoord, armBeginCoord, armBeginCoord);
		
		//Draw left side:
		GL11.glVertex3f(startCoord, armBeginCoord, armEndCoord);
		GL11.glVertex3f(endCoord, armBeginCoord, armEndCoord);
		GL11.glVertex3f(endCoord, armEndCoord, armEndCoord);
		GL11.glVertex3f(startCoord, armEndCoord, armEndCoord);
		
		//Draw front side:
		//GL11.glColor3f(0.5F, 0.5F, 0.5F);
		GL11.glVertex3f(endCoord, armBeginCoord, armBeginCoord);
		GL11.glVertex3f(endCoord, armEndCoord, armBeginCoord);
		GL11.glVertex3f(endCoord, armEndCoord, armEndCoord);
		GL11.glVertex3f(endCoord, armBeginCoord, armEndCoord);
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}

}
