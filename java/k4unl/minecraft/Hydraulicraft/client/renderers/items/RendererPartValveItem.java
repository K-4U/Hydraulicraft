package k4unl.minecraft.Hydraulicraft.client.renderers.items;

public class RendererPartValveItem {//implements IItemRenderer {
/*
    private static RendererPartValve f = new RendererPartValve();
	
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
		int metadata = item.getItemDamage();
		switch(type){
	        case ENTITY: {
	        	//GL11.glRotatef(90, 0, 0, 1);
	            render(-0.5F, 0.0F, -0.5F, 1.0F, metadata);
	            return;
	        }
	        case EQUIPPED: {
	            //GL11.glRotated(180, 0, 1, 0);
	            //GL11.glTranslatef(0.5F, 0.0F, 0.0F);
	            render(-0.2F, 0.4F, 0.3F, 0.8F, metadata);
	            return;
	        }
	        case EQUIPPED_FIRST_PERSON: {
	            //GL11.glRotated(180, 0, 1, 0);
	            render(-1.0F, 1F, 0.4F, 0.7F, metadata);
	            return;
	        }
	        case INVENTORY: {
	            render(0.0F, -0.1F, 0.0F, 1.0F, metadata);
	            return;
	        }
	        default:
	            return;
	    }
	}
	
	private void render(float x, float y, float z, float scale, int metadata){
		
		GL11.glScalef(scale, scale, scale);/*
		Map<EnumFacing, TileEntity> connectedSides = new HashMap<EnumFacing, TileEntity>();
		for(EnumFacing dir : EnumFacing.VALID_DIRECTIONS){
			connectedSides.put(dir, null);
		}*/
//		f.doRender(x, y, z, 0, metadata, null);
//	}

}
