package k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.client.renderers;

import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities.TileHydraulicGenerator;
import k4unl.minecraft.k4lib.client.RenderHelper;
import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

public class RendererHydraulicGenerator extends TileEntitySpecialRenderer {

    private static final ResourceLocation resLoc =
            new ResourceLocation(ModInfo.LID, "textures/model/hydraulicGenerator.png");

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f, int destroyStage) {

        TileHydraulicGenerator t = (TileHydraulicGenerator) tileentity;
        //Get metadata for rotation:
        int rotation = 0;//t.getDir();
        int metadata = t.getBlockMetadata();

        doRender(t, (float) x, (float) y, (float) z, f, rotation, metadata);
    }

    public void itemRender(float x, float y, float z, float f) {

        GL11.glPushMatrix();

        GL11.glTranslatef(x, y, z);

        GL11.glTranslatef(1.0F, 0.0F, 0.0F);
        GL11.glRotatef(90F, 0.0F, -1.0F, 0.0F);

        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);

        GL11.glPushMatrix();
        //GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
        GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
        //Do rendering
        //GL11.glBegin(GL11.GL_QUADS);
        GL11.glColor3f(0.8F, 0.8F, 0.8F);
        drawFluidContainers();
        drawBase();

        //GL11.glEnd();

        //GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    public void doRender(TileHydraulicGenerator t, float x, float y, float z, float f, int rotation, int metadata) {

        GL11.glPushMatrix();

        GL11.glTranslatef(x, y, z);

        switch (t.getFacing()) {
            case EAST:
                GL11.glTranslatef(1.0F, 0.0F, 0.0F);
                GL11.glRotatef(90F, 0.0F, -1.0F, 0.0F);
                //GL11.glRotatef(90F, 0.0F, 0.0F, -1.0F);
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


        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);

        GL11.glPushMatrix();

        //GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
        GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
        //Do rendering
        GL11.glColor3f(0.8F, 0.8F, 0.8F);
        drawFluidContainers();
        //drawHydraulicsTank(t, false);
        drawBase();

        //GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    private void drawBase() {

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        float insetFirst = RenderHelper.pixel * 2;
        float baseHeight = RenderHelper.pixel * 3;

        float tXI = RenderHelper.renderPixel * 30;
        float tXO = 1.0F;
        float tYIT = 0.0F;
        float tYIB = RenderHelper.renderPixel * 13;
        float tYTB = RenderHelper.renderPixel * 30;
        float tYFT = RenderHelper.renderPixel * 29;

        Vector3fMax vector = new Vector3fMax(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        //Top side:
        GL11.glBegin(GL11.GL_QUADS);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.0F, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.5F, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin() + insetFirst, 0.5F, tYTB);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin() + insetFirst, 0.0F, tYTB);

        //Bottom side:
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.0F, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.5F, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 0.5F, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.0F, 0.5F);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_POLYGON);
        //Draw west side:
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.5F, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.5F, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin() + insetFirst, tXI, tYIT);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin() + baseHeight, vector.getZMin() + insetFirst, tXI, tYIB);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin() + baseHeight, vector.getZMin(), tXO, tYIB);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 1.0F, 0.5F);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_POLYGON);
        //Draw east side:
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.5F, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), tXO, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin() + baseHeight, vector.getZMin(), tXO, tYIB);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin() + baseHeight, vector.getZMin() + insetFirst, tXI, tYIB);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin() + insetFirst, tXI, tYIT);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.5F, 0.0F);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_QUADS);
        //Draw north side
        //Upper bit
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin() + insetFirst, 0.5F, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin() + insetFirst, 1.0F, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin() + insetFirst, 1.0F, 1.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin() + insetFirst, 0.5F, 1.0F);

        //Lower top bit
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin() + baseHeight, vector.getZMin() + insetFirst, 0.0F, tYFT);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin() + baseHeight, vector.getZMin() + insetFirst, 0.5F, tYFT);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin() + baseHeight, vector.getZMin(), 0.5F, 1.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin() + baseHeight, vector.getZMin(), 0.0F, 1.0F);

        //Lower bit
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin() + baseHeight, vector.getZMin(), 1.0F, tYTB);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin() + baseHeight, vector.getZMin(), 0.5F, tYTB);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.5F, 1.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 1.0F, 1.0F);


        //Draw south side
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.0F, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.5F, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.5F, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.0F, 0.5F);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void drawFluidContainers() {

        GL11.glBegin(GL11.GL_QUADS);
        float containerHeight = RenderHelper.pixel * 10;
        float containerBegin = RenderHelper.pixel * 4;
        float containerEnd = containerHeight + containerBegin;
        float containerDepthBegin = RenderHelper.pixel * 2;
        float containerDepthEnd = containerDepthBegin + RenderHelper.pixel * 2;
        Vector3fMax vector = new Vector3fMax(RenderHelper.pixel * 2, containerBegin, containerDepthBegin, RenderHelper.pixel * 5, containerEnd, containerDepthEnd);
        drawFluidContainer(vector);

        vector = new Vector3fMax(RenderHelper.pixel * 6, containerBegin, containerDepthBegin, RenderHelper.pixel * 10, containerEnd, containerDepthEnd);
        drawFluidContainer(vector);

        vector = new Vector3fMax(RenderHelper.pixel * 11, containerBegin, containerDepthBegin, RenderHelper.pixel * 14, containerEnd, containerDepthEnd);
        drawFluidContainer(vector);

        GL11.glEnd();
    }

    private void drawFluidContainer(Vector3fMax vector) {
        //Top side:
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.0F, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.5F, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.5F, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 0.0F, 0.5F);

        //Bottom side:
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.0F, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.5F, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.5F, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.0F, 0.5F);

        //Draw west side:
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.0F, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.5F, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.5F, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.0F, 0.5F);

        //Draw east side:
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 0.5F, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.0F, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.0F, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.5F, 0.0F);

        //Draw north side
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.5F, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.0F, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.0F, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.5F, 0.0F);

    }

    private void drawHydraulicsTank(TileHydraulicGenerator t, boolean isItem) {
        //float containerWidth = 21.0F/128.0F;
        float containerSpacing = 16.0F / 128.0F;
        float containerHeight = 76.0F / 128.0F;
        float containerBegin = 34.0F / 128.0F;
        float containerEnd = containerHeight + containerBegin;
        float containerDepthBegin = 0.09475F;
        float containerDepthEnd = containerDepthBegin + 0.04F;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Vector3fMax vectorFilled = new Vector3fMax(containerSpacing, containerBegin, containerDepthBegin, 1.0F - containerSpacing, containerEnd, containerDepthEnd);
        if (!isItem) {
            float h = vectorFilled.getYMax() - vectorFilled.getYMin();
            vectorFilled.setYMax(vectorFilled.getYMin() + (h * ((float) t.getHandler().getStored() / (float) t.getMaxStorage())));


            TextureAtlasSprite fluidIcon;
            if (t.getHandler().isOilStored()) {
                fluidIcon = Functions.getFluidIcon(Fluids.fluidHydraulicOil);
            } else {
                fluidIcon = Functions.getFluidIcon(FluidRegistry.WATER);
            }

            if (t.getHandler().getStored() > 0) {
                RenderHelper.drawTesselatedCubeWithTexture(vectorFilled, fluidIcon);
            }

            //Reset texture after using tesselators.
            FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);
        }

        GL11.glDisable(GL11.GL_BLEND);
    }

}
