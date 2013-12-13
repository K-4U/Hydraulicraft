package pet.minecraft.Hydraulicraft.client.renderers;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicHose;
import pet.minecraft.Hydraulicraft.lib.config.ModInfo;
import pet.minecraft.Hydraulicraft.models.ModelHydraulicHose;

public class RendererHydraulicHose extends TileEntitySpecialRenderer {

	private static final ResourceLocation resLoc = new ResourceLocation(ModInfo.LID,
			"textures/model/hydraulicHose_tmap.png");


	private ModelHydraulicHose renderModel;
	
	public RendererHydraulicHose(){
		renderModel = new ModelHydraulicHose();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y,
			double z, float f) {
		//Open the GL Matrix
		GL11.glPushMatrix();
		
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float)z + 0.5F);
		
		//Just do it...
		GL11.glRotatef(180, 0F, 0F, 1F);
		
		
		//Bind texture
		TileHydraulicHose hose = (TileHydraulicHose) tileEntity;
		
		this.bindTexture(resLoc);
		
		GL11.glPushMatrix();
		
		renderModel.setConnectedSides(hose.getConnectedSides());
		
		renderModel.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

}
