package k4unl.minecraft.Hydraulicraft.client.renderers.pct;

/**
 * @author Koen Beckers (K-4U)
 */
public enum PCTModelLoader //implements ICustomModelLoader
{
    /*instance;

    private              Map<String, ProceduralConnectedTexture> textures       = Maps.newHashMap();
    private static final String                                  modelIndicator = "advancedConnectedTexture/";

    //This function determines whether or not this modelloader will handle a resource location.
    @Override
    public boolean accepts(ResourceLocation modelLocation)
    {
        final String resourceDomain = modelLocation.getResourceDomain();
        final String resourcePath = modelLocation.getResourcePath();
        final boolean isValidPath = resourcePath.startsWith(modelIndicator) ||
          resourcePath.startsWith("models/block/advancedConnectedTexture/") ||
          resourcePath.startsWith("models/item/advancedConnectedTexture/");
        if (!resourceDomain.equals(ModInfo.ID)) {
            return false;
        }
        return isValidPath;
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws IOException
    {
        String textureName = modelLocation.getResourcePath().substring(
          modelLocation.getResourcePath().indexOf(modelIndicator) + modelIndicator.length());

        return new PCTModel(textures.get(textureName));
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) { }

    @SubscribeEvent
    public void onPreTextureStitch(TextureStitchEvent.Pre event) {
        for (final ProceduralConnectedTexture proceduralConnectedTexture : textures.values())
        {
            proceduralConnectedTexture.registerSprites(event.map);
        }
    }

    public void registerTexture(String textureName, ProceduralConnectedTexture texture) {
        textures.put(textureName, texture);
    }

    public ProceduralConnectedTexture getTexture(String textureName) {
        return textures.get(textureName);
    }

    public static String describeTextureAt(World worldIn, BlockPos pos, EnumFacing side) {
        final IBlockState state = worldIn.getBlockState(pos);
        final BlockRendererDispatcher rendererDispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        final IBakedModel model = rendererDispatcher.getModelFromBlockState(state, worldIn, pos);
        if (model instanceof PCTModelInstance) {
            final ProceduralConnectedTexture proceduralConnectedTexture = ((PCTModelInstance) model).getProceduralConnectedTexture();
            return proceduralConnectedTexture.describeTextureAt(worldIn, pos, side);
        }
        return "Not a Procedural Connected Texture";
    }*/
}
