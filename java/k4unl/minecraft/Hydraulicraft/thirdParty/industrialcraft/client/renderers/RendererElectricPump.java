package k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.client.renderers;

import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities.TileElectricPump;
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

public class RendererElectricPump extends TileEntitySpecialRenderer {

    private static final ResourceLocation resLoc =
            new ResourceLocation(ModInfo.LID, "textures/model/electricPump.png");

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y,
                                   double z, float f, int destroyStage) {

        TileElectricPump t = (TileElectricPump) tileentity;
        //Get metadata for rotation:
        int rotation = 0;//t.getDir();
        int metadata = t.getBlockMetadata();

        doRender(t, (float) x, (float) y, (float) z, f, rotation, metadata);
    }

    public void itemRender(float x, float y,
                           float z, int tier) {

        GL11.glPushMatrix();
        GL11.glPushMatrix();

        GL11.glTranslatef(x, y, z);

        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);

        GL11.glPushMatrix();
        //GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
        GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
        //Do rendering
        GL11.glColor3f(0.8F, 0.8F, 0.8F);
        drawBase(tier);
        drawElectricConnector();
        drawHydraulicsConnector();
        drawElectricBlock(null, true);
        drawHydraulicsTank(null, true);


        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    public void doRender(TileElectricPump t, float x, float y, float z, float f, int rotation, int metadata) {

        GL11.glPushMatrix();
        GL11.glPushMatrix();

        GL11.glTranslatef(x, y, z);

        switch (t.getFacing()) {
            case EAST:
                GL11.glTranslatef(1.0F, 0.0F, 0.0F);
                GL11.glRotatef(90F, 0.0F, -1.0F, 0.0F);
                break;
            case UP:
                break;
            case WEST:
                GL11.glTranslatef(0.0F, 0.0F, 1.0F);
                GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
                break;
            case DOWN:
                //GL11.glTranslatef(0.0F, 1.0F, 1.0F);
                //GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
                break;
            case SOUTH:
                GL11.glTranslatef(1.0F, 0.0F, 1.0F);
                GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
                break;
            case NORTH:
            default:
                break;
        }


        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);

        GL11.glPushMatrix();
        //GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
        GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
        //Do rendering
        GL11.glColor3f(0.8F, 0.8F, 0.8F);

        drawBase(t.getTier());
        drawElectricConnector();
        drawHydraulicsConnector();
        drawElectricBlock(t, false);
        drawHydraulicsTank(t, false);


        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    private void drawBase(int tier) {

        GL11.glBegin(GL11.GL_QUADS);
        float tXb[] = {RenderHelper.bigRenderPixel * 16, RenderHelper.bigRenderPixel * 17, RenderHelper.bigRenderPixel * 18};
        float tXe[] = {RenderHelper.bigRenderPixel * 17, RenderHelper.bigRenderPixel * 18, RenderHelper.bigRenderPixel * 19};
        //Base
        Vector3fMax vector = new Vector3fMax(0.0F, 0.0F, 0.0F, 1.0F, RenderHelper.pixel, 1.0F);
        //RenderHelper.drawColoredCube(new Vector3fMax(0.0F, 0.0F, 0.0F, 1.0F, 0.1F, 1.0F));

        //Top side:
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), RenderHelper.bigRenderPixel * 0, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), RenderHelper.bigRenderPixel * 16, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), RenderHelper.bigRenderPixel * 16, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), RenderHelper.bigRenderPixel * 0, 0.5F);

        //Bottom side:
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), RenderHelper.bigRenderPixel * 0, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), RenderHelper.bigRenderPixel * 16, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), RenderHelper.bigRenderPixel * 16, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), RenderHelper.bigRenderPixel * 0, 0.5F);

        //Draw west side:
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), tXe[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), tXb[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), tXb[tier], 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), tXe[tier], 0.0F);

        //Draw east side:
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), tXe[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), tXb[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), tXb[tier], 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), tXe[tier], 0.0F);

        //Draw north side
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), tXe[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), tXb[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), tXb[tier], 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), tXe[tier], 0.0F);

        //Draw south side
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), tXb[tier], 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), tXe[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), tXb[tier], 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), tXe[tier], 0.0F);
        GL11.glEnd();
    }

    private void drawElectricConnector() {

        GL11.glBegin(GL11.GL_QUADS);
        Vector3fMax vector = new Vector3fMax(RenderHelper.pixel * 2, RenderHelper.pixel, RenderHelper.pixel * 12, RenderHelper.pixel * 14, RenderHelper.pixel * 14, 1.0F);
        //RenderHelper.drawTexturedCube(new Vector3fMax(0.2F, 0.1F, 0.8F, 0.8F, 0.8F, 1.0F));

        //Top side:
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), RenderHelper.bigRenderPixel * 29, RenderHelper.renderPixel * 29); //TR
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), RenderHelper.bigRenderPixel * 29, RenderHelper.renderPixel * 16);    //TL
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), RenderHelper.bigRenderPixel * 25, RenderHelper.renderPixel * 16);    //BL
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), RenderHelper.bigRenderPixel * 25, RenderHelper.renderPixel * 29); //BR

        //Draw west side:
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), RenderHelper.bigRenderPixel * 29, RenderHelper.renderPixel * 29); //BR
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), RenderHelper.bigRenderPixel * 29, RenderHelper.renderPixel * 16); //TR
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), RenderHelper.bigRenderPixel * 25, RenderHelper.renderPixel * 16); //TL
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), RenderHelper.bigRenderPixel * 25, RenderHelper.renderPixel * 29); //BL

        //Draw east side:
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), RenderHelper.bigRenderPixel * 29, RenderHelper.renderPixel * 29); //BR
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), RenderHelper.bigRenderPixel * 29, RenderHelper.renderPixel * 16); //TR
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), RenderHelper.bigRenderPixel * 25, RenderHelper.renderPixel * 16); //TL
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), RenderHelper.bigRenderPixel * 25, RenderHelper.renderPixel * 29); //BL

        //Draw north side
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), RenderHelper.bigRenderPixel * 25, RenderHelper.renderPixel * 29); //BR
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), RenderHelper.bigRenderPixel * 25, RenderHelper.renderPixel * 16); //TR
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), RenderHelper.bigRenderPixel * 13, RenderHelper.renderPixel * 16); //TL
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), RenderHelper.bigRenderPixel * 13, RenderHelper.renderPixel * 29); //BL

        //Draw south side
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), RenderHelper.bigRenderPixel * 43, RenderHelper.renderPixel * 13); //BR
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), RenderHelper.bigRenderPixel * 43, RenderHelper.renderPixel * 0); //TR
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), RenderHelper.bigRenderPixel * 31, RenderHelper.renderPixel * 0); //TL
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), RenderHelper.bigRenderPixel * 31, RenderHelper.renderPixel * 13); //BL
        GL11.glEnd();
    }

    private void drawHydraulicsConnector() {

        GL11.glBegin(GL11.GL_QUADS);
        //Connector for Hydraulics
        Vector3fMax vector = new Vector3fMax(RenderHelper.pixel * 2, RenderHelper.pixel, 0.0F, RenderHelper.pixel * 14, RenderHelper.pixel * 14, RenderHelper.pixel * 4);
        //RenderHelper.drawColoredCube(vector);

        //Top side:
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), RenderHelper.bigRenderPixel * 29, RenderHelper.renderPixel * 29); //TR
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), RenderHelper.bigRenderPixel * 29, RenderHelper.renderPixel * 16);    //TL
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), RenderHelper.bigRenderPixel * 25, RenderHelper.renderPixel * 16);    //BL
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), RenderHelper.bigRenderPixel * 25, RenderHelper.renderPixel * 29); //BR

        //Draw west side:
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), RenderHelper.bigRenderPixel * 29, RenderHelper.renderPixel * 29); //BR
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), RenderHelper.bigRenderPixel * 29, RenderHelper.renderPixel * 16); //TR
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), RenderHelper.bigRenderPixel * 25, RenderHelper.renderPixel * 16); //TL
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), RenderHelper.bigRenderPixel * 25, RenderHelper.renderPixel * 29); //BL

        //Draw east side:
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), RenderHelper.bigRenderPixel * 29, RenderHelper.renderPixel * 29); //BR
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), RenderHelper.bigRenderPixel * 29, RenderHelper.renderPixel * 16); //TR
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), RenderHelper.bigRenderPixel * 25, RenderHelper.renderPixel * 16); //TL
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), RenderHelper.bigRenderPixel * 25, RenderHelper.renderPixel * 29); //BL

        //Draw north side
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), RenderHelper.bigRenderPixel * 31, RenderHelper.renderPixel * 13); //BR
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), RenderHelper.bigRenderPixel * 31, RenderHelper.renderPixel * 0); //TR
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), RenderHelper.bigRenderPixel * 19, RenderHelper.renderPixel * 0); //TL
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), RenderHelper.bigRenderPixel * 19, RenderHelper.renderPixel * 13); //BL

        //Draw south side
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), RenderHelper.bigRenderPixel * 25, RenderHelper.renderPixel * 29); //BR
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), RenderHelper.bigRenderPixel * 25, RenderHelper.renderPixel * 16); //TR
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), RenderHelper.bigRenderPixel * 13, RenderHelper.renderPixel * 16); //TL
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), RenderHelper.bigRenderPixel * 13, RenderHelper.renderPixel * 29); //BL

        GL11.glEnd();
    }


    private void drawElectricBlock(TileElectricPump t, boolean isItem) {

        GL11.glPushMatrix();

        if (!isItem) {
            if (t.getIsRunning()) {
                GL11.glColor3f(1.0F, t.getRenderingPercentage(), t.getRenderingPercentage());
            }
        }

        float sideXb = 0.0F;
        float sideXe = RenderHelper.bigRenderPixel * 4;
        float sideYb = RenderHelper.renderPixel * 24;
        float sideYe = RenderHelper.renderPixel * 27;


        GL11.glTranslatef(0.0F, 0.0F, 0.5F);
        RenderHelper.draw2DCircle(0.5F, 0.5F, RenderHelper.pixel * 3);

        GL11.glTranslatef(0.0F, 1.0F, -0.5F);
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        RenderHelper.drawCylinder(0.5F, 0.5F, 0.5F, RenderHelper.pixel * 3, RenderHelper.pixel * 4, sideXb, sideYb, sideXe, sideYe);

        GL11.glPopMatrix();

        if (!isItem) {
            if (t.getIsRunning()) {
                GL11.glColor3f(0.9f, 0.9f, 0.9f);
            }
        }
    }

    private void drawHydraulicsTank(TileElectricPump t, boolean isItem) {

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Vector3fMax vectorFilled = new Vector3fMax(0.001F + RenderHelper.pixel * 4, 0.001F + RenderHelper.pixel * 4, 0.001F + RenderHelper.pixel * 4, -0.001F + RenderHelper.pixel * 12, -0.001F + RenderHelper.pixel * 12, -0.001F + RenderHelper.pixel * 8);
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


        GL11.glBegin(GL11.GL_QUADS);
        GL11.glColor4f(0.9F, 0.9F, 0.9F, 1.0F);
        //Block Hydraulics
        Vector3fMax vector = new Vector3fMax(RenderHelper.pixel * 5, RenderHelper.pixel * 5, RenderHelper.pixel * 4, RenderHelper.pixel * 11, RenderHelper.pixel * 11, RenderHelper.pixel * 8);
        //RenderHelper.drawColoredCube(new Vector3fMax(0.3F, 0.3F, 0.2F, 0.7F, 0.7F, 0.5F));

        float sideXb = RenderHelper.bigRenderPixel * 8;
        float sideXe = RenderHelper.bigRenderPixel * 13;
        float sideYb = RenderHelper.renderPixel * 16;
        float sideYe = RenderHelper.renderPixel * 22;
        //Top side:
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), sideXe, sideYb);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), sideXe, sideYe);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), sideXb, sideYe);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), sideXb, sideYb);

        //Bottom side:
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), sideXe, sideYb);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), sideXe, sideYe);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), sideXb, sideYe);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), sideXb, sideYb);

        //Draw west side:
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), sideXe, sideYb);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), sideXe, sideYe);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), sideXb, sideYe);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), sideXb, sideYb);

        //Draw east side:
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), sideXe, sideYb);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), sideXe, sideYe);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), sideXb, sideYe);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), sideXb, sideYb);

        //Draw north side
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), sideXb, sideYb);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), sideXb, sideYe);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.0F, sideYe);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.0F, sideYb);

        //Draw south side
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.0F, sideYb);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), sideXb, sideYb);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), sideXb, sideYe);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.0F, sideYe);

        GL11.glEnd();

        GL11.glDisable(GL11.GL_BLEND);
    }

}
