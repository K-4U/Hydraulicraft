package k4unl.minecraft.Hydraulicraft.client.renderers.consumers.harvester;



import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHydraulicHarvester;
import k4unl.minecraft.Hydraulicraft.client.renderers.RenderHelper;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class RendererHarvesterSource extends TileEntitySpecialRenderer {

	private static final ResourceLocation resLoc =
			new ResourceLocation(ModInfo.LID,"textures/model/hydraulicHarvester_tmap.png");
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y,
			double z, float f) {
		int metadata = tileentity.getBlockMetadata();
		doRender((TileHydraulicHarvester) tileentity, x, y, z, f, metadata);
	}
	
	public void doRender(TileHydraulicHarvester t , double x, double y,
			double z, float f, int metadata){
		GL11.glPushMatrix();
		
		GL11.glTranslatef((float) x, (float) y, (float)z);
		
		//Get metadata for rotation:
		/*
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
		}*/
		
		FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);
		
		GL11.glColor3f(0.8F, 0.8F, 0.8F);
		GL11.glPushMatrix();
		
		//GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
		GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
		//Do rendering
		renderBlock(t);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		
	}
	
	public void renderBlock(TileHydraulicHarvester h){
		//Draw TOP side.
		GL11.glBegin(GL11.GL_QUADS);
		RenderHelper.vertexWithTexture(0.0F, 1.0F, 1.0F, 0F, 0F);
		RenderHelper.vertexWithTexture(1.0F, 1.0F, 1.0F, 0.5F, 0F);		
		RenderHelper.vertexWithTexture(1.0F, 1.0F, 0.0F, 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(0.0F, 1.0F, 0.0F, 0.0F, 0.5F);
		
		//Draw bottom side.
		RenderHelper.vertexWithTexture(1.0F, 0.0F, 1.0F, 0.0F, 0F); // TR
		RenderHelper.vertexWithTexture(0.0F, 0.0F, 1.0F, 0.5F, 0F);	 //BR
		RenderHelper.vertexWithTexture(0.0F, 0.0F, 0.0F, 0.5F, 0.5F); //TL
		RenderHelper.vertexWithTexture(1.0F, 0.0F, 0.0F, 0.0F, 0.5F); //BL
		

		//Draw back side:
		RenderHelper.vertexWithTexture(0.0F, 0.0F, 1.0F, 0.0F, 0.0F); // BR
		RenderHelper.vertexWithTexture(0.0F, 1.0F, 1.0F, 0.5F, 0.0F); //TR
		RenderHelper.vertexWithTexture(0.0F, 1.0F, 0.0F, 0.5F, 0.5F); //TL
		RenderHelper.vertexWithTexture(0.0F, 0.0F, 0.0F, 0.0F, 0.5F); //BL

		//Draw front side:
		
		RenderHelper.vertexWithTexture(1.0F, 0.0F, 0.0F, 1.0F, 0.5F); //BR
		RenderHelper.vertexWithTexture(1.0F, 1.0F, 0.0F, 1.0F, 0.0F); //TR
		RenderHelper.vertexWithTexture(1.0F, 1.0F, 1.0F, 0.5F, 0.0F); //TL
		RenderHelper.vertexWithTexture(1.0F, 0.0F, 1.0F, 0.5F, 0.5F); //BL
		
		if(h != null){
			float fPercentage = (float)h.getHandler().getStored() / (float)h.getMaxStorage();
			float pPercentage = h.getPressure(ForgeDirection.UNKNOWN) / h.getMaxPressure(h.getHandler().isOilStored(), null);
			float maxHeight = 0.452F;
			float beginH = 0.445F;
			float width = 0.14F;
			float fBeginW = 0.68F;
			float pBeginW = 0.18F;
			
			float bY = beginH + maxHeight;
			float fTY = bY - (maxHeight * fPercentage);
			float pTY = bY - (maxHeight * pPercentage);
			float fLX = fBeginW;
			float fRX = fBeginW + width;
			float pLX = pBeginW;
			float pRX = pBeginW + width;
			int color;
			if(h.getHandler().isOilStored()){
				color = Constants.COLOR_OIL;
			}else{
				color = Constants.COLOR_WATER;
			}
			float a = (float)((color >> 24) & 0xFF)/255;
			float r = (float)((color >> 16) & 0xFF)/255;
			float g = (float)((color >> 8) & 0xFF)/255;
			float b = (float)((color >> 0) & 0xFF)/255;
			GL11.glEnd();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glBegin(GL11.GL_QUADS);
		
		
			GL11.glColor4f(r, g, b, a);
			GL11.glVertex3f(1.001F, bY, fRX);
			GL11.glVertex3f(1.001F, fTY, fRX);
			GL11.glVertex3f(1.001F, fTY, fLX);
			GL11.glVertex3f(1.001F, bY, fLX); //TR
			
			color = Constants.COLOR_PRESSURE;
			a = (float)((color >> 24) & 0xFF)/255;
			r = (float)((color >> 16) & 0xFF)/255;
			g = (float)((color >> 8) & 0xFF)/255;
			b = (float)((color >> 0) & 0xFF)/255;
	
			GL11.glColor4f(r, g, b, a);
			GL11.glVertex3f(1.001F, bY, pRX);
			GL11.glVertex3f(1.001F, pTY, pRX);
			GL11.glVertex3f(1.001F, pTY, pLX);
			GL11.glVertex3f(1.001F, bY, pLX); //TR
			
			GL11.glEnd();
			
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glBegin(GL11.GL_QUADS);
		}
		
		GL11.glColor3f(1, 1, 1);
		//Draw right side:
		RenderHelper.vertexWithTexture(0.0F, 0.0F, 0.0F, 0.0F, 0.0F); // BR
		RenderHelper.vertexWithTexture(0.0F, 1.0F, 0.0F, 0.5F, 0.0F);	 //TR
		RenderHelper.vertexWithTexture(1.0F, 1.0F, 0.0F, 0.5F, 0.5F); //TL
		RenderHelper.vertexWithTexture(1.0F, 0.0F, 0.0F, 0.0F, 0.5F); //BL

		//Draw left side:
		RenderHelper.vertexWithTexture(0.0F, 0.0F, 1.0F, 0.0F, 0.0F); // BR
		RenderHelper.vertexWithTexture(1.0F, 0.0F, 1.0F, 0.5F, 0.0F);	 //TR
		RenderHelper.vertexWithTexture(1.0F, 1.0F, 1.0F, 0.5F, 0.5F); //TL
		RenderHelper.vertexWithTexture(0.0F, 1.0F, 1.0F, 0.0F, 0.5F); //BL
		
		GL11.glEnd();
	}

}
