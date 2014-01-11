package k4unl.minecraft.Hydraulicraft.client.renderers;

import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RendererHarvesterTrolley extends TileEntitySpecialRenderer {

	private static final ResourceLocation resLoc =
		new ResourceLocation(ModInfo.LID,"textures/model/harvesterTrolley_tmap.png");
	
	public RendererHarvesterTrolley(){
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y,
			double z, float f) {
		GL11.glPushMatrix();
		
		GL11.glTranslatef((float) x, (float) y, (float)z);
		
		//Get metadata for rotation:
		int metadata = tileentity.getBlockMetadata();
		switch(metadata){
		case 2:
			GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(-1.0F, 0.0F, 0.0F);
			break;
		case 3:
			GL11.glRotatef(-90F, 0.0F, 1.0F, 0F);
			GL11.glTranslatef(0.0F, 0.0F, -1.0F);
			break;
		case 4:
			GL11.glRotatef(180F, 0.0F, 1.0F, 0F);
			GL11.glTranslatef(-1.0F, 0.0F, -1.0F);
			break;
		}
		
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
		GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
		//Do rendering
		drawBase((TileHarvesterTrolley) tileentity);
		drawHead((TileHarvesterTrolley) tileentity);
		
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		//GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		
	}
	
	private void drawBase(TileHarvesterTrolley tileentity){
		float endWidth = 0.8F;
		float beginWidth = 0.2F;
		//Draw TOP side.
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(1.0F, 0.0F, 0.0F);
		GL11.glVertex3f(beginWidth, endWidth, endWidth);
		GL11.glVertex3f(endWidth, endWidth, endWidth);
		GL11.glVertex3f(endWidth, endWidth, beginWidth);
		GL11.glVertex3f(beginWidth, endWidth, beginWidth);
		GL11.glEnd();
		
		//Draw bottom side.
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(1.0F, 1.0F, 0.0F);
		GL11.glVertex3f(endWidth, beginWidth, endWidth);
		GL11.glVertex3f(beginWidth, beginWidth, endWidth);
		GL11.glVertex3f(beginWidth, beginWidth, beginWidth);
		GL11.glVertex3f(endWidth, beginWidth, beginWidth);
		GL11.glEnd();
		
		//Draw back side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(0.0F, 1.0F, 0.0F);
		GL11.glVertex3f(beginWidth, beginWidth, endWidth);
		GL11.glVertex3f(beginWidth, endWidth, endWidth);
		GL11.glVertex3f(beginWidth, endWidth, beginWidth);
		GL11.glVertex3f(beginWidth, beginWidth, beginWidth);
		GL11.glEnd();
		
		//Draw front side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(0.0F, 1.0F, 1.0F);
		GL11.glVertex3f(endWidth, beginWidth, beginWidth);
		GL11.glVertex3f(endWidth, endWidth, beginWidth);
		GL11.glVertex3f(endWidth, endWidth, endWidth);
		GL11.glVertex3f(endWidth, beginWidth, endWidth);
		GL11.glEnd();
		
		//Draw right side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(0.0F, 0.0F, 1.0F);
		GL11.glVertex3f(beginWidth, beginWidth, beginWidth); 
		GL11.glVertex3f(beginWidth, endWidth, beginWidth); 
		GL11.glVertex3f(endWidth, endWidth, beginWidth); 
		GL11.glVertex3f(endWidth, beginWidth, beginWidth);
		GL11.glEnd();
		
		//Draw left side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(0.0F, 0.0F, 0.0F);
		GL11.glVertex3f(beginWidth, beginWidth, endWidth);
		GL11.glVertex3f(endWidth, beginWidth, endWidth);
		GL11.glVertex3f(endWidth, endWidth, endWidth);
		GL11.glVertex3f(beginWidth, endWidth, endWidth);
		GL11.glEnd();
	}
	
	private void drawHead(TileHarvesterTrolley tileentity){
		float width = 0.2F;
		float endWidth = 0.8F;
		float beginWidth = 0.2F;
		float length = 0.9F;
		
		float beginCoord = 0.8F;
		float endCoord = length + beginCoord;
		float centerCoord = 0.5F;
		float beginCenter = centerCoord - (width/2);
		float endCenter = 1F - beginCenter;
		
		//Draw back side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(0.0F, 1.0F, 0.0F);
		GL11.glVertex3f(beginCenter, beginCoord, endCenter); //Bottom right corner
		GL11.glVertex3f(beginCenter, endCoord, endCenter);  //Top right corner
		GL11.glVertex3f(beginCenter, endCoord, beginCenter);  //Top left corner
		GL11.glVertex3f(beginCenter, beginCoord, beginCenter); //Bottom left corner.
		GL11.glEnd();
		
		//Draw front side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(0.0F, 1.0F, 1.0F);
		GL11.glVertex3f(endCenter, beginCoord, beginCenter); //BR
		GL11.glVertex3f(endCenter, endCoord, beginCenter); //TR
		GL11.glVertex3f(endCenter, endCoord, endCenter); //TL
		GL11.glVertex3f(endCenter, beginCoord, endCenter); //BL
		GL11.glEnd();
		
		//Draw right side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(0.0F, 0.0F, 1.0F);
		GL11.glVertex3f(beginCenter, beginCoord, beginCenter); //BR
		GL11.glVertex3f(beginCenter, endCoord, beginCenter);  //TR
		GL11.glVertex3f(endCenter, endCoord, beginCenter);  //TL
		GL11.glVertex3f(endCenter, beginCoord, beginCenter); //BL
		GL11.glEnd();
		
		//Draw left side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(0.0F, 0.0F, 0.0F);
		GL11.glVertex3f(beginCenter, beginCoord, endCenter); //BL
		GL11.glVertex3f(endCenter, beginCoord, endCenter); //BR
		GL11.glVertex3f(endCenter, endCoord, endCenter); //TR
		GL11.glVertex3f(beginCenter, endCoord, endCenter); //TL
		GL11.glEnd();
	}

}
