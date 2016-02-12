package k4unl.minecraft.Hydraulicraft.thirdParty.rf.client.renderers;

import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.thirdParty.rf.tileEntities.TileRFPump;
import k4unl.minecraft.k4lib.client.RenderHelper;
import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

public class RendererRFPump extends TileEntitySpecialRenderer {

    private static final ResourceLocation resLoc =
            new ResourceLocation(ModInfo.LID, "textures/model/rfPump_low.png");

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f, int destroyStage) {

        TileRFPump t = (TileRFPump) tileentity;

        doRender(t, (float) x, (float) y, (float) z, f);
    }

    public void itemRender(float x, float y, float z, int tier) {

        GL11.glPushMatrix();

        GL11.glTranslatef(x, y, z);

        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);

        GL11.glPushMatrix();
        //GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
        GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
        //Do rendering

        drawTEBlock(null, true);
        drawHydraulicsTank(null, true);


        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    public void doRender(TileRFPump t, float x, float y, float z, float f) {

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
                break;
            case SOUTH:
                GL11.glTranslatef(1.0F, 0.0F, 1.0F);
                GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
                break;
            case NORTH:
            default:
                break;
        }


        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        GL11.glPushMatrix();
        //GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
        GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
        //Do rendering
        GL11.glColor3f(0.8F, 0.8F, 0.8F);

        //drawBase(t.getTier());
        //drawTEConnector();
        //drawHydraulicsConnector();
        //drawTEBlock(t, false);
        drawHydraulicsTank(t, false);


        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    private void drawTEBlock(TileRFPump t, boolean isItem) {

        GL11.glPushMatrix();

        if (!isItem) {
            if (t.getIsRunning()) {
                GL11.glColor3f(0.9f, 0.4f, 0.4f);
            }
        }

        GL11.glBegin(GL11.GL_QUADS);
        //Block T
        Vector3fMax vector = new Vector3fMax(RenderHelper.pixel * 4, RenderHelper.pixel * 4, 0.5F, RenderHelper.pixel * 12, RenderHelper.pixel * 12, RenderHelper.pixel * 12);
        //RenderHelper.drawColoredCube(new Vector3fMax(0.25F, 0.25F, 0.5F, 0.75F, 0.75F, 0.8F));

        float sideXb = RenderHelper.bigRenderPixel * 8;
        float sideXe = RenderHelper.bigRenderPixel * 13;
        float sideYb = RenderHelper.renderPixel * 24;
        float sideYe = 1.0F;

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

        GL11.glPopMatrix();

        if (!isItem) {
            if (t.getIsRunning()) {
                GL11.glColor3f(0.9f, 0.9f, 0.9f);
            }
        }
    }

    private void drawHydraulicsTank(TileRFPump t, boolean isItem) {
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Vector3fMax vectorFilled = new Vector3fMax(0.001F + RenderHelper.pixel * 5, 0.001F + RenderHelper.pixel * 5, 0.001F + RenderHelper.pixel * 4, -0.001F + RenderHelper.pixel * 11, -0.001F + RenderHelper.pixel * 11, -0.001F + RenderHelper.pixel * 8);
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
                GL11.glBegin(GL11.GL_QUADS);
                RenderHelper.drawGL11SideEastWithTexture(vectorFilled, fluidIcon);
                RenderHelper.drawGL11SideWestWithTexture(vectorFilled, fluidIcon);
                RenderHelper.drawGL11SideTopWithTexture(vectorFilled, fluidIcon);
                GL11.glEnd();
            }

            //Reset texture after using tesselators.
            //FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);
        }
    }

}
