package k4unl.minecraft.Hydraulicraft.client.renderers.generators;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicPump;
import k4unl.minecraft.k4lib.client.RenderHelper;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class RendererHydraulicPump extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {

    private static final ResourceLocation resLoc =
            new ResourceLocation(ModInfo.LID, "textures/model/hydraulicPump.png");

    public static final int   RENDER_ID         = RenderingRegistry.getNextAvailableRenderId();
    public static final Block FAKE_RENDER_BLOCK = new Block(Material.rock) {

        @Override
        public IIcon getIcon(int meta, int side) {

            return HCBlocks.hydraulicPressureWall.getIcon(meta, side);
        }
    };


    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y,
                                   double z, float f) {

        TileHydraulicPump t = (TileHydraulicPump) tileentity;
        //Get metadata for rotation:
        int rotation = 0;//t.getDir();
        int metadata = t.getBlockMetadata();

        doRender(t, (float) x, (float) y, (float) z, f, rotation, metadata);
    }

    public void itemRender(float x, float y,
                           float z, float f, int tier) {

        GL11.glPushMatrix();

        GL11.glTranslatef(x, y, z);
        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);

        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 1.0F, 1.0F);
        GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(90F, 0.0F, 0.0F, -1.0F);
        //GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
        GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
        GL11.glColor3f(0.8F, 0.8F, 0.8F);
        //Do rendering

        float thickness = RenderHelper.pixel;
        renderTieredBars(tier, thickness);
        //renderInsidesWithoutLighting(thickness);
        renderGauges(thickness);

        //GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    public void doRender(TileHydraulicPump t, float x, float y,
			float z, float f, int rotation, int metadata){
		GL11.glPushMatrix();
		
		
		
		GL11.glTranslatef(x, y, z);
		FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);
		
		GL11.glPushMatrix();
		
		switch(t.getFacing()){
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
		case UNKNOWN:
		case UP:
		default:
			break;
		}
		
		//GL11.glDisable(GL11.GL_TEXTURE_2D); //Do not use textures
		GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
		//Do rendering
		GL11.glColor3f(0.9F, 0.9F, 0.9F);
		float thickness = RenderHelper.pixel;

		renderTieredBars(t.getTier(), thickness);
		//renderInsides(thickness, t);
		renderGaugesContents(thickness, t);
		renderGauges(thickness);
		
		GL11.glEnable(GL11.GL_LIGHTING); 
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
	
	private void renderInsidesWithoutLighting(float thickness){
		//thickness -= 0.025F;
		GL11.glBegin(GL11.GL_QUADS);
		Vector3fMax insides = new Vector3fMax(thickness, thickness, thickness, 1.0F-thickness, 1.0F-thickness, 1.0F-thickness);
		RenderHelper.drawTexturedCube(insides);
		GL11.glEnd();
	}
	
	private void renderGauges(float thickness){
		//thickness -= 0.025F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_QUADS);
		Vector3fMax vectorPressure = new Vector3fMax(1.0F - thickness - RenderHelper.pixel*6, 0.0F, thickness+RenderHelper.pixel, 1.0F - thickness - RenderHelper.pixel, 1.0002F-thickness, 1.0F - thickness - RenderHelper.pixel);
		float texturePixel = RenderHelper.renderPixel;

		RenderHelper.vertexWithTexture(vectorPressure.getXMin(), vectorPressure.getYMax(), vectorPressure.getZMax(), texturePixel*19, texturePixel*14); //BL
		RenderHelper.vertexWithTexture(vectorPressure.getXMax(), vectorPressure.getYMax(), vectorPressure.getZMax(), texturePixel*24, texturePixel*14);	//BR
		RenderHelper.vertexWithTexture(vectorPressure.getXMax(), vectorPressure.getYMax(), vectorPressure.getZMin(), texturePixel*24, 0.0F); //TR
		RenderHelper.vertexWithTexture(vectorPressure.getXMin(), vectorPressure.getYMax(), vectorPressure.getZMin(), texturePixel*19, 0.0F); //TL

		GL11.glEnd();
		GL11.glDisable(GL11.GL_BLEND);
		
	}
	@SuppressWarnings("cast")
	private void renderGaugesContents(float thickness, TileHydraulicPump t){
		//thickness -= 0.025F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		float a = (float)(Constants.COLOR_PRESSURE >> 24 & 255) / 255.0F;
        float r = (float)(Constants.COLOR_PRESSURE >> 16 & 255) / 255.0F;
        float g = (float)(Constants.COLOR_PRESSURE >> 8 & 255) / 255.0F;
        float b = (float)(Constants.COLOR_PRESSURE & 255) / 255.0F;
        GL11.glAlphaFunc(GL11.GL_EQUAL, a);
        GL11.glColor4f(r, g, b, a);

		Vector3fMax vectorPressure = new Vector3fMax(1.0F - thickness - RenderHelper.pixel*6, 0.0F, thickness+RenderHelper.pixel, 1.0F - thickness - RenderHelper.pixel, 1.0002F-thickness, 1.0F - thickness - RenderHelper.pixel);
		float h = vectorPressure.getZMax() - vectorPressure.getZMin();
		vectorPressure.setZMin(vectorPressure.getZMax() - (h * (t.getHandler().getPressure(ForgeDirection.UNKNOWN) / t.getMaxPressure(t.getHandler().isOilStored(), t.getFacing()))));
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3f(vectorPressure.getXMin(), vectorPressure.getYMax(), vectorPressure.getZMax()); //BL
		GL11.glVertex3f(vectorPressure.getXMax(), vectorPressure.getYMax(), vectorPressure.getZMax()); //BR
		GL11.glVertex3f(vectorPressure.getXMax(), vectorPressure.getYMax(), vectorPressure.getZMin()); //TR
		GL11.glVertex3f(vectorPressure.getXMin(), vectorPressure.getYMax(), vectorPressure.getZMin()); //TL
		GL11.glEnd();
		
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
	}
	
	private void renderInsides(float thickness, TileHydraulicPump t){
		//thickness -= 0.025F;
		GL11.glBegin(GL11.GL_QUADS);
		Vector3fMax insides = new Vector3fMax(thickness, thickness, thickness, 1.0F-thickness, 1.0F-thickness, 1.0F-thickness);	
		//RenderHelper.drawTexturedCubeWithLight(insides, t);
		
		GL11.glEnd();
	}	
	
	private void renderTieredBars(int tier, float thickness){
		Vector3fMax ln = new Vector3fMax(thickness, 0.0F, 0.0F, 1.0F-thickness, thickness, thickness);
		Vector3fMax tn = new Vector3fMax(thickness, 1.0F-thickness, 0.0F, 1.0F-thickness, 1.0F, thickness);
		Vector3fMax ne = new Vector3fMax(1.0F-thickness, 0.0F, 0.0F, 1.0F, 1.0F, thickness);
		
		for(int i = 0; i<4; i++){
			drawTieredHorizontalCube(ln, tier, thickness);
			drawTieredHorizontalCube(tn, tier, thickness);
			drawTieredVerticalCube(ne, tier, thickness);
			
			GL11.glTranslatef(1.0F, 0.0F, 0.0F);
			GL11.glRotatef(90.0F, 0.0F, -1.0F, 0.0F);
		}
	}

	private void drawTieredHorizontalCube(Vector3fMax vector, int tier, float thickness){
		GL11.glBegin(GL11.GL_QUADS);
		//RenderHelper.drawColoredCube(vector);
		float texturePixel = RenderHelper.renderPixel;
		float tXb[] = {texturePixel*16, texturePixel*17, texturePixel*18};
		float tXe[] = {texturePixel*17, texturePixel*18, texturePixel*19};
		tXe[tier] = tXb[tier] + (thickness/2);

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

	private void drawTieredVerticalCube(Vector3fMax vector, int tier, float thickness){
		GL11.glBegin(GL11.GL_QUADS);
		//RenderHelper.drawColoredCube(vector);
		float texturePixel = RenderHelper.renderPixel;
		float tXb[] = {texturePixel*16, texturePixel*17, texturePixel*18};
		float tXe[] = {texturePixel*17, texturePixel*18, texturePixel*19};
		tXe[tier] = tXb[tier] + (thickness/2);

		//Top side:
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMax(), tXe[tier], 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMax(), tXe[tier], (thickness/2));
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMax(), vector.getZMin(), tXb[tier], (thickness/2));
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMax(), vector.getZMin(), tXb[tier], 0.0F);

		//Bottom side:
		RenderHelper.vertexWithTexture(vector.getXMax(), vector.getYMin(), vector.getZMax(), tXe[tier], 0.0F);
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMax(), tXe[tier], (thickness/2));
		RenderHelper.vertexWithTexture(vector.getXMin(), vector.getYMin(), vector.getZMin(), tXb[tier], (thickness/2));
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

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        FAKE_RENDER_BLOCK.setBlockBounds(RenderHelper.pixel, RenderHelper.pixel, RenderHelper.pixel, 1.0F - RenderHelper.pixel, 1.0F - RenderHelper.pixel, 1.0F - RenderHelper.pixel);
        renderer.renderBlockAsItem(FAKE_RENDER_BLOCK, 1, 1.0F);
        GL11.glRotatef(90F, 0.0F, -1.0F, 0.0F);
        itemRender(-0.5F, -0.5F, -0.5F, 0, metadata);
        renderer.setRenderBoundsFromBlock(Blocks.stone);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

        renderer.setRenderBounds(RenderHelper.pixel, RenderHelper.pixel, RenderHelper.pixel, 1.0F-RenderHelper.pixel, 1.0F-RenderHelper.pixel, 1.0F-RenderHelper.pixel);
        boolean ret = renderer.renderStandardBlock(FAKE_RENDER_BLOCK, x, y, z);
        renderer.setRenderBoundsFromBlock(Blocks.stone);

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
}
