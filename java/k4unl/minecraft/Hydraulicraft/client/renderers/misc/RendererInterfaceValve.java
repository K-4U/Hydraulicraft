package k4unl.minecraft.Hydraulicraft.client.renderers.misc;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import k4unl.minecraft.Hydraulicraft.client.renderers.RenderHelper;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Location;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Vector3fMax;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileInterfaceValve;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RendererInterfaceValve implements ISimpleBlockRenderingHandler {

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
	    Tessellator tes = Tessellator.instance;



        boolean ret = renderer.renderStandardBlock(block, x, y, z);




	    int xDifference = 0;
	    int yDifference = 0;
	    int zDifference = 0;
	    TileInterfaceValve valve = (TileInterfaceValve)world.getTileEntity(x, y, z);
	    boolean isValidTank = valve.isValidTank();
	    Log.info("Render: isTank = " + isValidTank);
	    if(valve.isValidTank()) {
		    Location corner1 = valve.getTankCorner1();
		    Location corner2 = valve.getTankCorner2();
		    xDifference = corner2.getX() - corner1.getX();
		    yDifference = corner2.getY() - corner1.getY();
		    zDifference = corner2.getZ() - corner1.getZ();

		    Tessellator.instance.addTranslation(corner1.getX(), corner1.getY(), corner1.getZ());

		    RenderHelper.drawTesselatedCube(new Vector3fMax(-0.2F, -0.2F, -0.2F, 1.2F, 1.2F, 1.2F));

		    Tessellator.instance.addTranslation(-corner1.getX(), -corner1.getY(), -corner1.getZ());
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
}
