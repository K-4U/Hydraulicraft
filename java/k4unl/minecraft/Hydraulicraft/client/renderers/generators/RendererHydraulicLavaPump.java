package k4unl.minecraft.Hydraulicraft.client.renderers.generators;

import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicLavaPump;
import k4unl.minecraft.k4lib.client.RenderHelper;
import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

public class RendererHydraulicLavaPump extends TileEntitySpecialRenderer {

    private static final ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/model/hydraulicLavaPump.png");

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        doRender((TileHydraulicLavaPump)te, x, y, z, partialTicks, destroyStage);
    }

    public void doRender(TileHydraulicLavaPump t, double x, double y, double z, float f, int destroyStage) {

        GL11.glPushMatrix();

        GL11.glTranslated(x, y, z);
        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);


        switch (t.getFacing()) {
            case EAST:
                GL11.glTranslatef(0.0F, 1.0F, 1.0F);
                GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(90F, 0.0F, 0.0F, -1.0F);
                break;
            case NORTH:
                GL11.glTranslatef(1.0F, 1.0F, 1.0F);
                GL11.glRotatef(90F, -1.0F, 0.0F, 0.0F);
                GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
                break;
            case WEST:
                GL11.glTranslatef(1.0F, 1.0F, 0.0F);
                GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(90F, 0.0F, 0.0F, 1.0F);
                break;
            case DOWN:
                GL11.glTranslatef(0.0F, 1.0F, 1.0F);
                GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
                break;
            case SOUTH:
                GL11.glTranslatef(0.0F, 1.0F, 0.0F);
                GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
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
        //renderTieredBars(t.getTier(), thickness);
        //renderInsides(thickness, t);
        renderGaugesContents(thickness, t);
        renderGauges(thickness);


        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    private void renderInsidesWithoutLighting(float thickness) {
        GL11.glBegin(GL11.GL_QUADS);
        Vector3fMax insides = new Vector3fMax(thickness, thickness, thickness, 1.0F - thickness, 1.0F - thickness, 1.0F - thickness);
        RenderHelper.drawTexturedCube(insides);
        GL11.glEnd();
    }

    private void renderGauges(float thickness) {
        //net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GL11.glEnable(GL11.GL_BLEND);

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        GL11.glBegin(GL11.GL_QUADS);
        //RenderHelper.startTesselating();
        float texturePixel = RenderHelper.renderPixel;
        Vector3fMax vectorPressure = new Vector3fMax(1.0F - thickness - RenderHelper.pixel * 6, 0.0F, thickness + RenderHelper.pixel, 1.0F - thickness - RenderHelper.pixel, 1.0002F - thickness, 1.0F - thickness - RenderHelper.pixel);
        
        RenderHelper.vertexWithTexture(vectorPressure.getXMin(), vectorPressure.getYMax(), vectorPressure.getZMax(), texturePixel * 19, texturePixel * 14); //BL
        RenderHelper.vertexWithTexture(vectorPressure.getXMax(), vectorPressure.getYMax(), vectorPressure.getZMax(), texturePixel * 24, texturePixel * 14);    //BR
        RenderHelper.vertexWithTexture(vectorPressure.getXMax(), vectorPressure.getYMax(), vectorPressure.getZMin(), texturePixel * 24, 0.0F); //TR
        RenderHelper.vertexWithTexture(vectorPressure.getXMin(), vectorPressure.getYMax(), vectorPressure.getZMin(), texturePixel * 19, 0.0F); //TL

        Vector3fMax vectorLavaWindow = new Vector3fMax(thickness + RenderHelper.pixel, 0.0F, thickness + RenderHelper.pixel, 1.0F - thickness - RenderHelper.pixel * 6, 1.001F - thickness, 1.0F - thickness - RenderHelper.pixel);

        RenderHelper.vertexWithTexture(vectorLavaWindow.getXMin(), vectorLavaWindow.getYMax(), vectorLavaWindow.getZMax(), texturePixel * 24, texturePixel * 14); //BL
        RenderHelper.vertexWithTexture(vectorLavaWindow.getXMax(), vectorLavaWindow.getYMax(), vectorLavaWindow.getZMax(), texturePixel * 30, texturePixel * 14); //BR
        RenderHelper.vertexWithTexture(vectorLavaWindow.getXMax(), vectorLavaWindow.getYMax(), vectorLavaWindow.getZMin(), texturePixel * 30, 0.0F);  //TR
        RenderHelper.vertexWithTexture(vectorLavaWindow.getXMin(), vectorLavaWindow.getYMax(), vectorLavaWindow.getZMin(), texturePixel * 24, 0.0F);  //TL

        //RenderHelper.tesselatorDraw();
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
        //GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
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

        Vector3fMax vectorPressure = new Vector3fMax(1.0F - thickness - RenderHelper.pixel * 6, 0.0F, thickness + RenderHelper.pixel, 1.0F - thickness - RenderHelper.pixel, 1.0002F - thickness, 1.0F - thickness - RenderHelper.pixel);
        float h = vectorPressure.getZMax() - vectorPressure.getZMin();
        vectorPressure.setZMin(vectorPressure.getZMax() - (h * (t.getHandler().getPressure(EnumFacing.UP) / t.getMaxPressure(t.getHandler().isOilStored(), t.getFacing()))));
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3f(vectorPressure.getXMin(), vectorPressure.getYMax(), vectorPressure.getZMax()); //BL
        GL11.glVertex3f(vectorPressure.getXMax(), vectorPressure.getYMax(), vectorPressure.getZMax()); //BR
        GL11.glVertex3f(vectorPressure.getXMax(), vectorPressure.getYMax(), vectorPressure.getZMin()); //TR
        GL11.glVertex3f(vectorPressure.getXMin(), vectorPressure.getYMax(), vectorPressure.getZMin()); //TL
        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
    }

    private void renderInsides(float thickness, TileHydraulicLavaPump t) {
        GL11.glBegin(GL11.GL_QUADS);
        Vector3fMax insides = new Vector3fMax(thickness, thickness, thickness, 1.0F - thickness, 1.0F - thickness, 1.0F - thickness);
        RenderHelper.drawTexturedCubeWithLight(insides, t);

        GL11.glEnd();
    }

    @SuppressWarnings("cast")
    private void drawLavaTank(TileHydraulicLavaPump t, boolean isItem, float thickness) {
        if (!isItem) {
            Vector3fMax vectorFilled = new Vector3fMax(thickness + (RenderHelper.pixel * 2), 0.8F, thickness + (RenderHelper.pixel * 2), 1.0F - thickness -
                    RenderHelper.pixel * 7, 1.005F - thickness, 1.0F - thickness - RenderHelper.pixel * 2);
            float h = vectorFilled.getZMax() - vectorFilled.getZMin();
            FluidTankInfo[] tankInfo = t.getTankInfo(EnumFacing.UP);
            if (tankInfo != null) {

                float fluidAmount = 0F;
                if (tankInfo[0].fluid != null) {
                    fluidAmount = tankInfo[0].fluid.amount;
                }

                vectorFilled.setZMin(vectorFilled.getZMax() - (h * (fluidAmount / (float) tankInfo[0].capacity)));


                TextureAtlasSprite fluidIcon = Functions.getFluidIcon(FluidRegistry.LAVA);

                if (fluidAmount > 0) {
                    RenderHelper.drawTesselatedCubeWithTexture(vectorFilled, fluidIcon);
                }
            }
            //Reset texture after using tesselators.
            FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);
        }
    }

    private void renderTieredBars(PressureTier tier, float thickness) {
        Vector3fMax ln = new Vector3fMax(thickness, 0.0F, 0.0F, 1.0F - thickness, thickness, thickness);
        Vector3fMax tn = new Vector3fMax(thickness, 1.0F - thickness, 0.0F, 1.0F - thickness, 1.0F, thickness);
        Vector3fMax ne = new Vector3fMax(1.0F - thickness, 0.0F, 0.0F, 1.0F, 1.0F, thickness);

        for (int i = 0; i < 4; i++) {
            drawTieredHorizontalCube(ln, tier, thickness);
            drawTieredHorizontalCube(tn, tier, thickness);
            drawTieredVerticalCube(ne, tier, thickness);

            GL11.glTranslatef(1.0F, 0.0F, 0.0F);
            GL11.glRotatef(90.0F, 0.0F, -1.0F, 0.0F);
        }
    }

    private void drawTieredHorizontalCube(Vector3fMax vector, PressureTier tier_, float thickness) {
        GL11.glBegin(GL11.GL_QUADS);
        //RenderHelper.drawColoredCube(vector);
        float texturePixel = RenderHelper.renderPixel;
        float tXb[] = {texturePixel * 16, texturePixel * 17, texturePixel * 18};
        float tXe[] = {texturePixel * 17, texturePixel * 18, texturePixel * 19};
        int tier = tier_.toInt();
        tXe[tier] = tXb[tier] + (thickness / 2);

        //Top side:
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), tXe[tier], 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), tXe[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), tXb[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), tXb[tier], 0.0F);

        //Bottom side:
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), tXe[tier], 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), tXe[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), tXb[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), tXb[tier], 0.0F);

        //Draw west side:
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), tXe[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), tXb[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), tXb[tier], 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), tXe[tier], 0.0F);

        //Draw east side:
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), tXe[tier], 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), tXe[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), tXb[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), tXb[tier], 0.0F);

        //Draw north side
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), tXe[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), tXb[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), tXb[tier], 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), tXe[tier], 0.0F);

        //Draw south side
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), tXe[tier], 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), tXe[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), tXb[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), tXb[tier], 0.0F);
        GL11.glEnd();
    }

    private void drawTieredVerticalCube(Vector3fMax vector, PressureTier tier_, float thickness) {
        GL11.glBegin(GL11.GL_QUADS);
        //RenderHelper.drawColoredCube(vector);
        float texturePixel = RenderHelper.renderPixel;
        float tXb[] = {texturePixel * 16, texturePixel * 17, texturePixel * 18};
        float tXe[] = {texturePixel * 17, texturePixel * 18, texturePixel * 19};
        int tier = tier_.toInt();
        tXe[tier] = tXb[tier] + (thickness / 2);

        //Top side:
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), tXe[tier], 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), tXe[tier], (thickness / 2));
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), tXb[tier], (thickness / 2));
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), tXb[tier], 0.0F);

        //Bottom side:
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), tXe[tier], 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), tXe[tier], (thickness / 2));
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), tXb[tier], (thickness / 2));
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), tXb[tier], 0.0F);

        //Draw west side:
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), tXb[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), tXb[tier], 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), tXe[tier], 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), tXe[tier], 0.5F);

        //Draw east side:
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), tXe[tier], 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), tXe[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), tXb[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), tXb[tier], 0.0F);

        //Draw north side
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), tXb[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), tXb[tier], 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), tXe[tier], 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), tXe[tier], 0.5F);

        //Draw south side
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), tXe[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), tXb[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), tXb[tier], 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), tXe[tier], 0.0F);
        GL11.glEnd();
    }
}
