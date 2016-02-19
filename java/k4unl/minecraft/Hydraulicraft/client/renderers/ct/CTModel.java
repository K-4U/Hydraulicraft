package k4unl.minecraft.Hydraulicraft.client.renderers.ct;

/**
 * @author Koen Beckers (K-4U)
 */
public class CTModel/* implements IBakedModel, ISmartBlockModel */ {
/*
    private IExtendedBlockState state;
    private Map<EnumFacing, TextureAtlasSprite> textures = new HashMap<>();

    public CTModel(IExtendedBlockState state) {
        this.state = state;
    }

    @Override
    public IBakedModel handleBlockState(IBlockState state) {
        return new CTModel((IExtendedBlockState)state);
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing face) {
        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        ArrayList<BakedQuad> quads = new ArrayList<BakedQuad>();
        for (EnumFacing face : EnumFacing.values()) {
            quads.add(createQuadFace(face, getTextureForFace(face)));
        }
        return quads;
    }

    private BakedQuad createQuadFace(EnumFacing face, TextureAtlasSprite texture){
        return new BakedQuad(createQuadVerticesData(0, 1, 1, 0, 1, face, texture), -1, face);
    }

    private int[] createQuadVerticesData(float x1, float x2, float y, float z1, float z2, EnumFacing face, TextureAtlasSprite texture) {
        Vector3f vertex1 = rotateFace(x1 - 0.5F, y - 0.5F, z1 - 0.5F, face);
        vertex1.add(0.5F, 0.5F, 0.5F);

        Vector3f vertex2 = rotateFace(x1 - 0.5F, y - 0.5F, z2 - 0.5F, face);
        vertex2.add(0.5F, 0.5F, 0.5F);

        Vector3f vertex3 = rotateFace(x2 - 0.5F, y - 0.5F, z2 - 0.5F, face);
        vertex3.add(0.5F, 0.5F, 0.5F);

        Vector3f vertex4 = rotateFace(x2 - 0.5F, y - 0.5F, z1 - 0.5F, face);
        vertex4.add(0.5F, 0.5F, 0.5F);

        int u1 = 0, v1 = 0, u2 = 16, v2 = 16;

        int vertexData [][]  = new int[4][7];

        switch(face){
            case EAST:
                vertexData[0] = createVertexData(vertex1, -1, texture, u2, v1);
                vertexData[1] = createVertexData(vertex2, -1, texture, u1, v1);
                vertexData[2] = createVertexData(vertex3, -1, texture, u1, v2);
                vertexData[3] = createVertexData(vertex4, -1, texture, u2, v2);
                break;
            case NORTH:
                vertexData[0] = createVertexData(vertex1, -1, texture, u2, v2);
                vertexData[1] = createVertexData(vertex2, -1, texture, u2, v1);
                vertexData[2] = createVertexData(vertex3, -1, texture, u1, v1);
                vertexData[3] = createVertexData(vertex4, -1, texture, u1, v2);
                break;
            case WEST:
                vertexData[0] = createVertexData(vertex1, -1, texture, u1, v2);
                vertexData[1] = createVertexData(vertex2, -1, texture, u2, v2);
                vertexData[2] = createVertexData(vertex3, -1, texture, u2, v1);
                vertexData[3] = createVertexData(vertex4, -1, texture, u1, v1);
                break;
            default:
                vertexData[0] = createVertexData(vertex1, -1, texture, u1, v1);
                vertexData[1] = createVertexData(vertex2, -1, texture, u1, v2);
                vertexData[2] = createVertexData(vertex3, -1, texture, u2, v2);
                vertexData[3] = createVertexData(vertex4, -1, texture, u2, v1);
        }

        return Ints.concat(vertexData[0], vertexData[1], vertexData[2], vertexData[3]);
    }

    private Vector3f rotateFace(float x, float y, float z , EnumFacing side) {

        Vector3f vector = new Vector3f();

        switch(side) {
            /*case DOWN:  return new Vec3( x, -y, -z);
            case UP:    return new Vec3( x,  y,  z);
            case NORTH: return new Vec3( x,  z, -y);
            case SOUTH: return new Vec3( x, -z,  y);
            case WEST:  return new Vec3(-y,  x,  z);
            case EAST:  return new Vec3( y, -x,  z);*/
/*
    case DOWN:
            vector.setX(x);
    vector.setY(-y);
    vector.setZ(-z);
    break;
    case UP:
            vector.setX(x);
    vector.setY(y);
    vector.setZ(z);
    break;
    case NORTH:
            vector.setX(x);
    vector.setY(z);
    vector.setZ(-y);
    break;
    case SOUTH:
            vector.setX(x);
    vector.setY(-z);
    vector.setZ(y);
    break;
    case WEST:
            vector.setX(-y);
    vector.setY(x);
    vector.setZ(z);
    break;
    case EAST:
            vector.setX(y);
    vector.setY(-x);
    vector.setZ(z);
    break;
}
return vector;
        }

private int[]createVertexData(Vector3f vertex,int color,TextureAtlasSprite texture,float u,float v){
        return new int[]{
        Float.floatToRawIntBits(vertex.getX()),
        Float.floatToRawIntBits(vertex.getY()),
        Float.floatToRawIntBits(vertex.getZ()),
        color,
        Float.floatToRawIntBits(texture.getInterpolatedU(u)),
        Float.floatToRawIntBits(texture.getInterpolatedV(v)),
        0
        };
        }


private TextureAtlasSprite getTextureForFace(EnumFacing face){

        World world=Minecraft.getMinecraft().theWorld;
        BlockConnectedTexture block=(BlockConnectedTexture)state.getBlock();
        int iconNum=block.getIconForFace(world,x,y,z,face);
        return
        }

@Override
public boolean isAmbientOcclusion(){
        return true;
        }

@Override
public boolean isGui3d(){
        return true;
        }

@Override
public boolean isBuiltInRenderer(){
        return false;
        }

@Override
public TextureAtlasSprite getParticleTexture(){
        return textures.get(EnumFacing.UP);
        }

@Override
public ItemCameraTransforms getItemCameraTransforms(){
        return ItemCameraTransforms.DEFAULT;
        }*/
}
