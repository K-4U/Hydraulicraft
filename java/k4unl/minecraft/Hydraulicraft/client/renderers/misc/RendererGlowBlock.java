package k4unl.minecraft.Hydraulicraft.client.renderers.misc;

/**
 * @author K-4U
 */
public class RendererGlowBlock { //implements ISimpleBlockRenderingHandler {
/*
    private static final ResourceLocation resLoc =
      new ResourceLocation(ModInfo.LID, "textures/model/");

    public static final Block FAKE_RENDER_BLOCK = new Block(Material.rock) {

        @Override
        public IIcon getIcon(int meta, int side) {

            return currentBlockToRender.getIcon(meta, side);
        }
    };

    public static       Block currentBlockToRender = Blocks.stone;
    public static final int   RENDER_ID            = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

        currentBlockToRender = block;
        renderer.renderBlockAsItem(FAKE_RENDER_BLOCK, 1, 1.0F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

        boolean ret = renderer.renderStandardBlock(block, x, y, z);

        if (block instanceof IGlowBlock) {

            IIcon icon = ((IGlowBlock) block).getGlowIcon();
            //Render it again here, with brightness 0xE000E0
            Tessellator t = Tessellator.instance;
            t.setBrightness(0xE000E0);
            t.setColorRGBA(255, 255, 255, 255);
            t.addTranslation(x, y, z);
            //OpenGlHelper.setLightmapTextureCoords(OpenGlHelper
             //       .lightmapTexUnit, 240f, 240f);
            RenderHelper.drawTesselatedCubeWithTexture(new Vector3fMax(-0.001F, -0.001F, -0.001F, 1.001F, 1.001F, 1.001F), icon);
            //t.setBrightness(0xFFFFFF);
            t.addTranslation(-x, -y, -z);
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
}
