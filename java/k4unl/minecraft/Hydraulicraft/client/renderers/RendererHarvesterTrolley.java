package k4unl.minecraft.Hydraulicraft.client.renderers;

import java.util.List;

import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class RendererHarvesterTrolley extends TileEntitySpecialRenderer {

	private static final ResourceLocation resLoc =
		new ResourceLocation(ModInfo.LID,"textures/model/harvesterTrolley_tmap.png");
    private final RenderItem customRenderItem;
    private final EntityItem renderedItem;
	
	public RendererHarvesterTrolley(){
	    customRenderItem = new RenderItem(){
            @Override
            public boolean shouldBob(){

                return false;
            };
        };
        customRenderItem.setRenderManager(RenderManager.instance);
        
        renderedItem = new EntityItem(FMLClientHandler.instance().getClient().theWorld);
        renderedItem.hoverStart = 0.0F;
	}
	
	private final float baseWidth_ = 0.1F;
	private final float beginTop_ = 0.9F;
	private final float endTop = beginTop_ + baseWidth_;
	private final float xOffset = 0F;
	private final float zOffset = 0F;
	private final float beginTopX = beginTop_ + xOffset;
	private final float beginTopZ = beginTop_ + zOffset;
	
	private static final float DEG2RAD = (float) (3.14159/180);
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y,
			double z, float f) {
		
		TileHarvesterTrolley t = (TileHarvesterTrolley)tileentity;
		//Get metadata for rotation:
		int rotation = t.getFacing().ordinal();
		int metadata = t.getBlockMetadata();
		
		doRender(t, (float)x, (float)y, (float)z, f, rotation, metadata);
	}
	
	
	public void doRender(TileHarvesterTrolley t, float x, float y,
			float z, float f, int rotation, int metadata){
		GL11.glPushMatrix();
		
		GL11.glTranslatef(x, y, z);

		switch(ForgeDirection.getOrientation(rotation)){
		case EAST:
			GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(-1.0F, 0.0F, 0.0F);
			break;
		case WEST:
			GL11.glRotatef(-90F, 0.0F, 1.0F, 0F);
			GL11.glTranslatef(0.0F, 0.0F, -1.0F);
			break;
		case NORTH:
			GL11.glRotatef(180F, 0.0F, 1.0F, 0F);
			GL11.glTranslatef(-1.0F, 0.0F, -1.0F);
			break;
		default:
			break;
		}
		
		
		FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);
		GL11.glColor3f(0.8F, 0.8F, 0.8F);
		GL11.glPushMatrix();
		//GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
		GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
		//Do rendering
		if(t != null){
		    float sideLength = t.getOldSideLength() + (t.getSideLength() - t.getOldSideLength()) * f;
			GL11.glTranslatef(0.0F, 0.0F, sideLength);
		}
		drawBase(t);
		drawHead(t, f);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		drawPistonArm(t, f);
		
		
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
	
	void drawWheel(float centerX, float centerY, float centerZ){
		float radius = 0.05F;
		
		float textureWidth = 74F / 256F / 2;
		float textureXCenter = 153F / 256F + textureWidth;
		float textureYCenter = 0.5F + textureWidth;
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
	 
		for (int i=0; i < 360; i+=10){
			float degInRad = i*DEG2RAD;
			float x = (float) (centerX - 0.01);
			float y = (float) (centerY + Math.sin(degInRad)*radius);
			float z = (float) (centerZ + Math.cos(degInRad)*radius);
	      
			float textureX = (float) (textureXCenter + Math.sin(degInRad)*textureWidth);
			float textureY = (float) (textureYCenter + Math.cos(degInRad)*textureWidth);
			RenderHelper.vertexWithTexture(x, y, z, textureX, textureY);
		}
		GL11.glEnd();
	}
	

	private void drawBase(TileHarvesterTrolley tileentity){
		float width = 0.45F;
		float length = 0.45F;
		
		float beginCoord = 1.28F;
		float endCoord = length + beginCoord;
		float centerCoord = 0.5F;
		float beginCenter = centerCoord - width/2;
		float endCenter = 1F - beginCenter;
		GL11.glPushMatrix();
		//GL11.glDisable(GL11.GL_TEXTURE_2D);
		drawWheel(beginCenter, beginCoord+0.055F, beginCenter+0.045F);
		drawWheel(beginCenter, beginCoord+0.055F, endCenter-0.045F);
		GL11.glRotatef(180F, 0F, 1F, 0F);
		GL11.glTranslatef(-1.0F, 0.0F, -1.0F);
		drawWheel(beginCenter, beginCoord+0.055F, beginCenter+0.045F);
		drawWheel(beginCenter, beginCoord+0.055F, endCenter-0.045F);
		GL11.glPopMatrix();
		//GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		//Draw TOP side.
		GL11.glBegin(GL11.GL_QUADS);
		RenderHelper.vertexWithTexture(beginCenter, endCoord, endCenter, 0.5F, 0.5F); //BR
		RenderHelper.vertexWithTexture(endCenter, endCoord, endCenter, 0.5F, 0.0F); //TR
		RenderHelper.vertexWithTexture(endCenter, endCoord, beginCenter, 0.0F, 0.0F); //TL
		RenderHelper.vertexWithTexture(beginCenter, endCoord, beginCenter, 0.0F, 0.5F); //BL
		
		//Draw bottom side
		RenderHelper.vertexWithTexture(endCenter, beginCoord, endCenter, 1.0F, 0.5F); //TR
		RenderHelper.vertexWithTexture(beginCenter, beginCoord, endCenter, 1.0F, 0.0F);  //BR
		RenderHelper.vertexWithTexture(beginCenter, beginCoord, beginCenter, 0.5F, 0.0F); //TL
		RenderHelper.vertexWithTexture(endCenter, beginCoord, beginCenter, 0.5F, 0.5F); //BL
		
		
		//Draw back side.
		RenderHelper.vertexWithTexture(beginCenter, beginCoord, endCenter, 0.5F, 0.5F); //Bottom right corner
		RenderHelper.vertexWithTexture(beginCenter, endCoord, endCenter, 0.5F, 0.0F);  //Top right corner
		RenderHelper.vertexWithTexture(beginCenter, endCoord, beginCenter, 0.0F, 0.0F);  //Top left corner
		RenderHelper.vertexWithTexture(beginCenter, beginCoord, beginCenter, 0.0F, 0.5F); //Bottom left corner.
		
		//Draw front side:
		RenderHelper.vertexWithTexture(endCenter, beginCoord, beginCenter, 0.5F, 0.5F); //BR
		RenderHelper.vertexWithTexture(endCenter, endCoord, beginCenter, 0.5F, 0.0F); //TR
		RenderHelper.vertexWithTexture(endCenter, endCoord, endCenter, 0.0F, 0.0F); //TL
		RenderHelper.vertexWithTexture(endCenter, beginCoord, endCenter, 0.0F, 0.5F); //BL
		
		//Draw right side:
		RenderHelper.vertexWithTexture(beginCenter, beginCoord, beginCenter, 0.5F, 0.5F); //BR
		RenderHelper.vertexWithTexture(beginCenter, endCoord, beginCenter, 0.5F, 0.0F);  //TR
		RenderHelper.vertexWithTexture(endCenter, endCoord, beginCenter, 0.0F, 0.0F);  //TL
		RenderHelper.vertexWithTexture(endCenter, beginCoord, beginCenter, 0.0F, 0.5F); //BL
		
		//Draw left side:
		RenderHelper.vertexWithTexture(beginCenter, beginCoord, endCenter, 0.5F, 0.5F); //BL
		RenderHelper.vertexWithTexture(endCenter, beginCoord, endCenter, 0.5F, 0.0F); //BR
		RenderHelper.vertexWithTexture(endCenter, endCoord, endCenter, 0.0F, 0.0F); //TR
		RenderHelper.vertexWithTexture(beginCenter, endCoord, endCenter, 0.0F, 0.5F); //TL
		GL11.glEnd();
	}
	
	private void drawHead(TileHarvesterTrolley tileentity, float f){
		GL11.glPushMatrix();
		float fromEdge = 0.1F;
		float sideTexture = 25.0F / 256.0F;
		float otherEdge = 1.0F - fromEdge;
		if(tileentity != null){
		    float extendedLength = tileentity.getOldExtendedLength() + (tileentity.getExtendedLength() - tileentity.getOldExtendedLength()) * f;
			GL11.glTranslatef(0.0F, -extendedLength, 0.0F);
		}
		//Facing east for TOP and BOTTOM
		//Draw TOP side.
		GL11.glBegin(GL11.GL_QUADS);
		RenderHelper.vertexWithTexture(fromEdge, endTop, otherEdge, 0.5F, 0.5F); //BR
		RenderHelper.vertexWithTexture(otherEdge, endTop, otherEdge, 0.5F, 0.0F); //TR
		RenderHelper.vertexWithTexture(otherEdge, endTop, fromEdge, 0.0F, 0.0F); //TL
		RenderHelper.vertexWithTexture(fromEdge, endTop, fromEdge, 0.0F, 0.5F); //BL
		
		//Draw bottom side.
		RenderHelper.vertexWithTexture(otherEdge, beginTopX, otherEdge, 0.5F+sideTexture, 1.0F); //TR
		RenderHelper.vertexWithTexture(fromEdge, beginTopX, otherEdge, 0.5F+sideTexture, 0.5F);  //BR
		RenderHelper.vertexWithTexture(fromEdge, beginTopX, fromEdge, 0.0F+sideTexture, 0.5F); //TL
		RenderHelper.vertexWithTexture(otherEdge, beginTopX, fromEdge, 0.0F+sideTexture, 1.0F); //BL
		
		//Draw back side:
		RenderHelper.vertexWithTexture(fromEdge, beginTopX, otherEdge, 0.0F, 1.0F); //BR
		RenderHelper.vertexWithTexture(fromEdge, endTop, otherEdge, sideTexture, 1.0F); //TR
		RenderHelper.vertexWithTexture(fromEdge, endTop, fromEdge, sideTexture, 0.5F); //TL
		RenderHelper.vertexWithTexture(fromEdge, beginTopX, fromEdge, 0.0F, 0.5F); //BL
	
		
		//Draw front side:
		RenderHelper.vertexWithTexture(otherEdge, beginTopX, fromEdge, 0.0F, 1.0F);
		RenderHelper.vertexWithTexture(otherEdge, endTop, fromEdge, sideTexture, 1.0F);
		RenderHelper.vertexWithTexture(otherEdge, endTop, otherEdge, sideTexture, 0.5F);
		RenderHelper.vertexWithTexture(otherEdge, beginTopX, otherEdge, 0.0F, 0.5F);
		
		//Draw right side:
		RenderHelper.vertexWithTexture(fromEdge, beginTopX, fromEdge, 0.0F, 1.0F); 
		RenderHelper.vertexWithTexture(fromEdge, endTop, fromEdge, sideTexture, 1.0F); 
		RenderHelper.vertexWithTexture(otherEdge, endTop, fromEdge, sideTexture, 0.5F); 
		RenderHelper.vertexWithTexture(otherEdge, beginTopX, fromEdge, 0.0F, 0.5F);
		
		
		//Draw left side:
		RenderHelper.vertexWithTexture(fromEdge, beginTopX, otherEdge, 0.0F, 0.5F); //BL
		RenderHelper.vertexWithTexture(otherEdge, beginTopX, otherEdge, 0.0F, 1.0F); //BR
		RenderHelper.vertexWithTexture(otherEdge, endTop, otherEdge, sideTexture, 1.0F); //TR
		RenderHelper.vertexWithTexture(fromEdge, endTop, otherEdge, sideTexture, 0.5F); //TL
		GL11.glEnd();
		
		if(tileentity != null){
    		List<ItemStack> renderedItems = tileentity.getRenderedItems();
    		for(int i = 0; i < renderedItems.size(); i++){
                 float scaleFactor = 0.7F;
                 renderedItem.setEntityItemStack(renderedItems.get(i));
                 GL11.glPushMatrix();
                 GL11.glTranslated(0.5, 0.9, 0.5+i * 0.1);
                 GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);
                 GL11.glScalef(1.0F, -1F, -1F);
    
                 boolean fancySetting = RenderManager.instance.options.fancyGraphics;
                 RenderManager.instance.options.fancyGraphics = true;
                 customRenderItem.doRender(renderedItem, 0, 0, 0, 0, 0);
                 RenderManager.instance.options.fancyGraphics = fancySetting;
                 
                 GL11.glPopMatrix();
            }
    		FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);
		}
		GL11.glPopMatrix();
	}
	
	private void drawPistonArm(TileHarvesterTrolley tileentity, float f) {
		float half = 0.8F;
		float begin = half;
		float totalLength = 0f;
		float maxLength = 1f;
		if(tileentity != null){
			//totalLength = tileentity.getExtendedLength();
			totalLength = tileentity.getOldExtendedLength() + (tileentity.getExtendedLength() - tileentity.getOldExtendedLength()) * f;
			maxLength = tileentity.getMaxLength();
		}
		totalLength+=0.3F;
		
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
		float armBeginCoord = 0.5F - thickness / 2;
		float armEndCoord = 0.5F + thickness / 2;
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
