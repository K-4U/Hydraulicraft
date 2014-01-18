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
	
	private float baseWidth_ = 0.1F;
	private float beginTop_ = 0.9F;
	private float endTop = beginTop_ + baseWidth_;
	private float xOffset = 0F;
	private float zOffset = 0F;
	private float beginTopX = beginTop_ + xOffset;
	private float beginTopZ = beginTop_ + zOffset;
	
	private static final float DEG2RAD = (float) (3.14159/180);
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y,
			double z, float f) {
		GL11.glPushMatrix();
		
		GL11.glTranslatef((float) x, (float) y, (float)z);
		
		TileHarvesterTrolley t = (TileHarvesterTrolley)tileentity;
		//Get metadata for rotation:
		int metadata = t.getDir();
		switch(metadata){
		case 3:
			GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(-1.0F, 0.0F, 0.0F);
			break;
		case 1:
			GL11.glRotatef(-90F, 0.0F, 1.0F, 0F);
			GL11.glTranslatef(0.0F, 0.0F, -1.0F);
			break;
		case 0:
			GL11.glRotatef(180F, 0.0F, 1.0F, 0F);
			GL11.glTranslatef(-1.0F, 0.0F, -1.0F);
			break;
		}
		
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
		GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
		//Do rendering
		
		GL11.glTranslatef(0.0F, 0.0F, t.getSideLength());
		drawBase(t);
		drawHead(t);
		drawPistonArm(t);
		
		
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		
	}
	
	
	void drawWheel(float centerX, float centerY, float centerZ){
		float radius = 0.05F;
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
	 
		for (int i=0; i < 360; i+=10){
			float degInRad = i*DEG2RAD;
			float x = (float) (centerX - 0.01);
			float y = (float) (centerY + Math.sin(degInRad)*radius);
			float z = (float) (centerZ + Math.cos(degInRad)*radius);
	      
			GL11.glVertex3d(x, y, z);
		}
		GL11.glEnd();
	}
	

	private void drawBase(TileHarvesterTrolley tileentity){
		float width = 0.45F;
		float length = 0.45F;
		
		float beginCoord = 1.28F;
		float endCoord = length + beginCoord;
		float centerCoord = 0.5F;
		float beginCenter = centerCoord - (width/2);
		float endCenter = 1F - beginCenter;
		GL11.glPushMatrix();
		drawWheel(beginCenter, beginCoord+0.055F, beginCenter+0.045F);
		drawWheel(beginCenter, beginCoord+0.055F, endCenter-0.045F);
		GL11.glRotatef(180F, 0F, 1F, 0F);
		GL11.glTranslatef(-1.0F, 0.0F, -1.0F);
		drawWheel(beginCenter, beginCoord+0.055F, beginCenter+0.045F);
		drawWheel(beginCenter, beginCoord+0.055F, endCenter-0.045F);
		GL11.glPopMatrix();
		
		//Draw TOP side.
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(1.0F, 0.0F, 0.0F);
		GL11.glVertex3f(beginCenter, endCoord, endCenter); //BR
		GL11.glVertex3f(endCenter, endCoord, endCenter); //TR
		GL11.glVertex3f(endCenter, endCoord, beginCenter); //TL
		GL11.glVertex3f(beginCenter, endCoord, beginCenter); //BL
		GL11.glEnd();
		
		//Draw bottom side.
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(1.0F, 1.0F, 0.0F);
		GL11.glVertex3f(endCenter, beginCoord, endCenter); //TR
		GL11.glVertex3f(beginCenter, beginCoord, endCenter);  //BR
		GL11.glVertex3f(beginCenter, beginCoord, beginCenter); //TL
		GL11.glVertex3f(endCenter, beginCoord, beginCenter); //BL
		GL11.glEnd();
		
		
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
	
	private void drawHead(TileHarvesterTrolley tileentity){
		GL11.glPushMatrix();
		float fromEdge = 0.1F;
		float otherEdge = 1.0F - fromEdge;
		GL11.glTranslatef(0.0F, -tileentity.getExtendedLength(), 0.0F);
		//Facing east for TOP and BOTTOM
		//Draw TOP side.
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(1.0F, 0.0F, 0.0F);
		GL11.glVertex3f(fromEdge, endTop, otherEdge); //BR
		GL11.glVertex3f(otherEdge, endTop, otherEdge); //TR
		GL11.glVertex3f(otherEdge, endTop, fromEdge); //TL
		GL11.glVertex3f(fromEdge, endTop, fromEdge); //BL
		GL11.glEnd();
		
		//Draw bottom side.
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(1.0F, 1.0F, 0.0F);
		GL11.glVertex3f(otherEdge, beginTopX, otherEdge); //TR
		GL11.glVertex3f(fromEdge, beginTopX, otherEdge);  //BR
		GL11.glVertex3f(fromEdge, beginTopX, fromEdge); //TL
		GL11.glVertex3f(otherEdge, beginTopX, fromEdge); //BL
		GL11.glEnd();
		
		//Draw back side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(0.0F, 1.0F, 0.0F);
		GL11.glVertex3f(fromEdge, beginTopX, otherEdge); //BR
		GL11.glVertex3f(fromEdge, endTop, otherEdge); //TR
		GL11.glVertex3f(fromEdge, endTop, fromEdge); //TL
		GL11.glVertex3f(fromEdge, beginTopX, fromEdge); //BL
		GL11.glEnd();
		
		//Draw front side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(0.0F, 1.0F, 1.0F);
		GL11.glVertex3f(otherEdge, beginTopX, fromEdge);
		GL11.glVertex3f(otherEdge, endTop, fromEdge);
		GL11.glVertex3f(otherEdge, endTop, otherEdge);
		GL11.glVertex3f(otherEdge, beginTopX, otherEdge);
		GL11.glEnd();
		
		//Draw right side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(0.0F, 0.0F, 1.0F);
		GL11.glVertex3f(fromEdge, beginTopX, fromEdge); 
		GL11.glVertex3f(fromEdge, endTop, fromEdge); 
		GL11.glVertex3f(otherEdge, endTop, fromEdge); 
		GL11.glVertex3f(otherEdge, beginTopX, fromEdge);
		GL11.glEnd();
		
		//Draw left side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(0.0F, 0.0F, 0.0F);
		GL11.glVertex3f(fromEdge, beginTopX, otherEdge);
		GL11.glVertex3f(otherEdge, beginTopX, otherEdge);
		GL11.glVertex3f(otherEdge, endTop, otherEdge);
		GL11.glVertex3f(fromEdge, endTop, otherEdge);
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}
	
	private void drawPistonArm(TileHarvesterTrolley tileentity) {
		float half = 0.8F;
		float begin = half;
		float totalLength = tileentity.getExtendedLength();
		totalLength+=0.3F;
		float maxLength = tileentity.getMaxLength();
		float remainingPercentage = totalLength;
		float thickness = 0.15F;
		float maxThickness = 0.5F;
		float armLength = 0.81F;
		float thicknessChange = (maxThickness - thickness) / (maxLength / armLength);

		GL11.glRotatef(-90F, 0.0F, 0.0F, 1F);
		GL11.glTranslatef(-2.1F, 0.0F, 0.0F);
		while(remainingPercentage > 0F && remainingPercentage < 200F){
			drawPistonArmPiece(thickness, begin, remainingPercentage + begin);
			remainingPercentage-=armLength;
			thickness+=thicknessChange;
		}
	}
	
	private void drawPistonArmPiece(float thickness, float begin, float end){
		float armBeginCoord = 0.5F - (thickness / 2);
		float armEndCoord = 0.5F + (thickness / 2);
		float startCoord = begin;
		float endCoord = end;
		GL11.glColor3f(0.8F, 0.8F, 0.8F);
		
		//Draw TOP side.
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glVertex3f(startCoord, armEndCoord, armEndCoord);
		GL11.glVertex3f(endCoord, armEndCoord, armEndCoord);
		GL11.glVertex3f(endCoord, armEndCoord, armBeginCoord);
		GL11.glVertex3f(startCoord, armEndCoord, armBeginCoord);
		GL11.glEnd();
		
		//Draw bottom side.
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glVertex3f(endCoord, armBeginCoord, armEndCoord);
		GL11.glVertex3f(startCoord, armBeginCoord, armEndCoord);
		GL11.glVertex3f(startCoord, armBeginCoord, armBeginCoord);
		GL11.glVertex3f(endCoord, armBeginCoord, armBeginCoord);
		GL11.glEnd();
		
		//Draw right side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glVertex3f(startCoord, armBeginCoord, armBeginCoord); 
		GL11.glVertex3f(startCoord, armEndCoord, armBeginCoord); 
		GL11.glVertex3f(endCoord, armEndCoord, armBeginCoord); 
		GL11.glVertex3f(endCoord, armBeginCoord, armBeginCoord);
		GL11.glEnd();
		
		//Draw left side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glVertex3f(startCoord, armBeginCoord, armEndCoord);
		GL11.glVertex3f(endCoord, armBeginCoord, armEndCoord);
		GL11.glVertex3f(endCoord, armEndCoord, armEndCoord);
		GL11.glVertex3f(startCoord, armEndCoord, armEndCoord);
		GL11.glEnd();
		
		//Draw front side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(0.5F, 0.5F, 0.5F);
		GL11.glVertex3f(endCoord, armBeginCoord, armBeginCoord);
		GL11.glVertex3f(endCoord, armEndCoord, armBeginCoord);
		GL11.glVertex3f(endCoord, armEndCoord, armEndCoord);
		GL11.glVertex3f(endCoord, armBeginCoord, armEndCoord);
		GL11.glEnd();
		
		//Draw back side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(0.5F, 0.5F, 0.5F);
		GL11.glVertex3f(endCoord, armBeginCoord, armBeginCoord);
		GL11.glVertex3f(endCoord, armEndCoord, armBeginCoord);
		GL11.glVertex3f(endCoord, armEndCoord, armEndCoord);
		GL11.glVertex3f(endCoord, armBeginCoord, armEndCoord);
		GL11.glEnd();
	}

}
