package k4unl.minecraft.Hydraulicraft.client.renderers.gow;

import k4unl.minecraft.Hydraulicraft.client.renderers.RenderHelper;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Vector3fMax;
import k4unl.minecraft.Hydraulicraft.tileEntities.gow.TilePortalTeleporter;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

public class RendererPortalTeleporter extends TileEntitySpecialRenderer {
	
	@Override
	public void renderTileEntityAt(TileEntity ent, double x, double y,
			double z, float frame) {
		doRender((TilePortalTeleporter)ent, x, y, z, frame);

	}
	
	private void doRender(TilePortalTeleporter myTeleporter, double x, double y, double z, float frame){
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		
		//FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		
		renderTeleporter(myTeleporter, frame);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		
	}
	
	private void renderTeleporter(TilePortalTeleporter teleporter, float frame){
		if(MinecraftForgeClient.getRenderPass() == 1){
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glAlphaFunc(GL11.GL_EQUAL, teleporter.getTransparancy(frame));
			GL11.glBegin(GL11.GL_QUADS);
			Vector3fMax vector = new Vector3fMax(0.499F, 0.499F, 0.499F, 0.501F, 0.501F, 0.501F);
	
			
			if(teleporter != null){
				if(teleporter.getBaseDir() != null){
					if(teleporter.getBaseDir().equals(ForgeDirection.NORTH) | teleporter.getPortalDir().equals(ForgeDirection.NORTH)){
						vector.setZMin(0.0F);
						vector.setZMax(1.0F);
					}
					if(teleporter.getPortalDir().equals(ForgeDirection.UP)){
						vector.setYMin(0.0F);
						vector.setYMax(1.0F);
					}
					if(teleporter.getBaseDir().equals(ForgeDirection.EAST) || teleporter.getPortalDir().equals(ForgeDirection.EAST)){
						vector.setXMin(0.0F);
						vector.setXMax(1.0F);
					}
					if(teleporter.isEdge(ForgeDirection.NORTH)){
						vector.setZMin(-0.5F);
					}
					if(teleporter.isEdge(ForgeDirection.SOUTH)){
						vector.setZMax(1.5F);
					}
					if(teleporter.isEdge(ForgeDirection.UP)){
						vector.setYMax(1.5F);
					}
					if(teleporter.isEdge(ForgeDirection.DOWN)){
						vector.setYMin(-.5F);
					}
					if(teleporter.isEdge(ForgeDirection.WEST)){
						vector.setXMin(-0.5F);
					}
					if(teleporter.isEdge(ForgeDirection.EAST)){
						vector.setXMax(1.5F);
					}
				}
				
				GL11.glColor4f(0.63671875F, 0.0F, 0.84765625F, teleporter.getTransparancy(frame));
			}
			//GL11.glColor3f
			
			RenderHelper.drawWhiteCube(vector);
			
			GL11.glEnd();
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		}
	}

}
