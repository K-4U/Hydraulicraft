package k4unl.minecraft.Hydraulicraft.client.renderers.misc;

import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicFluidPump;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by K-4U on 21-5-2015.
 */
public class RendererArchimedesScrew extends TileEntitySpecialRenderer {

    private static final ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/model/");

    @Override
    public void renderTileEntityAt(TileEntity ent, double x, double y, double z, float f, int destroyStage) {
        doRender((TileHydraulicFluidPump) ent, x, y, z, f, destroyStage);
    }

    public static void doRender(TileHydraulicFluidPump tileentity, double x, double y,
                                double z, float f, int destroyStage) {
        GL11.glPushMatrix();

        GL11.glTranslatef((float) x, (float) y, (float) z);

        //Get metadata for rotation:
        if (tileentity != null) {
            switch (tileentity.getFacing()) {
                case WEST:
                    GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
                    GL11.glTranslatef(-1.0F, 0.0F, -1.0F);
                    break;
                case SOUTH:
                    GL11.glRotatef(-90F, 0.0F, 1.0F, 0F);
                    GL11.glTranslatef(0.0F, 0.0F, -1.0F);
                    break;
                case NORTH:
                    GL11.glRotatef(90F, 0.0F, 1.0F, 0F);
                    GL11.glTranslatef(-1.0F, 0.0F, 0.0F);
                    break;
                default:
                    break;
            }
        }

        //FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);
        //GL11.glColor3f(0.8F, 0.8F, 0.8F);

        GL11.glPushMatrix();

        //Do rendering
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        GL11.glTranslated(0.8, 0.5, 0.5);
        GL11.glRotatef(116F, 0.0F, 0.0F, -1.0F);

        renderHelix(tileentity, f);

        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();

    }

    public static void renderHelix(TileHydraulicFluidPump pump, float f) {
        int slices = 80;
        double ri = 0.15;
        double ro = 0.3;
        double height = 2.0;
        double thickness = 1.0 / 16.0;
        int rev = 8;

        GL11.glPushMatrix();
        GL11.glRotated(-360F * pump.getRotational(f), 0.0F, 1.0F, 0.0F);
        GL11.glBegin(GL11.GL_QUADS);
        for (int s = 0; s < slices; s++) {
            double t1 = (2 * Math.PI * rev) / slices * s;
            double t2 = (2 * Math.PI * rev) / slices * (s + 1);
            double h1 = (1.0 * height / slices) * s;
            double h2 = (1.0 * height / slices) * (s + 1);
            /* Bottom */
            GL11.glColor3f(86F / 255F, 92F / 255F, 92F / 255F);
            GL11.glVertex3d(Math.cos(t1) * ri, h1, Math.sin(t1) * ri);
            GL11.glVertex3d(Math.cos(t1) * ro, h1, Math.sin(t1) * ro);
            GL11.glVertex3d(Math.cos(t2) * ro, h2, Math.sin(t2) * ro);
            GL11.glVertex3d(Math.cos(t2) * ri, h2, Math.sin(t2) * ri);
            /* Top */
            GL11.glVertex3d(Math.cos(t2) * ri, h2 + thickness, Math.sin(t2) * ri);
            GL11.glVertex3d(Math.cos(t2) * ro, h2 + thickness, Math.sin(t2) * ro);
            GL11.glVertex3d(Math.cos(t1) * ro, h1 + thickness, Math.sin(t1) * ro);
            GL11.glVertex3d(Math.cos(t1) * ri, h1 + thickness, Math.sin(t1) * ri);
            /* Side */
            GL11.glColor3f(119F / 255F, 119F / 255F, 119F / 255F);
            GL11.glVertex3d(Math.cos(t1) * ro, h1 + thickness, Math.sin(t1) * ro);
            GL11.glVertex3d(Math.cos(t2) * ro, h2 + thickness, Math.sin(t2) * ro);
            GL11.glVertex3d(Math.cos(t2) * ro, h2, Math.sin(t2) * ro);
            GL11.glVertex3d(Math.cos(t1) * ro, h1, Math.sin(t1) * ro);
        }
        GL11.glEnd();

        /* Cylinder */
        GL11.glColor3f(0.5F, 0.5F, 0.5F);
        GL11.glBegin(GL11.GL_QUADS);
        for (int s = 0; s < slices; s++) {
            double t1 = (2 * Math.PI) / slices * s;
            double t2 = (2 * Math.PI) / slices * (s + 1);
            GL11.glVertex3d(Math.cos(t1) * ri, height, Math.sin(t1) * ri);
            GL11.glVertex3d(Math.cos(t2) * ri, height, Math.sin(t2) * ri);
            GL11.glVertex3d(Math.cos(t2) * ri, 0, Math.sin(t2) * ri);
            GL11.glVertex3d(Math.cos(t1) * ri, 0, Math.sin(t1) * ri);
        }
        GL11.glEnd();

        /* Outer Cylinder */
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(77F / 255F, 216F / 255F, 1.0F, 0.3F);
        //GL11.glColor3f(0.5F, 0.5F, 0.5F);
        GL11.glBegin(GL11.GL_QUADS);
        for (int s = 0; s < slices; s++) {
            double t1 = (2 * Math.PI) / slices * s;
            double t2 = (2 * Math.PI) / slices * (s + 1);
            GL11.glVertex3d(Math.cos(t1) * ro, height, Math.sin(t1) * ro);
            GL11.glVertex3d(Math.cos(t2) * ro, height, Math.sin(t2) * ro);
            GL11.glVertex3d(Math.cos(t2) * ro, 0, Math.sin(t2) * ro);
            GL11.glVertex3d(Math.cos(t1) * ro, 0, Math.sin(t1) * ro);

            GL11.glVertex3d(Math.cos(t1) * ro, height, Math.sin(t1) * ro);
            GL11.glVertex3d(Math.cos(t2) * ro, height, Math.sin(t2) * ro);
            GL11.glVertex3d(Math.cos(t2) * ro, 0, Math.sin(t2) * ro);
            GL11.glVertex3d(Math.cos(t1) * ro, 0, Math.sin(t1) * ro);
        }
        GL11.glEnd();
        GL11.glPopMatrix();
    }
}
