package k4unl.minecraft.Hydraulicraft.client.renderers.items;

public class RendererHarvesterTrolleyItem {//implements IItemRenderer {
/*
	private static RendererHarvesterTrolley t = new RendererHarvesterTrolley();
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
	    NBTTagCompound tag = item.getTagCompound();
	    IHarvesterTrolley trolley = tag != null ? Hydraulicraft.trolleyRegistrar.getTrolley(tag.getString("name")) : null;
	    if(trolley != null){
    	    ResourceLocation resLoc = trolley.getTexture();
    	    
    		switch(type){
    	        case ENTITY: {
    	        	//GL11.glRotatef(90, 0, 0, 1);
    	            render(-0.5F, -1.2F, -0.5F, 1.0F, 0, resLoc);
    	            return;
    	        }
    	        case EQUIPPED: {
    	            //GL11.glRotated(180, 0, 1, 0);
    	            //GL11.glTranslatef(0.5F, 0.0F, 0.0F);
    	            render(-0.2F, 0.0F, 0.3F, 1.0F, 4, resLoc);
    	            return;
    	        }
    	        case EQUIPPED_FIRST_PERSON: {
    	            //GL11.glRotated(180, 0, 1, 0);
    	            render(-1.0F, 0.6F, 0.4F, 0.7F, 4, resLoc);
    	            return;
    	        }
    	        case INVENTORY: {
    	            render(0.0F, -0.8F, 0.0F, 1.0F, 4, resLoc);
    	            return;
    	        }
    	        default:
    	            return;
    	    }
		}
	}
	
	private void render(float x, float y, float z, float scale, int rotation, ResourceLocation resLoc){
		
		GL11.glScalef(scale, scale, scale);
		t.doRender(null, x, y, z, 0, rotation, 0, resLoc);
	}
*/
}
