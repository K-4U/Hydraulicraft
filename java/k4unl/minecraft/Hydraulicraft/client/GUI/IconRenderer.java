package k4unl.minecraft.Hydraulicraft.client.GUI;


import k4unl.minecraft.k4lib.client.VertexTransformerTransparency;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.VertexBufferConsumer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;


/**
 * Utility class for more advanced icon rendering
 *
 * @author Vswe
 */
@SideOnly(Side.CLIENT)
public final class IconRenderer {

    //private static ResourceLocation iconTexture =  TextureMap.locationItemsTexture; //new ResourceLocation("textures/atlas/items.png");
    private static ResourceLocation blockTexture = TextureMap.LOCATION_BLOCKS_TEXTURE; //  new ResourceLocation("textures/atlas/blocks.png");

    //TODO: FIX ME
    private static RenderItem renderBlocks = Minecraft.getMinecraft().getRenderItem();
    private static int curOnTime;
    private static final int TIME_PERIOD = 10;

    /**
     * Renders a merged icon from the given items. By increasing the transformation from 0 to 1 this will make the first
     * item graphically transform into the second one.
     *
     * @param x              the x coordinate of the result
     * @param y              the y coordinate of the result
     * @param z              the z coordinate of the result, this should most likely be the z level of your gui
     *                       (this.zLevel when called)
     * @param recipeItem     the starting item that will be graphically transformed
     * @param resultItem     the result item that the first item will graphically transform into
     * @param transformation the rendering split of the transformation. Set to 0 this will only render the first item.
     *                       Set to 1 this will only render the second item. Making it gradually increase from 0 to 1
     *                       will make the first item graphically transform into the second one
     * @param wobble         whether the result should wobble or not, when set to true an animating result which constantly is
     *                       changing due to changes in transformation will wobble. If set to false the transformation will
     *                       be smooth
     */
    public static void drawMergedIcon(int x, int y, float z, ItemStack recipeItem, ItemStack resultItem, float transformation, boolean wobble) {
        //enable blend to be able to draw the different alpha values
        GL11.glEnable(GL11.GL_BLEND);
        //bind the texture for the icons
        

        //Fetch the icons from the items, or null if we don't have any items.

        //calculate the alpha values, the first one decreases with the transformation and the other increases
        //these alpha values will end respectively star below 0. This is to prevent them from being rendered in the
        //very edges of the transformation
        float alphaOffset = transformation * 1.5F;
        float alpha1 = 1.25F - alphaOffset;
        float alpha2 = -0.25F + alphaOffset;

        Block recipeBlock = Block.getBlockFromItem(recipeItem.getItem());
        Block resultBlock = Block.getBlockFromItem(resultItem.getItem());
        if (recipeBlock != null && recipeBlock instanceof BlockAir) {
            recipeBlock = null;
        }
        if (resultBlock != null && resultBlock instanceof BlockAir) {
            resultBlock = null;
        }


        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        
        drawIcon(x, y, z, 16, 16, alpha2 + 0.2F, alpha2, wobble, resultBlock, resultItem.getItemDamage(), resultItem, false);

        drawIcon(x, y, z, 16, 16, alpha1 + 0.2F, alpha1, wobble, recipeBlock, recipeItem.getItemDamage(), recipeItem, true);
        

        GL11.glDisable(GL11.GL_BLEND);
    }

    /*
     * Renders an icon with the given properties
     * @param x the x coordinate of the icon
     * @param y the y coordinate of the icon
     * @param z the z coordinate of the icon, this should most likely be the z level of your gui
     *          (this.zLevel when called)
     * @param icon the icon to be rendered, if this is null nothing will be rendered
     * @param w the full width of the target area
     * @param h the full height of the target area
     * @param size how big part of the target area that should be filled. 1 for the full area, 0 or lower won't
     *             render anything.
     * @param alpha the alpha value that should be used to render. 1 for completely visible, 0 or lower won't render
     *              anything. For this to work you need to enable blend (GL11.glEnable(GL11.GL_BLEND))
     * @param wobble whether the icon should wobble or not, when set to true an animating icon which constantly is
     *               changing size will wobble. If set to false the size change will be smooth
     * @param isBlock whether we need to render a block, or just an icon.
     * @param itemDamage TODO 
     */
    public static void drawIcon(int x, int y, float z, int w, int h, float size, float alpha, boolean wobble, Block isBlock, int itemDamage, ItemStack item, boolean opposite) {
        //without an alpha size or an icon we have nothing to render
        if (alpha <= 0 || size <= 0) {
            return;
        }
        
/*        boolean hasCustomRenderer = MinecraftForgeClient.getItemRenderer(item, INVENTORY) != null;
        if(hasCustomRenderer){
            if(++curOnTime > TIME_PERIOD) curOnTime = 0;
            int tempOnTime = opposite ? TIME_PERIOD - curOnTime : curOnTime; 
            if(!opposite || (float)tempOnTime / TIME_PERIOD < alpha){
                GL11.glColor4f(1F, 1F, 1F, alpha);
                GL11.glEnable(GL11.GL_LIGHTING);
                ForgeHooksClient.renderInventoryItem(renderBlocks, FMLClientHandler.instance().getClient().getTextureManager(), item, true, z, x, y);
                GL11.glDisable(GL11.GL_LIGHTING);
            }
        }
        else if(icon != null){
*/
        //GlStateManager.pushMatrix();
        //cap the alpha and size values
        if (alpha > 1) {
            alpha = 1;
        }
        if (size > 1) {
            size = 1;
        }


        //calculate the size of the target bounds
        float targetWidthMargin = w * (1 - size) / 2;
        float targetHeightMargin = h * (1 - size) / 2;

        //calculate teh target bounds
        float targetLeft = x + targetWidthMargin;
        float targetRight = x + w - targetWidthMargin;
        float targetTop = y + targetHeightMargin;
        float targetBot = y + h - targetHeightMargin;

        //if we want to make the animation wobble we turn the target bounds into integers. By doing this the inexact
        //target values and the exact source values won't match completely. This makes it wobble since how exact the
        //match is will differ. If the target bounds are close to integers already the match will still be fairly good
        //while target bounds that are far from their integer representation will make the match fairly. By constantly
        //moving between good and bad values will make it all wobble.
        /*if (wobble) {
            targetLeft = (float) Math.floor(targetLeft);
            targetRight = (float) Math.floor(targetRight);
            targetTop = (float) Math.floor(targetTop);
            targetBot = (float) Math.floor(targetBot);
        }*/

        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

        tessellator.getBuffer().setTranslation(x, y, z + 5);
        IBakedModel itemModel = renderBlocks.getItemModelMesher().getItemModel(item);
        VertexBuffer worldRenderer = Tessellator.getInstance().getBuffer();
        IVertexConsumer consumer = new VertexTransformerTransparency(new VertexBufferConsumer(worldRenderer), alpha);
        for (EnumFacing dir : EnumFacing.VALUES) {
            for (BakedQuad quad : itemModel.getQuads(null, dir, 0)) { // TODO check params?
                quad.pipe(consumer);
            }
        }
        tessellator.getBuffer().setTranslation(0, 0, 0);

        tessellator.draw();

/*
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_EQUAL, alpha);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //set the alpha value for the rendering
        GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);




        renderBlocks.setupGuiTransform(x, y, ibakedmodel.isGui3d());
        ibakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GUI);
        renderItem(item, ibakedmodel, alpha);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();*/
/*
        if (isBlock == null || isBlock instanceof BlockAir) {
            //render the icon with the given bounds. This is done in the same way an icon is normally being rendered by
            //the base gui. However, there's no method to be called that allows you to specify all these things.
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 1.0F);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(sourceLeft, sourceBot);
            GL11.glVertex3f(targetLeft, targetBot, z);

            GL11.glTexCoord2f(sourceRight, sourceBot);
            GL11.glVertex3f(targetRight, targetBot, z);

            GL11.glTexCoord2f(sourceRight, sourceTop);
            GL11.glVertex3f(targetRight, targetTop, z);

            GL11.glTexCoord2f(sourceLeft, sourceTop);
            GL11.glVertex3f(targetLeft, targetTop, z);
            GL11.glEnd();

            GL11.glTranslatef(0.0F, 0.0F, -1.0F);
            GL11.glPopMatrix();
        } else if (isBlock != null) {
            renderBlock(isBlock, x, y, z, itemDamage, alpha, targetWidthMargin);
        }*/
        //}
        //restore the alpha  value
        GL11.glColor4f(1F, 1F, 1F, 1F);
    }
}
