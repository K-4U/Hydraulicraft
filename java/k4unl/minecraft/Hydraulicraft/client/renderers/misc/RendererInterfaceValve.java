package k4unl.minecraft.Hydraulicraft.client.renderers.misc;

import k4unl.minecraft.Hydraulicraft.client.renderers.IconSupplier;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileInterfaceValve;
import k4unl.minecraft.k4lib.client.RenderHelper;
import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.k4lib.lib.Location;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidTankInfo;
import org.lwjgl.opengl.GL11;

public class RendererInterfaceValve extends TileEntitySpecialRenderer {//implements ISimpleBlockRenderingHandler  {

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y,
      double z, float f, int destroyStage) {

        TileInterfaceValve t = (TileInterfaceValve) tileentity;
        //Get metadata for rotation:

        doRender(t, (float) x, (float) y, (float) z, f, destroyStage);
    }

    public void doRender(TileInterfaceValve t, float x, float y, float z, float f, int destroyStage) {

        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);
        //		FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);

        if (t.isValidTank()) {
            FluidTankInfo tankInfo = t.getTankInfo(EnumFacing.UP)[0];
            Location corner1 = new Location(t.getTankCorner1());
            Location corner2 = new Location(t.getTankCorner2());

            int innerXDifference;
            int innerYDifference;
            int innerZDifference;
            int locationDifferenceX = t.getPos().getX() - corner1.getX();
            int locationDifferenceY = t.getPos().getY() - corner1.getY();
            int locationDifferenceZ = t.getPos().getZ() - corner1.getZ();

            int outerXDifference;
            int outerYDifference;
            int outerZDifference;
            corner2.addX(1);
            corner2.addY(1);
            corner2.addZ(1);

            outerXDifference = corner2.getX() - corner1.getX();
            outerYDifference = corner2.getY() - corner1.getY();
            outerZDifference = corner2.getZ() - corner1.getZ();

            //Log.info(corner2_out.printCoords());

            GL11.glEnable(GL11.GL_BLEND);

            GL11.glTranslatef(-(locationDifferenceX), -(locationDifferenceY), -(locationDifferenceZ));
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            GL11.glAlphaFunc(GL11.GL_GEQUAL, 0.4F);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);

            TextureAtlasSprite icon = IconSupplier.tankGrid;
            float u = icon.getMinU();
            float v = icon.getMinV();
            float U = icon.getMaxU();
            float V = icon.getMaxV();
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            //GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
            //GL11.glBegin(GL11.GL_QUADS);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glBegin(GL11.GL_QUADS);
            for (int xR = 0; xR < outerXDifference; xR++) {
                for (int yR = 0; yR < outerYDifference; yR++) {
                    //Draw north side

                    RenderHelper.vertexWithTexture(-0.002F + xR, -0.002F + yR, -0.002F, u, v);
                    RenderHelper.vertexWithTexture(-0.002F + xR, 0.002F + yR + 1, -0.002F, u, V);
                    RenderHelper.vertexWithTexture(0.002F + xR + 1, 0.002F + yR + 1, -0.002F, U, V);
                    RenderHelper.vertexWithTexture(0.002F + xR + 1, -0.002F + yR, -0.002F, U, v);

                    //Draw south side
                    RenderHelper.vertexWithTexture(-0.002F + xR, -0.002F + yR, outerZDifference + 0.002F, u, v);
                    RenderHelper.vertexWithTexture(0.002F + xR + 1, -0.002F + yR, outerZDifference + 0.002F, u, V);
                    RenderHelper.vertexWithTexture(0.002F + xR + 1, 0.002F + yR + 1, outerZDifference + 0.002F, U, V);
                    RenderHelper.vertexWithTexture(-0.002F + xR, 0.002F + yR + 1, outerZDifference + 0.002F, U, v);
                }
            }
            for (int zR = 0; zR < outerZDifference; zR++) {
                for (int yR = 0; yR < outerYDifference; yR++) {
                    //Draw west side:
                    RenderHelper.vertexWithTexture(-0.002F, -0.002F + yR, 0.002F + zR + 1, u, v);
                    RenderHelper.vertexWithTexture(-0.002F, 0.002F + yR + 1, 0.002F + zR + 1, u, V);
                    RenderHelper.vertexWithTexture(-0.002F, 0.002F + yR + 1, -0.002F + zR, U, V);
                    RenderHelper.vertexWithTexture(-0.002F, -0.002F + yR, -0.002F + zR, U, v);

                    //Draw east side:
                    RenderHelper.vertexWithTexture(outerXDifference + 0.002F, -0.002F + yR, -0.002F + zR, u, v);
                    RenderHelper.vertexWithTexture(outerXDifference + 0.002F, 0.002F + yR + 1, -0.002F + zR, u, V);
                    RenderHelper.vertexWithTexture(outerXDifference + 0.002F, 0.002F + yR + 1, 0.002F + zR + 1, U, V);
                    RenderHelper.vertexWithTexture(outerXDifference + 0.002F, -0.002F + yR, 0.002F + zR + 1, U, v);
                }
            }

            for (int zR = 0; zR < outerZDifference; zR++) {
                for (int xR = 0; xR < outerXDifference; xR++) {
                    //Top side
                    RenderHelper.vertexWithTexture(-0.002F + xR, outerYDifference + 0.02F, 0.02F + zR + 1, u, v);
                    RenderHelper.vertexWithTexture(0.002F + xR + 1, outerYDifference + 0.02F, 0.02F + zR + 1, u, V);
                    RenderHelper.vertexWithTexture(0.002F + xR + 1, outerYDifference + 0.02F, -0.02F + zR, U, V);
                    RenderHelper.vertexWithTexture(-0.002F + xR, outerYDifference + 0.02F, -0.02F + zR, U, v);

                    //Bottom side
                    RenderHelper.vertexWithTexture(0.002F + xR + 1, -0.002F, 0.002F + zR + 1, u, v);
                    RenderHelper.vertexWithTexture(-0.002F + xR, -0.002F, 0.002F + zR + 1, u, V);
                    RenderHelper.vertexWithTexture(-0.002F + xR, -0.002F, -0.002F + zR, U, V);
                    RenderHelper.vertexWithTexture(0.002F + xR + 1, -0.002F, -0.002F + zR, U, v);
                }
            }



            /*********************
             *
             * DRAW FLUID
             *
             *********************/

            float percentage;
            if (tankInfo != null) {
                if (tankInfo.fluid != null) {
                    innerXDifference = corner2.getX() - corner1.getX() - 1;
                    innerYDifference = corner2.getY() - corner1.getY() - 1;
                    innerZDifference = corner2.getZ() - corner1.getZ() - 1;

                    percentage = (float) tankInfo.fluid.amount / (float) tankInfo.capacity;
                    int height = (int) Math.floor((double) percentage *
                      (double) innerYDifference);

                    TextureAtlasSprite fluidIcon = Functions.getFluidIcon(tankInfo.fluid.getFluid());

//                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    //Log.info(height + "");

                    for (int xR = 0; xR <= innerXDifference; xR++) {
                        for (int zR = 0; zR <= innerZDifference; zR++) {
                            if (!tankInfo.fluid.getFluid().isGaseous()) {
                                boolean bottomRendered = false;
                                for (int zY = 1; zY <= height; zY++) {
                                    Vector3fMax insides = new Vector3fMax(xR,
                                      zY, zR,
                                      xR + 1, zY,
                                      zR + 1);
                                    insides.setYMax(zY + 0.99999F);

                                    if (zY == 1) {
                                        RenderHelper.drawGL11SideBottomWithTexture(insides, fluidIcon);
                                        bottomRendered = true;
                                    }

                                    if (zR == 0) {
                                        RenderHelper.drawGL11SideNorthWithTexture(insides, fluidIcon);
                                    }
                                    if (zR == innerZDifference) {
                                        RenderHelper.drawGL11SideSouthWithTexture(insides, fluidIcon);
                                    }
                                    if (zY == innerYDifference) {
                                        RenderHelper.drawGL11SideTopWithTexture(insides, fluidIcon);
                                    }
                                    if (xR == 0) {
                                        RenderHelper.drawGL11SideWestWithTexture(insides, fluidIcon);
                                    }
                                    if (xR == innerXDifference) {
                                        RenderHelper.drawGL11SideEastWithTexture(insides, fluidIcon);
                                    }
                                    //RenderHelper.drawTesselatedCubeWithTexture(insides, fluidIcon);
                                }
                                //Log.info((percentage * (float)innerYDifference) % 1.0F + "");
                                //Log.info(innerYDifference + "");
                                //Log.info(percentage + "");
                                //Log.info(height + "");
                                if ((percentage * (float) innerYDifference) % 1.0F >= 0.01F) {
                                    Vector3fMax insides = new Vector3fMax(xR,
                                      height + 1, zR,
                                      xR + 1, 0,
                                      zR + 1);
                                    insides.setYMax(height + ((percentage * (float) innerYDifference) % 1.0F) + 1);

                                    if (!bottomRendered) {
                                        RenderHelper.drawGL11SideBottomWithTexture(insides, fluidIcon);
                                    }
                                    if (zR == 0) {
                                        insides.setZMin(zR + 0.001F);
                                        RenderHelper.drawGL11SideNorthWithTexture(insides, fluidIcon);
                                    }
                                    if (zR == innerZDifference) {
                                        insides.setZMax(zR + 1 - 0.001F);
                                        RenderHelper.drawGL11SideSouthWithTexture(insides, fluidIcon);
                                    }

                                    if (xR == 0) {
                                        insides.setXMin(0.001F);
                                        RenderHelper.drawGL11SideWestWithTexture(insides, fluidIcon);
                                    }
                                    if (xR == innerXDifference) {
                                        insides.setXMax(xR + 1 - 0.001F);
                                        RenderHelper.drawGL11SideEastWithTexture(insides, fluidIcon);
                                    }

                                    RenderHelper.drawGL11SideTopWithTexture(insides, fluidIcon);
                                }
                            } else {
                                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F + (percentage * 0.7F));
                                for (int zY = 1; zY <= innerYDifference; zY++) {
                                    Vector3fMax insides = new Vector3fMax(xR + 0.001F,
                                      zY + 0.001F, zR + 0.001F,
                                      xR - 0.001F + 1, -0.001F,
                                      zR - 0.001F + 1);
                                    insides.setYMax(zY - 0.001F + 1);
                                    if (zY == 1) {
                                        RenderHelper.drawGL11SideBottomWithTexture(insides, fluidIcon);
                                    }

                                    if (zY == innerYDifference) {
                                        RenderHelper.drawGL11SideTopWithTexture(insides, fluidIcon);
                                    }

                                    if (zR == 0) {
                                        insides.setZMin(zR - 0.001F);
                                        RenderHelper.drawGL11SideNorthWithTexture(insides, fluidIcon);
                                    }
                                    if (zR == innerZDifference) {
                                        RenderHelper.drawGL11SideSouthWithTexture(insides, fluidIcon);
                                    }

                                    if (xR == 0) {
                                        RenderHelper.drawGL11SideWestWithTexture(insides, fluidIcon);
                                    }
                                    if (xR == innerXDifference) {
                                        RenderHelper.drawGL11SideEastWithTexture(insides, fluidIcon);
                                    }
                                }
                            }

                        }
                    }
                }
            }
            GL11.glEnd();
            GL11.glDisable(GL11.GL_BLEND);

            //RenderHelper.drawColoredCube(insides);
            //GL11.glEnd();

        }

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}

