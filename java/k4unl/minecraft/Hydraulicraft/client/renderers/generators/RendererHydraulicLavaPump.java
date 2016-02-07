package k4unl.minecraft.Hydraulicraft.client.renderers.generators;

import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicLavaPump;
import k4unl.minecraft.k4lib.client.RenderHelper;
import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTankInfo;
import org.lwjgl.opengl.GL11;

public class RendererHydraulicLavaPump extends TileEntitySpecialRenderer {

    private static final ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/model/hydraulicLavaPump.png");

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {

        doRender((TileHydraulicLavaPump) te, x, y, z, partialTicks, destroyStage);
    }

    public void doRender(TileHydraulicLavaPump t, double x, double y, double z, float f, int destroyStage) {

        GL11.glPushMatrix();

        GL11.glTranslated(x, y, z);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);


        switch (t.getFacing()) {
            case EAST:
                GL11.glTranslatef(1.0F, 0.0F, 0.0F);
                GL11.glRotatef(90F, 0.0F, -1.0F, 0.0F);
                break;
            case NORTH:
                break;
            case WEST:
                GL11.glTranslatef(0.0F, 0.0F, 1.0F);
                GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
                break;
            case SOUTH:
                GL11.glTranslatef(1.0F, 0.0F, 1.0F);
                GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
                break;
            case UP:
            default:
                break;
        }

        //GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
        GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
        //Do rendering
        GL11.glColor3f(0.8F, 0.8F, 0.8F);
        float thickness = RenderHelper.pixel;
        renderGaugesContents(thickness, t);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    private void renderGaugesContents(float thickness, TileHydraulicLavaPump t) {

        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        drawLavaTank(t, false, thickness);

        GL11.glDisable(GL11.GL_TEXTURE_2D);

        float a = (Constants.COLOR_PRESSURE >> 24 & 255) / 255.0F;
        float r = (Constants.COLOR_PRESSURE >> 16 & 255) / 255.0F;
        float g = (Constants.COLOR_PRESSURE >> 8 & 255) / 255.0F;
        float b = (Constants.COLOR_PRESSURE & 255) / 255.0F;
        GL11.glAlphaFunc(GL11.GL_EQUAL, a);
        GL11.glColor4f(r, g, b, a);

        /*Vector3fMax vectorPressure = new Vector3fMax(1.0F - thickness - RenderHelper.pixel * 5, 0.0F, thickness + RenderHelper.pixel*2, 1.0F -
          thickness - RenderHelper.pixel*2, 1.002F - thickness, 1.0F - thickness - RenderHelper.pixel * 2);*/
        Vector3fMax vectorPressure = new Vector3fMax(
          0F + thickness + RenderHelper.pixel * 2,
          thickness + RenderHelper.pixel * 2,
          thickness - 0.001F,
          0F + thickness + RenderHelper.pixel * 5,
          1F - thickness - (2 * RenderHelper.pixel),
          1F);
        float h = vectorPressure.getYMax() - vectorPressure.getYMin();
        vectorPressure.setYMin(vectorPressure.getYMax() - (h * (t.getHandler().getPressure(EnumFacing.UP) / t.getMaxPressure(t.getHandler().isOilStored(), t.getFacing()))));
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3f(vectorPressure.getXMin(), vectorPressure.getYMax(), vectorPressure.getZMin()); //BL
        GL11.glVertex3f(vectorPressure.getXMax(), vectorPressure.getYMax(), vectorPressure.getZMin()); //BR
        GL11.glVertex3f(vectorPressure.getXMax(), vectorPressure.getYMin(), vectorPressure.getZMin()); //TR
        GL11.glVertex3f(vectorPressure.getXMin(), vectorPressure.getYMin(), vectorPressure.getZMin()); //TL
        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
    }

    @SuppressWarnings("cast")
    private void drawLavaTank(TileHydraulicLavaPump t, boolean isItem, float thickness) {

        if (!isItem) {
            /*Vector3fMax vectorFilled = new Vector3fMax(thickness + (RenderHelper.pixel * 2), 0.8F, thickness + (RenderHelper.pixel * 2), 1.0F -
              thickness -
                    RenderHelper.pixel * 7, 1.002F - thickness, 1.0F - thickness - RenderHelper.pixel * 2);*/
            Vector3fMax vectorFilled = new Vector3fMax(
              0F + thickness + RenderHelper.pixel * 7,
              thickness + RenderHelper.pixel * 2,
              thickness - 0.001F,
              0F + thickness + RenderHelper.pixel * 12,
              1F - thickness - (2 * RenderHelper.pixel),
              1F);
            float h = vectorFilled.getYMax() - vectorFilled.getYMin();
            FluidTankInfo[] tankInfo = t.getTankInfo(EnumFacing.UP);
            if (tankInfo != null) {

                float fluidAmount = 0F;
                if (tankInfo[0].fluid != null) {
                    fluidAmount = tankInfo[0].fluid.amount;
                }

                vectorFilled.setYMax(vectorFilled.getYMin() + (h * (fluidAmount / (float) tankInfo[0].capacity)));

                TextureAtlasSprite fluidIcon = Functions.getFluidIcon(FluidRegistry.LAVA);

                if (fluidAmount > 0) {
                    GL11.glBegin(GL11.GL_QUADS);
                    RenderHelper.vertexWithTexture(vectorFilled.getXMin(), vectorFilled.getYMax(), vectorFilled.getZMin(), fluidIcon.getMinU(),
                      fluidIcon.getMaxV()); //BL
                    RenderHelper.vertexWithTexture(vectorFilled.getXMax(), vectorFilled.getYMax(), vectorFilled.getZMin(), fluidIcon.getMaxU(),
                      fluidIcon.getMaxV()); //BR
                    RenderHelper.vertexWithTexture(vectorFilled.getXMax(), vectorFilled.getYMin(), vectorFilled.getZMin(), fluidIcon.getMaxU(),
                      fluidIcon.getMinV()); //TR
                    RenderHelper.vertexWithTexture(vectorFilled.getXMin(), vectorFilled.getYMin(), vectorFilled.getZMin(), fluidIcon.getMinU(),
                      fluidIcon.getMinV()); //TL
                    GL11.glEnd();
                    //RenderHelper.drawTesselatedCube(vectorFilled);
                    /*GL11.glBegin(GL11.GL_QUADS);
                    RenderHelper.drawColoredCube(vectorFilled);
                    GL11.glEnd();*/
                    //RenderHelper.drawTesselatedCubeWithTexture(vectorFilled, fluidIcon);
                }
            }
        }
    }
}
