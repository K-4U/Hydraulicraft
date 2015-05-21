package k4unl.minecraft.Hydraulicraft.client.renderers.misc;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicFluidPump;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

/**
 * Created by K-4U on 21-5-2015.
 */
public class RendererArchimedesScrew extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {

    private static final ResourceLocation resLoc =
            new ResourceLocation(ModInfo.LID,"textures/model/");

    public static final int   RENDER_ID         = RenderingRegistry.getNextAvailableRenderId();
    public static final Block FAKE_RENDER_BLOCK = new Block(Material.rock) {

        @Override
        public IIcon getIcon(int meta, int side) {

            return HCBlocks.blockHydraulicFluidPump.getIcon(meta, side);
        }
    };

    @Override
    public void renderTileEntityAt(TileEntity ent, double x, double y, double z, float f) {
        int metadata = ent.getBlockMetadata();
        doRender((TileHydraulicFluidPump) ent, x, y, z, f, metadata);
    }

    public static void doRender(TileHydraulicFluidPump tileentity , double x, double y,
                                double z, float f, int metadata){
        GL11.glPushMatrix();

        GL11.glTranslatef((float) x, (float) y, (float)z);

        //Get metadata for rotation:
        if(tileentity != null){
            switch(tileentity.getFacing()){
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
        GL11.glColor3f(0.8F, 0.8F, 0.8F);
        GL11.glPushMatrix();

        //Do rendering
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glTranslated(0.5, 1.0, 0.5);
        renderHelix(tileentity, f);


        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
        GL11.glPopMatrix();
        GL11.glPopMatrix();

    }

    public static void renderHelix(TileHydraulicFluidPump pump, float f){
        int slices = 40;
        double ri = 0.25;
        double ro = 0.5;
        double height = 1.0;
        double thickness = 0.1;
        int rev = 2;

        //GL11.glRotatef(f * 360, 0, 1, 0);
        GL11.glBegin(GL11.GL_QUADS);
        for (int s = 0; s < slices; s++)
        {
            double t1 = (2 * Math.PI * rev) / slices * s;
            double t2 = (2 * Math.PI * rev) / slices * (s + 1);
            double h1 = (1.0 * height / slices) * s;
            double h2 = (1.0 * height / slices) * (s + 1);
            /* Bottom */
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
            GL11.glVertex3d(Math.cos(t1) * ro, h1 + thickness, Math.sin(t1) * ro);
            GL11.glVertex3d(Math.cos(t2) * ro, h2 + thickness, Math.sin(t2) * ro);
            GL11.glVertex3d(Math.cos(t2) * ro, h2, Math.sin(t2) * ro);
            GL11.glVertex3d(Math.cos(t1) * ro, h1, Math.sin(t1) * ro);
        }
        /* Cylinder */
        GL11.glColor3f(1.0f, 0, 0);
        for (int s = 0; s < slices; s++)
        {
            double t1 = (2 * Math.PI) / slices * s;
            double t2 = (2 * Math.PI) / slices * (s + 1);
            GL11.glVertex3d(Math.cos(t1) * ri, 1, Math.sin(t1) * ri);
            GL11.glVertex3d(Math.cos(t2) * ri, 1, Math.sin(t2) * ri);
            GL11.glVertex3d(Math.cos(t2) * ri, 0, Math.sin(t2) * ri);
            GL11.glVertex3d(Math.cos(t1) * ri, 0, Math.sin(t1) * ri);
        }
        GL11.glEnd();
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        renderer.renderBlockAsItem(FAKE_RENDER_BLOCK, 1, 1.0F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

        boolean ret = renderer.renderStandardBlock(FAKE_RENDER_BLOCK, x, y, z);

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return RENDER_ID;
    }
}
