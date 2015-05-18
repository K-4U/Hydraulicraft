package k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.client.renderers;

import cpw.mods.fml.client.FMLClientHandler;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities.TileElectricPump;
import k4unl.minecraft.k4lib.client.RenderHelper;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import org.lwjgl.opengl.GL11;

public class RendererElectricPump extends TileEntitySpecialRenderer {

	private static final ResourceLocation resLoc =
			new ResourceLocation(ModInfo.LID,"textures/model/electricPump.png");
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y,
			double z, float f) {
		
		TileElectricPump t = (TileElectricPump)tileentity;
		//Get metadata for rotation:
		int rotation = 0;//t.getDir();
		int metadata = t.getBlockMetadata();
		
		doRender(t, (float)x, (float)y, (float)z, f, rotation, metadata);
	}
	
	public void itemRender(float x, float y,
			float z, int tier){
		GL11.glPushMatrix();
		GL11.glPushMatrix();
		
		GL11.glTranslatef(x, y, z);

		FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);
		
		GL11.glPushMatrix();
		//GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
		GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
		//Do rendering
		GL11.glColor3f(0.8F, 0.8F, 0.8F);
		drawBase(tier);
		drawElectricConnector();
		drawHydraulicsConnector();
		drawElectricBlock(null, true);
		drawHydraulicsTank(null, true);
		
		
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
	
	public void doRender(TileElectricPump t, float x, float y,
			float z, float f, int rotation, int metadata){
		GL11.glPushMatrix();
		GL11.glPushMatrix();
		
		GL11.glTranslatef(x, y, z);

		switch(t.getFacing()){
			case EAST:
				GL11.glTranslatef(1.0F, 0.0F, 0.0F);
				GL11.glRotatef(90F, 0.0F, -1.0F, 0.0F);
				break;
			case UP:
				break;
			case WEST:
				GL11.glTranslatef(0.0F, 0.0F, 1.0F);
				GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
				break;
			case DOWN:
				//GL11.glTranslatef(0.0F, 1.0F, 1.0F);
				//GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
				break;
			case SOUTH:
				GL11.glTranslatef(1.0F, 0.0F, 1.0F);
				GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
				break;
			case NORTH:
			case UNKNOWN:
			default:
				break;
		}
		
		
		FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);
		
		GL11.glPushMatrix();
		//GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
		GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
		//Do rendering
		GL11.glColor3f(0.8F, 0.8F, 0.8F);
		
		drawBase(t.getTier());
		drawElectricConnector();
		drawHydraulicsConnector();
		drawElectricBlock(t, false);
		drawHydraulicsTank(t, false);
		
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
	
	private void drawBase(int tier){
		GL11.glBegin(GL11.GL_QUADS);
		float tXb[] = {128.0F/256.0F, 141.0F/256.0F, 154.0F/256.0F};
		float tXe[] = {141.0F/256.0F, 154.0F/256.0F, 167.0F/256.0F};
		//Base
		Vector3fMax vector = new Vector3fMax(0.0F, 0.0F, 0.0F, 1.0F, 0.1F, 1.0F);
		//RenderHelper.drawColoredCube(new Vector3fMax(0.0F, 0.0F, 0.0F, 1.0F, 0.1F, 1.0F));
		
		//Top side:
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.0F, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.5F, 0.0F);		
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.0F, 0.5F);
		
		//Bottom side:
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.0F, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.5F, 0.0F);		
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.0F, 0.5F);

		//Draw west side:
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), tXe[tier], 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), tXb[tier], 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), tXb[tier], 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), tXe[tier], 0.0F);
		
		//Draw east side:
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), tXe[tier], 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), tXb[tier], 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), tXb[tier], 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), tXe[tier], 0.0F);
		
		//Draw north side
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), tXe[tier], 0.5F); 
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), tXb[tier], 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), tXb[tier], 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), tXe[tier], 0.0F);

		//Draw south side
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), tXb[tier], 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), tXe[tier], 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), tXb[tier], 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), tXe[tier], 0.0F);
		GL11.glEnd();
	}
	
	private void drawElectricConnector(){
		GL11.glBegin(GL11.GL_QUADS);
		Vector3fMax vector = new Vector3fMax(0.2F, 0.1F, 0.8F, 0.8F, 0.8F, 1.0F);
		//RenderHelper.drawTexturedCube(new Vector3fMax(0.2F, 0.1F, 0.8F, 0.8F, 0.8F, 1.0F));
		float topXb = 179.0F/256.0F;
		float topYe = 154.0F/256.0F;
		
		float sideXb = 102.0F/256.0F;
		float sideXe = 192.0F/256.0F;
		
		float sideYb = 218.0F/256.0F;
		float sideYe = 244.0F/256.0F;
		
		float endXb = sideXb;
		float endXe = topXb;
		
		float endYb = 0.5F;
		float endYe = sideYb;
		//Top side:
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 1.0F, 0.5F); //TR
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), topXb, 0.5F);	 //TL
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), topXb, topYe);	 //BL
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 1.0F, topYe); //BR
		
		//Draw west side:
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), sideXe, sideYe);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), sideXb, sideYe);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), sideXb, sideYb);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), sideXe, sideYb);
		
		//Draw east side:
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), sideXe, sideYe);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), sideXb, sideYe);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), sideXb, sideYb);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), sideXe, sideYb);
		
		//Draw north side
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), endXe, endYb); 
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), endXe, endYe);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), endXb, endYe);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), endXb, endYb);

		//Draw south side
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), endXe, endYb);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), endXb, endYb);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), endXb, endYe);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), endXe, endYe);
		GL11.glEnd();
	}
	
	private void drawHydraulicsConnector(){
		GL11.glBegin(GL11.GL_QUADS);
		//Connector for Hydraulics
		Vector3fMax vector = new Vector3fMax(0.25F, 0.1F, 0.0F, 0.75F, 0.75F, 0.2F);
		//RenderHelper.drawColoredCube(new Vector3fMax(0.25F, 0.1F, 0.0F, 0.75F, 0.75F, 0.2F));
		float topXb = 179.0F/256.0F;
		float topYe = 154.0F/256.0F;
		
		float sideXb = 102.0F/256.0F;
		float sideXe = 192.0F/256.0F;
		
		float sideYb = 218.0F/256.0F;
		float sideYe = 244.0F/256.0F;
		
		float endXb = sideXb;
		float endXe = topXb;
		
		float endYb = 0.5F;
		float endYe = sideYb;
		//Top side:
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 1.0F, 0.5F); //TR
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), topXb, 0.5F);	 //TL
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), topXb, topYe);	 //BL
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 1.0F, topYe); //BR
		
		//Draw west side:
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), sideXe, sideYe);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), sideXb, sideYe);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), sideXb, sideYb);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), sideXe, sideYb);
		
		//Draw east side:
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), sideXe, sideYe);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), sideXb, sideYe);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), sideXb, sideYb);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), sideXe, sideYb);
		
		//Draw north side
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), endXe, endYb); 
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), endXe, endYe);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), endXb, endYe);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), endXb, endYb);

		//Draw south side
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), endXe, endYb);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), endXb, endYb);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), endXb, endYe);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), endXe, endYe);
		GL11.glEnd();
	}
	
	
	private void drawElectricBlock(TileElectricPump t, boolean isItem){
		GL11.glPushMatrix();
		
		if(!isItem){
			if(t.getIsRunning()){
				GL11.glColor3f(1.0F, t.getRenderingPercentage(), t.getRenderingPercentage());
			}
		}
		
		float sideXb = 0.0F/256.0F;
		float sideXe = 100F/256.0F;
		//float sideYb = 192.0F/256.0F;
		float sideYe = 200.0F/256.0F;
		
		
		GL11.glTranslatef(0.0F, 0.0F, 0.5F);
		RenderHelper.draw2DCircle(0.5F, 0.5F, 0.15F);
		
		GL11.glTranslatef(0.0F, 1.0F, -0.5F);
		GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
		RenderHelper.drawCylinder(0.5F, 0.5F, 0.5F, 0.15F, 0.3F, sideXb, sideYe, sideXe, sideYe);
		
		GL11.glPopMatrix();
		
		if(!isItem){
			if(t.getIsRunning()){
				GL11.glColor3f(0.9f, 0.9f, 0.9f);
			}
		}
	}
	
	private void drawHydraulicsTank(TileElectricPump t, boolean isItem){
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		Vector3fMax vectorFilled = new Vector3fMax(0.301F, 0.301F, 0.201F, 0.699F, 0.699F, 0.499F);
		if(!isItem){
			float h = vectorFilled.getYMax() - vectorFilled.getYMin();
			vectorFilled.setYMax(vectorFilled.getYMin() + (h * ((float)t.getHandler().getStored() / (float)t.getMaxStorage())));
		
		
			IIcon fluidIcon;
			if(t.getHandler().isOilStored()){
				//RenderHelper.setARGBFromHex(Constants.COLOR_OIL + 0xFE000000);
				/*float a = 0.7F;
		        float r = (float)(Constants.COLOR_OIL >> 16 & 255) / 255.0F;
		        float g = (float)(Constants.COLOR_OIL >> 8 & 255) / 255.0F;
		        float b = (float)(Constants.COLOR_OIL & 255) / 255.0F;*/
		        fluidIcon = Fluids.fluidHydraulicOil.getIcon();
			}else{
				fluidIcon = FluidRegistry.WATER.getIcon();
			}
			
			if(t.getHandler().getStored() > 0){
				RenderHelper.drawTesselatedCubeWithTexture(vectorFilled, fluidIcon);
			}
			
			//Reset texture after using tesselators.
			FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);
		}
		
		
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(0.9F, 0.9F, 0.9F, 1.0F);
		//Block Hydraulics
		Vector3fMax vector = new Vector3fMax(0.3F, 0.3F, 0.2F, 0.7F, 0.7F, 0.5F);
		//RenderHelper.drawColoredCube(new Vector3fMax(0.3F, 0.3F, 0.2F, 0.7F, 0.7F, 0.5F));
		
		float sideXb = 64.0F/256.0F;
		float sideXe = 102.0F/256.0F;
		float sideYb = 0.5F;
		float sideYe = 192.0F/256.0F;
		//Top side:
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), sideXe, sideYb);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), sideXe, sideYe);		
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), sideXb, sideYe);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), sideXb, sideYb);
		
		//Bottom side:
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), sideXe, sideYb);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), sideXe, sideYe);		
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), sideXb, sideYe);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), sideXb, sideYb);

		//Draw west side:
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), sideXe, sideYb);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), sideXe, sideYe);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), sideXb, sideYe);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), sideXb, sideYb);
		
		//Draw east side:
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), sideXe, sideYb);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), sideXe, sideYe);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), sideXb, sideYe);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), sideXb, sideYb);
		
		//Draw north side
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), sideXb, sideYb); 
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), sideXb, sideYe);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.0F, sideYe);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.0F, sideYb);

		//Draw south side
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.0F, sideYb);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), sideXb, sideYb);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), sideXb, sideYe);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.0F, sideYe);
		
		GL11.glEnd();
		
		GL11.glDisable(GL11.GL_BLEND);
	}
	
}
