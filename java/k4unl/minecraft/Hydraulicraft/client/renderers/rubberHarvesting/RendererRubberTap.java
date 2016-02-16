package k4unl.minecraft.Hydraulicraft.client.renderers.rubberHarvesting;

import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.tileEntities.rubberHarvesting.TileRubberTap;
import k4unl.minecraft.k4lib.client.RenderHelper;
import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

/**
 * @author Koen Beckers (K-4U)
 */
public class RendererRubberTap extends TileEntitySpecialRenderer<TileRubberTap> {

    @Override
    public void renderTileEntityAt(TileRubberTap te, double x, double y, double z, float partialTicks, int destroyStage) {

        if (te.isTapping()) {
            GL11.glPushMatrix();

            GL11.glTranslatef((float) x, (float) y, (float) z);

            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glAlphaFunc(GL11.GL_GEQUAL, 0.4F);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);

            GL11.glPushMatrix();

            TextureAtlasSprite fluidSprite = Functions.getFluidIcon(Fluids.fluidRubber);

            Vector3fMax vector = new Vector3fMax(RenderHelper.pixel * 7, 0.0F, RenderHelper.pixel * 5, RenderHelper
              .pixel * 9, RenderHelper.pixel * 6, RenderHelper.pixel * 7);

            GL11.glBegin(GL11.GL_QUADS);
            RenderHelper.drawGL11SideBottomWithTexture(vector, fluidSprite);
            RenderHelper.drawGL11SideTopWithTexture(vector, fluidSprite);
            RenderHelper.drawGL11SideEastWithTexture(vector, fluidSprite);
            RenderHelper.drawGL11SideWestWithTexture(vector, fluidSprite);
            RenderHelper.drawGL11SideNorthWithTexture(vector, fluidSprite);
            RenderHelper.drawGL11SideSouthWithTexture(vector, fluidSprite);
            for(int i = 1; i <= te.getTankDepth(); i++) {
                vector.setYMax(-i+1.0F);
                vector.setYMin(-i);


                RenderHelper.drawGL11SideBottomWithTexture(vector, fluidSprite);
                RenderHelper.drawGL11SideTopWithTexture(vector, fluidSprite);
                RenderHelper.drawGL11SideEastWithTexture(vector, fluidSprite);
                RenderHelper.drawGL11SideWestWithTexture(vector, fluidSprite);
                RenderHelper.drawGL11SideNorthWithTexture(vector, fluidSprite);
                RenderHelper.drawGL11SideSouthWithTexture(vector, fluidSprite);

            }
            GL11.glEnd();

            GL11.glPopMatrix();

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
    }
}
