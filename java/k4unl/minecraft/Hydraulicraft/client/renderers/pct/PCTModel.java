package k4unl.minecraft.Hydraulicraft.client.renderers.pct;

/**
 * Created by codew on 9/11/2015.
 */
public class PCTModel //implements IModel, IRetexturableModel
{
    /*private final ProceduralConnectedTexture proceduralConnectedTexture;
    private final ImmutableMap<String, String> defaultTextures;

    public PCTModel(ProceduralConnectedTexture proceduralConnectedTexture)
    {
        this(proceduralConnectedTexture, null);
    }

    //Used for inventory and other non-world textures.
    public PCTModel(ProceduralConnectedTexture proceduralConnectedTexture, ImmutableMap<String, String> textures)
    {
        defaultTextures = textures;
        this.proceduralConnectedTexture = proceduralConnectedTexture;
    }

    @Override
    public Collection<ResourceLocation> getDependencies()
    {
        return ImmutableList.of(new ResourceLocation("block/cube"));
    }

    @Override
    public Collection<ResourceLocation> getTextures()
    {
        return Lists.newArrayList();
    }

    @Override
    public IFlexibleBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {
        return new PCTModelInstance(state, format, bakedTextureGetter, proceduralConnectedTexture);
    }

    @Override
    public IModelState getDefaultState()
    {
        return TRSRTransformation.identity();
    }

    @Override
    public IModel retexture(ImmutableMap<String, String> textures)
    {
        return new PCTModel(proceduralConnectedTexture, textures);
    }*/
}
