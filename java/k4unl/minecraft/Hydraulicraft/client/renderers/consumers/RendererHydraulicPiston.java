package k4unl.minecraft.Hydraulicraft.client.renderers.consumers;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicPiston;
import k4unl.minecraft.k4lib.client.RenderHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RendererHydraulicPiston extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {

    public static final int   RENDER_ID         = RenderingRegistry.getNextAvailableRenderId();
    public static final Block FAKE_RENDER_BLOCK = new Block(Material.rock) {

        @Override
        public IIcon getIcon(int meta, int side) {

            return HCBlocks.hydraulicPiston.getIcon(meta, side);
        }
    };

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

        renderer.renderBlockAsItem(FAKE_RENDER_BLOCK, 1, 1.0F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

        return renderer.renderStandardBlock(FAKE_RENDER_BLOCK, x, y, z);
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {

        return true;
    }

    @Override
    public int getRenderId() {

        return RENDER_ID;
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y,
      double z, float f) {

        int metadata = tileentity.getBlockMetadata();
        doRender((TileHydraulicPiston) tileentity, x, y, z, f, metadata);
    }

    public static void doRender(TileHydraulicPiston tileentity, double x, double y,
	  double z, float f, int metadata) {

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

		GL11.glColor3f(0.8F, 0.8F, 0.8F);
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING); //Disregard lighting
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		drawPistonArm(tileentity, f);
		
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING); //Disregard lighting
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		
	}

	public static void drawPistonArm(TileHydraulicPiston tileentity, float f){
		float half = 1.0F - RenderHelper.pixel * 2;
		float totalLength = 0F;
		float maxLength = 1F;
		if(tileentity != null){
			totalLength = tileentity.getOldExtendedLength() + (tileentity.getExtendedLength() - tileentity.getOldExtendedLength()) * f;
			maxLength = tileentity.getMaxLength();
			if(tileentity.getIsHarvesterPart()){
				half = 1F;
				totalLength+=0.5F;
			}
		}
		
		float begin = half;
		float remainingPercentage = totalLength;
		float thickness = 0.15F;
		float maxThickness = 0.46F;
		float armLength = 0.81F;
		float thicknessChange = (maxThickness - thickness) / (maxLength / armLength);

		while(remainingPercentage > 0F && remainingPercentage < 200F){
			drawPistonArmPiece(thickness, begin, remainingPercentage + begin);
			remainingPercentage-=armLength;
			thickness+=thicknessChange;
		}
	}
	
	public static void drawPistonArmPiece(float thickness, float begin, float end){
		float armBeginCoord = 0.5F - (thickness / 2);
		float armEndCoord = 0.5F + (thickness / 2);
		float startCoord = begin;
		float endCoord = end;
		GL11.glPushMatrix();
		GL11.glColor3f(0.8F, 0.8F, 0.8F);
		
		//Draw TOP side.
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3f(startCoord, armEndCoord, armEndCoord);
		GL11.glVertex3f(endCoord, armEndCoord, armEndCoord);
		GL11.glVertex3f(endCoord, armEndCoord, armBeginCoord);
		GL11.glVertex3f(startCoord, armEndCoord, armBeginCoord);
		
		//Draw bottom side.
		GL11.glVertex3f(endCoord, armBeginCoord, armEndCoord);
		GL11.glVertex3f(startCoord, armBeginCoord, armEndCoord);
		GL11.glVertex3f(startCoord, armBeginCoord, armBeginCoord);
		GL11.glVertex3f(endCoord, armBeginCoord, armBeginCoord);
		
		//Draw right side:
		GL11.glVertex3f(startCoord, armBeginCoord, armBeginCoord); 
		GL11.glVertex3f(startCoord, armEndCoord, armBeginCoord); 
		GL11.glVertex3f(endCoord, armEndCoord, armBeginCoord); 
		GL11.glVertex3f(endCoord, armBeginCoord, armBeginCoord);
		
		//Draw left side:
		GL11.glVertex3f(startCoord, armBeginCoord, armEndCoord);
		GL11.glVertex3f(endCoord, armBeginCoord, armEndCoord);
		GL11.glVertex3f(endCoord, armEndCoord, armEndCoord);
		GL11.glVertex3f(startCoord, armEndCoord, armEndCoord);
		
		//Draw front side:
		//GL11.glColor3f(0.5F, 0.5F, 0.5F);
		GL11.glVertex3f(endCoord, armBeginCoord, armBeginCoord);
		GL11.glVertex3f(endCoord, armEndCoord, armBeginCoord);
		GL11.glVertex3f(endCoord, armEndCoord, armEndCoord);
		GL11.glVertex3f(endCoord, armBeginCoord, armEndCoord);
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}

}
