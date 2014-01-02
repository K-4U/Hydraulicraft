package k4unl.minecraft.Hydraulicraft.client.models;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;


public class ModelHydraulicHose_new extends ModelBase
{
  //fields
    ModelRenderer Hose1Center;
    ModelRenderer Hose2Center;
    ModelRenderer Hose1Right;
    ModelRenderer Hose1Left;
  
  public ModelHydraulicHose_new()
  {
    textureWidth = 64;
    textureHeight = 32;
    
    Hose1Center = new ModelRenderer(this, 0, 0);
    Hose1Center.addBox(-1F, -1F, -1F, 2, 2, 2);
    Hose1Center.setRotationPoint(1.5F, 17.5F, 1.5F);
    Hose1Center.setTextureSize(64, 32);
    Hose1Center.mirror = true;
    setRotation(Hose1Center, 0F, 0F, 0F);
    Hose2Center = new ModelRenderer(this, 0, 0);
    Hose2Center.addBox(-1F, -1F, -1F, 2, 2, 2);
    Hose2Center.setRotationPoint(-1.5F, 14.5F, -1.5F);
    Hose2Center.setTextureSize(64, 32);
    Hose2Center.mirror = true;
    setRotation(Hose2Center, 0F, 0F, 0F);
    Hose1Right = new ModelRenderer(this, 0, 4);
    Hose1Right.addBox(0F, -1F, -1F, 6, 2, 2);
    Hose1Right.setRotationPoint(2F, 17.5F, 1.5F);
    Hose1Right.setTextureSize(64, 32);
    Hose1Right.mirror = true;
    setRotation(Hose1Right, 0F, 0F, 0F);
    Hose1Left = new ModelRenderer(this, 0, 8);
    Hose1Left.addBox(0F, -1F, -1F, 10, 2, 2);
    Hose1Left.setRotationPoint(2F, 17.5F, 1.5F);
    Hose1Left.setTextureSize(64, 32);
    Hose1Left.mirror = true;
    setRotation(Hose1Left, 0F, 0F, -3.141593F);

  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Hose1Center.render(f5);
    Hose2Center.render(f5);
    Hose1Right.render(f5);
    Hose1Left.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z){
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
  }

  @Override
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity){
      super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
  }

  /*public void setConnectedSides(Map<ForgeDirection, TileEntity> map){
      connectedSides = map;
  }*/

}
