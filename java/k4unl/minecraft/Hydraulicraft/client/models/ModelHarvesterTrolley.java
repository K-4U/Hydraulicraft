package k4unl.minecraft.Hydraulicraft.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;


public class ModelHarvesterTrolley extends ModelBase
{
  //fields
    ModelRenderer Trolley;
    ModelRenderer Arm;
  
  public ModelHarvesterTrolley()
  {
    textureWidth = 64;
    textureHeight = 32;
    
      Trolley = new ModelRenderer(this, 0, 0);
      Trolley.addBox(-4F, -4F, -4F, 8, 8, 8);
      Trolley.setRotationPoint(0F, 16F, 0F);
      Trolley.setTextureSize(64, 32);
      Trolley.mirror = true;
      setRotation(Trolley, 0F, 0F, 0F);
      Arm = new ModelRenderer(this, 0, 16);
      Arm.addBox(-2F, -6F, -2F, 4, 8, 4);
      Arm.setRotationPoint(0F, 10F, 0F);
      Arm.setTextureSize(64, 32);
      Arm.mirror = true;
      setRotation(Arm, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Trolley.render(f5);
    Arm.render(f5);
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
