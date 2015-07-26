package k4unl.minecraft.Hydraulicraft.client.renderers.items;

import cpw.mods.fml.client.FMLClientHandler;
import k4unl.minecraft.Hydraulicraft.client.models.ModelDivingSuit;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
                //GL11.glRotatef(90, 0, 0, 1);
                render(-0.5F, -0.55F, -0.5F, 1.0F, whichPart);
                break;
            case EQUIPPED:
                GL11.glRotated(180, 0, 1, 0);
                GL11.glTranslatef(-0.5F, 0.4F, -0.3F);
                render(-0.2F, 0.4F, 0.3F, 0.8F, whichPart);
                break;
            case EQUIPPED_FIRST_PERSON:
                GL11.glRotated(90, 0, 1, 0);
                GL11.glTranslatef(-0.6F, 0.6F, 0.0F);
                render(-1.0F, 1.0F, 0.4F, 0.7F, whichPart);
                break;
            case FIRST_PERSON_MAP:
                break;
            case INVENTORY:
                GL11.glRotatef(180, 0, 1, 0);
                render(0.0F, 0.4F, 0.0F, 0.8F, whichPart);
                break;
            default:
                break;
        }
    }

    private void render(float x, float y, float z, float scale, type whichPart){
        GL11.glPushMatrix();


        //GL11.glScalef(scale, scale, scale);
        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(new ResourceLocation(HCItems.itemDivingHelmet.getArmorTexture(null, null, 0, null)));

        modelDivingSuit.isChild = false;
        if(whichPart == type.HELMET){
            GL11.glScalef(1.8F, 1.8F, 1.8F);
            GL11.glTranslatef(0.0F, 0.25F, 0.0F);
            modelDivingSuit.bipedHead.isHidden = false;
            modelDivingSuit.bipedHead.render(0.0625F);
        }
        if(whichPart == type.CONTROLLER){
            GL11.glScalef(1.2F, 1.2F, 1.2F);
            GL11.glTranslatef(0.0F, -0.35F, 0.0F);
            modelDivingSuit.bipedLeftArm.isHidden = false;
            modelDivingSuit.bipedRightArm.isHidden = false;
            modelDivingSuit.bipedBody.isHidden = false;
            modelDivingSuit.bipedLeftArm.render(0.0625F);
            modelDivingSuit.bipedRightArm.render(0.0625F);
            modelDivingSuit.bipedBody.render(0.0625F);
        }
        if(whichPart == type.LEGS){
            GL11.glScalef(1.75F, 1.75F, 1.75F);
            GL11.glTranslatef(0.0F, -1.1F, 0.0F);
            modelDivingSuit.bipedLeftLeg.isHidden = false;
            modelDivingSuit.bipedRightLeg.isHidden = false;
            modelDivingSuit.flipperLeft.isHidden = true;
            modelDivingSuit.flipperRight.isHidden = true;
            modelDivingSuit.bipedLeftLeg.render(0.0625F);
            modelDivingSuit.bipedRightLeg.render(0.0625F);
        }
        if(whichPart == type.BOOTS){
            GL11.glTranslatef(-0.15F, -2.4F, -0.0F);
            GL11.glScalef(1.70F, 1.70F, 1.70F);
            modelDivingSuit.flipperLeft.isHidden = false;
            modelDivingSuit.flipperRight.isHidden = false;
            modelDivingSuit.flipperLeft.render(0.0625F);
            modelDivingSuit.flipperRight.render(0.0625F);
        }
        GL11.glPopMatrix();
    }
}
