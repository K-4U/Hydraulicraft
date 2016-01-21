package k4unl.minecraft.Hydraulicraft.client.renderers.items;

import k4unl.minecraft.Hydraulicraft.client.renderers.misc.RendererJarOfDirt;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartItemModel;

import java.util.List;

public class ItemRendererJarOfDirt implements ISmartItemModel{

    private IBakedModel baseModel;
    private static RendererJarOfDirt rendererJarOfDirt = new RendererJarOfDirt();

    public ItemRendererJarOfDirt(IBakedModel baseModel) {
        this.baseModel = baseModel;
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack) {
        return null;
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing p_177551_1_) {
        return baseModel.getFaceQuads(p_177551_1_);
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return baseModel.getGeneralQuads();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return baseModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return baseModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return baseModel.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return baseModel.getItemCameraTransforms();
    }

    //implements IItemRenderer {
/*


    @Override public boolean handleRenderType(ItemStack item, ItemRenderType type) {

        return true;
    }

    @Override public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {

        return true;
    }

    @Override public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        switch (type) {
            case ENTITY:
                render(-0.5F, -0.55F, -0.5F, 1.0F);
                break;
            case EQUIPPED:
                render(-0.2F, 0.4F, 0.3F, 0.8F);
                break;
            case EQUIPPED_FIRST_PERSON:
                render(-1.0F, 1.0F, 0.4F, 0.7F);
                break;
            case FIRST_PERSON_MAP:
                break;
            case INVENTORY:
                render(0.0F, -0.3F, 0.0F, 0.8F);
                break;
            default:
                break;
        }
    }

    private void render(float x, float y, float z, float scale){
        GL11.glScalef(scale, scale, scale);
        rendererJarOfDirt.renderTileEntityAt(null, x, y, z, 0);
    }
    */
}
