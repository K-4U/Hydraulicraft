package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.client.renderers;

import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileHydraulicDynamo;
import k4unl.minecraft.k4lib.client.RenderHelper;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

public class RendererHydraulicDynamo extends TileEntitySpecialRenderer {

    private static final ResourceLocation resLoc =
            new ResourceLocation(ModInfo.LID, "textures/model/hydraulicDynamo.png");

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f, int destroyStage) {

        TileHydraulicDynamo t = (TileHydraulicDynamo) tileentity;
        //Get metadata for rotation:
        int rotation = 0;//t.getDir();
        int metadata = t.getBlockMetadata();

        doRender(t, (float) x, (float) y, (float) z, f, rotation, metadata);
    }

    public void itemRender(float x, float y,
                           float z, float f) {

        GL11.glPushMatrix();

        GL11.glTranslatef(x, y, z);

        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);

        GL11.glPushMatrix();
        //GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
        GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
        //Do rendering
        GL11.glColor3f(0.8F, 0.8F, 0.8F);
        GL11.glBegin(GL11.GL_QUADS);
        drawBase();
        drawAxle();
        GL11.glEnd();

        drawMovingPart(f);


        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    public void doRender(TileHydraulicDynamo t, float x, float y,
                         float z, float f, int rotation, int metadata) {

        GL11.glPushMatrix();

        GL11.glTranslatef(x, y, z);

        switch (t.getFacing()) {
            case EAST:
                GL11.glTranslatef(0.0F, 1.0F, 1.0F);
                GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(90F, 0.0F, 0.0F, -1.0F);
                break;
            case NORTH:
                GL11.glTranslatef(0.0F, 0.0F, 1.0F);
                GL11.glRotatef(90F, -1.0F, 0.0F, 0.0F);
                //GL11.glRotatef(90F, 0.0F, 0.0F, 1.0F);
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


        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);

        GL11.glPushMatrix();
        //GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
        GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting

        //Do rendering
        GL11.glColor3f(0.8F, 0.8F, 0.8F);
        GL11.glBegin(GL11.GL_QUADS);

        drawBase();
        drawAxle();

        GL11.glEnd();

        drawMovingPart(t.getPercentageOfRender());


        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    private void drawBase() {

        float sideXb = 0.125F;
        float sideXe = 0.25F;

        Vector3fMax vector = new Vector3fMax(0.0F, 0.0F, 0.0F, 1.0F, RenderHelper.pixel * 4, 1.0F);
        //Top side:
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.0F, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.5F, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.5F, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.0F, 0.5F);

        //Bottom side:
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.0F, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.5F, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 0.5F, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.0F, 0.5F);

        //Draw west side:
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), sideXe, 1.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), sideXb, 1.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), sideXb, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), sideXe, 0.5F);

        //Draw east side:
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), sideXe, 1.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), sideXb, 1.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), sideXb, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), sideXe, 0.5F);

        //Draw north side
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), sideXe, 1.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), sideXb, 1.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), sideXb, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), sideXe, 0.5F);

        //Draw south side
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), sideXb, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), sideXb, 1.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), sideXe, 1.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), sideXe, 0.5F);

        vector = new Vector3fMax(0.0F, 0.5F, 0.0F, 1.0F, 0.5F + RenderHelper.pixel * 4, 1.0F);
        //Top side:
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.0F, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.5F, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.5F, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.0F, 0.5F);

        //Bottom side:
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.0F, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.5F, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 0.5F, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.0F, 0.5F);

        //Draw west side:
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), sideXe, 1.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), sideXb, 1.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), sideXb, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), sideXe, 0.5F);

        //Draw east side:
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), sideXe, 1.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), sideXb, 1.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), sideXb, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), sideXe, 0.5F);

        //Draw north side
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), sideXe, 1.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), sideXb, 1.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), sideXb, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), sideXe, 0.5F);

        //Draw south side
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), sideXb, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), sideXb, 1.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), sideXe, 1.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), sideXe, 0.5F);
    }

    private static void drawAxle() {

        float c = 0.5F;
        float w = RenderHelper.pixel * 8;
        float b = c - (w / 2);
        float e = c + (w / 2);
        float xTopb = RenderHelper.renderPixel * 24;
        float xTope = RenderHelper.renderPixel * 32;
        float yTope = RenderHelper.renderPixel * 8;
        float ySidee = RenderHelper.renderPixel * 12;

        Vector3fMax vector = new Vector3fMax(b, 0.25F, b, e, 1.0F, e);
        //Top side:
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), xTopb, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), xTope, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), xTope, yTope);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), xTopb, yTope);

        //Draw west side:
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), xTopb, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), xTopb, ySidee);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.5F, ySidee);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 0.5F, 0.0F);

        //Draw east side:
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), xTopb, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), xTopb, ySidee);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.5F, ySidee);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.5F, 0.0F);

        //Draw north side
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), xTopb, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), xTopb, ySidee);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.5F, ySidee);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.5F, 0.0F);

        //Draw south side
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.5F, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), xTopb, 0.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), xTopb, ySidee);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.5F, ySidee);
    }

    private void drawMovingPart(float p) {

        float base = RenderHelper.pixel * 4;
        float w = RenderHelper.pixel * 4;

        float width = RenderHelper.pixel * 14;
        float b = -(width / 2);
        float e = (width / 2);

        float sideXb = 0.0F;
        float sideXe = RenderHelper.renderPixel * 4;

        //Let's see if we can do this..
        GL11.glTranslatef(0.5F, 0.0F, 0.5F);
        GL11.glRotatef(360.0F * p, 0.0F, 1.0F, 0.0F);


        GL11.glBegin(GL11.GL_QUADS);
        Vector3fMax vector = new Vector3fMax(b, base, b, e, base + w, e);
        //Top side:
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), 0.25F, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), 0.75F, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), 0.75F, 1.0F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), 0.25F, 1.0F);

        //Bottom side:
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), 0.25F, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), 0.75F, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), 0.75F, 1.0F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), 0.25F, 1.0F);

        //Draw west side:
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), sideXe, RenderHelper.renderPixel * 30);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), sideXb, RenderHelper.renderPixel * 30);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), sideXb, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), sideXe, 0.5F);

        //Draw east side:
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), sideXe, RenderHelper.renderPixel * 30);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), sideXb, RenderHelper.renderPixel * 30);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), sideXb, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), sideXe, 0.5F);

        //Draw north side
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), sideXe, RenderHelper.renderPixel * 30);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), sideXb, RenderHelper.renderPixel * 30);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), sideXb, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMin(), sideXe, 0.5F);

        //Draw south side
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), sideXb, 0.5F);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), sideXb, RenderHelper.renderPixel * 30);
        RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), sideXe, RenderHelper.renderPixel * 30);
        RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), sideXe, 0.5F);

        GL11.glEnd();
    }

}
