package k4unl.minecraft.Hydraulicraft.client.renderers.transportation;

import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.k4lib.client.RenderHelper;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

public class RendererPartHose extends TileEntitySpecialRenderer {

	private static float pixel = 1.0F / 16F;

	private static final ResourceLocation resLoc[] = {
		new ResourceLocation(ModInfo.LID,"textures/model/hydraulicHose_tmap_0.png"),
		new ResourceLocation(ModInfo.LID,"textures/model/hydraulicHose_tmap_1.png"),
		new ResourceLocation(ModInfo.LID,"textures/model/hydraulicHose_tmap_2.png")
	};
	
	public void doRender(double x, double y, double z, float f, int tier, boolean[] connectedSides){
		GL11.glPushMatrix();
		
		GL11.glTranslatef((float) x, (float) y, (float)z);
		
		
		if(connectedSides == null){
			connectedSides = new boolean[7];
			//for(EnumFacing dir : EnumFacing.VALID_DIRECTIONS){
			//	connectedSides.put(dir, null);
			//}
		}
		//Bind texture
		FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc[tier]);
		
		GL11.glColor3f(0.8F, 0.8F, 0.8F);
		GL11.glPushMatrix();
		
		//GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
		GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
		//Do rendering
		drawFirstCable(connectedSides);
		
		//GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y,
			double z, float f, int destroyStage) {
		
		//if(!(tileentity instanceof TilePressureHose)) return;
		
		/*TileMultipart mp = (TileMultipart)tileentity;
		if(Multipart.hasPartHose(mp)){
			PartHose tp = Multipart.getHose(mp);
			doRender(x, y, z, f, tp.getTier().toInt(), tp.getConnectedSides());
		}*/
	}
	
	
	private void drawFirstCable(boolean[] connectedSides){
		float center = 0.5F;
		float offset = RenderHelper.pixel*2;
		drawCable(connectedSides, center-offset);
		drawCable(connectedSides, center+offset);
	}
	
	
	private void drawCable(boolean[] connectedSides, float center){
		float width = RenderHelper.pixel*4;
		float min = center - (width / 2);
		float max = center + (width / 2);
		
		if(connectedSides[EnumFacing.UP.ordinal()]){
			drawCube(new Vector3fMax(min, max, min, max, 1.0F, max), EnumFacing.UP);
		}
		
		if(connectedSides[EnumFacing.DOWN.ordinal()]){
			drawCube(new Vector3fMax(min, 0.0F, min, max, min, max), EnumFacing.DOWN);
		}
		
		if(connectedSides[EnumFacing.NORTH.ordinal()]){
			drawCube(new Vector3fMax(min, min, 0.0F, max, max, min), EnumFacing.NORTH);
		}
		
		if(connectedSides[EnumFacing.SOUTH.ordinal()]){
			drawCube(new Vector3fMax(min, min, max, max, max, 1.0F), EnumFacing.SOUTH);
		}
		
		if(connectedSides[EnumFacing.WEST.ordinal()]){
			drawCube(new Vector3fMax(0.0F, min, min, min, max, max), EnumFacing.WEST);
		}
		
		if(connectedSides[EnumFacing.EAST.ordinal()]){
			drawCube(new Vector3fMax(max, min, min, 1.0F, max, max), EnumFacing.EAST);
		}
		
		boolean upAndDown = (connectedSides[EnumFacing.UP.ordinal()] && connectedSides[EnumFacing.DOWN.ordinal()]);
		boolean northAndSouth = (connectedSides[EnumFacing.NORTH.ordinal()] && connectedSides[EnumFacing.SOUTH.ordinal()]);
		boolean eastAndWest = (connectedSides[EnumFacing.EAST.ordinal()] && connectedSides[EnumFacing.WEST.ordinal()]);
		
		boolean upOrDown = (connectedSides[EnumFacing.UP.ordinal()] || connectedSides[EnumFacing.DOWN.ordinal()]);
		boolean northOrSouth = (connectedSides[EnumFacing.NORTH.ordinal()] || connectedSides[EnumFacing.SOUTH.ordinal()]);
		boolean eastOrWest = (connectedSides[EnumFacing.EAST.ordinal()] || connectedSides[EnumFacing.WEST.ordinal()]);
		
		
		boolean corner = (upOrDown && (northOrSouth || eastOrWest)) || (northOrSouth && eastOrWest); 
		boolean end = (!upAndDown && !northAndSouth && !eastAndWest);
		if(corner || end){
			drawCorner(new Vector3fMax(min, min, min, max, max, max));
		}else{
			if(upAndDown){
				drawCube(new Vector3fMax(min, min, min, max, max, max), EnumFacing.DOWN);
			}else if(northAndSouth){
				drawCube(new Vector3fMax(min, min, min, max, max, max), EnumFacing.NORTH);
			}else if(eastAndWest){
				drawCube(new Vector3fMax(min, min, min, max, max, max), EnumFacing.EAST);
			}
		}
	}
	
	private void drawCube(Vector3fMax vector, EnumFacing dirToDraw){
		GL11.glBegin(GL11.GL_QUADS);
		
		float th = 1.0F;
		
		float txl = 0.0F;
		float txh = th;
		float tyl = 0.0F;
		float tyh = 0.5F;
		
		float sxl = 0.0F;
		float sxh = th;
		float syl = 0.0F;
		float syh = 0.5F;
		
		if(dirToDraw.equals(EnumFacing.SOUTH) || dirToDraw.equals(EnumFacing.NORTH)){
			txl = th;
			txh = 0.0F;
			tyl = 0.5F;
			tyh = 0.0F;
		}
		if(dirToDraw.equals(EnumFacing.UP) || dirToDraw.equals(EnumFacing.DOWN)){
			sxl = th;
			sxh = 0.0F;
			syl = 0.5F;
			syh = 0.0F;
		}
		//Top side:
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), txl, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), th, tyl);		
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), txh, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.0F, tyh);
		
		//Bottom side:
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), txl, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), th, tyl);		
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), txh, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.0F, tyh);

		//Draw west side:
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), th, syl);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), sxh, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.0F, syh);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), sxl, 0.0F);
		
		//Draw east side:
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 1.0F, syl);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), sxh, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.0F, syh);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), sxl, 0.0F);
		
		//Draw north side
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 1.0F, syl); 
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), sxh, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.0F, syh);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), sxl, 0.0F);

		//Draw south side
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.0F, syl);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), sxh, 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 1.0F, syh);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), sxl, 0.5F);
		
		GL11.glEnd();
	}
	
	private void drawCorner(Vector3fMax vector){
		GL11.glBegin(GL11.GL_QUADS);

		//Top side:
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.0F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.5F, 0.5F);		
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.5F, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.0F, 1.0F);
		
		//Bottom side:
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.0F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.5F, 0.5F);		
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 0.5F, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.0F, 1.0F);

		//Draw west side:
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.5F, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.0F, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 0.0F, 0.5F);
		
		
		//Draw east side:
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.5F, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.0F, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.0F, 0.5F);
		
		//Draw north side
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 0.5F, 0.5F); 
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.5F, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.0F, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.0F, 0.5F);

		//Draw south side
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.0F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.5F, 0.5F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.5F, 1.0F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.0F, 1.0F);
		
		GL11.glEnd();
	}
}
