package k4unl.minecraft.Hydraulicraft.client.renderers.transportation;

import k4unl.minecraft.k4lib.client.RenderHelper;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

/**
 * @author Koen Beckers (K-4U)
 */
public class RendererPartFluidInterface extends TileEntitySpecialRenderer {

    public void doRender(double x, double y, double z, float f, EnumFacing side, int destroyStage){
        GL11.glPushMatrix();

        GL11.glTranslatef((float) x, (float) y, (float)z);


        if(side == null){
            side = EnumFacing.EAST;
        }
        //Bind texture
        //FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);

        GL11.glColor3f(0.8F, 0.8F, 0.8F);
        GL11.glPushMatrix();

        GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
        GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
        GL11.glEnable(GL11.GL_BLEND);

        GL11.glAlphaFunc(GL11.GL_GEQUAL, 0.4F);
        //Do rendering

        float thickness = 2 * RenderHelper.pixel;
        float width = 8 * RenderHelper.pixel;
        float min = 0.5F - (width/2);
        float max = 0.5F + (width/2);
        float tMax = 1.0F - thickness;
        float tMin = 0.0F + thickness;
        Vector3fMax vector;
        if(side.equals(EnumFacing.UP)){
            vector = new Vector3fMax(min, tMax, min, max, 1.0F, max);
        }else if(side.equals(EnumFacing.DOWN)){
            vector = new Vector3fMax(min, 0.0F, min, max, tMin, max);
        }else if(side.equals(EnumFacing.NORTH)){
            vector = new Vector3fMax(min, min, 0.0F, max, max, tMin);
        }else if(side.equals(EnumFacing.SOUTH)){
            vector = new Vector3fMax(min, min, tMax, max, max, 1.0F);
        }else if(side.equals(EnumFacing.WEST)){
            vector = new Vector3fMax(0.0F, min, min, tMin, max, max);
        }else if(side.equals(EnumFacing.EAST)){
            vector = new Vector3fMax(tMax, min, min, 1.0F, max, max);
        }else{
            vector = new Vector3fMax(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

        GL11.glBegin(GL11.GL_QUADS);
        RenderHelper.drawColoredCube(vector);
        GL11.glEnd();

        //GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_, int destroyStage) {

    }
}
