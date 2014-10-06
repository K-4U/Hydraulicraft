package k4unl.minecraft.Hydraulicraft.client.renderers.gow;

import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.tileEntities.gow.TilePortalFrame;
import k4unl.minecraft.k4lib.client.RenderHelper;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class RendererPortalFrame extends TileEntitySpecialRenderer {
	private static final ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/model/portalBase.png");
	
	
	@Override
	public void renderTileEntityAt(TileEntity ent, double x, double y,
			double z, float frame) {
		doRender((TilePortalFrame)ent, x, y, z, frame);

	}
	
	public void doRender(TilePortalFrame myFrame, double x, double y, double z, float frame){
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		
		//FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		
		renderFrame(myFrame);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		
	}
	
	private void renderFrame(TilePortalFrame frame){
		float colorFaceR = 255.0F;
		float colorFaceG = 255.0F;
		float colorFaceB = 255.0F;
		int color = frame.getDye();
		
		switch(color){
		case 0:
			colorFaceR = 221.0F;
			colorFaceG = 221.0F;
			colorFaceB = 221.0F;
			break;
		case 1:
			colorFaceR = 219.0F;
			colorFaceG = 125.0F;
			colorFaceB = 62.0F;
			break;
		case 2:
			colorFaceR = 179.0F;
			colorFaceG = 80.0F;
			colorFaceB = 188.0F;
			break;
		case 3:
			colorFaceR = 107.0F;
			colorFaceG = 138.0F;
			colorFaceB = 1201.0F;
			break;
		case 4:
			colorFaceR = 177.0F;
			colorFaceG = 166.0F;
			colorFaceB = 39.0F;
			break;
		case 5:
			colorFaceR = 65.0F;
			colorFaceG = 174.0F;
			colorFaceB = 56.0F;
			break;
		case 6:
			colorFaceR = 208.0F;
			colorFaceG = 132.0F;
			colorFaceB = 153.0F;
			break;
		case 7:
			colorFaceR = 64.0F;
			colorFaceG = 64.0F;
			colorFaceB = 64.0F;
			break;
		case 8:
			colorFaceR = 154.0F;
			colorFaceG = 161.0F;
			colorFaceB = 161.0F;
			break;
		case 9:
			colorFaceR = 46.0F;
			colorFaceG = 110.0F;
			colorFaceB = 137.0F;
			break;
		case 10:
			colorFaceR = 126.0F;
			colorFaceG = 61.0F;
			colorFaceB = 181.0F;
			break;
		case 11:
			colorFaceR = 46.0F;
			colorFaceG = 56.0F;
			colorFaceB = 141.0F;
			break;
		case 12:
			colorFaceR = 79.0F;
			colorFaceG = 50.0F;
			colorFaceB = 31.0F;
			break;
		case 13:
			colorFaceR = 53.0F;
			colorFaceG = 70.0F;
			colorFaceB = 27.0F;
			break;
		case 14:
			colorFaceR = 150.0F;
			colorFaceG = 52.0F;
			colorFaceB = 48.0F;
			break;
		case 15:
			colorFaceR = 25.0F;
			colorFaceG = 22.0F;
			colorFaceB = 22.0F;
			default:
				break;
		}
		colorFaceR = colorFaceR / 255.0F;
		colorFaceG = colorFaceG / 255.0F;
		colorFaceB = colorFaceB / 255.0F;
		
		float colorFrameR = 1.0F;
		float colorFrameG = 0.0F;
		float colorFrameB = 0.0F;
		if(frame.getIsActive()){
			colorFrameR = 0.0F;
			colorFrameG = 1.0F;
		}
		
		
		
		float minPP = RenderHelper.pixel * 6;
		float minNP = RenderHelper.pixel * 5;
		float maxPP = RenderHelper.pixel * (16-6);
		float maxNP = RenderHelper.pixel * (16-5);
		GL11.glBegin(GL11.GL_QUADS);
		//RenderHelper.drawColoredCube(vector);
		
		boolean isN = false;
		boolean isS = false;
		boolean isU = false;
		boolean isD = false;
		boolean isE = false;
		boolean isW = false;
		//Yuck
		GL11.glColor3f(colorFaceR, colorFaceG, colorFaceB);
		if(frame.isConnectedTo(ForgeDirection.NORTH)){
			Vector3fMax vTB = new Vector3fMax(minPP, minNP, 0.0F, maxPP, maxNP, minNP);
			Vector3fMax vEW = new Vector3fMax(minNP, minPP, 0.0F, maxNP, maxPP, minNP);
			
			RenderHelper.renderSide(vTB, ForgeDirection.UP);
			RenderHelper.renderSide(vTB, ForgeDirection.DOWN);
			RenderHelper.renderSide(vEW, ForgeDirection.EAST);
			RenderHelper.renderSide(vEW, ForgeDirection.WEST);
			
			GL11.glColor3f(colorFrameR, colorFrameG, colorFrameB);
			Vector3fMax vTBW = new Vector3fMax(minNP, minNP, 0.0F, minPP, maxNP, minNP);
			Vector3fMax vTBE = new Vector3fMax(maxPP, minNP, 0.0F, maxNP, maxNP, minNP);
			Vector3fMax vEWB = new Vector3fMax(minNP, minNP, 0.0F, maxNP, minPP, minNP);
			Vector3fMax vEWT = new Vector3fMax(minNP, maxPP, 0.0F, maxNP, maxNP, minNP);
			
			RenderHelper.renderSide(vTBW, ForgeDirection.UP);
			RenderHelper.renderSide(vTBW, ForgeDirection.DOWN);
			RenderHelper.renderSide(vTBE, ForgeDirection.UP);
			RenderHelper.renderSide(vTBE, ForgeDirection.DOWN);
			
			RenderHelper.renderSide(vEWT, ForgeDirection.EAST);
			RenderHelper.renderSide(vEWT, ForgeDirection.WEST);
			RenderHelper.renderSide(vEWB, ForgeDirection.EAST);
			RenderHelper.renderSide(vEWB, ForgeDirection.WEST);
			isN = true;
		}
		if(frame.isConnectedTo(ForgeDirection.SOUTH)){
			GL11.glColor3f(colorFaceR, colorFaceG, colorFaceB);
			Vector3fMax vTB = new Vector3fMax(minPP, minNP, maxNP, maxPP, maxNP, 1.0F);
			Vector3fMax vEW = new Vector3fMax(minNP, minPP, maxNP, maxNP, maxPP, 1.0F);
			RenderHelper.renderSide(vTB, ForgeDirection.UP);
			RenderHelper.renderSide(vTB, ForgeDirection.DOWN);
			RenderHelper.renderSide(vEW, ForgeDirection.EAST);
			RenderHelper.renderSide(vEW, ForgeDirection.WEST);
			
			GL11.glColor3f(colorFrameR, colorFrameG, colorFrameB);
			Vector3fMax vTBW = new Vector3fMax(minNP, minNP, maxNP, minPP, maxNP, 1.0F);
			Vector3fMax vTBE = new Vector3fMax(maxPP, minNP, maxNP, maxNP, maxNP, 1.0F);
			Vector3fMax vEWB = new Vector3fMax(minNP, minNP, maxNP, maxNP, minPP, 1.0F);
			Vector3fMax vEWT = new Vector3fMax(minNP, maxPP, maxNP, maxNP, maxNP, 1.0F);
			
			RenderHelper.renderSide(vTBW, ForgeDirection.UP);
			RenderHelper.renderSide(vTBW, ForgeDirection.DOWN);
			RenderHelper.renderSide(vTBE, ForgeDirection.UP);
			RenderHelper.renderSide(vTBE, ForgeDirection.DOWN);
			
			RenderHelper.renderSide(vEWT, ForgeDirection.EAST);
			RenderHelper.renderSide(vEWT, ForgeDirection.WEST);
			RenderHelper.renderSide(vEWB, ForgeDirection.EAST);
			RenderHelper.renderSide(vEWB, ForgeDirection.WEST);
			isS = true;
		}
		
		if(frame.isConnectedTo(ForgeDirection.EAST)){
			Vector3fMax vTB = new Vector3fMax(maxNP, minNP, minPP, 1.0F, maxNP, maxPP);
			Vector3fMax vNS = new Vector3fMax(maxNP, minPP, minNP, 1.0F, maxPP, maxNP);
			GL11.glColor3f(colorFaceR, colorFaceG, colorFaceB);
			RenderHelper.renderSide(vTB, ForgeDirection.UP);
			RenderHelper.renderSide(vTB, ForgeDirection.DOWN);
			
			RenderHelper.renderSide(vNS, ForgeDirection.NORTH);
			RenderHelper.renderSide(vNS, ForgeDirection.SOUTH);
			
			GL11.glColor3f(colorFrameR, colorFrameG, colorFrameB);
			Vector3fMax vTBN = new Vector3fMax(maxNP, minNP, minNP, 1.0F, maxNP, minPP);
			Vector3fMax vTBS = new Vector3fMax(maxNP, minNP, maxPP, 1.0F, maxNP, maxNP);
			
			Vector3fMax vNST = new Vector3fMax(maxNP, maxPP, minNP, 1.0F, maxNP, maxNP);
			Vector3fMax vNSB = new Vector3fMax(maxNP, minNP, minNP, 1.0F, minPP, maxNP);
			RenderHelper.renderSide(vTBN, ForgeDirection.UP);
			RenderHelper.renderSide(vTBN, ForgeDirection.DOWN);
			RenderHelper.renderSide(vTBS, ForgeDirection.UP);
			RenderHelper.renderSide(vTBS, ForgeDirection.DOWN);
			
			RenderHelper.renderSide(vNST, ForgeDirection.NORTH);
			RenderHelper.renderSide(vNST, ForgeDirection.SOUTH);
			RenderHelper.renderSide(vNSB, ForgeDirection.NORTH);
			RenderHelper.renderSide(vNSB, ForgeDirection.SOUTH);
			isE = true;
		}
		if(frame.isConnectedTo(ForgeDirection.WEST)){
			Vector3fMax vTB = new Vector3fMax(0.0F, minNP, minPP, minNP, maxNP, maxPP);
			Vector3fMax vNS = new Vector3fMax(0.0F, minPP, minNP, minNP, maxPP, maxNP);
			GL11.glColor3f(colorFaceR, colorFaceG, colorFaceB);
			RenderHelper.renderSide(vTB, ForgeDirection.UP);
			RenderHelper.renderSide(vTB, ForgeDirection.DOWN);
			
			RenderHelper.renderSide(vNS, ForgeDirection.NORTH);
			RenderHelper.renderSide(vNS, ForgeDirection.SOUTH);
			
			GL11.glColor3f(colorFrameR, colorFrameG, colorFrameB);
			Vector3fMax vTBN = new Vector3fMax(0.0F, minNP, minNP, minNP, maxNP, minPP);
			Vector3fMax vTBS = new Vector3fMax(0.0F, minNP, maxPP, minNP, maxNP, maxNP);
			
			Vector3fMax vNST = new Vector3fMax(0.0F, maxPP, minNP, minNP, maxNP, maxNP);
			Vector3fMax vNSB = new Vector3fMax(0.0F, minNP, minNP, minNP, minPP, maxNP);
			RenderHelper.renderSide(vTBN, ForgeDirection.UP);
			RenderHelper.renderSide(vTBN, ForgeDirection.DOWN);
			RenderHelper.renderSide(vTBS, ForgeDirection.UP);
			RenderHelper.renderSide(vTBS, ForgeDirection.DOWN);
			
			RenderHelper.renderSide(vNST, ForgeDirection.NORTH);
			RenderHelper.renderSide(vNST, ForgeDirection.SOUTH);
			RenderHelper.renderSide(vNSB, ForgeDirection.NORTH);
			RenderHelper.renderSide(vNSB, ForgeDirection.SOUTH);
			isW = true;
		}
		
		if(frame.isConnectedTo(ForgeDirection.UP)){
			Vector3fMax vNS = new Vector3fMax(minPP, maxNP, minNP, maxPP, 1.0F, maxNP);
			Vector3fMax vEW = new Vector3fMax(minNP, maxNP, minPP, maxNP, 1.0F, maxPP);
			GL11.glColor3f(colorFaceR, colorFaceG, colorFaceB);
			RenderHelper.renderSide(vNS, ForgeDirection.NORTH);
			RenderHelper.renderSide(vNS, ForgeDirection.SOUTH);
			
			RenderHelper.renderSide(vEW, ForgeDirection.EAST);
			RenderHelper.renderSide(vEW, ForgeDirection.WEST);
			
			GL11.glColor3f(colorFrameR, colorFrameG, colorFrameB);
			Vector3fMax vEWS = new Vector3fMax(minNP, maxNP, minNP, maxNP, 1.0F, minPP);
			Vector3fMax vEWN = new Vector3fMax(minNP, maxNP, maxPP, maxNP, 1.0F, maxNP);
			Vector3fMax vNSW = new Vector3fMax(minNP, maxNP, minNP, minPP, 1.0F, maxNP);
			Vector3fMax vNSE = new Vector3fMax(maxPP, maxNP, minNP, maxNP, 1.0F, maxNP);
			
			RenderHelper.renderSide(vEWS, ForgeDirection.EAST);
			RenderHelper.renderSide(vEWS, ForgeDirection.WEST);
			RenderHelper.renderSide(vEWN, ForgeDirection.EAST);
			RenderHelper.renderSide(vEWN, ForgeDirection.WEST);

			RenderHelper.renderSide(vNSW, ForgeDirection.NORTH);
			RenderHelper.renderSide(vNSW, ForgeDirection.SOUTH);
			RenderHelper.renderSide(vNSE, ForgeDirection.NORTH);
			RenderHelper.renderSide(vNSE, ForgeDirection.SOUTH);
			
			isU = true;
		}
		if(frame.isConnectedTo(ForgeDirection.DOWN)){
			Vector3fMax vNS = new Vector3fMax(minPP, 0.0F, minNP, maxPP, minNP, maxNP);
			Vector3fMax vEW = new Vector3fMax(minNP, 0.0F, minPP, maxNP, minNP, maxPP);
			GL11.glColor3f(colorFaceR, colorFaceG, colorFaceB);
			RenderHelper.renderSide(vNS, ForgeDirection.NORTH);
			RenderHelper.renderSide(vNS, ForgeDirection.SOUTH);
			
			RenderHelper.renderSide(vEW, ForgeDirection.EAST);
			RenderHelper.renderSide(vEW, ForgeDirection.WEST);
			
			GL11.glColor3f(colorFrameR, colorFrameG, colorFrameB);
			Vector3fMax vEWS = new Vector3fMax(minNP, 0.0F, minNP, maxNP, minNP, minPP);
			Vector3fMax vEWN = new Vector3fMax(minNP, 0.0F, maxPP, maxNP, minNP, maxNP);
			Vector3fMax vNSW = new Vector3fMax(minNP, 0.0F, minNP, minPP, minNP, maxNP);
			Vector3fMax vNSE = new Vector3fMax(maxPP, 0.0F, minNP, maxNP, minNP, maxNP);
			
			RenderHelper.renderSide(vEWS, ForgeDirection.EAST);
			RenderHelper.renderSide(vEWS, ForgeDirection.WEST);
			RenderHelper.renderSide(vEWN, ForgeDirection.EAST);
			RenderHelper.renderSide(vEWN, ForgeDirection.WEST);

			RenderHelper.renderSide(vNSW, ForgeDirection.NORTH);
			RenderHelper.renderSide(vNSW, ForgeDirection.SOUTH);
			RenderHelper.renderSide(vNSE, ForgeDirection.NORTH);
			RenderHelper.renderSide(vNSE, ForgeDirection.SOUTH);
			isD = true;
		}
		
		boolean isCorner = ((isU || isD) && (isN || isS || isE || isW)) || ((isN || isS) && (isE || isW));
		if(((!isU && !isD && !isN && !isS && !isW && !isE && !isW) || (isU != isD || isN != isS || isE != isW)) && !isCorner){
			RenderHelper.drawCubeWithLines(5, frame.getIsActive(), colorFaceR, colorFaceG, colorFaceB);
		}
		if(isCorner){
			RenderHelper.drawCubeWithLines(4, frame.getIsActive(), colorFaceR, colorFaceG, colorFaceB);
		}
		
		if(isU && isD){
			Vector3fMax vNS = new Vector3fMax(minPP, minNP, minNP, maxPP, maxNP, maxNP);
			Vector3fMax vEW = new Vector3fMax(minNP, minNP, minPP, maxNP, maxNP, maxPP);
			GL11.glColor3f(colorFaceR, colorFaceG, colorFaceB);
			RenderHelper.renderSide(vNS, ForgeDirection.NORTH);
			RenderHelper.renderSide(vNS, ForgeDirection.SOUTH);
			
			RenderHelper.renderSide(vEW, ForgeDirection.EAST);
			RenderHelper.renderSide(vEW, ForgeDirection.WEST);
			
			GL11.glColor3f(colorFrameR, colorFrameG, colorFrameB);
			Vector3fMax vEWS = new Vector3fMax(minNP, minNP, minNP, maxNP, maxNP, minPP);
			Vector3fMax vEWN = new Vector3fMax(minNP, minNP, maxPP, maxNP, maxNP, maxNP);
			Vector3fMax vNSW = new Vector3fMax(minNP, minNP, minNP, minPP, maxNP, maxNP);
			Vector3fMax vNSE = new Vector3fMax(maxPP, minNP, minNP, maxNP, maxNP, maxNP);
			
			RenderHelper.renderSide(vEWS, ForgeDirection.EAST);
			RenderHelper.renderSide(vEWS, ForgeDirection.WEST);
			RenderHelper.renderSide(vEWN, ForgeDirection.EAST);
			RenderHelper.renderSide(vEWN, ForgeDirection.WEST);

			RenderHelper.renderSide(vNSW, ForgeDirection.NORTH);
			RenderHelper.renderSide(vNSW, ForgeDirection.SOUTH);
			RenderHelper.renderSide(vNSE, ForgeDirection.NORTH);
			RenderHelper.renderSide(vNSE, ForgeDirection.SOUTH);
		}else if(isN && isS){
			GL11.glColor3f(colorFaceR, colorFaceG, colorFaceB);
			Vector3fMax vTB = new Vector3fMax(minPP, minNP, minNP, maxPP, maxNP, maxNP);
			Vector3fMax vEW = new Vector3fMax(minNP, minPP, minNP, maxNP, maxPP, maxNP);
			
			RenderHelper.renderSide(vTB, ForgeDirection.UP);
			RenderHelper.renderSide(vTB, ForgeDirection.DOWN);
			RenderHelper.renderSide(vEW, ForgeDirection.EAST);
			RenderHelper.renderSide(vEW, ForgeDirection.WEST);
			
			GL11.glColor3f(colorFrameR, colorFrameG, colorFrameB);
			Vector3fMax vTBW = new Vector3fMax(minNP, minNP, minNP, minPP, maxNP, maxNP);
			Vector3fMax vTBE = new Vector3fMax(maxPP, minNP, minNP, maxNP, maxNP, maxNP);
			Vector3fMax vEWB = new Vector3fMax(minNP, minNP, minNP, maxNP, minPP, maxNP);
			Vector3fMax vEWT = new Vector3fMax(minNP, maxPP, minNP, maxNP, maxNP, maxNP);
			
			RenderHelper.renderSide(vTBW, ForgeDirection.UP);
			RenderHelper.renderSide(vTBW, ForgeDirection.DOWN);
			RenderHelper.renderSide(vTBE, ForgeDirection.UP);
			RenderHelper.renderSide(vTBE, ForgeDirection.DOWN);
			
			RenderHelper.renderSide(vEWT, ForgeDirection.EAST);
			RenderHelper.renderSide(vEWT, ForgeDirection.WEST);
			RenderHelper.renderSide(vEWB, ForgeDirection.EAST);
			RenderHelper.renderSide(vEWB, ForgeDirection.WEST);
		}else if(isE && isW){
			Vector3fMax vTB = new Vector3fMax(minNP, minNP, minPP, maxNP, maxNP, maxPP);
			Vector3fMax vNS = new Vector3fMax(minNP, minPP, minNP, maxNP, maxPP, maxNP);
			GL11.glColor3f(colorFaceR, colorFaceG, colorFaceB);
			RenderHelper.renderSide(vTB, ForgeDirection.UP);
			RenderHelper.renderSide(vTB, ForgeDirection.DOWN);
			
			RenderHelper.renderSide(vNS, ForgeDirection.NORTH);
			RenderHelper.renderSide(vNS, ForgeDirection.SOUTH);
			
			GL11.glColor3f(colorFrameR, colorFrameG, colorFrameB);
			Vector3fMax vTBN = new Vector3fMax(minNP, minNP, minNP, maxNP, maxNP, minPP);
			Vector3fMax vTBS = new Vector3fMax(minNP, minNP, maxPP, maxNP, maxNP, maxNP);
			
			Vector3fMax vNST = new Vector3fMax(minNP, maxPP, minNP, maxNP, maxNP, maxNP);
			Vector3fMax vNSB = new Vector3fMax(minNP, minNP, minNP, maxNP, minPP, maxNP);
			RenderHelper.renderSide(vTBN, ForgeDirection.UP);
			RenderHelper.renderSide(vTBN, ForgeDirection.DOWN);
			RenderHelper.renderSide(vTBS, ForgeDirection.UP);
			RenderHelper.renderSide(vTBS, ForgeDirection.DOWN);
			
			RenderHelper.renderSide(vNST, ForgeDirection.NORTH);
			RenderHelper.renderSide(vNST, ForgeDirection.SOUTH);
			RenderHelper.renderSide(vNSB, ForgeDirection.NORTH);
			RenderHelper.renderSide(vNSB, ForgeDirection.SOUTH);
		}
		
		
		GL11.glEnd();
	}

}
