package k4unl.minecraft.Hydraulicraft.client.renderers;

import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHarvesterFrame;
import k4unl.minecraft.Hydraulicraft.client.models.ModelHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RendererHarvesterTrolley extends TileEntitySpecialRenderer {

	private static final ResourceLocation resLoc =
		new ResourceLocation(ModInfo.LID,"textures/model/harvesterTrolley_tmap.png");
			


	private ModelHarvesterTrolley renderModel;
	
	public RendererHarvesterTrolley(){
		renderModel = new ModelHarvesterTrolley();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y,
			double z, float f) {
		//Open the GL Matrix
		GL11.glPushMatrix();
		
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float)z + 0.5F);
		
		
		GL11.glRotatef(180, 0F, 0F, 1F);
		
		bindTexture(resLoc);
		
		GL11.glPushMatrix();
		
		renderModel.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

}
