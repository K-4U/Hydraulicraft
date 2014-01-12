package k4unl.minecraft.Hydraulicraft.client.renderers;

import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicPiston;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;

public class RendererHydraulicPiston extends TileEntitySpecialRenderer {
	private ResourceLocation resLoc = new ResourceLocation("");
	
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
		drawBase((TileHydraulicPiston)tileentity);
		if(!((TileHydraulicPiston)tileentity).getIsHarvesterPart()){
			drawPistonHead((TileHydraulicPiston)tileentity);
		}
		drawPistonArm((TileHydraulicPiston)tileentity);
		
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		//GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		
	}
	
	private void drawBase(TileHydraulicPiston tileentity){
		float half = 0.9F;
		if(tileentity.getIsHarvesterPart()){
			half = 1F;
		}
		//Draw TOP side.
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(1.0F, 0.0F, 0.0F);
		GL11.glVertex3f(0.0F, 1.0F, 1.0F);
		GL11.glVertex3f(half, 1.0F, 1.0F);
		GL11.glVertex3f(half, 1.0F, 0.0F);
		GL11.glVertex3f(0.0F, 1.0F, 0.0F);
		GL11.glEnd();
		
		//Draw bottom side.
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(1.0F, 1.0F, 0.0F);
		GL11.glVertex3f(half, 0.0F, 1.0F);
		GL11.glVertex3f(0.0F, 0.0F, 1.0F);
		GL11.glVertex3f(0.0F, 0.0F, 0.0F);
		GL11.glVertex3f(half, 0.0F, 0.0F);
		GL11.glEnd();
		
		//Draw back side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(0.0F, 1.0F, 0.0F);
		GL11.glVertex3f(0.0F, 0.0F, 1.0F);
		GL11.glVertex3f(0.0F, 1.0F, 1.0F);
		GL11.glVertex3f(0.0F, 1.0F, 0.0F);
		GL11.glVertex3f(0.0F, 0.0F, 0.0F);
		GL11.glEnd();
		
		//Draw front side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(0.0F, 1.0F, 1.0F);
		GL11.glVertex3f(half, 0.0F, 0.0F);
		GL11.glVertex3f(half, 1.0F, 0.0F);
		GL11.glVertex3f(half, 1.0F, 1.0F);
		GL11.glVertex3f(half, 0.0F, 1.0F);
		GL11.glEnd();
		
		//Draw right side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(0.0F, 0.0F, 1.0F);
		GL11.glVertex3f(0.0F, 0.0F, 0.0F); 
		GL11.glVertex3f(0.0F, 1.0F, 0.0F); 
		GL11.glVertex3f(half, 1.0F, 0.0F); 
		GL11.glVertex3f(half, 0.0F, 0.0F);
		GL11.glEnd();
		
		//Draw left side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(0.0F, 0.0F, 0.0F);
		GL11.glVertex3f(0.0F, 0.0F, 1.0F);
		GL11.glVertex3f(half, 0.0F, 1.0F);
		GL11.glVertex3f(half, 1.0F, 1.0F);
		GL11.glVertex3f(0.0F, 1.0F, 1.0F);
		GL11.glEnd();
	}
	
	private void drawPistonHead(TileHydraulicPiston tileentity){
		float half = 0.9F;
		if(tileentity.getIsHarvesterPart()){
			half = 1F;
		}
		float startCoord = half;
		startCoord += tileentity.getExtendedLength();
		
		float headThickness = 0.1F;
		float endCoord = startCoord + headThickness;
		//Draw TOP side.
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(1.0F, 0.0F, 0.0F);
		GL11.glVertex3f(startCoord, 1.0F, 1.0F);
		GL11.glVertex3f(endCoord, 1.0F, 1.0F);
		GL11.glVertex3f(endCoord, 1.0F, 0.0F);
		GL11.glVertex3f(startCoord, 1.0F, 0.0F);
		GL11.glEnd();
		
		//Draw bottom side.
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(1.0F, 1.0F, 0.0F);
		GL11.glVertex3f(endCoord, 0.0F, 1.0F);
		GL11.glVertex3f(startCoord, 0.0F, 1.0F);
		GL11.glVertex3f(startCoord, 0.0F, 0.0F);
		GL11.glVertex3f(endCoord, 0.0F, 0.0F);
		GL11.glEnd();
		
		//Draw back side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(0.0F, 1.0F, 0.0F);
		GL11.glVertex3f(startCoord, 0.0F, 1.0F);
		GL11.glVertex3f(startCoord, 1.0F, 1.0F);
		GL11.glVertex3f(startCoord, 1.0F, 0.0F);
		GL11.glVertex3f(startCoord, 0.0F, 0.0F);
		GL11.glEnd();
		
		//Draw front side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(0.0F, 1.0F, 1.0F);
		GL11.glVertex3f(endCoord, 0.0F, 0.0F);
		GL11.glVertex3f(endCoord, 1.0F, 0.0F);
		GL11.glVertex3f(endCoord, 1.0F, 1.0F);
		GL11.glVertex3f(endCoord, 0.0F, 1.0F);
		GL11.glEnd();
		
		//Draw right side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(0.0F, 0.0F, 1.0F);
		GL11.glVertex3f(startCoord, 0.0F, 0.0F); 
		GL11.glVertex3f(startCoord, 1.0F, 0.0F); 
		GL11.glVertex3f(endCoord, 1.0F, 0.0F); 
		GL11.glVertex3f(endCoord, 0.0F, 0.0F);
		GL11.glEnd();
		
		//Draw left side:
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor3f(0.0F, 0.0F, 0.0F);
		GL11.glVertex3f(startCoord, 0.0F, 1.0F);
		GL11.glVertex3f(endCoord, 0.0F, 1.0F);
		GL11.glVertex3f(endCoord, 1.0F, 1.0F);
		GL11.glVertex3f(startCoord, 1.0F, 1.0F);
		GL11.glEnd();
	}
	
	private void drawPistonArm(TileHydraulicPiston tileentity){
		float half = 0.9F;
		if(tileentity.getIsHarvesterPart()){
			half = 1F;
		}
		float begin = half;
		float totalLength = tileentity.getExtendedLength();
		float maxLength = tileentity.getMaxLength();
		if(tileentity.getIsHarvesterPart()){
			totalLength+=0.5F;
		}
		float remainingPercentage = totalLength;
		float thickness = 0.15F;
		float maxThickness = 0.48F;
		float armLength = 0.81F;
		float thicknessChange = (maxThickness - thickness) / (maxLength / armLength);

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
	}

}
