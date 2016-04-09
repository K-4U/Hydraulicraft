package k4unl.minecraft.Hydraulicraft.blocks.gow;

import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.network.NetworkHandler;
import k4unl.minecraft.Hydraulicraft.network.packets.PacketSpawnParticle;
import k4unl.minecraft.Hydraulicraft.tileEntities.gow.TilePortalTeleporter;
import k4unl.minecraft.k4lib.lib.Location;
import k4unl.minecraft.k4lib.lib.TeleportHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockPortalTeleporter extends GOWBlockRendering {

    private AxisAlignedBB boundingBox;


    public BlockPortalTeleporter() {
        super(Names.portalTeleporter.unlocalized);
        boundingBox = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return super.getBoundingBox(state, source, pos);
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {

        return true;
    }

    @Override
    public boolean isCollidable() {

        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return boundingBox;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return boundingBox;
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {

        if (!worldIn.isRemote) {
            NBTTagCompound entCompound = entityIn.getEntityData();
            Location teLocation = new Location(pos);
            TilePortalTeleporter teleporter = (TilePortalTeleporter) teLocation.getTE(worldIn);
            if (teleporter == null) {
                return;
            }
            if (teleporter.getPortalBase() == null) {
                return;
            }
            Location teleportLocation = teleporter.getPortalBase().getTarget();
            long lastInPortal = entCompound.getLong("lastInPortal" + teleporter.getPortalBase().getIPLong());
            if (worldIn.getTotalWorldTime() - lastInPortal > (HCConfig.INSTANCE.getInt("portalTimeoutInSeconds") * 20)) {
                if (teleportLocation != null) {
                    teleporter.usePressure();
                    TeleportHelper.teleportEntity(entityIn, teleportLocation);
                    Random rnd = new Random(System.currentTimeMillis() / 1000);
                    double dx;
                    double dy;
                    double dz;
                    for (int i = 0; i <= 5; i++) {
                        dx = (rnd.nextFloat() - 0.6D) * 0.1D;
                        dy = (rnd.nextFloat() - 0.6D) * 0.1D;
                        dz = (rnd.nextFloat() - 0.6D) * 0.1D;

                        //world.spawnParticle("cloud", x, y, z, d3, d4, d5);
                        NetworkHandler.INSTANCE.sendToAllAround(new PacketSpawnParticle(EnumParticleTypes.CLOUD, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, dx, dy, dz), worldIn);
                    }

                    entCompound.setLong("lastInPortal" + teleporter.getPortalBase().getIPLong(), worldIn.getTotalWorldTime());
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState state, World worldIn, BlockPos pos, Random rand) {
        /*for (int l = 0; l < 1; ++l)
        {*/
        if (rand.nextInt(100) <= 50) {
            double d0 = pos.getX() + rand.nextFloat();
            double d1 = pos.getY() + rand.nextFloat();
            double d2 = pos.getZ() + rand.nextFloat();
            double d3;
            double d4;
            double d5;
            d3 = (rand.nextFloat() - 0.6D) * 0.1D;
            d4 = (rand.nextFloat() - 0.6D) * 0.1D;
            d5 = (rand.nextFloat() - 0.6D) * 0.1D;

            worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }
        //}
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return null;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {

        return new TilePortalTeleporter();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }
}
