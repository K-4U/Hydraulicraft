package k4unl.minecraft.Hydraulicraft.client.GUI;


import static net.minecraftforge.client.IItemRenderer.ItemRenderType.INVENTORY;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


/**
 * Utility class for more advanced icon rendering
 *
 * @author Vswe
 */
@SideOnly(Side.CLIENT)
public final class IconRenderer {
    private static ResourceLocation iconTexture =  TextureMap.locationItemsTexture; //new ResourceLocation("textures/atlas/items.png");
    private static ResourceLocation blockTexture = TextureMap.locationBlocksTexture; //  new ResourceLocation("textures/atlas/blocks.png");

    private static RenderBlocks renderBlocks = new RenderBlocks();
    private static int curOnTime;
    private static final int TIME_PERIOD = 10;

    /**
     * Renders a merged icon from the given items. By increasing the transformation from 0 to 1 this will make the first
     * item graphically transform into the second one.
     * @param x the x coordinate of the result
     * @param y the y coordinate of the result
     * @param z the z coordinate of the result, this should most likely be the z level of your gui
     *          (this.zLevel when called)
     * @param recipeItem the starting item that will be graphically transformed
     * @param resultItem the result item that the first item will graphically transform into
     * @param transformation the rendering split of the transformation. Set to 0 this will only render the first item.
     *                       Set to 1 this will only render the second item. Making it gradually increase from 0 to 1
     *                       will make the first item graphically transform into the second one
     * @param wobble whether the result should wobble or not, when set to true an animating result which constantly is
     *               changing due to changes in transformation will wobble. If set to false the transformation will
     *               be smooth
     */
    public static void drawMergedIcon(int x, int y, float z, ItemStack recipeItem, ItemStack resultItem, float transformation, boolean wobble) {
        //enable blend to be able to draw the different alpha values
        GL11.glEnable(GL11.GL_BLEND);
        //bind the texture for the icons
        

        //Fetch the icons from the items, or null if we don't have any items.
        //Null icons will be prevented from being rendered
        IIcon recipeIcon = recipeItem == null ? null : recipeItem.getIconIndex();
        IIcon resultIcon = resultItem == null ? null : resultItem.getIconIndex();

        //calculate the alpha values, the first one decreases with the transformation and the other increases
        //these alpha values will end respectively star below 0. This is to prevent them from being rendered in the
        //very edges of the transformation
        float alphaOffset = transformation * 1.5F;
        float alpha1 = 1.25F - alphaOffset;
        float alpha2 = -0.25F + alphaOffset;
        //TODO: FIX ME
        //TODO: OH MY GOD FUCKING FIX ME
/*
        //draw the icons, the size of the icons is alpha + 0.2F
        Block recipeBlock = recipeId < Block.blocksList.length ? Block.blocksList[recipeId] : null;
        Block resultBlock = resultId < Block.blocksList.length ? Block.blocksList[resultId] : null;
        if(recipeBlock != null && recipeBlock.blockID == 0){
        	recipeBlock = null;
        }
        if(resultBlock != null && resultBlock.blockID == 0){
        	resultBlock = null;
        }

        
        if (resultItem.getItemSpriteNumber() == 0 && resultBlock != null && RenderBlocks.renderItemIn3d(Block.blocksList[resultId].getRenderType())){
        	Minecraft.getMinecraft().getTextureManager().bindTexture(blockTexture);
        }else{
        	Minecraft.getMinecraft().getTextureManager().bindTexture(iconTexture);        	
        }
        
        drawIcon(x, y, z, resultIcon, 16, 16, alpha2 + 0.2F, alpha2, wobble, resultBlock, resultItem.getItemDamage(), resultItem, false);

        
        if (recipeItem.getItemSpriteNumber() == 0 && recipeBlock != null && RenderBlocks.renderItemIn3d(Block.blocksList[recipeId].getRenderType())){
        	Minecraft.getMinecraft().getTextureManager().bindTexture(blockTexture);
        }else{
        	Minecraft.getMinecraft().getTextureManager().bindTexture(iconTexture);        	
        }
        
        drawIcon(x, y, z, recipeIcon, 16, 16, alpha1 + 0.2F, alpha1, wobble, recipeBlock, recipeItem.getItemDamage(), recipeItem, true);
        
*/
        GL11.glDisable(GL11.GL_BLEND);
    }

    /**
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
    public static void drawIcon(int x, int y, float z, IIcon icon, int w, int h, float size, float alpha, boolean wobble, Block isBlock, int itemDamage, ItemStack item, boolean opposite) {
        //without an alpha size or an icon we have nothing to render
        if (alpha <= 0 || size <= 0) {
            return;
        }
        
        boolean hasCustomRenderer = MinecraftForgeClient.getItemRenderer(item, INVENTORY) != null;
        if(hasCustomRenderer){
            if(++curOnTime > TIME_PERIOD) curOnTime = 0;
            int tempOnTime = opposite ? TIME_PERIOD - curOnTime : curOnTime; 
            if(!opposite || (float)tempOnTime / TIME_PERIOD < alpha){
                GL11.glColor4f(1F, 1F, 1F, alpha);
                GL11.glEnable(GL11.GL_LIGHTING);
                ForgeHooksClient.renderInventoryItem(renderBlocks,FMLClientHandler.instance().getClient().getTextureManager(), item, true, z, x, y);
                GL11.glDisable(GL11.GL_LIGHTING);
            }
        }
        else if(icon != null){

            //cap the alpha and size values
            if (alpha > 1) {
                alpha = 1;
            }
            if (size > 1) {
                size = 1;
            }
    
            //set the alpha value for the rendering
            GL11.glColor4f(1F, 1F, 1F, alpha);
    
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
            if (wobble) {
                targetLeft = (float)Math.floor(targetLeft);
                targetRight = (float)Math.floor(targetRight);
                targetTop = (float)Math.floor(targetTop);
                targetBot = (float)Math.floor(targetBot);
            }
    
            //calculate the size of the source bounds
            float sourceWidthMargin = (icon.getMaxU() - icon.getMinU()) * (1 - size) / 2;
            float sourceHeightMargin = (icon.getMaxV() - icon.getMinV()) * (1 - size) / 2;
    
            //calculate the source bounds
            float sourceLeft = icon.getMinU() + sourceWidthMargin;
            float sourceRight = icon.getMaxU() - sourceWidthMargin;
            float sourceTop = icon.getMinV() + sourceHeightMargin;
            float sourceBot = icon.getMaxV() - sourceHeightMargin;

       
            if(isBlock == null){
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
            }else if(isBlock != null){
            	renderBlock(isBlock, x, y, z, itemDamage, alpha, targetWidthMargin);
            }
        }
        //restore the alpha  value
        GL11.glColor4f(1F, 1F, 1F, 1F);
    }
    
    public static void renderBlock(Block block, int x, int y, float z, int itemDamage, float alpha, float targetWidthMargin){
    	//GL11.glEnable(GL11.GL_BLEND);
    	
    	GL11.glPushMatrix();
    	GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glTranslatef(x - 2, y + 3, -4.0F + z);
        GL11.glScalef(10.0F, 10.0F, 10.0F);
        GL11.glTranslatef(1.0F, 0.5F, 1.0F);
        GL11.glScalef(1.0F, 1.0F, -1.0F);
        GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);

        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glColor4f(1F, 1F, 1F, alpha);
        
        renderBlocks.useInventoryTint = false;
        renderBlocks.setRenderBounds(targetWidthMargin, targetWidthMargin, targetWidthMargin, 1-targetWidthMargin, 1-targetWidthMargin, 1-targetWidthMargin);
        renderBlocks.renderBlockAsItem(block, itemDamage, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
        //GL11.glDisable(GL11.GL_BLEND);
        
    }

    private IconRenderer() {}
}
