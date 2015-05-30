package k4unl.minecraft.Hydraulicraft.client.renderers.consumers.harvester;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import k4unl.minecraft.Hydraulicraft.client.models.ModelHarvesterFrame;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHarvesterFrame;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RendererHarvester implements ISimpleBlockRenderingHandler {
    private static final ResourceLocation resLoc =
            new ResourceLocation(ModInfo.LID,"textures/model/harvesterFrame_tmap.png");
    private ModelHarvesterFrame renderModel;

    public RendererHarvester(){
        renderModel = new ModelHarvesterFrame();
    }

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


    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        //Open the GL Matrix
        GL11.glPushMatrix();

        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float)z + 0.5F);

        if(tileEntity != null){
            TileHarvesterFrame frame = (TileHarvesterFrame) tileEntity;
            if(frame.getIsRotated()){
                GL11.glRotatef(180, 1F, 0F, 1F);
            }else{
                GL11.glRotatef(180, 0F, 0F, 1F);
            }
        }else{
            GL11.glRotatef(180, 0F, 0F, 1F);
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resLoc);

        GL11.glPushMatrix();

        renderModel.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();


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
