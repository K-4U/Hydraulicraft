package k4unl.minecraft.Hydraulicraft.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelHarvesterFrame extends ModelBase
{
  //fields
    ModelRenderer frameBR;
    ModelRenderer frameBL;
    ModelRenderer frameTL;
    ModelRenderer frameTR;
    ModelRenderer top;
    ModelRenderer front;
    ModelRenderer back;
  
  public ModelHarvesterFrame()
  {
    textureWidth = 64;
    textureHeight = 32;
    
      frameBR = new ModelRenderer(this, 0, 0);
      frameBR.addBox(-8F, -0.5F, -0.5F, 16, 1, 1);
      frameBR.setRotationPoint(0F, 20F, 4F);
      frameBR.setTextureSize(64, 32);
      frameBR.mirror = true;
      setRotation(frameBR, 0F, 0F, 0F);
      frameBL = new ModelRenderer(this, 0, 0);
      frameBL.addBox(-8F, -0.5F, -0.5F, 16, 1, 1);
      frameBL.setRotationPoint(0F, 20F, -4F);
      frameBL.setTextureSize(64, 32);
      frameBL.mirror = true;
      setRotation(frameBL, 0F, 0F, 0F);
      frameTL = new ModelRenderer(this, 0, 0);
      frameTL.addBox(-8F, -0.5F, -0.5F, 16, 1, 1);
      frameTL.setRotationPoint(0F, 12F, -4F);
      frameTL.setTextureSize(64, 32);
      frameTL.mirror = true;
      setRotation(frameTL, 0F, 0F, 0F);
      frameTR = new ModelRenderer(this, 0, 0);
      frameTR.addBox(-8F, -0.5F, -0.5F, 16, 1, 1);
      frameTR.setRotationPoint(0F, 12F, 4F);
      frameTR.setTextureSize(64, 32);
      frameTR.mirror = true;
      setRotation(frameTR, 0F, 0F, 0F);
      top = new ModelRenderer(this, 0, 2);
      top.addBox(-8F, 0F, -3.5F, 16, 0, 7);
      top.setRotationPoint(0F, 12F, 0F);
      top.setTextureSize(64, 32);
      top.mirror = true;
      setRotation(top, 0F, 0F, 0F);
      front = new ModelRenderer(this, 0, 2);
      front.addBox(-8F, 0F, -3.5F, 16, 0, 7);
      front.setRotationPoint(0F, 16F, -4F);
      front.setTextureSize(64, 32);
      front.mirror = true;
      setRotation(front, 1.570796F, 0F, 0F);
      back = new ModelRenderer(this, 0, 2);
      back.addBox(-8F, 0F, -3.5F, 16, 0, 7);
      back.setRotationPoint(0F, 16F, 4F);
      back.setTextureSize(64, 32);
      back.mirror = true;
      setRotation(back, 1.570796F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    frameBR.render(f5);
    frameBL.render(f5);
    frameTL.render(f5);
    frameTR.render(f5);
    top.render(f5);
    front.render(f5);
    back.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  @Override
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity){
      super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
  }

}
