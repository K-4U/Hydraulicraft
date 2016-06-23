package k4unl.minecraft.Hydraulicraft.client.renderers.storage;

import k4unl.minecraft.Hydraulicraft.tileEntities.storage.TileFluidTank;
import k4unl.minecraft.k4lib.client.RenderHelper;
import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidTankInfo;
import org.lwjgl.opengl.GL11;

/**
 * @author Koen Beckers (K-4U)
 */
public class RendererBlockFluidTank extends TileEntitySpecialRenderer<TileFluidTank> {

    @Override
    public void renderTileEntityAt(TileFluidTank te, double x, double y, double z, float partialTicks, int destroyStage) {

        GL11.glPushMatrix();

        GL11.glTranslatef((float) x, (float) y, (float) z);

        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glAlphaFunc(GL11.GL_GEQUAL, 0.4F);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);

        GL11.glPushMatrix();
        FluidTankInfo tankInfo = te.getTankInfo(EnumFacing.UP)[0];

        if (tankInfo != null && tankInfo.fluid != null && tankInfo.fluid.getFluid() != null) {
            TextureAtlasSprite fluidSprite = Functions.getFluidIcon(tankInfo.fluid.getFluid());

            Vector3fMax vector = new Vector3fMax(RenderHelper.pixel * 3, RenderHelper.pixel, RenderHelper.pixel * 3, 1F - (RenderHelper.pixel) * 3, 1F - RenderHelper.pixel, 1F - (RenderHelper.pixel * 3));

            float h = vector.getYMax() - vector.getYMin();
            vector.setYMax(vector.getYMin() + (h * ((float) tankInfo.fluid.amount / (float) tankInfo.capacity)));

            GL11.glBegin(GL11.GL_QUADS);
            RenderHelper.drawGL11SideBottomWithTexture(vector, fluidSprite);
            RenderHelper.drawGL11SideTopWithTexture(vector, fluidSprite);
            RenderHelper.drawGL11SideEastWithTexture(vector, fluidSprite);
            RenderHelper.drawGL11SideWestWithTexture(vector, fluidSprite);
            RenderHelper.drawGL11SideNorthWithTexture(vector, fluidSprite);
            RenderHelper.drawGL11SideSouthWithTexture(vector, fluidSprite);
            GL11.glEnd();
        }

        GL11.glPopMatrix();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}
