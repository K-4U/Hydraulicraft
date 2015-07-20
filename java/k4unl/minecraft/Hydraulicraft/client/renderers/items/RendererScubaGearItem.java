package k4unl.minecraft.Hydraulicraft.client.renderers.items;

import k4unl.minecraft.Hydraulicraft.client.models.ModelDivingSuit;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class RendererScubaGearItem implements IItemRenderer {
    private static ModelDivingSuit modelDivingSuit = new ModelDivingSuit();
    private enum type {
        HELMET, CONTROLLER, LEGS, BOOTS
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {

        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {

        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        RendererScubaGearItem.type whichPart = null;
        if(item.getItem().equals(HCItems.itemDivingHelmet)){
            whichPart = RendererScubaGearItem.type.HELMET;
        }
        if(item.getItem().equals(HCItems.itemDivingController)){
            whichPart = RendererScubaGearItem.type.CONTROLLER;
        }
        if(item.getItem().equals(HCItems.itemDivingLegs)){
            whichPart = RendererScubaGearItem.type.LEGS;
        }
        if (item.getItem().equals(HCItems.itemDivingBoots)) {
            whichPart = RendererScubaGearItem.type.BOOTS;
        }

        switch (type) {
            case ENTITY:
                render(-0.5F, -0.55F, -0.5F, 1.0F, whichPart);
                break;
            case EQUIPPED:
                render(-0.2F, 0.4F, 0.3F, 0.8F, whichPart);
                break;
            case EQUIPPED_FIRST_PERSON:
                render(-1.0F, 1.0F, 0.4F, 0.7F, whichPart);
                break;
            case FIRST_PERSON_MAP:
                break;
            case INVENTORY:
                render(0.0F, -0.3F, 0.0F, 0.8F, whichPart);
                break;
            default:
                break;
        }
    }

    private void render(float x, float y, float z, float scale, type whichPart){
        GL11.glPushMatrix();

        GL11.glTranslatef(x + 0.5F, y + 1.0F, z + 0.5F);
        GL11.glScalef(scale, scale, scale);

        if(whichPart == type.HELMET){
            modelDivingSuit.bipedHead.isHidden = false;
            modelDivingSuit.bipedHead.render(0.0625F);
        }
        GL11.glPopMatrix();
    }
}
