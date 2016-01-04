package k4unl.minecraft.Hydraulicraft.client.renderers.misc;

import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileInterfaceValve;
import k4unl.minecraft.k4lib.client.RenderHelper;
import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.k4lib.lib.Location;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidTankInfo;
import org.lwjgl.opengl.GL11;

public class RendererInterfaceValve extends TileEntitySpecialRenderer {//implements ISimpleBlockRenderingHandler  {

    /*
        public static final int   RENDER_ID         = RenderingRegistry.getNextAvailableRenderId();
        public static final Block FAKE_RENDER_BLOCK = new Block(Material.rock) {

            @Override
            public IIcon getIcon(int meta, int side) {

                return currentBlockToRender.getIcon(meta, side);
            }
        };

        public static Block currentBlockToRender = Blocks.stone;

        @Override
        public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

            currentBlockToRender = block;
            renderer.renderBlockAsItem(FAKE_RENDER_BLOCK, 1, 1.0F);
        }

        @Override
        public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

            boolean ret = renderer.renderStandardBlock(block, x, y, z);

            int outerXDifference;
            int outerYDifference;
            int outerZDifference;
            TileInterfaceValve valve = (TileInterfaceValve)world.getTileEntity(x, y, z);
            //Log.info("Render: isTank = " + isValidTank);
            if(valve.isValidTank()) {
                Location corner1_out = new Location(valve.getTankCorner1());

                Location corner2_out = new Location(valve.getTankCorner2());
                corner2_out.addX(1);
                corner2_out.addY(1);
                corner2_out.addZ(1);

                outerXDifference = corner2_out.getX() - corner1_out.getX();
                outerYDifference = corner2_out.getY() - corner1_out.getY();
                outerZDifference = corner2_out.getZ() - corner1_out.getZ();

                //Log.info(corner2_out.printCoords());


                Tessellator.instance.addTranslation(corner1_out.getX(), corner1_out.getY(), corner1_out.getZ());
                GL11.glEnable(GL11.GL_BLEND);

                GL11.glAlphaFunc(GL11.GL_GEQUAL, 0.4F);
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);

                IIcon icon = IconSupplier.tankGrid;
                float u = icon.getMinU();
                float v = icon.getMinV();
                float U = icon.getMaxU();
                float V = icon.getMaxV();
                Tessellator tessellator = Tessellator.instance;

                for(int xR = 0; xR < outerXDifference; xR++){
                    for(int yR = 0; yR < outerYDifference; yR++) {
                        //Draw north side
                        tessellator.setNormal(0, 0, -1);
                        tessellator.addVertexWithUV(-0.002F + xR, -0.002F + yR, -0.002F, u, v);
                        tessellator.addVertexWithUV(-0.002F + xR, 0.002F + yR+1, -0.002F, u, V);
                        tessellator.addVertexWithUV(0.002F + xR + 1, 0.002F + yR+1, -0.002F, U, V);
                        tessellator.addVertexWithUV(0.002F + xR + 1, -0.002F + yR, -0.002F, U, v);

                        //Draw south side
                        tessellator.setNormal(0, 0, 1);
                        tessellator.addVertexWithUV(-0.002F + xR, -0.002F + yR, outerZDifference+0.002F, u, v);
                        tessellator.addVertexWithUV(0.002F + xR + 1, -0.002F + yR, outerZDifference+0.002F, u, V);
                        tessellator.addVertexWithUV(0.002F + xR + 1, 0.002F + yR + 1, outerZDifference+0.002F, U, V);
                        tessellator.addVertexWithUV(-0.002F + xR, 0.002F + yR + 1, outerZDifference+0.002F, U, v);
                    }
                }
                for(int zR = 0; zR < outerZDifference; zR++){
                    for(int yR = 0; yR < outerYDifference; yR++) {
                        //Draw west side:
                        tessellator.setNormal(-1, 0, 0);
                        tessellator.addVertexWithUV(-0.002F, -0.002F + yR, 0.002F + zR + 1, u, v);
                        tessellator.addVertexWithUV(-0.002F, 0.002F + yR + 1, 0.002F + zR + 1, u, V);
                        tessellator.addVertexWithUV(-0.002F, 0.002F + yR + 1, -0.002F + zR, U, V);
                        tessellator.addVertexWithUV(-0.002F, -0.002F + yR, -0.002F + zR, U, v);

                        //Draw east side:
                        tessellator.setNormal(1, 0, 0);
                        tessellator.addVertexWithUV(outerXDifference + 0.002F, -0.002F + yR, -0.002F + zR, u, v);
                        tessellator.addVertexWithUV(outerXDifference + 0.002F, 0.002F + yR + 1, -0.002F + zR, u, V);
                        tessellator.addVertexWithUV(outerXDifference + 0.002F, 0.002F + yR + 1, 0.002F + zR + 1, U, V);
                        tessellator.addVertexWithUV(outerXDifference + 0.002F, -0.002F + yR, 0.002F + zR + 1, U, v);
                    }
                }

                for(int zR = 0; zR < outerZDifference; zR++){
                    for(int xR = 0; xR < outerXDifference; xR++) {
                        //Top side
                        tessellator.setNormal(0, 1, 0);
                        tessellator.addVertexWithUV(-0.002F + xR, outerYDifference + 0.02F, 0.02F + zR + 1, u, v);
                        tessellator.addVertexWithUV(0.002F + xR + 1, outerYDifference + 0.02F, 0.02F + zR + 1, u, V);
                        tessellator.addVertexWithUV(0.002F + xR + 1, outerYDifference + 0.02F, -0.02F + zR, U, V);
                        tessellator.addVertexWithUV(-0.002F + xR, outerYDifference + 0.02F, -0.02F + zR, U, v);

                        //Bottom side
                        tessellator.setNormal(0, -1, 0);
                        tessellator.addVertexWithUV(0.002F + xR + 1, -0.002F, 0.002F + zR + 1, u, v);
                        tessellator.addVertexWithUV(-0.002F + xR, -0.002F, 0.002F + zR + 1, u, V);
                        tessellator.addVertexWithUV(-0.002F + xR, -0.002F, -0.002F + zR, U, V);
                        tessellator.addVertexWithUV(0.002F + xR + 1, -0.002F, -0.002F + zR, U, v);
                    }
                }

                //RenderHelper.drawTesselatedCubeWithTexture(new Vector3fMax(-0.02F, -0.02F, -0.02F, outerXDifference + 0.02F, outerYDifference + 0.02F, outerZDifference + 0.02F), IconSupplier.tankGrid);
                GL11.glDisable(GL11.GL_BLEND);
                Tessellator.instance.addTranslation(-corner1_out.getX(), -corner1_out.getY(), -corner1_out.getZ());
            }
            return ret;
        }

        @Override
        public boolean shouldRender3DInInventory(int modelId) {

            return true;
        }

        @Override
        public int getRenderId() {

            return RENDER_ID;
        }
    */
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
            Location corner1 = t.getTankCorner1();
            Location corner2 = t.getTankCorner2();

            int innerXDifference;
            int innerYDifference;
            int innerZDifference;
            int locationDifferenceX = t.getPos().getX() - corner1.getX();
            int locationDifferenceY = t.getPos().getY() - corner1.getY();
            int locationDifferenceZ = t.getPos().getZ() - corner1.getZ();


            innerXDifference = corner2.getX() - corner1.getX() - 1;
            innerYDifference = corner2.getY() - corner1.getY() - 1;
            innerZDifference = corner2.getZ() - corner1.getZ() - 1;

            GL11.glTranslatef(-locationDifferenceX, -locationDifferenceY, -locationDifferenceZ);

            //GL11.glColor3f(1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            //GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
            GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
            //GL11.glBegin(GL11.GL_QUADS);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper
                    .lightmapTexUnit, 240f, 240f);


            float percentage;
            if (tankInfo != null) {
                if (tankInfo.fluid != null) {
                    percentage = (float) tankInfo.fluid.amount / (float) tankInfo.capacity;
                    int height = (int) Math.floor((double) percentage *
                            (double) innerYDifference);

                    TextureAtlasSprite fluidIcon = Functions.getFluidIcon(tankInfo.fluid.getFluid());

                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    //Log.info(height + "");
                    boolean wasTessellating = RenderHelper.beginTesselatingWithTexture();
                    for (int xR = 1; xR <= innerXDifference; xR++) {
                        for (int zR = 1; zR <= innerZDifference; zR++) {
                            if (!tankInfo.fluid.getFluid().isGaseous()) {
                                boolean bottomRendered = false;
                                for (int zY = 1; zY <= height; zY++) {
                                    Vector3fMax insides = new Vector3fMax(xR,
                                            zY, zR,
                                            xR + 1, zY,
                                            zR + 1);
                                    insides.setYMax(zY + 0.99999F);

                                    if (zY == 1) {
                                        RenderHelper.drawTesselatedSideBottomWithTexture(insides, fluidIcon);
                                        bottomRendered = true;
                                    }

                                    if (zR == 1) {
                                        RenderHelper.drawTesselatedSideNorthWithTexture(insides, fluidIcon);
                                    }
                                    if (zR == innerZDifference) {
                                        RenderHelper.drawTesselatedSideSouthWithTexture(insides, fluidIcon);
                                    }
                                    if (zY == innerYDifference) {
                                        RenderHelper.drawTesselatedSideTopWithTexture(insides, fluidIcon);
                                    }
                                    if (xR == 1) {
                                        RenderHelper.drawTesselatedSideWestWithTexture(insides, fluidIcon);
                                    }
                                    if (xR == innerXDifference) {
                                        RenderHelper.drawTesselatedSideEastWithTexture(insides, fluidIcon);
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
                                        RenderHelper.drawTesselatedSideBottomWithTexture(insides, fluidIcon);
                                    }
                                    if (zR == 1) {
                                        RenderHelper.drawTesselatedSideNorthWithTexture(insides, fluidIcon);
                                    }
                                    if (zR == innerZDifference) {
                                        RenderHelper.drawTesselatedSideSouthWithTexture(insides, fluidIcon);
                                    }

                                    if (xR == 1) {
                                        RenderHelper.drawTesselatedSideWestWithTexture(insides, fluidIcon);
                                    }
                                    if (xR == innerXDifference) {
                                        RenderHelper.drawTesselatedSideEastWithTexture(insides, fluidIcon);
                                    }

                                    RenderHelper.drawTesselatedSideTopWithTexture(insides, fluidIcon);
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
                                        RenderHelper.drawTesselatedSideBottomWithTexture(insides, fluidIcon);
                                    }

                                    if (zY == innerYDifference) {
                                        RenderHelper.drawTesselatedSideTopWithTexture(insides, fluidIcon);
                                    }

                                    if (zR == 1) {
                                        RenderHelper.drawTesselatedSideNorthWithTexture(insides, fluidIcon);
                                    }
                                    if (zR == innerZDifference) {
                                        RenderHelper.drawTesselatedSideSouthWithTexture(insides, fluidIcon);
                                    }

                                    if (xR == 1) {
                                        RenderHelper.drawTesselatedSideWestWithTexture(insides, fluidIcon);
                                    }
                                    if (xR == innerXDifference) {
                                        RenderHelper.drawTesselatedSideEastWithTexture(insides, fluidIcon);
                                    }
                                }
                            }

                        }
                    }
                    RenderHelper.stopTesselating(wasTessellating);

                }
            }
            GL11.glDisable(GL11.GL_BLEND);

            //RenderHelper.drawColoredCube(insides);
            //GL11.glEnd();


        }

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}

