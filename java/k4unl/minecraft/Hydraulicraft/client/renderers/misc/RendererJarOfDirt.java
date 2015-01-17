package k4unl.minecraft.Hydraulicraft.client.renderers.misc;

import cpw.mods.fml.client.FMLClientHandler;
import k4unl.minecraft.Hydraulicraft.client.models.ModelJarOfDirt;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RendererJarOfDirt extends TileEntitySpecialRenderer {

    private static final ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/model/jarOfDirt.png");

    private ModelJarOfDirt renderModel;

    public RendererJarOfDirt() {

        renderModel = new ModelJarOfDirt();
    }

    @Override
    public void renderTileEntityAt(TileEntity tEnt, double x, double y, double z, float f) {

        GL11.glPushMatrix();

        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.0F, (float)z + 0.5F);

        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        float scale = 0.7f;
        GL11.glScalef(scale, scale,scale);
        GL11.glPushMatrix();
        GL11.glRotatef(180, 0F, 0F, 1F);

        renderModel.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

}
