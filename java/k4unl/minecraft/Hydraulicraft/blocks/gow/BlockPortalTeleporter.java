package k4unl.minecraft.Hydraulicraft.blocks.gow;

import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.network.NetworkHandler;
import k4unl.minecraft.Hydraulicraft.network.packets.PacketSpawnParticle;
import k4unl.minecraft.Hydraulicraft.tileEntities.gow.TilePortalTeleporter;
import k4unl.minecraft.k4lib.lib.Location;
import k4unl.minecraft.k4lib.lib.TeleportHelper;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockPortalTeleporter extends GOWBlockRendering {
    private static Vector3fMax blockBounds = new Vector3fMax(0.499F, 0.499F, 0.499F, 0.501F, 0.501F, 0.501F);


    public BlockPortalTeleporter() {

        super(Names.portalTeleporter.unlocalized);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean canRenderInLayer(EnumWorldBlockLayer layer) {
        return true;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {

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
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn) {
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
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
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
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
        return null;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {

        return new TilePortalTeleporter();
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullBlock() {
        return false;
    }


}
