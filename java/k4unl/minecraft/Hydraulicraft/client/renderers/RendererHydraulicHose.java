package k4unl.minecraft.Hydraulicraft.client.renderers;

import java.util.HashMap;
import java.util.Map;

import k4unl.minecraft.Hydraulicraft.TileEntities.transporter.TilePressureHose;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Vector3fMax;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class RendererHydraulicHose extends TileEntitySpecialRenderer {

	private static final ResourceLocation resLoc[] = {
		new ResourceLocation(ModInfo.LID,"textures/model/hydraulicHose_tmap_0.png"),
		new ResourceLocation(ModInfo.LID,"textures/model/hydraulicHose_tmap_1.png"),
		new ResourceLocation(ModInfo.LID,"textures/model/hydraulicHose_tmap_2.png")
	};
	
	public void doRender(double x, double y, double z, float f, int tier, Map<ForgeDirection, TileEntity> connectedSides){
		GL11.glPushMatrix();
		
		GL11.glTranslatef((float) x, (float) y, (float)z);
		
		
		if(connectedSides == null){
			connectedSides = new HashMap<ForgeDirection, TileEntity>();
			//for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
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
			double z, float f) {
		
		if(!(tileentity instanceof TilePressureHose)) return;
		
		TilePressureHose mp = (TilePressureHose)tileentity;
		//if(Multipart.hasPartHose(mp)){
		//	PartHose tp = Multipart.getHose(mp);
		doRender(x, y, z, f, mp.getTier(), mp.getConnectedSides());
		//}
	}
	
	
	private void drawFirstCable(Map<ForgeDirection, TileEntity> connectedSides){
		float center = 0.5F;
		float offset = 0.1F;
		drawCable(connectedSides, center-offset);
		drawCable(connectedSides, center+offset);
	}
	
	
	private void drawCable(Map<ForgeDirection, TileEntity> connectedSides, float center){
		float width = 0.2F;
		float min = center - (width / 2);
		float max = center + (width / 2);
		Vector3fMax TBV = new Vector3fMax(min, min, min, max, max, max);
		
		if(connectedSides.containsKey(ForgeDirection.UP)){
			drawCube(new Vector3fMax(min, max, min, max, 1.0F, max), ForgeDirection.UP);
		}
		
		if(connectedSides.containsKey(ForgeDirection.DOWN)){
			drawCube(new Vector3fMax(min, 0.0F, min, max, min, max), ForgeDirection.DOWN);
		}
		
		if(connectedSides.containsKey(ForgeDirection.NORTH)){
			drawCube(new Vector3fMax(min, min, 0.0F, max, max, min), ForgeDirection.NORTH);
		}
		
		if(connectedSides.containsKey(ForgeDirection.SOUTH)){
			drawCube(new Vector3fMax(min, min, max, max, max, 1.0F), ForgeDirection.SOUTH);
		}
		
		if(connectedSides.containsKey(ForgeDirection.WEST)){
			drawCube(new Vector3fMax(0.0F, min, min, min, max, max), ForgeDirection.WEST);
		}
		
		if(connectedSides.containsKey(ForgeDirection.EAST)){
			drawCube(new Vector3fMax(max, min, min, 1.0F, max, max), ForgeDirection.EAST);
		}
		
		boolean upAndDown = (connectedSides.containsKey(ForgeDirection.UP) && connectedSides.containsKey(ForgeDirection.DOWN)); 
		boolean northAndSouth = (connectedSides.containsKey(ForgeDirection.NORTH) && connectedSides.containsKey(ForgeDirection.SOUTH));
		boolean eastAndWest = (connectedSides.containsKey(ForgeDirection.EAST) && connectedSides.containsKey(ForgeDirection.WEST));
		
		boolean upOrDown = (connectedSides.containsKey(ForgeDirection.UP) || connectedSides.containsKey(ForgeDirection.DOWN)); 
		boolean northOrSouth = (connectedSides.containsKey(ForgeDirection.NORTH) || connectedSides.containsKey(ForgeDirection.SOUTH));
		boolean eastOrWest = (connectedSides.containsKey(ForgeDirection.EAST) || connectedSides.containsKey(ForgeDirection.WEST));
		
		
		boolean corner = (upOrDown && (northOrSouth || eastOrWest)) || (northOrSouth && eastOrWest); 
		boolean end = (!upAndDown && !northAndSouth && !eastAndWest);
		if(corner || end){
			drawCorner(new Vector3fMax(min, min, min, max, max, max));
		}else{
			if(upAndDown){
				drawCube(new Vector3fMax(min, min, min, max, max, max), ForgeDirection.DOWN);
			}else if(northAndSouth){
				drawCube(new Vector3fMax(min, min, min, max, max, max), ForgeDirection.NORTH);
			}else if(eastAndWest){
				drawCube(new Vector3fMax(min, min, min, max, max, max), ForgeDirection.EAST);
			}
		}
	}
	
	private void drawCube(Vector3fMax vector, ForgeDirection dirToDraw){
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
		
		if(dirToDraw.equals(ForgeDirection.SOUTH) || dirToDraw.equals(ForgeDirection.NORTH)){
			txl = th;
			txh = 0.0F;
			tyl = 0.5F;
			tyh = 0.0F;
		}
		if(dirToDraw.equals(ForgeDirection.UP) || dirToDraw.equals(ForgeDirection.DOWN)){
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
